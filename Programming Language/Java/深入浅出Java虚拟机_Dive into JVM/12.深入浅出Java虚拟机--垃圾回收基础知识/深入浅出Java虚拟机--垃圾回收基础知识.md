**修订记录**

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2017.07.22 | 丁一 | 初稿 |

## 1.如何识别垃圾对象？
JVM是如何识别对象是否存活呢？ 主要有以下几种方法：

- 引用计数法
- 可达性分析算法

### 1.1.引用计数法
引用计数法的逻辑非常简单：在堆中存储对象时，在对象头处维护一个counter计数器，如果一个对象增加了一个引用与之相连，则将counter++。如果一个引用关系失效则counter–。如果一个对象的counter变为0，则说明该对象已经被废弃，不处于存活状态。

这种方法来标记对象的状态会导致两个问题：

1. jdk从1.2开始增加了多种引用方式：软引用、弱引用、虚引用，且在不同引用情况下程序应进行不同的操作。如果我们只采用一个引用计数法来计数无法准确的区分这么多种引用的情况。
2. 引用计数法最致命的缺陷是不能解决循环引用的问题。
如果一个对象A持有对象B，而对象B也持有一个对象A，那发生了类似操作系统中死锁的循环持有，这种情况下A与B的counter恒大于1，会使得GC永远无法回收这两个对象。
比如下面的这个例子：

```java
    public static void testGC() {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();

        objA.instance = objB;
        objB.instance = objA;

        objA = null;
        objB = null;

        System.gc();
    }
```
objA指向的对象ReferenceCountingGC，被objA和objB的instance所引用，虽然objA为null，但objB中的instance引用计数还是存在。所以counter不为0，不会被回收。

### 1.2.可达性分析算法
在主流的商用程序语言中(Java和C#)，都是使用可达性分析算法判断对象是否存活的。这个算法的基本思路就是通过一系列名为GC Roots的对象作为起始点，从这些节点开始向下搜索，搜索所走过的路径称为引用链(Reference Chain)，当一个对象到GC Roots没有任何引用链相连时，则证明此对象是不可用的，下图对象object5, object6, object7虽然有互相判断，但它们到GC Roots是不可达的，所以它们将会判定为是可回收对象。

![ ](images/%E5%8F%AF%E8%BE%BE%E5%88%86%E6%9E%90.jpg)

那么那些点可以作为GC Roots呢？一般来说，如下情况的对象可以作为GC Roots：

- 虚拟机栈(栈桢中的本地变量表)中的引用的对象 
- 方法区中的类静态属性引用的对象 
- 方法区中的常量引用的对象 
- 本地方法栈中JNI（Native方法）的引用的对象 

一般我们能控制的就是JVM栈中的引用和静态类，常量的引用。标记也分为几个阶段，比如

1. 标记直接和根引用的对象
2. 标记间接和根引用的对象
3. 由于分代算法，被老年代对象所引用的新生代的对象

不可达对象一定会被回收吗？不一定。
执行垃圾回收前JVM会执行不可达对象的finalize方法，如果执行完毕之后该对象又变为可达，则不会被回收它。
但一个对象的finalize方法只会被执行一次。

如下例子：
```java
public class FinalizeEscapeGC {
    public static FinalizeEscapeGC SAVE_HOOK = null;

    public void isAlive() {
        System.out.println("yes, i am still alive :)");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed!");
        FinalizeEscapeGC.SAVE_HOOK = this;
    }

    public static void main(String[] args) throws Throwable {
        SAVE_HOOK = new FinalizeEscapeGC();

        //对象第一次成功拯救自己
        SAVE_HOOK = null;
        System.gc();
        // 因为Finalizer方法优先级很低，暂停0.5秒，以等待它
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no, i am dead :(");
        }

        // 下面这段代码与上面的完全相同，但是这次自救却失败了
        SAVE_HOOK = null;
        System.gc();
        // 因为Finalizer方法优先级很低，暂停0.5秒，以等待它
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no, i am dead :(");
        }
    }
}
```

输出结果为：
>finalize method executed!
>yes, i am still alive :)
>no, i am dead :(

如果把finalize()方法中的
> FinalizeEscapeGC.SAVE_HOOK = this
改成
> FinalizeEscapeGC.SAVE_HOOK = new FinalizeEscapeGC();

则会输出：
>finalize method executed!
>yes, i am still alive :)
>finalize method executed!
>yes, i am still alive :)

因为在第一次调用finalize()方法时，SAVE_HOOK又new了一个新的对象，所以在下次gc时，会继续触发finalize()方法。

### 1.3.虚拟机如何实现可达性算法？
java中的主流虚拟机HotSpot采用可达性分析算法来确定一个对象的状态，那么HotSpot在具体实现该算法时采用了哪些结构？

#### 1.3.1.使用OopMap记录并枚举根节点
虚拟机首先需要枚举所有的GC Roots根节点，虚拟机栈的空间不大，遍历一次的时间或许可以接受，但是方法区的空间很可能就有数百兆，遍历一次需要很久。更加关键的是，当我们遍历所有GC Roots根节点时，我们需要暂停所有用户线程，即Stop The World（简称STW），因为我们需要一个此时此刻的”虚拟机快照”，如果我们不暂停用户线程，那么虚拟机仍处于运行状态，我们无法确保能够正确遍历所有的根节点。所以此时的时间开销过大更是我们不能接受的。

基于这种情况，虚拟机实现了一种叫做OopMap的数据结构，这种数据结构在类加载完成时把对象内的偏移量是什么类型计算出，在JIT编译过程中，也会在特定的位置记录下栈和寄存器中哪些位置是引用。当需要遍历根结点时访问所有OopMap即可。

#### 1.3.2.用安全点约束根节点
在OopMap的帮助下，虚拟机可以快速完成根节点枚举，但如果一旦引用关系发生变化，或者OopMap内容变化的指令非常多，如果为每一条指令都生成OopMap，那么会需要大量的额外空间。

实际上，虚拟机并没有为每一个指令都创建OopMap，只在特定的位置上创建了这些信息，这些位置称为安全点（Safepoints）。

safepoint指的特定位置主要有:

1. 循环的末尾 (防止大循环的时候一直不进入safepoint，而其他线程在等待它进入safepoint)
1. 方法返回前
1. 调用方法的call之后
1. 抛出异常的位置

之所以选择这些位置作为safepoint的插入点，主要的考虑是“避免程序长时间运行而不进入safepoint”，比如GC的时候必须要等到Java线程都进入到safepoint的时候VMThread才能开始执行GC，如果程序长时间运行而没有进入safepoint，那么GC也无法开始，JVM可能进入到Freezen假死状态。在stackoverflow上有人提到过一个问题，由于BigInteger的pow执行时JVM没有插入safepoint,导致大量运算时线程一直无法进入safepoint，而GC线程也在等待这个Java线程进入safepoint才能开始GC，结果JVM就Freezen了。

JVM在很多场景下使用到safepoint, 最常见的场景就是GC的时候。对一个Java线程来说，它要么处在safepoint,要么不在safepoint。

VMThread会一直等待直到VMOperationQueue中有操作请求出现，比如GC请求。而VMThread要开始工作必须要等到所有的Java线程进入到safepoint。JVM维护了一个数据结构，记录了所有的线程，所以它可以快速检查所有线程的状态。当有GC请求时，所有进入到safepoint的Java线程会在一个Thread_Lock锁阻塞，直到当JVM操作完成后，VM释放Thread_Lock，阻塞的Java线程才能继续运行。

#### 1.3.3.安全区域
safepoint只能处理正在运行的线程，它们可以主动运行到safepoint。而一些Sleep或者被blocked的线程不能主动运行到safepoint。这些线程也需要在GC的时候被标记检查，

JVM引入了safe region的概念。safe region是指一块区域，这块区域中的引用都不会被修改，比如线程被阻塞了，那么它的线程堆栈中的引用是不会被修改的，JVM可以安全地进行标记。线程进入到safe region的时候先标识自己进入了safe region，等它被唤醒准备离开safe region的时候，先检查能否离开，如果GC已经完成，那么可以离开，否则就在safe region呆在。这可以理解，因为如果GC还没完成，那么这些在safe region中的线程也是被stop the world所影响的线程的一部分，如果让他们可以正常执行了，可能会影响标记的结果。

在JVM启动参数的GC参数里，多加一句:
> -XX:+PrintGCApplicationStoppedTime

它就会把全部的JVM停顿时间（不只是GC），打印在GC日志里。
```java
2016-08-22T00:19:49.559+0800: 219.140: Total time for which application threads were stopped: 0.0053630 seconds
```
## 2.JVM Client和Server运行模式
### 区别
JVM 运行在Server模式下的启动会比Client模式慢，但启动后Server模式可以大大提升性能。Client模式的响应延时低。
所以Server模式适合服务器后台执行业务操作。
Client模式合适GUI界面和用户进行交互。

### 如何查看运行模式
使用java -version命令，
比如
```java
C:\Documents and Settings\Administrator>java -version
java version "1.6.0_21"
Java(TM) SE Runtime Environment (build 1.6.0_21-b06)
Java HotSpot(TM) Client VM (build 17.0-b16, mixed mode, sharing)
```

### 如何修改运行模式
JVM如果不显式指定是-Server模式还是-client模式，JVM能够根据下列原则进行自动判断（适用于Java5版本或者Java以上版本）。
也可以指定运行模式：

```java
java -client yourclass
java -server yourclass
```

或者通过配置文件切换：
32位的虚拟机在目录JAVA_HOME/jre/lib/i386/jvm.cfg,
64位的在JAVA_HOME/jre/lib/amd64/jvm.cfg, 目前64位只支持server模式。

## 3.吞吐量优先和响应优先（低暂停）
gc时的开销：
增加额外的线程调度开销：直接开销是上下文切换，间接开销是因为缓存的影响。
**高吞吐量** 需要尽可能少运行GC，但每次gc的时间可能会长，也就是 高暂停。
**低暂停** 需要每次GC的时间很短，但为了完成垃圾回收又需要频繁执行GC，这样会造成吞吐量的下降。

高吞吐量最好因为这会让应用程序的最终用户感觉只有应用程序线程在做“生产性”工作。 直觉上，吞吐量越高程序运行越快。

低暂停时间最好因为从最终用户的角度来看不管是GC还是其他原因导致一个应用被挂起始终是不好的。 这取决于应用程序的类型，有时候甚至短暂的200毫秒暂停都可能打断终端用户体验。 因此，具有低的最大暂停时间是非常重要的，特别是对于一个交互式应用程序。

不幸的是”高吞吐量”和”低暂停时间”是一对相互竞争的目标（矛盾）。这样想想看，为了清晰起见简化一下：GC需要一定的前提条件以便安全地运行。例如，必须保证应用程序线程在GC线程试图确定哪些对象仍然被引用和哪些没有被引用的时候不修改对象的状态。 为此，应用程序在GC期间必须停止(或者仅在GC的特定阶段，这取决于所使用的算法)。 然而这会增加额外的线程调度开销：直接开销是上下文切换，间接开销是因为缓存的影响。加上JVM内部安全措施的开销，这意味着GC随之而来的不可忽略的开销，将增加GC线程执行实际工作的时间。 因此我们可以通过尽可能少运行GC来最大化吞吐量，例如，只有在不可避免的时候进行GC，来节省所有与它相关的开销。

然而，仅仅偶尔运行GC意味着每当GC运行时将有许多工作要做，因为在此期间积累在堆中的对象数量很高。单个GC需要花更多时间来完成， 从而导致更高的平均和最大暂停时间。因此，考虑到低暂停时间，最好频繁地运行GC以便更快速地完成。 这反过来又增加了开销并导致吞吐量下降，我们又回到了起点。

综上所述，在设计（或使用）GC算法时，我们必须确定我们的目标：一个GC算法只可能针对两个目标之一（即只专注于最大吞吐量或最小暂停时间），或尝试找到一个二者的折衷。





