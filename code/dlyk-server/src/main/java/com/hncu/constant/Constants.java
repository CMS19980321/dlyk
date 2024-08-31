package com.hncu.constant;

/**
 * 常量类
 */
public class Constants {

    //JWT的密钥
    public static final String SECRET = "w3osER)wTxc0>pTr03ryP";

    //redis的key的命名规范(在项目开发中)： xxx项目:xxx模块:xxx功能:唯一业务参数
    public static final String REDIS_JWT_KEY = "cukh:user:login:";

    //生成唯一数据的redis的key
    public static final String REDIS_ONLY_NUMBER_KEY = "dlyk:tran:onlynumber";

    //负责人redis的key
    public static final String REDIS_OWNER_KEY = "cukh:user:owner";

    //选择记住我之后的jwt（token）的过期时间,七天
    public static final Long EXPIRE_TIME = 7 * 24 * 60L;

    //默认jwt（token）的过期时间，默认30分钟
    public static final Long DEFAULT_EXPIRE_TIME = 30L;

    //分页时每页显示10条数据
    public static final int PAGE_SIZE = 10;

    //常量空“”
    public static final String EMPTY = "";

    //登录uri地址
    public static final String LOGIN_URI = "/api/login";

    //导出Excel的uri地址
    public static final String EXPORT_EXCEL_URI = "/api/exportExcel";

    //请求token名
    public static final String TOKEN_NAME = "Authorization";

    //记住我的参数名
    public static final String REMEMBERME_NAME = "rememberMe";


    public static final String EXCel_FILE_NAME = "客户信息数据";


}
