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
    <script type="text/javascript">
        function addUser(deptId){

            parent.asyncbox.open({id:'dealdiv',url:'/home/user?op=add&deptId='+deptId,title:'添加用户',modal:true,width : 550,height:370,btnsbar:parent.jQuery.btn.OKCANCEL,
                callback : function(action,opener){
                    if(action == 'ok'){
                        var formfield = opener.jQuery("#user_form").serialize();
                        jQuery.ajax({type: "POST",url: "/home/user?op=add",data:formfield ,
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

        function editUser(userId){
            parent.asyncbox.open({id:'dealdiv',url:'/home/user?op=update&userId='+userId,title:'修改用户',modal:true,width : 550,height:370,btnsbar:parent.jQuery.btn.OKCANCEL,
                callback : function(action,opener){
                    if(action == 'ok'){
                        var formfield = opener.jQuery("#user_form").serialize();
                        alert(formfield);
                        jQuery.ajax({type: "POST",url: "/home/user?op=update",data:formfield ,
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

        function deleteUser(userId){
            if(!confirm("你确定要删除吗？")){
                return false;
            }
            jQuery.ajax({type: "POST",url: "/home/user?op=delete&userId="+userId ,
                success: function(msg){
                    if(msg == "success"){
                        window.location.reload();
                    }else{
                        alert(msg);
                    }
                }
            });
        }

    </script>
</head>
<body>
<form name="myform" id="myform" method="post">
    <input type="hidden" id="deptId" name="deptId" value="${deptId}"/>
    <div class="result-title">
        <div class="result-list">
            <a href="javascript:addUser('${deptId}');"><i class="icon-font"></i>新增用户</a>
        </div>
    </div>
    <div class="result-content">
        <table class="result-tab" width="800">
            <tr>
                <th width="60">用户账号</th>
                <th>用户权限</th>
                <th>操作</th>
            </tr>
            <c:forEach var="user" items="${userList}">
                <tr>
                    <td>${user.account}</td>
                    <td>
                        <c:if test="${fn:indexOf(user.roleId,'0001') > -1}">
                        滩访市供电局，
                        </c:if>
                        <c:if test="${fn:indexOf(user.roleId,'0002') > -1}">
                            国网安丘市供电公司，
                        </c:if>
                        <c:if test="${fn:indexOf(user.roleId,'0003') > -1}">
                            昌乐供电公司，
                        </c:if>
                        <c:if test="${fn:indexOf(user.roleId,'0004') > -1}">
                            昌邑供电公司，
                        </c:if>
                        <c:if test="${fn:indexOf(user.roleId,'0005') > -1}">
                            高密市供电公司，
                        </c:if>
                        <c:if test="${fn:indexOf(user.roleId,'0006') > -1}">
                            寒亭区供电公司，
                        </c:if>
                        <c:if test="${fn:indexOf(user.roleId,'0007') > -1}">
                            国网临朐县供电公司，
                        </c:if>
                        <c:if test="${fn:indexOf(user.roleId,'0008') > -1}">
                            青州市供电公司，
                        </c:if>
                        <c:if test="${fn:indexOf(user.roleId,'0009') > -1}">
                            国网寿光市供电公司，
                        </c:if>
                        <c:if test="${fn:indexOf(user.roleId,'0010') > -1}">
                            国网诸城市供电公司，
                        </c:if>
                    </td>
                    <td>
                        <a class="link-update" href="javascript:editUser('${user.id}');">修改</a>
                        <a class="link-del" href="javascript:deleteUser('${user.id}');">删除</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</form>
</body>
</html>