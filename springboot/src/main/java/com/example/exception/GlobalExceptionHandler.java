package com.example.exception;


import com.example.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice("com.example.controller")//对com.example.controller包及其子包进行拦截
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json数据
    public Result error(Exception e){
        e.printStackTrace();//打印异常信息
        return Result.error();
    }
    @ExceptionHandler(CustomException.class)
    @ResponseBody//返回json数据
    public Result error(CustomException e){
        //e.printStackTrace();//打印异常信息
        return Result.error(e.getCode(),e.getMsg());
    }

}
