<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import {fetchAdhesionData, deleteAdhesionById} from '@/api/adhesionAPI';


const adhesions = ref([]);

const annulerAdhesion = async(id) =>{
    deleteAdhesionById(id);

    const index = adhesions.value.findIndex((adhesion) => adhesion.id === id);
    if (index !== -1) {
        adhesions.value.splice(index, 1);
    }
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
