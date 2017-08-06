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
    <input type="hidden" id="userId" name="userId" value="${user.id}"/>
    <div style="margin-top:15px;">
        <label>用户姓名：</label><input class="common-text required" id="account" name="account" value="${user.account}" type="text">
    </div>
    <div style="margin-top:15px;">
        <label>登录密码：</label><input class="common-text required" id="password" name="password" value="" type="password">
    </div>
    <div style="margin-top:15px;">
        <label>用户权限：</label>
        <input type="checkbox" name="roleId" value="0001" <c:if test="${fn:indexOf(user.roleId,'0001') > -1}">checked</c:if>/>滩访市供电局

        <input type="checkbox" name="roleId" value="0002" <c:if test="${fn:indexOf(user.roleId,'0002') > -1}">checked</c:if>/>国网安丘市供电公司

        <input type="checkbox" name="roleId" value="0003" <c:if test="${fn:indexOf(user.roleId,'0003') > -1}">checked</c:if>/>昌乐供电公司

        <input type="checkbox" name="roleId" value="0004" <c:if test="${fn:indexOf(user.roleId,'0004') > -1}">checked</c:if>/>昌邑供电公司

        <input type="checkbox" name="roleId" value="0005" <c:if test="${fn:indexOf(user.roleId,'0005') > -1}">checked</c:if>/>高密市供电公司

        <input type="checkbox" name="roleId" value="0006" <c:if test="${fn:indexOf(user.roleId,'0006') > -1}">checked</c:if>/>寒亭区供电公司

        <input type="checkbox" name="roleId" value="0007" <c:if test="${fn:indexOf(user.roleId,'0007') > -1}">checked</c:if>/>国网临朐县供电公司

        <input type="checkbox" name="roleId" value="0008"  <c:if test="${fn:indexOf(user.roleId,'0008') > -1}">checked</c:if>/>青州市供电公司

        <input type="checkbox" name="roleId" value="0009"  <c:if test="${fn:indexOf(user.roleId,'0009') > -1}">checked</c:if>/>国网寿光市供电公司

        <input type="checkbox" name="roleId" value="00010" <c:if test="${fn:indexOf(user.roleId,'0010') > -1}">checked</c:if>/>国网诸城市供电公司
    </div>
</div>
</form>
</body>
</html>