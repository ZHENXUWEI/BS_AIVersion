<script setup>
import {onMounted,ref } from 'vue'
import { useRoute,useRouter } from 'vue-router';
import { caiLiaoOtoOAPI,sheHeInfoAPI,caiLiaoUpLoadAPI,deleteCaiLiaoAPI } from '@/api/service';
import { ElMessage } from 'element-plus'
import { getCookie} from '@/utils/cookie'

const users = ref({});
const route = useRoute();
const router = useRouter();
const shenHeData=ref({})
//后退
const confirmFun=()=>{

  router.go(-1)
}

const urlParams = route.query;
const tableData=ref([])

const shenheStatus=ref('3')
const shenheYijian=ref('')

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

const modelName=ref("")
onMounted(async ()=>{
      const urlParams = route.query;
    if("modelName" in urlParams){
      modelName.value=urlParams.modelName
      getCaiLiaoinfo(urlParams.modelName)
    }
})

const getCaiLiaoShenHeData=async()=>{
  const info = getCookie()
    if(info){
        users.value=info
    }
   const res= await sheHeInfoAPI({id:users.value.id ,modelName:urlParams.modelName})
   const a=res.data
   console.log(6666,a)
   if(a==undefined) return
   if(a.materialStatus=='0'){
     shenheStatus.value='3'
     shenheYijian.value=a.shenheYijian
   }

   if(a.materialStatus=='1'&&a.submissionStatus=='0'){
    shenheStatus.value='0'
    shenheYijian.value=a.shenheYijian
   }

   if(a.materialStatus=='1'&&a.submissionStatus=='1'){
    shenheStatus.value='1'
    shenheYijian.value=a.shenheYijian
   }

   if(a.materialStatus=='1'&&a.submissionStatus=='2'){
    shenheStatus.value='2'
    shenheYijian.value=a.shenheYijian
   }
    
   
  //  console.log(shenHeData.value)
}

// 审核
getCaiLiaoShenHeData()

const centerDialogVisible = ref(false)
const cailiaoName=ref('')

const openDialog=(data)=>{
  // console.log()
  if(shenHeData.value.shenheStatus=="0"){
    ElMessage.error('请先删除源文件')
    return
  }
  cailiaoName.value=data

  centerDialogVisible.value = true
  }

// 上传资料

const httprequest=async ({file})=>{


  const info = getCookie()
    if(info){
        users.value=info
    }
  const urlParams = route.query;
  const formData=new FormData()
  formData.append('file',file)
  formData.append('id', users.value.id)
  formData.append('modelName',urlParams.modelName)
  formData.append('materialName',cailiaoName.value)
  const res=await caiLiaoUpLoadAPI(formData)
  // console.log(res)
  if(res.status==0){
    // getCaiLiaoShenHeData(urlParams)
    // shenHeData.value={
    //   shenheStatus:'2',
    //   shenheYijian:"文件未审核"
    // }
    ElMessage({
          message: '材料上传成功',
          type: 'success',
          offset:window.screen.height/1.2
        });
    getCaiLiaoShenHeData()
    getCaiLiaoinfo(urlParams.modelName)
    centerDialogVisible.value = false
  }else{
    ElMessage({
          message: '材料上传成功',
          type: 'error',
          offset:window.screen.height/1.2
        });
  }


}

// 删除
const deleteFun=async (materilid)=>{
  if(shenHeData.shenheStatus==3){
    ElMessage({
          message: '文件未提交',
          type: 'error',
          offset:window.screen.height/1.2
        });
    return
  }

  const info = getCookie()
    if(info){
        users.value=info
    }

  const res= await deleteCaiLiaoAPI(materilid)
    console.log(res)
  if(res.code==200){
    ElMessage.success('删除成功');
    // shenHeData.value={
    //   shenheStatus:'3',
    //   shenheYijian:"文件未上传"
    // }
    getCaiLiaoinfo(urlParams.modelName)
    getCaiLiaoShenHeData()
  }else{
    ElMessage.error("删除失败");
  }
}
const goback=()=>{
  router.go(-1)
}


const upload = ref("upload")
const fileList = ref([]);
const handleExceed= () => {
  // upload.clearFiles()
  // const file = files[0]
  // file.uid = genFileId()
  // upload.handleStart(file)
  upload.clearFiles()
}

const onChange= (file,newFileList) => {
  // 清空原有的文件列表
  fileList.value = [];
  // 添加新选择的文件到文件列表
  // fileList.value = newFileList;

}

const goShenBaoFun=()=>{
  router.push({path:`/applyManage`})
}
</script>
<template>
  <div id="main">
        <div class="main_center">
          <div class="head_title"><h1>材料详情</h1></div>
          <div class="modelName_div">
            <div class="title">
              <img src="../assets/images/材料.png" alt="" style="margin-right: 0.5vw;">
              <span >{{modelName}}</span>
            </div>
            <span style="cursor: pointer;" class="shenbao" @click="goShenBaoFun">去申报</span>
        </div>
        <div v-for="(item,index) in tableData" :key="index" class="upload">
            <h1>{{index+1+"."+item.materialName}}</h1>
            <div class="upload-demo">

             <el-button 
             @click="openDialog(item.materialName)"
             :disabled="item.materialStatus==1||shenheStatus==1"
             style="width:6vw;height:2vw;font-size:1vw" 
             type="primary">点击上传</el-button>
            <el-button 
              style="width:6vw;height:2vw;margin-left:0.5vw;font-size:1vw;" 
              :disabled="item.materialStatus==0||shenheStatus==1"
              @click="deleteFun(item.materialId)"
              type="danger">删除</el-button>
            </div>
        </div>
        <div class="shenhe_info">
          <div >
            <h1 style="margin-bottom:1vw;">材料审核:</h1>
            <input type="radio" disabled :checked="shenheStatus=='1'"><span>审核通过</span>
            <input type="radio" disabled :checked="shenheStatus=='0'"><span>审核不通过</span>
            <input type="radio" disabled :checked="shenheStatus=='2'"><span>待审核</span>
            <input type="radio" disabled :checked="shenheStatus=='3'"><span>文件未完成上传</span>
          </div>
          <div style="margin-top:1vw;"><h1 style="font-size: 1vw;">审核意见:</h1><span>{{shenheYijian}}</span></div>
        </div>

        <el-button type="primary" 
        style="font-size: 1vw;"
        class="edit_button" @click="goback">确定</el-button> 
        </div>
      
  </div>

	<el-dialog 
  style="width: 30%; " 
  title="上传材料" 
  v-model="centerDialogVisible"
  :close-on-click-modal="false"
  >
      <div class="dialog_box">
        <div class="title">
          <img src="../assets/images/材料.png" alt="" style="margin-right: 0.5vw;">
          <span >{{modelName}}</span>
        </div>
        <div class="title">
          <span >材料名称:</span><span style="margin-left:0.5vw ;font-size:1.2vw">{{cailiaoName}}</span>
        </div>
        <div class="upload-demo1">
          <el-upload class="upload-demo2"
          ref="upload"
          action="#"
          :http-request="httprequest"
          :limit="1"
          :file-list="fileList"
          :on-change="onChange"
          
        >
        <el-button style="width:5.5vw;height:2vw;font-size:1vw" type="primary">选择文件</el-button>
        
      </el-upload>
        </div>
        
      </div>      
  </el-dialog>
        
</template>

<style scoped>
.main_center{
  width: 65%;
  padding: 3vw;
  padding-top: 0;
  margin: 0 auto;display: flex;
  flex-direction: column;
  border: 0.1vw solid #ccc;
  background-color: white;
  align-items: center;
}

.main_center .head_title{
  width: 100%;
  height: 5vw;
  display: flex;
  align-items: center;
  font-size: 1.2vw;
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
  font-size: 1.2vw;
  font-weight: bold;
}
.modelName_div img{
  width: 2.8vw;
  height: 3;
}
.modelName_div .shenbao{
  padding: 0.5vw 1.6vw;
  background:linear-gradient(45deg, #1FB456,#35C97F) ;
  font-size: 0.8vw;
  border-radius: 1vw;
  color: white;
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
  width: 55%!important;
  font-size: 1.3vw;
  font-weight: bold;
 
}
#main .upload-demo{
  display: flex;
  width: 45%!important;

  justify-content: end;
  align-items: center;
}

.shenhe_info{
  width: 100%;
  margin-top: 2vw;
}

.shenhe_info input{
  font-size: 1vw;
}
.shenhe_info span{
  font-size: 1vw;
  margin: 0 0.5vw;
}
.edit_button{
  width: 8vw;
  height: 2vw;
  margin-top: 2vw;
  align-self: center;
  
  font-size: 1vw;
  background:linear-gradient(45deg, #3580F5,#5FA2FD) ;
}

.dialog_box{
  display: flex;
  flex-direction: column;
  width: 90%;
  margin-top: 2vw;
  justify-self: center;
}
.dialog_box .title{
  display: flex;
  align-items: center;
  font-size: 1.2vw;
  font-weight: bold;
  margin-bottom: 1vw;
 
}

.dialog_box .title img{
  width: 2.5vw;
  height: 2.5vw;
}

.dialog_box .upload-demo1{
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  margin-top: 1vw;
  
}

.dialog_box .upload-demo2{
  width: 100%;
  display: flex;
  flex-direction: column;
  
  align-items: center;
 
}
</style>