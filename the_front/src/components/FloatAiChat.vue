<template>
  <!-- 悬浮按钮 -->
  <div
      class="float-button"
      :style="{ left: buttonLeft + 'px', top: buttonTop + 'px' }"
      @mousedown="startDrag"
      @click="toggleChatWindow"
  >
    <el-icon size="24"><Message /></el-icon>
  </div>

  <!-- 聊天窗口 -->
  <div
      class="chat-window"
      v-if="isVisible"
      :style="{ left: windowLeft + 'px', top: windowTop + 'px' }"
      @mousedown="startWindowDrag"
  >
    <!-- 窗口头部 -->
    <div class="chat-header">
      <span>AI智能助手</span>
      <el-icon class="close-icon" @click="toggleChatWindow"><Close /></el-icon>
    </div>

    <!-- 聊天内容区 -->
    <div class="chat-messages">
      <div
          v-for="(msg, index) in chatHistory"
          :key="index"
          :class="msg.isUser ? 'user-msg' : 'ai-msg'"
      >
        <div class="avatar">{{ msg.isUser ? '我' : 'AI' }}</div>
        <div class="msg-content">{{ msg.content }}</div>
      </div>
    </div>

    <!-- 输入区 -->
    <div class="chat-input">
      <el-input
          v-model="userInput"
          placeholder="输入问题..."
          @keyup.enter.native="sendMessage"
      ></el-input>
      <el-button type="primary" @click="sendMessage">发送</el-button>
    </div>
  </div>
</template>

<script>
import { aiChatAPI } from '@/api/ai'
import { Message, Close } from '@element-plus/icons-vue'

export default {
  components: { Message, Close },
  data() {
    return {
      // 悬浮按钮位置
      buttonLeft: 30,
      buttonTop: 500,
      // 聊天窗口位置和状态
      isVisible: false,
      windowLeft: 200,
      windowTop: 200,
      // 拖拽相关
      isDragging: false,
      isWindowDragging: false,
      dragStartX: 0,
      dragStartY: 0,
      // 聊天数据
      chatHistory: [],
      userInput: ''
    }
  },
  created() {
    // 监听鼠标移动和释放事件
    document.addEventListener('mousemove', this.handleDrag)
    document.addEventListener('mouseup', this.stopDrag)
    // 从本地存储加载历史记录
    this.loadHistory()
  },
  beforeUnmount() {
    document.removeEventListener('mousemove', this.handleDrag)
    document.removeEventListener('mouseup', this.stopDrag)
    // 保存聊天历史
    this.saveHistory()
  },
  methods: {
    // 加载聊天历史
    loadHistory() {
      const history = localStorage.getItem('aiChatHistory')
      if (history) {
        this.chatHistory = JSON.parse(history)
      }
    },
    // 保存聊天历史
    saveHistory() {
      localStorage.setItem('aiChatHistory', JSON.stringify(this.chatHistory))
    },
    // 切换聊天窗口显示/隐藏
    toggleChatWindow() {
      this.isVisible = !this.isVisible
    },
    // 开始拖拽悬浮按钮
    startDrag(e) {
      e.stopPropagation()
      this.isDragging = true
      this.dragStartX = e.clientX - this.buttonLeft
      this.dragStartY = e.clientY - this.buttonTop
    },
    // 开始拖拽聊天窗口
    startWindowDrag(e) {
      e.stopPropagation()
      this.isWindowDragging = true
      this.dragStartX = e.clientX - this.windowLeft
      this.dragStartY = e.clientY - this.windowTop
    },
    // 处理拖拽
    handleDrag(e) {
      if (this.isDragging) {
        this.buttonLeft = e.clientX - this.dragStartX
        this.buttonTop = e.clientY - this.dragStartY
        // 限制在可视区域内
        this.buttonLeft = Math.max(0, Math.min(window.innerWidth - 60, this.buttonLeft))
        this.buttonTop = Math.max(0, Math.min(window.innerHeight - 60, this.buttonTop))
      } else if (this.isWindowDragging) {
        this.windowLeft = e.clientX - this.dragStartX
        this.windowTop = e.clientY - this.dragStartY
        // 限制在可视区域内
        this.windowLeft = Math.max(0, Math.min(window.innerWidth - 350, this.windowLeft))
        this.windowTop = Math.max(0, Math.min(window.innerHeight - 500, this.windowTop))
      }
    },
    // 停止拖拽
    stopDrag() {
      this.isDragging = false
      this.isWindowDragging = false
    },
    // 发送消息
    async sendMessage() {
      const question = this.userInput.trim()
      if (!question) return

      // 添加用户消息
      this.chatHistory.push({
        isUser: true,
        content: question,
        timestamp: new Date().getTime()
      })

      this.userInput = ''
      this.saveHistory()

      try {
        // 显示加载中
        const loadingIndex = this.chatHistory.push({
          isUser: false,
          content: '正在思考...',
          timestamp: new Date().getTime()
        }) - 1

        // 调用AI接口
        const answer = await aiChatAPI({ question })

        // 更新为实际回答
        this.chatHistory[loadingIndex].content = answer
        this.saveHistory()
      } catch (error) {
        this.chatHistory.push({
          isUser: false,
          content: `出错了: ${error.message}`,
          timestamp: new Date().getTime()
        })
        this.saveHistory()
      }
    }
  }
}
</script>

<style scoped>
.float-button {
  position: fixed; /* 固定定位，相对于视口 */
  right: 30px; /* 距离右侧30px */
  bottom: 80px; /* 距离底部80px（避开可能存在的其他固定元素） */
  z-index: 9999; /* 确保在其他内容之上，参考RightPanel的z-index设计 */
  /* 其他样式保持不变 */
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  transition: background-color 0.3s;
}

.float-button:hover {
  background-color: #66b1ff;
}

.chat-window {
  position: fixed;
  right: 30px;
  bottom: 150px; /* 在悬浮按钮上方 */
  z-index: 10000; /* 层级高于按钮 */
  width: 350px;
  height: 500px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
}

.chat-header {
  padding: 12px 15px;
  background-color: #409eff;
  color: white;
  border-radius: 10px 10px 0 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: move;
}

.close-icon {
  cursor: pointer;
}

.chat-messages {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
  background-color: #f5f7fa;
}

.user-msg, .ai-msg {
  display: flex;
  margin-bottom: 15px;
  max-width: 80%;
}

.user-msg {
  margin-left: auto;
  flex-direction: row-reverse;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 8px;
  flex-shrink: 0;
}

.user-msg .avatar {
  background-color: #67c23a;
}

.msg-content {
  padding: 10px 12px;
  border-radius: 8px;
  background-color: white;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.user-msg .msg-content {
  background-color: #e6f7ff;
}

.chat-input {
  display: flex;
  padding: 10px;
  border-top: 1px solid #eee;
}

.el-input {
  flex: 1;
  margin-right: 8px;
}
</style>