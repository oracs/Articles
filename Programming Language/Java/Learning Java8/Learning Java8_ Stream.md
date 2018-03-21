修订记录

| 时间 | 作者 | 内容 |
|--------|--------|--------|
| 2016.10.11 | 丁一 | 初稿 |

### 引言
先从一个例子开始，看看为什么在Java8中要引入流(Stream)?
比如实现这么一个需求：在学生集合中查找男生的数量。
传统的写法为：

```java
public long getCountsOf
MaleStudent(List<Student> students) {
    long count = 0;
    for (Student student : students) {
        if (student.isMale()) {
            count++;
        }
    }
    return count;
}
```
看似没什么问题，因为我们写过太多类似的"样板"代码，尽管智能的IDE通过code template功能让这一枯燥过程变得简化，但终究不能改变冗余代码的本质。
再看看使用流的写法：

```java
 public long getCountsOfMaleStudent(List<Student> students) {
     return students.stream().filter(Student::isMale).count();
 }
```
一行代码就把问题解决了！
虽然读者可能还不太熟悉流的语法特性，但这正是函数式编程思想的体现：

- 回归问题本质，按照心智模型思考问题。
- Stream提供的高阶函数以及链式调用，可以对数据进行更高层的抽象和控制。
- 很容易转成并行流，提供并行计算的能力。
- 延迟加载。
- 简化代码。

下面正式进入流的介绍。

### 创建流
创建流的方式可以有很多种，其中最常见的方式是通过Collection的Stream()方法或者Arrays的Stream()方法来生成流。
比如：

```java
List<Integer> numbers = Arrays.asList(1, 2, 3);
Stream<Integer> numberStream = numbers.stream();

String[] words = new String[]{"one", "two"};
Stream<String> wordsStream = Arrays.stream(words);
```

当然Stream接口本身也提供了许多和流相关的操作。

```java
// 创建流
Stream<Integer> numbers = Stream.of(1, 2, 3);
// 创建空流
Stream<String> emptyStream = Stream.empty();
// 创建一个元素为“hi”的无限流
Stream<String> infiniteString = Stream.generate(() -> "hi");
// 创建一个从0开始的递增无限流
Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
```
其中Stream.generate()和Stream.iterate()产生的都是无限流，如果要把他们截取为有限流，可以使用limit()方法，
比如：

```java
Stream<Double> top10 = Stream.generate(Math::random).limit(10);
```
另外，可以通过skip()方法跳过元素，concat()方法连接两个流。

```java
Stream<Integer> skipedStream = Stream.of(1, 2, 3, 4).skip(2); // 3, 4
Stream<String> concatedStream = Stream.concat(Stream.of("hello"), Stream.of(",world")); // hello,world
```

### 常用的流操作
#### filter
filter()方法的作用就是根据输入的条件表达式过滤元素。
接口定义如下：

```java
Stream<T> filter(Predicate<? super T> predicate);
```
从中可以看出，输入参数是一个Predicate，也即是一个条件表达式。
一个例子：

```java
Stream.of("a", "1b", "c", "0x").filter(value -> isDigit(value.charAt(0)));
```
过滤出第一个字符是数字的元素。
输出结果为：
> 1b, 0x

#### map
map()的主要作用是通过映射函数转换成新的数据。
接口定义如下：

```java
<R> Stream<R> map(Function<? super T, ? extends R> mapper);
```
从中可以看出，输入参数是一个Function。
一个例子：

```java
Stream.of("a", "b", "c").map(String::toUpperCase);
```
把字符串转换成大写。
输出结果：
> A, B, C

#### flatMap
flatMap()的作用类似于map()，但它通过Function返回的依然是一个Stream，也即是把多个Stream转换成一个扁平的Stream。
接口定义如下：

```java
<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
```
一个例子：

```java
Stream<List<Integer>> listStream = Stream.of(asList(1, 2), asList(3, 4));
Stream<Integer> integerStream = listStream.flatMap(numbers -> numbers.stream());
```
它把两个list组成的Stream转成一个包含全部元素的Stream。
输出：
> [1, 2, 3, 4]

### 有状态的转换
在前面介绍的函数中，无论是map还是filter，都不会改变流的状态，也即结果并不依赖之前的元素。
除此之外，Java8也提供了有状态的转换，常用的操作是distinct和sorted。

#### distinct
distinct()的主要作用是去除流中的重复元素。和Oracle的distinct一个作用。
举例如下：

```java
Stream<String> distinctStream = Stream.of("one", "one", "two", "three").distinct(); 
```
去除字符串中的重复元素，返回结果为：
> one, two, three

#### sorted
sorted()的主要作用是对流按照指定的条件进行排序。
接口定义如下：

```java
Stream<T> sorted(Comparator<? super T> comparator);
```

从中可以看出，入参是一个Comparator，也即是一个函数式接口。
一个例子：

```java
Stream<String> sortedStream = Stream.of("one", "two", "three").sorted(Comparator.comparing(String::length).reversed());
```
对字符串按照长度进行降序排列。
注意，这里使用了Comparator.comparing方法来简化调用。
输出结果为：
> [three, one, two]

### Optional类型
在介绍下个主题前，先介绍一个Java8新增的数据结构：Optional。
Optional的主要作用是对结果进行了封装，结果可能有值，也可能没有值，并且对结果可以进行后续处理，比如添加默认值，映射其他值，抛出异常等。
下面是常用的操作举例：

```java
// 生成了一个Optional数据
Optional<String> maxStrOpt = Stream.of("one", "two", "three").max(String::compareToIgnoreCase);

// 如果值存在的情况下，把数据添加到List中
ArrayList<String> result = new ArrayList<String>();
maxStrOpt.ifPresent(result::add);

// 把结果映射为大写，然后取出。
Optional<String> upperResult = maxStrOpt.map(String::toUpperCase);
System.out.println(upperResult.get());

// 值为空的情况下的后续处理
maxStrOpt.orElse(""); // 添加默认值""
maxStrOpt.orElseGet(() -> System.getProperty("user.dir")); // 通过表达式返回结果
maxStrOpt.orElseThrow(RuntimeException::new); // 抛出异常
```

### 聚合操作
之前介绍的函数都是返回的Stream，根据Stream延迟加载的特性，它是不会真正执行的，只有在做了本节的聚合操作以及后续章节介绍的收集操作后，才会真正执行。
所谓聚合操作就是把一组数据通过操作聚合为一个结果的过程。
下面介绍常用的聚合操作：

#### count
count()的作用是统计元素的总数，很多时候需要配合filter一起使用。
一个例子：

```java
long count = Stream.of("one", "two", "three").filter(word -> word.contains("o")).count();
```
统计字符流中包含字符o的单词数量。
结果：
> 2

#### max/min
max/min()的主要作用是取得元素的最大值/最小值。
接口定义如下：

```java
Optional<T> max(Comparator<? super T> comparator);
Optional<T> min(Comparator<? super T> comparator);
```
从中可以看出，入参是一个Comparator函数式结果，返回的是一个Optional。
一个例子：

```java
Optional<String> maxStrOpt = Stream.of("one", "two", "three").max(String::compareToIgnoreCase);
System.out.println(maxStrOpt.get());
```
按照字母表比较，统计最大值，结果为：
> two

#### findFirst/findAny
findFirst()的主要作用是找到第一个匹配的结果。
findAny()的主要作用是找到找到任意匹配的一个结果。它在并行流中特别有效，因为只要在任何分片上找到一个匹配元素，整个计算就会结束。
返回结果都是Optional。
接口定义如下：

```java
Optional<T> findFirst();
Optional<T> findAny();
```
一个例子：

```java
 Optional<String> findFirstResult = Stream.of("one", "two", "three").filter(word -> word.contains("o").findFirst();
 System.out.println(findFirstResult.get());
 Optional<String> findAnyResult = Stream.of("one", "two", "three").filter(word -> word.contains("t").findAny();        
 System.out.println(findAnyResult.get());
```
结果为：
> one
> two

#### anyMatch/allMatch/noneMatch
如果只关心是否匹配成功，即返回boolean结果，则可以使用anyMatch/allMatch/noneMatch函数。
接口定义如下：

```java
boolean anyMatch(Predicate<? super T> predicate);
boolean allMatch(Predicate<? super T> predicate);
boolean noneMatch(Predicate<? super T> predicate);
```
其中,
anyMatch表示任意匹配(or);
allMatch表示全部匹配(and);
noneMatch表示不匹配(not)。
一个例子：

```java
boolean anyMatch = Stream.of("one", "two", "three").anyMatch(word -> word.contains("o"));
boolean allMatch = Stream.of("one", "two", "three").allMatch(word -> word.contains("o"));
boolean noneMatch = Stream.of("one", "two", "three").noneMatch(word -> word.contains("o");
System.out.println(anyMatch + ", " + allMatch + ", " + noneMatch);
```
结果为：
> true, false, false

#### reduce
reduce()主要进行归约操作，它提供了三种不同的用法。
**用法1：**
接口定义：

```java
Optional<T> reduce(BinaryOperator<T> accumulator);
```
它主要接收一个BinaryOperator的累加器，返回Optional类型。
一个例子：

```java
Optional<Integer> sum1 = Stream.of(1, 2, 3).reduce((x, y) -> x + y);
System.out.println(sum1.get());
```
对数字流求和，结果为：
> 6

**用法2：**
接口定义：

```java
T reduce(T identity, BinaryOperator<T> accumulator);
```

和上一个方法不一样的地方是：它提供了一个初始值identity，这样就保证整个计算结果时不可能为空，所以不再返回Optional，直接返回对应的类型T。
一个例子：

```java
Integer sum2 = Stream.of(1, 2, 3).reduce(10, (x, y) -> x + y);
System.out.println(sum2);
```
结果为：
> 16

**用法3：**
接口定义：

```java
<U> U reduce(U identity,
               BiFunction<U, ? super T, U> accumulator,
               BinaryOperator<U> combiner);
```
这是最复杂的一种用法，它主要用于把元素转换成不同的数据类型。
accumulator是累加器，主要进行累加操作，combiner是把不同分段的数据组合起来(并行流场景)。
一个例子：

```java
Integer sum3 = Stream.of("on", "off").reduce(0, (total, word) -> total + word.length(), (x, y) -> x + y);
System.out.println(sum3);
```
统计元素的单词长度，并累加在一起，结果为：
> 5

### 收集操作 (collect)
collect()方法主要用于把流转换成其他的数据类型。
#### 转换成集合
可以通过Collectors.toList()/toSet()/toCollection()方法转成List，Set，以及指定的集合类型。
一个例子：

```java
List<Integer> numbers = asList(1, 2, 3, 4, 5);
// 转换成List
List<Integer> numberList = numbers.stream().collect(toList());
// 转换成Set
Set<Integer> numberSet = numbers.stream().collect(toSet());
// 通过toCollection转成TreeSet
TreeSet<Integer> numberTreeSet = numbers.stream().collect(Collectors.toCollection(TreeSet::new));
```
注：

- 这里对类似Collectors.toList的方法实施了静态导入。
- toList()默认转成ArrayList，toSet()默认转成HashSet，如果这两种数据类型都不满足要求的话，可以通过toCollectio()方法转成需要的集合类型。

#### 转换成值
除了转成集合外，还可以把结果转成值。
常用的转换函数包括：

- Collectors.summarizingInt()/summarizingLong()/summarizingDouble()  // 获取统计信息，进行求和、平均、数量、最大值、最小值。
- Collectors.maxBy()/minBy() // 求最大值/最小值
- Collectors.counting() // 求数量
- Collectors.summingInt()/summingLong()/summingDouble() // 求和
- Collectors.averagingInt()/averagingDouble()/averagingDouble() // 求平均
- Collectors.joining() // 对字符串进行连接操作

一个例子：

```java
List<String> wordList = Arrays.asList("one", "two", "three");

// 获取统计信息，打印平均和最大值
IntSummaryStatistics summary = wordList.stream().collect(summarizingInt(String::length));
System.out.println(summary.getAverage() + ", " + summary.getMax());

// 获取单词的平均长度
Double averageLength = wordList.stream().collect(averagingInt(String::length));

// 获取最大的单词长度
Optional<String> maxLength = wordList.stream().collect(maxBy(Comparator.comparing(String::length)));

```
这些方法的共同特点是：返回的数据类型都是Collector。虽然可以单独在Collect()方法中使用，但实际却很少这样用（毕竟Stream本身也提供了类似的方法），它更常用的用法是配合groupingBy()方法一起使用，以便对分组后的数据进行二次加工。

### 分区操作(partitioningBy)
partitioningBy操作是基于collect操作完成的，它会根据条件对流进行分区操作，返回一个Map，Key是boolean型，Value是对应分区的List，也就是说结果只有符合条件和不符合条件两种。
接口定义如下：

```java
public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(Predicate<? super T> predicate)
```
一个例子：

```java
public Map<Boolean, List<Student>> maleAndFemaieStudents(Stream<Student> students) {
    return students.collect(Collectors.partitioningBy(student -> student.isMale()));
}
```
按性别对学生流进行分区，结果保存在Map中。

### 分组操作(groupingBy)
groupingBy操作也是基于collect操作完成的，功能是根据条件进行分组操作，他和partitioningBy不同的一点是，它的输入是一个Function，这样返回结果的Map中的Key就不再是boolean型，而是符合条件的分组值，使用场景会更广泛。
接口定义如下：

```java
public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends K> classifier)
```
一个例子

```java
public Map<String, List<Student>> studentByName(Stream<Student> students) {
    return students.collect(Collectors.groupingBy(student -> student.getName()));
}
```
按照学生的姓名进行分组。
之前也提过，groupingBy函数可以配合聚合函数做更复杂的操作。
下面介绍几种常见的使用场景：

#### 按照城市所在的州进行分组，再统计数量

```java
public Map<String, Long> stateToCount(Stream<City> cities) {
    return cities.collect(groupingBy(City::getState, counting()));
}
```
#### 按照城市所在的州进行分组，再统计人口总数

```java
public Map<String, Integer> stateToCityPopulation(Stream<City> cities) {
    return cities.collect(groupingBy(City::getState, summingInt(City::getPopulation)));
}
```

#### 按照城市所在的州进行分组，再找出每州人口最多的城市

```java
public Map<String, City> stateToLargestCity(Stream<City> cities) {
    return cities.collect(groupingBy(City::getState, maxBy(Comparator.comparing(City::getPopulation))));
}
```
#### 按照城市所在的州进行分组，再找出每州城市名最长的名称

```java
public Map<String, Optional<String>> stateToLongestCityName(Stream<City> cities) {
    return cities.collect(groupingBy(City::getState, mapping(City::getName, maxBy(Comparator.comparing(String::length)))));
}
```
#### 按照城市所在的州进行分组，再按照人口获取统计信息。利用统计信息可以执行求和、平均、数量、最大/最小值

```java
public Map<String, IntSummaryStatistics> stateToCityPopulationSummary(Stream<City> cities) {
    return cities.collect(groupingBy(City::getState, summarizingInt(City::getPopulation)));
}
```
#### 按照城市所在的州进行分组，再把每州的城市名连接起来

```java
public Map<String, String> stateToCityNames(Stream<City> cities) {
    return cities.collect(groupingBy(City::getState,
                reducing("", City::getName, (s, t) -> s.length() == 0 ? t : s + ", " + t)));
}
```

#### 按照城市所在的州进行分组，再把每州的城市名连接起来，使用joining函数

```java
public Map<String, String> stateToCityNames2(Stream<City> cities) {
    return cities.collect(groupingBy(City::getState,
                mapping(City::getName, joining(", "))));
}
```

从以上例子可以看出，groupingBy函数配合聚合函数可以组成表示出很复杂的应用场景。

### 基本类型流(IntStream,LongStream,DoubleStream)
在前面介绍的流中，都是使用的Stream配合泛型来标示元素类型的。
Java8中还为基本数据类型提供了更直接的流方式，以简化使用。

- 对于byte，short，int，char，booelan类型可以使用 **IntStream**；
- 对于long类型可以使用 **LongStream**；
- 对于float和Double类型可以使用 **DoubleStream**。

创建基本类型流的例子：

```java
IntStream intStream = IntStream.of(1, 2, 3);
IntStream rangeStream = IntStream.range(1, 10);  // 不包含上限10
IntStream rangeClosedStream = IntStream.rangeClosed(1, 10);  // 包含上限10
```
基本类型流还直接提供了sum, average, max, min等在Stream中并没有的方法。
还有一个mapToInt/mapToLong/mapToDouble方法把流转成基本类型流。
利用这两个个特性，可以方便执行某些操作，再看一个例子。

```java
Stream<String> twoWords = Stream.of("one", "two");
int twoWordsLength = twoWords.mapToInt(String::length).sum();
```
对原始字符串流统计字符总长度。

### 在文件操作中使用流
文件操作也是我们平时用的比较多的一种操作，利用流也可以帮助我们简化操作。
#### 访问目录和过滤

```java
Files.list(Paths.get(".")).forEach(System.out::println);
Files.list(Paths.get(".")).filter(Files::isDirectory);
```
#### 按扩展名过滤文件

```java
Files.newDirectoryStream(Paths.get("."), path -> path.toString().endsWith("md")).forEach(System.out::println);
File[] textFiles = new File(".").listFiles(path -> path.toString().endsWith("txt"));
```
#### 访问子目录

```java
List<File> allFiles = Stream.of(new File(".").listFiles()).flatMap(file -> file.listFiles() == null ? Stream.of(file) : Stream.of(file.listFiles())).collect(toList());
```