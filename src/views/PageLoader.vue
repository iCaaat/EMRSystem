<script>
export default {
  name: "PageLoader"

}
</script>

<template>
  <!--加载-->
  <div class="loader">
    <div class="spinner">
      <img src="@/assets/images/loader.gif" alt=""/>
    </div>
  </div>
</template>

<style scoped>
/***** loader ******/
.loader {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  z-index: 999999;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--white);
}
.loader img {
  width: 80px;
}
/***** loader ******/
</style>