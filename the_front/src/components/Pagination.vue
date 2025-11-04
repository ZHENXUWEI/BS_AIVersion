<template>
	<div class="pagination">
		<span style="font-size: 1.2vw;">共{{ totalItems }}条记录 第{{ currentPage }}/{{ totalPages }}页</span>
		<el-pagination    
      v-model:page-size="pageSize"
			layout="prev, pager, next, jumper"
      :disabled="disabled"
      :total="totalItems"
			@current-change="handleCurrentChange"
			background
    />
	</div>
</template>

<script setup>
import { ref, computed } from 'vue'
const currentPage = ref(1)
const	props = defineProps({
		itemsPerPage: {
			type: Number,
			required: true,
		},
		totalItems: {
			type: Number,
			required: true
		}
})

const pageSize = computed(() => props.pageSize)
const totalItems = computed(() => props.totalItems)
const totalPages = computed(() => Math.ceil(props.totalItems / props.itemsPerPage))
const emit = defineEmits(['page-changed'])

const handleCurrentChange = (page) => {
  // console.log(`Current page: ${page}`);
	emit('page-changed', page)
	currentPage.value = page
}
</script>

<style scoped>

.pagination {
	display: flex;
	align-items: center;
	justify-content: space-between;
}
.el-pagination {
	display: flex;
	font-size: 1vw;
	justify-content: flex-end;
}
</style>
