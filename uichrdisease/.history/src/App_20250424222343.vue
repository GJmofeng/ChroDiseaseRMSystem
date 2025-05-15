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

/* 新拟态基础样式 */
.neumorphic {
  background: linear-gradient(145deg, var(--surface-color), #f5f7fa);
  box-shadow: 
    8px 8px 16px var(--shadow-color),
    -8px -8px 16px var(--highlight-color),
    inset 1px 1px 2px rgba(255, 255, 255, 0.3),
    inset -1px -1px 2px rgba(174, 174, 192, 0.1);
  border-radius: 16px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 3D描边动画 */
@keyframes borderGlow {
  0% {
    box-shadow: 
      0 0 0 0 rgba(76, 132, 255, 0.4),
      inset 0 0 0 0 rgba(76, 132, 255, 0.4);
  }
  50% {
    box-shadow: 
      0 0 10px 2px rgba(76, 132, 255, 0.2),
      inset 0 0 5px 1px rgba(76, 132, 255, 0.2);
  }
  100% {
    box-shadow: 
      0 0 0 0 rgba(76, 132, 255, 0.4),
      inset 0 0 0 0 rgba(76, 132, 255, 0.4);
  }
}

/* 卡片纹理效果 */
.card-texture {
  position: relative;
  overflow: hidden;
}

.card-texture::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(
    125deg,
    rgba(255,255,255,0.3) 0%,
    rgba(255,255,255,0.1) 40%,
    transparent 60%
  );
  transform: translateZ(20px);
  transition: all 0.3s ease;
}

.card-texture:hover::before {
  transform: translateZ(30px) rotate(5deg);
  opacity: 0.8;
}

/* 按钮效果增强 */
.btn {
  background: var(--primary-gradient);
  box-shadow: 
    4px 4px 8px var(--shadow-color),
    -4px -4px 8px var(--highlight-color),
    inset 1px 1px 2px rgba(255, 255, 255, 0.3);
  border: none;
  position: relative;
  overflow: hidden;
  transform-style: preserve-3d;
}

.btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    120deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  transition: 0.5s;
}

.btn:hover::before {
  left: 100%;
}

.btn:active {
  transform: scale(0.98);
  box-shadow: 
    2px 2px 4px var(--shadow-color),
    -2px -2px 4px var(--highlight-color),
    inset 2px 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
