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
          <template #item="{element: tag, index}">
            <div 
              class="tab-item" 
              :class="{
                active: activePath === tag.path,
                'tab-home': tag.path === '/main'
              }" 
              @click="handleTabClick(tag)"
              @contextmenu.prevent="handleContextMenu($event, tag, index)"
            >
              <span class="tab-icon">
                <i :class="['fas', tag.icon]"></i>
              </span>
              <span class="tab-title">{{ tag.title }}</span>
              <span 
                v-if="tag.closable" 
                class="tab-close"
                @click.stop="handleTabClose(tag, index)"
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

    <!-- 右键菜单 -->
    <div 
      v-show="contextMenu.visible" 
      class="context-menu" 
      :style="contextMenuStyle"
      @click.stop
    >
      <div 
        class="context-menu-item" 
        @click="handleCloseTab"
        v-if="contextMenu.tag?.closable"
      >
        <i class="fas fa-times mr-2"></i>关闭标签
      </div>
      <div class="context-menu-item" @click="handleCloseOther">
        <i class="fas fa-times-circle mr-2"></i>关闭其他
      </div>
      <div class="context-menu-item" @click="handleCloseAll">
        <i class="fas fa-times-square mr-2"></i>关闭所有
      </div>
      <div class="context-menu-item" @click="handleRefreshTab">
        <i class="fas fa-sync-alt mr-2"></i>刷新页面
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import draggable from 'vuedraggable'

const router = useRouter()

// 标签列表 - 使用本地存储
const tags = ref([
  { title: '首页', path: '/main', icon: 'fa-home', closable: false }
])

// 当前激活的路径
const activePath = ref(router.currentRoute.value.path)

// 右键菜单状态
const contextMenu = ref({
  visible: false,
  x: 0,
  y: 0,
  tag: null,
  index: -1
})

// 计算右键菜单样式
const contextMenuStyle = computed(() => ({
  left: contextMenu.value.x + 'px',
  top: contextMenu.value.y + 'px'
}))

// 路由路径到标签配置的映射
const pathToTagConfig = {
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

// 从本地存储加载标签
function loadTags() {
  const savedTags = localStorage.getItem('navTags')
  const currentPath = router.currentRoute.value.path
  
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
    } else {
      // 如果首页标签存在但不在第一位，将其移到第一位
      const homeIndex = parsedTags.findIndex(tag => tag.path === '/main')
      if (homeIndex > 0) {
        const homeTag = parsedTags.splice(homeIndex, 1)[0]
        parsedTags.unshift(homeTag)
      }
    }
    
    // 如果当前不在首页，确保当前页面的标签存在
    if (currentPath !== '/main') {
      const hasCurrentPath = parsedTags.some(tag => tag.path === currentPath)
      if (!hasCurrentPath) {
        const config = pathToTagConfig[currentPath]
        if (config) {
          parsedTags.push({
            title: config.title,
            path: currentPath,
            icon: config.icon,
            closable: true
          })
        }
      }
    }
    
    tags.value = parsedTags
  } catch (e) {
    console.error('加载标签失败:', e)
    // 如果加载失败，只显示首页标签
    tags.value = [{ title: '首页', path: '/main', icon: 'fa-home', closable: false }]
  }
}

// 添加标签
function addTag(path) {
  // 如果是首页，不需要添加标签
  if (path === '/main') {
    activePath.value = path
    return
  }

  // 检查标签是否已存在
  const existingTag = tags.value.find(tag => tag.path === path)
  if (existingTag) {
    activePath.value = path
    return
  }

  // 获取标签配置
  const config = pathToTagConfig[path]
  // 如果路径没有配置，不添加标签
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

  // 将新标签添加到当前激活标签的后面
  const currentIndex = tags.value.findIndex(tag => tag.path === activePath.value)
  if (currentIndex !== -1) {
    tags.value.splice(currentIndex + 1, 0, newTag)
  } else {
    tags.value.push(newTag)
  }
  
  activePath.value = path
  saveTags() // 保存到本地存储
}

// 点击标签
function handleTabClick(tag) {
  if (tag.path === activePath.value) {
    // 如果点击当前激活的标签，刷新页面
    handleRefreshTab()
    return
  }
  activePath.value = tag.path
  router.push(tag.path).catch(() => {
    // 如果路由跳转失败，移除该标签
    const index = tags.value.findIndex(t => t.path === tag.path)
    if (index !== -1) {
      closeTab(index)
    }
  })
}

// 关闭标签
function handleTabClose(tag, idx) {
  closeTab(idx)
}

// 关闭标签的具体实现
function closeTab(idx) {
  const tag = tags.value[idx]
  if (!tag || !tag.closable) return

  if (activePath.value === tag.path) {
    // 优先激活左侧标签
    const nextTag = tags.value[idx - 1] || tags.value[idx + 1]
    if (nextTag) {
      activePath.value = nextTag.path
      router.push(nextTag.path)
    } else {
      // 如果没有其他标签，回到首页
      activePath.value = '/main'
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
  hideContextMenu()
}

// 关闭所有标签
function handleCloseAll() {
  const homePath = '/main'
  tags.value = tags.value.filter(tag => !tag.closable)
  activePath.value = homePath
  router.push(homePath)
  saveTags()
  hideContextMenu()
}

// 处理右键菜单
function handleContextMenu(event, tag, index) {
  event.preventDefault()
  contextMenu.value = {
    visible: true,
    x: event.clientX,
    y: event.clientY,
    tag,
    index
  }
}

// 关闭右键菜单
function hideContextMenu() {
  contextMenu.value.visible = false
}

// 刷新当前页面
function handleRefreshTab() {
  const currentPath = router.currentRoute.value.path
  router.replace({
    path: '/redirect' + currentPath
  }).catch(() => {
    console.error('页面刷新失败')
  })
  hideContextMenu()
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

// 监听路由变化
watch(() => router.currentRoute.value.path, (newPath, oldPath) => {
  // 忽略重定向路由
  if (newPath === '/redirect') return
  
  // 如果是首次加载且在首页，不需要处理
  if (!oldPath && newPath === '/main') return
  
  // 如果路径在配置中存在，则添加标签
  if (newPath === '/main' || pathToTagConfig[newPath]) {
    addTag(newPath)
  }
}, { immediate: true })

// 监听点击事件以关闭右键菜单
onMounted(() => {
  loadTags() // 加载保存的标签
  document.addEventListener('click', hideContextMenu)
})

onUnmounted(() => {
  document.removeEventListener('click', hideContextMenu)
})
</script>

<style scoped>
.tab-nav-container {
  position: relative;
  width: 100%;
  background: #fff;
}

.tab-nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 16px;
  border-bottom: 1px solid #f0f0f0;
  background: #fff;
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
  padding-bottom: 4px;
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
  white-space: nowrap;
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

.context-menu {
  position: fixed;
  background: white;
  border-radius: 4px;
  padding: 4px 0;
  min-width: 160px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  border: 1px solid #ebeef5;
}

.context-menu-item {
  padding: 8px 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  color: #606266;
  font-size: 14px;
  transition: all 0.2s;
}

.context-menu-item:hover {
  background: #ecf5ff;
  color: #409eff;
}

.context-menu-item i {
  width: 16px;
  text-align: center;
  margin-right: 8px;
}
</style> 