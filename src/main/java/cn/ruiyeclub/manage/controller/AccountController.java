package cn.ruiyeclub.manage.controller;

import cn.ruiyeclub.common.annotation.JwtClaim;
import cn.ruiyeclub.common.annotation.SysLogs;
import cn.ruiyeclub.common.bean.ResponseCode;
import cn.ruiyeclub.common.bean.ResponseResult;
import cn.ruiyeclub.common.shiro.jwt.JwtToken;
import cn.ruiyeclub.manage.dto.param.SignInParam;
import cn.ruiyeclub.manage.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Licoy
 * @version 2018/4/13/14:02
 */
@RestController
@RequestMapping(value = {"/account"})
@Api(tags = {"账户相关"})
public class AccountController {

    @Autowired
    private SysUserService userService;

    @PostMapping(value = {"/sign-in"})
    @ApiOperation(value = "登录")
    @SysLogs("登录")
    public ResponseResult signIn(@RequestBody @Validated @ApiParam(value = "登录数据",required = true) SignInParam signInParam){
        userService.signIn(signInParam);
        return ResponseResult.e(ResponseCode.SIGN_IN_OK,((JwtToken)SecurityUtils.getSubject().getPrincipal()).getToken());
    }

    @PostMapping(value = "/current")
    @ApiOperation(value = "获取当前用户信息")
    @SysLogs("获取当前用户信息")
    public ResponseResult current(){
        return ResponseResult.e(ResponseCode.OK, userService.getCurrentUser());
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "注销登录")
    @SysLogs("注销登录")
    public ResponseResult logout(){
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        }catch (Exception e){
            return ResponseResult.e(ResponseCode.LOGOUT_FAIL);
        }
        return ResponseResult.e(ResponseCode.LOGOUT_OK);
    }

    @PostMapping(value = "/all-permission-tag")
    @ApiOperation(value = "获取所有的权限标示")
    public ResponseResult<List<String>> getAllPermissionTag(@JwtClaim String t){
        return ResponseResult.e(ResponseCode.OK,userService.getAllPermissionTag());
    }

}
