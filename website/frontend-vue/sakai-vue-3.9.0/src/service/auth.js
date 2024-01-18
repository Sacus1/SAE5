import { ref } from 'vue';
import { loginRequest, registerRequest, getClientData, updateClientDataRequest } from '@/api/authAPI';

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
    };

    const register = async ({ email, password, nom, prenom, telephone, telephone2, telephone3, dateNaissance, profession, adresse, ville, codePostal, civilite }) => {
        try {
            const payload = {
                email: email,
                password: password,
                nom: nom,
                prenom: prenom,
                telephone: telephone,
                telephone2: telephone2,
                telephone3: telephone3,
                dateNaissance: dateNaissance,
                profession: profession,
                adresse: adresse,
                ville: ville,
                codePostal: codePostal,
                civilite: civilite
            };

            const response = await registerRequest(payload);
            token.value = response.data.token;
            localStorage.setItem('token', token.value);
            // You might want to store user information in localStorage as well.
            return response;
        } catch (error) {
            console.error('Registration failed:', error);
            throw error;
        }
    };

    const fetchClientData = async () => {
        try {
            const clientData = await getClientData(token.value);
            // Use the clientData to set placeholders or perform other actions
            return clientData;
        } catch (error) {
            console.error('Failed to fetch client data:', error);
            throw error;
        }
    };

    const updateClientData = async (clientData) => {
        try {
            // Assuming there is an API endpoint for updating client data
            const response = await updateClientDataRequest(clientData, token.value);

            // You might want to handle the response or return a specific value.
            return response;
        } catch (error) {
            console.error('Failed to update client data:', error);
            throw error;
        }
    };

    return { login, logout, register, fetchClientData, updateClientData };
};
