<template>
  <div class="permission-manage-page">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="角色管理" name="role">
        <div class="card">
          <!-- 角色列表工具栏 -->
          <div class="toolbar flex flex-wrap items-center mb-4 gap-2">
            <el-button type="primary" @click="onAddRole">添加角色</el-button>
            <el-input v-model="roleSearchKey" placeholder="角色名称" class="ml-2 w-64" clearable />
            <el-button type="primary" @click="searchRoles">查询</el-button>
            <el-button @click="resetRoleSearch">重置</el-button>
          </div>

          <!-- 角色列表 -->
          <el-table :data="filteredRoles" border style="width: 100%" height="350">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="roleName" label="角色名称" width="180" />
            <el-table-column prop="roleCode" label="角色编码" width="180" />
            <el-table-column prop="description" label="描述" />
            <el-table-column label="操作" width="250" fixed="right">
              <template #default="scope">
                <el-button type="primary" size="small" @click="editRole(scope.row)">编辑</el-button>
                <el-button type="success" size="small" @click="setPermission(scope.row)">设置权限</el-button>
                <el-button type="danger" size="small" @click="confirmDeleteRole(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>

      <el-tab-pane label="用户角色分配" name="userRole">
        <div class="card">
          <!-- 用户角色工具栏 -->
          <div class="toolbar flex flex-wrap items-center mb-4 gap-2">
            <el-input v-model="userSearchKey" placeholder="用户名/账号" class="ml-2 w-64" clearable />
            <el-button type="primary" @click="searchUsers">查询</el-button>
            <el-button @click="resetUserSearch">重置</el-button>
          </div>

          <!-- 用户列表 -->
          <el-table :data="filteredUsers" border style="width: 100%" height="350">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userid" label="登录账号" width="150" />
            <el-table-column prop="fullname" label="姓名" width="150" />
            <el-table-column prop="role" label="当前角色" width="150" />
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="scope">
                <el-button type="success" size="small" @click="assignUserRole(scope.row)">分配角色</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 角色表单对话框 -->
    <el-dialog 
      v-model="showRoleDialog" 
      :title="isEdit ? '编辑角色' : '添加角色'" 
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form 
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleRules"
        label-width="80px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="roleForm.description" placeholder="请输入描述信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showRoleDialog = false">取消</el-button>
        <el-button type="primary" @click="submitRoleForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 权限分配对话框 -->
    <el-dialog 
      v-model="showPermissionDialog" 
      title="分配权限" 
      width="600px"
      :close-on-click-modal="false"
    >
      <div v-if="currentRole" class="mb-4">
        正在为角色 <span class="font-bold">{{ currentRole.roleName }}</span> 分配权限
      </div>
      <el-tree
        ref="permissionTree"
        :data="menuTree"
        node-key="id"
        show-checkbox
        :props="{ 
          label: 'menuName',
          children: 'children'
        }"
        :default-checked-keys="checkedPermissionIds"
      />
      <template #footer>
        <el-button @click="showPermissionDialog = false">取消</el-button>
        <el-button type="primary" @click="savePermissions">保存</el-button>
      </template>
    </el-dialog>

    <!-- 用户角色分配对话框 -->
    <el-dialog 
      v-model="showUserRoleDialog" 
      title="分配用户角色" 
      width="500px"
      :close-on-click-modal="false"
    >
      <div v-if="currentUser" class="mb-4">
        正在为用户 <span class="font-bold">{{ currentUser.fullname }}</span> 分配角色
      </div>
      <el-checkbox-group v-model="selectedUserRoles">
        <div class="role-checkbox-container">
          <el-checkbox 
            v-for="role in roleList" 
            :key="role.id" 
            :label="role.id"
            class="role-checkbox"
          >
            {{ role.roleName }}
          </el-checkbox>
        </div>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="showUserRoleDialog = false">取消</el-button>
        <el-button type="primary" @click="saveUserRoles">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { 
  getRoleList, 
  addRole, 
  updateRole, 
  deleteRole, 
  getMenuList, 
  getRolePermission,
  setRolePermission,
  getUserRoles,
  setUserRoles
} from '@/api/permission';
import { getUserList } from '@/api/user';

// 标签页状态
const activeTab = ref('role');

// 角色管理相关状态
const roleList = ref([]);
const roleSearchKey = ref('');
const showRoleDialog = ref(false);
const isEdit = ref(false);
const roleForm = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  description: ''
});
const roleRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
};
const roleFormRef = ref(null);
const filteredRoles = computed(() => {
  if (!roleSearchKey.value) return roleList.value;
  return roleList.value.filter(role => 
    role.roleName.includes(roleSearchKey.value) || 
    role.roleCode.includes(roleSearchKey.value)
  );
});

// 权限设置相关状态
const showPermissionDialog = ref(false);
const menuTree = ref([]);
const currentRole = ref(null);
const checkedPermissionIds = ref([]);
const permissionTree = ref(null);

// 用户角色分配相关状态
const userList = ref([]);
const userSearchKey = ref('');
const showUserRoleDialog = ref(false);
const currentUser = ref(null);
const selectedUserRoles = ref([]);
const filteredUsers = computed(() => {
  if (!userSearchKey.value) return userList.value;
  return userList.value.filter(user => 
    user.userid.includes(userSearchKey.value) || 
    user.fullname.includes(userSearchKey.value)
  );
});

// 加载初始数据
onMounted(async () => {
  await loadRoles();
  await loadMenus();
  await loadUsers();
});

// 角色相关方法
async function loadRoles() {
  try {
    const res = await getRoleList();
    if (res.code === 200) {
      roleList.value = res.data;
    }
  } catch (error) {
    console.error('加载角色列表失败:', error);
    ElMessage.error('加载角色列表失败');
  }
}

function onAddRole() {
  isEdit.value = false;
  resetRoleForm();
  showRoleDialog.value = true;
}

function editRole(role) {
  isEdit.value = true;
  roleForm.id = role.id;
  roleForm.roleName = role.roleName;
  roleForm.roleCode = role.roleCode;
  roleForm.description = role.description || '';
  showRoleDialog.value = true;
}

function resetRoleForm() {
  roleForm.id = null;
  roleForm.roleName = '';
  roleForm.roleCode = '';
  roleForm.description = '';
}

async function submitRoleForm() {
  if (!roleFormRef.value) return;
  
  await roleFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const method = isEdit.value ? updateRole : addRole;
        const res = await method(roleForm);
        
        if (res.code === 200) {
          ElMessage.success(isEdit.value ? '角色更新成功' : '角色添加成功');
          showRoleDialog.value = false;
          await loadRoles();
        } else {
          ElMessage.error(res.message || (isEdit.value ? '角色更新失败' : '角色添加失败'));
        }
      } catch (error) {
        console.error(isEdit.value ? '更新角色失败:' : '添加角色失败:', error);
        ElMessage.error(isEdit.value ? '更新角色失败' : '添加角色失败');
      }
    }
  });
}

function confirmDeleteRole(role) {
  ElMessageBox.confirm(
    `确定要删除角色 "${role.roleName}" 吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await deleteRole(role.id);
      if (res.code === 200) {
        ElMessage.success('角色删除成功');
        await loadRoles();
      } else {
        ElMessage.error(res.message || '角色删除失败');
      }
    } catch (error) {
      console.error('删除角色失败:', error);
      ElMessage.error('删除角色失败');
    }
  }).catch(() => {
    // 用户取消删除操作
  });
}

function searchRoles() {
  // 使用计算属性自动过滤
}

function resetRoleSearch() {
  roleSearchKey.value = '';
}

// 权限菜单相关方法
async function loadMenus() {
  try {
    const res = await getMenuList();
    if (res.code === 200) {
      menuTree.value = res.data;
    }
  } catch (error) {
    console.error('加载菜单列表失败:', error);
    ElMessage.error('加载菜单列表失败');
  }
}

async function setPermission(role) {
  currentRole.value = role;
  try {
    const res = await getRolePermission(role.id);
    if (res.code === 200) {
      checkedPermissionIds.value = res.data;
      showPermissionDialog.value = true;
    } else {
      ElMessage.error(res.message || '获取角色权限失败');
    }
  } catch (error) {
    console.error('获取角色权限失败:', error);
    ElMessage.error('获取角色权限失败');
  }
}

async function savePermissions() {
  if (!currentRole.value || !permissionTree.value) return;
  
  try {
    // 获取当前选中的节点ID
    const checkedKeys = permissionTree.value.getCheckedKeys();
    const halfCheckedKeys = permissionTree.value.getHalfCheckedKeys();
    const allSelectedKeys = [...checkedKeys, ...halfCheckedKeys];
    
    const res = await setRolePermission(currentRole.value.id, allSelectedKeys);
    if (res.code === 200) {
      ElMessage.success('权限设置成功');
      showPermissionDialog.value = false;
    } else {
      ElMessage.error(res.message || '权限设置失败');
    }
  } catch (error) {
    console.error('设置权限失败:', error);
    ElMessage.error('设置权限失败');
  }
}

// 用户角色相关方法
async function loadUsers() {
  try {
    const res = await getUserList();
    if (res.code === 200) {
      userList.value = res.data;
    }
  } catch (error) {
    console.error('加载用户列表失败:', error);
    ElMessage.error('加载用户列表失败');
  }
}

async function assignUserRole(user) {
  currentUser.value = user;
  selectedUserRoles.value = [];
  
  try {
    const res = await getUserRoles(user.id);
    if (res.code === 200) {
      selectedUserRoles.value = res.data;
      showUserRoleDialog.value = true;
    } else {
      ElMessage.error(res.message || '获取用户角色失败');
    }
  } catch (error) {
    console.error('获取用户角色失败:', error);
    ElMessage.error('获取用户角色失败');
  }
}

async function saveUserRoles() {
  if (!currentUser.value) return;
  
  try {
    const res = await setUserRoles(currentUser.value.id, selectedUserRoles.value);
    if (res.code === 200) {
      ElMessage.success('用户角色设置成功');
      showUserRoleDialog.value = false;
      await loadUsers();
    } else {
      ElMessage.error(res.message || '用户角色设置失败');
    }
  } catch (error) {
    console.error('设置用户角色失败:', error);
    ElMessage.error('设置用户角色失败');
  }
}

function searchUsers() {
  // 使用计算属性自动过滤
}

function resetUserSearch() {
  userSearchKey.value = '';
}
</script>

<style scoped>
.permission-manage-page {
  padding: 20px;
}

.card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.role-checkbox-container {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-top: 10px;
}

.role-checkbox {
  min-width: 120px;
}
</style> 