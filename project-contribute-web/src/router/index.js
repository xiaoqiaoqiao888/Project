// 配置路由文件
import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'

// import * as response from '@/util/response'
// 引入组件
export const Login = require('../components/Login.vue').default
export const HomeIndex = require('../components/Home.vue').default
export const WorkBench = require('../components/workBench/index.vue').default
export const WorkbenchIndex = require('../components/workBench/views/WorkbenchIndex.vue').default
export const Project = require('../components/workBench/views/Project.vue').default
export const Packages = require('../components/workBench/views/Package.vue').default
export const PackageTask = require('../components/workBench/views/PackageTask.vue').default
export const totalProgress = require('../components/workBench/views/TotalProgress.vue').default
export const Task = require('../components/workBench/views/Task.vue').default
export const Check = require('../components/workBench/views/Check.vue').default
export const StageProject = require('../components/workBench/views/StageProject.vue').default
export const PackageBag = require('../components/workBench/views/PackageBag.vue').default
export const ourCapacity = require('../components/functionShow/index.vue').default
export const staffManage = require('../components/systemSettings/index.vue').default
export const roleManage = require('../components/systemSettings/roleIndex.vue').default
export const roleCont = require('../components/systemSettings/views/roleManagement.vue').default
// 注册路由index
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/', // 首页
      // name: 'HomeIndex',
      component: HomeIndex,
      redirect: { path: '/WorkBench/Project' },
      children: [
        {
          path: '/WorkBench', // 我的工作台
          name: 'WorkBench',
          component: WorkBench,
          children: [
            // {
            //   path: '/WorkBench/WorkbenchIndex', // 工作台页面
            //   name: 'WorkbenchIndex',
            //   component: WorkbenchIndex,
            // },
            {
              path: '/WorkBench/Project', // 项目
              name: 'Project',
              component: Project,
            },
            {
              path: '/WorkBench/totalProgress', // 总进展页面
              name: 'totalProgress',
              component: totalProgress,
            },
            {
              path: '/WorkBench/Package', // 工程包
              name: 'Package',
              component: Packages,
            },
            {
              path: '/WorkBench/Check', // 验收
              name: 'Check',
              component: Check,
            },
            {
              path: '/WorkBench/Task', // 任务
              name: 'Task',
              component: Task,
            },
            {
              path: '/WorkBench/StageProject', // 阶段
              name: 'StageProject',
              component: StageProject,
            },
            {
              path: '/WorkBench/PackageBag', // 项目下的工程包列表
              name: 'PackageBag',
              component: PackageBag,
            },
            {
              path: '/WorkBench/PackageTask', // 工程包下的任务列表
              name: 'PackageTask',
              component: PackageTask,
            },
          ]
        },
        {
          path: '/ourCapacity/AbilityList', // 我们的
          name: 'ourCapacity',
          component: ourCapacity,
        },
        {
          path: '/staffManage', // 员工管理
          name: 'staffManage',
          component: staffManage,
        },
        {
          path: '/roleManage', // 角色管理
          name: 'roleManage',
          component: roleManage,
          children: [
            {
              path: '/roleManage', // 角色管理列表
              name: 'roleCont',
              component: roleCont,
            },
          ]
        },
      ]
    },
    {
      path: '/Login',
      name: 'Login',
      component: Login,
    },

  ]
})
