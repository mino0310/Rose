import { createApp } from "vue";
import { createPinia } from "pinia";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from "./App.vue";
import router from "./router";

import "./assets/main.css";

import "normalize.css";

// 추가로 사용하기 위해 import
import "bootstrap/dist/css/bootstrap-utilities.css"

const app = createApp(App);

app.use(createPinia());
app.use(ElementPlus);
app.use(router);

app.mount("#app");
