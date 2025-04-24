import {createRouter, createWebHistory} from 'vue-router'
import UserLogin from '../components/pages/UserLogin.vue'
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
                path: '/UserManagement',
                name: 'SystemHome',
                component: SystemHome,
            },
        ]

    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
export default router;
