<!-- the_front/src/components/ModelDataInput.vue -->
<template>
  <div class="data-input-container">
    <h2>输入企业指标数据</h2>
    <el-form ref="dataForm" :model="formData" label-width="120px">
      <el-form-item
          v-for="(indicator, index) in indicators"
          :key="index"
          :label="indicator.name"
          :required="indicator.required"
      >
        <el-input
            v-model="formData[indicator.key]"
            :placeholder="`请输入${indicator.name}`"
            :type="indicator.type || 'text'"
        ></el-input>
        <div class="threshold-hint" v-if="indicator.threshold">
          参考阈值: {{ indicator.threshold }}
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitData">提交匹配</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getModelIndicatorsAPI, matchModelDataAPI } from '@/api/service';

const router = useRouter();
const formData = ref({});
const indicators = ref([]);
const modelId = ref('');

// 从路由参数获取模型ID
onMounted(() => {
  const route = router.currentRoute.value;
  modelId.value = route.query.modelId;
  fetchIndicators();
});

// 获取模型所需指标
const fetchIndicators = async () => {
  try {
    const res = await getModelIndicatorsAPI({ modelId: modelId.value });
    indicators.value = res.data;

    // 初始化表单数据
    res.data.forEach(indicator => {
      formData.value[indicator.key] = '';
    });
  } catch (error) {
    console.error('获取指标失败:', error);
  }
};

// 提交数据进行匹配
const submitData = async () => {
  try {
    const params = {
      modelId: modelId.value,
      data: formData.value
    };
    const res = await matchModelDataAPI(params);
    // 跳转到结果页面，并传递匹配结果
    router.push({
      path: '/model-match-result',
      query: {
        success: res.data.success,
        details: JSON.stringify(res.data.details)
      }
    });
  } catch (error) {
    console.error('匹配失败:', error);
  }
};

// 重置表单
const resetForm = () => {
  indicators.value.forEach(indicator => {
    formData.value[indicator.key] = '';
  });
};
</script>

<style scoped>
.data-input-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.threshold-hint {
  color: #666;
  font-size: 12px;
  margin-top: 5px;
}

.el-form-item {
  margin-bottom: 15px;
}
</style>