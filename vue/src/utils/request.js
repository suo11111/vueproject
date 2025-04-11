import axios from "axios";
import {ElMessage} from "element-plus";

//封装网络请求

const  request = axios.create({
    baseURL: "http://localhost:9090",
    timeout: 30000//后台连接超时时间
})

//request拦截器
//可以自请求发送前对请求做一些处理
request.interceptors.request.use(config => {
    config.headers['Content-type'] = 'application/json;charset=UTF-8';
    // 获取存储的JWT令牌
    const token = localStorage.getItem('vue-project-token');
    if (token) {
        // 在请求头中添加JWT令牌
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config
}, error => {
    return Promise.reject(error)
});


//response拦截器
//可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        //兼容服务器端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        if (error.response) {
            if (error.response.status === 401) {
                // 处理未授权的情况，例如跳转到登录页面
                ElMessage.error('未授权，请重新登录');
                localStorage.removeItem('vue-project-token');
                localStorage.removeItem('vue-project-user');
                location.href = '/login';
            } else if (error.response.status === 404) {
                ElMessage.error("未找到请求接口");
            } else if (error.response.status === 500) {
                ElMessage.error('系统异常，请查看后端控制台报错');
            } else {
                console.error(error.message);
            }
        } else {
            ElMessage.error('网络请求失败，请检查网络连接');
        }
        return Promise.reject(error);
    }
)

export default request