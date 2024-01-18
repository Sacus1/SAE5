import { createRouter, createWebHistory } from 'vue-router';
import AppLayout from '@/layout/AppLayout.vue';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: AppLayout,
            children: [
                {
                    path: '/',
                    name: 'dashboard',
                    component: () => import('@/views/Dashboard.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/profil',
                    name: 'profil',
                    component: () => import('@/views/uikit/Profil.vue'),
                    meta: { requiresAuth: true }
                },

                {
                    path: '/modifier-profil',
                    name: 'modifier-profil',
                    component: () => import('@/views/uikit/ModifierProfil.vue'),
                    meta: { requiresAuth: true }
                },

                {
                    path: '/nouvelle-adhesion',
                    name: 'nouvelle-adhesion',
                    component: () => import('@/views/uikit/NouvelleAdhesion.vue'),
                    meta: { requiresAuth: true }
                },

                {
                    path: '/adhesions',
                    name: 'adhesions',
                    component: () => import('@/views/uikit/Adhesions.vue'),
                    meta: { requiresAuth: true }
                },

                {
                    path: '/paniers',
                    name: 'paniers',
                    component: () => import('@/views/uikit/Paniers.vue'),
                    meta: { requiresAuth: true }
                },

                {
                    path: '/abonnements',
                    name: 'abonnements',
                    component: () => import('@/views/uikit/Abonnements.vue'),
                    meta: { requiresAuth: true }
                },

                {
                    path: '/empty',
                    name: 'empty',
                    component: () => import('@/views/pages/Empty.vue')
                }
            ]
        },
        {
            path: '/landing',
            name: 'landing',
            component: () => import('@/views/pages/Landing.vue')
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('@/views/pages/auth/Login.vue')
        },
        {
            path: '/register',
            name: 'register',
            component: () => import('@/views/pages/auth/Register.vue')
        },
        {
            path: '/access',
            name: 'accessDenied',
            component: () => import('@/views/pages/auth/Access.vue')
        },
        {
            path: '/error',
            name: 'error',
            component: () => import('@/views/pages/auth/Error.vue')
        },
        {
            path: '/:catchAll(.*)',
            name: 'notfound',
            component: () => import('@/views/pages/NotFound.vue')
        }
    ]
});

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    if (to.matched.some((record) => record.meta.requiresAuth)) {
        if (token) {
            return next();
        }
        return next('/login'); // Redirect to login page if no valid token
    } else if ((to.name === 'login' || to.name === 'register') && token) {
        return next('/'); // already logged in, redirect to dashboard
    }
    next();
});

export default router;
