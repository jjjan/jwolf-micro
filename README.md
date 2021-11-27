<span style="color:red;font:bold 50">jwolf-micro v1.0.0</span>
# 一、项目简介
jwolf-micro是基于最新主流技术栈实现的一套全栈微服务开源学习型项目。

# 二、系统架构
![系统架构图](./doc/doc-resource/系统架构图.png)
## 1.后端技术架构
```
1.后端核心springcloud alibaba2021,springcloud2020,springboot2.5.4
2.注册中心,配置中心nacos2.0.3
3.数据库mysql8,mybatis-plus
4.缓存redis
5.搜索引擎Elasticsearch7.10.x
6.消息队列rocketMq/kafka
7.网关nginx,springcloud-gateway
8.微服务调用openfegin与熔断限流sentinel1.8
9.分布式事务seata1.4.x
10.监控报警Prometheus/grafana
11.其它mongodb/minio/oauth2/spring-security等
```

## 2.大前端
```
1.后台管理Vue-element-admin
2.H5/移动端/小程序uni-app（规划）
```
## 3.AI
```
1.推荐算法（规划）
2.人脸识别（规划）
```

# 三、部署架构
```
1.fat-jar+shell 
2.docker+jenkins
3.k8s（2022实现）
```
![部署架构图](./doc/doc-resource/jenkis-docker-cicd.png)

# 四、项目目录说明
```
├── doc
├── pom.xml
├── jwolf-common
    ├── src
    │   └── main
    │       ├── java
    │       │   └── com
    │       │       └── jwolf
    │       │           └── common
    │       │               ├── config
    │       │               ├── base
    │       │               ├── constant
    │       │               ├── core
    │       │               ├── exception
    │       │               └── util
    │       └── resources
├── jwolf-auth
├── jwolf-manage
├── jwolf-service
    ├── jwolf-service-xx1
    ├── jwolf-service-xx2
├── jwolf-service-api
    ├── jwolf-service-xx1-api
    ├── jwolf-service-xx2-api
├── jwolf-manage-ui
└── script

```


# 五、更新日志
- 2021/11/27 README.md初始化
# 六、quickstart

# 七、其它说明


