package config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author jwolf
 * @Date 2021-10-29 22:24
 */

@Getter
@Setter
@RefreshScope
public class CommonAutoRefreshConfig {

    @Value("${retDataPrintLen:100}")
    private int retDataPrintLen; //允许打印的返回数据长度
    @Value("${reqParamsPrintLen:200}")
    private int reqParamsPrintLen; //允许打印的请求参数长度
    @Value("${monitor.cost:2000}")
    private Long cost; // 耗时阈值
    @Value("${logBatchStoreSize:20}")
    private int logBatchStoreSize; // 耗时阈值


}
