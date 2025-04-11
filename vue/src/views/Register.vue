<template>
  <div class="login-container">
    <div class="login-box">
      <div style="padding: 40px 30px; background-color: white; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0.1)">
        <el-form ref="formRef" :rules="data.rules" :model="data.form" style="width: 350px">
          <div style="margin-bottom: 30px; font-size: 25px;text-align: center; color: #3c7fff; font-weight: bold">欢迎注册会员！</div>
          <el-form-item prop="username">
            <el-input size="large" v-model="data.form.username" autocomplete="off" placeholder="请输入账号" prefix-icon="User"/>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password size="large" v-model="data.form.password" autocomplete="off" placeholder="请输入密码" prefix-icon="Lock"/>
          </el-form-item>
          <el-form-item prop="conformPassword">
            <el-input show-password size="large" v-model="data.form.conformPassword" autocomplete="off" placeholder="请确认密码" prefix-icon="Lock"/>
          </el-form-item>
          <div style="margin-bottom: 20px">
            <el-button @click="register" size="large" type="primary" style="width: 100%">注 册</el-button>
          </div>
          <div style="text-align: right">已有账号？请<a style="color: #0066bc; text-decoration: none" href="/login">登陆</a></div>
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

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if(value !== data.form.password){
    callback(new Error('两次密码不一致'))
  } else {
    callback()
  }
}
const data=reactive({
  form:{},
  rules:{
    username:[
      {required:true,message:'请输入账号',trigger:'blur'}
    ],
    password:[
      {required:true,message:'请输入密码',trigger:'blur'}
    ],
    conformPassword:[
      {validator: validatePass, trigger:'blur'}]
  }
})
const formRef=ref()


const register=()=>{
  formRef.value.validate((valid)=>{
    if(valid){
      request.post('/register',data.form).then(res=>{
        if(res.code==='200'){
          ElMessage.success('注册成功')
          setTimeout(()=>{
            location.href='/login'
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