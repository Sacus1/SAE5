<script setup>
import { ref, onMounted } from "vue";
import {getPaniers} from "@/api/paniersAPI"

/*
const produits = [{
    idPanier: '',
    nom : '',
    prix : '',
    image : '',
    produits : [
        {
            nom: '',
            image: '',
            description: '',
            quantite: '',
            unite: '',
        }
    ],
}]
*/

const paniers = ref([{
    idPanier: '0',
    nom: 'panier1',
    prix: 'prixPanier1',
    image: 'imagePanier1',
},
{
    idPanier: '1',
    nom: 'panier2',
    prix: 'prixPanier2',
    image: 'imagePanier2',
}
]);


onMounted(async () => {
    // Fetch jardins
    const paniersData = await getPaniers();
    paniers.value.push(...paniersData);
});
    

</script>
    
<template>
    <div class="card">
        <div class="grid grid-nogutter">
            <div v-for="(item, index) in paniers" :key="index" class="col-12 sm:col-6 lg:col-12 xl:col-4 p-2">
                <div class="p-4 border-1 surface-border surface-card border-round">
                    <div class="flex flex-column align-items-center gap-3 py-5">
                        <Image class="w-9 shadow-2 border-round" :src="item.image" :alt="item.name" />
                        <div class="text-2xl font-bold">{{ item.nom }}</div>
                    </div>
                    <div class="flex align-items-center justify-content-between">
                        <span class="text-2xl font-semibold">{{ item.prix }} €</span>
                        <Button icon="pi pi-shopping-cart" rounded></Button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>