package com.jwolf.service.user.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.jwolf.common.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SentinelHandler {
    /**
     * 熔断降级处理方法参数可以【最后多一个 BlockException，其余入参出参类型与原函数一致】，方法public static.
     * @param id
     * @param e
     * @return
     */
    public static ResultEntity exceptionHandler(Long id, BlockException e) {
        log.error("调用Sentinel exceptionHandler方法->{}",e.getMessage());
        return ResultEntity.fail("9999","降级处理");
    }

    /**
     * 流控规则检验在前，如果触发流控规则，则不会进入@SentinelResource方法，也就不会进入fallbackHandler
     * @param id
     * @param e
     * @return
     */
    public static ResultEntity  fallbackHandler( Long id,Throwable e ) {
        log.error("调用Sentinel fallbackHandler方法->{}",e.getMessage() );
        return  ResultEntity.fail("9999","异常处理");
    }

}