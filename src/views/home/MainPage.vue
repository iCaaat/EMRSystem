<script>
import * as echarts from 'echarts';
import request from "@/utils/request";

export default {
  name: 'MainPage',
  data() {
    return {
      stats: {
        totalPatients: 1,
        totalMedicalRecords: 1,
        totalPrescriptions: 1,
        // totalDoctors: 1,
        totalUsers: 1,
      },
      chartData: {
        labels: ['病历', '处方', '医生', '患者'],
        values: [5000, 3500, 150, 2000]
      },
      prescriptionData: [],
      patientData: [],
      medicalRecordData: [],
      userData: [],
      total: 0,
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
          this.$set(this.stats, "totalPrescriptions", this.prescriptionData.length)
          // console.log(this.prescriptionData)
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
          this.$set(this.stats, "totalPatients", this.patientData.length)
          console.log(this.patientData)
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
          this.$set(this.stats, "totalMedicalRecords", this.medicalRecordData.length)
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
          this.userData = res.data.list
          this.total = res.data.total
          this.$set(this.stats, "totalUsers", this.userData.length)
          // console.log(this.userData)
        }
      })
    },
  },
  mounted() {
    this.initChart();
    this.load();
    this.listPrescription();
    this.listMedicalRecord();
    this.loadPatients();
  }
};
</script>

<template>
  <div class="home-page">
    <!-- 快速入口 -->
    <div class="quick-access">
      <el-row>
        <el-col :span="8">
          <el-tag
              type="success"
              size="large"
              class="quick-tag"
              @click="goToPage('/medical-records')"
          >
            病历管理
          </el-tag>
        </el-col>
        <el-col :span="8">
          <el-tag
              type="info"
              size="large"
              class="quick-tag"
              @click="goToPage('/prescriptions')"
          >
            处方管理
          </el-tag>
        </el-col>
        <el-col :span="8">
          <el-tag
              type="warning"
              size="large"
              class="quick-tag"
              @click="goToPage('/user-management')"
          >
            用户管理
          </el-tag>
        </el-col>
      </el-row>
    </div>

    <!-- 数据统计条 -->
    <div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div>
            <el-statistic
                group-separator=","
                :value="stats.totalUsers"
                title="总用户数"
            >
              <template slot="suffix">
                <i class="el-icon-user"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic title="总患者数" :value="stats.totalPatients">
              <template slot="suffix">
                <i class="el-icon-user-solid"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic
                group-separator=","
                decimal-separator="."
                :value="stats.totalMedicalRecords"
                title="总病历数"
            >
              <template slot="suffix">
                <i class="el-icon-tickets" style="color: yellow"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic :value="stats.totalPrescriptions" title="总处方数">
              <template slot="suffix">
                <i class="el-icon-edit" style="color: #53a8ff"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
      </el-row>
    </div>

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
