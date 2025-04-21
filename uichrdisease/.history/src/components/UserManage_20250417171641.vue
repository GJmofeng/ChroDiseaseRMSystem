<t<h2 class="title">欢迎登陆</h2>
<el-form 
    :model="loginForm"
    :rules="rules"
    ref="formRef"
    >

    <el-form-item label="用户名" prop="userid">
        <el-input placeholder="请输入用户名" v-model="loginForm.userid"/>
    </el-form-item>

    <el-form-item label="密码" prop="password">
        <el-input type="password" placeholder="请输入密码" v-model="loginForm.password"/>
    </el-form-item>

    <el-form-item>
        <el-button type="primary" @click="submitForm" class="submit-btn">登录</el-button>
    </el-form-item>
</el-form>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { login } from '../api/user'
import { useRouter } from 'vue-router'

const router = useRouter()

const loginForm = ref({
    userid: '',
    password: ''
})

const rules = ref({
    userid: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
    ]
})

const formRef = ref(null);

const submitForm = () => {
    formRef.value.validate((valid) => {
        if (valid) {
            login(loginForm.value).then(res => {
                if (res.code === 200) {
                    ElMessage({
                        message: '登录成功',
                        type: 'success'
                    })
                    localStorage.setItem('token', res.data.token)
                    router.push('/home')
                } else {
                    ElMessage({
                        message: res.msg,
                        type: 'error'
                    })
                }
            })
        } else {
            ElMessage({
                message: '请输入正确的用户名和密码',
                type: 'error'
            })
            return false;
        }
    })
}

const resetForm = (formEl) => {
    if (!formEl) return
    formEl.resetFields()
}
</script>

<style scoped>
.title {
    text-align: center;
    margin-bottom: 20px;
}

.submit-btn {
    margin-right: 20px;
}
</style>