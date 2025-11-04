<script setup>
import {ref,onMounted} from "vue"
import { useRouter,useRoute } from 'vue-router';
import {enterpriseReportAPI,postEnterpriseReportAPI } from '@/api/service';
import { getCookie} from '@/utils/cookie'
import { ElMessage } from 'element-plus'
// const router = useRouter();
// const route = useRoute();
const currentYear = ref(null);
currentYear.value = new Date().getFullYear();

const users = ref({});
const formData=ref({})
formData.value={
    companyId:1,
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

const rules = {
    yinyeshouruTotal: [
    { required: true, message: '此项为必填项', trigger: 'blur' }
  ],
  yanfaTotal: [
    { required: true, message: '此项为必填项', trigger: 'blur' },
  ],
  zichanTotal: [
    { required: true, message: '此项为必填项', trigger: 'blur' },
  ],
  xinzenguqTotal: [
    { required: true, message: '此项为必填项', trigger: 'blur' },
  ],
  zhuyingTotal: [
    { required: true, message: '此项为必填项', trigger: 'blur' },
  ],
  fuzhaiTotal: [
    { required: true, message: '此项为必填项', trigger: 'blur' },
  ],
  jinglirunTotal: [
    { required: true, message: '此项为必填项', trigger: 'blur' },
  ]
};
 
const formRef = ref(null);


const YearFun=async (Yearparam)=>{
    // console.log(Yearparam)
    formData.value={
   
    yinyeshouruTotal:"",
    yanfaTotal:"",
    zichanTotal:"",
    xinzenguqTotal:"",
    xiaoshouTotal:"",
    zhuyingTotal:"",
    fuzhaiTotal:"",
    jinglirunTotal:""
}
    const info = getCookie()
    if(info){
        users.value=info
    }
    Year.value=Yearparam
    const res=await enterpriseReportAPI({companyId: users.value.id ,year:Yearparam})
    if(res.data==null){
        return
    }
    // console.log(res.data)
    formData.value=res.data
    // console.log(formData.value)

}



const submitFun=()=>{
    const info = getCookie()
    if(info){
        users.value=info
    }
    formData.value.companyId=users.value.id
    formData.value.year=Year.value
    formRef.value.validate(async (valid) => {
    if (valid) {
        const res=await postEnterpriseReportAPI(formData.value)
        console.log(res)
        if(res.status==0){
          ElMessage({
          message: '提交成功',
          type: 'success',
          offset:window.screen.height/1.2
        });
            }else{
              ElMessage({
              message: '提交失败',
              type: 'error',
              offset:window.screen.height/1.2
            });
            }
    } else {
      ElMessage.error('表单验证失败');
      return false;
    }
  });

}


const yearList=ref([])
yearList.value=[currentYear.value+5,
currentYear.value+4,
currentYear.value+3,
currentYear.value+2,
currentYear.value+1,
currentYear.value,
currentYear.value-1,
currentYear.value-2,
currentYear.value-3,
currentYear.value-4
]

const activeIndex = ref(0)
const Year = ref(currentYear);

const changeYear=(year)=>{
  Year.value=year
//   console.log(Year.value)
  YearFun(Year.value)
}

onMounted(async() => {
    YearFun(Year.value);
    activeIndex.value=5
})
</script>

<template>
    <div id="main">
        <div class="content_head">
            <span v-for="(item,index) in yearList" 
            :key="index"
            :class="{active:activeIndex === index}" 
            @click="changeYear(item), activeIndex=index"
            >
              {{ item }}年
            </span>
        </div>
       <div id="content">  
       
            <h1 style="font-size: 1.5vw;">{{Year}}年度报表</h1>
            <el-form :model="formData" :rules="rules" ref="formRef" id="nianbao">
                    <el-form-item   prop="yinyeshouruTotal">
                        <span class="left">营业收入总额</span><input  type="text" v-model="formData.yinyeshouruTotal"><span>万元</span>
                    </el-form-item>
                
                    <el-form-item  prop="yanfaTotal">
                        <span class="left">研究开发费用总额</span><input  type="text" v-model="formData.yanfaTotal"><span>万元</span>
                    </el-form-item>
                    
                    <el-form-item  prop="zichanTotal">
                        <span class="left">资产总额</span><input  type="text" v-model="formData.zichanTotal"><span>万元</span>
                    </el-form-item>
                    
                    <el-form-item prop="xinzenguqTotal">
                        <span class="left">新增股权总额（合格机构投资者的实缴额）</span><input  type="text" v-model="formData.xinzenguqTotal"><span>万元</span>
                    </el-form-item>
                    
                    <el-form-item  prop="xiaoshouTotal">
                        <span class="left">销售收入</span> <input  type="text" v-model="formData.xiaoshouTotal"><span>万元</span>
                    </el-form-item>
                    
                    <el-form-item  prop="zhuyingTotal">
                        <span class="left">主营业务收入</span> <input  type="text" v-model="formData.zhuyingTotal"><span>万元</span>
                    </el-form-item>
                    
                    <el-form-item  prop="fuzhaiTotal">
                        <span class="left">负债总额</span>  <input type="text" v-model="formData.fuzhaiTotal"><span>万元</span>
                    </el-form-item>
                
                    <el-form-item  prop="jinglirunTotal">
                        <span class="left">净利润率</span><input  type="text" v-model="formData.jinglirunTotal"><span></span>
                    </el-form-item>
                <el-button type="primary" 
                style="font-size: 1vw;"
                class="edit_button" @click="submitFun">提交</el-button> 
        </el-form>
        
                      
       </div>
    </div>
  
</template>

<style scoped>
    #main{
        display: flex;
        width: 100%;
        
        flex-direction: column;
       
        align-items: center;
        
    }


    .content_head{
        background-color: white;
        width: 100%;
        height: 3vw;
        margin-bottom: 1vw;
        display: flex;
       padding-left: 2vw;
        align-items: center;
        margin-left: 1vw;
      }
  
      .content_head span{
         
          padding: 0.4vw 1.4vw;
          font-size: 1vw;
          cursor: pointer;
          border-radius: 1vw;
      }
      .active{
        background:linear-gradient(45deg, #3580F5,#5FA2FD) ;
        color: white;
      }
    #content{
        display: flex;
        width: 100%;
        flex-direction: column;
        
        align-items: center;
        justify-content: center;
        align-self: center;
        background-color: white;
        margin-left: 1vw;
      }
      #content h1{
        width: 100%;
        height: 3vw;
        display: flex;
        align-items: center;
        font-size: 1.2vw;
        font-weight: 500;
        padding-left: 2vw;
        border-bottom: 0.05vw solid #ccc;
      }

    #nianbao{
        width: 100%;;
        display: flex;
        padding: 0 2vw;
        flex-direction: column;
        align-items: center;
        padding-bottom: 5vw;
       padding-right: 15vw;
        margin-top: 3vw;
    }

    .el-form-item{
        display: flex;
        justify-content: space-between;
        align-items: center;
        height: 2.5vw;
    }

    .el-form-item .left{
        width: 20vw;
        text-align: right;
        margin-right: 2vw;
        font-size: 1vw;
        font-size: var(--el-form-label-font-size);
        line-height: 20px;
        color: var(--el-text-color-regular);
    }

    .el-form-item span:last-child{
      width: 5vw;
      padding-left: 0.5vw;
      font-size: 1vw;
    }

    .el-form-item input{
      width: 8vw;
      height: 2vw;
      outline: none;
      border: 0.1vw solid #ccc;
      border-radius: 0.2vw;
    }
    .edit_button{
        width: 8vw;
        height: 2vw;
        bottom: 2vw;
        align-self: center;
        margin-left: 15vw;
        font-size: 1vw;
        background:linear-gradient(45deg, #3580F5,#5FA2FD) ;
    }

    input {
      
      font-size: 1vw;
      
    }
</style>
