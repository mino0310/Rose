<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import router from "@/router";

const posts = ref([]);
axios.get("/api/posts?page=1&size=5").then((response) => {
    response.data.forEach((r: any) => {
      posts.value.push(r);
      console.log(r);
    });
});

const moveToRead = () => {
  router.push({name:"read"})
}
</script>
<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div class="title">
        <router-link :to="{name: 'read', params:{postId: post.id}}">{{ post.title}} </router-link>
      </div>
      <div class="content">
        {{ post.content }}
      </div>

      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2023-02-08</div>
      </div>
    </li>
  </ul>
</template>


<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 1.6rem;

    .title {
      a{
        font-size: 1.2rem;
        color: #303030;
        text-decoration: none;
      }

      &:hover {
        text-decoration: underline;
      }
    }
    .content {
      font-size: 0.95rem;
      margin-top: 8px;
      color: #5d5d5d;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    &:last-child {
      margin-bottom: 0;
    }

    .sub {
      margin-top: 4px;
      font-size: 0.8rem;

      .regDate {
        margin-left: 10px;
      }
    }
  }
}
</style>
