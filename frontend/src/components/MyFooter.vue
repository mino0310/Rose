<template>
  <v-footer
      dark
      padless
  >
    <v-card
        flat
        tile
        class="indigo lighten-1 white--text text-center"
    >
      <v-card-text>
        <v-btn
            v-for="icon in icons"
            :key="icon.name"
            class="mx-4 white--text"
            icon
            :href="icon.link"
            target="_blank"
        >
          <v-icon size="24px">
            {{ icon.name }}
          </v-icon>
        </v-btn>
      </v-card-text>

      <v-card-text class="white--text">
        너는 할 수 있어. 나는 할 수 있어. 반드시 해낼 수 있어. 해낼 거야!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      </v-card-text>

      <v-divider></v-divider>

      <v-card-text class="white--text">
        {{ new Date().getFullYear() }} — <strong>Vuetify</strong>
      </v-card-text>
    </v-card>
  </v-footer>
</template>
<script>
export default {
  name: 'myFooter',
  props: {
  },
  data(){
    return {
      icons: [
        {name:'mdi-facebook', link:'https://www.facebook.com'},
        {name:'mdi-twitter', link:'https://twitter.com'},
        {name:'mdi-linkedin', link:'https://www.linkedin.com'},
        {name:'mdi-instagram', link:'https://www.instagram.com'},
      ]
    }
  }
}
</script>
