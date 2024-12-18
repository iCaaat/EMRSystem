<script>
import AppBodyTable from "@/views/user/UserTable.vue";

export default {
  name: 'AppContainer',
  components: {AppBodyTable},
  data() {
    return {
      asideWidth: 250  // 默认宽度
    }
  },
}
</script>

<template>
  <!--布局容器-->
  <el-container style="height: 100vh; min-height: 100px; border: 1px solid #c6e2ff">
    <!--   边栏   -->
    <el-aside :style="{ width: asideWidth + 'px' }" class="aside-container">
      <AppAside :width="asideWidth" @changeWidth="asideWidth = $event"></AppAside>
    </el-aside>


    <el-container>
      <!-- 布局头部 -->
      <el-header height="auto">
        <AppHeader></AppHeader>
      </el-header>

      <!--主体-->
      <el-main class="main-container">
        <AppMain></AppMain>
      </el-main>
    </el-container>

  </el-container>
</template>

<style scoped>
.main-container {
  background-color: #c6e2ff;
  flex-grow: 1; /* 主体占据剩余空间 */
  padding: 0;
}
.aside-container {
  background-color: #b3d8ff;
  width: auto; /* 允许宽度由内容决定 */
  min-width: 60px; /* 防止内容过少导致太窄 */
  flex-shrink: 0; /* 防止侧边栏被压缩 */
  display: flex;
}

.el-header, .el-footer {
  background-color: #409EFF;
  color: #333;
  text-align: center;
  line-height: 80px;
  top: 0;
  left: 0;
  width: 100%; /* 宽度100% */
  z-index: 1000; /* 确保在内容上方 */
  padding: 0 1px; /* 内边距，避免内容贴边 */
}
</style>
