**修订记录**

| 时间 | 内容 |
|--------|--------|
| 2017.08.17 | 初稿 |

## 标题
```html
<h1>title1</h>
<h2>title2</h>
```
`<hr />` 标签在 HTML 页面中创建水平线。

## 注释
注释是这样写的：
`<!-- This is a comment -->`
注意：开始括号之后（左边的括号）需要紧跟一个叹号，结束括号之前（右边的括号）不需要。

## 段落
`<p>` 段落
`<br/>`换行

## 样式
style 属性用于改变 HTML 元素的样式。

在 HTML 4 中，有若干的标签和属性是被废弃的。被废弃（Deprecated）的意思是在未来版本的 HTML 和 XHTML 中将不支持这些标签和属性。
应该避免使用下面这些标签和属性：

标签
```html
<center>
<font> 和 <basefont>
<s> 和 <strike>
<u>
```

属性
```html
align
bgcolor
color
```
对于以上这些标签和属性：请使用样式代替！

例子
```html
<html>

<body style="background-color:yellow">
<h2 style="background-color:red">This is a heading</h2>
<p style="background-color:green">This is a paragraph.</p>
</body>

</html>
```

## 引用
### 短引用`<q>`
通常会为 `<q>` 元素包围引号。

```html
<p>WWF 的目标是：<q>构建人与自然和谐共存的世界。</q></p>
```
输出为：
WWF 的目标是 “构建人与自然和谐相处的世界。”

### 长引用`<blockquote>`
```html
HTML <blockquote> 元素定义被引用的节。
```

浏览器通常会对 `<blockquote>` 元素进行缩进处理。

```html
<p>以下内容引用自 WWF 的网站：</p>
<blockquote cite="http://www.worldwildlife.org/who/index.html">
五十年来，WWF 一直致力于保护自然界的未来。
世界领先的环保组织，WWF 工作于 100 个国家，
并得到美国一百二十万会员及全球近五百万会员的支持。
</blockquote>
```

### html css
通过使用 HTML4.0，所有的格式化代码均可移出 HTML 文档，然后移入一个独立的样式表。

链接外部样式表
```html
<html>

<head>
<link rel="stylesheet" type="text/css" href="../html/csstest1.css" >
</head>

<body>
<h1>我通过外部样式表进行格式化。</h1>
<p>我也一样！</p>
</body>

</html>
```

## 链接
```html
<a href="url">Link text</a>
```
href 属性规定链接的目标。

开始标签和结束标签之间的文字被作为超级链接来显示。

### target属性
使用 Target 属性，你可以定义被链接的文档在何处显示。

下面的这行会在新窗口打开文档：
```html
<a href="http://www.w3school.com.cn/" target="_blank">Visit W3School!</a>
```

### 锚
命名锚的语法：
```html
<a name="label">锚（显示在页面上的文本）</a>
```
当使用命名锚（named anchors）时，我们可以创建直接跳至该命名锚（比如页面中某个小节）的链接，这样使用者就无需不停地滚动页面来寻找他们需要的信息了。
首先，我们在 HTML 文档中对锚进行命名（创建一个书签）：
```html
<a name="tips">基本的注意事项 - 有用的提示</a>
```

然后，我们在同一个文档中创建指向该锚的链接：
```html
<a href="#tips">有用的提示</a>
```

## 图像
```html
<img src="boat.gif" alt="Big Boat">
```
在浏览器无法载入图像时，替换文本属性告诉读者她们失去的信息。

## 表格
`<th>` ：表头

```html
<table border="1">
    <tr>
        <th>Heading</th>
        <th>Another Heading</th>
    </tr>
    <tr>
        <td>row 1, cell 1</td>
        <td>row 1, cell 2</td>
    </tr>
    <tr>
        <td>row 2, cell 1</td>
        <td>row 2, cell 2</td>
    </tr>
</table>
```

如果表格中没有内容，不能为空，需要填写空占位符，即·&nbsp;·
```html
<tr>
	<td>&nbsp;</td>
	<td>row 2, cell 2</td>
</tr>
```

## 列表
### 无序列表
```html
<ul>
	<li>Coffee</li>
	<li>Milk</li>
</ul>
```
显示效果：
•Coffee
•Milk

### 有序列表
```html
<ol>
	<li>Coffee</li>
	<li>Milk</li>
</ol>
```
浏览器显示如下：
1.Coffee
2.Milk

## 块：`<div>`和`<span>`
### `<div>`
HTML`<div>` 元素是块级元素，它是可用于组合其他 HTML 元素的容器。
`<div>` 元素没有特定的含义。除此之外，由于它属于块级元素，浏览器会在其前后显示折行。

### `<span>`
HTML `<span>` 元素是内联元素，可用作文本的容器，它没有特定的含义。
当与 CSS 一同使用时，`<span>` 元素可用于为部分文本设置样式属性。

## 类(class)
对 HTML 进行分类（设置类），使我们能够为元素的类定义 CSS 样式。
为相同的类设置相同的样式，或者为不同的类设置不同的样式。

### 分类块级元素
HTML `<div>` 元素是块级元素。它能够用作其他 HTML 元素的容器。
设置 `<div>` 元素的类，使我们能够为相同的 `<div>` 元素设置相同的类：
```html
<html>
<head>
    <style>
        .cities {
            background-color:black;
            color:white;
            margin:20px;
            padding:20px;
        }
    </style>
</head>

<body>

<div class="cities">
    <h2>London</h2>
    <p>London is the capital city of England.
        It is the most populous city in the United Kingdom,
        with a metropolitan area of over 13 million inhabitants.</p>
</div>

<div class="cities">
    <h2>Paris</h2>
    <p>Paris is the capital and most populous city of France.</p>
</div>

<div class="cities">
    <h2>Tokyo</h2>
    <p>Tokyo is the capital of Japan, the center of the Greater Tokyo Area,
        and the most populous metropolitan area in the world.</p>
</div>

</body>
</html>
```

### 分类行内元素
HTML `<span>` 元素是行内元素，能够用作文本的容器。
设置 `<span>` 元素的类，能够为相同的 `<span>` 元素设置相同的样式。
```html
<html>
<head>
    <style>
        span.red {color:red;}
    </style>
</head>
<body>

<h1>My <span class="red">Important</span> Heading</h1>

</body>
</html>
```

## 布局
`<div>` 元素常用作布局工具，因为能够轻松地通过 CSS 对其进行定位。

这个例子使用了四个 `<div>` 元素来创建多列布局：

```html
<head>
    <style>
        #header {
            background-color:black;
            color:white;
            text-align:center;
            padding:5px;
        }
        #nav {
            line-height:30px;
            background-color:#eeeeee;
            height:300px;
            width:100px;
            float:left;
            padding:5px;
        }
        #section {
            width:350px;
            float:left;
            padding:10px;
        }
        #footer {
            background-color:black;
            color:white;
            clear:both;
            text-align:center;
            padding:5px;
        }
    </style>

</head>
<body>

<div id="header">
    <h1>City Gallery</h1>
</div>

<div id="nav">
    London<br>
    Paris<br>
    Tokyo<br>
</div>

<div id="section">
    <h1>London</h1>
    <p>
        London is the capital city of England. It is the most populous city in the United Kingdom,
        with a metropolitan area of over 13 million inhabitants.
    </p>
    <p>
        Standing on the River Thames, London has been a major settlement for two millennia,
        its history going back to its founding by the Romans, who named it Londinium.
    </p>
</div>

<div id="footer">
    Copyright W3School.com.cn
</div>

</body>
```

html5表示

```html
<head>
    <style>
        header {
            background-color:black;
            color:white;
            text-align:center;
            padding:5px;
        }
        nav {
            line-height:30px;
            background-color:#eeeeee;
            height:300px;
            width:100px;
            float:left;
            padding:5px;
        }
        section {
            width:350px;
            float:left;
            padding:10px;
        }
        footer {
            background-color:black;
            color:white;
            clear:both;
            text-align:center;
            padding:5px;
        }

    </style>

</head>
<body>
<header>
    <h1>City Gallery</h1>
</header>

<nav>
    London<br>
    Paris<br>
    Tokyo<br>
</nav>

<section>
    <h1>London</h1>
    <p>
        London is the capital city of England. It is the most populous city in the United Kingdom,
        with a metropolitan area of over 13 million inhabitants.
    </p>
    <p>
        Standing on the River Thames, London has been a major settlement for two millennia,
        its history going back to its founding by the Romans, who named it Londinium.
    </p>
</section>

<footer>
    Copyright W3School.com.cn
</footer>


</body>
```

## 框架
通过使用框架，你可以在同一个浏览器窗口中显示不止一个页面。每份HTML文档称为一个框架，并且每个框架都独立于其他的框架。

```html
<frameset cols="25%,75%">
   <frame src="frame_a.htm">
   <frame src="frame_b.htm">
</frameset>
```

## 内联框架(iframe)

```html
<html>
<body>

<iframe src="../example/html/demo_iframe.html" width="200" height="200" frameborder="0"></iframe>

<p>某些老式的浏览器不支持内联框架。</p>
<p>如果不支持，则 iframe 是不可见的。</p>

</body>
</html>
```

## 脚本

```html
<body>

<script type="text/javascript">
document.write("<h1>Hello World!</h1>")
</script> 

</body>
```
## 头部元素
`<head>` 元素是所有头部元素的容器。`<head>` 内的元素可包含脚本，指示浏览器在何处可以找到样式表，提供元信息，等等。

以下标签都可以添加到 head 部分：

| 头部元素 | 描述 |
|--------|--------|
|  title      |  定义文档的标题     |
|  base      |  为页面上的所有链接规定默认地址或默认目标     |
|  link      |  定义文档与外部资源之间的关系     |
|  meta      |  提供关于HTML文档的元数据     |
|  script      |  用于定义客户端脚本，比如 JavaScript     |
|  style      | 用于为 HTML 文档定义样式信息      |


### 1.`<title>` 元素
`<title> `标签定义文档的标题。
```html
<head>
    <title>Title of the document</title>
</head>
```

### 2.`<base>`元素
`<base>` 标签为页面上的所有链接规定默认地址或默认目标（target）：

```html
<head>
	<base href="http://www.w3school.com.cn/images/" />
	<base target="_blank" />
</head>
```

### 3.`<link>` 元素
`<link>` 标签定义文档与外部资源之间的关系。

<link> 标签最常用于连接样式表：

```html
<head>
	<link rel="stylesheet" type="text/css" href="mystyle.css" />
</head>
```

###  4.`<style>` 元素
`<style>` 标签用于为 HTML 文档定义样式信息。

您可以在 style 元素内规定 HTML 元素在浏览器中呈现的样式：

```html
<head>
	<style type="text/css">
		body {background-color:yellow}
		p {color:blue}
	</style>
</head>
```

### 5.`<meta>` 元素
`<meta>` 标签提供关于HTML文档的元数据。元数据不会显示在页面上，但是对于机器是可读的。

典型的情况是，meta 元素被用于规定页面的描述、关键词、文档的作者、最后修改时间以及其他元数据。


### 6.`<script>` 元素
`<script>` 标签用于定义客户端脚本，比如 JavaScript。

## HTML 表单(form)
HTML 表单用于搜集不同类型的用户输入。

### `<form>` 元素
HTML 表单用于收集用户输入。

### `<form>` 元素的属性

#### 1.Action 属性
action 属性定义在提交表单时执行的动作。向服务器提交表单的通常做法是使用提交按钮。

在下面的例子中，指定了某个服务器脚本来处理被提交表单：

```html
<form action="action_page.php">
```
如果省略 action 属性，则 action 会被设置为当前页面。

#### 2.Method 属性
method 属性规定在提交表单时所用的 HTTP 方法（GET 或 POST）：
```html
<form action="action_page.php" method="GET">
```

##### 何时使用 GET？
使用 GET 时，表单数据在页面地址栏中是可见的：
> action_page.php?firstname=Mickey&lastname=Mouse

注释：GET 适合少量数据的提交。浏览器会设定容量限制。

##### 何时使用 POST？
如果表单正在更新数据，或者包含敏感信息（例如密码）。
POST 的安全性更加，因为在页面地址栏中被提交的数据是不可见的。

### `<input>` 元素
`<input>` 元素是最重要的表单元素。
`<input>` 元素有很多形态，根据不同的 type 属性。常见的类型如下：

| 类型 | 描述 |
|--------|--------|
|  text      |   定义常规文本输入     |
|  radio      |  定义单选按钮输入（选择多个选择之一）      |
|  submit      |  定义提交按钮（提交表单）      |
|  password      |  输入密码，显示为*      |
|  checkbox      |  复选框     |
|  button      |  按钮     |
|  html5 number      |   用于应该包含数字值的输入字段。     |
|  html5 date      |  用于应该包含日期的输入字段     |
|  html5 datetime      |  允许用户选择日期和时间（有时区）。     |
|  html5 color     |  用于应该包含颜色的输入字段。     |
|  html5 range     |  用于应该包含一定范围内的值的输入字段。     |
|  html5 month     |  允许用户选择月份和年份。     |
|  html5 week     | 允许用户选择周和年。      |

#### 1.文本输入
`<input type="text">` 定义用于文本输入的单行输入字段：

```html
<form>
    First name:<br>
    <input type="text" name="firstname">
    <br>
    Last name:<br>
    <input type="text" name="lastname">
</form>
```

#### 2.单选按钮输入
`<input type="radio">` 定义单选按钮。

单选按钮允许用户在有限数量的选项中选择其中之一：

```html
<form>
    <input type="radio" name="sex" value="male" checked>Male
    <br>
    <input type="radio" name="sex" value="female">Female
</form>
```

#### 3.提交按钮
`<input type="submit">` 定义用于向表单处理程序（form-handler）提交表单的按钮。

表单处理程序通常是包含用来处理输入数据的脚本的服务器页面。

表单处理程序在表单的 action 属性中指定：

```html
<form action="action_page.php">
    First name:<br>
    <input type="text" name="firstname" value="Mickey">
    <br>
    Last name:<br>
    <input type="text" name="lastname" value="Mouse">
    <br><br>
    <input type="submit" value="Submit">
</form>
```

#### 4.password输入类型
定义密码字段。

```html
<form>
    User name:<br>
    <input type="text" name="username">
    <br>
    User password:<br>
    <input type="password" name="psw">
</form>
```

#### 5.checkbox输入类型
定义复选框。
```html
<form>
    <input type="checkbox" name="vehicle" value="Bike">I have a bike
    <br>
    <input type="checkbox" name="vehicle" value="Car">I have a car
</form>
```

#### 6.button输入类型
定义按钮。

```html
<input type="button" onclick="alert('Hello World!')" value="Click Me!">
```

#### 7.html5 number输入类型
用于应该包含数字值的输入字段，并能对数字做出限制。
```html
<form>
  Quantity (between 1 and 5):
  <input type="number" name="quantity" min="1" max="5">
</form>
```

限制如下：

| 属性 | 描述 |
|--------|--------|
|  disabled      |  规定输入字段应该被禁用。     |
|  max      |  规定输入字段的最大值。     |
|  maxlength      |  规定输入字段的最大字符数。     |
|  min       |   规定输入字段的最小值。    |
|  pattern      |  规定通过其检查输入值的正则表达式。     |
|  readonly      |  规定输入字段为只读（无法修改）。     |
|  required      |  规定输入字段是必需的（必需填写）。     |
|  size      |   规定输入字段的宽度（以字符计）。    |
|  step      |  规定输入字段的合法数字间隔。     |
|  value      |   规定输入字段的默认值。    |

#### 8.html5 date输入类型
用于应该包含日期的输入字段。
```html
<form>
  Enter a date before 1980-01-01:
  <input type="date" name="bday" max="1979-12-31"><br>
  Enter a date after 2000-01-01:
  <input type="date" name="bday" min="2000-01-02"><br>
</form>
```

#### 9.html5 datetime输入类型
允许用户选择日期和时间（有时区）。

```html
<form>
  Birthday (date and time):
  <input type="datetime" name="bdaytime">
</form>
```

#### 10.html5 color输入类型
用于应该包含颜色的输入字段。

```html
<form>
  Select your favorite color:
  <input type="color" name="favcolor">
</form>
```

#### 11.html5 range输入类型
用于应该包含一定范围内的值的输入字段。

```html
<form>
  <input type="range" name="points" min="0" max="10">
</form>
```

#### 12.html5 month输入类型
允许用户选择月份和年份。

```html
<form>
  Birthday (month and year):
  <input type="month" name="bdaymonth">
</form>
```

#### 13.html5 week输入类型
允许用户选择周和年。

```html
<form>
  Select a week:
  <input type="week" name="week_year">
</form>
```

### `<input>` 元素的属性
支持的属性如下：

| 属性 | 描述 |
|--------|--------|
|  value      |  规定输入字段的初始值     |
|  readonly      |  规定输入字段为只读（不能修改）     |
|  disabled      |  规定输入字段是禁用的     |
|  size      |  规定输入字段的尺寸（以字符计）     |
|  maxlength      |  规定输入字段允许的最大长度     |
|  html5 autocomplete      |  规定表单或输入字段是否应该自动完成。     |
|  html5 autofocus      |  规定当页面加载时 `<input>` 元素应该自动获得焦点。     |
|  html5 form      |  规定 `<input>` 元素所属的一个或多个表单。     |
|  html5 formaction      |  规定当提交表单时处理该输入控件的文件的 URL。     |
|  html5 formenctype      |  规定当把表单数据（form-data）提交至服务器时如何对其进行编码     |
|  html5 formmethod      |  定义用以向 action URL 发送表单数据（form-data）的 HTTP 方法     |
|  html5 formtarget      |   规定的名称或关键词指示提交表单后在何处显示接收到的响应    |
|  html5 height 和 width 属性      | 规定 `<input>` 元素的高度和宽度      |
|  html5 list      |  引用的 `<datalist>` 元素中包含了 `<input>` 元素的预定义选项     |
|  html5 min 和 max      |  规定 `<input>` 元素的最小值和最大值。     |
|  html5 multiple      |  规定允许用户在 `<input>` 元素中输入一个以上的值     |

#### 1.value 属性
规定输入字段的初始值：

```html
<form action="">
    First name:<br>
    <input type="text" name="firstname" value="John">
    <br>
    Last name:<br>
    <input type="text" name="lastname">
</form>
```

#### 2.readonly 属性
规定输入字段为只读（不能修改）：

```html
<form action="">
    First name:<br>
    <input type="text" name="firstname" value="John" readonly>
    <br>
    Last name:<br>
    <input type="text" name="lastname">
</form>

```

#### 3.disabled 属性
规定输入字段是禁用的。不可用和不可点击的。

```html
<form action="">
    First name:<br>
    <input type="text" name="firstname" value="John" disabled>
    <br>
    Last name:<br>
    <input type="text" name="lastname">
</form>
```

#### 4.size 属性
规定输入字段的尺寸（以字符计），是显示长度，不限制输入字符数量。

```html
<form action="">
    First name:<br>
    <input type="text" name="firstname" value="John" size="40">
    <br>
    Last name:<br>
    <input type="text" name="lastname">
</form>
```

#### 5.maxlength 属性
规定输入字段允许的最大长度：

```html
<form action="">
    First name:<br>
    <input type="text" name="firstname" maxlength="10">
    <br>
    Last name:<br>
    <input type="text" name="lastname">
</form>
```

#### 6.html5 autocomplete 属性
规定表单或输入字段是否应该自动完成。

当自动完成开启，浏览器会基于用户之前的输入值自动填写值。

```html
<form action="action_page.php" autocomplete="on">
    First name:<input type="text" name="fname"><br>
    Last name: <input type="text" name="lname"><br>
    E-mail: <input type="email" name="email" autocomplete="off"><br>
    <input type="submit">
</form>
```

#### 7.html5 autofocus 属性
autofocus 属性是布尔属性。

如果设置，则规定当页面加载时 `<input>` 元素应该自动获得焦点。

```html
First name:<input type="text" name="fname" autofocus="on">
```

#### 8.html5 form 属性
form 属性规定 `<input>` 元素所属的一个或多个表单。

提示：如需引用一个以上的表单，请使用空格分隔的表单 id 列表。

```html
<form action="action_page.php" id="form1">
    First name: <input type="text" name="fname"><br>
    <input type="submit" value="Submit">
</form>

Last name: <input type="text" name="lname" form="form1">
```

#### 9.html5 formaction 属性
规定当提交表单时处理该输入控件的文件的 URL。
formaction 属性覆盖 `<form>` 元素的 action 属性。
formaction 属性适用于 type="submit" 以及 type="image"。

```html
<form action="action_page.php">
    First name: <input type="text" name="fname"><br>
    Last name: <input type="text" name="lname"><br>
    <input type="submit" value="Submit"><br>
    <input type="submit" formaction="demo_admin.asp"
           value="Submit as admin">
</form>
```

#### 10.html5 formenctype 属性
formenctype 属性规定当把表单数据（form-data）提交至服务器时如何对其进行编码（仅针对 method="post" 的表单）。
formenctype 属性覆盖 `<form>` 元素的 enctype 属性。
formenctype 属性适用于 type="submit" 以及 type="image"。

```html
<form action="demo_post_enctype.asp" method="post">
    First name: <input type="text" name="fname"><br>
    <input type="submit" value="Submit">
    <input type="submit" formenctype="multipart/form-data"
           value="Submit as Multipart/form-data">
</form>
```

#### 11.html5 formmethod 属性
formmethod 属性定义用以向 action URL 发送表单数据（form-data）的 HTTP 方法。
formmethod 属性覆盖 `<form>` 元素的 method 属性。
formmethod 属性适用于 type="submit" 以及 type="image"。

```html
<form action="action_page.php" method="get">
    First name: <input type="text" name="fname"><br>
    Last name: <input type="text" name="lname"><br>
    <input type="submit" value="Submit">
    <input type="submit" formmethod="post" formaction="demo_post.asp"
           value="Submit using POST">
</form>
```

#### 12.html5 formtarget 属性
formtarget 属性规定的名称或关键词指示提交表单后在何处显示接收到的响应。
formtarget 属性会覆盖 `<form>` 元素的 target 属性。
formtarget 属性可与 type="submit" 和 type="image" 使用。

```html
<form action="action_page.php">
   First name: <input type="text" name="fname"><br>
   Last name: <input type="text" name="lname"><br>
   <input type="submit" value="Submit as normal">
   <input type="submit" formtarget="_blank"
   value="Submit to a new window">
</form> 
```

#### 13.html5 height 和 width 属性
height 和 width 属性规定 `<input>` 元素的高度和宽度。
height 和 width 属性仅用于 `<input type="image">`。

```html
<input type="image" src="img_submit.gif" alt="Submit" width="48" height="48">
```

#### 14.html5 list 属性
list 属性引用的 `<datalist>` 元素中包含了 `<input>` 元素的预定义选项。

```html
<input list="browsers">

<datalist id="browsers">
   <option value="Internet Explorer">
   <option value="Firefox">
   <option value="Chrome">
   <option value="Opera">
   <option value="Safari">
</datalist> 

```

#### 15.html5 min 和 max 属性
min 和 max 属性规定 `<input>` 元素的最小值和最大值。
min 和 max 属性适用于如需输入类型：number、range、date、datetime、datetime-local、month、time 以及 week。

```html
Enter a date before 1980-01-01:
<input type="date" name="bday" max="1979-12-31">

 Enter a date after 2000-01-01:
<input type="date" name="bday" min="2000-01-02">

 Quantity (between 1 and 5):
<input type="number" name="quantity" min="1" max="5">
```

#### 16.html5 multiple 属性
multiple 属性是布尔属性。
如果设置，则规定允许用户在 `<input>` 元素中输入一个以上的值。
multiple 属性适用于以下输入类型：email 和 file。

```html
Select images: <input type="file" name="img" multiple>
```

### form的其他元素
#### 1.`<fieldset>` 元素
`<fieldset>` 元素组合表单中的相关数据
`<legend>` 元素为 `<fieldset>` 元素定义标题。

例子：
```html
<form action="action_page.php">
    <fieldset>
        <legend>Personal information:</legend>
        First name:<br>
        <input type="text" name="firstname" value="Mickey">
        <br>
        Last name:<br>
        <input type="text" name="lastname" value="Mouse">
        <br><br>
        <input type="submit" value="Submit"></fieldset>
</form>
```

#### 2.`<select>` 元素
实现下拉列表。

实例:
```html
<form>
    <select name="cars">
        <option value="volvo">Volvo</option>
        <option value="saab">Saab</option>
        <option value="fiat">Fiat</option>
        <option value="audi">Audi</option>
    </select>
</form>
```

#### 3.`<textarea>` 元素
定义多行输入字段（文本域）：

```html
<textarea name="message" rows="10" cols="30">
    The cat was playing in the garden.
</textarea>
```

#### 4.`<button>` 元素
定义可点击的按钮。

```html
<button type="button" onclick="alert('Hello World!')">Click Me!</button>
```

#### 5.HTML5 `<datalist>` 元素
`<datalist>` 元素为 `<input>` 元素规定预定义选项列表。类似下拉列表，但支持输入。

用户会在他们输入数据时看到预定义选项的下拉列表。

`<input>` 元素的 list 属性必须引用 `<datalist>` 元素的 id 属性。

## html5语义元素
语义元素清楚地向浏览器和开发者描述其意义。
非语义元素的例子：`<div>` 和 `<span>` - 无法提供关于其内容的信息。
语义元素的例子：`<form>`、`<table>` 以及 `<img>` - 清晰地定义其内容。

### 1.HTML5 <section> 元素
`<section>` 元素定义文档中的节。节（section）是有主题的内容组，通常具有标题。

可以将网站首页划分为简介、内容、联系信息等节。

```html
<section>
   <h1>WWF</h1>
   <p>The World Wide Fund for Nature (WWF) is....</p>
</section>
```

### 2.HTML5 `<header>` 元素
`<header>` 元素为文档或节规定页眉。它被用作介绍性内容的容器。

一个文档中可以有多个 `header>`元素。

```html
<article>
   <header>
     <h1>What Does WWF Do?</h1>
     <p>WWF's mission:</p>
   </header>
   <p>WWF's mission is to stop the degradation of our planet's natural environment,
  and build a future in which humans live in harmony with nature.</p>
</article> 
```

### 3.HTML5 `footer>`元素
`footer>`元素为文档或节规定页脚。页脚通常包含文档的作者、版权信息、使用条款链接、联系信息等等。

可以在一个文档中使用多个 `<footer>` 元素。

```html
<footer>
   <p>Posted by: Hege Refsnes</p>
   <p>Contact information: <a href="mailto:someone@example.com">
  someone@example.com</a>.</p>
</footer>
```

### 4.HTML5 `<nav>` 元素
定义导航链接集合。`<nav>` 元素旨在定义大型的导航链接块。

```html
<nav>
<a href="/html/">HTML</a> |
<a href="/css/">CSS</a> |
<a href="/js/">JavaScript</a> |
<a href="/jquery/">jQuery</a>
</nav> 
```

### 5.HTML5 `<aside>` 元素
`<aside>` 元素页面主内容之外的某些内容（比如侧栏）。

```html
<aside>
   <h4>Epcot Center</h4>
   <p>The Epcot Center is a theme park in Disney World, Florida.</p>
</aside> 
```

### 6.HTML5 `<figure>` 和 `<figcaption>` 元素
标题（caption）的作用是为图片添加说明。

通过 HTML5，图片和标题能够被组合在 `<figure>` 元素中：

```html
<figure>
   <img src="pic_mountain.jpg" alt="The Pulpit Rock" width="304" height="228">
   <figcaption>Fig1. - The Pulpit Pock, Norway.</figcaption>
</figure> 
```

























