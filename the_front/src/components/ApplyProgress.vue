<script setup>
import { ref ,onMounted} from 'vue'
import { useRoute,useRouter } from 'vue-router';
import { shenBaoProgressAPI} from '@/api/service';
const route = useRoute();
const router = useRouter();
const urlParams = route.query;
//后退
const confirmFun=()=>{
  router.push({path:"/applyManage"})
}

const getProgress=async (obj)=>{
   const res= await shenBaoProgressAPI(obj)
   if(res.data==null) return
   tableData.value=[res.data]
   console.log(res.data)
}

onMounted(()=>{
    
    getProgress(urlParams)
})
const tableData=ref([])
tableData.value = [{
		shenbaoStatus: '0',
		shenbaoYijian: '未知状态',
    shenheBumen: '未知单位',
    shenheTime: '未知时间'
  }]
</script>
<template>
  <div id="main">
    <div id="content">
      <h2 style="text-align: center;margin-bottom: 30px;">申报进程</h2>
	<el-table :data="tableData" border class="policyAppl" style="width: 100%;" >
    <el-table-column label="申报状态" align="center">
			<template #default="scope">
        {{ scope.row.shenbaoStatus=="1"?"已审核":"未审核"}}
      </template>
		</el-table-column>
    
    <el-table-column prop="shenbaoYijian" label="申报意见" align="center"/>
		<el-table-column prop="shenheBumen" label="审核部门" align="center"/>
    <el-table-column prop="shenheTime" label="审核时间" align="center" />
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