package cn.ruiyeclub.manage.service;

import cn.ruiyeclub.manage.dto.param.PageParam;
import cn.ruiyeclub.manage.entity.SysLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Licoy
 * @version 2018/4/28/9:56
 */
public interface SysLogService extends IService<SysLog>{

    IPage<SysLog> listPage(PageParam pageParam);

    void remove(List<String> idList);

}
