import axios from 'axios';

const request = axios.create({
    baseURL: 'http://localhost:9090',
    timeout: 5000
})

// 请求拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';


    // config.headers['token'] = user.token; // 设置token
    return config;
}, error => {
    return Promise.reject(error);
});

// response拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }
        return res;
    }, error => {
        console.log('err' + error)
        return Promise.reject(error)
    }
)

export default request;
