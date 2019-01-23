package com.yr.shiro.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yr.shiro.entity.AccountEntity;
import com.yr.shiro.service.AccountService;
import com.yr.shiro.util.JsonUtils;


/**
 * 账户登录 controller
 * 
 * @author Administrator
 *
 */
@Controller
public class AccountController {
	
	@Autowired
	private AccountService<AccountEntity> resourceService;

	/**
	 * 添加
	 * 
	 * @author zxy
	 * @param user
	 * @return 2018年4月11日 下午10:16:12
	 *
	 */
//	@RequiresGuest // 游客身份可以进入
	@Transactional
	@RequestMapping(value = "/addAccount", method = RequestMethod.POST)
	public String add(AccountEntity resource, ModelMap map) {
		Boolean boo = resourceService.addAccount(resource);
		if (boo) {
			map.put("addSu", 2);
			return "shiro-login";
		} else {
			map.put("addSu", 1);
			return "shiro-add";
		}
	}

	/**
	 * 删除
	 * 
	 * @author zxy
	 * @param user
	 * @return 2018年4月11日 下午10:16:07
	 *
	 */
//	@RequiresPermissions(value = {"del"})
	@Transactional
	@RequestMapping(value = "/delAccount/{id}", method = RequestMethod.POST)
	public String del(AccountEntity resource, ModelMap map) {
		Boolean bool = resourceService.delAccount(resource.getId());
		if (bool) {
			map.put("dlt", 2);
			return "shiro-show";
		} else {
			map.put("dlt", 1);
			return "shiro-show";
		}
	}

	/**
	 * 修改
	 * 
	 * @author zxy
	 * @param id
	 * @param modelMap
	 * @return 2018年3月12日 下午7:47:00
	 *
	 */
//	@RequiresPermissions(value = {"upd"}) // 必须有 upd 权限才能进入
	@Transactional
	@RequestMapping(value = "/updAccount", method = RequestMethod.POST)
	public String upd(AccountEntity resource, ModelMap map) {
		Boolean bool = resourceService.updAccount(resource);
		if (bool) {
			map.put("upd", 2);
			return "shiro-show";
		} else {
			map.put("upd", 1);
			return "shiro-show";
		}
	}

//	@RequiresAuthentication // 已通过 login 进行身份验证
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody String get(Integer id, ModelMap map) {
		AccountEntity listUser = resourceService.getAccount(id);
		String str = JsonUtils.beanToJson(listUser);
		return str;
	}

	/**
	 * 查询
	 * 
	 * @RequiresRoles(value = {"userRoot"}) // 必须有 userRoot 角色才能进入,否则抛出异常 页面500错误
	 * @RequiresUser // 表示当前Subject 已经身份验证通过或者通过(记住我)登录的 , 否则停留在登录页面
	 * @RequiresGuest // 表示当前Subject没有身份验证或者通过记住我登录过,即是游客身份
	 * @RequiresRoles(value = {"userRoot","user"},logical = Logical.AND) // 表示当前Subject需要有userRoot 和 user 两个角色才能进入,否抛异常
	 * @RequiresAuthentication  // 表示当前Subject已经通过login进行身份验证;即 Subjec.isAuthenticated()返回 true, 如果没验证则停留在登录页面
	 * @RequiresPermissions(value = {"upd","del"},logical = Logical.OR) // 表示当前Subject需要有 sel 或者 del 权限才能进入
	 * 
	 * @author zxy
	 * @return 2018年3月12日 下午7:51:37
	 *
	 */
	
//	@RequiresUser // 进行过身份验证并且通过的才能进入或者保留了cookie值(记住我)的
	@RequestMapping(value = "/selAccount", method = RequestMethod.GET)
	public @ResponseBody String sel(HttpServletResponse response, HttpServletRequest request) {
		List<AccountEntity> listUser = resourceService.queryAccount();
		String aa = "";
		try {
			// false表示数组中的属性不需要转成json,如果是true代表只将数组中的属性转成json格式
			aa = JsonUtils.beanListToJson(listUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aa;
	}
	
}