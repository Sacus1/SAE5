// stores/profileImage.js
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useProfileImageStore = defineStore('profileImage', {
    state: () => ({
        profileImage: '/default-pfp.svg',
        isSet: false
    }),
    actions: {
        update(value) {
            this.profileImage = value;
            this.isSet = true;
        }
    }
});
