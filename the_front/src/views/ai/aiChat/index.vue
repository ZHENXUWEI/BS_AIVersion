<template>
  <HomeHeader></HomeHeader>
  <div class="app-container">
    <div class="ai-chat-container">
      <!-- 对话历史展示区 -->
      <div class="chat-history">
        <div
            v-for="(msg, index) in chatHistory"
            :key="index"
            :class="msg.isUser ? 'user-message' : 'ai-message'"
        >
          <div class="avatar">{{ msg.isUser ? '我' : 'AI' }}</div>
          <div class="content">{{ msg.content }}</div>
        </div>
      </div>

      <!-- 输入区 -->
      <div class="chat-input">
        <el-input
            v-model="userInput"
            placeholder="请输入问题..."
            @keyup.enter.native="sendMessage"
            clearable
        />
        <el-button type="primary" @click="sendMessage">发送</el-button>
        <el-button type="warning" @click="clearChatHistory">清空记录</el-button>
      </div>
    </div>
  </div>
  <Footer/>
</template>

<script>
import { aiChatAPI } from "@/api/ai";  // 只保留基础AI接口
import HomeHeader from "@/components/HomeHeader.vue";
import Footer from "@/components/Footer.vue";

export default {
  components: { Footer, HomeHeader },
  data() {
    return {
      chatHistory: [],
      userInput: ""
    };
  },
  created() {
    this.loadChatHistory();
    window.addEventListener('beforeunload', this.saveChatHistory);
  },
  beforeDestroy() {
    this.saveChatHistory();
    window.removeEventListener('beforeunload', this.saveChatHistory);
  },
  methods: {
    loadChatHistory() {
      const saved = localStorage.getItem('aiChatHistory');
      if (saved) {
        try {
          this.chatHistory = JSON.parse(saved);
        } catch (e) {
          console.error('加载聊天记录失败', e);
          localStorage.removeItem('aiChatHistory');
        }
      }
    },
    saveChatHistory() {
      if (this.chatHistory.length > 0) {
        localStorage.setItem('aiChatHistory', JSON.stringify(this.chatHistory));
      }
    },
    async sendMessage() {
      if (!this.userInput.trim()) {
        this.$message.warning("请输入问题内容");
        return;
      }

      // 添加用户消息
      this.chatHistory.push({
        isUser: true,
        content: this.userInput.trim(),
        timestamp: new Date().getTime()
      });

      try {
        // 切换为基础AI接口（取消知识库调用）
        const res = await aiChatAPI({ question: this.userInput.trim() });
        // 处理AI回复（根据后端返回格式调整）
        if (res && res.code === 200 && res.data?.answer) {
          this.chatHistory.push({
            isUser: false,
            content: res.data.answer,
            timestamp: new Date().getTime()
          });
        } else {
          this.chatHistory.push({
            isUser: false,
            content: `获取回复失败: ${res?.msg || '未知错误'}`,
            timestamp: new Date().getTime()
          });
        }
      } catch (err) {
        this.chatHistory.push({
          isUser: false,
          content: `接口调用失败: ${err.message || '网络异常'}`,
          timestamp: new Date().getTime()
        });
      }

      this.userInput = "";
      this.saveChatHistory();
    },
    clearChatHistory() {
      this.chatHistory = [];
      localStorage.removeItem('aiChatHistory');
      this.$message.success("已清空聊天记录");
    }
  }
};
</script>

<style scoped>
/* 样式部分保持不变 */
.ai-chat-container {
  display: flex;
  flex-direction: column;
  height: 80vh;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  overflow: hidden;
}

.chat-history {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
}

.user-message, .ai-message {
  display: flex;
  margin-bottom: 15px;
}

.user-message {
  justify-content: flex-end;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 10px;
}

.user-message .avatar {
  background-color: #67c23a;
}

.content {
  max-width: 60%;
  padding: 10px 15px;
  border-radius: 8px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-message .content {
  background-color: #e6f7ff;
}

.chat-input {
  display: flex;
  padding: 10px;
  border-top: 1px solid #e6e6e6;
}

.el-input {
  flex: 1;
  margin-right: 10px;
}
</style>