// profileAPI.js
import axios from 'axios';

const BASE_URL = 'http://localhost:3001';
const token = localStorage.getItem('token') || null;

export const fetchJardinData = async () => {
    try {
        const response = await axios.post(`${BASE_URL}/jardins`, { token });
        return response.data;
    } catch (error) {
        console.error('Failed to fetch jardins data:', error);
        throw error;
    }
};

export const fetchTypesAdhesionData = async () => {
    try {
        const response = await axios.post(`${BASE_URL}/types-adhesion`);
        return response.data;
    } catch (error) {
        console.error('Failed to fetch adhesion type data:', error);
        throw error;
    }
};

export const addAdhesionData = async (jardinId, typeAdhesionId) => {
    try {
        const response = await axios.post(`${BASE_URL}/adhesions`, { token, typeAdhesionId, jardinId });
        return response.data;
    } catch (error) {
        console.error('Failed to fetch adhesion type data:', error);
        throw error;
    }
};

export const fetchAdhesionData = async () => {
    try {
        const response = await axios.get(`${BASE_URL}/adhesions`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        return response.data;
    } catch (error) {
        console.error('Failed to fetch adhesion type data:', error);
        throw error;
    }
};
