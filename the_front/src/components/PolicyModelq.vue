<script setup>
import { ref, onMounted } from "vue";
import { getCookie } from "@/utils/cookie";
import {
  policyModelAPI,
  policyModelUnqualified,
  postEnterpriseDataAPI,
} from "@/api/service";
import { useRouter } from "vue-router";
import { format } from "date-fns";
const router = useRouter();

// 查看详情对话框表格数据

const DiaTableData = ref([]);
const tableDataLeft = ref([]);
const tableDataRight = ref([]);
const users = ref({});
const N = ref(0);
const getPolicyModelData = async (obj) => {
  const res = await policyModelAPI(obj);
  console.log(res);
  // tableData.value=res.data
  var len = res.data.length;
  if (len % 2 == 0) {
    N.value = len / 2;
    tableDataLeft.value = res.data.slice(0, N.value);
    tableDataRight.value = res.data.slice(N.value);
    console.log(tableDataLeft.value);
  } else {
    N.value = Math.floor(len / 2) + 1;
    tableDataLeft.value = res.data.slice(0, N.value);
    tableDataRight.value = res.data.slice(N.value);
  }
  // console.log(res.data.length)
};

onMounted(() => {
  const info = getCookie();
  if (info) {
    users.value = info;
  }

  getPolicyModelData({ id: users.value.id }); //{id:users.value.id}
  // toSumbDataFun()
});

const isDaBiao = (obj) => {
  if (obj == undefined) return "";
  else {
    if (obj.length == 0) {
      return "已达标";
    } else {
      return "未达标";
    }
  }
};

const isShenBao = (obj) => {
  if (obj == undefined) return "";
  else {
    if (obj.length == 0) {
      return "历史未申报";
    } else {
      return "历史已申报";
    }
  }
};

const isVisible = ref(false);
const isSubmit = ref(false);
const DaBiaoStr = ref("");
const QiYeStr = ref("");
const LiShiStr = ref("");
const ZT = ref("");
const DBStatus=ref('')

// const toSearchDetail=(obj1,obj2)=>{
//   if(obj1==undefined||obj2==undefined) return
//   isVisible.value=true
//   LiShiStr.value=""
//   DaBiaoStr.value=""
//   QiYeStr.value=""
//   ZT.value=isDaBiao(obj1)+"/"+isShenBao(obj2)
//   if(isDaBiao(obj1)=="未达标"){
//     DaBiaoStr.value=`${obj1[0].expectIndicator}`
//     QiYeStr.value=`${obj1[0].yourIndicator}`
//   }
//   if(isShenBao(obj2)=="历史已申报"){
//     // console.log(111)
//     var str='企业历史申报的时间：'
//     for(var i=0;i<obj2.length;i++){

//       // console.log(111,)
//       str+=format(Date(obj2[i]), 'yyyy-MM-dd')+" "
//       // console.log(obj2)
//     }
//     LiShiStr.value=str
//   }
//   DiaTableData.value.push(ZT.value, DaBiaoStr.value, QiYeStr.value, LiShiStr.value)
//   console.log(DiaTableData.value);

//   // return {DaBiaoStr,LiShiStr}
// }

const modelName = ref("");
const centerDialogVisible = ref(false);
const showMore = (item) => {
  var obj1 = item.indicatorName;
  var obj2 = item.history;
  if (obj1 == undefined || obj2 == undefined) return;
  centerDialogVisible.value = true;
  modelName.value = item.modelName;
  LiShiStr.value = "";
  DaBiaoStr.value = "";
  QiYeStr.value = "";
  ZT.value = isDaBiao(obj1) + "/" + isShenBao(obj2);
  if (isDaBiao(obj1) == "未达标") {
    DaBiaoStr.value = `${obj1[0].expectIndicator}`;
    QiYeStr.value = `${obj1[0].yourIndicator}`;
  }
  if (isShenBao(obj2) == "历史已申报") {
    // console.log(111)
    var str = "企业历史申报的时间：";
    for (var i = 0; i < obj2.length; i++) {
      // console.log(111,)
      str += format(Date(obj2[i]), "yyyy-MM-dd") + " ";
      // console.log(obj2)
    }
    LiShiStr.value = str;
  }
  DiaTableData.value.push(
    ZT.value,
    DaBiaoStr.value,
    QiYeStr.value,
    LiShiStr.value
  );
};

// 跳转到提交材料页面
const isVisble = ref(false);
const toSumbMaterialFun = (str) => {
  if (str == "未达标") {
    isVisble.value = true;
    return;
  }
  router.push("/applyMaterials");
};
const unqualData = ref([]);

const isVisble2 = ref(false);

const toSumbDataFun = async (id,idb) => {
  let obj = { id: users.value.id, policyId: id };
  const res = await policyModelUnqualified(obj);
  unqualData.value = res.data;
  console.log(res);
  DBStatus.value= isDaBiao(idb) == "未达标"? "补全资料": "修改资料"
  isVisble2.value = true;
};

const submitUn = async () => {
  let obj = ref([
    {
      name: "公司id",
      key: "id",
      value: users.value.id,
      type: "text",
      year: 2025,
      options: null,
    },
  ]);
  const mergedData = [...obj.value, ...unqualData.value];
  console.log(mergedData);
  let res = await postEnterpriseDataAPI(mergedData);
  console.log(res);
  isVisble2.value = false;
};
</script>
<template>
  <div id="main">
    <h1>政策评价模型</h1>
    <div id="content">
      <div class="policy_left">
        <div class="el_card" v-for="(item, index) in tableDataLeft" :key="index">
          <div class="el_card_top">
            <div class="first">
              <img src="../assets/images/模型.png" alt="" style="margin-right: 0.5vw" />
              <span>{{ item.modelName }}</span>
            </div>
            <div class="second">
              <span @click="toSumbDataFun(item.modelId,item.indicatorName)">{{
                isDaBiao(item.indicatorName) == "未达标"
                  ? "补全资料"
                  : "修改资料"
              }}</span>
              <span style="
                  margin-left: 0.3vw;
                  background: linear-gradient(45deg, #1fb456, #35c97f);
                " @click="toSumbMaterialFun(isDaBiao(item.indicatorName))">提交材料</span>
            </div>
          </div>
          <div class="el_card_bottom">
            <span>
              <font class="dabiao" :class="{
                activedabiao: isDaBiao(item.indicatorName) == '未达标',
              }">
                {{ isDaBiao(item.indicatorName) }}</font>
              <font class="suxian">|</font>
              <font class="shenbao" :class="{
                activeshenbao: isShenBao(item.history) == '历史已申报',
              }">
                {{ isShenBao(item.history) }}</font>
            </span>
            <span style="cursor: pointer" @click="showMore(item)">查看更多></span>
          </div>
        </div>
      </div>

      <div class="policy_right">
        <div class="el_card" v-for="(item, index) in tableDataRight" :key="index">
          <div class="el_card_top">
            <div class="first">
              <img src="../assets/images/模型.png" alt="" style="margin-right: 0.5vw" />
              <span>{{ item.modelName }}</span>
            </div>

            <div class="second">
              <span @click="toSumbDataFun(item.modelId)">{{
                isDaBiao(item.indicatorName) == "未达标"
                  ? "补全资料"
                  : "修改资料"
              }}</span>
              <span style="
                  margin-left: 0.3vw;
                  background: linear-gradient(45deg, #1fb456, #35c97f);
                " @click="toSumbMaterialFun(isDaBiao(item.indicatorName))">提交材料</span>
            </div>
          </div>
          <div class="el_card_bottom">
            <span>
              <font class="dabiao" :class="{
                activedabiao: isDaBiao(item.indicatorName) == '未达标',
              }">
                {{ isDaBiao(item.indicatorName) }}</font>
              <font class="suxian">|</font>
              <font class="shenbao">{{ isShenBao(item.history) }}</font>
            </span><span style="cursor: pointer" @click="showMore(item)">查看更多></span>
          </div>
        </div>
      </div>
    </div>
  </div>
  <el-dialog style="width: 50%" title="政策状态详情" v-model="centerDialogVisible" :close-on-click-modal="false">
    <div class="main_center">
      <div class="modelName_div">
        <div class="title">
          <img src="../assets/images/模型.png" alt="" style="margin-right: 0.5vw" />
          <span>{{ modelName }}</span>
        </div>
        <div class="caozuo">
          <span class="shenbao" style="margin-right: 0" @click="toSumbDataFun">
            {{ ZT.split("/")[0] == "未达标" ? "补全资料" : "修改资料" }}
          </span>
          <span class="shenbao" @click="toSumbMaterialFun(ZT.split('/')[0])" style="
              margin-left: 0.3vw;
              background: linear-gradient(45deg, #1fb456, #35c97f);
            ">提交材料</span>
        </div>
      </div>
      <div class="upload">
        <p>
          您的政策状态为:<span style="margin-left: 0.5vw">
            <font class="dabiao" :class="{ activedabiao: ZT.split('/')[0] == '未达标' }">
              {{ ZT.split("/")[0] }}
            </font>
            <font class="suxian">/</font>
            <font class="shenbao" :class="{ activeshenbao: ZT.split('/')[1] == '历史已申报' }">
              {{ ZT.split("/")[1] }}
            </font>
          </span>
        </p>
        <p v-if="DaBiaoStr">
          该政策模型期望的指标条件:<span style="color: red; margin-left: 0.5vw">{{ DaBiaoStr }}</span>
        </p>
        <p v-if="QiYeStr">
          企业真实指标条件:<span style="color: red; margin-left: 0.5vw">{{
            QiYeStr
          }}</span>
        </p>
        <p v-if="LiShiStr">
          <span>{{ LiShiStr }}</span>
        </p>
      </div>
    </div>
  </el-dialog>

  <el-dialog style="width: 50%" v-model="isVisble" :close-on-click-modal="false">
    <div class="main_center">
      <div class="modelName_div" style="border: none">
        <div class="title">贵企业未达标，请补全数据!</div>
        <span class="shenbao" @click="isVisble = false">确定</span>
      </div>
    </div>
  </el-dialog>

  <el-dialog style="width: 50%" v-model="isVisble2" :title=DBStatus :close-on-click-modal="false" :before-close="handleClose">
    <div class="main_center">
      <div class="ql" style="border: none">
        <div class="title">
          <div style="height: 25vw">
            <el-scrollbar>
              <div style="height: 100">
                <el-row :gutter="20" v-for="item in unqualData" :key="item" style="align-items: center;">
                  <el-col :span="12">
                    <!-- 占据 50% 的宽度 -->
                    <h3 >{{ item.name }}</h3>
                  </el-col>
                  <el-col :span="12">
                    <!-- 占据 50% 的宽度 -->
                    <!-- 文本输入 -->
                    <el-input v-if="item.type === 'text' && !item.options" v-model="item.value"
                      :placeholder="'请输入' + item.name" style="width: 15vw;" />
                    <el-select v-else-if="item.type === 'text' && item.options" v-model="item.value" placeholder="请选择"
                      style="width:  15vw;">
                      <el-option v-for="opt in item.options" :key="opt" :label="opt" :value="opt" />
                    </el-select>
                    <!-- 地址 -->
                    <el-cascader v-else-if="item.type == 'region'" style="width: 15vw; height: 2vw" class="cascader"
                      placeholder="请选择符合的一项" v-model="item.value" :options="optionSSX" @change="handleChange" />
                    <!-- 成立时间 -->
                    <el-config-provider v-else-if="item.type == 'date'" :locale="locale">
                      <el-date-picker style="width: 15vw; height: 2vw" type="date" v-model="item.value"
                        placeholder="Pick a day" @change="timeFun" format="YYYY/MM/DD HH:mm:ss" />
                    </el-config-provider>
                    <!-- 单选 -->
                    <el-radio-group v-if="item.type === 'single'" v-model="item.value">
                      <el-radio v-for="opt in item.options || []" :value="opt" :key="opt" :label="opt">{{ opt
                        }}</el-radio>
                    </el-radio-group>

                    <!-- 多选 -->
                    <el-checkbox-group v-else-if="item.type === 'multiple'" v-model="item.value">
                      <el-checkbox v-for="opt in item.options || []" :key="opt" :label="opt">{{ opt }}</el-checkbox>
                    </el-checkbox-group>
                  </el-col>
                </el-row>
              </div>
            </el-scrollbar>
          </div>
          <!-- <div class="item">
                    <h3>企业规模:</h3>
                    <el-input v-model="formData.ent_guiMo" style="width: 14vw;height:2vw" type="text"/>
                  </div> -->
        </div>
      </div>
      <!-- <span class="submit" @click="isVisble2=false">取消</span> -->
      <span class="submit" @click="submitUn">确定</span>
    </div>
  </el-dialog>

  <!-- /////////////// -->
</template>

<style scoped>
.ql {
  width: 100%;
  padding: 0 3vw;
  margin: 0 auto;
  align-items: center;
  justify-content: space-between;
  border: 0.1vw solid #ccc;
  border-radius: 0.5vw;
  padding: 0.6vw;
  background-color: #fdfdff;
}

.el-row {
  margin-top: 1vh;
}

.submit {
  display: flex;
  /* display: inline-block; */
  align-items: center;

  padding: 0.5vw 0.8vw;
  background: linear-gradient(45deg, #3880f8, #5ca3ff);
  font-size: 0.8vw;
  border-radius: 1vw;
  color: white;
  cursor: pointer;
}

.item {
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  gap: 10px;
}

#main {
  display: flex;
  width: 100%;
  height: 50vw;
  flex-direction: column;
  align-items: center;
  background-color: white;
  margin-left: 1vw;
}

#main h1 {
  width: 100%;
  padding: 2vw 0 2vw 2vw;
  font-size: 1.4vw;
  font-weight: bold;
}

#content {
  display: flex;
  width: 100%;
  justify-self: center;
  justify-content: space-between;
  margin: 0 auto;

  padding: 0 2vw;
}

.policy_left {
  width: 49%;
}

.policy_right {
  width: 49%;
}

.el_card {
  border: 0.2vw solid white;
  border-radius: 0.8vw;
  width: 100%;
  height: 8.2vw;
  border: 0.1vw solid #ccc;
  margin-bottom: 0.5vw;
  background-color: #fafbff;
}

.el_card_top {
  width: 100%;
  height: 6vw;
  display: flex;
  background-color: #fafbff;
  align-items: center;
  justify-content: space-between;
  padding: 1vw;
  border-radius: 0.8vw;
}

.el_card_top img {
  width: 2.8vw;
  height: 3vw;
}

.el_card_top .first {
  display: flex;
  align-items: center;
}

.el_card_top .first span:last-child {
  font-size: 1.1vw;
  font-weight: bold;
}

.el_card_top .second {
  display: flex;
  align-items: center;
}

.el_card_top .second span {
  cursor: pointer;
  display: block;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  width: 5vw;
  height: 2vw;
  font-size: 0.6vw;
  border-radius: 1vw;
  background: linear-gradient(45deg, #3880f8, #5ca3ff);
}

.el_card_bottom {
  display: flex;
  width: 100%;
  justify-content: space-between;
  height: 2vw;
  align-items: center;
  font-size: 0.8vw;
  padding: 0 1vw;
  background-color: #f4f5fa;
  border-radius: 0 0 0.8vw 0.8vw;
}

.main_center {
  width: 100%;
  padding: 1vw;
  padding-top: 0;
  margin: 0 auto;
  display: flex;
  flex-direction: column;

  background-color: white;
  align-items: center;
}

.modelName_div {
  width: 100%;
  padding: 0 3vw;
  display: flex;
  margin: 0 auto;
  align-items: center;
  justify-content: space-between;
  border: 0.1vw solid #ccc;
  border-radius: 0.5vw;
  padding: 0.6vw;
  background-color: #fdfdff;
}

.modelName_div .title {
  display: flex;
  align-items: center;
}

.modelName_div .title span {
  font-size: 1.1vw;
  font-weight: bold;
}

.modelName_div img {
  width: 2.5vw;
  height: 2.5;
}

.modelName_div .caozuo {
  display: flex;
}

.modelName_div .shenbao {
  display: flex;
  display: block;
  align-items: center;

  padding: 0.5vw 0.8vw;
  background: linear-gradient(45deg, #3880f8, #5ca3ff);
  font-size: 0.8vw;
  border-radius: 1vw;
  color: white;
  cursor: pointer;
}

#main .upload {
  margin-top: 2vw;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

#main .upload p {
  margin-bottom: 1vw;
  font-size: 1vw;
}

.suxian {
  color: #ccc;
  margin: 0 0.5vw;
}

.dabiao {
  color: #01be60;
}

.shenbao {
  color: gray;
}

.activeshenbao {
  color: #4087f9 !important;
}

.activedabiao {
  color: red !important;
}
</style>
