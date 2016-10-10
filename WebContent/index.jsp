<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.sziit.util.CommonUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%
	//Linux解决中文乱码
	request.setCharacterEncoding("UTF-8");
	//String path = request.getContextPath();
	String code = request.getParameter("code");
	String odname= CommonUtil.getOpenid(code);
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equlv="Access-Control-Allow-Origin" content="*">
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<title>绑定账号</title>
		<link rel="stylesheet" href="http://apps.bdimg.com/libs/jquerymobile/1.4.5/jquery.mobile-1.4.5.min.css">
		<script src="http://apps.bdimg.com/libs/jquery/1.10.2/jquery.min.js"></script>
		<script src="http://apps.bdimg.com/libs/jquerymobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
	</head>
	<body onload="init();">
		<div data-role="page" id="home">
			<div data-role="header">
				<h1>绑定界面</h1>
			</div>
			<div data-role="content">
				<div data-role="fieldcontain">
					<label for="uersname">学 号:</label>
					<input type="text" name="username" id="username" value="" />
				</div>
				<div data-role="fieldcontain">
					<label for="password">密 码:</label>
					<input type="password" name="password" id="password" value="" />
				</div>
				<input type="button" data-line="true" id="loginButton" value="登录" />
			</div> 
			<div data-role="footer" data-position="fixed">
				<h1>深圳信息职业学院</h1>
			</div>
		</div>
		<div data-role="page" id="success" data-theme="b">
			<div data-role="header">
				<h1>绑定界面</h1>
			</div>
			<div data-role="content">
				<p>恭喜您绑定成功！</p>
				<span>赶快去试试其他的功能吧^_^</span>
				<input type="button" data-line="true" id="cancelButton" value="解除绑定" onClick="deleteLogin();"/>
			</div>
			<div data-role="footer" data-position="fixed">
				<h1>深圳信息职业学院</h1>
			</div>
		</div>
		<div data-role="page" id="successed" data-theme="b">
			<div data-role="header">
				<h1>绑定界面</h1>
			</div>
			<div data-role="content">
				<p>您已绑定成功了！</p>
				<span>赶快去试试其他的功能吧^_^</span>
				<input type="button" data-line="true" id="cancelButton" value="解除绑定" onClick="deleteLogin();"/>
			</div>
			<div data-role="footer" data-position="fixed">
				<h1>深圳信息职业学院</h1>
			</div>
		</div>
		<div data-role="page" id="fail" data-theme="c">
			<div data-role="header">
				<h1>绑定界面</h1>
				<a href="#home" data-icon="arrow-l" class="ui-btn-left">返回</a>
			</div>
			<div data-role="content">
				<p>绑定失败！</p>
				<span>账号或密码错误，请重新尝试绑定#_#</span>
			</div>
			<div data-role="footer" data-position="fixed">
				<h1>深圳信息职业学院</h1>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		var odname = "<%=odname%>";
		$(function(){
			$("#loginButton").click(function(){
				var un = $("#username").val();
				var ps = $("#password").val();
				var params = "key=login&un="+un+"&ps="+ps;
				//验证账号正确性
				$.ajax({
					url: "http://sziit.ngrok.natapp.cn/sziitWeixinProject/ajax.do",
					data : params,
					type: "GET",
					success:function(xmlData){
						if(xmlData || xmlData == "true"){
							doInsert(un);
						}else{
							window.location.href = "#fail";
						}
					},
					error:function(event, XMLHttpRequest, ajaxOptions, thrownError){
						window.location.href = "#fail";
					}
				});
			});
		});
		//解除绑定
		function deleteLogin(){
			var params = "key=cancel&odname="+odname;
			//验证账号正确性
			$.ajax({
				url: "http://sziit.ngrok.natapp.cn/sziitWeixinProject/ajax.do",
				data : params,
				type: "GET",
				success:function(xmlData){
					if(xmlData == "true"){
						window.location.href = "#home";
					}else{
						window.location.href = "#successed";
					}
				},
				error:function(event, XMLHttpRequest, ajaxOptions, thrownError){
					window.location.href = "#fail";
				}
			});
		}
		//检查当前用户是否绑定过
		function init(){
			$.ajax({
				url: "http://sziit.ngrok.natapp.cn/sziitWeixinProject/ajax.do?od="+odname,
				type: "POST",
		        crossDomain: true,
				success:function(xmlData){
					if(xmlData == "false"){
						window.location.href="#successed";
					}
				},
				error:function(event, XMLHttpRequest, ajaxOptions, thrownError){
				}
			});
		}
		function doInsert(userName){
			var param = "userName="+userName+"&odname="+odname;
			$.ajax({
				url: "http://sziit.ngrok.natapp.cn/sziitWeixinProject/loginForOk.do",
				data : param,
				type: "GET",
				dataType: "html",
				success:function(xmlData){
					if(xmlData == "true"){
						window.location.href = "#success";
					}else{
						window.location.href = "#fail";
					}
				},
				error:function(event, XMLHttpRequest, ajaxOptions, thrownError){
					window.location.href = "#fail";
				}
			});
		}
	</script>
</html>