
## JavaScript的使用
JavaScript脚本必须位于 `<script>` 与 `</script>` 标签之间，可被放置在 HTML 页面的 `<body>` 和 `<head>` 部分中。

###` <body>` 中的 JavaScript
```JavaScript
<body>
  <script>
    document.write("<h1>This is a heading</h1>");
    document.write("<p>This is a paragraph</p>");
  </script>
</body>
```

### `<head>` 中的 JavaScript 函数
```JavaScript
<head>
    <script>	
        function myFunction()
        {
            document.getElementById("demo").innerHTML="My First JavaScript Function";
        }
    </script>
</head>
<body>
  <p id="demo">A Paragraph</p>
  <button type="button" onclick="myFunction()">Try it</button>
</body>
```

### 外部的 JavaScript
```JavaScript
<!DOCTYPE html>
<html>
  <body>
    <script src="myScript.js"></script>
  </body>
</html>
```

## 输出
读取元素：`document.getElementById("demo").innerHTML="My First JavaScript"`
写到文档：`document.write("<p>My First JavaScript</p>")`

## 变量
使用 var 关键词来声明变量：
```javascript
var x=2;
var y=3;
var z=x+y;
var name="Thomas";
```
也可以在一条语句中声明很多变量。该语句以 var 开头，并使用逗号分隔。
```javascript
var name="Gates", age=56, job="CEO";
```

如果只声明变量并没有赋值，则变量值为：undefined。

## 数据类型
JavaScript支持的数据类型有：字符串、数字、布尔、数组、对象、Null、Undefined。

JS是动态类型语言，

```javascript
// 布尔型
var x=true
var y=false

// 数组
var cars=new Array();
cars[0]="Audi";
cars[1]="BMW";
cars[2]="Volvo";

var cars=new Array("Audi","BMW","Volvo");
var cars=["Audi","BMW","Volvo"];

// 对象，由花括号分隔。
var person={
  firstname : "Bill",
  lastname  : "Gates",
  id        :  5566
};

name=person.lastname;
name=person["lastname"];

// Undefined 和 Null
Undefined 这个值表示变量不含有值。
可以通过将变量的值设置为 null 来清空变量。
```

## 函数
定义函数的关键字是function，代码块用花括号包裹。
```javascript
<!DOCTYPE html>
<html>
<head>
<script>
function myFunction()
{
alert("Hello World!");
}
</script>
</head>

<body>
<button onclick="myFunction()">点击这里</button>
</body>
</html>
```

### 带参数的函数
```javascript
<button onclick="myFunction('Bill Gates','CEO')">点击这里</button>

<script>
function myFunction(name,job)
{
alert("Welcome " + name + ", the " + job);
}
</script>
```

### 带有返回值的函数
```javascript
function myFunction(a,b)
{
return a*b;
}

document.getElementById("demo").innerHTML=myFunction(4,3);
```

## if语句
在 JavaScript 中，我们可使用以下条件语句：
•if 语句 - 只有当指定条件为 true 时，使用该语句来执行代码
•if...else 语句 - 当条件为 true 时执行代码，当条件为 false 时执行其他代码
•if...else if....else 语句 - 使用该语句来选择多个代码块之一来执行
•switch 语句 - 使用该语句来选择多个代码块之一来执行

```javascript
if (time<10)
  {
  x="Good morning";
  }
else if (time<20)
  {
  x="Good day";
  }
else
  {
  x="Good evening";
  }

```

## Switch 语句
```javascript
var day=new Date().getDay();
switch (day)
{
case 6:
  x="Today it's Saturday";
  break;
case 0:
  x="Today it's Sunday";
  break;
default:
  x="Looking forward to the Weekend";
}
```

$$ for语句
JavaScript 支持不同类型的循环：
•for - 循环代码块一定的次数
•for/in - 循环遍历对象的属性
•while - 当指定的条件为 true 时循环指定的代码块
•do/while - 同样当指定的条件为 true 时循环指定的代码块
•break 语句用于跳出循环。
•continue 用于跳过循环中的一个迭代。

```javascript
for (var i=0,len=cars.length; i<len; i++)
{
document.write(cars[i] + "<br>");
}
```

### For/In 循环
```javascript
var person={fname:"John",lname:"Doe",age:25};

for (x in person)
  {
  txt=txt + person[x];
  }
```

## while语句
### while 循环
```javascript
while (i<5)
  {
  x=x + "The number is " + i + "<br>";
  i++;
  }
```

### do/while 循环
```javascript
do
  {
  x=x + "The number is " + i + "<br>";
  i++;
  }
while (i<5);
```
## 异常

- try 语句测试代码块的错误。
- catch 语句处理错误。
- throw 语句创建自定义错误。

```javascript
<!DOCTYPE html>
<html>
<head>
<script>
var txt="";
function message()
{
try
  {
  adddlert("Welcome guest!");
  }
catch(err)
  {
  txt="There was an error on this page.\n\n";
  txt+="Error description: " + err.message + "\n\n";
  txt+="Click OK to continue.\n\n";
  alert(txt);
  }
}
</script>
</head>

<body>
<input type="button" value="View message" onclick="message()">
</body>

</html>

```


