import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: () => import('@/views/Home.vue'),
            children: []
        }, {
            path: '/poi/search',
            name: 'search',
            component: () => import('@/views/searchPoi.vue'),
            children: []
        }
    ]
})

export default router