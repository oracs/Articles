
## 概述
JQuery是一个“写的更少，但做的更多”的轻量级 JavaScript 库。

jQuery 库包含以下特性：

- HTML 元素选取
- HTML 元素操作
- CSS 操作
- HTML 事件函数
- JavaScript 特效和动画
- HTML DOM 遍历和修改
- AJAX
- Utilities


### 加载JQuery库
JQuery库需要从网上下载，就是一个.js文件。然后在header中引入。

```javascript
<head>
<script type="text/javascript" src="jquery.js"></script>
</head>
```
注意，`<script>` 标签应该位于页面的 `<head>` 部分。

## 语法
基础语法是：$(selector).action()

### 语法实例
```javascript
$(this).hide()  // 隐藏当前的 HTML 元素
${"#test"}.hide()  // 隐藏 id="test" 的元素
${"p"}.hide()  // 隐藏所有 <p> 元素
$(".test").hide()  // 隐藏所有 class="test" 的元素。
```

### 文档就绪函数
```javascript
$(document).ready(function(){

--- jQuery functions go here ----

});
```
这是为了防止文档在未完全加载就运行 jQuery 代码。

## jQuery选择器
除了上述常规的表达式外，jQuery 还支持使用 XPath 表达式来选择带有给定属性的元素。
$("[href]") 选取所有带有 href 属性的元素。
$("[href='#']") 选取所有带有 href 值等于 "#" 的元素。
$("[href!='#']") 选取所有带有 href 值不等于 "#" 的元素。
$("[href$='.jpg']") 选取所有 href 值以 ".jpg" 结尾的元素。


| 语法 | 描述 |
|--------|--------|
|   $(this)     |   当前 HTML 元素     |
|   $("p")     |   所有 <p> 元素     |
|   $("p.intro")     |   所有 class="intro" 的 <p> 元素     |
|   $(".intro")     |  所有 class="intro" 的元素      |
|   $("#intro")     |  id="intro" 的元素      |
|   $("ul li:first")     |  每个 <ul> 的第一个 <li> 元素     |
|   $("[href$='.jpg']")     |   所有带有以 ".jpg" 结尾的属性值的 href 属性     |
|   $("div#intro .head")     |   id="intro" 的 <div> 元素中的所有 class="head" 的元素     |

## jQuery 事件
$("button").click(function() {..some code... } )

也可以把函数放在单独的文件中，
```javascript
<head>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript" src="my_jquery_functions.js"></script>
</head>
```

jquery事件例子：

| Event 函数 | 绑定函数至 |
|--------|--------|
| $(document).ready(function)   | 将函数绑定到文档的就绪事件   |
| $(selector).click(function)   |  触发或将函数绑定到被选元素的点击事件  |
| $(selector).dblclick(function)   | 触发或将函数绑定到被选元素的双击事件   |
| $(selector).focus(function)   |  触发或将函数绑定到被选元素的获得焦点事件  |
| $(selector).mouseover(function)   |  触发或将函数绑定到被选元素的鼠标悬停事件  |


## JQuery效果
### 隐藏和显示
隐藏：$(selector).hide(speed,callback);
显示：$(selector).show(speed,callback);

其中，speed，callback参数可以为空。
speed 参数规定隐藏/显示的速度，可以取以下值："slow"、"fast" 或毫秒。
callback 参数是隐藏或显示完成后所执行的函数名称。

```javascript
$("button").click(function(){
  $("p").hide(1000);
});

```

还可以使用 toggle() 方法来切换 hide() 和 show() 方法。
```javcscript
$("button").click(function(){
  $("p").toggle();
});
```

### 淡入淡出
jQuery 拥有下面四种 fade 方法：
•fadeIn()
•fadeOut()
•fadeToggle()
•fadeTo()

#### fadeIn() 方法 
使隐藏的元素淡出。
语法：$(selector).fadeIn(speed,callback);

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
<script>
$(document).ready(function(){
  $("button").click(function(){
    $("#div1").fadeIn();
    $("#div2").fadeIn("slow");
    $("#div3").fadeIn(3000);
  });
});
</script>
</head>

<body>
<p>演示带有不同参数的 fadeIn() 方法。</p>
<button>点击这里，使三个矩形淡入</button>
<br><br>
<div id="div1" style="width:80px;height:80px;display:none;background-color:red;"></div>
<br>
<div id="div2" style="width:80px;height:80px;display:none;background-color:green;"></div>
<br>
<div id="div3" style="width:80px;height:80px;display:none;background-color:blue;"></div>
</body>
</html>
```

#### fadeOut() 方法 
使显示的元素淡入。
语法：$(selector).fadeOut(speed,callback);

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript">
$(document).ready(function(){
  $("button").click(function(){
    $("#div1").fadeOut();
    $("#div2").fadeOut("slow");
    $("#div3").fadeOut(3000);
  });
});
</script>
</head>

<body>
<p>演示带有不同参数的 fadeOut() 方法。</p>
<button>点击这里，使三个矩形淡出</button>
<br><br>
<div id="div1" style="width:80px;height:80px;background-color:red;"></div>
<br>
<div id="div2" style="width:80px;height:80px;background-color:green;"></div>
<br>
<div id="div3" style="width:80px;height:80px;background-color:blue;"></div>
</body>
</html>
```

### fadeToggle() 方法
fadeToggle() 方法可以在 fadeIn() 与 fadeOut() 方法之间进行切换。
语法：$(selector).fadeToggle(speed,callback);

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
<script>
$(document).ready(function(){
  $("button").click(function(){
    $("#div1").fadeToggle();
    $("#div2").fadeToggle("slow");
    $("#div3").fadeToggle(3000);
  });
});
</script>
</head>

<body>

<p>演示带有不同参数的 fadeToggle() 方法。</p>
<button>点击这里，使三个矩形淡入淡出</button>
<br><br>
<div id="div1" style="width:80px;height:80px;background-color:red;"></div>
<br>
<div id="div2" style="width:80px;height:80px;background-color:green;"></div>
<br>
<div id="div3" style="width:80px;height:80px;background-color:blue;"></div>
</body>

</html>
```

#### fadeTo() 方法
fadeTo() 方法允许渐变为给定的不透明度（值介于 0 与 1 之间）。

语法：$(selector).fadeTo(speed,opacity,callback);

- speed 参数， 必须，规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。
- opacity 参数，必须，将淡入淡出效果设置为给定的不透明度（值介于 0 与 1 之间）。
- callback 参数，可选，是该函数完成后所执行的函数名称。

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
<script>
$(document).ready(function(){
  $("button").click(function(){
    $("#div1").fadeTo("slow",0.15);
    $("#div2").fadeTo("slow",0.4);
    $("#div3").fadeTo("slow",0.7);
  });
});
</script>
</head>

<body>

<p>演示带有不同参数的 fadeTo() 方法。</p>
<button>点击这里，使三个矩形淡出</button>
<br><br>
<div id="div1" style="width:80px;height:80px;background-color:red;"></div>
<br>
<div id="div2" style="width:80px;height:80px;background-color:green;"></div>
<br>
<div id="div3" style="width:80px;height:80px;background-color:blue;"></div>

</body>

</html>
```

### 滑动
jQuery 拥有以下滑动方法：
•slideDown()
•slideUp()
•slideToggle()


#### slideDown() 方法
slideDown() 方法用于向下滑动元素。

语法：$(selector).slideDown(speed,callback);

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript">
$(document).ready(function(){
  $(".flip").click(function(){
    $(".panel").slideDown("slow");
  });
});
</script>

<style type="text/css">
div.panel,p.flip
{
margin:0px;
padding:5px;
text-align:center;
background:#e5eecc;
border:solid 1px #c3c3c3;
}
div.panel
{
height:120px;
display:none;
}
</style>
</head>

<body>

<div class="panel">
<p>W3School - 领先的 Web 技术教程站点</p>
<p>在 W3School，你可以找到你所需要的所有网站建设教程。</p>
</div>

<p class="flip">请点击这里</p>

</body>
</html>
```

#### slideUp() 方法
slideUp() 方法用于向上滑动元素。

语法：$(selector).slideUp(speed,callback);

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript">
$(document).ready(function(){
  $(".flip").click(function(){
    $(".panel").slideUp("slow");
  });
});
</script>

<style type="text/css">
div.panel,p.flip
{
margin:0px;
padding:5px;
text-align:center;
background:#e5eecc;
border:solid 1px #c3c3c3;
}
div.panel
{
height:120px;
}
</style>
</head>

<body>

<div class="panel">
<p>W3School - 领先的 Web 技术教程站点</p>
<p>在 W3School，你可以找到你所需要的所有网站建设教程。</p>
</div>

<p class="flip">请点击这里</p>

</body>

</html>
```

#### slideToggle() 方法
slideToggle() 方法可以在 slideDown() 与 slideUp() 方法之间进行切换。

```javascript

<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript">
$(document).ready(function(){
$(".flip").click(function(){
    $(".panel").slideToggle("slow");
  });
});
</script>

<style type="text/css">
div.panel,p.flip
{
margin:0px;
padding:5px;
text-align:center;
background:#e5eecc;
border:solid 1px #c3c3c3;
}
div.panel
{
height:120px;
display:none;
}
</style>
</head>

<body>

<div class="panel">
<p>W3School - 领先的 Web 技术教程站点</p>
<p>在 W3School，你可以找到你所需要的所有网站建设教程。</p>
</div>

<p class="flip">请点击这里</p>

</body>
</html>
```

### 动画
#### animate() 方法
animate() 方法用于创建自定义动画。

语法：$(selector).animate({params},speed,callback);

params 参数，必选，定义形成动画的 CSS 属性。
speed 参数，可选，规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。
callback 参数，可选，是动画完成后所执行的函数名称。

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
</script>
<script>
$(document).ready(function(){
  $("button").click(function(){
    $("div").animate({left:'250px'});
  });
});
</script>
</head>

<body>
<button>开始动画</button>
<p>默认情况下，所有 HTML 元素的位置都是静态的，并且无法移动。如需对位置进行操作，记得首先把元素的 CSS position 属性设置为 relative、fixed 或 absolute。</p>
<div style="background:#98bf21;height:100px;width:100px;position:absolute;">
</div>

</body>

</html>
```

animate()方法还可以操作多个属性：
```javascript
$(document).ready(function(){
  $("button").click(function(){
    $("div").animate({
      left:'250px',
      opacity:'0.5',
      height:'150px',
      width:'150px'
    });
  });
});
```

animate() 方法还可以使用相对值。
需要在值的前面加上 += 或 -=：

```javascript
$("button").click(function(){
  $("div").animate({
    left:'250px',
    height:'+=150px',
    width:'+=150px'
  });
});
```

### stop()方法
stop() 方法用于在动画或效果完成前对它们进行停止。
```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
</script>

<script> 
$(document).ready(function(){
  $("#flip").click(function(){
    $("#panel").slideDown(5000);
  });
  $("#stop").click(function(){
    $("#panel").stop();
  });
});
</script>
 
<style type="text/css"> 
#panel,#flip
{
padding:5px;
text-align:center;
background-color:#e5eecc;
border:solid 1px #c3c3c3;
}
#panel
{
padding:50px;
display:none;
}
</style>
</head>

<body>
 
<button id="stop">停止滑动</button>
<div id="flip">点击这里，向下滑动面板</div>
<div id="panel">Hello world!</div>

</body>

</html>
```

### Callback 函数
Callback 函数在当前动画 100% 完成之后执行。
```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
</script>

<script type="text/javascript">
$(document).ready(function(){
  $("button").click(function(){
  $("p").hide(1000,function(){
    alert("The paragraph is now hidden");
    });
  });
});
</script>
</head>
<body>
<button type="button">Hide</button>
<p>This is a paragraph with little content.</p>
</body>

</html>
```

### Chaining
Chaining，方法连接， 允许我们在一条语句中允许多个 jQuery 方法（在相同的元素上）。

```javascript

<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
</script>

<script>
$(document).ready(function()
  {
  $("button").click(function(){
    $("#p1").css("color","red").slideUp(2000).slideDown(2000);
  });
});
</script>
</head>

<body>

<p id="p1">jQuery 乐趣十足！</p>
<button>点击这里</button>

</body>


</html>
```

## JQuery HTML相关
### 获得HTML内容和属性
•text() - 设置或返回所选元素的文本内容
•html() - 设置或返回所选元素的内容（包括 HTML 标记）
•val() - 设置或返回表单字段的值
•attr() - 用于获取属性值。

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
<script>
$(document).ready(function(){
  $("#btn1").click(function(){
    alert("Text: " + $("#test").text());
  });
  $("#btn2").click(function(){
    alert("HTML: " + $("#test").html());
  });
  $("btn3").click(function(){
    alert("Value: " + $("#name").val());
  });
});
</script>
</head>

<body>
<p id="test">这是段落中的<b>粗体</b>文本。</p>
<p>姓名：<input type="text" id="name" value="米老鼠"></p>
<button id="btn1">显示文本</button>
<button id="btn2">显示 HTML</button>
<button id="btn3">显示值</button>
</body>

</html>
```

### 设置HTML内容和属性
•text() - 设置或返回所选元素的文本内容
•html() - 设置或返回所选元素的内容（包括 HTML 标记）
•val() - 设置或返回表单字段的值

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>

<script>
$(document).ready(function(){
  $("#btn1").click(function(){
    $("#test1").text("Hello world!");
  });
  $("#btn2").click(function(){
    $("#test2").html("<b>Hello world!</b>");
  });
  $("#btn3").click(function(){
    $("#test3").val("Dolly Duck");
  });
});
</script>
</head>

<body>
<p id="test1">这是段落。</p>
<p id="test2">这是另一个段落。</p>
<p>Input field: <input type="text" id="test3" value="Mickey Mouse"></p>
<button id="btn1">设置文本</button>
<button id="btn2">设置 HTML</button>
<button id="btn3">设置值</button>
</body>

</html>
```

另外，这text(),html(),val()3个函数还支持回调函数。

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>

<script>
$(document).ready(function(){
  $("#btn1").click(function(){
    $("#test1").text(function(i,origText){
      return "Old text: " + origText + " New text: Hello world! (index: " + i + ")";
    });
  });

  $("#btn2").click(function(){
    $("#test2").html(function(i,origText){
      return "Old html: " + origText + " New html: Hello <b>world!</b> (index: " + i + ")";
    });
  });

});
</script>
</head>

<body>
<p id="test1">这是<b>粗体</b>文本。</p>
<p id="test2">这是另一段<b>粗体</b>文本。</p>
<button id="btn1">显示旧/新文本</button>
<button id="btn2">显示旧/新 HTML</button>
</body>

</html>
```

### 添加元素
•append() - 在被选元素的结尾插入内容
•prepend() - 在被选元素的开头插入内容
•after() - 在被选元素之后插入内容
•before() - 在被选元素之前插入内容

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>

<script>
$(document).ready(function(){
  $("#btn1").click(function(){
    $("p").append(" <b>Appended text</b>.");
  });

  $("#btn2").click(function(){
    $("ol").prepend("<li>Prepended item</li>");
  });

  $("#btn3").click(function(){
    $("#li2").before("<b>before text</b>");
  });

  $("#btn4").click(function(){
    $("#li2").after("<b>after text</b>");
  });
});
</script>
</head>

<body>
<p>This is a paragraph.</p>
<p>This is another paragraph.</p>
<ol>
<li>List item 1</li>
<li id="li2">List item 2</li>
<li>List item 3</li>
</ol>
<button id="btn1">追加文本</button>
<button id="btn2">追加列表项</button>
<button id="btn3">before insert文本</button>
<button id="btn4">after insert列表项</button>
</body>

</html>
```

### 删除元素
•remove() - 删除被选元素（及其子元素）
•empty() - 从被选元素中删除子元素

```javascript

<html>
<head>
<script type="text/javascript" src="jquery.js"></script>

<script>
$(document).ready(function(){
  $("button").click(function(){
    $("#div1").remove();
  });
});
</script>
</head>

<body>

<div id="div1" style="height:100px;width:300px;border:1px solid black;background-color:yellow;">
This is some text in the div.
<p>This is a paragraph in the div.</p>
<p>This is another paragraph in the div.</p>
</div>

<br>
<button>删除 div 元素</button>

</body>
</html>
```

### 获取并设置 CSS 类
•addClass() - 向被选元素添加一个或多个类
•removeClass() - 从被选元素删除一个或多个类
•toggleClass() - 对被选元素进行添加/删除类的切换操作
•css() - 设置或返回样式属性

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>

<script>
$(document).ready(function(){
  $("button").click(function(){
    $("h1,h2,p").addClass("blue");
    $("div").addClass("important");
  });
});
</script>
<style type="text/css">
.important
{
font-weight:bold;
font-size:xx-large;
}
.blue
{
color:blue;
}
</style>
</head>
<body>

<h1>标题 1</h1>
<h2>标题 2</h2>
<p>这是一个段落。</p>
<p>这是另一个段落。</p>
<div>这是非常重要的文本！</div>
<br>
<button>向元素添加类</button>

</body>

</html>
```

### CSS方法
css() 方法设置或返回被选元素的一个或多个样式属性。

- 返回CSS属性：css("propertyname");
- 设置CSS属性：css("propertyname","value");
- 设置多个CSS属性：css({"propertyname":"value","propertyname":"value",...});

```javascript
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>

<script>
$(document).ready(function(){
  $("button").click(function(){
    $("p").css("background-color","yellow");
  });
});
</script>
</head>

<body>
<h2>这是标题</h2>
<p style="background-color:#ff0000">这是一个段落。</p>
<p style="background-color:#00ff00">这是一个段落。</p>
<p style="background-color:#0000ff">这是一个段落。</p>
<p>这是一个段落。</p>
<button>设置 p 元素的背景色</button>
</body>

</html>
```

### 尺寸

- width() 方法设置或返回元素的宽度（不包括内边距、边框或外边距）。
- height() 方法设置或返回元素的高度（不包括内边距、边框或外边距）。
- innerWidth() 方法返回元素的宽度（包括内边距）。
- innerHeight() 方法返回元素的高度（包括内边距）。
- outerWidth() 方法返回元素的宽度（包括内边距和边框）。
- outerHeight() 方法返回元素的高度（包括内边距和边框）。
- outerWidth(true) 方法返回元素的宽度（包括内边距、边框和外边距）。
- outerHeight(true) 方法返回元素的高度（包括内边距、边框和外边距）。

## JQuery遍历
### 向上遍历 DOM 树
•parent()：返回被选元素的直接父元素。
•parents():返回被选元素的所有祖先元素，它一路向上直到文档的根元素 (`<html>`)。
•parentsUntil():返回介于两个给定元素之间的所有祖先元素。

#### parent()
返回被选元素的直接父元素。
```javascript
<!DOCTYPE html>
<html>
<head>
<style>
.ancestors *
{
display: block;
border: 2px solid lightgrey;
color: lightgrey;
padding: 5px;
margin: 15px;
}
</style>
<script src="jquery.js">
</script>
<script>
$(document).ready(function(){
  $("span").parent().css({"color":"red","border":"2px solid red"});
});
</script>
</head>
<body>

<div class="ancestors">
  <div style="width:500px;">div (曾祖父)
    <ul>ul (祖父)
      <li>li (直接父)
        <span>span</span>
      </li>
    </ul>
  </div>

  <div style="width:500px;">div (祖父)
    <p>p (直接父)
        <span>span</span>
      </p>
  </div>
</div>

</body>
</html>
```

#### parents() 方法
返回被选元素的所有祖先元素，它一路向上直到文档的根元素 (`<html>`)。
```javascript
$(document).ready(function(){
  $("span").parents();
});
```

#### parentsUntil()
返回介于两个给定元素之间的所有祖先元素。
```javascript
$(document).ready(function(){
  $("span").parentsUntil("div");
});
```

### 向下遍历 DOM 树
用于向下遍历 DOM 树的 jQuery 方法：
•children()：返回被选元素的所有直接子元素。
•find(): 返回被选元素的后代元素，一路向下直到最后一个后代。

#### children() 方法
返回被选元素的所有直接子元素。

```javascript
<!DOCTYPE html>
<html>
<head>
<style>
.descendants *
{
display: block;
border: 2px solid lightgrey;
color: lightgrey;
padding: 5px;
margin: 15px;
}
</style>
<script src="jquery.js">
</script>
<script>
$(document).ready(function(){
  $("div").children().css({"color":"red","border":"2px solid red"});
});
</script>
</head>
<body>

<div class="descendants" style="width:500px;">div (当前元素)
  <p>p (子)
    <span>span (孙)</span>
  </p>
  <p>p (child)
    <span>span (孙)</span>
  </p>
</div>

</body>
</html>

```
####  find() 方法
返回被选元素的后代元素，一路向下直到最后一个后代。返回所有满足条件的元素。

```javascript
<!DOCTYPE html>
<html>
<head>
<style>
.descendants *
{
display: block;
border: 2px solid lightgrey;
color: lightgrey;
padding: 5px;
margin: 15px;
}
</style>
<script src="jquery.js">
</script>
<script>
$(document).ready(function(){
  $("div").find("span").css({"color":"red","border":"2px solid red"});
});
</script>
</head>
<body>

<div class="descendants" style="width:500px;">div (current element)
  <p>p (子)
    <span>span (孙)</span>
  </p>
  <p>p (child)
    <span>span (孙)</span>
  </p>
</div>

</body>
</html>
```

### 遍历同胞元素
常用的 DOM 树水平遍历方法：
•siblings()： 
•next()
•nextAll()
•nextUntil()
•prev()
•prevAll()
•prevUntil()

#### siblings()
返回被选元素的所有同胞元素。
```javascript
<!DOCTYPE html>
<html>
<head>
<style>
.siblings *
{
display: block;
border: 2px solid lightgrey;
color: lightgrey;
padding: 5px;
margin: 15px;
}
</style>
<script src="jquery.js">
</script>
<script>
$(document).ready(function(){
  $("h2").siblings().css({"color":"red","border":"2px solid red"});
});
</script>
</head>
<body class="siblings">

<div>div (父)
  <p>p</p>
  <span>span</span>
  <h2>h2</h2>
  <h3>h3</h3>
  <p>p</p>
</div>

</body>
</html>
```

#### next() 方法
返回被选元素的下一个同胞元素。
该方法只返回一个元素。

```javascript
<!DOCTYPE html>
<html>
<head>
<style>
.siblings *
{
display: block;
border: 2px solid lightgrey;
color: lightgrey;
padding: 5px;
margin: 15px;
}
</style>
<script src="jquery.js">
</script>
<script>
$(document).ready(function(){
  $("h2").next().css({"color":"red","border":"2px solid red"});
});
</script>
</head>
<body class="siblings">

<div>div (父)
  <p>p</p>
  <span>span</span>
  <h2>h2</h2>
  <h3>h3</h3>
  <p>p</p>
</div>

</body>
</html>
```
h3会标红

#### nextAll() 方法
返回被选元素的所有跟随的同胞元素。
下面的例子返回` <h2>` 的所有跟随的同胞元素：
```javascript
$(document).ready(function(){
  $("h2").nextAll();
});
```

#### nextUntil() 方法
法返回介于两个给定参数之间的所有跟随的同胞元素。

下面的例子返回介于 `<h2>` 与` <h6>` 元素之间的所有同胞元素：h3,h4,h5

```javascript
<!DOCTYPE html>
<html>
<head>
<style>
.siblings *
{
display: block;
border: 2px solid lightgrey;
color: lightgrey;
padding: 5px;
margin: 15px;
}
</style>
<script src="jquery.js">
</script>
<script>
$(document).ready(function(){
  $("h2").nextUntil("h6").css({"color":"red","border":"2px solid red"});
});
</script>
</head>
<body class="siblings">

<div>div (父)
  <p>p</p>
  <span>span</span>
  <h2>h2</h2>
  <h3>h3</h3>
  <h4>h4</h4>
  <h5>h5</h5>
  <h6>h6</h6>
  <p>p</p>
</div>

</body>
</html>

```

### 过滤
三个最基本的过滤方法是：

- first() 
- last()
- eq()

#### first() 方法
返回被选元素的首个元素。
下面的例子返回第一个div,p元素。

```javascript
<!DOCTYPE html>
<html>
<head>
<script src="jquery.js">
</script>
<script>
$(document).ready(function(){
  $("div p").first().css("background-color","yellow");
});
</script>
</head>
<body>

<h1>欢迎来到我的主页</h1>
<div>
<p>这是 div 中的一个段落。</p>
</div>

<div>
<p>这是 div 中的另一个段落。</p>
</div>

<p>这也是段落。</p>

</body>
</html>
```

#### last() 方法
返回被选元素的最后一个元素。

```javascript
$(document).ready(function(){
  $("div p").last();
});
```
####  eq() 方法
返回被选元素中带有指定索引号的元素。
索引号从 0 开始，下面的例子选取第二个 `<p>` 元素（索引号 1）：

```javascript
<!DOCTYPE html>
<html>
<head>
<script src="jquery.js">
</script>
<script>
$(document).ready(function(){
  $("p").eq(1).css("background-color","yellow");
});
</script>
</head>
<body>

<h1>欢迎来到我的主页</h1>
<p>我是唐老鸭 (index 0)。</p>
<p>唐老鸭 (index 1)。</p>
<p>我住在 Duckburg (index 2)。</p>
<p>我最好的朋友是米老鼠 (index 3)。</p>

</body>
</html>
```
#### filter() 方法
filter() 方法允许规定一个标准。不匹配这个标准的元素会被从集合中删除，匹配的元素会被返回。

下面的例子返回带有类名 "intro" 的所有` <p>` 元素：

```javascript
<!DOCTYPE html>
<html>
<head>
<script src="jquery.js">
</script>
<script>
$(document).ready(function(){
  $("p").filter(".intro").css("background-color","yellow");
});
</script>
</head>
<body>

<h1>欢迎来到我的主页</h1>
<p>我是唐老鸭。</p>
<p class="intro">我住在 Duckburg。</p>
<p class="intro">我爱 Duckburg。</p>
<p>我最好的朋友是 Mickey。</p>

</body>
</html>

```
#### not() 方法
not() 方法返回不匹配标准的所有元素。
not() 方法与 filter() 相反。

```javascript
$(document).ready(function(){
  $("p").not(".intro");
});
```





