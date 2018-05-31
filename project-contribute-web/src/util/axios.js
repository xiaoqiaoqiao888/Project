import axios from 'axios'
import Cookie from 'js-cookie'
import * as response from './response'
import router from '../router'
import { Message } from 'iview';

const envConfig = {
  production: process.env.HOST_URL+'/pmt', // 该地址目前为测试地址
  stage: 'http://[stage]/',
  development: 'http://localhost:8080/pmt'
}

export const baseURL = envConfig[process.env.NODE_ENV || 'development']
console.log(baseURL) // 测试用最后删掉
export const HTTP = axios.create({
  baseURL,
  timeout: 50000,
  headers: {
    common: {
      'X-Requested-With': 'XMLHttpRequest',
      'Authorization': Cookie.get('Authorization') == undefined ? '' : Cookie.get('Authorization'),
      'Content-Disposition': 'attachment',
      'filename': 'filename.xlsx'
    },
    post: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  }
})
HTTP.interceptors.request.use(function (config) {
  // console.log('调用axios')
  config.headers.Authorization = Cookie.get('Authorization') == undefined ? '' : Cookie.get('Authorization')
  return config
}, function (error) {
  return Promise.reject(error)
})

// 添加一个返回拦截器  拦截401请求
HTTP.interceptors.response.use(function (response) {
  return response
}, function (error) {
  if (error.response) {
    if (error.response.status == 401) {
      Cookie.remove('userInf')
      Cookie.remove('Authorization')
      localStorage.removeItem('projectId')
      router.push({path: '/Login'})
      return false // 防止弹出报错信息
    } else if (error.response.status == 406) {
      Message.error(error.response.data);
      return false // 防止弹出报错信息
    }
  }
  return Promise.reject(error)
})

// 处理post请求参数的方法
const postParam = (param) => {
  var data = new FormData()
  for (var i in param) {
    if (param[i] != null) {
      data.append(i, param[i])
    }
  }
  return data
}

// 全局提示方法
export const message = (obj) => {
  console.log(obj.catch)
  obj.object.$Message[obj.type]({
    content: obj.content,
    duration: obj.time || 2,
    onClose: () => {
      if (obj.callBack) obj.callBack()
    }
  })
}

// 请求部分
export const API = {
  // get请求方式
  get: params => HTTP({
    method: 'get',
    url: params.url,
    params: params.data,
  }).then((res) => {
    var data = response.data(res, params.object);
    params.then(data)
  }).catch((res) => {
    message({
      catch: res,
      content: params.errorIfo,
      object: params.object,
      type: 'info',
    })
  }),
  post: params => HTTP({
    method: 'post',
    url: params.url,
    data: postParam(params.data),
  }).then((res) => {
    var data = response.data(res, params.object)
    params.then(data)
  }).catch((res) => { // res, params.errorIfo, 'info'
    message({
      catch: res,
      content: params.errorIfo,
      object: params.object,
      type: 'info',
    })
  }),
}
