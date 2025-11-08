<script setup>
import { RouterLink, RouterView, useRouter } from 'vue-router'
import FloatAiChat from './components/FloatAiChat.vue'
import { ref, onMounted, onUnmounted } from 'vue'

const isAiChatPage = ref(false)
const router = useRouter()

function updateRouteStatus(to) {
  isAiChatPage.value = to.path === '/ai/chat'
}

onMounted(() => {
  // 初始检测
  updateRouteStatus(router.currentRoute.value)

  // 监听路由变化
  router.afterEach((to) => {
    updateRouteStatus(to)
  })
})
</script>

<template>
  <RouterView />
  <FloatAiChat v-show="!isAiChatPage" />
</template>