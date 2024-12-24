<script>
import Cookies from "js-cookie";
import request from "@/utils/request";

export default {
  name: "ViewPrescription",
  data() {
    return {
      user: Cookies.get('user') ? JSON.parse(Cookies.get('user')) : {},
      prescriptionTable: [],
      total: 0,
      params: {
        doctorId: '',
        medicationName: '',
        remarks: '',
        pageSize: 13,
        pageNum: 1
      },
      doctorOptions: [{
        value: '',
        label: '',
      }],
      doctorId: '',
      doctorData: [],
    }
  },
  created() {
    this.loadDoctors();
  },
  methods: {
    // 获取医生信息
    loadDoctors() {
      request.get('/user/doctor').then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.doctorData = res.data
          // console.log(this.doctorData)
          this.generateOptions(this.doctorData)
          // console.log(this.params)
          this.listPrescription()
        }
      })
    },
    // 处理选项
    generateOptions(data) {
      this.doctorOptions = data.map(item => {
        return {
          value: item.doctorId.toString(),
          label: `${item.user.username} Id: ${item.userId}` // 拼接username和userId
        }
      })
    },
    reset() {
      this.params = {
        doctorId: '',
        medicationName: '',
        remarks: '',
        pageSize: 13,
        pageNum: 1
      }
      this.listPrescription()
    },
    deleteUserRequest(row) {
      console.log(row)
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {

        request.delete('/prescription/delete/' + row.prescriptionId).then(res => {
          if (res.code === '200') {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.listPrescription()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败!'
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 处理分页
    handleCurrentPageChange(pageNum) {
      this.params.pageNum = pageNum
    },
    // 处理时间格式函数
    formatDate() {
      try {
        for(let i = 0; i < this.prescriptionTable.length; i++) {
          const date = new Date(this.prescriptionTable[i].createdAt);
          // 检查时间是否有效
          if (isNaN(date.getTime())){
            this.$message.error('时间格式不正确');
          }

          const year = date.getFullYear();
          const month = String(date.getMonth() + 1).padStart(2, '0');
          const day = String(date.getDate()).padStart(2, '0');
          const hours = String(date.getHours()).padStart(2, '0');
          const minutes = String(date.getMinutes()).padStart(2, '0');

          this.prescriptionTable[i].createdAt = `${year}年${month}月${day}日${hours}:${minutes}`;
        }
      } catch (e) {
        this.$message.error(e)
      }
    },
    // 根据doctorId查询医生姓名插入表中
    setDoctorName() {
      // console.log(this.doctorData)
      try {
        for (let i=0; i < this.doctorData.length; i++) {
          for (let j=0; j < this.prescriptionTable.length; j++) {
            if (this.doctorData[i].doctorId === this.prescriptionTable[j].doctorId) {
              this.prescriptionTable[j].doctorName = this.doctorData[i].user.username
            }
          }
        }
      } catch (e) {
        this.$message.error(e)
      }
    },
    listPrescription() {
      if(this.doctorId !== '' || this.doctorId !== null) {
        this.params.doctorId = this.doctorId
      }
      // console.log(this.params)
      request({
        url: '/prescription/list',
        method: 'get',
        params: this.params
      }).then(res => {
        if(res.code === '200') {
          this.prescriptionTable = res.data.list
          this.formatDate()
          this.setDoctorName()
          // console.log(this.prescriptionTable)
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<template>
  <div>
    <h2>查询处方</h2>

    <!-- 搜索表单 -->
    <div style="margin-bottom: 20px">
      <el-input style="width: 240px;" placeholder="搜索用药名称" v-model="params.medicationName" ></el-input>
      <el-input style="width: 240px; margin-left: 5px;" placeholder="搜索备注" v-model="params.remarks"></el-input>
      <el-select v-model="doctorId" filterable placeholder="搜索开具医生" style="width: 240px; margin-left: 5px;">
        <el-option
            v-for="item in doctorOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
            :disabled="item.disabled">
        </el-option>
      </el-select>
      <el-button style="margin-left: 5px;" type="primary" @click="listPrescription"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px;" type="warning" @click="reset"><i class="el-icon-refresh"></i>重置</el-button>
    </div>

    <!--表格-->
    <el-table :data="prescriptionTable" stripe border>
      <el-table-column prop='doctorId' label="医生编号" width="80"></el-table-column>
      <el-table-column prop='doctorName' label="医生姓名" width="80"></el-table-column>
      <el-table-column prop='medicationName' label="药品名称"></el-table-column>
      <el-table-column prop='dosage' label="剂量"></el-table-column>
      <el-table-column prop='frequency' label="用药频率" ></el-table-column>
      <el-table-column prop='duration' label="持续时间" ></el-table-column>
      <el-table-column prop='instructions' label="用药说明" ></el-table-column>
      <el-table-column prop='remarks' label="备注" ></el-table-column>
      <el-table-column prop='createdAt' label="开具时间" ></el-table-column>
      <el-table-column label="操作">
        <template v-slot="scoped">
          <el-link type="danger" style="margin: 2px" @click="deleteUserRequest(scoped.row)">删除处方</el-link>
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
  </div>
</template>

<style scoped>

</style>
