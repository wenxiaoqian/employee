<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>后台管理</title>
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/main.css"/>
    <script type="text/javascript" src="/js/jquery.js"></script>
</head>
<body>
<form id="user_form">
<div style="padding:20px;">
    <div>
        <label>所在部门：</label>${deptName}
    </div>
    <input type="hidden" id="userId" name="userId" value="${user.id}"/>
    <div style="margin-top:15px;">
        <label>用户姓名：</label><input class="common-text required" id="uname" name="uname" value="${user.username}" type="text">
    </div>
    <div style="margin-top:15px;">
        <label>用户手机：</label><input class="common-text required" id="iphone" name="iphone" value="${user.iphone}" type="text">
    </div>
    <div style="margin-top:15px;">
        <label>登录密码：</label><input class="common-text required" id="userpwd" name="userpwd" value="" type="password">
    </div>
    <div style="margin-top:15px;">
        <label>用户权限：</label>
        <input type="checkbox" name="roleId" value="1" <c:if test="${fn:indexOf(user.role,'1') > -1}">checked</c:if>/>信息管理
        <input type="checkbox" name="roleId" value="2" <c:if test="${fn:indexOf(user.role,'2') > -1}">checked</c:if>/>允许登录PC电脑
    </div>
</div>
</form>
</body>
</html>