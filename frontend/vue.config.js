const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    // port: 3000,
    proxy: "http://localhost:8080"
  },
  outputDir: "../src/main/resources/static",
});
