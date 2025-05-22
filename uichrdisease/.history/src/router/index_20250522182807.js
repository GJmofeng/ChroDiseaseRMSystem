import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import FrontPage from '../views/FrontPage.vue'
import UserManage from '../views/UserManage.vue'
import RegionManage from '../views/RegionManage.vue'
import ParticipationInfo from '../views/business/ParticipationInfo.vue'

const routes = [
  {
    path: '/',
    name: 'FrontPage',
    component: FrontPage
  },
  {
    path: '/redirect/:path(.*)',
    name: 'Redirect',
    component: () => import('@/views/Redirect.vue')
  },
  {
    path: '/main',
    name: 'MainLayout',
    component: MainLayout,
    children: [
      {
        path: '',
        name: 'Home',
        component: Home
      },
      {
        path: 'user-manage',
        name: 'UserManage',
        component: UserManage
      },
      {
        path: 'region-manage',
        name: 'RegionManage',
        component: RegionManage
      },
      {
        path: 'participation',
        name: 'ParticipationInfo',
        component: () => import('@/views/business/ParticipationInfo.vue')
      },
      {
        path: 'chronic-cert',
        name: 'ChronicCertManage',
        component: () => import('@/views/chronic/ChronicCertManage.vue')
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/disease',
    component: Layout,
    redirect: '/disease/info',
    name: 'Disease',
    meta: { title: '慢病管理', icon: 'el-icon-first-aid-kit' },
    children: [
      {
        path: 'info',
        name: 'DiseaseInfo',
        component: () => import('@/views/disease/DiseaseInfo.vue'),
        meta: { title: '慢病信息管理' }
      }
    ]
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes: routes
})

export default router 