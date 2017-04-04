<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<form action="/api/image/upload" method="post" enctype="multipart/form-data">
    <input type="hidden" name="uName" value="文小钱"/>
    <input type="hidden" name="idCard" value="430223198704276550"/>
    <input name="file" type="file" /><button type="submit">submit</button>
</form>
</body>
</html>