import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router/index.js";
import axios from "axios";
import 'element-plus/dist/index.css'
import ElementPlus from 'element-plus';

const app = createApp(App)

axios.defaults.baseURL = 'http://localhost:8080'

app.use(router)

app.use(ElementPlus);

app.mount('#app')


