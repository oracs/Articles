# Scala面向对象编程

修订记录

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.12.12 | 丁一 | 初稿 |

[TOC]


## 类
Scala的类定义看起来和Java很像：
```scala
class Counter {
    var value = 0
    def increment() {
        value += 2
    }
}    
```
但还是有细微差异：

- 字段声明前必须有val/var限定，表示变量是否不可变/可变。
- 方法的声明使用def关键字。
- 类中的成员（字段、方法）默认访问权限是public的，除非显示声明为private或者protected。

另外在Scala中，对象比较使用 == 操作符，相当于Java中的Equals比较。如果按引用比较，使用eq函数，不等使用ne函数。

## 字段
Scala给每个字段都提供了setter和getter方法。
规则：
- 如果字段是私有的，则setter和getter方法也是私有的。
- 如果字段是var的，则会生成getter和setter方法
- 如果字段是val的，则只生成getter方法
- 如果不需要setter和getter方法，则字段声明为private[this], 成为对象私有字段。

一个例子：
```scala
class Counter {
    var count = 1
    private var value = 0
    private[this] var value2 = 0

    def increment() {
        value += 2
    }

    def current = value

    def isLess(other : Counter) = value < other.value
}

object CounterClient extends App {
    val counter = new Counter
    println(counter.count)
    counter.count = 2
    println(counter.count)

    counter.increment()
    println(counter.current)
}
```
从上面的例子可以看出，声明的var count字段，客户端可以直接调用，进行访问和修改操作。
>println(counter.count)
>counter.count = 2
>println(counter.count)
>1
>2

对于private var value = 0，这个变量，由于是内部私有可变变量，所以外部不可见，只能在内部使用。
对于private[this] var value2 = 0，这个变量，它设成了对象私有变量，这样对于方法isLess()来说，other参数虽然类型是Counter，但是它也看不到value2变量，只能看到value变量。

## 方法
Scala中定义方法和Java有些不一样，它用def关键字声明方法，参数是参数名:类型名的方式。return关键字也不是必须的，它会自动以最后一行的返回结果作为返回值。
```scala
    class ChecksumAccumulator {
        var i = 0
        private var sum = 0

        def add(b: Byte): Unit = {
            sum += b
        }

        def checksum(): Int = {
            ~(sum & 0xFF) + 1 
        }
    }
```
Scala很擅长类型推导，所以方法的返回类型不是必须的，
比如上面的checksum()方法，可以省略掉返回类型，写成：
```scala
        def checksum() = {
            ~(sum & 0xFF) + 1
        }
```
如果方法体是一行代码就可以搞定，Scala还可以简化的更彻底，省略{}。像下面的样子：
```scala
def checksum() = ~(sum & 0xFF) + 1
```

如果返回类型是Unit（相当于Java的void），则Scala可以省略 :Unit= , 后面的语句用大括号包裹。
```scala
def add(b: Byte) {
    sum += b
}
```
但一旦写成这种写法，即使方法最后一行有返回结果，方法也会被认为返回Unit。
```scala
def greet() { 
    "hello"
}
```
执行完greet()方法，依然返回空。

在Scala中，还有一类无参数的方法比较特殊。对于这类方法，Scala允许在访问的时候，去掉方法中的()，看起来就像：
> accu.greet

这在Scala中叫统一访问原则，也就是说Scala把无参方法和val/var field当成一样的成员来访问。这样就不会出现像Java中下面的代码：同一个lenth，既要用length，也要用length()，傻傻分不清楚。 
> "aa".length();
> new int[3].length


## 构造函数
### 主构造函数
Scala是一门简洁的语言，所以构造函数在声明类的时候就可以直接指定，而不再像Java放在同类名的方法中。
```scala
class Person(val name: String, val age: Int) {
    println("name=" + name + ", age=" + age)

    override def toString = getClass.getName + ", name=" + name
}
```
实例化的时候和Java一样，都用new
>new Person("dy", 30)

短短几行，就完成了一个Java Bean功能。干的漂亮。
这也是很多Scala入门书第一个要介绍的例子，好像总的黑Java那么两下。
虽然Scala是一门简洁的语言，但它也是一门复杂的语言。
这种方式的构造函数叫做主构造函数。
主构造函数中的field前缀可以是val,var, private, override...，看完这些组合，你可能又要吐血了...
所以需要总结下：

| 主构造函数参数 | 生成的字段/方法 |
|--------|--------|
| name:String       | 对象私有字段       |
| val/var name:String   | 私有字段, 公有的getter/setter方法 |
| private val/var name:String   | 私有字段, 私有的getter/setter方法       |
| override val/var name:String   | 覆写父类的同名字段, 共有的getter/setter方法       |

从上面的表格，可以看出，除了name:String这种方式不会生成方法外，其他的方式Scala都会自动生成getter/setter方法，这样就省去了如Java般的样板代码。
当然你可能会有一个疑问，如果构造函数和类声明放在一起，那么如果构造函数中的构造体放在哪里呢？
实际上，在Scala的类中，只要不是声明field和method的地方，都是主构造函数的执行体。
比如上面例子中的
> println("name=" + name + ", age=" + age)

就是构造函数中的一条语句。

### 默认值
主构造器可以使用默认参数，比如上面的Person类，可以给参数指定默认值，比如下面的代码：
```scala
class Person(val name: String, val age: Int=30) {
    println("name=" + name + ", age=" + age)

    override def toString = getClass.getName + ", name=" + name
}
```
这样在实例化的时候，可以用下面的方式声明：
> new Person("dy")

和new Person("dy", 30)的效果是一样的，但是比Java又做了简化。

### 辅助构造函数
介绍完主构造函数，聪明的你一定想到了肯定还有辅助构造函数。
它是用this()关键字进行调用。内部一定会调用主构造函数或者其他辅助构造函数。
举个例子：
```scala
case class Address(street: String, city: String, state: String, zip: String) {
    def this(zip: String) =
        this("[unknown]", Address.zipToCity(zip), Address.zipToState(zip), zip)
}

object Address {
    def zipToCity(zip: String) = "Anytown"

    def zipToState(zip: String) = "CA"
}

case class Person(name: String, age: Option[Int], address: Option[Address]) {
    def this(name: String) = this(name, None, None)

    def this(name: String, age: Int) = this(name, Some(age), None)

    def this(name: String, age: Int, address: Address) =
        this(name, Some(age), Some(address))

    def this(name: String, address: Address) = this(name, None, Some(address))
}
```
它在Person类中提供了一系列的辅助构造函数。
调用：
```scala
val a1 = new Address("1 Scala Lane", "Anytown", "CA", "98765")
val a2 = new Address("98765")
new Person("Buck Trends1")
new Person("Buck Trends2", Some(20), Some(a1))
new Person("Buck Trends3", 20, a2)
new Person("Buck Trends4", 20)
```

### 调用父类构造函数
Scala中继承父类使用extends关键字，这时我们就可以在父类中传入参数了。
```scala
case class Person(name: String,
                  age: Option[Int] = None,
                  address: Option[Address] = None)

class Employee(name: String,
               age: Option[Int] = None,
               address: Option[Address] = None,
               val title: String = "[unknown]",
               val manager: Option[Employee] = None) extends Person(name, age, address) {
    override def toString =
        s"Employee($name, $age, $address, $title, $manager)"
}
```
调用：
```scala
val a1 = new Address ("1 Scala Lane", "Anytown", "CA", "98765")
val a2 = new Address ("98765")
val ceo = new Employee ("Joe CEO", title = "CEO")
new Employee ("Buck Trends1")
new Employee ("Buck Trends2", Some (20), Some (a1) )
new Employee ("Buck Trends3", Some (20), Some (a1), "Zombie Dev")
```

### 私有构造函数
最后再介绍一类特别的构造函数：私有构造函数。
比如我们在Java中声明单例类的时候，喜欢把构造函数设成私有的。
Scala提供了下面的语法：
>class Person private(val id: Int) { ... }


## 伴生对象
介绍完类，你一定好奇，怎么没看到静态方法？ 在Scala中，新增了一个叫object的对象，它用关键字object声明。
object一定是单例的，它提供的方法都是静态方法。
如果object和class放在同一个文件中，且是同名，那么object就是class的伴生对象。
当然也可以和类不同名，也不放在一起，这时候就是独立对象，仍然是单例，一般用作工具类。
一个伴生对象的例子：
```scala
    class ChecksumAccumulator {
        var i = 0
        private var sum = 0

        def add(b: Byte): Unit = {
            sum += b
        }

        def checksum(): Int = {
            ~(sum & 0xFF) + 1 
        }
    }
    
    object ChecksumAccumulator {
        private val cache = mutable.Map.empty[String, Int]

        def calculate(s: String): Int =
            if (cache.contains(s))
                cache(s)
            else {
                val acc = new ChecksumAccumulator
                for (c <- s)
                    acc.add(c.toByte)
                val cs = acc.checksum()
                cache += (s -> cs)
                cs
            }
    }
    
    ChecksumAccumulator.calculate("hello,world")
```
从中可以看出，calculate方法是放在ChecksumAccumulator类的伴生对象中提供的。

### 工厂对象
object还可以用来提供工厂方法。比如下面的例子：
```scala
object Element {
    def elem(contents: Array[String]): Element =
        new ArrayElement(contents)
    def elem(chr: Char, width: Int, height: Int): Element =
        new UniformElement(chr, width, height)
    def elem(line: String): Element =
        new LineElement(line)
}
```
提供了三种实例化Elemen的方法。

### apply方法
很喜欢Scala中集合的声明方式。
>List(1,2,3)

就完成了一个LIst从new到赋值的过程。那么它内部是怎么做到的呢？其实就是实现了object List的apply方法。
实现了apply方法，Scala就支持通过class(p..)的方式创建对象了。 
一个例子：
```scala
object Person {
    def apply(name: String): Person = new Person(name)
    def apply(name: String, age: Int): Person= new Person(name, Some(age))
    def apply(name: String, age: Int, address: Address): Person =
        new Person(name, Some(age), Some(address))
    def apply(name: String, address: Address): Person =
        new Person(name, address = Some(address))
}
```
调用：
```scala
Person("Buck Trends1") 
Person("Buck Trends2", Some(20), Some(a1))
Person("Buck Trends3", 20, a1)
```

### main方法
在Java中，main()方法是程序调用的入口，那么Scala的main函数在哪里呢？其实它就在object中。
```scala
object Person {
    def main(args: Array[String]) {
        println(new Person("Buck Trends1"))
    }
}
```
当然，Scala还提供了一种更简洁的方式，就是让object混入App trait，这样就可以无需声明main方法，直接写方法体了。
```scala
object Client extends App {
	println(new Person("Buck Trends1"))
}
```

## 继承
scala的继承依然使用extends关键字，extends不一定是class，也可以是trait。
如果不写extends，则默认从AnyRef继承。
```scala
class Person(val name: String, val age: Int=30) {
    println("name=" + name + ", age=" + age)

    override def toString = getClass.getName + ", name=" + name
}

class Employee(name: String, age: Int, val salary: Double)
        extends Person (name, age) {

    override def toString = super.toString + ", salary=" + salary
}

object Employee {
    def main(args: Array[String]) {
        val emp = new Employee("dy", 30, 1000)
        println(emp)
        if (emp.isInstanceOf[Person3]) {
            println(emp.getClass == classOf[Employee])
        }
    }
}
```


Scala中可以用字段重写无参数方法，重写规则：
- def只能重写def
- val可以重写val或者不带参数的def
- var重写另一个抽象的var

一个例子：
```scala
abstract class Person {
    def id: Int

    def greet(msg: String)
}

class Student(override val id: Int) extends Person4 {
    override def greet(msg: String) = println(msg)
}
```
可以看到，通过Student的构造函数，id把父类Person中的同名id方法重写了。

Scala的类型检查也和Java有所不同：

| scala | java |
|--------|--------|
| obj.isInstanceOf[C]   | obj.instanceOf(C) |
| obj.asInstanceOf[C]   | (C)obj |
| classOf[C]   | (C.class |

## 嵌套类
Scala是支持嵌套类的，即在class中再定义class。
一个例子：
```scala
class Network {
    class Member(val name: String) {
        val contacts = new ArrayBuffer[Member] 
    }
    private val members = new ArrayBuffer[Member]
    def join(name: String) = {
        val m = new Member(name)
        members += m
        m
    }
}

object Client extends App {
    val chatter = new Network
    val myFace = new Network

    val fred = chatter.join("Fred")
    val wilma = chatter.join("Wilma")

    fred.contacts += wilma // OK

    val barney = myFace.join("Barney")
}
```
在Network类的内部又定义了一个叫Member的类。
Network可以增加属于这个Network的Member。但是如果增加一个不属于这个Network的Member，是会报错的。
比如：
> fred.contacts += barney

要解决这个问题，需要使用类型投影技术，把Member类的contacts变量类型扩大到Network上。
像这样：
```scala
    class Member(val name: String) {
        val contacts = new ArrayBuffer[Network#Member]  
    }
```


## 枚举
Scala默认是没有枚举类型的，但可以继承Enumeration类达到同样的效果。
初始化成员的时候必须要使用Value。
一个例子：
```scala
object Colors extends Enumeration {
    val Red = Value(0, "stop")
    val Yellow = Value(10)
    val Green = Value("go")

    def main(args: Array[String]) {
        println(Colors.Yellow)

        for (c <- Colors.values) {
            println(c.id + "," + c)
        }
    }
}
```
如果觉得上面的Value用法有点繁琐，可以简化调用：
```scala
    type Colors = Value
    val Red, Yellow, Green = Value
```







