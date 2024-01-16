<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';
import { getProfileImage, updateProfileImage } from '@/api/profileAPI';
import { useProfileImageStore } from '@/stores/profileImage'
import { storeToRefs } from 'pinia'


const toast = useToast();
const token = localStorage.getItem('token') || null;
const profileImageStore = useProfileImageStore()
const {profileImage} = storeToRefs(profileImageStore);
const uploadedFilesCount = ref(1);


const onUpload = async (event) => {
  try {

    const blobURL = event.files[0].objectURL;
    uploadedFilesCount.value++;

    await updateProfileImage(token, blobURL);
    toast.add({ severity: 'info', summary: 'Success', detail: 'File Uploaded', life: 3000 });
  } catch (error) {
    console.error('Error updating profile image:', error);
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to update profile image', life: 3000 });
  }
};

onMounted(async()=>{
    await getProfileImage(token);
})

</script>

<template>
    <div class="grid">
        <div class="col-12">
            <div class="card">
                <h5>Photo de Profil</h5>
                <div class="flex justify-content-center">
                    <Image :src=profileImage alt="Image" width="250" preview />
                </div>
            </div>
            <div class="card p-fluid">
                <h5>Upload Photo de Profil</h5>
                <FileUpload
                name="UploadPFP"
                @uploader="onUpload"
                accept="image/*"
                :fileLimit=uploadedFilesCount
                :maxFileSize="1000000"
                :multiple="false"
                customUpload
                />
            </div>
            <Toast />
        </div>
    </div>
</template>
