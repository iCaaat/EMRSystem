<script>
import Cookies from "js-cookie";
import request from "@/utils/request";

export default {
  name: 'AddPrescription',
  data() {
    return {
      user: Cookies.get('user') ? JSON.parse(Cookies.get('user')) : {},
      form: {},
      formRules:{
        medicationName: [
          { required: true, message: '请输入药品名称', trigger: 'blur' }
        ],
        dosage: [
          { required: false, message: '请输入剂量', trigger: 'blur' },
        ],
        frequency: [
          { required: false, message: '请输入频率', trigger: 'blur' }
        ],
        duration: [
          { required: false, message: '请输入持续时间', trigger: 'blur' },
        ],
        instruction: [
          { required: false, message: '请输入注意事项', trigger: 'blur' },
        ],
        remarks: [
          { required: false, message: '请输入备注', trigger: 'blur' },
        ]
      },
      isDoctor: false,
    }
  },
  created() {
    if (this.user.role === 'doctor') {
      this.isDoctor = true
    } else {
      this.$message.error('您不是医生，无法新增处方')
    }
  },
  methods: {
    // 提交表单
    onSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.form.doctorUserId = this.user.userId
          if (this.form.dosage === '' || this.form.dosage === undefined) {
            this.form.dosage = "按说明书进行服用"
          }
          if (this.form.frequency === '' || this.form.frequency === undefined) {
            this.form.frequency = "按说明书进行服用"
          }
          if (this.form.duration === '' || this.form.duration === undefined) {
            this.form.duration = "按说明书进行服用"
          }
          if (this.form.instructions === '' || this.form.instructions === undefined) {
            this.form.instructions = "按说明书进行服用"
          }
          if (this.form.remarks === '' || this.form.remarks === undefined) {
            this.form.remarks = "无"
          }
          console.log(this.form)
          request({
            url: '/prescription/add',
            method: 'put',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success("新增成功");
            } else {
              this.$message.error(res.msg)
            }
          })
        } else {
          // 表单验证未通过
          this.$message.error("未填写必须信息");
        }
      })

    }

  }
}
</script>

<template>
  <div class="container">
    <h2>新增处方</h2>
    <el-form ref="form" :model="form" label-width="100px" :rules="formRules" v-if="isDoctor" class="form">
      <el-form-item label="医生姓名:" class="form-item">
        <el-select v-model="this.user.username" placeholder="请选择医生" disabled class="select-box">
        </el-select>
      </el-form-item>
      <el-form-item label="UID:" class="form-item">
        <el-select v-model="this.user.userId" placeholder="user_id" disabled class="select-box">
        </el-select>
      </el-form-item>
      <el-form-item label="药品名称"  prop="medicationName" class="form-item">
        <el-input type="textarea" v-model="form.medicationName" placeholder="请填写药品名称" class="input-box"></el-input>
      </el-form-item>
      <el-form-item label="剂量" prop="dosage" class="form-item">
        <el-input type="textarea" v-model="form.dosage" placeholder="默认为按说明书进行服用" class="input-box"></el-input>
      </el-form-item>
      <el-form-item label="用药频率" prop="frequency" class="form-item">
        <el-input type="textarea" v-model="form.frequency" placeholder="默认为按说明书进行服用" class="input-box"></el-input>
      </el-form-item>
      <el-form-item label="持续时间"  prop="duration" class="form-item">
        <el-input type="textarea" v-model="form.duration" placeholder="默认为按说明书进行服用" class="input-box"></el-input>
      </el-form-item>
      <el-form-item label="用药说明"  prop="instructions" class="form-item">
        <el-input type="textarea" v-model="form.instructions" placeholder="默认为按说明书进行服用" class="input-box"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="remarks" class="form-item">
        <el-input type="textarea" v-model="form.remarks" placeholder="默认为无" class="input-box"></el-input>
      </el-form-item>

      <el-form-item class="form-item">
        <el-button type="primary" @click="onSubmit">立即创建</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
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
  margin-bottom: 30px;
  font-weight: bold;
}

/* 表单项样式 */
.form-item {
  margin-bottom: 20px;
}

/* 输入框样式 */
.input-box,
.select-box {
  width: 100%;
  border-radius: 4px;
  box-sizing: border-box;
  margin-bottom: 10px;
}

/* 按钮样式 */
.el-button {
  margin-right: 10px;
  font-size: 1rem;
}

/* 修改文本框宽度 */
.el-input__inner {
  padding: 10px;
}

/* 设置文本框为多行 */
.el-input__inner::placeholder {
  color: #bbb;
  font-size: 1rem;
}

/* 控制按钮间距 */
.el-button {
  margin-top: 20px;
}

/* 调整表单的宽度 */
.el-form {
  width: 100%;
}
</style>
