<template>
  <div class="tab-nav-bar">
    <draggable v-model="tags" class="tab-list" item-key="path" :animation="200" :ghost-class="'tab-ghost'">
      <template #item="{element: tag, index}">
        <div class="tab-item" :class="{active: activePath === tag.path}" @click="handleTabClick(tag)">
          <span class="tab-icon"><i :class="['fas', tag.icon]"></i></span>
          {{ tag.title }}
          <span v-if="tag.closable" class="tab-close" @click.stop="handleTabClose(tag, index)">
            <i class="fas fa-times"></i>
          </span>
        </div>
      </template>
    </draggable>
    <div class="tab-menu">
      <el-dropdown trigger="click">
        <span class="tab-menu-trigger">
          <i class="fas fa-ellipsis-v"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="handleCloseOther">关闭其他</el-dropdown-item>
            <el-dropdown-item @click="handleCloseAll">关闭所有</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import draggable from 'vuedraggable'

const router = useRouter()
const emit = defineEmits(['update:modelValue'])

// 标签列表
const tags = ref([
  { title: '首页', path: '/main', icon: 'fa-home', closable: false }
])

// 当前激活的路径
const activePath = ref(router.currentRoute.value.path)

// 添加标签
function addTag(path) {
  const existingTag = tags.value.find(tag => tag.path === path)
  if (existingTag) {
    activePath.value = path
    return
  }

  let newTag = null
  switch (path) {
    case '/main/region-manage':
      newTag = {
        title: '行政区管理',
        path: '/main/region-manage',
        icon: 'fa-map-marker-alt',
        closable: true
      }
      break
    case '/main/user-manage':
      newTag = {
        title: '用户管理',
        path: '/main/user-manage',
        icon: 'fa-users',
        closable: true
      }
      break
    default:
      if (path === '/main') return
      newTag = {
        title: path.split('/').pop(),
        path: path,
        icon: 'fa-question-circle',
        closable: true
      }
  }

  if (newTag) {
    tags.value.push(newTag)
    activePath.value = path
  }
}

// 点击标签
function handleTabClick(tag) {
  activePath.value = tag.path
  router.push(tag.path)
}

// 关闭标签
function handleTabClose(tag, idx) {
  if (activePath.value === tag.path) {
    const nextTag = tags.value[idx - 1] || tags.value[idx + 1]
    if (nextTag) {
      activePath.value = nextTag.path
      router.push(nextTag.path)
    } else {
      activePath.value = '/main'
      router.push('/main')
    }
  }
  tags.value.splice(idx, 1)
}

// 关闭其他标签
function handleCloseOther() {
  tags.value = tags.value.filter(tag => !tag.closable || tag.path === activePath.value)
}

// 关闭所有标签
function handleCloseAll() {
  tags.value = tags.value.filter(tag => !tag.closable)
  activePath.value = '/main'
  router.push('/main')
}

// 监听路由变化
watch(() => router.currentRoute.value.path, (newPath) => {
  addTag(newPath)
}, { immediate: true })
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
</style> 