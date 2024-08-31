<script>
import {doGet} from "../http/httpRequest.js";

export default {
  name: "ActivityView",
  data(){
    return{
      //市场活动搜索表单对象，初始值是空
      activityQuery:{},
      //市场活动列表对象，初始值为空
      activityList:[{
        ownerDO:{} //user对象中存在DO对象
      }],
      //分页数据，每页显示条数
      pageSize:0,
      //分页数据，总条数
      total:0,
      //负责人下拉列表数据
      ownerOptions:[{}],
      //定义市场活动搜索表单验证规则
      activityRules:{
        //验证活动预算字段
        cost : [
          //正则表达式，从网上找，或者AI工具找，找到后需要测试一下，因为有可能找到的正则有问题
          { pattern : /^[0-9]+(\.[0-9]{2})?$/, message: '活动预算必须是整数或者两位小数', trigger: 'blur'}
        ]
      },

    }
  },
  mounted() {
    this.getData(1)
  },
  methods:{
    getData(current){
      //console.log(this.activityQuery.activityTime)
      //时间类型打印
      //console.log(typeof  this.activityQuery.activityTime)

      let startTime = '' // 开始时间
      let endTime = '' // 结束时间
      for(let key in this.activityQuery.activityTime){
        if (key === '0'){
          startTime = this.activityQuery.activityTime[key]
        }

        if (key === '1'){
          endTime = this.activityQuery.activityTime[key]
        }
      }

      doGet("/api/activities", {
        current:current,
        ownerId:this.activityQuery.ownerId,
        name:this.activityQuery.name,
        startTime:startTime,
        endTime:endTime,
        cost:this.activityQuery.cost,
        createTime:this.activityQuery.createTime
      }).then(resp => {
        //alert("111")
        console.log(resp)
        if (resp.data.code === 200){
          this.activityList = resp.data.data.list
          this.pageSize = resp.data.data.pageSize
          this.total = resp.data.data.total
        }
      })
    },

    //分页函数(其中current当前页不需要手动传递，由ele-plus组件帮我们传
    toPage(current){
      this.getData(current)
    },

    //负责人查询
    loadOwner(){
      doGet("api/owner",{}).then(resp => {
        if (resp.data.code === 200){
          this.ownerOptions = resp.data.data
        }
      })

    },

    //搜索
    onSearch(){
      this.getData(1)
    },

    //搜索条件重置
    onReset(){
      this.activityQuery = {}
    },

    //录入市场活动
    add(){
      this.$router.push("/dashboard/activity/add")
    },

    //编辑市场活动
    edit(id){
      this.$router.push("/dashboard/activity/edit/" + id)
    },

    //查看详情
    view(id){
      this.$router.push("/dashboard/activity/" + id)
    }



  }
}
</script>

<template>
  <el-form :inline="true" :model="activityQuery" class="demo-form-inline" :rules="activityRules">

    <el-form-item label="负责人">
      <el-select
          v-model="activityQuery.ownerId"
          placeholder="请选择负责人"
          @click="loadOwner"
          clearable>
        <el-option
            v-for="item in ownerOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
        />
      </el-select>
    </el-form-item>


    <el-form-item label="活动名称" >
      <el-input v-model="activityQuery.name" placeholder="请输入活动名称" clearable />
    </el-form-item>


    <el-form-item label="活动时间">
      <el-date-picker
          v-model="activityQuery.activityTime"
          type="datetimerange"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="YYYY-MM-DD HH:mm:ss"
      />
    </el-form-item>

    <el-form-item label="活动预算" prop="cost">
      <el-input v-model="activityQuery.cost" placeholder="请输入活动预算" clearable />
    </el-form-item>

    <el-form-item label="创建时间">
      <el-date-picker
          v-model="activityQuery.createTime"
          type="datetime"
          placeholder="创建时间"
          value-format="YYYY-MM-DD HH:mm:ss"
      />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="onSearch">搜 索</el-button>
      <el-button type="primary" plain @click="onReset">重置</el-button>
    </el-form-item>
  </el-form>

  <el-button type="primary" @click="add">录入市场活动</el-button>
  <el-button type="danger" @click="batchDel">批量删除</el-button>
  <el-table
      :data="activityList"
      style="width: 100%"
      @selection-change="handleSelectionChange">

    <el-table-column type="selection" width="55" />
    <el-table-column type="index" label="序号" width="60" />
    <el-table-column property="ownerDO.name" label="负责人" />

    <el-table-column property="name" label="活动名称" show-overflow-tooltip />

    <el-table-column property="startTime" label="开始时间" show-overflow-tooltip />
    <el-table-column property="endTime" label="结束时间" show-overflow-tooltip />
    <el-table-column property="cost" label="活动预算" show-overflow-tooltip />
    <el-table-column property="createTime" label="创建时间" show-overflow-tooltip />

    <el-table-column  label="操作" show-overflow-tooltip width="230">
      <template #default="scope">
        <el-button type="primary" size="small" @click="view(scope.row.id)">详情</el-button>
        <el-button type="success" size="small" @click="edit(scope.row.id)">编辑</el-button>
        <el-button type="danger" size="small" @click="del(scope.row.id)">删除</el-button>
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
</template>

<style scoped>
.el-form{
  margin-bottom: 20px;
}

.el-table{
  margin-top: 12px;
}
.el-pagination{
  margin-top: 12px;
}

.demo-form-inline .el-select {
  --el-select-width: 220px;
}
</style>