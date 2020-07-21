package cn.ruiyeclub.common.annotation;

import java.lang.annotation.*;

/**
 * 系统日志
 * @author Licoy
 * @date 2018/4/27 17:36
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLogs {
    String value();

}
