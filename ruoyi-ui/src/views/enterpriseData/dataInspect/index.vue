<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="企业名称" prop="companyName">
        <el-input
          v-model="queryParams.companyName"
          placeholder="请输入企业名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="法人" prop="legalPerson">
        <el-input
          v-model="queryParams.legalPerson"
          placeholder="请输入法人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          v-hasPermi="['enterpriseData:dataInspect:query']"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleCompanyAdd"
          v-hasPermi="['enterpriseData:dataInspect:add']"
          >新增</el-button
        >
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdateCompany"
          v-hasPermi="['system:companyProperty:edit']"
          >修改</el-button
        >
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDeleteCompanyBatch"
          v-hasPermi="['system:companyProperty:remove']"
          >删除</el-button
        >
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:companyProperty:export']"
          >导出</el-button
        >
      </el-col> -->
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="companyList"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="公司名称" align="center" prop="companyName" />
      <el-table-column label="法人" align="center" prop="legalPerson" />
      <el-table-column label="地址" align="center" prop="address" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="getCompanyDetail(scope.row.id)"
            v-hasPermi="['enterpriseData:dataInspect:list']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            @click="handleUpdateCompany(scope.row)"
            v-hasPermi="['enterpriseData:dataInspect:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            @click="handleDeleteCompany(scope.row)"
            v-hasPermi="['enterpriseData:dataInspect:remove']"
            >删除</el-button
          >
          <el-button
            size="mini"
            type="text"
            @click="handleHobbyManage(scope.row)"
            v-hasPermi="['enterpriseData:hobby:list']"
            >喜好管理</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改company_property对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="openPropertyData"
      width="500px"
      append-to-body
    >
      <el-form
        ref="formPropertyData"
        :model="formPropertyData"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="分类属性" prop="name" v-if="isAdd">
          <el-autocomplete
            v-model="property"
            class="inline-input"
            :fetch-suggestions="querySearchAsyncProperty"
            placeholder="请输入内容"
            @select="handleSelectProperty"
          >
            <template slot-scope="{ item }">
              <div class="value">{{ item.value }}</div>
            </template>
          </el-autocomplete>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-autocomplete
            v-model="formPropertyData.content"
            class="inline-input"
            :fetch-suggestions="querySearchAsyncContent"
            placeholder="请输入内容"
            @select="handleSelectContent"
          >
            <template slot-scope="{ item }">
              <div class="value">{{ item.value }}</div>
            </template>
          </el-autocomplete>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitPropertyDataForm"
          >确 定</el-button
        >
        <el-button @click="cancel('property')">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改company_indicator对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="openIndicatorData"
      :before-close="handleAddClose"
      width="500px"
      append-to-body
    >
      <el-form
        ref="formIndicatorData"
        :model="formIndicatorData"
        :rules="rules"
        label-width="80px"
      >
        <!-- <el-form-item label="指标名称" prop="name">
          <el-input
            v-model="formIndicatorData.name"
            placeholder="请输入指标名称"
          />
        </el-form-item> -->
        <!-- <el-form-item label="英译名称" prop="key">
          <el-input
            v-model="formIndicatorData.key"
            placeholder="请输入英译名称"
          />
        </el-form-item> -->

        <el-form-item label="指标名称" prop="name" v-if="this.isAdd">
          <el-autocomplete
            v-model="indicator"
            class="inline-input"
            :fetch-suggestions="querySearchAsyncIndicator"
            placeholder="请输入内容"
            @select="handleSelectIndicator"
          >
            <template slot-scope="{ item }">
              <div class="value">{{ item.value }}</div>
            </template></el-autocomplete
          >
        </el-form-item>
        <el-form-item label="指标阈值" prop="threshold">
          <el-input
            v-model="formIndicatorData.threshold"
            placeholder="请输入指标阈值"
          />
        </el-form-item>
        <el-form-item label="年份" prop="year">
          <el-input v-model="formIndicatorData.year" placeholder="请输入年份" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitIndicatorDataForm"
          >确 定</el-button
        >
        <el-button @click="cancel('indicator')">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改company对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="openCompany"
      width="500px"
      append-to-body
    >
      <el-form
        ref="formCompany"
        :model="formCompany"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="公司名称" prop="companyName">
          <el-input
            v-model="formCompany.companyName"
            placeholder="请输入公司名称"
          />
        </el-form-item>
        <el-form-item label="公司法人" prop="legalPerson">
          <el-input
            v-model="formCompany.legalPerson"
            placeholder="请输入公司法人名称"
          />
        </el-form-item>
        <el-form-item label="公司地址" prop="address">
          <el-input
            v-model="formCompany.address"
            placeholder="请输入公司地址"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCompanyForm">确 定</el-button>
        <el-button @click="cancel('company')">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 二级菜单 -->
    <el-dialog
      title="详细企业数据"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose"
    >
      <div slot="title" class="dialog-title">
        <el-row>
          <el-col :span="1.5">
            <span class="dialog-title-text" style="padding-right: 50px"
              >详细企业数据</span
            >
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handlePropertyDataAdd"
              style=""
              v-hasPermi="['enterpriseData:companyProperty:add']"
              >新增属性</el-button
            >
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleIndicatorDataAdd"
              style=""
              v-hasPermi="['enterpriseData:companyIndicator:add']"
              >新增指标</el-button
            >
          </el-col>
        </el-row>
      </div>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="属性" name="property">
          <div class="propertyTable">
            <el-table
              :data="companyPropertyList"
              height="250"
              border
              style="width: 100%"
            >
              <el-table-column
                type="index"
                width="50"
                label="序号"
                align="center"
              >
              </el-table-column>
              <el-table-column prop="name" label="属性" align="center">
              </el-table-column>
              <el-table-column prop="content" label="内容" align="center">
              </el-table-column>
              <el-table-column
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
              >
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    @click="handleUpdateCompanyPropertyData(scope.row)"
                    v-hasPermi="['enterpriseData:companyProperty:edit']"
                    >修改</el-button
                  >
                  <el-button
                    size="mini"
                    type="text"
                    @click="handleDeletePropertyData(scope.row)"
                    v-hasPermi="['enterpriseData:companyProperty:remove']"
                    >删除</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="指标" name="indicator">
          <div class="IndicatorTable">
            <div style="margin-bottom: 10px">
              <el-input
                v-model="indicatorQuery.year"
                placeholder="搜索所属年份"
                size="small"
                style="width: 180px; margin-right: 10px"
                clearable
              />
              <el-input
                v-model="indicatorQuery.name"
                placeholder="搜索指标名称"
                size="small"
                style="width: 180px; margin-right: 10px"
                clearable
              />
              <el-button
                type="primary"
                size="small"
                @click="handleIndicatorSearch"
                >搜索</el-button
              >
              <!-- <el-button size="small" @click="handleIndicatorReset"
                >重置</el-button
              > -->
            </div>
            <el-table
              :data="filteredCompanyIndicatorList"
              height="500"
              border
              style="width: 100%"
            >
              <el-table-column type="index" label="序号" align="center">
              </el-table-column>
              <el-table-column prop="year" label="所属年份" align="center">
              </el-table-column>
              <el-table-column prop="name" label="指标名称" align="center">
              </el-table-column>
              <el-table-column prop="key" label="英译" align="center">
              </el-table-column>
              <el-table-column prop="threshold" label="阈值" align="center">
              </el-table-column>
              <el-table-column
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
              >
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    @click="handleUpdateCompanyIndicatorData(scope.row)"
                    v-hasPermi="['enterpriseData:companyIndicator:edit']"
                    >修改</el-button
                  >
                  <el-button
                    size="mini"
                    type="text"
                    @click="handleDeleteCompanyIndicator(scope.row)"
                    v-hasPermi="['enterpriseData:companyIndicator:remove']"
                    >删除</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="年报" name="report">
          <div style="margin-bottom: 10px">
            <el-select
              v-model="selectedYear"
              placeholder="请选择年份"
              @change="fetchReportData"
            >
              <el-option
                v-for="year in yearList"
                :key="year"
                :label="year"
                :value="year"
              />
            </el-select>
          </div>
          <div>
            <el-button
              type="primary"
              size="mini"
              @click="saveReportData"
              style="margin-left: 10px"
              >保存</el-button
            >
          </div>
          <el-form :model="reportForm" label-width="100px">
            <el-form-item label="营业收入">
              <el-input v-model="reportForm.yinyeshouruTotal" />
            </el-form-item>
            <el-form-item label="研发总额">
              <el-input v-model="reportForm.yanfaTotal" />
            </el-form-item>
            <el-form-item label="资产总额">
              <el-input v-model="reportForm.zichanTotal" />
            </el-form-item>
            <el-form-item label="新增股权">
              <el-input v-model="reportForm.xinzenguqTotal" />
            </el-form-item>
            <el-form-item label="销售总额">
              <el-input v-model="reportForm.xiaoshouTotal" />
            </el-form-item>
            <el-form-item label="主营总额">
              <el-input v-model="reportForm.zhuyingTotal" />
            </el-form-item>
            <el-form-item label="负债总额">
              <el-input v-model="reportForm.fuzhaiTotal" />
            </el-form-item>
            <el-form-item label="净利润">
              <el-input v-model="reportForm.jinglirunTotal" />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <!-- 喜好管理对话框 -->
    <el-dialog
      title="企业喜好管理"
      :visible.sync="hobbyDialogVisible"
      width="80%"
      :before-close="handleHobbyClose"
    >
      <el-form :model="hobbyForm" label-width="100px">
        <el-form-item
          v-for="(item, index) in hobbyForm.formData"
          :key="index"
          :label="item.name"
        >
          <el-checkbox-group
            v-model="item.children"
            @change="handleHobbyChange($event, item)"
          >
            <el-checkbox
              v-for="option in item.options"
              :key="option"
              :label="option"
              >{{ option }}</el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleHobbyClose">取 消</el-button>
        <el-button
          type="primary"
          v-hasPermi="['enterpriseData:hobby:update']"
          @click="submitHobbyForm"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCompany,
  updateCompany,
  addCompany,
  delCompany,
  listCompanyById,
  listCompanyCategoryDataByCategoryId,
  listCompanyIndicatorDataList,
  listCompanyPropertyDataById,
  listCompanyPropertyDataByDtoId,
  updateCompanyPropertyData,
  addCompanyProperty,
  delCompanyProperty,
  listCompanyIndicatorDataByDtoId,
  listCompanyIndicatorDataById,
  updateCompanyIndicatorData,
  addCompanyIndicator,
  delCompanyIndicator,
  listCompanyCategoryByCompanyId,
  listCompanyIndicatorDictionaryByCompanyId,
  getCompanyReport,
  saveCompanyReport,
  getPolicyClassInfo,
  getPolicyHobby,
  updatePolicyHobby,
} from "@/api/system/company";
import { delCompanyBatch } from "../../../api/system/company";

export default {
  name: "Company",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      dialogVisible: false,
      // 指标表格数据
      companyList: [],
      companyPropertyList: [],
      companyIndicatorList: [],
      formPropertyDataList: [],
      dataItemList: [],
      indicatorItemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      openPropertyData: false,
      openIndicatorData: false,
      openCompany: false,
      // 查询参数
      queryParams: {
        name: null,
        person: null,
        address: null,
      },
      // 表单参数
      formCompany: {},
      formCategory: {},
      formPropertyData: {},
      formIndicatorData: {},
      companyDetailData: {},
      // 表单校验
      rules: {},
      //categroy_id
      nowId: 0,
      nowCompanyId: 0,
      isAdd: false,
      property: "",
      indicator: "",
      contentItemList: [], // 用于存储内容选项的列表
      opt: {},
      activeTab: "property",
      yearList: [],
      selectedYear: "",
      reportForm: {
        yinyeshouruTotal: "",
        yanfaTotal: "",
        zichanTotal: "",
        xinzenguqTotal: "",
        xiaoshouTotal: "",
        zhuyingTotal: "",
        fuzhaiTotal: "",
        jinglirunTotal: "",
      },
      hobbyDialogVisible: false,
      hobbyForm: {
        companyId: null,
        formData: [],
      },
      currentHobbyData: null,
      indicatorQuery: {
        year: "",
        name: "",
      },
    };
  },
  computed: {
    filteredCompanyIndicatorList() {
      return this.companyIndicatorList.filter((item) => {
        const yearMatch = this.indicatorQuery.year
          ? String(item.year).includes(this.indicatorQuery.year)
          : true;
        const nameMatch = this.indicatorQuery.name
          ? item.name.includes(this.indicatorQuery.name)
          : true;
        return yearMatch && nameMatch;
      });
    },
  },
  created() {
    this.getList();
  },
  methods: {
    handleAddClose() {
      this.openIndicatorData = false;
      this.reset("indicator");
      this.getList();
    },
    isNumberic(str) {
      return /^\d+$/.test(str);
    },
    //检测是否为多选
    checkStringMultiple(str) {
      const notStartWithSemicolon = !str.startsWith(";");
      const atLeastTwoSemicolons = (str.match(/;/g) || []).length >= 2;
      const endsWithSemicolon = str.endsWith(";");
      return notStartWithSemicolon && atLeastTwoSemicolons && endsWithSemicolon;
    },
    //检测是否为单选
    checkStringSingle(str) {
      const notStartWithSemicolon = !str.startsWith(";");
      const onlyOneSemicolons = (str.match(/;/g) || []).length == 1;
      const endsWithSemicolon = str.endsWith(";");
      return notStartWithSemicolon && onlyOneSemicolons && endsWithSemicolon;
    },
    //检测用户提供的时间区间是否合规
    isValidDateTimePair(str) {
      console.log(str);
      // 定义正则表达式
      const regex =
        /^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})-(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})$/;

      // 使用正则表达式匹配整个字符串
      const match = str.match(regex);
      if (!match) {
        return false; // 如果格式不匹配，返回 false
      }

      const startTime = match[1];
      const endTime = match[2];

      // 验证开始时间
      const date1 = new Date(startTime);
      if (isNaN(date1.getTime())) {
        return false; // 如果开始时间无效，返回 false
      }

      // 验证结束时间
      const date2 = new Date(endTime);
      if (isNaN(date2.getTime())) {
        return false; // 如果结束时间无效，返回 false
      }

      // 可选：验证结束时间是否大于开始时间
      if (date2 <= date1) {
        return false; // 如果结束时间早于或等于开始时间，返回 false
      }

      return true; // 如果所有验证都通过，返回 true
    },
    //检测是否有重复选项
    hasDuplicateOptions(str) {
      const options = str.split(";");
      const seenOptions = new Set();
      for (const option of options) {
        if (option.trim() === "") continue;
        if (seenOptions.has(option)) {
          return true;
        }
        seenOptions.add(option);
      }
      return false;
    },
    getCompanyDetail(id) {
      listCompanyCategoryByCompanyId(id).then((res) => {
        console.log(res);
        this.dataItemList = res.rows;
      });
      listCompanyIndicatorDictionaryByCompanyId(id).then((res) => {
        console.log(res);
        this.indicatorItemList = res.rows;
      });
      this.nowCompanyId = id;
      this.dialogVisible = true;
      listCompanyPropertyDataById(id).then((res) => {
        console.log(res);
        this.companyPropertyList = res.rows;
      });
      listCompanyIndicatorDataById(id).then((res) => {
        console.log(res);
        this.companyIndicatorList = res.rows;
        this.companyIndicatorList.forEach((element) => {
          element.threshold =
            element.threshold != null ? element.threshold : element.remark;
        });
        console.log(this.companyIndicatorList);
      });
      // 年报tab初始化
      const currentYear = new Date().getFullYear();
      this.yearList = [];
      for (let i = currentYear - 5; i <= currentYear + 5; i++) {
        this.yearList.push(i.toString());
      }
      this.selectedYear = currentYear.toString();
      this.fetchReportData();
    },
    handleClose(done) {
      this.dialogVisible = false;
    },
    /** 查询指标列表 */
    getList() {
      this.loading = true;
      listCompany(this.queryParams).then((response) => {
        this.companyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel(target) {
      if (target === "company") {
        this.openCompany = false;
      } else if (target === "property") {
        this.openPropertyData = false;
      } else if (target === "indicator") {
        this.openIndicatorData = false;
      }
      this.reset(target);
    },
    // 表单重置
    reset(target) {
      if (target === "company") {
        this.formCompany = {
          id: null,
          companyName: null,
          legalPerson: null,
          address: null,
        };
      } else if (target === "property") {
        this.formPropertyData = {
          id: null,
          content: null,
        };
      } else if (target === "indicator") {
        console.log("清除了");
        this.indicator = null;
        this.formIndicatorData = {
          id: null,
          name: null,
          threshold: null,
          key: null,
          remark: null,
        };
        console.log(this.formIndicatorData);
      }
      // this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleCompanyAdd() {
      this.reset("company");
      this.openCompany = true;
      this.title = "添加分类属性";
    },
    handlePropertyDataAdd() {
      listCompanyCategoryByCompanyId(this.nowCompanyId).then((res) => {
        console.log(res);
        this.dataItemList = res.rows;
      });
      this.isAdd = true;
      this.reset("property");
      this.openPropertyData = true;
      this.title = "添加分类明细";
    },
    handleIndicatorDataAdd() {
      this.$prompt("请输入年份", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /^\d{4}$/,
        inputErrorMessage: "年份格式不正确，请输入4位数字年份",
      })
        .then(({ value }) => {
          listCompanyIndicatorDictionaryByCompanyId(
            this.nowCompanyId,
            value
          ).then((res) => {
            console.log(res);
            this.indicatorItemList = res.rows;
          });
          this.isAdd = true;
          this.reset("indicator");
          this.openIndicatorData = true;
          this.title = "添加指标明细";
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消输入",
          });
        });
    },
    /** 修改按钮操作 */
    handleUpdateCompany(row) {
      this.reset("company");
      const id = row.id;
      listCompanyById(id).then((response) => {
        this.formCompany = response.rows[0];
        this.openCompany = true;
        this.title = "修改企业信息";
      });
    },
    handleUpdateCompanyPropertyData(row) {
      this.isAdd = false;
      this.nowId = row.id;
      this.reset("property");
      const id = row.id;
      listCompanyPropertyDataByDtoId(id).then((response) => {
        console.log(response);
        this.formPropertyData = response.rows[0];
        this.openPropertyData = true;
        this.title = "修改属性明细";
        listCompanyCategoryDataByCategoryId(
          this.formPropertyData.categoryId
        ).then((response) => {
          this.contentItemList = response.rows;
        });
      });
    },
    handleUpdateCompanyIndicatorData(row) {
      this.isAdd = false;
      this.nowId = row.id;
      this.reset("indicator");
      const id = row.id;
      listCompanyIndicatorDataByDtoId(id).then((response) => {
        console.log(response);
        this.formIndicatorData = response.rows[0];
        this.indicator = this.formIndicatorData.name;
        if (this.formIndicatorData.threshold == null) {
          this.formIndicatorData.threshold = this.formIndicatorData.remark;
        }
        this.openIndicatorData = true;
        this.title = "修改指标明细";
      });
    },
    /** 提交按钮 */
    submitPropertyDataForm() {
      this.formPropertyData.companyId = this.nowCompanyId;
      this.$refs["formPropertyData"].validate((valid) => {
        if (valid) {
          if (!this.isAdd) {
            updateCompanyPropertyData(this.formPropertyData).then(
              (response) => {
                this.$modal.msgSuccess("修改成功");
                this.openPropertyData = false;
                this.dialogVisible = false;
                this.property = "";
                this.getList();
              }
            );
          } else {
            this.formPropertyData.id = this.nowId;
            this.formPropertyData.companyId = this.nowCompanyId;
            this.formPropertyData.name = this.property;
            console.log(this.formPropertyData);
            const matchingItem = this.dataItemList.find(
              (item) => item.name === this.property
            );
            if (!matchingItem) {
              this.$message.error("请按照指标名标准选择指标名");
            } else {
              addCompanyProperty(this.formPropertyData).then((response) => {
                listCompanyPropertyDataById(this.nowCompanyId).then(
                  (response) => {
                    this.companyPropertyList = response.rows;
                  }
                );
                listCompanyPropertyDataById(this.nowCompanyId).then(
                  (response) => {
                    this.companyPropertyList = response.rows;
                  }
                );
                this.$modal.msgSuccess("新增成功");
                this.property = "";
                this.openPropertyData = false;
              });
            }
          }
        }
      });
    },
    submitIndicatorDataForm() {
      this.$refs["formIndicatorData"].validate((valid) => {
        console.log(this.formIndicatorData);
        if (valid) {
          // if (this.formIndicatorData.id != null) {
          if (!this.isAdd) {
            if (
              this.formIndicatorData.remark != null &&
              this.formIndicatorData.threshold != null
            ) {
              this.formIndicatorData.remark = null;
            }
            if (
              this.formIndicatorData.type === "multiple" &&
              (!this.checkStringMultiple(this.formIndicatorData.threshold) ||
                this.hasDuplicateOptions(this.formIndicatorData.threshold))
            ) {
              // console.log("进来了");
              this.$message.error(
                "数据项和阈值类型不匹配，多选数据项阈值应以“A;B;C;”的形式，每个选项后以分号结尾，且至少含有两个不同的选项"
              );
            } else if (
              this.formIndicatorData.type === "single" &&
              !this.checkStringSingle(this.formIndicatorData.threshold)
            ) {
              this.$message.error(
                "数据项和阈值类型不匹配，单选数据项阈值应以“A;”的形式，选项后以分号结尾，有且仅有一个选项"
              );
            } else if (
              this.formIndicatorData.type === "date" &&
              (!this.isValidDateTimePair(this.formIndicatorData.threshold) ||
                this.isNumberic(this.formIndicatorData.threshold))
            ) {
              this.$message.error(
                "该数据项的阈值要求为具体的时间段，且阈值格式为“yyyy-MM-dd HH:mm:ss”"
              );
            } else {
              if (!this.isNumberic(this.formIndicatorData.threshold)) {
                this.formIndicatorData.remark =
                  this.formIndicatorData.threshold;
                delete this.formIndicatorData.threshold;
              }
              updateCompanyIndicatorData(this.formIndicatorData).then(
                (response) => {
                  listCompanyIndicatorDataById(this.nowCompanyId).then(
                    (response) => {
                      this.companyIndicatorList = response.rows;
                      this.companyIndicatorList.forEach((element) => {
                        element.threshold =
                          element.threshold != null
                            ? element.threshold
                            : element.remark;
                      });
                      console.log(this.companyIndicatorList);
                    }
                  );
                  this.$modal.msgSuccess("修改成功");
                  this.openIndicatorData = false;
                  // this.dialogVisible = false;
                  this.indicator = "";
                  // this.getList();
                }
              );
            }
          } else {
            this.formIndicatorData.id = this.nowId;
            this.formIndicatorData.companyId = this.nowCompanyId;
            this.formIndicatorData.name = this.indicator;
            this.formIndicatorData.dictionaryId = this.opt[0].id;
            this.formIndicatorData.type = this.opt[0].type;

            console.log(this.formIndicatorData);
            console.log(this.opt);
            const matchingItem = this.indicatorItemList.find(
              (item) => item.name === this.indicator
            );
            this.formIndicatorData.key = matchingItem.key;
            if (!matchingItem) {
              this.$message.error("请按照指标名标准选择指标名");
            } else if (
              this.opt[0].type === "multiple" &&
              (!this.checkStringMultiple(this.formIndicatorData.threshold) ||
                this.hasDuplicateOptions(this.formIndicatorData.threshold))
            ) {
              this.$message.error(
                "数据项和阈值类型不匹配，多选数据项阈值应以“A;B;C;”的形式，每个选项后以分号结尾，且至少含有两个不同的选项"
              );
            } else if (
              this.opt[0].type === "single" &&
              !this.checkStringSingle(this.formIndicatorData.threshold)
            ) {
              this.$message.error(
                "数据项和阈值类型不匹配，单选数据项阈值应以“A;”的形式，选项后以分号结尾，有且仅有一个选项"
              );
            } else if (
              this.opt[0].type === "date" &&
              (!this.isValidDateTimePair(this.formIndicatorData.threshold) ||
                this.isNumberic(this.formIndicatorData.threshold))
            ) {
              this.$message.error(
                "该数据项的阈值要求为具体的时间段，且阈值格式为“yyyy-MM-dd HH:mm:ss-yyyy-MM-dd HH:mm:ss”"
              );
            }
            // else if (
            //   this.opt[0].type === "text" &&
            //   !this.isNumberic(this.formIndicatorData.threshold)
            // ) {
            //   this.$message.error(
            //     "数据项与阈值类型不匹配，数值类型的指标请用数字"
            //   );
            // }
            else {
              if (
                this.opt[0].type !== "text" ||
                (this.opt[0].type === "text" &&
                  !this.isNumberic(this.formIndicatorData.threshold))
              ) {
                this.formIndicatorData.remark =
                  this.formIndicatorData.threshold;
                delete this.formIndicatorData.threshold;
              }
              console.log(this.formIndicatorData);
              addCompanyIndicator(this.formIndicatorData).then((response) => {
                listCompanyIndicatorDataById(this.nowCompanyId).then(
                  (response) => {
                    this.companyIndicatorList = response.rows;
                    this.companyIndicatorList.forEach((element) => {
                      element.threshold =
                        element.threshold != null
                          ? element.threshold
                          : element.remark;
                    });
                    console.log(this.companyIndicatorList);
                  }
                );
                this.$modal.msgSuccess("新增成功");
                this.indicator = "";
                this.openIndicatorData = false;
              });
            }
          }
          // this.formIndicatorData.id = null;
        }
      });
    },
    submitCompanyForm() {
      this.$refs["formCompany"].validate((valid) => {
        if (valid) {
          if (this.formCompany.id != null) {
            updateCompany(this.formCompany).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.openCompany = false;
              this.dialogVisible = false;
              this.getList();
            });
          } else {
            addCompany(this.formCompany).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.openCompany = false;
              this.dialogVisible = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDeleteCompanyBatch(rows) {
      console.log(this.ids);
      const ids = this.ids;
      this.$modal
        .confirm('是否确认删除指标编号为"' + ids + '"的数据项？')
        .then(function () {
          console.log(ids);
          return delCompanyBatch(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    handleDeleteCompany(row) {
      const ids = row.id;
      this.$modal
        .confirm('是否确认删除指标编号为"' + ids + '"的数据项？')
        .then(function () {
          return delCompany(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    handleDeleteCompanyIndicator(row) {
      const ids = row.id;
      this.$modal
        .confirm('是否确认删除指标编号为"' + ids + '"的数据项？')
        .then(function () {
          return delCompanyIndicator(ids);
        })
        .then(() => {
          this.$modal.msgSuccess("删除成功");
          listCompanyIndicatorDataById(this.nowCompanyId).then((response) => {
            this.companyIndicatorList = response.rows;
            this.companyIndicatorList.forEach((element) => {
              element.threshold =
                element.threshold != null ? element.threshold : element.remark;
            });
          });
          listCompanyIndicatorDictionaryByCompanyId(id).then((res) => {
            console.log(res);
            this.indicatorItemList = res.rows;
          });
        })
        .catch(() => {});
    },
    handleDeletePropertyData(row) {
      const ids = row.id;
      this.$modal
        .confirm('是否确认删除指标编号为"' + ids + '"的数据项？')
        .then(function () {
          return delCompanyProperty(ids);
        })
        .then(() => {
          this.$modal.msgSuccess("删除成功");
          listCompanyPropertyDataById(this.nowCompanyId).then((response) => {
            this.companyPropertyList = response.rows;
          });
          listCompanyCategoryByCompanyId(this.nowCompanyId).then((res) => {
            console.log(res);
            this.dataItemList = res.rows;
          });
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/companyProperty/export",
        {
          ...this.queryParams,
        },
        `companyClass_${new Date().getTime()}.xlsx`
      );
    },
    updateStatus(data) {
      if (data.status) {
        data.status = 1;
      } else if (!data.status) {
        data.status = 0;
      }
      updateCompanyCategory(data).then((response) => {
        this.getList();
      });
    },
    querySearchAsyncProperty(queryString, cb) {
      var dataItemList = this.dataItemList;
      var results = queryString
        ? dataItemList.filter(this.createPropertyFilter(queryString))
        : dataItemList;
      results = results.map((data) => {
        return {
          id: data.id,
          value: data.name,
          name: data.name,
        };
      });
      cb(results);
    },
    createPropertyFilter(queryString) {
      return (state) => {
        return (
          state.name.toLowerCase().indexOf(queryString.toLowerCase()) === 0 ||
          state.name.toLowerCase().includes(queryString.toLowerCase())
        );
      };
    },
    handleSelectProperty(item) {
      // 这里需要调用后端API获取对应的内容选项
      // 示例代码，需要根据实际API进行修改
      console.log(item);
      listCompanyCategoryDataByCategoryId(item.id).then((response) => {
        console.log(response);
        this.contentItemList = response.rows;
        console.log(this.contentItemList);
      });
    },
    querySearchAsyncIndicator(queryString, cb) {
      var dataItemList = this.indicatorItemList;
      console.log(dataItemList);
      var results = queryString
        ? dataItemList.filter(this.createIndicatorFilter(queryString))
        : dataItemList;
      results = results.map((data) => {
        return {
          value: data.name,
          name: data.name,
        };
      });
      cb(results);
    },
    createIndicatorFilter(queryString) {
      return (state) => {
        return (
          state.name.toLowerCase().indexOf(queryString.toLowerCase()) === 0 ||
          state.name.toLowerCase().includes(queryString.toLowerCase())
        );
      };
    },
    handleSelectIndicator(item) {
      this.opt = this.indicatorItemList.filter((obj) => obj.name == item.value);
      console.log(this.opt);
      console.log(item);
    },
    querySearchAsyncContent(queryString, cb) {
      var contentItemList = this.contentItemList;
      var results = queryString
        ? contentItemList.filter(this.createContentFilter(queryString))
        : contentItemList;
      results = results.map((data) => {
        return {
          value: data.content,
          content: data.content,
        };
      });
      cb(results);
    },
    createContentFilter(queryString) {
      return (state) => {
        return (
          state.content.toLowerCase().indexOf(queryString.toLowerCase()) ===
            0 || state.content.toLowerCase().includes(queryString.toLowerCase())
        );
      };
    },
    handleSelectContent(item) {
      this.formPropertyData.content = item.content;
    },
    fetchReportData() {
      if (!this.selectedYear || !this.nowCompanyId) return;
      getCompanyReport({
        year: this.selectedYear,
        companyId: this.nowCompanyId,
      }).then((res) => {
        if (res.status === 0) {
          Object.assign(this.reportForm, res.data);
        } else {
          this.$message.error(res.msg || "获取年报失败");
        }
      });
    },
    saveReportData() {
      if (!this.selectedYear || !this.nowCompanyId) return;
      const params = {
        year: this.selectedYear,
        companyId: this.nowCompanyId,
        ...this.reportForm,
      };
      saveCompanyReport(params).then((res) => {
        if (res.data && res.data.status === 0) {
          this.$message.success("保存成功");
        } else {
          this.$message.error(res.data.msg || "保存失败");
        }
      });
    },
    // 处理喜好管理按钮点击
    handleHobbyManage(row) {
      this.hobbyForm.companyId = row.id;
      this.hobbyDialogVisible = true;
      this.loadHobbyData();
    },

    // 加载喜好数据
    async loadHobbyData() {
      try {
        // 获取所有分类和可选项
        const classInfoRes = await getPolicyClassInfo();
        if (classInfoRes.status === 0) {
          this.hobbyForm.formData = classInfoRes.data.map((item) => ({
            id: item.id,
            name: item.name,
            children: [], // 先不选
            options: item.children || [],
            subChildren: item.subChildren || null,
          }));
        }

        // 获取已保存的喜好
        const hobbyRes = await getPolicyHobby(this.hobbyForm.companyId);
        if (hobbyRes.status === 0 && hobbyRes.data) {
          // 遍历已保存的每个分类
          hobbyRes.data.forEach((savedItem) => {
            const formItem = this.hobbyForm.formData.find(
              (f) => f.name === savedItem.name
            );
            if (formItem) {
              // 直接赋值children为已选项
              formItem.children = savedItem.children || [];
            }
          });
        }
      } catch (error) {
        this.$message.error("加载喜好数据失败");
        console.error(error);
      }
    },

    // 解析已保存的喜好数据
    parseSavedHobbyData() {
      if (!this.currentHobbyData) return;

      this.currentHobbyData.forEach((item) => {
        const [category, value] = item.split(":");
        const formItem = this.hobbyForm.formData.find(
          (f) => f.name === category
        );
        if (formItem) {
          formItem.children = [value];
        }
      });
    },

    // 处理喜好选择变化
    handleHobbyChange(value, item) {
      // 如果选择了"全部"，则清空其他选项
      if (value.includes("全部")) {
        item.children = ["全部"];
      }

      // 如果是适用区域,且有子区域数据
      if (item.name === "适用区域" && item.subChildren) {
        // 如果选择了某个城市,需要处理其子区域
        const selectedCity = value.find((v) =>
          item.subChildren.some((sc) => sc.name === v)
        );
        if (selectedCity) {
          const cityData = item.subChildren.find(
            (sc) => sc.name === selectedCity
          );
          if (cityData) {
            // 这里可以添加处理子区域的逻辑
            console.log("Selected city districts:", cityData.children);
          }
        }
      }
    },

    // 提交喜好表单
    async submitHobbyForm() {
      try {
        const formData = this.hobbyForm.formData.map((item) => ({
          id: item.id,
          name: item.name,
          children: item.children,
        }));

        const res = await updatePolicyHobby({
          companyId: this.hobbyForm.companyId,
          formData: formData,
        });

        if (res.status === 0) {
          this.$message.success("保存成功");
          this.handleHobbyClose();
        } else {
          this.$message.error(res.msg || "保存失败");
        }
      } catch (error) {
        this.$message.error("保存失败");
        console.error(error);
      }
    },

    // 关闭喜好管理对话框
    handleHobbyClose() {
      this.hobbyDialogVisible = false;
      this.hobbyForm = {
        companyId: null,
        formData: [],
      };
      this.currentHobbyData = null;
    },
    handleIndicatorSearch() {
      // 由于用的是计算属性，输入框变化已自动生效，这里可留空或做其它处理
    },
    handleIndicatorReset() {
      this.indicatorQuery.year = "";
      this.indicatorQuery.name = "";
    },
  },
};
</script>

<style scoped>
.el-select {
  width: 100%;
}
</style>
