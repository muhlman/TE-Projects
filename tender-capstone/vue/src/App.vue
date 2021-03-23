<template>
  <div class="app">
    
    <top-nav 
    v-if="$route.name!=='login' && $route.name!=='register'"
    />
    
    <router-view />
  </div>
</template>

<script>

import TopNav from './components/TopNav.vue'

export default {

components: {
  TopNav
},
}
</script>


<style>
@import './assets/styles.css';

h1 {
  font-size:3vw;
}

.logo {
  padding-left: 50px;
}
  .app {
    
    height:100vh;
  }
  html, body {
  background-color:#fdf2f2 !important;
  height: 100%;
  min-height: 100vh;
  }
</style>

