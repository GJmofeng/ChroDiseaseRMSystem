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
      '/api': {
        target: 'http://154.12.36.159:8080',
        changeOrigin: true,
        secure: false,
        configure: (proxy, options) => {
          proxy.on('proxyReq', (proxyReq, req, res) => {
            proxyReq.setHeader('Access-Control-Allow-Origin', '*')
            proxyReq.setHeader('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,PATCH,OPTIONS')
            proxyReq.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization')
          })
        }
      }
    }
  }
})

// 关闭eslint检查

export const eslint = {
  lintOnSave: false
}


