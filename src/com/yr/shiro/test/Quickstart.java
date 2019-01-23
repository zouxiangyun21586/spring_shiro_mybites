package com.yr.shiro.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * Shiro.ini 文件, 用来维系用户 — 角色 — 权限之间的关系.
 * 
 * ini 文件说明
 * [users]：用户名=密码，角色1，角色2
 * [roles]：角色=权限1，权限2
 * 
 * @author Administrator
 *
 */
public class Quickstart {

    private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);


    public static void main(String[] args) {

    	// 创建具有配置// 领域，用户，角色和权限的Shiro SecurityManager的最简单方法 是使用简单的INI配置。
    	// 我们将通过使用可以接收.ini文件的工厂来完成此操作，并
    	// 返回SecurityManager实例：
    	
    	// 在类路径的根目录中使用shiro.ini文件
    	// （文件：和url：分别从文件和url中加载前缀）： 
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini"); // 加载shiro.ini 文件
        SecurityManager securityManager = factory.getInstance();

     // 对于这个简单的快速入门示例，使SecurityManager
        // 可以作为JVM单例进行访问。大多数应用程序不会做这
        // ，而是依靠自己的容器配置或web.xml中的
        // web应用。这就是这个简单的快速入门的范围之内，所以
        // 我们只是做了最低限度，因此您可以继续来感受
        // 事情。
        SecurityUtils.setSecurityManager(securityManager);

     // 现在已经建立了一个简单的Shiro环境，让我们看看你能做什么：

        // 获取当前正在执行的用户： 
        Subject currentUser = SecurityUtils.getSubject();

     // 使用Session做一些事情（不需要Web或EJB容器!!!） 
        /*Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue"); // 键 值
        String value = (String) session.getAttribute("someKey"); // 根据键获取值
        if (value.equals("aValue")) {
            log.info("-->Retrieved the correct value! [" + value + "]");
        }*/

        // 让我们登录当前用户，以便我们可以检查角色和权限：
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken("root", "admin"); // 用户名 密码
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("-->没有用户使用用户名" + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.info("-->账户密码 " + token.getPrincipal() + " 不正确");
            } catch (LockedAccountException lae) {
                log.info("帐户的用户名 " + token.getPrincipal() + " 被锁定. 请与您的管理员联系解锁");
            }
            catch (AuthenticationException ae) { // ...在这里捕获更多的异常（也许定制的特定于你的应用程序？
            	ae.printStackTrace();
            }
        }

        // 说出他们是谁：
        // 打印他们的标识主体（在这种情况下是用户名）： 
        log.info("-->User [" + currentUser.getPrincipal() + "] logged in successfully."); // 用户名

        // 测试角色：
        if (currentUser.hasRole("schwartz")) { // 判断角色是否存在
            log.info("-->可能Schwartz和你在一起！");
        } else {
            log.info("你好, 请注册角色使用");
        }

        // 测试类型权限（不是实例级别）
        if (currentUser.isPermitted("lightsaber:weild")) {  // 判断角色是否有该权限
            log.info("-->你有 lightsaber 权限. 请正确的使用它");
        } else {
            log.info("对不起, 你没有 lightsaber 权限");
        }

        // 一个非常强大的实例级别权限：
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("-->你被允许用winnebago:drive:eagle5权限 ");
        } else {
            log.info("对不起，你不允许使用 winnebago:drive:eagle5权限 ！");
        }
        
        if (currentUser.isPermitted("nc:zxy")) {
            log.info("-->你有 zxy 权限！'");
        } else {
            log.info("sorry 无权限 ！");
        }

        // 全部完成 - 注销！
        currentUser.logout();
        
        System.exit(0);
    }
}