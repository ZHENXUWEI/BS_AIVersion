import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhLocale from 'element-plus/es/locale/lang/zh-cn'
import IconTooling from '@/components/icons/IconTooling.vue';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

app.component('IconTooling', IconTooling);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(createPinia())
app.use(router)
app.use(ElementPlus, { locale: zhLocale })
app.mount('#app')
