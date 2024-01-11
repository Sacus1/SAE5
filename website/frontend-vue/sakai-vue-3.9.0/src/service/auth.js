import { ref } from 'vue';
import { loginRequest } from '@/api/authAPI';

const token = ref(localStorage.getItem('token') || null);

export const useAuth = () => {
    const login = async (email, password) => {
        try {
            const payload = {
                mail: email,
                password: password
            };

            const response = await loginRequest(payload);
            token.value = response.data.token;
            localStorage.setItem('token', token.value);
            // You might want to store user information in localStorage as well.
            // localStorage.setItem('user', JSON.stringify(response.data.user));
            return response;
        } catch (error) {
            console.error('Login failed:', error);
            throw error;
        }
    };

    const logout = () => {
        token.value = null;
        localStorage.removeItem('token');
        // Clear other stored user information if needed.
        // localStorage.removeItem('user');
    };

    const getAccessToken = () => token.value;

    return { login, logout, getAccessToken };
};
