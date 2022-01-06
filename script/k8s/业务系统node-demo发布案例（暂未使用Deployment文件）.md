# 编写node.js应用程序
将此代码保存在以文件名命名hellonode 的文件夹中server.js
var http = require('http');
var handleRequest = function(request, response) {
      console.log('Received request for URL: ' + request.url);
      response.writeHead(200);
      response.end('Hello World!');

};
var www = http.createServer(handleRequest);
www.listen(8080);
这可以先试用node server.js来运行一下，应该能够在http：// localhost：8080 /上看到“Hello World！”消息。按Ctrl-C停止正在运行的Node.js服务器。

# 将应用程序打包到docker容器中
这里需要使用Dockerfile文件来创建容器，在server.js文件夹内创建Dockerfile文件，如下：
FROM node:6.9.2
EXPOSE 8080
COPY server.js .
CMD node server.js
Docker镜像的这个配置从Docker注册表中的官方Node.js LTS镜像开始，公开端口8080，将server.js文件复制到镜像并启动Node.js服务器。

#使用build指令根据Dockerfile文件创建镜像
使用Docker守护进程构建Docker镜像（注意尾随点）：
docker build -t hello-node:v1 .

#创建部署
Kubernetes Pod是一个由一个或多个容器组成的组合，用于管理和联网。本教程中的Pod只有一个Container。Kubernetes 部署会检查Pod的运行状况，并在Pod终止时重新启动Pod的容器。部署是管理Pod的创建和扩展的推荐方法。使用该kubectl run命令创建管理Pod的Deployment。Pod根据hello-node:v1Docker镜像运行Container 。将 --image-pull-policy标志设置Never为始终使用本地映像，而不是从Docker注册表中提取它（因为您还没有将它推到那里）：
```bash
kubectl run hello-node --image=hello-node:v1 --port=8080 --image-pull-policy=Never
```
#查看部署：
kubectl get deployments
#查看Pod：
kubectl get pods

#创建服务
默认情况下，Pod只能通过Kubernetes集群中的内部IP地址访问。要从hello-nodeKubernetes虚拟网络外部访问Container，须将Pod公开为Kubernetes 服务。可以使用以下kubectl expose命令将Pod公开到公共Internet ：
kubectl expose deployment hello-node --type=LoadBalancer
#查看刚刚创建的服务：
kubectl get services
--type=LoadBalancer标志表示在群集外部公开的服务。

#访问应用
使用本地IP地址自动打开浏览器窗口，并显示“Hello World”消息。

#更新应用
这编辑server.js文件以返回新消息：
response.end(' www.study.163.com');

#重新构建版本的镜像
docker build -t hello-node:v2 .

#更新部署的镜像
```bash
kubectl set image deployment/hello-node hello-node=hello-node:v2
```
#再次运行您的应用以查看新消息