package cn.ruiyeclub.common.handler;


import cn.ruiyeclub.common.bean.ResponseCode;
import cn.ruiyeclub.common.bean.ResponseResult;
import cn.ruiyeclub.common.exception.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author Ray。
 * @date  2019/11/18
 */
@RestControllerAdvice(basePackages = {"cn.ruiyeclub"})
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RequestException.class)
    public ResponseResult requestExceptionHandler(RequestException e){
        if(e.getE()!=null) {e.printStackTrace();}
        return ResponseResult.builder().msg(e.getMsg()).status(e.getStatus()).build();
    }


    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseResult requestExceptionHandler(DataIntegrityViolationException e){
        return ResponseResult.builder().msg("数据操作格式异常").status(ResponseCode.FAIL.code).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();
        String s = "参数验证失败";
        if(result.hasErrors()){
            List<ObjectError> errors = result.getAllErrors();
            s = errors.get(0).getDefaultMessage();
        }
        return ResponseResult.builder().status(ResponseCode.FAIL.code).msg(s).build();
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseResult requestExceptionHandler(Exception e){
        e.printStackTrace();
        return ResponseResult.builder().msg("服务器飘了，管理员去拿刀修理了~").status(ResponseCode.FAIL.code).build();
    }

}
