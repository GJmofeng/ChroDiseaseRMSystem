import {createRouter, createWebHistory} from 'vue-router'
import UserLogin from '../components/pages/UserLogin.vue'
import SystemHome from '../components/pages/SystemHome.vue'
import UserManage from '../components/pages/UserManage.vue'

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
                path: '/UserManage',
                name: 'UserManage',
                component: UserManage,
            },
        ]

    },
];

const router = createRouter({
    history: createWebHistory(),
    routes:
});
export default router;
