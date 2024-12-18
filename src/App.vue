<template>
  <div id="app" v-loading="isLoading">
    <!--<AppContainer></AppContainer>-->
    <router-view></router-view>
  </div>
</template>

<style>

</style>

<script>
import PageLoader from "@/views/PageLoader.vue";

import '@/assets/css/loading.css'
import AppContainer from "@/views/AppContainer.vue";

export default {
  data() {
    return {
      isLoading: true,
    }
  },
  components: {
    AppContainer,
    PageLoader
  },
  methods: {
  },
  mounted() {
    this.isLoading = false
  }
}

</script>
