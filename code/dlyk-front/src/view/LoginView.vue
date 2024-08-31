<script>
import {doGet, doPost} from "../http/httpRequest.js";
import {getTokenName, messageTip, removeToken} from "../util/util.js";


export default {
  /*组件名字*/
  name: "LoginView",
  //定义页面使用的变量，定义时需要给初始值赋空值
  data(){
    return {
      //对象变量定义，使用{}
      user:{},
      //字符串变量定义,使用‘’
      name:'',
      //数字变量定义，使用0
      age:0,
      //数组变量定义，使用[]
      arr:[],
      //list结合定义,使用[{}]
      userlist:[{}],
      //定义登陆表单的验证规则
      loginRules:{
        loginAct:[
          { required: true, message: '请输入登陆账号', trigger: 'blur' },
        ],
        loginPwd:[
          { required: true, message: '请输入登陆密码', trigger: 'blur' },
          { max: 16, min:6, message: '密码登陆长度为6-16', trigger: 'blur' }
        ],
      }

    }
  },
  //页面渲染的时候触发调用该函数(函数钩子)
  mounted() {
    this.freeLogin()
  },

  //页面上需要使用功能的js函数。都需要在methods属性中定义
  methods:{
    //登陆函数
    login(){
      //提交前验证输入框的合法性
      this.$refs.loginRefForm.validate((isValid) =>{
        //isValid时验证后的结果，如果是true的话表示验证通过，都在未通过
        if(isValid){
          let formData = new FormData;
          formData.append("loginAct",this.user.loginAct)
          formData.append("loginPwd",this.user.loginPwd)
          formData.append("rememberMe",this.user.rememberMe)

          //验证通过，可以提交登陆请求,未提交无需处理
          doPost("/api/login",formData).then((resp) =>{
            console.log(resp)
            if (resp.data.code === 200){
              //登录成功
              messageTip("登录成功","success")
              //删除localStorage与sessionStorage存储的Token
              removeToken()
              //前端存储jwt
              if (this.user.rememberMe === true){
                window.localStorage.setItem(getTokenName(),resp.data.data)
              } else {
                window.sessionStorage.setItem(getTokenName(),resp.data.data)
              }
              //跳转到系统的主页面
              window.location.href="/dashboard";

            } else {
              //登录失败
              messageTip("登录失败","error")
            }
          });
        }
          }
      )
    },

    //免登陆
    freeLogin(){
      let  token = window.localStorage.getItem(getTokenName())
      if (token){//token存在值，不为空
        doGet("/api/login/free",{}).then(resp => {
          if (resp.data.code === 200){
            //token验证通过,免登陆
            window.location.href = "/dashboard"
          }
        })

      }
    }
  }
}
</script>

<template>
  <el-container>
    <!--    左侧-->
    <el-aside width="200px">
      <img src="../assets/loginBox.svg">
      <P class="imgTitle">
        欢迎使用动力云客系统
      </P>
    </el-aside>

    <el-main>
      <div class="loginTile">欢迎登陆</div>
      <!--    右侧-->
      <el-form  ref="loginRefForm" :model="user" label-width="120px" :rules="loginRules">
        <el-form-item label="账号" prop="loginAct">
          <el-input v-model="user.loginAct"></el-input>
        </el-form-item >
        <el-form-item label="密码" prop="loginPwd">
          <el-input type="password" v-model="user.loginPwd"></el-input>
        </el-form-item>

        <el-form-item >
          <el-button type="primary" @click="login">登陆</el-button>
        </el-form-item>

        <el-form-item >
          <el-checkbox label="记住我" v-model="user.rememberMe"></el-checkbox>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<style scoped>
.el-aside{
  background: #1a1a1a;
  width: 40%;
  text-align: center;
}
.el-main{
  height: calc(100vh);
}
img{
  height: 413px;
}
.imgTitle{
  color: #f9f9f9;
  font-size: 28px;
}

.el-form{
  width: 60%;
  margin: auto;
}

.loginTile{
  text-align: center;
  margin-top: 100px;
  margin-bottom: 15px;
  font-weight: bold;
}

.el-button{
  width: 100%;
}
</style>