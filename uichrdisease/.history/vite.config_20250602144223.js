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
        // target: 'http://154.12.36.159:8080',
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/api/, ''),
        configure: (proxy, options) => {
          proxy.on('proxyReq', (proxyReq, req, res) => {
            // 设置CORS头
            // proxyReq.setHeader('Access-Control-Allow-Origin', 'http://154.12.36.159')
            proxyReq.setHeader('Access-Control-Allow-Origin', 'http://localhost')
            proxyReq.setHeader('Access-Control-Allow-Methods', 'GET,POST,PUT,DELETE,OPTIONS,PATCH')
            proxyReq.setHeader('Access-Control-Allow-Headers', 'Content-Type,X-CAF-Auth-Token,sessionToken,token,customercoderoute,authorization,conntectionid,Cookie,request-ajax,X-Requested-With')
            proxyReq.setHeader('Access-Control-Allow-Credentials', 'true')
            proxyReq.setHeader('Access-Control-Expose-Headers', 'Content-Length,Content-Range,Authorization,token')
          })
          proxy.on('proxyRes', (proxyRes, req, res) => {
            // 确保响应头中包含CORS信息
            proxyRes.headers['Access-Control-Allow-Origin'] = 'http://154.12.36.159'
            proxyRes.headers['Access-Control-Allow-Methods'] = 'GET,POST,PUT,DELETE,OPTIONS,PATCH'
            proxyRes.headers['Access-Control-Allow-Headers'] = 'Content-Type,X-CAF-Auth-Token,sessionToken,token,customercoderoute,authorization,conntectionid,Cookie,request-ajax,X-Requested-With'
            proxyRes.headers['Access-Control-Allow-Credentials'] = 'true'
            proxyRes.headers['Access-Control-Expose-Headers'] = 'Content-Length,Content-Range,Authorization,token'
          })
        },
        timeout: 15000
      }
    }
  }
})

// 关闭eslint检查

export const eslint = {
  lintOnSave: false
}


