<h2 class="title">欢迎登陆</h2>
<el-form 
    :model="loginForm"
    :rules="rules"
    ref="formRef">
    <el-form-item label="用户名" prop="userid">
        <el-input placeholder="请输入用户名" v-model="loginForm.userid"/>
    </el-form-item>
    <el-form-item label="密码" prop="password">
        <el-input type="password" placeholder="请输入密码" v-model="loginForm.password"/>
    </el-form-item>
    <el-form-item>
        <el-button type="primary" @click="submitForm">登录</el-button>
        <el-button @click="resetForm(formRef)">重置</el-button>
    </el-form-item>
</el-form>