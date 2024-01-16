<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useLayout } from '@/layout/composables/layout';
import { useRouter } from 'vue-router';
import { useAuth } from '@/service/auth';
import { useProfileImageStore } from '@/stores/profileImage'
import { storeToRefs } from 'pinia'


const { layoutConfig, onMenuToggle } = useLayout();

const { logout } = useAuth();


const outsideClickListener = ref(null);
const topbarMenuActive = ref(false);
const router = useRouter();
const profileImageStore = useProfileImageStore();
const {profileImage} = storeToRefs(profileImageStore);
const token = localStorage.getItem('token') || null;

onMounted(() => {
    bindOutsideClickListener();
});

onBeforeUnmount(() => {
    unbindOutsideClickListener();
});
const disconnect = () =>{
    logout();
    router.push('/login');
}


const onTopBarMenuButton = () => {
    topbarMenuActive.value = !topbarMenuActive.value;
};

const onProfileclick = () => {
    onTopBarMenuButton();
    router.push('/profil');
}
const topbarMenuClasses = computed(() => {
    return {
        'layout-topbar-menu-mobile-active': topbarMenuActive.value
    };
});


const bindOutsideClickListener = () => {
    if (!outsideClickListener.value) {
        outsideClickListener.value = (event) => {
            if (isOutsideClicked(event)) {
                topbarMenuActive.value = false;
            }
        };
        document.addEventListener('click', outsideClickListener.value);
    }
};
const unbindOutsideClickListener = () => {
    if (outsideClickListener.value) {
        document.removeEventListener('click', outsideClickListener);
        outsideClickListener.value = null;
    }
};
const isOutsideClicked = (event) => {
    if (!topbarMenuActive.value) return;

    const sidebarEl = document.querySelector('.layout-topbar-menu');
    const topbarEl = document.querySelector('.layout-topbar-menu-button');

    return !(sidebarEl.isSameNode(event.target) || sidebarEl.contains(event.target) || topbarEl.isSameNode(event.target) || topbarEl.contains(event.target));
};
</script>

<template>
    <div class="layout-topbar">
        
        <button class="p-link layout-menu-button layout-topbar-button" @click="onMenuToggle()">
            <i class="pi pi-bars"></i>
        </button>

        <router-link to="/" class="layout-topbar-logo">
            <img src="/logo-arrosoir-cocagne-vert.svg" alt="logo" />
            <span>Jardins de Cocagne</span>
        </router-link>
        

        <button class="p-link layout-topbar-menu-button layout-topbar-button" @click="onTopBarMenuButton()">
            <i class="pi pi-ellipsis-v"></i>
        </button>
        
        <div class="layout-topbar-menu" :class="topbarMenuClasses">
            <Button v-if="token" label="Se Déconnecter" severity="danger" class="w-full p-3 text-xl" @click="disconnect"></Button>

            <button @click="onProfileclick()" class="p-link layout-topbar-button">
                <Avatar :image="profileImage" size="large" shape="circle"></Avatar>
                <span>Profile</span>
            </button>
        </div>
        
    </div>
</template>

<style lang="scss" scoped></style>
