
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









