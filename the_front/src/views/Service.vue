<script setup>
import {computed} from "vue"
import HomeHeader from "@/components/HomeHeader.vue"
import ServiceHeader from "@/components/serviceHeader.vue"
import { Menu as  Message,Box,Share,Notebook } from '@element-plus/icons-vue'
import { useRoute } from 'vue-router';
const router = useRoute();
// index.value="1-1"
const activeMenu=computed(() => {
        // 根据当前路由动态设置 el-menu 的 active 状态
        const route = router.path.split('/').pop();
        if(route=="infoEdit") return "1-1"
        if(route=="enpriseIncom"||route=="nianbao") return "1-2"
        if(route=="goventerprisematch") return "2-1"
        if(route=="policyModel") return "3-1"
        if(route=="policyModelYuCe") return "3-2"
        if(route=="applyMaterials") return "4-1"
        if(route=="applyManage") return "5-1"
    });

</script>

<template>
    <div id="main">
        <HomeHeader></HomeHeader>
        <service-header></service-header>
        <div style="width: 90%;margin:0 auto">
            <el-container class="layout-container-demo" style="height: 500px;">
                <!-- :default-openeds="['1', '3']" -->
                <el-aside width="16vw" style="background-color: white;">
                    <el-scrollbar>
                        <el-menu 
                        router
                         :default-active="activeMenu"
                         
                        >
                            <el-sub-menu index="1" >
                                <template #title>
                                    <el-icon><message /></el-icon>企业信息
                                </template>
                                <el-menu-item-group>
                                    <!-- to="/infoEdit" -->
                                    <el-menu-item index="1-1" :route="{ name: 'InfoEdit' }"  >
                                        企业信息
                                    </el-menu-item>
                                    <!-- to="/enpriseIncom" -->
                                    <el-menu-item index="1-2" :route="{ name: 'EnpriseInCom'}">
                                        年度收入
                                    </el-menu-item>
                                    <!-- <el-menu-item index="enpriseInCom" >
                                        <router-link class="" to="/enpriseInCom">年度收入</router-link>
                                    </el-menu-item> -->
                                    <!-- <el-menu-item index="enpriseInCom/:pa" >
                                        年度收入
                                    </el-menu-item> -->
                                </el-menu-item-group>
                            </el-sub-menu>
                            <el-sub-menu index="2">
                                <template #title>
                                <el-icon><Share /></el-icon>政策推荐
                                </template>
                                <el-menu-item-group>
                                    <!-- to="/goventerprisematch" -->
                                <el-menu-item index="2-1" :route="{ name: 'Goventerprisematch' }">
                                    政企匹配
                                </el-menu-item>
                                <el-menu-item index="2-2" :route="{ name: 'Goventerprisehobby' }">
                                    政策喜好
                                </el-menu-item>
                                </el-menu-item-group>
                            </el-sub-menu>
                            <el-sub-menu index="3">
                                <template #title>
                                <el-icon><Box /></el-icon>政策模型
                                </template>
                                <el-menu-item-group>
                                    <!-- to="/policyModel" -->
                                    <el-menu-item index="3-1" :route="{ name: 'PolicyModel' }">
                                        政策评价模型
                                    </el-menu-item>
                                    <el-menu-item index="3-2" :route="{ name: 'PolicyModelYuCe' }">
                                        政策预测模型
                                    </el-menu-item>
                                </el-menu-item-group> 
                            </el-sub-menu>
                            <el-sub-menu index="4">
                                <template #title>
                                <el-icon><Notebook /></el-icon>申报记录
                                </template>
                                
                                <el-menu-item-group>
                                    <!-- to="/applyMaterials" -->
                                <el-menu-item index="4-1" :route="{ name: 'ApplyMaterials' }">
                                    政策申报材料
                                </el-menu-item>
                                </el-menu-item-group>
                                <el-menu-item-group>
                                    <!-- to="/applyManage" -->
                                <el-menu-item index="4-2" :route="{ name: 'ApplyManage' }">
                                    政策申报管理
                                </el-menu-item>
                                </el-menu-item-group>
                            </el-sub-menu>
                        </el-menu>
                    </el-scrollbar>
                </el-aside>
             
                <el-container>
                <el-main>
                    <el-scrollbar>
                        <RouterView />
                    </el-scrollbar>
                </el-main>
                </el-container>
            </el-container>
        </div>
    </div>
</template>

<style scoped>

#main{
    width: 100%;
    
    display: flex;
    flex-direction: column;
}
.layout-container-demo {
    margin-top: 5vh;
  
}
.layout-container-demo .el-header {
    position: relative;
    background-color: var(--el-color-primary-light-7);
    color: var(--el-text-color-primary);
  }
  .layout-container-demo .el-aside {
    color: var(--el-text-color-primary);
    background: var(--el-color-primary-light-8);
  }
  .layout-container-demo .el-menu {
    border-right: none;
  }
  .layout-container-demo .el-main {
    padding: 0;
  }
  .layout-container-demo .toolbar {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    right: 20px;
  }

  .rl{
    color: inherit;
    text-decoration: none;
    border: none;
    padding: 0;
    background-color: transparent;
    font-size: 2vh;
    cursor: pointer;
  }
</style>
