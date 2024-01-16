<script setup>
import { useAuth } from '@/service/auth';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const { login } = useAuth();
const email = ref('');
const password = ref('');

const handleLogin = async () => {
  try {
    await login(email.value, password.value);
    // Optionally, you can navigate to another route upon successful login.
    router.push('/');
  } catch (error) {
    // Handle login error, e.g., show an error message.
    console.error('Login failed:', error);
  }
};

const redirectToRegister = () => {
    router.push('/register');
}

</script>

<template>
    <div class="surface-ground flex align-items-center justify-content-center min-h-screen min-w-screen overflow-hidden">
        <div class="flex flex-column align-items-center justify-content-center">
            <img src="/logo-arrosoir-cocagne-vert.svg" alt="Cocagne logo" class="mb-1 w-10rem flex-shrink-0" />
            <div style="border-radius: 56px; padding: 0.3rem; background: var(--primary-color)">
                <div class="w-full surface-card py-8 px-5 sm:px-8" style="border-radius: 53px">
                    <div class="text-center mb-5">
                        <span class="text-primary-600 font-medium text-2xl">Sign in to continue</span>
                    </div>

                    <div>
                        <label for="email1" class="block text-900 text-xl font-medium mb-2">Email</label>
                        <InputText id="email1" type="text" placeholder="Email address" class="w-full md:w-30rem mb-5" style="padding: 1rem" v-model="email" />

                        <label for="password1" class="block text-900 font-medium text-xl mb-2">Password</label>
                        <Password id="password1" v-model="password" placeholder="Password" :toggleMask="true" class="w-full mb-3" inputClass="w-full" :inputStyle="{ padding: '1rem' }"></Password>

                        <div class="flex align-items-center justify-content-between mb-5 gap-5">
                            
                            <a class="font-medium no-underline ml-2 text-right cursor-pointer" style="color: var(--primary-color)">Forgot password?</a>
                        </div>
                        <div class="flex align-items-center justify-content-between mb-5 gap-5">
                            <Button label="Sign In" class="w-full p-3 text-xl" @click="handleLogin"></Button>
                            <Button label="Register" class="w-full p-3 text-xl" @click="redirectToRegister"></Button>
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
