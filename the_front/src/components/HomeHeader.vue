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
import { ElMessage } from "element-plus";
import {createRouter as $router} from "vue-router/dist/vue-router.esm-browser.js";

const props = defineProps({
  parentData: String,
});
const router = useRouter();
const route = useRoute();
const users = ref({});
// users.value.companyName="浙江省产业大数据有限公司"
const isOpen = ref(false);
const fetchUsers = async () => {
  // const redirectUri = encodeURIComponent("http://172.19.15.224:5317");
  // const fullUrl = `http://172.18.30.42/ssoserver/moc2/authorize?response_type=code&client_id=zcfw&redirect_uri=${redirectUri}&state=ok`;
  try {
    // 直接请求后端的/oauth-login接口，由后端处理重定向
    window.location.href = '/api/oauth-login'; // 利用后端接口重定向到SSO服务
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

const loginOut = () => {
  removeCookie();
  users.value = "";
  router.push({ name: "Home" });
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

    // const token = await getAccessToken(query.code, query.state);
    // if (token.data) {
    //   const openRes = await getOpenId(token.data);
    //   if (openRes.data.openid) {
    //     const usrInfo = await getusrinfo(token.data, openRes.data.openid);
    //     console.log("usrInfo");
    //     console.log(usrInfo);
    //   }
    // }
  } else {
    console.log("URL 上没有任何参数");
  }
  const info = getCookie();

  console.log(111111111111);
  console.log(info);

  if (info) {
    users.value = info;
  }
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

    <div class="user">
      <div class="out_com_box" v-if="users.companyName">
        <div
          class="com_box"
          @mouseenter="isOpen = true"
          @mouseleave="isOpen = false"
        >
          <!-- <span class="qiyelogo">企业</span> -->
          <span class="companyName" @click="BusinessSystem">{{
            users.companyName
          }}</span>
          <!-- <div class="dropdown_box" v-if="isOpen">
                    <div  class="dropdown">
                        <ul>
                            <li @click="BusinessSystem">业务系统</li>
                            <li>管理系统</li>
                            <li >退出登录</li>
                        </ul>
                    </div>
                </div> -->
        </div>
        <span class="suxian"></span>
        <span
          class="loginout"
          @click="loginOut"
          style="margin-right: 2vw; cursor: pointer;font-size: 0.9vw;"
          >退出登录</span
        >
      </div>

      <div class="in_com_box" v-else>
        <el-button style="margin-right: 2vw;cursor: pointer;font-size: 0.9vw;padding: 0.9vw;max-height: 1vw;border-radius: 0.2vw;" @click="fetchUsers"><span >登录</span>
          </el-button
      >
      </div>
      
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
