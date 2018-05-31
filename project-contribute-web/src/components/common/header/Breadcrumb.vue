<template>
    <div class="breadcrumb fl">
        <span class="fl">{{title}}</span>
        <Select v-model="selModel" v-if="type && projectList.length" class="select-cont fl" @on-change="selectProject">
            <Option v-for="item in projectList" :value="item.value" :key="item.value">{{ item.label }}</Option>
        </Select>
        <span v-else class="cont fl">{{content}}</span>
    </div>
</template>
<script>
    import * as response from '../../../util/response.js'
    export default {
        props: ['title','content','type'],
        components : {
        },
        data () {
            return {
                selModel: '',
                projectList: [],
            }
        },
        methods: {
            // 获取项目列表
            getProjectList() {
                var data = {
                    projectName: '',
                }
                this.$http.get('/project/list-condition', { params: data })
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        console.log(data);
                        this.projectList = [];
                        for(var i=0;i<data.length;i++){ //0-未开始 1-进行中 4-延期进行中 3-已完成
                            // 未开始和已完成的不在项目列表中显示
                            if(data[i].projectState != 0 &&  data[i].projectState != 3){
                                this.projectList.push({
                                    label: data[i].projectName,
                                    value: data[i].id
                                })
                            }
                        }
                        // 测试定位bug
                        console.log(this.projectList);
                        if(this.projectList.length){
                            console.log(localStorage.getItem('projectId'));
                            // 处理清库引发的localStorage的undefined问题
                            if(localStorage.getItem('projectId')=="undefined"){
                                localStorage.setItem('projectId', this.projectList[0].value);
                            }
                            this.selModel =  localStorage.getItem('projectId') ?
                            parseInt(localStorage.getItem('projectId')) : this.projectList[0].value;
                            this.$emit('sendSelected', this.selModel);
                        }
                        console.log(this.selModel);
                    }
                })
                .catch(res => {
                    // console.log(res);
                    this.$Message.error('获取项目列表失败')
                })
            },
            // 下拉选择项目
            selectProject(val){
                localStorage.setItem('projectId', val);
                this.$emit('sendSelected', val);
            }
        },
        mounted() {
            this.getProjectList();
        }
    }
</script>
<style lang="less" scoped>
    .breadcrumb{
        font-size: 24px;
        letter-spacing: 0.67px;
        line-height: 28px;
        margin: 40px 0 40px 20px;
        .cont{
            opacity: 0.25;
            font-size: 14px;
            letter-spacing: 0.39px;
            line-height: 28px;
            margin-left: 15px;
        }
    }
</style>