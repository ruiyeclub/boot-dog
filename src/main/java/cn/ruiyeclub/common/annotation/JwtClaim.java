package cn.ruiyeclub.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 获取所有的权限标示
 * @author licoy.cn
 * @date 2018/10/16
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtClaim {

    String value() default "username";

    boolean exception() default true;

}
