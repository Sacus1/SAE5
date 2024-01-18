<script setup>
import { useAuth } from '@/service/auth';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { DateTime } from "luxon";


const router = useRouter();
const { fetchClientData, updateClientData } = useAuth();


const refValues = ref({
  email: '',
  nom: '',
  prenom: '',
  telephone: '',
  telephone2: '',
  telephone3: '',
  dateNaissance: '',
  profession: '',
  adresse: '',
  ville: '',
  codePostal: ''
});

const savedDate = ref('');
onMounted(async () => {
    // Fetch refValues from the DB when the component is mounted
    const clientData = await fetchClientData();
    refValues.value = Object.assign({}, refValues.value, clientData);
    savedDate.value = refValues.value.dateNaissance;
    refValues.value.dateNaissance = '';
});

const updateProfil = async () => {
    await updateClientData(refValues.value);
    router.push('/');
};

</script>

<template>
    <div class="col-12 md:col-7 col-offset-2">
        <div class="card p-fluid">
            <h5>Modifier vos informations</h5>
            <div class="field grid">
                <div class="p-2">
                </div>
            </div>
            <div class="field grid">
                <label for="email" class="col-12 mb-2 md:col-3">Adresse Mail</label>
                <div class="col-12 md:col-8">
                    <InputText id="email" type="text" v-model="refValues.email"/>
                </div>
            </div>
            <div class="field grid">
                <label for="password" class="col-12 mb-2 md:col-3">Mot de passe</label>
                <div class="col-12 md:col-8">
                    <Password id="password" :toggleMask="true"></Password>
                </div>
            </div>
            <div class="field grid">
                <label for="nom" class="col-12 mb-2 md:col-3">Nom</label>
                <div class="col-12 md:col-8">
                    <InputText id="nom" type="text" v-model="refValues.nom"/>
                </div>
            </div>
            <div class="field grid">
                <label for="prenom" class="col-12 mb-2 md:col-3">Prenom</label>
                <div class="col-12 md:col-8">
                    <InputText id="prenom" type="text" v-model="refValues.prenom"/>
                </div>
            </div>
            <div class="field grid">
                <label for="telephone" class="col-12 mb-2 md:col-3">Telephone</label>
                <div class="col-12 md:col-8">
                    <InputText id="telephone" type="text" v-model="refValues.telephone"/>
                </div>
            </div>
            <div class="field grid">
                <label for="telephone2" class="col-12 mb-2 md:col-3">Telephone 2</label>
                <div class="col-12 md:col-8">
                    <InputText id="telephone2" type="text" v-model="refValues.telephone2"/>
                </div>
            </div>
            <div class="field grid">
                <label for="telephone3" class="col-12 mb-2 md:col-3">Telephone 3</label>
                <div class="col-12 md:col-8">
                    <InputText id="telephone3" type="text" v-model="refValues.telephone3"/>
                </div>
            </div>
            <div class="field grid">
                <label for="dateNaissance" class="col-12 mb-2 md:col-3">Date de Naissance</label>
                <div class="col-12 md:col-8">
                    <!--InputText id="dateNaissance" type="text" v-model="refValues.dateNaissance" v-model="dateNaissance"/-->
                    <Calendar id="dateNaissance" :placeholder="DateTime.fromISO(savedDate).setLocale('fr').toLocaleString()" v-model="refValues.dateNaissance" :manualInput="false" :showIcon="true" :showButtonBar="true" dateFormat="dd/mm/yy" />
                </div>
            </div>
            <div class="field grid">
                <label for="profession" class="col-12 mb-2 md:col-3">Profession</label>
                <div class="col-12 md:col-8">
                    <InputText id="profession" type="text" v-model="refValues.profession"/>
                </div>
            </div>
            <div class="field grid">
                <label for="adresse" class="col-12 mb-2 md:col-3">Adresse</label>
                <div class="col-12 md:col-8">
                    <!--InputText id="adresse" type="text" v-model="refValues.adresse" v-model="adresse"/-->
                    <Textarea id="address1" rows="4" v-model="refValues.adresse" autoResize />
                </div>
            </div>
            <div class="field grid">
                <label for="ville" class="col-12 mb-2 md:col-3">Ville</label>
                <div class="col-12 md:col-8">
                    <InputText id="ville" type="text" v-model="refValues.ville"/>
                </div>
            </div>
            <div class="field grid">
                <label for="codePostal" class="col-12 mb-2 md:col-3">Code Postal</label>
                <div class="col-12 md:col-8">
                    <InputText id="codePostal" type="text" v-model="refValues.codePostal"/>
                </div>
            </div>
            <Button label="Envoyer" @click="updateProfil" ></Button>
        </div>
    </div>
</template>
