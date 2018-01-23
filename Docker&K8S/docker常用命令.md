#Docker常用命令
**elite团队整理出品**

修订记录

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.6.27 | 丁一 | 初稿 |
| 2017.2.22 | 丁一 | 完善Dockerfile命令 |

[TOC]

## 一.docker基本信息
- ### 查看docker版本(version)
```
root@Ubuntu1604--001:/# docker version
Client:
 Version:      1.11.1
 API version:  1.23
 Go version:   go1.5.4
 Git commit:   5604cbe
 Built:        Tue Apr 26 23:43:49 2016
 OS/Arch:      linux/amd64
Server:
 Version:      1.11.1
 API version:  1.23
 Go version:   go1.5.4
 Git commit:   5604cbe
 Built:        Tue Apr 26 23:43:49 2016
 OS/Arch:      linux/amd64
```

- ### 显示docker系统信息(info)
```
root@Ubuntu1604--001:/# docker info
Containers: 6
 Running: 0
 Paused: 0
 Stopped: 6
Images: 5
Server Version: 1.11.1
Storage Driver: aufs
 Root Dir: /var/lib/docker/aufs
 Backing Filesystem: extfs
 Dirs: 21
 Dirperm1 Supported: true
Logging Driver: json-file
Cgroup Driver: cgroupfs
Plugins: 
 Volume: local
 Network: bridge null host
Kernel Version: 4.4.0-21-generic
Operating System: Ubuntu 16.04 LTS
OSType: linux
Architecture: x86_64
CPUs: 2
Total Memory: 7.793 GiB
Name: Ubuntu1604--001
ID: 6TXJ:GZQ5:EQRH:I5WZ:LVNE:YUZT:XYNU:H5GJ:STMF:QIJU:D4MN:KADE
Docker Root Dir: /var/lib/docker
Debug mode (client): false
Debug mode (server): false
Http Proxy: http://proxymsn.zte.com.cn:80/
Registry: https://index.docker.io/v1/
WARNING: No swap limit support
```

## 二.对镜像的操作
- ### 查找镜像(search)
**docker search image_name**
```
root@Ubuntu1604--001:/# docker search mysql
NAME                       DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
mysql                      MySQL is a widely used, open-source relati...   2541      [OK]       
mysql/mysql-server         Optimized MySQL Server Docker images. Crea...   162                  [OK]
centurylink/mysql          Image containing mysql. Optimized to be li...   45                   [OK]
sameersbn/mysql                                                            36                   [OK]
appcontainers/mysql        Centos/Debian Based Customizable MySQL Con...   8                    [OK]
marvambass/mysql           MySQL Server based on Ubuntu 14.04              6                    [OK]
drupaldocker/mysql         MySQL for Drupal                                2                    [OK]
```

- ### 下载镜像(pull)
**docker pull image_name**
```
root@Ubuntu1604--001:/# docker pull python
Using default tag: latest
latest: Pulling from library/python
51f5c6a04d83: Retrying in 1 second 
65e9ddd8bd7a: Retrying in 1 second 
c41545ebedf5: Retrying in 1 second 
51f5c6a04d83: Pull complete 
65e9ddd8bd7a: Pull complete 
c41545ebedf5: Pull complete 
04aed1875617: Pull complete 
cf8af6bdf113: Pull complete 
b1dde6937fab: Pull complete 
c8bc9f75687c: Pull complete 
Digest: sha256:2633e11654afd17e452422652e467030956c1c48aa625c4eb9ad214eade2ff17
Status: Downloaded newer image for python:latest
```

- ### 显示镜像列表(images)
**docker images**
-a, --all=false Show all images; 
--no-trunc=false Don't truncate output;
-q, --quiet=false Only show numeric IDs
```
root@Ubuntu1604--001:~# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
zte-centos          latest              fffe091fe5a9        23 hours ago        256 MB
ubuntu              latest              2fa927b5cdd3        3 weeks ago         122 MB
centos              7.0.1406            72e825c857b2        8 months ago        210.2 MB
```

- ### 删除镜像(rmi)
**docker rmi image_name**
-f, --force=false Force; 
--no-prune=false Do not delete untagged parents
```
root@Ubuntu1604--001:~# docker rmi -f 74
Deleted: sha256:7494a14a7bef2fb547378e6e3237c1f78234b3fb43b70a8ab80394b9f3175af3
Deleted: sha256:9b8df836317579e14c6421fa2cc25e21b877866e072cccac62abcfe640b9a905
```

- ### 镜像操作历史(history)
-no-trunc=false Don't truncate output; 
-q, --quiet=false Only show numeric IDs
**docker history image_name**
```
root@Ubuntu1604--001:/# docker history zte-centos
IMAGE               CREATED             CREATED BY                                      SIZE                COMMENT
fffe091fe5a9        23 hours ago        /bin/bash                                       45.81 MB            
72e825c857b2        8 months ago        /bin/sh -c #(nop) ADD file:7c8ce4768c86ab8a5b   210.2 MB            
<missing>           14 months ago       /bin/sh -c #(nop) MAINTAINER The CentOS Proje   0 B           
```

- ### 保存镜像(save)
**$docker save image_name -o file_path**
-o, --output="" Write to an file 
另外，>表示重定向文件
···
root@Ubuntu1604--001:~/docker# docker save ubuntu > /root/docker/ubuntu.tar
root@Ubuntu1604--001:~/docker# ls /root/docker/
hello.txt  ubuntu.tar  zte-centos
···

- ### 加载镜像(load)
**$docker load -i file_path**
加载一个tar包格式的镜像; 
-i, --input="" Read from a tar archive file
```
// 使用scp将save.tar拷到机器b上，然后
$docker load < /home/save.tar
```

- ### 上传镜像(push)
**$docker push new_image_name**


## 三.对容器的操作
- ### 启动容器(run)
**docker run image_name**
--name: 对容器命名
-d: 容器在后台运行
--restart=always, 出问题后自动重新启动
--restart=on-failure：n, 退出代码非0才会重启,最多重启n次
-p 主机端口号:容器暴漏端口号， 端口映射
-e：设置proxy环境变量
-v 宿主机目录：容器目录， 将宿主机目录作为卷，加载到容器里。
--net host：使用主机网络, host就写成host，不要改成自己的主机ip
--volumes from src_containner containner, 指定容器的卷加入到新容器中
--sig-proxy=false, ctrl+c， 容器不会退出
--net=networkname, 将容器启动在某个创建好的网络[networkname]中
--rm，容器停止运行后自动删除。
```
// 交互式进行容器
root@Ubuntu1604--001:/# docker run -it zte-centos /bin/bash
[root@f6cb183a55c3 /]# 
// 在容器中运行"echo"命令，输出"hello word"
root@Ubuntu1604--001:/# docker run ubuntu echo "hello,world"
hello,world
// 在容器中安装新的程序
$docker run image_name apt-get install -y app_name
// 加载宿主机的目录到容器中
$docker run -d -p 80 --name webset -v $PWD/website:/var/www/html/website nginx
// 容器以本机主机ip启动，并设置代理
$docker run -i -t --net host -e http_proxy=“http://proxyxa.zte.com.cn:80/” -e https_proxy=“http://proxyxa.zte.com.cn:80/” centos:7.0.1406 /bin/bash
// 将redis容器启动在自定义网络app上.
docker run -d --net=app --name db redis:3.2.1
```

- ### 查看容器(ps)
-a: 显示全部容器
-l:显示最后一个容器
-n x: 显示最后x个容器列表
```
// 列出当前所有正在运行的container
root@Ubuntu1604--001:~# docker ps
CONTAINER ID        IMAGE               COMMAND             CREATED              STATUS              PORTS               NAMES
e13331fb01bb        zte-centos          "/bin/bash"         9 seconds ago        Up 9 seconds                            determined_davinci
4674508d52f1        ubuntu              "/bin/bash"         About a minute ago   Up About a minute                       naughty_boyd
// 列出最近一次启动的container
root@Ubuntu1604--001:~# docker ps -l
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
e13331fb01bb        zte-centos          "/bin/bash"         2 minutes ago       Up 2 minutes                            determined_davinci
// 列出全部的container
root@Ubuntu1604--001:~# docker ps -a
```


- ### 生成新的容器(commit)
**docker commit ID new_image_name**
-a, --author="" Author; 
-m, --message="" Commit message

- ### 启动容器(start)
**docker start Name/ID**

- ### 重启容器(start)
**docker restart Name/ID**

- ### 停止容器(stop)
**docker stop Name/ID**

- ### 杀掉容器(kill)
**docker kill Name/ID**

- ### 删除容器(rm)
**删除单个容器：**
**$docker rm Name/ID**
-f, --force=false; 、
-l, --link=false Remove the specified link and not the underlying container; 
-v, --volumes=false Remove the volumes associated to the container
**删除所有容器**
**$docker rm `docker ps -a -q`**

- ### 显示容器日志(logs)
**$docker logs Name/ID**
-f, --follow=false Follow log output; 一直监控，
-t, --timestamps=false Show timestamps
--tail 行数，显示最后几行
```
root@Ubuntu1604--001:~# docker logs 34
root@34b1ebaf1d40:/# 
root@34b1ebaf1d40:/# 
root@34b1ebaf1d40:/# 
root@34b1ebaf1d40:/# 
root@34b1ebaf1d40:/# python
Python 3.5.1 (default, Jun  9 2016, 15:37:54) 
[GCC 4.9.2] on linux
Type "help", "copyright", "credits" or "license" for more information.
>>> println("hello,world")
Traceback (most recent call last):
```

- ### 列出容器中的差异文件/目录(diff)
**	$docker diff Name/ID**
list列表会显示出三种事件，A 增加的，D 删除的，C 被改变的
```
root@Ubuntu1604--001:~# docker diff 34
C /root
A /root/.bash_history
A /root/.python_history
C /usr
C /usr/local
C /usr/local/lib
C /usr/local/lib/python3.5
C /usr/local/lib/python3.5/__pycache__
A /usr/local/lib/python3.5/__pycache__/_collections_abc.cpython-35.pyc
A /usr/local/lib/python3.5/__pycache__/_sitebuiltins.cpython-35.pyc
A /usr/local/lib/python3.5/__pycache__/_sysconfigdata.cpython-35.pyc
A /usr/local/lib/python3.5/__pycache__/_weakrefset.cpython-35.pyc
```

- ### 显示运行中的容器的进程信息(top)
**$docker top Name/ID**
```
root@Ubuntu1604--001:~# docker top 71
UID                 PID                 PPID                C                   STIME               TTY                 TIME                CMD
root                31053               31022               0                   16:02               pts/20              00:00:00            /bin/bash
```

- ### 容器资源统计(stats)
**$docker top Name/ID**
```
root@Ubuntu1604--001:~# docker stats dameon_task 
CONTAINER           CPU %               MEM USAGE / LIMIT     MEM %               NET I/O             BLOCK I/O           PIDS
dameon_task         0.13%               4.243 MB / 8.368 GB   0.05%               7.594 kB / 648 B    7.545 MB / 0 B      2
CONTAINER           CPU %               MEM USAGE / LIMIT     MEM %               NET I/O             BLOCK I/O           PIDS
```

- ### 在容器内部运行进程(exec)
```
root@Ubuntu1604--001:~# docker exec -d dameon_task touch /etc/new_config_file
// 进入容器内部
$docker exec -it website /bin/bash
```

- ### 从容器里拷贝文件/目录到本地路径(cp)
**$docker cp Name:/container_path to_path**
```
root@Ubuntu1604--001:~# docker cp 71:/hello.txt /root/docker
root@Ubuntu1604--001:~# cd docker/
root@Ubuntu1604--001:~/docker# ls
hello.txt  zte-centos
```

- ### 附着容器(attach)
**$docker attach ID**
--no-stdin=false Do not attach stdin; 
--sig-proxy=true Proxify all received signal to the process

## 四.对仓库的操作
- ### 登陆仓库服务器(login)
**$docker login**
 -e, --email="" Email; 
 -p, --password="" Password;
 -u, --username="" Username

## 五.制作镜像
- ### 根据DockerFile生成镜像(build)
**docker build -t="repo_name/image_name:tag" .**
其中最后的.表示当前目录中的镜像， 也可以指定为git仓库地址。
-f name, 指定dockerfile的目录和文件名
--no-cache, 忽略缓存

## 六. DockerFile指令解析
- ### FROM指令
**语法**：FROM image:tag
**解释**：设置要制作的镜像基于哪个镜像，FROM指令必须是整个Dockerfile的第一个指令，如果指定的镜像不存在默认会自动从Docker Hub上下载。

- ### MAINTAINER指令
**语法**：MAINTAINER name
**解释**：作者信息

- ### RUN指令
**语法**：
①RUN command #将会调用/bin/sh -c command
②RUN ["executable", "param1", "param2"]    #将会调用exec执行，以避免有些时候shell方式执行时的传递参数问题，而且有些基础镜像可能不包含/bin/sh
**解释**：RUN指令会在一个新的容器中执行任何命令，然后把执行后的改变提交到当前镜像，提交后的镜像会被用于Dockerfile中定义的下一步操作，RUN中定义的命令会按顺序执行并提交，这正是Docker廉价的提交和可以基于镜像的任何一个历史点创建容器的好处，就像版本控制工具一样。

- ### CMD指令
容器启动时要执行的命令
RUN指令是构建镜像时执行的命令。
```
CMD ["/bin/true"]
CMD ["/bin/true", "-l"]
```
**注意：** 
1. docker run中的命令会覆盖CMD中的命令。
2. DockerFile只能指定一个CMD指令，如果指定多个，只有最后一个会生效。

- ### ENTRYPOINT指令
docker run中指定的命令会被当做参数传入到ENTRYPOINT指定的指令
···
ENTRYPOINT ["/usr/sbin/nginx"]
···
$docker run -it imagename -g "daemon off;"
则启动容器时执行的命令就是：/usr/sbin/nginx -g "daemon off;"
--entrypoint可以在运行时覆盖ENTRYPOINT指令

- ### WORKDIR指令
指定工作目录
···
WORKDIR /opt/webapp/db
RUN bundle install
WORKDIR /opt/webapp
ENTRYPOINT ["rackup"]
···
-w 标志可以在运行时替换工作目录

- ### ENV指令
构建镜像中设置环境变量
···
ENV RVM_PATH /home/kvm/
ENV RVM_PATH /home/kvm/ RVM_ARCHFLAGS="-arch i386" // 指定多个变量
ENV TARGET_DIR /opt/app
WORKDIR $TARGET_DIR
···
-e在运行时指定变量

- ### USER指令
指定镜像执行的用户, 可以是用户名或者是组名，或者uid/gid
```
USER user
USER user:group
USER uid
USER uid:gid
USER user:gid
USER uid:group
```
如果不指定USER，默认是root用户

- ### VOLUME指令
卷功能可以将源代码、数据库或者其他数据添加到镜像中而不是提交到镜像中，并且允许在多个容器内共享这些内容。
```
VOLUME ["/opt/project"]
VOLUME ["/opt/project", "/date"]  // 指定多个卷
```

- ### ADD指令
将构建环境中的文件或目录复制到镜像中。
···
ADD source.lic /opt/app/software.lic // 将构建环境中的source.lic文件复制到镜像
···
1. 如果是以/结尾，则认为是目录。
2. 如果是压缩文件（gzip, bzip2, xz），add后自动解压。

- ### COPY指令
只复制构建环境中的文件或目录到镜像中，不会做提取和解压。
···
COPY conf.d/ /etc/apache2/
···

- ### LABEL指令
向镜像添加元数据，元数据以键值对的形式提供。 
···
LABEL version="1.0
LABEL location="xa" type="data center" role="web server"
···

- ### ARG指令
build时传递给构建运行时的变量， 可以使用--build-arg来调用。
···
ARG build
ARG webapp_user=user
···
则使用build命令构建镜像时，
$ docker build --build-arg build=1234 -t jams/webapp .
这里，build变量会被设置为1234， webapp_user则为user。
docker预制了变量，可以直接使用
```
HTTP_PROXY
http_proxy
HTTPS_PROXY
https_proxy
FTP_PROXY
ftp_proxy
NO_PROXY
no_proxy
```

- ### ONBUILD指令
为镜像添加触发器。当一个镜像被作用其他镜像的基础镜像时，该触发器会被执行。 
···
ONBUILD ADD . /app/src
···
只会继承一次，不会在孙子镜像中触发。

## 七.网络相关
- ### 创建docker网络
**docker network create netname**
```
// 单主机桥接网络
# docker network create app
1e6e13daa97f90618543d10107a12170387dc268cfae0ce0d2ac9e56fa96b639
```

- ### 查看某个docker网络信息
**docker network inspect netname**
```
# docker network inspect app
[
    {
        "Name": "app",
        "Id": "1e6e13daa97f90618543d10107a12170387dc268cfae0ce0d2ac9e56fa96b639",
        "Scope": "local",
        "Driver": "bridge",
        "EnableIPv6": false,
        "IPAM": {
            "Driver": "default",
            "Options": {},
            "Config": [
                {
                    "Subnet": "172.18.0.0/16",
                    "Gateway": "172.18.0.1/16"
                }
            ]
        },
        "Internal": false,
        "Containers": {},
        "Options": {},
        "Labels": {}
    }
]
```

- ### 查看docker网络列表
**docker network ls**
```
# docker network ls
NETWORK ID          NAME                DRIVER
1e6e13daa97f        app                 bridge              
6dae366e8b9e        bridge              bridge              
3efe7c049fcc        host                host                
12d0eed7f745        none                null       
```

- ### 删除某个docker网络
**docker network rm netname**

- ### 将已运行容器连接到某个docker网络
**docker network connect netname containnername**

- ### 将已运行容器断开某个docker网络
**docker network disconnect netname containnername**



