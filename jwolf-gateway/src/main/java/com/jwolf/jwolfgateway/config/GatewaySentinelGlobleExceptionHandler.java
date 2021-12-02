package com.jwolf.jwolfgateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.jwolf.common.bean.ResultEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 相当于自定义全局异常处理器。各下游服务器就不用设置全局异常处理了，有网关统一处理就够了，避免sentinel感知不到异常无法触发相关规则
 * 如果不处理，则会抛出流控降级异常到前端
 */

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewaySentinelGlobleExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        if (!BlockException.isBlockException(ex)) {
            return writeResponse(ResultEntity.fail("服务异常,详情:" + ex.getMessage()), exchange);
        }
        return GatewayCallbackManager.getBlockHandler()
                .handleRequest(exchange, ex)
                .flatMap(response -> {
                    //可设置响应头，httpStatus等
                    ServerHttpResponse serverHttpResponse = exchange.getResponse();
                    serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                    return writeResponse(ResultEntity.fail("500", "xxxxxx"), exchange);

                });
    }

    /**
     * 数据写回前端
     *
     * @param resultEntity
     * @param exchange
     * @return
     */
    private Mono<Void> writeResponse(ResultEntity resultEntity, ServerWebExchange exchange) {
        byte[] data = JSON.toJSONString(resultEntity).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(data);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    ;

}


