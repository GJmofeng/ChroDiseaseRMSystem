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
    proxy: {
      '/user': {
        target: 'http://154.12.36.159:8080',
        changeOrigin: true,
        secure: false,
        rewrite: path => path.replace(/^\/user/, '/user'),
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
          'Access-Control-Allow-Headers': 'Content-Type, Authorization'
        }
      },
      '/division': {
        target: 'http://154.12.36.159:8080',
        changeOrigin: true,
        secure: false,
        rewrite: path => path.replace(/^\/division/, '/division')
      },
      '/insureds': {
        target: 'http://154.12.36.159:8080',
        changeOrigin: true,
        secure: false,
        rewrite: path => path.replace(/^\/insureds/, '/insureds')
      },
      '/medicalCard': {
        target: 'http://154.12.36.159:8080',
        changeOrigin: true,
        secure: false,
        rewrite: path => path.replace(/^\/medicalCard/, '/medicalCard')
      },
      '/disease': {
        target: 'http://154.12.36.159:8080',
        changeOrigin: true,
        secure: false,
        rewrite: path => path.replace(/^\/disease/, '/disease')
      },
      '/policy': {
        target: 'http://154.12.36.159:8080',
        changeOrigin: true,
        secure: false,
        rewrite: path => path.replace(/^\/policy/, '/policy')
      }
    }
  }
})

// 关闭eslint检查

export const eslint = {
  lintOnSave: false
}


