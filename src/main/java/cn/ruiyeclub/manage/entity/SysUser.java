package cn.ruiyeclub.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUser extends Model<SysUser> implements Serializable  {

    
    @TableId
    private String id;
    private String username;
    private Integer age;
    private String password;
    private Integer status;
    @TableField(exist = false)
    private List<SysRole> roles;
    private Date createDate;

    private static final long serialVersionUID = 1L;

}
