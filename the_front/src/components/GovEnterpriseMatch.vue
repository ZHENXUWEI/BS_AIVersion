<script setup>
import {ref,onMounted} from "vue"
import { getCookie} from '@/utils/cookie'
import {goventMatchAPI} from '@/api/service'
import { useRouter } from 'vue-router'
import {daysUntil} from "@/utils/daysUtil"
const router = useRouter()

const users = ref({});
const tableData=ref([]);
// const lastDays = (deadTime)=>{
//     return daysUntil(deadTime)
//   }

const timeStatusFun=(t)=>{
    return daysUntil(t)
    // if(t==null){
    //     return "长期有效"
    // }else{
    //     return lastDays(t)<=0?"已过期限":"剩余"+lastDays(t)+"天"
    // }  
}
// 获取政策匹配数据
const getGoventMatchData=async (obj)=>{
  const res=await goventMatchAPI(obj)
  tableData.value=res.data
  console.log(111,tableData.value)
}
// 时间过滤器
const formatDate = (date1) => {
  let date2 = new Date(date1)
  let year = date2.getFullYear()
    let month = date2.getMonth() + 1
    let day = date2.getDate()  
    
    return `${year}-${month}-${day}`
  }
// 跳转到政策详情页面
const toPolicyDetailFun=(policyid)=>{
    router.push({path:'/policydetail',query:{policyid}})
}

onMounted(()=>{
    const info = getCookie()
    if(info){
        users.value=info
    }
    
    getGoventMatchData({id:users.value.id})

})

// tableData.value = [
//   {
//     id:1,
//     policyid:1,
//     policyName: '【萧山区】2024年一季度工业投资奖励资助政策',
//     fabuBumen: '萧山区经济和杭息化局',
//     fabuTime:"2024-1-1",
//     shengyuTime:"剩今28天"
//   },
//   {
//     id:1,
//     policyid:1,
//     policyName: '【萧山区】2024年一季度工业投资奖励资助政策',
//     fabuBumen: '萧山区经济和杭息化局',
//     fabuTime:"2024-1-1",
//     shengyuTime:"剩今28天"
//   },
//   {
//     id:1,
//     policyid:1,
//     policyName: '【萧山区】2024年一季度工业投资奖励资助政策',
//     fabuBumen: '萧山区经济和杭息化局',
//     fabuTime:"2024-1-1",
//     shengyuTime:"剩今28天"
//   },
//   {
//     id:1,
//     policyid:1,
//     policyName: '【萧山区】2024年一季度工业投资奖励资助政策',
//     fabuBumen: '萧山区经济和杭息化局',
//     fabuTime:"2024-1-1",
//     shengyuTime:"剩今28天"
//   },
// ]

const checkProcess = () => {
	router.push('/applyProcess')
}


const stringLenFun=(str)=>{
    if(str==null){
        return
    }

    if(str.length<=35){
        return str
    }else{
        return str.slice(0, 36)+"......"
    }
}
</script>
<template>
  <div id="main">
    <h1>政企匹配</h1>
    <div id="content">
      
        <div class="policyBox">
          <div class="policyItem" v-for="item in tableData" :key="item.id">
          <div class="left" style="display: flex;flex-direction: column;">
            <div style="display: flex;align-items: center;">
              
              <span class="iconfont icon-bendi_local" style="background-color: rgba(236,237,253,.5);padding: 5px 8px;border-radius: 3px;color:#3880F8;">               
                <img src="../assets/images/定位.png"
                 alt="">
                <span style="margin-left:0.5vw;font-size:0.9vw;">{{ item.applicableArea }}</span>
              </span>
              <h3 style="display: flex; margin-left: 1vw;font-size:1.2vw;overflow: hidden;">{{ stringLenFun(item.name )}}</h3>
            </div>

            <div style="display: flex; color: #ccc;margin-top: 0.5vw;">
              <span style="margin-right: 2vw; font-size:1vw">牵头部门：{{ item.leaderDepartment }}</span>
              <span style="font-size:1vw">发布时间：{{ formatDate(item.publishDate) }}</span>
            </div>

          </div>
          <div class="right" style="display: flex;justify-content: space-between;">
            <div style="text-align: center;">
              <span style="font-size:1vw">政策剩余时间</span>
              <h3><i style="font-style: normal;font-size: 1.8vw;color: red;">{{ daysUntil(item.expireDate) }}</i></h3>
            </div>
          </div>
        </div>
        </div>
    
      <!-- <el-table :data="tableData" border  class="policyAppl" style="width: 100%;font-size:1.2vw" >
        <el-table-column  prop="name" label="政策名称" align="center"  />
        <el-table-column  prop="source" label="发布部门" align="center" style="padding: 10px 0; font-size:1.2vw"/>
        <el-table-column style="font-size:1.2vw" label="发布时间"  align="center">
          <template  #default="scope">
              {{scope.row.publishDate? scope.row.publishDate.split('T')[0]:""}}
            </template>
       </el-table-column>
        <el-table-column style="font-size:1.2vw" label="剩余时间" align="center">
          <template #default="scope">
              {{timeStatusFun(scope.row.deadTime)}} 
          </template>
        </el-table-column>
     
        <el-table-column style="font-size:1vw" label="操作" align="center">
            <template #default="scope">
                <el-button type="danger" style="width:4vw;height:3vx;font-size:1vw" @click="toPolicyDetailFun(scope.row.id)">查看</el-button> 
            </template>
          
        </el-table-column>
      </el-table> -->
    </div>
  </div>
	
</template>

<style scoped>
#main{
  display: flex;
  width: 100%;
  height: 50vw;
  flex-direction: column;
  align-items: center;
  background-color: white;
  margin-left: 1vw;
}
#main h1{
  width: 100%;
  padding: 2vw 0 1vw 2vw;
  font-size: 1.4vw;
  font-weight: bold;
}
#content {
  width: 100%;
  margin: 0 auto;
}

.polcyBox {
  font-size: 0.5vw;
}
.policyItem {
  margin: 0 auto;
  width: 95%;
    height: 5vw;
    background-color: #fff;
    display: flex;
    margin-top: 1vw;
    justify-content: space-between;
    align-items: center;
    padding: 4vw 2vw;
    border-radius: 10px;
    border: 0.1vw solid #ccc;
  }
.iconfont{
  display: flex;
  align-items: center;
  font-size: 1vw;
 
}

.iconfont img{
  width: 1vw;
 height: 1.2vw;
}
</style>