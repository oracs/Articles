**修订记录**

| 时间 | 内容 |
|--------|--------|
| 2018.02.01 | 初稿 |

## 概述
AJAX(Asynchronous JavaScript and XML)：异步的 JavaScript 和 XML。
它不是新的编程语言，而是一种使用现有标准的新方法。它可以与服务器交换数据并更新部分网页，在不需要重新加载整个页面。

### 创建对象
XMLHttpRequest 是 AJAX 的基础。用于在后台与服务器交换数据。这意味着可以在不重新加载整个网页的情况下，对网页的某部分进行更新。

创建 XMLHttpRequest 对象的语法：
```javascript
variable=new XMLHttpRequest();
```

### 向服务器发送请求
```javascript
xmlhttp.open("GET","test1.txt",true);
xmlhttp.send();
```

| 方法 | 描述 |
|--------|--------|
|  open(method,url,async)      |  规定请求的类型、URL 以及是否异步处理请求。<br>•method：请求的类型；GET 或 POST<br>•url：文件在服务器上的位置<br>•async：true（异步）或 false（同步）      |
|  send(string)      |   将请求发送到服务器。<br> •string：仅用于 POST 请求 |
     |

### 服务器响应
通过xmlhttp.xxx属性获取返回结果。

| 属性 | 描述 |
|--------|--------|
|  responseText      |  获得字符串形式的响应数据。     |
|  responseXML     |   获得 XML 形式的响应数据。 |

###  onreadystatechange 事件
当请求被发送到服务器时，我们需要执行一些基于响应的任务。
每当 readyState 改变时，就会触发 onreadystatechange 事件。
readyState 属性存有 XMLHttpRequest 的状态信息。
下面是 XMLHttpRequest 对象的三个重要的属性：

| 属性 | 描述 |
|--------|--------|
| onreadystatechange  | 存储函数（或函数名），每当 readyState 属性改变时，就会调用该函数。  |
| readyState  |  存有 XMLHttpRequest 的状态。从 0 到 4 发生变化。<br>•0: 请求未初始化<br>•1: 服务器连接已建立<br>•2: 请求已接收<br>•3: 请求处理中<br>•4: 请求已完成，且响应已就绪 |
| status  |  200: "OK"<br>404: 未找到页面 |


例子
```javascript
<html>
<head>
<script type="text/javascript">
function loadXMLDoc()
{
var xmlhttp;
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
    document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
    }
  }
xmlhttp.open("POST","demo_post.asp.htm",true);
xmlhttp.send();
}
</script>
</head>
<body>

<h2>AJAX</h2>
<button type="button" onclick="loadXMLDoc()">请求数据</button>
<div id="myDiv"></div>

</body>
</html>

```