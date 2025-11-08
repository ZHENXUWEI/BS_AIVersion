<template>
  <!-- 悬浮按钮（固定左上角、无拖拽） -->
  <div class="float-button" @click="toggleChatWindow">
    AI
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
          :disabled="isListening"
      ></el-input>
      <el-button
          :type="isListening ? 'warning' : 'success'"
          @click="toggleVoiceInput"
          :disabled="false"
      >{{ isListening ? '停止' : '语音' }}</el-button>
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
      // 聊天窗口位置和状态（保留窗口拖拽功能）
      isVisible: false,
      windowLeft: 200,
      windowTop: 200,
      // 拖拽相关（仅保留窗口拖拽）
      isWindowDragging: false,
      dragStartX: 0,
      dragStartY: 0,
      // 聊天数据
      chatHistory: [],
      userInput: '',
      // 语音输入相关
      isListening: false,
      recognition: null
    }
  },
  created() {
    // 初始化语音识别
    this.initSpeechRecognition()
    // 仅保留窗口拖拽的事件监听
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
    // 停止语音识别
    if (this.recognition && this.isListening) {
      this.recognition.stop()
    }
  },
  methods: {
    // 初始化语音识别
    initSpeechRecognition() {
      const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition
      if (!SpeechRecognition) {
        this.$message.warning('您的浏览器不支持语音输入功能')
        return
      }

      this.recognition = new SpeechRecognition()
      this.recognition.lang = 'zh-CN'
      this.recognition.continuous = false
      this.recognition.interimResults = false

      this.recognition.onresult = (event) => {
        const transcript = event.results[0][0].transcript
        this.userInput += transcript
      }

      this.recognition.onend = () => {
        if (this.isListening) {
          this.recognition.start()
        }
      }

      this.recognition.onerror = (event) => {
        console.error('语音识别错误:', event.error)
        this.$message.error('语音识别失败: ' + event.error)
        this.isListening = false
      }
    },

    // 切换语音输入状态
    toggleVoiceInput() {
      if (!this.recognition) return

      if (this.isListening) {
        this.recognition.stop()
        this.isListening = false
        this.$message.success('语音输入已停止')
      } else {
        try {
          this.recognition.start()
          this.isListening = true
          this.$message.success('请开始说话...')
        } catch (error) {
          console.error('启动语音识别失败:', error)
          this.$message.error('无法启动语音输入，请稍后再试')
        }
      }
    },

    // 加载聊天历史
    loadHistory() {
      const history = localStorage.getItem('floatAiChatHistory')
      if (history) {
        this.chatHistory = JSON.parse(history)
      }
    },

    // 保存聊天历史
    saveHistory() {
      localStorage.setItem('floatAiChatHistory', JSON.stringify(this.chatHistory))
    },

    // 切换聊天窗口显示/隐藏
    toggleChatWindow() {
      this.isVisible = !this.isVisible
    },

    // 开始拖拽聊天窗口
    startWindowDrag(e) {
      e.stopPropagation()
      this.isWindowDragging = true
      this.dragStartX = e.clientX - this.windowLeft
      this.dragStartY = e.clientY - this.windowTop
    },

    // 处理拖拽（仅保留窗口拖拽逻辑）
    handleDrag(e) {
      if (this.isWindowDragging) {
        this.windowLeft = e.clientX - this.dragStartX
        this.windowTop = e.clientY - this.dragStartY
        // 限制窗口在可视区域内
        this.windowLeft = Math.max(0, Math.min(window.innerWidth - 350, this.windowLeft))
        this.windowTop = Math.max(0, Math.min(window.innerHeight - 500, this.windowTop))
      }
    },

    // 停止拖拽
    stopDrag() {
      this.isWindowDragging = false
    },

    // 发送消息
    async sendMessage() {
      if (this.isListening) {
        this.recognition.stop()
        this.isListening = false
      }

      const question = this.userInput.trim()
      if (!question) return

      this.chatHistory.push({
        isUser: true,
        content: question,
        timestamp: new Date().getTime()
      })

      this.userInput = ''
      this.saveHistory()

      try {
        const loadingIndex = this.chatHistory.push({
          isUser: false,
          content: '正在思考...',
          timestamp: new Date().getTime()
        }) - 1

        const answer = await aiChatAPI({ question })
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
/* 悬浮按钮：固定左上角、随窗口缩放 */
.float-button {
  position: fixed;
  left: 2vw;  /* 距离左侧为视口宽度的2% */
  top: 15vh;   /* 距离顶部为视口高度的2% */
  z-index: 9998;

  /* 大小随窗口变化（使用vw单位） */
  width: 7vw;   /* 宽度为视口宽度的10% */
  height: 7vw;  /* 高度与宽度一致（保持圆形） */

  /* 限制最大/最小尺寸，避免过大或过小 */
  max-width: 120px;
  max-height: 120px;
  min-width: 60px;
  min-height: 60px;

  border-radius: 50%;
  background-color: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  transition: all 0.3s;

  /* 文字大小随按钮大小变化 */
  font-size: 4vw;
  max-font-size: 48px;
  min-font-size: 24px;
}

.float-button:hover {
  background-color: #66b1ff;
  transform: scale(1.05);
}

/* 聊天窗口样式保持不变 */
.chat-window {
  position: fixed;
  right: 30px;
  bottom: 150px;
  z-index: 10000;
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

.chat-input .el-button {
  margin-left: 4px;
}

.el-button[icon="el-icon-microphone"] {
  background-color: #13ce66;
  border-color: #13ce66;
}

.el-button[icon="el-icon-microphone"]:not(.is-success) {
  background-color: #409eff;
  border-color: #409eff;
}
</style>