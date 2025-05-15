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
        component: () => import('@/views/SysHome.vue')
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
