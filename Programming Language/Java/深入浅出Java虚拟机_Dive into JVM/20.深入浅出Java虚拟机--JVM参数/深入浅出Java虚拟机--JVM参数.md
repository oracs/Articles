**修订记录**

| 时间 | 内容 |
|--------|--------|--------|
| 2017.06.20  | 初稿 |

## 堆大小设置
| 参数 | 描述 | 适用版本 | 默认值 | 备注 |
|--------|--------|--- | ----- |
|   -Xms     |   最小堆内存 |  | 物理内存的1/64  | 默认(MinHeapFreeRatio参数可以调整)空余堆内存小于40%时，<br>  JVM就会增大堆直到-Xmx的最大限制. |
| -Xmx |最大堆内存 | | 物理内存的1/4 | 默认(MaxHeapFreeRatio参数可以调整)空余堆内存大于70%时，<br> JVM会减少堆直到 -Xms的最小限制 |
| -Xmn | 新生代大小| jdk1.4 or lator | | 注意：此处的大小是（eden + 2 survivor space。与jmap -heap中显示的New gen是不同的。 |
| -XX:PermSize| 永久代大小| | 物理内存的1/64 |  |
| -XX:MaxPermSize | 永久代最大值 | | 物理内存的1/4 |  |
| -Xss  | 每个线程的栈大小 |  |  | JDK5.0以后每个线程堆栈大小为1M, 在相同物理内存下,减小这个值能生成更多的线程。<br> 但是操作系统对一个进程内的线程数还是有限制的,不能无限生成, 经验值在3000~5000左右。<br> 这个选项对性能影响比较大，需要严格的测试。  |
| -XX:NewRatio  | 新生代(包括Eden和两个Survivor区)与年老代的比值 |  |  | -XX:NewRatio=4表示年轻代与年老代所占比值为1:4,年轻代占整个堆栈的1/5。<br>Xms=Xmx并且设置了Xmn的情况下，该参数不需要进行设置。 |
| -XX:SurvivorRatio  | Eden区与Survivor区的大小比值 |  |  | 设置为8,则两个Survivor区与一个Eden区的比值为2:8,一个Survivor区占整个年轻代的1/10 |
| -XX:LargePageSizeInBytes | 内存页的大小不可设置过大，会影响Perm的大小 |  |  |  |
| -XX:+UseFastAccessorMethods  |  原始类型的快速优化 |  |  |  |
| -XX:MaxTenuringThreshold | 垃圾最大年龄 |  |  | 该参数只有在串行GC时才有效。<br>如果设置为0的话,则年轻代对象不经过Survivor区,直接进入年老代。对于年老代比较多的应用,可以提高效率。<br>如果将此值设置为一个较大值,则年轻代对象会在Survivor区进行多次复制,这样可以增加对象在年轻代的存活时间,增加在年轻代即被回收的概率。 |
| -XX:+AggressiveOpts | 加快编译  |  |  |  |
| -XX:+UseBiasedLocking | 锁机制的性能改善 |  |  |  |
| -XX:SoftRefLRUPolicyMSPerMB | 每兆堆空闲空间中SoftReference的存活时间 |  | one second of lifetime per free megabyte in the heap | softly reachable objects will remain alive for some amount of time after the last time they were referenced. |
| -XX:PretenureSizeThreshold | 对象超过多大是直接在老生代分配  |  | 0 | 单位：字节。新生代采用Parallel Scavenge GC时无效。<br>另一种直接在旧生代分配的情况是大的数组对象,且数组中无外部引用对象。 |
| -XX:+CollectGen0First | FullGC时是否先YGC |  | false |  |
| -XX:TLABWasteTargetPercent | TLAB占eden区的百分比 |  | 1% |  |

## 非CMS回收器相关
| 参数 | 描述 | 适用版本 | 默认值 | 备注 |
|--------|--------|--------|-------- |-------- |
| -XX:-UseParallelGC | 策略为新生代使用并行清除  |  | -server时启用其他情况下，默认不启用 | 年老代使用单线程Mark-Sweep-Compact的垃圾收集器。 |
| -XX:-UseParallelOldGC | 策略为老年代使用并行清除的垃圾收集器。 |  | 默认不启用 |  |
| -XX:-UseSerialGC | 使用串行垃圾收集器。 |  | -client时启用其他情况下，默认不启用 |  |
| -XX:PretenureSizeThreshold | 大于该值的对象直接在老年代中分配 |  |  |  |
| -XX:MaxTenuringThreshold | 对象年龄大于该值将晋升到老年代 |  |  |  |
| -XX:-UseAdaptiveSizePolicy | 始终以MaxTenuringThrehsold为准，并且不会重新计算，会是恒定值。 |  |  |  |

## CMS回收器相关
| 参数 | 描述 | 适用版本 | 默认值 | 备注 |
|--------|--------|--------|-------- |-------- |
| -XX:+UseConcMarkSweepGC | 使用CMS内存收集 |  |  | 测试中配置这个以后,-XX:NewRatio=4的配置失效了,原因不明.所以,此时年轻代大小最好用-Xmn设置。 |
| -XX:CMSFullGCsBeforeCompaction  | 多少次后进行内存压缩 |  |  | 由于并发收集器不对内存空间进行压缩,整理,所以运行一段时间以后会产生"碎片",使得运行效率降低。<br>此值设置运行多少次GC以后对内存空间进行压缩，整理。 |
| -XX:+CMSParallelRemarkEnabled | 降低标记停顿 |  |  |  |
| -XX+UseCMSCompactAtFullCollection | 在FULL GC的时候， 对年老代的压缩 |  |  | CMS是不会移动内存的， 因此，这个非常容易产生碎片，导致内存不够用。<br>因此，内存的压缩这个时候就会被启用。 增加这个参数是个好习惯。可能会影响性能,但是可以消除碎片。 |
| -XX:+UseCMSInitiatingOccupancyOnly | 使用手动定义初始化定义开始CMS收集  |  |  | 禁止hostspot自行触发CMS GC |
| -XX:CMSInitiatingOccupancyFraction=70 | 使用cms作为垃圾回收。使用70％后开始CMS收集 |  |  | 为了保证不出现promotion failed错误,该值的设置需要满足以下公式 |
| -XX:CMSInitiatingPermOccupancyFraction | 设置Perm Gen使用到达多少比率时触发 |  | 92 |  |
| -XX:+CMSIncrementalMode | 设置为增量模式 |  |  | 用于单CPU情况 |

## 日志相关
| 参数 | 描述 | 适用版本 | 默认值 | 备注 |
|--------|--------|-------- |-------- |--------  |
| -XX:+PrintGC | 输出GC日志 | |  |  |
| -XX:+PrintGCDetails  | 输出GC的详细日志 | |  |  |
| -XX:+PrintGCTimeStamps | 输出GC时间戳(以基准时间的形式)  | | |  |
| -XX:+PrintHeapAtGC | 在进行GC的前后打印出堆的信息  |  |  | |
| -XX:+PrintTenuringDistribution | 跟踪每次minor GC后新的存活周期的阈值 |  |  |  |
| -Xloggc:/path/gc.log | 日志文件的输出路径 |  |  | |
| -XX:+PrintGCApplicationStoppedTime | 打印由GC产生的停顿时间 |  |  | |
| -XX:+DisableExplicitGC | 关闭System.gc() |  |  |  |

## JIT
| 参数 | 描述 | 适用版本 | 默认值 | 备注 |
|--------|--------|-------- |-------- |--------  |
| -XX: CompileThreshold | JIT：方法调用计数器阀值 | |  |  |
| -XX: -UseCounterDecay | JIT：是否使用方法调用计数半衰周期 | |  |  |
| -XX: CounterHalfLifeTime | JIT：设置方法调用计数半衰周期 | |  |  |
| -XX: BackEdgeThreshhold | JIT：回边计数器阀值 | |  | 虚拟机默认未实现 |
| -XX: BackgroundCompilation | JIT：禁止后台编译| |  |  |
| -XX: PrintCompilation | JIT：打印编译成本地代码的方法 | |  |  |
| -XX: PrintInlining | JIT：打印方法内联信息 | |  |  |
| -XX: PrintAssembly | JIT：打印编译后的汇编代码 | |  |  |















