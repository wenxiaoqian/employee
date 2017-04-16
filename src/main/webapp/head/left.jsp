<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="sidebar-wrap">
    <div class="sidebar-title">
        <h1>菜单</h1>
    </div>
    <div class="sidebar-content">
        <ul class="sidebar-list">
            <li>
                <a href="#"><i class="icon-font">&#xe003;</i>常用操作</a>
                <ul class="sub-menu">

                    <li><a href="/home/question"><i class="icon-font">&#xe005;</i>
                        <c:if test="${fn:indexOf(user.role, '1') == -1}">
                        部门负责的反馈
                        </c:if>
                        <c:if test="${fn:indexOf(user.role, '1') > -1}">
                        问题反馈列表
                        </c:if>
                    </a></li>
                    <li><a href="/home/quescount"><i class="icon-font">&#xe006;</i>问题统计报表</a></li>
                    <li><a href="/home/userrank"><i class="icon-font">&#xe006;</i>用户排名报表</a></li>
                </ul>
            </li>
            <c:if test="${fn:indexOf(user.role, '1') > -1}">
            <li>
                <a href="#"><i class="icon-font">&#xe018;</i>系统管理</a>
                <ul class="sub-menu">
                    <li><a href="/home/user"><i class="icon-font">&#xe017;</i>用户管理</a></li>
                    <li><a href="/home/class"><i class="icon-font">&#xe008;</i>问题类型</a></li>
                </ul>
            </li>
            </c:if>
        </ul>
    </div>
</div>
</body>
</html>
