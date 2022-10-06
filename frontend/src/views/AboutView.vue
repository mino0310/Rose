<template>
  <div class="about">

    <h1>This is an about page</h1>
    <button v-on:click="firstClick">TEST 요청</button>

  </div>
</template>

<script>

export default {
  name: 'AboutView',
  components: {},
  beforeCreate() {

  },
  methods: {
    firstClick() {
      console.log('눌렀습니다');
      console.log('요청 보냅니다.');
      // this.$http.get('hello');
      this.$http.get('hello', {
        method: 'get',
        params: {
          te: 'forTest',
        },
      });
    },
  },

};
</script>
