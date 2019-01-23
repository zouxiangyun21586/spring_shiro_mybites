package com.yr.shiro.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 登录 controller
 * 
 * @author Administrator
 *
 */
@Controller
public class LoginHandly {

	@RequestMapping(value = "/shiro_login", method = RequestMethod.GET)
	public String selectAllReuse(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String account = request.getParameter("account"); // 获取页面输入的参数,用来进行查询是否有权限操作
		String password = request.getParameter("password");
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken(account, password); // 判断用户名与密码是否正确
		try {
			subject.login(token);
//			subject.isPermitted("sel");// 判断是否有权限 会进入 AuthorizationInfo 授权方法
		} catch (AuthenticationException ae) {
			map.put("err", "信息有误,登录失败");
			return "/shiro-login";
		}
		return "/welcome";
	}
}
