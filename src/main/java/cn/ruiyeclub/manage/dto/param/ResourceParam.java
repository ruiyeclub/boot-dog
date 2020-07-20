package cn.ruiyeclub.manage.dto.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author licoy.cn
 * @version 2018/4/22
 */
@Data
public class ResourceParam {

    @NotBlank(message = "资源名称不能为空")
    private String name;

    private String parentId;

    @NotNull(message = "资源类型不能为空")
    private Short type;

    @NotBlank(message = "资源链接不能为空")
    private String url;

    private String color;

    private String permission;

    private String icon;

    @NotNull(message = "资源排序不能为空")
    private Long sort;

    private Boolean verification = true;

}
