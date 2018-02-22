**修订记录**

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2017.08.10 | 丁一 | 初稿 |

## 概述
在jdk1.2之前，Java的引用定义的很纯粹：存储的数值代表的是另一块内存的起始地址。

## 引用的分类。
上述纯粹的引用对于一些“食之无味、弃之可惜”的对象显得无能为力。如果对象在内存足够时，能保留在内存中，当内存在发生GC后还是很紧张时，可以丢弃掉，这就很有用了。

jdk1.2之后，Java的引用被分为四种：

- 强引用(Strong Reference)
- 软引用(Soft Reference)
- 弱引用(Weak Reference)
- 虚引用(Phantom Reference)

### 强引用
通常我们通过new来创建一个新对象时返回的引用就是一个强引用。

比如

```java
Student student = new Student();
```
若一个对象通过一系列强引用可到达，它就是强可达的(strongly reachable)，那么它就不被回收。
但它在下面两个场景使用时稍显麻烦。

**场景1：HashMap**
考虑下面的场景：现在有一个Product类代表一种产品，这个类被设计为不可扩展的，而此时我们想要为每个产品增加一个编号。一种解决方案是使用`HashMap<Product, Integer>`。于是问题来了，如果我们已经不再需要一个Product对象存在于内存中（比如已经卖出了这件产品），假设指向它的引用为productA，我们这时会给productA赋值为null，然而这时productA过去指向的Product对象并不会被回收，因为它显然还被HashMap使用着。所以这种情况下，我们想要真正的回收一个Product对象，仅仅把它的强引用赋值为null是不够的，还要把相应的条目从HashMap中移除。

**场景2：缓存**
我们一般将大对象或者频繁使用的对象放在缓存中，加速调用。但如果对象不需要时，除了需要删除原对象，还需要手工清理缓存中的对象，这样才能保持数据一致性。

另外，除了以上两种场景，即使手工把引用设置为null，gc认为该对象不存在引用，这时就可以收集它。但至于在哪次gc回收是不确定的。对于后面介绍的弱引用和虚引用，它能保证在每次gc后都回收对象。

### 软引用
软引用可达的对象在内存不充足时才会被回收，比如full gc。

gc对软应用的处理如下：

1. 垃圾收集器在某个时间点决定一个对象是弱可达的(weakly reachable)，比如将引用设置为null，这时垃圾收集器会清除所有指向该对象的弱引用，
1. 然后把这个弱可达对象标记为可终结(finalizable)的，这样它随后就会被回收。
1. 垃圾收集器会把那些刚清除的弱引用放入创建弱引用对象时所指定的引用队列(Reference Queue)中。

对于如下的代码：

```java
String abc=new String("abc");       
SoftReference<String> abcSoftRef = new SoftReference<String>(abc);       
abc=null;       
System.out.println("before gc: "+abcSoftRef.get());       
System.gc();       
System.out.println("after gc: "+abcSoftRef.get());  
```
运行结果:    

> before gc: abc    
> after gc: abc  

可以看出，如果内存充足的情况下，及时触发gc，也不会清除对象。


### 弱引用
相比软引用只有在内存不足时才会触发回收不同，弱引用是在每次gc时都会触发执行。弱引用的处理过程和软引用类似（参考上节）。

对于如下的代码：

```java
String abc=new String("abc");
WeakReference<String> abcWeakRef = new WeakReference<String>(abc);
abc=null;
System.out.println("before gc: "+abcWeakRef.get());
System.gc();
System.out.println("after gc: "+abcWeakRef.get());
```
运行结果:

> before gc: abc
> after gc: null

可以看出，只要触发gc，就会导致清除弱引用及对象。

在前面强引用的使用场景中，显然“从HashMap中移除不再需要的条目”这个工作我们不想自己完成，我们希望告诉垃圾收集器：在只有HashMap中的key在引用着Product对象的情况下，就可以回收相应Product对象了。显然，根据前面弱引用的定义，使用弱引用能帮助我们达成这个目的。我们只需要用一个指向Product对象的弱引用对象来作为HashMap中的key就可以了。

实际上，对于这种情况，Java类库为我们提供了WeakHashMap类，使用和这个类，它的键自然就是弱引用对象，无需我们再手动包装原始对象。这样一来，当productA变为null时（表明它所引用的Product已经无需存在于内存中），这时指向这个Product对象的就是由弱引用对象weakProductA了，那么显然这时候相应的Product对象时弱可达的，所以指向它的弱引用会被清除，这个Product对象随即会被回收，指向它的弱引用对象会进入引用队列中。

如下代码是使用HashMap的例子：

```java
 Map<Student, String> map = new HashMap<>();
 Student s1 = new Student();
 Student s2 = new Student();

 map.put(s1, "aaa");
 map.put(s2, "bbb");

 s1 = null;
 System.gc();
 map.values().stream().forEach(x -> System.out.println(x+","));
```
输出结果：

> aaa,
> bbb,

可以看到，即使把引用s1设为null，并手工调用了gc，map的值还是两个。

下面是使用WeakHashMap的代码：

```java
 Map<Student, String> map = new WeakHashMap<>();
 Student s1 = new Student();
 Student s2 = new Student();

 map.put(s1, "aaa");
 map.put(s2, "bbb");

 s1 = null;
 System.gc();
 map.values().stream().forEach(x -> System.out.println(x+","));
```
输出结果：

> bbb,

可以看到，在把s1设为null，并手工调用了gc后，map中原有s1的key和value都自动清除了。

### 虚引用
虚引用顾名思义，就是形同虚设，与其他几种引用都不同，虚引用并不会决定对象的生命周期。如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收。

虚引用主要用来跟踪对象被垃圾回收的活动。虚引用与软引用和弱引用的一个区别在于：虚引用必须和引用队列（ReferenceQueue）联合使用。当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，把这个虚引用加入到与之关联的引用队列中。程序可以通过判断引用队列中是否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。程序如果发现某个虚引用已经被加入到引用队列，那么就可以在所引用的对象的内存被回收之前采取必要的行动。

一个例子：

```java
String abc = new String("abc");
System.out.println(abc.getClass() + "@" + abc.hashCode());
final ReferenceQueue referenceQueue = new ReferenceQueue<String>();

new Thread() {
    public void run() {
        while (isRun) {
            Object o = referenceQueue.poll();
            if (o != null) {
                try {
                    Field rereferent = Reference.class.getDeclaredField("referent");
                    rereferent.setAccessible(true);
                    Object result = rereferent.get(o);
                    System.out.println("gc will collect:"
                            + result.getClass() + "@"
                            + result.hashCode());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}.start();

PhantomReference<String> phantomRef = new PhantomReference<String>(abc, referenceQueue);
abc = null;
Thread.currentThread().sleep(3000);
System.gc();
Thread.currentThread().sleep(3000);
isRun = false;
```

输出结果:

> class java.lang.String@96354
> gc will collect:class java.lang.String@96354


