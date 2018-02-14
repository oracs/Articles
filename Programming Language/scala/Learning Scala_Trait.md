# Learning Scala: Trait

修订记录

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.12.8 | 丁一 | 初稿 |

[TOC]

## 什么是Trait
trait很像Java中的接口，但使用场景又比接口丰富。它允许有默认方法实现，可以声明字段。类可以在继承中混入(Mix-in)trait，也可是在类实例化时混入，功能很灵活。总的说来，trait有以下两种用途：
- 用做接口
- 叠加功能

## Trait的使用方法
Trait使用trait关键字进行声明，内部声明方法或者字段。
比如有下面这个trait，提供了飞行的功能：
```scala
trait Flyable {
    def log()

    def fly() {
        println("I can fly")
    }
}
```
其中log()方法是抽象方法，没有实现。fly()方法有一个默认实现。
然后让一个Dog类混入Flyable trait，这时的狗狗就变成了一只会飞的狗狗。
```scala
class Dog extends Flyable {
    override def toString = "barking"

    def log() {
        println(s"dog $toString")
    }
}
```
我们这里实现了log方法，由于trait的log方法是抽象方法，所以这里不用加override关键字。同时我们还重载了Object的toString()方法，这个需要加上override关键字。
再看一下调用的情况：
```scala
val dog = new Dog()
dog.fly()
dog.log()

val flyer: Flyable = new Dog()
flyer.fly()
flyer.log()
```
输出：
>I can fly
>dog barking
>I can fly
>dog barking

从中可以看出：dog具备的trait中提供的fly功能。
当然，一个类型也可以混入多个trait。
下面是另一个例子：
```scala
class Animal
trait HasLeg

class Frog extends Animal with Flyable with HasLeg {
    override def toString = "green"

    override def fly(): Unit = {
        println("i am a frog")
    }

    override def log() {
        println(s"color is $toString")
    }
}
```
这里Frog从基类Animal中继承并混入了两个trait：Flyable和HasLeg。多个trait间用with关键字分割。
调用为：
```scala
 val frog: Flyable = new Frog()
 frog.fly() 
 frog.log() 
```
输出结果为：
>i am a frog
>color is green

## 从瘦接口变成胖接口
Scala trait的一个重要作用就是可以在接口的基础上提供更多的默认实现，这样既能减少了子类的实现，又能提供更丰富的基础功能。
比如我们有一个日志Trait：
```scala
trait Logger {
    def log(msg: String)
}
```
如果我们想提供不同级别的日志打印，按照传统Java的实现方式，需要扩展多个级别的子类，比如WarningLogger，ErrorLogger等。
但Scala的trait简化了这个操作，我们可以直接在trait中提供默认的日志级别。比如下面的实现：
```scala
trait Logger {
    def log(msg: String)

    def info(msg: String) {
        log("INFO: " + msg)
    }

    def warn(msg: String) {
        log("WARN: " + msg)
    }

    def error(msg: String) {
        log("ERROR: " + msg)
    }
}
```
这样的一个Logger Trait就具备的各种日志级别打印功能，当类混入了这个特质后，就可以使用默认实现进行日志输出了。 
```scala
class ConsoleLogger extends Logger {
    def log(msg: String) {
        println(msg)
    }
}

val consoleLogger = new ConsoleLogger()
consoleLogger.info("hello")
consoleLogger.warn("hello")
consoleLogger.error("hello")
```
这样一个用于控制台打印的logger就可以工作了。
输出：
>INFO: hello
>WARN: hello
>ERROR: hello

## 叠加功能
Scala允许在类实例化的时候混入trait，这样的写法就更加灵活，解耦的更彻底。
比如一个日志trait
```scala
trait Log{
    def log(msg: String)
}
```
它有几个不同的子trait，继承自Log。trait的继承用extends关键字表示。
```scala
// 具备打印功能的Logger
trait ConsoleLogger extends Log  {    
    override def log(msg: String) {
        println(msg)
    }
}

// 加了日期头的Logger
trait TimeLogger extends Log {
    override def log(msg: String) {
        super.log(new Date() + "" + msg)
    }
}

// 短格式的Logger
trait ShortLogger extends Log {
    override def log(msg: String) {
        super.log {
            if (msg.length <= maxLength) msg
            else msg.substring(0, maxLength - 3) + "..."
        }
    }
}
```
这里面有一个语法要特别介绍下：super.方法名()。它表示要把方法中的参数结果传递给调用它的trait同名方法。因此这个trait是不能直接混入到类的，它前面一定有一个没有super调用的trait。比如上面的ConsoleLogger。
看一下混入了trait的类实现：
```scala
class SavingsAccount extends Log {
    def withdraw(amount: Double) {
        if (amount < 10) log("insufficient funds")
        log("withdraw " + amount + " dollars.")
    }
}
```
只是单纯混入了最上层的trait Log，并没有用到我们上面提到的子trait。
这就是Scala具备Magic的地方：在类实例化的时候混入trait。
```scala
val account = new SavingsAccount() with ConsoleLogger with TimeLogger with ShortLogger
account.withdraw(100)
```
输出：
>Fri Dec 09 14:53:16 CST 2016withdraw 100...

有一个地方需要注意：这种方式的混入顺序是从**右边**开始的，如果把上面的调用改为：
```scala
val account = new SavingsAccount() with ConsoleLogger with ShortLogger with TimeLogger
account.withdraw(100)
```
输出结果为：
> Fri Dec 09 1...

## 在Trait中使用Field
trait中除了可以声明方法外，还可以声明字段。feild可以是具体字段，也可以是抽象字段。
先看具体字段的情况:
我们在ShortLogger trait中增加了一个maxLength=15的field。
```scala
trait ShortLogger extends Log {
    val maxLength = 15  
    override def log(msg: String) {
        super.log {
            if (msg.length <= maxLength) msg
            else msg.substring(0, maxLength - 3) + "..."
        }
    }
}
```
这样在混入该特质的实现类中,则自动包含这个字段。
```scala
val account = new SavingsAccount() with ConsoleLogger with TimeLogger with ShortLogger
println(account.maxLength)
```
输出为：
>15

下面再看看抽象字段的情况。
所谓抽象字段，就是在trait中只声明字段，不进行赋值。这种情况，赋值必须在类中完成。
比如下面的Logger trait，增加了一个叫abstractField的field，它没有被赋值。
```scala
trait Logger {
    def log(msg: String)
    val abstractField: Int 
}
```
这样必须在实现类中对该字段赋值，否则会报编译错误。
这里还有一个坑，必须在字段前加上lazy关键字，否则会报父类初始化出错的问题。
```scala
class ConsoleLoggerObject extends Logger {
    lazy val abstractField = 0
    def log(msg: String) {
        println(msg)
    }
}
```
除了加上lazy关键字，还有一种比较奇怪的语法也可以解决这个问题：在类的extends和with关键字间初始化变量。
```scala
class ConsoleLogger2 extends {
    val abstractField = 0
} with Logger {
    def log(msg: String) {  
        println(msg)
    }
}
```
## Trait的构造顺序
trait的构造顺序规则如下：
1.  首先调用超类构造器
1.  特质构造器在超类构造器之后，类构造器之前进行
1.  特质由左到右被构造
1.  每个特质中，父特质先被构造
1.  如果父特质已经被构造，则不会重复构造
1.  所有特质构造完毕，子类被构造

这样对于下面的调用，
```scala
class SavingsAccount extends Account with FileLogger with ShortLogger
```
构造顺序如下：
1. Account (超类)
1. Logger（第一个特质的父特质）
1. FileLogger(第一个特质)
1. ShortLogger（第二个特质，它的父特质Logger已经被构造，则不会重复构造）
1. SavingsAccount (类)

trait和class的唯一区别就是trait是没有构造函数的。
```scala
    trait T1 {
        println(s" in T1: x = $x")  // x默认初始化为0
        val x = 1
        println(s" in T1: x = $x")
    }

    trait T2 {
        println(s" in T2: y = $y")  // y = null，会被认为是字符串类型
        val y = "T2"
        println(s" in T2: y = $y")
    }

    class Base12 {
        println(s" in Base12: b = $b")  // 字符串默认初始化为null
        val b = "Base12"
        println(s" in Base12: b = $b")
    }

    class C12 extends Base12 with T1 with T2 {
        println(s" in C12: c = $c")
        val c = "C12"
        println(s" in C12: c = $c")
    }

    println(s"Creating C12:")
    new C12
    println(s"After Creating C12")
```
输出结果为：
>Creating C12:
> in Base12: b = null
> in Base12: b = Base12
> in T1: x = 0
> in T1: x = 1
> in T2: y = null
> in T2: y = T2
> in C12: c = null
> in C12: c = C12
>After Creating C12

## 扩展自类的trait
前面的例子我们看到，trait可以继承自trait，同时，trait也可以继承自类。
比如下面的例子：
继承自Exception类以及log的trait
```scala
trait LoggerException extends Exception with Log {
    def log() {log(getMessage)}
}
```
然后可以混入这个trait：
```scala
abstract class unhappyException extends LoggerException {
    override def getMessage() ="error..."
}
```
