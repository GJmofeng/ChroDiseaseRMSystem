import {createRouter, createWebHistory} from 'vue-router'
import UserLogin from '../components/pages/UserManage.vue'
import SystemHome from '../components/pages/SystemHome.vue'

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
        children: [
            {

    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
export default router;
