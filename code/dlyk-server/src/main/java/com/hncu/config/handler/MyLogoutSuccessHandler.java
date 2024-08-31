package com.hncu.config.handler;


import com.hncu.constant.Constants;
import com.hncu.model.TUser;
import com.hncu.result.CodeEnum;
import com.hncu.result.R;
import com.hncu.service.RedisService;
import com.hncu.util.JSONUtils;
import com.hncu.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private RedisService redisService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //用户退出登录，那么把redis中的jwt删除
        TUser tUser = (TUser) authentication.getPrincipal();

        redisService.delete(Constants.REDIS_JWT_KEY + tUser.getId());

        //执行到这里，说明退出成功，那我们向前端返回json就行了
        R result = R.OK(CodeEnum.OK);

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把json写出去，写到浏览器
        ResponseUtils.write(response, resultJSON);
    }
}
