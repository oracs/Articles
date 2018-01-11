nte
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

## HTML DOM
DOM: Document Object Model，文档对象模型。
通过 HTML DOM，可访问 JavaScript HTML 文档的所有元素。
当网页被加载时，浏览器会创建页面的DOM，它被构造为对象的树。

### DOM树
![ ](images\dom_tree.jpg)

通过可编程的对象模型，JavaScript 获得了足够的能力来创建动态的 HTML。

- 能够改变页面中的所有 HTML 元素
- 能够改变页面中的所有 HTML 属性
- 能够改变页面中的所有 CSS 样式
- 能够对页面中的所有事件做出反应

## DOM HTML操作
### 改变 HTML 输出流
```javascript
document.write(xxx);
```

### 改变 HTML 内容
使用 innerHTML 属性。
```javascript
document.getElementById(id).innerHTML=new HTML
```

### 改变 HTML 属性
使用 attribute 属性。
```javascript
document.getElementById(id).attribute=new value
```
## DOM CSS操作
### 改变 HTML 样式
```javascript
document.getElementById(id).style.property=new style
```
例子：
```javascript
<p id="p2">Hello World!</p>

<script>
	document.getElementById("p2").style.color="blue";
</script>
```

## DOM 事件操作
HTML 事件的例子：

- 当用户点击鼠标时
- 当网页已加载时
- 当图像已加载时
- 当鼠标移动到元素上时
- 当输入字段被改变时
- 当提交 HTML 表单时
- 当用户触发按键时
```javascript
<html>
<head>
<script>
function changetext(id)
{
id.innerHTML="谢谢!";
}
</script>
</head>
<body>
<h1 onclick="changetext(this)">请点击该文本</h1>
</body>
</html>
```

## DOM 元素操作
添加和删除节点（HTML 元素）。
### 创建新的 HTML 元素
document.createElement(): 增加元素
document.createTextNode: 增加元素的文本


```javascript
<div id="div1">
<p id="p1">这是一个段落</p>
<p id="p2">这是另一个段落</p>
</div>

<script>
var para=document.createElement("p");
var node=document.createTextNode("这是新段落。");
para.appendChild(node);

var element=document.getElementById("div1");
element.appendChild(para);
</script>
```

### 删除已有的 HTML 元素
```javascript
<div id="div1">
<p id="p1">这是一个段落。</p>
<p id="p2">这是另一个段落。</p>
</div>

<script>
var parent=document.getElementById("div1");
var child=document.getElementById("p1");
parent.removeChild(child);
</script>
```
## 对象
### 创建 JavaScript 对象
创建新对象有两种不同的方法：
1.定义并创建对象的实例
2.使用函数来定义对象，然后创建新的对象实例

#### 创建直接的实例
```javascript
person=new Object();
person.firstname="Bill";
person.lastname="Gates";
person.age=56;
person.eyecolor="blue";
```
也可以简化成：
```javascript
person={firstname:"John",lastname:"Doe",age:50,eyecolor:"blue"};
```

访问对象中的元素：
```javascript
var person={fname:"Bill",lname:"Gates",age:56};

for (x in person)
  {
  txt=txt + person[x];
  }
```

#### 使用对象构造器
```javascript
function person(firstname,lastname,age,eyecolor)
{
this.firstname=firstname;
this.lastname=lastname;
this.age=age;
this.eyecolor=eyecolor;
}
```

调用方式：
```javascript
var myFather=new person("Bill","Gates",56,"blue");
var myMother=new person("Steve","Jobs",48,"green");

```
##### 把方法添加到 JavaScript 对象
```javascript
function person(firstname,lastname,age,eyecolor)
{
this.firstname=firstname;
this.lastname=lastname;
this.age=age;
this.eyecolor=eyecolor;

this.changeName=changeName;
function changeName(name)
{
this.lastname=name;
}
}

```

