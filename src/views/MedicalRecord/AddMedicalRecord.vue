<script>
import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
  name: 'AddMedicalRecord',
  data() {
    return {
      user: Cookies.get("user") ? JSON.parse(Cookies.get("user")) : {},
      patientOptions: [{
        value: '病人 Id: xx',
        label: 'Id'
      }],
      patientValue: '',
      selectedPatient: [],
      total: 0,
      params: {
        doctorId: '',
        medicationName: '',
        remarks: '',
        pageSize: 13,
        pageNum: 1
      },
      patientData: [],
      patientTableData: [],
      doctorData: [],
      isDoctor: false,
      form: {
        chart: null,
      },
      isSelectPatient: false,
      prescriptionOptions: [],
      selectFromTable: false, // 是否从表格中选择
      prescriptionId: null,
      rules: {
        diagnosis: [{ required: true, message: '请输入诊断', trigger: 'blur' }],
      }
    }
  },
  created() {
    if(this.user.role === 'doctor') {
      this.isDoctor = true;
      this.load();
      this.loadPatientsTable();
      this.loadDoctors();
      this.listPrescription();
    } else {
      this.$message.error("您非医生，无法添加病历")
    }
  },
  methods: {
    // 获取医生信息
    loadDoctors() {
      request.get('/user/doctor').then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.doctorData = res.data
          // console.log(this.doctorData)
        }
      })
    },
    // 读取患者信息
    loadPatients() {
      this.patientData = this.userData.filter(user => user.role === 'patient');
    },
    load() {
      request.get('/user/page', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.userData = res.data.list
          this.total = res.data.total
          // console.log(JSON.stringify(this.userData))
          this.loadPatients()
          this.generateOptions(this.patientData, 'patient')
        }
      })
    },
    // 获取患者信息
    loadPatientsTable() {
      request.get('/user/patient', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.patientTableData = res.data.list
          // console.log(JSON.stringify(this.patientTableData))
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    // 处理选项
    generateOptions(data, type) {
      if (type === 'doctor') {
        this.doctorOptions = data.map(item => {
          return {
            value: item.userId.toString(),
            label: `${item.username} Id: ${item.userId}` // 拼接username和userId
          }
        })
      } else if (type === 'patient') {
        this.patientOptions = data.map(item => {
          return {
            value: item.userId.toString(),
            label: `${item.username} Id: ${item.userId}` // 拼接username和userId
          }
        })
      } else if (type === 'prescription') {
        this.prescriptionOptions = data.map(item => {
          return {
            value: item.prescriptionId.toString(),
            label: `药品名称：${item.medicationName} 开具医生： ${item.doctorName} 备注：${item.remarks}` // 拼接username和userId
          }
        })
        console.log(this.prescriptionOptions)
      }
    },
    confirmAdd() {
      // 将 patientOptions 的 value 转换成数字型数组
      console.log(this.patientValue)
      const patientValues = this.patientValue.map(item => parseInt(item, 10));
      console.log(patientValues)
      // 发送医生的userId和病人的id数组给后端
      request({
        url: '/DoctorPatient/add',
        method: 'put',
        data: {
          doctorValue: this.doctorValue,
          patientValues: patientValues
        }
      }).then(res => {

        if (res.code === '200') {
          this.$message.success('绑定成功')
        } else {
          this.$message.error(res.msg)
        }
        this.load()
      })
    },
    selectPatient() {
      this.selectedPatient = [];
      let num = Number(this.patientValue)
      try {
        for(let i = 0; i < this.patientTableData.length; i++) {
          if(this.patientTableData[i].userId === num) {
            this.selectedPatient.push(this.patientTableData[i])
          }
        }
        // console.log(this.selectedPatient)
        this.selectedPatient.forEach(item => {
          // 处理患者性别，female->女，male->男
          if(item.gender === 'female') {
            item.gender = '女'
          } else if(item.gender === 'male') {
            item.gender = '男'
          }
          // 处理患者年龄，根据dateOfBirth计算年龄
          let dateOfBirth = new Date(item.dateOfBirth)
          let now = new Date()
          item.age = now.getFullYear() - dateOfBirth.getFullYear()
          this.isSelectPatient = true;
        })
      } catch (e) {
        this.$message.error(e)
      }
    },
    // 上传前检验文件类型和大小
    beforeUpload(file){
      const isImage = file.type.startsWith('image/');
      const isLt8M = file.size / 1024 / 1024 < 8;
      if (!isImage) {
        this.$message.error('只能上传图片文件!');
        return false;
      }
      if (!isLt8M) {
        this.$message.error('上传文件大小不能超过 8MB!');
        return false;
      }
      return true;
    },
    // 清除上传文件
    handleFileRemove() {
      this.form.chart = null;
    },
    // 上传成功
    handleSuccess(response) {
      // console.log(response)
      this.form.chart = response.data.filePath;
      // console.log(this.form.chart)
      this.$message.success('上传成功');
    },
    // 上传失败
    handleError(err) {
      this.$message.error('上传失败');
    },
    listPrescription() {
      // console.log(this.doctorId)
      if(this.doctorId !== '' || this.doctorId !== null) {
        this.params.doctorId = this.doctorId
      }
      // console.log(this.params)
      request({
        url: '/prescription/list',
        method: 'get',
        params: this.params
      }).then(res => {
        if(res.code === '200') {
          this.prescriptionTable = res.data.list
          this.setDoctorName()
          this.generateOptions(this.prescriptionTable, 'prescription')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    setDoctorName() {
      // console.log(this.doctorData)
      try {
        for (let i=0; i < this.doctorData.length; i++) {
          for (let j=0; j < this.prescriptionTable.length; j++) {
            if (this.doctorData[i].doctorId === this.prescriptionTable[j].doctorId) {
              this.prescriptionTable[j].doctorName = this.doctorData[i].user.username
            }
          }
        }
        // console.log(this.prescriptionTable)
      } catch (e) {
        this.$message.error(e)
      }
    },
    // 提交表单
    onSubmit() {
      let patientUserId = Number(this.patientValue)
      if (this.prescriptionId != null && this.prescriptionId !== '') {
        this.form.prescriptionId = Number(this.prescriptionId)
      }

      try {
        for (let i = 0; i < this.doctorData.length; i++) {
          if (this.user.userId === this.doctorData[i].user.userId) {
            this.form.doctorId = this.doctorData[i].doctorId
          }
        }
        for (let i = 0; i < this.patientTableData.length; i++) {
          if (patientUserId === this.patientTableData[i].user.userId) {
            this.form.patientId = this.patientTableData[i].patientId;
          }
        }
      } catch (e) {
        this.$message.error(e)
      }

      request({
        url: '/medicalRecord/add',
        method: 'put',
        data: this.form
      }).then(res => {
        if(res.code === '200') {
          this.$message.success('添加成功')
        } else {
          this.$message.error(res.msg)
        }
      })
      console.log(this.form)
    },
    handleChange() {
      if (this.selectFromTable === false) {
        this.prescriptionId = null;
      }
    }

  }
}
</script>

<template>
  <div class="container">
    <h2 class="tittle">新增病历</h2>
    <div v-if="isDoctor">
      <!-- 患者选择 -->
      <div class="section">
        <el-form class="form">
          <el-form-item label="选择患者：" class="form-item">
            <el-select
                v-model="patientValue"
                filterable
                @change="selectPatient"
                placeholder="请选择患者"
                class="select-box">
              <el-option
                  v-for="item in patientOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <!-- 表格展示选择的患者信息以便确认 -->
      <div class="section">
        <el-table
            :data="selectedPatient"
            class="table"
        >
          <el-table-column prop="userId" label="用户ID"></el-table-column>
          <el-table-column prop="user.username" label="患者姓名"></el-table-column>
          <el-table-column prop="gender" label="性别"></el-table-column>
          <el-table-column prop="age" label="患者年龄（岁）"></el-table-column>
        </el-table>
      </div>
      <!--填写新增病例表单-->
      <el-form ref="form" v-if="isSelectPatient" :model="form" :rules="rules" label-width="80px" class="form">
        <el-form-item label="医生姓名:" class="form-item">
          <el-select v-model="this.user.username" placeholder="请选择医生" disabled class="select-box">
          </el-select>
        </el-form-item>
        <el-form-item label="UID:" class="form-item">
          <el-select v-model="this.user.userId" placeholder="user_id" disabled class="select-box">
          </el-select>
        </el-form-item>
        <el-form-item label="诊断" class="form-item" prop="diagnosis">
          <el-input type="textarea" v-model="form.diagnosis"></el-input>
        </el-form-item>
        <el-form-item label="症状" class="form-item">
          <el-input type="textarea" v-model="form.symptoms"></el-input>
        </el-form-item>
        <el-form-item label="处方" class="form-item">
          <el-input type="textarea" v-model="form.prescription"></el-input>
        </el-form-item>
        <el-form-item label="从表选择">
          <el-radio-group v-model="selectFromTable" @change="handleChange">
            <el-radio :label="false">否</el-radio>
            <el-radio :label="true">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选择处方" v-if="selectFromTable">
          <el-select v-model="prescriptionId" filterable placeholder="请选择">
            <el-option
                v-for="item in prescriptionOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="上传图像" >
          <el-upload
              action="http://localhost:9090/api/upload"
              ref="upload"
              drag
              :before-upload="beforeUpload"
              :on-success="handleSuccess"
              :on-error="handleError"
              :on-remove="handleFileRemove"
              name="file"
              accept="image/*"
              :headers="{ token: this.user.token}"
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传图像文件，且不超过8mb</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注" class="form-item">
          <el-input type="textarea" v-model="form.remarks"></el-input>
        </el-form-item>
        <el-form-item class="form-item">
          <el-button type="primary" @click="onSubmit">立即创建</el-button>
          <el-button>取消</el-button>
        </el-form-item>
      </el-form>
    </div>

  </div>
</template>

<style scoped>
/* 容器样式 */
.container {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 标题样式 */
.title {
  text-align: center;
  font-size: 2rem;
  color: #409eff;
  margin-bottom: 20px;
  font-weight: bold;
}

/* 每个部分的样式 */
.section {
  margin-bottom: 30px;
}

/* 表单项样式 */
.form-item {
  margin-bottom: 20px;
}

/* 输入框样式 */
.el-input,
.el-select {
  width: 100%;
  border-radius: 4px;
  box-sizing: border-box;
}

/* 按钮样式 */
.el-button {
  margin-right: 10px;
  font-size: 1rem;
}

/* 表格样式 */
.table {
  width: 100%;
  margin-top: 20px;
  border-radius: 8px;
}

.el-table {
  background-color: #fff;
  border-radius: 8px;
}

.el-table-column {
  text-align: center;
}

/* 选择框样式 */
.select-box {
  width: 100%;
}
</style>
