<script>
import {doGet, doPost} from "../http/httpRequest.js";
import {goback, messageTip} from "../util/util.js";

export default {
  name: "ClueDetailView",

  //注入
  inject:["reload"],

  data(){
    return{
      //线索详情对象，初始值为空
      clueDetail:{
        ownerDO:{},
        activityDO:{},
        intentionStateDO:{},
        appellationDO:{},
        needLoanDO:{},
        intentionProductDO:{},
        stateDO:{},
        sourceDO:{},
      },
      //线索跟踪记录对象，初始值为空
      clueRemark:{},
      //跟踪方式的下拉值选项，初始值为空
      noteWayOptions:[{}],

      //线索跟踪记录列表
      clueRemarkList:[{
        noteWayDO : {},
        createByDO : {},
        editByDO : {},

      }],
      pageSize:0,
      total:0,

      //定义转换客户的弹窗是否弹出来，默认是false不弹出来，true就弹出来
      convertCustomerDialogVisible : false,
      //线索转换为客户的form表单对象，初始值是空
      customerQuery : {},

      //定义线索转换为客户的验证规则
      convertCustomerRules : {
        product : [
          { required: true, message: '请选择意向产品', trigger: ['blur', 'change'] }
        ],
        description : [
          { required: true, message: '客户描述不能为空', trigger: 'blur' },
          { min: 5, max: 255, message: '客户描述长度为5-255个字符', trigger: 'blur' }
        ],
        nextContactTime : [
          { required: true, message: '请选择下次联系时间', trigger: 'blur' }
        ]
      },

      //意向产品的下拉值选项，初始值为空
      productOptions : [{}],
    }
  },

  mounted() {
    this.loadClueDetail();
    this.loadClueRemarkList(1);
  },

  methods:{
    goback,
    loadClueDetail(){
      let id = this.$route.params.id
      doGet("/api/clue/detail/" + id,{}).then(resp =>{
        if (resp.data.code === 200){
          this.clueDetail = resp.data.data
        }
      })
    },

    //提交线索跟踪记录
    clueRemarkSubmit(){
      doPost("/api/clue/remark",{
        clueId:this.clueDetail.id,
        noteContent:this.clueRemark.noteContent,
        noteWay:this.clueRemark.noteWay,
      }).then(resp => {
        if (resp.data.code === 200){
          messageTip("提交成功",'success')
          //页面刷新
          this.reload()
        }else {
          messageTip("提交失败",'error')
        }
      })
    },

    //加载字典数据
    loadDicValue(typeCode) {
      doGet("/api/dicvalue/" + typeCode, {}).then( resp => {
        if (resp.data.code === 200) {
          if (typeCode === 'noteWay') {
            this.noteWayOptions = resp.data.data;
          } else if (typeCode === 'product'){
            this.productOptions = resp.data.data;
          }
        }
      })
    },

    //查询活动备注列表数据
    loadClueRemarkList(current){
      doGet("/api/clue/remark", {
        current:current,
        clueId: this.$route.params.id
      }).then(resp => {
        //alert("111")
        console.log(resp.data.data)
        if (resp.data.code === 200){
          this.clueRemarkList = resp.data.data.list
          this.pageSize = resp.data.data.pageSize
          this.total = resp.data.data.total
        }
      })
    },

    //分页函数(其中current当前页不需要手动传递，由ele-plus组件帮我们传
    toPage(current){
      this.loadClueRemarkList(current)
    },

    //转换客户
    convertCustomer(){
      this.convertCustomerDialogVisible = true
    },

    //线索转换客户
    convertCustomerSubmit(){
      this.$refs.convertCustomerRefForm.validate(isValid => {
        if (isValid){
          //视为添加操作
          doPost("/api/clue/customer",{
            clueId : this.clueDetail.id,
            product : this.customerQuery.product,
            description : this.customerQuery.description,
            nextContactTime : this.customerQuery.nextContactTime,
          }).then(resp => {
            if (resp.data.code === 200){
              if (resp.data.code === 200){
                messageTip("转换成功",'success')
                //页面刷新
                this.reload()
              }else {
                messageTip("转换失败",'error')
              }
            }
          })
        }
      })
    }




  },



}
</script>

<template>
  <el-form
      ref="clueRemarkRefForm"
      :model="clueRemark"
      label-width="110px"
      :rules="clueRemarkRules"
      style="max-width: 95%;">

    <el-form-item label="负责人">
      <div class="desc">{{clueDetail.ownerDO.name}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="所属活动">
      <div class="desc">{{clueDetail.activityDO.name}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="姓名">
      <div class="desc">{{clueDetail.fullName}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="称呼">
      <div class="desc">{{clueDetail.appellationDO.typeValue}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="手机">
      <div class="desc">{{clueDetail.phone}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="微信">
      <div class="desc">{{clueDetail.weixin}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="QQ">
      <div class="desc">{{clueDetail.qq}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="邮箱">
      <div class="desc">{{clueDetail.email}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="年龄">
      <div class="desc">{{clueDetail.age}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="职业">
      <div class="desc">{{clueDetail.job}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="年收入">
      <div class="desc">{{clueDetail.yearIncome}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="住址">
      <div class="desc">{{clueDetail.address}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="贷款">
      <div class="desc">{{clueDetail.needLoanDO.typeValue}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="意向状态">
      <div class="desc">{{clueDetail.intentionStateDO.typeValue}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="意向产品">
      <div class="desc">{{clueDetail.intentionProductDO.name}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="线索状态">
      <div class="desc">{{clueDetail.stateDO.typeValue}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="线索来源">
      <div class="desc">{{clueDetail.sourceDO.typeValue}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="线索描述">
      <div class="desc">{{clueDetail.description}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="下次联系时间">
      <div class="desc">{{clueDetail.nextContactTime}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="填写跟踪记录" prop="noteContent">
      <el-input
          v-model="clueRemark.noteContent"
          :rows="8"
          type="textarea"/>
    </el-form-item>
    <el-form-item label="跟踪方式" prop="noteWay">
      <el-select
          v-model="clueRemark.noteWay"
          placeholder="请选择跟踪方式"
          @click="loadDicValue('noteWay')"
          style="width: 100%"
          clearable>
        <el-option
            v-for="item in noteWayOptions"
            :key="item.id"
            :label="item.typeValue"
            :value="item.id"/>
      </el-select>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="clueRemarkSubmit">提 交</el-button>
      <el-button type="success" @click="convertCustomer" v-if="clueDetail.state !== -1">转换客户</el-button>
      <el-button type="success" plain @click="goback">返 回</el-button>
    </el-form-item>
  </el-form>

  <!--html横线-->
  <hr/>
  <el-table
      :data="clueRemarkList"
      style="width: 100%"
  >

    <el-table-column type="index" label="序号" width="60" />
    <el-table-column property="noteWayDO.typeValue" label="跟踪方式" />
    <el-table-column property="noteContent" label="跟踪内容" />
    <el-table-column property="createTime" label="跟踪时间"  />
    <el-table-column property="createByDO.name" label="跟踪人"  />
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
  <!--线索转换为客户的弹窗（对话框）-->
  <el-dialog v-model="convertCustomerDialogVisible" title="线索转换客户" width="55%" center>
    <el-form ref="convertCustomerRefForm" :model="customerQuery" label-width="110px" :rules="convertCustomerRules">
      <el-form-item label="意向产品" prop="product">
        <el-select v-model="customerQuery.product" placeholder="请选择" style="width: 100% ;" @click="loadDicValue('product')">
          <el-option
              v-for="item in productOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="客户描述" prop="description">
        <el-input
            v-model="customerQuery.description"
            :rows="8"
            type="textarea"
            placeholder="请输入客户描述"/>
      </el-form-item>
      <el-form-item label="下次跟踪时间" prop="nextContactTime">
        <el-date-picker
            v-model="customerQuery.nextContactTime"
            type="datetime"
            style="width: 100%;"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择下次跟踪时间"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="convertCustomerDialogVisible = false">关 闭</el-button>
        <el-button type="primary" @click="convertCustomerSubmit">转 换</el-button>
      </span>
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