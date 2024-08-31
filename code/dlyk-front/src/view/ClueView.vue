<script>
import {doDelete, doGet, doPost} from "../http/httpRequest.js";
import {messageConfirm, messageTip} from "../util/util.js";

export default {
  name: "ClueView",
  inject:['reload'],
  data(){
    return{
      //线索列表对象
      clueList:[
        {
          ownerDO:{},
          activityDO:{},
          intentionStateDO:{},
          appellationDO:{},
          needLoanDO:{},
          intentionProductDO:{},
          stateDO:{},
          sourceDO:{},

        },
      ],
      //每页线索数据条数
      pageSize:0,
      //总数据条数
      total:0,
      //弹窗参数true：弹出窗口，否则不弹
      importExcelDialogVisible:false,
    }
  },

  mounted() {
    this.getData(1)
  },

  methods:{
    handleSelectionChange(){

    },

    //查询用户列表数据
    getData(current){
      doGet("/api/clues", {
        current:current
      }).then(resp => {
        //alert("111")
        console.log(resp)
        if (resp.data.code === 200){
          this.clueList = resp.data.data.list
          this.pageSize = resp.data.data.pageSize
          this.total = resp.data.data.total
        }
      })
    },

    //分页函数(其中current当前页不需要手动传递，由ele-plus组件帮我们传
    toPage(current){
      this.getData(current)
    },

    //导入Excel
    importExcel(){
      this.importExcelDialogVisible = true

    },

    //上传文件的请求提交
    uploadFile(param){
      console.log(param)
      console.log(param);
      let fileObj = param.file // 相当于input里取得的files
      let formData = new FormData() // new一个FormData对象
      formData.append('file', fileObj)//文件对象，前面file是参数名，后面fileObj是参数值
      doPost("/api/importExcel", formData).then(resp => {
        if (resp.data.code === 200) {
          //Excel导入成功，提示一下
          messageTip("导入成功", "success");
          //清理一下上传的文件
          this.$refs.uploadRef.clearFiles();
          //页面刷新
          this.reload();
        } else {
          //Excel导入失败
          messageTip("导入失败", "error");
        }
      })
    },

    //提交山传Excel文件
    submitExcel() {
      //引用上传组件,触发uploadFile函数
      this.$refs.uploadRef.submit();
    },

    //录入线索
    addClue(){
      this.$router.push("/dashboard/clue/add")
    },

    //编辑线索
    edit(id){
      this.$router.push("/dashboard/clue/edit/" + id)
    },

    //详情页
    view(id){
      this.$router.push("/dashboard/clue/detail/" + id)
    },

    //删除线索
    del(id){
      //删除前的确认提示
      messageConfirm("您确认要删除这个数据吗")
          .then(() => { //用户点击确认时触发的then函数
            doDelete("/api/clue/" + id , {} ).then(resp => {
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
  }

}
</script>

<template>
  <el-button type="primary" class="btn" @click="addClue" v-hasPermission="'clue:add'">录入线索</el-button>
  <el-button type="success" class="btn" @click="importExcel" v-hasPermission="'clue:import'">导入线索(Excel)</el-button>
  <el-button type="danger" class="btn" @click="batchDelClue" v-hasPermission="'clue:delete'">批量删除</el-button>

  <el-table
      :data="clueList"
      style="width: 100%"
      @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="50"/>
    <el-table-column type="index" label="序号" width="65"/>
    <el-table-column property="ownerDO.name" label="负责人" width="120" />
    <el-table-column property="activityDO.name" label="所属活动"/>
    <el-table-column label="姓名">
      <template #default="scope">
        <a href="javascript:" @click="view(scope.row.id)">{{ scope.row.fullName }}</a>
      </template>
    </el-table-column>
    <el-table-column property="appellationDO.typeValue" label="称呼"/>
    <el-table-column property="phone" label="手机" width="120"/>
    <el-table-column property="weixin" label="微信" width="120"/>
    <el-table-column property="needLoanDO.typeValue" label="是否贷款"/>
    <el-table-column property="intentionStateDO.typeValue" label="意向状态"/>
    <el-table-column property="intentionProductDO.name" label="意向产品"/>

    <el-table-column  label="线索状态">
      <template #default="scope">
        <span style="background: aquamarine" v-if = "scope.row.state === -1">{{ scope.row.stateDO.typeValue }}</span>
        <span  v-else >{{ scope.row.stateDO.typeValue }}</span>
      </template>
    </el-table-column>

    <el-table-column property="sourceDO.typeValue" label="线索来源"/>
    <el-table-column property="nextContactTime" label="下次联系时间" width="165"/>
    <el-table-column label="操作" width="230">
      <template #default="scope">
        <el-button type="primary" @click="view(scope.row.id)" v-hasPermission="'clue:view'">详情</el-button>
        <el-button type="success" @click="edit(scope.row.id)" v-hasPermission="'clue:edit'">编辑</el-button>
        <el-button type="danger" @click="del(scope.row.id)" v-hasPermission="'clue:delete'">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <p>
    <el-pagination
        background
        layout="prev, pager, next"
        :page-size="pageSize"
        :total="total"
        @prev-click="toPage"
        @next-click="toPage"
        @current-change="toPage"/>
  </p>


  <!--活动备注记录弹窗  -->
  <el-dialog v-model="importExcelDialogVisible"  title="导入线索Excel" width="55%" center draggable>
    <el-upload
        ref="uploadRef"
        method="post"
        :http-request="uploadFile"
        :auto-upload="false"
    >
      <template #trigger>
        <el-button type="primary">选择Excel文件</el-button>
      </template>
      仅支持后缀名为.xls和。xlsx的文件

      <template #tip>
        <div class="fileTip">
          重要提示:
          <ul>
            <li>上传仅支持后缀名为.xls或.xlsx的文件；</li>
            <li>给定Excel文件的第一行将视为字段名；</li>
            <li>请确认您的文件大小不超过50MB；</li>
            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式；</li>
            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式；</li>
          </ul>
        </div>
      </template>
    </el-upload>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="importExcelDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="submitExcel">提交</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<style scoped>
.el-table{
  margin-top: 15px;
}
.fileTip{
  padding-top: 30px;
}
</style>