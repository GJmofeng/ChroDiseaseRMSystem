<template>
  <div></div>
</template>

<script setup>
import { onBeforeMount } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

onBeforeMount(() => {
  const { params } = route
  const { path } = params
  
  // 获取原始路径
  const redirectPath = '/' + path
  
  // 重定向到原始路径
  router.replace(redirectPath)
})
</script> 