<script>

</script>

<template>
  <div>
    <h2>新增病例</h2>
    <!-- 表格 -->
    <el-table :data="assignedPatientsShow" stripe border>
      <el-table-column
          type="index"
          width="50">
      </el-table-column>
      <el-table-column prop='userId' label="id" width="70"></el-table-column>
      <el-table-column prop='user.username' label="患者姓名" width="80"></el-table-column>
      <el-table-column prop='gender' label="性别" width="50"></el-table-column>
      <el-table-column prop='age' label="年龄" width="50"></el-table-column>
      <el-table-column prop='user.phoneNumber' label="电话" ></el-table-column>
      <el-table-column prop='medicalHistory' label="用药历史" ></el-table-column>
      <el-table-column prop='allergies' label="过敏反应" ></el-table-column>
      <el-table-column prop='emergencyContact' label="紧急联系人" ></el-table-column>
      <el-table-column prop='contactPhone' label="紧急联系电话" ></el-table-column>
      <el-table-column label="操作">
        <template v-slot="scoped">
          <el-link type="primary" style="margin: 2px" @click="viewMedicalRecord(scoped.row)">查看病历</el-link>
          <el-link type="danger" style="margin: 2px" @click="deleteUserRequest(scoped.row)">从名下删除</el-link>
        </template>
      </el-table-column>
    </el-table>
  </div>

</template>

<style scoped>

</style>
