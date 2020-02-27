package com.ybj.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.ybj.auth.dao.UserMapper;
import com.ybj.auth.model.User;
import com.ybj.auth.service.UserRoleServiceI;
import com.ybj.auth.service.UserServiceI;
import com.ybj.auth.utils.JacksonUtils;
import com.ybj.auth.utils.Md5EncryptionUtil;
import com.ybj.utils.common.JsonResult;
import com.ybj.utils.constant.BusniessConstants;
import com.ybj.utils.constant.ConfigConstants;
import com.ybj.utils.constant.MessageConstants;
import com.ybj.utils.exception.CustomGenericException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制类
 * @author caicai.gao
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceI userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleServiceI userRoleService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;




//    使用缓存后， 方法不再输出日志
    @Cacheable(value = "userList", key = "#queryInfo" )
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(HttpServletRequest request, String queryInfo ){
        JsonNode jsonNode = JacksonUtils.stringToJsonNode(queryInfo);
        log.info("查询所有用户   执行了");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!jsonNode.get("userName").asText().equals(""),"USER_FULL_NAME" , jsonNode.get("userName").asText());
        List<User> list = userMapper.selectList(queryWrapper);
        return list;
    }

    @GetMapping("/getUserById")
    public User getUserById(Long userId){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = userService.getById(userId);
        return user;
    }

    /**
     * 用户一览列表
     * @param serverParams 查询参数
     * @return JsonResult 状态响应类
     */
    @PostMapping(value = "/users", produces="application/json")
    public JsonResult getUsers(@RequestBody Map<String, Object> serverParams) {
        Map<String, Object> searchParams = (Map<String, Object>) serverParams.get("serverParams");
        IPage<User> userPage = userService.getAll(searchParams);
        return JsonResult.ok().addData(userPage.getRecords()).add(BusniessConstants.TOTAL_COUNT,userPage.getTotal());
    }

    @PostMapping(value = "/user")
    public JsonResult addUser(@RequestBody User user) {
//        user.setCreateTime(LocalDateTime.now());
//        user.setUpdateTime(LocalDateTime.now());
        user.setCreatedBy("1");
        user.setUpdatedBy("1");
        userMapper.insert(user);
        System.out.println();
        return JsonResult.ok();
    }



    /**
     * 删除用户（逻辑删除，将用户记录中是否删除状态更改为1-即已删除）
     * @param req HttpServletRequest
     * @return JsonResult 状态响应类
     */
    @PostMapping(value = "/delete")
    public JsonResult delete(HttpServletRequest req) {
        String userId = req.getParameter("userId");
        try {
            User deleteUser = userService.getById(userId);
            if (deleteUser.getLoginName().equals(ConfigConstants.ADMIN_USER)){
                return JsonResult.ok(ConfigConstants.ERROR_CODE_BUSINESS, MessageConstants.ORG_STRUC_MSG004);
            } else {
                // 先删除与角色的关联信息
                userRoleService.removeById(userId);
                // 删除用户（逻辑删除）
                userService.removeById(userId);
            }
            return JsonResult.ok("删除用户成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
            return JsonResult.fail("删除用户失败！");
        }
    }

    /**
     * 保存用户
     * @param user 用户信息
     * @return JsonResult 状态响应类
     */
    @PostMapping(value = "/save")
    public JsonResult save(@RequestBody User user) {
//        User loginUser = (User)SecurityUtils.getSubject().getPrincipal();
        try {
            user.setCreatedBy("1");
            user.setUpdatedBy("1");
            user.setDeleted(0);
            if (user.getUserId() != null) {
                // 修改用户
                user.setUpdateTime(LocalDateTime.now());
                user = userService.saveUser(1, user);
                return JsonResult.ok("用户修改成功！").addData(user);
            } else {
                // 新建用户
                user = userService.saveUser(0, user);
                return JsonResult.ok("用户新建成功！").addData(user);
            }
        } catch (CustomGenericException ex1) {
            log.error(ex1.getMessage());
            if (user.getUserId() != null) {
                return JsonResult.fail("修改用户失败！");
            } else {
                return JsonResult.fail("新建用户失败！");
            }
        }
    }


    /**
     * 修改密码
     * @param req HttpServletRequest
     * @return JsonResult 状态响应类
     */
    @PostMapping(value = "/updatePassword")
    public JsonResult updatePassword(HttpServletRequest req) {
        String loginName = req.getParameter("loginName");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        User user = new User();
        user.setLoginName(loginName);
        user.setPassword(Md5EncryptionUtil.convertMd5(oldPassword));
        // 用户验证
        User loginUser = userService.checkUser(user);
        if (loginUser != null) {
            loginUser.setPassword(Md5EncryptionUtil.convertMd5(newPassword));
            userService.updateById(loginUser);
            return JsonResult.ok("密码修改成功！");
        } else {
            return JsonResult.ok(ConfigConstants.ERROR_CODE_BUSINESS, MessageConstants.USER_MSG003);

        }
    }

    /**
     * 重置密码
     * @param req HttpServletRequest
     * @return JsonResult 状态响应类
     */
    @PostMapping(value = "/resetPassword")
    public JsonResult resetPassword(HttpServletRequest req) {
        String userId = req.getParameter("userId");
        User oldUser = userService.getById(userId);
        oldUser.setPassword(Md5EncryptionUtil.convertMd5(ConfigConstants.PASSWORD));
        oldUser.setUpdateTime(LocalDateTime.now());
        userService.updateById(oldUser);
        return JsonResult.ok("密码重置成功！");
    }



    @PostMapping(value = "/upload")
    public JsonResult upload(HttpServletRequest request, MultipartFile file) {
        User loginUser = (User)SecurityUtils.getSubject().getPrincipal();
        try {
            return JsonResult.ok();

        } catch (CustomGenericException ex1) {
            log.error(ex1.getMessage());
            return JsonResult.fail("查询用户权限失败！");
        }
    }

    @CacheEvict(value = "userList",allEntries = true)
    @PostMapping("/editUser")
    public JsonResult editUser(@RequestBody User user){
        System.out.println("user = " + user);
        try{
            user.setUpdatedBy("1");
            userService.updateById(user);
            return JsonResult.ok();
        } catch (Exception e){
            return JsonResult.fail();
        }
    }

}