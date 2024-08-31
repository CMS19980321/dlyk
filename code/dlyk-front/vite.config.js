import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server:{
    host:'0.0.0.0' ,//ip地址
    port: 8081, // 设置服务启动端口号
    open: false, // 设置服务启动时是否自动打开浏览器
  }
})
