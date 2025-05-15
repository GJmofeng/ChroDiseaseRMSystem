<template>
  <div class="register-container">
    <!-- 装饰元素 -->
    <div class="decoration decoration-1"></div>
    <div class="decoration decoration-2"></div>

    <!-- 注册容器 -->
    <div class="register-box">
      <div class="register-title">
        <i class="fas fa-hospital"></i>
        <span>慢性病报销系统 - 用户注册</span>
      </div>

      <form @submit.prevent="handleRegister">
        <!-- 用户名输入 -->
        <div class="form-group">
          <i class="fas fa-user"></i>
          <input 
            type="text" 
            v-model="formData.username" 
            class="form-input" 
            placeholder="请输入用户名" 
            required
          >
        </div>

        <!-- 密码输入 -->
        <div class="form-group">
          <i class="fas fa-lock"></i>
          <input 
            type="password" 
            v-model="formData.password" 
            class="form-input" 
            placeholder="请输入密码" 
            required
          >
        </div>

        <!-- 确认密码 -->
        <div class="form-group">
          <i class="fas fa-lock"></i>
          <input 
            type="password" 
            v-model="formData.confirmPassword" 
            class="form-input" 
            placeholder="请确认密码" 
            required
          >
        </div>

        <!-- 手机号 -->
        <div class="form-group">
          <i class="fas fa-mobile-alt"></i>
          <input 
            type="tel" 
            v-model="formData.phone" 
            class="form-input" 
            placeholder="请输入手机号" 
            required
          >
        </div>

        <!-- 验证码 -->
        <div class="form-group">
          <div class="captcha-group">
            <div class="captcha-input">
              <i class="fas fa-shield-alt"></i>
              <input 
                type="text" 
                v-model="formData.captcha" 
                class="form-input" 
                placeholder="请输入验证码" 
                required
              >
            </div>
            <div class="captcha-image" @click="refreshCaptcha">
              {{ captchaCode }}
            </div>
          </div>
        </div>

        <!-- 注册按钮 -->
        <button type="submit" class="register-btn">注 册</button>

        <!-- 返回登录 -->
        <div class="login-option">
          <span>已有账号？</span>
          <router-link to="/login" class="login-link">返回登录</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const captchaCode = ref('1234')

const formData = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  phone: '',
  captcha: ''
})

// 刷新验证码
const refreshCaptcha = () => {
  captchaCode.value = Math.floor(1000 + Math.random() * 9000).toString()
}

// 处理注册
const handleRegister = () => {
  // 验证密码是否一致
  if (formData.password !== formData.confirmPassword) {
    alert('两次输入的密码不一致')
    return
  }

  // 验证验证码
  if (formData.captcha !== captchaCode.value) {
    alert('验证码错误')
    return
  }

  // TODO: 调用注册 API
  console.log('注册信息：', formData)
  
  // 注册成功后跳转到登录页
  router.push('/login')
}

// 页面加载时生成验证码
refreshCaptcha()
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f8f9fc 0%, #f1f4f9 100%);
  position: relative;
  overflow: hidden;
}

.decoration {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(135deg, #4c84ff 0%, #2d5cfe 100%);
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

.register-box {
  width: 400px;
  padding: 40px;
  background: linear-gradient(145deg, #ffffff, #f5f7fa);
  border-radius: 24px;
  box-shadow: 
    20px 20px 60px rgba(174, 174, 192, 0.2),
    -20px -20px 60px rgba(255, 255, 255, 0.9),
    inset 1px 1px 2px rgba(255, 255, 255, 0.3);
  position: relative;
  z-index: 1;
}

.register-title {
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

.register-title i {
  color: #4c84ff;
  font-size: 28px;
}

.form-group {
  margin-bottom: 24px;
  position: relative;
}

.form-input {
  width: calc(100% - 56px);
  padding: 12px 16px 12px 40px;
  border: 1px solid #eef0f5;
  border-radius: 12px;
  font-size: 14px;
  transition: all 0.3s ease;
  background: white;
  box-shadow: 
    inset 2px 2px 5px rgba(174, 174, 192, 0.1),
    inset -2px -2px 5px rgba(255, 255, 255, 0.5);
}

.form-input:focus {
  border-color: #4c84ff;
  box-shadow: 
    0 0 0 3px rgba(76, 132, 255, 0.1),
    inset 2px 2px 5px rgba(174, 174, 192, 0.1),
    inset -2px -2px 5px rgba(255, 255, 255, 0.5);
  outline: none;
}

.form-group i {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
}

.captcha-group {
  display: flex;
  gap: 12px;
}

.captcha-input {
  flex: 1;
}

.captcha-input .form-input {
  width: calc(100% - 56px);
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
  -webkit-user-select: none;
  user-select: none;
  box-shadow: 
    2px 2px 5px rgba(174, 174, 192, 0.2),
    -2px -2px 5px rgba(255, 255, 255, 0.9);
}

.register-btn {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #4c84ff 0%, #2d5cfe 100%);
  color: white;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.register-btn::before {
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

.register-btn:hover::before {
  opacity: 1;
  transform: rotate(30deg) translate(50%, -50%);
}

.register-btn:active {
  transform: scale(0.98);
}

.login-option {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.login-link {
  color: #4c84ff;
  text-decoration: none;
  margin-left: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.login-link:hover {
  text-decoration: underline;
}
</style> 