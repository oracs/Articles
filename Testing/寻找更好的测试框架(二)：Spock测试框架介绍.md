## 寻找更好的测试框架(二)：Spock测试框架介绍

**修订记录**

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.6.1 | 丁一 | 初稿 |

**目录**

[TOC]

#### Spock简介
Spock是一款基于Groovy语言开发的高效测试框架。相比于JUnit，ScalaTest等测试框架来说，Spock的最大特点在于其结构化的语法表达，参数化测试的强大表现力，自带Mock功能的简洁性。相信看完本文的介绍，你会对Spock爱不释手的。

#### 基本语法
简单的说，Spock测试框架是由下面8个标签以及代码块组成的：

|  标签  |      描述     |使用比例|
|--------|----------------|-----|
| given:  | 创建初始条件 | 85% |
| setup:  | 同given | 0% |
| when:   | 触发测试行为  | 99% |
| then:   | 检验测试结果  | 99% |
| and:    | 附加条件或结果 | 60% |
| expect: | when-then的结合体 | 20% |
| where:  | 参数化测试 | 40% |
| cleanup:| 释放资源 | 5% |

- ##### given-controlStructure-then的使用例子
```groovy
class SpockLabelTest extends Specification{
    def "A basket with two products weights as their sum (precondition)"() {
        given: "an empty basket, a TV and a camera"
        Product tv = new Product(name: "bravia", price: 1200, weight: 18)
        Product camera = new Product(name: "panasonic", price: 350, weight: 2)
        Basket basket = new Basket()
        
        when: "user wants to buy the TV and the camera"
        basket.addProduct tv
        basket.addProduct camera

        then: "basket weight is equal to both camera and tv"
        basket.currentWeight == (tv.weight + camera.weight)
    }
}
```

1. 测试类需要继承spock.lang.Specification
2. given-when-then也是标准BDD开发的测试原语

- ##### expect的使用例子
```groovy
    def "An empty basket has no weight (alternative)"() {
        given: "an empty basket"
        Basket basket = new Basket()

        expect: "that the weight is 0"
        basket.currentWeight == 0
    }
```
expect将given-then语句合二为一，测试行为后紧跟着验证结论，测试更加简洁。

- ##### and的使用例子
```groovy
    def "A basket with three products weights as their sum"() {
        given: "an empty basket"
        Basket basket = new Basket()

        and: "several products"
        Product tv = new Product(name: "bravia", price: 1200, weight: 18)
        Product camera = new Product(name: "panasonic", price: 350, weight: 2)
        Product hifi = new Product(name: "jvc", price: 600, weight: 5)

        when: "user wants to buy the TV and the camera and the hifi"
        basket.addProduct tv
        basket.addProduct camera
        basket.addProduct hifi

        then: "basket weight is equal to all product weight"
        basket.currentWeight == (tv.weight + camera.weight + hifi.weight)
    }
```
and标签可以搭配given-when-then中的每个标签任意组合使用。

#### 参数化测试
参数化测试是Spock的一大亮点，通过表格化的条件设置和结果展示，让人很直观的就能了解测试全貌。
- ##### 使用where块
```groovy
    def "Trivial adder test"() {
        given: "an adder"
        Adder adder = new Adder()

        expect: "that it calculates the sum of two numbers"
        adder.add(first, second) == sum

        where: "some scenarios are"
        first | second || sum
        1     | 1      || 2
        3     | 2      || 5
        82    | 16     || 98
        3     | -3     || 0
        0     | 0      || 0
    }
```
是不是很直观？
条件参数用 | 分割（支持多个），结果参数用 || 分割（支持多个）
如果没有结果参数， 则列值用下划线 _ 表示
```groovy
    def "Tiff, gif, raw,mov and bmp are invalid extensions"() {
        given: "an image extension checker"
        ImageNameValidator validator = new ImageNameValidator()

        expect: "that only valid filenames are accepted"
        !validator.isValidImageExtension(pictureFile)

        where: "sample image names are"
        pictureFile       || _
        "screenshot.bmp"  || _
        "IMG3434.raw"     || _
        "christmas.mov"   || _
        "sky.tiff"        || _
        "dance_bunny.gif" || _
    }
```
参数不仅可以使用字面量，还可以使用**表达式**
```groovy
    def "Testing the Adder for #first + #second = #sum "() {
        given: "an adder"
        Adder adder = new Adder()

        expect: "that it calculates the sum of two numbers"
        adder.add(first, second) == sum

        where: "some scenarios are"
        first             | second  || sum
        2 + 3             | 10 - 2  || new Integer(13).intValue()
        [1, 2, 3].get(1)  | 3       || IntegerFactory.createFrom("five")
    }
```

- ##### 使用管道表示法
虽然数据表（用|和||分割）的表示法已经非常直观，但如果要测试大批量数据或者从外部输入，这种方式就有些捉襟见肘。还好，Spock提供了一种称之为数据管道的方式来解决这个问题。
管道符： <<
```groovy
def "Multiplying #first and #second is always a negative number"() {
    given: "a calculator"
    Calculator calc = new Calculator()

    expect: "that multiplying a positive and negative number is also negative"
    calc.multiply(first, second) < 0

    where: "some scenarios are"
    first << (20..80)
    second << (-65..-5)
}
```
其中，(20..80) 表示从20--80的一组测试数据。
并且这种方式还支持使用**常量**：
```groovy
def "Multipling #first and #second is always a negative number"() {
    given: "a calculator"
    Calculator calc = new Calculator()

    expect: "that multiplying a positive and negative number"
    calc.multiply(first,second) < 0

    where: "some scenarios are"
    first << [20,34,44,67]
    second = -1
}
```
其中，对于first中的所有参数，second参数的值都是-1。
还支持使用**表达式**：
```groovy
def "Multipling #first and #second is always a negative number (alt)"() {
    given: "a calculator"
    Calculator calc = new Calculator()

    expect: "that multiplying a positive and negative number"
    calc.multiply(first,second) < 0

    where: "some scenarios are"
    first << [20,34,44,67]
    second = first * -1
}
```

- ##### 使用外部文件
有时的测试输入源会是外部文件，比如图像的文件名存储在一个名为validImageNames.txt中，格式如下：
```
hello.jpg
another.jpeg
modern0034.JPEG
city.Png
city_004.PnG
landscape.JPG
```
Spock也是支持用管道符(<<)来读取外部文件输入的。
```groovy
    def "Valid images are PNG and JPEG files"() {
        given: "an image extension checker"
        ImageNameValidator validator = new ImageNameValidator()

        expect: "that all filenames are accepted"
        validator.isValidImageExtension(pictureFile)

        where: "sample image names are"
        pictureFile << new File("validImageNames.txt").readLines()
    }
```
是不是很强大？
但，这还不是终极版本！Spock还支持自定义处理类来支持特殊的输入，比如从文件读取后返回多个字段等。 代码有点多，不在这里写了，用到时再查资料吧 :)

#### Mock
在做单元测试时，我们经常会使用Mock工具来模拟不好测试的对象，常用的Mock工具有Mockito，PowerMock等，但一般都是第三方组件。
**像Spock这种把Mock组件也集成在测试框架中的软件并不多见！**
它可不是简单的集成了一个Mock组件，相反正是利用Groovy的简洁语法，构建出简洁、好用的Mock组件。

在Spock中有Stub和Mock两种组件，但由于Mock已经涵盖了Stub的行为，所以我们这里以Mock为例进行介绍。
- ##### 模拟对象状态
```groovy
def "If warehouse has the product on stock everything is fine"() {
    given: "a basket and a TV"
    Product tv = new Product(name: "bravia", price: 1200, weight: 18)
    Basket basket = new Basket()

    and: "a warehouse with enough stock"
    WarehouseInventory inventory = Mock(WarehouseInventory)
    inventory.isProductAvailable("bravia", 1) >> true
    inventory.isEmpty() >> false
    basket.setWarehouseInventory(inventory)

    when: "user checks out the tv"
    basket.addProduct tv

    then: "order can be shipped right away"
    basket.canShipCompletely()
}
```
其中用**Mock**()函数来模拟待测对象，用 **>>** 符号来设置返回结果。
如果需要模拟返回多个测试结果，需要用 **>>>** 来标识。
```groovy
def "Inventory is always checked in the last possible moment"() {
    given: "a basket and a TV"
    Product tv = new Product(name: "bravia", price: 1200, weight: 18)
    Basket basket = new Basket()

    and: "a warehouse with fluctuating stock levels"
    WarehouseInventory inventory = Mock(WarehouseInventory)
    inventory.isProductAvailable("bravia", _) >>> true >> false
    inventory.isEmpty() >>> [false, true]
    basket.setWarehouseInventory(inventory)

    when: "user checks out the tv"
    basket.addProduct tv

    then: "order can be shipped right away"
    basket.canShipCompletely()

    when: "user wants another TV"
    basket.addProduct tv

    then: "order can no longer be shipped"
    !basket.canShipCompletely()
}
```
如果模拟调用函数传入的**参数不确定**的话，可以用 _ 来表示。
```groovy
inventory.isProductAvailable(_, 1) >> true
inventory.isProductAvailable( _, _) >> true
```
还可以利用Groovy的闭包(closure)来**模拟异常**
```groovy
def "A problematic inventory means nothing can be shipped"() {
    given: "a basket and a TV"
    Product tv = new Product(name: "bravia", price: 1200, weight: 18)
    Basket basket = new Basket()

    and: "a warehouse with serious issues"
    WarehouseInventory inventory = Mock(WarehouseInventory)
    inventory.isProductAvailable("bravia", _) >> {
        throw new RuntimeException("critical error")
    }
    basket.setWarehouseInventory(inventory)

    when: "user checks out the tv"
    basket.addProduct tv

    then: "order cannot be shipped"
    !basket.canShipCompletely()
}
```
Groovy的闭包真的很强大，除了模拟异常，还可以模拟复杂行为，类似于Java8的lambda表达式。
```groovy
and: "a shipping calculator that charges 10 dollars for each product"
ShippingCalculator shippingCalculator = Stub(ShippingCalculator)
shippingCalculator.findShippingCostFor(_, _) >> {
    Product product, int count -> product.weight == 0 ? 0 : 10 * count
}
```

- ##### 模拟对象行为
这个有点类似Mockito的Verify()方法，但Spock的的写法更简洁：

```groovy
def "Warehouse is queried for each product"() {
    given: "a basket, a TV and a camera"
    Product tv = new Product(name: "bravia", price: 1200, weight: 18)
    Product camera = new Product(name: "panasonic", price: 350, weight: 2)
    Basket basket = new Basket()

    and: "a warehouse with limitless stock"
    WarehouseInventory inventory = Mock(WarehouseInventory)
    basket.setWarehouseInventory(inventory)

    when: "user checks out both products"
    basket.addProduct tv
    basket.addProduct camera
    boolean readyToShip = basket.canShipCompletely()

    then: "order can be shipped"
    readyToShip
    2 * inventory.isProductAvailable(_, _) >> true
    0 * inventory.preload(_, _)
}
```
其中最重要的是最后两行，这个是spock进行对象行为模拟测试的写法。
```
N * mockObject.method(arguments) >> result
```
1. 其中N表示调用次数，0表示未调用
2. 这个表达式是可以设置条件参数和返回结果的，也就是说，既可以测试状态，又可以测试行为。
3. N可以为_ ，表示任意次数，也就是一个区间，比如(1..3)，表示1到3次，或者是(1.._)，表示至少一次。
4. arguments可以为_，表示任意参数。
5. method也可以为_，表示任务方法调用

```groovy
1 * subscriber.receive("hello")      // exactly one call
0 * subscriber.receive("hello")      // zero calls
(1..3) * subscriber.receive("hello") // between one and three calls (inclusive)
(1.._) * subscriber.receive("hello") // at least one call
(_..3) * subscriber.receive("hello") // at most three calls
_ * subscriber.receive("hello")      // any number of calls, including zero
1 * subscriber.receive("hello")     // an argument that is equal to the String "hello"
1 * subscriber.receive(!"hello")    // an argument that is unequal to the String "hello"
1 * subscriber.receive()            // the empty argument list (would never match in our example)
1 * subscriber.receive(_)           // any single argument (including null)
1 * subscriber.receive(*_)          // any argument list (including the empty argument list)
1 * subscriber.receive(!null)       // any non-null argument
1 * subscriber.receive(_ as String) // any non-null argument that is-a String
1 * subscriber.receive({ it.size() > 3 }) // an argument that satisfies the given predicate 
1 * subscriber._(*_)     // any method on subscriber, with any argument list
1 * subscriber._         // shortcut for and preferred over the above
1 * _._                  // any method call on any mock object
1 * _                    // shortcut for and preferred over the above
```
**空参数**：
```groovy
2 * inventory.isProductAvailable(!null, 1) >> true
```
**参数指定类型**：
```groovy
2 * inventory.isProductAvailable(_ as String, _ as Integer) >> true
```

**参数为闭包**：
```groovy
1 * creditCardSevice.sale(1550, { category -> category.vip == false })
```

**注意：**
对于静态方法，私有方法，单例等，Spock的Mock也爱莫能助，只能借助无所不能的PowerMock了！

#### 通用组件
- ##### @Subject: 标记测试类
有时在测试代码非常多的情况下，为了能更好的识别出待测类，可以用@Subject注解来进行标识。
```groovy
    def "A basket with two products weights as their sum (better)"() {
        given: "an empty basket"
        @Subject
        Basket basket = new Basket()

        and: "several products"
        Product tv = new Product(name: "bravia", price: 1200, weight: 18)
        Product camera = new Product(name: "panasonic", price: 350, weight: 2)

        when: "user wants to buy the TV and the camera and the hifi"
        basket.addProduct tv
        basket.addProduct camera

        then: "basket weight is equal to all product weight"
        basket.currentWeight == (tv.weight + camera.weight)
    }
```

- ##### @Title: 描述类的测试说明
```groovy
  @Title("Unit test for basket weight")
  @Subject(Basket)
  class BasketWeightDetailedSpec extends spock.lang.Specification{
    [...test methods here redacted for brevity...]
  }
```
1. @Title用来描述类的测试说明``
2. @Subject除了用在测试方法内外，还可以用在类名前，用来表示测试类适用于全部的测试方法。

- ##### setup()和cleanup(): 测试用例初始化和资源清理
```groovy
class CommonCleanupSpec extends spock.lang.Specification {
    Product tv
    Product camera
    Basket basket

    def setup() {
        tv = new Product(name: "bravia", price: 1200, weight: 18)
        camera = new Product(name: "panasonic", price: 350, weight: 2)
        basket = new Basket()
    }

    def "A basket with one product weights as that product"() {
        when: "user wants to buy the TV"
        basket.addProduct tv

        then: "basket weight is equal to all product weight"
        basket.currentWeight == tv.weight
    }

    def cleanup() {
        basket.clearAllProducts()
    }
}
```
setup()和cleanup()对应JUnit的@Before和@After功能，用来做测试用例初始化和资源清理工作。

- ##### setupSpec()和cleanupSpec(): 测试类的初始化和资源清理
```groovy
class LifecycleSpec extends spock.lang.Specification {
    def setupSpec() {
        println "Will run only once"
    }

    def setup() {
        println "Will run before EACH feature"
    }

    def "first feature being tested"() {
        expect: "trivial test"
        println "first feature runs"
        2 == 1 + 1
    }

    def cleanup() {
        println "Will run once after EACH feature"
    }

    def cleanupSpec() {
        println "Will run once at the end"
    }
}
```
setupSpec()和cleanupSpec()对应JUnit的@BeforeClass, and @AfterClass,，用来做测试类的初始化和资源清理工作。

- ##### @Shared：共享的测试类
如果是在setupSpec中声明的测试类，需要加上@Shared注解，否则会无法识别。
```groovy
class SharedSpec extends Specification {
    @Shared
    CreditCardProcessor creditCardProcessor;
    BillableBasket basket

    def setupSpec() {
        creditCardProcessor = new CreditCardProcessor()
    }

    def setup() {
        basket = new BillableBasket()
        creditCardProcessor.newDayStarted()
        basket.setCreditCardProcessor(creditCardProcessor)
    }
}
```

- ##### @Unroll：参数化测试中分别执行测试用例
在前文介绍的参数化测试中，虽然有很多测试场景，但默认是当做一条用例执行的。要想当成多条用例执行，需要在测试方法前加上@Unroll注解。
```groovy
    @Unroll("Adder test #first, #second and #sum (alt2)")
    def "Trivial adder test (alt2)"() {
        given: "an adder"
        Adder adder = new Adder()

        when: "the add method is called for two numbers"
        int result = adder.add(first, second)

        then: "the result should be the sum of them"
        result == sum

        where: "some scenarios are"
        first | second || sum
        1     | 1      || 2
        3     | 2      || 5
        3     | -3     || 0
    }
```
1. 执行结果会被认为有3条。
2. Unroll中可以填写测试说明，如果在使用的条件参数前面加上了#号，可以在运行测试结果的名称中显示具体的参数值。

- ##### thrown()和notThrown()：抛出和不抛出异常
```groovy
    def "Error conditions for unknown products"() {
        given: "a warehouse"
        WarehouseInventory inventory = new WarehouseInventory()

        when: "warehouse is queried for the wrong product"
        inventory.isProductAvailable("productThatDoesNotExist", 1)

        then: "an exception should be thrown"
        thrown(IllegalArgumentException)
    }

    def "Negative quantity is the same as 0"() {
        given: "a warehouse"
        WarehouseInventory inventory = new WarehouseInventory()

        and: "a product"
        Product tv = new Product(name: "bravia", price: 1200, weight: 18)

        when: "warehouse is loaded with a negative value"
        inventory.preload(tv, -5)

        then: "the stock is empty for that product"
        notThrown(IllegalArgumentException)
        !inventory.isProductAvailable(tv.getName(), 1)
    }
```

- ##### @Timeout: 设置超时
```groovy
@Timeout(value = 5000, unit = TimeUnit.MILLISECONDS)
def "credit card charge happy path - alt "() {
    given: "a basket, a customer and a TV"
    Product tv = new Product(name: "bravia", price: 1200, weight: 18)
    BillableBasket basket = new BillableBasket()
    Customer customer = new Customer(name: "John", vip: false, creditCard: "testCard")

    and: "a credit card service"
    CreditCardProcessor creditCardSevice = new CreditCardProcessor()
    basket.setCreditCardProcessor(creditCardSevice)

    when: "user checks out the tv"
    basket.addProduct tv
    boolean success = basket.checkout(customer)

    then: "credit card is charged"
    success
}
```

- ##### @Ignore/@IgnoreIf/@IgnoreRest: 忽略测试
@Ignore: 忽略本条测试
@IgnoreRest：忽略其他测试
@IgnoreIf：有条件忽略
```groovy
    @IgnoreIf({ jvm.java9 })
    def "credit card charge happy path2"() {

    }

    @IgnoreIf({ os.windows })
    def "credit card charge happy path - alt"() {

    }

    @IgnoreIf({ env.containsKey("SKIP_SPOCK_TESTS") })
    def "credit card charge happy path - alt 2"() {

    }
```


#### 总结
可以看出，Spock测试框架的内容非常丰富，除了常规的单元测试外，还涵盖了参数化测试，Mock对象以及其他扩展功能，如果愿意的话，Spock甚至还可以做更高层的FT和ST级别的测试（参考《Java Testing with Spock》），所以如果对测试有更高追求的话，推荐用Spock！
