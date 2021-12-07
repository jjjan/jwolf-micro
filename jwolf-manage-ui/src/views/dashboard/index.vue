<template>
  <div class="dashboard-container">
    <div class="dashboard-text">name: {{ name }}</div>
    <el-button @click="me">ME</el-button>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {getInfo} from '@/api/user'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  mounted() {
  // getInfo()
  },
  activated() {

    getInfo()
  },
  methods:{
    me(){
      getInfo().then(resp=>{
       // if(resp.headers['content-type']=="text/html" && resp.request.responseURL){
	  //window.location.href = resp.request.responseURL;
	//}


      })
    }



  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
