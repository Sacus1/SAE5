import axios from 'axios';

const BASE_URL = 'http://localhost:3001'; // Replace with your actual backend URL.

export const loginRequest = async ({ mail, password }) => {
    try {
        const response = await axios.post(`${BASE_URL}/login`, { mail, password });
        return response;
    } catch (error) {
        console.error('Login request failed:', error);
        throw error;
    }
};
