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
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/user/, '/user')
      }
    }
  }
})

// 关闭eslint检查

export const eslint = {
  lintOnSave: false
}

function onBatchDelete() {
  if (!multipleSelection.value.length) {
    ElMessage.warning('请先选择要删除的成员')
    return
  }
  ElMessageBox.confirm('确定要删除选中的成员吗？', '提示', {
    type: 'warning',
  }).then(async () => {
    const ids = multipleSelection.value.map(u => u.id)
    try {
      await axios.post('/user/deleteBatch', ids)
      ElMessage.success('删除成功')
      fetchUsers()
      multipleSelection.value = []
    } catch (e) {
      ElMessage.error('删除失败')
    }
  })
}

