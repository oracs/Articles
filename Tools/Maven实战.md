# Maven实战

**修订记录**

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.07.18 | 丁一 | 初稿 |

**目录**

[TOC]

### maven设置代理
在windows目录：用户\.m2中新建文件settings.xml,(可以从maven安装目录的\conf\settings.xml拷贝一份)，在<proxies>中添加如下内容：
```xml
<proxy>
  <id>zte-proxy</id>
  <active>true</active>
  <protocol>http</protocol>      
  <host>proxyxa.zte.com.cn</host>
  <port>80</port>
 <onProxyHosts>localhost|127.0.0.1|localaddress|.localdomain.com|10.0.0.0/8|.zte.com.cn</nonProxyHosts>
</proxy>
```

### 国内可用的maven仓库
在${user.home}/.m2/settings.xml中的<mirrors>添加下面的参数：
- zte仓库
```xml
  <mirror>
    <id>nexus-zte</id>
    <mirrorOf>*</mirrorOf>
    <name>Nexus ZTE</name>
    <url>http://maven.zte.com.cn/content/groups/public</url>
  </mirror>
```

- oschina仓库
```xml
    <mirror>
      <id>oschina.maven.repository</id>
      <name>oschina maven repository</name>
      <url>http://maven.oschina.net/content/groups/public/</url>
      <mirrorOf>*</mirrorOf>
    </mirror>
```

### 在pom中设置仓库
```xml
 <repositories>
 	<repository>
 		<id>sonatype-forge</id>
 		<name>Sonatype Forge</name>
 		<url>http://repository.sonatype.org/content/groups/forge/</url>
 		<releases>
 			<enabled>true</enabled>
 		</releases>
 		<snapshots>
 			<enabled>false</enabled>
 		</snapshots>
 	</repository>
 </repositories>
```

### 修改本地仓库路径
默认本地路径保存在：${user.home}/.m2/repository
如果想改变这个默认路径，可以修改文件:${user.home}/.m2/settings.xml
```xml
<settings>
  <localRepository>you_local_path</localRepository>
</settings>  
```

### 编译：mvn clean compile

### 测试：mvn clean test

### 打包：mvn clean package

### 发布：mvn clean install
-pl modulename： 构建指定的模块，模块间用逗号分隔
-am modulename: 同时构建所列模块的依赖模块
-amd modulename: 同时构建依赖于所列模块的模块
-rf: 从指定的模块回复反应堆。
可以组合使用。
-P[profile_name]: 激活profile

把本地jar安装到maven仓库
mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc14 -Dversion=10.2.0.2.0 -Dpackaging=jar -Dfile=d:\ojdbc14.jar


### 生成项目骨架： mvn archetype:generate

### 查询项目依赖： mvn dependency
- 按列表显示：mvn dependency:list
- 按树型结构展示： mvn dependency:tree
- 分析依赖：mvn dependency:analyze
