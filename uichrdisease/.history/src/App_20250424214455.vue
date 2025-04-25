<template>
  <router-view></router-view>
</template>

<script setup>
import 'element-plus/dist/index.css'
</script>

<style>
:root {
  --primary-color: #4c84ff;
  --primary-gradient: linear-gradient(135deg, #4c84ff 0%, #2d5cfe 100%);
  --surface-color: #ffffff;
  --background-color: #f8f9fc;
  --shadow-color: rgba(174, 174, 192, 0.2);
  --highlight-color: rgba(255, 255, 255, 0.9);
}

body {
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  background: linear-gradient(135deg, var(--background-color) 0%, #f1f4f9 100%);
  min-height: 100vh;
  position: relative;
}

/* 全局光效 */
body::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(
    circle at 50% 0%,
    rgba(255,255,255,0.8) 0%,
    rgba(255,255,255,0) 75%
  );
  pointer-events: none;
  z-index: 1;
}
</style>
