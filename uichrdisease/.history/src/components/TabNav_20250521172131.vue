<template>
  <div class="tab-nav-bar">
    <draggable v-model="tags" class="tab-list" item-key="path" :animation="200" :ghost-class="'tab-ghost'">
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
          <span class="tab-icon"><i :class="['fas', tag.icon]"></i></span>
          {{ tag.title }}
          <span v-if="tag.closable" class="tab-close" @click.stop="handleTabClose(tag, index)">
            <i class="fas fa-times"></i>
          </span>
        </div>
      </template>
    </draggable>
    
    <!-- 右键菜单 -->
    <div v-show="contextMenu.visible" class="context-menu" :style="contextMenuStyle">
      <div class="context-menu-item" @click="handleCloseTab" v-if="contextMenu.tag?.closable">
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

    <!-- 更多操作下拉菜单 -->
    <div class="tab-menu">
      <el-dropdown trigger="click">
        <span class="tab-menu-trigger">
          <i class="fas fa-ellipsis-v"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="handleCloseOther">
              <i class="fas fa-times-circle mr-2"></i>关闭其他
            </el-dropdown-item>
            <el-dropdown-item @click="handleCloseAll">
              <i class="fas fa-times-square mr-2"></i>关闭所有
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onUnmounted } from 'vue'
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
  }
  // 可以添加更多路由配置...
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
  const config = pathToTagConfig[path] || {
    title: path.split('/').pop(),
    icon: 'fa-file'
  }

  // 创建新标签
  const newTag = {
    title: config.title,
    path: path,
    icon: config.icon,
    closable: true
  }

  tags.value.push(newTag)
  activePath.value = path
  saveTags() // 保存到本地存储
}

// 点击标签
function handleTabClick(tag) {
  activePath.value = tag.path
  router.push(tag.path)
}

// 关闭标签
function handleTabClose(tag, idx) {
  closeTab(idx)
}

// 关闭标签的具体实现
function closeTab(idx) {
  const tag = tags.value[idx]
  if (!tag.closable) return

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
  tags.value = tags.value.filter(tag => !tag.closable || tag.path === currentTag.path)
  saveTags()
  hideContextMenu()
}

// 关闭所有标签
function handleCloseAll() {
  tags.value = tags.value.filter(tag => !tag.closable)
  activePath.value = '/main'
  router.push('/main')
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

// 从本地存储加载标签
function loadTags() {
  const savedTags = localStorage.getItem('navTags')
  if (savedTags) {
    try {
      const parsedTags = JSON.parse(savedTags)
      // 确保首页标签始终存在
      if (!parsedTags.some(tag => tag.path === '/main')) {
        parsedTags.unshift({ title: '首页', path: '/main', icon: 'fa-home', closable: false })
      }
      tags.value = parsedTags
    } catch (e) {
      console.error('加载标签失败:', e)
    }
  }
}

// 监听路由变化
watch(() => router.currentRoute.value.path, (newPath) => {
  addTag(newPath)
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
.tab-nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  padding: 0 16px 8px 16px;
  border-radius: 10px 10px 0 0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 18px;
  position: relative;
}

.tab-list {
  display: flex;
  align-items: flex-end;
  gap: 2px;
  flex: 1;
  overflow-x: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.tab-list::-webkit-scrollbar {
  display: none;
}

.tab-item {
  display: flex;
  align-items: center;
  background: #f7f7f7;
  border-radius: 10px 10px 0 0;
  padding: 0 18px 0 14px;
  height: 38px;
  font-size: 15px;
  color: #222;
  cursor: pointer;
  position: relative;
  margin-right: 2px;
  transition: all 0.2s;
  white-space: nowrap;
  user-select: none;
}

.tab-item.active {
  background: #fff;
  color: #2563eb;
  font-weight: 600;
  border-bottom: 2.5px solid #2563eb;
  box-shadow: 0 2px 8px rgba(76,132,255,0.04);
}

.tab-item:not(.active):hover {
  background: #f0f7ff;
  color: #2563eb;
}

.tab-icon {
  margin-right: 6px;
  font-size: 16px;
  display: flex;
  align-items: center;
}

.tab-close {
  margin-left: 10px;
  color: #bbb;
  font-size: 13px;
  cursor: pointer;
  transition: color 0.2s;
}

.tab-close:hover {
  color: #ff4d4f;
}

.tab-menu {
  margin-left: 16px;
}

.tab-menu-trigger {
  padding: 8px;
  cursor: pointer;
  color: #666;
  transition: color 0.2s;
}

.tab-menu-trigger:hover {
  color: #2563eb;
}

.tab-ghost {
  opacity: 0.5;
  background: #f0f7ff;
}

/* 右键菜单样式 */
.context-menu {
  position: fixed;
  background: white;
  border-radius: 4px;
  padding: 4px 0;
  min-width: 160px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  z-index: 1000;
}

.context-menu-item {
  padding: 8px 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  color: #333;
  transition: all 0.2s;
}

.context-menu-item:hover {
  background: #f0f7ff;
  color: #2563eb;
}

.context-menu-item i {
  width: 16px;
  text-align: center;
  margin-right: 8px;
}

/* 首页标签特殊样式 */
.tab-home {
  background: #f0f7ff;
  color: #2563eb;
  font-weight: 500;
}

.tab-home:not(.active):hover {
  opacity: 0.9;
}
</style> 