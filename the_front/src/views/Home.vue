<script setup>
import HomeHeader from "@/components/HomeHeader.vue"
import Footer from "@/components/Footer.vue"
import { ref, onMounted } from "vue"
import { setKeyWordCookie, getCookie } from '@/utils/cookie'
import { policyinfoAPI, policystatusAPI, policyNumberAPI, policyVisitNAPI, policyMatchNAPI, policyPushAPI, policyeffectiveAPI } from "@/api/policy"
import { More, Search } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router';
import { daysUntil } from "@/utils/daysUtil"

const router = useRouter();
const route = useRoute();
const policyStatus = ref({});
const policyinfoNewData = ref({});
const policyinfoHotData = ref({});
const policyinfoPushData = ref({});
const policyEffectiveData = ref({})
const searchData = ref("");
const lastDays = (deadTime) => {

    return daysUntil(deadTime)
}
const getpolicyEffectiveData = async () => {
       var jsonData = {
        categories: [],
        query1: "可申报政策",
        query2: "",
        timePeriod: []
    }

    const res = await policyeffectiveAPI(jsonData)
    // console.log("233" + res)
    if (res.data == undefined) {
        policyEffectiveData.value = []
        return
    }
    var arr = res.data.row

     var newArr=[]

    for(var i=0;i<arr.length;i++){
        if(lastDays(arr[i].expireDate)!="已过期"){
            // console.log(arr[i])
            newArr.push(arr[i])
            if(newArr.length==5) break
        }
    }

     policyEffectiveData.value=newArr
    // console.log(arr.length)
    // if (arr.length > 5) {
    //     policyEffectiveData.value = arr.slice(0, 5);
    // } else {
    //     policyEffectiveData.value = arr
    // }

}


const getPolicyNewData = async () => {
    // console.log(type1,policyType,subPolicyType)
    var jsonData = {
        categories: [],
        query1: "最新政策",
        query2: "",
        timePeriod: []
    }
    const jsonString = JSON.stringify(jsonData);
    const res = await policyinfoAPI(jsonString)
    if (res.data == undefined) {
        policyinfoNewData.value = []
        return
    }
    var arr = res.data.row
    // console.log("-----------")
    // console.log(arr)
     var newArr=[]

    for(var i=0;i<arr.length;i++){
        if(lastDays(arr[i].expireDate)!="已过期"){
            // console.log(arr[i])
            newArr.push(arr[i])
            if(newArr.length==5) break
        }
    }
    policyinfoNewData.value=newArr
    // console.log(arr.length)
    // if (arr.length > 5) {
    //     policyinfoNewData.value = arr.slice(0, 5);
    // } else {
    //     policyinfoNewData.value = arr
    // }


}

const getPolicyHotData = async () => {
    // console.log(type1,policyType,subPolicyType)
    var jsonData = {
        categories: [],
        query1: "浏览量",
        query2: "",
        timePeriod: []
    }
    const jsonString = JSON.stringify(jsonData);
    const res = await policyinfoAPI(jsonString)

    if (res.data == undefined) {
        policyinfoHotData.value = []
        return
    }
    var arr = res.data.row
    var newArr=[]

    for(var i=0;i<arr.length;i++){
        if(lastDays(arr[i].expireDate)!="已过期"){
            // console.log(arr[i])
            newArr.push(arr[i])
            if(newArr.length==5) break
        }
    }
    policyinfoHotData.value=newArr
    // if (arr.length > 5) {
    //     policyinfoHotData.value = arr.slice(0, 5);
    // } else {
    //     policyinfoHotData.value = arr
    // }
}

const getPolicyPushData = async () => {
    const info = getCookie()
    // console.log(1111111111111111)
    // console.log(info.id)
    let res;
    if(info){
        if(info.id){
            res = await policyPushAPI(info.id)//'getCookie().id'
        }
    }
//  console.log(res)
    if (res.data == undefined) {
        policyinfoHotData.value = []
        return
    }

    var arr = res.data.row
    // console.log(arr)
     var newArr=[]

    for(var i=0;i<arr.length;i++){
        if(lastDays(arr[i].expireDate)!="已过期"){
            // console.log(arr[i])
            newArr.push(arr[i])
            if(newArr.length==5) break
        }
    }
    policyinfoPushData.value=newArr
    // if (arr.length > 5) {
    //     policyinfoPushData.value = arr.slice(0, 5);
    // } else {
    //     policyinfoPushData.value = arr
    // }
    // console.log(policyinfoPushData.value)

}

const detail = async (id) => {
    await policyNumberAPI(id)
    router.push({ path: `/detail`, query: { id } })
}

onMounted(async () => {
    const res = await policystatusAPI()
    policyStatus.value = res.data

    await getPolicyNewData()
    await getPolicyHotData()
    await getPolicyPushData()
    await getpolicyEffectiveData()
})
const searchFun = () => {
    setKeyWordCookie(searchData.value)
    router.push({ path: `/policy` })
}

const toPolicyModelFun = async () => {
    const info = getCookie()
    if (!info) {

        return
    }
    await policyMatchNAPI()
    router.push("/policyModel")
}


const toPolicyModelYuCeFun = async () => {
    const info = getCookie()
    if (!info) {

        return
    }
    await policyMatchNAPI()
    router.push("/policyModelYuCe")
}
const currentYear = ref(null);
currentYear.value = new Date().getFullYear();

const moreInfo = () => {
    router.push("/policy")
}
</script>

<template>
    <div id="main">
        <HomeHeader></HomeHeader>
        <div class="banner">
            <img width="100%" src="../assets/images/banner.jpg" alt="">
            <div class="policySearch">
                <div class="info">
                    <p>政策信息查询</p>
                    <p>汇集最新行业政策,智能解读匹配,政策支持早知道</p>
                </div>

                <div class="search">
                    <el-icon style="font-size: 1.5vw; color:#ccc;margin:0 1vw 0 1vw">
                        <Search />
                    </el-icon>
                    <input id="username" type="text" placeholder="请输入政策名称" v-model="searchData" />
                    <button class="btn" @click="searchFun">智能搜索</button>
                </div>

            </div>
            <div class="policyOverview">

                <div class="policyOverview_show">
                    <img src="../assets/images/政策数量.png" alt="">
                    <div>
                        <p>
                            <font>{{ policyStatus.policyN }}</font>条
                        </p>
                        <p>政策库</p>
                    </div>
                </div>

                <div class="policyOverview_show">
                    <img src="../assets/images/用户访问.png" class="policyOverview_img" alt="">
                    <div>
                        <!-- <p><font >{{ policyStatus.userVisitN}}万+</font>次</p> -->
                        <p>
                            <font>{{  policyStatus.userVisitN }}</font>次
                        </p>
                        <p>用户访问</p>
                    </div>
                </div>

                <div class="policyOverview_show">
                    <img src="../assets/images/企业用户.png" class="policyOverview_img" alt="">
                    <div>
                        <p>
                            <font>{{ policyStatus.companyN }}</font>个
                        </p>
                        <p>企业用户</p>
                    </div>
                </div>

                <div class="policyOverview_show">
                    <img src="../assets/images/政策智能匹配.png" class="policyOverview_img" alt="">
                    <div>
                        <p>
                            <font>{{ policyStatus.matchN }}</font>次
                        </p>
                        <p>政策智能匹配</p>
                    </div>
                </div>

                <div class="policyOverview_show">
                    <img src="../assets/images/企业申报数量.png" class="policyOverview_img" alt="">
                    <div>
                        <p>
                            <font>{{ policyStatus.declarationN }}</font>次
                        </p>
                        <p>企业申报数量</p>
                    </div>
                </div>

            </div>
        </div>

        <div class="policy_new">
            <div class="policy_left">
                <div class="newP">
                    <h1>热度推荐</h1>
                    <p @click="moreInfo" style="cursor: pointer;">更多>></p>
                </div>
                <div class="el-card " v-for="(item, index) in policyinfoHotData" :key="index">
                    <h5 @click="detail(item.id)">{{ item.name }}</h5>
                    <p>
                        <span>牵头部门：</span><span>{{ item.leaderDepartment }}</span>
                        <span style="margin-left: 0.5vw;">发布时间：</span><span>{{ item.publishDate.split('T')[0] }}</span>
                    </p>
                    <div >
                        <el-tag class="el-tag" type="danger">{{ lastDays(item.expireDate) }}</el-tag>
                        <el-tag style="margin-left: 1vw;">{{ item.policyType ? item.policyType:"暂无政策类型"}}</el-tag>
                        <!-- {{ item.isMoney ? "资金支持" : "无资金支持" }} -->
                    </div>
                </div>

            </div>

            <div class="policy_right">
                <div class="newP">
                    <h1>最新政策</h1>
                    <p @click="moreInfo" style="cursor: pointer;">更多>></p>
                </div>
                <div class="el-card " v-for="(item, index) in policyinfoNewData" :key="index">
                    <h5 @click="detail(item.id)">{{ item.policyName }}</h5>
                    <p>
                        <span>牵头部门：</span><span>{{ item.leaderDepartment }}</span>
                        <span style="margin-left: 0.5vw;">发布时间：</span><span>{{ item.publishDate.split('T')[0] }}</span>
                    </p>
                    <div>
                        <el-tag type="danger">{{ lastDays(item.expireDate) }}</el-tag>
                        <el-tag style="margin-left: 1vw;">{{ item.policyType ? item.policyType:"暂无政策类型"}}</el-tag>
                        <!-- {{ item.isMoney ? "资金支持" : "无资金支持" }} -->
                    </div>
                </div>
            </div>
        </div>
        <div class="policy_new policy_new_extra">
            <div class="policy_left">
                <div class="newP">
                    <h1>精准推送政策</h1>
                    <p @click="moreInfo" style="cursor: pointer;">更多>></p>
                </div>
                <div class="el-card" v-for="(item, index) in policyinfoPushData" :key="index">
                    <h5 @click="detail(item.id)">{{ item.policyName }}</h5>
                    <p>
                        <span>牵头部门：</span><span>{{ item.leaderDepartment }}</span>
                        <span style="margin-left: 0.5vw;">发布时间：</span><span>{{ item.publishDate.split('T')[0] }}</span>
                    </p>
                    <div>
                        <el-tag type="danger">{{ lastDays(item.expireDate) }}</el-tag>
                        <el-tag style="margin-left: 1vw;">{{ item.policyType ? item.policyType:"暂无政策类型"}}</el-tag>
                        <!-- {{ item.isMoney ? "资金支持" : "无资金支持" }} -->
                    </div>
                </div>
            </div>
            <div class="policy_right">
                <div class="newP">
                    <h1>可申报政策</h1>
                    <p @click="moreInfo" style="cursor: pointer;">更多>></p>
                </div>
                <div class="el-card " v-for="(item, index) in policyEffectiveData" :key="index">
                    <h5 @click="detail(item.id)">{{ item.policyName }}</h5>
                    <p>
                        <span>牵头部门：</span><span>{{ item.leaderDepartment }}</span>
                        <span style="margin-left: 0.5vw;">发布时间：</span><span>{{ item.publishDate.split('T')[0] }}</span>
                    </p>
                    <div>
                        <el-tag type="danger">{{ lastDays(item.expireDate) }}</el-tag>
                        <el-tag style="margin-left: 1vw;">{{ item.policyType ? item.policyType:"暂无政策类型"}}</el-tag>
                        <!-- {{ item.isMoney ? "资金支持" : "无资金支持" }} -->
                    </div>
                </div>
            </div>
        </div>


        <div class="hotpolicy">
            <div class="hotpolicy_header">
                <h3>热门评价模型</h3>
                <p>海量项目申报查询 &nbsp;&nbsp;&nbsp;&nbsp; 一对一政策精准评价</p>
            </div>

            <div class="enterprise">
                <div>
                    <img class="model_img" src="../assets/images/模型0.png" alt="">
                    <h4>{{ currentYear }}科技型中小企业</h4>
                    <span class="txt">浙江省科学技术厅</span>
                    <span class="enterprise_text" @click="toPolicyModelFun" style="cursor: pointer;">
                        立即自评</span>
                </div>
                <div>
                    <img class="model_img" src="../assets/images/模型1.png" alt="">
                    <h4>{{ currentYear }}高新技术企业</h4>
                    <span class="txt">浙江省科学技术厅</span>
                    <span class="enterprise_text" @click="toPolicyModelFun" style="cursor: pointer;">
                        立即自评</span>
                </div>
                <div>
                    <img class="model_img" src="../assets/images/模型2.png" alt="">
                    <h4>{{ currentYear }}国家企业技术中心</h4>
                    <span class="txt">浙江省科学技术厅</span>
                    <span class="enterprise_text" @click="toPolicyModelFun" style="cursor: pointer;">
                        立即自评</span>
                </div>

                <div>
                    <img class="model_img" src="../assets/images/模型3.png" alt="">
                    <h4>{{ currentYear }}创新型中小企业</h4>
                    <span class="txt">浙江省经济和信息化厅</span>
                    <span class="enterprise_text" @click="toPolicyModelFun" style="cursor: pointer;">
                        立即自评</span>
                </div>

                <div>
                    <img class="model_img" src="../assets/images/模型4.png" alt="">
                    <h4>{{ currentYear }}专精特新中小企业</h4>
                    <span class="txt">浙江省经济和信息化厅</span>
                    <span class="enterprise_text" @click="toPolicyModelFun" style="cursor: pointer;">
                        立即自评</span>
                </div>

                <div>
                    <img class="model_img" src="../assets/images/模型5.png" alt="">
                    <h4>{{ currentYear }}专精特新小巨人企业</h4>
                    <span class="txt">浙江省经济和信息化厅</span>
                    <span class="enterprise_text" @click="toPolicyModelFun" style="cursor: pointer;">
                        立即自评</span>
                </div>
            </div>
        </div>
        <div class="hotpolicy">
            <div class="hotpolicy_header">
                <h3>热门预测模型</h3>
                <p>海量项目申报查询 &nbsp;&nbsp;&nbsp;&nbsp; 一对一政策精准预测</p>
            </div>

            <div class="enterprise">
                <div>
                    <img class="model_img" src="../assets/images/模型0.png" alt="">
                    <h4>{{ currentYear + 1 }}科技型中小企业</h4>
                    <span class="txt">浙江省科学技术厅</span>
                    <span class="enterprise_text" @click="toPolicyModelYuCeFun" style="cursor: pointer;">
                        立即自评</span>
                </div>
                <div>
                    <img class="model_img" src="../assets/images/模型1.png" alt="">
                    <h4>{{ currentYear + 1 }}高新技术企业</h4>
                    <span class="txt">浙江省科学技术厅</span>
                    <span class="enterprise_text" @click="toPolicyModelYuCeFun" style="cursor: pointer;">
                        立即自评</span>
                </div>
                <div>
                    <img class="model_img" src="../assets/images/模型2.png" alt="">
                    <h4>{{ currentYear + 1 }}国家企业技术中心</h4>
                    <span class="txt">浙江省科学技术厅</span>
                    <span class="enterprise_text" @click="toPolicyModelYuCeFun" style="cursor: pointer;">
                        立即自评</span>
                </div>

                <div>
                    <img class="model_img" src="../assets/images/模型3.png" alt="">
                    <h4>{{ currentYear + 1 }}创新型中小企业</h4>
                    <span class="txt">浙江省经济和信息化厅</span>
                    <span class="enterprise_text" @click="toPolicyModelYuCeFun" style="cursor: pointer;">
                        立即自评</span>
                </div>

                <div>
                    <img class="model_img" src="../assets/images/模型4.png" alt="">
                    <h4>{{ currentYear + 1 }}专精特新中小企业</h4>
                    <span class="txt">浙江省经济和信息化厅</span>
                    <span class="enterprise_text" @click="toPolicyModelYuCeFun" style="cursor: pointer;">
                        立即自评</span>
                </div>

                <div>
                    <img class="model_img" src="../assets/images/模型5.png" alt="">
                    <h4>{{ currentYear + 1 }}专精特新小巨人企业</h4>
                    <span class="txt">浙江省经济和信息化厅</span>
                    <span class="enterprise_text" @click="toPolicyModelYuCeFun" style="cursor: pointer;">
                        立即自评</span>
                </div>
            </div>
        </div>
        <Footer/>
    </div>
</template>

<style scoped>
input:-internal-autofill-previewed,
input:-internal-autofill-selected {
    transition: background-color 15000s ease-out 10s;
}

input::placeholder {
    font-size: 0.8vw;
    /* 设置占位符文字大小为18像素 */
    color: #909090;
    /* 设置占位符文字颜色 */
}

.banner {
    width: 100%;
    position: relative;
    z-index: -999;

}

.banner .policySearch {
    position: absolute;
    display: flex;
    flex-direction: column;
    width: 38vw;
    height: 12vw;
    justify-content: space-around;

    left: 12.5%;
    top: 18%;
    z-index: 9999;
}


.banner .policySearch .info {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    height: 4vw;

}

.policySearch .info p:first-child {
    font-size: 1.8vw;
    font-weight: bold;
}

.policySearch .info p:last-child {
    font-size: 1vw;
    font-weight: lighter;
    color: grey;
}

.policySearch .search {
    display: flex;
    align-items: center;
    width: 38vw;
    height: 3.5vw;

    background-color: white;
    border-radius: 0.5vw;
}

.policySearch #username {

    width: 28vw;
    outline: none;
    border: none;
    height: 3vw;
    background-color: white;
}

.policySearch .btn {
    width: 6vw;
    height: 3vw;
    font-size: 1vw;
    outline: none;
    border: none;
    border-radius: 0.5vw;
    background: linear-gradient(45deg, #3580F5, #5FA2FD);
    color: white;
    cursor: pointer;
}

.banner .policyOverview {
    width: 75%;
    height: 8vw;
    position: absolute;
    display: flex;
    justify-content: space-around;
    align-items: center;
    left: 50%;
    bottom: 0;
    transform: translate(-50%, 0);
    background-color: rgba(255, 255, 255, 0.5);

}



.policyOverview_show {
    display: flex;
}

.policyOverview_show img {
    width: 3vw;
}

.policyOverview_show div {
    margin-left: 1vw;
    font-size: 0.9vw;
}

.policyOverview_show div font {
    font-size: 1.4vw;
    color: #3276FD;
}

.policy_new {
    display: flex;
    width: 75%;
    justify-self: center;
    justify-content: space-between;

    margin: 0 auto;
    margin-top: 5vw;

}

.policy_new_extra {
    margin-top: 2.5vw;
}

.policy_left {
    width: 49%;
}

.policy_right {
    width: 49%;
}

.newP {
    margin-top: 0.5vw;
    height: 5vw;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-items: center;
    justify-content: space-around;

}

.newP h1 {
    font-size: 1.5vw;

}

.newP p {
    text-align: end;
    font-size: 0.8vw;
    color: #6B9EF7;
}


.el-card {
    display: flex;
    flex-direction: column;
    width: 100%;
    margin-top: 1vw;
    padding: 1vw;
}

.el-card h5 {
    display: flex;
    /* flex-wrap: wrap; */
    cursor: pointer;
    font-size: 1.2vw;
    width: 100%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;

}

.el-card span {
    font-size: 0.8vw;
    font-weight: normal;
}

.el-card div {
    width: 100%;
    height: 2vw;
    margin-bottom: 0.5vw;
    text-align: end
}

.el-card div .el-tag {
    width:7vw;
    padding: 0 1.5vw;
    font-size: 0.8vw;
    overflow-x: hidden;
    height: 1.3vw;
}


.hotpolicy {
    margin-top: 5vw;
    background-color: white;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-items: center;
    align-items: center;
    padding: 2vw;
    
}


.hotpolicy .hotpolicy_header {
    display: flex;
    height: 5vw;
    flex-direction: column;
    justify-content: center;

    align-items: center;
}

.hotpolicy .hotpolicy_header p {
    font-size: 0.8vw;
    font-weight: lighter;
    margin-top: 1vw;
}

.hotpolicy h3 {
    font-size: 1.8vw;
}

.hotpolicy .enterprise {
    width: 100%;
    display: flex;
    justify-content: space-around;
    padding-top: 1vw;
}

.enterprise div {
    width: 14%;
    height: 15vw;
    margin-top: 2vw;
    position: relative;
    display: flex;
    flex-direction: column;
    justify-content: end;
    align-items: center;
    background-color: white;
    background-image: url("../assets/images/框.png");
    background-size: cover;
}

.enterprise div h4 {
    font-size: 1vw;
    font-weight: normal;
}

.enterprise div span {
    margin: 1vw 0;
}

.enterprise .txt {
    font-size: 0.8vw;
}

.model_img {
    width: 12vw;
    height: 12vw;
    position: absolute;
    left: 50%;
    top: -4vw;
    transform: translate(-50%, 0);
}

.enterprise_text {
    display: block;
    width: 7vw;
    height: 1.8vw;
    font-size: 1vw;
    text-align: center;
    background-color: #0d9deb;
    border-radius: 1vw;
    color: white;
    line-height: 1.8vw;
}


</style>
