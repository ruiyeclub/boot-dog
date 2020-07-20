package cn.ruiyeclub.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysRole extends Model<SysRole> implements Serializable  {

    @TableId
    private String id;
    
    private String name;

    @TableField(exist = false)
    private List<SysResource> resources;

    private static final long serialVersionUID = 1L;

}
