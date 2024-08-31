package com.hncu.config.handler;


import com.hncu.result.CodeEnum;
import com.hncu.result.R;
import com.hncu.util.JSONUtils;
import com.hncu.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //执行到这里，说明登录成功，那我们向前端返回json就行了
        //R result = R.FAIL(accessDeniedException.getLocalizedMessage());
        //R result = R.FAIL("抱歉，没有权限访问");
        R result = R.FAIL(CodeEnum.ACCESS_DENIED);

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把json写出去，写到浏览器
        ResponseUtils.write(response, resultJSON);
    }
}
