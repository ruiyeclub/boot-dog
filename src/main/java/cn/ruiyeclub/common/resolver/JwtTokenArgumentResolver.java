package cn.ruiyeclub.common.resolver;

import cn.ruiyeclub.common.annotation.JwtClaim;
import cn.ruiyeclub.common.bean.ResponseCode;
import cn.ruiyeclub.common.exception.RequestException;
import cn.ruiyeclub.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * @author licoy.cn
 * @version 2018/10/16
 */
@Slf4j
public class JwtTokenArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtClaim.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String authorization = request.getHeader("Authorization");
        log.info("token?{}",authorization);
        String result = null;
        JwtClaim token = null;
        if(authorization!=null){
            Annotation[] methodAnnotations = parameter.getParameterAnnotations();
            for (Annotation methodAnnotation : methodAnnotations) {
                if(methodAnnotation instanceof JwtClaim){
                    token = (JwtClaim) methodAnnotation;
                    break;
                }
            }
            if(token!=null){
                result = JwtUtil.get(authorization,token.value());
            }
        }
        if(result!=null){
            return result;
        }
        if(token==null || token.exception()){
            throw new RequestException(ResponseCode.NOT_SING_IN);
        }else{
            return null;
        }
    }
}
