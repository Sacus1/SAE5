<template>
    <CustomButton @click="onClick()" class="btn">{{ text }}</CustomButton>
</template>

<script>
export default({
    name:"MyButton",
    props:{
        text: String,
    },
    methods:{
        onClick(){
            console.log('click')
        }
    }
})
</script>
