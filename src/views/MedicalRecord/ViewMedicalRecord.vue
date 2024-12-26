<script>

import request from "@/utils/request";
import Cookies from "js-cookie";


export default {
  name: "ViewMedicalRecord",
  data() {
    return {
      user: Cookies.get('user') ? JSON.parse(Cookies.get('user')) : {},
      params: {
        patientId: '',
        symptoms: '',
        diagnosis: '',
        pageSize: 13,
        pageNum: 1
      },
      // 搜索病人选项
      patientOptions: [],
      // 病历数据
      medicalRecordData: [],
      // 病人数据
      patientData: [],
      // 医生数据
      doctorData: [],
      total: 0, // 总条数
      detailVisible: false, // 详情弹窗
      editVisible: false, // 编辑弹窗
      selectedRow: null, // 选中的行
      imageDialogVisible: false, // 图片弹窗
      prescriptionData: [],
      selectedPrescription: [],
      editForm: {},
      selectFromTable: true,

      prescriptionId: null,
      prescriptionOptions: [],
      rules: {
        diagnosis: [{ required: true, message: '请输入诊断结果', trigger: 'blur' }],
      }
    }
  },
  created() {
    this.listMedicalRecord()
    this.listPrescription()
  },
  methods: {
    listMedicalRecord() {
      // console.log(this.params)
      request({
        url: '/medicalRecord/list',
        method: 'get',
        params: this.params
      }).then(res => {
        if (res.code === '200') {
          this.medicalRecordData = res.data.list
          this.loadPatients()
          this.loadDoctors()
          // console.log(this.medicalRecordData)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    reset() {
      this.params = {
        patientId: '',
        symptoms: '',
        diagnosis: '',
        pageSize: 13,
        pageNum: 1
      }
    },
    // 处理分页
    handleCurrentPageChange(pageNum) {
      this.params.pageNum = pageNum
    },
    viewDetail(row) {
      this.selectedPrescription = []
      this.selectedRow = row
      // console.log(this.selectedRow)
      if (this.selectedRow.chart === '' || this.selectedRow.chart === null) {
        this.$set(this.selectedRow, 'existChart', false)
      } else {
        this.$set(this.selectedRow, 'existChart', true)
      }
      for (let i = 0; i < this.prescriptionData.length; i++) {
        if (this.prescriptionData[i].prescriptionId === this.selectedRow.prescriptionId) {
          this.selectedPrescription.push(this.prescriptionData[i])
          break
        }
      }
      // console.log(this.selectedPrescription)
      if (this.selectedPrescription.length === 0) {
        this.$set(this.selectedRow, 'existPrescription', false)
      } else {
        this.$set(this.selectedRow, 'existPrescription', true)
      }
      this.detailVisible = true
    },
    edit(row) {
      this.editForm = row
      console.log(this.editForm)
      this.editVisible = true
    },
    deleteRecord(row) {
      console.log(row)
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: 'medicalRecord/delete/' + row.recordId,
          method: 'delete'
        }).then(res => {
          if (res.code === '200') {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.listMedicalRecord()
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    // 获取患者信息
    loadPatients() {
      request.get('/user/patient').then(res => {
        // console.log(res)
        if (res.code === '200') {
          this.patientData = res.data.list
          // console.log(this.patientData)
          this.generatePatientOptions()
          try {
            for (let i = 0; i < this.medicalRecordData.length; i++) {
              for (let j = 0; j < this.patientData.length; j++) {
                if (this.medicalRecordData[i].patientId === this.patientData[j].patientId) {
                  // this.medicalRecordData[i].patientName = this.patientData[j].user.username
                  this.$set(this.medicalRecordData[i], 'patientName', this.patientData[j].user.username)
                }
              }
            }
            // console.log(this.medicalRecordData)
          } catch (e) {
            this.$message.error(e)
          }
        }

      })
    },
    // 生成病人选项
    generatePatientOptions() {
      this.patientOptions = this.patientData.map(patient => ({
        value: patient.patientId,
        label: `${patient.user.username} Id: ${patient.patientId}`
      }))
    },
    // 获取医生信息
    loadDoctors() {
      request.get('/user/doctor').then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.doctorData = res.data
          // console.log(this.doctorData)
          try {
            for (let i = 0; i < this.medicalRecordData.length; i++) {
              for (let j = 0; j < this.doctorData.length; j++) {
                if (this.medicalRecordData[i].doctorId === this.doctorData[j].doctorId) {
                  // this.medicalRecordData[i].doctorName = this.doctorData[j].user.username
                  this.$set(this.medicalRecordData[i], 'doctorName', this.doctorData[j].user.username)
                }
              }
            }
            // console.log(this.medicalRecordData)
          } catch (error) {
            console.error(error)
          }
        }
      })
    },
    // 查看图片
    viewImage() {
      this.imageDialogVisible = true; // 打开
    },
    listPrescription() {
      // console.log(this.params)
      request({
        url: '/prescription/list',
        method: 'get',
        params: this.params
      }).then(res => {
        if (res.code === '200') {
          this.prescriptionData = res.data.list
          // console.log(this.prescriptionData)
          this.generateOptions(this.prescriptionData, 'prescription')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    // 处理是否从表选择
    handleChange() {
      if (this.selectFromTable === false) {
        this.editForm.prescriptionId = null;
      }
    },
    generateOptions(data, type) {
      if (type === 'prescription') {
        this.prescriptionOptions = data.map(item => {
          return {
            value: item.prescriptionId.toString(),
            label: `药品名称：${item.medicationName} 开具医生： ${item.doctorName} 备注：${item.remarks}` // 拼接username和userId
          }
        })
        console.log(this.prescriptionOptions)
      }
    },
    onSubmit() {
      this.$confirm('确认新增处方吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$refs.form.validate(valid => {
          if (valid) {
            // console.log(this.editForm)
            request({
              url: '/medicalRecord/update',
              method: 'post',
              data: this.editForm
            }).then(res => {
              if (res.code === '200') {
                this.$message.success('编辑成功')
                this.editVisible = false
                this.listMedicalRecord()
              } else {
                this.$message.error(res.msg)
              }
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消编辑'
        })
      })
    }
  }
}
</script>

<template>
  <div class="medical-record-page">
    <h2 class="page-title">查询病历</h2>

    <!-- 搜索表单 -->
    <div class="search-form">
      <el-input class="search-input" placeholder="搜索症状" v-model="params.symptoms" />
      <el-input class="search-input" placeholder="搜索诊断" v-model="params.diagnosis" />
      <el-select v-model="params.patientId" filterable placeholder="搜索病人" class="search-input">
        <el-option
            v-for="item in patientOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
            :disabled="item.disabled"
        />
      </el-select>
      <el-button class="search-button" type="primary" @click="listMedicalRecord">
        <i class="el-icon-search"></i>搜索
      </el-button>
      <el-button class="search-button" type="warning" @click="reset">
        <i class="el-icon-refresh"></i>重置
      </el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="medicalRecordData" stripe border class="medical-record-table">
      <el-table-column prop="recordId" label="病历ID" width="70" />
      <el-table-column prop="patientId" label="患者ID" width="70" />
      <el-table-column prop="patientName" label="患者姓名" />
      <el-table-column prop="doctorId" label="医生ID" width="70" />
      <el-table-column prop="doctorName" label="医生姓名" />
      <el-table-column prop="diagnosis" label="诊断" />
      <el-table-column prop="symptoms" label="症状" />
      <el-table-column prop="remarks" label="备注" />
      <el-table-column label="操作">
        <template v-slot="scoped">
          <el-link type="primary" class="action-link" @click="viewDetail(scoped.row)">查看详情</el-link>
          <el-link type="warning" class="action-link" @click="edit(scoped.row)">编辑</el-link>
          <el-link type="danger" class="action-link" @click="deleteRecord(scoped.row)">删除病历</el-link>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
          background
          :current-page="params.pageNum"
          :page-size="params.pageSize"
          layout="prev, pager, next"
          @current-change="handleCurrentPageChange"
          :total="total"
      />
    </div>

    <!-- 查看详情弹窗 -->
    <el-dialog :visible.sync="detailVisible" width="80%" class="detail-dialog">
      <el-descriptions title="病历详情" class="margin-top">
        <el-descriptions-item label="患者姓名">{{ selectedRow?.patientName }}</el-descriptions-item>
        <el-descriptions-item label="主治医生">{{ selectedRow?.doctorName }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ selectedRow?.remarks }}</el-descriptions-item>
        <el-descriptions-item label="诊断信息">{{ selectedRow?.diagnosis }}</el-descriptions-item>
        <el-descriptions-item label="症状">{{ selectedRow?.symptoms }}</el-descriptions-item>
        <el-descriptions-item label="处方">{{ selectedRow?.prescription }}</el-descriptions-item>
        <el-descriptions-item label="图像" v-if="selectedRow?.existChart">
          <el-link @click="viewImage">查看<i class="el-icon-view el-icon--right"></i></el-link>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 编辑弹窗 -->
    <el-dialog :visible.sync="editVisible" width="60%" title="编辑病历" class="edit-dialog">
      <el-form ref="form" :model="editForm">
        <!-- 表单项... -->
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button @click="editVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<style scoped>
.medical-record-page {
  padding: 20px;
  background-color: #f9f9f9;
}

.page-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.search-input {
  width: 240px;
}

.search-button {
  margin-left: 10px;
}

.medical-record-table {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.action-link {
  margin: 0 5px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.detail-dialog,
.edit-dialog {
  .el-dialog__body {
    padding: 20px;
  }
}
</style>
