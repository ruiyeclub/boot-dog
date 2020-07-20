package cn.ruiyeclub.manage.dto.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Ray。
 * @date 2020/7/20 19:46
 */
@Data
public class SignInParam {

    @NotBlank(message = "用户名不可以为空！")
    private String username;

    @NotBlank(message = "密码不可以为空！")
    @Pattern(regexp = "^(\\w){6,18}$",message = "密码应为[A-Za-z0-9_]组成的6-18位字符！")
    private String password;
}
