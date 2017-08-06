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
    <link rel="stylesheet" href="/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <style type="text/css">
        .ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
    </style>
</head>
<body>
<div style="width:220px;float:left;padding: 10px 20px;">
    <ul id="treeDemo" class="ztree"></ul>
</div>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript">
  var returnValue = "";
  var setting = {
    view: {
      selectedMulti: false
    },
    edit: {
      enable: false,
      editNameSelectAll: false
    },
    data: {
      simpleData: {
        enable: true
      }
    },
    callback: {
      onClick: zTreeOnClick
    }

  };

  var zNodes = ${json};

  function zTreeOnClick(event, treeId, treeNode) {
    returnValue = treeNode.name.substring(treeNode.name.indexOf("-")+1,treeNode.name.length);
  };

  function selectAll() {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
  }

  $(document).ready(function(){
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    $("#selectAll").bind("click", selectAll);
  });
</script>
</body>
</html>
