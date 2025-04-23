import {createRouter, createWebHistory} from 'vue-router'
import UserLogin from '@/pages/UserLogin.vue'

const routes = [
    {
        path: '/',
        name: 'UserLogin',
        component: UserLogin
    },
    // {
    //     path: '/home',
    //     name: 'Home',
    //     component: () => import('@/pages/Home.vue'),
    //     meta: {
    //         requiresAuth: true
    //     }
    // },
    // {
    //     path: '/register',
    //     name: 'Register',
    // }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
export default router;
