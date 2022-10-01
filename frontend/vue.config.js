const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3000,
    // 개발시 front 에서 localhost로 요청을 보낼 경우에 url 과 포트를 다르게 지정해줄 수 있음
    proxy: "http://localhost:8080"
  },
  outputDir: "../src/main/resources/static",
  lintOnSave: false,
});
