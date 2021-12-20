<template>
  <img alt="Vue logo" src="./assets/logo.png">
  <HelloWorld  msg="Welcome to Your Vue.js App"/>
  <el-button style="font-size:xx-large;color: red" @click.stop="clickme">点我</el-button>
</template>

<script>

import HelloWorld from './components/HelloWorld.vue'



export default {
  name: 'App',
  components: {
    HelloWorld
  },
  data(){
    return{
      date:'2021-12'

    }
  },
  methods:{
    clickme(){
      alert(new Date);
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
