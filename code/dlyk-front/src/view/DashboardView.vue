<script>


import {doGet} from "../http/httpRequest.js";
import {ArrowDown, CreditCard, Fold} from "@element-plus/icons-vue";
import {messageConfirm, messageTip, removeToken} from "../util/util.js";

export default {
  //定义组件名
  name: "DashboardView",
  components: {CreditCard, Fold, ArrowDown},
  data(){
    return {
      isCollapse:false,
      //登陆用户对象，初始值为空
      user:{},
      //控制仪表板右侧内容体是否显示,默认为true显示
      isRouterAlive:true,
      //当前的访问路由路径
      currentRouterPath:''
    }
  },
  //提供者，生产者
  provide() {
    return {
      //提供一个函数(要求是箭头函数)
      reload: ()=> {
        this.isRouterAlive = false; //右侧内容隐藏
        this.$nextTick(function () {//$nextTick，但数据更新了，在dom中欧给渲染后，自动执行该函数
          this.isRouterAlive = true;
        })
      },
    }

    //在这之中还可以字符串，数字，对象，数组...
  },

  //vue生命周期中国的函数钩子，该钩子在页面渲染后完成
  mounted() {
    this.loadLoginUser();
    this.loadCurrentRouterPath();
  },

  methods:{
    showMenu(){
      this.isCollapse = !this.isCollapse
    },
    //加载当前登录用户
    loadLoginUser(){
      // alert("加载当前用户!!!")
      //此处使用箭头函数，可以被this调用到
      doGet("/api/login/info",{}).then((resp) => {
        //console.log(resp)
        this.user = resp.data.data
      });
    },

    //退出登录
    logout(){
      doGet("/api/logout",{}).then(resp => {
        if (resp.data.code === 200){
          removeToken();
          messageTip("退出成功","success")
          window.location.href='/'
        }else {
          messageConfirm( "退出异常,是否强制退出")
              .then(() => { //用户点击确认时触发的then函数
                //后端token验证未通过，前端token不正确，删除
                removeToken()
                window.location.href='/'
              })
              .catch(() => {//用户点击取消时触发的catch函数
                messageTip("取消强制退出","warning")
              })
        }
      })
    },

    //加载当前路径的路由
    loadCurrentRouterPath(){
      let  path = this.$route.path
      console.log(path)
      let arr = path.split("/")
      if (arr.length > 3){  // 如:/dashboard/activity/add 路径超过两层，取前两层
        this.currentRouterPath = "/" + arr[1] + "/" + arr[2]
      }else {
        this.currentRouterPath = path
      }
    },
  }

}
</script>

<template>
  <el-container>
    <!--    左侧-->
    <el-aside v-bind:width="isCollapse?'64px':'200px'">
      <div class="menuTitle" @click="dashboard">@城院客户管理系统</div>
      <el-menu
          active-text-color="#ffd04b"
          background-color="#334157"
          class="el-menu-vertical-demo"
          :default-active="currentRouterPath"
          text-color="#fff"
          :unique-opened="true"
          :collapse="isCollapse"
          :collapse-transition="false"
          :router="true"
          style="border-right: 0px solid ;"
      >
        <!--  市场活动菜单      -->
        <el-sub-menu :index="index" v-for="(menuPermission , index) in user.mneuPermissionList" :key="menuPermission.id"> <!--一级菜单-->
          <template #title>
            <!--动态展示图标-->
            <el-icon><component :is="menuPermission.icon"></component></el-icon>
            <span>{{menuPermission.name}}</span>

          </template>
          <el-menu-item v-for="subPermission in menuPermission.subPermissionList" :key="subPermission.id" :index="subPermission.url"><!--二级菜单-->
            <el-icon><component :is="subPermission.icon"></component></el-icon>

            {{subPermission.name}}
          </el-menu-item>

        </el-sub-menu>


      </el-menu>
    </el-aside>
    <el-container class="rightContent">
      <!--      右上侧-->
      <el-header>
        <el-icon class="show" @click="showMenu()">
          <Fold />
        </el-icon>
        <el-dropdown :hide-on-click="false">
          <span class="el-dropdown-link">
            {{user.name}}
            <el-icon class="el-icon--right"><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>我的资料</el-dropdown-item>
              <el-dropdown-item>修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <!--      右中侧-->
      <el-main v-if="isRouterAlive">
        <router-view/>
      </el-main>
      <!--      右下侧-->
      <el-footer>@版权所有 2009-2099 湖南城市学院 湖南省益阳市迎宾东路518号</el-footer>
    </el-container>
  </el-container>
</template>

<style scoped>
.el-aside{
  background: #1a1a1a;
}
.el-header{
  background: azure;
  line-height: 35px;
  height: 35px;
}
.el-footer{
  background: aliceblue;
  /*上下居中技巧*/
  height: 35px;
  line-height: 35px;
  text-align: center;
}
.rightContent{
  height: calc(100vh);
}
.menuTitle{
  height: 35px;
  line-height: 35px;
  color: #f9f9f9;
  text-align: center;
}
.show{
  cursor: pointer;
}
.el-dropdown{
  float: right;
  line-height: 35px;
}
</style>