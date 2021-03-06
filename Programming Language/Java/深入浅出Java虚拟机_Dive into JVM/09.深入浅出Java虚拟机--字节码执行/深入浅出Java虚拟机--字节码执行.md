**修订记录**

| 时间 | 内容 |
|--------|--------|
| 2017.08.16 | 初稿 |

## 方法调用
首先澄清两个概念：

- 直接引用：方法在实际运行时内存布局中的入口地址。
- 符号引用：在class文件中存储的方法信息。

Java中的方法调用并不涉及具体方法的执行，而是要确定调用哪个方法。
Java是通过符号引用找到对应的方法的，这样在类加载，甚至在运行期才能确定目标方法的直接引用。

方法调用主要分为两种：

- 解析调用
- 分派调用

### 1.解析调用
我们知道，在类加载的解析阶段，有些符号引用是会被直接转换成直接引用的。 这类过程称为解析调用。
到底哪些符号引用会被解析呢？需要满足“编译期可知，运行时不可变”的原则。即该方法在编译时即可确定其调用的版本，且该方法在运行期间是不会改变的。


符合静态解析标准的方法主要有以下几种：

- 私有方法
- 静态方法
- 父类方法
- 被final修饰的方法

可以看出，上述几种方法都是不支持覆写的，所以在编译期即可确认其执行版本，因而支持静态解析。

Java虚拟机一共提供了5个方法调用的指令：

| 指令 | 描述 |
|--------|--------|
| invokestatic    |  调用静态方法      |
| invokespecial       |  调用实例构造器方法，私有方法和父类方法      |
| invokevirtual       |  调用实例方法      |
| invokeinterface       |  调用接口方法，会在运行时再确定一个实现此接口的对象      |
| invokedynamic       |  先在运行时动态解析出调用点限定符所引用的方法，然后再执行该方法      |

invokestatic,invokespecial两个指令所调用的方法都是在编译期即可确定其唯一调用版本，符合这个条件的有静态方法，私有方法，实例构造器和父类方法四类。它们在类加载的时候就会把符号引用解析为该方法的直接引用。这些方法可以统称为非虚方法，与之相反，其它方法就称为虚方法。

虽然final方法是使用invokevirtual指令来调用的，但是由于它无法被覆盖，在Java语言规范中明确说明了final方法是一种非虚方法。

### 2.分派调用
分派调用是Java多态行为的一种体现，它分为：

- 静态分派（overload）
- 动态分派（override）

#### 静态分派
静态分派多发生在方法的重载(overload)上，来看下面这个例子：

```java
public class StaticDispatch {
    static abstract class Human {
    }
	
    static class Man extends Human {
    }
	
    static class Woman extends Human {
    }  

    public void sayHello(Human guy) {  
        System.out.println("hello guy...");  
    }  
	
    public void sayHello(Man man) {  
        System.out.println("hello man...");  
    }

    public void sayHello(Woman woman) {  
        System.out.println("hello woman...");  
    }

    public static void main(String[] args) {  
        Human man = new Man();  
        Human woman = new Woman();  
        StaticDispatch sd = new StaticDispatch();  
        sd.sayHello(man);  
        sd.sayHello(woman);  
    }  
}
```

执行结果为：

>hello guy...
>hello guy...

为什么虚拟机执行的是public void sayHello(Human guy)呢？这里需要解释一个概念，首先来看下main方法中的前两行代码:

```java
Human man = new Man();  
Human woman = new Woman();  
```

一个实例对象有静态类型和实际类型两个类型，静态类型在编译时即确定，而实际类型则需要到运行时才可确定。上述两个变量的静态类型均为Human，而实际类型则为Man和Woman。

静态类型在编译时即可确定并不是说静态类型不可改变，下面两行代码即可改变静态类型：

```java
sd.sayHello((Man)man);  
sd.sayHello((Woman)woman);  
```

由于虚拟机在编译重载方法调用指令时是通过参数的静态类型进行选择的，并且静态类型是在编译期即可确定的，所以在上述的例子中虚拟机执行的方法是public void sayHello(Human guy)。

编译器虽然能确认出方法的重载版本，但很多情况下，这个重载版本并不是唯一的，往往只能确定一个“最合适”的版本。
比如下面的例子：
```java
public class Overload {
    public static void SayHello(Object o) {
        System.out.println("obj");
    }

    public static void sayHello(int i) {
        System.out.println("int");
    }

    public static void sayHello(long l) {
        System.out.println("long");
    }

    public static void sayHello(char c) {
        System.out.println("char");
    }
}
```

对于调用：

sayHello('c');
结果是 
> char

这没有问题，如果把sayHello(char c)方法注释掉呢？
再次执行方法，会看到输出变成了：
> int

如果再把sayHello(int i)方法注释掉呢？
再次执行方法，会看到输出变成了：
> long

也就是说，虚拟机会找最接近参数类型的方法进行调用。

#### 动态分派
动态分派即在运行时才确定方法执行的具体版本并进行分派。动态分派最典型的场景就是：方法重写(override)。

```java
public class DynamicDispatch {
    static abstract class Human {  
        protected abstract void sayHello();  
    }  
    static class Man extends Human {  
        @Overrideprotected void sayHello() {  
            System.out.println("man say hello");              
        }  
    }  
    static class Woman extends Human {  
        @Overrideprotected void sayHello() {  
            System.out.println("woman say hello");  
        }  
    }  

    public static void main(String[] args) {  
        Human man = new Man();  
        Human woman = new Woman();  
        man.sayHello();  
        woman.sayHello();  
        man = new Woman();  
        man.sayHello();  
    }  
}
```

运行结果：

>man say hello
>woman say hello
>woman say hello

在上述代码中，两个静态类型均为Human的对象调用相同的方法却实际上并没有执行相同的方法，说明其方法的分派并不是通过静态类型来确定，而是根据两个变量的实际类型来确定的。Java虚拟机是如何利用实际类型来分派方法的执行版本的呢？来看看上述代码的字节码：

```java
  public static void main(java.lang.String[]);                                                           descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code: 
      stack=2, locals=3, args_size=1
         0: new           #2     // class DynamicDispatch$Man
         3: dup
         4: invokespecial #3     // Method DynamicDispatch$Man."<init>":()V
         7: astore_1
        8: new           #4     // class DynamicDispatch$Woman
        11: dup
        12: invokespecial #5     // Method DynamicDispatch$Woman."<init>":()V
        15: astore_2
        16: aload_1
        17: invokevirtual #6     // Method DynamicDispatch$Human.sayHello:()V
        20: aload_2
        21: invokevirtual #6     // Method DynamicDispatch$Human.sayHello:()V
        24: new           #4     // class DynamicDispatch$Woman
        27: dup
        28: invokespecial #5     // Method DynamicDispatch$Woman."<init>":()V
        31: astore_1
        32: aload_1 
        33: invokevirtual #6     // Method DynamicDispatch$Human.sayHello:()V
        36: return 
```

第0-7句，是实例化对象，并存储到局部变量表中。8-15句，是实例化另一个对象。
第16和第20两句分别把刚刚创建的两个对象的引用压到栈顶，这两个对象是将执行的sayHello()方法的所有者，称为接收者(Receiver)，第17和第21两行是方法调用指令，单从字节码的角度来看，这两条调用指令无论是指令(都是invokevirtual)还是参数(都是常量池中Human.sayHello()的符号引用)都完全一样，但是这两条指令最终执行的目标方法并不相同，其原因需要从invokevirutal指令的多态查找过程开始说起，invokevirtual指令的运行时解析过程大致分为以下步骤：

1. 找到操作数栈顶的第一个元素所指向的对象实际类型，记作C。
1. 如果在类型C中找到与常量中描述符和简单名称都相同的方法，则进行访问权限校验，如果通过则返回这个方法的直接引用，查找结束；不通过则返回java.lang.IllegalAccessError错误。
1. 否则，按照继承关系从下往上依次对C的各个父类进行第2步的搜索与校验过程。
1. 如果始终没有找到合适的方法，则抛出java.lang.AbstractMethodError错误。

由于invokevirtual指令执行的第一步就是在运行期确定接收者的实际类型，所以两次调用中的invokevirtual指令把常量池中的类方法符号引用解析到了不同的直接引用上，这个过程就是Java语言中方法重写的本质。我们把这种在运行期根据实际类型确定方法执行版本的分派过程称为动态分派。

Java语言目前属于：静态分配支持多分派，动态分派支持单分派。

## 基于栈的字节码解释执行引擎
虚拟机的执行引擎在执行Java代码的时候都有解释执行和编译执行两种选择。

- 解释执行：通过解释器执行
- 编译执行：通过编译器产生本地代码执行

### 解释执行
Java中的javac编译器完成了程序代码经过语法解析、词法解析到抽象语法树，再遍历语法树生成线性的字节码指令流的过程。
因为这一过程是在Java虚拟机外部完成的，而解释器在虚拟机内部。

### 基于栈的解释器执行过程
先看一段代码：
```java
public class ClassResolvor {
  public int calc() {
    int a = 100;
    int b = 200;o
    int c = 300;
    return (a + b) * c;
  }
}
```

生成的字节码如下：
```java
  public int calc();
    descriptor: ()I
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=4, args_size=1
         0: bipush        100
         2: istore_1
         3: sipush        200
         6: istore_2
         7: sipush        300
        10: istore_3
        11: iload_1
        12: iload_2
        13: iadd
        14: iload_3
        15: imul
        16: ireturn
```

字节码执行的过程如下：

#### 1.执行偏移地址为0的指令
![ ](images/case1_1.jpg)

#### 2.执行偏移地址为1的指令
![ ](images/case1_2.jpg)

#### 3.执行偏移地址为11的指令
![ ](images/case1_3.jpg)

#### 4.执行偏移地址为12的指令
![ ](images/case1_4.jpg)

#### 5.执行偏移地址为13的指令
![ ](images/case1_5.jpg)

#### 6.执行偏移地址为14的指令
![ ](images/case1_6.jpg)

#### 7.执行偏移地址为16的指令
![ ](images/case1_7.jpg)

## 案例2
有一个java类：SimpleClass.class，代码如下：

```java
package org.jvminternals;
public class SimpleClass {
    public void sayHello() {
        System.out.println("Hello");
    }
}
```

执行下列命令：
> javap -v -p -s -sysinfo -constants classes/org/jvminternals/SimpleClass.class

输出文件：

跟任何典型的字节码一样，操作数与局部变量、操作数栈、运行时常量池的主要交互如下所示。

构造器函数包含两个指令。首先，this 变量被压栈到操作数栈，然后父类的构造器函数被调用，而这个构造器会消费 this，之后 this 被弹出操作数栈。

![ ](images/case2_1.jpg)

`sayHello()`方法更加复杂，正如之前解释的那样，因为它需要用运行时常量池中的指向符号引用的真实引用。第一个操作码 getstatic 从System类中将out静态变量压到操作数栈。下一个操作码 ldc 把字符串 “Hello” 压栈到操作数栈。最后 invokevirtual 操作符会调用 System.out 变量的 println 方法，从操作数栈作弹出”Hello” 变量作为 println 的一个参数，并在当前线程开辟一个新栈帧。

![ ](images/case2_2.jpg)
![ ](images/case2_3.jpg)
![ ](images/case2_4.jpg)


