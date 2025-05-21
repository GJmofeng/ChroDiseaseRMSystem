<template>
  <div class="tab-nav-container">
    <div class="tab-nav-bar">
      <div class="tab-list-wrapper">
        <draggable 
          v-model="tags" 
          class="tab-list" 
          item-key="path" 
          :animation="200" 
          :ghost-class="'tab-ghost'"
          handle=".tab-item"
        >
          <template #item="{element: tag}">
            <div 
              class="tab-item" 
              :class="{
                active: activePath === tag.path,
                'tab-home': tag.path === '/main'
              }" 
              @click="handleTabClick(tag)"
            >
              <span class="tab-icon">
                <i :class="['fas', tag.icon]"></i>
              </span>
              <span class="tab-title">{{ tag.title }}</span>
              <span 
                v-if="tag.closable" 
                class="tab-close"
                @click.stop="handleTabClose(tag, $index)"
              >
                <i class="fas fa-times"></i>
              </span>
            </div>
          </template>
        </draggable>
      </div>

      <div class="tab-actions">
        <el-dropdown trigger="click" @command="handleCommand">
          <span class="tab-menu-trigger">
            <i class="fas fa-ellipsis-v"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="closeOther">
                <i class="fas fa-times-circle mr-2"></i>关闭其他
              </el-dropdown-item>
              <el-dropdown-item command="closeAll">
                <i class="fas fa-times-square mr-2"></i>关闭所有
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onUnmounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import draggable from 'vuedraggable'

const router = useRouter()
const route = useRoute()

// 标签列表
const tags = ref([
  { title: '首页', path: '/main', icon: 'fa-home', closable: false }
])

// 当前激活的路径
const activePath = computed(() => route.path)

// 路由路径到标签配置的映射
const pathToTagConfig = {
  '/main': {
    title: '首页',
    icon: 'fa-home'
  },
  '/main/region-manage': {
    title: '行政区管理',
    icon: 'fa-map-marker-alt'
  },
  '/main/user-manage': {
    title: '用户管理',
    icon: 'fa-users'
  },
  '/main/patient-manage': {
    title: '患者管理',
    icon: 'fa-user-injured'
  },
  '/main/reimbursement': {
    title: '报销管理',
    icon: 'fa-money-bill-wave'
  }
}

// 添加标签
function addTag(path) {
  // 如果是首页，不需要添加标签
  if (path === '/main') {
    return
  }

  // 检查标签是否已存在
  const existingTag = tags.value.find(tag => tag.path === path)
  if (existingTag) {
    return
  }

  // 获取标签配置
  const config = pathToTagConfig[path]
  if (!config) {
    console.warn(`未找到路径 ${path} 的标签配置`)
    return
  }

  // 创建新标签
  const newTag = {
    title: config.title,
    path: path,
    icon: config.icon,
    closable: true
  }

  tags.value.push(newTag)
  saveTags()
}

// 点击标签
function handleTabClick(tag) {
  if (tag.path === activePath.value) {
    return
  }
  router.push(tag.path)
}

// 关闭标签
function handleTabClose(tag, idx) {
  if (!tag.closable) return
  
  if (tag.path === activePath.value) {
    // 优先激活左侧标签
    const nextTag = tags.value[idx - 1] || tags.value[idx + 1]
    if (nextTag) {
      router.push(nextTag.path)
    } else {
      router.push('/main')
    }
  }
  
  tags.value.splice(idx, 1)
  saveTags()
}

// 关闭其他标签
function handleCloseOther() {
  const currentTag = tags.value.find(tag => tag.path === activePath.value)
  if (!currentTag) return
  
  tags.value = tags.value.filter(tag => !tag.closable || tag.path === currentTag.path)
  saveTags()
}

// 关闭所有标签
function handleCloseAll() {
  tags.value = tags.value.filter(tag => !tag.closable)
  router.push('/main')
  saveTags()
}

// 处理下拉菜单命令
function handleCommand(command) {
  switch (command) {
    case 'closeOther':
      handleCloseOther()
      break
    case 'closeAll':
      handleCloseAll()
      break
  }
}

// 保存标签到本地存储
function saveTags() {
  const saveData = tags.value.map(tag => ({
    title: tag.title,
    path: tag.path,
    icon: tag.icon,
    closable: tag.closable
  }))
  localStorage.setItem('navTags', JSON.stringify(saveData))
}

// 从本地存储加载标签
function loadTags() {
  const savedTags = localStorage.getItem('navTags')
  const currentPath = route.path
  
  // 如果是首次访问或在首页，只显示首页标签
  if (!savedTags || currentPath === '/main') {
    tags.value = [{ title: '首页', path: '/main', icon: 'fa-home', closable: false }]
    return
  }

  try {
    const parsedTags = JSON.parse(savedTags)
    // 确保首页标签始终存在且在第一位
    if (!parsedTags.some(tag => tag.path === '/main')) {
      parsedTags.unshift({ title: '首页', path: '/main', icon: 'fa-home', closable: false })
    }
    
    // 如果当前不在首页，确保当前页面的标签存在
    if (currentPath !== '/main' && pathToTagConfig[currentPath]) {
      const hasCurrentPath = parsedTags.some(tag => tag.path === currentPath)
      if (!hasCurrentPath) {
        parsedTags.push({
          ...pathToTagConfig[currentPath],
          path: currentPath,
          closable: true
        })
      }
    }
    
    tags.value = parsedTags
  } catch (e) {
    console.error('加载标签失败:', e)
    tags.value = [{ title: '首页', path: '/main', icon: 'fa-home', closable: false }]
  }
}

// 监听路由变化
watch(() => route.path, (newPath) => {
  // 忽略重定向路由
  if (newPath.startsWith('/redirect')) return
  
  // 如果路径在配置中存在，则添加标签
  if (pathToTagConfig[newPath]) {
    addTag(newPath)
  }
}, { immediate: true })

// 组件挂载时加载标签
onMounted(() => {
  loadTags()
})
</script>

<style scoped>
.tab-nav-container {
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
}

.tab-nav-bar {
  display: flex;
  align-items: center;
  padding: 8px 16px;
  height: 48px;
}

.tab-list-wrapper {
  flex: 1;
  overflow: hidden;
  margin-right: 16px;
}

.tab-list {
  display: flex;
  align-items: center;
  gap: 4px;
  height: 32px;
  overflow-x: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
  white-space: nowrap;
}

.tab-list::-webkit-scrollbar {
  display: none;
}

.tab-item {
  display: inline-flex;
  align-items: center;
  height: 32px;
  padding: 0 12px;
  background: #f5f7fa;
  border-radius: 4px;
  font-size: 14px;
  color: #606266;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
  border: 1px solid transparent;
  margin-right: 4px;
}

.tab-item:hover {
  background: #ecf5ff;
  color: #409eff;
}

.tab-item.active {
  background: #409eff;
  color: #fff;
  font-weight: 500;
}

.tab-item.tab-home {
  background: #f0f7ff;
  color: #2563eb;
  font-weight: 500;
}

.tab-icon {
  margin-right: 6px;
  font-size: 14px;
}

.tab-title {
  margin-right: 6px;
}

.tab-close {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  margin-left: 4px;
  border-radius: 50%;
  font-size: 12px;
  color: inherit;
  opacity: 0.7;
  transition: all 0.2s;
}

.tab-close:hover {
  background: rgba(0, 0, 0, 0.1);
  opacity: 1;
}

.active .tab-close:hover {
  background: rgba(255, 255, 255, 0.2);
}

.tab-actions {
  display: flex;
  align-items: center;
}

.tab-menu-trigger {
  padding: 6px;
  cursor: pointer;
  color: #606266;
  transition: color 0.2s;
  border-radius: 4px;
}

.tab-menu-trigger:hover {
  color: #409eff;
  background: #ecf5ff;
}

.tab-ghost {
  opacity: 0.5;
  background: #ecf5ff !important;
  border: 1px dashed #409eff !important;
}
</style> 