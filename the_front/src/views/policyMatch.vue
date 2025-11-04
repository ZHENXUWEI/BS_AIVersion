<script setup>
import HomeHeader from "@/components/HomeHeader.vue"
import {ref,onMounted} from "vue"
import { getCookie} from '@/utils/cookie'
 import {policyMatchNAPI,policyMatchListPAPI } from "@/api/policy"
 import { useRouter,useRoute  } from 'vue-router';
 const router = useRouter();
const route = useRoute();
// import { lastDayOfDecade } from "date-fns"
const tableDataLeft=ref([])
const tableDataRight=ref([])
const N = ref(0);
const policy=async ()=>{
  const res=await policyMatchListPAPI()
  if(res.data==undefined)
  return
  var len=res.data.length
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

const getUrl=(index)=>{
  return `/img/模型${index}.png`
  // return new URL(`./assets/images/模型${index}.png`, import.meta.url);
}
onMounted(async ()=>{
    policy()
  })

  const toPolicyModelFun=async()=>{
    const info = getCookie()
    if(!info){
        
        return
    }
   await policyMatchNAPI()
   router.push("/policyModel")
}
const toPolicyShenBaoFun=async()=>{
    const info = getCookie()
    if(!info){
        
        return
    }
   await policyMatchNAPI()
   router.push("/applyMaterials")
}

</script>

<template>
  <div id="main">
    <HomeHeader></HomeHeader>
    <div class="banner1">
        <img width="100%" src="../assets/images/banner2.png" alt="">
        <div class="info">
            <h1>政策匹配</h1>
            <p>海量项目申报查询,一对一政策精准自评</p>
        </div>
    </div>

    <div class="policy_model">
        <div class="policy_left">
          <div class="el_card" v-for="(item,index) in tableDataLeft">
            <div class="el_card_left">
              <div class="first">
                <h1>{{item.modelName}}</h1>
                <p>
                  <!-- <span>浙江省</span><span class="two">{{item.money}}万元</span> -->
                </p>
              </div>
              <div class="second">
                <p style="margin-bottom: 1vw;">申报时间:{{item.periodTime}}</p>
                <p>来源：{{item.source}}</p>
              </div>

              <div class="third">
                  <span @click="toPolicyModelFun" style="cursor: pointer;">立即自评</span>
                  <span @click="toPolicyShenBaoFun" style="cursor: pointer;">委托申报</span>
              </div>
            </div>
            <div class="el_card_right">
              <img :src="getUrl(index)"  alt="">
            </div>
          </div>

        </div>

        <div class="policy_right">
          <div class="el_card" v-for="(item,index) in tableDataRight">
            <div class="el_card_left">
              <div class="first">
                <h1>{{item.modelName}}</h1>
                <p>
                  <!-- <span>浙江省</span><span class="two">{{item.money}}万元</span> -->
                </p>
                
              </div>
              <div class="second">
                <p style="margin-bottom: 1vw;">申报时间:{{item.periodTime}}</p>
                <p>来源：{{item.source}}</p>
              </div>

              <div class="third">
                  <span @click="toPolicyModelFun" style="cursor: pointer;">立即自评</span>
                  <span @click="toPolicyShenBaoFun" style="cursor: pointer;">模拟申报</span>
              </div>
            </div>
            <div class="el_card_right">
              <img :src="getUrl(N+index)" alt="">
            </div>
          </div>
 
          
        </div>
    </div>



</div>
    
</template>

<style scoped>
.banner1{
    width: 100%;
    position: relative;
    z-index: -999;
  }
  .banner1 .info{
    position: absolute;
    display: flex;
    flex-direction: column;
    width: 38vw;
    height: 3vw;
    left: 12.5%;
    top:40%;
    transform: translate(0,-70%);
    
  }

  .banner1 .info h1{
    font-size: 1.8vw;
    font-weight: bold;
  }
  .banner1  .info p{
    font-size: 1vw;
    font-weight:lighter;
    color:grey;
  }
  .policy_model{
    display: flex;
    width: 75%;
    justify-self: center;
    justify-content: space-between;
    margin: 0 auto;
    margin-top: 3vw;
   
  }

  .policy_left{
    width: 49.5%;
  }

  .policy_right{
    width: 49.5%;
  }

.el_card{
  border: 0.2vw solid white;
  border-radius: 0.8vw;
  width: 100%;
  height: 12.5vw;
  background-image: url("../assets/images/框1.png");
  background-position: center; /* 背景图片居中 */
  background-position: center;
  background-clip: border-box; 
  display: flex;
  flex-direction:row;
  justify-content: space-around;
  margin-bottom: 1vw;
}

.el_card .el_card_left{
  width: 80%;
  height: 100%;
  display: flex;
  flex-direction: column;
  padding:1vw 0 1vw 2vw;
  
}

.el_card .el_card_left .first{
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: space-between;
 
}
.el_card_left .first h1{
  font-size: 1.2vw !important;
  width: 15vw;
}

.el_card_left .first p{
  height: 100%;
  display: flex;
  justify-content: end;
 
  align-items: center;
}
.el_card_left .first span{
  padding: 0.2vw 0.6vw;
  border: 0.1vw solid #3580F5;
  border-radius: 0.2vw 0 0 0.2vw;
  background-color: #3580F5;
  font-size: 0.8vw;
  color: white;
}

.el_card_left .first .two{
  background-color: white;
  color: #3580F5;
  width: 5vw;
  border-radius: 0 0.2vw 0.2vw 0;
}

.el_card .el_card_left div{
  margin-bottom: 1vw;
}

.el_card .el_card_left .second{
  font-size: 0.8vw;
  font-weight: lighter;
}

.el_card .el_card_left .third{
  display: flex;
}

.el_card_left .third span{
  padding: 0.4vw 1.8vw;
  background:linear-gradient(45deg, #3580F5,#5FA2FD) ;
  color:white;
  border-radius: 1vw;
  font-size: 0.9vw;
  margin-right: 1vw;
}

.el_card_left .first h1{
  font-size: 1.6vw;
  font-weight: bold;
  margin-right: 0.3vw;
}

.el_card .el_card_right{
  width: 30%;
  height: 100%;
 
}

.el_card .el_card_right img{
  width: 100%;
  height: 100%;
}



</style>
