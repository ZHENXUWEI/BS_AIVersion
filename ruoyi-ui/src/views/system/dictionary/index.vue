<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="数据字典名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入数据字典名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="外键英文关键字" prop="key">
        <el-input
          v-model="queryParams.key"
          placeholder="请输入外键英文关键字"
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
          @click="handleAdd"
          v-hasPermi="['system:dictionary:add']"
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
          @click="handleUpdate"
          v-hasPermi="['system:dictionary:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:dictionary:remove']"
        >删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:dictionary:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="dictionaryList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="数据字典ID" align="center" prop="id" />
      <el-table-column label="数据字典名称" align="center" prop="name" />
      <el-table-column label="外键英文关键字" align="center" prop="key" />
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          {{ scope.row.status == 1 ? "已生效" : "未生效" }}
        </template>
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
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:dictionary:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dictionary:remove']"
            >删除</el-button
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

    <!-- 添加或修改指标对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="15%">
        <el-form-item label="数据字典名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入数据字典名称" />
        </el-form-item>
        <el-form-item label="外键英文关键字" prop="key">
          <el-input v-model="form.key" placeholder="请输入外键英文关键字" />
        </el-form-item>
        <el-form-item label="字段类型" prop="type">
          <el-select
            v-model="form.type"
            placeholder="请选择字段类型"
            style="width: 100%"
          >
            <el-option label="单选题" value="single" />
            <el-option label="文本框" value="text" />
            <el-option label="多选题" value="multiple" />
            <el-option label="日期" value="date" />
            <el-option label="地区选择" value="region" />
          </el-select>
        </el-form-item>
        <el-form-item
          label="选项"
          prop="options"
          v-if="
            form.type === 'single' ||
            form.type === 'multiple' ||
            form.type === 'text'
          "
        >
          <div
            v-for="(option, index) in form.options"
            :key="index"
            class="option-item"
          >
            <el-input
              v-model="form.options[index]"
              :placeholder="'请输入选项' + String.fromCharCode(65 + index)"
              style="width: 80%; margin-bottom: 10px"
            />
            <el-button
              type="danger"
              icon="el-icon-delete"
              circle
              size="mini"
              @click="removeOption(index)"
              v-if="form.options.length > 1"
            ></el-button>
          </div>
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="addOption"
            v-if="form.options.length < 26"
            >添加选项</el-button
          >
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listDictionary,
  getDictionary,
  delDictionary,
  addDictionary,
  updateDictionary,
} from "@/api/system/dictionary";

export default {
  name: "Dictionary",
  data() {
    const validateName = (rule, value, callback) => {
      if (!value) {
        callback(new Error("数据字典名称不能为空"));
        return;
      }
      const queryParams = {
        name: value,
        pageSize: 999999,
      };
      listDictionary(queryParams)
        .then((response) => {
          const exists = response.rows.some((item) =>
            this.form.id
              ? item.id !== this.form.id && item.name === value
              : item.name === value
          );
          if (exists) {
            callback(new Error("该数据字典名称已存在"));
          } else {
            callback();
          }
        })
        .catch(() => {
          callback(new Error("验证失败，请重试"));
        });
    };

    const validateKey = (rule, value, callback) => {
      if (!value) {
        callback(new Error("外键英文关键字不能为空"));
        return;
      }
      const queryParams = {
        key: value,
        pageSize: 999999,
      };
      listDictionary(queryParams)
        .then((response) => {
          const exists = response.rows.some((item) =>
            this.form.id
              ? item.id !== this.form.id && item.key === value
              : item.key === value
          );
          if (exists) {
            callback(new Error("该外键英文关键字已存在"));
          } else {
            callback();
          }
        })
        .catch(() => {
          callback(new Error("验证失败，请重试"));
        });
    };

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
      // 指标表格数据
      dictionaryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        key: null,
        status: null,
      },
      // 表单参数
      form: {
        id: null,
        name: null,
        key: null,
        type: null,
        status: null,
        options: [""], // 初始一个空选项
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "数据字典名称不能为空", trigger: "blur" },
          { validator: validateName, trigger: "blur" },
        ],
        key: [
          {
            required: true,
            message: "外键英文关键字不能为空",
            trigger: "blur",
          },
          { validator: validateKey, trigger: "blur" },
        ],
        type: [
          { required: true, message: "字段类型不能为空", trigger: "change" },
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询指标列表 */
    getList() {
      this.loading = true;
      listDictionary(this.queryParams).then((response) => {
        this.dictionaryList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        key: null,
        type: null,
        status: null,
        options: [""], // 重置时添加一个空选项
      };
      this.resetForm("form");
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
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加指标";
      this.form.options = [""]; // 重置时添加一个空选项
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getDictionary(id).then((response) => {
        this.form = response.data;
        // 如果修改时没有options字段，初始化一个空选项
        if (!this.form.options) {
          this.form.options = [""];
        }
        this.open = true;
        this.title = "修改指标";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          // 处理选项数据
          if (
            this.form.type === "single" ||
            this.form.type === "multiple" ||
            this.form.type === "text"
          ) {
            // 过滤空选项并按字母顺序排序
            this.form.option = this.form.options
              .filter((opt) => opt.trim())
              .sort((a, b) => a.localeCompare(b))
              .join(";");
          }

          if (this.form.id != null) {
            console.log(this.form);
            if (this.form.option === "" && this.form.type === "text") {
              console.log("进来了");
              this.form.option = null;
              this.form.options = [];
            }
            updateDictionary(this.form).then((response) => {
              console.log(this.form);
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            if (this.form.option === "") {
              this.form.option = null;
              this.form.options = [];
            }
            addDictionary(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    // 添加选项
    addOption() {
      if (this.form.options.length < 26) {
        this.form.options.push("");
      }
    },
    // 删除选项
    removeOption(index) {
      this.form.options.splice(index, 1);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除指标编号为"' + ids + '"的数据项？')
        .then(function () {
          return delDictionary(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/dictionary/export",
        {
          ...this.queryParams,
        },
        `dictionary_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
