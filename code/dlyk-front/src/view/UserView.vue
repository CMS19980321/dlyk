<script>
import {doDelete, doGet, doPost, doPut} from "../http/httpRequest.js";
import {messageConfirm, messageTip, removeToken} from "../util/util.js";

export default {
  name: "UserView",

  //注入父集页面提供的所有属性·，函数，对象等等
  inject:['reload'],

  data(){
    return{
      userList:[{}],
      //每页线索数据条数
      pageSize:0,
      //总数据条数
      total:0,
      //新增用户弹窗，true为弹出，默认不弹
      userDialogVisible:false,
      //定义添加用户和编辑的表单对象，初始值为空
      userQuery:{},
      //定义用户验证规则
      userRules:{
        loginAct:[
          { required: true, message: '请输入登陆账号', trigger: 'blur' },
        ],
        loginPwd:[
          { required: true, message: '请输入登陆密码', trigger: 'blur' },
          { max: 16, min:6, message: '密码登陆长度为6-16', trigger: 'blur' }
        ],
        name:[
          { required: true, message: '请输入登陆姓名', trigger: 'blur' },
          { pattern:/^[\u4e00-\u9fa5]+$/,message: "姓名必须为中文",trigger: 'blur'}
        ],
        phone:[
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          { pattern : /^1[3-9]\d{9}$/, message: '手机号码格式有误', trigger: 'blur'}
        ],
        email:[
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
          { pattern : /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '邮箱格式有误', trigger: 'blur'}
        ],
        accountNoExpired:[
          { required: true, message: '请选择账号是否未过期', trigger: 'blur' },
        ],
        credentialsNoExpired:[
          { required: true, message: '请选择密码是否未过期', trigger: 'blur' },
        ],
        accountNoLocked:[
          { required: true, message: '请选择账号是否未未锁定', trigger: 'blur' },
        ],
        accountEnabled:[
          { required: true, message: '请选择账号是否可用', trigger: 'blur' },
        ]

      },
      //下拉选项数组
      options:[
        {label:'是',value : 1},
        {label:'否',value : 0},
      ],
      //用户id的数组
      userIdArray:[]

    }
  },

  mounted() {
    this.getData(1)
  },

  methods:{
    //勾选或者取消勾选的时候，触发的函数(已经勾选的的数据会传给我们这个函数)
    handleSelectionChange(selectionDataArray){
      //console.log(selectionDataArray)
      this.userIdArray = [] //每次勾选前清空，防止相同数据
      selectionDataArray.forEach(data =>{
        let userId = data.id
        this.userIdArray.push(userId)
      })

      //alert(this.userIdArray)
    },
    //查询用户列表数据
    getData(current){
      doGet("/api/users", {
        current:current
      }).then(resp => {
        //alert("111")
        console.log(resp)
        if (resp.data.code === 200){
          this.userList = resp.data.data.list
          this.pageSize = resp.data.data.pageSize
          this.total = resp.data.data.total
        }
      })
    },

    //分页函数(其中current当前页不需要手动传递，由ele-plus组件帮我们传
    toPage(current){
      this.getData(current)
    },

    view(id){
      console.log(id)
      //跳转到/dashboard/user/1路由上
      this.$router.push("/dashboard/user/" + id)
    },

    //新增用户
    add(){
      //制空，防止编辑与添加同用一个弹出框的时候相同标题
      this.userQuery = {}
      this.userDialogVisible = true
    },

    userSubmit(){
      //构建表单数据
    let formData = new FormData;
      for (let field in this.userQuery) {
        formData.append(field,this.userQuery[field])
      }

      this.$refs.userRefForm.validate((isValid) => {
        //前端格式验证通过才提交
        if (isValid){

          if (this.userQuery.id > 0 ){ //编辑
            doPut("/api/user",formData).then(resp =>{
              if (resp.data.code === 200){
                messageTip("编辑成功",'success')
                //页面刷新
                this.reload()
              }else {
                messageTip("编辑失败",'error')
              }
            })
          } else {
            doPost("/api/user",formData).then(resp =>{
              if (resp.data.code === 200){
                messageTip("提交成功",'success')
                //页面刷新
                this.reload()
              }else {
                messageTip("提交失败",'error')
              }
            })
          }

        }
      })

    },

    //编辑用户
    edit(id){
      this.userDialogVisible = true
      this.loadUser(id)
    },

    //加载用户信息
    loadUser(id){
      doGet("/api/user/" + id,{}).then(resp => {
        if (resp.data.code === 200){
          this.userQuery = resp.data.data
          this.userQuery.loginPwd = ""
        }
      })
    },

    //删除用户
    del(id){
      //删除前的确认提示
      messageConfirm("您确认要删除这个数据吗")
          .then(() => { //用户点击确认时触发的then函数
            doDelete("/api/user/" + id , {} ).then(resp => {
              console.log(resp)
              if (resp.data.code === 200){
                messageTip("删除成功","success")
                //页面刷新
                this.reload();
              }else {
                messageTip("删除失败,原因:" + resp.data.msg,"error")
              }
            })
          })
          .catch(() => {//用户点击取消时触发的catch函数
            messageTip("取消取消删除","warning")
          })
    },

    //批量删除
    batchDel(){
      if (this.userIdArray.length <= 0){
        messageTip("请选择要删除的数据","warning")
        return
      }

      messageConfirm("您确认要删除选择数据吗").then(() => { //用户点击确认时触发的then函数
            //入参类型转换数组-->字符串，[1,2,3,4,5]-->"1,2,3,4,5"
            let ids = this.userIdArray.join(",")
            doDelete("/api/user",{
              ids:ids
            }).then(resp => {
              //console.log(resp)
              if (resp.data.code === 200){
                messageTip("批量删除成功","success")
                //页面刷新
                this.reload();
              }else {
                messageTip("批量删除失败,原因:" + resp.data.msg,"error")
              }
            })
          })
          .catch(() => {//用户点击取消时触发的catch函数
            messageTip("取消取消删除","warning")
          })


    }
  }
}
</script>

<template>
  <el-button type="primary" @click="add" v-hasPermission="'user:add'">添加用户</el-button>
  <el-button type="danger" @click="batchDel" v-hasPermission="'user:delete'">批量删除</el-button>
  <el-table
      :data="userList"
      style="width: 100%"
      @selection-change="handleSelectionChange">

    <el-table-column type="selection" width="55" />
    <el-table-column type="index" label="序号" width="60" />
    <el-table-column property="loginAct" label="账号" />

    <el-table-column property="name" label="姓名" show-overflow-tooltip />

    <el-table-column property="phone" label="手机" show-overflow-tooltip />
    <el-table-column property="email" label="邮箱" show-overflow-tooltip />
    <el-table-column property="createTime" label="创建时间" show-overflow-tooltip />

    <el-table-column  label="操作" show-overflow-tooltip >
      <template #default="scope">
        <el-button type="primary" size="small" @click="view(scope.row.id)" v-hasPermission="'user:view'">详情</el-button>
        <el-button type="success" size="small" @click="edit(scope.row.id)" v-hasPermission="'user:edit'">编辑</el-button>
        <el-button type="danger" size="small" @click="del(scope.row.id)" v-hasPermission="'user:delete'">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination
      background layout="prev, pager, next"
      :page-size=pageSize
      :total=total
      @prev-click="toPage"
      @next-click="toPage"
      @current-change="toPage"
  />

  <!--新增用户的弹窗(对话框)  -->
  <el-dialog v-model="userDialogVisible" :title="userQuery.id > 0?'编辑用户':'新增用户'" width="55%" center draggable>
    <el-form  ref="userRefForm" :model="userQuery" label-width="110px" :rules="userRules">
      <el-form-item label="账号" prop="loginAct">
        <el-input v-model="userQuery.loginAct"></el-input>
      </el-form-item >

      <el-form-item label="密码" v-if="userQuery.id > 0"><!--编辑-->
        <el-input type="password" v-model="userQuery.loginPwd"></el-input>
      </el-form-item>

      <el-form-item label="密码" prop="loginPwd" v-else> <!--添加-->
        <el-input type="password" v-model="userQuery.loginPwd"></el-input>
      </el-form-item>

      <el-form-item label="姓名" prop="name">
        <el-input  v-model="userQuery.name"></el-input>
      </el-form-item>

      <el-form-item label="手机" prop="phone">
        <el-input  v-model="userQuery.phone"></el-input>
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input  v-model="userQuery.email"></el-input>
      </el-form-item>

      <el-form-item label="账号未过期" prop="accountNoExpired">
        <el-select v-model="userQuery.accountNoExpired" placeholder="请选择" >

          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="密码未过期" prop="credentialsNoExpired">
        <el-select v-model="userQuery.credentialsNoExpired" placeholder="请选择" >
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="账号未锁定" prop="accountNoLocked">
        <el-select v-model="userQuery.accountNoLocked" placeholder="请选择" >
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="账号是否启用" prop="accountEnabled">
        <el-select v-model="userQuery.accountEnabled" placeholder="请选择" >
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>


    <template #footer>
      <div class="dialog-footer">
        <el-button @click="userDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="userSubmit">提交</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.el-table{
  margin-top: 12px;
}
.el-pagination{
  margin-top: 12px;
}
.el-select{
  width: 100%;
}

</style>