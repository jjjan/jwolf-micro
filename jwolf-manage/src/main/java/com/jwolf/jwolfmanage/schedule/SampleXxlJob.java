package com.jwolf.jwolfmanage.schedule;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SampleXxlJob {
       //简单任务示例（Bean模式）
       @XxlJob("jwolfDemo")
       public void jwolfDemo(){
           String params = XxlJobHelper.getJobParam();
           int shardTotal = XxlJobHelper.getShardTotal();
           int shardIndex = XxlJobHelper.getShardIndex();
           log.info("调度中心传来的任务参数:{},shardTotal:{},shardIndex:{}",params,shardTotal,shardIndex);
           //这样日志会发送到调度中心
           XxlJobHelper.log("XXL-JOB, Hello World.");
           //int a=1/0;//如果往上抛了异常，则调度成功，执行失败，显示执行失败原因,也可通过XxlJobHelper设置
       }

}