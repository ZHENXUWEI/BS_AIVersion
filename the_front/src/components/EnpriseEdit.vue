<script setup>
import { ref, onMounted } from "vue"
import { entNatureDataAPI, postEnterpriseDataAPI, enterpriseDataAPI } from '@/api/service';
import { ElMessage } from 'element-plus'
import { getCookie } from '@/utils/cookie'
import zhCn from "element-plus/es/locale/lang/zh-cn";
import { getMonth } from "date-fns";
const locale = zhCn;
const users = ref({});
const checkedValues = ref([]);
checkedValues.value = ["获得省级以上质量奖荣誉", "拥有自主品牌"]
const optionsWHSP = ref([])
const optionsXz = ref([])
//
const optionsYFJGJB = ref([])
const optionsCPLY = ref([])
const is_own_provincial_pride = ref(false)
const is_own_iso = ref(false)
const is_revision_standards = ref(false)
const is_own_brand = ref(false)


const formData = ref({})
const checkData = ref({})
const infoData = ref({})
const idData = ref({})
formData.value = {
  //公司名称
  "companyName": "aaaa",
  // 研究与试验发展经费支出
  "research_expense": "aaa",
  // 详细地址
  "detailAdd": "aaa",
  // 认证状况
  "accreditation_status": "aaa",
  // 主营业务收入
  "main_income": 111,
  // 知识产权数量
  "number": "4",
  // 2022销售收入
  "2022_sales_revenue": 111,
  // 企业经认定科技成果数
  "technological_achievement_number": 11,
  // 截至目前企业科技人员数
  "utn_scientific_number": 111,
  // 几项核心业务采用信息系统支撑
  "how_many_system": 11,
  // 产品通过发达国家和地区产品认证（国际标准协会行业认证）数:
  "international_Standards_Certifications": 11,
  "is_key_position_in_chain": 1,
  // 科技人员数
  "scientific_number": 11,
  // 2023资产总额
  "2023_asset": 11,
  // 2022负债总额
  "2022_liability": 11,
  // 知识产权获得方式
  "method": 11,
  // 行业系数
  "hangYe_xiShu": 11,
  "is_sqev": 1,
  "params": {},
  // 注册时间
  "establishDate": "2024-11-04",
  // 2022新增股权总额
  "2022_new_equity": 11,
  // 企业所属特定细分市场
  "entDetailMarket": 11,
  // 联系方式
  "phone": 11,
  // 2023新增股权总额
  "2023_new_equity": 11,
  // 在职员工总数
  "employee_number": 11,
  // 新产品销售收入
  "new_income": 1,
  // 满足以下任意一项：1.自建或与高等院校、科研机构联合建立研发机构；2.设立技术研究院、企业技术中心、企业工程中心、院士专家工作站、博士后工作站等。
  "built_or_set": 11,
  // 主导产品在全国细分市场占有率大于10%
  "lead_business_more10%": 11,
  // 企业技术开发仪器设备原值
  "instruments_original": 1,
  // 2023研发开发费用总额
  "2023_develop_expense": 11,
  "leading_product_market_share": 11,
  "is_own_provincial_pride": true,
  // 软件著作数
  "software_number": 11,
  // 当年被受理的发明专利申请数
  "invention_application_number": 11,
  // 创新直通条件
  "innovation_through_condition": 1,
  // 制定了企业研究开发的组织管理制度，建立了研发投入核算体系，编制了研发费用辅助账
  "manage_system": 11,
  // I类知识产权II类知识产权
  "intellectual_property_rights": 11,
  // 研究与试验发展人员数
  "research_experiment_number": 11,

  "USCC": 11,
  // 通过国家(国际组织)认证的实验室和检测机构数
  "laboratory_insitution_number": 11,
  "is_own_iso": true,
  "is_revision_standards": true,
  // 实用新型专利
  "utility_patent": 11,
  // 当年被受理的专利申请数
  "application_patent_number": 11,
  // 质量管理水平
  "manage_level": [3.2, 5.3, 6.8, 8.7],
  // 登记机关
  "reqAuthority": 11,
  // 对企业产品（服务）在技术上发挥核心支持作用
  "product": 11,
  // 企业所在地
  "preAddress": ["浙江省", "温州市", "洞头区"],
  // 最近三年主持和参加制定的国际、国家和行业标准数
  "standard_number": 11,
  // 企业从事细分市场年数大于3年
  "enterprise_business_more3": 11,
  // 发明专利数
  "invention_number": 11,
  "updateTime": 11,
  // 主导产品所属领域情况
  "lead_field_circumstance": 11,
  "direct_market_brand_advantage": 111,
  // 主导产品所属领域指标
  "product_domain_metrics": 11,
  "institution_level": 11,
  "national_top_five": 1,
  "basic_applied_number": 11,
  "total_project_number": 1,
  "createTime": 11,
  "provencial_research_number": 1,
  "2023_main_income": 11,
  "2022_main_income": 11,
  "total_profit": 11,
  "2022_profit ": 11,
  "oneClass": 1,
  "enterprise_ability": 1,
  "invention_patent_number": 11,
  "proprietary_recognized_number": 11,
  "innovation_top_500": 1,
  "new_profit": 11,
  "2023_enterprise_income": 11,
  "company_involve_number": 11,
  "new_equity_financing": 11,
  "oneSelfClass": 11,
  "I_valuable_number": 111,
  "national_research_number": 11,
  "2021_new_equity": 11,
  "tech": "10",
  "2022_enterprise_income": 11,
  "2022_asset": 11,
  "provincial_top_three": 1,
  "enterprise_nature": 1,
  "businessScope": 1,
  "cTaxAuthority": 1,
  "2023_profit": 1,
  "utn_employee_number": 1,
  "condition": 1,
  "reward_system": 1,
  "I_research_number": 1,
  "enterprise_business": 1,
  "digitalCulLevel": 1,
  // 企业性质
  "entNature": "国有独资",
  "remark": 1,
  "I_intelligent_more2": 1,
  "belong_field": 1,
  "platform": 1,
  "entDetailYear": 1,
  // 获得以下任何荣誉，且在有效期内
  "is_own_brand": true,
  "award": 1,

  "updateBy": 1,
  "twoClass": 1,
  "legalPerson": "小周",
  "2023_liability": 1,
  "class": "com.ruoyi.web.controller.company.domain.Company",
  "registered_number": 1,
  "right_number": 1,
  "oneValueClass": 1,
  "2022_develop_expense": 1,
  "lead_filed": 1,
  "technological_progress_awards": 1,
  "companyType": 1,
  "specification": 1,
  // 企业规模
  "ent_guiMo": 1,
  // 企业所属行业
  "entBelongs": "邮政业",
  "enterprise_business_year": 1,
  "external_expert_number": 1,
  "createBy": 1,
  "expert_doctor_number": 1,
  "research_institution": 1,
  "detailAdd": 1,
  "science_technology_awards": 1,
  "2023_sales_revenue": 1,
  "searchValue": 1,
  "bLicenseUrl": 1,
  "is_idr": 1
}

formData.value = [
  { key: 'name', name: '姓名213213', type: 'text', value: '' },
  { key: 'name', name: '统一社会信用代码建立开放式的创新创业平台', type: 'text', value: '' },
  { key: 'name', name: '公司id', type: 'text', value: '' },
  { key: 'name', name: '姓名213', type: 'text', value: '' },
  { key: 'age', name: '年龄1231', type: 'text', value: '' },
  { key: 'preAddress', name: '地址21321', type: 'text', value: [] },
  { key: 'establishDate', name: '成立时间', type: 'date', value: '' },
  { key: 'gender', name: '性别', type: 'single', value: '', options: ['男', '女'] },
  { key: 'xr', name: 'hhh', type: 'single', value: '', options: ['男', '女'] },
  { key: 'hobbies', name: '建立了科技成果转化的组织实施与激励奖励制度，建立开放式的创新创业平台', type: 'multiple', value: [], options: ['读书', '旅行', '运动'] },
  { key: 'hobbies', name: '建立了科技成果转化的组织实施与激励奖励制度', type: 'multiple', value: [], options: ['bb', 'cc', 'dd'] },
  {
    key: 'city', name: '城市', type: 'region', value: '北京', options: [
      { label: '北京', value: '北京' },
      { label: '上海', value: '上海' },
      { label: '广州', value: '广州' }
    ]
  }
]




const valueDATE = ref([])
const handleChange = (value) => {
  console.log(value)
}

// valueDATE.value=formData.value.establishDate

const timeFun = (valueDATE) => {
  console.log(valueDATE)

  infoData.value.establishDate = valueDATE
}


// 企业所属行业

const getQiYeXingZhiData = async () => {
  const res = await entNatureDataAPI()
  optionsBelongs.value = res
}

// 获取回显数据
const getHuiXiangFun = async (obj) => {
  //reduce绑定数据

  let currentYear=obj.year
  const res = await enterpriseDataAPI(obj)

  // upData.value = res.data
  formData.value = res.data

  //对应年份数据
  formData.value=formData.value.filter(item => {
     const firstFourChars = item.name.substring(0, 4);
     return firstFourChars === currentYear.toString() || !/^\d{4}$/.test(firstFourChars);
});
  console.log(formData.value)
  //地址转换
  // let preAddress = formData.value[4].value.slice(1, -1).split(', ')
  // formData.value[4].value = preAddress
  //  console.log(6666,formData.value)
  //单多选绑定
  // is_own_provincial_pride.value = formData.value.is_own_provincial_pride == 1.0 ? true : false
  // is_own_iso.value = formData.value.is_own_iso == 1.0 ? true : false
  // is_revision_standards.value = formData.value.is_revision_standards == 1.0 ? true : false
  // is_own_brand.value = formData.value.is_own_brand == 1.0 ? true : false

  // checkData.value.forEach(item => {
  //   // 多选类型需要数组
  //   if (item.type === 'multiple') {
  //     formData[item.key] = item.value ? item.value.split(', ') : [];
  //     console.log(formData.value[item.key])

  //   } else {
  //     formData[item.key] = item.value;
  //   }
  // });
  idData.value= formData.value.filter(item=> item.key=='id')
  checkData.value = formData.value.filter(item => item.type === 'single' || item.type === 'multiple' &&item.key!=='id') ;
  infoData.value = formData.value.filter(item => item.type !== 'single' && item.type !== 'multiple' &&item.key!=='id');
  infoData.value.forEach(item => {
    if (item.type === 'region') {
      if (!item.value) {
        // value 为 null 或 undefined 时，赋空数组
        item.value = [];
      } else if (typeof item.value === 'string') {
        item.value = item.value.split(';');
      }
    }

  })
  checkData.value.forEach(item => {
    if (item.type == 'multiple') {
      if (!item.value) {
        // value 为 null 或 undefined 时，赋空数组
        item.value = [];
      }
      else {
        // 去掉末尾的分号
        let cleanedInput = item.value.endsWith(';') ? item.value.slice(0, -1) : item.value;
        // 使用逗号分割字符串
        let resultArray = cleanedInput.split(', ');
        console.log(resultArray)
        item.value= resultArray
      }
    }
    if (item.type === 'single' && item.value != null) {
      item.value = item.value.replace(/;$/, '');
      console.log(item.value)
    }
    // else if(item.type='single'){
    //   if (!item.value) {
    //     // value 为 null 或 undefined 时，赋空数组
    //     item.value = 0;
    //   }
    // }
  })


  // console.log(formData.value.provincial_top_three) 
}




// getQiYeXingZhiData()

// 企业性质数据

// const valueXz = ref('集体企业')

optionsXz.value = [
  {
    value: '国有独资',
    label: '国有独资',
  },
  {
    value: '国有控股',
    label: '国有控股',
  },
  {
    value: '集体企业',
    label: '集体企业',
  },
  {
    value: '私营企业',
    label: '私营企业',
  },
  {
    value: '股份制',
    label: '股份制',
  },
  {
    value: '不属于以上分类',
    label: '不属于以上分类',
  },
]


const optionsBs = ref([])
optionsBs.value = [
  {
    value: '农、林、牧、渔业',
    label: '农、林、牧、渔业',
  },
  {
    value: '工业',
    label: '工业',
  },
  {
    value: '建筑业',
    label: '建筑业',
  },
  {
    value: '零售业',
    label: '零售业',
  },
  {
    value: '批发业',
    label: '批发业',
  },
  {
    value: '交通运输业',
    label: '交通运输业',
  },
  {
    value: '邮政业',
    label: '邮政业',
  },
  {
    value: '仓储业',
    label: '仓储业',
  },
  {
    value: '其他',
    label: '其他',
  },
]
// 省市县三级联动数据
// const value = ref([])
const optionSSX = ref([])
// value.value=["浙江省","杭州市","西湖区"]
optionSSX.value = [
  {
    value: '浙江省',
    label: '浙江省',
    children: [
      /* 杭州市 */
      {
        value: '杭州市',
        label: '杭州市',
        children: [
          { value: '上城区',    label: '上城区' },
          { value: '拱墅区',    label: '拱墅区' },
          { value: '西湖区',    label: '西湖区' },
          { value: '滨江区',    label: '滨江区' },
          { value: '萧山区',    label: '萧山区' },
          { value: '余杭区',    label: '余杭区' },
          { value: '临平区',    label: '临平区' },
          { value: '钱塘区',    label: '钱塘区' },
          { value: '富阳区',    label: '富阳区' },
          { value: '临安区',    label: '临安区' },
          { value: '桐庐县',    label: '桐庐县' },
          { value: '淳安县',    label: '淳安县' },
          { value: '建德市',    label: '建德市' }
        ]
      },
      /* 宁波市 */
      {
        value: '宁波市',
        label: '宁波市',
        children: [
          { value: '海曙区',    label: '海曙区' },
          { value: '江北区',    label: '江北区' },
          { value: '镇海区',    label: '镇海区' },
          { value: '北仑区',    label: '北仑区' },
          { value: '鄞州区',    label: '鄞州区' },
          { value: '奉化区',    label: '奉化区' },
          { value: '象山县',    label: '象山县' },
          { value: '宁海县',    label: '宁海县' },
          { value: '余姚市',    label: '余姚市' },
          { value: '慈溪市',    label: '慈溪市' }
        ]
      },
      /* 温州市 */
      {
        value: '温州市',
        label: '温州市',
        children: [
          { value: '鹿城区',    label: '鹿城区' },
          { value: '龙湾区',    label: '龙湾区' },
          { value: '瓯海区',    label: '瓯海区' },
          { value: '洞头区',    label: '洞头区' },
          { value: '永嘉县',    label: '永嘉县' },
          { value: '平阳县',    label: '平阳县' },
          { value: '苍南县',    label: '苍南县' },
          { value: '文成县',    label: '文成县' },
          { value: '泰顺县',    label: '泰顺县' },
          { value: '瑞安市',    label: '瑞安市' },
          { value: '乐清市',    label: '乐清市' },
          { value: '龙港市',    label: '龙港市' }
        ]
      },
      /* 嘉兴市 */
      {
        value: '嘉兴市',
        label: '嘉兴市',
        children: [
          { value: '南湖区',    label: '南湖区' },
          { value: '秀洲区',    label: '秀洲区' },
          { value: '嘉善县',    label: '嘉善县' },
          { value: '海盐县',    label: '海盐县' },
          { value: '海宁市',    label: '海宁市' },
          { value: '平湖市',    label: '平湖市' },
          { value: '桐乡市',    label: '桐乡市' }
        ]
      },
      /* 湖州市 */
      {
        value: '湖州市',
        label: '湖州市',
        children: [
          { value: '吴兴区',    label: '吴兴区' },
          { value: '南浔区',    label: '南浔区' },
          { value: '德清县',    label: '德清县' },
          { value: '长兴县',    label: '长兴县' },
          { value: '安吉县',    label: '安吉县' }
        ]
      },
      /* 绍兴市 */
      {
        value: '绍兴市',
        label: '绍兴市',
        children: [
          { value: '越城区',    label: '越城区' },
          { value: '柯桥区',    label: '柯桥区' },
          { value: '上虞区',    label: '上虞区' },
          { value: '新昌县',    label: '新昌县' },
          { value: '诸暨市',    label: '诸暨市' },
          { value: '嵊州市',    label: '嵊州市' }
        ]
      },
      /* 金华市 */
      {
        value: '金华市',
        label: '金华市',
        children: [
          { value: '婺城区',    label: '婺城区' },
          { value: '金东区',    label: '金东区' },
          { value: '武义县',    label: '武义县' },
          { value: '浦江县',    label: '浦江县' },
          { value: '磐安县',    label: '磐安县' },
          { value: '兰溪市',    label: '兰溪市' },
          { value: '义乌市',    label: '义乌市' },
          { value: '东阳市',    label: '东阳市' },
          { value: '永康市',    label: '永康市' }
        ]
      },
      /* 衢州市 */
      {
        value: '衢州市',
        label: '衢州市',
        children: [
          { value: '柯城区',    label: '柯城区' },
          { value: '衢江区',    label: '衢江区' },
          { value: '常山县',    label: '常山县' },
          { value: '开化县',    label: '开化县' },
          { value: '龙游县',    label: '龙游县' },
          { value: '江山市',    label: '江山市' }
        ]
      },
      /* 舟山市 */
      {
        value: '舟山市',
        label: '舟山市',
        children: [
          { value: '定海区',    label: '定海区' },
          { value: '普陀区',    label: '普陀区' },
          { value: '岱山县',    label: '岱山县' },
          { value: '嵊泗县',    label: '嵊泗县' }
        ]
      },
      /* 台州市 */
      {
        value: '台州市',
        label: '台州市',
        children: [
          { value: '椒江区',    label: '椒江区' },
          { value: '黄岩区',    label: '黄岩区' },
          { value: '路桥区',    label: '路桥区' },
          { value: '三门县',    label: '三门县' },
          { value: '天台县',    label: '天台县' },
          { value: '仙居县',    label: '仙居县' },
          { value: '温岭市',    label: '温岭市' },
          { value: '临海市',    label: '临海市' },
          { value: '玉环市',    label: '玉环市' }
        ]
      },
      /* 丽水市 */
      {
        value: '丽水市',
        label: '丽水市',
        children: [
          { value: '莲都区',    label: '莲都区' },
          { value: '青田县',    label: '青田县' },
          { value: '缙云县',    label: '缙云县' },
          { value: '遂昌县',    label: '遂昌县' },
          { value: '松阳县',    label: '松阳县' },
          { value: '云和县',    label: '云和县' },
          { value: '庆元县',    label: '庆元县' },
          { value: '景宁畲族自治县', label: '景宁畲族自治县' },
          { value: '龙泉市',    label: '龙泉市' }
        ]
      }
    ]
  }
];


// 文化水平数据
// const valueWHSP = ref('三级及以上')

optionsWHSP.value = [
  {
    value: '三级及以上',
    label: '三级及以上',
  },
  {
    value: '二级',
    label: '二级',
  },
  {
    value: '一级',
    label: '一级',
  }
]



optionsCPLY.value = [
  {
    value: '在产业链供应链关键环节及关键领域"补短板""锻长板""填空白"取得实际成效',
    label: '在产业链供应链关键环节及关键领域"补短板""锻长板""填空白"取得实际成效',
  },
  {
    value: '属于工业"六基"领域、中华老字号名录或企业主导产品服务关键产业链重点龙头企业',
    label: '属于工业"六基"领域、中华老字号名录或企业主导产品服务关键产业链重点龙头企业',
  },
  {
    value: '不属于以上情况',
    label: '不属于以上情况',
  }
]

optionsYFJGJB.value = [
  {
    value: '国家级',
    label: '国家级',
  },
  {
    value: '省级',
    label: '省级',
  },
  {
    value: '市级',
    label: '市级',
  },
  {
    value: '市级以下',
    label: '市级以下',
  },
  {
    value: '未建立研发机构',
    label: '未建立研发机构',
  }
]


const currentYear = ref(null);
currentYear.value = new Date().getFullYear();
const yearList = ref([])
yearList.value = [
  currentYear.value + 5,
  currentYear.value + 4,
  currentYear.value + 3,
  currentYear.value + 2,
  currentYear.value + 1,
  currentYear.value,
  currentYear.value - 1,
  currentYear.value - 2,
  currentYear.value - 3,
  currentYear.value - 4
]
const activeName = ref('1')
const activeIndex = ref(0)
const Year = ref(currentYear);

const changeYear = (year) => {
  activeName.value = '1'
  Year.value = year
  // console.log(Year.value)
  getHuiXiangFun({ id: users.value.id, year: Year.value })//{ id: users.value.id, year: Year.value }
}

onMounted(async () => {
  const info = getCookie()
  if (info) {
    users.value = info
  }
  // console.log(111111,Year.value)
  getHuiXiangFun({ id: users.value.id, year: Year.value })//{ id: users.value.id, year: Year.value }
  activeIndex.value = 5

})

// 提交数据

const submitFun = async () => {
  formData.value = [...infoData.value, ...checkData.value, ...idData.value]

  formData.value.forEach(item => {
    if (item.type == 'multiple') {
      if (Array.isArray(item.value) && item.value.length === 0) {
        // value 为 null 或 undefined 时，赋空数组
        item.value = null;
      }
      else {
        let x = item.value.join(", ");
        item.value = x
        console.log(item.value)
      }

    }
    if (item.type == 'date') {
      const date = new Date(item.value)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      item.value = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      console.log(item.value)
    }
    // else if(item.type='single'){
    //   if (!item.value) {
    //     // value 为 null 或 undefined 时，赋空数组
    //     item.value = 0;
    //   }
    // }
  })


  formData.value = formData.value.map(item => {
    if (item.type === 'region' && Array.isArray(item.value)) {
      return { ...item, value: item.value.join(';') };
    }
    return item;
  });
  console.log(formData.value)
  const info = getCookie()
  if (info) {
    users.value = info
  }
  formData.value.key = users.value.id //users.value.id
  formData.value.id = users.value.id//users.value.id
  formData.value.year = Year




  // console.log(is_own_provincial_pride,is_own_iso,is_revision_standards,is_own_brand)
  // formData.value.is_own_provincial_pride = is_own_provincial_pride.value ? 1 : 0
  // formData.value.is_own_iso = is_own_iso.value ? 1 : 0
  // formData.value.is_revision_standards = is_revision_standards.value ? 1 : 0
  // formData.value.is_own_brand = is_own_brand.value ? 1 : 0
  // console.log(55555, formData.value)
  // const Data=[formData.value]
  // console.log(Data)
  // console.log(formData.value.establishDate)
  // const date = new Date(formData.value.establishDate)
  // const year = date.getFullYear()
  // const month = String(date.getMonth() + 1).padStart(2, '0');
  // const day = String(date.getDate()).padStart(2, '0');
  // const hours = String(date.getHours()).padStart(2, '0');
  // const minutes = String(date.getMinutes()).padStart(2, '0');
  // const seconds = String(date.getSeconds()).padStart(2, '0');
  // formData.value.establishDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  // console.log(formData.value.establishDate)
  // //测试
  // // formData.value.utn_scientific_number= Math.floor(7.0).toString(), // 转换为整数并转为字符串
  // // console.log(formData.value.utn_scientific_number)

  //更新数据
  // formData.value[4].value = formData.value[4].value.join(", ")
  const res = await postEnterpriseDataAPI(formData.value)
  console.log(res)

  if (res.status == 0) {
    // ElMessage.success("提交成功")
    ElMessage({
      message: '提交成功',
      type: 'success',
      offset: window.screen.height
    });
    console.log(Year.value)
    getHuiXiangFun({ id: users.value.id, year: Year.value })//{ id: users.value.id, year: Year.value }
  } else {
    ElMessage({
      message: '提交失败',
      type: 'error',
      offset: window.screen.height
    });
  }
}
</script>

<template>
  <div id="main">
    <div class="content_head">
      <span v-for="(item, index) in yearList" :key="index" :class="{ active: activeIndex === index }"
        @click="changeYear(item), activeIndex = index">
        {{ item }}年
      </span>
    </div>

    <div class="submitForm">
      <el-form :model="infoData">
        <el-row :gutter="20" justify="center" style="width:100%;align-items: center"> <!-- 设置列间距 -->
          <el-col v-for="(item, index) in infoData" :key="item.key" :span="12"
            :class="{ 'info-left': index % 2 == 0, 'info-right': index % 2 == 1 }">
            <!-- 每列占一半宽度 style="margin-bottom: 1.5vh;display: flex; justify-content: space-around" -->
            <el-scrollbar><el-form-item size="default">
                <el-tooltip :content="item.name" placement="top">
                  <template #title>
                    {{ item.name }}
                  </template>
                  <span class="custom-label">{{ item.name }}</span>
                </el-tooltip>
                <!-- 文本输入 -->
                <el-input v-if="item.type === 'text' && !item.options " v-model="item.value"
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

                <!-- 下拉选择
                <el-select v-else-if="item.type === 'select'" v-model="item.value" placeholder="请选择">
                  <el-option v-for="opt in item.options || []" :key="opt" :label="opt" :value="opt" />
                </el-select> -->

                <!-- 其他类型可扩展 -->
                <span v-else>暂不支持该类型</span>
              </el-form-item>
            </el-scrollbar>
          </el-col>
        </el-row>
      </el-form>
      <el-form :model="checkData">
        <el-form-item size="default">
          <el-row :gutter="20" justify="left" style="width:100%"> <!-- 设置列间距 -->
            <el-col v-for="item in checkData" :key="item.key" :span="24" style="margin-bottom: 1vh;margin-left: 3vw;">
              <label style="height: 32px;font-size: var(--el-form-label-font-size);
        line-height: 32px; color: var(--el-text-color-regular);">{{ item.name }}</label><br>
              <!-- 单选 -->
              <el-radio-group v-if="item.type === 'single'" v-model="item.value">
                <el-radio v-for="opt in item.options || []" :value="opt" :key="opt" :label="opt">{{ opt }}</el-radio>
              </el-radio-group>

              <!-- 多选 -->
              <el-checkbox-group v-else-if="item.type === 'multiple'" v-model="item.value">
                <el-checkbox v-for="opt in item.options || []" :key="opt" :label="opt">{{ opt }}</el-checkbox>
              </el-checkbox-group>
            </el-col>
          </el-row> <!-- 每列占一半宽度 -->
        </el-form-item>
      </el-form>
    </div>

    <span class="edit_button" @click="submitFun">提交</span>
    <!--  <span class="edit_button" @click="submitFun">{{ activeName == "4" ? "提交" : "下一步" }} </span> -->
  </div>

</template>

<style scoped>
.info-left {
  margin-bottom: 1.5vh;
  display: flex !important;
  justify-content: space-around
}

.info-right {
  margin-bottom: 1.5vh;
  display: flex !important;
  justify-content: space-between
}

.custom-label {

  justify-content: center;
  box-sizing: border-box;
  display: inline-flex;
  flex: 0 0 auto;
  height: auto;
  font-size: var(--el-form-label-font-size);
  line-height: 20px;
  color: var(--el-text-color-regular);
  /* 隐藏超出部分 */
  text-overflow: ellipsis;
  /* 显示省略号 */
  width: 10vw;
  margin-right: 1vw;
  /* 保持固定宽度 */
}

.align-right {
  width: 50%;
  display: flex;
  justify-content: flex-start;
  /* 水平居左对齐 */
  align-items: center;
  /* 垂直居中对齐 */
}

.el-form-item__label {
  white-space: wrap;
  /* 允许换行 */
  word-break: break-word;
  /* 长单词换行 */
  text-align: right;
}



.submitForm {
  background-color: white;
  padding: 3vh;
  width: 100%;
}

.col-center {
  display: flex;
}

.el-tabs {

  width: 100%;
}

#main {
  display: flex;
  width: 100%;
  flex-direction: column;
  margin-top: 0.5vw;
  align-items: center;
  justify-content: center;
  margin-left: 1vw;
}

#content {
  display: flex;

  flex-direction: column;
  margin-top: 1vw;
  align-items: center;
  justify-content: center;
  align-self: center;
}

#main h1,
#main h3 {
  margin: 0 0.25vw;
  padding: 0;
}

::v-deep .select1 {
  display: block !important;
  width: 14vw !important;
  height: 2vw;

}

::v-deep .select2 {
  display: block !important;
  width: 14vw !important;
  height: 2vw !important;

}

::v-deep .el-input {
  margin: 0 !important;
  padding: 0 !important;

}


#main .attr_box {
  display: flex;
  flex-direction: column;
  width: 100%;
  justify-content: center;
}

.attr_box h3 {
  font-size: 0.9vw;
}

.attr_box_content {
  display: flex;
  justify-content: space-between;
}

.attr_box_content h3 {
  width: 15vw;
  display: flex;
  justify-content: end;
  padding-right: 1vw;

  font-size: 1vw;

}

.attr_box_content #left,
.attr_box_content #right {
  width: 100%;
}

.attr_box_content #left .item,
.attr_box_content #right .item {
  display: flex;
  justify-content: space-between;
  margin-top: 1vw;
  align-items: center;
}

/*科研创新能力*/
.keyan_box_content {
  display: flex;
  justify-content: space-between;

}

.keyan_box_content h3 {
  width: 15vw;
  font-size: 1vw;

  display: flex;
  justify-content: end;
}

.keyan_box_content #left,
.keyan_box_content #right {
  display: flex;
  flex-direction: column;

}



.keyan_box_content #left div,
.keyan_box_content #right div {
  display: flex;
  justify-content: space-between;
  margin-top: 1vw;
  align-items: center;
}


/*知识产权*/
.zs_box_content {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.zs_box_content h3 {
  margin-top: 1vh;
}

.padding_left {
  /* padding: 1vw 2vw; */
}

.zs_box_content .with_box div {
  display: flex;
  align-items: center;
  justify-content: space-around;
}

.zs_box_content .with_box div h3 {
  width: 15vw !important;
  display: flex;
  justify-content: end;
  font-size: 1vw;

}

.text_one {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1vw 13vw;
}

.text_one div {
  display: flex;
  align-items: center;
  width: 100%;
}

.zs_box_content .padding_left {
  display: flex;

  width: 100%;
}


.zs_box_content .padding_left #left,
.zs_box_content .padding_left #right {
  display: flex;
  flex-direction: column;

}

.zs_box_content #left div,
.zs_box_content #right div {
  display: flex;
  justify-content: space-between;
  margin-top: 1vw;
  align-items: center;

}


.area_two,
.area_three {
  padding: 1vw 2vw;
}

.area_one,
.area_two,
.area_three {
  display: flex;
  flex-direction: column;
  margin-top: 1vw;

}

#main .area_one {
  width: 100%;
  display: flex;
  justify-content: center;
  /* align-items: center; */

}


.area_one h3 {
  width: 15vw;
  display: flex;
  justify-content: end;
  align-items: center;
}

.area_one>div {
  margin-top: 1vw;
  display: flex;
  /* justify-content: space-between; */
}

.edit_button {
  width: 8vw;
  height: 2vw;
  display: block;
  display: flex;
  justify-content: center;
  align-items: center;

  background-color: rgb(8, 154, 227);
  cursor: pointer;
  border-radius: 1vh;
  color: white;
  margin-top: 1vw;
  margin-bottom: 1vw;
  font-size: 1vw;
}

.content_head {
  background-color: white;
  width: 100%;
  height: 3vw;
  margin-bottom: 1vw;
  display: flex;
  align-items: center;
}

.content_head span {

  padding: 0.4vw 1.5vw;
  font-size: 1vw;
  cursor: pointer;
  border-radius: 1vw;
}

.active {
  background: linear-gradient(45deg, #3580F5, #5FA2FD);
  color: white;
}
</style>
