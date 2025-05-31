import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import axios from 'axios'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  css: {
    postcss: {
      plugins: [
        require('tailwindcss'),
        require('autoprefixer')
      ]
    }
  },
  server: {
    host: '0.0.0.0',
    port: 80,
    proxy: {
      '/api': {
        target: 'http://154.12.36.159:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
        secure: false,
        ws: true
      }
    }
  }
})

// 关闭eslint检查
export const eslint = {
  lintOnSave: false
}


