<script>
import PatientForm from "@/components/user/PatientForm.vue";
import DoctorForm from "@/components/user/DoctorForm.vue";
import AdminForm from "@/components/user/AdminForm.vue";
import request from "@/utils/request";
import {Alert} from "element-ui";
export default {
  name: 'AddUser',
  components: {
    PatientForm,
    DoctorForm,
    AdminForm
  },
  data() {
    return {
      form: {
        username: '',
        role: '',
        email: '',
        phoneNumber: '',
        password: '',
      },
      rules: {
        username: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6位', trigger: 'blur' },
          { max: 18, message: '密码长度不能大于18位', trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ],
        email: [
          { required: false, message: '请输入邮箱', trigger: 'blur' }
        ],
        phoneNumber: [
          { required: true, message: '请输入电话号码', trigger: 'blur' },
          { validator: this.checkPhone, trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    submitForm(child) {
      for(let key in this.form) {
        if(this.form[key] === '') {
          if(key === 'email') continue;
          else {
            this.$message.error('请填写完整信息');
            return;
          }
        }
      }
      // console.log(child)
      console.log("receive！")
      let defaultForm = child;
      if (this.form.role === 'patient') {
        defaultForm.user = this.form;
        console.log(JSON.stringify(defaultForm));
        request({
          url: '/user/addPatient',
          method: 'post',
          data: defaultForm
        }).then((res) => {
          // 输出提交信息
          this.$message({
            type: 'success',
            message: '提交成功!'
          });
          console.log(res)
        });
      }
      if (this.form.role === 'doctor') {
        defaultForm.user = this.form;
        console.log(JSON.stringify(defaultForm));
        request({
          url: '/user/addDoctor',
          method: 'post',
          data: defaultForm
        }).then((res) => {
          // 输出提交信息
          this.$message({
            type: 'success',
            message: '提交成功!'
          });
          console.log(res)
        });
      }
      if (this.form.role === 'admin') {
        defaultForm.user = this.form;
        console.log(JSON.stringify(defaultForm));
        request({
          url: '/user/addAdmin',
          method: 'post',
          data: defaultForm
        }).then((res) => {
          // 输出提交信息
          this.$message({
            type: 'success',
            message: '提交成功!'
          });
          console.log(res)
          setTimeout( () => {
            location.reload();
          },1000)
        });
      }
    },
    resetForm() {
      for(let key in this.form) {
        this.form[key] = '';
      }
    },
    checkPhone(rule, value, callback) {
      if (!/^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value)) {
        callback(new Error('请输入正确的手机号'));
      }
      callback();
    }
  }
}
</script>

<template>
  <div class="container">
    <h2 class="title">添加用户</h2>
    <el-form ref="form"
             :model="form"
             label-width="100px"
             class="form-layout"
             :rules="rules">
      <el-form-item label="姓名" prop="username" class="form-item">
        <el-input v-model="form.username" placeholder="请输入姓名" class="input-box"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password" class="form-item">
        <el-input v-model="form.password" placeholder="请输入密码" show-password class="input-box"></el-input>
      </el-form-item>
      <el-form-item label="邮件" prop="email" class="form-item">
        <el-input v-model="form.email" placeholder="请输入邮件" class="input-box"></el-input>
      </el-form-item>
      <el-form-item label="电话" prop="phoneNumber" class="form-item">
        <el-input v-model="form.phoneNumber" placeholder="请输入电话" class="input-box"></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="role" class="form-item">
        <el-select v-model="form.role" placeholder="请选择角色" class="select-box">
          <el-option label="医生" value="doctor"></el-option>
          <el-option label="病人" value="patient"></el-option>
          <el-option label="管理员" value="admin"></el-option>
        </el-select>
      </el-form-item>

      <div v-if="form.role === 'patient'">
        <PatientForm @submitChildForm="submitForm" @resetChildForm="resetForm"></PatientForm>
      </div>
      <div v-else-if="form.role === 'doctor'">
        <DoctorForm @submitChildForm="submitForm" @resetChildForm="resetForm"></DoctorForm>
      </div>
      <div v-else-if="form.role === 'admin'">
        <AdminForm @submitChildForm="submitForm" @resetChildForm="resetForm"></AdminForm>
      </div>
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
