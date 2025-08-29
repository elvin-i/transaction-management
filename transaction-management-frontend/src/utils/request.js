import axios from 'axios'
import notification from 'ant-design-vue/es/notification'
import { VueAxios } from './axios'

// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  baseURL: process.env.VUE_APP_API_BASE_URL,
  timeout: 6000 // 请求超时时间
})

// 异常拦截处理器
const errorHandler = (error) => {
  if (error.response) {
    const data = error.response.data
    if (error.response.status === 403) {
      notification.error({
        message: 'Forbidden',
        description: data.message
      })
    }
    if (error.response.status === 401 && !(data.result && data.result.isLogin)) {
      console.log(error)
      notification.error({
        message: 'Unauthorized',
        description: 'Authorization verification failed'
      })
    }
  }
  return Promise.reject(error)
}
function getCookie (cname) {
  var name = cname + '='
  var ca = document.cookie.split(';')
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i]
    while (c.charAt(0) == ' ') c = c.substring(1)
    if (c.indexOf(name) != -1) {
      return c.substring(name.length, c.length)
    }
  }
  return ''
}
// request interceptor
request.interceptors.request.use(config => {
  config.headers[process.env.VUE_APP_AUTHORIZATION_HEADER_KEY] = Math.random()
  let user =  {
    username:'buukle',
    userId:'bk_0000000000000000',
    mainTenant:'1',
    subTenantIds:'1,9'
  }
  config.headers['User'] = JSON.stringify(user)
  return config
}, errorHandler)

// response interceptor
request.interceptors.response.use((response) => {
  console.log(response)
  return response.data
}, errorHandler)

const installer = {
  vm: {},
  install (Vue) {
    Vue.use(VueAxios, request)
  }
}

export default request

export {
  installer as VueAxios,
  request as axios
}
