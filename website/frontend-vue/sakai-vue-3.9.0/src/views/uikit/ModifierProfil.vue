<script setup>
import { useAuth } from '@/service/auth';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const { fetchClientData } = useAuth();
const emailValue = ref('');
const emailPlaceholder = ref('');
const passwordValue = ref('');
const passwordPlaceholder = ref('');
const thirdFieldValue = ref('');
const thirdFieldPlaceholder = ref('');

onMounted(async () => {
  // Fetch placeholders from the DB when the component is mounted
  placeholders.value = await fetchClientData();
  
  
});
</script>

<template>
    <div class="col-12 md:col-8 col-offset-2">
        <div class="card p-fluid">
                <h5>Modifier vos informations</h5>
                <div class="field grid">
                    <label for="email" class="col-12 mb-2 md:col-2 md:mb-0">Adresse mail</label>
                    <div class="col-12 md:col-10">
                        <InputText id="email" type="text" :placeholder="emailPlaceholder" v-model="emailValue"/>
                    </div>
                </div>
                <div class="field grid">
                    <label for="password" class="col-12 mb-2 md:col-2 md:mb-0">Password</label>
                    <div class="col-12 md:col-10">
                        <Password id="password" v-model="password" placeholder="Password" :toggleMask="true"></Password>
                    </div>
                </div>
                <div class="field grid">
                    <label for="email3" class="col-12 mb-2 md:col-2 md:mb-0">Email</label>
                    <div class="col-12 md:col-10">
                        <InputText id="email3" type="text" />
                    </div>
                </div>
                <div class="field grid">
                    <label for="email3" class="col-12 mb-2 md:col-2 md:mb-0">Email</label>
                    <div class="col-12 md:col-10">
                        <InputText id="email3" type="text" />
                    </div>
                </div>
            </div>
    </div>
</template>
