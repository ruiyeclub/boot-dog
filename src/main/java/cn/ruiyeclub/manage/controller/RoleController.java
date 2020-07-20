package cn.ruiyeclub.manage.controller;

import cn.ruiyeclub.common.annotation.SysLogs;
import cn.ruiyeclub.common.bean.ResponseCode;
import cn.ruiyeclub.common.bean.ResponseResult;
import cn.ruiyeclub.manage.dto.system.role.FindRoleDTO;
import cn.ruiyeclub.manage.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author Licoy
 * @version 2018/4/19/9:41
 */
@RestController
@RequestMapping(value = {"/system/role"})
@Api(tags = {"角色管理"})
public class RoleController{

    @Autowired
    private SysRoleService sysRoleService;

    @RequiresPermissions("system:user:list")
    @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取角色数据")
    @SysLogs("分页获取角色数据")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "身份认证Token")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "用户获取过滤条件") FindRoleDTO findRoleDTO){
        return ResponseResult.e(ResponseCode.OK,sysRoleService.listPage(findRoleDTO));
    }

    //TODO 修改用户权限
}
