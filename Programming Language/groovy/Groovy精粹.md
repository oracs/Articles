# Groovy精粹

**修订记录**

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.11.16 | 丁一 | 初稿 |
| 2016.11.23 | 丁一 | 增加MOP和binding的内容 |

[TOC]

## 不一样的Groovy
Groovy是一门基于JVM平台的动态语言。它语法简单，学习成本低，和Java有很好的互操作性，并且还提供了元编程、DSL等高级特性，可以让我们尽情享受编程的乐趣。

没有比较就没有伤害，让我们先看看Groovy不一样的地方。

### 语句结尾不再强制加分号
Java语句结尾必须加分号，让很多人心烦，还好，Groovy取消了这个限制。

### 不需要强制声明类型k
不需要声明类型，这是对动态语言起码的尊重。
java中的如下方法：
> public String say(String words);

在Groovy中可以写为：
> def say(words)

### 调用方法时可以省略点和括号
这个特性在scala中也有，这样写出的代码就更接近自然语言了。
比如有下面这个方法调用：
> robot.move(left)

可以简化为：
> robot move left

看起来就和我们说话一样。

### 不需要强制return
这点和ruby/scala很像，return不是必须的。以最后一行语句的返回类型为准。
比如下面的调用：
```groovy
def greet(words) {
	"hello,$words" 
}
```
返回的结果就是最后一行组合出的字符串。

### 更甜的语法糖
很多时候发明一种语言很可能是想解决上一门语言中使用不爽的地方。Groovy的语法糖让苦涩的爪哇咖啡豆变得香甜起来。
对于Java的遍历操作：
```java
for (int i = 0; i < 10; i++) {
    println(i)
}
```
在Groovy中可以这么写：

- for (i in 1..9) println i
- (1..9).each {println it}
- 1.upto(9) {println it}
- 10.times {println it}

这只是糖山一角，更多语法特性等待着你去发掘。

### Groovy Bean
在Java中编写和使用Java Bean是一件很枯燥的事情，还好Groovy为我们做了简化。
```java
class Name {
    String firstname, lastname
}    
def name = new Name(firstname: "jack", lastname: "chen")
name.firstname
name.lastname = 'yang'
```
无论从声明、初始化到使用，都得到了极大的简化。

### 闭包
虽然Java8也引入了Lambda表达式，但闭包在groovy中使用的更纯粹。 

### 元编程
groovy中提供了针对MOP(Meta Object Protocal)的编程，主要提供了Reflection，Expando，Category，metaClass四种方式。
这样在运行时执行一个不存在的方法就不再是梦想了~~

### DSL
得益于groovy强大的语法特性，可以很容易构建出下面的dsl语句：
> move right by 3.meters at 5.km/h

并且Groovy还支持外部脚本的加载，这样看起来又很像在执行外部dsl，感觉groovy就是为dsl而生的一门语言。。。

以上介绍的这些特性会在后面的章节中详解介绍，下面让我们正式进入Groovy之旅。

## Type
### String
针对Java中原生的字符串，推荐使用单引号包裹，比如：
> String text = 'hello'

用双引号包裹的字符串，在Groovy中叫GString。它有个好处就是可以用 $变量名 的方式引用变量，这点又和Ruby很像。
下面是一个例子：
```groovy
def me = 'Tarzan'
def you = 'Jane'
def line = "me $me - you $you"
assert line == 'me Tarzan - you Jane'
```

如果要想使用大段的文本，可以使用"""（这点和python很像），比如：
```groovy
def year = 70
def sql = """
SELECT FROM MyTable
WHERE Year = $year
"""
assert sql == """
SELECT FROM MyTable
WHERE Year = 70
"""
```
下面是一些字符串的常用操作例子：
```groovy
String greeting = 'Hello Groovy!'
assert greeting.startsWith('Hello')
assert greeting.getAt(0) == 'H'
assert greeting[0] == 'H'
assert greeting.indexOf('Groovy') >= 0
assert greeting.contains('Groovy')
assert greeting[6..11] == 'Groovy'
assert 'Hi' + greeting - 'Hello' == 'Hi Groovy!'
assert greeting.count('o') == 3
assert 'x'.padLeft(3) == '  x'
assert 'x'.padRight(3,'_') == 'x__'
```
### Number
在Groovy中，everthing is object。（感觉每种语言都把Java不纯粹的OO黑了个遍）
这样数字也可以进行方法调用了。
下面是一些数字的操作例子：
```groovy
assert 1 == (-1).abs()
assert 2 == 2.5.toInteger() // conversion
assert 2 == 2.5 as Integer // enforced coercion
assert 2 == (int) 2.5 // cast
assert 3 == 2.5f.round()
assert 3.142 == Math.PI.round(3)
assert 4 == 4.5f.trunc()
assert 2.718 == Math.E.trunc(3)
assert '2.718'.isNumber() // String methods
assert 5 == '5'.toInteger()
assert 5 == '5' as Integer
assert 53 == (int) '5' // gotcha!
assert '6 times' == 6 + ' times' // Number + String
```

**注意：**
针对两个整数相除，groovy会返回浮点值结果。如果要求两个整数相除的除数，需要使用intdiv()方法：
```groovy
14.intdiv(3)  // 4
```

并且Groovy针对数字序列还提供了几个函数，可以很方便的进行遍历操作。
```groovy
def store = ''
10.times{
    store += 'x'
}
assert store == 'xxxxxxxxxx'
store = ''
1.upto(5) { number ->
    store += number
}
assert store == '12345'
store = ''
2.downto(-2) { number ->
    store += number + ' '
}
assert store == '2 1 0 -1 -2 '
store = ''
0.step(0.5, 0.1){ number ->
    store += number + ' '
}
assert store == '0 0.1 0.2 0.3 0.4 '
```
## Operator
groovy支持java中的所有操作符，本节只列举一些groovy特有的：

### Elvis操作符(?:)
对于下面的表达式：
> def value = argument ? argument : standard

可以简化成：
> def value = argument ?: standard

其中的?:就叫做Elvis操作符，不知道中文该怎么翻译。。
这样我们在判断数据不为空的时候，比较方便：
```groovy
String name = person.getName() ?: "Bob"```
```

### 安全导航(safe-navigation)操作符(?.)
?.操作符可以用来简化空指针处理：
比如下面的这行代码：
> if (str != null) { str.reverse() }

可以用下面的语句代替：
> str?.reverse()

效果是一样的，但更简洁。

```groovy
def foo(str) {
//if (str != null) { str.reverse() }
str?.reverse()
}
assert foo('evil') == 'live'
assert foo(null) == null
```
但这个操作符并不能替代对空指针应该做的异常处理。


### 方法指针操作符（.&）
用于将被调用的方法存入一个方法变量中，接着只要调用这个变量就相当于调用之前的那个方法。
例子：
```groovy
def list = [1, 2, 3]
def addit = list.&add
addit 4
assert list == [1, 2, 3, 4]
```
另一个复杂例子：
```groovy
class SizeFilter {
    Integer limit
    boolean sizeUpTo(String value) {
        return value.size() <= limit
    }
}
SizeFilter filter6 = new SizeFilter(limit:6)
SizeFilter filter5 = new SizeFilter(limit:5)
Closure sizeUpTo6 = filter6.&sizeUpTo
def words = ['long string', 'medium', 'short', 'tiny']
assert 'medium' == words.find (sizeUpTo6)
assert 'short' == words.find (filter5.&sizeUpTo)
```

### Spread操作符(*)
用于收集列表中的一些公共属性，然后将他们合成一个新的列表
例子：
```groovy
class Car {
    String make
    String model
}
def cars = [
       new Car(make: 'Peugeot', model: '508'),
       new Car(make: 'Renault', model: 'Clio')]
def makes = cars*.make
assert makes == ['Peugeot', 'Renault']
```

再看一个解包的例子，可以在集合前使用*：
```groovy
def range = (1..3)
assert [0,1,2,3] == [0,*range]
def map = [a:1,b:2]
assert [a:1, b:2, c:3] == [c:3, *:map]
```

### ==操作符
grovvy中的==操作符符就等于Java中的equals。这样一来就方便多了：
一些例子：
```groovy
str1 = 'hello'
str2 = str1
str3 = new String('hello')
str4 = 'Hello'
assert str1 == str2
assert str1 == str3
assert !(str1 == str4)
```
### is操作符
用于判断两个引用是否相同。相当于在java中的==。
```groovy
def list1 = ['Groovy 1.8','Groovy 2.0','Groovy 2.3']        
def list2 = ['Groovy 1.8','Groovy 2.0','Groovy 2.3']        
assert list1 == list2                                       
assert !list1.is(list2) 
```
### as操作符
用于不同类型的转换：
```groovy
Integer x = 123
String s = x as String
```
### 比较操作符（<==>）
比较操作符<=>，内部其实是调用了compareTo方法：
```groovy
assert (1 <=> 1) == 0
assert (1 <=> 2) == -1
assert (2 <=> 1) == 1
assert ('a' <=> 'z') == -1
```

### with操作符
with操作可以把上下文相关操作都放在with语句块内，省略了调用源的方法，看起来更加简洁。
比如这样一段代码：
```groovy
lst = [1, 2]
lst.add(3)
lst.add(4)
println lst.size()
println lst.contains(2)
```
虽然都是对list进行操作，但看起来有些杂乱无章。
使用with操作符后，代码变成这样：
```groovy
lst = [1, 2]
lst.with {
	add(3)
	add(4)
	println size()
	println contains(2)
}
```
感觉清爽多了。
with操作符还可以用在对象的初始化上。
比如有下面的实体类：
```groovy
class Address {
    String line1
    String line2
    String city
    String zipCode
    String country
}
```
我们可以用下面的方法初始化：
```groovy
def addr = new Address()
addr.with {
    line1 = '1st, Main Street'
    line2 = 'Suite 345'
    city = 'Metropolis'
    zipCode = '12345'
}
assert addr.line1 == '1st, Main Street'
```

### 操作符重载
Groovy中的操作符是可以重载的，利用这个特性，我们可以构造出很多方便的方法调用。
Groovy中重载操作符可以通过覆盖同名的操作符方法就可以了。
下面是groovy中的操作符及方法名：

| Operator | method |
|--------|--------|
|   a + b     |   a.plus(b)     |
|   a – b     |   a.minus(b)     |
|   a * b     |   a.multiply(b)     |
|   a ** b      |  a.power(b)      |
|   a / b     |   a.div(b)     |
|  a % b      |   a.mod(b)     |
|  a &#124; b      |  a.or(b)      |
|   a & b     |  a.and(b)      |
|  a ^ b      |  a.xor(b)      |
|  a++ or ++a      |   a.next()     |
|  a-- or --a      |  a.previous()      |
|  a[b]      |  a.getAt(b)      |
|  a[b] = c      |   a.putAt(b, c)     |
|  a `<<` b      |  a.leftShift(b)      |
|  a `>>` b      |  a.rightShift(b)      |
|  ~a      |  a.bitwiseNegate()      |
|  -a      |   a.negative()     |
|  +a      |   a.positive()     |

下面是一个例子：
```groovy
class Money {
    int amount
    String currency
    Money plus (Money other) {
        if (null == other) return this
        if (other.currency != currency) {
            throw new IllegalArgumentException(
                    "cannot add $other.currency to $currency")
        }
        return new Money(amount + other.amount, currency)
    }
}

Money buck = new Money(1, 'USD')
assert buck
assert buck == new Money(1, 'USD')
assert buck + buck == new Money(2, 'USD')
```

### 正则表达式
Groovy中的正则表达式操作符主要有下面几种：

- 匹配操作符：=~， 返回Matcher类型。
- 是否匹配操作符： ==~，返回Boolean类型。

正则表达式一般用/包裹，如：/regular/， 这样可以在正则式里面直接使用转义字符\

=~操作符的例子：
```groovy
def twister = 'she sells sea shells at the sea shore of seychelles'
assert twister =~ /s.a/
def finder = (twister =~ /s.a/)
assert finder instanceof java.util.regex.Matcher
```

==~操作符的例子：
```groovy
assert twister ==~ /(\w+ \w+)*/
def WORD = /\w+/
matches = (twister ==~ /($WORD $WORD)*/)
assert matches instanceof java.lang.Boolean
assert !(twister ==~ /s.e/)
```

遍历匹配
```groovy
def myFairStringy = 'The rain in Spain stays mainly in the plain!'
def wordEnding = /\w*ain/
def rhyme = /\b$wordEnding\b/
def found = ''
myFairStringy.eachMatch(rhyme) { match ->
    found += match + ' '
}
assert found == 'rain Spain plain '
found = ''
(myFairStringy =~ rhyme).each { match ->
    found += match + ' '
}
assert found == 'rain Spain plain '
```

匹配多个结果：
```groovy
def (a,b,c) = 'a b c' =~ /\S/
assert a == 'a'
assert b == 'b'
assert c == 'c'

def matcher = 'a:1 b:2 c:3' =~ /(\S+):(\S+)/
assert matcher.hasGroup()
assert matcher[0] == ['a:1', 'a', '1'] // 1st match
assert matcher[1][2] == '2' // 2nd match, 2nd group

def matcher2 = 'a:1 b:2 c:3' =~ /(\S+):(\S+)/
matcher2.each { full, key, value ->
    assert full.size() == 3
    assert key.size() == 1 // a,b,c
    assert value.size() == 1 // 1,2,3
}
```

## Collection
Groovy的集合分为三种List，Map，Range。和Python一样，数据结构简单，不像Scala让人看完集合库的设计有种想放弃的感觉。

### List
Groovy中的List声明和使用都非常简单
```groovy
List myList = [1, 2, 3]
assert myList.size() == 3
assert myList[0] == 1
assert myList instanceof ArrayList
```

集合的操作符：
```groovy
myList = ['a','b','c','d','e','f']
assert myList[0..2] == ['a','b','c']
assert myList[0,2,4] == ['a','c','e']
myList[0..2] = ['x','y','z']
assert myList == ['x','y','z','d','e','f']
myList[3..5] = []
assert myList == ['x','y','z']
myList[1..1] = [0, 1, 2]
assert myList == ['x', 0, 1, 2, 'z']

myList = []
myList += 'a'
assert myList == ['a']
myList += ['b','c']
assert myList == ['a','b','c']
myList = []
myList << 'a' << 'b'
assert myList == ['a','b']
assert myList - ['b'] == ['a']
assert myList * 2 == ['a','b','a','b']

myList = ['a', 'b', 'c']
assert myList.isCase('a')
assert 'b' in myList
```

下面是List的一些常用操作：
```groovy
assert [1,2].reverse() == [2,1]
assert [3,1,2].sort() == [1,2,3]
def list = [ [1,0], [0,1,2] ]
list = list.sort { item -> item.size() }
list = [1, 2, 3]
assert list.max() == 3
assert list.min() == 1

```
集合的高级操作：
```groovy
def list = [1, 2, 3]
def even = list.find { item ->
    item % 2 == 0
}
assert even == 2
assert list.every { item -> item < 5 }
assert list.any { item -> item < 2 }
def store = ''
list.each { item ->
    store += item
}
assert store == '123'
store = ''
list.reverseEach { item ->
    store += item
}
assert store == '321'
store = ''
list.eachWithIndex { item, index ->
    store += "$index:$item "
}
assert store == '0:1 1:2 2:3 '
assert list.join('-') == '1-2-3'
result = list.inject(0) { clinks, guests ->
    clinks + guests
}
assert result == 0 + 1 + 2 + 3
assert list.sum() == 6
factorial = list.inject(1) { fac, item ->
    fac * item
}
assert factorial == 1 * 1 * 2 * 3
```

### Map
Groovy中Map的声明和使用也很简单：
```groovy
def myMap = [a:1, b:2, c:3]
assert myMap instanceof LinkedHashMap
assert myMap.size() == 3
assert myMap['a'] == 1
assert myMap.a == 1
assert myMap.get('a') == 1
assert myMap.get('d',0) == 0
assert myMap.d == 0
myMap['d'] = 4
assert myMap.d == 4
```

多个map的比较和插入
```groovy
myMap = [a:1, b:2, c:3]
def other = [b:2, c:3, a:1]
assert myMap == other
assert [a:1] + [b:2] == [a:1, b:2]
def emptyMap = [:]
assert emptyMap.size() == 0
def explicitMap = new TreeMap()
explicitMap.putAll(myMap)
assert explicitMap['a'] == 1
def composed = [x:'y', *:myMap]
assert composed == [x:'y', a:1, b:2, c:3]
```

map的常用操作：
```groovy
myMap = [a:1, b:2, c:3]
myMap.clear()
assert myMap.isEmpty()
myMap = [a:1, b:2, c:3]
myMap.remove('a')
assert myMap.size() == 2
myMap = [a:1, b:2, c:3]
assert myMap.size() == 3
assert myMap.containsKey('a')
assert myMap.containsValue(1)
assert myMap.entrySet() instanceof Collection
assert myMap.any {entry -> entry.value > 2 }
assert myMap.every {entry -> entry.key < 'd'}
assert myMap.keySet() == ['a','b','c'] as Set
assert myMap.values().toList() == [1, 2, 3]
def abMap = myMap.subMap(['a', 'b'])
assert abMap.size() == 2
abMap = myMap.findAll { entry -> entry.value < 3 }
assert abMap.size() == 2
assert abMap.a == 1
def doubled = myMap.collect { entry -> entry.value *= 2 }
assert doubled instanceof List
```

Map的遍历操作
```groovy
myMap = [a:1, b:2, c:3]
def store = ''
myMap.each { entry ->
    store += entry.key
    store += entry.value
}
assert store == 'a1b2c3'

store = ''
myMap.each { key, value ->
    store += key
    store += value
}
assert store == 'a1b2c3'

store = ''
for (key in myMap.keySet()) {
    store += key
}
assert store == 'abc'

store = ''
for (value in myMap.values()) {
    store += value
}
assert store == '123'
```

### Range
Range是Groovy特有的一种数据类型，用来表示一组数据区间。
主要有下面几种表示方法：
```groovy
start..end // [start, end]
start..<end // [start, end)
```

下面是关于Ranged声明和使用的例子：
```groovy
assert (0..10).contains(0)
assert (0..10).contains(10)
assert (0..10).contains(-1) == false
assert (0..<10).contains(9)
assert (0..<10).contains(10) == false
def a = 0..10
assert a instanceof Range
assert a.contains(5)
a = new IntRange(0,10)
assert a.contains(5)
assert (0.0..1.0).contains(1.0)
assert (0.0..1.0).containsWithinBounds(0.5)
def today = new Date()
def yesterday = today - 1
assert (yesterday..today).size() == 2
assert ('a'..'c').contains('b')
```

在遍历中使用Range
```groovy
def log = ''
for (element in 5..9){
    log += element
}
assert log == '56789'

log = ''
for (element in 9..5){
    log += element
}
assert log == '98765'

log = ''
(9..<5).each { element ->
    log += element
}
assert log == '9876'
```

## Control Structure
### Groovy Truth
与Java通过布尔表达式来判断布尔结果不同，Groovy有自己的一套判断准则。
只要满足下面列出的条件，都会返回true。

| 类型 | 判断标准 |
|--------|--------|
| Boolean | true |
| Matcher | 有匹配结果 |
| Collection | 集合不为空 |
| Map | Map不为空 |
| String/GString | 字符串不为空 |
| Number, Character | 非0 |
| None of the above  | 对象非空 |

下面是一些例子：
```groovy
assert !false
assert ('a' =~ /./)
assert ![]
Iterator iter = [1].iterator()
assert iter
iter.next()
assert !iter
assert ![:]
assert 'a'
assert !''
assert 1
assert 1.2f
assert !0
assert ! null
assert new Object()
class AlwaysFalse {
    boolean asBoolean() { false }
}
assert ! new AlwaysFalse()
```

### 条件控制
Groovy中有两种条件控制语句，if和switch。
语法格式和java一样。只是条件值只要符合Groovy True，会为被认为返回true。
Groovy提供了更好用的switch语句，可以按照Range、列表、类型、闭包、正则式进行匹配，比原有Java匹配扩展了不少。
下面看一个例子：
```groovy
switch (10) {
    case 0 : assert false ; break
    case 0..9 : assert false ; break
    case [8,9,11] : assert false ; break
    case Float : assert false ; break
    case {it%3 == 0}: assert false ; break
    case ~/../ : assert true ; break
    default : assert false ; break
}
```

### 循环控制
除了可以使用Java原有的while, for等循环语句外，Groovy还对for循环提供了一种改进语法：
> for (variable in iterable) { body }

看起来和python的for循环很像，发明语言还是要多从其他语言吸收精华。。
一些for..in循环的例子：
```groovy
store = ''
for (i in [1, 2, 3]) {
    store += i
}
assert store == '123'

myString = 'Groovy range index'
store = ''
for (i in 0 ..< myString.size()) {
    store += myString[i]
}
assert store == myString
```

## OOP
### 类
groovy中的类定义和java一样，只是由于动态语言的特性，可以使用def定义以及省略类型声明。
看一个例子：
```groovy
class ClassWithTypedAndUntypedFieldsAndProperties {
    public fieldWithModifier
    String typedField
    def untypedField
    protected field1, field2, field3
    private assignedField = new Date()
    static classField

    public static final String CONSTA = 'a', CONSTB = 'b'
    def someMethod(){
        def localUntypedMethodVar = 1
        int localTypedMethodVar = 1
        def localVarWithoutAssignment, andAnotherOne
    }
}
```
注意：如果是单纯声明类，则文件名和类名需要保持一致。如果是要创建groovy脚本，则脚本中可以定义多个类，则文件名和类名不需要一致。

### 方法参数调用
由于是动态语言可以省略类型，所以对于同名且相同参数的方法，需要一个方法指定类型。这样groovy才能找到最合适和匹配。
```groovy
class ClassWithTypedAndUntypedMethodParams {
    static void main(args) {
        assert 'untyped' == method(1)
        assert 'typed' == method('whatever')
        assert 'two args' == method(1, 2)
    }
    static method(arg) {
        return 'untyped'
    }
    static method(String arg) {
        return 'typed'
    }
    static method(arg1, Number arg2) {
        return 'two args'
    }
}
```

groovy的方法参数可以指定默认值，可变参数，map映射参数等特性。
```groovy
class Summer {
    def sumWithDefaults(a, b, c=0){
        return a + b + c
    }
    def sumWithList(List args){
        return args.inject(0){sum,i -> sum += i}
    }
    def sumWithOptionals(a, b, Object[] optionals){
        return a + b + sumWithList(optionals.toList())
    }
    def sumNamed(Map args){
        ['a','b','c'].each{args.get(it,0)}
        return args.a + args.b + args.c
    }
}
def summer = new Summer()
assert 2 == summer.sumWithDefaults(1,1)
assert 3 == summer.sumWithDefaults(1,1,1)
assert 2 == summer.sumWithList([1,1])
assert 3 == summer.sumWithList([1,1,1])
assert 2 == summer.sumWithOptionals(1,1)
assert 3 == summer.sumWithOptionals(1,1,1)
assert 2 == summer.sumNamed(a:1, b:1)
assert 3 == summer.sumNamed(a:1, b:1, c:1)
assert 1 == summer.sumNamed(c:1)
```
注意：传入map参数的方式也叫： **Named Parameters**的方法调用。
它的方法第一个参数是一个Map类型，调用的时候可以按照name1:value1, name2:value2...的方式传入参数，这个位置顺序不受限制。
比如：
```groovy
def namedParamsMethod(params, param2, param3) {
	assert params.a == 1
	assert params.b == 2
	assert params.c == 3
	assert param2 == "param1"
	assert param3 == "param2"
}

namedParamsMethod("param1","param2",a:1, b:2, c:3)
```

除了使用Named Parameters，还可以使用@TupleConstructor注解，按照元组的方式注入参数。
比如有这样一个类：
```groovy
@TupleConstructor
class Speed {
    Number amount
    Unit unit
    String toString() { "$amount $unit/h" }
}
```
可以这样调用：
>def speed = new Speed(10, Unit.meter)
>assert "10 m/h" == speed.toString()

它的顺序和类中声明的顺序一致，如果换成
> new Speed(Unit.meter, 10)

则会报错。


### Groovy Bean
Groovy Bean是Groovy的一大特色，是为了简化Java Bean操作而提供的一种Bean对象，也叫POGO(plain-old Groovy objects)。
groovy bean的声明和使用都得到了极大的简化，下面是一个例子：
```groovy
class DoublerBean {
    public value
    void setValue(value){
        this.value = value
    }
    def getValue(){
        value * 2
    }
}
bean = new DoublerBean(value: 100)
assert 200 == bean.value
assert 100 == bean.@value
```

## Closure
闭包(Closure)说白了就是一段可以包含自由变量(可选)的代码块。在Java中也叫Lambda表达式。
闭包的好处是既可以执行方法，又可以在函数中作为参数传入和作为返回结果传出。

### 声明
在Groovy中声明一个闭包非常简单：
```groovy
def three = {
    println "three"
}
three()
```
这就定义了一个简单闭包。它没有参数。这比java即使没有参数也要保留()的方式不知道强到那里去了。
如果包含参数，可以这样定义：
> {var1, var2...} -> block

比如
```groovy
def greetString = {greeting ->
    return "hello, ${greeting}!"
}
greetString("world")
```
当仅有一个参数调用时，可以省略参数定义，方法体内用it代替。类似于scala的_
上面的代码就可以简化为：
```groovy
def greetString = {"hello, $it"}
println greetString("world")
```

### 闭包变量中的生命周期
```groovy
def Closure makeCounter() {
    def local_variable = 0
    return {return local_variable += 1}
}


def c1 = makeCounter()
c1()
c1()
def c2 = makeCounter()
println "c1=${c1()}, c2=${c2()}"
```
运行结果为：
> c1=3, c2=1

也就是说闭包实例会对属于它的变量进行生命周期管理。

### 调用方式
闭包一般采用下面几种方式调用：

- closure()
- closure.call()

一个例子：
```groovy
def adder = { x, y -> return x+y }
assert adder(4, 3) == 7
assert adder.call(2, 6) == 8
```
对于多个同名方法，groovy的闭包还可以做到运行时的重载调用，可以用来延迟加载。
```groovy
class MultiMethodSample {
    int mysteryMethod (String value) {
        return value.length()
    }
    int mysteryMethod (List list) {
        return list.size()
    }
    int mysteryMethod (int x, int y) {
        return x+y
    }
}
MultiMethodSample instance = new MultiMethodSample()
Closure multi = instance.&mysteryMethod
assert 10 == multi ('string arg')
assert 3 == multi (['list', 'of', 'values'])
assert 14 == multi (6, 8)
```
其中instance.&mysteryMethod中的&的操作符，有点像java的::方法引用操作符。

### 闭包作为参数传入
这个在集合操作中经常可以看到，
比如：
```groovy
def odd = [1,2,3].findAll{ item ->
    item % 2 == 1
}
```
再看一个例子：
```groovy
def tellFortune(closure) {
    closure new Date("09/20/2012"), "Your day is filled with ceremony"
}

tellFortune { date, fortune ->
    println "Fortune for ${date} is '${fortune}'"
}
```


### 闭包作为结果返回
```groovy
def foo(n) {
    return {n += it}
}
def accumulator = foo(1)
assert accumulator(2) == 3
```
### 实现接口
在原有java代码经常用匿名内部类完成接口实现，比如：
```groovy
button.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		JOptionPane.showMessageDialog(frame, "You clicked!");
	}
});
```
groovy中可以简化为：
```groovy
button.addActionListener(
	{ JOptionPane.showMessageDialog(frame, "You clicked!") } as ActionListener
)
```

### 柯里化
柯里化可以把形如method(x, y)的调用转换成method(x)(y)的形式。
举个例子：
```groovy
def mult = { x, y -> return x * y }
def twoTimes = mult.curry(2)
assert twoTimes(5) == 10
```

这样利用柯里化功能，就可以让代码调用得到简化：
```groovy
def configurator = { format, filter, line ->
    filter(line) ? format(line) : null
}
def formatter = { line -> "elite: $line" }
def debugFilter = { line -> line.contains('debug') }

def myConf = configurator.curry(formatter, debugFilter)
assert myConf('here is some debug message') == "elite: here is some debug message"
```
### delegate
一般情况下，闭包的delegate等于owner，就是调用闭包的所有者，但我们可以修改闭包的delegate以达到动态访问的效果。
一个例子：
```groovy
class Handler {
    def f1() { println "f1 of Handler called ..."}
    def f2() { println "f2 of Handler called ..."}
}
class Example {
    def f1() { println "f1 of Example called ..."}
    def f2() { println "f2 of Example called ..."}

    def foo(closure) {
        println closure.delegate
        closure.delegate = new Handler()
        closure()
    }
}
def f1() { println "f1 of Script called..." }

new Example().foo {
    f1()
    f2()
}
```
结果输出为：
>book.groovy.programming_groovy.ch04_closure.closure@2e222612
>f1 of Script called...
>f2 of Handler called ...

从结果可以看出，闭包内的方法先找owner的方法。如果没有的话，会找指定代理的对象中的方法，如果再找不到就会报错。
可以把closure.delegate = new Handler()这行去掉。执行后，会报下面的错误：
>groovy.lang.MissingMethodException: No signature of method: book.groovy.programming_groovy.ch04_closure.closure.f2() is applicable for argument types

## MOP
MOP的全称是Meta Object Protocal，也即是元编程。
元编程是Groovy非常强大的一项功能，它允许在运行时动态增加方法或者属性，这就给我们在某种场合使用带来很大便利。

### 执行不存在的方法和属性
Groovy所有的方法调用都会先触发到一个叫GroovyObject的接口行为上。
下面是这个接口的定义：
```groovy
public interface GroovyObject {
    Object invokeMethod(String var1, Object var2);
    Object getProperty(String var1);
    void setProperty(String var1, Object var2);
    MetaClass getMetaClass();
    void setMetaClass(MetaClass var1);
}
```
这个接口定义的非常简单，Groovy内置了许多实现这个接口的类，比如MetaClass，它的默认实现类是MetaClassImpl。很多动态特性都是利用这些元类完成的。

执行不存在的方法，需要重写methodMissing方法，
例子：
```groovy
class MethodPretender {
    def methodMissing(String name, Object args) {
        "called $name with $args"
    }
}
def methodPretender = new MethodPretender()
assert methodPretender.hello('world') == 'called hello with [world]'
```

当然也可以通过Class.metaClass.methodMissing{}达到一样的效果：
```groovy
class MethodPretender {}
MethodPretender.metaClass.methodMissing = {String name, args ->
    "called $name with $args"
}

def methodPretender = new MethodPretender()
assert methodPretender.hello('world') == 'called hello with [world]'
```



执行不存在的属性，需要重写propertyMissing方法：
例子：
```groovy
class PropPretender {
    def propertyMissing(String name) {
        "accessed $name"
    }
}
def propPretender = new PropPretender()
assert propPretender.hello == 'accessed hello'
```

### Expando和MetaClass
Expando类实现自GroovyObjectSupport抽象类，它又实现了GroovyObject接口。所以Expando具备了动态行为。
举个例子：
```groovy
def boxer = new Expando()
boxer.takeThis = 'ouch!'
boxer.fightBack = { times -> takeThis * times }
assert boxer.fightBack(3) == 'ouch!ouch!ouch!'
```
如果Expando只是像这样使用，恐怕没什么实用价值。Expando最主要的用途是配合metaClass使用。每个类或者实例，新增一个不存在的方法或者属性后，它的metaClass类型就是ExpandoMetaClass。如下例所示：
```groovy
assert String.metaClass =~ /MetaClassImpl/
String.metaClass.low = { -> delegate.toLowerCase() } // delegate reference to refer to the actual String instance
assert String.metaClass =~ /ExpandoMetaClass/
assert "DiErK".low() == "dierk"
```
给String新增了一个叫low的方法。

MetaClass可以说是MOP界的王道，用它可以获取到类中的所有信息。很像Java的反射功能。
例子：
```groovy
MetaClass mc = String.metaClass
final Object[] NO_ARGS = []
assert 1 == mc.respondsTo("toString", NO_ARGS).size()
assert 3 == mc.properties.size()
assert 76 == mc.methods.size()
assert 176 == mc.metaMethods.size()
assert "" == mc.getMetaMethod("toString").invoke("", NO_ARGS)
assert "" == mc.invokeMethod("","toString", NO_ARGS)
assert null == mc.invokeStaticMethod(String, "println", NO_ARGS)
assert "" == mc.invokeConstructor(NO_ARGS)
```
MetaClass可以用在类和实例上，下面分别举个例子：
给类动态增加方法和属性：
```groovy
class MyGroovy1 { }
def before = new MyGroovy1()
MyGroovy1.metaClass.myProp = "MyGroovy prop"
MyGroovy1.metaClass.test = {-> myProp }
try {
    before.test()
    assert false, "should throw MME"
} catch(mme) { println "MyGroovy1: throw mme" }

assert new MyGroovy1().test() == "MyGroovy prop"
```
重点关注这两句话：

> MyGroovy1.metaClass.myProp = "MyGroovy prop"
> MyGroovy1.metaClass.test = {-> myProp }

这说明新增的myProp属性和test方法是作用在MyGroovy1这个类上的。因此，只有在重新实例化对象后，新对象才具备了动态方法的能力。在这之前声明的对象都是不具备动态方法的。

下面再看一个metaclass作用在对象上的例子：
```groovy
class MyGroovy2 { }
def myGroovy = new MyGroovy2()
myGroovy.metaClass.myProp = "MyGroovy prop"
myGroovy.metaClass.test = {-> myProp }
try {
    new MyGroovy2().test()
    assert false, "should throw MME"
} catch(mme) { println "MyGroovy2: throw mme"  }
assert myGroovy.test() == "MyGroovy prop"
```
跟上段代码有种“找不同”的感觉。。。
重点关注这两行：
>myGroovy.metaClass.myProp = "MyGroovy prop"
>myGroovy.metaClass.test = {-> myProp }
说明metaClass是作用于MyGroovy2类的实例myGroovy。

如果想添加静态方法，需要在metaClass后使用static关键字。
例子：
```groovy
Integer.metaClass.static.answer = { -> 42}
assert Integer.answer() == 42
println Integer.answer()
```
还可以添加构造函数，如下：
```groovy
Integer.metaClass.constructor = { Calendar calendar ->
    new Integer(calendar.get(Calendar.DAY_OF_YEAR))
}
assert 327 == new Integer(Calendar.instance)
```
给Integer增加了一个Calendar类型的构造函数。

父类和接口如果通过元类新增了方法，它的子类和实现也都同步增加了该方法。
如下例所示：
```groovy
class MySuperGroovy { }
class MySubGroovy extends MySuperGroovy { }
MySuperGroovy.metaClass.added = {-> true }
assert new MySubGroovy().added()
Map.metaClass.toTable = {->
    delegate.collect{ [it.key, it.value] }
}
assert [a:1, b:2].toTable() == [
        ['a', 1],
        ['b', 2]
]
```
如果想给类/对象同时增加多个方法，可以包裹在{}块中，
一个综合的例子：
```groovy
Integer.metaClass {
    daysFromNow = { ->
        Calendar today = Calendar.instance
        today.add(Calendar.DAY_OF_MONTH, delegate)
        today.time
    }
    getDaysFromNow = { ->
        Calendar today = Calendar.instance
        today.add(Calendar.DAY_OF_MONTH, delegate)
        today.time
    }
    'static' {
        isEven = { val -> val % 2 == 0 }
    }
    constructor = { Calendar calendar ->
        new Integer(calendar.get(Calendar.DAY_OF_YEAR))
    }
    constructor = { int val ->
        println "Intercepting constructor call"
        constructor = Integer.class.getConstructor(Integer.TYPE)
        constructor.newInstance(val)
    }
}
```
另外要注意metaClass动态方法命名的一些约定：
1.如果方法是一个getter，即无参数但要返回结果，则方法名需要以getXyz命名,其中X大写，后面字母小写。
2.如果方法是一个setter，即有参数但无返回结果，则方法名需要以setXyz命名,其中X大写，后面字母小写。
3.如果方法返回布尔型结果，则方法名需要以isXyz命名。
4.其他情况，无特殊要求。

### Category
Category是另一种Groovy提供的动态语言特性。它能把其他类的静态方法赋给想要添加这个特性的类。
它有几点限制：

- category所在类的方法必须是静态的；
- category方法的第一个参数必须是调用这个方法的参数类型。
- 在使用的时候，需要包裹在use(category_name) {block}中。

听着不太好理解，看一个例子：
```groovy
import groovy.time.TimeCategory

def janFirst1970 = new Date(0)

use TimeCategory, {
    Date xmas = janFirst1970 + 1.year - 7.days
    assert xmas.month == Calendar.DECEMBER
    assert xmas.date == 25
}
```
这个例子利用了Groovy现有的Category方法：TimeCategory。
对于这行调用：
> janFirst1970 + 1.year - 7.days

janFirst1970是一个Date类型，后面的+操作符，在TimeCategory中进行了重载：
```groovy
    public static Date plus(Date date, BaseDuration duration) {
        return duration.plus(date);
    }
```
可以看到他有两个入参，第一个就是Date类型，第二个是一个BaseDuration类型。
另外，在在TimeCategory中还有如下两个方法：
```groovy
    public static DatumDependentDuration getYear(Integer self) {
        return getYears(self);
    }
    
    public static Duration getDays(Integer self) {
        return new Duration(self.intValue(), 0, 0, 0, 0);
    }    
```
这也可以当做一个Category来使用，他的入参是一个Integer，方法名getXXX(),groovy可以直接使用xxx()的方式调用，于是可以写成下面的调用方式：
> 1.year
> 7.days

它返回的Duration可以继续作为参数传递给plus()的第二个参数。

除了使用已有的Category外，我们还可以自定义category。
```groovy
class Marshal {
    static String marshal(Integer self) {
        self.toString()
    }
    static Integer unMarshal(String self) {
        self.toInteger()
    }
}

use Marshal, {
    assert 1.marshal() == "1"
    assert "1".unMarshal() == 1
    [Integer.MIN_VALUE, -1, 0, Integer.MAX_VALUE].each {
        assert it.marshal().unMarshal() == it
    }
}
```

如果要同时使用多个Category，需要在use的括号中写多个category，用逗号分隔。
```groovy
use(StringUtil, FindUtil) {
    str = "123487651"
    println str.toSSN()
    println str.extractOnly { it == '8' || it == '1' }
}
```

另外，我们还可以使用@Category注解来简化Category的使用。
比如上面的例子，可以简化为：

```groovy
@Category(Integer)
class IntegerMarshal {
    String marshal() {  // 省去了源参数和static
        toString()
    }
}
@Category(String)
class StringMarshal {
    Integer unMarshal() {
        this.toInteger()
    }
}
use ([IntegerMarshal, StringMarshal]) {  // 使用多个category
    assert 1.marshal() == "1"
    assert "1".unMarshal() == 1
}
```
@Category注解可以省略static和第一个参数类型。

### Mixin
Mixin在Ruby、Python等语言中也都支持，叫做：混入。
它和Category相比，解耦的更彻底一些。如果想使用某个类的方法，打上Mixin注解就可以使用了。如下例所示：
```groovy
class Friend {
    def listen() {
        "$name is listening as a friend"
    }
}

@Mixin(Friend)
class Person {
    String firstName
    String lastName
    String getName() { "$firstName $lastName"}
}

john = new Person(firstName: "John", lastName: "Smith")
assert "John Smith is listening as a friend" == john.listen()
```
除了使用@Mixin注解方式外，还可以在类和对象metaClass上使用mixin，
Class Mixin的例子：
```groovy
class Dog {
    String name
}
Dog.mixin Friend
buddy = new Dog(name: "Buddy")
println buddy.listen()
```

Instance Mixin的例子：
```groovy
class Cat {
    String name
}
socks = new Cat(name: "Socks")
socks.metaClass.mixin Friend
println socks.listen()
```

这种方式也支持混入多个mixin
```groovy
class EvenSieve {
    def getNo2() {
        removeAll { it % 2 == 0}
        return this
    }
}
class MinusSieve {
    def minus(int num) {
        removeAll { it % num == 0}
        return this
    }
}

ArrayList.mixin EvenSieve, MinusSieve   // mixin
assert (0..10).toList().no2 - 3 - 5 == [1, 7]
```
## Binding
Groovy中可以用binding来设置和获取变量，绑定后的变量具有全局作用域。
```groovy
count = 1
assert count == 1
assert binding.getVariable("count") == 1
binding.setVariable("count", 2)
assert count == 2
assert binding.getVariable("count") == 2

def local = count
assert local == 2
try {
    binding.getVariable("local")
    assert false
} catch (e) {
    assert e in MissingPropertyException
}
```
这里的binding是来自groovy内置的类Script, 它提供了getBinding方法来获取binding对象。
```groovy
public abstract class Script extends GroovyObjectSupport {
    private Binding binding;

    protected Script() {
        this(new Binding());
    }

    protected Script(Binding binding) {
        this.binding = binding;
    }

    public Binding getBinding() {
        return this.binding;
    }
}
```
从上面的例子可以看出，没有加def的变量是直接保存在binding中可以当全局变量中使用，而加了def的变量只在当前作用域有效。
通常，我们通常使用binding在GroovyShell执行脚本中绑定变量使用。
```groovy
def Binding binding = new Binding()
binding.message = "Hello, World"
shell = new GroovyShell(binding)
shell.evaluate("println message")
```
再看一个稍微复杂一点的例子，利用binding和closure构建DSL的例子：
```groovy
class Account2 {
    double spend = 11.00
    double balance = 100.00
    boolean active = true

    void credit(double value) {
        balance += value
    }
}

def binding = new Binding()
binding.reward = { closure ->
    closure.delegate = delegate
    closure()
}
binding.apply = { closure ->
    closure.delegate = delegate
    closure()
}

def account = new Account2()
binding.account = account
binding.active = account.active
binding.monthSpend = account.spend
binding.credit = Account2.&credit
assert account.balance == 100.00

def shell = new GroovyShell(binding)
shell.evaluate(
"""
reward {
    apply {
        if (active && monthSpend > 10.00)
            credit 5.00
    }
}
"""
)
assert account.balance == 105.00
```
reward和apply是binding的是闭包，且是嵌套的，最后一个闭包(apply)内执行代码块。这种写法就是DSL。

