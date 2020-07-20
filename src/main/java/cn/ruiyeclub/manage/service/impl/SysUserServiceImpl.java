package cn.ruiyeclub.manage.service.impl;

import cn.ruiyeclub.common.bean.ResponseCode;
import cn.ruiyeclub.common.exception.RequestException;
import cn.ruiyeclub.common.shiro.jwt.JwtToken;
import cn.ruiyeclub.common.util.IpUtils;
import cn.ruiyeclub.common.util.MD5EncryptUtil;
import cn.ruiyeclub.manage.dto.param.*;
import cn.ruiyeclub.manage.entity.SysResource;
import cn.ruiyeclub.manage.entity.SysRole;
import cn.ruiyeclub.manage.entity.SysUser;
import cn.ruiyeclub.manage.entity.SysUserRole;
import cn.ruiyeclub.manage.mapper.SysUserMapper;
import cn.ruiyeclub.manage.service.*;
import cn.ruiyeclub.manage.dto.result.SysUserResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper,SysUser> implements SysUserService {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private SysResourceService resourceService;

    @Override
    public SysUser findUserByName(String name,boolean hasResource) {
        SysUser user = this.getOne(new QueryWrapper<SysUser>().eq("username",name));
        if(user == null){
            return null;
        }
        user.setRoles(roleService.findAllRoleByUserId(user.getId(),hasResource));
        return user;
    }

    @Override
    public SysUser findUserById(String id,boolean hasResource) {
        SysUser user = this.getById(id);
        if(user == null){
            return null;
        }
        user.setRoles(roleService.findAllRoleByUserId(user.getId(),false));
        return user;
    }

    @Override
    public void signIn(SignInParam signInParam) {
        if( "".equals(signInParam.getUsername()) || "".equals(signInParam.getPassword()) ){
            throw new RequestException(ResponseCode.SING_IN_INPUT_EMPTY);
        }
        JwtToken token = new JwtToken(null,signInParam.getUsername(),signInParam.getPassword());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            if(!subject.isAuthenticated()){
                throw new RequestException(ResponseCode.SIGN_IN_INPUT_FAIL);
            }
        }catch (DisabledAccountException e){
            throw new RequestException(ResponseCode.SIGN_IN_INPUT_FAIL.code,e.getMessage(),e);
        }catch (Exception e){
            throw new RequestException(ResponseCode.SIGN_IN_FAIL,e);
        }
    }


    @Override
    public SysUserResult getCurrentUser(){
        IpUtils.executeLogin();
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()){
            throw new RequestException(ResponseCode.NOT_SING_IN);
        }
        JwtToken jwtToken = new JwtToken();
        Object principal = subject.getPrincipal();
        if(principal==null){
            throw RequestException.fail("用户信息获取失败");
        }
        BeanUtils.copyProperties(principal,jwtToken);
        SysUser user = this.findUserByName(jwtToken.getUsername(),false);
        if(user==null){
            throw RequestException.fail("用户不存在");
        }
        //获取菜单/权限信息
        List<SysResource> allPer = userRolesRegexResource(roleService.findAllRoleByUserId(user.getId(),true));
        SysUserResult vo = new SysUserResult();
        BeanUtils.copyProperties(user,vo);
        vo.setResources(allPer);
        return vo;
    }

    @Override
    public List<String> getAllPermissionTag() {
        IpUtils.executeLogin();
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()){
            throw new RequestException(ResponseCode.NOT_SING_IN);
        }
        JwtToken jwtToken = new JwtToken();
        Object principal = subject.getPrincipal();
        if(principal==null){
            throw RequestException.fail("用户信息获取失败");
        }
        BeanUtils.copyProperties(principal,jwtToken);
        SysUser user = this.getOne(new QueryWrapper<SysUser>()
                .eq("username",jwtToken.getUsername())
                .orderByAsc("id"));
        if(user==null){
            throw RequestException.fail("用户不存在");
        }
        List<SysRole> allRoleByUserId = roleService.findAllRoleByUserId(user.getId(), true);
        List<String> permissions = new LinkedList<>();
        for (SysRole sysRole : allRoleByUserId) {
            if(sysRole.getResources()!=null && sysRole.getResources().size()>0){
                sysRole.getResources().forEach(s-> permissions.add(s.getPermission()));
            }
        }
        return permissions;
    }

    @Override
    public List<SysResource> userRolesRegexResource(List<SysRole> roles){
        if(roles!=null && roles.size()>0){
            Map<String,SysResource> resourceMap = new LinkedHashMap<>();
            roles.forEach(role -> {
                if(role.getResources()!=null && role.getResources().size()>0){
                    role.getResources().forEach(resource -> //含有则不覆盖
                            resourceMap.putIfAbsent(resource.getId(), resource));
                }
            });
            Map<String,SysResource> cacheMap = new ConcurrentHashMap<>();
            List<SysResource> resourceList = new CopyOnWriteArrayList<>();
            resourceMap.forEach((k,v)-> {
                SysResource allParent = resourceService.getResourceAllParent(v, cacheMap,resourceMap);
                //判断是否已经包含此对象
                if(!resourceList.contains(allParent)){
                    resourceList.add(allParent);
                }
            });
            return resourceList;
        }
        return null;
    }

    @Override
    public IPage<SysUserResult> getAllUserBySplitPage(PageParam pageParam) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("create_date");
        Page<SysUser> userPage = this.page(new Page<>(pageParam.getPage(),
                pageParam.getPageSize()), wrapper);
        Page<SysUserResult> userVOPage = new Page<>();
        BeanUtils.copyProperties(userPage,userVOPage);
        List<SysUserResult> userVOS = new ArrayList<>();
        userPage.getRecords().forEach(v->{
            SysUserResult vo = new SysUserResult();
            BeanUtils.copyProperties(v,vo);
            //查找匹配所有用户的角色
            vo.setRoles(roleService.findAllRoleByUserId(v.getId(),false));
            userVOS.add(vo);
        });
        userVOPage.setRecords(userVOS);
        return userVOPage;
    }

    @Override
    public void statusChange(String userId, Integer status) {
        SysUser user = this.getById(userId);
        if(user==null){
            throw RequestException.fail("用户不存在");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(SecurityUtils.getSubject().getPrincipal(),sysUser);
        if(user.getUsername().equals(sysUser.getUsername())){
            throw RequestException.fail("不能锁定自己的账户");
        }
        user.setStatus(status);
        try {
            this.updateById(user);
            //若为0 需要进行清除登陆授权以及权限信息
            /*if(status==0){

            }*/
            shiroService.clearAuthByUserId(userId);
        }catch (Exception e){
            throw RequestException.fail("操作失败",e);
        }
    }

    @Override
    public void removeUser(String userId) {
        SysUser user = this.getById(userId);
        if(user==null){
            throw RequestException.fail("用户不存在！");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(SecurityUtils.getSubject().getPrincipal(),sysUser);
        if(user.getUsername().equals(sysUser.getUsername())){
            throw RequestException.fail("不能删除自己的账户！");
        }
        try {
            this.removeById(userId);
            shiroService.clearAuthByUserId(userId);
        }catch (Exception e){
            throw RequestException.fail("删除失败",e);
        }
    }

    @Override
    public void add(UserAddParam addDTO) {
        SysUser findUser = this.findUserByName(addDTO.getUsername(),false);
        if(findUser!=null){
            throw RequestException.fail(
                    String.format("已经存在用户名为 %s 的用户",addDTO.getUsername()));
        }
        try {
            findUser = new SysUser();
            BeanUtils.copyProperties(addDTO,findUser);
            findUser.setCreateDate(new Date());
            findUser.setPassword(MD5EncryptUtil.encrypt((findUser.getPassword())+findUser.getUsername()));
            this.save(findUser);
            this.updateUserRole(findUser);
        }catch (Exception e){
            throw RequestException.fail("添加用户失败",e);
        }
    }

    @Override
    public void update(String id, UserUpdateParam updateDTO) {
        SysUser user = this.getById(id);
        if(user==null){
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的用户",id));
        }
        SysUser findUser = this.getOne(new QueryWrapper<SysUser>()
                    .eq("username",updateDTO.getUsername()).ne("id",id));
        if(findUser!=null){
            throw RequestException.fail(
                    String.format("更新失败，已经存在用户名为 %s 的用户",updateDTO.getUsername()));
        }
        BeanUtils.copyProperties(updateDTO,user);
        try {
            this.updateById(user);
            this.updateUserRole(user);
            shiroService.clearAuthByUserId(user.getId());
        }catch (RequestException e){
            throw RequestException.fail(e.getMsg(),e);
        }catch (Exception e){
            throw RequestException.fail("用户信息更新失败",e);
        }
    }

    @Override
    public void updateUserRole(SysUser user) {
        try {
            userRoleService.remove(new QueryWrapper<SysUserRole>().eq("uid",user.getId()));
            if(user.getRoles()!=null && user.getRoles().size()>0){
                user.getRoles().forEach(v-> userRoleService.save(SysUserRole.builder()
                        .uid(user.getId())
                        .rid(v.getId()).build()));
            }
        }catch (Exception e){
            throw RequestException.fail("用户权限关联失败",e);
        }
    }

    @Override
    public void resetPassword(ResetPasswordParam resetPasswordParam){
        SysUser user = this.getById(resetPasswordParam.getUid().trim());
        if(user==null){
            throw RequestException.fail(String.format("不存在ID为 %s 的用户", resetPasswordParam.getUid()));
        }
        String password = MD5EncryptUtil.encrypt((resetPasswordParam.getPassword())+user.getUsername());
        user.setPassword(password);
        try {
            this.updateById(user);
            shiroService.clearAuthByUserId(user.getId());
        }catch (Exception e){
            throw RequestException.fail(String.format("ID为 %s 的用户密码重置失败", resetPasswordParam.getUid()),e);
        }
    }
}
