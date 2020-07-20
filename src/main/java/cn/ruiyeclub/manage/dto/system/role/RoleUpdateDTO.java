package cn.ruiyeclub.manage.dto.system.role;

import cn.ruiyeclub.manage.entity.SysResource;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Licoy
 * @version 2018/4/19/9:51
 */
@Data
public class RoleUpdateDTO {

    @NotBlank(message = "姓名不能为空")
    private String name;

    @Size(min = 1,message = "请至少选择一个权限资源")
    private List<SysResource> resources;

}
