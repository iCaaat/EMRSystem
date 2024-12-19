<script>
export default  {
  name: 'Error404',
}


</script>

<template>
  <div>
    <div class="hit-the-floor">404</div>
    <div class="attention">Not Found</div>
    <div class="backToHome" @click="$router.push('/')">HOME</div>
  </div>
</template>

<style scoped>
.hit-the-floor {
  color: #fff;
  font-size: 12em;
  font-weight: bold;
  font-family: Helvetica,serif;
  text-shadow:
      0 1px 0 #ccc,
      0 2px 0 #c9c9c9,
      0 3px 0 #bbb,
      0 4px 0 #b9b9b9,
      0 5px 0 #aaa,
      0 6px 1px rgba(0,0,0,.1),
      0 0 5px rgba(0,0,0,.1),
      0 1px 3px rgba(0,0,0,.3),
      0 3px 5px rgba(0,0,0,.2),
      0 5px 10px rgba(0,0,0,.25),
      0 10px 10px rgba(0,0,0,.2),
      0 20px 20px rgba(0,0,0,.15);
}

.hit-the-floor {
  text-align: center;
  margin: 50px;
}

.attention {
  text-align: center;
  font-size: 4em;
  color: #fff;
  font-weight: bold;
  font-family: Helvetica,serif;
  text-shadow:
      0 1px 0 #ccc,
      0 2px 0 #c9c9c9,
      0 3px 0 #bbb,
      0 4px 0 #b9b9b9,
      0 5px 0 #aaa,
      0 6px 1px rgba(0,0,0,.1),
      0 0 5px rgba(0,0,0,.1),
      0 1px 3px rgba(0,0,0,.3),
      0 3px 5px rgba(0,0,0,.2),
      0 5px 10px rgba(0,0,0,.25),
      0 10px 10px rgba(0,0,0,.2),
      0 20px 20px rgba(0,0,0,.15);
}

.backToHome {
  text-align: center;
  margin: 150px;
  font-size: 2em;
  font-family: Helvetica,serif;
  text-shadow:
      0 1px 0 #ccc,
      0 1px 0 #c9c9c9,
      0 2px 0 #bbb,
      0 3px 0 #b9b9b9,
      0 4px 0 #aaa,
      0 5px 1px rgba(0,0,0,.1),
      0 0 4px rgba(0,0,0,.1),
      0 1px 2px rgba(0,0,0,.3),
      0 2px 4px rgba(0,0,0,.2),
      0 4px 8px rgba(0,0,0,.25),
      0 8px 8px rgba(0,0,0,.2),
      0 18px 18px rgba(0,0,0,.15);
}

body {
  background-color: #f1f1f1;
}
</style>
