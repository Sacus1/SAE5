<script setup>
import { ref, onMounted } from "vue";

/*
const produtcts = [{
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
const products = ref([{
    idPanier: '0',
    nom: 'panier1',
    prix: 'prixPanier1',
    image: 'imagePanier1',
    produits: [
        {
            nom: 'produit1',
            image: 'imageProduit1',
            description: 'descriptionProduit1',
            quantite: 'quantiteProduit1',
            unite: 'uniteProduit1',
        },
        {
            nom: 'produit2',
            image: 'imageProduit2',
            description: 'descriptionProduit2',
            quantite: 'quantiteProduit2',
            unite: 'uniteProduit2',
        }
    ],
},
{
    idPanier: '1',
    nom: 'panier2',
    prix: 'prixPanier2',
    image: 'imagePanier2',
    produits: [
        {
            nom: 'produit3',
            image: 'imageProduit3',
            description: 'descriptionProduit3',
            quantite: 'quantiteProduit3',
            unite: 'uniteProduit3',
        },
        {
            nom: 'produit4',
            image: 'imageProduit4',
            description: 'descriptionProduit4',
            quantite: 'quantiteProduit4',
            unite: 'uniteProduit4',
        }
    ],
}
]);

onMounted(() => {
    //ProductService.getProducts().then((data) => (products.value = data.slice(0, 12)));
});
const layout = ref('grid');

</script>
    
<template>
    <div class="card">
        <div class="grid grid-nogutter">
            <div v-for="(item, index) in products" :key="index" class="col-12 sm:col-6 lg:col-12 xl:col-4 p-2">
                <div class="p-4 border-1 surface-border surface-card border-round">
                    <div class="flex flex-wrap align-items-center justify-content-between gap-2">
                        <div class="flex align-items-center gap-2">
                            <i class="pi pi-tag"></i>
                            <span class="font-semibold">{{ item.category }}</span>
                        </div>
                    </div>
                    <div class="flex flex-column align-items-center gap-3 py-5">
                        <img class="w-9 shadow-2 border-round" src="logo-arrosoir-cocagne-vert.svg" :alt="item.name" />
                        <div class="text-2xl font-bold">{{ item.name }}</div>
                    </div>
                    <div class="flex align-items-center justify-content-between">
                        <span class="text-2xl font-semibold">${{ item.price }}</span>
                        <Button icon="pi pi-shopping-cart" rounded></Button>
                    </div>
                </div>
                <div v-for="(item2, index2) in item.produits" :key="index2">
                    <span class="font-semibold">{{ item.nom }}</span>
                </div>
            </div>
        </div>
    </div>
</template>