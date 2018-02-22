**修订记录**

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2017.09.02 | 丁一 | 新建 |

## 1.Linux的性能监控工具
### 1.1.top
top命令是Linux下常用的性能分析工具，能够实时显示系统中各个进程的资源占用状况，类似于Windows的任务管理器。

top显示系统当前的进程和其他状况,是一个动态显示过程,即可以通过用户按键来不断刷新当前状态.如果在前台执行该命令,它将独占前台,直到用户终止该程序为止. 比较准确的说,top命令提供了实时的对系统处理器的状态监视.它将显示系统中CPU最“敏感”的任务列表.该命令可以按CPU使用.内存使用和执行时间对任务进行排序；而且该命令的很多特性都可以通过交互式命令或者在个人定制文件中进行设定.

下面详细介绍它的使用方法。

#### 1.1.1.top命令信息介绍
```java
[root@omcdrl ~]# top
top - 14:36:13 up 8 days,  5:50,  2 users,  load average: 0.00, 0.02, 0.04
Tasks: 404 total,   1 running, 403 sleeping,   0 stopped,   0 zombie
Cpu(s):  0.0%us,  0.1%sy,  0.0%ni, 99.8%id,  0.1%wa,  0.0%hi,  0.0%si,  0.0%st
Mem:   8050144k total,  4202680k used,  3847464k free,   726776k buffers
Swap: 31457272k total,        0k used, 31457272k free,  2618044k cached

  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND
  3770 oracle    20   0 1131m  17m  15m S  0.3  0.2   0:59.01 oracle
  4187 oracle    20   0 1134m  28m  24m S  0.3  0.4  14:19.30 oracle
  8155 root      20   0 15300 1496  960 R  0.3  0.0   0:00.52 top
```

统计信息区前五行是系统整体的统计信息。第一行是任务队列信息，同 uptime 命令的执行结果。其内容如下
```java
[root@omcdrl ~]# uptime
 14:42:55 up 8 days,  5:56,  2 users,  load average: 0.01, 0.02, 0.02
```
其中
 14:42:55 表示当前时间
 up 8 days,  5:56, 标识系统已经运行了8天零5小时56分
 2 users  表示当前有两个用户正在使用top命令
 load average: 0.01, 0.02, 0.02 表示最近1分钟、5分钟和15分钟内的系统平均负载。 满负荷的值为1*CPU数量


第二、三行为进程和CPU的信息。当有多个CPU时，这些内容可能会超过两行。含义如下：
```java
Tasks:
  404 total, 表示进程总数404
  1 running, 正在运行的进程数1
  403 sleeping, 睡眠的进程数
  0 stopped,  停止的进程数
  0 zombie  僵尸进程

Cpu(s):
  0.0%us,  用户空间占用CPU百分比
  1.0.1%sy,  内核空间占用CPU百分比
  0.0%ni, 用户进程空间内改变过优先级的进程占用CPU百分比
  99.8%id, 空闲CPU百分比
  0.1%wa, 等待输入输出的CPU时间百分比
  0.0%hi, 硬件CPU中断占用百分比
  0.0%si, 软中断占用百分比
  0.0%s   虚拟机占用百分比
```
最后两行为内存信息。含义如下：
```java
Mem:
  8050144k total, 物理内存总量
  4202680k used,  已使用的物理内存总量
  3847464k free,  空闲的物理内存
  726776k buffers 用作内核缓存的内存量

Swap:
  31457272k total,  交换区总量
  0k used, 使用的交换区总量
  31457272k free,  空闲的交换区总量
  2618044k cached  缓冲的交换区总量
```
进程统计区显示了各个进程的详细信息，各个列含义如下：
```java
序号  列名    含义
a    PID     进程id
b    PPID    父进程id
c    RUSER   Real user name
d    UID     进程所有者的用户id
e    USER    进程所有者的用户名
f    GROUP   进程所有者的组名
g    TTY     启动进程的终端名。不是从终端启动的进程则显示为 ?
h    PR      优先级
i    NI      nice值。负值表示高优先级，正值表示低优先级
j    P       最后使用的CPU，仅在多CPU环境下有意义
k    %CPU    上次更新到现在的CPU时间占用百分比
l    TIME    进程使用的CPU时间总计，单位秒
m    TIME+   进程使用的CPU时间总计，单位1/100秒
n    %MEM    进程使用的物理内存百分比
o    VIRT    进程使用的虚拟内存总量，单位kb。VIRT=SWAP+RES
p    SWAP    进程使用的虚拟内存中，被换出的大小，单位kb。
q    RES     进程使用的、未被换出的物理内存大小，单位kb。RES=CODE+DATA
r    CODE    可执行代码占用的物理内存大小，单位kb
s    DATA    可执行代码以外的部分(数据段+栈)占用的物理内存大小，单位kb
t    SHR     共享内存大小，单位kb
u    nFLT    页面错误次数
v    nDRT    最后一次写入到现在，被修改过的页面数。
w    S    进程状态(D=不可中断的睡眠状态,R=运行,S=睡眠,T=跟踪/停止,Z=僵尸进程)
x    COMMAND 命令名/命令行
y    WCHAN   若该进程在睡眠，则显示睡眠中的系统函数名
z    Flags   任务标志，参考 sched.h
```
#### 1.1.2.top交互命令
交互命令是指进入top界面后使用的一些交互选项，具体命令如下：
**基础操作**
1：显示CPU详细信息，每核显示一行
d / s ：修改刷新频率，单位为秒
h：可显示帮助界面
n：指定进程列表显示行数，默认为满屏行数
q：退出top

**面板隐藏显示**
l：隐藏/显示第1行负载信息；
t：隐藏/显示第2~3行CPU信息；
m：隐藏/显示第4~5行内存信息；

**进程列表排序**
M：根据驻留内存大小进行排序；
P：根据CPU使用百分比大小进行排序；
T：根据时间/累计时间进行排序；

#### 1.1.3.top命令的选项
使用top -h 或-v 查看top的版本 以及所有可用参数
```java
[root@omcdrl ~]# top -h
    top: procps version 3.2.8
usage:    top -hv | -abcHimMsS -d delay -n iterations [-u user | -U user] -p pid [,pid ...]
```

各参数含义如下：

```java
-a： 结果根据内存占用量排序
-b： 批处理模式，这个模式下会禁用交互式选项。 开启top工作在批处理模式，这样在需要将top的输出信息输出 到其他程序或文件时非常有用。在这种模式下，top不接受任何输入操作，直到通过-n 设置的重复次数后，或者退出top才终止。
-c： 命令行/程序名 开关， 使用该选项后，COMMAND列会把调用程序的命令行参数显示出来。
-d 秒 ：设置屏幕刷新的延迟时间，如在命令行输入top -d 0.09 则屏幕将每隔0.09秒刷新。若原来的个人配置文件设置的是每隔3秒将不生效。延迟时间可以设置很小的数，但是不能是负数。
-h：Help
-H：表示显示各线程的状态，不配置则只显示进程状态。
-i：只显示空闲或者僵死进程
-m：内存列展示USED而不展示VIRT
-M：内存汇总行的单位自动检测，用浮点数展示。
Mem: 7861.469M total, 4112.914M used, 3748.555M free, 716.602M buffers
-n 次数 ：刷新N次后退出top
-p : 指定进程ID
-s : 安全模式，禁用交互命令
-S : 累计模式 该模式下，每个子进程占用的CPU时间也会累加到父进程上。
-u : 展示指定用户ID相关的内容
-U : 类似于-u 但是-U后可以跟真实的，有效的，保存的和文件系统的UID。
-v : Show library version and the usage prompt, then quit.
```

### 1.2.vmstat
vmstat命令是最常见的Linux/Unix监控工具，可以展现给定时间间隔的服务器的状态值,包括服务器的CPU使用率，内存使用，虚拟内存交换情况,IO读写情况。
一般vmstat工具的使用是通过两个数字参数来完成的，第一个参数是采样的时间间隔数，单位是秒，第二个参数是采样的次数.
```java
Usage:
vmstat [options] [delay [count]]

Options:
-a, —active active/inactive memory
-f, —forks number of forks since boot
-m, —slabs slabinfo
-n, —one-header do not redisplay header
-s, —stats event counter statistics
-d, —disk disk statistics
-D, —disk-sum summarize disk statistics
-p, —partition partition specific statistics
-S, —unit define display unit
-w, —wide wide output

-h, —help display this help and exit
-V, —version output version information and exit
```
```java
root@zhaijf:/# vmstat 2 3
procs —————-memory————— —-swap— ——-io—— -system— ———cpu——-
r b swpd free buff cache si so bi bo in cs us sy id wa st
1 0 0 4553852 111592 1323356 0 0 9 16 149 34 1 0 98 0 0
0 0 0 4553852 111592 1323356 0 0 0 0 666 1455 1 0 99 0 0
0 0 0 4553852 111592 1323356 0 0 0 0 536 1320 1 0 99 0 0
```
命令介绍完毕，现在介绍每个参数的意思。
```java
r 表示运行队列(就是说多少个进程真的分配到CPU)，当这个值超过了CPU数目，就会出现CPU瓶颈了。
b 表示阻塞的进程。
swpd 虚拟内存已使用的大小，如果大于0，表示机器物理内存不足。
free 空闲的物理内存的大小。
buff Linux/Unix系统是用来存储。
cache 缓存区
si 每秒从磁盘读入虚拟内存的大小，如果这个值大于0，表示物理内存不够用或者内存泄露。
so 每秒虚拟内存写入磁盘的大小，如果这个值大于0，同上。
bi 块设备每秒接收的块数量
bo 块设备每秒发送的块数量。
in 每秒CPU的中断次数，包括时间中断
cs 每秒上下文切换次数。
us 用户CPU时间。
sy 系统CPU时间，如果太高，表示系统调用时间长，例如是IO操作频繁。
id 空闲 CPU时间，一般，id + us + sy = 100。
wt 等待IO CPU时间。
```

### 1.3.pidstat工具
监视线程的性能情况
```java
pidstat -p 1187 -u 1 3'
```
1187为程序的PID
1和3代表每秒钟采样1次，合计采样3次
工具的参数说明：
```java
-p 指定进程ID
-u 表示对cpu的监控
-t 监控线程级别的性能
-d 表明监控对象为磁盘I/O
-r 监控内存使用情况
-r的结果显示说明：
•minflt/s进程第秀不需要从磁盘调出内存页的总数
•majflt/s进程每秒需要从磁盘调出内存页的总数
•VSZ进程使用虚拟内存大小，单位KB
•RSS进程占用物理内存大小，单位KB
•%MEM占用内存比率
```
## 2.JDK的命令行工具

| 工具 | 描述 |
|--|--|
| jps |打印Hotspot VM进程。VMID、JVM参数、main()函数参数、主类名/Jar路径 |
| jstat |查看Hotspot VM 运行时信息，比如类加载、内存、GC[可分代查看]、JIT编译 |
| jinfo |查看和修改虚拟机各项配置 |
| jmap |heapdump: 生成VM堆转储快照、查询finalize执行队列、Java堆和永久代详细信息 |
| jstack |查看VM当前时刻的线程快照: 当前VM内每一条线程正在执行的方法堆栈集合 |
| javap |查看经javac之后产生的JVM字节码代码 |
| jcmd | 一个多功能工具, 可以用来导出堆, 查看Java进程、导出线程信息、 执行GC、查看性能相关数据等。<br>几乎集合了jps、jstat、jinfo、jmap、jstack所有功能|

### 2.1.jps
jps的全称为：JVM Process Status Tool。类似unix中的ps命令。列出正在运行的虚拟机进程，并显示执行的main函数所在的类名，以及这些进程的本地虚拟机唯一ID（LVMID：Local Virtual Maching Identifier）。
LVMID和操作系统的PID是保持一致的。

命令格式：
```java
jps [-options][hostid]
```
其中hostid为RMI注册表中注册的主机名。
options如下：
```java
-q：只输出LVMID，省略主类的名称。
-m: 输出虚拟机进程启动时传递给主类main()的参数
-l：输出主类的全名，如果进程执行的是jar包，输出jar路径
-v: 输出虚拟机进行启动时JVM参数
```

例子：
```java
/home/zenap # jps -l
12 com.zte.ums.zenap.fm.active.FmActiveApp
237 sun.tools.jps.Jps
```

### 2.2.jstat
jstat(JVM Statistic Monitoring Tool)用于监控虚拟机各种运行状态信息的命令行工具。它可以显示本地或者远程虚拟机进程中的类装载、内存、GC、JIT等运行数据。

jstat命令格式为：
```java
jstat [option vmid [interval [s|ms] [count]]]
```
对于VMID，如果是本地虚拟机进程，VMID=LVMID，如果是远程进程，则VMID的格式为：
```java
[protocal:][//]lvmid[@hostname[:port]/servername]
```
参数interval和count代表查询间隔和次数。如果省略这两个参数，表示只查询一次。
option参数含义：
```java
-class: 
```

举例：
```java
jstat -class pid:显示加载class的数量，及所占空间等信息。 
jstat -compiler pid:显示VM实时编译的数量等信息。 
jstat -gc pid:可以显示gc的信息，查看gc的次数，及时间。其中最后五项，分别是young gc的次数，young gc的时间，full gc的次数，full gc的时间，gc的总时间。 
jstat -gc 2764 250 20 ，表示每250ms采集一次进程2764的gc信息，一共执行20次。
jstat –gcutil pid， 查看堆中各个内存区域的变化以及GC的工作状态；
jstat  –gcutil -h10 3024 250 600 , 每250毫秒打印一次，一共打印600次 每隔10行显示一次head
jstat -gccapacity:可以显示，VM内存中三代（young,old,perm）对象的使用和占用大小，如：PGCMN显示的是最小perm的内存使用量，PGCMX显示的是perm的内存最大使用量，PGC是当前新生成的perm内存占用量，PC是但前perm内存占用量。其他的可以根据这个类推， OC是old内纯的占用量。 
jstat -gcnew pid:new对象的信息。 
jstat -gcnewcapacity pid:new对象的信息及其占用量。 
jstat -gcold pid:old对象的信息。 
jstat -gcoldcapacity pid:old对象的信息及其占用量。 
jstat -gcpermcapacity pid: perm对象的信息及其占用量。 
jstat -util pid:统计gc信息统计。 
jstat -printcompilation pid:当前VM执行的信息。 
```

除了以上一个参数外，还可以同时加上两个数字，如：jstat -printcompilation 3024 250 6是每250毫秒打印一次，一共打印6次，还可以加上-h3每三行显示一下标题。
样例：

```java
jstat -gcutil -h5 [pid] 4s 100   // jstat –gcutil [pid] [intervel] [count]
```

### 2.3.jinfo

### 2.4.jmap
jmap可以生成Java程序的堆Dump文件，也可以查看堆内对象实例的统计信息，查看ClassLoader的信息及finalizer队列。

例：生成并导出PID为2972的Java程序的对象统计信息

```java
jmap -histo 2972 >/tmp/s.txt

2672为程序的PID
-histo显示内存中实例数量和合计
-dump:format=b应用程序的堆快照二进制输出
-dump:file=/tmp/heap.hprof将堆快照导出的文件名
-permstat查看系统的ClassLoader的信息
-finalizerinfo查看系统finalizer队列中的对象
```

### 2.5.jstack
用于导出Java应用程序的线程堆栈

```java
jstack -l 2577 > /tmp/s.txt
-l 打印锁的附加信息
2577为程序的PID
```

### 2.6.javap
对clasa文件进行解析，获取字节码详细信息。

#### -c参数
对代码进行反汇编

#### -v/-verbose参数
输出附加信息

### 2.7.jcmd

## 3.JDK图形化监控工具
| 工具 | 描述 |
|--|--|
| jconsole |基于JMX的可视化监视、管理工具。可以查看内存、线程、类、CPU信息, 以及对JMX MBean进行管理 |
| jvisualvm | JDK中最强大运行监视和故障处理工具<br>可以监控内存泄露、跟踪垃圾回收、执行时内存分析、CPU分析、线程分析|
