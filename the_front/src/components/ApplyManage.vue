<script setup>
import {ref,onMounted} from "vue"
import { getCookie} from '@/utils/cookie'
import {policyModelAPI,shenBaoProgressAPI} from '@/api/service'
import { useRouter } from 'vue-router'
import { ElMessageBox} from 'element-plus'
 const router = useRouter()

const users = ref({});
const tableDataLeft=ref([])
const tableDataRight=ref([])
const tableData=ref([])
const N = ref(0);
const getPolicyModelData=async (obj)=>{
  const res=await policyModelAPI(obj)
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
  var arr=[]
  // console.log(tableData)
  // const res= await shenBaoProgressAPI({modelName:mName,id:id})
  for(var i=0;i<tableDataLeft.value.length;i++){
    // console.log(tableDataLeft.value[i].modelName)
    const res= await shenBaoProgressAPI({modelName: tableDataLeft.value[i].modelName,id:obj.id})
  //  console.log(res)
    arr.push(res.data)
  }

  for(var i=0;i<tableDataRight.value.length;i++){
    // console.log(tableDataLeft.value[i].modelName)
    const res= await shenBaoProgressAPI({modelName: tableDataRight.value[i].modelName,id:obj.id})
  //  console.log(res)
    arr.push(res.data)
  }
  tableData.value=arr
  // console.log(tableData.value)
}


tableData.value = [{
		shenbaoStatus: '0',
		shenbaoYijian: '未知状态',
    shenheBumen: '未知单位',
    shenheTime: '未知时间'
  }]

const tableDataStatus=ref({})
const getProgress=async (mName)=>{
  
    const info = getCookie()
    if(info){
        users.value=info
    }
    var id=users.value.id
   
   const res= await shenBaoProgressAPI({modelName:mName,id:id})
   if(res.data==null) return
   tableDataStatus.value=res.data
  //  console.log(222222,res.data.shenbaoStatus)
}

const isDaBiao =(obj) => {
      if(obj==undefined)
          return ""
      else{
          if(obj.length==0){
            return "达标"
          }else{
            return "未达标"
          }
      } 
    };

    const isSatus =(obj) => {
      console.log(obj)
      if(obj==null || Object.keys(obj).length == 0)
          return "未知状态"
      else{
          if(obj.shenbaoStatus=="1"){
            return "已通过"
          }else if(obj.shenbaoStatus=="0"){
            return "未通过"
          }else if(obj.shenbaoStatus=="2"){
            return "未审核"
          }
      } 
    };

// const isShenBao =(obj) => {
//       if(obj==undefined)
//           return ""
//       else{
//           if(obj.length==0){
//             return "历史未申报"
//           }else{
//             return "历史已申报"
//           }
//       } 
//     };

onMounted(()=>{
    const info = getCookie()
    if(info){
        users.value=info
    }
    
    getPolicyModelData({id:users.value.id})
    // getProgress("2024年高新技术企业")
})



// const checkProcess = (mName) => {
//   router.push({path:'/applyProcess',query:{id:users.value.id,modelName:mName}})
// }

// const toManage=()=>{
//   router.push('/applyMaterials')
// }
const centerDialogVisible=ref(false)
const modelName=ref("")
const showMore=(item)=>{
  tableDataStatus.value={}
  centerDialogVisible.value=true
  modelName.value=item.modelName
  getProgress(item.modelName)

}

const BuQuanFun=(status)=>{
  if(status=="已通过"){
    ElMessageBox.alert('申报已通过不要补全材料!')
   
  }

  router.push({path:`/applyMaterials`})
}
</script>
<template>
	<div id="main">
    <h1>政策申报管理</h1>
    <div id="content">
      <div class="policy_left">
        <div class="el_card" 
        v-for="(item,index) in tableDataLeft"
        :key="index"
        >
          <div class="el_card_top">
              <div class="first">
                <img src="../assets/images/申报.png" alt="" style="margin-right: 0.5vw;">
                <span>{{item.modelName}}</span>
              </div>
              <div class="second">
                <span @click="BuQuanFun(isSatus(tableData[index]))">补全材料</span>
                
              </div>
          </div>
          <div class="el_card_bottom">
              <span  :class="{active:isSatus(tableData[index]) =='未通过'}">{{isSatus(tableData[index])}}</span>
              <span style="cursor: pointer;" @click="showMore(item)">查看更多></span>
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
              <img src="../assets/images/申报.png" alt="" style="margin-right: 0.5vw;">
              <span >{{item.modelName}}</span>
            </div>
            
            <div class="second">
              <span @click="BuQuanFun(isSatus(tableData[N-1+index]))">补全材料</span>
              
            </div>
            
          </div>
          <div class="el_card_bottom">
            <span :class="{active:isSatus(tableData[N-1+index])=='未通过'}">{{isSatus(tableData[N-1+index])}}</span>
              <span style="cursor: pointer;" @click="showMore(item)">查看更多></span>
          </div>
        </div> 
      </div>
    </div>
  </div>
  <el-dialog 
  style="width: 45%; " 
  title="政策申报进度" 
  v-model="centerDialogVisible"
  :close-on-click-modal="false"
  >
      <div class="main_center">
        
        <div class="modelName_div">
          <div class="title">
            <img src="../assets/images/申报.png" alt="" style="margin-right: 0.5vw;">
            <span >{{modelName}}</span>
          </div>
          <span class="shenbao" @click="BuQuanFun(isSatus(tableDataStatus))">补全材料</span>
          
      </div>
      <div class="shenheStatus">
        <p><span>申报状态：</span><span>{{isSatus(tableDataStatus)}}</span></p>
        <p><span>申报意见：</span><span>{{tableDataStatus.shenbaoYijian}}</span></p>
        <p><span>审核部门：</span><span>{{tableDataStatus.shenheBumen}}</span></p>
        <p><span>审核时间：</span><span>{{tableDataStatus.shenheTime}}</span></p>
      </div>

</div>

            
  </el-dialog>
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
.active{
  color:red
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


.shenheStatus{
  width: 100%;
  margin-top: 2vw;
}

.shenheStatus p{
  margin-top: 0.5vw;
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