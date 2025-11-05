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
          <div class="content">
            <!-- 加载状态显示 -->
            <template v-if="msg.isLoading">
              <span class="loading-dot">●</span>
              <span class="loading-dot">●</span>
              <span class="loading-dot">●</span>
            </template>
            <!-- 逐字显示的内容 -->
            <template v-else>{{ msg.displayContent }}</template>
          </div>
        </div>
      </div>

      <!-- 输入区 -->
      <div class="chat-input">
        <el-input
            v-model="userInput"
            placeholder="请输入问题..."
            @keyup.enter.native="sendMessage"
            clearable
            :disabled="isSending"/>
        <el-button
            type="primary"
            @click="sendMessage"
            :loading="isSending">发送</el-button>
        <el-button type="warning" @click="clearChatHistory">清空记录</el-button>
      </div>
    </div>
  </div>
  <Footer/>
</template>

<script>
import {aiChatAPI} from "@/api/ai";
import HomeHeader from "@/components/HomeHeader.vue";
import Footer from "@/components/Footer.vue";

export default {
  components: { Footer, HomeHeader },
  data() {
    return {
      chatHistory: [],
      userInput: "",
      isSending: false,  // 控制整体发送状态
      typingTimer: null  // 逐字显示的定时器
    };
  },
  created() {
    this.loadChatHistory();
    window.addEventListener('beforeunload', this.saveChatHistory);
  },
  beforeDestroy() {
    this.saveChatHistory();
    window.removeEventListener('beforeunload', this.saveChatHistory);
    if (this.typingTimer) clearInterval(this.typingTimer);
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
      const question = this.userInput.trim();
      if (!question) {
        this.$message.warning("请输入问题内容");
        return;
      }

      console.log('=== 开始发送消息 ===');
      console.log('问题:', question);

      // 添加用户消息
      this.chatHistory.push({
        isUser: true,
        content: question,
        displayContent: question,
        timestamp: new Date().getTime()
      });

      // 添加AI加载消息
      const aiMsgIndex = this.chatHistory.push({
        isUser: false,
        isLoading: true,
        content: "",
        displayContent: "",
        timestamp: new Date().getTime()
      }) - 1;

      this.userInput = "";
      this.isSending = true;
      this.saveChatHistory();

      try {
        console.log('=== 调用AI接口前 ===');
        const fullAnswer = await aiChatAPI({ question });
        console.log('=== AI接口调用完成 ===');
        console.log('收到的回答类型:', typeof fullAnswer);
        console.log('收到的回答内容:', fullAnswer);
        console.log('回答长度:', fullAnswer?.length);

        // 更严格的空值检查
        if (!fullAnswer || fullAnswer.trim() === "" || fullAnswer === "未获取到有效回答") {
          console.warn('回答内容无效');
          throw new Error('AI返回内容无效');
        }

        console.log('=== 更新AI消息 ===');
        this.chatHistory[aiMsgIndex].isLoading = false;
        this.chatHistory[aiMsgIndex].content = fullAnswer;
        console.log('AI消息更新完成');

        // 逐字显示效果
        console.log('=== 开始逐字显示 ===');
        this.typeWriter(aiMsgIndex, fullAnswer);

      } catch (err) {
        console.error('=== 发送消息错误 ===', err);
        this.chatHistory[aiMsgIndex].isLoading = false;
        this.chatHistory[aiMsgIndex].displayContent = `接口调用失败: ${err.message || '网络异常'}`;
        console.log('错误处理完成');
      } finally {
        this.isSending = false;
        this.saveChatHistory();
        console.log('=== 发送消息流程结束 ===');
      }
    },
    // 逐字显示函数
    typeWriter(msgIndex, fullText) {
      let currentIndex = 0;
      // 清除之前的定时器（防止多次触发）
      if (this.typingTimer) clearInterval(this.typingTimer);

      // 每50ms显示一个字符
      this.typingTimer = setInterval(() => {
        if (currentIndex < fullText.length) {
          // 拼接当前字符
          this.chatHistory[msgIndex].displayContent += fullText.charAt(currentIndex);
          currentIndex++;
          this.saveChatHistory();  // 实时保存
        } else {
          // 显示完成，清除定时器
          clearInterval(this.typingTimer);
          this.typingTimer = null;
        }
      }, 50);  // 速度可调整（越小越快）
    },
    clearChatHistory() {
      if (this.typingTimer) clearInterval(this.typingTimer);
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

.loading-dot {
  display: inline-block;
  margin: 0 2px;
  animation: loading 1.4s infinite ease-in-out both;
}
/* 三个点的动画延迟，形成循环效果 */
.loading-dot:nth-child(1) { animation-delay: -0.32s; }
.loading-dot:nth-child(2) { animation-delay: -0.16s; }

@keyframes loading {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

/* 禁用状态样式优化 */
.el-input.is-disabled .el-input__inner {
  background-color: #f5f7fa;
  cursor: not-allowed;
}
</style>