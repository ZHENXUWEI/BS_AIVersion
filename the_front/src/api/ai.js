import request from '@/utils/request' // 假设已有封装的请求工具

// AI 问答接口
export function aiChatAPI(data) {
    return request({
        url: '/api/ai/api-chat',
        method: 'post',
        data: { question: data.question?.trim() } // 确保参数不为空
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
