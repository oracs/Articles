修订记录

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.9.29 | 丁一 | 初稿 |

### 引言
我们知道，Scala是一门面向对象和函数式编程的混合语言。面向对象的编程思想随着Java、Ruby等语言的兴起已经深入人心了，但近些年，另一种编程流派--“函数式编程”也在悄然兴起。本文要介绍的就是函数式编程在Scala中的应用。

### 函数是“一等公民”
如同面向对象中经常提到的“万事万物皆对象”思想一样，函数是代码中的“一等公民”，函数既可以完成方法的调用，也可以作为参数和返回值在代码中传递，可谓"万事万物皆函数"。
#### 函数字面量
函数字面量(literal)也叫匿名函数或者Lambda，是一个匿名的代码块，比如：

> (i: Int) => {i * 2}

就是一个函数字面量。
可以把这个字面量赋给变量，然后执行函数调用，比如

> val doubler = (i: Int) => {i * 2}
> double(3)

结果为6。

#### 函数作为参数
函数可以作为参数来传递，这时传入函数的参数也是一个函数。比如下面这个例子：

```scala
  def operation(func: (Int, Int) => Int) = {
      println(func(4, 4))
  }
```
定义了一个支持通用操作的函数，再定义一些操作,比如加、减、乘、除，当做参数传入函数：

```scala
val add = (a: Int, b: Int) => a + b
operation(add) // 4
val subtract = (x: Int, y:Int) => { x - y }
operation(subtract) // 0
val multiply = (x: Int, y:Int) => { x * y }
operation(multiply) // 16
```

#### 函数作为返回值
函数还可以作为返回值，得到的结果，作为函数继续调用。比如：

```scala
def greeting() = (name: String) => {"hello" + " " + name}
val greet= greeting()
greet("world")  // hello world
```
#### 占位符语法
Scala的语法哲学有一项就是：简洁。可以通过占位符(&#95;)来简化函数的写法。
当参数只在表达式中出现一次的情况下，参数可以用&#95;代替。
##### 一个参数的例子：

```scala
List(1, 2, 3).filter(x => x > 2)
```
可以简写为：

```scala
List(1, 2, 3).filter(_ > 2)
```

##### 两个参数的例子：

```scala
List(1, 2, 3).reduce((x, y) => x + y)
```
可以简写为：

```scala
List(1, 2, 3).reduce(_ + _)
```

##### 如果从占位符无法推导出类型，需要指定类型：

```scala
val add = (x: Int, y: Int) => x + y
```
可以简写为：

```scala
val add = (_: Int) + (_: Int)
```

##### 如果参数在表达式中出现了两次，是无法用占位符替换的。比如

```scala
val doubler = (x: Int) => {x+x}
```
是不能替换为：

```scala
doubler(_ + _)
```

### 闭包
所谓闭包，就是一段函数以及它所引用的自由变量。
先看一段代码：

```scala
var more = 1
val addMore = (x: Int) => x + more
```
其中more就是**自由变量**，addMore所引用的函数就是**闭包**。
如果more在闭包创建之后发生了改变，闭包引用的自由变量值也会同时变化。
比如：

```scala
var more = 1
val addMore = (x: Int) => x + more
addMore(2)
more = 2
addMore(2)
```
最终的输出结果：4。
自由变量的值在函数体内部也可以改变。

```scala
  var more = 1
  val addMore = (x: Int) => {
      more = more - 1
      x + more
  }
```
这点，Scala和Java是不同的：Java中的Lambda表达式(闭包)的自由变量是不允许被改变的。而Scala中的自由变量是允许改变的，如上例所示，变量more的修饰符是var，是可以改变的。

### 高阶函数
所谓高阶函数，至少要满足下面的一个条件：

- 接受一个或多个函数作为入参
- 返回一个函数

本节主要介绍Scala集合库的三个常用高阶函数：map，filter，reduce。
#### map
map函数的主要作用是通过一个函数规则转成另一种数据格式。
一个例子：

```scala
(1 to 3).map(_ * 2)  // 1, 4, 6
```
表示把1..9序列中的每个元素 * 2。
_ &#42; 2就是采用占位符的简写格式，也可以用函数名来代替。

```scala
val doubler = (x: Int) => x * 2
(1 to 9).map(doubler)
```

#### filter
filter函数的主要作用是传入一个返回结果为Boolean值的条件表达式进行过滤。
一个例子：

```scala
(1 to 9).filter(_ % 2 == 0)
```
求1..9中的偶数。结果为：2,4,6,8

#### reduce
reduce函数主要进行归约操作，它有2个传入参数，一个参数是累计值，另一个参数是下个计算值。
一个例子：

```scala
(1 to 9).reduce(_ + _)
```
对1..9的序列求和，结果为：45

#### 组合在一起
除了单个使用每个函数外，还可以对这些函数进行流式接口调用。
一个例子：

```scala
(1 to 9).map(_ * 2).filter(_ % 2 == 0).reduce(_ + _)
```
对1..9的序列中的每个元素*2，再过滤出偶数，最后再求和。
结果为：90

除了以上介绍的三个函数外，集合库还有大量的高阶函数，可以关注另一篇介绍Scala集合的文章。

### 尾递归
递归是函数式编程中唯一能作用循环的方式。
先举一个利用递归计算阶乘的例子。

```scala
 def factorial(i: BigInt): BigInt = {
     if (i == 1) i
     else i * factorial(i - 1)
 }
 
 for (i <- 1 to 10)
    println(s"$i:\t${ factorial(i)}")
```
这段代码输出后，会返回1到10的阶乘。但这个递归调用有个问题：如果当i值很大的时候，会出现调用栈溢出的情况。 
那有没有方法解决呢？这就要用到尾递归。

所谓尾递归，就是递归调用是函数的最后一个(尾部)调用。

再看上面的例子，在factorial()自身调用后，还要和i进行乘积调用，所以不是尾递归调用。尾递归有一个好处，就是他可以让编译器优化成循环操作，变成循环操作后，就不会再存在栈溢出的风险了。JVM原生编译器是不支持尾递归优化的，但Scala提供的scalac编译指令可以做到。在你认为是尾递归嗲用的函数前加上@tailrec注解就可以了。 

改成尾递归的代码：

```scala
def factorial2(i: BigInt): BigInt = {
    @tailrec
    def fact(i: BigInt, accumulator: BigInt): BigInt = {
        if (i == 1) accumulator
        else fact(i - 1, i * accumulator)
    }
    
    fact(i, 1)
}

for (i <- 1 to 10)
    println(s"$i:\t${ factorial2(i
```
我们新增了一个内部函数fact，它新增了一个累计变量accumulator，用来缓存每次调用的执行结果。这样就把原有递归调用转换成了尾递归调用。

### 纯函数
所谓的纯函数简单来讲就是函数不能有副作用，保证引用透明。即函数本身不会修改参数的值也不会修改函数外的变量，无论执行多少次，同样的输入都会有同样的输出。
例如，下面三个函数

```scala
def add(x: Int, y: Int) = x + y

def clear(list: mutable.MutableList): Unit = {
  list.clear()
}

def random() = Random.nextInt()
```
以上代码中定义的三个函数，其中，
add()符合纯函数的定义；
clear()会清除传入的List的所有元素，所以不是纯函数；
random() 无法保证每次调用都产生同样的输入，所以也不是纯函数。

### 偏应用函数
如果用“函数名 _”的形式来进行声明，它就是一种**偏应用函数**。
比如：

```scala
def sum(a: Int, b: Int, c: Int) = a + b + c
val a = sum _
a(1, 2, 3) // 6
```
这里的占位符_并不代表一个参数，而是代表了全部参数列表。
所以调用方式可以为：a(1,2,3)。
当然也可以声明部分参数的实现，比如：

```scala
val b = sum(1, _: Int, 3)
b(2) // 6
```
所以当进行b(2)调用时，内部相当于转换成：a.apply(1, 2, 3)

#### 偏函数
偏函数和偏应用函数还有所不同，偏函数只接收某种类型的一个参数，通过case来对各种场景进行处理，如果匹配不上就抛出MatchError异常。
如下例子：

```scala
val inverse: PartialFunction[Double, Double] = {
        case d if d != 0.0 => 1.0 / d
    }
```
对于调用

> inverse(2.0)

结果为：

> 0.5

对于调用

> inverse(0.0)
则会抛出异常：

> scala.MatchError: 0.0 (of class java.lang.Double)
> at scala.PartialFunction$$anon$1.apply(PartialFunction.scala:248)
> at scala.PartialFunction$$anon$1.apply(PartialFunction.scala:246)
> ...

#### 可变参数
如果函数中的参数长度不固定，可以在参数的最后加上*号。
比如：

```scala
  def echo(args: String*) {
      for (elem <- args) {
          println(elem)
      }
  }
```
这样对于下列的调用都是支持的：

> echo("one")
> echo("a", "b", "c")

但如果参数的类型是数组的话，就不能直接调用。
比如对于下面的调用：

> val arrs = Array("x", "y")
> echo(arrs)

则会抛出异常：

> error: type mismatch

正确的调用方式是指定元素类型：_*
如下：

> echo(arrs: _*)


### 柯里化
柯里化这个名称是由逻辑学家Haskell Brooks Curry的名字命名得来的。它指的是将原来接受多个参数的函数变成接受一个参数的函数过程。
先看一个例子，对于下面的函数；

```scala
def mul(x: Int, y: Int) = x * y
```
根据柯里化的特性，等价于：

```scala
def mul(x: Int)(y: Int) = x * y
```
这样调用

> mul(2, 3)

就相当于

> mul(2)(3)

其内部的转换相当于

> def mul(x: Int) = (y: Int) => x * y

#### 柯里化转换
可以利用转换函数将多个参数的普通函数转成柯里化函数，也可以进行反向转换。
如下面的例子：

```scala
def cat(s1: String, s2: String) = s1 + s2
// 转换成柯里化函数
val catCurried = (cat _).curried
catCurried("hello")("world")
// 进行反向转换
val catUncurried = Function.uncurried(catCurried)
catUncurried("hello", " world")
```
#### 柯里化和偏应用函数
柯里化后的函数依然可以使用偏应用函数。
如下面的例子：

```scala
def multiplier(i: Int)(factor: Int) = i * factor
val byFive = multiplier(5) _
byFive(2) // 10
```

### 控制抽象
先介绍一个Scala的语法特性：
如果**只有一个**参数调用，可以用花括号{}代替圆括号()。
如下：

> println("hello, world.")

可以写成：

> println {"hello, world.}

如果有2个及以上参数调用的话，则不能使用花括号代替。
比如：

> val s = "hello, world."
> s.substring {1， 3}

调用时会报编译错误！

#### 传名参数
首先看一段代码：

```scala
  def myAssert(predicate: () => Boolean) = {
      if (predicate()) {
          println("pass")
      } else {
          println("failed.")
      }
  }
```
这段函数的调用如下：

> myAssert(() => 5 > 3)

虽然调用正确，但从使用者的角度看，总觉得哪里不对？
常见的单元测试工具，一般都写成：assertTrue(5>3)。
那Scala是不是也可以写成这样子呢？
也许在Java8中是不行的（其实本来不想黑Java的，但Java即使空参数也要保留括号的做法()->..实在让人无法同意更多）
Scala提供了一种叫做"**传名参数**"的特性来实现这个功能。
代码如下：

```scala
  def myAssert(predicate: => Boolean) = {
      if (predicate) {
          println("pass")
      } else {
          println("failed.")
      }
  }
```
好像有点找不同的感觉！
其实两段代码的差异，就在于第一行：

> predicate: => Boolean

去掉了括号！看似一点小改动，但却让返回类型发生了根本性改变。从原有的返回普通函数变成了传名函数。
传名函数的作用就是简化函数的调用，不用再写前面的括号了，oh, yeah~~~
这样函数的调用就是：

> myAssert(5 > 3)

再看一个更高级的例子：自定义一个until关键字。

```scala
def until(condition: => Boolean)(block: => Unit) {
    if (!condition) {
        block
        until(condition)(block)
    }
}
```
激动人心的时刻来了，可以这样使用until函数：

```scala
var x = 10
until (x == 0) {
    x -= 1
    println(x)
}
```
是不是和使用其他控制语句一样的感觉？看似不可思议的功能，Scala让一切变得简单、可能。

### 结语
虽然Scala不是纯粹的函数式编程语言，但函数式编程才是Scala的灵魂。也许从Java转到Scala的程序员一开始还是习惯使用面向对象编程，但渐渐地，他会被函数式编程飘逸俊美的特性所吸引，并使之发扬光大。