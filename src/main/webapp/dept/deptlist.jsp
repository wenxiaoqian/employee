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
    <link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"  />
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
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
  var returnValue = "";
  var setting = {
    view: {
      addHoverDom: addHoverDom,
      removeHoverDom: removeHoverDom,
      selectedMulti: false
    },
    edit: {
      enable: true,
      editNameSelectAll: true
    },
    data: {
      simpleData: {
        enable: true
      }
    },
    callback: {
      beforeEditName: beforeEditName,
      beforeRemove: deleteDept,
      onClick: zTreeOnClick
    }

  };

  var zNodes = ${json};

  function zTreeOnClick(event, treeId, treeNode) {
    //returnValue = treeNode.name.substring(treeNode.name.indexOf("-")+1,treeNode.name.length);
  };

  function selectAll() {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
  }

  $(document).ready(function(){
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    $("#selectAll").bind("click", selectAll);
  });

  function addDept(parentId){
    asyncbox.open({id:'dealdiv',url:'/home/dept?op=add&pid='+parentId,title:'添加部门',modal:true,width : 550,height:270,btnsbar:jQuery.btn.OKCANCEL,
      callback : function(action,opener){
        if(action == 'ok'){
          var formfield = opener.jQuery("#dept_form").serialize();
          jQuery.ajax({type: "POST",url: "/home/dept?op=add",data:formfield ,
            success: function(msg){
              if(msg == "success"){
                window.location.reload();
              }else{
                alert(msg);
              }
            }
          });
        }
        return true;
      }
    });
  }

  function editDept(keyId){
    asyncbox.open({id:'dealdiv',url:'/home/dept?op=update&keyId='+keyId,title:'修改部门',modal:true,width : 550,height:270,btnsbar:jQuery.btn.OKCANCEL,
      callback : function(action,opener){
        if(action == 'ok'){
          var formfield = opener.jQuery("#dept_form").serialize();
          jQuery.ajax({type: "POST",url: "/home/dept?op=update",data:formfield ,
            success: function(msg){
              if(msg == "success"){
                window.location.reload();
              }else{
                alert(msg);
              }
            }
          });
        }
        return true;
      }
    });
  }

  function deleteDept(treeId, treeNode){
    if(treeNode.id == 1){
      alert("一级目录不可以删除");
      return false;
    }
    if(!confirm("你确定要删除吗？")){
      return false;
    }
    jQuery.ajax({type: "POST",url: "/home/dept?op=delete&keyId="+treeNode.id ,
      success: function(msg){
        if(msg == "success"){
          window.location.reload();
        }else{
          alert(msg);
        }
      }
    });
  }

  function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");
    if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
    var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
        + "' title='add node' onfocus='this.blur();'></span>";
    sObj.after(addStr);
    var btn = $("#addBtn_"+treeNode.tId);
    if (btn) btn.bind("click", function(){
      addDept(treeNode.id);
    });
  };

  function removeHoverDom(treeId, treeNode) {
    $("#addBtn_"+treeNode.tId).unbind().remove();
  };

  function beforeEditName(treeId, treeNode) {
    editDept(treeNode.id);
    return false;
  }



</script>
</body>
</html>
