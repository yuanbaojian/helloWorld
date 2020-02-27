package com.ybj.auth.jwt;


import com.ybj.auth.constants.AuthConstants;
import com.ybj.auth.model.User;
import com.ybj.auth.model.Permission;
import com.ybj.auth.service.UserServiceI;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author yabo.wang
 * @Date 2020/1/8 13:42
 * @Description
 */
@Slf4j
public class BaseJwtRealm extends AuthorizingRealm {

    @Autowired
    HttpServletRequest request;

    @Autowired
    UserServiceI userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    /**
     * @param authToken AuthenticationToken
     * @Description shiro登录认证
     * @author wangyabo 
     * @date 2020/1/8 13:44
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) {
        //  1.获得前台传递的token
        String token = (String)authToken.getCredentials();
        String loginName = JwtUtil.getLoginName(token);
        String redisKey = AuthConstants.TOKEN_PREFIX + loginName;
        String redisToken = stringRedisTemplate.opsForValue().get(redisKey);
        if(redisToken == null){
            throw new UnknownAccountException();
        } else{
            stringRedisTemplate.expire(redisKey , AuthConstants.TOKEN_EXPIRED_TIME , TimeUnit.SECONDS);
        }
        log.info("验证成功");
        return new SimpleAuthenticationInfo(token,token,"my_realm");
    }

    /**
     * @param principals PrincipalCollection
     * @Description shiro授权
     * @author wangyabo
     * @date 2020/1/8 13:44
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User loginUser = (User) getAvailablePrincipal(principals);
        // 权限名的集合
        Set<String> permissions = new HashSet<>();
        loginUser = userService.checkUser(loginUser);
        List<Permission> lstPermission = userService.selectPermissionByUser(loginUser.getUserId());
        for (Permission p : lstPermission) {
            permissions.add(p.getPermissionCode());
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 返回权限集合
        simpleAuthorizationInfo.addStringPermissions(permissions);
        HttpSession session = request.getSession();
        session.setAttribute("permissions", permissions);
        return simpleAuthorizationInfo;
    }

    /**
     * @param token AuthenticationToken
     * @Description 重写Realm的 supports()方法是通过 JWT 进行登录判断的关键
     *  因为前文中创建了 BaseJwtToken 用于替换 Shiro 原生 token
     *  所以必须在此方法中显式的进行替换，否则在进行判断时会一直失败
     * @author wangyabo
     * @date 2020/1/8 13:49
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof BaseJwtToken;
    }

}
