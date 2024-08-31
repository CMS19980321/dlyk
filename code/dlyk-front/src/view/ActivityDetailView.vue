<script>
import {doDelete, doGet, doPost, doPut} from "../http/httpRequest.js";
import {goback, messageConfirm, messageTip} from "../util/util.js";

export default {
  name: "ActivityDetailView",
  inject:['reload'],
  data(){
    return {
      activityDetail:{
        ownerDO:{},
        createByDO:{},
        editByDO:{},

      },
      //提交活动的验证规则
      activityRemarkRules:{
        noteContent:[
          { required: true, message: '请输入登陆账号', trigger: 'blur' },
          { max: 255, min:5, message: '活动备注长度为5-255个字符', trigger: 'blur' }
        ],
      },
      //编辑活动的验证规则
      editActivityRemarkRules:{
        noteContent:[
          { required: true, message: '请输入登陆账号', trigger: 'blur' },
          { max: 255, min:5, message: '活动备注长度为5-255个字符', trigger: 'blur' }
        ],
      },
      //
      activityRules:{},
      //市场活动备注对象，初始值为空
      activityRemarkQuery:{},
      editActivityRemarkQuery:{},
      //活动备注的列表对象，初始值为空
      activityRemarkList:[
        {
          createByDO:{},
          editByDO:{},
        }
      ],
      pageSize:0,
      total:0,
      //编辑活动注解的弹窗，true弹窗，false不弹窗
      activityRemarkDialogVisible:false,
    }
  },
  mounted() {
    this.loadActivityDetail();
    this.loadActivityRemarkList(1)
  },
  methods:{
    //导入函数声明一下才能使用(自动的)
    goback,
    //加载市场活动详情
    loadActivityDetail(){
      //从路由获取id
      let id = this.$route.params.id;
      doGet("api/activity/" + id,{}).then(resp => {
        if (resp.data.code === 200){
          this.activityDetail = resp.data.data;
          if (!this.activityDetail.createByDO){
            this.activityDetail.createByDO = {}
          }
          if (!this.activityDetail.editByDO){
            this.activityDetail.editByDO = {}
          }
          if (!this.activityDetail.ownerDO){
            this.activityDetail.ownerDO = {}
          }
        }
      })
    },

    //提交活动备注
    activityRemarkSubmit(){
      this.$refs.activityRemarkRefForm.validate((isValid) => {
        if (isValid){
          doPost("/api/activity/remark",{
            activityId:this.activityDetail.id,
            noteContent:this.activityRemarkQuery.noteContent,
          }).then(resp => {
            if (resp.data.code === 200){
              messageTip("提交成功",'success')
              //页面刷新
              this.reload()
            }else {
              messageTip("提交失败",'error')
            }
          })
        }
      })
    },

    //查询活动备注列表数据
    loadActivityRemarkList(current){
      doGet("/api/activity/remark", {
        current:current,
        activityId: this.$route.params.id
      }).then(resp => {
        //alert("111")
        console.log(resp.data.data)
        if (resp.data.code === 200){
          this.activityRemarkList = resp.data.data.list
          this.pageSize = resp.data.data.pageSize
          this.total = resp.data.data.total
        }
      })
    },

    //分页函数(其中current当前页不需要手动传递，由ele-plus组件帮我们传
    toPage(current){
      this.loadActivityRemarkList(current)
    },

    //编辑活动备注
    edit(id){
      this.activityRemarkDialogVisible = true
      doGet("/api/activity/remark/" + id,{}).then(resp => {
        if (resp.data.code ===200){

          this.editActivityRemarkQuery = resp.data.data
        }
      })
    },

    //编辑活动备注
    editActivityRemarkSubmit(){
      this.$refs.editActivityRemarkRefForm.validate((isValid) => {
        if (isValid){
          doPut("/api/activity/remark",{
            id:this.editActivityRemarkQuery.id,
            noteContent:this.editActivityRemarkQuery.noteContent,
          }).then(resp => {
            if (resp.data.code === 200){
              messageTip("编辑成功",'success')
              //页面刷新
              this.reload()
            }else {
              messageTip("编辑失败",'error')
            }
          })
        }
      })
    },
    //删除活动备注
    del(id){
      //删除前的确认提示
      messageConfirm("您确认要删除这个数据吗")
          .then(() => { //用户点击确认时触发的then函数
            doDelete("/api/activity/remark/" + id , {} ).then(resp => {
              //console.log(resp)
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
            messageTip("取消删除","warning")
          })
    }
  }
}
</script>

<template>
  <el-form ref="activityRemarkRefForm" :model="activityRemarkQuery" label-width="120px" :rules="activityRemarkRules">
    <el-form-item label="ID">
      <div class="desc">{{activityDetail.id}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="负责人">
      <div class="desc"> {{activityDetail.ownerDO.name}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="活动名称">
      <div class="desc">{{activityDetail.name}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="开始时间">
      <div class="desc">{{activityDetail.startDate}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="结束时间">
      <div class="desc">{{activityDetail.endDate}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="活动预算">
      <div class="desc">{{activityDetail.cost}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="活动描述">
      <div class="desc">{{activityDetail.description}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="创建时间">
      <div class="desc">{{activityDetail.createTime}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="创建人">
      <div class="desc">{{activityDetail.createByDO.name}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="编辑时间">
      <div class="desc">{{activityDetail.editTime}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="编辑人">
      <div class="desc">{{activityDetail.editByDO.name}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="填写备注" prop="noteContent">
      <el-input
          v-model="activityRemarkQuery.noteContent"
          :rows="8"
          type="textarea"
          placeholder="请输入活动备注"
      />
    </el-form-item>

    <el-form-item >
      <el-button type="primary" @click="activityRemarkSubmit">提交</el-button>
      <el-button @click="goback">返回</el-button>
    </el-form-item>

  </el-form>

  <el-table
      :data="activityRemarkList"
      style="width: 100%"
      >

    <el-table-column type="index" label="序号" width="60" />
    <el-table-column property="noteContent" label="备注内容" />
    <el-table-column property="createTime" label="备注时间"  />
    <el-table-column property="createByDO.name" label="备注人"  />
    <el-table-column property="editTime" label="编辑时间"  />
    <el-table-column property="editByDO.name" label="编辑人"  />
    <el-table-column  label="操作" show-overflow-tooltip >
      <template #default="scope">
        <a href="javascript:" @click="edit(scope.row.id)">编辑</a>
        &nbsp;
        <a href="javascript:" @click="del(scope.row.id)">删除</a>
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

  <!--活动备注记录弹窗  -->
  <el-dialog v-model="activityRemarkDialogVisible"  title="编辑市场活动备注记录" width="55%" center draggable>
    <el-form  ref="editActivityRemarkRefForm" :model="editActivityRemarkQuery" label-width="110px" :rules="editActivityRemarkRules">
      <el-form-item label="活动备注" prop="noteContent">
        <el-input
            v-model="editActivityRemarkQuery.noteContent"
            :rows="8"
            type="textarea"
            placeholder="请输入活动备注"
        />
      </el-form-item>

    </el-form>


    <template #footer>
      <div class="dialog-footer">


        <el-button @click="activityRemarkDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="editActivityRemarkSubmit">提交</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.desc{
  background-color: #F0FFFF;
  width: 100%;
  padding-left: 15px;
}
</style>