package cn.ruiyeclub.manage.dto.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Licoy
 * @version 2018/4/25/11:54
 */
@Data
public class ResetPasswordParam {

    @NotBlank(message = "用户标识ID不能为空")
    private String uid;

    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(\\w){6,18}$",message = "密码应为[A-Za-z0-9_]组成的6-18位字符！")
    private String password;

}
