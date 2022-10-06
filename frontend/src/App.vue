<template>
  <v-app>
    <myHeader/>
    <v-main>
      <router-view/>
    </v-main>
    <myFooter/>
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

    }
  }

};
</script>
