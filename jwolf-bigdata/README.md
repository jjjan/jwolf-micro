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
 
# 五‘组件
## hbase2.1.3 
###1.docker [安装与测试](https://hub.docker.com/r/harisekhon/hbase)
>  docker run --name hbase --net=host  harisekhon/hbase,进入容器执行命令（hbase shell）进入交互界面
 ```bash
HBase Shell
Use "help" to get list of supported commands.
Use "exit" to quit this interactive shell.
For Reference, please visit: http://hbase.apache.org/2.0/book.html#shell
Version 2.1.3, rda5ec9e4c06c537213883cca8f3cc9a7c19daf67, Mon Feb 11 15:45:33 CST 2019
Took 0.0061 seconds                                                                                                                                                       
hbase(main):001:0> create 'person','info'
Created table person
Took 2.0373 seconds                                                                                                                                                       
=> Hbase::Table - person
hbase(main):002:0> list
TABLE                                                                                                                                                                     
person                                                                                                                                                                    
1 row(s)
Took 0.0622 seconds                                                                                                                                                       
=> ["person"]

```
###2.java连接操作时有网络问题待解决,详见HbaseTest.java