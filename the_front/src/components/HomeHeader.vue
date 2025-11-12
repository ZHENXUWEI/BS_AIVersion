<script setup>
import { ref, onMounted, defineEmits, defineProps } from "vue";
import {
  loginAPI,
  // getAccessToken,
  getOpenId,
  // getusrinfo,
  SSOLogin,
  getInfo,
  jump,
} from "@/api/user";
import { policyVisitNAPI } from "@/api/policy";
import { getCookie, setCookie, removeCookie } from "@/utils/cookie";
import { useRouter, useRoute } from "vue-router";
import {ElMessage, ElMessageBox} from "element-plus";
import {createRouter as $router} from "vue-router/dist/vue-router.esm-browser.js";
import Cookies from "js-cookie";
import request from "@/utils/request.js";

const props = defineProps({
  parentData: String,
});
const router = useRouter();
const route = useRoute();
const users = ref({});
// users.value.companyName="浙江省产业大数据有限公司"
const isOpen = ref(false);
const isLogin = ref(false); // 登录状态标记
const fetchUsers = async () => {
  // const redirectUri = encodeURIComponent("http://172.19.15.224:5317");
  // const fullUrl = `http://172.18.30.42/ssoserver/moc2/authorize?response_type=code&client_id=zcfw&redirect_uri=${redirectUri}&state=ok`;
  try {
    // 直接请求后端的/login接口
    window.location.href = '/login';
  } catch (error) {
    console.error('登录跳转失败:', error);
  }

  // await jump();

    // const res = await loginAPI(1);
    // console.log(res);
    // users.value = res.data[0];
    // users.value.companyName = 'test';
    // setCookie({
    //   id: 1,
    //   companyName: users.value.companyName,
    //   token: 123456,
    // });
    // await policyVisitNAPI();
    // router.push({ path: `/policyModel` });
};

// 检查登录状态
const checkLoginStatus = () => {
  // 若依默认token存储键为Admin-Token，可根据实际情况修改
  const token = Cookies.get('Admin-Token');
  isLogin.value = !!token;
};

// 未登录时跳转到登录页
const goToLogin = () => {
  router.push('/login');
};

// 已登录时跳转到若依后台
const goToRuoYiBackend = () => {
  // 若依后台地址，根据实际部署地址修改
  window.location.href = 'http://localhost:81';
};

// 退出登录处理
const handleLogout = async () => {
  try {
    // 显示确认弹窗
    await ElMessageBox.confirm(
        '确定要退出登录吗？',
        '退出确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    );

    // 调用若依退出接口（若依默认退出接口）
    await request({
      url: '/logout',
      method: 'post'
    });

    // 清除本地存储的token和用户信息
    Cookies.remove('Admin-Token');
    Cookies.remove('username');
    Cookies.remove('password');
    Cookies.remove('rememberMe');

    // 更新登录状态
    isLogin.value = false;

    // 跳转到登录页
    router.push('/');
    ElMessage.success('退出成功');
  } catch (error) {
    // 取消退出时不做处理
    if (error === 'cancel') return;
    ElMessage.error('退出失败，请重试');
  }
};
onMounted(async () => {
  const query = route.query;
  if (Object.keys(query).length > 1) {
    const token = await SSOLogin(query.code, query.state);
    console.log(token);
    setCookie({
      token: token.token,
    });
    const usrInfo = await getInfo();
    console.log(usrInfo);
    const res = await loginAPI(usrInfo.user.companyId);
    console.log(res);
    users.value = res.data[0];
    setCookie({
      id: res.data[0].id,
      companyName: users.value.companyName,
    });
    await policyVisitNAPI();
    router.push({ path: `/policyModel` });
  } else {
    console.log("URL 上没有任何参数");
  }
  const info = getCookie();

  if (info) {
    users.value = info;
  }
  checkLoginStatus();
  // 监听路由变化，实时更新登录状态
  router.afterEach(() => {
    checkLoginStatus();
  });
});
const activeIndex = ref(0);

if (route.path == "/") {
  activeIndex.value = 0;
} else if (route.path == "/policy" || route.path == "/detail") {
  activeIndex.value = 1;
} else if (route.path == "/match") {
  activeIndex.value = 2;
} else {
  activeIndex.value = null;
}

const BusinessSystem = () => {
  let id = users.value.id;
  router.push({ path: `/service` });
};

const toAI=()=>{
  router.push('/ai/chat')
}
</script>

<template>
  {{ parentData }}
  <div id="header">
    <div class="nav">
<!--      <img src="../assets/images/logo.png" alt="" />-->
      <span class="suxian"></span>
      <span style="font-size: 1.8vw; font-weight: bold">政策服务平台</span>

      <div class="nav_span">
        <span
          :class="{ active: activeIndex === 0 }"
          style="cursor: pointer; margin-left: 5vw"
          ><RouterLink class="rl" to="/">首页</RouterLink>
        </span>
        <span :class="{ active: activeIndex === 1 }" style="cursor: pointer"
          ><RouterLink class="rl" to="/policy">政策查询</RouterLink></span
        >
        <span :class="{ active: activeIndex === 2 }" style="cursor: pointer"
          ><RouterLink class="rl" to="/match">政策匹配</RouterLink></span
        >
        <el-button
            type="primary"
            @click="toAI()"
            style="margin: 1vw;"
        >
          AI 智能问答</el-button>
      </div>
    </div>

    <div class="header-actions">
      <!-- 登录/退出/后台跳转按钮组 -->
      <template v-if="isLogin">
        <el-button
            type="primary"
            @click="goToRuoYiBackend"
            style="margin-right: 10px"
        >
          前往后台管理
        </el-button>
        <el-button
            type="warning"
            @click="handleLogout"
        >
          退出登录
        </el-button>
      </template>
      <el-button
          v-else
          type="primary"
          @click="goToLogin"
      >
        登录
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.in_com_box{
  position: relative;
  display: flex;
  align-items: center;
}
#header {
  width: 100%;
  display: flex;
  align-self: center;
  align-items: center;
  justify-content: space-between;
  background-color: #ffffff;
}
#header .nav {
  display: flex;
  align-items: center;
}
#header .nav img {
  width: 17vw;
  height: 3.5vw;
  margin-left: 2vw;
}
.suxian {
  display: block;
  width: 0.1vw !important;
  height: 1.5vw;
  border: 1px solid #ccc;
  margin: 0 0.8vw;
}

#header .nav_span {
  display: flex;
  align-items: center;
}
#header .nav_span span {
  display: flex;
  display: block;
  align-items: center;
  font-size: 1vw;
  margin-left: 2vw;
  padding: 1.2vw 0;
}

.active {
  border-bottom: 2px solid blue;
}

#header .user {
  display: flex;
  align-items: center;
}
.out_com_box {
  position: relative;
  display: flex;
  align-items: center;
  font-size: 1vw;
}
.com_box {
  display: flex;
  align-items: center;
  cursor: pointer;
}

#header .user .qiyelogo {
  display: block;
  font-size: 1vw;
  width: 3vw;
  height: 3vw;
  color: white;
  background: linear-gradient(to right, #448af9, #579dff);
  text-align: center;
  line-height: 3vw;
  border-radius: 50%;
  margin-right: 1vw;
}

#nav .companyName {
  display: block;
  display: flex;
  width: 15vw !important;
  height: 5vw;
  justify-content: center;
  align-items: center;
}

.dropdown_box {
  position: absolute;
  top: 100%;
  left: 50%;
  margin: 0;
  padding: 0;

  z-index: 999999;
}
.dropdown {
  background-color: red;

  background-color: #f9f9f9;
  width: 8vw;
  height: 9vw;
  box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
  z-index: 999;
  border-radius: 10px;
  font-size: 0.8vw;
}

.dropdown ul {
  list-style-type: none;
  width: 100%;
  height: 9vw;
  display: flex;
  flex-direction: column;
  padding: 1vw;
  justify-content: space-around;
  align-items: center;
}

.dropdown ul li:hover {
  background-color: #f1f1f1;
}

.rl {
  color: inherit;
  text-decoration: none;
  border: none;
  padding: 0;
  background-color: transparent;
  font-size: 1vw;
  cursor: pointer;
}

.loginout {
  background-color: #0d9deb;
  border-radius: 0.5vw;
  outline: none ;
  color: white;
  border: 0.1vw solid #dcdfe6;
  padding: 0.3vw;
}

</style>
