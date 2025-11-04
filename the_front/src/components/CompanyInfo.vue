<script setup>
import Header from "./Header.vue"
import { useRoute } from 'vue-router';
import { loginAPI } from "@/api/user"
import {ref} from "vue"
const is_show=ref(true)
const route=useRoute()
const companyInfo=ref({})
const urlParams = route.query;
const getCompanyInfo=async()=>{
    const res=await loginAPI(urlParams.id)
    companyInfo.value=res
}
getCompanyInfo()
</script>

<template>
    <div id="main">
        <Header ></Header>
        <div class="top">
            <span style="font-size: 12px;">您的位置：我的>企业详情</span>
        </div>

        <div class="middle">
            <div class="left">
                <div><a href="#target1">工商信息</a></div>
                <div><a href="#target2">法人代表</a></div>
                <div><a href="#target3">营业执照</a></div>
            </div>
            <div class="hidden_box">

            </div>
            <div class="right">
                <div class="header_box">
                    <h1>企业名称：{{companyInfo.companyName}}</h1>
                </div>
                <div class="polciy_content" style="margin-top: 2vh;" id="target1">
                    <div class="title">
                        <div></div>
                        <span>工商注册信息</span>
                    </div>
                    <div class="content">
                        <table>
                            <tr>
                                <td>统一社会信用代码</td>
                                <td>{{companyInfo.USCC}}</td>
                                <td>企业类型</td>
                                <td>{{companyInfo.companyType}}</td>
                            </tr>
                            <tr>
                                <td>登记机关</td>
                                <td>{{companyInfo.regAuthority}}</td>
                                <td>成立日期</td>
                                <td>{{companyInfo.establishDate}}</td>
                            </tr>
                            <tr >
                                <td>经营范围</td>
                                <td colspan="3">{{companyInfo.businessScope}}</td>
                            </tr>
                            <tr >
                                <td>主管税务局名称</td>
                                <td colspan="3">{{companyInfo.cTaxAuthority}}</td>
                            </tr>
                        </table>
                    </div>
                    
                </div>
                
                <div class="polciy_content" id="target2">
                    <div class="title">
                        <div></div>
                        <span>法人代表</span>
                    </div>
                    <div class="content">
                        <table>
                            <tr>
                                <td>法人</td>
                                <td>{{companyInfo.legalPerson}}</td>
                                <td>联系方式</td>
                                <td>{{companyInfo.phone}}</td>
                            </tr>
                        </table>
                    </div>
                </div>

                <div class="polciy_content" id="target3">
                    <div class="title">
                        <div></div>
                        <span>营业执照</span>
                    </div>
                    <div class="content">
                        <img src="" alt="">
                    </div>
                    
                </div>
                <!-- <span class="edit_button">修改</span> -->
            </div>
            
        </div>

        <div id="footer">
            <p>主办单位：杭州市发展和改革委员会地址：杭州市解放东路18号市民中心B座17楼邮编：310020电话：0571-96345</p>
            <p>网站标识码：3301000019浙公网安33010202002719号备案证编号：浙ICP备2022028025号-3</p>
        </div>

        <!-- <el-dialog title="修改企业信息" v-model="is_show">
            
        </el-dialog>
         -->
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
    height: 30vh;
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
    display: flex;
    flex-direction: column;
    align-items: center;
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
    font-size: 3vh;
    align-self: center;
}

table {
    width: 100%;
    border-collapse: collapse;
}
#target1 table {
    width: 100%;
    height: 35vh;
    border-collapse: collapse;
}
td {
    border: 1px solid #f1e8e8;
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

.edit_button{
    display: block;
    padding: 1vh 2vh;
    background-color: rgb(8, 154, 227);
    cursor: pointer;
    border-radius: 1vh;
    color: white;
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
