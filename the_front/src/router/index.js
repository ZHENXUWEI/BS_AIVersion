import { createRouter, createWebHistory } from 'vue-router'
import AIChat from '@/views/ai/aiChat/index.vue';
// import HomeView from '../views/HomeView.vue'
import Login from '@/views/Login.vue'
import Policy from '@/components/Policy.vue'
import Match from '@/components/Match.vue'
import Detail from '@/views/PolicyDetail.vue'
import CompanyInfo from '@/components/CompanyInfo.vue'
import Index from '@/views/Index.vue'
import Home from '@/views/Home.vue'
import Service from '@/views/Service.vue'
import EnpriseEdit from '@/components/EnpriseEdit.vue'
import EnpriseInCom from '@/components/EnpriseInCom.vue'
import Policysbcl from '@/components/PolicyShenBaoCaiLiao.vue'
import NianBao from '@/components/NianBao.vue'
import PolicyModel from '@/components/PolicyModelq.vue'
import PolicyModelYuCe from '@/components/policyModelYuCe.vue'
import ApplyProcess from '@/components/ApplyProgress.vue'
import ApplyMaterials from '@/components/ApplyMaterials.vue'
import MaterialsInfo from '@/components/MaterialsInfo.vue'
import MaterialsDetail from '@/components/MaterialsDetail.vue'
import ApplyManage from '@/components/ApplyManage.vue'
import GovEnterpriseMatch from '@/components/GovEnterpriseMatch.vue'
import GovEnterpriseHobby from '@/components/Goventerprisehobby.vue'
import PolicyDetail from '@/components/PolicyDetail.vue'
import PolicyMatch from '@/views/policyMatch.vue'
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'Home',
            component: Home
        },{
            path: '/ai', // 父路径
            name: 'AI',
            children: [
                {
                    path: 'chat', // 完整路径：/ai/chat
                    name: 'AIChat',
                    component: AIChat,
                    meta: { title: 'AI问答', icon: 'el-icon-comment' }
                }
            ]
        },
        {
            path: '/service',
            name: 'Service',
            component: Service,
            children:[
                {
                    path:'/infoEdit',
                    name:"InfoEdit",
                    component:EnpriseEdit
                },
                {
                    path:'/goventerprisematch',
                    name:"Goventerprisematch",
                    component:GovEnterpriseMatch
                },
                {
                    path: '/goventerprisehobby',
                    name: 'Goventerprisehobby',
                    component: GovEnterpriseHobby
                },
                {
                    path: '/policyModel',
                    name: 'PolicyModel',
                    component: PolicyModel
                },
                {
                    path: '/policyModelYuCe',
                    name: 'PolicyModelYuCe',
                    component: PolicyModelYuCe
                },
                {
                    path: '/applyProcess',
                    name:"ApplyProcess",
                    component: ApplyProcess
                },
                {
                    path: '/applyMaterials',
                    name:"ApplyMaterials",
                    component: ApplyMaterials
                },
                {
                    path: '/materialsInfo',
                    name:"MaterialsInfo",
                    component: MaterialsInfo
                },
                {
                    path: '/materialsDetail',
                    name:"MaterialsDetail",
                    component: MaterialsDetail
                },
                {
                    path: '/applyManage',
                    name:"ApplyManage",
                    component: ApplyManage
                },
                {
                    path: '/policydetail',

                    component: PolicyDetail
                },
                {
                    path:'/enpriseInCom',
                    name:"EnpriseInCom",
                    component:EnpriseInCom,
                    // children:[
                    //   {
                    //     path:'nianbao',
                    //     component:NianBao,
                    //     name:"NianBao",
                    //     props: true

                    //   }
                    // ]
                },
                {
                    path:'/policysbcl',
                    component:Policysbcl
                }
            ]
        },
        {
            path: '/companyinfo',
            name: 'CompanyInfo',
            component: CompanyInfo
        },
        {
            path: '/login', component: Login
        },
        {
            path: '/policy', component: Policy
        },
        {
            path: '/match', component: PolicyMatch
        },
        {
            path: '/detail', component: Detail
        },
    ]
})

export default router
