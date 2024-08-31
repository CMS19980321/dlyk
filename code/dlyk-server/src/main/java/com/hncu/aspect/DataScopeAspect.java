package com.hncu.aspect;

import com.hncu.commons.DataScope;
import com.hncu.constant.Constants;
import com.hncu.query.BaseQuery;
import com.hncu.util.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;


/**
 * aspectJ实现AOP
 * @Author caimeisahng
 * @Date 2024/6/30 20:09
 * @Version 1.0
 */

@Component
@Aspect
public class DataScopeAspect {
    //切入点
    @Pointcut(value = "@annotation(com.hncu.commons.DataScope)")
    private void pointCut() {
    }

    @Around(value = "pointCut()")
    //DataScope 连接点
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        //拿到方法上的注解
        DataScope dataScope = methodSignature.getMethod().getDeclaredAnnotation(DataScope.class);

        String tableAlias = dataScope.tableAlias();
        String tableField = dataScope.tableField();

        //在spring web容器中，可以获当前请求的request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader(Constants.TOKEN_NAME);

        //从Token中解析该用户是管理员还是普通用户,获取用户角色
        List<String> roleList = JWTUtils.parseJWTByUserRole(token);
        Integer userId = JWTUtils.parseJWTByUserId(token);

        if (!roleList.contains("admin")) {//不包含admin角色的时候，只查询用户自己的数据，否则查询所有的数据
            Object params = joinPoint.getArgs()[0];//拿方法的第一个参数
            if (params instanceof BaseQuery) {
                BaseQuery query = (BaseQuery) params;
                //实例SQL语句 select *from t_user tu where tu.id = 2 (普通用户)
                //and?
                query.setFilterSQL(" and " + tableAlias + "." + tableField + "=" + userId);
            }
        }

        System.out.println("方法执行之前");
        Object result = joinPoint.proceed();
        System.out.println("方法执行之后");

        return result;

    }
}
