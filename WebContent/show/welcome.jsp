<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
<body bgcolor="#B0E0E6">
	<center>
		<br/>
		<shiro:notAuthenticated>
			<font color="yellow" size="6">登录失败</font>
			<br/>
			<font color="red" size="4">登录失败原因</font>: 未进行身份验证,直接访问 (包括"记住我")
			<br/>
		</shiro:notAuthenticated>
		<shiro:authenticated>
			<font color="#8968CD" size="6">登录成功</font>
			<br/>
			<br/>
			<font size="2">用户  [<font color="blue" size="5"><shiro:principal/></font>] 已身份验证通过</font>
			<br/>
			<font size="6"><a href="<%=request.getContextPath() %>/show/shiro-show.jsp">进入账户界面</a></font>
			<br/>
			<br/>
		</shiro:authenticated>
		<shiro:hasRole name="user">
			用户 [<font color="blue" size="5"><shiro:principal/></font>] 拥有  user 角色
			<br/>
		</shiro:hasRole>
		<shiro:lacksRole name = "admin">
			用户 [<font color="blue" size="5"><shiro:principal/></font>] 没有 admin 角色
			<br/>
		</shiro:lacksRole>
		<shiro:hasAnyRoles name = "admin,userRoot">
			用户 [<font color="blue" size="5"><shiro:principal/></font>] 拥有admin 或者 userRoot 角色
			<br/>
			<br/>
		</shiro:hasAnyRoles>
		<shiro:guest>
			<br/>
			<a href = "Spring_shiro/show/shiro-login.jsp">游客访问 </a>
		</shiro:guest>
	</center>
</body>
</html>