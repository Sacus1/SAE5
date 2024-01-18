import axios from 'axios';

const BASE_URL = 'http://localhost:3001';

export const loginRequest = async ({ mail, password }) => {
    try {
        const response = await axios.post(`${BASE_URL}/login`, { mail, password });
        return response;
    } catch (error) {
        console.error('Login request failed:', error);
        throw error;
    }
};

export const registerRequest = async ({ email, password, nom, prenom, telephone, telephone2, telephone3, dateNaissance, profession, adresse, ville, codePostal, civilite }) => {
    try {
        const response = await axios.post(`${BASE_URL}/register`, {
            email,
            password,
            nom,
            prenom,
            telephone,
            telephone2,
            telephone3,
            dateNaissance,
            profession,
            adresse,
            ville,
            codePostal,
            civilite
        });
        return response;
    } catch (error) {
        console.error('Registration request failed:', error);
        throw error;
    }
};

export const getClientData = async (token) => {
    try {
        const response = await axios.post(`${BASE_URL}/modifier-profil`, { token });
        return response.data;
    } catch (error) {
        console.error('Failed to fetch client data:', error);
        throw error;
    }
};

export const updateClientDataRequest = async (clientData, token) => {
    try {
        const response = await axios.put(`${BASE_URL}/modifier-profil`, { clientData, token });
        return response;
    } catch (error) {
        console.error('Update client data request failed:', error);
        throw error;
    }
};
