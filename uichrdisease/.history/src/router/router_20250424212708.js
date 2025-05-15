import {createRouter, createWebHistory} from 'vue-router'
import UserLogin from '../components/pages/UserLogin.vue'
import SystemHome from '../components/pages/SystemHome.vue'
import UserManage from '../components/pages/UserManage.vue'

const routes = [
    {
        path: '/UserLogin',
        name: 'UserLogin',
        component: UserLogin,
    },
    {
        path: '/',
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
    routes: routes,
});

export default router;
