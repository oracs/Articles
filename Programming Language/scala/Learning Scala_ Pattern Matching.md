## Learning Scala: Pattern Matching

修订记录

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.9.19 | 丁一 | 初稿 |

[TOC]

### 引言
模式匹配是函数式编程语言的一个重要特质，作为面向对象和函数式编程混合体的Scala也毫无意外的支持这一特质。作为switch语句更好的替代者，Scala的模式匹配具有以下特征：
- match是Scala的表达式，它始终以值作为结果；
- Scala的备选项表达式永远不会掉到下一个case语句；
- 如果没有匹配的模式，会抛出MatchError异常。

### 模式匹配的种类
#### 通配匹配
通配符_匹配任意对象，包括类名、方法名、参数名等。
```scala
expr match {
    case BinOp(op, left, right) => println(expr + " is binary operation")
    case _ => println("other express")
}
如果不关注BinOp的参数，则可以用如下方式表达：
expr match {
    case BinOp(_, _, _) => println(expr + " is binary operation")
    case _ => println("other express")
}
```

#### 常量匹配
匹配数值、布尔、字符串等字面量。
```scala
def describe(x: Any) = x match {
    case 5 => "five"
    case true => "true"
    case "hello" => "hi"
    case Nil => "empty"
    case _ => "others"
}
```
如果调用为：
> println(describe(5))
> println(describe(true))
> println(describe("hello"))
> println(describe(Nil))
> println(describe(List(5)))

则输出：
> five
> true
> hi
> empty
> others

#### 变量匹配
通配符_虽然可以匹配任意对象，但如果=>后面表达式需要使用匹配的内容，则无力表达，这时候就可以使用变量匹配，即起个变量名。
```scala
    def varMatch(expr: Any) = expr match {
        case 0 => println("0")
        case other => println("non-zero expr: " + other)
    }
```
如果调用为：
> varMatch(0)
> varMatch("abc")

则输出：
> 0
> non-zero expr: abc

<font color="red">**注意：**</font>
这里会有一个变量和常量傻傻分不清楚的情况。
比如Math库中的两个常量E(2.71828)和PI(3.14159)，对于下面的代码：
```scala
import java.lang.Math._

E match {
    case PI => println("value=" + PI)
    case other => println("other=" + other)
}
```
则输出结果为：
> other=2.718281828459045

也即是说不会匹配到PI，因为系统会认为PI是常量。
如果把PI改成变量pi，看看结果：
```scala
import java.lang.Math._

val pi = PI
E match {
    case pi => println("value=" + pi)
    case other => println("other=" + other)
}
```
输出结果为：
> value=2.718281828459045

也即表明匹配到了第一个表达式pi，系统认为它是个变量。
如果想匹配变量的源变量或者常量（听着有点绕口），可以在变量上加··符号。
```scala
import java.lang.Math._

val pi = PI
E match {
    case `pi` => println("value=" + pi)
    case other => println("other=" + other)
}
```
则输出结果为：
> other=2.718281828459045

这表示系统会认为pi的实际值为PI常量。

#### 类型匹配
可以按照类型进行匹配
```scala
def generalSize(x: Any) = x match {
    case s: String => s.length
    case m: Map[_, _] => m.size
    case _ => 1
}
```
如果调用为：
> println(generalSize("abc"))
> println(generalSize(Map("one" -> 1, "two" -> 2)))
> intln(generalSize(Math.PI))

则输出结果为：
> 3
> 2
> 1 

在Scala中，如果不使用模式匹配，判断一个对象的类型或者转换类型，需要这么写：
> if (x.isInstanceOf[String]) {
>     val s = x.asInstanceOf[String]
>     s.length
> else ...    

所以可以看到，使用Scala的模式匹配还是可以大大简化代码的！

这里还有一个坑就是关于“类型擦除的”！
Scala和Java一样，对于泛型在运行期是不会保存类型的，这样对于Map中就不能匹配具体的参数类型，比如
> case m: Map[String, Int] =>

这在编译时就会出错，只能使用通配参数类型
```
case m: Map[_, _] =>
```

擦除规则的例外就是**数组**，因为无论Scala还是Java都对数组做了特殊处理，数组元素类型得以在运行期得以保留。所以像下面的匹配是合法的：
> case a: Array[Int] =>

#### 正则表达式匹配
正则表达式匹配是字符串匹配的一种特殊情况，利用强大的正则表达式，可以使很多问题化繁为简。
```scala
 val BookExtractorRE = """Book: title=([^,]+),\s+author=(.+)""".r
 val MagazineExtractorRE = """Magazine: title=([^,]+),\s+issue=(.+)""".r
 val catalog = Seq(
     "Book: title=Programming Scala Second Edition, author=Dean Wampler",
     "Magazine: title=The New Yorker, issue=January 2014",
     "Unknown: text=Who put this here??"
 )
 for (item <- catalog) {
     item match {
         case BookExtractorRE(title, author) => //
             println(s"""Book "$title", written by $author""")
         case MagazineExtractorRE(title, issue) =>
             println(s"""Magazine "title", issue $issue""")
         case entry => println(s"Unrecognized entry: $entry")
     }
 }
```

#### 序列匹配(数组、列表、元组)
- **匹配数组**
```scala
 def arrayMatch(arr: Any) = arr match {
     case Array(0) => "0"
     case Array(x, y) => x+ ", " + y
     case Array(0, _*) => "0..."
     case _ => "others"
 }
```

如果调用为：
> println(arrayMatch(Array(0)))
> println(arrayMatch(Array(0, 1)))
> println(arrayMatch(Array(0, 1, 2)))
> println(arrayMatch(Array(1, 2, 3)))

则输出： 
> 0
> 0, 1
> 0...
> others

- **匹配列表**
```scala
 def listMatch(lst: Any) = lst match {
     case 0 :: Nil => "0"
     case x :: y :: Nil => x + ", " + y
     case 0 :: tail => "0..."
     case _ => "others"
 }
```
k
如果调用为：
> println(listMatch(List(0)))
> println(listMatch(List(0, 1)))
> println(listMatch(List(0, 1, 2)))
> println(listMatch(List(1, 2, 3)))

则输出： 
> 0
> 0, 1
> 0...
> others

另一个把列表转换成字符串的高级例子：
```scala
def listToString(lst: Any):String = lst match {
    case head :: tail => s"($head :: ${listToString(tail)})"
    case Nil => "(Nil)"
}
```
如果调用为：
> println(listToString(List(1, 2, 3, 4, 5)))

则输出为：
> (1 :: (2 :: (3 :: (4 :: (5 :: (Nil))))))

- **匹配元组**
```scala
def tupleMatch(langs: Seq[(String, String, String)]) = {
        for (tuple <- langs) {
            tuple match {
                case ("Scala", _, _) => println("Found Scala")
                case (lang, first, last) =>
                    println(s"Found other language: $lang ($first, $last)")
            }
        }
    }
```
如果调用为：
```scala
 val langs = Seq(
     ("Scala", "Martin", "Odersky"),
     ("Clojure", "Rich", "Hickey"),
     ("Lisp", "John", "McCarthy"))
 
 tupleMatch(langs)
```
则输出为：
> Found Scala
> Found other language: Clojure (Rich, Hickey)
> Found other language: Lisp (John, McCarthy)

#### 样例类匹配
如果要匹配的数据具有相关性，可以放入到样例类中(case classes)，样例类的好处是不用使用new，直接用类名就可以实例化。可以认为Sample class是一个不可变的值对象，并且还会自动加上equals, hashCode，toString等内置方法。
```scala
case class Address(street: String, city: String, country: String)
case class Person(name: String, age: Int, address: Address)

val alice = Person("Alice", 25, Address("1 Scala Lane", "Chicago", "USA"))
val bob = Person("Bob", 29, Address("2 Java Ave.", "Miami", "USA"))
val charlie = Person("Charlie", 32, Address("3 Python Ct.", "Boston", "USA"))

for (person <- Seq(alice, bob, charlie)) {
	person match {
		case Person("Alice", 25, Address(_, "Chicago", _) => println("Hi Alice!")
		case Person("Bob", 29, Address("2 Java Ave.", "Miami", "USA")) =>
			println("Hi Bob!")
		case Person(name, age, _) =>
			println(s"Who are you, $age year-old person named $name?")
	}
}
```
输出结果为：
> Hi Alice!
> Hi Bob!
> Who are you, 32 year-old person named Charlie?

如果已经很明确要实现的类型种类，可以在父类中加上sealed关键字，这样可以在编译时检查实现的类型是否完整。
```scala
case class Connect(body: String) extends HttpMethod
case class Delete(body: String) extends HttpMethod
case class Get(body: String) extends HttpMethod
case class Head(body: String) extends HttpMethod
case class Options(body: String) extends HttpMethod
case class Post(body: String) extends HttpMethod
case class Put(body: String) extends HttpMethod
case class Trace(body: String) extends HttpMethod

def handle(method: HttpMethod) = method match {
    case Connect(body) => s"connect: (length: ${method.bodyLength}) $body"
    case Delete(body) => s"delete: (length: ${method.bodyLength}) $body"
    case Get(body) => s"get: (length: ${method.bodyLength}) $body"
    case Head(body) => s"head: (length: ${method.bodyLength}) $body"
    case Options(body) => s"options: (length: ${method.bodyLength}) $body"
    case Post(body) => s"post: (length: ${method.bodyLength}) $body"
    case Put(body) => s"put: (length: ${method.bodyLength}) $body"
    case Trace(body) => s"trace: (length: ${method.bodyLength}) $body"
}

val methods = Seq(
    Connect("connect body..."),
    Delete("delete body..."),
    Get("get body..."),
    Head("head body..."),
    Options("options body..."),
    Post("post body..."),
    Put("put body..."),
    Trace("trace body..."))
```
输出结果为：
> connect: (length: 15) connect body...
> delete: (length: 14) delete body...
> get: (length: 11) get body...
> head: (length: 12) head body...
> options: (length: 15) options body...
> post: (length: 12) post body...
> put: (length: 11) put body...
> trace: (length: 13) trace body...

### 模式匹配的其他用法
#### 变量绑定
匹配的表达式可以用变量来替换，相当于别名，方便后续访问，格式为 var @ expr
```scala
 case class WhereOp[T](columnName: String, op: Op, value: T)
 case class WhereIn[T](columnName: String, val1: T, vals: T*)

 val wheres = Seq(
         WhereIn("state", "IL", "CA", "VA"),
         WhereOp("state", EQ, "IL"),
         WhereOp("name", EQ, "Buck Trends"),
         WhereOp("age", GT, 29))

 for (where <- wheres) {
     where match {
         case WhereIn(col, val1, vals @ _*) =>
             val valStr = (val1 +: vals).mkString(", ")
             println (s"WHERE $col IN ($valStr)")
         case WhereOp(col, op, value) => println (s"WHERE $col $op $value")
         case _ => println (s"ERROR: Unknown expression: $where")
     }
 }
```
输出为：
> WHERE state IN (IL, CA, VA)
> WHERE state = IL
> WHERE name = Buck Trends
> WHERE age > 29


#### 守卫
匹配表达式中还可以使用if语句来限定条件。
```scala
 case class Person(name: String, age: Int)

 def older(p: Person): Option[String] = p match {
     case Person(name, age) if age > 10 => Some(name)
     case _ => None
 }
```

#### 匹配嵌套结构
scala模式匹配的强大之处还在于能嵌套匹配嵌套结构：
```scala
 abstract class Item
 case class Articles(description: String, price: Double) extends Item
 case class Bundles(description: String, discount: Double, items: Item*) extends Item

 def price(it: Item): Double = it match {
     case Articles(_, p) => p
     case Bundles(_, disc, its @ _*) => its.map(price _).sum - disc
 }

 val bundles = Bundles("Bundle1", 20.0,
     Articles("scala", 39.9),
     Bundles("Bundle2", 10.0,
         Articles("java", 23.4),
         Articles("python", 12.3)))
```


#### Option类型
Java语言让人诟病的一个地方就是代码中充斥着大量的null以及各种空指针异常，在Scala中，可以利用Option类型很好的解决这个问题。如果有值的情况，会返回Some(x)，如果为空，会返回None。
如果一个结果可能返回空，则可以利用Option类型通过模式匹配来进行不同的处理。
```scala
def show(x: Option[String]) = x match {
    case Some(s) => s
    case None => "?"
}

val capitals = Map("China" -> "beijing", "Japan" -> "tokyo")
println(show(capitals.get("China")))
println(show(capitals.get("Korea")))
```
输出结果为：
> beijing
> ?






