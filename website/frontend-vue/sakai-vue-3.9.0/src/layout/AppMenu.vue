<script setup>
import { ref } from 'vue';

import AppMenuItem from './AppMenuItem.vue';

const model = ref([
    {
        label: 'Accueil',
        items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-home', to: '/' }]
    },
    {
        label: 'Compte',
        items: [
            { label: 'Profil', icon: 'pi pi-fw pi-id-card', to: '/profil' },
            { label: 'Modifier', icon: 'pi pi-fw pi-user-edit', to: '/modifier-profil' },
        ]
    },
    {
        label: 'Jardins',
        items: [
            { label: 'Nouvelle Adhésion', icon: 'pi pi-fw pi-plus', to: '/nouvelle-adhesion' },
            { label: 'Adhésions', icon: 'pi pi-fw pi-credit-card', to: '/adhesions' },
        ]
    },
    {
        label: 'Abonnements',
        items: [
            { label: 'Paniers', icon: 'pi pi-fw pi-shopping-bag', to: '/paniers' },
            { label: 'Vos Abonnements', icon: 'pi pi-fw pi-tags', to: '/abonnements' },
        ]
    }
]);
</script>

<template>
    <ul class="layout-menu">
        <template v-for="(item, i) in model" :key="item">
            <app-menu-item v-if="!item.separator" :item="item" :index="i"></app-menu-item>
            <li v-if="item.separator" class="menu-separator"></li>
        </template>
    </ul>
</template>

<style lang="scss" scoped></style>
