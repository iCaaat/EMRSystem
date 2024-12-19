<script>
export default {
  data() {
    return {
      sizeForm: {
        gender: 'male',
        dateOfBirth: '',
        address: '',
        emergencyContact: '',
        contactPhone: '',
        medicalHistory: '',
        allergies: '',
      },
      rule: {
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        dateOfBirth: [
            { required: true, message: '请选择日期', trigger: 'change' }
        ],
        address: [
            { required: false, message: '请输入地址', trigger: 'blur' }
        ],
        emergencyContact: [
            { required: false, message: '请输入紧急联系人', trigger: 'blur' }
        ],
        contactPhone: [
            { required: false, message: '请输入联系电话', trigger: 'blur' },
            { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        medicalHistory: [
            { required: false, message: '请输入病史', trigger: 'blur' }
        ],
        allergies: [
            { required: false, message: '请输入过敏史', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    submitForm() {
      this.$confirm('确认提交？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确认提交
        // 检查信息完整性
        for(let key in this.sizeForm) {
          if(this.sizeForm[key] === '') {
            if(key === 'address' || key === 'emergencyContact' || key === 'contactPhone' || key === 'medicalHistory' || key === 'allergies') {
            } else {
              this.$message.error('请填写完整信息');
              return;
            }
          }
        }
        // 提交表单
        // console.log(JSON.stringify(this.sizeForm))
        console.log('submit!');
        this.$emit('submitChildForm', this.sizeForm);
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消提交'
        })
      })
    },
    resetForm() {
      for(let key in this.sizeForm) {
        this.sizeForm[key] = '';
      }
      this.$emit('resetChildForm', this.sizeForm);
    }
  }
};
</script>

<template>
  <el-form ref="form" :model="sizeForm" label-width="100px" size="medium" class="form-layout" :rules="rule">
    <el-form-item label="性别" prop="gender">
      <el-radio v-model="sizeForm.gender" label="male">男</el-radio>
      <el-radio v-model="sizeForm.gender" label="female">女</el-radio>
    </el-form-item>
    <el-form-item label="出生年月" prop="dateOfBirth">
      <div class="block">
        <el-date-picker
            v-model="sizeForm.dateOfBirth"
            type="month"
            placeholder="选择月">
        </el-date-picker>
      </div>
    </el-form-item>
    <el-form-item label="地址" prop="address">
      <el-input v-model="sizeForm.address" placeholder="请输入地址"></el-input>
    </el-form-item>
    <el-form-item label="紧急联系人" prop="emergencyContact">
      <el-input v-model="sizeForm.emergencyContact" placeholder="请输入紧急联系人"></el-input>
    </el-form-item>
    <el-form-item label="紧急联系电话" prop="contactPhone">
      <el-input v-model="sizeForm.contactPhone" placeholder="请输入紧急联系电话"></el-input>
    </el-form-item>

    <el-form-item label="用药历史" prop="medicalHistory">
      <el-input
          type="textarea"
          :rows="2"
          placeholder="请输入内容"
          v-model="sizeForm.medicalHistory">
      </el-input>
    </el-form-item>
    <el-form-item label="过敏反应" prop="allergies">
      <el-input
          type="textarea"
          :rows="2"
          placeholder="请输入内容"
          v-model="sizeForm.allergies">
      </el-input>
    </el-form-item>
    <el-form-item size="large">
      <el-button type="primary" @click="submitForm">提交</el-button>
      <el-button @click="resetForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>
/* 定义表单布局 */
.form-layout {
  gap: 20px; /* 每行之间的间距 */
}

.el-form-item {
  max-width: 1500px;
}

</style>
