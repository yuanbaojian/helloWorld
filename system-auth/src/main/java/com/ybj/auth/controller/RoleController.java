package com.ybj.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ybj.auth.model.Role;
import com.ybj.auth.model.User;
import com.ybj.auth.service.RolePermissionServiceI;
import com.ybj.auth.service.RoleServiceI;
import com.ybj.utils.common.JsonResult;
import com.ybj.utils.constant.BusniessConstants;
import com.ybj.utils.constant.ConfigConstants;
import com.ybj.utils.constant.MessageConstants;
import com.ybj.utils.exception.CustomGenericException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 角色管理Controller
 * @author caicai.gao
 */
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleServiceI roleService;

	@Autowired
	private RolePermissionServiceI rolePermissionService;

	/**
	 * 角色一览列表
	 * @param serverParams 查询参数
	 * @return JsonResult 状态响应类
	 */
	@PostMapping(value = "/roles", produces="application/json")
	public JsonResult getRoles(@RequestBody Map<String, Object> serverParams) {
		Map<String, Object> searchParams = (Map<String, Object>) serverParams.get("serverParams");
		IPage<Role> rolePage = roleService.getAll(searchParams);
		return JsonResult.ok().addData(rolePage.getRecords()).add(BusniessConstants.TOTAL_COUNT,rolePage.getTotal());

	}

    /**
     * 新建或修改角色
     * @param req HttpServletRequest
     * @return JsonResult 状态响应类
     */
	@PostMapping(value = "/save")
	public JsonResult save(HttpServletRequest req) {
		User loginUser = (User)SecurityUtils.getSubject().getPrincipal();
		// request值获取
		String oid = req.getParameter("oid");
		String roleName = req.getParameter("roleName");
		String remark = req.getParameter("remark");
		String createdBy = loginUser.getUserId();
        String updatedBy = loginUser.getUserId();

		try {
			Role role;
			if (oid != null && !"".equals(oid)) {
				role = roleService.saveRole(0, oid, roleName, remark, createdBy, updatedBy);
                return JsonResult.ok("角色修改成功！").addData(role);
			} else {
				role = roleService.saveRole(1, oid, roleName, remark, createdBy, updatedBy);
                return JsonResult.ok("角色新建成功！").addData(role);
			}
		} catch (CustomGenericException ex1) {
            log.error(ex1.getMessage());
            if (oid != null && !"".equals(oid)) {
                return JsonResult.fail("修改角色失败！");
            } else {
                return JsonResult.fail("新建角色失败！");
            }
		}
	}

    /**
     * 删除角色
     * @param req HttpServletRequest
     * @return JsonResult 状态响应类
     */
	@PostMapping(value = "/delete")
	public JsonResult delete(HttpServletRequest req) {
		String oid = req.getParameter("oid");
		try {
			if (oid != null && !"".equals(oid)) {
				QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
				// 使用数据库字段名
				queryWrapper.eq("ROLE_ID",oid);
				int roleCount = roleService.count(queryWrapper);
				if (roleCount == 0) {
					// 删除角色与权限的关联
					rolePermissionService.removeById(oid);
					// 删除角色
					roleService.removeById(oid);
                    return JsonResult.ok("删除角色成功！");
				} else {
					// ORA-02292-找到子记录，表明该角色被引用，不可以删除
                    return JsonResult.ok(ConfigConstants.ERROR_CODE_BUSINESS, MessageConstants.ROLE_MSG003);
				}
			} else {
                return JsonResult.ok(ConfigConstants.ERROR_CODE_BUSINESS,"删除角色失败,缺失参数：oid");
            }
		} catch (CustomGenericException e) {
            log.error(e.getMessage());
            return JsonResult.fail("删除角色失败！");
		}
	}
	
}
