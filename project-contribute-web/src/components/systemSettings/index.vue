<template>
    <div class="right-cont-box">
       <div class="left">
           <left-tree @refreshTable = "refreshRight"></left-tree>
       </div> 
       <div class="right">
           <staff-manage ref="memberTable"></staff-manage>
       </div>
    </div>
</template>
<script>
    import LeftTree from '@/components/systemSettings/common/LeftTree'
    import StaffManage from '@/components/systemSettings/views/staffManagement'

    export default {
        data() {
            return {
    
            }
        },
        components: {
            LeftTree,
            StaffManage
        },
        mounted() {
    
        },
        created(){
    
        },
        methods: {
            refreshRight(id) {
                this.$refs.memberTable.getMemberList(id); 
            }
        } 
    }
</script>
<style scoped>
    .right-cont-box{ width:100%;}
    .left{
        width:25%;
        height:100%;
        background:#fff;
        position: fixed;
        bottom:0;
        border:1px solid #DAD9D9; 
        padding:0 20px;
    }
    .right{
        margin-left:26.8%;
        min-height: 500px;
    }
</style>