<script>
export default {
  data() {
    return {
      sizeForm: {
        gender: 'male',
        department: '',
        experienceYears: '',
        qualification: '',
        specialty: '',
      },
      rule: {
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        department: [
          { required: true, message: '请输入科室', trigger: 'blur' }
        ],
        experience_years: [
          { required: true, message: '请输入工作年限', trigger: 'blur' }
        ],
        qualification: [
          { required: false, message: '请输入资格证书', trigger: 'blur' }
        ],
        specialty: [
          { required: true, message: '请输入专长', trigger: 'blur' }
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
            if(key === 'qualification') {
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
<div>
  <el-form ref="form" :model="sizeForm" label-width="100px" size="medium" class="form-layout" :rules="rule">
    <el-form-item label="性别" prop="gender">
      <el-radio v-model="sizeForm.gender" label="male">男</el-radio>
      <el-radio v-model="sizeForm.gender" label="female">女</el-radio>
    </el-form-item>
    <el-form-item label="工作年限" prop="experience_years">
      <el-input-number v-model="sizeForm.experienceYears" :min="0" :max="70" label="工作年限"></el-input-number>
    </el-form-item>
    <el-form-item label="科室" prop="department">
      <el-input v-model="sizeForm.department" placeholder="请输入科室信息"></el-input>
    </el-form-item>

    <el-form-item label="专长" prop="specialty">
      <el-input
          type="textarea"
          :rows="2"
          placeholder="请输入内容"
          v-model="sizeForm.specialty">
      </el-input>
    </el-form-item>
    <el-form-item label="资格证书" prop="qualification">
      <el-input
          type="textarea"
          :rows="2"
          placeholder="请输入内容"
          v-model="sizeForm.qualification">
      </el-input>
    </el-form-item>
    <el-form-item size="large">
      <el-button type="primary" @click="submitForm">提交</el-button>
      <el-button @click="resetForm">重置</el-button>
    </el-form-item>
  </el-form>
</div>
</template>

<style scoped>
/* 定义表单布局 */
.form-layout {
  gap: 20px; /* 每行之间的间距 */
}

.el-form-item {
  max-width: 1200px;
}
</style>
