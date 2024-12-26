<script>
import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
  name: 'ChangePassword',
  data() {
    return {
      user: Cookies.get("user") ? JSON.parse(Cookies.get("user")): {},
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      rules: {
        oldPassword: [
          { required: true, message: '请输入旧密码', trigger: 'blur' },
          { min:6, max: 18, message: '密码长度在 6 到 18 个字符', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min:6, max: 18, message: '密码长度在 6 到 18 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { min:6, max: 18, message: '密码长度在 6 到 18 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    reset() {
      this.passwordForm = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    },
    submit() {
      this.$refs.passwordForm.validate((valid) => {
        if (valid) {
          this.$confirm('确定修改密码吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            if (this.user.userId === '' || this.user.userId === null) {
              this.$message.error("读取用户信息失败，请重新登陆")
            } else if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
              this.$message.error('新密码和确认密码不一致')
            } else {
              this.passwordForm.userId = this.user.userId
              console.log(this.passwordForm)
              request({
                url: '/user/changePassword',
                method: 'put',
                data: this.passwordForm
              }).then(res => {
                if (res.code === '200')
                {
                  this.$message.success('修改密码成功')
                  Cookies.remove('user')
                  this.$router.push('/login')
                } else {
                  this.$message.error(res.msg)
                }
              })
            }
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消修改'
            })
          })
        }
      })

    }
  }
}
</script>

<template>
  <div class="password-container">
    <h2 class="password-title">修改密码</h2>

    <el-form label-width="120px" :model="passwordForm" :rules="rules" ref="passwordForm" class="password-form">
      <el-form-item label="旧密码">
        <el-input type="password" v-model="passwordForm.oldPassword" placeholder="请输入旧密码"></el-input>
      </el-form-item>
      <el-form-item label="新密码">
        <el-input type="password" v-model="passwordForm.newPassword" placeholder="请输入新密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码">
        <el-input type="password" v-model="passwordForm.confirmPassword" placeholder="请再次输入新密码"></el-input>
      </el-form-item>
    </el-form>

    <div class="button-group">
      <el-button type="primary" @click="submit">提交修改</el-button>
      <el-button @click="reset">重置</el-button>
    </div>
  </div>
</template>

<style scoped>
/* 主容器样式 */
.password-container {
  width: 70%;
  max-width: 600px;
  margin: 30px auto;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 标题样式 */
.password-title {
  text-align: center;
  font-size: 1.8rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

/* 表单样式 */
.password-form {
  background-color: #fafafa;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

/* 表单项样式 */
.el-form-item {
  margin-bottom: 20px;
}

/* 输入框样式 */
.el-input {
  width: 100%;
  border-radius: 4px;
}

/* 按钮组样式 */
.button-group {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
}

/* 按钮样式 */
.el-button {
  min-width: 120px;
  font-size: 1rem;
}
</style>
