package com.ailin.resolver;

import com.ailin.MyException.AllParamException;
import com.ailin.mode.ResultMode;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
public class CommonExceptionHandler {


    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
//    @ResponseBody
//    @ExceptionHandler(value = Exception.class)
//    public ResultMode<String> errorHandler(Exception ex) {
//        return new ResultMode<>(-99,"未知异常",null);
//    }
 
    /**
     * 拦截自定义异常
     * @param ex
     * @return
     */
    @ExceptionHandler(AllParamException.class)
    @ResponseBody
    public ResultMode<String> exceptionHandler(AllParamException ex){
        if(ex.getCode()==null){
            ex.setCode("-99");
        }
        return new ResultMode<>(Integer.parseInt(ex.getCode()),ex.getMessage(),null);
    }
}