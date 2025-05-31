<template>
  <div class="login-container">
    <!-- 粒子动画容器 -->
    <div class="canvas-container" ref="canvasContainer"></div>
    
    <!-- 装饰元素 -->
    <div class="decoration decoration-1"></div>
    <div class="decoration decoration-2"></div>

    <!-- 登录容器 -->
    <div class="login-box">
      <div class="login-title">
        <i class="fas fa-hospital"></i>
        <span>慢性病报销系统</span>
      </div>

      <form @submit.prevent="handleLogin">
        <!-- 用户名输入 -->
        <div class="form-group">
          <i class="fas fa-user"></i>
          <input 
            type="text" 
            v-model="userid" 
            class="form-input" 
            placeholder="请输入用户ID" 
            required
          >
        </div>

        <!-- 密码输入 -->
        <div class="form-group">
          <i class="fas fa-lock"></i>
          <input 
            type="password" 
            v-model="password" 
            class="form-input" 
            placeholder="请输入密码" 
            autocomplete="current-password"
            required
          >
        </div>

        <!-- 记住我 -->
        <div class="extra-options">
          <label class="remember-me">
            <input type="checkbox" v-model="rememberMe">
            <span>记住我</span>
          </label>
        </div>

        <!-- 登录按钮 -->
        <button type="submit" class="login-btn" :disabled="loading">
          {{ loading ? '登录中...' : '登 录' }}
        </button>

        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api/auth'

const router = useRouter()
const canvasContainer = ref(null)
let particleSystem = null
const userid = ref('')
const password = ref('')
const loading = ref(false)
const errorMessage = ref('')
const rememberMe = ref(false)

class Particle {
  constructor(x, y, size, speedX, speedY, color) {
    this.x = x
    this.y = y
    this.size = size
    this.speedX = speedX
    this.speedY = speedY
    this.color = color
    this.element = document.createElement('div')
    this.element.className = 'particle'
    this.element.style.width = `${size}px`
    this.element.style.height = `${size}px`
    this.element.style.backgroundColor = color
    this.element.style.opacity = '0.6'
    canvasContainer.value.appendChild(this.element)
  }

  update() {
    this.x += this.speedX
    this.y += this.speedY

    if (this.x > window.innerWidth || this.x < 0) this.speedX *= -1
    if (this.y > window.innerHeight || this.y < 0) this.speedY *= -1

    this.element.style.transform = `translate(${this.x}px, ${this.y}px)`
  }
}

class ParticleSystem {
  constructor() {
    this.particles = []
    this.colors = ['#4c84ff', '#2d5cfe', '#3B82F6', '#60A5FA']
    this.init()
  }

  init() {
    const particleCount = 50

    for (let i = 0; i < particleCount; i++) {
      const size = Math.random() * 10 + 5
      const x = Math.random() * window.innerWidth
      const y = Math.random() * window.innerHeight
      const speedX = (Math.random() - 0.5) * 2
      const speedY = (Math.random() - 0.5) * 2
      const color = this.colors[Math.floor(Math.random() * this.colors.length)]

      this.particles.push(new Particle(x, y, size, speedX, speedY, color))
    }

    this.animate()
  }

  animate() {
    this.particles.forEach(particle => particle.update())
    this.animationFrame = requestAnimationFrame(() => this.animate())
  }

  destroy() {
    if (this.animationFrame) {
      cancelAnimationFrame(this.animationFrame)
    }
    this.particles.forEach(particle => {
      if (particle.element && particle.element.parentNode) {
        particle.element.parentNode.removeChild(particle.element)
      }
    })
  }
}

// 处理登录
const handleLogin = async () => {
  if (!userid.value || !password.value) {
    errorMessage.value = '请输入用户名和密码'
    return
  }

  loading.value = true
  errorMessage.value = ''

  try {
    const res = await login(userid.value, password.value)
    if (res.code === 200) {
      // 保存token和用户信息
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('userInfo', JSON.stringify(res.data.user))
      
      // 如果选择记住我，保存用户名
      if (rememberMe.value) {
        localStorage.setItem('rememberedUserid', userid.value)
      } else {
        localStorage.removeItem('rememberedUserid')
      }
      
      // 跳转到首页
      router.push('/')
    } else {
      errorMessage.value = res.msg || '登录失败'
    }
  } catch (error) {
    console.error('登录失败:', error)
    errorMessage.value = error.response?.data?.msg || '登录失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 检查是否有记住的用户名
onMounted(() => {
  const rememberedUserid = localStorage.getItem('rememberedUserid')
  if (rememberedUserid) {
    userid.value = rememberedUserid
    rememberMe.value = true
  }
  
  particleSystem = new ParticleSystem()
  
  window.addEventListener('resize', () => {
    if (canvasContainer.value) {
      canvasContainer.value.innerHTML = ''
      particleSystem = new ParticleSystem()
    }
  })
})

onUnmounted(() => {
  if (particleSystem) {
    particleSystem.destroy()
  }
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f8f9fc 0%, #f1f4f9 100%);
  position: relative;
  overflow: hidden;
}

.canvas-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.particle {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
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

.login-box {
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

.login-title i {
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

.extra-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  font-size: 14px;
  color: #666;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.login-btn {
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

.error-message {
  color: #ff4d4f;
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
}
</style> 