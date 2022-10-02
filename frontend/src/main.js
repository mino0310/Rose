import Vue from 'vue';
import Axios from 'axios';
import router from './router';
import store from './store';
import App from './App.vue';
import vuetify from './plugins/vuetify';

Vue.config.productionTip = false;
Vue.prototype.$http = Axios;

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount('#app');
