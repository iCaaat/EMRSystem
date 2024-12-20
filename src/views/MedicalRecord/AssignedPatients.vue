<script>
import Cookies from "js-cookie";
import request from "@/utils/request";

export default {
  name: 'AssignedPatients',
  data() {
    return {
      user: Cookies.get('user') ? JSON.parse(Cookies.get('user')) : {},
      tableData: [
      ],
      patientData: [],
      total: 0,
      params: {
        username: '',
        phoneNumber: '',
        role: '',
        email: '',
        pageSize: 13,
        pageNum: 1
      },
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      request.get('/user/page', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.tableData = res.data.list
          this.total = res.data.total
          // console.log(JSON.stringify(this.tableData))
          // this.loadRelation()
        }
      })
    },
    // 获取患者信息
    loadPatients() {
      request.get('/user/patient', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.patientData = res.data
          // console.log(JSON.stringify(this.patientData))
        }
      })
    },
    // 获取医患关系信息
    loadRelation() {
      request({
        url: '/'
      })
    }
  }
}
</script>

<template>
<div>
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
      </template>
    </el-table-column>
  </el-table>
</div>
</template>

<style scoped>

</style>
