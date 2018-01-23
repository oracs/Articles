## Docker Compose工具的使用
**elite团队出品**

**修订记录**

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.08.16 | 丁一 | 初稿 |
| 2017.02.17 | 丁一 | 增加：启动时指定文件 |

**目录**

[TOC]

### Docker Compose简介
Docker Compose是一款简单的Docker容器编排工具，通过一个yaml文件定义一组要启动的容器，以及容器运行时的属性，然后利用docker-compose up命令就可以启动这些定义好的容器了。这样比单纯用docker run命令以及在启动多个容器时，就显得方便很多。

### Docker Compose的安装
在linux机器上利用curl命令来安装：
```
$ curl -L https://github.com/docker/compose/releases/download/1.8.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
$ chmod +x /usr/local/bin/docker-compose
```
如果安装好后，执行下面命令进行验证：
```
$ docker-compose --version
docker-compose version 1.8.0, build f3628c7
```
### docker-compose.yml文件
 命名只能叫docker-compose，不能有大写，或者其他字符。
 一个例子：
 ```
config:
  image: "config:1.0.0-SNAPSHOT"
  hostname: config
  links:
    - discovery
  environment:
    EUREKA_HOST: discovery
    EUREKA_PORT: 8761
  ports:
    - "8888:8888"
 ```
 config是目录名，内部的字段和docker run中的参数含义一致。
 如果不指定name参数，则compose启动的时候会自动分配名称：服务名
 
### 启动服务
进入docker-compose.yml文件所在的目录，执行命令
$docker-compose up -d
加上-d参数，表示后台启动。
其他启动参数：
-f 指定docker-compose.xml文件，默认是 docker-compose.xml, 当一条命令有多个-f参数时，会做替换操作
-p 指定docker-compose的项目目录，也就是docker-compose.xml文件的存储目录
 
 ```
root@Ubuntu1604--002:~/compose# docker-compose up -d
Creating compose_postgresdb_1
Creating compose_discovery_1
Creating compose_postgres_1
Creating compose_config_1
Creating compose_some_1
Creating compose_person_1
Creating compose_ui_1
Creating compose_monitor_1
 ```
### 查看运行的服务
$ docker-compose ps
```
root@Ubuntu1604--002:~/compose# docker-compose ps
        Name                      Command               State            Ports          
---------------------------------------------------------------------------------------
compose_config_1       /bin/sh -c /app/runboot.sh       Up       0.0.0.0:8888->8888/tcp 
compose_discovery_1    /bin/sh -c /app/runboot.sh       Up       0.0.0.0:8761->8761/tcp 
compose_monitor_1      /bin/sh -c /app/runboot.sh       Up       8989/tcp               
compose_person_1       /bin/sh -c /app/runboot.sh       Up       8082/tcp               
compose_postgres_1     /docker-entrypoint.sh postgres   Up       5432/tcp               
compose_postgresdb_1   sh                               Exit 0                          
compose_some_1         /bin/sh -c /app/runboot.sh       Up       8083/tcp               
compose_ui_1           /bin/sh -c /app/runboot.sh       Up       0.0.0.0:80->80/tcp
```
### 查看服务日志信息
$ docker-compose logs

### 停止运行的服务
$ docker-compose stop
```
root@Ubuntu1604--002:~/compose# docker-compose stop
Stopping compose_monitor_1 ... 
Stopping compose_ui_1 ... 
Stopping compose_person_1 ... 
Stopping compose_config_1 ... 
Stopping compose_postgres_1 ... 
```

### 杀掉运行的服务
$ docker-compose kill
```
root@Ubuntu1604--002:~/compose# docker-compose kill
Killing compose_ui_1 ... done
Killing compose_person_1 ... done
Killing compose_config_1 ... done
Killing compose_postgres_1 ... done
```