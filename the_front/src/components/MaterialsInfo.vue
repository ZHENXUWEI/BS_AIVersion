<script setup>
import { useRoute,useRouter } from 'vue-router';
import { caiLiaoQingDanAPI } from '@/api/service';
import {ref,onMounted} from "vue"
const route = useRoute();
const router = useRouter();
const urlParams = route.query;
const tableData=ref([])
// 后退
const confirmFun=()=>{
  router.go(-1)
}

const getCaiLiaoData=async(obj)=>{
	// console.log(obj)
   const res= await caiLiaoQingDanAPI(obj)
//    var aa=res.data
   var aa=res.data.docDetail.split(";")
//    var tt=[]
//    for(var i=0;i<aa.length;i++){
// 		tt.append({modelName:})
//    }

   const result = aa.filter(part => part.trim() !== '').map(part => {
  		return {modelName:urlParams.modelName, name: part.trim() }; 
	});
//    console.log(result)
   tableData.value=result
//    console.log(tableData.value.docDetail)
}

getCaiLiaoData(urlParams)


</script>
<template>
	<div id="main">
		<div id="content">
			<h2 style="margin-bottom: 30px;margin-left: 30px;">材料清单</h2>
			<el-table :data="tableData" border class="policyAppl" style="width: 100%;" >
			<el-table-column prop="modelName" label="政策模型" align="center"/>
			<el-table-column prop="name" label="材料名称" align="center"/>
			</el-table>
			<div class="btns">
				<el-button type="success" @click="confirmFun">确定</el-button>
			</div>
		</div>
	</div>
</template>

<style scoped>
#main{
	display: flex;
	width: 100%;
	height: 100vh;
	flex-direction: column;
	
	align-items: center;
	
  }
  #content{
	display: flex;
	width: 85%;
	flex-direction: column;
	margin-top: 6vh;
	align-items: center;
	justify-content: center;
	align-self: center;
  }
.policyAppl {
	margin-left: 30px;
	font-size: 20px;
}
.btns {
	display: flex;
	justify-content: center;
	margin-top: 30px;
}
</style>