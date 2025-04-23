import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
})

// 关闭eslint检查
// @ts-ignore
// @ts-ignore

export const v = {
  lintOnSave: false
}

