
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

## 运算符








