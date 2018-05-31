<template>
    <div>
        <div class="cl">
            <Breadcrumb title="项目管理" content="我的项目情况" />
            <UserInfor />
        </div>
        <div class="cont-bg mlr-20">
            <Tabs :value="tabActive" :animated="false" @on-click="tabClick">
                <TabPane label="项目进展" name="name1">
                    <ProgressProject v-if="tabActive == 'name1'" />
                </TabPane>
                <TabPane label="阶段进展" name="name2">
                    <ProgressStage v-if="tabActive == 'name2'" />
                </TabPane>
                <TabPane label="工程包进展" name="name3">
                    <ProgressWork v-if="tabActive == 'name3'" />
                </TabPane>
                <TabPane label="任务进展" name="name4">
                    <ProgressTask v-if="tabActive == 'name4'" />
                </TabPane>
                <TabPane label="项目详情" name="name5">
                    <ProgressProjectDetail v-if="tabActive == 'name5'" />
                </TabPane>
                <TabPane label="项目成员" name="name6">
                    <ProgressMember v-if="tabActive == 'name6'" />
                </TabPane>
            </Tabs>
        </div>
    </div>
</template>
<script>
    import Breadcrumb from '@/components/common/header/Breadcrumb'
    import UserInfor from '@/components/common/header/UserInfor'
    import ProgressProject from './components/ProgressProject'
    import ProgressStage from './components/ProgressStage'
    import ProgressWork from './components/ProgressWork'
    import ProgressTask from './components/ProgressTask'
    import ProgressProjectDetail from './components/ProgressProjectDetail'
    import ProgressMember from './components/ProgressMember'
    export default {
        data() {
            return {
                tabActive: 'name1',
            }
        },
        components: {
            Breadcrumb,
            UserInfor,
            ProgressProject,
            ProgressStage,
            ProgressWork,
            ProgressTask,
            ProgressProjectDetail,
            ProgressMember,
        },
        mounted() {
            
        },
        created(){
    
        },
        methods: {
            // 点击tab进行切换
            tabClick(name) {
                this.tabActive = name;
                // if(name=='name1'){ // 项目进展
                    
                // }else if(name=='name2'){ // 阶段进展
                    
                // }else if(name=='name3'){ // 工程包进展

                // }else if(name=='name4'){ // 任务进展
                    
                // }else if(name=='name5'){ // 项目详情
                    
                // }else if(name=='name6'){ // 项目成员
                    
                // }
            },
        } 
    }
</script>
<style scoped>
    
    
</style>