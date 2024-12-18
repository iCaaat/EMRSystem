<script>
import PatientForm from "@/views/user/PatientForm.vue";
import DoctorForm from "@/views/user/DoctorForm.vue";
import AdminForm from "@/views/user/AdminForm.vue";
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
          { required: true, message: '请输入密码', trigger: 'blur' }
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
  <div>
    <h3>添加用户</h3>
    <el-form ref="form"
             :model="form"
             label-width="100px"
             class="form-layout"
             :rules="rules">
      <el-form-item label="姓名" prop="username">
        <el-input v-model="form.username" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="邮件" prop="email">
        <el-input v-model="form.email" placeholder="请输入邮件"></el-input>
      </el-form-item>
      <el-form-item label="电话" prop="phoneNumber">
        <el-input v-model="form.phoneNumber" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="role">
        <el-select v-model="form.role" placeholder="请选择角色">
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
/* 定义表单布局 */
.form-layout {
  gap: 20px; /* 每行之间的间距 */
}

.el-form-item {
  max-width: 1200px;
}
</style>
