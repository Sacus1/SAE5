import axios from 'axios';

const BASE_URL = 'http://localhost:3001';

export const getPaniers = async () => {
    try {
        const response = await axios.post(`${BASE_URL}/paniers`);
        const paniersData = response.data;

        // Assuming 'images' property contains an array of blobs
        if (paniersData && paniersData.length) {
            paniersData.forEach((panier) => {
                if (panier.images && panier.images.length) {
                    panier.imageURLs = panier.images.map((blob) => URL.createObjectURL(new Blob([blob], 'image/jpeg')));
                }
            });
        }

        return paniersData;
    } catch (error) {
        console.error('Error fetching paniers:', error);
        return null;
    }
};
