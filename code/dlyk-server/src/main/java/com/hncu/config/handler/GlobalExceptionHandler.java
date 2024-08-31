package com.hncu.config.handler;

import com.hncu.result.CodeEnum;
import com.hncu.result.R;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    DataIntegrityViolationException e;

    /**
     *  异常处理方法(controller发生异常，使用该方法捕获)
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public R handlerException(Exception e) {
        e.printStackTrace();
        return R.FAIL(e.getMessage());
    }

    @ExceptionHandler
    public R handlerException(AccessDeniedException e) {
        e.printStackTrace();
        return R.FAIL(CodeEnum.ACCESS_DENIED);
    }

    /**
     *  数据访问异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = DataAccessException.class)
    public R handlerSQLException(DataAccessException e) {
        System.out.println("后台数据库异常被捕获了");
        e.printStackTrace();
        return R.FAIL(CodeEnum.DATA_ACCESS_EXCEPTION);
    }
}
