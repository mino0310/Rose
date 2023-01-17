<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";

// const userId = ref("");
// const password = ref("");
// const address = ref("");
// const name = ref("");
const title = ref("");
const content = ref("");

const create = function () {
  let data = {
    title: title.value,
    content: content.value

    // userId: userId.value,
    // name: name.value,
    // address: address.value
  };
  axios.post("http://localhost:8090/posts", data);
};

</script>
<template>
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요"></el-input>
  </div>
  <div>
    <el-input v-model="content" type="textarea" rows="15"></el-input>
  </div>

  <div class="mt-2">
    <el-button type="primary" @click="create">게시글 생성</el-button>
  </div>
</template>

<style scoped>

</style>