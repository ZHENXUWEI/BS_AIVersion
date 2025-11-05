import request from '@/utils/request' // 假设已有封装的请求工具

// AI 问答接口
export function aiChatAPI(data) {
    return request({
        url: '/api/ai/chat',
        method: 'post',
        data: { question: data.question?.trim() }, // 确保参数不为空
        timeout: 60000 // 前端超时同步延长至60秒
    }).then(response => {
        // 统一处理成功与失败响应
        if (response.code === 200) {
            // 正常响应时返回answer
            return response.data?.answer;
        } else {
            // 错误响应时抛出异常信息
            throw new Error(response.msg || '接口调用失败');
        }
    }).catch(error => {
        // 捕获网络错误或超时
        throw new Error(error.message || '网络异常，请稍后重试');
    });
}

// // 流式AI问答接口
// export function aiChatStreamAPI(data) {
//     return request({
//         url: '/api/ai/chats',
//         method: 'post',
//         data: { question: data.question?.trim() },
//         responseType: 'stream', // 关键：指定流式响应
//     })
// }

// 知识库相关接口
export function addKnowledgeAPI(data) {
    return request({
        url: '/api/ai/knowledge/add',
        method: 'post',
        data: data
    })
}

export function knowledgeChatAPI(data) {
    return request({
        url: '/api/ai/knowledge/chat',
        method: 'post',
        data: data
    })
}

export function listKnowledgeAPI(category) {
    return request({
        url: '/api/ai/knowledge/list',
        method: 'get',
        params: { category }
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
