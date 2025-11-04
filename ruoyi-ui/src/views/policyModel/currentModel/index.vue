<template>
  <div class="container">
    <div class="table">
      <el-form
        :model="queryParams"
        ref="queryForm"
        size="small"
        :inline="true"
        v-show="showSearch"
        label-width="68px"
      >
        <el-form-item label="政策名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入政策名称"
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
            >搜索</el-button
          >
          <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
            >重置</el-button
          > -->
        </el-form-item>
      </el-form>
      <el-form :inline="true" :model="formAdd" class="demo-form-inline">
        <el-form-item>
          <el-button
            type="primary"
            @click="modelAdd"
            v-hasPermi="['system:policyModel:add']"
            >新增模型</el-button
          >
          <el-button
            type="success"
            icon="el-icon-download"
            @click="handleExport"
            v-hasPermi="['system:policyModel:export']"
            >导出</el-button
          >
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table
        :indent="indents"
        v-loading="listLoading"
        :data="list.slice((currentPage - 1) * pagesize, currentPage * pagesize)"
        element-loading-text="Loading"
        border
        fit
        highlight-current-row
        max-height="1000"
      >
        <el-table-column type="selection" width="55" />

        <el-table-column align="center" label="序号" width="55">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>

        <el-table-column label="政策模型名称" align="center">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>

        <el-table-column label="政策状态" align="center">
          <template slot-scope="scope">
            {{
              scope.row.status === 0
                ? "未发布"
                : scope.row.status === 1
                ? "申报中"
                : "已截止"
            }}
          </template>
        </el-table-column>

        <el-table-column label="模型材料数量" align="center">
          <template slot-scope="scope">
            {{ scope.row.docNumber }}
            <el-button
              size="mini"
              type="text"
              @click="handleDocNumber(scope)"
              v-hasPermi="['system:docList:list']"
              >查看信息</el-button
            >
          </template>
        </el-table-column>

        <el-table-column align="center" min-width="100">
          <template slot="header" slot-scope="scope">
            <!-- <el-input v-model="search" size="mini" placeholder="输入关键字搜索" /> -->
            操作
          </template>
          <template slot-scope="scope">
            <el-button
              size="mini"
              v-hasPermi="['system:policyModelDetail:list']"
              @click="lookup(scope.row)"
              >编辑模型</el-button
            >
            <el-button
              size="mini"
              v-hasPermi="['system:policyModel:matchCompany']"
              @click="check(scope.$index)"
              >匹配企业</el-button
            >
            <el-button
              size="mini"
              type="success"
              @click="handleExportItem(scope.row)"
              v-hasPermi="['system:policyModel:export']"
              >导出</el-button
            >
          </template>
        </el-table-column>
        <el-table-column align="center">
          <template slot="header" slot-scope="scope">
            <!-- <el-input v-model="search" size="mini" placeholder="输入关键字搜索" /> -->
            操作
          </template>
          <template slot-scope="scope">
            <el-button
              size="mini"
              v-hasPermi="['system:policyContent:edit']"
              @click="handleUpdateModel(scope.row)"
              >修改基本属性</el-button
            >
            <el-button
              size="mini"
              type="danger"
              v-hasPermi="['system:policyModel:remove']"
              @click="delModel(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!-- 添加数据 -->
      <el-dialog :visible.sync="dialogFormVisibleAdd">
        <el-form :model="formAdd" :rules="rules" ref="formAdd">
          <el-form-item
            label="政策模型名称"
            prop="name"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="formAdd.name"
              autocomplete="off"
              placeholder="请输入政策模型名称"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="发布时间"
            prop="publishDate"
            :label-width="formLabelWidth"
          >
            <el-input
              type="date"
              v-model="formAdd.publishDate"
              autocomplete="off"
              placeholder="请输入发布时间"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="截止时间"
            prop="expireDate"
            :label-width="formLabelWidth"
          >
            <el-input
              type="date"
              v-model="formAdd.expireDate"
              autocomplete="off"
              placeholder="请输入发布时间"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="描述"
            prop="description"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="formAdd.description"
              autocomplete="off"
              placeholder="请输入模型描述"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="内容"
            prop="content"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="formAdd.content"
              autocomplete="off"
              placeholder="请输入模型内容"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="资金(单位:万)"
            prop="money"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="formAdd.money"
              autocomplete="off"
              placeholder="请输入模型资金"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="发布部门"
            prop="source"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="formAdd.source"
              autocomplete="off"
              placeholder="请输入发布部门"
            ></el-input>
          </el-form-item>
          <!-- <el-form-item label="材料数量" :label-width="formLabelWidth">
            <el-input v-model="formAdd.docNumber" autocomplete="off" placeholder="请输入材料数量"></el-input>
          </el-form-item>  -->
          <!-- <el-form-item label="材料清单" :label-width="formLabelWidth">
            <el-input v-model="formAdd.docDetail" autocomplete="off" placeholder="请输入材料清单"></el-input>
          </el-form-item> -->
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisibleAdd = false">取 消</el-button>
          <el-button
            type="primary"
            v-hasPermi="['system:policyModel:add', 'system:policyContent:edit']"
            @click="modelRecordAdd()"
            >确 定</el-button
          >
        </div>
      </el-dialog>

      <!-- 分页器 -->
      <div class="block" style="margin-top: 10vh">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pagesize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="list.length"
        >
        </el-pagination>
      </div>

      <el-dialog
        title="模型展示"
        :visible.sync="dialogVisible"
        width="82%"
        :before-close="handleClose"
      >
        <div slot="title" class="dialog-title">
          <el-row>
            <el-col :span="1.5">
              <span class="dialog-title-text" style="padding-right: 20px"
                >模型展示</span
              >
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="dialogFormPreVisible = true"
                v-hasPermi="['system:policyModelDetail:list']"
                >新增</el-button
              >
            </el-col>
          </el-row>
        </div>
        <div class="policy_model">
          <!--             @cell-mouse-enter="handleCellEnter"
            @cell-mouse-leave="handleCellLeave"
            @cell-click="handleCellClick" -->
          <el-table
            :data="policyModelList"
            height="400"
            border
            style="width: 100%"
            ref="lookupTable"
          >
            <el-table-column align="center" label="序号">
              <template slot-scope="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column
              prop="id"
              label="指标序号"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="indicatorDictionaryId"
              label="字典序号"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="indicatorType"
              label="指标复杂类型"
              align="center"
            >
              <template slot-scope="scope">
                <div>
                  {{ scope.row.indicatorType === "0" ? "简单" : "复杂" }}
                </div>
              </template></el-table-column
            >
            <el-table-column prop="name" label="数据项" align="center">
            </el-table-column>
            <el-table-column prop="operator" label="符号" align="center">
            </el-table-column>
            <el-table-column prop="threshold" label="阈值" align="center">
            </el-table-column>
            <el-table-column
              prop="complexFormula"
              label="复杂公式"
              align="center"
            >
              <template slot-scope="scope">
                <div>
                  {{ scope.row.complexFormula || "-" }}
                </div>
              </template>
              <!-- 废弃 可编辑单元格 -->
              <!-- <div class="item" slot-scope="scope">
                <el-tooltip
                  effect="dark"
                  :content="scope.row.complexFormula"
                  placement="top"
                  :disabled="
                    !scope.row.complexFormula ||
                    scope.row.complexFormula.trim() === ''
                  "
                >
                  <el-input
                    class="item__input"
                    v-model="scope.row.complexFormula"
                    placeholder="请输入复杂公式"
                    @blur="handleInputBlur(scope.row, $event)"
                    v-if="scope.row.type === `text`"
                  ></el-input>
                </el-tooltip>
                <div class="item__txt">{{ scope.row.complexFormula }}</div>
              </div> -->

              <!-- 废弃 圆形下拉框 -->
              <!-- <template slot-scope="scope" v-if="scope.row.type === `text`">
                使用 v-if 检查 complexFormulaList 是否为 null，如果是，则显示 el-dropdown
                <div
                  v-if="
                    scope.row.complexFormulaList === null ||
                    scope.row.complexFormulaList.length === 0
                  "
                >
                  <el-dropdown @command="handleDropdownCommand" key="item">
                    <el-button circle icon="el-icon-plus" size="mini">
                    </el-button>
                    <el-dropdown-menu
                      slot="dropdown"
                      :items="complexIndicatorList"
                    >
                      <template v-for="item in complexIndicatorList">
                        <el-dropdown-item :command="[item, scope]">{{
                          item
                        }}</el-dropdown-item>
                      </template>
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>

                使用 v-else 来显示 complexFormulaList 中的项
                <div v-else>
                  <div
                    v-for="(item, index) in scope.row.complexFormulaList"
                    :key="index"
                  >
                    {{ item }}
                    <el-dropdown @command="handleDropdownCommand" key="item">
                      <el-button
                        circle
                        icon="el-icon-plus"
                        size="mini"
                        v-if="index === 0"
                      >
                      </el-button>
                      <el-dropdown-menu
                        slot="dropdown"
                        :items="complexIndicatorList"
                      >
                        <template v-for="item in complexIndicatorList">
                          <el-dropdown-item :command="[item, scope]">{{
                            item
                          }}</el-dropdown-item>
                        </template>
                      </el-dropdown-menu>
                      <el-button
                        circle
                        icon="el-icon-minus"
                        size="mini"
                        @click="delIndicator(item, index, scope)"
                      >
                      </el-button>
                    </el-dropdown>
                  </div>
                </div>
              </template> -->
            </el-table-column>
            <el-table-column fixed="right" label="操作" align="center">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="danger"
                  v-hasPermi="['system:policyModelIndicator:remove']"
                  @click="handleDeletePolicyModel(scope.row)"
                  >删除</el-button
                >
                <el-button
                  size="mini"
                  type="primary"
                  v-hasPermi="['system:policyModelIndicator:edit']"
                  @click="handleUpdatePolicyModel(scope.$index)"
                  >修改</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="formula" style="margin-top: 20px">
          <div style="margin-bottom: 10px">
            公式编辑器
            <el-button
              type="primary"
              plain
              size="mini"
              @click="handleUpdateFormula"
              v-hasPermi="['system:policyModelFormula:edit']"
              >更新</el-button
            >
            <el-button type="primary" @click="open" size="mini">提示</el-button>
          </div>
          <el-input v-model="formula" placeholder="请输入内容"></el-input>
        </div>

        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmit()">确 定</el-button>
        </span>
      </el-dialog>

      <el-dialog
        title="新增"
        :visible.sync="dialogVisibleAdd"
        width="30%"
        :before-close="handleCloseAdd"
      >
        <div class="info">
          <el-form
            ref="form"
            :model="formAdd"
            label-width="100px"
            v-if="indicatorType === '0'"
          >
            <el-form-item label="数据项">
              <el-autocomplete
                v-model="state"
                class="inline-input"
                :fetch-suggestions="querySearchAsync"
                placeholder="请输入内容"
                @select="handleSelect"
              >
                <template slot-scope="{ item }">
                  <div class="value">{{ item.value }}</div>
                </template></el-autocomplete
              >
            </el-form-item>
            <el-form-item label="符号">
              <el-select v-model="formAdd.operation" placeholder="请选择操作符">
                <el-option label=">" value=">"></el-option>
                <el-option label="<" value="<"></el-option>
                <el-option label="=" value="=="></el-option>
                <el-option label="-" value="-"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="阈值">
              <el-input
                v-model="formAdd.threshold"
                placeholder="请写入阈值"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                v-hasPermi="['system:policyModelSimple:add']"
                @click="onSubmit"
                >立即创建</el-button
              >
              <el-button @click="handleCloseAdd">取消</el-button>
            </el-form-item>
          </el-form>

          <el-form
            ref="form"
            :model="formAddComplex"
            label-width="100px"
            v-else
          >
            <el-form-item label="数据项">
              <el-input
                v-model="formAddComplex.name"
                placeholder="请写入指标名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="复杂公式">
              <el-input
                v-model="formAddComplex.complexFormula"
                placeholder="请写入复杂公式"
                type="textarea"
              ></el-input>
            </el-form-item>
            <el-form-item label="符号">
              <el-select
                v-model="formAddComplex.operation"
                placeholder="请选择操作符"
              >
                <el-option label=">" value=">"></el-option>
                <el-option label="<" value="<"></el-option>
                <el-option label="=" value="=="></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="阈值">
              <el-input
                v-model="formAddComplex.threshold"
                placeholder="请写入阈值"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                v-hasPermi="['system:policyModelComplex:add']"
                @click="onSubmitComplex"
                >立即创建</el-button
              >
              <el-button @click="handleCloseAdd">取消</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-dialog>

      <el-dialog
        title="同年企业详情"
        :visible.sync="dialogVisibleCompany"
        width="30%"
        :before-close="handleCloseCompanyDetail"
      >
        <div class="info">
          <el-table
            :data="companyDetailData"
            style="width: 100%"
            height="250"
            v-loading="loading"
          >
            <el-table-column type="index" label="序号" align="center">
            </el-table-column>
            <el-table-column prop="companyName" label="企业公司" align="center">
            </el-table-column>
          </el-table>
        </div>
      </el-dialog>
      <el-dialog
        title="材料名称详情"
        :visible.sync="dialogVisibleDocDetailName"
        width="20%"
        :before-close="handleCloseDocDetailName"
      >
        <div class="info">
          <el-input v-model="docDetailName"></el-input>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisibleDocDetailName = false"
            >取 消</el-button
          >
          <el-button type="primary" @click="onSubmitDocDetailUpdate()"
            >确 定</el-button
          >
        </span>
      </el-dialog>

      <el-dialog
        title="材料详情"
        :visible.sync="dialogVisibleDocDetail"
        width="30%"
        :before-close="handleCloseDocDetail"
      >
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleDocDetailAdd()"
            style="position: absolute; top: 1.8vh; left: 6vw"
            v-hasPermi="['system:policyModelDocDetail:edit']"
            >新增</el-button
          >
        </el-col>
        <div class="info">
          <el-table :data="docDetail" style="width: 100%" height="250">
            <el-table-column type="index" label="序号" align="center">
            </el-table-column>
            <el-table-column prop="name" label="材料名称" align="center">
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
                  @click="handleUpdateDocDetail(scope)"
                  v-hasPermi="['system:policyModelDocDetail:edit']"
                  >修改</el-button
                >
                <el-button
                  size="mini"
                  type="text"
                  @click="handleDeleteDocDetail(scope)"
                  v-hasPermi="['system:policyModelDocDetail:edit']"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-dialog>

      <!-- 修改单项模型指标 -->
      <el-dialog
        title="单项模型修改"
        :visible.sync="dialogVisibleUpdate"
        width="30%"
        :before-close="handleCloseUpdate"
      >
        <div class="updateForm">
          <el-form
            :inline="true"
            :model="formInline"
            label-position="left"
            label-width="auto"
            v-if="indicatorType === '0'"
          >
            <el-form-item label="阈值">
              <el-input
                v-model="formInline.threshold"
                placeholder="阈值"
              ></el-input>
            </el-form-item>
            <el-form-item label="操作符">
              <el-select v-model="formInline.operator" placeholder="操作符">
                <el-option label=">" value=">"></el-option>
                <el-option label="<" value="<"></el-option>
                <el-option label="=" value="=="></el-option>
                <el-option label="-" value="-"></el-option>
              </el-select>
            </el-form-item>
          </el-form>

          <el-form
            :inline="true"
            :model="formInlineComplex"
            label-position="left"
            label-width="auto"
            v-else
          >
            <el-form-item label="复杂公式">
              <el-input
                v-model="formInlineComplex.complexFormula"
                placeholder="请写入复杂公式"
                type="textarea"
              ></el-input>
            </el-form-item>
            <el-form-item label="阈值">
              <el-input
                v-model="formInlineComplex.threshold"
                placeholder="阈值"
              ></el-input>
            </el-form-item>
            <el-form-item label="操作符">
              <el-select
                v-model="formInlineComplex.operator"
                placeholder="操作符"
              >
                <el-option label=">" value=">"></el-option>
                <el-option label="<" value="<"></el-option>
                <el-option label="=" value="=="></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        <span slot="footer" class="dialog-footer" v-if="indicatorType === '0'">
          <el-button @click="dialogVisibleUpdate = false">取 消</el-button>
          <el-button
            type="primary"
            v-hasPermi="['system:policyModelIndicator:edit']"
            @click="onSubmitUpdate()"
            >确 定</el-button
          >
        </span>

        <span slot="footer" class="dialog-footer" v-else>
          <el-button @click="dialogVisibleUpdate = false">取 消</el-button>
          <el-button
            type="primary"
            v-hasPermi="['system:policyModelIndicator:edit']"
            @click="onSubmitUpdateComplex()"
            >确 定</el-button
          >
        </span>
      </el-dialog>

      <!-- 模型展示部分点击新增框弹出的对话框 -->
      <el-dialog
        title="指标类型"
        :visible.sync="dialogFormPreVisible"
        width="15%"
      >
        <el-select v-model="indicatorType" placeholder="请选择新增的指标类型">
          <el-option label="自定义指标" value="1"></el-option>
          <el-option label="字典指标" value="0"></el-option>
        </el-select>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleAdd">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {
  updatePolicy,
  listPolicy,
  listFuturePolicyModel,
  addPolicyModelRecord,
  addPolicyModel,
  listPolicyModel,
  listPolicyModelByPolicyId,
  updatePolicyFormulaData,
  listDataItem,
  delPolicyModel,
  updatePolicyModelData,
  matchCompanyList,
  updateComplexFormula,
  docDetailUpdate,
  delModelRecord,
  exportPolicy,
  addPolicyModelRecordComplex,
  listTextDataItem,
} from "@/api/system/policy";
import { download } from "@/utils/request";

export default {
  data() {
    return {
      indicatorType: "0",
      companyName: "",
      showSearch: true,
      role: "",
      num: 0,
      indents: 0,
      dialogVisible: false,
      options: [],
      value: [],
      list: [],
      policyModelList: [],
      complexIndicatorList: [],
      loading: false,
      listLoading: true,
      search: "",
      dialogTableVisible: false,
      dialogFormVisibleAdd: false,
      dialogFormVisible: false,
      dialogVisibleCompany: false,
      dialogVisibleAdd: false,
      dialogVisibleDocDetail: false,
      dialogVisibleDocDetailName: false,
      formula: "",
      form: {
        id: "",
        name: "",
        item: "",
        threshold: null,
        operation: "",
      },
      formAdd: {
        id: null,
        name: "",
        item: "",
        threshold: null,
        operation: "",
      },
      formAddComplex: {
        id: null,
        name: "",
        item: "",
        complexFormula: null,
        threshold: null,
        operation: "",
      },
      queryParams: {
        name: null,
        publishDate: null,
        status: null,
      },
      tableData: [],
      companyDetailData: [],
      dataItemList: [],
      dataItemTextList: [],
      formLabelWidth: "120px",
      currentPage: 1,
      currentIndex: "",
      pagesize: 10,
      state: "",
      timeout: null,
      dialogVisibleUpdate: false,
      formInline: {
        id: 0,
        operator: "",
        threshold: 0,
      },
      formInlineComplex: {
        id: null,
        name: "",
        item: "",
        complexFormula: null,
        threshold: null,
        operator: "",
      },
      loading: true,
      nowPolicyId: 0,
      complexFormulaList: [],
      docDetail: [],
      docDetailName: "",
      docDetailNameId: 0,
      policyId: 0,
      modelId: 0,
      docDetailAdd: 0,
      opt: {}, //模型指标新增时所选的数据项
      // 需要编辑的属性
      editProp: ["complexFormula"],
      // 保存进入编辑的cell
      clickCellMap: {},
      dialogFormPreVisible: false,
      rules: {
        name: [
          { required: true, message: "请输入政策模型名称", trigger: "blur" },
          {
            validator: (rule, value, callback) => {
              console.log(value);
              const year = new Date().getFullYear();
              const reg = new RegExp("^" + year);
              if (!reg.test(value)) {
                callback("政策模型名称必须以" + year + "开头");
              } else {
                callback();
              }
            },
            trigger: "blur",
          },
        ],
        // ...其他规则
      },
    };
  },
  watch: {
    tableData(val) {
      this.doLayout();
    },
  },
  mounted() {
    // this.list = this.states.map((item) => {
    //   return { value: `value:${item}`, label: `label:${item}` };
    // });
  },
  created() {
    this.fetchData();
  },
  methods: {
    getPolicyStatus(publishDate, expireDate) {
      if (publishDate == null && expireDate == null) {
        return 0;
      }
      const now = Date.now();
      const pub = new Date(publishDate).getTime();
      const exp = new Date(expireDate).getTime();

      if (now < pub) return 0;
      if (now > exp) return 2;
      return 1;
    },
    onSubmitUpdateComplex() {
      listTextDataItem().then((res) => {
        this.dataItemTextList = res.rows;
      });
      this.dataItemTextList = this.dataItemTextList.map((item) => ({
        ...item,
        name: item.name.replace(/\r\n/g, ""), // 使用正则表达式替换 \r\n 为空字符串
      }));
      console.log(this.dataItemTextList);
      const res = this.checkString(
        this.formInlineComplex.complexFormula,
        this.dataItemTextList
      );
      if (res == true) {
        console.log(this.formInlineComplex);
        this.dialogVisibleUpdate = false;
        updatePolicyModelData(this.formInlineComplex).then((res) => {
          listPolicyModelByPolicyId(this.policyModelList[0].policyId).then(
            (res) => {
              // console.log(res);
              this.policyModelList = res.rows;
              this.policyModelList.forEach((element) => {
                element.threshold =
                  element.threshold != null
                    ? element.threshold
                    : element.remark;
              });
              if (
                res.rows &&
                res.rows[0] &&
                res.rows[0].formula !== undefined
              ) {
                this.formula = res.rows[0].formula;
              } else {
                this.formula = "";
              }
            }
          );
          this.dialogVisibleUpdate = false;
          this.$modal.msgSuccess("修改成功");
        });
      } else if (res == false) {
        this.$message.error("复杂公式中存在中文字符");
      } else {
        this.$message.error("复杂公式中存在未知引用：" + res);
      }
    },
    onSubmitComplex() {
      const exist = this.policyModelList.some(
        (item) => item.name === this.formAddComplex.name
      );
      console.log(exist);
      if (exist) {
        this.$message.error("复杂公式指标与基础指标重名");
      } else {
        listTextDataItem().then((res) => {
          this.dataItemTextList = res.rows;
        });
        this.dataItemTextList = this.dataItemTextList.map((item) => ({
          ...item,
          name: item.name.replace(/\r\n/g, ""), // 使用正则表达式替换 \r\n 为空字符串
        }));
        const res = this.checkString(
          this.formAddComplex.complexFormula,
          this.dataItemTextList
        );
        console.log("检测结果是------" + res);
        if (res == true) {
          console.log(this.formAddComplex);
          addPolicyModelRecordComplex(
            this.nowPolicyId,
            encodeURIComponent(this.formAddComplex.complexFormula),
            this.formAddComplex.operation,
            this.formAddComplex.threshold,
            this.formAddComplex.name
          ).then((res) => {
            listPolicyModelByPolicyId(this.nowPolicyId).then((res) => {
              this.policyModelList = res.rows;
              this.policyModelList.forEach((element) => {
                element.threshold =
                  element.threshold != null
                    ? element.threshold
                    : element.remark;
              });
              if (
                res.rows &&
                res.rows[0] &&
                res.rows[0].formula !== undefined
              ) {
                this.formula = res.rows[0].formula;
              } else {
                this.formula = "";
              }
            });
            this.dialogVisibleAdd = false;
            this.formAddComplex = {
              id: null,
              name: "",
              item: "",
              complexFormula: null,
              threshold: null,
              operation: "",
            };
            this.$modal.msgSuccess("添加成功");
          });
        } else if (res == false) {
          this.$message.error("复杂公式中存在中文字符");
        } else {
          this.$message.error("复杂公式中存在未知引用：" + res);
        }
      }
    },
    checkString(str, list) {
      // 检查是否包含中文标点符号
      if (/[（）【】]/.test(str)) {
        return false; // 包含中文标点符号，返回 false
      }
      // 提取字符串中的所有文字组
      const textGroups =
        str.match(
          /[\u4e00-\u9fa5a-zA-Z0-9]+[\u4e00-\u9fa5]+[\u4e00-\u9fa5a-zA-Z0-9]*/g
        ) || [];
      console.log(textGroups);
      console.log("这是list：");
      console.log(list);
      // 过滤出 type 为 "text" 的对象
      const textList = list
        .filter((item) => item.type === "text")
        .map((item) => item.name);
      // 检查每个文字组是否存在于 textList 中
      for (const group of textGroups) {
        if (!textList.includes(group)) {
          return group; // 返回第一个不存在的文字组
        }
      }
      return true; // 所有文字组都存在
    },
    // 方法：检查字符串中的数字是否都存在于 list 中的 num 属性中
    checkNumbersInList(list, str) {
      console.log(list);
      // 提取字符串中的所有数字
      const numbersInStr = this.extractNumbers(str);

      // 将 list 中的 num 属性提取到一个数组中
      const numsInList = list.map((item) => item.indicatorDictionaryId);

      // 检查字符串中的每个数字是否都存在于 numsInList 中
      for (const num of numbersInStr) {
        if (!numsInList.includes(num)) {
          return num; // 返回第一个不匹配的数字
        }
      }
      return true;
    },

    // 提取字符串中的所有数字
    extractNumbers(str) {
      const regex = /\d+/g; // 匹配所有数字
      const numbers = str.match(regex); // 提取所有数字
      return numbers ? numbers.map(Number) : []; // 转换为数字数组
    },
    handleInputBlur(row, event) {
      console.log("输入框失去焦点了！");
      console.log("当前行的数据:", row);
      console.log("输入框的值:", event.target.value);
      this.validateInput(row, event.target.value);
    },
    validateInput(row, value) {
      const res = this.checkNumbersInList(this.policyModelList, value);
      if (res == true) {
        const data = {
          id: row.id,
          complexFormula: value,
        };
        updateComplexFormula(data).then((res) => {
          listPolicyModelByPolicyId(this.nowPolicyId).then((res) => {
            console.log(res);
            this.policyModelList = res.rows;
            this.policyModelList.forEach((element) => {
              element.threshold =
                element.threshold != null ? element.threshold : element.remark;
            });
            this.complexIndicatorList = this.policyModelList.map(
              (item) => item.name
            );
            // console.log(this.complexIndicatorList);
            if (res.rows && res.rows[0] && res.rows[0].formula !== undefined) {
              this.formula = res.rows[0].formula;
            } else {
              this.formula = "";
            }
          });
        });
      } else {
        this.$message.error("复杂公式中存在未知引用：" + res);
      }
      console.log(this.policyModelList);
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
      const regex =
        /^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})-(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})$/;
      const match = str.match(regex);
      if (!match) {
        return false;
      }
      const time1 = match[1];
      const time2 = match[2];
      const date1 = new Date(time1);
      const date2 = new Date(time2);
      if (isNaN(date1.getTime()) || isNaN(date2.getTime())) {
        return false;
      }
      return true;
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
    doLayout() {
      let that = this;
      this.$nextTick(() => {
        that.$refs.table.doLayout();
      });
    },
    handleDeletePolicyModel(row) {
      const ids = row.id;
      this.$modal
        .confirm('是否确认删除指标编号为"' + ids + '"的数据项？')
        .then(function () {
          return delPolicyModel(ids);
        })
        .then(() => {
          listPolicyModelByPolicyId(this.policyModelList[0].policyId).then(
            (res) => {
              // console.log(res);
              this.policyModelList = res.rows;
              this.policyModelList.forEach((element) => {
                element.threshold =
                  element.threshold != null
                    ? element.threshold
                    : element.remark;
              });
              if (
                res.rows &&
                res.rows[0] &&
                res.rows[0].formula !== undefined
              ) {
                this.formula = res.rows[0].formula;
              } else {
                this.formula = "";
              }
            }
          );
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    handleSizeChange(val) {
      this.pagesize = val;
      // console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      this.currentPage = val;
    },
    onSubmitDocDetailUpdate() {
      if (this.docDetailName === "") {
        this.$message.error("材料不能为空");
      } else {
        if (this.docDetailAdd === 1) {
          this.docDetail.push({
            name: this.docDetailName,
          });
        } else {
          this.docDetail[this.docDetailNameId].name = this.docDetailName;
        }
        let docDetailNameList = this.docDetail
          .map((item) => item.name)
          .join(";");
        docDetailUpdate(this.modelId, docDetailNameList).then((res) => {
          this.$message.success("更新成功");
          this.fetchData();
          this.dialogVisibleDocDetailName = false;
        });
      }
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      if (this.queryParams.name !== null && this.queryParams !== " ") {
        const name = this.queryParams.name;
        this.list = this.list.filter((item) => item.name.includes(name));
      }
    },
    resetQuery() {
      this.fetchData();
    },
    handleDeleteDocDetail(scope) {
      console.log(scope.$index);
      const ids = scope.$index;
      console.log(this.docDetail);
      this.$modal
        .confirm('是否确认删除指标编号为"' + ids + '"的数据项？')
        .then(() => {
          this.docDetail.splice(scope.$index, 1);
          let docDetailNameList =
            this.docDetail.length >= 1
              ? this.docDetail.map((item) => item.name).join(";")
              : "";
          docDetailUpdate(this.modelId, docDetailNameList).then((res) => {
            this.fetchData();
            this.$message.success("删除成功");
          });
        });
    },
    delModel(row) {
      console.log(row.id);
      this.$modal
        .confirm('是否确认删除指标编号为"' + row.id + '"的数据项？')
        .then(() => {
          delModelRecord(row.id).then((res) => {
            this.$message.success("删除成功");
          });
        });
    },
    handleCloseDocDetail() {
      this.fetchData();
      this.dialogVisibleDocDetail = false;
    },
    handleCloseDocDetailName() {
      this.dialogVisibleDocDetailName = false;
    },
    handleDocNumber(scope) {
      console.log(scope.row);
      this.modelId = scope.row.id;
      console.log(this.modelId);
      if (scope.row.docDetail !== null) {
        // 去除末尾的空格和分号
        scope.row.docDetail = scope.row.docDetail
          .trimEnd() // 去除末尾的空格
          .replace(/;$/, ""); // 去除末尾的分号（如果存在）
        const items = scope.row.docDetail.split(";");
        this.docDetail = items.map((item) => {
          return { name: item.trim() };
        });
      } else {
        const items = "";
        this.docDetail = [];
      }

      this.dialogVisibleDocDetail = true;
    },
    handleUpdateDocDetail(scope) {
      this.docDetailAdd = 0;
      console.log(scope.$index);
      this.docDetailName = scope.row.name;
      this.docDetailNameId = scope.$index;
      this.dialogVisibleDocDetailName = true;
    },
    async fetchData() {
      this.listLoading = true;
      await listPolicyModel(this.queryParams).then((res) => {
        this.list = res.rows;
        this.list.map((item) => {
          item.status = this.getPolicyStatus(item.publishDate, item.expireDate);
        });
        console.log(this.list);
      });
      this.listLoading = false;
    },
    handleClose(done) {
      this.fetchData();
      this.dialogVisible = false;
    },
    handleAdd() {
      console.log(this.indicatorType);
      this.dialogFormPreVisible = false;
      this.state = "";
      this.formAdd.name = "";
      this.formAdd.threshold = "";
      this.formAdd.operation = "";
      // console.log(this.nowPolicyId);
      listDataItem(this.nowPolicyId).then((res) => {
        this.dataItemList = res.rows;
      });
      listTextDataItem().then((res) => {
        this.dataItemTextList = res.rows;
        console.log(this.dataItemTextList);
      });
      this.dialogVisibleAdd = true;
    },
    handleCloseAdd() {
      this.dialogVisibleAdd = false;
    },
    handleCloseCompanyDetail() {
      this.dialogVisibleCompany = false;
      this.loading = true;
    },
    handleDocDetailAdd() {
      this.docDetailAdd = 1;
      this.dialogVisibleDocDetailName = true;
      this.docDetailName = "";
    },
    onSubmit() {
      // console.log(this.formAdd);
      console.log(this.opt);
      this.formAdd.name = this.state;
      const matchingItem = this.dataItemList.find(
        (item) => item.name === this.state
      );
      console.log(
        this.opt[0].type === "single" &&
          this.checkStringSingle(this.formAdd.threshold)
      );
      // console.log(matchingItem);
      if (!matchingItem) {
        this.$message.error("请按照指标名标准选择指标名");
      } else if (
        this.formAdd.operation == null ||
        this.formAdd.operation == ""
      ) {
        this.$message.error("请选择操作符，若没有则选用请选用“-”");
      } else {
        if (
          (!this.isNumberic(this.formAdd.threshold) &&
            this.formAdd.operation == "-") ||
          (this.isNumberic(this.formAdd.threshold) &&
            this.formAdd.operation != "-")
        ) {
          if (
            this.opt[0].type === "multiple" &&
            (!this.checkStringMultiple(this.formAdd.threshold) ||
              this.hasDuplicateOptions(this.formAdd.threshold))
          ) {
            this.$message.error(
              "数据项和阈值类型不匹配，多选数据项阈值应以“A;B;C;”的形式，每个选项后以分号结尾，且至少含有两个不同的选项"
            );
          } else if (
            this.opt[0].type === "single" &&
            !this.checkStringSingle(this.formAdd.threshold)
          ) {
            this.$message.error(
              "数据项和阈值类型不匹配，单选数据项阈值应以“A;”的形式，选项后以分号结尾，有且仅有一个选项"
            );
          } else if (
            this.opt[0].type === "date" &&
            (!this.isValidDateTimePair(this.formAdd.threshold) ||
              this.isNumberic(this.formAdd.threshold))
          ) {
            this.$message.error(
              "该数据项的阈值要求为具体的时间段，且阈值格式为“yyyy-MM-dd HH:mm:ss-yyyy-MM-dd HH:mm:ss”"
            );
          } else {
            if (
              this.opt[0].type === "text" &&
              this.formAdd.threshold != null &&
              this.formAdd.operator === "-"
            ) {
              this.$message.error(
                "数据项与操作符，阈值类型不匹配，数值类型的指标请用数字以及“>，<，=”操作符"
              );
            }
            console.log(this.opt[0].type);
            console.log(this.formAdd);
            // let result =
            //   (this.opt[0].type === "single" ||
            //     this.opt[0].type === "multiple") &&
            //   (!this.checkString(this.formAdd.threshold) ||
            //     this.hasDuplicateOptions(this.formAdd.threshold));
            // console.log(result);
            addPolicyModelRecord(
              this.nowPolicyId,
              matchingItem.id,
              this.formAdd.operation,
              this.formAdd.threshold
            ).then((res) => {
              console.log("get");
              listPolicyModelByPolicyId(this.nowPolicyId).then((res) => {
                // console.log(res);
                this.policyModelList = res.rows;
                this.policyModelList.forEach((element) => {
                  element.threshold =
                    element.threshold != null
                      ? element.threshold
                      : element.remark;
                });
                if (
                  res.rows &&
                  res.rows[0] &&
                  res.rows[0].formula !== undefined
                ) {
                  this.formula = res.rows[0].formula;
                } else {
                  this.formula = "";
                }
              });
              this.dialogVisibleAdd = false;
              this.formAdd = {
                id: null,
                name: "",
                item: "",
                threshold: "",
                operation: "",
              };
              this.$modal.msgSuccess("添加成功");
            });
          }
        } else {
          this.$message.error(
            "操作符和阈值类型不匹配，除数值以外的类型请用操作符“-”"
          );
        }
      }
    },
    lookup(row) {
      // console.log(row.id);
      this.nowPolicyId = row.id;
      listPolicyModelByPolicyId(row.id).then((res) => {
        console.log(res);
        this.policyModelList = res.rows;
        this.policyModelList.forEach((element) => {
          element.threshold =
            element.threshold != null ? element.threshold : element.remark;
        });
        this.complexIndicatorList = this.policyModelList.map(
          (item) => item.name
        );
        // console.log(this.complexIndicatorList);
        if (res.rows && res.rows[0] && res.rows[0].formula !== undefined) {
          this.formula = res.rows[0].formula;
        } else {
          this.formula = "";
        }
        this.$nextTick(() => {
          this.$refs.lookupTable.doLayout();
        });
      });
      listDataItem(this.nowPolicyId).then((res) => {
        this.dataItemList = res.rows;
        console.log("这里是lookup的list：");
        console.log(this.dataItemList);
      });
      this.dialogVisible = true;
    },
    check(index) {
      console.log(this.list[index]);
      if (!this.list[index].formula) {
        this.$confirm(
          "您并未指定该政策模型的匹配法则，这将导致匹配到所有企业，点击确定继续匹配",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            console.log(this.list[index]);
            // 用户点击确定后的逻辑
            setInterval(() => {
              this.loading = false;
            }, 8000);
            matchCompanyList(
              this.list[index].id,
              this.list[index].formula
            ).then((res) => {
              console.log(res);
              this.companyDetailData = res.rows;
            });
            clearInterval();
            this.dialogVisibleCompany = true;
            this.loading = true;
          })
          .catch(() => {
            // 用户点击取消后的逻辑
          });
      } else {
        setInterval(() => {
          this.loading = false;
        }, 8000);
        matchCompanyList(this.list[index].id, this.list[index].formula).then(
          (res) => {
            console.log(res);
            this.companyDetailData = res.rows;
          }
        );
        clearInterval();
        this.dialogVisibleCompany = true;
        this.loading = true;
      }
    },
    handleUpdateFormula() {
      const policy = {
        id: this.policyModelList[0].policyId,
        formula: this.formula,
      };
      updatePolicyFormulaData(policy).then((res) => {
        this.fetchData();
        this.dialogVisible = false;
        this.$modal.msgSuccess("修改成功");
      });
      // console.log(this.policyModelList[0].policyId);
    },
    querySearchAsync(queryString, cb) {
      var dataItemList = this.dataItemList;
      var results = queryString
        ? dataItemList.filter(this.createStateFilter(queryString))
        : dataItemList;
      results = results.map((data) => {
        return {
          value: data.name,
          name: data.name,
        };
      });
      cb(results);
    },
    createStateFilter(queryString) {
      return (state) => {
        return (
          state.name.toLowerCase().indexOf(queryString.toLowerCase()) === 0 ||
          state.name.toLowerCase().includes(queryString.toLowerCase())
        );
      };
    },
    handleSelect(item) {
      // console.log(this.dataItemList);
      this.opt = this.dataItemList.filter((obj) => obj.name == item.value);
      console.log(this.opt);
    },
    handleUpdatePolicyModel(index) {
      this.indicatorType = this.policyModelList[index].indicatorType;
      console.log(this.policyModelList);
      if (this.policyModelList[index].indicatorType === "0") {
        this.formInline = {
          id: this.policyModelList[index].id,
          operator: this.policyModelList[index].operator,
          threshold: this.policyModelList[index].threshold,
          type: this.policyModelList[index].type,
        };
      } else if (this.policyModelList[index].indicatorType === "1") {
        this.formInlineComplex = {
          id: this.policyModelList[index].id,
          name: this.policyModelList[index].name,
          complexFormula: this.policyModelList[index].complexFormula,
          threshold: this.policyModelList[index].threshold,
          operator: this.policyModelList[index].operator,
        };
      }
      console.log(this.formInline);
      console.log(this.policyModelList[index]);
      this.dialogVisibleUpdate = true;
    },
    handleCloseUpdate() {
      this.dialogVisibleUpdate = false;
    },
    onSubmitUpdate() {
      if (
        (!this.isNumberic(this.formInline.threshold) &&
          this.formInline.operator == "-") ||
        (this.isNumberic(this.formInline.threshold) &&
          this.formInline.operator != "-")
      ) {
        // console.log(this.checkStringMultiple(this.formInline.threshold));
        // console.log(this.formInline.type === "multiple");

        // console.log(
        //   (this.formInline.type === "multiple" &&
        //     !this.checkStringMultiple(this.formInline.threshold)) ||
        //     this.hasDuplicateOptions(this.formInline.threshold)
        // );
        if (
          this.formInline.type === "multiple" &&
          (!this.checkStringMultiple(this.formInline.threshold) ||
            this.hasDuplicateOptions(this.formInline.threshold))
        ) {
          // console.log("进来了");
          this.$message.error(
            "数据项和阈值类型不匹配，多选数据项阈值应以“A;B;C;”的形式，每个选项后以分号结尾，且至少含有两个不同的选项"
          );
        } else if (
          this.formInline.type === "single" &&
          !this.checkStringSingle(this.formInline.threshold)
        ) {
          this.$message.error(
            "数据项和阈值类型不匹配，单选数据项阈值应以“A;”的形式，选项后以分号结尾，有且仅有一个选项"
          );
        } else if (
          this.formInline.type === "date" &&
          (!this.isValidDateTimePair(this.formInline.threshold) ||
            this.isNumberic(this.formInline.threshold))
        ) {
          this.$message.error(
            "该数据项的阈值要求为具体的时间段，且阈值格式为“yyyy-MM-dd HH:mm:ss-yyyy-MM-dd HH:mm:ss”"
          );
        } else {
          if (!this.isNumberic(this.formInline.threshold)) {
            this.formInline.remark = this.formInline.threshold;
            delete this.formInline.threshold;
          }
          console.log(this.formInline);
          updatePolicyModelData(this.formInline).then((res) => {
            listPolicyModelByPolicyId(this.policyModelList[0].policyId).then(
              (res) => {
                // console.log(res);
                this.policyModelList = res.rows;
                this.policyModelList.forEach((element) => {
                  element.threshold =
                    element.threshold != null
                      ? element.threshold
                      : element.remark;
                });
                if (
                  res.rows &&
                  res.rows[0] &&
                  res.rows[0].formula !== undefined
                ) {
                  this.formula = res.rows[0].formula;
                } else {
                  this.formula = "";
                }
              }
            );
            this.dialogVisibleUpdate = false;
            this.$modal.msgSuccess("修改成功");
          });
        }
        // console.log("测试");
      } else {
        this.$message.error(
          "操作符和阈值类型不匹配，除数值以外的类型请用操作符“-”"
        );
      }
      // console.log(this.formInline);
    },
    open() {
      this.$alert("数字表示指标序号，语法支持()、&、|", "使用说明", {
        confirmButtonText: "确定",
        // callback: (action) => {
        //   this.$message({
        //     type: "info",
        //     message: `action: ${action}`,
        //   });
        // },
      });
    },
    handleDropdownCommand(payload) {
      console.log(payload);
      const [command, scope] = payload;
      this.complexFormulaList.push(payload[0]);
      this.complexFormulaList.push.apply(
        this.complexFormulaList,
        payload[1].row.complexFormulaList
      );
      console.log(this.complexFormulaList);
      const data = {
        id: scope.row.id,
        complexFormulaList: this.complexFormulaList,
      };
      updateComplexFormula(data).then((res) => {
        listPolicyModelByPolicyId(this.nowPolicyId).then((res) => {
          console.log(res);
          this.policyModelList = res.rows;
          this.policyModelList.forEach((element) => {
            element.threshold =
              element.threshold != null ? element.threshold : element.remark;
          });
          this.complexIndicatorList = this.policyModelList.map(
            (item) => item.name
          );
          // console.log(this.complexIndicatorList);
          if (res.rows && res.rows[0] && res.rows[0].formula !== undefined) {
            this.formula = res.rows[0].formula;
          } else {
            this.formula = "";
          }
        });
      });
      this.complexFormulaList = [];
    },
    delIndicator(item, index, scope) {
      console.log(scope.row.complexFormulaList);
      // let remainingElements = scope.row.complexFormulaList.filter(
      //   (str) => str !== item
      // );
      let key = scope.row.complexFormulaList.indexOf(item);
      if (key !== -1) {
        scope.row.complexFormulaList.splice(key, 1);
      }
      const data = {
        id: scope.row.id,
        complexFormulaList: scope.row.complexFormulaList,
      };

      console.log("where");
      console.log(data);
      updateComplexFormula(data).then((res) => {
        listPolicyModelByPolicyId(this.nowPolicyId).then((res) => {
          this.policyModelList = res.rows;
          this.policyModelList.forEach((element) => {
            element.threshold =
              element.threshold != null ? element.threshold : element.remark;
          });
          this.complexIndicatorList = this.policyModelList.map(
            (item) => item.name
          );
          // console.log(this.complexIndicatorList);
          if (res.rows && res.rows[0] && res.rows[0].formula !== undefined) {
            this.formula = res.rows[0].formula;
          } else {
            this.formula = "";
          }
        });
      });
    },
    handleSubmit() {
      this.fetchData();
      this.dialogVisible = false;
    },
    modelAdd() {
      this.formAdd = { id: null };
      this.dialogFormVisibleAdd = true;
    },
    //修改基本属性
    handleUpdateModel(row) {
      this.formAdd = row;
      console.log(this.formAdd);
      this.dialogFormVisibleAdd = true;
    },
    modelRecordAdd() {
      this.$refs.formAdd.validate((valid) => {
        if (!valid) return; // 校验不通过，直接中断
        // 通过校验后的提交逻辑
        if (this.formAdd.id !== null) {
          updatePolicy(this.formAdd).then((res) => {
            this.dialogFormVisibleAdd = false;
            this.fetchData();
            this.$modal.msgSuccess("更新成功");
          });
        } else {
          addPolicyModel(this.formAdd).then((res) => {
            this.dialogFormVisibleAdd = false;
            this.fetchData();
            this.$modal.msgSuccess("添加成功");
          });
        }
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$confirm("是否确认导出所有政策模型数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$modal.loading("正在导出数据，请稍候...");
          return download(
            "/system/policyContent/export",
            {}, // 不传递 id 参数，表示导出所有数据
            "政策模型数据.xlsx"
          );
        })
        .then(() => {
          this.$modal.closeLoading();
        })
        .catch(() => {});
    },
    /** 导出单个模型按钮操作 */
    handleExportItem(row) {
      this.$confirm("是否确认导出该政策模型数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$modal.loading("正在导出数据，请稍候...");
          return download(
            "/system/policyContent/export",
            {
              id: row.id,
            },
            `政策模型数据_${row.name}.xlsx`
          );
        })
        .then(() => {
          this.$modal.closeLoading();
        })
        .catch(() => {});
    },
    /** 鼠标移入cell */
    handleCellEnter(row, column, cell, event) {
      const property = column.property;
      if (property === "date" || property === "name" || property === "food") {
        cell.querySelector(".item__txt").classList.add("item__txt--hover");
      }
    },
    /** 鼠标移出cell */
    handleCellLeave(row, column, cell, event) {
      const property = column.property;
      if (this.editProp.includes(property)) {
        cell.querySelector(".item__txt").classList.remove("item__txt--hover");
      }
    },
    /** 点击cell */
    handleCellClick(row, column, cell, event) {
      const property = column.property;
      if (this.editProp.includes(property)) {
        // 保存cell
        this.saveCellClick(row, cell);
        cell.querySelector(".item__txt").style.display = "none";
        cell.querySelector(".item__input").style.display = "block";
        cell.querySelector("input").focus();
      }
    },
    /** 保存进入编辑的cell */
    saveCellClick(row, cell) {
      const id = row.id;
      if (this.clickCellMap[id] !== undefined) {
        if (!this.clickCellMap[id].includes(cell)) {
          this.clickCellMap[id].push(cell);
        }
      } else {
        this.clickCellMap[id] = [cell];
      }
    },
    /** 取消编辑状态 */
    cancelEditable(cell) {
      cell.querySelector(".item__txt").style.display = "block";
      cell.querySelector(".item__input").style.display = "none";
    },
    /** 保存数据 */
    save(row) {
      const id = row.id;
      // 取消本行所有cell的编辑状态
      this.clickCellMap[id].forEach((cell) => {
        this.cancelEditable(cell);
      });
      this.clickCellMap[id] = [];
    },
  },
};
</script>

<style lang="scss">
.container {
  .inline-input {
    width: 100%;
  }

  // .updateForm {
  //   // .el-input__inner {
  //   //   width: 100%;
  //   // }
  //   .el-form-item__content {
  //     width: 15.625rem;
  //   }
  // }

  .btn {
    position: absolute;
    top: 5vh;
  }

  .policy_model {
    .el-input__inner {
      border: none;
      text-align: center;
      // border-bottom: 1px solid #ebebeb;
      // height: 4vh;
    }
  }

  overflow: hidden;
  width: 100%;
  height: 100vh;
  background-color: #f2f2f2;

  .front {
    margin: 40px;
    font-size: 20px;
    background-color: white;

    .title {
    }

    .count {
    }
  }

  .table {
    margin: 40px;
  }
}

// .item {
//   .item__input {
//     display: none;
//     width: auto;
//     max-width: 100%; /* 限制输入框的最大宽度 */
//     overflow: hidden; /* 隐藏超出部分 */
//     text-overflow: ellipsis; /* 显示省略号 */
//     white-space: nowrap; /* 防止换行 */
//     .el-input__inner {
//       height: 24px !important;
//       width: 100%; /* 确保输入框宽度自适应 */
//     }
//     .el-input__suffix {
//       i {
//         font-size: 12px !important;
//         line-height: 26px !important;
//       }
//     }
//   }
//   .item__txt {
//     box-sizing: border-box;
//     border: 1px solid transparent;
//     width: auto;
//     line-height: 24px;
//     padding: 0 8px;
//     overflow: hidden; /* 隐藏超出部分 */
//     text-overflow: ellipsis; /* 显示省略号 */
//     white-space: nowrap; /* 防止换行 */
//   }
//   .item__txt--hover {
//     border: 1px solid #dddddd;
//     border-radius: 4px;
//     cursor: text;
//   }
// }
</style>
