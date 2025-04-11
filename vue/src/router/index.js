import {createRouter, createWebHistory} from 'vue-router'
import Login from '@/views/Login.vue'

const router=createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes:[
      {path: '/', redirect: '/login'},
      {path: '/login', name: 'login',meta:{title:'登陆界面'}, component: () => import('../views/Login.vue')},
      {path: '/register', name: 'register',meta:{title:'注册界面'}, component: () => import('../views/Register.vue')},
      {path:'/manage',meta:{title:'管理后台'},component:()=>import('../views/Manage.vue'),children:[
           {path:'home',name:'home',meta:{title:'首页'},component:()=>import('../views/Home.vue')},
           {path: 'products', name: 'products', meta:{title:'商品信息管理'},component: () => import('../views/Products.vue')},
           {path: 'employee', name: 'employee', meta:{title:'理货员信息管理'},component: () => import('../views/Employee.vue')},
           {path: 'admin', name: 'admin', meta:{title:'店长信息管理'},component: () => import('../views/Admin.vue')},
           {path: 'person', name: 'person', meta:{title:'个人信息'},component: () => import('../views/Person.vue')},
           {path: 'password', name: 'password', meta:{title:'修改密码'},component: () => import('../views/Password.vue')},
           {path: 'shelves', name: 'shelves', meta:{title:'货架管理'},component: () => import('../views/Shelf.vue')},
           {path: 'supplier', name: 'supplier', meta:{title:'供应商管理'},component: () => import('../views/Supplier.vue')},
           {path: 'data', name: 'data', meta:{title:'数据统计'},component: () => import('../views/Data.vue')},
      ]},
      {path:'/404',name:'404',meta:{title:'找不到页面'},component:()=>import('../views/404.vue')},
      {path:'/:pathMatch(.*)',redirect:'/404'}
  ]
})
//标题
router.beforeEach((to,from,next)=>{
    document.title=to.meta.title
    next()
})


export default router