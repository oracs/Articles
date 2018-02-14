| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.11.22 | 丁一 | 初稿 |

## 使用Category构建DSL
我们经常对Java中的日期操作诟病很多，原因是太难使用了，如果能写成下面的语句该有多美好:

```groovy
1.year - 7.days
2.days.ago.at(4.30)
```

这种写法相信谁到都能看得懂。
利用Groovy的Category特性，我们也能轻松搞定

```groovy
class DateUtil {
    static int getDays(Integer self) { self }

    static Calendar getAgo(Integer self) {
        def date = Calendar.instance
        date.add(Calendar.DAY_OF_MONTH, -self)
        date
    }

    static Date at(Calendar self, Double time) {
        def hour = (int) (time.doubleValue())
        def minute = (int) (Math.round((time.doubleValue() - hour) * 100))
        self.set(Calendar.HOUR_OF_DAY, hour)
        self.set(Calendar.MINUTE, minute)
        self.set(Calendar.SECOND, 0)
        self.time
    }
}
```

我们就可以这样调用了：

```groovy
use(DateUtil) {
    println 2.days.ago.at(4.30)
}
```

## 使用ExpandoMetaClass构建DSL
上面的语句有个小瑕疵，就是要在调用前需要加上use(DateUtil)语句，使用起来稍微有些不方便。
还好Groovy提供了强大的元编程能力，可以给类轻松加上动态方法。

```groovy
Integer.metaClass{
    getDays = { ->
        delegate
    }
    getAgo = { ->
        def date = Calendar.instance
        date.add(Calendar.DAY_OF_MONTH, -delegate)
        date
    }
}
Calendar.metaClass.at = { Map time ->
    def hour = 0
    def minute = 0
    time.each {key, value ->
        hour = key.toInteger()
        minute = value.toInteger()
    }
    delegate.set(Calendar.HOUR_OF_DAY, hour)
    delegate.set(Calendar.MINUTE, minute)
    delegate.set(Calendar.SECOND, 0)
    delegate.time
}
```
这样调用就变成了：

```groovy
println 2.days.ago.at(4:30)
```
## 使用GroovyShell加载和执行脚本
有时候我们只想执行一段纯粹的脚本，看起来像外部DSL的感觉。
比如下面订披萨的DSL，定义在orderPizza.dsl文件中。

```groovy
size large
crust thin
topping Olives, Onions, Bell_Pepper
address "101 Main St., ..."
card visa, '1234-1234-1234-1234'
```
那如何执行这段脚本呢？简单的办法就是把它作为Closure参数传入一个方法，然后利用Groovy methodMissing特性，动态为方法执行体添加这些方法实现。
下面的是实现脚本，定义在Pizzahut.groovy文件中。

```groovy
def large = 'large'
def thin = 'thin'
def visa = 'Visa'
def Olives = 'Olives'
def Onions = 'Onions'
def Bell_Pepper = 'Bell Pepper'
orderInfo = [:]

def methodMissing(String name, args) {
    orderInfo[name] = args
}
def acceptOrder(closure) {
    closure.delegate = this
    closure()
    orderInfo.each { key, value ->
        println "${key} -> ${value.join(': ')}"
    }
}
```
开头定义的常量是方法执行传入的参数，如果不定义会报找不到属性的错误。
methodMissing()是GroovyObject内置的方法，如果找不到的方法都会触发到这个方法中。
closure调用的时候必须要指定delegate，这样才知道closure在那个类中执行。
调用的脚本如下，定义在invoke.groovy文件中。

```groovy
def path ="c:/methodInterception"
def dslDef = new File(path + 'Pizzahut.groovy').text
def dsl = new File(path + 'orderPizza.dsl').text
def script = """
${dslDef}
acceptOrder {
    ${dsl}
}
"""
new GroovyShell().evaluate(script)
```

注意，这里使用的是Groovy内置的脚本执行器：GroovyShell，利用它可以很方便的加载和执行脚本。

## 从另一个例子说起
扫地机器人是我们都熟悉的一个生活好帮手，假定它能向东、西、南、北四个方向移动。
如果用DSL能像下面的形式表示：

```groovy
robot.move left
```

这该如何实现呢？我们先定义模型类：

```groovy
class Robot {
    void move(Direction dir) {
        println "robot moved $dir"
    }
}

enum Direction {
    left, right, forward, backward
}
```

结合前面的知识，这个DSL应该很好实现：

```groovy
def shell = new GroovyShell(this.class.classLoader)
shell.evaluate '''
import book.groovy.Groovy_in_Action.ch19_dsl.model.Robot
import static book.groovy.Groovy_in_Action.ch19_dsl.model.Direction.*
def robot = new Robot()
robot.move left
'''
```
使用了GroovyShell执行脚本，但存在几个问题：

- import包显示很不和谐
- new Robot()使用起来不方便

## 使用Binding绑定变量
我们这节通过Binding变量来消除上一节遇到的问题。
Binding是GroovyShell的第二个参数，他可以临时存储变量信息。声明过的变量可以直接在脚本中使用：

```groovy
import dsl.model.*
def binding = new Binding(
        robot: new Robot(),
        left: Direction.left,
        right: Direction.right,
        forward: Direction.forward,
        backward: Direction.backward
)
shell = new GroovyShell(this.class.classLoader, binding)
shell.evaluate '''
robot.move left
'''
```
我们把robot和Direction都进行了变量绑定，import也自然在执行体中导入。
看起来还不错。
enum和robot()的调用，还有一种简化的写法。

```groovy
def robot = new Robot()
def binding = new Binding(
        robot: robot,
        move: robot.&move,
        *: Direction.values().collectEntries { [(it.name()): it] }
)
shell = new GroovyShell(this.class.classLoader, binding)
shell.evaluate '''
move left
'''
```
这样enum绑定变量的代码简化了，利用方法指针，DSL语句也得到了简化。

## 为数值添加方法
前面实现了简单的move指令，如果我们想指定移动的距离，像下面这样表达：

```groovy
move right, 3.meters
```

该如何实现呢？
利用前面提到的Category特性，这个DSL也很容易实现。
先实现模型类：

```groovy
enum Unit {
    centimeter('cm', 0.01),
    meter ( 'm', 1),
    kilometer ('km', 1000)

    String abbreviation
    double multiplier

    Unit(String abbr, double mult) {
        this.abbreviation = abbr
        this.multiplier = mult
    }
    String toString() { abbreviation }
}

@TupleConstructor
class Distance {
    double amount
    Unit unit

    Speed div(Duration dur) {
        new Speed(amount, unit)
    }

    String toString() { "$amount$unit" }
}

class DistanceCategory {
    static Distance getCentimeters(Number num) {
        new Distance(num, Unit.centimeter)
    }
    static Distance getMeters(Number num) {
        new Distance(num, Unit.meter)
    }
    static Distance getKilometers(Number num) {
        new Distance(num, Unit.kilometer)
    }
}
```
我们给Robot新增一个move方法：

```groovy
    void move(Direction dir, Distance d) {
        println "robot moved $dir by $d"
    }
```
我们就可以像下面这样调用了：

```groovy
def shell = new GroovyShell(this.class.classLoader, binding)
use(DistanceCategory) {
    shell.evaluate '''
move right, 3.meters
'''
}
```

## 使用named arguments
我们还想再进一步，除了移动的距离外，还想指定移动的时速，向下面的表达式：

```groovy
move right, by: 3.m, at: 5.km/h
```

这里有两点不同：

- by和at参数的输入格式
- km/h中的/表示

针对这两点，第一个可以用Groovy的named arguments来解决，第二个可以用/操作符(divide)重载来解决。
实现如下：

```groovy
// 更新DistanceCategory类方法
class DistanceCategory {
    static Distance getCm(Number num) { getCentimeters(num) }
    static Distance getM(Number num) { getMeters(num) }
    static Distance getKm(Number num) { getKilometers(num) }
}

// 新增两个模型类
@TupleConstructor
class Speed {
    Number amount
    Unit unit
    String toString() { "$amount $unit/h" }
}

enum Duration {
    h
}

// Distance类重载divide(/操作符)方法
@TupleConstructor
class Distance {
    double amount
    DistanceUnit unit
    Speed div(Duration dur) {
        new Speed(amount, unit)
    }
    String toString() { "$amount$unit" }
}

// Robot新增一个move方法
    void move(Map m, Direction dir) {
        println "robot moved $dir by $m.by at ${m.at ?: '1 km/h'}"
    }
```
这样就可以像向下面的方式调用了：

```groovy
move right, by: 3.m, at: 5.km/h
```

## Command Chain
上节的DSL已经很优秀了，如果想写的更卓越的话，比如下面的表达式：

```groovy
move right by 3.meters at 5.km/h
```

这种写法更纯粹，省略了逗号和前缀。
那该如何实现呢？ 
一般来说有两种方法。一个是利用buidler模式，让方法本身返回this，形成连贯接口，然后省略掉点和括号。
像这样：

```groovy
def move(dir) {
    println "moving $dir"
    this
}

def by(distance) {
    print ",$distance"
    this
}

def at(speed) {
    print ",$speed /h"
}
```

还有一种方法是利用闭包和Map的嵌套。

```groovy
def move(Direction dir) {
    [by: { Distance dist ->
        [at: { Speed speed ->
            println "robot moved $dir by $dist at $speed"
        }]
    }]
}
```
看着很眼花，它实际上相当于如下调用：

```groovy
def map1 = move(right)
def byClosure = map1['by']
def map2 = byClosure(3.meters)
def atClosure = map2['at']
atClosure(5.km/h)
```

这两种方法都可以达到如下的调用效果：

```groovy
move right by 3.meters at 5.km/h
```

## 使用闭包进行上下文切换
groovy的with语句可以将一组上下文相关语句放在一起。我们利用这个特性，可以提升DSL的表达力。
比如：

```groovy
def robot = new Robot()
robot.with {
    move left
    move forward
}
```
但似乎叫robot.with不是很符合习惯。我们可以使用一个重命名方法的小伎俩解决这个问题。

```groovy
// Robot类中新定义一个方法
    void execute(Closure actions) {
        this.with actions
    }
```
这样调用就改为；

```groovy
robot.execute {
    move left
    move forward
}
```
这样的实现，表达力更自然。

## 结语
感觉Groovy就是天生为DSL创造的一门语言，无论内部DSL，还是外部DSL，使用Groovy都能轻松搞定。如果你是用的JVM开发平台，那么用Groory开发DSL是不二选择！

