import {createRouter, createWebHistory} from 'vue-router'
import UserLogin from '../components/UserManage.vue'
import UserManage from '../components/SystemHome.vue'

const routes = [
    {
        path: '/',
        name: 'UserLogin',
        component: UserLogin,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
export default router;
