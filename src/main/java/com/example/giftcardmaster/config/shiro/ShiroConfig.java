package com.example.giftcardmaster.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /**
     * 配置 ShiroDialect（Shiro 方言） 对象
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager")
                                                                     DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //设置未认证(登录)时，访问需要认证的资源时跳转的页面
        shiroFilterFactoryBean.setLoginUrl("/login");

        //设置认证成功时跳转的页面
        shiroFilterFactoryBean.setSuccessUrl("/home");

        //设置访问无权限的资源时跳转的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        //指定路径和过滤器的对应关系
        Map<String, String> filterMap = new HashMap<>();

        //设置/login不需要登录就能访问
        filterMap.put("/home", "anon");
        filterMap.put("/home/search", "anon");
        filterMap.put("/login", "anon");
        filterMap.put("/register", "anon");
        filterMap.put("/404", "anon");
        filterMap.put("/error", "anon");

        filterMap.put("/css/**", "anon");
        filterMap.put("/images/**", "anon");
        filterMap.put("/fonts/**", "anon");
        filterMap.put("/js/**", "anon");

//        //设置/user/list需要登录用户拥有角色user时才能访问
//        filterMap.put("/user/list", "roles[user]");

        //其他路径则需要登录才能访问
        filterMap.put("/**", "user");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("realm") Realm realm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
        return defaultWebSecurityManager;
    }

    @Bean
    public Realm realm() {
        MyRealm realm = new MyRealm();
        //使用HashedCredentialsMatcher带加密的匹配器来替换原先明文密码匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //指定加密算法
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //指定加密次数
        hashedCredentialsMatcher.setHashIterations(3);
        realm.setCredentialsMatcher(hashedCredentialsMatcher);
        return realm;
    }

    /**
     * cookie对象
     */
    private SimpleCookie rememberMeCookie() {
        // 设置cookie名称，对应login.html页面的<input type="checkbox" name="rememberMe"/>
        SimpleCookie cookie = new SimpleCookie("GiftCardMasterRememberMe");
        // 设置cookie的过期时间，单位为秒，这里为一天
        cookie.setMaxAge(86400);
        return cookie;
    }

    /**
     * cookie管理对象
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     */
    private CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie加密的密钥 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("c2FuX2pvc2VfZnJlbW9udF9MQQ=="));
        return cookieRememberMeManager;
    }
}
