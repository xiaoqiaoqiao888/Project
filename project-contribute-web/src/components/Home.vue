<template>
  <div class="camelot-container">
        <div class="left-nav">
            <left-nav></left-nav>
        </div>
        <div class="right-cont">
            <router-view></router-view>
        </div>
  </div>
</template>

<script>
import LeftNav from '@/components/common/LeftNav'
export default {
    data() {
        return {

        }
    },
    components: {
        LeftNav

    },
    mounted() {

    },
    created(){

    },
    methods: {

    } 
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .left-nav{
      width:6.8%;
      height:100%;
      position:fixed;
      top:0;
      bottom:0;
      left:0;
      background:#272E41;
  }
  .right-cont{
    width:93.2%;
    height:100%;
    position:fixed;
    top:0;
    bottom:0;
    margin-left:6.8%;
    background: #F5F8FA; 
    overflow-y:auto;
  }
</style>
