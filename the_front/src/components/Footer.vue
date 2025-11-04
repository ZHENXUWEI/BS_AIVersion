<template>
    <div class="footer">
        <p style="font-size: 0.729vw ;">
<!--            <a class="footer-link">建设单位：浙江省产业大数据有限公司</a>-->
            <a class="footer-link">使用问题反馈邮箱：1291546985@qq.com</a>
        </p>
    </div>
</template>

<style scoped>
.footer {
    box-shadow: 0 -0.02vw 0 0 #e0e0e0;
    /* 匹配原色 */
    /* background-color: rgba(236, 245, 255, 0.596); color: #333; */
    background: #111;
    color: #aaa;
    width: 100%;
    display: flex;
    margin-top: 3.5vw;
    padding: 1.042vw 0;
    align-items: center;
    align-self: center;
    justify-content: center;
    height: 3.5vw !important;
}

.footer-link {
    height: 100%;
    margin: 0 1.042vw;
    text-decoration: none;
    padding: 0;
    transition: color 0.1s ease;
}

.footer-link:hover {
    color: #007bff;
    cursor: default;
}
</style>