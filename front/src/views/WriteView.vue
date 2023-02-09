<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

// const userId = ref("");
// const password = ref("");
// const address = ref("");
// const name = ref("");
const title = ref("");
const content = ref("");
const router = useRouter();

const create = function () {
  let data = {
    title: title.value,
    content: content.value

    // userId: userId.value,
    // name: name.value,
    // address: address.value
  };
  // 프록시로 요청을 바꿀 것이므로 풀패스를 적어주면 안 된다.
  axios.post("/api/posts", data).then(
      () => {
        router.replace({name: "home"});
      }
  );
};

</script>
<template>
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요"></el-input>
  </div>
  <div>
    <el-input v-model="content" type="textarea" rows="15" @keydown.native.enter="create"></el-input>
  </div>

  <div class="mt-2">
    <div class="d-flex justify-content-end">
      <el-button type="primary" @click="create">작성완료</el-button>
    </div>
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