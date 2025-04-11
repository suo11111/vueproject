<template>
  <div>
    <div style="display: flex; grid-gap: 10px; margin-bottom: 10px">
      <div class="card" style="padding: 20px; flex: 1; height: 300px" id="bar"></div>
      <div class="card" style="padding: 20px; flex: 1; height: 300px" id="pie"></div>
    </div>
    <div style="display: flex; grid-gap: 10px">
      <div class="card" style="padding: 20px;  flex: 1; height: 300px" id="bar1"></div>
    </div>
  </div>
</template>

<script setup>
import {reactive, onMounted} from "vue";
import * as echarts from 'echarts';
import request from "@/utils/request.js";



const barOption={
  title: {
    text:'供应商供货量'
  },
  tooltip:{},
  legend:{
    data:['数量']
  },
  xAxis:{
    data:[]
  },
  yAxis:{},
  series:[{
    name:'数量',
    type:'bar',
    data:[],
    itemStyle:{
      normal: {
        color: function(params){
          let colors = ['#3191cd','#277b44','#c7d830','#ECC22B','#E87C25','#27727B','#C1232B','#27727B','#B5C334','#ECC22B','#E87C25','#27727B'];
          return colors[params.dataIndex %colors.length]
        }
      }
    }
  }]
}

const barOption1={
  title: {
    text:'商品销量,利润,利润销售额'
  },
  tooltip:{},
  legend:{
    data:['销量','利润','利润销售额']
  },
  xAxis:{
    data:[]
  },
  yAxis:{},
  series:[{
    name:'销量',
    type:'bar',
    barWidth: '20%', // 设置柱状图的宽度为 20%
    barGap: '0%', // 设置柱子之间的间距，使它们紧凑排列
    data:[],
  },
  {
    name:'利润',
    type:'bar',
    barWidth: '20%', // 设置柱状图的宽度为 20%
    barGap: '0%', // 设置柱子之间的间距，使它们紧凑排列
    data:[],
  },
  {
    name:'利润销售额',
    type:'bar',
    barWidth: '20%', // 设置柱状图的宽度为 20%
    barGap: '0%', // 设置柱子之间的间距，使它们紧凑排列
    data:[],

  }],
  dataZoom: [
    {
      type: 'slider',
      xAxisIndex: 0,
      start: 0,
      end: null  // 初始显示前 5/6 个的数据
    }
  ]
}

const pieOption={
  title: {
    text:'供应商供应范围',
    left:'center'
  },
  tooltip:{
    trigger:'item',
  },
  legend:{
    orient:'vertical',
    left:'left'
  },
  series:[{
    name:'供应范围',
    type:'pie',
    radius: '50%',
    data:[],
    label:{
      formatter:'{b}:{d}%'
    },
    emphasis:{
      itemStyle:{
        shadowBlur:10,
        shadowOffsetX:0,
        shadowColor:'rgba(0,0,0,0.5)'
      }
    }

  }]
}

//onMounted 表示页面的所有dom元素都加载完毕了，可以进行操作了
onMounted(()=>{
  //基于准备好的dom，初始化echarts实例
  const barChart = echarts.init(document.getElementById('bar'));
  request.get('/barData').then(res=>{
    //console.log(res.data)
    barOption.xAxis.data=res.data.supplier//横轴数据
    barOption.series[0].data=res.data.count//纵轴数据
    //使用刚指定的配置项和数据显示图表。
    barChart.setOption(barOption);

  })


  const barChart1 = echarts.init(document.getElementById('bar1'));
  request.get('/barData1').then(res=>{
    barOption1.xAxis.data=res.data.productName//横轴数据
    barOption1.dataZoom[0].end = Math.min(100, (5 / res.data.productName.length) * 100 )
    //console.log(barOption1.dataZoom[0].end)
    barOption1.series[0].data=res.data.productInfo.map(item => item.sales)//纵轴数据
    barOption1.series[1].data=res.data.productInfo.map(item => item.margin)//纵轴数据
    barOption1.series[2].data=res.data.productInfo.map(item => item.marginSales)
    //使用刚指定的配置项和数据显示图表。
    barChart1.setOption(barOption1);

  })

  const pieChart = echarts.init(document.getElementById('pie'));
  request.get('/pieData').then(res=>{
    //console.log(res.data)
    pieOption.series[0].data=res.data
    //使用刚指定的配置项和数据显示图表。
    pieChart.setOption(pieOption);

  })


})



</script>

<stle scoped>

</stle>