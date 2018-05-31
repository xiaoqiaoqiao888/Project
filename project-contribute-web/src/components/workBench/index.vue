<template>
    <div class="right-cont-box">
       <div class="left">
           <left-box></left-box>
       </div> 
       <div class="right">
           <router-view></router-view>
       </div>
    </div>
</template>
<script>
    import LeftBox from '@/components/workBench/common/LeftBox'
    export default {
        data() {
            return {
    
            }
        },
        components: {
            LeftBox
        },
        mounted() {
    
        },
        created(){
    
        },
        methods: {
    
        } 
    }
</script>
<style scoped>
    .right-cont-box{ width:100%;}
    .left{
        width:17%;
        height:100%;
        background:#fff;
        position: fixed;
        bottom:0;
        border:1px solid #DAD9D9; 
        padding-top:20px;
        z-index:1;
    }
    .right{
        /* margin-left:18.5%; */
        width:76.2%;
        position:fixed;
        left:23.8%;
        /* margin-left:18.2%; */
        height:100%;
        overflow-y:auto;
        /* min-height: 500px; */
    }
</style>