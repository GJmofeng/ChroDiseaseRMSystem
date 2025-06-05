import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import FrontPage from '../views/FrontPage.vue'
import UserManage from '../views/UserManage.vue'
import RegionManage from '../views/RegionManage.vue'
import ParticipationInfo from '../views/business/ParticipationInfo.vue'
import DiseaseInfo from '../views/disease/DiseaseInfo.vue'
import PolicyManagement from '../views/policy/PolicyManagement.vue'
import ReimbursementForm from '../views/reimbursement/ReimbursementForm.vue'
import ReimbursementList from '../views/reimbursement/ReimbursementList.vue'
import ReimbursementAudit from '../views/reimbursement/ReimbursementAudit.vue'
import ReimbursementRemit from '../views/reimbursement/ReimbursementRemit.vue'
import PermissionManage from '../views

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
        path: 'permission-manage',
        name: 'PermissionManage',
        component: PermissionManage
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
      },
      {
        path: 'disease-info',
        name: 'DiseaseInfo',
        component: DiseaseInfo
      },
      {
        path: 'policy-manage',
        name: 'PolicyManagement',
        component: PolicyManagement
      },
      {
        path: 'reimbursement-form',
        name: 'ReimbursementForm',
        component: ReimbursementForm
      },
      {
        path: 'reimbursement-list',
        name: 'ReimbursementList',
        component: ReimbursementList
      },
      {
        path: 'reimbursement-audit',
        name: 'ReimbursementAudit',
        component: ReimbursementAudit
      },
      {
        path: 'reimbursement-remit',
        name: 'ReimbursementRemit',
        component: ReimbursementRemit
      },
      {
        path: 'yearly-statistics',
        name: 'YearlyStatistics',
        component: () => import('@/views/statistics/YearlyStatistics.vue')
      },
      {
        path: 'monthly-statistics',
        name: 'MonthlyStatistics',
        component: () => import('@/views/statistics/MonthlyStatistics.vue')
      },
      {
        path: 'user-profile',
        name: 'UserProfile',
        component: () => import('@/views/profile/UserProfile.vue')
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
]

const router = createRouter({
  history: createWebHistory(),
  routes: routes
})

export default router 