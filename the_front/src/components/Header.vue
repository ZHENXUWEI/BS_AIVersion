
<script setup>
    import {ref,onMounted,defineEmits } from "vue"
    import { loginAPI} from "@/api/user"
    import {policyVisitNAPI } from "@/api/policy"
    import { getCookie,setCookie,removeCookie } from '@/utils/cookie'
    import { useRouter,useRoute  } from 'vue-router';
    import { ElMessage } from 'element-plus'
    const router = useRouter();
    const route = useRoute();
    const emit = defineEmits(['updateValue'])
    const users = ref({});
    const isOpen = ref(false);
    const searchData=ref('')
    const fetchUsers=async ()=>{
       
        const res=await loginAPI(1)
        // console.log(res)
        users.value=res.data[0]
        setCookie({
            id:2,
            companyName:users.value.companyName,
            token:123456
        })
        await policyVisitNAPI()
    }


const sendToParent=()=>{
  emit('updateValue', {policyName:searchData.value})
}
const loginOut=()=>{
        removeCookie()
        users.value=''
        router.push({ name: 'Home' });
    }

const companyInfo=()=>{
        let id=users.value.id
        router.push({path:`/service`})
    }

const searchFun=()=>{
        if(searchData.value==''){
            ElMessage.error('政策名称不能为空.')
            return
    }
    //    console.log(route.path)
    
    if(route.path=="/policy"){
        sendToParent()
        router.push({path:`/policy`,query: {policyName:searchData.value}})
    } else{
        router.push({path:`/policy`,query: {policyName:searchData.value}})
    }
         
    }
    onMounted(()=>{
        const info = getCookie()
        
        if(info){
            users.value=info
        }
    })
</script>

<template>
    <div id="header" >
        <div class="logo">
            <div style="margin-left:4vw" class="u1650_text ">
                <img class="logoimg" src="../assets/images/logo.png" alt="">
            </div>
            <div id="u1" style="margin-left:6.3vw" class="u1650_text ">
                <span>政策服务平台</span>
            </div>
       </div>
        <div  class="serach_box" >
            <input id="policyName" v-model="searchData"   placeholder="请输入政策名称"/>
            <el-button style="height: 2.5vw;border-radius:0 1vh 1vh 0" type="primary" @click="searchFun">搜索</el-button>
        </div>
    </div>
    
    <div id="nav" >
        <div>
            <span style="cursor: pointer;margin-left:11.2vw"><RouterLink class="rl" to="/" >首页</RouterLink> </span>
            <span style="cursor: pointer;"><RouterLink class="rl" to="/policy" >政策库</RouterLink></span>
            <span style="cursor: pointer;"><RouterLink class="rl" to="/match" >政策匹配</RouterLink></span>
        </div>
        <div class="com_box"
                v-if="users.companyName"
                @mouseenter="isOpen = true"
                @mouseleave="isOpen = false"
            >
                <span  class="companyName">{{users.companyName}}</span>
                <div class="dropdown_box">
                    <div v-if="isOpen" class="dropdown">
                        <ul>
                            <li @click="companyInfo">业务系统</li>
                            <li>管理系统</li>
                            <li @click="loginOut">退出登录</li>
                        </ul>
                    </div>
                </div>
            </div>
        
            <el-button @click="fetchUsers" v-else style="margin-right: 10vw;font-size:1vw;width:3vw;height:2vw">登录</el-button>
    </div>
    
</template>

<style scoped>
#header{
    width: 100%;
    height: 7.5vw;
    display: flex;
    align-self: center;
    justify-content:space-between;  
    align-items: center;
}

#header .logo{
    display: flex;
}

#header .logoimg{
    width: 10vw;
    height: 6vw;
}
.u1650_text{
    display: flex;
    align-items: center; 
    font-size: 1.9vw;
    font-weight: bold;
}

#header .serach_box{
    display: flex;
    flex-direction: row;
    align-items: center;
    width: 22vw;
    height: 2.5vw;
    border: 0.15vw solid #5A9CF8;
    border-right: none;
    border-radius: 0.5vw 0.5vw 0.5vw 0.5vw;
    padding: 1vw 0vw 1vw 2vw;
    margin-right: 8.5vw;
}

#policyName{
    border: none;
    outline: none;
    width: 18vw;
    height: 2.2vw;
}

#nav{
    width: 100%;
    height: 5.5vw;
    background: linear-gradient(to right,#457cff,#a7defc);
    display: flex; 
    justify-content: space-between;
    align-items: center; 
}


#nav div{
    display: flex;
}

#nav span{
    display: block;
    width: 5vw;
    color:white;   
    margin-left: 4vw;
}


.com_box {
    position: relative;
    cursor: pointer;
    margin-right: 6vw;
}
#nav .companyName{
    display:block;
    display: flex;
    width:15vw !important;
    height:5vw;
    justify-content: center;
    align-items: center;
    font-size: 1.2vw;
}
.dropdown {
    position: absolute;
    top:100%;
    left:50%;
    transform: translate(-25%,0);
    background-color: #f9f9f9;
    width: 8vw;
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
    z-index: 1;
    border-radius: 10px;
    font-size: 0.8vw;
  }
   
  .dropdown_box{
    padding: 1vw;
    margin-top: 0;
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
.rl{
    color: inherit;
    text-decoration: none;
    border: none;
    padding: 0;
    background-color: transparent;
    font-size: 1.2vw;
    cursor: pointer;
  }
</style>