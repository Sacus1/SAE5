<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import {fetchAdhesionData} from '@/api/adhesionAPI';


const adhesions = ref([{
    id: 1,
    jardin:"Nom du jardin",
    type:"Type d'adhésion",
    prix:"Prix",
    debut:"Date de début",
    fin:"Date de fin",
    enCours:true
},
{
    id: 2,
    jardin:"Nom du jardin 2",
    type:"Type d'adhésion 2",
    prix:"Prix 2",
    debut:"Date de début 2",
    fin:"Date de fin 2",
    enCours:false
}]);

const annulerAdhesion = async(id) =>{
    console.log(id);
}

onMounted(async () => {
    // Fetch jardins
    const adhesionData = await fetchAdhesionData();
    adhesions.value.push(...adhesionData);
});

</script>

<template>
    <div class="card">
        <DataTable showGridlines  :value="adhesions" tableStyle="min-width: 50rem">
            <Column field="jardin" header="Jardin" bodyClass="text-center" ></Column>
            <Column field="type" header="Type d'Adhésion" bodyClass="text-center"></Column>
            <Column field="prix" header="Prix" bodyClass="text-center"></Column>
            <Column field="debut" header="Date de Début" bodyClass="text-center"></Column>
            <Column field="fin" header="Date de Fin" bodyClass="text-center"></Column>
            <Column field="enCours" header="En Cours" dataType="boolean" bodyClass="text-center" style="min-width: 8rem">
                <template #body="{ data }">
                    <i class="pi" :class="{ 'pi-check-circle text-green-500 ': data.enCours, 'pi-times-circle text-red-500': !data.enCours }"></i>
                </template>
            </Column>
            <Column bodyClass="text-center">
                <template #body="{ data }">
                    <Button type="button" label="Annuler" class="p-button-danger" @click="annulerAdhesion(data.id)"></Button>
                </template>
            </Column>
        </DataTable>
    </div>
</template>
