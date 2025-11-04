<script setup>
    import Header from "./Header.vue"
    import {ref,onMounted} from "vue"
    import { useRoute,useRouter } from 'vue-router';
    import { policyinfoAPI } from "@/api/policy"
    import {daysUntil} from "@/utils/daysUtil"
    const policyinfoData=ref({
        leaderDepartment:"",
        deadTime:"",
        name:"",
        content:"",
        policyClass:"",
        policyConditions:"",
        contact:"",
        phone:""
    });
    const getPolicy=async (obj)=>{
    // console.log(type1,policyType,subPolicyType)
    const res=await policyinfoAPI(obj)
    policyinfoData.value=res.data.row[0]
    console.log(policyinfoData.value)
  }

  const route = useRoute();
  const router = useRouter();
  const urlParams = route.query;

  const backFun=()=>{
    router.go(-1)
  }

  onMounted(async ()=>{
    const urlParams = route.query;
    if("id" in urlParams){
      console.log(urlParams)
      getPolicy({type:3,id:urlParams.id})
    }
})
const lastDays = (deadTime)=>{
    return daysUntil(deadTime)
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
        <Header ></Header>
        <div class="top">
            <span style="font-size: 12px;">您的位置：政策库>政策详情</span>
        </div>

        <div class="middle">
            <div class="left">
                <div><a href="#target1">政策内容</a></div>
                <div><a href="#target2">政策类型</a></div>
                <div><a href="#target3">政策条件</a></div>
                <div><a href="#target4">联系方式</a></div>
            </div>
            <div class="hidden_box">

            </div>
            <div class="right">

                <div class="header_box">
                    <h1>{{policyinfoData.name}}</h1>
                    <span @click="backFun" style="cursor:pointer">返回</span>
                </div>

                <div class="policy_tab">
                    <table >
                        <tbody>
                            <tr>
                                <td>牵头部门</td>
                                <td>{{policyinfoData.leaderDepartment}}</td>
                                <td>剩余时间</td>
                                <td>{{timeStatusFun(policyinfoData.deadTime)}}</td>
                            </tr>
                            <tr >
                                <td>政策原文</td>
                                <td colspan="3">{{policyinfoData.name}}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="polciy_content" id="target1">
                    <div class="title">
                        <div></div>
                        <span>政策内容</span>
                    </div>
                    <p class="content">{{policyinfoData.content}}</p>
                </div>

                <div class="polciy_content" id="target2">
                    <div class="title">
                        <div></div>
                        <span>政策类型</span>
                    </div>
                    <p class="content">{{policyinfoData.policyClass}}</p>
                </div>

                <div class="polciy_content" id="target3">
                    <div class="title">
                        <div></div>
                        <span>政策条件</span>
                    </div>
                    <p class="content">
                        {{policyinfoData.policyConditions}}
                    </p>
                </div>

                <div class="polciy_content" id="target4">
                    <div class="title">
                        <div></div>
                        <span>联系方式</span>
                    </div>

                    <div class="content">
                        <table>
                            <tbody>
                                <tr>
                                    <td>单位联系人</td>
                                    <td>{{policyinfoData.contact}}</td>
                                    <td>联系电话</td>
                                    <td>{{policyinfoData.phone}}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    
                    
                </div>
            </div>
        </div>

        <div id="footer">
            <p>主办单位：杭州市发展和改革委员会地址：杭州市解放东路18号市民中心B座17楼邮编：310020电话：0571-96345</p>
            <p>网站标识码：3301000019浙公网安33010202002719号备案证编号：浙ICP备2022028025号-3</p>
        </div>
        
    </div>
</template>

<style scoped>

#main{
    width: 100%;
    height: 100%;
    /*padding: 20px;*/
    display: flex;
    flex-direction: column;
}

.top{
    width: 70%;
    height: 40px;
    display: flex;
    align-self: center;
    padding: 10px;
    background-color: aliceblue;  
}

.middle{
    width: 70%;
    height: 100%;
    display: flex;
    flex-direction: row;
    align-self: center;
    padding: 10px;
    justify-content: space-between;
}

.left{
    width: 8%;
    height: 40vh;
    position: fixed;
    top:45%;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    background-color: white;
    box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.5);
}

.left div{
    width: 100%;
    height: 15%;
   
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 20%;
}
.left div a{
    display: block;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: inherit;
  text-decoration: none;
}

.left div a:hover{
    background-color: #ddd;
}

.right{
    width: 85%;
    height: 100%;
}

.right .header_box{
    width: 100%;
    height: 15vh;
    display: flex;
    justify-content: space-between;
    border-bottom: 1px solid  #e2e4e6;
}

.right .header_box h1{
    width: 50%;
    font-size: larger;
}

.right .header_box span{
    display: block;
    width: 10%;
    height: 5vh;
    border: 1px solid blue;
    margin-right: 8vh;
    align-self: center;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 2vh;
    font-size: small;
}

table {
    width: 100%;
    border-collapse: collapse;
}
td {
    border: 1px solid #ddd;
    text-align: center;
    padding: 10px;
    flex:1;
}

td:nth-child(1),td:nth-child(3){
  
    background-color: #ddd;
}


.polciy_content{
    width: 100%;
    margin-top:8vh;
    display: flex;
    flex-direction: column;
}
.polciy_content .title{
   display: flex;
}
.polciy_content .title div{
    width: 1vh;
    height: 3vh;
    margin: 0 2vh;
    background-color: rgb(8, 154, 227);
    border-radius: 1vh;
}

.polciy_content .content{
    width: 100%;
    padding: 2vh;
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
    font-size: 1rem;
    cursor: pointer;
  }
</style>
