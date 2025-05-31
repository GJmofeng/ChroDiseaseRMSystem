import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
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
        rewrite: (path) => path.replace(/^\/api/, ''),
        configure: (proxy, options) => {
          proxy.on('proxyReq', (proxyReq, req, res) => {
            // 设置CORS头
            proxyReq.setHeader('Access-Control-Allow-Origin', '*')
            proxyReq.setHeader('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,PATCH,OPTIONS')
            proxyReq.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization, X-Requested-With')
            proxyReq.setHeader('Access-Control-Allow-Credentials', 'true')
          })
          proxy.on('proxyRes', (proxyRes, req, res) => {
            // 确保响应头中包含CORS信息
            proxyRes.headers['Access-Control-Allow-Origin'] = '*'
            proxyRes.headers['Access-Control-Allow-Methods'] = 'GET,PUT,POST,DELETE,PATCH,OPTIONS'
            proxyRes.headers['Access-Control-Allow-Headers'] = 'Content-Type, Authorization, X-Requested-With'
            proxyRes.headers['Access-Control-Allow-Credentials'] = 'true'
          })
        },
        timeout: 15000 // 添加代理超时设置
      }
    }
  }
})

// 关闭eslint检查

export const eslint = {
  lintOnSave: false
}


