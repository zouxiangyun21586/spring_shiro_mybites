package com.yr.shiro.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.yr.shiro.entity.AccountEntity;
import com.yr.shiro.entity.PowerEntity;
import com.yr.shiro.entity.RoleEntity;
import com.yr.shiro.service.AccountService;

/**
 * @RequiresAuthentication ：要求当前Subject 已经在当前的session 中被验证通过才能被注解的类/实例/方法访问或调用
 * @RequiresGuest ：要求当前的Subject 是一个“guest”，也就是他们必须是在之前的session中没有被验证或记住才能被注解的类/实例/方法访问或调用
 * @RequiresPermissions：要求当前的Subject 被允许一个或多个权限，以便执行注解的方法，比如：@RequiresPermissions("account:create") // account用户拥有create权限
 * @RequiresRoles：要求当前的Subject 拥有所有指定的角色。如果他们没有，则该方法将不会被执行，而且AuthorizationException 异常将会被抛出。比如：@RequiresRoles("admin")
 * @RequiresUser：需要当前的Subject 是一个应用程序用户才能被注解的类/实例/方法访问或调用。要么是通过验证被确认，或者在之前session 中的'RememberMe'服务被记住
 * 
 * @author Administrator
 *
 */
public class AccountRealm extends AuthorizingRealm {

	// 设置realm的名称
	@Override
	public void setName(String name) {
		super.setName("accountRealm");
	}
	
	@Autowired
	private AccountService<AccountEntity> accountService;
	
	/**
	 * 授权方法: 
	 * 1. 实际返回的是 SimpleAuthorizationInfo 类的实例 
	 * 2. 可以调用SimpleAuthorizationInfo 的 addRole 来添加当前登录 user 的权限信息. 
	 * 3. 可以调用PrincipalCollection 参数的 getPrimaryPrincipal() 方法来获取用户信息
	 * 
	 * 授权顺序:
	 * 1)subject	2)会话管理	3)认证	4)pluggable realm
	 * 
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		
		Connection conn = ConnectionUtil.getConn();
		
		try {
			// 查到权限数据，返回授权信息(要包括 上边的permissions) 
			SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
			
			if (!SecurityUtils.getSubject().isAuthenticated()) { // 判断是否认证过(此处是没有认证过)
				doClearCache(principalCollection);  
				SecurityUtils.getSubject().logout();  
				return null;  
			}
			
			String accountName = (String) principalCollection.getPrimaryPrincipal();  
			AccountEntity account = accountService.findAccountName(accountName); // 获取AccountEntity 实体中的内容
			String sql = "select rol_id from acc_rol where acc_id = ?";
			PreparedStatement pre=(PreparedStatement) conn.prepareStatement(sql);
			pre.setInt(1, account.getId());
			ResultSet rs = pre.executeQuery();
			List<RoleEntity> listRole = new ArrayList<RoleEntity>(); // 用于装从中间表中查询出的角色id
			while (rs.next()) {
				RoleEntity re = new RoleEntity(); // 将获取到的角色id存好
				re.setId(rs.getInt(1));
				listRole.add(re); // 将获取到的角色id存入list
			}
			
			List<RoleEntity> liRo = new ArrayList<RoleEntity>(); // 用于装角色表中查询出的角色名字
			for (RoleEntity roleEntity : listRole) {
				String sql3 = "select * from roleEntity where id = ?";
				PreparedStatement prep = (PreparedStatement) conn.prepareStatement(sql3);
				prep.setInt(1, roleEntity.getId());
				ResultSet resu = prep.executeQuery();
				while (resu.next()) {
					RoleEntity roleEt = new RoleEntity();
					roleEt.setId(resu.getInt(1));
					roleEt.setName(resu.getString(2));
					liRo.add(roleEt);
				}
				
			}
			
			List<PowerEntity> liPower = new ArrayList<PowerEntity>(); // 装中间表的权限id
			for (RoleEntity roleEntity : liRo) {
				authorizationInfo.addRole(roleEntity.getName()); // 将数据中查询出来的角色加入
				
				String sql1 = "select power_id from role_power where role_id = ?";
				PreparedStatement pr = (PreparedStatement) conn.prepareStatement(sql1);
				pr.setInt(1, roleEntity.getId());
				ResultSet res = pr.executeQuery();
				List<PowerEntity> listPower = new ArrayList<PowerEntity>(); // 装权限表
				while(res.next()){
					PowerEntity po = new PowerEntity();
					po.setId(res.getInt(1));
					listPower.add(po);
				}
				for (PowerEntity powerEntity : listPower) {
					PowerEntity powere = new PowerEntity();
					String sql2 = "select * from powerentity where id = ?";
					PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql2);
					ps.setInt(1, powerEntity.getId());
					ResultSet rss = ps.executeQuery();
					while(rss.next()){
						powere.setId(rss.getInt(1));
						powere.setName(rss.getString(2));
						liPower.add(powere);
					}
				}
			}
			
			for (PowerEntity powerEntity : liPower) {
				authorizationInfo.addStringPermission(powerEntity.getName());
			}
			
			return authorizationInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 认证方法
	 * 1. 编写表单: 表单的 action、和 username、password 的参数都是什么 ? 
	 * 回答: 提交到你想提交的地方, username 和 password 也参数名称都任意. 
	 * 2. 例如, 提交到了一个 SpringMVC 的 handler: 
	 * 1). 获取用户名、密码
	 * 2). 
	 * Subject currentUser = SecurityUtils.getSubject();
	 * UsernamePasswordToken token = new UsernamePasswordToken(username, password);
	 * currentUser.login(token);
	 * 3. 当 Subject 调用 login 方法时, 即会触发当前的 doGetAuthenticationInfo 方法. 且把 
	 * UsernamePasswordToken 对象传入, 然后再该方法中执行真正的认证: 访问数据库进行比对. 
	 * 1). 获取用户名和密码
	 * 
	 * 第一种方法没有使用加盐加密方法登录
	 * 第二中方法使用加盐加密方法登录(需使用加盐加密方法登录时,在applicationContext-shiro.xml 文件中将初始化开启 加盐加密 )
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		// 1、把AuthenticationToken转换为UsernamePasswordToken
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        // 2、从UsernamePasswordToken中获取username
        String account = usernamePasswordToken.getUsername();
        // 3、调用数据库的方法，从数据库中查询username对应的用户记录
        String accEntity = "";
		String accPassword = "";
		List<AccountEntity> listAcc = accountService.queryAccount();
		for (AccountEntity accountEntity : listAcc) {
			accEntity = accountEntity.getAccount();
			if(account.equals(accEntity)){
				accEntity = account;
				accPassword = accountEntity.getPassword();
				break;
			}else{
				accEntity = null;
			}
		} 
		if(accEntity == null || accEntity.equals("")){ // 用户不存在
			throw new UnknownAccountException("没有找到该账号");
		}
        
		// 6、根据用户的情况，来构建AuthenticationInfo对象返回，通常使用的实现类为：SimpleAuthenticationInfo
        // 以下信息是从数据库中获取的
        // 参数 ： principal ---> 认证的实体信息，可以是username，也可以是数据表对应的用户实体类对象
        Object principal = accEntity;
        // 参数 ：credentials ---> 密码
        Object credentials = accPassword;
        // 参数 ： realmName ---> 当前realm对象的name。调用父类的getName()方法即可
        String realmName = this.getName();
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, realmName);
        
        
        
//		String account = (String) token.getPrincipal(); // 从 token 中获取登录的 username! 注意不需要获取 password.
//		String accEntity = "";
//		String accPassword = "";
//		List<AccountEntity> listAcc = accountService.queryAccount();
//		for (AccountEntity accountEntity : listAcc) {
//			accEntity = accountEntity.getAccount();
//			if(account.equals(accEntity)){
//				accEntity = account;
//				accPassword = accountEntity.getPassword();
//				break;
//			}else{
//				accEntity = null;
//			}
//		} 
//		if(accEntity == null || accEntity.equals("")){ // 用户不存在
//			throw new UnknownAccountException("没有找到该账号");
//		}
//		
//		/** 
//		 * 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现   
//		 */  
//		String realmName = getName();
//		Object objPassword = password(accPassword);
//		String yz = "yzjmzxy"; // 设置盐值
//		ByteSource credentialsSalt = new Md5Hash(yz);
//		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(account, objPassword, credentialsSalt, realmName);
		
		return info;
	}

	// @PostConstruct: 相当于 bean 节点的 init-method 配置.
	public void setCredentialMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();

		credentialsMatcher.setHashAlgorithmName("MD5");
		credentialsMatcher.setHashIterations(1024);

		setCredentialsMatcher(credentialsMatcher);
	}

	public static Object password(String password){ // 将数据库中查询出来的密码用盐值加密
		String saltSource = "yzjmzxy"; // 设置盐值
		String hashAlgorithmName = "MD5";
		String credentials = password; // 密码
		Object salt = new Md5Hash(saltSource);
		int hashIterations = 1024;

		Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
		return result;
	}
	
	/**
     * 清空当前用户权限信息
     */
	public  void clearCachedAuthorizationInfo() {
		PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principalCollection, getName());
		super.clearCachedAuthorizationInfo(principals);
	}
	/**
	 * 指定principalCollection 清除
	 */
	public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principalCollection, getName());
		super.clearCachedAuthorizationInfo(principals);
	}

}
