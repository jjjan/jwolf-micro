# 一、大数据常见业务流程
## 1.常见数据来源:flume采集，kafka direct等
## 2.常见sink:hbase（快）,其它如hdfs,hive（可整合python脚本做ETL,老项目用得多）,也可以local-file,采集到kafka一般是做实时计算
## 3.spark-core,spark-sql实现统计聚合,数据挖掘等;spark-stream/flink用于实时处理kafka数据;结果存入mysql/redis/ES
## 4.websocket推送或http接口传到前端，利用echart,highchart展示
# 二、一些架构图
![重点](../doc/doc-resource/bigdata1.png)

![](../doc/doc-resource/bigdata2.png)

![](../doc/doc-resource/bigdata3.png)

![](../doc/doc-resource/bigdata4.png)

# 三、业务需求
## 1.离线需求
> 通过管理页面配置spark任务，存入mysql后通过runtime启动spark任务（结合任务参数）,结果存入mysql
## 2.实时需求
> 略


# 四、参考
 [1.大数据架构与组件优缺点](https://blog.csdn.net/leveretz/article/details/113846380)
 [2.实时数据处理](https://blog.csdn.net/qq_24084925/article/details/80842534)