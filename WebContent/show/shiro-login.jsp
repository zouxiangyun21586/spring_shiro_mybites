<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
	a{ text-decoration:none;}
	/* css注释： 鼠标经过热点文字被加下划线 */ 
	a:hover{ text-decoration:underline;}
	/* a 标签字体颜色为 黑色 */
	a{color:#000}
</style>
</head>
<body bgcolor="#E0EEEE">
	<center>
		<font size="6" color="#FF3E96"> 欢迎来到&nbsp; 
			<font size="7" color="Pink">二厶</font>
		&nbsp; 账户登录页面</font>
		<br/>
		<br/>
		<br/>
		<c:if test="${!empty requestScope.err}"> <!-- 登录有误时进入 -->
			<font size="5" color="#FF3030">${err}</font>
			<form action="/Spring_Shiro/shiro_login" method="get">
				用户名:<input type="text" name="account">
				<p>
				密码:<input type="password" name="password">
				<p>
				<input type="submit" > &nbsp;&nbsp;&nbsp;&nbsp;
				<input type="reset" >
			</form>
		</c:if>
		<c:if test="${empty requestScope.err}">
			<form action="/Spring_Shiro/shiro_login" method="get">
				用户名:<input type="text" name="account">
				<p>
				密码:<input type="password" name="password">
				<p>
				<input type="submit" > &nbsp;&nbsp;&nbsp;&nbsp;
				<input type="reset" >
			</form>
		</c:if>
		<br/>
	
		<a href="<%=request.getContextPath() %>/show/shiro-add.jsp">注册用户</a>
	</center>
</body>
</html>