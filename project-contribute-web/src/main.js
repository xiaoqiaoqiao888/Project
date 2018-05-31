// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
// import Routers from './router.js';
// iview
import iView from 'iview'
import 'iview/dist/styles/iview.css'
import './my-theme/index.less'
import './my-theme/index.css'
import { API, HTTP } from './util/axios'
import Moment from 'moment'
import ZkTable from 'vue-table-with-tree-grid'
import VueQuillEditor from 'vue-quill-editor'
// require styles 引入样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import echarts from 'echarts'

Vue.use(VueQuillEditor)
Vue.prototype.$http = HTTP
Vue.prototype.$api = API
Vue.prototype.$dateFormat = (value) => Moment(value).format('YYYY/MM/DD')
Vue.prototype.$dateFormat2 = (value) => Moment(value).format('YYYY.MM.DD')
Vue.prototype.$timeFormat = (value) => Moment(value).format('YYYY/MM/DD HH:mm:ss')
Vue.prototype.$timeFormat2 = (value) => Moment(value).format('YYYY.MM.DD HH:mm:ss')
Vue.prototype.$timeFormat3 = (value) => Moment(value).format('HH:mm:ss')
Vue.use(iView)
Vue.config.productionTip = false
Vue.use(ZkTable)
Vue.prototype.$echarts = echarts

// Vue.use(VueRouter);

/* eslint-disable no-new */
router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) { // 是否需要登录拦截
    if (store.state.token) { // 已登录
      next()
    } else {
      next({
        path: '/Login',
        query: {redirect: to.fullPath}
      })
    }
  } else {
    next()
  }
})
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
