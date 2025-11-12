<template>
  <div class="login-container">
    <h1>政策服务系统登录</h1>
    <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="80px">
      <el-form-item label="账号" prop="username">
        <el-input
            v-model="loginForm.username"
            placeholder="请输入账号"
            clearable
        />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            clearable
            @keyup.enter.native="handleLogin"
        />
      </el-form-item>
      <el-form-item label="验证码" prop="code" v-if="captchaEnabled">
        <el-input
            v-model="loginForm.code"
            placeholder="请输入验证码"
            style="width: 65%"
            @keyup.enter.native="handleLogin"
        />
        <div class="code-img" @click="getCode">
          <img :src="codeUrl" alt="验证码" />
        </div>
      </el-form-item>
      <el-form-item>
        <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
      </el-form-item>
      <el-form-item>
        <el-button
            type="primary"
            style="width: 100%"
            :loading="loading"
            @click="handleLogin"
        >
          <span v-if="!loading">登录</span>
          <span v-else>登录中...</span>
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import request from "@/utils/request.js";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt';

// 引入若依登录相关API
const login = (username, password, code, uuid) => {
  return request({
    url: '/login',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: { username, password, code, uuid }
  });
};

const getInfo = () => {
  return request({
    url: '/getInfo',
    method: 'get'
  });
};

const router = useRouter();
const loading = ref(false);
const captchaEnabled = ref(true);
const codeUrl = ref('');
const loginFormRef = ref(null);

const loginForm = reactive({
  username: '',
  password: '',
  code: '',
  uuid: '',
  rememberMe: false
});

const loginRules = reactive({
  username: [
    { required: true, trigger: 'blur', message: '请输入账号' }
  ],
  password: [
    { required: true, trigger: 'blur', message: '请输入密码' }
  ],
  code: [
    { required: true, trigger: 'blur', message: '请输入验证码' }
  ]
});

// 获取验证码
const getCode = () => {
  request({
    url: '/captchaImage',
    headers: { isToken: false },
    method: 'get',
    timeout: 20000
  }).then(res => {
    captchaEnabled.value = res.captchaEnabled !== undefined ? res.captchaEnabled : true;
    if (captchaEnabled.value) {
      codeUrl.value = `data:image/gif;base64,${res.img}`;
      loginForm.uuid = res.uuid;
    }
  });
};

// 读取cookie
const getCookie = () => {
  const username = Cookies.get('username');
  const password = Cookies.get('password');
  const rememberMe = Cookies.get('rememberMe');

  if (username) loginForm.username = username;
  if (password) loginForm.password = decrypt(password);
  if (rememberMe) loginForm.rememberMe = Boolean(rememberMe);
};

// 登录处理
const handleLogin = () => {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        // 保存记住密码
        if (loginForm.rememberMe) {
          Cookies.set('username', loginForm.username, { expires: 30 });
          Cookies.set('password', encrypt(loginForm.password), { expires: 30 });
          Cookies.set('rememberMe', loginForm.rememberMe, { expires: 30 });
        } else {
          Cookies.remove('username');
          Cookies.remove('password');
          Cookies.remove('rememberMe');
        }

        // 调用登录接口
        const loginRes = await login(
            loginForm.username,
            loginForm.password,
            loginForm.code,
            loginForm.uuid
        );

        // 保存token
        Cookies.set('Admin-Token', loginRes.token);

        // 获取用户信息（保持与若依流程一致）
        await getInfo();

        // 登录成功跳转到首页
        router.push('/').then(() => {
          ElMessage.success('登录成功');
        });
      } catch (error) {
        ElMessage.error(error.message || '登录失败');
        getCode(); // 刷新验证码
      } finally {
        loading.value = false;
      }
    }
  });
};

onMounted(() => {
  getCode();
  getCookie();
});
</script>

<style scoped>
.login-container {
  width: 400px;
  margin: 100px auto;
  padding: 30px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background: #fff;
}

.login-container h1 {
  text-align: center;
  color: #165DFF;
  margin-bottom: 20px;
}

.code-img {
  display: inline-block;
  width: 32%;
  margin-left: 3%;
  vertical-align: middle;
}

.code-img img {
  width: 100%;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
}
</style>