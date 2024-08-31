import axios from "axios";
import {getTokenName, messageConfirm, messageTip, removeToken} from "../util/util.js";
import {ElMessage, ElMessageBox} from "element-plus";

//定义后端接口的前缀
axios.defaults.baseURL="http://localhost:8089"


export function doGet(url,params) {
    return axios({
        method:"get",
        url:url,
        params:params,
        dataType:"json"
    })
}

export function doPost(url,data) {
    return axios({
        method:"post",
        url:url,
        data:data,
        dataType:"json"
    })
}

export function doPut(url,data) {
    return axios({
        method:"put",
        url:url,
        data:data,
        dataType:"json"
    })
}

export function doDelete(url,params) {
    return axios({
        method:"delete",
        url:url,
        params:params,
        dataType:"json"
    })
}

//请求拦截器
axios.interceptors.request.use( (config) => {
    // 在发送请求之前做些什么,在请求头中放token(jwt),传给后端接口
    let token = window.sessionStorage.getItem(getTokenName())
    if (!token){ //token不存在
        token = window.localStorage.getItem(getTokenName())
        if (token){
            config.headers['rememberMe'] = true
        }
    }
    if (token){
        config.headers['Authorization'] = token
    }
    return config;
},  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error);
});

//响应拦截器
axios.interceptors.response.use( (response) => {
    //兰姐token的验证的结果，进行相应的提示和页面跳转
    if (response.data.code > 900){ //Token验证未通过
        //前端给用户提示，并进行跳转页面
        messageConfirm(response.data.msg + ",是否重新去登陆")
            .then(() => { //用户点击确认时触发的then函数
                //后端token验证未通过，前端token不正确，删除
                removeToken()
                window.location.href='/'
            })
            .catch(() => {//用户点击取消时触发的then函数
                messageTip("取消去登陆","warning")
            })
        //不返回值解决重复弹窗的问题
        return ;
    }
    return response;
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
});
