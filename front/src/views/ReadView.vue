<script setup lang="ts">
import {onMounted, defineProps, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const props = defineProps({
  postId: {
    type: [Number, String],
    requires: true,
  }
});

const router = useRouter();

const post = ref({
  id: 0,
  title: "",
  content: ""
});

onMounted(()=> {
  axios.get(`/api/posts/${props.postId}`).then((res) => {
    post.value = res.data;
    console.log(post);
  })

})

const moveToEdit = () => {
  router.push({name: "edit", params: {postId: props.postId}});
}

</script>
<template>
  <el-row>
    <el-col>
      <h2>{{post.id}}</h2>

      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2023-02-08</div>
      </div>
    </el-col>
  </el-row>
  <el-row>
    <el-col>
      <div class="content">{{post.content}}</div>
    </el-col>
  </el-row>

  <el-row>
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit">수정</el-button>
      </div>
    </el-col>
  </el-row>
</template>
<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #303030;
  margin: 0px;
}

.content {
  font-size: 0.95rem;
  margin-top: 12px;
  color: #5d5d5d;
  white-space: break-spaces;
  line-height: 1.5;
}

.sub {
  margin-top: 6px;
  font-size: 0.8rem;

  .regDate {
    margin-left: 10px;
  }
}

</style>