<script setup>
import { useAuth } from '@/service/auth';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const { register } = useAuth();

const civiliteOptions = [
  { label: 'Male', value: 'Male' },
  { label: 'Femelle', value: 'Femelle' },
  { label: 'Autre', value: 'Autre' }
];

const selectedCivilite = ref('Male');
const email = ref('');
const password = ref('');
const nom = ref('');
const prenom = ref('');
const telephone = ref('');
const telephone2 = ref('');
const telephone3 = ref('');
const dateNaissance = ref('');
const profession = ref('');
const adresse = ref('');
const ville = ref('');
const codePostal = ref('');

const handleRegister = async () => {
  try {
    await register({
      email: email.value,
      password: password.value,
      nom: nom.value,
      prenom: prenom.value,
      telephone: telephone.value,
      telephone2: telephone2.value,
      telephone3: telephone3.value,
      dateNaissance: dateNaissance.value,
      profession: profession.value,
      adresse: adresse.value,
      ville: ville.value,
      codePostal: codePostal.value,
      civilite: selectedCivilite.value
    });
    router.push('/login');
  } catch (error) {
    // Handle register error, e.g., show an error message.
    console.error('Register failed:', error);
  }
};

const redirectToLogin = () => {
    router.push('/login');
}
</script>

<template>
    <div class="surface-ground flex align-items-center justify-content-center min-h-screen min-w-screen overflow-hidden">
        <div class="flex flex-column align-items-center justify-content-center">
            <img src="/logo-arrosoir-cocagne-vert.svg" alt="Cocagne logo" class="mb-1 w-10rem flex-shrink-0" />
            <div style="border-radius: 56px; padding: 0.3rem; background: var(--primary-color)">
                <div class="w-full surface-card py-8 px-5 sm:px-8" style="border-radius: 53px">
                    <div class="text-center mb-5">
                        <span class="text-primary-600 font-medium text-2xl">Sign up to continue</span>
                    </div>

                    <div>
                        <label for="email1" class="block text-900 text-xl font-medium mb-2">Adresse mail</label>
                        <InputText id="email1" type="text" placeholder="example@xyz.com" class="w-full md:w-30rem mb-5" style="padding: 1rem" v-model="email" />

                        <label for="password1" class="block text-900 font-medium text-xl mb-2">Mot de passe</label>
                        <Password id="password1" v-model="password" placeholder="Password" :toggleMask="true" class="w-full mb-3" inputClass="w-full" :inputStyle="{ padding: '1rem' }"></Password>
                        
                        <label for="nom1" class="block text-900 text-xl font-medium mb-2">Nom</label>
                        <InputText id="nom1" type="text" placeholder="Nom" class="w-full md:w-30rem mb-5" style="padding: 1rem" v-model="nom" />

                        <label for="prenom1" class="block text-900 text-xl font-medium mb-2">Prenom</label>
                        <InputText id="prenom1" type="text" placeholder="Prenom" class="w-full md:w-30rem mb-5" style="padding: 1rem" v-model="prenom" />

                        <label for="civilite1" class="block text-900 text-xl font-medium mb-2">Civilité</label>
                        <Dropdown
                        id="civilite1"
                        v-model="selectedCivilite"
                        :options="civiliteOptions"
                        optionLabel="label"
                        optionValue="value"
                        class="w-full md:w-30rem mb-5"
                        style="padding: .25rem"
                        />

                        <label for="profession1" class="block text-900 text-xl font-medium mb-2">Profession</label>
                        <InputText id="profession1" type="text" placeholder="Profession" class="w-full md:w-30rem mb-5" style="padding: 1rem" v-model="profession" />

                        <label for="dateNaissance1" class="block text-900 text-xl font-medium mb-2">Date de Naissance</label>
                        <Calendar id="dateNaissance1" placeholder="dd/mm/yyyy" :showIcon="true" :showButtonBar="true" class="w-full md:w-30rem mb-5" v-model="dateNaissance" dateFormat="dd/mm/yy"></Calendar>

                        <label for="telephone1" class="block text-900 text-xl font-medium mb-2">Telephone</label>
                        <InputText id="telephone1" type="text" placeholder="Telephone" class="w-full md:w-30rem mb-5" style="padding: 1rem" v-model="telephone" />

                        <label for="telephone21" class="block text-900 text-xl font-medium mb-2">Telephone 2</label>
                        <InputText id="telephone21" type="text" placeholder="Telephone 2" class="w-full md:w-30rem mb-5" style="padding: 1rem" v-model="telephone2" />

                        <label for="telephone31" class="block text-900 text-xl font-medium mb-2">Telephone 3</label>
                        <InputText id="telephone31" type="text" placeholder="Telephone 3" class="w-full md:w-30rem mb-5" style="padding: 1rem" v-model="telephone3" />

                        <label for="address1" class="block text-900 text-xl font-medium mb-2">Adresse</label>
                        <Textarea id="address1" rows="4" placeholder="Adresse" class="w-full md:w-30rem mb-5" style="padding: 1rem" v-model="adresse"/>


                        <label for="ville1" class="block text-900 text-xl font-medium mb-2">Ville</label>
                        <InputText id="ville1" placeholder="Ville" class="w-full md:w-30rem mb-5" style="padding: 1rem" v-model="ville"/>

  
                        <label for="codePostal1" class="block text-900 text-xl font-medium mb-2">Code Postal</label>
                        <InputText id="codePostal1" placeholder="Code Postal" class="w-full md:w-30rem mb-5" style="padding: 1rem" v-model="codePostal"/>

                        

                        <div class="flex align-items-center justify-content-between mb-5 gap-5"></div>


                        
                    
                        <div class="flex align-items-center justify-content-between mb-5 gap-5">
                            <Button label="Valider l'Inscription" class="w-full p-3 text-xl" @click="handleRegister"></Button>
                            <Button severity="secondary" label="Se Connecter" class="w-full p-3 text-xl" @click="redirectToLogin"></Button>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.pi-eye {
    transform: scale(1.6);
    margin-right: 1rem;
}

.pi-eye-slash {
    transform: scale(1.6);
    margin-right: 1rem;
}
</style>
