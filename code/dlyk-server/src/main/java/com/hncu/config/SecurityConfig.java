package com.hncu.config;

import com.hncu.config.filter.JwtFilter;
import com.hncu.config.handler.MyAccessDeniedHandler;
import com.hncu.config.handler.MyAuthenticationFailureHandler;
import com.hncu.config.handler.MyAuthenticationSuccessHandler;
import com.hncu.config.handler.MyLogoutSuccessHandler;
import com.hncu.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * @Author caimeisahng
 * @Date 2024/6/9 11:18
 * @Version 1.0
 */
@EnableMethodSecurity //开启方法级别的权限检查
@Configuration
public class SecurityConfig {


    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Resource
    private JwtFilter jwtFilter;

    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;

    //加密器配直
    @Bean
    public PasswordEncoder passwordEncode(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,CorsConfigurationSource configurationSource) throws Exception {
        //禁用跨站请求伪造
        return httpSecurity
                .formLogin((formLogin) ->{
                    formLogin.loginProcessingUrl(Constants.LOGIN_URI)
                            .usernameParameter("loginAct")
                            .passwordParameter("loginPwd")
                            .successHandler(myAuthenticationSuccessHandler)
                            .failureHandler(myAuthenticationFailureHandler);
                })
                .authorizeHttpRequests((authorize) ->{
                    authorize.requestMatchers(Constants.LOGIN_URI).permitAll().
                            anyRequest().authenticated();//任何需求都需要登陆后才能访问
                })
                .csrf(AbstractHttpConfigurer::disable) //禁止使用跨站请求伪造
                //支持跨域请求
                .cors((cors) -> {
                    cors.configurationSource(configurationSource);
                })
                //Session禁用
                .sessionManagement((session) ->{
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                //添加自定义的Filter
                .addFilterBefore(jwtFilter, LogoutFilter.class)
                //退出登陆
                .logout((logout) -> {
                    logout.logoutUrl("/api/logout")//退出体检到该地址，该地址不需要我们写Controller，有框架处理
                            .logoutSuccessHandler(myLogoutSuccessHandler);
                })

                .exceptionHandling( (exceptionHandling) -> {
                    //访问拒绝（没有权限），就执行该handler，在handler中给前端返回json
                    exceptionHandling.accessDeniedHandler(myAccessDeniedHandler);
                })

                .build();
    }

    @Bean
    public CorsConfigurationSource configurationSource(){

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));//允许任何请求来源
        configuration.setAllowedMethods(Arrays.asList("*"));//允许任何请求方法
        configuration.setAllowedHeaders(Arrays.asList("*"));//允许任何请求头(请求头中的内容)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
