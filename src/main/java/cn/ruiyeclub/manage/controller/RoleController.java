package cn.ruiyeclub.manage.controller;

import cn.ruiyeclub.common.annotation.SysLogs;
import cn.ruiyeclub.common.bean.ResponseCode;
import cn.ruiyeclub.common.bean.ResponseResult;
import cn.ruiyeclub.manage.dto.param.FindRoleParam;
import cn.ruiyeclub.manage.dto.param.RoleAddParam;
import cn.ruiyeclub.manage.dto.param.RoleUpdateParam;
import cn.ruiyeclub.manage.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author Ray。
 * @date 2020/4/19 9:41
 */
@RestController
@RequestMapping(value = {"/system/role"})
@Api(tags = {"角色管理"})
public class RoleController{

    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation("分页获取角色数据")
    @RequiresPermissions("system:user:list")
    @PostMapping("/list")
    @SysLogs("分页获取角色数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "用户获取过滤条件") FindRoleParam findRoleParam){
        return ResponseResult.e(ResponseCode.OK,sysRoleService.listPage(findRoleParam));
    }

    @ApiOperation("删除角色")
    @RequiresPermissions("system:user:remove")
    @PostMapping("/remove/{id}")
    @SysLogs("删除角色")
    public ResponseResult remove(@PathVariable("id") String rid){
        sysRoleService.removeWithId(rid);
        return ResponseResult.e(ResponseCode.OK);
    }

    @ApiOperation("添加角色")
    @RequiresPermissions("system:user:add")
    @PostMapping("/add")
    @SysLogs("添加角色")
    public ResponseResult add(@RequestBody @Validated RoleAddParam roleAddParam){
        sysRoleService.add(roleAddParam);
        return ResponseResult.e(ResponseCode.OK);
    }

    @ApiOperation("修改角色权限")
    @RequiresPermissions("system:user:update")
    @PostMapping("/update/{id}")
    @SysLogs("修改角色权限")
    public ResponseResult update(@PathVariable("id") String rid,
                                 @RequestBody @Validated RoleUpdateParam roleUpdateParam){
        sysRoleService.update(rid,roleUpdateParam);
        return ResponseResult.e(ResponseCode.OK);
    }

}
