<template>
    <div class="right-cont-box">
       <div class="left">
           <left-box @refreshTable = "refreshRight"></left-box>
       </div> 
       <div class="right">
           <ability-list ref="abilityTable"></ability-list>
       </div>
    </div>
</template>
<script>
    import LeftBox from '@/components/functionShow/common/LeftBox'
    import AbilityList from '@/components/functionShow/views/AbilityList'
    export default {
        data() {
            return {
    
            }
        },
        components: {
            LeftBox,
            AbilityList
        },
        mounted() {
    
        },
        created(){
    
        },
        methods: {
            // 选择不同职能组刷新右侧
            refreshRight(groupId) {
                this.$refs.abilityTable.getPowerList(groupId);
            },
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
    }
    .right{
        margin-left:18.5%;
        min-height: 500px;
    }
</style>