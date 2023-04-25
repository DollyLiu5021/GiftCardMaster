package com.example.giftcardmaster.config.shiro;

import com.example.giftcardmaster.data.dbentity.UserEntity;
import com.example.giftcardmaster.data.dbentity.UserRoleEntity;
import com.example.giftcardmaster.service.UserRoleService;
import com.example.giftcardmaster.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyRealm extends AuthorizingRealm {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object username = principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(getRoles(username.toString()));
        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        String username = token.getUsername();
        Map<String, Object> userInfo = getUserInfo(username);
        if (userInfo == null) {
            throw new UnknownAccountException();
        }

        //盐值，此处使用用户名作为盐
        ByteSource salt = ByteSource.Util.bytes(username);

        return new SimpleAuthenticationInfo(username, userInfo.get("password"), salt, getName());
    }

    /**
     * 数据库查询，通过用户名获取用户信息
     */
    private Map<String, Object> getUserInfo(String username) {
        Map<String, Object> userInfo = null;
        UserEntity user = userService.getByUsername(username);
        if (user != null) {
            userInfo = new HashMap<>();
            userInfo.put("username", user.getUsername());

            //加密算法，原密码，盐值，加密次数
            userInfo.put("password", new SimpleHash("MD5", user.getPassword(), username, 3));
        }
        return userInfo;
    }

    /**
     * 查询数据库，获取用户角色 (可以多个)
     */
    private Set<String> getRoles(String username) {
        Set<String> roles = new HashSet<>();
        UserEntity user = userService.getByUsername(username);
        UserRoleEntity userRole = userRoleService.getById(user.getUserRoleId());
        roles.add(userRole.getName());
        return roles;
    }
}

