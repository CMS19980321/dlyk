import {ElMessage, ElMessageBox} from "element-plus";

/**
 * 消息提示
 * @param msg
 * @param type
 */
export function messageTip(msg,type){
    ElMessage({
        showClose: true,//显示时间，单位为毫秒。 设为 0 则不会自动关闭
        center:true,//文字是否居中
        duration:3000,//显示时间，单位为毫秒。 设为 0 则不会自动关闭
        message: msg,//提示消息文字
        type: type,//消息类型:'success' | 'warning' | 'info' | 'error'
    })
}

/**
 * @return(String)
 */
export function getTokenName(){
    return "cukh token";
}

/**
 * 删除localStorage与sessionStorage存储的Token
 */
export  function removeToken(){
    //alert("删除token函数触发了")
    window.localStorage.removeItem(getTokenName())
    window.sessionStorage.removeItem(getTokenName())
}

export function messageConfirm(msg){
    return ElMessageBox.confirm(
        msg, //提示语
        '系统提示',//提示的标题
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
}

//封装返回函数
export function goback(){
    this.$router.go(-1)
}

//获取token
export function getToken(){
    // 在发送请求之前做些什么,在请求头中放token(jwt),传给后端接口
    let token = window.sessionStorage.getItem(getTokenName())
    if (!token){ //token不存在
        token = window.localStorage.getItem(getTokenName())
    }
    if (token){
        return token;
    } else {
        messageConfirm(response.data.msg + "请求token为空,是否重新去登陆")
            .then(() => { //用户点击确认时触发的then函数
                //后端token验证未通过，前端token不正确，删除
                removeToken()
                window.location.href='/'
            })
            .catch(() => {//用户点击取消时触发的then函数
                messageTip("取消去登陆","warning")
            })
    }
}