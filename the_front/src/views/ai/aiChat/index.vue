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
      </div>
    </div>
  </div>
  <Footer/>
</template>

<script>
import { aiChatAPI } from "@/api/ai";
import HomeHeader from "@/components/HomeHeader.vue";
import Footer from "@/components/Footer.vue"; // 后续创建的AI接口

export default {
  components: {Footer, HomeHeader},
  data() {
    return {
      chatHistory: [], // 对话历史 [{ isUser: true/false, content: '...' }]
      userInput: ""    // 用户输入内容
    };
  },
  methods: {
    async sendMessage() {
      if (!this.userInput.trim()) return;

      // 添加用户消息到历史
      this.chatHistory.push({
        isUser: true,
        content: this.userInput
      });

      // 调用AI接口获取回复
      try {
        const res = await aiChatAPI({ question: this.userInput });
        // 添加AI回复到历史
        this.chatHistory.push({
          isUser: false,
          content: res.data.answer
        });
      } catch (err) {
        this.$message.error("AI回复失败，请重试");
      }

      // 清空输入框
      this.userInput = "";
    }
  }
};
</script>

<style scoped>
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
