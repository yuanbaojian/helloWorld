package com.ybj.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ybj.auth.constants.AuthConstants;
import com.ybj.auth.jwt.JwtUtil;
import com.ybj.auth.model.User;
import com.ybj.auth.service.UserServiceI;
import com.ybj.auth.utils.Md5EncryptionUtil;
import com.ybj.utils.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

/**
 * @Author yabo.wang
 * @Date 2020/1/13 14:03
 * @Description 登录控制类
 */
@Slf4j
@RestController
@RequestMapping(value = "/auth")
public class LoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserServiceI userService;


    /***
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param user User
     * @Description 登录验证
     * @author wangyabo
     * @date 2020/1/10 10:12
     */
    @PostMapping(value = "/login" )
    public JsonResult login(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) {
        // 获取加密后的密码
        String passwordEncrypt = Md5EncryptionUtil.convertMd5(user.getPassword());
        // 根据用户名查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("LOGIN_NAME", user.getLoginName());
        User currentUser = userService.getOne(queryWrapper);
        if (currentUser == null) {
            return JsonResult.ok(AuthConstants.ACCOUNT_EMPTY,"该账号在系统中不存在");
        }
        if (!passwordEncrypt.equals(currentUser.getPassword())) {
            return JsonResult.ok(AuthConstants.PASSWORD_ERROR,"您输入的用户名或密码不正确，请检查后再输入");
        }
        if (AuthConstants.ACCOUNT_FORBIDDEN == currentUser.getStatus()) {
            return JsonResult.ok(AuthConstants.PASSWORD_ERROR,"该账号已被禁用，请联系管理员");
        }
        // 生成token令牌
        String token = JwtUtil.generateToken(currentUser.getLoginName(),currentUser.getUserId(), passwordEncrypt);
        try {
        // 将签名放入redis缓存,有过期时间
            this.saveTokenToRedis(token, currentUser.getLoginName(), AuthConstants.TOKEN_EXPIRED_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("生成的token令牌为:{}", token);
        // 向前台发送成功状态码
        return JsonResult.ok().addData(token);

    }


    /** 将token存储到redis中
     * @param token
     * @param expiredTimeSecconds
     * @throws
     */
    private void saveTokenToRedis(String token, String loginName, Integer expiredTimeSecconds) throws Exception {
        stringRedisTemplate.opsForValue().set(AuthConstants.TOKEN_PREFIX + loginName ,token, Duration.ofSeconds(expiredTimeSecconds));
    }


    /**
     *  暂时注销掉 登出的流程， 因为登录没有完全启用，获得不到loginName
     * @param request
     * @return
     */
    @DeleteMapping("/logout")
    public JsonResult logout(HttpServletRequest request){
//        String loginName = JwtUtil.getLoginName();
//        String key = AuthConstants.TOKEN_PREFIX + loginName;
//        stringRedisTemplate.delete(key);
        return JsonResult.ok();
    }


}
