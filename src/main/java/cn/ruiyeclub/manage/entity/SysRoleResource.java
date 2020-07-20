package cn.ruiyeclub.manage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Licoy
 * @version 2018/4/16/8:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysRoleResource extends Model<SysRoleResource> implements Serializable {

    @TableId
    private String id;

    private String rid;

    private String pid;

    private static final long serialVersionUID = 1L;
}
