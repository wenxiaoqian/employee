<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>后台管理</title>
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"  />
    <link rel="stylesheet" href="/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <style type="text/css">
        .ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
    </style>
</head>
<body>
<jsp:include page="/head/head.jsp"/>
<div class="container clearfix">
    <jsp:include page="/head/left.jsp"/>
    <!--/sidebar-->
    <div class="main-wrap">

        <div class="crumb-wrap">
            <div class="crumb-list"><i class="icon-font"></i><a href="/">首页</a><span class="crumb-step">&gt;</span><span class="crumb-name">部门管理</span></div>
        </div>
        <div class="result-wrap" style="float: left;">
            <iframe id="userlist" name="userlist" src="/home/dept?op=deptList" style="border:0px;display:inline-block;height:500px;width:800px;"></iframe>
        </div>
    </div>
    <!--/main-->
</div>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
</body>
</html>