// profileAPI.js
import axios from 'axios';
import { useProfileImageStore } from '@/stores/profileImage';

const BASE_URL = 'http://localhost:3001';
const imageStore = useProfileImageStore();

export const updateProfileImage = async (token, blobURL) => {
    try {
        imageStore.profileImage = blobURL;
        const blob = await fetch(blobURL).then((r) => r.blob());

        const formData = new FormData();

        const data = {
            token: token
        };
        formData.append('data', JSON.stringify(data));
        formData.append('image', blob);

        const response = await axios.put(`${BASE_URL}/profil`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response;
    } catch (error) {
        console.error('Profile image update request failed:', error);
        throw error;
    }
};

export const getProfileImage = async (token) => {
    if (!imageStore.isSet) {
        try {
            const response = await axios.post(`${BASE_URL}/profil`, { token: token }, { responseType: 'blob' });
            const blobURL = URL.createObjectURL(response.data);
            imageStore.profileImage = blobURL;
            return blobURL;
        } catch (error) {
            imageStore.profileImage = '/default-pfp.svg';
            console.error('Profile image retrieval request failed:', error);
            throw error;
        }
    }
};
