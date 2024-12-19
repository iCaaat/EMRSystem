const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,


// //   代理
//   devServer: {
//     proxy: {
//       '/api': {
//         target: 'http://localhost:9090',
//         changeOrigin: true,
//         pathRewrite: { '^/api': '' }
//       }
//     }
//   }
})
