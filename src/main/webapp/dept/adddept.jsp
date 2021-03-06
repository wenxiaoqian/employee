<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<form id="dept_form">
    <div style="padding:20px;">
        <c:if test="${pid != 0}">
            <div>
                <label>父级部门：</label>${deptName}
            </div>
        </c:if>
        <input type="hidden" id="parentId" name="parentId" value="${pid}"/>
        <div style="margin-top:15px;">
            <label>部门名称：</label><input class="common-text required" id="deptName" name="deptName" value="" type="text">
        </div>
        <div style="margin-top:15px;">
            <label>部门编号：</label><input class="common-text required" id="deptNo" name="deptNo" value="" type="text">
        </div>
    </div>
</form>
</body>
</html>