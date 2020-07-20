package cn.ruiyeclub.manage.service.impl;

import cn.ruiyeclub.manage.entity.SysResource;
import cn.ruiyeclub.manage.entity.SysRoleResource;
import cn.ruiyeclub.manage.mapper.SysRolePermissionMapper;
import cn.ruiyeclub.manage.service.SysResourceService;
import cn.ruiyeclub.manage.service.SysRoleResourceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Licoy
 * @version 2018/4/16/9:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRoleResource>
        implements SysRoleResourceService {

    @Autowired
    private SysResourceService resourceService;

    @Override
    public List<SysResource> findAllResourceByRoleId(String rid) {
        List<SysRoleResource> rps = this.list(new QueryWrapper<SysRoleResource>().eq("rid", rid));
        if(rps!=null){
            List<String> pids = new ArrayList<>();
            rps.forEach(v->pids.add(v.getPid()));
            if(pids.size()==0){
                return null;
            }
            QueryWrapper<SysResource> sysResourceQueryWrapper = new QueryWrapper<SysResource>()
                    .in("id", pids)
                    .orderByAsc("sort");
            return resourceService.list(sysResourceQueryWrapper);
        }
        return null;
    }
}
