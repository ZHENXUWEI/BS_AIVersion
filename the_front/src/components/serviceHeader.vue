<script setup>
import {ref,onMounted} from "vue"
import { getCookie} from '@/utils/cookie'
import {enterpriseDataAPI } from '@/api/service';
const users = ref({});
const formData=ref({})
formData.value={
  id:1,
  companyName:'浙江工贸职业技术学院',
 
  legalPerson:'张利仁',
  phone:'15305779355',
}

// const getHuiXiangFun=async (obj)=>{
//        const res= await enterpriseDataAPI(obj)
//        formData.value=res.data
//        console.log(formData.value)
// }

onMounted(async ()=>{
        const info = getCookie()
        if(info){
            users.value=info
        }

       const res=await enterpriseDataAPI({id:users.value.id})
       formData.value=res.data
       console.log(formData.value)
    })

</script>

<template>
    <div id="head" >
        <div class="head_in">
            <div class="com_box1">
                <span>{{users.companyName}}</span>
                <span>欢迎您</span>
            </div>
           
            <!-- <div class="com_box2"> -->
               
                <!-- <span>法人：{{formData.legalPerson}}</span>    -->
                <!-- <span>法人：张利仁</span> -->
                <!-- <span>手机号：{{formData.phone}}</span>  -->
                 <!-- <span>手机号：13857117571</span> -->
            <!-- </div> -->
        </div>
            <!-- <div id="u1" class="u1650_text ">
                政策服务平台
            </div> -->
            
        
        

    </div>
    
    
</template>

<style scoped>
#head{
    z-index: -999;
    width: 100%;
    height: 6vw;
    display: flex;
    flex-direction: column;
    background: linear-gradient(to right,#457cff,#a7defc);
   justify-content: center;
   
}
#head .head_in{
    width: 90%;
    margin: 0 auto;
}
#head .com_box1 {
   
    color:white; 
   
}

#head .com_box1 span{
    margin-left: 1vw;
    font-size: 1.3vw;
}
#head .com_box2{
    margin-left: 1vw;  
    display: flex;
}

#head  .com_box2 span{
    margin-top: 0.3vw;
    margin-right: 1vw;
    font-size: 0.8vw;
    color: white;
}

</style>
