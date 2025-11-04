<template>
  <div class="policy_hobby">
    <h3 style="font-size: 1vw;padding-top: 1vw;">政策喜好设置</h3>
    <form class="hobbyForm" ref="formRef">
      <div :key="item.id" v-for="(item, index) in hobbyData.categories" class="formItem">
        <span style="text-align: left;margin-right: 0.5vw;width: 4vw;">{{ item.name }}：</span>
        <div class="formItemCheck">
          <div :key="i" v-for="(subItem, i) in item.children" class="itemChecks">
            <input 
              type="checkbox"
              :id="`checkbox-${index}-${i}`"
              :checked="item.selectedIndex.has(i)"
              :value="subItem" 
              @change="handleChange(subItem, index, i)" 
              style="margin-right: 0.2vw;width: 0.8vw;height: 0.8vw;margin-top: 4px;"
            >
            <label :for="`checkbox-${index}-${i}`">{{ subItem }}</label>
          </div>
        </div>
      </div>

      <!-- 修改后的区县选择项 -->
      <div v-if="showDistrictSelector && selectedCities.length > 0" class="formItem">
        <span style="text-align: left;margin-right: 0.5vw;width: 4vw;">区县：</span>
        <div class="districts-wrapper"  >
          <div>
            <div v-for="city in selectedCities" :key="city" class="city-district-group" >
            <div class="districts-container" v-if="getDistrictsByCity(city).length">
              <div class="city-label">{{ city }}：</div> 
              <div style="display: flex;">
                <div v-for="(district, di) in getDistrictsByCity(city)" :key="di" class="itemChecks">      
                <input 
                  type="checkbox"
                  :id="`district-${city}-${di}`"
                  :checked="isDistrictSelected(city, district)"
                  @change="handleDistrictChange(district, city)"
                  style="margin-right: 0.2vw;width: 0.8vw;height: 0.8vw;margin-top: 4px;"
                >
                <label :for="`district-${city}-${di}`">{{ district }}</label>
              </div>
              </div>
            </div>
          </div>
          </div>
        </div>
      </div>

      <div style="text-align: left;margin-top: 1vw;">
        <el-button type="primary" @click="submit" :disabled="isSubmit">提交</el-button>
        <el-button type="info" @click="resetEvent">重置</el-button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { getCookie } from '@/utils/cookie'
import { policyclassinfoAPI, policyhobbyAPI } from '@/api/policy'
import { ElMessage } from 'element-plus'

const users = ref({});
const hobbyData = reactive({})
let datalist = []
const showDistrictSelector = ref(true);
const currentCityDistricts = ref(['']);
const currentCityName = ref('');
const selectedDistrictIndex = ref(0);
const flag = ref(true)
const info = getCookie()
if(info){
    users.value=info
}
let formData = []
const formRef = ref(null)
const selectedCities = ref([]);
const selectedDistricts = ref({}); // 改为对象形式存储 { "城市-区县": true }

const handleChange = (subitem, index, i) => {
  let currentCategory = datalist[index];
  let selectedSet = currentCategory.selectedIndex;
  
  if (selectedSet.has(i)) {
    selectedSet.delete(i);
  } else {
    if (subitem === "全部") {
      currentCategory.selectedIndex = new Set([i]);
      // 当选择全部时，区县选项改为未选中状态
      Object.keys(selectedDistricts.value).forEach(key => {
        selectedDistricts.value[key] = false
      });
    } else {
      const allOptionIndex = currentCategory.children.findIndex(item => item === "全部");
      if (allOptionIndex !== -1 && selectedSet.has(allOptionIndex)) {
        selectedSet.delete(allOptionIndex);
      }
      selectedSet.add(i);
    }
  }
  
  if (currentCategory.name === '适用区域') {
    if (selectedSet.has(i)) {
      if (!selectedCities.value.includes(subitem)) {
        selectedCities.value.push(subitem);
      }
    } else {
      const index = selectedCities.value.indexOf(subitem);
      if (index !== -1) {
        selectedCities.value.splice(index, 1);
      }
    }
  }
};

// 获取某个城市下的所有区县

const getDistrictsByCity = (city) => {
  const areaCategory = hobbyData.categories.find(item => item.name === '适用区域');
  
  if (!areaCategory?.subChildren) return [];
  const cityData = areaCategory.subChildren.find(item => item.name === city);
  return cityData?.children || [];
};

// 检查区县是否被选中
const isDistrictSelected = (city, district) => {
  
  return selectedDistricts.value[`${city}-${district}`];
};

// 处理区县选择变化
const handleDistrictChange = (district, city) => {
  const key = `${city}-${district}`;
  if (selectedDistricts.value[key]) {
    delete selectedDistricts.value[key];
  } else {
    selectedDistricts.value[key] = true;
  }
  // 触发响应式更新
  selectedDistricts.value = {...selectedDistricts.value};
};

const getPolicyHobby = async (obj) => {
  const res = await policyclassinfoAPI()
  if (res.status == 0) {
    hobbyData.categories = res.data.map(item => ({
      ...item,
      selectedIndex: new Set()
    }))
    datalist = hobbyData.categories
    getPolicyHobbyInfo({companyId:userId.value})
  }
}

const hobbyInfo = ref([])

const getPolicyHobbyInfo = async (obj) => {
  const res = await policyhobbyAPI({obj, method:'GET'})
  if (res.status == 0) {
    hobbyInfo.value = res.data
    setCheckedStatus(hobbyInfo.value);
  }
}

const setCheckedStatus = (hobbyInfo) => {
  hobbyData.categories.forEach(category => {
    category.selectedIndex.clear();
    hobbyInfo.forEach(hobby => {
      if (category.name === hobby.name) {
        hobby.children.forEach(preference => {
          const index = category.children.findIndex(child => child === preference);
          if (index !== -1) {
            category.selectedIndex.add(index);
          }
        });
        
        // 设置区县选择状态
        if (category.name === '适用区域' && hobby.subChildren) {
          selectedCities.value = hobby.subChildren.map(city => city.name);
          hobby.subChildren.forEach(city => {
            city.children.forEach(district => {
              selectedDistricts.value[`${city.name}-${district}`] = true;
            });
          });
        }
      }
    });
  });
};

const userId = ref(null)

onMounted(() => {
  const info = getCookie()
  if(info){
    userId.value=info.id
  }
  getPolicyHobby({companyId:userId.value})
})

const submit = async (e) => {
  e.preventDefault()
  isSubmit.value = true
  let arr = []
  
  datalist.forEach(item => {
    if (item.selectedIndex.size > 0) {
      item.selectedIndex.forEach(i => {
        arr.push(item.children[i])
      })
    }
    
    if (item.name === '适用区域') {
      // 按城市组织区县数据
      const subChildrenData = selectedCities.value.map(city => {
        const districtsForCity = Object.keys(selectedDistricts.value)
          .filter(key => key.startsWith(`${city}-`))
          .map(key => key.replace(`${city}-`, ''));
        
        return {
          name: city,
          children: districtsForCity
        };
      }).filter(city => city.children.length > 0);
      
      formData.push({
        id: item.id,
        name: item.name,
        children: arr,
        subChildren: subChildrenData,
        updateTime: dateFormat(new Date)
      });
    } else {
      formData.push({
        id: item.id,
        name: item.name,
        children: arr,
        updateTime: dateFormat(new Date)
      });
    }
    arr = []
  })
  
  const obj = {
    companyId: userId.value,
    formData
  }

  formData = []
  const res = await policyhobbyAPI({obj, method:'POST'})
  if (res.status == 0) {
    ElMessage.success('提交成功!')
    getPolicyHobbyInfo({companyId:userId.value})
  } else {
    ElMessage.error('提交失败!')
  }
  datalist.forEach(item => {
    item.selectedIndex = new Set()
  })
}

const resetEvent = () => {
  isSubmit.value = false
  datalist.forEach(item => {
    item.selectedIndex = new Set()
  })
  selectedDistricts.value = {}
  selectedCities.value = []
  formRef.value.reset()
}

const dateFormat = (date) => {
  let year = date.getFullYear()
  let month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
  let day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
  return `${year}-${month}-${day}`
}

const isSubmit = ref(false)
</script>

<style scoped>
.policy_hobby {
  width: 100%;
  text-align: center;
  background-color: #fff;
}
.hobbyForm {
  font-size: 0.8vw;
  margin-top: 1vw;
  padding: 0 1vw 1vw 5vw;
}
.formItem {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 5px;
}
.formItemCheck {
  flex: 3;
  display: flex;
  flex-wrap: wrap;
}
.itemChecks {
  display: flex;
  margin-right: 10px;
  margin-bottom: 10px;
}
.el-button {
  width: 3vw;
  height: 2vw;
  font-size: 0.7vw;
}

/* 新增区县相关样式 */
.districts-wrapper {
  flex: 3;
  display: flex;
  flex-direction: column;
}
.city-district-group {
  margin-bottom: 10px;
}
.city-label {
  font-weight: bold;
  text-align: left;
  margin-bottom: 5px;
}
/* .districts-container {
  display: flex;
  flex-wrap: wrap;
} */
</style>