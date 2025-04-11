<template>
  <div class="welcome-container">
    <div class="welcome-card">
      <span class="welcome-text">欢 迎 来 到 幸 福 百 家 超 市 ！</span>
    </div>
  </div>
  <div >
    <el-carousel height="350px" style="width: 70%; margin: 0 auto">
      <el-carousel-item v-for="item in data.imgs" :key="item">
        <img style="width: 100%; height:100%" :src="item" alt="">
      </el-carousel-item>
    </el-carousel>
  </div>

</template>


<script setup>

import {reactive} from "vue";
import img_0 from '@/assets/img_0.png'
import img_1 from '@/assets/img_1.png'
import img_2 from '@/assets/img_2.png'

const data=reactive({
  imgs:[img_0, img_1, img_2]
})


</script>

<style scoped>


.el-carousel-item {
  color: #475669;
  opacity: 100%;
  line-height: 150px;
  margin: 0;
}

.welcome-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 150px; /* 根据需要调整高度 */
  background: linear-gradient(135deg, #9bc0fa, #3975ea); /* 渐变背景 */
  margin-bottom: 20px;
}

.welcome-card {
  background: rgba(255, 255, 255, 0.8); /* 半透明背景 */
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

.welcome-text {
  font-size: 36px; /* 大字体 */
  font-weight: bold;
  color: #333; /* 深色字体 */
  text-align: center;
}


</style>


