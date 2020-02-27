package com.ybj.auth.shiro;


import com.ybj.auth.jwt.BaseJwtFilter;
import com.ybj.auth.jwt.BaseJwtRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;

/**
 * @Author yabo.wang
 * @Date 2020/1/9 10:14
 * @Description shiro的基本配置
 */
@Configuration
public class ShiroConfig {


    /***
     * @param securityManager SecurityManager
     * @Description 配置过滤链，shiro请求过滤处理
     * @author wangyabo 
     * @date 2020/1/9 10:22
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置 securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 配置自定义过滤器链JWTFilter
        LinkedHashMap<String, Filter> filters = new LinkedHashMap<>();
        filters.put("jwt", new BaseJwtFilter());
        shiroFilterFactoryBean.setFilters(filters);
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 所有请求都要经过 jwt过滤器
        filterChainDefinitionMap.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /***
     * @Description shiro安全管理器
     * @author wangyabo
     * @date 2020/1/9 10:23
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 配置 SecurityManager，并注入 shiroRealm
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }
    /***
     * @Description 自定义realm
     * @author wangyabo 
     * @date 2020/1/9 10:27
     */
    @Bean
    public Realm shiroRealm(){
        // 配置 Realm
        return new BaseJwtRealm();
    }

    /***
     * @param securityManager SecurityManager
     * @Description 开启shiro注解
     * @author wangyabo 
     * @date 2020/1/9 10:27
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }



}
