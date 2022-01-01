# 前言
> 自己编写k8s部署文件比较麻烦,成熟的开源软件的k8s部署文件如redis,mysql并不需要自己从头编写,只需要通过类似yum的部署工具heml将heml官方或其它
第三方或私有heml仓库拉下来稍作自定义修改就可以在k8s上跑起来。但自己的业务系统还是得自己编写k8s部署文件或helm chart进行部署。
# 自己写部署文件redis-deployment-service.yml
> 说明：这里是更具docker-compose文件自己翻译过来的，这里用的Deployment,存储卷用的hostpath不好合适，最好的方案的使用statefulSet+nfs/glusterfs等持久卷（后面的heml安装即用该方式），
由于使用hostpath挂载redis的data,固使用了node打label方式让redis pod只调度到指定的node
- 1.创建命名空间，不建议用默认的default：kubectl create namespace jwolf
- 2.先创建configmap用于存放redis配置: kubectl create configmap redis-configmap --from-file=/etc/redis/redis.conf -n jwolf
- 3.创建：kubectl apply -f redis-deployment-service.yml ，按文件删除资源：kubectl delete -f redis-deployment-service.yml
- 4.查看各资源状态：kubectl get all -A -o wide

```yaml

kind: Service
apiVersion: v1
metadata:
  labels:
    name: redis
  name: redis
  namespace: jwolf    #使用自定义namespace
spec:
  type: NodePort 
  ports:
    - name: redis
      port: 6379
      targetPort: 6379
      nodePort: 6379
  selector:
    name: redis
---
apiVersion: apps/v1  
kind: Deployment
metadata:
  name: redis
  namespace: jwolf    #使用自定义namespace
spec:
  replicas: 1 
  selector:
    matchLabels:
      name: redis
  template:
    metadata:
      labels:
         name: redis
    spec:
      nodeSelector:
        mynodelable: node1
      containers:
      - name: redis-container
        image: redis:6.2.6
        imagePullPolicy: IfNotPresent
        volumeMounts:
        - name: myconfigmap     #与volumes的name对应
          mountPath: "/usr/local/etc"   #与vomums的key为redis.conf的path拼接得到容器内redis配置文件绝对路径
        - name: redis-data             #容器内挂载点名称
          mountPath: /data    #容器内data保存路径，该路径在redis.conf配置了
        command:
          - "redis-server"   #启动命令
        args:
          - "/usr/local/etc/redis/redis.conf"  #带配置文件启动
        ports:
             - containerPort: 6379
               name: redis
               protocol: TCP
      volumes:
      - name: myconfigmap
        configMap:
          name: redis-configmap  #指定使用的configmap
          items:
            - key: redis.conf  #该值为configmap创建--from-file的文件名
              path: redis/redis.conf
      - name: redis-data          #数据卷名称，需要与容器内挂载点名称一致
        hostPath:
           path: /home/data/redis-data   #将容器挂载点名称相同的path挂载到宿主机path（但这种方式有局限性，pod分配到其它node数据就丢失了，需要挂载到NFS文件系统或限定pod调度）
      


```
# 使用k8s pv,pvc优化文件挂载,这里使用nfs,先安装先nsf
```bash
1.随便搞1个/多个node安装nfs,这里就选个master(192.168.1.18)就够了
yum -y install nfs-utils
2.建两个文件夹作为挂载点（pv2后面helm安装redis1主1从时用）
mkdir /home/data/k8s-data/redis/datadir-pv-1 -p
mkdir /home/data/k8s-data/redis/datadir-pv-2 -p 
3.配置nfs
echo "/home/data/k8s-data/redis/datadir-pv-1   192.168.1.0/24(rw,no_root_squash)"  >> /etc/exports
echo "/home/data/k8s-data/redis/datadir-pv-1   192.168.1.0/24(rw,no_root_squash)"  >> /etc/exports
4.启动并验证
systemctl enable nfs
systemctl start nfs
systemctl status nfs
showmount -e 192.168.1.18

```

# 先kubectl apply -f redis-pv-pvc.yaml，待与pod绑定

```yaml

apiVersion: v1
kind: PersistentVolume
metadata:
  namespace: jwolf
  name: redis-datadir-pv-1
spec:
  capacity:
    storage: 4Gi
  accessModes:
    - ReadWriteMany
  nfs:
    server: 192.168.1.18
    path: /home/data/k8s-data/redis/datadir-pv-1
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: redis-datadir-pvc-1
  namespace: jwolf
spec:
  accessModes:
    - ReadWriteMany
  volumeName: redis-datadir-pv-1
  resources:
    requests:
      storage: 2Gi
---
apiVersion: v1
kind: PersistentVolume
metadata:
  namespace: jwolf
  name: redis-datadir-pv-2
spec:
  capacity:
    storage: 4Gi
  accessModes:
    - ReadWriteMany
  nfs:
    server: 192.168.1.18
    path: /home/data/k8s-data/redis/datadir-pv-2
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: redis-datadir-pvc-2
  namespace: jwolf
spec:
  accessModes:
    - ReadWriteMany
  volumeName: redis-datadir-pv-2
  resources:
    requests:
      storage: 2Gi

```
```bash
[root@master k8s]# kubectl get pv,pvc -A -o wide
NAME                                  CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM                       STORAGECLASS   REASON   AGE   VOLUMEMODE
persistentvolume/redis-datadir-pv-1   4Gi        RWX            Retain           Bound    jwolf/redis-datadir-pvc-1                           3s    Filesystem
persistentvolume/redis-datadir-pv-2   4Gi        RWX            Retain           Bound    jwolf/redis-datadir-pvc-2                           3s    Filesystem

NAMESPACE   NAME                                        STATUS   VOLUME               CAPACITY   ACCESS MODES   STORAGECLASS   AGE   VOLUMEMODE
jwolf       persistentvolumeclaim/redis-datadir-pvc-1   Bound    redis-datadir-pv-1   4Gi        RWX                           3s    Filesystem
jwolf       persistentvolumeclaim/redis-datadir-pvc-2   Bound    redis-datadir-pv-2   4Gi        RWX                           2s    Filesystem


```
#修改redis-deployment-service.yml 的hostPath为pvc(persistentVolumeClaim持久卷申领)，再apply一下即可
```yaml
      - name: redis-data          #数据卷名称，需要与容器内挂载点名称一致
        persistentVolumeClaim:
          claimName: redis-datadir-pvc-1
       # hostPath:
       #    path: /home/data/redis-data   #将容器挂载点名称相同的path挂载到宿主机path（但这种方式有局限性，pod分配到其它node数据就丢失了，需要挂载到NFS文件系统或限定pod调度）

```
# 清理战场kubectl delete -f redis-deployment-service.yml，改用helm来安装一个1主1从的redis集群

```bash
1. 下载解压
wget https://get.helm.sh/helm-v3.7.2-linux-amd64.tar.gz 或github 项目releases下载并解压
mv helm /usr/local/bin/
2. 类似yum方式使用,添加一些第三方源
 helm repo add bitnami https://charts.bitnami.com/bitnami
 helm repo add apphub https://apphub.aliyuncs.com
 helm repo add stable http://mirror.azure.cn/kubernetes/charts/    
 helm repo add aliyun  https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts
 helm repo list
3.拉取chart并适当修改,官方搜索helm search hub redis,第三方仓库搜索heml search repo redis，helm提供了一个类似dockerhub镜像网站的helm chart网站https://artifacthub.io
建议在网站搜索相关chart进行安装——有足够的chart文档说明。如果不需要自定义参数或个别自定义参数可以直接helm install my-release --set master.persistence.existingClaim=PVC_NAME bitnami/redis，一般还是需要自定义，
就先拉下来作一些修改，helm pull bitnami/redis 解压后修改里面的valuse.yaml，改好后执行安装heml install my-release ./my-releasedir -n xxx ,helm chart其实就是别人基于一种模板编写的各种部署并有各种配置参数可以通过修改chart解压包里的
values.yaml进行自定义，不修改也可直接helm install XXchart快速基于k8s部署一个redis，类似docker run
4.不改配置直接启动helm  helm install  redis ./redis(chart解压目录)   --set replica.replicaCount=1  -n jwolf，其实并不能启动，容器会一直pendding,
kubectl logs -f --tail 222 pod/redis-master-0 -n jwolf 并无日志说明容器还没确定就异常了，需要kubectl describe  pod/redis-master-0 -n jwolf 看到
报错信息【Warning  FailedScheduling  <unknown>  default-scheduler  running "VolumeBinding" filter plugin for pod "redis-master-0": pod has unbound immediate PersistentVolumeClaims】，bitnami做的这个chart并无默认的
数据卷配置，需要自己bond pvc,修改values.yaml即可或直接commandline指定配置,先卸了之前的helm uninstall redis -n jwolf，再安装 helm install   redis ./redis  
 --set replica.replicaCount=1,master.persistence.existingClaim=redis-datadir-pvc-1,replica.persistence.enabled=false -n jwolf ,因副本绑定pvc与master还不同，暂未找到绑定方式
 固副本暂时禁用pvc,使用的是临时卷emptyDir

5.最终启动命令
helm install   redis ./redis   --set replica.replicaCount=1,master.persistence.existingClaim=redis-datadir-pvc-1,replica.persistence.enabled=false,master.service.nodePort=6379,master.service.type=NodePort,auth.password='jwolf' -n jwolf
pod状态为CrashLoopBackOff，kubectl logs -f pod/redis-master-0 -n jwolf报错【 Can't open the append-only file: Permission denied】，挂在点加个写权限即可chmod 664 /home/data/k8s-data/redis/datadir-pv-1/* -R
```
# 验证
##进入容器验证
kubectl exec -it pod/redis-master-0 bash -n jwolf ,容器内验证：curl redis-master-0(或service ClusterIP或pd IP):6379

 ** Please be patient while the chart is being deployed **

Redis&trade; can be accessed on the following DNS names from within your cluster:

    redis-master.jwolf.svc.cluster.local for read/write operations (port 6379)
    redis-replicas.jwolf.svc.cluster.local for read-only operations (port 6379)


##用宿主机的redis-cli直接连接，需要确定redis pod被调度的ip并且宿主机要有redis-cli可执行二进制文件
redis-cli -h 192.168.1.20 -p 6379 -a jwolf

##使用helm输入提示进行验证,实测连接不上,容器间通信有问题？需要安装coreDNS?replica连接不上master也是因为这个原因？网上有些童鞋用的stable/redis 这个chart，可以尝试一下

To get your password run:

    export REDIS_PASSWORD=$(kubectl get secret --namespace jwolf redis -o jsonpath="{.data.redis-password}" | base64 --decode)

To connect to your Redis&trade; server:

1. Run a Redis&trade; pod that you can use as a client:

   kubectl run --namespace jwolf redis-client --restart='Never'  --env REDISCLI_AUTH=$REDIS_PASSWORD  --image docker.io/bitnami/redis:6.2.6-debian-10-r53 --command -- sleep infinity

   Use the following command to attach to the pod:

   kubectl exec --tty -i redis-client \
   --namespace jwolf -- bash

2. Connect using the Redis&trade; CLI:
   REDISCLI_AUTH="$REDIS_PASSWORD" redis-cli -h redis-master
   REDISCLI_AUTH="$REDIS_PASSWORD" redis-cli -h redis-replicas

To connect to your database from outside the cluster execute the following commands:

    export NODE_IP=$(kubectl get nodes --namespace jwolf -o jsonpath="{.items[0].status.addresses[0].address}")
    export NODE_PORT=$(kubectl get --namespace jwolf -o jsonpath="{.spec.ports[0].nodePort}" services redis-master)
    REDISCLI_AUTH="$REDIS_PASSWORD" redis-cli -h $NODE_IP -p $NODE_PORT