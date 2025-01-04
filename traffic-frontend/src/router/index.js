import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/poi/search',
            name: 'search',
            component: () => import('@/views/searchPoi.vue'),
            children: []
        }, {
            path: '/',
            name: 'welcome',
            component: () => import('@/views/WelcomeView.vue'),
            children: [
                {
                    path: '',
                    name: 'welcome-login',
                    component: () => import('@/views/welcome/LoginPage.vue')
                }, {
                    path: 'register',
                    name: 'welcome-register',
                    component: () => import('@/views/welcome/RegisterPage.vue')
                }, {
                    path: 'reset',
                    name: 'welcome-reset',
                    component: () => import('@/views/welcome/ResetPage.vue')
                }
            ]
        }, {
            path: '/index',
            name: 'home',
            component: () => import('@/views/Home.vue'),
            children: []
        }, {
            path: '/admin',
            name: 'admin',
            component: () => import('@/views/management.vue'),
        }
    ]
})

export default router