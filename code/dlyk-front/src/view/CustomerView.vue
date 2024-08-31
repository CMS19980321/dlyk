<script>
import {doGet} from "../http/httpRequest.js";
import axios from "axios";
import {getToken, messageTip} from "../util/util.js";

export default {
  name: "CustomerView",
  data(){
    return{
      //客户数组列表对象，初始值为空
      customerList:[{
        clueDO : {},
        ownerDO : {},
        activityDO : {},
        appellationDO : {},
        needLoanDO : {},
        intentionStateDO : {},
        stateDO : {},
        sourceDO : {},
        intentionProductDO : {},
      }],

      //分页时每页显示多少条数据
      pageSize : 0,
      //总共有多少条
      total : 0,
      //客户的id数组
      customerIdArray:[],

    }
  },

  mounted() {
    this.getData(1);
  },

  methods:{
    //勾选或者取消勾选的时候，触发的函数(已经勾选的的数据会传给我们这个函数)
    handleSelectionChange(selectionDataArray){
      //console.log(selectionDataArray)
      this.customerIdArray = [] //每次勾选前清空，防止相同数据
      selectionDataArray.forEach(data =>{
        let customerId = data.id
        this.customerIdArray.push(customerId)
      })

      //alert(this.userIdArray)
    },

    //获取线索分页列表数据
    getData(current) {
      doGet("/api/customers", {
        current : current //当前页，前面是参数名，后面是参数值
      }).then(resp => {
        if (resp.data.code === 200) {
          console.log(resp.data.data.list)
          this.customerList = resp.data.data.list; // resp.data  =  R 对象
          this.pageSize = resp.data.data.pageSize;
          this.total = resp.data.data.total;
        }
      })
    },

    //分页函数
    page(number) { //number值是element-plus回调时传给我们的，number这个值就是当前页
      this.getData(number);
    },

    exportExcel(ids)  {
      //1.向后端发送一个请求(我们来实现)
      let frame = document.createElement("iframe");
      let token = getToken();
      //2.后端查询数据库数据，把数据写入Excel，然后把Excel数据以IO流的方式输出道前端浏览器
      if (ids){
        frame.src = axios.defaults.baseURL + "/api/exportExcel?Authorization=" + token + "&ids=" + ids
      }else {
        frame.src = axios.defaults.baseURL + "/api/exportExcel?Authorization=" + token
      }

      frame.style.display = "none" //iframe隐藏，不再显示
      document.body.appendChild(frame)
      //3.前端弹出一个浏览器进行下载(浏览器本身实现的，不需要我们去实现)
    },

    //批量导出
    batchExportExcel(){
      this.exportExcel(null);
    },

    //选择导出
    chooseExportExcel(){
      if (this.customerIdArray.length <= 0 ){
        messageTip("请选择要导出的数据", "warning")
        return
      }
      //ids = 1,2,3,4,
      let ids = this.customerIdArray.join(",")
      this.exportExcel(ids)
    }
  },
}
</script>

<template>
  <el-button type="primary" class="btn" @click="batchExportExcel">批量导出(Excel)</el-button>
  <el-button type="success" class="btn" @click="chooseExportExcel">选择导出(Excel)</el-button>

  <el-table
      :data="customerList"
      style="width: 100%"
      @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="50"/>
    <el-table-column type="index" label="序号" width="65"/>
    <el-table-column property="ownerDO.name" label="负责人" width="120" />
    <el-table-column property="activityDO.name" label="所属活动"/>
    <el-table-column label="姓名">
      <template #default="scope">
        <a href="javascript:" @click="view(scope.row.id)">{{ scope.row.clueDO.fullName }}</a>
      </template>
    </el-table-column>
    <el-table-column property="appellationDO.typeValue" label="称呼"/>
    <el-table-column property="clueDO.phone" label="手机" width="120"/>
    <el-table-column property="clueDO.weixin" label="微信" width="120"/>
    <el-table-column property="needLoanDO.typeValue" label="是否贷款"/>
    <el-table-column property="intentionStateDO.typeValue" label="意向状态"/>
    <el-table-column property="stateDO.typeValue" label="线索状态"/>
    <el-table-column property="sourceDO.typeValue" label="线索来源"/>
    <el-table-column property="intentionProductDO.name" label="意向产品"/>
    <el-table-column property="nextContactTime" label="下次联系时间" width="165"/>
    <el-table-column label="操作" width="85">
      <template #default="scope">
        <el-button type="primary" @click="view(scope.row.id)">详情</el-button>
      </template>
    </el-table-column>
  </el-table>
  <p>
    <el-pagination
        background
        layout="prev, pager, next"
        :page-size="pageSize"
        :total="total"
        @prev-click="page"
        @next-click="page"
        @current-change="page"/>
  </p>

</template>

<style scoped>
.el-table{
  margin-top: 10px;
}
</style>