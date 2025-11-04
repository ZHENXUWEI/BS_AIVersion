
<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
    <div id="policy_box">
        <el-table :header-cell-style="{'text-align':'center','font-size':'20px'}"
        :cell-style="{'text-align':'center'}"
         :data="policyinfoData" height="500" style="width: 100%">
            <el-table-column prop="PolicyModelName" label="模型名称" width="180" />
            <el-table-column prop="PolicyStatus" label="政策状态" width="180" />
            <el-table-column fixed="right" label="操作" min-width="120">
                <template #default="scope">
                  <el-button link type="primary" size="small">
                    <RouterLink  >补全材料</RouterLink>
                  </el-button>
                  <el-button link type="primary" size="small">
                    <RouterLink  >查看进度</RouterLink>
                  </el-button>
                </template>
              </el-table-column>
        </el-table>
    </div>
</template>

<style scoped>

</style>
