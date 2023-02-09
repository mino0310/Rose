<script setup lang="ts">

import axios from "axios";

import {ref, defineProps} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const post = ref();

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
});

axios.get(`/api/posts/${props.postId}`).then((res) => {
  post.value = res.data;
})

const edit = () => {
  axios.patch(`/api/posts/${props.postId}`, post.value).then(() => {
    router.replace({name: 'home'});
  })
}


</script>

<template>
  <div>
    <el-input v-model="post.title"></el-input>
  </div>
  <div>
    <el-input v-model="post.content" type="textarea" rows="15" @keydown.native.enter="create"></el-input>
  </div>

  <div class="mt-2">
    <el-button type="warning" @click="edit()">수정 완료</el-button>
  </div>
</template>

<style scoped>
li {
  margin-bottom: 1rem;
}

li:last-child {
  margi-bottom: 0;
}
</style>