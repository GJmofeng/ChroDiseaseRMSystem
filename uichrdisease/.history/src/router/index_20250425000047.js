import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import Home from '../views/Home.vue'

const routes = [
  {
    path: '/',
    name: 'MainLayout',
    component: MainLayout,
    children: [
      {
        path: '',
        name: 'Home',
        component: Home
      }
    ]
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes: routes
})

export default router 