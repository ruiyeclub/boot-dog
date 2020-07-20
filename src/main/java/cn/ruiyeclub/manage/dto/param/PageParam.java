package cn.ruiyeclub.manage.dto.param;

import lombok.Data;

/**
 * @author Ray。
 * @date 2020/7/20 19:49
 */
@Data
public class PageParam {

    private Integer page = 1;
    private Integer pageSize = 10;
    private Boolean asc = false;
}
