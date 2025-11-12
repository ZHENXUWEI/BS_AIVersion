<!-- the_front/src/components/ModelMatchResult.vue -->
<template>
  <div class="match-result-container">
    <div class="result-header" :class="success ? 'success' : 'failure'">
      <el-icon :size="48" class="result-icon">
        <successFilled v-if="success" />
        <CircleCloseFilled v-else />
      </el-icon>
      <h2>{{ success ? '匹配成功' : '匹配失败' }}</h2>
    </div>

    <div class="result-details">
      <h3>匹配详情</h3>
      <el-table :data="details" border>
        <el-table-column prop="name" label="指标名称" width="200"></el-table-column>
        <el-table-column prop="value" label="您输入的值"></el-table-column>
        <el-table-column prop="threshold" label="阈值要求"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status ? 'success' : 'danger'">
              {{ scope.row.status ? '达标' : '未达标' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="result-actions">
      <el-button @click="goBack">返回修改</el-button>
      <el-button
          type="primary"
          @click="goNext"
          v-if="success"
      >
        继续下一步
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { SuccessFilled, CircleCloseFilled } from '@element-plus/icons-vue';

const router = useRouter();
const success = ref(false);
const details = ref([]);

onMounted(() => {
  const route = router.currentRoute.value;
  success.value = route.query.success === 'true';
  details.value = JSON.parse(route.query.details || '[]');
});

const goBack = () => {
  router.back();
};

const goNext = () => {
  // 跳转到下一步操作页面
  router.push('/application-form');
};
</script>

<style scoped>
.match-result-container {
  max-width: 1000px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.result-header {
  text-align: center;
  padding: 30px 0;
  margin-bottom: 20px;
  border-radius: 8px;
}

.result-header.success {
  background-color: #f0f9eb;
  color: #52c41a;
}

.result-header.failure {
  background-color: #fff2f0;
  color: #f5222d;
}

.result-icon {
  margin-bottom: 15px;
}

.result-details {
  margin-bottom: 30px;
}

.result-details h3 {
  margin-bottom: 15px;
  font-size: 16px;
}

.result-actions {
  text-align: center;
  margin-top: 20px;
}

.result-actions .el-button {
  margin: 0 10px;
}
</style>