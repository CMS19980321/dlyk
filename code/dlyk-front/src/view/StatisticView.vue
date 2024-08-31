<script>
import {doGet} from "../http/httpRequest.js";
import * as echarts from 'echarts';

export default {
  name: "StatisticView",
  data(){
    return {
      //概览统计数据对象
      summaryData:{}
    }
  },

  mounted() {
    this.loadSummary();
    //加载销售漏斗图
    this.loadSaleFunnelChart();
    //加载线索来源饼图
    this.loadSourcePieChart();
  },

  methods:{
    //加载概览数据
    loadSummary(){
      doGet("/api/summary/data",{}).then(resp => {
        if (resp.data.code === 200){
          this.summaryData = resp.data.data
        }
      })
    },


    loadSaleFunnelChart(){
      //先进行数据查询，再进行图的渲染
      doGet("/api/saleFunnel/data",{}).then(resp => {
        if (resp.data.code === 200){
          let saleFunnelData = resp.data.data

          //画图步骤
          //拿到页面上渲染图标的dom元素
          var chartDom = document.getElementById('saleFunnelChart');
          //使用echarts对dom元素进行初始化，得到一个空白的图表对象
          var myChart = echarts.init(chartDom);
          //配置可选项(查看文档-->配置项手册)
          var option = {
            //标题组件，包含主标题和副标题。
            title: {
              //主标题文本，支持使用 \n 换行。
              text: '销售漏斗图',
              left: 'center',
              top: 'bottom',
            },
            //提示框组件
            tooltip: {
              //触发类型：
              //数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
              trigger: 'item',
              //提示框浮层内容格式器，支持字符串模板和回调函数两种形式。
              //漏斗图: {a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
              formatter: '{a} <br/>{b} : {c}'
            },

            //工具栏。内置有导出图片，数据视图，动态类型切换，数据区域缩放，重置五个工具。
            toolbox: {
              //各工具配置项。
              feature: {
                //数据视图工具，可以展现当前图表所用的数据，编辑后可以动态更新
                dataView: { readOnly: false },
                //配置项还原。
                restore: {},
                //保存为图片。
                saveAsImage: {}
              }
            },
            //图例组件。
            legend: {
              data: ['线索', '客户', '交易', '成交']
            },
            //系列
            series: [
              {
                //图表类型。funnel代表漏斗图
                name: '销售漏斗数据名称',
                //系列名称
                type: 'funnel',
                //漏斗图组件离容器左侧的距离。
                left: '10%',
                //漏斗图组件离容器上侧的距离。
                top: 60,
                //漏斗图组件离容器下侧的距离。
                bottom: 60,
                width: '80%',
                min: 0,
                max: 100,
                minSize: '0%',
                maxSize: '100%',
                sort: 'descending',
                gap: 2,
                label: {
                  show: true,
                  position: 'inside'
                },
                labelLine: {
                  length: 10,
                  lineStyle: {
                    width: 1,
                    type: 'solid'
                  }
                },
                itemStyle: {
                  borderColor: '#fff',
                  borderWidth: 1
                },
                emphasis: {
                  label: {
                    fontSize: 20
                  }
                },
                //数据项(系列中的数据内容数组。数组项可以为单个数值,也可以是对象值)
                /*data: [
                  { value: 20, name: '成交' },
                  { value: 60, name: '交易' },
                  { value: 80, name: '客户' },
                  { value: 100, name: '线索' }
                ]*/

                data : saleFunnelData
              }
            ]
          };
          //如果配直了可选项，就把可选项设置道空白的图表对象中
          option && myChart.setOption(option);


        }
      })



    },

    loadSourcePieChart(){

      doGet("/api/sourcePie/data",{}).then(resp => {
        if (resp.data.code === 200){
          let sourcePieData = resp.data.data

          var chartDom = document.getElementById('sourcePieChart');
          var myChart = echarts.init(chartDom);
          var option = {
            title: {
              text: '线索来源统计',
              left: 'center',
              top:'bottom',
            },
            tooltip: {
              trigger: 'item'
            },
            //图例
            legend: {
              orient: 'horizontal',
            },
            series: [
              {
                name: 'Access From',
                type: 'pie',
                //饼图的半径
                radius: '60%',
                /*data: [
                  { value: 1048, name: 'Search Engine' },
                  { value: 735, name: 'Direct' },
                  { value: 580, name: 'Email' },
                  { value: 484, name: 'Union Ads' },
                  { value: 300, name: 'Video Ads' }
                ],*/
                data : sourcePieData,
                emphasis: {
                  //高亮状态的扇区和标签样式。
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          };

          option && myChart.setOption(option);
        }
      })


    },
  }
}
</script>

<template>
  <!--概览统计-->
  <el-row>
    <el-col :span="6">
      <el-statistic :value="summaryData.effectiveActivityCount">
        <template #title>
          <div style="display: inline-flex; align-items: center">
            市场活动
          </div>
        </template>
        <template #suffix>/{{summaryData.totalActivityCount}}</template>
      </el-statistic>
    </el-col>

    <el-col :span="6">
      <el-statistic title="线索总数" :value="summaryData.totalClueCount" />
    </el-col>


    <el-col :span="6">
      <el-statistic title="客户总数" :value="summaryData.totalCustomerCount" />
    </el-col>

    <el-col :span="6">
      <el-statistic :value="summaryData.successTranAmount">
        <template #title>
          <div style="display: inline-flex; align-items: center">
            交易总额
          </div>
        </template>
        <template #suffix>/{{summaryData.totalTranAmount}}</template>
      </el-statistic>
    </el-col>
  </el-row>

  <!-- 销售漏斗图 -->
  <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
  <div id="saleFunnelChart" style="width: 48%; height:350px; margin:10px; float: left;">图渲染在此处</div>

  <!-- 线索来源饼图图 -->
  <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
  <div id="sourcePieChart" style="width: 48%; height:350px; margin:10px; float: left;">图渲染在此处</div>
</template>

<style scoped>
.el-row{
  text-align: center;
}
</style>