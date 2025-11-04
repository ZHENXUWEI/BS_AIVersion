<script setup>
    import {ref,onMounted} from "vue"
    import { loginAPI } from "@/api/user"
    import { policyinfoAPI,policystatusAPI,policyVisitNAPI,policyMatchNAPI } from "@/api/policy"
    import { getCookie,setCookie,removeCookie } from '@/utils/cookie'
    import {daysUntil} from "@/utils/daysUtil"
    import { useRouter } from 'vue-router';
    import { ElMessage } from 'element-plus'
    const router = useRouter();
    // import axios  from "axios";
    const users = ref({});
    const isOpen = ref(false);
    const policyStatus = ref({});
    const policyinfoData=ref({});
    const searchData=ref('')
    const value = ref('')

    const options = [
    {
        value: '牵头部门',
        label: '牵头部门',
    },
    {
        value: '适用区域',
        label: '适用区域',
    },
    {
        value: '政策类别',
        label: '政策类别',
    },
    {
        value: '政策类型',
        label: '政策类型',
    },
    {
        value: '申报状态',
        label: '申报状态',
    },
    {
        value: '发布时间',
        label: '发布时间',
    },
    ]

    
    const fetchUsers=async ()=>{
        const res=await loginAPI(1)
        users.value=res.data[0]
        console.log(users.value)
        setCookie({
            id:2,
            companyName:users.value.companyName,
            token:123456
        })
        await policyVisitNAPI()
        const res1=await policystatusAPI()
        policyStatus.value=res1.data
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
    router.push({path:`/policy`,query: {policyName:searchData.value}})
}
const selectFun=async (data)=>{
    // console.log(data)
    var areaName="全部"
    if(data=='牵头部门') areaName="发改"
    if(data=='适用区域') areaName="国家级"
    if(data=='政策类别') areaName="节能环保"
    if(data=='政策类型') areaName="财税扶持"
    if(data=='申报状态') areaName="申报中"
    if(data=='发布时间') areaName="全部"

   const res=await policyinfoAPI({type:1,policyType:data,subPolicyType:areaName})
   if(res.data==undefined){
        policyinfoData.value=[]
        return
    }
    var arr=res.data.row   

    console.log(arr)
    // console.log(arr.length)
   if(arr.length>5){
    policyinfoData.value=arr.slice(0, 5);

   }else{
    policyinfoData.value=arr
   }
}

const lastDays = (deadTime)=>{
return daysUntil(deadTime)
}

const getPolicy=async ()=>{
// console.log(type1,policyType,subPolicyType)
    const res=await policyinfoAPI({type:1,policyType:"牵头部门",subPolicyType:"全部"})
    if(res.data==undefined){
        policyinfoData.value=[]
        return
    }
    var arr=res.data.row   
    console.log(arr.length)
   if(arr.length>5){
    policyinfoData.value=arr.slice(0, 5);
   }else{
    policyinfoData.value=arr
   }
  
}

// 立即自评
const toPolicyModelFun=async()=>{
    const info = getCookie()
    if(!info){
        ElMessage.error('请先登录.')
        return
    }
   await policyMatchNAPI()
   router.push("/policyModel")
}
onMounted(async ()=>{
    const info = getCookie()
    if(info){
        users.value=info
    }
    const res=await policystatusAPI()
    policyStatus.value=res.data
    console.log(policyStatus.value)

    getPolicy()

})


const detail=(id)=>{
    router.push({path:`/detail`,query: {id}})
}

const timeStatusFun=(t)=>{
    if(t==null){
        return "长期有效"
    }else{
        return lastDays(t)<=0?"已过期限":"剩余"+lastDays(t)+"天"
    }
    
}
</script>

<template>
   <div id="main">
        <div id="header" class="ax_default">
           <div class="logo">
                <div style="margin-left:4vw" class="u1650_text ">
                    <img class="logoimg" src="../assets/images/logo.png" alt="">
                </div>
                <div style="margin-left:6.3vw" class="u1650_text ">
                    <span>杭钢集团科技创新平台</span>
                </div>
           </div>
        
            <div style="margin-right:4vw" class="u1650_text ">
                <span>政策服务平台</span>
            </div>
        </div>
        
        <div id="nav" >
            <div class="nav_span">
                <span style="cursor: pointer;margin-left:11.2vw"><RouterLink class="rl" to="/" >首页</RouterLink> </span>
                <span style="cursor: pointer;"><RouterLink class="rl" to="/policy" >政策库</RouterLink></span>
                <span style="cursor: pointer;"><RouterLink class="rl" to="/match" >政策匹配</RouterLink></span>
            </div>

            <div  class="search " style="margin-left: 15vw;">
                <input id="username" 
                type="text"
                style="width: 15vw;height:2vw;"  
                placeholder="请输入政策名称"
                v-model="searchData"
                />
                <button class="btn"
                @click="searchFun"
                >智能搜索</button>
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
            <el-button @click="fetchUsers" v-else style="margin-right: 4vw;font-size:1vw;width:3vw;height:2vw">登录</el-button>
            
           
        </div>

        <div id="carousel" >
            <div class="block text-center">
                <el-carousel height="600px">
                <el-carousel-item v-for="item in 4" :key="item">
                    <img src="../assets/images/banner1.jpg" width="100%" height="600px" alt="">
                </el-carousel-item>
                </el-carousel>
            </div> 
            
        </div>


        <div id="policyOverview">
            <div class="policyOverview_show">
                <img src="../assets/images/icon1.jpg" class="policyOverview_img" alt="">
                <div style="padding-left: 10px;">
                    <p><font  color="orange">{{policyStatus.policyN}}万</font>条</p>
                    <p>政策库</p>
                </div>
            </div>

            <div class="policyOverview_show">
                <img src="../assets/images/icon2.jpg" class="policyOverview_img" alt="">
                <div style="padding-left: 10px;">
                    <p><font  color="orange">{{ Math.floor((60000+policyStatus.userVisitN)/10000)}}万+</font>次</p>
                    <p>用户访问</p>
                </div>
            </div>

            <div class="policyOverview_show">
                <img src="../assets/images/icon3.jpg" class="policyOverview_img" alt="">
                <div style="padding-left: 10px;">
                    <p><font  color="orange">{{policyStatus.companyN}}</font>个</p>
                    <p>企业用户</p>
                </div>
            </div>

            <div class="policyOverview_show">
                <img src="../assets/images/icon4.png" class="policyOverview_img" alt="">
                <div style="padding-left: 10px;">
                    <p><font  color="orange">{{policyStatus.matchN}}次</font>条</p>
                    <p>政策智能匹配</p>
                </div>
            </div>

            <div class="policyOverview_show">
                <img src="../assets/images/icon5.jpg" class="policyOverview_img" alt="">
                <div style="padding-left: 10px;">
                    <p ><font  color="orange">{{policyStatus.declarationN}}</font>次</p>
                    <p >企业申报数量</p>
                </div>
            </div>
        </div>

        <div id="newPolicy">
            <div class="newP">
                <h1>最新政策</h1>
                <p><span>
                    <font color="grey" ><RouterLink class="rl" to="/policy" style="font-size:2.5vh;cursor: pointer;" >更多>></RouterLink></font>
                </span>
                </p>
            </div>
            <div style="width:100%">
                <el-card style="padding-left:3vh;" class="selectPolicy"  shadow="hover">
                    <el-select 
                    @change="selectFun"
                    class="selectDeep"
                    v-model="value" 
                    placeholder="政策类别" 
                    style="width: 120px;">
                    <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    />
                    </el-select>
                      
                </el-card>
    
                <el-card 
                class="el-card "
                v-for="(item,index) in policyinfoData" 
                :key="index" 
                style="max-width: 100%;margin-top:20px" 
                shadow="always">
                <h5 @click="detail(item.id)" style="cursor: pointer;;padding-left:5vh;font-size:2.5vh">{{item.name}}</h5>
                <p style="padding:2vh 0 3vh 5vh;color: grey;font-size:small" >
                    <span>牵头部门：</span><span>{{item.leaderDepartment}}</span>
                    <span style="margin-left: 3vh;">发布时间：</span><span>{{item.publishDate.split('T')[0]}}</span>
                </p>
                <div  style="width:100%;height:30px;padding-right: 5vh;text-align:end" >
                    <el-tag style="padding:0 20px;" type="danger">{{timeStatusFun(item.deadTime)}}</el-tag>
                    <el-tag style="padding:0 20px;margin-left: 3vh;" type="info">{{item.isMoney?"资金支持":"无资金支持"}}</el-tag>    
                </div>
            </el-card> 
            </div>
        </div>
        <div id="hotpolicy">
                <h3>热门项目申报</h3>
                <p style="color: grey;font-size:1vw;padding:1vw 0 1vw 2vw">海量项目申报查询 &nbsp;&nbsp;&nbsp;&nbsp; 一对一政策精准自评</p>
                <div class="enterprise">
                    <div>
                        <h4>国家高新技术企业</h4>
                        <span class="enterprise_text" 
                         @click="toPolicyModelFun"
                        style="cursor: pointer;">
                            立即自评</span>
                    </div>
                    <div>
                        <h4>科技型中小企业</h4>
                        <span class="enterprise_text" 
                         @click="toPolicyModelFun"
                        style="cursor: pointer;">
                            立即自评</span>
                    </div>
                    <div>
                        <h4>创新型中小企业</h4>
                        <span class="enterprise_text" 
                         @click="toPolicyModelFun"
                        style="cursor: pointer;">
                            立即自评</span>
                    </div>
                </div>
        </div>
        <div id="footer">
            
        </div>
   </div>

</template>

<style scoped>

#header{
    width: 100%;
    height: 7.5vw;
    display: flex;
    align-self: center;
    justify-content:space-between;  
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
    margin-right: 1.5vw;
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

/*轮播图*/
.carousel{
    display: flex;
    height: 55vw;
    position: relative;
}
.demonstration {
    color: var(--el-text-color-secondary);
  }
  
  .el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
    text-align: center;
  }
  
  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }
  
  .el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
  }

 /* 企业信息展示栏*/
 .enterprise-show{
    position: absolute;
    left:50%;
    top: 50%;
    transform: translate(50%,-50%);
    width: 300px;
    height: 280px;
    background-color: #9cd0ec;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-around;
 }

 .enterprise-show .content-show{
    width: 88%;
    height: 60%;
    background-color: white;
    border-radius: 10px;
 }

 .content-policy{
    width: 88%;
    display: flex;
    justify-content: space-between;
 }

 .content-policy span{
    display: block;
    color: white;
    padding: 2px 30px;
    font-size: small;
    border-radius: 2px;
 }

/*智能搜索框*/
.search{
    width: 23vw;
    height: 55%;
    
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: white;
    border-radius: 2vw;
    
}

.search input{
   border:none;
   outline: none;
}

.search .btn{
    cursor: pointer;
    border: 0.1vw solid #409eff;
    outline: none;
    width: 4.5vw;
    height:1.8vw;
    border-radius:0.8vw;
    background-color: #409eff;
    color:white;
    font-size:0.8vw;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-left: 1.6vw;
}

#policyOverview{
    display: flex;
    width: 85%;
    justify-content: space-between;
    height: 7vw;
    align-items: center;
    justify-self: center;
}

.policyOverview_show{
    display: flex;
}

.policyOverview_show .policyOverview_img{
    width: 3vw;
    height: 3vw;
}

.policyOverview_sho p{
    font-size: 1vw;
}

.policyOverview_show font{
    font-size: 2vw;
}

#newPolicy{
   
    width: 85%;
    display: flex;
    justify-self: center;
    flex-direction: column;
}

.newP{
    margin-top: 0.5vw;
    height: 8vw;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-items: center;
    justify-content: space-around;
    background-color: white;
    box-shadow: 1px 1px 1px 0px #ccc inset;
}

.newP h1{
    font-size: 1.5vw;
    padding-left: 2vw;
}

.newP p{
    text-align: end;
}
.selectPolicy{
    display: flex;
    align-items: center;
    height: 2vw;
    max-width: 100%;
}
::v-deep .el-select__wrapper{
    box-shadow: none;
    
    }
::v-deep .is-hovering{
    box-shadow: none !important;
   
}

#hotpolicy{
    border: 1px solid grey;
    width: 85%;
    display: flex;
    justify-self: center;
    flex-direction: column;
    padding: 2vw;
    
}

#hotpolicy h3{
    font-size: 1.5vw;
}

#hotpolicy .enterprise{
    width: 100%;
    display: flex;
    justify-content: space-around;
    padding-top: 1vw;
}

.enterprise div{
    width: 20%;
    height: 100px;
    border: 1px solid grey;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    background-color: white;
    border-radius: 10px;
}

.enterprise_text{
    display: block;
    width: 7vw;
    height: 1.8vw;
    font-size: small;
    text-align: center;
    background-color: #0d9deb;
    border-radius: 1vw;
    color:white;
    line-height: 1.8vw;
}

#footer{
    
    width: 100%;
    height: 100px;
    padding: 20px;
    background-color: #faf8f8;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-around;
    margin-top: 5%;
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