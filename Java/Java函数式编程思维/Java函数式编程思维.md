## Java函数式编程思维

修订记录

| 时间 | 内容 |
|--------|--------|
| 2016.10.29 | 初稿 |

[TOC]

### 引言
一直以来，Java都被认为是一种面向对象的编程语言，“万事万物皆对象”的思想已经深入人心。但随着Java8的发布，一切看起来似乎有些改变。Lambda表达式和Stream的引入，让Java焕发了新的活力，它允许人们可以用函数式编程思维思考问题。本文主要介绍了函数式编程思想在Java中的应用。

### 指令式还是声明式？
先看一段代码：计算商品价格的最大值。
我们一般会这样实现：
```java
int max = 0;
for(int price : prices) {
    if (max < price) max = price;
}
```
这就是典型的“指令式”(imperative)写法。它利用计算机的指令或者语法，告诉计算机一步步要做什么。
针对同样的功能，再看看另一种写法：
```java
int max = prices.stream().reduce(0, Math::max);
```
这段简洁的代码就是“声明式”(declarative)写法。他更像是告诉计算机要实现什么样的功能，而不关注计算机内部如何处理。
如果熟悉软件设计原则的读者会发现，这正是“好莱坞原则”的使用，Tell，Don't Ask!

### 用函数式思想实现设计模式
在GoF的经典著作《设计模式》一书中，详细介绍了23种常见的设计模式。细心的读者可能会发现，书名下面还有一行小字：可复用面向对象软件的基础。也就是说它是用面向对象思想实现的。这么多年过去了，对设计模式的争论一直在进行，但这已不再重要，今天让我们以函数式编程的角度重新审视设计模式。

#### 命令模式
命令模式一般会对命令进行封装，对外提供接口供使用者调用。
先看一个例子：扫地机器人可以执行直行、左转、右转等指令操作。
先定义指令接口和实现类：
```java
// 命令接口
public interface Command {
    void execute();
}

// 前进命令实现
public class forward implements Command {
    public void execute() {
        System.out.println("go forward");
    }
}

// 右转命令实现
public class Right implements Command {
    @Override
    public void execute() {
        System.out.println("go right");
    }
}
```
下面实现机器人：
```java
public class Robot {
    public static void move(Command... commands) {
        for (Command command : commands) {
            command.execute();
        }
    }

    public static void main(String[] args) {
        Robot.move(new Forward(), new Right());
    }
}
```
虽然功能实现了，但有一个问题就是：创建的类太多！业务逻辑本来是要关注的焦点，但却被淹没在过多的类实现中。
我们看看函数式编程怎么实现？
因为Command接口中的execute()是一个无入参和无返回结果的方法，这让我们很自然的想起了Java内置的函数式接口Runnable，它也有一个同样签名的run()方法。虽然Runnable接口本来是用在多线程处理中的，但这里我们取巧的用在函数式编程中。
首先我们把Robot类的move()方法的入参替换为Runnable接口：
```java
    public static void flexibleMove(Runnable... commands) {
        Stream.of(commands).forEach(Runnable::run);
    }
```
这样一来，我们只需要在类方法中实现命令就可以了。
```java
    public static void forward() {
        System.out.println("go forward");
    }

    public static void right() {
        System.out.println("go right");
    }
```

调用就变成了：
```java
Robot.flexibleMove(Robot::forward, Robot::right);
```
这种实现减少了很多命令实现类，把焦点更多放在业务逻辑上。

#### 策略模式
策略模式可以通过注入不同的实现策略，从而实现接口和实现的分离。
先看一个用面向对象思想实现的策略模式：对文本设置不同的格式化策略，从而进行不同的输出。
下面是代码实现：
定义文本编辑器类，构造函数实现默认格式化策略，也可以通过方法设置其他的格式化策略，
```java
public class Editor {
    private Formatter formatter;
    private String text;

    public Editor(String text) {
        this.text = text;
        this.formatter = new DefaultFormatter();
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public String output() {
        return formatter.format(text);
    }
}    
```
各种策略的实现：
```java
// 默认实现策略，也即原文本输出
public class DefaultFormatter implements Formatter {
    @Override
    public String format(String text) {
        return text;
    }
}

// 转成大写输出策略
public class UppercaseFormatter implements Formatter {
    @Override
    public String format(String text) {
        return text.toUpperCase();
    }
}
```
客户端调用：
```java
String text = "Hello, World";
Editor editor = new Editor(text);
String defaultText = editor.output();
editor.setFormatter(new UppercaseFormatter());
String upperText = editor.output();
```
这是标准的策略模式实现，我们再看看如何用函数式的方式实现？
从formatter的format方法看入参和返回结果都是String类型，自然会想到Java内置的函数式接口UnaryOperator。
下面是代码的实现：
文本类:
```java
public class EditorPlus {
    private String text;
    private UnaryOperator<String> formatter;

    public EditorPlus(String text) {
        this.text = text;
        this.formatter = s -> s;
    }

    public void setFormatter(UnaryOperator<String> formatter) {
        this.formatter = formatter;
    }

    public String output() {
        return formatter.apply(text);
    }
}    
```
客户端调用：
```java
EditorPlus editor = new EditorPlus("Hello, World");
String defaultText = editor.output();
editor.setFormatter(String::toUpperCase);
String upperText = editor.output();
```
formatter的类型变成了UnaryOperator，这样默认实现就可以写成Lambda表达式：s->s， 其他的格式化策略也都可以用Lambda表达式实现，不再需要写那么多的策略实现类了。

#### 装饰模式
装饰模式可以在不改变原组件行为的基础上增加新的特性，特性本身也就是可以叠加的。
先看一个使用面向对象思想实现的需求：给相机增加滤镜功能，滤镜可以有多个。
先定义一个设备接口, 只包含获取颜色的方法：
```java
public interface Equipment {
    Color getColor();
}
```
再定义相机类，实现了设备接口：
```java
public class Camera implements Equipment {
    private Color color;

    public Camera(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return this.color;
    }
}
```
我们再定义一个滤镜的抽象类，也实现了设备接口：
```java
public abstract class FilterDecorator implements Equipment {
    protected Equipment equipment;
    
    public FilterDecorator(Equipment equipment) {
        this.equipment = equipment;
    }
    
    public abstract Color getColor();
}
```
接着定义两个滤镜：变亮和变暗，继承滤镜抽象类。
```java
public class BrighterFilter extends FilterDecorator {
    public BrighterFilter(Equipment equipment) {
        super(equipment);
    }

    @Override
    public Color getColor() {
        return equipment.getColor().brighter();
    }
}

public class DarkerFilter extends FilterDecorator {
    public DarkerFilter(Equipment equipment) {
        super(equipment);
    }

    @Override
    public Color getColor() {
        return equipment.getColor().darker();
    }
}
```
客户端的调用为：
```java
Equipment decoratedCamera = new DarkerFilter(
                                new BrighterFilter(
                                    new Camera(new Color(155, 120, 30))));
decoratedCamera.getColor()

```
这段代码实现的中规中矩，让我们再看看如何用函数式的方式实现：
这里的滤镜功能是把一个Color转成另一个Color，所以很自然想到了Java自带的函数式接口Function。它有一个compose方法可以实现方法的组合。
下面看代码的实现：
```java
public class Camera {
    private Color color;
    private Stream<Function<Color, Color>> filterStream;

    public Camera(Color color, Function<Color, Color>... filters) {
        this.color = color;
        this.filterStream = Stream.of(filters);
    }

    public Color getColor() {
        Function<Color, Color> composedFilter = filterStream.reduce(
                (filter, next) -> filter.compose(next))
                    .orElse(color -> color);
        return composedFilter.apply(this.color);
    }
}    
```
Camera的输入由原有的Equipment接口，变成了Function列表。并且内部用reduce()结合compose()方法实现特性的累加。代码简化了很多。
最后再看看客户端调用：
```java
Camera camera = new Camera(new Color(155, 120, 30), Color::brighter, Color::darker);
camera.getColor();
```
通过方法引用，让代码进一步简化。
从上述三个模式的函数式实现可以看出，使用函数式的方式可以大幅减少类的数量，代码也更精简，语义也更明确。

### 设计连贯接口
连贯接口(Fluent Interface)是内部DSL一种常用的设计手法。它通常采用Builder设计模式让方法返回this对象，这样方法就能像链一样调用，形成连贯接口。
先看一个连贯接口的例子：设计一个邮件发送功能，包括发送地址，目的地址，收件人，标题，主题等。
```java
// 定义一个邮件发送builder
public class MailBuilder {
    public MailBuilder from(final String address) {return this; }
    public MailBuilder to(final String address) {return this; }
    public MailBuilder subject(final String line) {return this; }
    public MailBuilder body(final String message) {return this; }
    public void send() { System.out.println("sending..."); }
}    
```
客户端调用为：
```java
new MailBuilder()
        .from("ding.yi@zte.com.cn")
        .to("wxcop@zte.com.cn")
        .subject("article")
        .body("fp in java")
        .send();
```

这就是我们常见的连贯接口的使用方式。但它也有两个小问题：
1. new的方式让连贯接口的可读性降低。
1. 这还是“指令式”的写法，先构造邮件体，再发送邮件。更好的语义是mailer.send(mail)的方式。

那能不能做到这两点呢？使用函数式接口是可以做到的。
在Java内置的函数式接口中，有一个叫Consumer的接口，可以用来接收参数进行消费，这和我们的意图正好相符。
代码实现如下：
```java
public class Mailer {
    private Mailer() {}

    public Mailer from(final String address) { return this; }
    public Mailer to(final String address) { return this; }
    public Mailer subject(final String line) { return this; }
    public Mailer body(final String message) { return this; }

    public static void send(final Consumer<Mailer> block) {
        final Mailer mailer = new Mailer();
        block.accept(mailer);
        System.out.println("sending...");
    }
}    
```
从上面的实现可以看出，构造函数变成了私有，send()方法变成了静态的，并且接收一个Consumer类型的block。
这样客户端调用就变成了：
```java
Mailer.send(mail ->
            mail.from("ding.yi@zte.com.cn")
                .to("wxcop@zte.com.cn")
                .subject("article")
                .body("fp in java"));
```
这样的代码既保留了连贯接口，也更具有表现力。

### 使用资源
我们日常开发中经常会对资源进行操作，比如数据库连接，文件操作，锁操作等。这些对资源的操作有一个共性特点：先打开资源，然后对资源进行操作，最后关闭资源。代码通常会这样写：
```java
resource.open();
try {
    doSomethingWith(resource);
} finally {
   resource.close();
}
```
这是一段样板代码，看似简单，但在使用的过程中，总会有粗心的程序员忘记加上finally语句来释放资源，从而导致内存泄露。
那是否有一种方法能在操作完资源后自动关闭资源呢？
利用函数式接口是可以做到的，可以把Consumer接口传递到样板代码中，这样客户端只用关注对资源的操作处理就可以了，关闭操作会自动完成。
代码实现如下：
```java
public static void handle(Consumer<Resource> consumer) {
    Resource resource = new Resource();
    try {
        consumer.accept(resource);
    } finally {
        resource.close();
    }
}
```
这样在客户端使用的时候，就可以写成：
```java
handle(resource -> doSomethinWith(resource));
```
这样再也不用担心使用资源后，忘记释放资源了！

### 延迟加载
延迟加载是函数式编程的一个重要特征，使用得当的话，可以很好的提升软件性能。
下面看一个例子：对一个耗时较长的操作，执行“与”操作。
先看看没有使用延迟加载的效果：
```java
public class Evaluation {
    public static boolean evaluate(final int value)  {
        System.out.println("evaluating ..." + value);
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value > 100;
    }

    public static void eagerEvaluator(final boolean input1, final boolean input2) {
        System.out.println("eagerEvaluator called...");
        System.out.println("accept?: " + (input1 && input2));
    }
    
    public static void main(String[] args) throws InterruptedException {
        eagerEvaluator(evaluate(1), evaluate(2));
    }
}
```
evaluate()操作是一个耗时操作，比如耗时20s，eagerEvaluator()方法传入的是两个布尔结果，在内部进行“与”操作，由于两个evaluate()方法必须都要执行完，所以总的操作时间至少需要40s。
我们知道，Java中的&&操作是一个短路操作，也就是说，如果第一个条件结果为false，就不再进行后续条件的判断，直接返回false。
我们是否能用上这个特性呢？如果传给evaluate()方法的参数是一个函数式接口，这样它就不会立即执行，而是等到真正调用的时候才执行，从而达到使用短路操作的效果。
下面是代码的实现：
```java
public static void lazyEvaluator(final Supplier<Boolean> input1, final Supplier<Boolean> input2) {
    System.out.println("lazyEvaluator called...");
    System.out.println("accept?: " + (input1.get() && input2.get()));
}
```
传给lazyEvaluator()方法的是一个Supplier函数式接口，这样在调用的时候就可以写成：
```java
lazyEvaluator(() -> evaluate(1), () -> evaluate(2));
```
这样传入的lambda表达式，只有在调用时才执行，由于第一个条件返回false，就执行了短路操作，直接返回了结果。这样操作时间缩短了一半，性能提升明显。

### 结语
虽然Java引入了函数式编程元素，但也许Java终究不可能成为一门函数式编程语言，但这并不能妨碍我们使用函数式编思维解决问题。世界上的问题终究不是对立的，或许把面向对象和函数式编程结合起来使用，可能会取得意想不到的效果。
