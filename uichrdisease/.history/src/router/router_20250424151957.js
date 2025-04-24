import {createRouter, createWebHistory} from 'vue-router'
import UserLogin from '../components/UserManage.vue'

const routes = [
    {
        path: '/',
        name: 'UserLogin',
        component: UserLogin,
    },
    {
        path: '/user',
        name: 'User',
        component: () => import('../components/User.vue'),
    },
    {
        path: '/admin',
        name: 'Admin',
        component: () => import('../components/Admin.vue'),
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
export default router;
