## 寻找更好的测试框架(一)：ScalaTest测试框架介绍

**修订记录**

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.5.25 | 丁一 | 初稿 |

**目录**

[TOC]

#### ScalaTest简介

ScalaTest是一款基于Scala语言开发的测试框架。由于Scala是基于JVM平台的语言，所以可以天然对Java、Scala代码进行测试。得益于Scala的简洁语法，ScalaTest的测试风格也同样非常简约，除了简约的测试语法，还提供了多种测试风格供程序员选择，这样不同的开发角色可以自由选择适合的测试风格进行测试设计、开发了。

#### ScalaTest匹配器

Matchers是ScalaTest中的一个重要概念，主要分为ShouldMatchers和MustMatchers两种匹配器，他们都是Trait，所以可以自由mixin到测试类中。下面以ShouldMatchers为例介绍下使用语法。

- ##### Simple matchers
```scala
val list = 2 :: 4 :: 5 :: Nil
list.size should be(3) 
list.size should equal(3)
```
(1) 期望结果必须在()中，否则compile error
(2) 不能用==或者!=，3 == 5会执行得到false，但是不会导致test failed

- ##### String matchers
  ```scala
val string = """I fell into a burning ring of fire.
I went down, down, down and the flames went higher"""
string should startWith("I fell")
string should not endWith ("I fell")
string should include("down, down, down")
string should startWith regex "I.fel+"
string should not endWith regex("\\d{5}")
string should fullyMatch regex ("""I(.|\n|\s)*higher""")
```  
支持正则表达式，真的是好温馨。。。

- ##### Relational operator matchers
```scala
val num = 20
num should be < (30)
num should not be >(30)
num should be === (20) // 不能用==
num should not equal (21)
num should be > (0)
```
- ##### Floating matchers
```scala
(0.9 - 0.8) should be(0.1 plusOrMinus 0.01)
```
这个语法真的只能怪Java对浮点数的处理了。。。

- ##### Reference matchers
```scala
val list_1 = List(5)
val list_2 = list_1
val list_3 = List(5)
list_1 should be theSameInstanceAs (list_2)
list_1 should not be theSameInstanceAs(list_3)
```
- ##### Iterable matchers
```scala
List() should be('empty) 
8 :: 7 :: 6 :: 5 :: Nil should contain(7)
```
'empty是scala的一个Symbol，表示空对象

- ##### Seq and traversable matchers
```scala
(1 to 9) should have length (9)
(20 to 60 by 2) should have size (21)
```
- ##### Map matchers
```scala
val map = Map("Jimmy Page" -> "Led Zeppelin", "Sting" -> "The Police")
map should contain key ("Sting")
map should contain value ("Led Zeppelin")
map should not contain key("magic")
```

- ##### Compound matchers
```scala
val redHotChiliPeppers = List("Anthony Kiedis", "Flea", "Chad Smith", "Josh Klinghoffer")
redHotChiliPeppers should (contain("Anthony Kiedis") and (not contain ("John Frusciante") or contain("Dave Navarro")))
redHotChiliPeppers should not(contain("The Edge") or contain("Kenny G")) 
val numT = 20
var total = 3
numT should (equal(20) or equal {
    total += 6
})
total should be(9)
```
and和or以及右边的断言必须被()包住

- ##### java.util.Collection matchers
```scala
 import java.util.{ArrayList => JArrayList, HashMap => JHashMap, List => JList, Map => JMap}
 val jList: JList[Int] = new JArrayList[Int](20)
 jList.add(1)
 jList.add(2)
 jList.add(3)
 jList should have size (3)
 jList should have length (3) 
 jList should contain(1)
 jList should not contain (4)
 val emptyJList: JList[Int] = new JArrayList[Int]
 emptyJList should be('empty)
 val jMap: JMap[String, Int] = new JHashMap
 jMap.put("one", 1)
 jMap.put("two", 2)
 jMap.put("three", 3)
 jMap should contain key ("one")
 jMap should contain value (1)
 jMap should not contain key("four")
```

#### ScalaTest测试规格
前文提过，ScalaTest测试框架的一大亮点就是提供了不同风格的测试规格(Specification)供用户选择。常用的规格包括：FunSpec, WordSpec, FeatureSpec, FreeSpec, FlatSpec等。下面分别介绍：

- ##### FunSpec
```scala
class FunSpecTest extends FunSpec {
    describe("A Set") {
        describe("when empty") {
            it("should have size 0") {
                assert(Set.empty.size == 0)
            }
            it("should produce NoSuchElementException when head is invoked") {
                intercept[NoSuchElementException] {
                    Set.empty.head
                }
            }
        }
    }
}
```
FunSpec风格中，describe()用来描述测试主题，it()用来描述测试用例。intercept()用来进行异常测试，测试断言就用之前介绍过的匹配器。
终于可以用字符串表达测试用例名称了，不用像JUnit通过方法名来展示测试用例名称。Thank God!

- ##### WordSpec
```scala
 class WordSpecTest extends WordSpec {
     "A Set" when {
         "empty" should {
             "have size 0" in {
                 assert(Set.empty.size == 0)
             }
             "produce NoSuchElementException when head is invoked" in {
                 intercept[NoSuchElementException] {
                     Set.empty.head
                 }
             }
         }
     }
 }
```
形如其名，WordSpec主要靠文字表达测试语义。其中，when, should, can关键字用来描述测试主题且可以随意组合。in关键字前面用来描述测试用例，后面是测试实现。

- ##### FeatureSpec
```scala
  class FeatureSpecTest extends FeatureSpec with GivenWhenThen {
      feature("TV power button") {
          scenario("User presses power button when TV is off") {
              Given("a TV set that is switched off")
              val tv = new TVSet
              assert(!tv.isOn)

              When("the power button is pressed")
              tv.pressPowerButton()

              Then("the TV should switch on")
              assert(tv.isOn)
          }

          scenario("User presses power button when TV is on") {
              Given("a TV set that is switched on")
              val tv = new TVSet
              tv.pressPowerButton()
              assert(tv.isOn)

              When("the power button is pressed")
              tv.pressPowerButton()

              Then("the TV should switch off")
              assert(!tv.isOn)
          }
      }
  }

  class TVSet {
      private var on: Boolean = false
      def isOn: Boolean = on
      def pressPowerButton() {
          on = !on
      }
  }
```
FeatureSpec很适合做BDD开发，TS先做测试设计，feature()用来表达特性，scenario()是测试场景，given-when-then是标准BDD范式，可以先把测试场景设计出来，实现可以为空，然后开发人员逐条补充实现，利用TDD开发，直到实现全部测试场景，并测试通过。


- ##### FreeSpec
```scala
class FreeSpecTest extends FreeSpec {
    "A Set" - {
        "when empty" - {
            "should have size 0" in {
                assert(Set.empty.size == 0)
            }

            "should produce NoSuchElementException when head is invoked" in {
                intercept[NoSuchElementException] {
                    Set.empty.head
                }
            }
        }
    }
}
```
FreeSpec和WordSpec很像，不过正如其名所言，更自由一点，不像WordSpec受关键字when,should,can的限制，所有的测试意图都通过“- {”符号标记，in()依然用来表达测试用例和测试实现。

- ##### FlatSpec
```scala
class FlatSpecTest extends FlatSpec with Matchers {
    "An empty Set" should "have size 0" in {
        assert(Set.empty.size == 0)
    }

    it should "produce NoSuchElementException when head is invoked" in {
        intercept[NoSuchElementException] {
            Set.empty.head
        }
    }
}
```
相对于其他几种风格，FlatSpec风格更加扁平化，没有了冗余的测试头部，直接进入用例写实现，适合喜爱简洁的人群。 

#### ScalaTest通用组件
Fixture这个名词真的很难翻译，有些书翻译为“测试夹具”，让人有点摸不着头脑。我就索性叫他通用组件吧！下面介绍常用的几种：

- ##### Before and After
```scala
  class FixtureBeforeAndAfterTest extends FunSpec with ShouldMatchers with BeforeAndAfter {
      val list = new ListBuffer[Int]()

      before {
          info("starting add 5 to list")
          list += 5
      }

      after {
          info("clearing list")
          list.clear
      }

      describe("fixture.......") {
          it("add 1, size should be 2") {
              list += 6
              info(list.mkString("[", ",", "]"))
              list should have size(2)
          }

          it("add 1, size should be 3") {
              list += 6
              list += 7
              info(list.mkString("[", ",", "]"))
              list should have size(3)
          }
      }
  }
```
这个特性对应JUnit的@Before和@After特性，主要做测试用例初始化和清理用。 

- ##### Anonymous Objects
```scala
  class FixtureAnonymousObjectTest extends FunSpec with ShouldMatchers {
      def fixture = new {
          import scala.collection.mutable._

          val list = new ListBuffer[Int]()
          list += 5
      }

      describe("fixture with Anonymous Objects.......") {
          it("add 1, size should be 2") {
              val list = fixture.list
              list += 6
              info(list.mkString("[", ",", "]"))
              list should have size(2)
          }

          it("add 1, size should be 3") {
              val list = fixture.list
              list += 6
              list += 7
              info(list.mkString("[", ",", "]"))
              list should have size(3)
          }
      }
  }
```
利用Scala匿名对象的语法创建了一个共享对象，每个测试用例调用的时候进行初始化创建。

- ##### Fixture Traits

```scala
  class FixtureTraitTest extends FunSpec with ShouldMatchers {
      trait FixtureTrait {
          import scala.collection.mutable._
          val list = new ListBuffer[Int]()
          list += 5
      }

      describe("fixture with Trait.......") {
          it("add 1, size should be 2") {
              new FixtureTrait {
                  list += 6
                  info(list.mkString)
                  list should have size(2)
              }
          }

          it("add 1, size should be 3") {
              new FixtureTrait {
                  list += 6
                  list += 7
                  info(list.mkString)
                  list should have size(3)
              }
          }
      }
  }
```
利用scala的trait语法创建了一个trait对象，然后再测试用例中实例化调用。

#### 总结

通过上文的介绍，可以看出ScalaTest比JUnit还是有不少优势：

- 测试语法更加简洁和多样化

- 提供了不同种类的测试规格供用户选择

也有少许缺点：

- 有一定的Scala语言学习成本，但由于涉及的语法特性不多，所以也不会有太大问题。

- ScalaTest对参数化数据的测试没有太好的改进。