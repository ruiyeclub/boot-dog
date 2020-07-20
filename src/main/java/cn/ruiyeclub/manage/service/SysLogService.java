package cn.ruiyeclub.manage.service;

import cn.ruiyeclub.manage.dto.system.log.FindLogDTO;
import cn.ruiyeclub.manage.entity.SysLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Licoy
 * @version 2018/4/28/9:56
 */
public interface SysLogService extends IService<SysLog>{

    IPage<SysLog> listPage(FindLogDTO findLogDTO);

    void remove(List<String> idList);

}
