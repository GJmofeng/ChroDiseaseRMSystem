import {createRouter, createWebHistory} from 'vue-router'
import UserLogin from '../components/UserManage.vue'
import SystemHome from '../components/SystemHome.vue'

const routes = [
    {
        path: '/',
        name: 'UserLogin',
        component: UserLogin,
    },
    {
        path: '/SystemHome',
        name: 'SystemHome',
        component: SystemHome,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
export default router;
