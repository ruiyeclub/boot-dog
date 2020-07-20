package cn.ruiyeclub.manage.service.impl;

import cn.ruiyeclub.common.exception.RequestException;
import cn.ruiyeclub.manage.dto.param.FindRoleParam;
import cn.ruiyeclub.manage.dto.param.RoleAddParam;
import cn.ruiyeclub.manage.dto.param.RoleUpdateParam;
import cn.ruiyeclub.manage.entity.SysResource;
import cn.ruiyeclub.manage.entity.SysRole;
import cn.ruiyeclub.manage.entity.SysRoleResource;
import cn.ruiyeclub.manage.entity.SysUserRole;
import cn.ruiyeclub.manage.mapper.SysRoleMapper;
import cn.ruiyeclub.manage.service.ShiroService;
import cn.ruiyeclub.manage.service.SysRoleResourceService;
import cn.ruiyeclub.manage.service.SysRoleService;
import cn.ruiyeclub.manage.service.SysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleResourceService roleResourceService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private ShiroService shiroService;

    @Override
    public List<SysRole> findAllRoleByUserId(String uid,Boolean hasResource) {
        List<SysUserRole> userRoles = userRoleService.list(new QueryWrapper<SysUserRole>().eq("uid", uid));
        List<SysRole> roles = new ArrayList<>();
        userRoles.forEach(v->{
            SysRole role = this.getById(v.getRid());
            if(role!=null){
                if(hasResource){
                    List<SysResource> permissions = roleResourceService.findAllResourceByRoleId(role.getId());
                    role.setResources(permissions);
                }
            }
            roles.add(role);
        });
        return roles;
    }

    @Override
    public IPage<SysRole> listPage(FindRoleParam findRoleParam) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        Page<SysRole> rolePage = this.page(new Page<>(findRoleParam.getPage(),
                findRoleParam.getPageSize()), wrapper);
        if(findRoleParam.getHasResource()){
            if(rolePage.getRecords()!=null){
                rolePage.getRecords().forEach(v->
                        v.setResources(roleResourceService.findAllResourceByRoleId(v.getId())));
            }
        }
        return rolePage;
    }

    @Override
    public void removeWithId(String rid) {
        SysRole role = this.getById(rid);
        if(role==null){ throw RequestException.fail("角色不存在！");}
        try {
            this.removeById(rid);
            this.updateCache(role,true,false);
        }catch (DataIntegrityViolationException e){
            throw RequestException.fail(
                    String.format("请先解除角色为 %s 角色的全部用户！",role.getName()),e);
        }catch (Exception e){
            throw RequestException.fail("角色删除失败！",e);
        }
    }

    @Override
    public void update(String rid, RoleUpdateParam roleUpdateParam) {
        SysRole role = this.getById(rid);
        if(role==null){ throw RequestException.fail("角色不存在！");}
        BeanUtils.copyProperties(roleUpdateParam,role);
        try {
            this.updateById(role);
            roleResourceService.remove(new QueryWrapper<SysRoleResource>()
                    .eq("rid",rid));
            for (SysResource sysResource : roleUpdateParam.getResources()) {
                roleResourceService.save(SysRoleResource.builder()
                        .pid(sysResource.getId())
                        .rid(role.getId())
                        .build());
            }
            this.updateCache(role,true,false);
        }catch (Exception e){
            throw RequestException.fail("角色更新失败！",e);
        }

    }

    @Override
    public void add(RoleAddParam addDTO) {
        SysRole role = this.getOne(new QueryWrapper<SysRole>().eq("name",addDTO.getName()));
        if(role!=null){
            throw RequestException.fail(
                    String.format("已经存在名称为 %s 的角色",addDTO.getName()));
        }
        role = new SysRole();
        BeanUtils.copyProperties(addDTO,role);
        try {
            this.save(role);
            for (SysResource sysResource : addDTO.getResources()) {
                roleResourceService.save(SysRoleResource.builder()
                        .pid(sysResource.getId())
                        .rid(role.getId())
                        .build());
            }
        }catch (Exception e){
            throw RequestException.fail("添加失败",e);
        }
    }

    @Override
    public void updateCache(SysRole role,Boolean author, Boolean out) {
        List<SysUserRole> sysUserRoles = userRoleService.list(new QueryWrapper<SysUserRole>()
                .eq("rid", role.getId())
                .groupBy("uid"));
        List<String> userIdList = new ArrayList<>();
        if(sysUserRoles!=null && sysUserRoles.size()>0){
            sysUserRoles.forEach(v-> userIdList.add(v.getUid()));
        }
        shiroService.clearAuthByUserIdCollection(userIdList);
    }
}
