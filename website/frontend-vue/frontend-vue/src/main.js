import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import PrimeVue from "primevue/config";
import "primevue/resources/themes/lara-light-green/theme.css";
import Button from "primevue/button";

const app = createApp(App);
app.use(PrimeVue);

app.component("CustomButton", Button);

app.use(store).use(router).mount("#app");
