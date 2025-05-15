import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../
import Home from '../views/Home.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      {
        path: '',
        name: 'Home',
        component: Home
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 