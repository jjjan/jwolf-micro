# 前言
> 自己编写k8s部署文件比较麻烦,成熟的开源软件的k8s部署文件如redis,mysql并不需要自己从头编写,只需要通过类似yum的部署工具heml将heml官方或其它
第三方或私有heml仓库拉下来稍作自定义修改就可以在k8s上跑起来。但自己的业务系统还是得自己编写k8s部署文件或helm chart进行部署。
# 1.helm安装

```bash
1. 下载解压
wget https://get.helm.sh/helm-v3.7.2-linux-amd64.tar.gz 或github 项目releases下载并解压
mv helm /usr/local/bin/
2. 类似yum方式使用
 helm repo add bitnami https://charts.bitnami.com/bitnami
 helm repo add apphub https://apphub.aliyuncs.com
 helm repo add stable http://mirror.azure.cn/kubernetes/charts/    
 helm repo add aliyun  https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts
 helm repo list
3.拉取chart并适当修改,官方搜索helm search hub redis,第三方仓库搜索heml search repo redis，helm提供了一个类似dockerhub镜像网站的helm chart网站
建议在网站搜索相关chart进行安装——有足够的chart文档说明。如果不需要自定义参数或个别自定义参数可以直接$ helm install my-release --set master.persistence.existingClaim=PVC_NAME bitnami/redis，一般还是需要自定义，
就先拉下来作一些修改，helm pull bitnami/redis 解压后修改里面的valuse.yaml，改好后执行安装heml install my-release ./my-releasedir -n xxx



```
4.demo redis安装， kubectl get all -A -o wide 看到pod一直pending，kubectl logs -f --tail 222  pod/redis-master-0 -n jwolf无日志输出？？？
- helm install redis bitnami/redis --set auth.password=redis  --set replica.replicaCount=0 -n jwolf
-

```bash
 
** Please be patient while the chart is being deployed **

Redis&trade; can be accessed on the following DNS names from within your cluster:

    redis-master.default.svc.cluster.local for read/write operations (port 6379)
    redis-replicas.default.svc.cluster.local for read-only operations (port 6379)



To get your password run:

    export REDIS_PASSWORD=$(kubectl get secret --namespace default redis -o jsonpath="{.data.redis-
password}" | base64 --decode)

To connect to your Redis&trade; server:

1. Run a Redis&trade; pod that you can use as a client:

   kubectl run --namespace default redis-client --restart='Never'  --env REDISCLI_AUTH=
$REDIS_PASSWORD  --image docker.io/bitnami/redis:6.2.6-debian-10-r53 --command -- sleep infinity

   Use the following command to attach to the pod:

   kubectl exec --tty -i redis-client \
   --namespace default -- bash

2. Connect using the Redis&trade; CLI:
   REDISCLI_AUTH="$REDIS_PASSWORD" redis-cli -h redis-master
   REDISCLI_AUTH="$REDIS_PASSWORD" redis-cli -h redis-replicas

To connect to your database from outside the cluster execute the following commands:

    kubectl port-forward --namespace default svc/redis-master 6379:6379 &
    REDISCLI_AUTH="$REDIS_PASSWORD" redis-cli -h 127.0.0.1 -p 6379

```