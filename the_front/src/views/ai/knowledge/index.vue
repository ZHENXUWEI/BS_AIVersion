<template>
  <div class="app-container">
    <el-card>
      <el-button type="primary" @click="openAddDialog">新增知识库文档</el-button>

      <el-table :data="knowledgeList" border>
        <el-table-column prop="title" label="文档标题" width="300"></el-table-column>
        <el-table-column prop="category" label="分类"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="text" @click="deleteKnowledge(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增文档弹窗 -->
    <el-dialog title="新增知识库文档" v-model="addDialogVisible">
      <el-form :model="knowledgeForm" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="knowledgeForm.title"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="knowledgeForm.category">
            <el-option label="政策文件" value="policy"></el-option>
            <el-option label="企业数据" value="enterprise"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="knowledgeForm.content" rows="6"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitKnowledge">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { addKnowledgeAPI, listKnowledgeAPI } from '@/api/ai'

export default {
  data() {
    return {
      knowledgeList: [],
      addDialogVisible: false,
      knowledgeForm: {
        title: '',
        category: 'policy',
        content: ''
      }
    }
  },
  created() {
    this.loadKnowledgeList()
  },
  methods: {
    loadKnowledgeList() {
      listKnowledgeAPI().then(res => {
        this.knowledgeList = res.data
      })
    },
    openAddDialog() {
      this.addDialogVisible = true
      this.knowledgeForm = { title: '', category: 'policy', content: '' }
    },
    submitKnowledge() {
      addKnowledgeAPI(this.knowledgeForm).then(() => {
        this.$message.success('新增成功')
        this.addDialogVisible = false
        this.loadKnowledgeList()
      })
    }
  }
}
</script>
