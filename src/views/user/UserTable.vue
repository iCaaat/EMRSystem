<script>
import request from "@/utils/request";
import dayjs from 'dayjs';
import Cookies from "js-cookie";

export default {
  name: 'AppBodyTable',
  data() {
    return {
      user: Cookies.get('user') ? JSON.parse(Cookies.get('user')) : {},
      tableData: [
      ],
      rawTableDate: [],
      rowId: 0, // 当前选中行的id
      patientData: [],
      doctorData: [],
      adminData: [],
      total: 0,
      params: {
        username: '',
        phoneNumber: '',
        email: '',
        pageSize: 13,
        pageNum: 1
      },
      // 角色选择
      roleOptions: [
        {value: 'admin', label: '管理员'},
        {value: 'patient', label: '患者'},
        {value: 'doctor', label: '医生'}
      ],
      // 修改规则
      updateRules: {
        'user.phoneNumber': [
          { required: true, message: '请输入电话号码', trigger: 'blur' },
          { validator: this.checkPhone, trigger: 'blur' }
        ],
        'user.username': [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        department: [
          { required: true, message: '请输入科室', trigger: 'blur' }
        ],
        adminLevel: [
          { required: true, message: '请选择管理等级', trigger: 'change' },
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' },
        ],
        dateOfBirth: [
          { required: true, message: '请选择出生日期', trigger: 'change' },
        ],
        specialty: [
          { required: true, message: '请输入专业', trigger: 'blur' },
        ],
        experienceYears: [
          { required: true, message: '请输入工作年限', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min:6, max: 18, message: '密码为6~18位', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min:6, max: 18, message: '密码为6~18位', trigger: 'blur' }
        ],
        'user.email': [
          { required: false, message: '请输入邮箱', trigger: 'blur' },
          { validator: this.checkEmail, trigger: 'blur' }
        ]
      },
      // 管理等级选择
      adminLevelOptions: [
        {value: 'normal', label: '普通管理员'},
        {value: 'super', label: '超级管理员'}
      ],
      // 控制详细信息对话框显示
      dialogTableVisible: false,
      // 控制修改界面对话框显示
      updateDialogFormVisible: false,
      // 控制修改密码界面对话框显示
      changePassDialogFormVisible: false,
      // 存储选中的用户详细信息
      selectedUser: null,
      // 存储临时数据
      tempData: {
        user: { username: '', email: '', password: '', phoneNumber: ''}
      },
      // 修改密码表单
      changePassForm: {},
      // rawSelectedUser: null, // 用于显示的选中的用户详细信息
      // 控制详细信息对话框角色显示
      patientDetailBool: false,
      doctorDetailBool: false,
      adminDetailBool: false,
      // 控制详细信息对话框表单
      patientDetailForm: false,
      doctorDetailForm: false,
      adminDetailForm: false,
    }
  },
  created() {
    this.load();
    this.loadPatients();
    this.loadDoctors();
    this.loadAdmins();
  },
  updated() {
    this.loadPatients();
    this.loadDoctors();
    this.loadAdmins();
    this.handleDisplayTable()
    this.handleDisplaySelectedUser()
    // console.log(JSON.stringify(this.tableData))
  },
  methods: {
    load() {
      request.get('/user/page', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.tableData = res.data.list
          this.total = res.data.total
          // console.log(JSON.stringify(this.tableData))
        }
      })
    },
    // 获取患者信息
    loadPatients() {
      request.get('/user/patient', {params: this.params}).then(res => {

        if (res.code === '200') {
          this.patientData = res.data
          // console.log(JSON.stringify(this.patientData))
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    // 获取医生信息
    loadDoctors() {
      request.get('/user/doctor', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.doctorData = res.data
          // console.log(JSON.stringify(this.doctorData))
        }
      })
    },
    // 获取管理员信息
    loadAdmins() {
      request.get('/user/admin', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.adminData = res.data
          // console.log(JSON.stringify(this.adminData))
        }
      })
    },
    reset() {
      this.params = {
        username: '',
        phoneNumber: '',
        pageSize: 13,
        pageNum: 1
      }
      this.load()
    },
    // 处理展示信息
    handleDisplayTable() {
      if (this.tableData === null || this.tableData.length === 0) {
        return
      }
      this.rawTableDate = this.tableData;
      try {
        for (let i = 0; i < this.tableData.length; i++) {
          if (this.tableData[i].role === 'admin') {
            this.tableData[i].role = '管理员'
          } else if (this.tableData[i].role === 'patient') {
            this.tableData[i].role = '患者'
          } else if (this.tableData[i].role === 'doctor') {
            this.tableData[i].role = '医生'
          }
          let date = new Date(this.tableData[i].createdAt)
          this.tableData[i].createdAt = dayjs(date).format('YYYY-MM-DD HH:mm:ss')
          date = new Date(this.tableData[i].lastLoginAt)
          this.tableData[i].lastLoginAt = dayjs(date).format('YYYY-MM-DD HH:mm:ss')
        }
      } catch (e) {
        console.log(e)
      }
    },
    handleDisplaySelectedUser() {
      if(this.selectedUser === null || this.selectedUser.length === 0) {
        return
      }
      if (this.selectedUser.user.role === 'patient') {
        this.selectedUser.user.role = '患者';
        this.selectedUser.gender = this.selectedUser.user.gender === 'male' ? '男' : '女'
        let dateOfBirth = new Date(this.selectedUser.dateOfBirth)
        this.selectedUser.dateOfBirth = dayjs(dateOfBirth).format('YYYY-MM-DD')

        this.patientDetailBool = true
        this.doctorDetailBool = false
        this.adminDetailBool = false
      } else if (this.selectedUser.user.role === 'doctor') {
        this.selectedUser.user.role = '医生'
        this.selectedUser.gender = this.selectedUser.user.gender === 'male' ? '男' : '女'

        this.patientDetailBool = false
        this.doctorDetailBool = true
        this.adminDetailBool = false
      } else if (this.selectedUser.user.role === 'admin') {
        this.selectedUser.user.role = '管理员'
        this.selectedUser.adminLevel = this.selectedUser.adminLevel === 'super' ? '超级管理员' : '普通管理员'

        this.patientDetailBool = false
        this.doctorDetailBool = false
        this.adminDetailBool = true
      }
      let date = new Date(this.selectedUser.user.createdAt)
      this.selectedUser.user.createdAt = dayjs(date).format('YYYY-MM-DD HH:mm:ss')
      date = new Date(this.selectedUser.user.lastLoginAt)
      this.selectedUser.user.lastLoginAt = dayjs(date).format('YYYY-MM-DD HH:mm:ss')
    },
    // 点击分页
    handleCurrentPageChange(pageNum) {
      this.params.pageNum = pageNum
      this.load()
    },
    // // 点击每页显示多少条
    // handleSizeChange(pageSize) {
    //   this.params.pageSize = pageSize
    //   this.load()
    // },
    // 查看详细
    viewDetails(rowUser) {
      // console.log(JSON.stringify(rowUser))
      // console.log(rowUser.userId)
      if (rowUser.role === '患者') {
        try {
          for (let i = 0; i < this.patientData.length; i++) {
            // console.log(this.patientData[i].userId)
            if (this.patientData[i].userId === rowUser.userId) {
              this.selectedUser = this.patientData[i]
              // console.log(JSON.stringify(this.selectedUser))
              break
            }
          }
        } catch (e) {
          console.error(e)
        }
        this.dialogTableVisible = true;
      }
      if (rowUser.role === '医生') {
        try {
          for (let i = 0; i < this.doctorData.length; i++) {
            // console.log(JSON.stringify(this.doctorData[i].userId))
            if (this.doctorData[i].userId === rowUser.userId) {
              this.selectedUser = this.doctorData[i]
              // console.log(JSON.stringify(this.selectedUser))
              break
            }
          }
        } catch (e) {
          console.error(e)
        }
        this.dialogTableVisible = true;
      }
      if (rowUser.role === '管理员') {
        try {
          for (let i = 0; i < this.adminData.length; i++) {
            // console.log(JSON.stringify(this.adminData[i].userId))
            if (this.adminData[i].userId === rowUser.userId) {
              this.selectedUser = this.adminData[i]
              // console.log(JSON.stringify(this.selectedUser))
              break
            }
          }
        } catch (e) {
          console.error(e)
        }
        this.dialogTableVisible = true;
      }
    },
    // 查看修改表单
    viewUpdateForm(rowUser) {
      // console.log(this.patientData)
      // console.log(rowUser)
      if (rowUser.role === '患者') {
        this.patientDetailForm = true
        this.doctorDetailForm = false
        this.adminDetailForm = false
        this.updateDialogFormVisible = true;
        try {
          for (let i = 0; i < this.patientData.length; i++) {
            // console.log(this.patientData[i])
            if (this.patientData[i].userId === rowUser.userId) {
              // 将this.patientData[i]拷贝到this.tempData
              // console.log(this.patientData[i])
              this.tempData = {...this.patientData[i]};
              this.selectedUser = {...this.patientData[i]};
              // console.log(this.selectedUser)
              // console.log(this.tempData)
              break
            }
          }
        } catch (e) {
          console.error(e)
        }
      }
      if (rowUser.role === '医生') {
        this.patientDetailForm = false
        this.doctorDetailForm = true
        this.adminDetailForm = false
        this.updateDialogFormVisible = true;
        try {
          for (let i = 0; i < this.doctorData.length; i++) {
            // console.log(JSON.stringify(this.adminData[i].userId))
            if (this.doctorData[i].userId === rowUser.userId) {
              this.tempData = {...this.doctorData[i]};
              this.selectedUser = {...this.doctorData[i]};
              // console.log(JSON.stringify(this.tempData))
              break
            }
          }
        } catch (e) {
          console.error(e)
        }
      }
      if (rowUser.role === '管理员') {
        this.patientDetailForm = false
        this.doctorDetailForm = false
        this.adminDetailForm = true
        this.updateDialogFormVisible = true;
        try {
          for (let i = 0; i < this.adminData.length; i++) {
            // console.log(JSON.stringify(this.adminData[i].userId))
            if (this.adminData[i].userId === rowUser.userId) {
              this.tempData = {...this.adminData[i]};
              this.selectedUser = {...this.adminData[i]};
              // console.log(JSON.stringify(this.tempData))
              break
            }
          }
        } catch (e) {
          console.error(e)
        }
      }
    },
    // 删除角色请求
    deleteUserRequest(rowUser) {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        console.log(JSON.stringify(rowUser))
        rowUser.createdAt = '';
        rowUser.lastLoginAt = '';
        request({
          url: '/user/deleteUserByRole',
          method: 'post',
          data: rowUser
        }).then(res => {
          if (res.code === '200') {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
          } else {
            this.$message.error(res.msg);
          }
          this.load();
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 修改角色请求
    updateUserRequest() {
      // console.log(JSON.stringify(this.selectedUser))
      // console.log(JSON.stringify(this.tempData))
      this.selectedUser = {...this.tempData}

      this.selectedUser.user.createdAt = null;
      this.selectedUser.user.lastLoginAt = null;
      this.selectedUser.updatedAt = Date.now();

      this.$confirm('请确认修改信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$refs['updateForm'].validate((valid) => {
          if (valid) {
            let requestUrl
            if(this.selectedUser.user.role === '患者') {
              requestUrl= '/user/updatePatient';
            } else if(this.selectedUser.user.role === '医生') {
              requestUrl= '/user/updateDoctor';
            } else if(this.selectedUser.user.role === '管理员') {
              requestUrl= '/user/updateAdmin';
            }
            this.selectedUser.user.createdAt = null
            this.selectedUser.user.lastLoginAt = null
            console.log(JSON.stringify(this.selectedUser))
            request({
              url: requestUrl,
              method: 'post',
              data: this.selectedUser
            }).then(res => {
              if (res.code === '200') {
                this.$message({
                  type: 'success',
                  message: '修改成功!'
                });
                this.updateDialogFormVisible = false;
                this.tempData = this.selectedUser;
                this.load();
              } else {
                this.$message({
                  type: 'error',
                  message: '修改失败!'
                })
              }
            })
          }
        })
      }).catch( () => {
        this.$message({
          type: 'info',
          message: '已取消修改'
        });
      })
    },
    // 打开修改密码对话框
    viewChangePasswordForm(row) {
      this.changePassForm = JSON.parse(JSON.stringify(row));
      // console.log(this.changePassForm);
      this.changePassDialogFormVisible = true;

    },
    // 修改密码请求
    changePassRequest() {
      console.log(this.changePassForm);
      this.$refs['changePassForm'].validate((valid) => {
        if (valid) {
          request.put('/user/updatePassword', this.changePassForm).then(res => {
            if(res.code === '200') {
              this.$notify.success("修改密码成功")
              if (this.user.userId === this.changePassForm.userId) {
                Cookies.remove('user')
                this.$router.push('/login');
              }
              this.load();
              this.changePassDialogFormVisible = false;
            } else {
              this.$notify.error("修改密码失败")
            }
          })
        }
      })
    },
    // 修改用户状态
    changeStatus(row) {
      if (this.user.userId === row.userId && !row.status) {
        row.status = true
        this.$notify.error("不能修改自己的状态")
        return
      }
      let temp = row
      temp.createdAt = null
      temp.lastLoginAt = null
      request.put('/user/updateUser', row).then(res => {
        if(res.code === '200') {
          this.$notify.success("修改用户状态成功")
          this.load();
        } else {
          this.$notify.error(res.msg)
        }
      })
    },

    // 表单验证
    checkPhone(rule, value, callback) {
      // console.log(value)
      if (!/^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value)) {
        callback(new Error('请输入正确的手机号'));
      }
      callback();
    },
    checkEmail(rule, value, callback) {
      // console.log(value);
      if (!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value)) {
        callback(new Error('请输入正确的邮箱'));
      }
      callback();
    },
    checkPassword(rule, value, callback) {
      console.log(value);
    }
  }
}
</script>

<template>
<div>
  <!-- 搜索表单 -->
  <div style="margin-bottom: 20px">
    <el-input style="width: 240px;" placeholder="输入名称" v-model="params.username" ></el-input>
    <el-input style="width: 240px; margin-left: 5px;" placeholder="请输入电话" v-model="params.phoneNumber"></el-input>
    <el-input style="width: 240px; margin-left: 5px;" placeholder="请输入邮件" v-model="params.email"></el-input>
    <el-select v-model="params.role" placeholder="筛选角色" style="width: 240px; margin-left: 5px;">
      <el-option
          v-for="item in roleOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
          :disabled="item.disabled">
      </el-option>
    </el-select>
    <el-button style="margin-left: 5px;" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
    <el-button style="margin-left: 5px;" type="warning" @click="reset"><i class="el-icon-refresh"></i>重置</el-button>
  </div>

  <!-- 表格 -->
  <el-table :data="tableData" stripe border>
    <el-table-column
        type="index"
        width="50">
    </el-table-column>
    <el-table-column prop='userId' label="id" width="100"></el-table-column>
    <el-table-column prop='username' label="姓名" width="100"></el-table-column>
    <el-table-column prop='role' label="角色" width="80"></el-table-column>
    <el-table-column prop='email' label="邮箱" ></el-table-column>
    <el-table-column prop='phoneNumber' label="电话" ></el-table-column>
    <el-table-column prop='createdAt' label="创建于" ></el-table-column>
    <el-table-column prop='lastLoginAt' label="最后登录于" ></el-table-column>
    <el-table-column label="操作">
      <template v-slot="scoped">
        <el-link type="primary" style="margin: 2px" @click="viewDetails(scoped.row)">详细</el-link>
        <el-link type="warning" style="margin: 2px" @click="viewUpdateForm(scoped.row)">修改</el-link>
        <el-link type="danger" style="margin: 2px" @click="deleteUserRequest(scoped.row)">删除</el-link>
        <el-link type="warning" style="margin: 2px" @click="viewChangePasswordForm(scoped.row)">修改密码</el-link>
      </template>
    </el-table-column>
    <el-table-column label="状态" width="70">
      <template v-slot="scoped">
        <el-switch
            v-model="scoped.row.status"
            @change="changeStatus(scoped.row)"
            active-color="#13ce66"
            inactive-color="#ff4949">
        </el-switch>
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

  <!--查看详细-->
  <el-dialog title="详细信息" :visible.sync="dialogTableVisible" width="60%">
      <el-descriptions title="用户信息" direction="vertical" :column="4" border v-if="patientDetailBool">
        <el-descriptions-item label="姓名">{{ selectedUser?.user.username || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ selectedUser?.gender || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ selectedUser?.user.role || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ selectedUser?.dateOfBirth || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ selectedUser?.user.phoneNumber || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="邮件">{{ selectedUser?.user.email || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ selectedUser?.address || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系人">{{ selectedUser?.emergencyContact || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系电话">{{ selectedUser?.contactPhone || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ selectedUser?.user.createdAt || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="最后登录时间">{{ selectedUser?.user.lastLoginAt || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="用药历史">{{ selectedUser?.medicalHistory || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="过敏反应">{{ selectedUser?.allergies || '未知' }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="用户信息" direction="vertical" :column="4" border v-if="doctorDetailBool">
        <el-descriptions-item label="姓名">{{ selectedUser?.user.username || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ selectedUser?.gender || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ selectedUser?.user.role || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ selectedUser?.user.phoneNumber || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="邮件">{{ selectedUser?.user.email || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ selectedUser?.department || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="从业年限">{{ selectedUser?.experienceYears || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="专长">{{ selectedUser?.specialty || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="资格证书">{{ selectedUser?.qualification || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ selectedUser?.user.createdAt || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="最后登录时间">{{ selectedUser?.user.lastLoginAt || '未知' }}</el-descriptions-item>

      </el-descriptions>
      <el-descriptions title="用户信息" direction="vertical" :column="3" border v-if="adminDetailBool">
        <el-descriptions-item label="姓名">{{ selectedUser?.user.username || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ selectedUser?.adminLevel || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ selectedUser?.user.phoneNumber || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="邮件">{{ selectedUser?.user.email || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ selectedUser?.department || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ selectedUser?.user.createdAt || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="最后登录时间">{{ selectedUser?.user.lastLoginAt || '未知' }}</el-descriptions-item>

      </el-descriptions>
  </el-dialog>

  <!-- 修改界面 -->
  <el-dialog title="修改用户" :visible.sync="updateDialogFormVisible" width="60%" >
    <el-form label-width="100px" ref="updateForm" :model="tempData" v-if="patientDetailForm" :rules="updateRules">
      <el-form-item label="姓名" prop="user.username">
        <el-input v-model="tempData.user.username"></el-input>
      </el-form-item>
      <!--<el-form-item label="密码" prop="password">-->
      <!--  <el-input v-model="tempData?.user.password" show-password></el-input>-->
      <!--</el-form-item>-->
      <el-form-item label="邮件" prop="user.email">
        <el-input v-model="tempData.user.email"></el-input>
      </el-form-item>
      <el-form-item label="电话" prop="user.phoneNumber">
        <el-input v-model="tempData.user.phoneNumber"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-radio v-model="tempData.gender" label="male">男</el-radio>
        <el-radio v-model="tempData.gender" label="female">女</el-radio>
      </el-form-item>
      <el-form-item label="出生日期" prop="dateOfBirth">
          <el-date-picker
              v-model="tempData.dateOfBirth"
              type="month"
              placeholder="选择月">
          </el-date-picker>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="tempData.address"></el-input>
      </el-form-item>
      <el-form-item label="紧急联系人">
        <el-input v-model="tempData.emergencyContact"></el-input>
      </el-form-item>
      <el-form-item label="紧急联系电话">
        <el-input v-model="tempData.contactPhone"></el-input>
      </el-form-item>
      <el-form-item label="用药历史">
        <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="tempData.medicalHistory">
        </el-input>
      </el-form-item>
      <el-form-item label="过敏反应">
        <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="tempData.allergies">
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="updateUserRequest">提交</el-button>
        <el-button @click="updateDialogFormVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
    <el-form label-width="100px" ref="updateForm" :model="tempData" v-if="doctorDetailForm" :rules="updateRules">
      <el-form-item label="姓名" prop="user.username">
        <el-input v-model="tempData.user.username"></el-input>
      </el-form-item>
      <!--<el-form-item label="密码" prop="password">-->
      <!--  <el-input v-model="tempData?.user.password" show-password></el-input>-->
      <!--</el-form-item>-->
      <el-form-item label="邮件" prop="user.email">
        <el-input v-model="tempData.user.email"></el-input>
      </el-form-item>
      <el-form-item label="电话" prop="user.phoneNumber">
        <el-input v-model="tempData.user.phoneNumber"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-radio v-model="tempData.gender" label="male">男</el-radio>
        <el-radio v-model="tempData.gender" label="female">女</el-radio>
      </el-form-item>
      <el-form-item label="部门" prop="department">
        <el-input v-model="tempData.department"></el-input>
      </el-form-item>
      <el-form-item label="专长" prop="specialty">
        <el-input v-model="tempData.specialty"></el-input>
      </el-form-item>
      <el-form-item label="工作年限" prop="experienceYears">
        <el-input-number v-model="tempData.experienceYears" :min="0" :max="70" label="工作年限"></el-input-number>
      </el-form-item>
      <el-form-item label="资格证书">
        <el-input v-model="tempData.qualification"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="updateUserRequest">提交</el-button>
        <el-button @click="updateDialogFormVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
    <el-form label-width="100px" ref="updateForm" :model="tempData" v-if="adminDetailForm" :rules="updateRules">
      <el-form-item label="姓名" prop="user.username">
        <el-input v-model="tempData.user.username"></el-input>
      </el-form-item>
      <!--<el-form-item label="密码" prop="password">-->
      <!--  <el-input v-model="tempData?.user.password" show-password></el-input>-->
      <!--</el-form-item>-->
      <el-form-item label="邮件" prop="user.email">
        <el-input v-model="tempData.user.email"></el-input>
      </el-form-item>
      <el-form-item label="电话" prop="user.phoneNumber">
        <el-input v-model="tempData.user.phoneNumber"></el-input>
      </el-form-item>
      <el-form-item label="管理等级" prop="adminLevel">
        <el-select v-model="tempData.adminLevel" placeholder="请选择">
          <el-option
              v-for="item in adminLevelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="部门" prop="department">
        <el-input v-model="tempData.department"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="updateUserRequest">提交</el-button>
        <el-button @click="updateDialogFormVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!--修改密码界面-->
  <el-dialog title="修改密码" :visible.sync="changePassDialogFormVisible" width="30%" >
    <el-form label-width="100px" ref="changePassForm" :model="changePassForm" :rules="updateRules">
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="changePassForm.newPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="changePassRequest">提交</el-button>
        <el-button @click="changePassDialogFormVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</div>
</template>

<style scoped>

</style>
