<template>
  <div class="login-container">
    <div class="login-box">
      <div style="padding: 40px 30px; background-color: white; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0.1)">
        <el-form ref="formRef" :rules="data.rules" :model="data.form" style="width: 350px">
          <div style="margin-bottom: 30px; font-size: 25px;text-align: center; color: #3c7fff; font-weight: bold">欢迎来到幸福百家超市！</div>
          <el-form-item prop="username">
            <el-input size="large" v-model="data.form.username" autocomplete="off" placeholder="请输入账号" prefix-icon="User"/>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password size="large" v-model="data.form.password" autocomplete="off" placeholder="请输入密码" prefix-icon="Lock"/>
          </el-form-item>
          <el-form-item prop="role">
           <el-select v-model="data.form.role" placeholder="请选择角色" style="width: 100%" size="large">
            <el-option label="店长" value="ADMIN"></el-option>
            <el-option label="理货员" value="EMP"></el-option>
           </el-select>
          </el-form-item>
          <div style="margin-bottom: 20px">
            <el-button @click="login" size="large" type="primary" style="width: 100%">登录</el-button>
          </div>
          <div style="text-align: right">还没有账号？请<a style="color: #0066bc; text-decoration: none" href="/register">注册</a></div>
        </el-form>
      </div>

    </div>
  </div>
</template>


<script setup>
import {reactive, ref} from "vue";
import {User,Lock} from "@element-plus/icons-vue"
import {ElMessage} from "element-plus";
import request from "../utils/request.js";
const data=reactive({
  form:{ role: 'ADMIN'},
  rules:{
    username:[
      {required:true,message:'请输入账号',trigger:'blur'}
    ],
    password:[
      {required:true,message:'请输入密码',trigger:'blur'}
    ]
  }
})
const formRef=ref()
const login=()=>{
  formRef.value.validate((valid)=>{
    if(valid){
      request.post('/login',data.form).then(res=>{
        console.log(res)
        if(res.code==='200'){
          // 存储JWT令牌
          localStorage.setItem('vue-project-token', res.data.token);
          //存储后台返回的用户数据信息
          localStorage.setItem('vue-project-user',JSON.stringify(res.data))
          ElMessage.success('登录成功')
          setTimeout(()=>{
            location.href='/manage/home'
          },500)
        }else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  overflow: hidden;
  background-image: url("../assets/bg.png");
  background-size: cover;

}
.login-box{
  position: absolute;
  width: 65%;
  height: 100%;
  display: flex;
  align-items: center;
  right: 0;
}
</style>