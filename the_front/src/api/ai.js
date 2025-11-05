import request from '@/utils/request' // 假设已有封装的请求工具

// AI 问答接口
export function aiChatAPI(data) {
    return request({
        url: '/api/ai/chat',
        method: 'post',
        data: { question: data.question?.trim() },
        timeout: 60000
    }).then(response => {
        console.log('=== AI接口响应详情 ===');
        console.log('完整响应:', response);
        console.log('响应状态码:', response.status);
        console.log('响应消息:', response.msg);
        console.log('数据内容:', response.data);
        console.log('====================');

        // 修正：使用正确的字段名 status 而不是 code
        if (response.status === 200 || response.status === 0) {
            // 直接返回回答内容
            const answer = response.data?.content || "未获取到有效回答";
            console.log('提取的回答:', answer);
            return answer;
        } else {
            // 修正：使用 response.msg 而不是 response.message
            throw new Error(response.msg || '接口调用失败');
        }
    }).catch(error => {
        console.error('AI接口调用错误:', error);
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
