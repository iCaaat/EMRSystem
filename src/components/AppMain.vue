<script>
import AppHeader from '@/components/AppHeader.vue';
import AppAside from '@/components/AppAside.vue';
import AppMain from '@/components/AppMain.vue';

export default {
  name: 'AppMain',
  components: {
    AppHeader,
    AppAside,
    AppMain
  }
}
</script>

<template>
  <el-main style="position: relative">
    <div>
      <router-view></router-view>
    </div>
  </el-main>
</template>

<style scoped>
.el-main {
  background-color: #c6e2ff;
  color: #333;
}
</style>
