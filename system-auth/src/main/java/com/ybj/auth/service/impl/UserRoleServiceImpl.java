package com.ybj.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybj.auth.dao.UserRoleMapper;
import com.ybj.auth.model.UserRole;
import com.ybj.auth.service.UserRoleServiceI;
import org.springframework.stereotype.Service;

/**
 * 用户-角色管理实现类
 * @author caicai.gao
 */
@Service("UserRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleServiceI {
}
