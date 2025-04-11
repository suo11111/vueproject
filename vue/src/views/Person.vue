<template>
  <div class="card" style="width: 50%; padding: 40px 30px">
    <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right: 40px; padding-top: 20px">
      <div style="width: 100%; display: flex; justify-content: center; margin-bottom: 20px">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:9090/files/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="data.form.avatar" :src="data.form.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </div>
      <el-form-item label="名称" prop="name">
        <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称" />
      </el-form-item>
      <el-form-item label="账号" prop="username">
        <el-input disabled v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
      </el-form-item>
      <div v-if="data.user.role === 'EMP'">
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="data.form.sex">
            <el-radio label="男" value="男"></el-radio>
            <el-radio label="女" value="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="个人介绍" >
          <el-input :rows="3" type="textarea" v-model="data.form.description" autocomplete="off" placeholder="请输入个人介绍"/>
        </el-form-item>
        <el-form-item label="电话" prop="tel">
          <el-input v-model="data.form.tel" autocomplete="off" placeholder="请输入电话"/>
        </el-form-item>
      </div>
      <div style="text-align: center">
        <el-button type="primary" style="padding: 20px 30px" @click="updateUser">更新个人信息</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const formRef = ref()
const data = reactive({
  form:{},
  user: JSON.parse(localStorage.getItem("vue-project-user")),
  rules:{
    name: [
      {required: true, message: '请输入名称', trigger: 'blur'},
      {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
    ],
    sex: [
      {required: true, message: '请选择性别', trigger: 'blur'},
    ],
    username: [
      {required: true, message: '请输入账号', trigger: 'blur'},
    ],
    tel: [
      {required: true, message: '请输入电话', trigger: 'blur'},
    ]
  }
})

const handleAvatarSuccess = (res, file) => {
  data.form.avatar = res.data
}


const emit = defineEmits(['updateUser'])


 if(data.user.role === 'EMP'){
   request.get("/employee/selectById/" + data.user.id).then(res=>{
     data.form = res.data
   })
 }else{
   data.form = data.user
 }

 const updateUser=()=>{
   if(data.user.role === 'EMP') {
     request.put("/employee/update", data.form).then(res=>{
       if (res.code === '200') {
         ElMessage.success('更新成功')
         // 更新本地缓存
         localStorage.setItem("vue-project-user", JSON.stringify(data.form))
         // 更新父组件
         emit('updateUser')
       } else {
       ElMessage.error(res.msg)
     }
     })
   } else {
     request.put("/admin/update", data.form).then(res=> {
       if (res.code === '200') {
         ElMessage.success('更新成功')
         // 更新本地缓存
         localStorage.setItem("vue-project-user", JSON.stringify(data.form))
         // 更新父组件
         emit('updateUser')
       } else {
         ElMessage.error(res.msg)
       }
     })
   }
 }
</script>

<style>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>