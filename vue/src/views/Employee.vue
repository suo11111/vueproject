<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 240px; margin-right: 10px" v-model="data.Search.name" placeholder="请输入名称查询" prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="danger" @click="delBatch">批量删除</el-button>
      <!--    <el-button type="info">导入</el-button>
          <el-button type="success">导出</el-button>  -->
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column label="名称" prop="name" />
        <el-table-column label="性别" prop="sex" />
        <el-table-column label="账号" prop="username" />
        <el-table-column label="个人介绍" prop="description" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="电话" prop="tel" />
        <el-table-column label="头像">
          <template #default="scope">
            <img  v-if="scope.row.avatar" style="display: block; width: 40px; height: 40px; border-radius: 50%" :src="scope.row.avatar" alt=""/>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="primary" :icon="Edit" circle @click="handleUpdate(scope.row)"></el-button>
            <el-button type="danger" :icon="Delete" circle @click="handleDelete(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 15px">
        <el-pagination
            @current-change="load"
            @size-change="load"
            v-model:current-page="data.PageNum"
            v-model:page-size="data.PageSize"
            :page-sizes="[5, 10, 15, 20]"
            background
            layout="total,sizes,prev,pager,next,jumper"
            :total="data.total"
        />
      </div>
    </div>
    <el-dialog title="理货员信息" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right: 40px; padding-top: 20px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="data.form.sex" placeholder="请选择性别">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input :disabled="data.form.id" v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item label="个人介绍" >
          <el-input :rows="3" type="textarea" v-model="data.form.description" autocomplete="off" placeholder="请输入个人介绍"/>
        </el-form-item>
        <el-form-item label="电话" prop="tel">
          <el-input v-model="data.form.tel" autocomplete="off" placeholder="请输入电话"/>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
              action="http://localhost:9090/files/upload"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import {reactive, ref} from "vue";
import {Delete, Edit} from '@element-plus/icons-vue';
import {Search} from "@element-plus/icons-vue"
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const data = reactive({
  PageNum: 1,
  PageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  form:{},//表单数据(add)
  Search: {
    name: ""
  },
  ids: [],
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
const formRef=ref()//表单验证
const load = ()=>{
  request.get("/employee/selectPage",{
    params: {
      pageNum: data.PageNum,
      pageSize: data.PageSize,
      name: data.Search.name
    }
    //请求参数
  }).then(res=>{
    data.tableData = res.data.list
    data.total = res.data.total
    //console.log(res.data)
  })
}
load()

const handleAvatarSuccess = (res, file) => {
  data.form.avatar = res.data
}

const reset = ()=>{
  data.Search.name = ""
  load()
}
const handleAdd = ()=>{
  data.formVisible = true
  data.form={}//清空表单(脏数据)
}
const save = ()=>{//两个操作新增＋编辑
  formRef.value.validate((valid)=>{
    if(valid){
      data.form.id ? update() : add()
    }
  })

}

const add=()=>{
  request.post("/employee/add",data.form).then(res=>{//新增没有id
    if(res.code === '200'){
      data.formVisible = false
      ElMessage.success('操作成功')
      load()//重新加载数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update=()=>{
  request.put("/employee/update",data.form).then(res=>{//修改有id
    if(res.code === '200'){
      data.formVisible = false
      ElMessage.success('操作成功')
      load()//重新加载数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const handleUpdate = (row)=>{
  data.formVisible = true
  data.form = JSON.parse(JSON.stringify(row))//深拷贝
}
const handleDelete=(id)=>{
  ElMessageBox.confirm('确定删除该数据吗？', '删除确认', {type: 'warning'}).then(() => {
    request.delete("/employee/deleteById/"+id).then(res=>{
      if(res.code === '200'){
        ElMessage.success('操作成功')
        load()//重新加载数据
      } else {
        ElMessage.error(res.msg)
      }
      //console.log(id)
    })
  }).catch()
}
const handleSelectionChange = (rows)=>{//返回所有选中的行
                                       //从选中的行中获取id
  data.ids = rows.map(row => row.id)
}
const delBatch = ()=>{
  if(data.ids.length === 0){
    ElMessage.warning('请选择要删除的数据')
    return
  }
  ElMessageBox.confirm('确定删除该数据吗？', '删除确认', {type: 'warning'}).then(() => {
    request.delete("/employee/deleteBatch",{data: data.ids}).then(res => {
      if(res.code === '200'){
        ElMessage.success('操作成功')
        load()//重新加载数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}
</script>