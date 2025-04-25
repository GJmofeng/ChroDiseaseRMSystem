<template>
  <div class="login-page">
    <!-- 装饰元素 -->
    <div class="decoration decoration-1"></div>
    <div class="decoration decoration-2"></div>

    <!-- 登录容器 -->
    <div class="login-container neumorphic">
      <div class="login-title">
        <el-icon><House /></el-icon>
        <span>慢性病报销系统</span>
      </div>

      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" @submit.prevent="handleLogin">
        <!-- 用户名输入 -->
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
          />
        </el-form-item>

        <!-- 密码输入 -->
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <!-- 验证码 -->
        <el-form-item prop="captcha">
          <div class="captcha-group">
            <el-input
              v-model="loginForm.captcha"
              placeholder="请输入验证码"
              :prefix-icon="Key"
              class="captcha-input"
            />
            <div class="captcha-image" @click="refreshCaptcha">
              {{ captchaText }}
            </div>
          </div>
        </el-form-item>

        <!-- 记住我和忘记密码 -->
        <div class="extra-options">
          <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
          <el-link type="primary" :underline="false">忘记密码？</el-link>
        </div>

        <!-- 登录按钮 -->
        <el-button type="primary" class="login-btn" native-type="submit" :loading="loading">
          登 录
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { House, User, Lock, Key } from '@element-plus/icons-vue'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)
const captchaText = ref('')

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  captcha: '',
  remember: false
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}

// 刷新验证码
const refreshCaptcha = () => {
  captchaText.value = Math.floor(1000 + Math.random() * 9000).toString()
}

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    // 这里添加实际的登录逻辑
    // 模拟登录请求
    setTimeout(() => {
      loading.value = false
      ElMessage.success('登录成功')
      router.push('/')
    }, 1000)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 页面加载时生成验证码
refreshCaptcha()
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

/* 装饰元素 */
.decoration {
  position: absolute;
  border-radius: 50%;
  background: var(--primary-gradient);
  opacity: 0.1;
  filter: blur(40px);
}

.decoration-1 {
  width: 300px;
  height: 300px;
  top: -150px;
  left: -150px;
}

.decoration-2 {
  width: 200px;
  height: 200px;
  bottom: -100px;
  right: -100px;
  background: linear-gradient(135deg, #FF6B6B 0%, #FF8E53 100%);
}

.login-container {
  width: 400px;
  padding: 40px;
  position: relative;
  overflow: hidden;
  z-index: 2;
}

.login-container::before {
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

.login-container:hover::before {
  transform: translateZ(30px) rotate(5deg);
  opacity: 0.8;
}

.login-title {
  text-align: center;
  margin-bottom: 40px;
  color: #1a1a1a;
  font-size: 24px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.login-title .el-icon {
  font-size: 28px;
  color: var(--primary-color);
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-form-item__content) {
  display: flex;
  justify-content: center;
}

:deep(.el-input) {
  width: 100%;
}

:deep(.el-input__wrapper) {
  background: white !important;
  box-shadow: 
    inset 2px 2px 5px rgba(174, 174, 192, 0.1),
    inset -2px -2px 5px rgba(255, 255, 255, 0.5) !important;
  border-radius: 12px !important;
  padding: 0 16px !important;
  width: 100% !important;
  box-sizing: border-box !important;
  position: relative !important;
  overflow: hidden !important;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 
    0 0 0 3px rgba(76, 132, 255, 0.1),
    inset 2px 2px 5px rgba(174, 174, 192, 0.1),
    inset -2px -2px 5px rgba(255, 255, 255, 0.5) !important;
}

:deep(.el-input__inner) {
  height: 42px !important;
  border: none !important;
  background: transparent !important;
}

:deep(.el-input__prefix) {
  margin-right: 8px !important;
}

:deep(.el-input__suffix) {
  margin-left: 8px !important;
}

.captcha-group {
  display: flex;
  gap: 12px;
  width: 100%;
}

.captcha-input {
  flex: 1;
}

.captcha-image {
  width: 100px;
  height: 42px;
  border-radius: 8px;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 18px;
  font-weight: bold;
  color: #666;
  user-select: none;
  -webkit-user-select: none;
  box-shadow: 
    2px 2px 5px var(--shadow-color),
    -2px -2px 5px var(--highlight-color);
  transition: all 0.3s ease;
}

.captcha-image:hover {
  transform: scale(1.05);
}

.extra-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 16px 0;
}

.login-btn {
  width: 100%;
  height: 42px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 500;
  background: var(--primary-gradient);
  border: none;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.login-btn::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.3) 0%, rgba(255,255,255,0) 60%);
  transform: rotate(30deg);
  transition: 0.5s;
  opacity: 0;
}

.login-btn:hover::before {
  opacity: 1;
  transform: rotate(30deg) translate(50%, -50%);
}

.login-btn:active {
  transform: scale(0.98);
}
</style> 