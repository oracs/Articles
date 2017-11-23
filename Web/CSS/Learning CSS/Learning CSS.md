## CSS概述
CSS 指层叠样式表 (Cascading Style Sheets)，它定义如何显示 HTML 元素，是为了解决内容与表现分离的问题。
它通常存储在样式表中，外部样式表通常存储在 CSS 文件中，多个样式定义可层叠。

### 层叠次序
当同一个 HTML 元素被不止一个样式定义时，它的加载顺序如下：

- 内联样式（在 HTML 元素内部）
- 内部样式表（位于 `<head>` 标签内部）
- 外部样式表
- 浏览器缺省设置

## CSS基础知识
### 语法
CSS由两部分组成：选择器，以及一条或多条声明。比如：
```css
selector {declaration1; declaration2; ... declarationN }
```

选择器通常需要改变样式的 HTML 元素。
每条声明由一个属性和一个值组成。属性和值被冒号分开。比如：
```css
selector {property: value}
```

下面这行代码的作用是将 h1 元素内的文字颜色定义为红色，同时将字体大小设置为 14 像素。
![ ](http://wiki.zte.com.cn/download/attachments/123674749/css%E8%AF%AD%E6%B3%95%E6%A0%B7%E4%BE%8B.jpg)

#### 注意
1.如果值为若干单词，则要给值加引号，比如
```css
p {font-family: "sans serif";}
```
2.如果要定义不止一个声明，则需要用分号将每个声明分开
```css
p {text-align:center; color:red;}	
```
或者每行只描述一个属性，分多行完成。
```css
p {
  text-align: center;
  color: black;
  font-family: arial;
}
```

### 选择器的分组
被分组的选择器可以共享样式，用逗号将需要分组的选择器分开。
在下面的例子中，所有的标题元素都是绿色的。

```css
h1,h2,h3,h4,h5,h6 {
  color: green;
}
```

### 继承
如果对某个元素设置样式，那么它的子类元素也将继承这些样式。
下面的例子：


```css
body {
     font-family: Verdana, sans-serif;
     }
```
站点的 body 元素将使用 Verdana 字体。

通过 CSS 继承，子元素将继承最高级元素（在本例中是 body）所拥有的属性（这些子元素诸如 p, td, ul, ol, ul, li, dl, dt,和 dd）。不需要另外的规则，所有 body 的子元素都应该显示 Verdana 字体，子元素的子元素也一样。并且在大部分的现代浏览器都支持（Netscape 4浏览器除外）

### 如何插入样式表
有三种方式：

1. 外部样式表
1. 内部样式表
1. 内联样式

#### 外部样式表
当样式需要应用于很多页面时，外部样式表将是理想的选择。可以通过改变一个文件来改变整个站点的外观。
每个页面使用 `<link>` 标签链接到样式表。`<link>` 标签在（文档的）头部：

```css
<head>
	<link rel="stylesheet" type="text/css" href="mystyle.css" />
</head>
```

下面是一个样式表文件的例子：

```css
hr {color: sienna;}
p {margin-left: 20px;}
body {background-image: url("images/back40.gif");}
```

不要在属性值与单位之间留有空格。假如你使用 “margin-left: 20 px” 而不是 “margin-left: 20px” ，它仅在 IE 6 中有效，但是其他浏览器中却无法正常工作。

#### 内部样式表
当单个文档需要特殊的样式时，就应该使用内部样式表。你可以使用 `<style>` 标签在文档头部定义内部样式表，就像这样:

```css
<head>
	<style type="text/css">
  		hr {color: sienna;}
  		p {margin-left: 20px;}
  		body {background-image: url("images/back40.gif");}
	</style>
</head>
```
#### 内联样式
由于要将表现和内容混杂在一起，内联样式会损失掉样式表的许多优势。 **请慎用这种方法!**
例如当样式仅需要在一个元素上应用一次时。

要使用内联样式，你需要在相关的标签内使用样式（style）属性。Style 属性可以包含任何 CSS 属性。本例展示如何改变段落的颜色和左外边距：

```css
<p style="color: sienna; margin-left: 20px">
This is a paragraph
</p>
```

## CSS样式
### CSS背景
#### 背景色(background-color)
可以使用 background-color 属性为元素设置背景色。
```css
p {background-color: gray;}
```

#### 背景图像(background-image)
可以使用 background-image 属性设置背景图像。
```css
body {background-image: url(/i/eg_bg_04.gif);}
```

#### 背景重复(background-repeat)
如果需要对背景图像进行平铺，可以使用 background-repeat 属性。
默认地，背景图像将从一个元素的左上角开始。请看下面的例子：

```css
body
  { 
  background-image: url(/i/eg_bg_03.gif);
  background-repeat: repeat-y;
  }
```

属性值 repeat 导致图像在水平垂直方向上都平铺，就像以往背景图像的通常做法一样。
属性值 repeat-x 和 repeat-y 分别导致图像只在水平或垂直方向上重复，
属性值 no-repeat 则不允许图像在任何方向上平铺。

#### 背景定位(background-position)
可以利用 background-position 属性改变图像在背景中的位置。

下面的例子在 body 元素中将一个背景图像居中放置：
```css
body
  { 
    background-image:url('/i/eg_bg_03.gif');
    background-repeat:no-repeat;
    background-position:center;
  }
```
为 background-position 属性提供值有很多方法。可以使用 关键字、百分数值、长度值 进行设置。

##### 关键字
关键字包括：center、top、bottom、right、left。

```css
p
  { 
    background-image:url('bgimg.gif');
    background-repeat:no-repeat;
    background-position:top;
  }

```
##### 百分数值
百分数值应用于元素和图像。也就是说，图像中描述为 50% 50% 的点（中心点）与元素中描述为 50% 50% 的点（中心点）对齐。

```css
body
  { 
    background-image:url('/i/eg_bg_03.gif');
    background-repeat:no-repeat;
    background-position:50% 50%;
  }
```
如果你想把一个图像放在水平方向 2/3、垂直方向 1/3 处，可以这样声明：
```css
body
  { 
    background-image:url('/i/eg_bg_03.gif');
    background-repeat:no-repeat;
    background-position:66% 33%;
  }

```
##### 长度值
长度值解释的是元素内边距区左上角的偏移。偏移点是图像的左上角。
```css
body
  { 
    background-image:url('/i/eg_bg_03.gif');
    background-repeat:no-repeat;
    background-position:50px 100px;
  }

```

#### 背景关联(background-attachment)
如果文档比较长，那么当文档向下滚动时，背景图像也会随之滚动。当文档滚动到超过图像的位置时，图像就会消失。

可以通过 background-attachment 属性防止这种滚动。通过这个属性，可以声明图像相对于可视区是固定的（fixed），因此不会受到滚动的影响：

```css
body 
  {
  background-image:url(/i/eg_bg_02.gif);
  background-repeat:no-repeat;
  background-attachment:fixed
  }

```

### CSS文本
#### 文本缩进(text-indent)
可以使用 text-indent 属性实现文本缩进。

这个属性最常见的用途是将段落的首行缩进，下面的规则会使所有段落的首行缩进 5 em：

```css
p {text-indent: 5em;}
```

#### 使用负值
text-indent 还可以设置为负值。比如实现“悬挂缩进”，即第一行悬挂在元素中余下部分的左边：
```css
p {text-indent: -5em;}
```

#### 使用百分比值
百分数要相对于缩进元素父元素的宽度。换句话说，如果将缩进值设置为 20%，所影响元素的第一行会缩进其父元素宽度的 20%。
在下例中，缩进值是父元素的 20%，即 100 个像素：
```css
div {width: 500px;}
p {text-indent: 20%;}

<div>
	<p>this is a paragragh</p>
</div>
```
#### 继承
text-indent 属性可以继承，
如下示例中，标记中的段落`<p>`也会缩进 50 像素，这是因为这个段落继承了 id 为 inner 的 div 元素的缩进值
```css
div#outer {width: 500px;}
div#inner {text-indent: 10%;}
p {width: 200px;}

<div id="outer">
	<div id="inner">some text. some text. some text.
		<p>this is a paragragh.</p>
	</div>
</div>
```

#### 水平对齐(text-indent)
text-align属性，属性值：

- left：左对齐
- right：右对齐
- center: 居中对齐
- justify: 两端对齐

#### 字间隔(word-spacing)
word-spacing 属性可以改变单词之间的标准间隔。
如果设置正值，那么单词之间的间隔就会增加；如果设置一个负值，会把它拉近。

其默认值 normal 与设置值为 0 是一样的。

```css
p.spread {word-spacing: 30px;}
p.tight {word-spacing: -0.5em;}

<p class="spread">
This is a paragraph. The spaces between words will be increased.
</p>

<p class="tight">
This is a paragraph. The spaces between words will be decreased.
</p>
```

#### 字母间隔(letter-spacing)
和word-spacing类似，只不过是字母的间隔。

#### 文本装饰（text-decoration）
text-decoration 有 5 个值：

- none
- underline： 加下划线
- overline：在文本的顶端画一个上划线
- blink：让文本闪烁

#### 空白符(font-family)
white-space 属性可以指定对文档中的空格、换行符和 tab 字符的处理方式。

| 属性值 | 空格 | 换行符 | 自动换行 |
|--------|--------|--------|--------|
|   pre-line     |  合并      |   保留     |  允许      |
|   normal     |    合并    |   忽略     |    允许    |
|  nowrap      |    合并    |   忽略     |   不允许     |
|  pre      |    保留    |   保留     |  不允许      |
|  pre-wrap      |    保留    |    保留    |   允许     |


### CSS字体
#### 字体系列(font-family)
在 CSS 中，有两种不同类型的字体系列名称：

- 通用字体系列 - 拥有相似外观的字体系统组合（比如 "Serif" 或 "Monospace"）
- 特定字体系列 - 具体的字体系列（比如 "Times" 或 "Courier"）

除了各种特定的字体系列外，CSS 定义了 5 种通用字体系列：

- Serif 字体
- Sans-serif 字体
- Monospace 字体
- Cursive 字体
- Fantasy 字体

##### 使用通用字体系列
如果你希望文档使用一种 sans-serif 字体，但是你并不关心是哪一种字体，
可以设置以下风格：
```css
body {font-family: sans-serif;}
```
从 sans-serif 字体系列中选择一个字体（如 Helvetica），并将其应用到 body 元素。

##### 使用特定字体系列
下面的例子为所有 h1 元素设置了 Georgia 字体：

```css
h1 {font-family: Georgia;}
```

这样会产生一个问题，如果用户没有安装 Georgia 字体，就只能使用用户机器上的默认字体来显示 h1 元素。

可以通过结合特定字体名和通用字体系列来解决这个问题：

```css
h1 {font-family: Georgia, serif;}
```

##### 使用引号
只有当字体名中有一个或多个空格（比如 New York），或者如果字体名包括 # 或 $ 之类的符号，才需要在 font-family 中加引号。
比如:

```css
<p style="font-family: Times, TimesNR, 'New Century Schoolbook', Georgia,
 'New York', serif;">...</p>
```

#### 字体风格(font-style)
该属性有三个值：

- normal - 文本正常显示
- italic - 文本斜体显示
- oblique - 文本倾斜显示

实例

```css
p.normal {font-style:normal;}
p.italic {font-style:italic;}
p.oblique {font-style:oblique;}
```
斜体（italic）是一种简单的字体风格，对每个字母的结构有一些小改动，来反映变化的外观。与此不同，倾斜（oblique）文本则是正常竖直文本的一个倾斜版本。

通常情况下，italic 和 oblique 文本在 web 浏览器中看上去完全一样。

#### 字体加粗（font-weight）
font-weight 属性设置文本的粗细。

属性值：

- bold：粗体。
- normal: 正常
- bolder: 设置比所继承值更粗的一个字体
- lighter:设置比所继承值更细的一个字体

值 100 ~ 900 为字体指定了 9 级加粗度。
100 对应最细的字体变形，900 对应最粗的字体变形。数字 400 等价于 normal，而 700 等价于 bold。

例子：

```css
p.normal {font-weight:normal;}
p.thick {font-weight:bold;}
p.thicker {font-weight:900;}
```

### 字体大小(font-size)
font-size 属性设置文本的大小。

font-size 值可以是绝对或相对值。

绝对值：

- 将文本设置为指定的大小
- 不允许用户在所有浏览器中改变文本大小（不利于可用性）
- 绝对大小在确定了输出的物理尺寸时很有用

相对大小：

- 相对于周围的元素来设置大小
- 允许用户在浏览器改变文本大小

**注意**：如果您没有规定字体大小，普通文本（比如段落）的默认大小是 16 像素 (16px=1em)。

例子：
```css
h1 {font-size:60px;}
h2 {font-size:40px;}
p {font-size:14px;}
```
### CSS 链接
链接的四种状态：

- a:link - 普通的、未被访问的链接
- a:visited - 用户已访问的链接
- a:hover - 鼠标指针位于链接的上方
- a:active - 链接被点击的时刻

当为链接的不同状态设置样式时，请按照以下次序规则：

- a:hover 必须位于 a:link 和 a:visited 之后
- a:active 必须位于 a:hover 之后

其他相关属性：
text-decoration 属性大多用于去掉链接中的下划线：
background-color 属性规定链接的背景色：

综合例子：

```css
<head>
    <style>
        a.one:link {color:#ff0000;}
        a.one:visited {color:#0000ff;}
        a.one:hover {color:#ffcc00;}

        a.two:link {color:#ff0000;}
        a.two:visited {color:#0000ff;}
        a.two:hover {font-size:150%;}

        a.three:link {color:#ff0000;}
        a.three:visited {color:#0000ff;}
        a.three:hover {background:#66ff66;}

        a.four:link {color:#ff0000;}
        a.four:visited {color:#0000ff;}
        a.four:hover {font-family:'微软雅黑';}

        a.five:link {color:#ff0000;text-decoration:none;}
        a.five:visited {color:#0000ff;text-decoration:none;}
        a.five:hover {text-decoration:underline;}
    </style>
</head>

<body>
<p>请把鼠标指针移动到下面的链接上，看看它们的样式变化。</p>

<p><b><a class="one" href="../index.html" darget="_blank">这个链接改变颜色</a></b></p>
<p><b><a class="two" href="../index.html" darget="_blank">这个链接改变字体尺寸</a></b></p>
<p><b><a class="three" href="../index.html" darget="_blank">这个链接改变背景色</a></b></p>
<p><b><a class="four" href="../index.html" darget="_blank">这个链接改变字体</a></b></p>
<p><b><a class="five" href="../index.html" darget="_blank">这个链接改变文本的装饰</a></b></p>

</body>
```

### CSS 列表
对有序列表(ul, li)和无序列表(ol,li)设置样式。

列表类型（列表前的符号）：
list-style-type: disc  // 实心圆
list-style-type: circle  // 空心圆
list-style-type: square  // 实心矩形
list-style-type: none  // 无编号
list-style-type: decimal  // 数字
list-style-type: lower-roman  // 小写罗马数字
list-style-type: upper-roman  // 大写罗马数字
list-style-type: lower-alpha  // 小写字母
list-style-type: upper-alpha  // 大写字母




列表项图像
list-style-image : url(xxx.gif)


为简单起见，可以将以上 3 个列表样式属性合并为一个方便的属性：list-style，就像这样：
```css
    <style type="text/css">
        ul { list-style: square inside url('../i/eg_arrow.gif') }
    </style>
```

### CSS 表格
#### 表格边框
使用 border 属性设置边框。

```css
table, th, td
  {
  border: 1px solid blue;
  }
```
请注意，上例中的表格具有双线条边框。这是由于 table、th 以及 td 元素都有独立的边框。
如果需要把表格显示为单线条边框，请使用 border-collapse 属性。

#### 折叠边框
border-collapse 属性设置是否将表格边框折叠为单一边框：

```css
table
  {
  border-collapse:collapse;
  }

table,th, td
  {
  border: 1px solid black;
  }
```

#### 表格宽度和高度
通过 width 和 height 属性定义表格的宽度和高度。

下面的例子将表格宽度设置为 100%，同时将 th 元素的高度设置为 50px：

```css
table
  {
  width:100%;
  }

th
  {
  height:50px;
  }
```

#### 表格文本对齐
text-align 和 vertical-align 属性设置表格中文本的对齐方式。
text-align 属性设置水平对齐方式，比如左对齐、右对齐或者居中：
```css
td
  {
  text-align:right;
  }
```

#### 表格内边距
padding 属性设置 td 和 th 元素内边距。
```css
td
  {
  padding:15px;
  }
```

#### 表格颜色
background ：设置背景色
```css
table, td, th
  {
  border:1px solid green;
  }

th
  {
  background:green;
  color:white;
  }
```

### CSS 框模型
#### 概述
元素框的最内部分是实际的内容，直接包围内容的是内边距。内边距呈现了元素的背景。内边距的边缘是边框。边框以外是外边距，外边距默认是透明的，因此不会遮挡其后的任何元素。

内边距、边框和外边距都是可选的，默认值是零。

width 和 height 指的是内容区域的宽度和高度。

![ ](http://wiki.zte.com.cn/download/attachments/123674749/css桩模型概述.jpg)

增加内边距、边框和外边距不会影响内容区域的尺寸，但是会增加元素框的总尺寸。

![ ](http://wiki.zte.com.cn/download/attachments/123674749/桩模式例子.jpg)

#### 内边距(padding)
padding 属性定义元素的内边距。padding 属性接受长度值或百分比值，但不允许使用负值。

例如，所有 h1 元素的各边都有 10 像素的内边距，只需要这样：
```css
h1 {padding: 10px;}
```

还可以按照上、右、下、左的顺序分别设置各边的内边距，各边均可以使用不同的单位或百分比值：
```css
h1 {padding: 10px 0.25em 2ex 20%;}
```

也通过使用下面四个单独的属性，分别设置上、右、下、左内边距：

- padding-top
- padding-right
- padding-bottom
- padding-left

比如下面的写法和上例完全相同。
```css
h1 {
  padding-top: 10px;
  padding-right: 0.25em;
  padding-bottom: 2ex;
  padding-left: 20%;
  }
```
还可以为元素的内边距设置百分数值。百分数值是相对于其父元素的 width 计算的，这一点与外边距一样。所以，如果父元素的 width 改变，它们也会改变。

下面这条规则把段落的内边距设置为父元素 width 的 10%：
```css
p {padding: 10%;}
```

例如：如果一个段落的父元素是 div 元素，那么它的内边距要根据 div 的 width 计算。
```css
<div style="width: 200px;">
<p>This paragragh is contained within a DIV that has a width of 200 pixels.</p>
</div> 
```
### 边框
每个边框(border)有 3 个方面：宽度、样式，以及颜色。
CSS 规范指出，边框绘制在“元素的背景之上”。

#### 边框样式(border-style)
border-style 属性定义了 10 个不同样式，包括 none。

例如，可以为把一幅图片的边框定义为 outset，使之看上去像是“凸起按钮”：

```ss
a:link img {border-style: outset;}
```

也可以为一个边框定义多个样式，例如，下面的例子定义了四种边框样式：实线上边框、点线右边框、虚线下边框和一个双线左边框。
```css
p.aside {border-style: solid dotted dashed double;}
```
这里的值采用了 top-right-bottom-left 的顺序。

也可以单独设置某一边的样式：

- border-top-style
- border-right-style
- border-bottom-style
- border-left-style

下面两种写法是等价的：

```css
p {border-style: solid solid solid none;}
p {border-style: solid; border-left-style
```

#### 边框的宽度（border-width）
可以通过 border-width 属性为边框指定宽度。
有两种方法：

- 指定长度值，比如 2px 或 0.1em；
- 使用 3 个关键字之一，它们分别是 thin 、medium（默认值） 和 thick。** 注意：CSS 没有定义 3 个关键字的具体宽度 **

可以按照 top-right-bottom-left 的顺序设置元素的各边边框：
```css
p {border-style: solid; border-width: 15px 5px 15px 5px;}
```

也可以通过下列属性分别设置边框各边的宽度：

- border-top-width
- border-right-width
- border-bottom-width
- border-left-width

如果把 border-style 设置为 none，即使border-width有值，也显示为无边框。

#### 边框的颜色(border-color)
可以使用  border-color 属性 设置边框颜色。它一次可以接受最多 4 个颜色值。

可以使用任何类型的颜色值，例如可以是命名颜色，也可以是十六进制和 RGB 值：

```css
p {
  border-style: solid;
  border-color: blue rgb(25%,35%,45%) #909090 red;
  }
```

也可以为单边边框设置颜色：

- border-top-color
- border-right-color
- border-bottom-color
- border-left-color

### CSS 外边距(margin)
使用 margin 属性设置外边距。

margin 属性接受任何长度单位，可以是像素、英寸、毫米或 em。

下面的例子在 h1 元素的各个边上设置了 1/4 英寸宽的空白：
```css
h1 {margin : 0.25in;}
```

下面的例子为 h1 元素的四个边分别定义了不同的外边距：

```css
h1 {margin : 10px 0px 15px 5px;}
```
也可以单独设置外边距：

- margin-top
- margin-right
- margin-bottom
- margin-left

### CSS定位(Positioning)
定位的基本思想很简单，它允许定义元素框相对于其正常位置应该出现的位置，或者相对于父元素、另一个元素甚至浏览器窗口本身的位置。

div、h1 或 p 元素常常被称为块级元素。这意味着这些元素显示为一块内容，即“块框”。
与之相反，span 和 strong 等元素称为“行内元素”，这是因为它们的内容显示在行中，即“行内框”。

#### CSS position 属性
通过使用 position 属性，我们可以选择 4 种不同类型的定位，这会影响元素框生成的方式。

position 属性值的含义：
##### 1.static
元素框正常生成。块级元素生成一个矩形框，作为文档流的一部分，行内元素则会创建一个或多个行框，置于其父元素中。

##### 2.relative
元素框偏移某个距离。元素仍保持其未定位前的形状，它原本所占的空间仍保留。

#####3.absolute
元素框从文档流完全删除，并相对于其包含块定位。包含块可能是文档中的另一个元素或者是初始包含块。元素原先在正常文档流中所占的空间会关闭，就好像元素原来不存在一样。元素定位后生成一个块级框，而不论原来它在正常流中生成何种类型的框。

#####4.fixed
元素框的表现类似于将 position 设置为 absolute，不过其包含块是视窗本身。


#### CSS 相对定位
如果对一个元素进行相对定位，它将出现在它所在的位置上。然后，可以通过设置垂直或水平位置，让这个元素“相对于”它的起点进行移动。
```html
<head>
<style type="text/css">
h2.pos_left
{
position:relative;
left:-20px
}
h2.pos_right
{
position:relative;
left:20px
}
</style>
</head>

<body>
<h2>这是位于正常位置的标题</h2>
<h2 class="pos_left">这个标题相对于其正常位置向左移动</h2>
<h2 class="pos_right">这个标题相对于其正常位置向右移动</h2>
<p>相对定位会按照元素的原始位置对该元素进行移动。</p>
<p>样式 "left:-20px" 从元素的原始左侧位置减去 20 像素。</p>
<p>样式 "left:20px" 向元素的原始左侧位置增加 20 像素。</p>
</body>
```
如果将 top 设置为 20px，那么框将在原位置顶部下面 20 像素的地方。如果 left 设置为 30 像素，那么会在元素左边创建 30 像素的空间，也就是将元素向右移动。


#### CSS 绝对定位
绝对定位使元素的位置与文档流无关，因此不占据空间。这一点与相对定位不同，相对定位实际上被看作普通流定位模型的一部分，因为元素的位置相对于它在普通流中的位置。

普通流中其它元素的布局就像绝对定位的元素不存在一样：

```html
<head>
    <style type="text/css">
        h2.pos_abs
        {
            position:absolute;
            left:100px;
            top:150px
        }
    </style>
</head>

<body>
<h2 class="pos_abs">this is absolute title</h2>
<p>通过绝对定位，元素可以放置到页面上的任何位置。下面的标题距离页面左侧 100px，距离页面顶部 150px。</p>
</body>
```

#### CSS 浮动
我们通过 float 属性实现元素的浮动。

## CSS选择器
### 元素选择器
元素选择器又称为类型选择器（type selector）。
类型选择器匹配文档树中该元素类型的每一个实例。

```css
html {color:black;}
h1 {color:blue;}
h2 {color:silver;}
```
### 选择器分组
假设希望 h2 元素和段落都有灰色。可以写成：
```css
h2, p {color:gray;}
```
如果对同一类元素声明多个样式，可以写在一起，用分号分开。

```css
h1 {font: 28px Verdana; color: white; background: black;}
```

### 类选择器
类选择器允许以一种独立于文档元素的方式来指定样式。

```css
.important {color:red;}

<h1 class="important">
This heading is very important.
</h1>

<p class="important">
This paragraph is very important.
</p>
```
如果想仅对元素p进行样式设置，可以写成：p.important {color:red;}
又想对h1设置不同样式，可以写成：

```css
p.important {color:red;}
h1.important {color:blue;}
```
#### 多类选择器
一个 class 中可以包含多个样式，之间用空格分隔。例如，如果希望将一个特定的元素同时标记为重要（important）和警告（warning），就可以写作：

```css
.important {font-weight:bold;}
.warning {font-style:italic;}
.important.warning {background:silver;}

<p class="important warning">
This paragraph is a very important warning.
</p>
```
class 为 important 的所有元素都是粗体，而 class 为 warning 的所有元素为斜体，class 中同时包含 important 和 warning 的所有元素还有一个银色的背景 。

如果把把两个类选择器链接在一起，仅可以选择同时包含这些类名的元素（类名的顺序不限）。比如：
```css
.important.warning {background:silver;}
```
### ID 选择器
ID 选择器允许以一种独立于文档元素的方式来指定样式。
ID 选择器前面有一个 # 号，
ID 选择器不引用 class 属性的值，它要引用 id 属性中的值。

```css
#intro {font-weight:bold;}

<p id="intro">This is a paragraph of introduction.</p>
```
#### ID选择器和类选择器的区别

- 区别 1：只能在文档中使用一次
- 区别 2：不能使用 ID 列表
- 区别 3：ID 能包含更多含义
比如，并不知道要添加的样式出现在哪个元素上。

### 属性选择器
如果希望选择有某个属性的元素，而不论属性值是什么，可以使用简单属性选择器。

比如希望把包含标题（title）的所有元素变为红色，可以写作：

```css
*[title] {color:red;}
```
也可以只对有 href 属性的锚（a 元素）应用样式：

```css
a[href] {color:red;}
```

还可以对多个属性进行选择，只需将属性选择器链接在一起即可。

例如，为了将同时有 href 和 title 属性的 HTML 超链接的文本设置为红色，可以这样写：

```css
a[href][title] {color:red;}
```

除了选择拥有某些属性的元素，还可以进一步缩小选择范围，只选择有特定属性值的元素。


例如，假设希望将指向 Web 服务器上某个指定文档的超链接变成红色，可以这样写：

```css
a[href="http://www.w3school.com.cn/about_us.asp"] {color: red;}
```

### 后代选择器
我们可以定义后代选择器来创建一些规则，使这些规则在某些文档结构中起作用，而在另外一些结构中不起作用。

比如，如果希望只对 h1 元素中的 em 元素应用样式，可以这样写：

```css
h1 em {color:red;}
```
中间用空格分隔。
h1 em 这个语法就会选择从 ul 元素继承的所有 em 元素，而不论 em 的嵌套层次多深。

后代选择器还可以实现一些高级功能。

假设有一个文档，其中有一个边栏，还有一个主区。边栏的背景为蓝色，主区的背景为白色，这两个区都包含链接列表。不能把所有链接都设置为蓝色，因为这样一来边栏中的蓝色链接都无法看到。

可以写成：

```css
div.sidebar {background:blue;}
div.maincontent {background:white;}
div.sidebar a:link {color:white;}
div.maincontent a:link {color:blue;}
```
### 子元素选择器
子元素选择器（Child selectors）只能选择作为某元素子元素的元素。

如果希望选择只作为 h1 元素子元素的 strong 元素，可以这样写：
```css
h1 > strong {color:red;}
```
这个规则会把第一个 h1 下面的两个 strong 元素变为红色，但是第二个 h1 中的 strong 不受影响：

```css
<h1>This is <strong>very</strong> <strong>very</strong> important.</h1>
<h1>This is <em>really <strong>very</strong></em> important.</h1>
```

子选择器使用了大于号（子结合符）。
子结合符两边可以有空白符，这是可选的。

### 相邻兄弟选择器
相邻兄弟选择器（Adjacent sibling selector）可选择紧接在另一元素后的元素，且二者有相同父元素。

例如，如果要增加紧接在 h1 元素后出现的段落的上边距，可以这样写：

```css
h1 + p {margin-top:50px;}
```
表示：选择紧接在 h1 元素后出现的段落，h1 和 p 元素拥有共同的父元素
注意：用一个结合符只能选择两个相邻兄弟中的第二个元素。第一个元素不受影响。

### 伪类
CSS 伪类用于向某些选择器添加特殊的效果。

伪类的语法：
```css
selector : pseudo-class {property: value}
```

CSS 类也可与伪类搭配使用。
selector.class : pseudo-class {property: value}

#### 锚伪类
在支持 CSS 的浏览器中，链接的不同状态都可以不同的方式显示，这些状态包括：活动状态，已被访问状态，未被访问状态，和鼠标悬停状态。
```css
a:link {color: #FF0000}		/* 未访问的链接 */
a:visited {color: #00FF00}	/* 已访问的链接 */
a:hover {color: #FF00FF}	/* 鼠标移动到链接上 */
a:active {color: #0000FF}	/* 选定的链接 */
```

伪类可以与 CSS 类配合使用：
```css
a.red : visited {color: #FF0000}

<a class="red" href="css_syntax.asp">CSS Syntax</a>
```

假如上面的例子中的链接被访问过，那么它将显示为红色。

### 伪元素
CSS 伪元素用于向某些选择器设置特殊效果。

语法：
```css
selector:pseudo-element {property:value;}
```

CSS 类也可以与伪元素配合使用：
```css
selector.class:pseudo-element {property:value;}
```
"first-line" 伪元素用于向文本的首行设置特殊样式。

在下面的例子中，浏览器会根据 "first-line" 伪元素中的样式对 p 元素的第一行文本进行格式化：

```css
p:first-line
  {
  color:#ff0000;
  font-variant:small-caps;
  }
```

### CSS水平对齐
#### 使用 margin 属性来水平对齐
可通过将左和右外边距设置为 "auto"，来对齐块元素。


···css
.center
{
    margin:auto;
    width:70%;
    background-color:#b0e0e6;
}

#### 使用 position 属性进行左和右对齐
对齐元素的方法之一是使用绝对定位。
```css
 .right
 {
     position:absolute;
     right:0px;
     width:300px;
     background-color:#b0e0e6;
 }
```

#### 使用 float 属性来进行左和右对齐
对齐元素的另一种方法是使用 float 属性：

```css
 .right
 {
     float:right;
     width:300px;
     background-color:#b0e0e6;
 }
```

### CSS 尺寸
CSS 尺寸 (Dimension) 属性允许你控制元素的高度和宽度。

#### 使用像素值设置图像的高度
```css
img.normal
{
    height: auto
}

img.big
{
    height: 160px
}

img.small
{
    height: 30px
}
```

#### 使用百分比设置图像的高度
```css
 img.normal
 {
     height: auto
 }
 img.big
 {
     height: 50%
 }
 img.small
 {
     height: 10%
 }
```

#### CSS 尺寸属性

| 属性 | 描述 |
|--------|--------|
|   height     |   设置元素的高度。     |
|  line-height      |   设置行高。     |
|  max-height      |   设置元素的最大高度。     |
|  max-width      |   设置元素的最大宽度。     |
|  min-height      |  设置元素的最小高度。      |
|  min-width      |    设置元素的最小宽度。    |
|  width      |   设置元素的宽度。     |
































