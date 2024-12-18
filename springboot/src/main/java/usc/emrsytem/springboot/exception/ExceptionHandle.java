package usc.emrsytem.springboot.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import usc.emrsytem.springboot.common.Result;

@Slf4j
@RestControllerAdvice
public class ExceptionHandle {
    //全局异常处理
    @ExceptionHandler(value = Exception.class)
    public Result exceptionError(Exception e){

        log.error("服务器异常",e);
        return Result.error("服务器异常，请联系管理员");
    }

    @ExceptionHandler(value = ServiceException.class)
    public Result serviceExceptionError(ServiceException e){
        log.error("业务异常",e);
        return Result.error(e.getMessage());
    }

}
