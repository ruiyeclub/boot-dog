package cn.ruiyeclub.manage.service.impl;

import cn.ruiyeclub.common.bean.ResponseCode;
import cn.ruiyeclub.common.exception.RequestException;
import cn.ruiyeclub.manage.dto.param.PageParam;
import cn.ruiyeclub.manage.entity.SysLog;
import cn.ruiyeclub.manage.mapper.SysLogMapper;
import cn.ruiyeclub.manage.service.SysLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Licoy
 * @version 2018/4/28/9:57
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public IPage<SysLog> listPage(PageParam pageParam) {
        QueryWrapper<SysLog> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc(pageParam.getAsc());
        return this.page(new Page<>(pageParam.getPage(),pageParam.getPageSize()),wrapper);
    }

    @Override
    public void remove(List<String> idList) {
        try {
            this.removeByIds(idList);
        }catch (Exception e){
            throw new RequestException(ResponseCode.FAIL.code,"批量删除日志失败",e);
        }
    }
}
