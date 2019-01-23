<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/jquery-1.9.1.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示页面</title>
<style>

	#userShow{  
		display:none;  
		border:1em solid pink;
		height:50%;  
		width:24%;  
		position:absolute;/*让节点脱离文档流,我的理解就是,从页面上浮出来,不再按照文档其它内容布局*/  
		top:20%;/*节点脱离了文档流,如果设置位置需要用top和left,right,bottom定位*/  
		left:35%;  
		z-index:2;/*个人理解为层级关系,由于这个节点要在顶部显示,所以这个值比其余节点的都大*/  
		background: white;  
	} 
	
	#userOver{  
		width: 100%;  
		height: 100%;  
		opacity:0.3;/*设置背景色透明度,1为完全不透明,IE需要使用filter:alpha(opacity=80);*/  
		filter:alpha(opacity=80);  
		display: none;  
		position:absolute;  
		top:0;  
		left:0;  
		z-index:1;  
		background: silver;  
	}
	
	a{ text-decoration:none;}
	/* css注释： 鼠标经过热点文字被加下划线 */ 
	a:hover{ text-decoration:underline;}
	/* a 标签字体颜色为 黑色 */
	a{color:#000}
</style>
</head>
<body bgcolor="#E0EEEE">

	<p align="center">
		<font size="7" color="Pink">蘑菇</font>&nbsp;&nbsp;&nbsp;<font size="5" color="#FFC1C1">账户显示页面</font>
	</p>
	<br/>

	<p align="center">
		<shiro:user>
			<!-- principal 标签: 显示用户身份信息,默认调用 -->
			欢迎 <font color="#8968CD" size="5">[<shiro:principal/>]</font> 用户登录 ☆ω☆   &nbsp;&nbsp;&nbsp;&nbsp;<a href = "<%=request.getContextPath() %>/show/shiro-login.jsp">退出登录</a>
		</shiro:user>
	</p>
	<p align="center">
		<shiro:hasPermission name="del">
			用户 <font color="#8968CD" size="5">[<shiro:principal/>]</font> 拥有  del 权限
		<br/>
		</shiro:hasPermission>
		<shiro:hasPermission name="upd">
			用户 <font color="#8968CD" size="5">[<shiro:principal/>]</font> 拥有  upd 权限
		<br/>
		</shiro:hasPermission>
		<shiro:lacksPermission name = "upd">
			用户[<shiro:principal/>] 没有 upd 权限
		</shiro:lacksPermission>
	</p>
	
	<div align="center">
		<table border="1" bgcolor="#D1EEEE">
			<tr>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
				<th>&nbsp;账号&nbsp;</th>
				<th>&nbsp;密码&nbsp;</th>
				<shiro:hasAnyRoles name="root,userRoot">
					<th>&nbsp;操作&nbsp;</th>
				</shiro:hasAnyRoles>
			</tr>
			<tbody id="lo"></tbody>
		</table>
	</div>
	
	<form action="" method="POST" id="userDel">
	</form>
	
	<div id="userShow">
		<div align="right">
			<button>
				<a href="javascript:user_hide()">&nbsp;X&nbsp;</a>
			</button>
		</div> 
		<div align="center">
			<form action="<%=request.getContextPath() %>/updAccount" method="post">
				<p align="left">修改用户</p>
				<input type="hidden" name="id" id="uid" />
				账 号: <input type="hidden" name="account" id="uacc" /><br/>
				密&nbsp;&nbsp;&nbsp;码:<input name="password" type="text" id="upass"/><br/>
				<input name="sub" type="submit" id="sub"/>
			</form>
		</div>
	</div>
	
</body>
<script type="text/javascript">

	function aa()
	{
		document.getElementById("formId").submit();
	}

	function init(){
	
		$.ajax({
			type: "get",  // 请求方式(post或get)
			async:false,  //默认true(异步请求),设置为false(同步请求)
			url:"<%=request.getContextPath() %>/selAccount", // 发送请求的地址
			scriptCharset: 'utf-8',
			dataType:"json",
			success:function(a){
				$("#lo").empty();
				
				var user = eval("("+a+")"); // 将传过来的值转为json格式
				var abc = "";
				var list = user;
				
				for(var i in list){
				 	abc +=  "<tr><td>"+list[i].id+"</td>"+
				 			"<td>"+list[i].account+"</td>"+
				 			"<td>"+list[i].password+"</td>"+
				 			"<shiro:hasAnyRoles name='root,userRoot'><td><shiro:hasPermission name='upd'><a href='javascript:user_show("+list[i].id+")' >修改用户</a></shiro:hasPermission> &nbsp;"+
					 		"<shiro:hasPermission name='del'><a href='javascript:void(0)' class='del' id='<%=request.getContextPath() %>/delAccount/"+list[i].id+"'>删除用户</a></shiro:hasPermission> &nbsp;"+
					 		"</td></shiro:hasAnyRoles></tr>";
				}
				$("#lo").append(abc);
				loader();
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert(XMLHttpRequest.status);
				alert(XMLHttpRequest.readyState);
				alert(textStatus);
	        }
		});
	}
		
	/* 页面加载完出现 */
	$(document).ready(function(){
		init();
		loader();
		
	});

	
	function loader(){
		$(".del").click(function() {
			if(confirm("确认删除吗?")){
				var dlt = $(this).attr("id"); // attr() 方法设置或返回被选元素的属性值
				$("#userDel").attr("action", dlt).submit();
				return false;
			}
		});
	}
	
	var us = document.getElementById('userShow');
	var uo = document.getElementById('userOver');
    function user_show(id)
    {  
    	$.ajax({
			type: "get",  // 请求方式(post或get)
			async:false,  //默认true(异步请求),设置为false(同步请求)
			url:"<%=request.getContextPath() %>/get", // 发送请求的地址
			scriptCharset: 'utf-8',
			dataType:"json",
			data:{"id":id},
			success:function(aa){
				var ujson = eval("("+aa+")"); // 将传过来的值转为json格式

				
				$("#uid").val(ujson.id);
				$("#uacc").val(ujson.account); // val(aa) 赋值
				$("#upass").val(ujson.password); // 赋值
			}
		});
        us.style.display = "block";
        uo.style.display = "block";
    }  
    function user_hide()
    {  
        us.style.display = "none";
        uo.style.display = "none";
    }
    
</script>
</html>