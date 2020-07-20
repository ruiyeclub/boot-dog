package cn.ruiyeclub.manage.service.impl;

import cn.ruiyeclub.manage.entity.SysUserRole;
import cn.ruiyeclub.manage.mapper.SysUserRoleMapper;
import cn.ruiyeclub.manage.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Licoy
 * @version 2018/4/16/11:32
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
}
