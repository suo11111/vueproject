<template>
<div>
  <div class="card" style="margin-bottom: 5px">
  <el-input style="width: 240px; margin-right: 10px" v-model="data.Search.name" placeholder="请输入位置查询" prefix-icon="Search"></el-input>
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
      <el-table-column label="编号" width="80px" prop="id" />
      <el-table-column label="名字" width="80px" prop="name" />
      <el-table-column label="联系方式" prop="contact" />
      <el-table-column label="操作" width="120px">
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
  <el-dialog title="供应商信息" v-model="data.formVisible" width="500" destroy-on-close>
    <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right: 40px; padding-top: 20px">
      <el-form-item label="名字" prop="name">
        <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名字" />
      </el-form-item>
      <el-form-item label="联系方式" prop="contact">
        <el-input v-model="data.form.contact" autocomplete="off" placeholder="请输入联系方式" />
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
      {required: true, message: '请输入名字', trigger: 'blur'},
    ],
    contact: [
      {required: true, message: '请选择联系方式', trigger: 'blur'},
    ]
  }

})
const formRef=ref()//表单验证
const load = ()=>{
  request.get("/supplier/selectPage",{
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
  request.post("/supplier/add",data.form).then(res=>{//新增没有id
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
  request.put("/supplier/update",data.form).then(res=>{//修改有id
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
  //console.log(row)
  data.formVisible = true
  data.form = JSON.parse(JSON.stringify(row))//深拷贝
}
const handleDelete=(id)=>{
  ElMessageBox.confirm('确定删除该数据吗？', '删除确认', {type: 'warning'}).then(() => {
    request.delete("/supplier/deleteById/"+id).then(res=>{
      if(res.code === '200'){
        ElMessage.success('操作成功')
        load()//重新加载数据
      } else {
        ElMessage.error(res.msg)
      }
      console.log(id)
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
    request.delete("/supplier/deleteBatch",{data: data.ids}).then(res => {
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