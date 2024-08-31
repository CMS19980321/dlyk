package com.hncu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.hncu.constant.Constants;
import com.hncu.model.TUser;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.KeyRep.Type.SECRET;

/**
 * jwt的工具类
 *
 */
public class JWTUtils {

    /**
     * 生成jwt
     *
     * @return
     */
    public static String createJWT(String userJSON) {
        //组装头数据
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        //链式编程
        return JWT.create()
                //头部
                .withHeader(header)

                //负载(可以有多个)
                .withClaim("user", userJSON)

                //签名
                .sign(Algorithm.HMAC256(Constants.SECRET));
    }

    /**
     * 验证jwt
     *
     */
    public static Boolean verifyJWT(String jwt) {
        try {
            // 使用密钥创建一个jwt验证器
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Constants.SECRET)).build();

            //jwt验证器验证jwt
            jwtVerifier.verify(jwt); //如果此行代码没有抛出异常，就说明jwt验证通过，抛出异常，就说明jwt验证不通过

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解析jwt
     *
     */
    public static String parseJWT(String jwt) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 使用密钥创建一个jwt验证器对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Constants.SECRET)).build();

            //jwt验证器对象验证jwt，得到一个解码后的jwt对象
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);

            //验证通过了，就开始解析负载里面的数据
            Claim userJSON = decodedJWT.getClaim("user");

            return userJSON.asString();
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析jwt得到userId
     *
     * @param jwt
     * @return
     */
    public static Integer parseJWTByUserId(String jwt) {
        String userJSON = parseJWT(jwt);
        TUser user = JSONUtils.toBean(userJSON, TUser.class);
        return user.getId();
    }

    /**
     * 解析jwt得到userRole
     *
     * @param jwt
     * @return
     */
    public static List<String> parseJWTByUserRole(String jwt) {
        String userJSON = parseJWT(jwt);
        TUser user = JSONUtils.toBean(userJSON, TUser.class);
        return user.getRoleList();
    }
}
