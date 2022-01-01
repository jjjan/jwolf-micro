### 1.kompose-windows-amd64.exe -f docker-compose-xx.yml convert 转换的k8s部署文件可能无法直接使用，
如mysql的目录挂载需要修改为映射主机目录

### 2.解除NodePort类型service端口必须大于30000限制
```bash
[root@master k8s]# vim /etc/kubernetes/manifests/
etcd.yaml                     kube-apiserver.yaml           kube-controller-manager.yaml  kube-scheduler.yaml           
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    component: kube-apiserver
    tier: control-plane
  name: kube-apiserver
  namespace: kube-system
spec:
  containers:
  - command:
    - kube-apiserver
    - --service-node-port-range=1-65535  #新增
    - --advertise-address=192.168.1.18
    - --allow-privileged=true
    ...

```