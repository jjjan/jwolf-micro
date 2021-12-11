import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['X-Token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    //没登录时xhr请求后端接口会重定向到auth登录页，返回的不是json而是text/html,但无法渲染，通过location.href=response.request.responseURL渲染出来但登录后无法redirect，
    //故通过location.href请求/jwolf/manage/user/login，再转发到auth,登录后回调/jwolf/manage/user/login，
    //该中转页面再location.href请求前端，但这里需要通过Nginx转发保证前后端同域，这样cookie jsessionid
    //就可携带到前端页面了，如果前后端不分离则方便很多，也可以考虑vue项目打包放到后端resources
    if(response.headers['content-type'].indexOf('text/html')!=-1  && response.request.responseURL){
      debugger
      console.log(response)
      window.location.href = process.env.VUE_APP_BASE_API+"/jwolf/manage/user/login"
      return response;
      }
    const res = response.data
    // 如果自定义状态码不是则弹框
    if (res.code !== 200) {
      Message({
        message: res.message || 'Error 111',
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }

      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
