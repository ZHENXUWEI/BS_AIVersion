<script setup>
import { ref } from 'vue'
import {policyDataAPI} from '@/api/service';
import { useRoute,useRouter } from 'vue-router';
const route = useRoute();
const router = useRouter();

const urlParams = route.query;
const tableData=ref(null);

const getPolicyData=async (policyid)=>{
    console.log(policyid)
   const res= await policyDataAPI(policyid);
   tableData.value=res.data
   console.log( tableData.value)
}

tableData.value={
    policyName:"aaaa",
    leaderDepartment:"aaaa",
    publishDate:"aaaa",
    deadTime:"aaaa",
    policyType:"aaaa",
    policyFileUrl:"aaaaaaaaaaaaaaaaa",
}
getPolicyData(urlParams.policyid)


//后退
const confirmFun=()=>{

    router.go(-1)
}
</script>

<template>
    <div>
        <h1 style="width: 500px;">政策详情:</h1>
        <table border=1 width=500 height=400 align="center">
            <tbody>
                <tr>
                    <td>政策名称：</td>
                    <td>{{tableData.name}}</td>
                </tr>
                <tr>
                    <td>发布部门：</td>
                    <td>{{tableData.leaderDepartment}}</td>
                </tr>
                <tr>
                    <td>发布时间：</td>
                    <td>{{tableData.publishDate?tableData.publishDate.split("T")[0]:''}}</td>
                </tr>
                <tr>
                    <td>结束时间：</td>
                    <td>{{tableData.expireDate}}</td>
                </tr>
                <tr>
                    <td>政策类型</td>
                    <td>{{tableData.policyType}}</td>
                </tr>
                <tr>
                    <td>下载地址：</td>
                    <td style="text-align: left;"><a :href="tableData.policyFileUrl">
                        {{ tableData.policyFileUrl }}
                    </a></td>
                </tr>
            </tbody>
        </table>
        <span class="edit_button" @click="confirmFun">确定</span>
    </div>
  
</template>

<style scoped>
div{
    display: flex;
    flex-direction: column;
    
    align-items: center;
    justify-content: space-between;
}
table {
    margin-top: 1%;
    margin-bottom: 3%;
    border-collapse: collapse; /* 合并表格边框 */
    border: 1px solid #ccc; /* 设置表格边框样式和颜色 */
  }

  td {
    border: 1px solid #ddd;
    text-align: center;
    padding: 10px;
    flex:1;
}
.edit_button{
    width: 5vw;
    height: 2vw;
    display: block;
    display: flex;
    justify-content: center;
    align-items: center;
    
    background-color: rgb(8, 154, 227);
    cursor: pointer;
    border-radius: 1vh;
    color: white;
    margin-top: 1vw;
    font-size: 1vw;
}
</style>
