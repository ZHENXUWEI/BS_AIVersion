<script setup>
    import Header from "./Header.vue"
    import {ref,onMounted} from "vue"
    import { useRouter } from 'vue-router';
    import {policyMatchNAPI,policyMatchListPAPI } from "@/api/policy"
    import { getCookie } from '@/utils/cookie'
    import { ElMessage } from 'element-plus'
    const router = useRouter();
    const pmList=ref([])
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

  const toPolicyMaterialsFun=async()=>{
    const info = getCookie()
    if(!info){
        ElMessage.error('请先登录.')
        return
    }
   await policyMatchNAPI()
   router.push('/applyMaterials')
  }

  onMounted(async ()=>{
    const res=await policyMatchListPAPI()
    if(res.data==undefined)
    return
    pmList.value=res.data.slice(0, 6)
    console.log(pmList)

    })
</script>

<template>
  <div id="main">
    <Header></Header>
    <div id="content">
        <el-card shadow="always" style="max-width: 100%" class="el-card">
            <div v-for="(item,index) in pmList" :key="index" :class="{text_item:item!=null}">
              <img v-if="item!=null" src="../assets/images/111.png" width="370px" height="190px" alt="">
              <div v-if="item!=null"  class="content-desc">
                  <div id="u1236" class="ax_default _一级标题">
                      <div id="u1236_text" class="text ">
                        <p><span>{{item.modelName?item.modelName:''}}</span></p>
                        <p><span>{{item.money?item.money:""}}</span></p>
                        <p><span>申报时间：{{item.periodTime?item.periodTime:""}}</span></p>
                        <p><span>来源：{{item.source?item.source:""}}</span></p>
                      </div>
                    </div>
                    <!-- Unnamed (Rectangle) -->
                    <div id="u1237" class="primary_button">
                      <div 
                      id="u1237_text" 
                      class="text"
                      @click="toPolicyModelFun"
                      >
                        查看评价
                      </div>
                      <div   @click="toPolicyMaterialsFun" id="u1238_text" class="text">
                        委托申报
                      </div>
                    </div>
              </div>
                
            </div>
          
          </el-card> 
    </div>

    <div id="footer">
      
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

#content{
    width: 80%;
    display: felx;
    align-self: center;
    margin-top: 1vw;
}

.el-card{
    display: flex;
    flex-direction: column;
}

.text_item{
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    border-bottom: 0.1px solid grey;
    padding:1vw
}

#u1236_text{
  width: 20vw;
  height: 9vw;
  display: flex;
  flex-direction: column;
}

#u1236_text p{
  margin-top: 0.5vw;
}

.content-desc{
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.primary_button{
  width: 15vw;
  display: flex;
  flex-direction: row;
  
  justify-content: space-around;
}
.primary_button .text{
  padding: 0.2vw 0.5vw;
  border-radius: 0.5vw;
  background-color: rgb(52, 177, 239);
  cursor: pointer;
  color:white;
  font-size: 1vw;
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
  font-size: 1vw;
  cursor: pointer;
}

</style>
