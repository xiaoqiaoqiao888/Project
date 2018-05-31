<template>
    <div class="right-cont-box">
        <div class="left">
            <left-form @rolesListTable = "refreshRight"></left-form>
        </div>
       <div class="right">
           <router-view ref="memberTable"></router-view>
       </div>
    </div>
</template>
<script>
    import LeftForm from '@/components/systemSettings/common/LeftForm'
    export default {
        data() {
            return {
    
            }
        },
        components: {
            LeftForm
        },
        mounted() {
    
        },
        created(){
    
        },
        methods: {
            refreshRight(id) {
                // console.log(this.$refs.memberTable.activeTab)
                if(this.$refs.memberTable.activeTab=='name2'){
                    this.$refs.memberTable.getRoleResource(id);
                }else{
                    this.$refs.memberTable.roleMembersList(id); 
                }
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
        /* padding:0 20px; */
    }
    .right{
        margin-left:26.8%;
        min-height: 500px;
    }
</style>