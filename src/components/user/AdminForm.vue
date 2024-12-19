<script>
export default {
  data() {
    return {
      sizeForm: {
        adminLevel: '',
        department: '',
      },
      rule: {
        adminLevel: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        department: [
          { required: true, message: '请输入科室', trigger: 'blur' }
        ],
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
      <el-form-item label="管理等级" prop="adminLevel">
        <el-radio v-model="sizeForm.adminLevel" label="normal">普通管理员</el-radio>
        <el-radio v-model="sizeForm.adminLevel" label="super">超级管理员</el-radio>
      </el-form-item>
      <el-form-item label="科室" prop="department">
        <el-input v-model="sizeForm.department" placeholder="请输入科室信息"></el-input>
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
