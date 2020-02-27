package com.ybj.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ybj.auth.model.RolePermission;
import com.ybj.auth.service.PermissionServiceI;
import com.ybj.auth.service.RolePermissionServiceI;
import com.ybj.utils.common.JsonResult;
import com.ybj.utils.exception.CustomGenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限管理控制类
 * @author caicai.gao
 */
@Slf4j
@RestController
@RequestMapping("/permission")
public class PermissionController {

	@Autowired
	private PermissionServiceI permissionService;

	@Autowired
	private RolePermissionServiceI rolePermissionService;

	/**
	 * 获取角色权限PermissionTree
	 * @param req HttpServletRequest
	 * @return JsonResult 状态响应类
	 */
//	@PostMapping(value = "/getPermissionTree")
//	public JsonResult getPermissionTree(HttpServletRequest req) {
//		String roleId = req.getParameter("oid");
//		List<TreeJsonData> permissionData = permissionService.getPermissionTree(roleId);
//		return JsonResult.ok().addData(permissionData);
//	}

	/**
	 * 保存角色的权限
	 * @param req HttpServletRequest
	 * @return JsonResult 状态响应类
	 */
	@PostMapping(value = "/savePermissionTree")
	public JsonResult savePermissionTree(HttpServletRequest req) {
		RolePermission rolePermission = new RolePermission();
		try {
			// 获取参数角色id，即表RolePermission中的RoleId
			String oid = req.getParameter("oid");
			rolePermissionService.removeById(oid);
			// 获取修改后角色拥有的权限id
			String permissions = req.getParameter("perStr");
			// 将获取的权限id转换成集合
			JSONArray json = JSON.parseArray(permissions);
			// 遍历集合并保存
			for (int i = 0; i < json.size(); i++) {
				String permissionId = json.getString(i);
				rolePermission.setPermissionId(permissionId);
				rolePermission.setRoleId(oid);
				rolePermissionService.save(rolePermission);
			}
			return JsonResult.ok("权限保存成功！");
		} catch (CustomGenericException ex1) {
			log.error(ex1.getMessage());
			return JsonResult.fail("权限保存失败！");
		}
	}

}
