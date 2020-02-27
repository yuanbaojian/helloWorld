package com.ybj.auth.jwt;

import com.alibaba.fastjson.JSON;
import com.ybj.auth.constants.AuthConstants;
import com.ybj.utils.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author yabo.wang
 * @Date 2020/1/8 13:12
 * @Description
 * 所有的请求都会先经过 Filter，所以我们继承官方的 BasicHttpAuthenticationFilter ，并且重写鉴权的方法。
 * 代码的执行流程 preHandle -> isAccessAllowed -> isLoginAttempt -> executeLogin
 */
@Slf4j
public class BaseJwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * 请求头授权标识AUTHORIZATION
     */
    private static String authorization = "Authorization";

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * @param request ServletRequest
     * @param response ServletResponse
     * @Description 对于跨域请求的支持
     * @author wangyabo
     * @date 2020/1/9 10:48
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
//        //得到请求的URI
//        String requestURI = httpServletRequest.getRequestURI();
//        //如果请求的是无需认证的URL直接放行,本系统中有auth/login
//        for (String url : AuthConstants.ANON_URL){
//            if(antPathMatcher.match(url,requestURI)){
//                return true;
//            }
//        }
//        //否则进行是否尝试登录判断,即请求头中是否包含有授权字段
//        if (isLoginAttempt(request, response)) {
//            try {
//                log.info("执行验证方法");
//                return executeLogin(request, response);
//            } catch (Exception e) {
//                log.info("登录权限不足！");
//            }
//        }
//        return false;
        return true;
    }

    /**
     * @param request ServletRequest
     * @param response ServletResponse
     * @Description 判断用户访问的资源是否需要登录才能获取
     * 即前端请求头中是否带有Authorization授权字段
     * @author wangyabo
     * @date 2020/1/9 10:41
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorizationHeader = req.getHeader(BaseJwtFilter.authorization);
        return authorizationHeader != null;
    }

    /**
     * @param request ServletRequest
     * @param response ServletResponse
     * @Description getSubject(request, response).login(token) 就是触发 Shiro Realm 自身的登录控制
     * executeLogin() 始终返回 true 的原因是因为具体的是否登录成功的判断，需要在 Realm 中手动实现，此处不做统一判断
     * @author wangyabo
     * @date 2020/1/8 13:35
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        //得到授权的token
        String header = req.getHeader(authorization);
        BaseJwtToken token = new BaseJwtToken(header);
        //提交给realm进行认证操作,如果失败则抛出异常被捕获
        getSubject(request, response).login(token);
        //如果无异常代表登录成功，返回true，开始进行数据交互
        return true;
    }

    /**
     * @param request ServletRequest
     * @param response ServletResponse
     * @Description 重写该方法避免循环调用doGetAuthenticationInfo方法
     * @author wangyabo
     * @date 2020/1/9 10:46
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
       this.sendChallenge(request,response);
       return false;
    }

   /**
    * @param request ServletRequest
    * @param response ServletResponse
    * @Description 拦截失败的请求向前台响应
    * @author wangyabo
    * @date 2020/1/14 13:00
    * @exception  401错误
    */
    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setCharacterEncoding("utf-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        final String message = "会话超时，请重新登录";
        try (PrintWriter out = httpResponse.getWriter()) {
            out.print(JSON.toJSON(JsonResult.ok(AuthConstants.TOKEN_EXPIRED , message)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
