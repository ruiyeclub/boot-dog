package cn.ruiyeclub.manage.service;

import cn.ruiyeclub.manage.dto.param.FindRoleParam;
import cn.ruiyeclub.manage.dto.param.RoleAddParam;
import cn.ruiyeclub.manage.dto.param.RoleUpdateParam;
import cn.ruiyeclub.manage.entity.SysRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取指定ID用户的所有角色（并附带查询所有的角色的权限）
     * @param uid 用户ID
     * @return 角色集合
     */
    List<SysRole> findAllRoleByUserId(String uid,Boolean hasResource);

    /**
     * 更新缓存
     * @param role 角色
     * @param author 是否清空授权信息
     * @param out 是否清空session
     */
    void updateCache(SysRole role,Boolean author, Boolean out);

    IPage<SysRole> listPage(FindRoleParam findRoleParam);

    void removeWithId(String rid);

    void update(String rid, RoleUpdateParam roleUpdateParam);

    void add(RoleAddParam addDTO);
}
