import {createRouter, createWebHistory} from 'vue-router'
import UserLogin from '../components/pages/UserLogin.vue'
import SystemHome from '../components/pages/SystemHome.vue'
import UserManage from '../components/pages/UserManage.vue'
import SysHome from '../components/pages/SysHome.vue'

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
            {
                path: '/SysHome',
                name: 'SysHome',
                component: SysHome,
            },
        ]

    },
];

const router = createRouter({
    history: createWebHistory(),
    routes: routes,
});

export default router;
