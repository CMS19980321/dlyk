package com.hncu.config.handler;

import com.hncu.constant.Constants;
import com.hncu.model.TUser;
import com.hncu.result.R;
import com.hncu.service.RedisService;
import com.hncu.util.JSONUtils;
import com.hncu.util.JWTUtils;
import com.hncu.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private RedisService redisService;

    /*@Resource
    private UserService userService;*/

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //由于禁用了session，我们在登录成功后，需要在服务器保持用户的登录状态，前端下次来访问服务器端的时候，服务器端要知道这个人登录了
        TUser tUser = (TUser) authentication.getPrincipal();

        //1、生成一个jwt字符串
        String userJSON = JSONUtils.toJSON(tUser);
        String jwt = JWTUtils.createJWT(userJSON);

        //2、jwt字符串写入redis
        Integer userId = tUser.getId();
        redisService.setValue(Constants.REDIS_JWT_KEY + userId, jwt); //要设置jwt不同的过期时间，选择记住我是7天过期，否则是30分钟过期

        //根据用户是否选择了记住我设置token过期时间
        String rememberMe = request.getParameter("rememberMe"); //true，false，undefined
        if (Boolean.parseBoolean(rememberMe)) {
            redisService.expire(Constants.REDIS_JWT_KEY + userId, Constants.EXPIRE_TIME, TimeUnit.MINUTES);
        } else {
            redisService.expire(Constants.REDIS_JWT_KEY + userId, Constants.DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);
        }

        //更新最近登录时间 (同步操作、异步操作)，这里使用异步操作，异步操作可以提高接口返回数据的速度
        /*new Thread(() -> {
            TUser u = new TUser();
            u.setId(userId);
            u.setLastLoginTime(new Date());
            userService.updateUserLoginTime(u);
        }).start();*/

        //3、把jwt字符串返回给前端， 向前端返回json数据
        /*LoginData loginData = LoginData.builder().jwt(jwt).stringAuthorityList(tUser.getStringAuthorityList()).build();*/
        // R result = R.OK(tUser);
        R result = R.OK(jwt);

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把json写出去，写到浏览器
        ResponseUtils.write(response, resultJSON);
    }
}
