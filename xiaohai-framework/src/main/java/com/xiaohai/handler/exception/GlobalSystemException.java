package com.xiaohai.handler.exception;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.enums.AppHttpCodeEnum;
import com.xiaohai.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalSystemException {
    @ExceptionHandler(SystemException.class)
    public ResponseResult SystemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常：{}",e);
        //返回异常结果
        return ResponseResult.errorResult(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult ExceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常：{}",e);
        //返回异常结果
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, e.getMessage());
    }
}
