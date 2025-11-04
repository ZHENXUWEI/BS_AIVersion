<script setup>
import {ref,onMounted,watch} from "vue"
import { useRoute } from 'vue-router';
const route = useRoute();
import {enterpriseReportAPI,postEnterpriseReportAPI } from '@/api/service';
import { getCookie} from '@/utils/cookie'
import { ElMessage } from 'element-plus'
const props = defineProps({
      year: String,
    });

const urlParams = route.query;
const users = ref({});
// 年报数据对象
const formData=ref({})
formData.value={
    companyId:users.value.id,
    year:"2023",
    yinyeshouruTotal:'1',
    yanfaTotal:'1',
    zichanTotal:'1',
    xinzenguqTotal:'1',
    xiaoshouTotal:'1',
    zhuyingTotal:'1',
    fuzhaiTotal:'1',
    jinglirunTotal:'0.1'
}

const getHuiXiangDataFun=async (obj)=>{
     const res=await enterpriseReportAPI(obj)
     formData.value=res.data
     console.log(11111)
     console.log(formData.value)
}

//提交数据
const submitFun=async (year)=>{
    console.log(year)
    formData.value.year=props.year
    formData.value.companyId=users.value.id
   const res=await postEnterpriseReportAPI(formData.value)
   console.log(res)
   if(res.status==0){
          ElMessage.success("提交成功")
       }else{
        ElMessage.error("提交失败")
       }
}

onMounted(async ()=>{
    const info = getCookie()
    if(info){
        users.value=info
    }
   
})

watch(
  () => props.year, // 监听 id 的变化
    () => {
    getHuiXiangDataFun({companyId:users.value.id,year:props.year});
  }
);


</script>

<template>
    <div id="nianbao">
        {{ year }}
        <div id="left">
            <div>
                <h3>营业收入总额:</h3>
                <input type="text" v-model="formData.yinyeshouruTotal">
            </div>
            <div>
                <h3>研究开发费用总额:</h3>
                <input type="text" v-model="formData.yanfaTotal">
            </div>
            <div>
                <h3>资产总额:</h3>
                <input type="text" v-model="formData.zichanTotal">
            </div>
            <div>
                <h3 style="width: 24vh;">新增股权总额（合格机构投资者的实缴额）:</h3>
                <input type="text" v-model="formData.xinzenguqTotal">
            </div>
            
        </div>
        <div id="right">
            <div>
                <h3>销售收入:</h3>
                <input type="text" v-model="formData.xiaoshouTotal">
            </div>
            <div>
                <h3>主营业务收入：</h3>
                <input type="text" v-model="formData.zhuyingTotal">
            </div>
            <div>
                <h3>负债总额：</h3>
                <input type="text" v-model="formData.fuzhaiTotal">
            </div>
            <div>
                <h3>净利润率：</h3>
                <input type="text" v-model="formData.jinglirunTotal">
            </div>
        </div>
    </div>
    <span class="edit_button" @click="submitFun(year)">提交</span>
</template>

<style scoped>
#nianbao{
    display: flex;
    width: 100%;
    flex-direction: row;
    justify-content: space-around; 
}
#left,#right{
    display: flex;
    width: 40%;
   
    flex-direction: column;
    padding: 5vh;
}

#left>div,#right>div{
    display: flex;
    width: 100%;
    height: 4vh;
    justify-content: space-between;
    margin-top: 30px;
}


#left>div input,#left>div select{
    width: 60%;
}

#right>div input,#right>div select{
    width: 60%;
}

.edit_button{
    width: 10vh;
    height: 5vh;
    display: block;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 1vh 2vh;
    background-color: rgb(8, 154, 227);
    cursor: pointer;
    border-radius: 1vh;
    color: white;
}
</style>