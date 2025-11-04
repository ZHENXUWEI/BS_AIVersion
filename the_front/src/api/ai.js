import request from '@/utils/request' // 假设已有封装的请求工具

// AI 问答接口
export function aiChatAPI(data) {
    return request({
        url: '/api/ai/chat', // 后端 AI 接口地址（需与后端协商）
        method: 'post',
        data: data // 传递问题参数，如 { question: "温州市政策有哪些？" }
    })
}

// 若需要其他 AI 功能（如政策分析），可添加更多接口
export function aiPolicyAnalysisAPI(data) {
    return request({
        url: '/api/ai/policy-analysis',
        method: 'post',
        data: data
    })
}
