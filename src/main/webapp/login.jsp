<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><head>
    <title>出彩·机场人</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="js/jquery.js"></script>
    <link rel="stylesheet" type="text/css" href="css/login.css"/>
    <style type="text/css">
        body{background: #fff url(../images/1.jpg) 50% 0 no-repeat;}
    </style>
</head>
<body>
<h1>管理后台</h1>

<div class="login" style="margin-top:50px;">

    <div class="web_qr_login" id="web_qr_login" style="display: block; height: 265px;">
        <!--登录-->
        <div class="web_login" id="web_login">
            <div class="login-box">
                <div class="login_form">
                    <form action="" name="loginform" accept-charset="utf-8" id="login_form" class="loginForm" method="post"><input type="hidden" name="did" value="0"/>
                        <input type="hidden" name="to" value="log"/>
                        <div class="uinArea" id="uinArea">
                            <label class="input-tips" for="username">帐号：</label>
                            <div class="inputOuter" id="uArea">

                                <input type="text" id="username" name="username" class="inputstyle"/>
                            </div>
                        </div>
                        <div class="pwdArea" id="pwdArea">
                            <label class="input-tips" for="password">密码：</label>
                            <div class="inputOuter" id="pArea">
                                <input type="password" id="password" name="password" class="inputstyle"/>
                            </div>
                        </div>
                        <div style="padding-left:50px;margin-top:20px;"><input type="button" id="loginBtn" value="登 录" style="width:150px;" class="button_blue"/></div>
                    </form>
                </div>

            </div>

        </div>
        <!--登录end-->
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $("#loginBtn").click(function() {
            var username = $("#username").val();
            var password = $("#password").val();

            if (username == "" || password == "") {
                alert("帐号或密码不能为空！");
                return;
            }

            jQuery.ajax({type: "POST",url: "/login",data:$("#login_form").serialize() ,
                success: function(msg){
                    if(msg == "success"){
                        window.location.href = "/home/question";
                    }else{
                        alert(msg);
                    }
                }
            });
        });
    });
</script>
<div class="jianyi">*推荐使用ie8或以上版本ie浏览器或Chrome内核浏览器访问本站</div>
</body></html>