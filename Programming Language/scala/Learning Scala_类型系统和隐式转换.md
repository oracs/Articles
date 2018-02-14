# Learning Scala: 类型系统和隐式转换

修订记录

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.12.15 | 丁一 | 初稿 |

[TOC]

## 型变
Scala的型变(Variant)一般用于类/特质的类型声明中，它分为两种：
### 协变
协变(Covariant)一般用+T表示，表示与T的变化方向相同，常用于输出参数类型，
### 逆变
逆变(Contravariant)一般用-T表示，表示与T的变化方向相反，常用于输入参数类型。
从上面的规则可以看出，协变常用于函数的输出参数，也就是生产者的角色。而逆变常用于函数的输入参数，充当消费者的角色。
举个例子：

```scala
trait Queue[+T] {
	def head: T
    def tail: Queue[T]
}

trait OutputChannel[-T] {
	def write(x: T)
}

trait Function1[-S, +T] {
	def apply(x: S): T
}
```

## Bound
Scala的Bound有很多种，常用的有以下几类：
### 上界和下界
对于类型C[Comparable]，Java和Scala都认为C[String]并不是C[Comparable]的子类型，所以并不能用C[String]来替代原有类型。如果想要实现这一合理的请求该怎么办？Scala提供了称为上下界的表示法。
上界(upper bounds)：用T >: UpperBound表示；
下界(lower bounds)：用T <: LowerBound表示。
一个下界例子：
```scala
class Pair[T <: Comparable[T]](val first: T, val second: T) {
    def smaller = if (first.compareTo(second) < 0) first else second
```
这样实例化Pair的时候，可以传入String类型，因为它实现了Comparable接口。

一个上届的例子：
```scala
class Pair[T](val first: T, val second: T) {    
    def replaceFirst[R >: T](newFirst: R) = new Pair(newFirst, second)
}
```
这样，如果T是Student的话，可以用Pair[Person]来替换。

### 视图界定
视图界定(view bounds)一般用T <% ViewBound来表示，表示T可以隐式转换成ViewBound。
因为Scala的Int类型并没有实现Comparable接口，所以用new Pair(3,4)会报错。但是RichInt实现了Comparable接口，所以可以用视图界定，让Int隐式转成RichInt。
```scala
class Pair[T <% Ordered[T]](val first: T, val second: T) {
    def smaller = if (first < second) first else second
} 
```
### 上下文界定
上下文界定(Context Bounds)一般用T:M表示，表示存在一个类型为M[T]的隐式值用在方法中。
```scala
class Pair[T : Ordering](val first: T, val second: T) {
    def smaller(implicit ord: Ordering[T]) =
        if (ord.compare(first, second) < 0) first else second
}
```
存在一个类型为Ordering[T]的隐式值，该隐式值可以用在方法中，需要加上隐式参数。

## 高级类型
Scala的类型系统可谓五花八门，下面介绍几种常见的类型。
### 单例对象
在Builder模式中，我们一般用this返回当前实例。但如果在继承的场景中，返回this就会出错，因为这时它返回的是父类的实例。
那怎么解决呢？Scala中，可以用this.type来处理。
```scala
class Document {
    def setTitle(title: String):this.type = { println(s"title=$title"); this }
    def setAuthor(author: String):this.type = { println(s"author=$author"); this }
}

class Book extends Document {
    def addChapter(chapter: String) = { println(s"chapter=$chapter"); this }
}
```

### 类型投影
类型投影一般用于嵌套类的场景，让内部类可以适用于其他外部类实例，而非自己声明的外部类实例中。
```scala
class Network {
    class Member(val name: String) {
        val contacts = new ArrayBuffer[Network#Member]
    }
}
```
这样Member可以包含在任意的Network中，而不是仅在自己的Network中。

### 类型别名
我们可以给很长的类型起一个别名，用于简化类型的引入。
```scala
class Book {
    import scala.collection.mutable._
    type Index = HashMap[String, (Int, Int)]
}
```
这样，可以用Book.Index取代冗长的HashMap类型。注意:type只能嵌套在类和对象内部，不能放在最外层。

### 结构类型
结构类型类似于动态语言中的鸭子类型。
```scala
    def appendLines(target: { def append(str: String): Any },
                    lines: Iterable[String]) {
        for (l <- lines) { target.append(l); target.append("\n") }
    }
```
这样只要是带有append方法的的类都可以使用，这比使用trait更灵活。

### 复合类型
复合类型通过with关键字把多个类型连接在一起。
```scala
val image = new ArrayBuffer[java.awt.Shape with java.io.Serializable]
```
表示image具备Shape和Serializable两种类型。

### 自身类型
自身类型一般用this:class=>表示，表示特质只能混入到指定类型的子类中。
```scala
trait LoggedException extends Logged {
    this: Exception =>
    def log() { log(getMessage()) }
}
```
如果用一个非Exception的class混入LoggedException会报错：
比如：
> val f = new JFrame with LoggedException // error

### 抽象类型
一般在类或特质中定义了抽象类型，需要在子类中实现。
```scala
trait Reader {
    type Contents
    def read(fileName: String): Contents
}

class StringReader extends Reader {
    type Contents = String
    def read(fileName: String) = Source.fromFile(fileName, "UTF-8").mkString
}

class ImageReader extends Reader {
    type Contents = BufferedImage
    def read(fileName: String) = ImageIO.read(new File(fileName))
}
```
类型Contents是抽象的，在其子类StringReader和ImageReader中实现。

## 抽象val
先看一个例子：
```scala
abstract class Fruit {
    val v: String
    def m: String
}

abstract class Apple extends Fruit {
    val v: String
    // OK, 可以通过val重载def方法
    val m: String
}

abstract class BadApple extends Fruit {
    // ERROR: 不能用def重载val字段
//    def v: String
    def m: String
}
```
从上面的例子得出结论：可以在子类中通过val重载def方法，但不能用def重载val字段。

## 隐式转换
隐式转换是Scala一项很强大的功能，它可以构建出类似于动态语言的特性效果。一般是在方法或者类前加上implicit关键字，表示方法或类支持隐式转换。
隐式转换满足下列规则：
1.表达式类型与预期类型不相同时。
```scala
implicit def int2Fraction(n: Int) = Fraction(n, 1)
val result = 3 * Fraction(4, 5)
```
由于上面的表达式是Fraction类型的乘法，但左侧是Int类型，和预期不匹配，Scala便会去找是否存在int to Fraction的隐式转换？正好我们提前声明了一个隐式转换方法，这样便会把3转成Faction类型

2 当对象访问一个不存在的成员时
```scala
implicit def file2RichFile(from: File) = new RichFile(from)
new File("test.txt").read
```
由于File对象本身是没有read方法的，所以Scala会去找是否存在隐式转换。正好找到前面声明的file2RichFile()方法，把File转成了RichFile，RichFile是支持read方法的。

3 当调用方法，参数类型不匹配时
```scala
implicit def fraction2Double(f: Fraction) = f.num * 1.0 / f.den
val result2 = 3 * Fraction(4, 5)
```
这个隐式转换是要转成Int类型来适配，所以定义了fraction2Double的转换方法。

## 隐式参数
隐式参数一般出现在方法参数中，参数名前加上implicit关键字。通过隐式参数，也可以简化方法参数的调用。
看一个例子：
```scala
case class Delimiters(left: String, right: String)
def quote(what: String)(implicit delims: Delimiters) =
    delims.left + what + delims.right
```
按照一般的方法调用，我们写成下面的表达式肯定可以。
> val text = quote("Bonjour le monde")(Delimiters("?", "?"))

但既然方法中已经声明了隐式参数，一定能简化调用。
我们声明一个object，内部一个方法返回Delimiters对象。
```scala
object FrenchPunctuation {
    implicit val quoteDelimiters = Delimiters("?", "?")
}
```
然后，再import这个object，
```scala
import implicits.FrenchPunctuation._
```
就可以用下面的方式调用了：
```scala
val text2 = quote("Bonjour le monde")
```

隐式参数也需要遵循一定原则：
1.只有最后一个参数列表允许出现隐式参数，这也适用于只有一个参数列表的情况。
2.implicit关键字只允许出现在参数列表的最左边，而且只能出现一次，
3.假设参数列表以implicit关键字开头，那么所有参数都是隐式的。
下面是一个例子：
```scala
    class ImplicitRules{
//        def bad1(i: Int, implicit s: String) = "boo"
//        def bad2(i: Int)(implicit s: String)(implicit d: String) = "boo"
        def good1(i: Int)(implicit s: String, d: String) = "boo"
        def good4(implicit i: Int, s: String, d: String) = "boo"
    }
```
