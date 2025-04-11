<template>
  <div>
    <!--头部开始-->
    <div style="height: 60px; background-color: #3c7fff; display: flex; align-items: center">
      <div style="width: 240px; display: flex; align-items: center; padding-left: 20px">
        <img style="width: 40px" src="../assets/shelf.png" alt="">
        <span style="font-size: 24px; color: white; margin-left: 5px">超市货架管理系统</span>
      </div>
      <div style="flex: 1"></div>
      <div style="width: fit-content; display: flex; align-items: center; padding-right: 20px">
        <img :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="" style="width: 40px; height: 40px; border-radius: 50%">
        <span style="color: white; margin-left: 5px">{{ data.user.name }}</span>
      </div>
    </div>
    <!--头部结束-->

    <!--下面部分开始-->
    <div style="display: flex">
      <!--左侧导航菜单开始-->
      <div style="width: 200px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
        <el-menu router :default-active="router.currentRoute.value.path" style="border: 0">
          <el-menu-item index="/manage/home">
            <el-icon><House/></el-icon>
            系统首页
          </el-menu-item>
          <el-menu-item index="/manage/products">
            <el-icon><EditPen /></el-icon>
            商品管理
          </el-menu-item>
          <el-menu-item index="/manage/shelves">
            <el-icon><Refrigerator /></el-icon>
            货架管理
          </el-menu-item>
          <el-menu-item index="/manage/supplier">
            <el-icon><Van /></el-icon>
            供应商管理
          </el-menu-item>
          <el-menu-item index="/manage/data">
            <el-icon><Goods/></el-icon>
            数据统计
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><User/></el-icon>
              <span>员工管理</span>
            </template>
            <el-menu-item v-if="data.user.role === 'ADMIN'" index="/manage/admin">店长信息</el-menu-item>
            <el-menu-item index="/manage/employee">理货员信息</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/manage/person">
            <el-icon><UserFilled/></el-icon>
            个人信息
          </el-menu-item >
          <el-menu-item index="/manage/password">
            <el-icon><Lock/></el-icon>
            修改密码
          </el-menu-item >
          <el-menu-item @click="logout">
            <el-icon><SwitchButton/></el-icon>
            退出登陆
          </el-menu-item>
        </el-menu>
      </div>
      <!--左侧导航结束-->
      <!--右侧主体区域开始-->
      <div style="flex: 1;width: 0;background-color: #f7f9fd; padding: 10px">
        <RouterView @updateUser="updateUser" />
      </div>
      <!--右侧主体区域结束-->
     </div>
     <!--下面部分结束-->
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import {reactive} from "vue";

const data=reactive({
  user: JSON.parse(localStorage.getItem("vue-project-user"))
})

const logout=()=>{
  // 清除JWT令牌和用户信息
  localStorage.removeItem('vue-project-token');
  localStorage.removeItem('vue-project-user')
  location.href='/login'
}

const updateUser=()=>{
  data.user=JSON.parse(localStorage.getItem('vue-project-user'))
}
</script>

<style>
.el-menu .is-active{
  background-color: #e6ecf7 !important;
}
.el-sub-menu__title{
  background-color: white !important;
}

</style>



