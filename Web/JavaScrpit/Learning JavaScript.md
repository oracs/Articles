
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