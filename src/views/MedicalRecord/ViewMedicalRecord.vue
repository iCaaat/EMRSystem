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
<div>
  <h2>查询病历</h2>

  <!-- 搜索表单 -->
  <div style="margin-bottom: 20px">
    <el-input style="width: 240px;" placeholder="搜索症状" v-model="params.symptoms" ></el-input>
    <el-input style="width: 240px; margin-left: 5px;" placeholder="搜索诊断" v-model="params.diagnosis"></el-input>
    <el-select v-model="params.patientId" filterable placeholder="搜索病人" style="width: 240px; margin-left: 5px;">
      <el-option
          v-for="item in patientOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
          :disabled="item.disabled">
      </el-option>
    </el-select>
    <el-button style="margin-left: 5px;" type="primary" @click="listMedicalRecord"><i class="el-icon-search"></i>搜索</el-button>
    <el-button style="margin-left: 5px;" type="warning" @click="reset"><i class="el-icon-refresh"></i>重置</el-button>
  </div>

  <el-table
      :data="medicalRecordData"
      stripe
      style="width: 100%">
    <el-table-column
        prop="recordId"
        label="病历ID"
        width="70">
    </el-table-column>
    <el-table-column
        prop="patientId"
        label="患者ID"
        width="70">
    </el-table-column>
    <el-table-column
        prop="patientName"
        label="患者姓名">
    </el-table-column>
    <el-table-column
        prop="doctorId"
        label="医生ID"
        width="70">
    </el-table-column>
    <el-table-column
        prop="doctorName"
        label="医生姓名">
    </el-table-column>
    <el-table-column
        prop="diagnosis"
        label="诊断">
    </el-table-column>
    <el-table-column
        prop="symptoms"
        label="症状">
    </el-table-column>
    <el-table-column
        prop="remarks"
        label="备注">
    </el-table-column>
    <el-table-column label="操作">
      <template v-slot="scoped">
        <el-link type="primary" @click="viewDetail(scoped.row)">查看详情</el-link>
        <el-link type="warning" @click="edit(scoped.row)">编辑</el-link>
        <el-link type="danger" @click="deleteRecord(scoped.row)">删除病历</el-link>
      </template>
    </el-table-column>
  </el-table>

  <!-- 分页 -->
  <div style="margin-top: 20px">
    <el-pagination
        background
        :current-page="params.pageNum"
        :page-size="params.pageSize"
        layout="prev, pager, next"
        @current-change="handleCurrentPageChange"
        :total="total">
    </el-pagination>
  </div>

  <!-- 查看详情弹窗 -->
  <el-dialog
      :visible.sync="detailVisible"
      width="80%">
    <el-descriptions title="用户信息" class="margin-top">
      <el-descriptions-item label="患者姓名">{{ selectedRow?.patientName }}</el-descriptions-item>
      <el-descriptions-item label="主治医生">{{ selectedRow?.doctorName }}</el-descriptions-item>
      <el-descriptions-item label="备注">{{ selectedRow?.remarks }}</el-descriptions-item>
      <el-descriptions-item label="诊断信息">{{ selectedRow?.diagnosis }}</el-descriptions-item>
      <el-descriptions-item label="症状">{{ selectedRow?.symptoms}}</el-descriptions-item>
      <el-descriptions-item label="处方">{{ selectedRow?.prescription}}</el-descriptions-item>
      <el-descriptions-item label="开药处方" v-if="this.selectedRow?.existPrescription">
        <el-table
            :data="selectedPrescription"
            style="width: 100%">
          <el-table-column
              prop="medicationName"
              label="药品名称">
          </el-table-column>
          <el-table-column
              prop="dosage"
              label="剂量">
          </el-table-column>
          <el-table-column
              prop="duration"
              label="持续时间">
          </el-table-column>
          <el-table-column
              prop="frequency"
              label="用药频率">
          </el-table-column>
          <el-table-column
              prop="instructions"
              label="用药说明">
          </el-table-column>
        </el-table>
      </el-descriptions-item>
      <el-descriptions-item label="图像" v-if="selectedRow?.existChart">
        <el-link @click="viewImage">查看<i class="el-icon-view el-icon--right"></i> </el-link>

        <!-- 图片查看弹窗 -->
        <el-dialog title="查看图片" :visible.sync="imageDialogVisible" width="50%" append-to-body>
          <img :src="selectedRow?.chart" alt="详细图片" style="width: 100%;" />
        </el-dialog>
      </el-descriptions-item>
    </el-descriptions>
  </el-dialog>

  <!--编辑弹窗-->
  <el-dialog :visible.sync="editVisible" width="60%" title="编辑">
    <el-form ref="form" :model="editForm">
      <el-form-item label="患者姓名">
        <el-input placeholder="请输入内容" v-model="editForm.patientName" disabled></el-input>
      </el-form-item>
      <el-form-item label="患者Id">
        <el-input placeholder="请输入内容" v-model="editForm.patientId" disabled></el-input>
      </el-form-item>
      <el-form-item label="医生姓名">
        <el-input placeholder="请输入内容" v-model="editForm.doctorName" disabled></el-input>
      </el-form-item>
      <el-form-item label="医生Id">
        <el-input placeholder="请输入内容" v-model="editForm.doctorId" disabled></el-input>
      </el-form-item>
      <el-form-item label="诊断信息">
        <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="editForm.diagnosis">
        </el-input>
      </el-form-item>
      <el-form-item label="症状">
        <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="editForm.symptoms">
        </el-input>
      </el-form-item>
      <el-form-item label="处方">
        <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="editForm.prescription">
        </el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="editForm.remarks">
        </el-input>
      </el-form-item>
      <el-form-item label="从表选择">
        <el-radio-group v-model="selectFromTable" @change="handleChange">
          <el-radio :label="false">否</el-radio>
          <el-radio :label="true">是</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="选择处方" v-if="selectFromTable">
        <el-select v-model="editForm.prescriptionId" filterable placeholder="请选择" style="width: 100%;">
          <el-option
              v-for="item in prescriptionOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">提交</el-button>
        <el-button @click="editVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</div>
</template>

<style scoped>

</style>
