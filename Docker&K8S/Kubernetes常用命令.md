#Kubernetes常用命令
**elite团队整理出品**

修订记录

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.7.12 | 丁一 | 初稿 |

[TOC]

## 一.k8s相关操作
- ### 查看k8s版本
```
root@Ubuntu1604--001:~/pod# kubectl version
Client Version: version.Info{Major:"1", Minor:"1", GitVersion:"v1.1.3", GitCommit:"6a81b50c7e97bbe0ade075de55ab4fa34f049dc2", GitTreeState:"clean"}
Server Version: version.Info{Major:"1", Minor:"1", GitVersion:"v1.1.3", GitCommit:"6a81b50c7e97bbe0ade075de55ab4fa34f049dc2", GitTreeState:"clean"}
```

- ### 查看k8s运行环境信息
```
root@Ubuntu1604--001:~/pod# kubectl cluster-info
Kubernetes master is running at http://localhost:8080
```

- ### 查看master组件健康状态
```
root@Ubuntu1604--002:~# kubectl -s http://Ubuntu1604--001:8080 get componentstatus
NAME                 STATUS    MESSAGE              ERROR
controller-manager   Healthy   ok                   nil
scheduler            Healthy   ok                   nil
etcd-0               Healthy   {"health": "true"}   nil
```

- ### 查看node健康状态
```
root@Ubuntu1604--002:~# kubectl -s http://Ubuntu1604--001:8080 get nodes
NAME              LABELS                                   STATUS    AGE
ubuntu1604--002   kubernetes.io/hostname=ubuntu1604--002   Ready     9d
ubuntu1604--003   kubernetes.io/hostname=ubuntu1604--003   Ready     15h
```

## 二.pod相关命令
- ### 根据文件创建pod
```
root@Ubuntu1604--001:~/pod# kubectl create -f hello-world-pod.yaml 
pod "hello-world" created
```

- ### 查询pod状态
```
root@Ubuntu1604--001:~/pod# kubectl get pod hello-world
NAME          READY     STATUS    RESTARTS   AGE
hello-world   0/1       Pending   0          1m
```
如果想查询完整信息可以，添加-o参数，支持json和yaml
```
root@Ubuntu1604--001:~/pod# kubectl get pod hello-world -o json
{
    "kind": "Pod",
    "apiVersion": "v1",
    "metadata": {
        "name": "hello-world",
        "namespace": "default",
        "selfLink": "/api/v1/namespaces/default/pods/hello-world",
        "uid": "7b1b37f3-47d9-11e6-a320-0050560101ca",
        "resourceVersion": "103304",
        "creationTimestamp": "2016-07-12T02:36:48Z"
    },
```

- ### 查询pod描述信息
```
root@Ubuntu1604--001:~/pod# kubectl describe pod hello-world
Name:                           hello-world
Namespace:                      default
Image(s):                       ubuntu:latest
Node:                           ubuntu1604--003/10.92.249.203
Start Time:                     Tue, 12 Jul 2016 10:36:51 +0800
Labels:                         <none>
Status:                         Succeeded
```

- ### 查询pod日志
```
root@Ubuntu1604--001:~/pod# kubectl logs hello-world
```

- ### 更新pod
```
$kubectl replace /path/my-pod.yaml
```
如果加上--force参数，表示强制更新

- ### 删除pod
```
root@Ubuntu1604--001:~/pod# kubectl delete pod hello-world
pod "hello-world" deleted
```
加上--all参数，表示删除全部pod

- ### 查询事件
```
root@Ubuntu1604--001:~/pod# kubectl get event
```

- ### 远程连接容器
```
root@Ubuntu1604--001:~/pod# kubectl exec -it hello-world-volume hello /bin/bash
```

## 三.ReplicationController相关命令
- ### 查看ReplicationController信息
```
root@Ubuntu1604--001:~/pod# kubectl get replicationcontroller my-nginx
CONTROLLER   CONTAINER(S)   IMAGE(S)   SELECTOR    REPLICAS   AGE
my-nginx     nginx          nginx      app=nginx   2          9m
```

- ### 查看ReplicationController中的pod信息
```
root@Ubuntu1604--001:~/pod# kubectl get pod --selector app=nginx --label-columns app
NAME             READY     STATUS    RESTARTS   AGE       APP
my-nginx-2q39m   1/1       Running   0          10m       nginx
my-nginx-no1ld   1/1       Running   0          10m       nginx
```

- ### 快速创建ReplicationController
```
root@Ubuntu1604--001:~/pod# kubectl run my-nginx --image nginx --replicas 5 --labels app=nginx
replicationcontroller "my-nginx" created
```

- ### 删除ReplicationController
```
root@Ubuntu1604--001:~/pod# kubectl delete rc my-nginx
replicationcontroller "my-nginx" deleted
```
如果删除rc，需要保留pod，可以加上参数：--cascade=false

- ### 手工弹性伸缩
```
root@Ubuntu1604--001:~/pod# kubectl scale replicationcontroller my-nginx --replicas=3
replicationcontroller "my-nginx" scaled
```

- ### 自动弹性伸缩
```
root@Ubuntu1604--001:~/pod# kubectl autoscale rc my-nginx --min=1 --max=10 --cpu-percent=50
replicationcontroller "my-nginx" autoscaled
```
目前的弹性策略是根据CPU使用率。

- ### 查看自动弹性伸缩状态
```
root@Ubuntu1604--001:~/pod# kubectl get horizontalpodautoscaler my-nginx
NAME       REFERENCE                              TARGET    CURRENT     MINPODS   MAXPODS   AGE
my-nginx   ReplicationController/my-nginx/scale   50%       <waiting>   1         10        1m
```

- ### 滚动升级
```
$ kubectl rolling-update my-web-v1 -f my-web-v2.yaml --update-period=10s
```

## 四.service相关命令
- ### 查询service
```
root@Ubuntu1604--001:~/pod# kubectl get service my-nginx
NAME       CLUSTER_IP     EXTERNAL_IP   PORT(S)   SELECTOR    AGE
my-nginx   10.254.40.51   <none>        80/TCP    app=nginx   2m
```

- ### 查看service描述信息
```
root@Ubuntu1604--001:~/pod# kubectl describe service my-nginx
Name:                   my-nginx
Namespace:              default
Labels:                 <none>
Selector:               app=nginx
Type:                   ClusterIP
IP:                     10.254.131.145
Port:                   http    80/TCP
Endpoints:              172.17.0.2:80,172.17.0.2:80,172.17.0.3:80
Port:                   https   443/TCP
Endpoints:              172.17.0.2:443,172.17.0.2:443,172.17.0.3:443
Session Affinity:       None
No events.
```










