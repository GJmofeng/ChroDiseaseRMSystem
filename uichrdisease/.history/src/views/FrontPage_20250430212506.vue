<template>
  <div class="front-page">
    <!-- 导航栏 -->
    <header class="fixed w-full bg-white shadow-sm z-50">
      <nav class="container mx-auto px-4 py-4">
        <div class="flex justify-between items-center">
          <div class="text-2xl font-bold text-blue-600">慢性病报销系统</div>
          <div class="hidden md:flex space-x-8 items-center">
            <a href="#" class="hover:text-blue-600">首页</a>
            <a href="#" class="hover:text-blue-600">服务</a>
            <a href="#" class="hover:text-blue-600">办事指南</a>
            <a href="#" class="hover:text-blue-600">联系我们</a>
            <router-link to="/login" class="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition">
              登录/注册
            </router-link>
          </div>
        </div>
      </nav>
    </header>

    <!-- 主页英雄区域 -->
    <section class="hero-section bg-white">
      <div class="canvas-container" ref="canvasContainer"></div>
      <div class="content-container flex items-center justify-center text-center px-4">
        <div class="max-w-4xl mx-auto">
          <h1 class="text-5xl md:text-6xl font-bold mb-8 text-blue-600">
            便捷的慢性病报销服务
          </h1>
          <p class="text-xl md:text-2xl text-blue-500 mb-12">
            让医疗报销更简单，让健康保障更贴心
          </p>
          <router-link to="/register" class="bg-blue-600 text-white px-8 py-4 rounded-lg text-lg hover:bg-blue-700 transition">
            立即开始申请
          </router-link>
        </div>
      </div>
    </section>

    <!-- 服务特点展示区 -->
    <section class="py-20 bg-gray-50">
      <div class="container mx-auto px-4">
        <h2 class="text-3xl font-bold text-center mb-16 text-blue-600">我们的服务优势</h2>
        <div class="grid md:grid-cols-3 gap-8">
          <!-- 特点卡片 1 -->
          <div class="feature-card bg-white p-8 rounded-xl shadow-lg text-center">
            <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
                      d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
              </svg>
            </div>
            <h3 class="text-xl font-semibold mb-4 text-blue-600">便捷申请</h3>
            <p class="text-blue-500">在线提交材料，足不出户完成报销申请</p>
          </div>

          <!-- 特点卡片 2 -->
          <div class="feature-card bg-white p-8 rounded-xl shadow-lg text-center">
            <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
                      d="M13 10V3L4 14h7v7l9-11h-7z"/>
              </svg>
            </div>
            <h3 class="text-xl font-semibold mb-4 text-blue-600">智能审核</h3>
            <p class="text-blue-500">系统自动审核，加快报销进度</p>
          </div>

          <!-- 特点卡片 3 -->
          <div class="feature-card bg-white p-8 rounded-xl shadow-lg text-center">
            <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
                      d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
            </div>
            <h3 class="text-xl font-semibold mb-4 text-blue-600">全程追踪</h3>
            <p class="text-blue-500">实时查看申请进度，透明化处理流程</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 操作指南区域 -->
    <section class="py-20 bg-white">
      <div class="container mx-auto px-4">
        <h2 class="text-3xl font-bold text-center mb-16 text-blue-600">报销申请指南</h2>
        <div class="grid md:grid-cols-4 gap-8">
          <!-- 步骤 1 -->
          <div class="text-center">
            <div class="step-number mb-4">1</div>
            <h4 class="text-lg font-semibold mb-2 text-blue-600">注册账号</h4>
            <p class="text-blue-500">完成实名认证</p>
          </div>

          <!-- 步骤 2 -->
          <div class="text-center">
            <div class="step-number mb-4">2</div>
            <h4 class="text-lg font-semibold mb-2 text-blue-600">提交材料</h4>
            <p class="text-blue-500">上传必要证明文件</p>
          </div>

          <!-- 步骤 3 -->
          <div class="text-center">
            <div class="step-number mb-4">3</div>
            <h4 class="text-lg font-semibold mb-2 text-blue-600">等待审核</h4>
            <p class="text-blue-500">系统智能审核</p>
          </div>

          <!-- 步骤 4 -->
          <div class="text-center">
            <div class="step-number mb-4">4</div>
            <h4 class="text-lg font-semibold mb-2 text-blue-600">完成报销</h4>
            <p class="text-blue-500">资金到账</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="bg-gray-900 text-white py-12">
      <div class="container mx-auto px-4">
        <div class="grid md:grid-cols-4 gap-8">
          <!-- 关于我们 -->
          <div>
            <h4 class="text-lg font-semibold mb-4">关于我们</h4>
            <ul class="space-y-2">
              <li><a href="#" class="hover:text-blue-400">机构介绍</a></li>
              <li><a href="#" class="hover:text-blue-400">新闻动态</a></li>
              <li><a href="#" class="hover:text-blue-400">联系方式</a></li>
            </ul>
          </div>

          <!-- 服务指南 -->
          <div>
            <h4 class="text-lg font-semibold mb-4">服务指南</h4>
            <ul class="space-y-2">
              <li><a href="#" class="hover:text-blue-400">办事流程</a></li>
              <li><a href="#" class="hover:text-blue-400">常见问题</a></li>
              <li><a href="#" class="hover:text-blue-400">表格下载</a></li>
            </ul>
          </div>

          <!-- 政策法规 -->
          <div>
            <h4 class="text-lg font-semibold mb-4">政策法规</h4>
            <ul class="space-y-2">
              <li><a href="#" class="hover:text-blue-400">相关政策</a></li>
              <li><a href="#" class="hover:text-blue-400">法律法规</a></li>
              <li><a href="#" class="hover:text-blue-400">规章制度</a></li>
            </ul>
          </div>

          <!-- 联系方式 -->
          <div>
            <h4 class="text-lg font-semibold mb-4">联系我们</h4>
            <ul class="space-y-2">
              <li>服务热线：400-XXX-XXXX</li>
              <li>工作时间：周一至周五 9:00-17:00</li>
              <li>邮箱：support@example.com</li>
            </ul>
          </div>
        </div>

        <!-- 版权信息 -->
        <div class="mt-8 pt-8 border-t border-gray-800 text-center">
          <p>© 2024 慢性病报销系统. 版权所有</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const canvasContainer = ref(null)
let particleSystem = null

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
    this.colors = ['#2563EB', '#1E40AF', '#3B82F6', '#60A5FA']
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

onMounted(() => {
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

<style>
.front-page {
  min-height: 100vh;
}

.hero-section {
  min-height: 100vh;
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

.content-container {
  position: relative;
  z-index: 2;
}

.feature-card {
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.step-number {
  width: 40px;
  height: 40px;
  background: #2563EB;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

.particle {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
}
</style> 