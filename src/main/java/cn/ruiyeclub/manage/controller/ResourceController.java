package cn.ruiyeclub.manage.controller;

import cn.ruiyeclub.common.annotation.SysLogs;
import cn.ruiyeclub.common.bean.ResponseCode;
import cn.ruiyeclub.common.bean.ResponseResult;
import cn.ruiyeclub.manage.dto.param.ResourceParam;
import cn.ruiyeclub.manage.service.SysResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



/**
 * @author licoy.cn
 * @version 2018/4/22
 */
@RestController
@RequestMapping(value = "/system/resource")
@Api(tags = {"资源管理"})
public class ResourceController {

    @Autowired
    private SysResourceService resourceService;

    @PostMapping(value = {"/list"})
    @RequiresPermissions("system:resource:list")
    @ApiOperation(value = "获取所有的资源列表")
    @SysLogs("获取所有的资源列表")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "身份认证Token")
    public ResponseResult list(){
        return ResponseResult.e(ResponseCode.OK,resourceService.list());
    }

    @PostMapping(value = {"/add"})
    @RequiresPermissions("system:resource:add")
    @ApiOperation(value = "添加资源")
    @SysLogs("添加资源")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "身份认证Token")
    public ResponseResult add(@RequestBody @Validated @ApiParam("资源数据") ResourceParam resourceParam){
        resourceService.add(resourceParam);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @RequiresPermissions("system:resource:update")
    @ApiOperation(value = "添加资源")
    @SysLogs("添加资源")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "身份认证Token")
    public ResponseResult update(@PathVariable("id") @ApiParam("资源ID") String id,
                                 @RequestBody @Validated @ApiParam("资源数据") ResourceParam resourceParam){
        resourceService.update(id,resourceParam);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @RequiresPermissions("system:resource:remove")
    @ApiOperation(value = "删除资源")
    @SysLogs("删除资源")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "身份认证Token")
    public ResponseResult remove(@PathVariable("id") @ApiParam("资源ID") String id){
        resourceService.remove(id);
        return ResponseResult.e(ResponseCode.OK);
    }


}
