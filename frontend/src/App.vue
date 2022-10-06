<template>
  <v-app>
    <myHeader/>
    <v-main>
      <router-view/>
    </v-main>
    <myFooter :icons="icons"/>
  </v-app>
</template>

<script>

import MyFooter from "@/components/MyFooter";
import MyHeader from "@/components/MyHeader";

export default {
  name: 'App',
  components: {MyHeader, MyFooter},
  data() {
    return {
      icons: [
        {name: 'mdi-facebook', link: 'www.naver.com'},
        {name: 'mdi-twitter', link: 'www.naver.com'},
        // 'mdi-linkedin',
        // 'mdi-instagram',
      ]
    }
  }

};
</script>
