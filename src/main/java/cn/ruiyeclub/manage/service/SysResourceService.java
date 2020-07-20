package cn.ruiyeclub.manage.service;

import cn.ruiyeclub.manage.dto.system.resource.ResourceDTO;
import cn.ruiyeclub.manage.entity.SysResource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author Licoy
 * @version 2018/4/20/16:35
 */
public interface SysResourceService extends IService<SysResource> {

    /**
     * 添加资源
     * @param dto
     */
    void add(ResourceDTO dto);

    /**
     * 更新资源
     * @param id
     * @param dto
     */
    void update(String id,ResourceDTO dto);

    /**
     * 删除资源
     * @param id
     */
    void remove(String id);

    SysResource getResourceAllParent(SysResource resource,Map<String,SysResource> cacheMap,
                                     Map<String,SysResource> cacheMap2);



}
