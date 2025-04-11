<template>
<div>
  <div class="card" style="margin-bottom: 5px">
  <el-input style="width: 240px; margin-right: 10px" v-model="data.Search.productName" placeholder="请输入名称查询" prefix-icon="Search"></el-input>
  <el-button type="primary" @click="load">查询</el-button>
  <el-button type="warning" @click="reset">重置</el-button>
  </div>

  <div class="card" style="margin-bottom: 5px">
    <el-button type="primary" @click="handleAdd">新增</el-button>
    <el-button type="danger" @click="delBatch">批量删除</el-button>
    <el-upload
        style="display: inline-block"
      action="http://localhost:9090/product/import"
      :show-file-list="false"
      :on-success="importSuccess"
    >
      <el-button type="info" style="margin-left: 420px;">导入</el-button>
    </el-upload>

    <el-input style="width: 150px; margin-left: 10px; margin-right: 5px;" v-model="data.export.stockQuantity" placeholder="请输入最低库存" ></el-input>
    <el-button type="success" @click="exportData">导出</el-button>
  </div>
  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column label="序号" prop="productId" />
      <el-table-column label="名称" prop="productName" />
      <el-table-column label="类别" prop="category" />
      <el-table-column label="价格" prop="price" />
      <el-table-column label="库存" prop="stockQuantity" />
<!--      <el-table-column label="货架" prop="shelf" />-->
      <el-table-column label="描述" prop="description" show-overflow-tooltip/>
      <el-table-column label="供应商" prop="supplierName" />
      <el-table-column label="销量" prop="sales" />
      <el-table-column label="利润" prop="margin"/>
      <el-table-column label="操作" width="120">
      <template #default="scope">
        <el-button type="primary" :icon="Edit" circle @click="handleUpdate(scope.row)"></el-button>
        <el-button type="danger" :icon="Delete" circle @click="handleDelete(scope.row.productId)"></el-button>
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
  <el-dialog title="商品信息" v-model="data.formVisible" width="500" destroy-on-close>
    <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right: 40px; padding-top: 20px">
      <el-form-item label="名称" prop="productName">
        <el-input v-model="data.form.productName" autocomplete="off" placeholder="请输入名称" />
      </el-form-item>
      <el-form-item label="类别" prop="category">
        <el-select v-model="data.form.category" placeholder="请选择类别">
          <el-option label="水果" value="Fruit" />
          <el-option label="乳制品" value="Dairy" />
          <el-option label="烘焙食品" value="Bakery" />
          <el-option label="蔬菜" value="vegetable" />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="类别">
        <el-input v-model="data.form.category" autocomplete="off" placeholder="请输入类别"/>
      </el-form-item>-->
      <el-form-item label="价格" prop="price">
        <el-input v-model="data.form.price" autocomplete="off" placeholder="请输入价格"/>
      </el-form-item>
      <el-form-item label="库存" prop="stockQuantity">
        <el-input-number v-model="data.form.stockQuantity" autocomplete="off" placeholder="请输入库存"/>
      </el-form-item>
<!--      <el-form-item label="货架" prop="shelf">
        <el-input v-model="data.form.shelf" autocomplete="off" placeholder="请输入货架"/>
      </el-form-item>-->
      <el-form-item label="描述" >
        <el-input :rows="3" type="textarea" v-model="data.form.description" autocomplete="off" placeholder="请输入描述"/>
      </el-form-item>
      <el-form-item label="供应商" prop="supplierId">
        <el-select style="width: 100%" v-model="data.form.supplierId" placeholder="请选择供应商">
          <el-option v-for="item in data.supplierList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="销量">
        <el-input v-model="data.form.sales" autocomplete="off" placeholder="请输入销量"/>
      </el-form-item>-->
      <el-form-item label="利润" prop="margin">
        <el-input v-model="data.form.margin" autocomplete="off" placeholder="请输入利润"/>
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
  supplierList:[],//供应商列表
  Search: {
    productName: ""
  },
  export:{
    stockQuantity: ""
  },
  ids: [],
  rules:{
    productName: [
      {required: true, message: '请输入名称', trigger: 'blur'},
      {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
    ],
    category: [
      {required: true, message: '请选择类别', trigger: 'blur'},
    ],
    price: [
      {required: true, message: '请输入价格', trigger: 'blur'},
    ],
    stockQuantity: [
      {required: true, message: '请输入库存', trigger: 'blur'},
    ],
    margin: [
      {required: true, message: '请输入利润', trigger: 'blur'},
    ],
   supplierId: [
     {required: true, message: '请选择供应商', trigger: 'blur'},
    ]
  }

})
const formRef=ref()//表单验证

request.get('/supplier/selectAll').then(res => {
  data.supplierList = res.data
})

const exportData = () => {
  // 构建带有参数的URL
  const url = `http://localhost:9090/product/export?stockQuantity=${encodeURIComponent(data.export.stockQuantity)}`;
  // 以流的形式导出Excel  打开流的链接，浏览器会自动下载
  window.open(url);
}

const importSuccess = () => {
  if(res.code === '200'){
    ElMessage.success('批量导入成功')
    load()//重新加载数据
  } else {
    ElMessage.error(res.msg)
  }
}

const load = ()=>{
  request.get("/product/selectPage",{
    params: {
      pageNum: data.PageNum,
      pageSize: data.PageSize,
      productName: data.Search.productName
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
  data.Search.productName = ""
  load()
}
const handleAdd = ()=>{
  data.formVisible = true
  data.form={}//清空表单(脏数据)
}
const save = ()=>{//两个操作新增＋编辑
  formRef.value.validate((valid)=>{
    if(valid){
      data.form.productId ? update() : add()
    }
  })

}

const add=()=>{
  request.post("/product/add",data.form).then(res=>{//新增没有id
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
  request.put("/product/update",data.form).then(res=>{//修改有id
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
    request.delete("/product/deleteById/"+id).then(res=>{
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
  data.ids = rows.map(row => row.productId)
}
const delBatch = ()=>{
  if(data.ids.length === 0){
    ElMessage.warning('请选择要删除的数据')
    return
  }
  ElMessageBox.confirm('确定删除该数据吗？', '删除确认', {type: 'warning'}).then(() => {
    request.delete("/product/deleteBatch",{data: data.ids}).then(res => {
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