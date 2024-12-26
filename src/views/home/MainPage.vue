<script>
import * as echarts from 'echarts';
import request from "@/utils/request";

export default {
  name: 'MainPage',
  data() {
    return {
      stats: {
        totalPatients: 2000,
        totalMedicalRecords: 5000,
        totalPrescriptions: 3500,
        totalDoctors: 150
      },
      chartData: {
        labels: ['病历', '处方', '医生', '患者'],
        values: [5000, 3500, 150, 2000]
      }
    };
  },
  methods: {
    navigateTo(route) {
      this.$router.push(route);
    },
    initChart() {
      const chart = echarts.init(this.$refs.chart);
      const option = {
        title: {
          text: '统计数据',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          bottom: '10%',
          left: 'center'
        },
        series: [
          {
            name: '数据占比',
            type: 'pie',
            radius: '50%',
            data: this.chartData.labels.map((label, index) => ({
              name: label,
              value: this.chartData.values[index]
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      chart.setOption(option);
    },
    listPrescription() {
      // console.log(this.params)
      request({
        url: '/prescription/list',
        method: 'get',
        params: this.params
      }).then(res => {
        if (res.code === '200') {
          this.prescriptionData = res.data.list
          // console.log(this.prescriptionData)
          this.generateOptions(this.prescriptionData, 'prescription')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    // 获取患者信息
    loadPatients() {
      request.get('/user/patient').then(res => {
        // console.log(res)
        if (res.code === '200') {
          this.patientData = res.data.list
          // console.log(this.patientData)
          this.generatePatientOptions()
          try {
            for (let i = 0; i < this.medicalRecordData.length; i++) {
              for (let j = 0; j < this.patientData.length; j++) {
                if (this.medicalRecordData[i].patientId === this.patientData[j].patientId) {
                  // this.medicalRecordData[i].patientName = this.patientData[j].user.username
                  this.$set(this.medicalRecordData[i], 'patientName', this.patientData[j].user.username)
                }
              }
            }
            // console.log(this.medicalRecordData)
          } catch (e) {
            this.$message.error(e)
          }
        }

      })
    },
    listMedicalRecord() {
      // console.log(this.params)
      request({
        url: '/medicalRecord/list',
        method: 'get',
        params: this.params
      }).then(res => {
        if (res.code === '200') {
          this.medicalRecordData = res.data.list
          this.loadPatients()
          this.loadDoctors()
          // console.log(this.medicalRecordData)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
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
  },
  mounted() {
    this.initChart();
  }
};
</script>

<template>
  <div class="home-page">
    <!-- 快速入口 -->
    <el-row gutter="20" class="quick-links">
      <el-col :span="6">
        <el-card shadow="hover" @click="navigateTo('/medical-records')">
          病历管理
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" @click="navigateTo('/prescriptions')">
          处方管理
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" @click="navigateTo('/users')">
          用户管理
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据统计条 -->
    <el-row gutter="20" class="stats-bar">
      <el-col :span="6">
        <el-card>
          <h3>总病人数</h3>
          <p>{{ stats.totalPatients }}</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <h3>总病历数</h3>
          <p>{{ stats.totalMedicalRecords }}</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <h3>总处方数</h3>
          <p>{{ stats.totalPrescriptions }}</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <h3>总医生数</h3>
          <p>{{ stats.totalDoctors }}</p>
        </el-card>
      </el-col>
    </el-row>

    <!-- ECharts 图表 -->
    <div class="chart-container">
      <div ref="chart" class="chart"></div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.home-page {
  padding: 20px;
}

.quick-links .el-card {
  cursor: pointer;
  text-align: center;
  font-size: 16px;
  padding: 30px 0;
}

.stats-bar .el-card {
  text-align: center;
  padding: 20px;
  background-color: #f5f5f5;
}

.chart-container {
  margin-top: 20px;
  background: #fff;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.chart {
  width: 100%;
  height: 400px;
}
</style>
