<script setup>
 import { useRouter } from 'vue-router'
 import {ref,onMounted} from "vue"
 const router = useRouter()
import { getCookie} from '@/utils/cookie'
import {caiLiaoNumberAPI,caiLiaoOtoOAPI,policyModelAPI} from '@/api/service'

const tableDataLeft=ref([])
const tableDataRight=ref([])
const tableData=ref([])
const users = ref({});
const N = ref(0);
const caiLiaoNumber=async (obj)=>{
  const res=await caiLiaoNumberAPI(obj)
  // tableData.value=res.data
  var len=res.data.length
  if(len%2==0){
    N.value=len/2
    tableDataLeft.value=res.data.slice(0,N.value)
    tableDataRight.value=res.data.slice(N.value)
    
  }else{
    N.value=Math.floor(len/2)+1
    tableDataLeft.value=res.data.slice(0,N.value)
    tableDataRight.value=res.data.slice(N.value)
  }
  // console.log(res.data.length)
}


const tableDataLeft1=ref([])
const tableDataRight1=ref([])
const N1 = ref(0);
const getPolicyModelData=async (obj)=>{
  const res=await policyModelAPI(obj)
  // tableData.value=res.data
  var len=res.data.length
  if(len%2==0){
    N1.value=len/2
    tableDataLeft1.value=res.data.slice(0,N1.value)
    tableDataRight1.value=res.data.slice(N1.value)
    
  }else{
    N1.value=Math.floor(len/2)+1
    tableDataLeft1.value=res.data.slice(0,N1.value)
    tableDataRight1.value=res.data.slice(N1.value)
  }
  // console.log(res.data.length)
}

onMounted(async ()=>{
    const info = getCookie()
    if(info){
        users.value=info
    }
    
    caiLiaoNumber({id:users.value.id})
    getPolicyModelData({id:users.value.id})
})

const check = (mName) => {
  router.push({path:'/materialsInfo',query:{modelName:mName}})
}
const materialSub = (mName) => {
  router.push({path:'/materialsDetail',query:{modelName:mName}})
}

const toShenBao=(value,mName)=>{
  router.push('/applyManage')
}


const centerDialogVisible = ref(false)
// 获取材料信息一对一
const getCaiLiaoinfo=async(modelname)=>{
  const info = getCookie()
  if(info){
      users.value=info
  }
  const res=await caiLiaoOtoOAPI({id:users.value.id,modelName:modelname,year:new Date().getFullYear()})
  tableData.value=res.data
  console.log(tableData.value)
}
const index=ref(0);
const n=ref(1)
const showMore=(item,i,j)=>{
  index.value=i
  n.value=j
  if(item.docNumber<=0){
    isVisible.vlaue=true
    return
  }
  centerDialogVisible.value=true
  modelName.value=item.modelName
  getCaiLiaoinfo(item.modelName)

}
const isVisible=ref(false)
const modelName=ref("")


const isDaBiao =(obj) => {
      if(obj==undefined)
          return ""
      else{
          if(obj.length==0){
            return "已达标"
          }else{
            return "未达标"
          }
      } 
  };
const isVisible1=ref(false)

const visibleFun=async (modelName1,docNumber)=>{
  // console.log(tableDataLeft1.value[index])
  if(n.value==1){
     if(isDaBiao(tableDataLeft1.value[index.value].indicatorName)=="未达标"){
      isVisible1.value=true
      return
     }
      
    }
    if(n.value==2){
      if(isDaBiao(tableDataRight1.value[index.value].indicatorName)=="未达标"){
        isVisible1.value=true
        return
      }
    }
  
  if(docNumber<=0){
    isVisible.vlaue=true
    // ElMessage({
    //       message: '该模型不需要上传材料',
    //       type: 'error',
    //       offset:window.screen.height/1.2
    //     });
    return
  }
  modelName.value=modelName1
 
  // centerDialogVisible.value=true
  router.push({path:'/materialsDetail',query:{modelName:modelName1}})

}


const visibleFun1=async (modelName1,docNumber,index,n)=>{
  // console.log(tableDataLeft1.value[index])
  if(n==1){
     if(isDaBiao(tableDataLeft1.value[index].indicatorName)=="未达标"){
      isVisible1.value=true
      return
     }
      
    }
    if(n==2){
      if(isDaBiao(tableDataRight1.value[index].indicatorName)=="未达标"){
        isVisible1.value=true
        return
      }
    }
  
  if(docNumber<=0){
    isVisible.vlaue=true
    // ElMessage({
    //       message: '该模型不需要上传材料',
    //       type: 'error',
    //       offset:window.screen.height/1.2
    //     });
    return
  }
  modelName.value=modelName1
 
  // centerDialogVisible.value=true
  router.push({path:'/materialsDetail',query:{modelName:modelName1}})

}


const goShenBaoFun=()=>{
  router.push({path:`/applyManage`})
}

</script>
<template>
  <div id="main">
    <h1>政策申报材料</h1>
    <div id="content">
      <div class="policy_left">
        <div class="el_card" 
        v-for="(item,index) in tableDataLeft"
        :key="index"
        >
          <div class="el_card_top">
              <div class="first">
                <img src="../assets/images/材料.png" alt="" style="margin-right: 0.5vw;">
                <span>{{item.modelName}}</span>
              </div>
              <div class="second">
                <span @click="visibleFun1(item.modelName,item.docNumber,index,1)">提交材料</span>
                <span style="margin-left: 0.3vw;background:linear-gradient(45deg, #1FB456,#35C97F) ;"
                @click="goShenBaoFun">模拟申报</span>
              </div>
          </div>
          <div class="el_card_bottom">
              <span style="color: red;">材料数量：{{item.docNumber}}</span><span style="cursor: pointer;" @click="showMore(item,index,1)">查看更多></span>
          </div>
        </div> 
      </div>


      <div class="policy_right">
        <div class="el_card"
        v-for="(item,index) in tableDataRight"
        :key="index"
        >
          <div class="el_card_top">
            <div class="first">
              <img src="../assets/images/材料.png" alt="" style="margin-right: 0.5vw;">
              <span >{{item.modelName}}</span>
            </div>
            
            <div class="second">
              <span @click="visibleFun1(item.modelName,item.docNumber,index,2)">提交材料</span>
              <span style="margin-left: 0.3vw;background:linear-gradient(45deg, #1FB456,#35C97F) ;"
              @click="goShenBaoFun">模拟申报</span>
            </div>
            
          </div>
          <div class="el_card_bottom">
            <span style="color: red;">材料数量：{{item.docNumber}}</span><span style="cursor: pointer;" @click="showMore(item,index,2)">查看更多></span>
          </div>
        </div> 
      </div>
    </div>
  </div>
  <el-dialog 
  style="width: 45%; " 
  title="材料清单" 
  v-model="centerDialogVisible"
  :close-on-click-modal="false"
  >
      <div class="main_center">
        
        <div class="modelName_div">
          <div class="title">
            <img src="../assets/images/材料.png" alt="" style="margin-right: 0.5vw;">
            <span >{{modelName}}</span>
          </div>
          <span class="shenbao" @click="visibleFun(modelName,1)">提交材料</span>
          
      </div>
      <div v-for="(item,index) in tableData" :key="index" class="upload">
        <h1>{{index+1+"."+item.materialName}}</h1>
       </div>

</div>        
  </el-dialog>

  <el-dialog 
  style="width: 45%; " 
  v-model="isVisible"
  :close-on-click-modal="false"
  >
      <div class="main_center">
        
        <div class="modelName_div" style="border: none;">
          <div class="title">
            该模型不需要上传材料
          </div>
          <span class="shenbao" @click="isVisble=false">确定</span>
      </div>
  </div>        
</el-dialog>

<el-dialog style="width: 45%; " v-model="isVisible1"
:close-on-click-modal="false"
>
    <div class="main_center">
      
      <div class="modelName_div" style="border: none;">
        <div class="title">
          贵企业未达标，请补全材料!
        </div>
        <span class="shenbao" @click="isVisible1=false">确定</span>
    </div>
</div>        
</el-dialog>


</template>

<style scoped>
.centered-message {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) scale(1.0);
  z-index: 2000;
}
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
  padding: 2vw 0 2vw 2vw;
  font-size: 1.4vw;
  font-weight: bold;
}

#content{
  display: flex;
  width: 100%;
  justify-self: center;
  justify-content: space-between;
  margin: 0 auto;
  
 padding: 0 2vw;
}

.policy_left{
  width: 49%;
}

.policy_right{
  width: 49%;
}

.el_card{
  border: 0.2vw solid white;
  border-radius: 0.8vw;
  width: 100%;
  height: 8.2vw;
 border: 0.1vw solid #ccc;
  margin-bottom: 0.5vw;
  background-color: #FAFBFF;
}
.el_card_top{
  width: 100%;
  height: 6vw;
  display: flex;
  background-color: #FAFBFF;
  align-items: center;
  justify-content: space-between;
  padding: 1vw;
  border-radius: 0.8vw;
  
}
.el_card_top img{
  width: 2.8vw;
  height: 3vw;
}
.el_card_top .first{
  display: flex;
  align-items: center; 
}

.el_card_top .first span:last-child{
  font-size: 1.1vw;
  font-weight: bold;
}

.el_card_top .second{
  display: flex;
  align-items: center; 
}
.el_card_top .second span{
  cursor: pointer;
  display: block;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  width: 5vw;
  height: 2vw;
  font-size: 0.6vw;
  border-radius: 1vw;
  background:linear-gradient(45deg, #3880F8,#5CA3FF) ;
  
}

.el_card_bottom{
  display: flex;
  width: 100%;
  justify-content: space-between;
  height: 2vw;
  align-items: center;
  font-size: 0.8vw;
  padding: 0 1vw;
  background-color: #F4F5FA;
  border-radius: 0 0 0.8vw 0.8vw;
  
}


.main_center{
  width: 100%;
  padding: 1vw;
  padding-top: 0;
  margin: 0 auto;display: flex;
  flex-direction: column;
  
  background-color: white;
  align-items: center;
}

.modelName_div{
  width: 100%;
  padding: 0 3vw;
  display: flex;
  margin: 0 auto;
  align-items: center;
  justify-content: space-between;
  border: 0.1vw solid #ccc;
  border-radius: 0.5vw;
  padding: 0.6vw;
  background-color: #FDFDFF;
}

.modelName_div .title{
  display: flex;
  align-items: center;
}

.modelName_div .title span{
  font-size: 1.1vw;
  font-weight: bold;
}
.modelName_div img{
  width: 2.5vw;
  height: 2.5;
}

.modelName_div .shenbao{
  display: flex;
  display: block;
  align-items: center;

  padding: 0.5vw 0.8vw;
  background:linear-gradient(45deg, #3880F8,#5CA3FF) ;
  font-size: 0.8vw;
  border-radius: 1vw;
  color: white;
  cursor: pointer;
}

#main .upload{
  width: 95%;
  height: 5vw;
  display: flex;
  align-items: center;
  justify-content: space-around;
  justify-self: center;
  border-bottom: 0.1vw solid #ccc;
  margin: 0 auto;
}

#main .upload h1{
  width: 100%!important;
  font-size: 1.3vw;
  font-weight: bold;
 
}
</style>