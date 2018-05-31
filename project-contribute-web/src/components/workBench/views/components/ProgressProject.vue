<template>
    <div class="mlr-20 mb-20">
        <div class="title">
            <span class="title-sp">进度统计</span>
            <span class="txt-sp">项目整体进度：{{Math.round(projectCount.avgPlan*100)}}%</span>
        </div>
        <Row>
            <Col span="8">
                <div id="myChart1" class="myChart"></div> 
            </Col>
            <Col span="8">
                <div id="myChart2" class="myChart"></div> 
            </Col>
            <Col span="8">
                <div id="myChart3" class="myChart"></div> 
            </Col>
        </Row>
    
        <div class="title">
            <span class="title-sp">成本消耗统计</span>
            <span class="txt-sp">项目整体消耗成本：{{Math.round(projectCount.avgCost*100)}}%</span>
        </div>
        <Row>
            <Col span="8">
                <div id="myChart4" class="myChart"></div> 
            </Col>
            <Col span="8">
                <div id="myChart5" class="myChart"></div> 
            </Col>
            <Col span="8">
                <div id="myChart6" class="myChart"></div>  
            </Col>
        </Row>
    </div>
</template>
<script>
    import * as response from '../../../../util/response.js'
    export default {
        data() {
            return {
                packageChart:'',//统计工程包
                taskChart:'',//统计任务
                stageChart:'',//阶段统计
                projectCount: {} // 项目进度与成本统计
            }
        },
        mounted() {
            this.packageFun();
            this.stageFun();
            this.taskFun();
            this.getProjectCount();
        },
        created(){
    
        },
        methods: {
            // 阶段预算统计
            drawLine(){
                // 阶段
                let myChart1 = this.$echarts.init(document.getElementById('myChart1'))
                // 阶段
                myChart1.setOption({
                    color:['#1DE9B6','#04A9F5','#A389D4','#FF9E22','#ddd'],
                    title : {
                        text: '阶段统计',
                        // subtext: '纯属虚构',
                        x:'center',
                        textStyle:{
                            fontSize:14,
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    // legend: {
                    //     orient: 'vertical',
                    //     right: '-30',
                    //     data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
                    // },
                    series : [
                        {
                            name: '阶段统计',
                            type: 'pie',
                            radius : '50%',
                            center: ['50%', '50%'],
                            data:[
                                {value:this.stageChart.noStart, name:'未开始'},
                                {value:this.stageChart.haveInIand, name:'进行中'},
                                {value:this.stageChart.delayHaveInHand, name:'延期进行中'},
                                {value:this.stageChart.waitCheck, name:'待验收'},
                                {value:this.stageChart.completed, name:'已完成'}
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
                // 工程包
                let myChart2 = this.$echarts.init(document.getElementById('myChart2'))
                // 工程包
                myChart2.setOption({
                    color:['#1DE9B6','#04A9F5','#A389D4','#FF9E22','#ddd'],
                    title : {
                        text: '工程包统计',
                        // subtext: '纯属虚构',
                        x:'center',
                        textStyle:{
                            fontSize:14,
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    // legend: {
                    //     orient: 'vertical',
                    //     right: '-30',
                    //     data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
                    // },
                    series : [
                        {
                            name: '工程包统计',
                            type: 'pie',
                            radius : '50%',
                            center: ['50%', '50%'],
                            data:[
                                {value:this.packageChart.noStart, name:'未开始'},
                                {value:this.packageChart.haveInIand, name:'进行中'},
                                {value:this.packageChart.delayHaveInHand, name:'延期进行中'},
                                {value:this.packageChart.waitCheck, name:'待验收'},
                                {value:this.packageChart.completed, name:'已完成'}
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
                // 任务
                let myChart3 = this.$echarts.init(document.getElementById('myChart3'))
                // 任务
                myChart3.setOption({
                    color:['#1DE9B6','#04A9F5','#A389D4','#FF9E22','#ddd'],
                    title : {
                        text: '任务统计',
                        // subtext: '纯属虚构',
                        x:'center',
                        textStyle:{
                            fontSize:14,
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    // legend: {
                    //     orient: 'vertical',
                    //     right: '-30',
                    //     data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
                    // },
                    series : [
                        {
                            name: '任务统计',
                            type: 'pie',
                            radius : '50%',
                            center: ['50%', '50%'],
                            data:[
                                {value:this.taskChart.noStart, name:'未开始'},
                                {value:this.taskChart.haveInIand, name:'进行中'},
                                {value:this.taskChart.delayHaveInHand, name:'延期进行中'},
                                {value:this.taskChart.waitCheck, name:'待验收'},
                                {value:this.taskChart.completed, name:'已完成'}
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
                // 阶段预算
                let myChart4 = this.$echarts.init(document.getElementById('myChart4'))
                // 阶段预算
                myChart4.setOption({
                    color:['#1DE9B6','#A389D4'],
                    title : {
                        text: '阶段成本消耗',
                        // subtext: '纯属虚构',
                        x:'center',
                        textStyle:{
                            fontSize:14,
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    // legend: {
                    //     orient: 'vertical',
                    //     right: '-30',
                    //     data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
                    // },
                    series : [
                        {
                            name: '阶段成本消耗',
                            type: 'pie',
                            radius : '50%',
                            center: ['50%', '50%'],
                            data:[
                                {value:this.stageChart.overBudget, name:'超预算'},
                                {value:this.stageChart.onOverBudget, name:'未超预算'},
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
                // 工程包预算
                let myChart5 = this.$echarts.init(document.getElementById('myChart5'))
                // 工程包预算
                myChart5.setOption({
                    color:['#1DE9B6','#A389D4'],
                    title : {
                        text: '工程包成本消耗',
                        // subtext: '纯属虚构',
                        x:'center',
                        textStyle:{
                            fontSize:14,
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    // legend: {
                    //     orient: 'vertical',
                    //     right: '-30',
                    //     data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
                    // },
                    series : [
                        {
                            name: '工程包成本消耗',
                            type: 'pie',
                            radius : '50%',
                            center: ['50%', '50%'],
                            data:[
                                {value:this.packageChart.overBudget, name:'超预算'},
                                {value:this.packageChart.onOverBudget, name:'未超预算'},
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
                // 任务预算
                let myChart6 = this.$echarts.init(document.getElementById('myChart6'))
                // 任务预算
                myChart6.setOption({
                    color:['#1DE9B6','#A389D4'],
                    title : {
                        text: '任务成本消耗',
                        // subtext: '纯属虚构',
                        x:'center',
                        textStyle:{
                            fontSize:14,
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    // legend: {
                    //     orient: 'vertical',
                    //     right: '-30',
                    //     data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
                    // },
                    series : [
                        {
                            name: '任务成本消耗',
                            type: 'pie',
                            radius : '50%',
                            center: ['50%', '50%'],
                            data:[
                                {value:this.taskChart.overBudget, name:'超预算'},
                                {value:this.taskChart.onOverBudget, name:'未超预算'},
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
            },
            // 工程包统计
            packageFun(){
                let data = {
                    projectId:this.$route.query.id
                }
                this.$http.get('/Work/work-status-count', {params:data})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.packageChart = data
                        this.drawLine();

                    }
                })
                .catch(res => {
                    this.$Message.error('获取阶段统计失败')
                })
            },
            // 任务统计
            taskFun(){
                let data = {
                    projectId:this.$route.query.id
                }
                console.log(data)
                this.$http.get('/task/stateCount-budgetCount', {params:data})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.taskChart = data
                        this.drawLine();

                    }
                })
                .catch(res => {
                    this.$Message.error('获取任务统计失败')
                })
            },
            // 阶段统计
            stageFun(){
                console.log(this.$route.query.id)
                let data = {
                    projectId:this.$route.query.id
                }
                // console.log(data)
                this.$http.get('/state/stage-static', {params:data})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.stageChart = data
                        this.drawLine();

                    }
                })
                .catch(res => {
                    this.$Message.error('获取阶段统计失败')
                })
            },
            // 项目预算统计
            getProjectCount() {
                let data = {
                    id:this.$route.query.id
                }
                this.$http.get('/project/project-count', {params:data})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.projectCount = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取项目预算统计失败')
                }) 
            }
        } 
    }
</script>
<style scoped>
    .title{
        font-size:16px;
        margin-bottom:30px;
    }
    .title .title-sp{
        font-weight:bold;
        margin-right:10px;
    }
    .title .txt-sp{
        color:#666;
        font-size:12px;
    }
    .myChart{
        width:100%;
        height:230px;
    }
    .textarea-detail{
        width:480px;
        height:150px;
    }
    
</style>