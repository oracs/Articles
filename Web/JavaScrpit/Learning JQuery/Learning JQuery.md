
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





