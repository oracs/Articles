## Learning Java8: Lambda Expressions

修订记录

| 时间 | 内容 |
|--------|--------|
| 2016.09.22 | 初稿 |
| 2017.12.14 | 修改文档错误 |

[TOC]

### 为什么要使用Lambda表达式
先看几段Java8以前经常会遇到的代码：
- **创建线程并启动**
```java
// 创建线程
public class Worker implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            doWork();
        }
    }
}
// 启动线程
Worker w = new Worker();
new Thread(w).start();
```
- **比较数组**
```java
 // 定义一个比较器
 public class LengthComparator implements Comparator<String> {
     @Override
     public int compare(String first, String second) {
         return Integer.compare(first.length(), second.length());
     }
 }
 //对字符数组进行比较
 Arrays.sort(words, new LengthComparator());
```

- **给按钮添加单击事件**
```java
  public void onClick(Button button) {
      button.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              System.out.println("button clicked.");
          }
      });
  }
```
对于这三段代码，我们已经司空见惯了。
但他们的问题也很突出：就是**“噪声”**太多！想实现一个数组的比较功能，至少要写5行代码，但其中只有一行代码才是我们真正关注的！
Java复杂冗余的代码实现一直被程序员所诟病，好在随着JVM平台语言Scala的兴起以及函数式编程风格的风靡，让Oracle在Java的第8个系列版本中进行了革命性的变化，推出了一系列函数式编程风格的语法特性，比如Lambda表达式以及Stream。
如果采用Lambda表达式，上面三段代码的实现将会变得极为简洁。

- **创建线程并启动(采用Lambda版本)**
```java
new Thread(() -> {
    for (int i = 0; i < 100; i++) {
        doWork();
    }
}).start();
```

- **比较数组(采用Lambda版本)**
```java
 Arrays.sort(words, (first, second) -> Integer.compare(first.length(), second.length())
```

- **给按钮添加单击事件(采用Lambda版本)**
```java
 button.addActionListener((event) -> System.out.println("button clicked."));
```
怎么样？通过Lambda表达式，代码已经变得足够简洁，让你把关注点全部都放在业务代码上。

### Lambda表达式的语法
格式：(参数) -> 表达式
其中：

1. 参数可以为0-n个。如果有多个参数，以逗号(,)分割。如果有一个参数，括号()可以省去；如果没有参数，括号()也不能省去。[这就有点不够纯粹了，比scala还是差了点！]，参数前可以加类型名，但由于自动类型推导功能，可以省去。
1. 表达式可以是一行表达式，也可以是多条语句。如果是多条语句，需要包裹在大括号{}中。
1. 表达式不需要显示执行返回结果，它会从上下文中自动推导。
以下是一些例子：
- **一个参数**
```java
event -> System.out.println("button clicked.")
```

- **多个参数**
```java
(first, second) -> Integer.compare(first.length(), second.length()
```

- **0个参数**
```java
() -> System.out.println("what are you nongshalei?")
```

- **表达式块**
```java
() -> {
    for (int i = 0; i < 100; i++) {
        doWork();
    }
}
```

### 函数式接口
在Java8中新增加了一个注解: @FunctionalInterface，函数式接口。
什么是函数式接口呢？它包含了以下特征：
- 接口中仅有一个抽象方法，但允许存在默认方法和静态方法。
- @FunctionalInterface注解不是必须的，但建议最好加上，这样可以通过编译器来检查接口中是否仅存在一个抽象方法。

Lambda表达式的本质就是函数式接口的匿名实现。只是把原有的接口实现方式用一种更像函数式编程的语法表示出来。
Java8的java.util.function包已经内置了大量的函数式接口，如下所示：

| 函数式接口 | 参数类型 | 返回类型 | 方法名 | 描述 |
|--------|--------|--------|--------|--------|
| Supplier&lt;T&gt; | 无 | T | get | 产生一个类型为T的数据 |
| Consumer&lt;T&gt; | T | void | accept | 消费一个类型为T的数据 |
| BiConsumer&lt;T,U&gt; | T,U | void | accept | 消费类型为T和类型为U的数据 |
| Function&lt;T,R&gt; | T | R | apply | 把参数类型为T的数据经过函数处理转换成类型为R的数据 |
| BiFunction&lt;T,U,R&gt; | T,U | R | apply | 把参数类型为T和U的数据经过函数处理转换成类型为R的数据 |
| UnaryOperator&lt;T&gt; | T | T | apply | 对类型T进行了一元操作，仍返回类型T |
| BinaryOperator&lt;T&gt; | T,T | T | apply | 对类型T进行了二元操作，仍返回类型T |
| Predicate&lt;T&gt; | T | booelan | test | 对类型T进行函数处理，返回布尔值 |
| BiPredicate&lt;T,U&gt; | T,U | boolean | test | 对类型T和U进行函数处理，返回布尔值 |
从中可以看出：

- 内置的函数式接口主要分四类：Supplier， Consumer， Function，Predicate。Operator是Function的一种特例。
- 除了Supplier没有提供二元参数以为（这和java不支持多个返回值有关），其他三类都提供了二元入参。

以下是一个综合的例子：
```java
public class FunctionalCase {
    public static void main(String[] args) {
        String words = "Hello, World";

        String lowerWords = changeWords(words, String::toLowerCase);
        System.out.println(lowerWords);

        String upperWords = changeWords(words, String::toUpperCase);
        System.out.println(upperWords);

        int count = wordsToInt(words, String::length);
        System.out.println(count);

        isSatisfy(words, w -> w.contains("hello"));

        String otherWords = appendWords(words, ()->{
            List<String> allWords = Arrays.asList("+abc", "->efg");
            return allWords.get(new Random().nextInt(2));
        });
        System.out.println(otherWords);

        consumeWords(words, w -> System.out.println(w.split(",")[0]));
    }

    public static String changeWords(String words, UnaryOperator<String> func) {
        return func.apply(words);
    }

    public static int wordsToInt(String words, Function<String, Integer> func) {
        return func.apply(words);
    }

    public static void isSatisfy(String words, Predicate<String> func) {
        if (func.test(words)) {
            System.out.println("test pass");
        } else {
            System.out.println("test failed.");
        }
    }

    public static String appendWords(String words, Supplier<String> func) {
        return words + func.get();
    }

    public static void consumeWords(String words, Consumer<String> func) {
        func.accept(words);
    }
}
```

如果觉得这些内置函数式接口还不够用的话，还可以自定义自己的函数式接口，以满足更多的需求。

### 方法引用
如果Lambda表达式已经有实现的方法了，则可以用方法引用进行简化。
方法引用的语法如下：

- 对象::实例方法
- 类::静态方法
- 类::实例方法

这样前面提到的Lambda表达式：
```java
event -> System.out.println(event)
```
则可以替换为：
```java
System.out::println
```

另一个例子：
```java
(x,y)->x.compareToIgnoreCase(y)
```
可以替换为：
```java
String::compareToIgnoreCase
```

注意：<font color="red">**方法名后面是不能带参数的！**</font>
可以写成System.out::println，但不能写成System.out::println("hello")

如果能获取到本实例的this参数，则可以直接用**this::实例方法**进行访问，对于父类指定方法，用**super::实例方法**进行访问。
下面是一个例子：
```java
public class Greeter {
    public void greet() {
        String lowcaseStr = changeWords("Hello,World", this::lowercase);
        System.out.println(lowcaseStr);
    }

    public String lowercase(String word) {
        return word.toLowerCase();
    }

    public String changeWords(String words, UnaryOperator<String> func) {
        return func.apply(words);
    }
}

class ConcurrentGreeter extends Greeter {
    public void greet() {
        Thread thread = new Thread(super::greet);
        thread.start();
    }

    public static void main(String[] args) {
        new ConcurrentGreeter().greet();
    }
}
```

### 构造器引用
构造器引用和方法引用类似，只不过函数接口返回实例对象或者数组。
构造器引用的语法如下：

- 类::new
- 数组::new

举个例子：
```java
List<String> labels = Arrays.asList("button1", "button2");
Stream<Button> stream = labels.stream().map(Button::new);
List<Button> buttons = stream.collect(Collectors.toList());
```
其中的labels.stream().map(Button::new)相当于
labels.stream().map(label->new Button(label))

再看个数组类型的构造器引用的例子：
```java
Button[] buttons = stream.toArray(Button[]::new);
```
把Stream直接转成了数组类型，这里用Button[]::new来标示数组类型。

### 变量作用域
先看一段代码：
```java
  public void repeatMsg(String text, int count) {
      Runnable r = () -> {
          for (int i = 0; i < count; i++) {
              System.out.println(text);
              Thread.yield();
          }
      };
  }
```
一个lambda表达式一般由以下三部分组成：

- 参数
- 表达式
- 自由变量

参数和表达式好理解。那自由变量是什么呢？ 它就是在lambda表达式中引用的外部变量，比如上例中的text和count变量。
如果熟悉函数式编程的同学会发现，Lambda表达式其实就是"闭包"(closure)。只是Java8并未叫这个名字。
对于自由变量，如果Lambda表达式需要引用，是不允许发生修改的。其实在Java的匿名内部类中，如果要引用外部变量，变量是需要声明为final的，虽然Lambda表达式的自由变量不用强制声明成final，但同样也是不允许修改的。
比如下面的代码：
```java
  public void repeatMsg(String text, int count) {
      Runnable r = () -> {
          while (count > 0) {
              count--;  // 错误，不能修改外部变量的值
              System.out.println(text);
          }
      };
  }
```

另外，Lambda表达式中不允许声明一个和局部变量同名的参数或者局部变量。
比如下面的代码：
```java
Path first = Paths.get("/usr/bin");
Comparator<String> comp = (first, second) -> Integer.compare(first.length(), second.length());
// 错误，变量first已经被定义
```

### 接口中的默认方法
先说说为什么要在Java8接口中新增默认方法吧。
比如Collection接口的设计人员针对集合的遍历新增加了一个forEach()方法，用它可以更简洁的遍历集合。
比如:
```java
list.forEach(System.out::println());
```
但如果在接口中新增方法，按照传统的方法，Collection接口的自定义实现类都要实现forEach()方法，这对广大已有实现来说是无法接受的。
于是Java8的设计人员就想出了这个办法：在接口中新增加一个方法类型，叫默认方法，可以提供默认的方法实现，这样实现类如果不实现方法的话，可以默认使用默认方法中的实现。
一个使用例子：
```java
public interface Person {
    long getId();
    default String getName() {
        return "jack";
    }
}
```
默认方法的加入，可以替代之前经典的接口和抽象类的设计方式，统一把抽象方法和默认实现都放在一个接口中定义。这估计也是从Scala的Trait偷师来的技能吧。

### 接口中的静态方法
除了默认方法，Java8还支持在接口中定义静态方法以及实现。
比如Java8之前，对于Path接口，一般都会定义一个Paths的工具类，通过静态方法实现接口的辅助方法。
接口中有了静态方法就好办了， 统一在一个接口中搞定！虽然这看上去破坏了接口原有的设计思想。
```java
public interface Path{
  public static Path get(String first, String... more) {
    return FileSystem.getDefault().getPath(first, more);
  }
}
```
这样Paths类就没什么意义了~





