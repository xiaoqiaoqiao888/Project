<template>
    <div>
        <div class="cl">
            <Breadcrumb title="工程包管理" type="select" @sendSelected="getSelectedProject" />
            <UserInfor />
        </div>
        <div class="mlr-20">
            <div class="top-statistics cl mb-20">
                <div class="stat-box cont-bg mr-1">
                    <div class="public-title">
                        <span>任务统计</span>
                        <div id="myChart" class="myChart"></div> 
                    </div>
                </div>
                <div class="stat-box cont-bg ml-1">
                    <div class="public-title">
                        <span>任务成本消耗</span>
                        <div id="myChart2" class="myChart"></div>
                    </div>
                </div>
            </div>
            <div class="cont-bg">
                <div class="project-list-table">
                    <Table :columns="columns1" :data="data1"></Table>
                    <div class="page-box">
                        <Page
                            :total="Math.floor(size1)" 
                            :current="Math.floor(index)" 
                            :page-size="Math.floor(rows)"
                            @on-change="changePackage"
                            simple>
                        </Page>
                    </div>
                </div>
            </div>
        </div>
        <!-- 编辑任务 -->
        <Modal
            v-model="editTaskModel"
            title=""
            class-name="fixed-right-modal"
            :closable="isShowClose"
            :transition-names="transitionNames"
            :styles="{animationDuration: '1s'}"
            @on-cancel="closeModal2"
        >
            <Tabs :value="detailTab" @on-click="modalTabClick" :animated= "false">
                <TabPane label="编辑任务" name="name1">
                    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" label-position="top">
                        <!-- 如果是以某个工程包进入的不显示 -->
                        <!-- <FormItem>
                            <Cascader :data="packageData" v-model="packageValue1" placeholder="选择项目／阶段/工程包"></Cascader>
                        </FormItem> -->
                        <FormItem prop="taskName">
                            <Input v-model="formValidate.taskName" placeholder="任务名称"></Input>
                        </FormItem>
                        <FormItem prop="expTaskTime">
                            <Input v-model="formValidate.expTaskTime" placeholder="任务估时" number @on-change="changeTaskValue"></Input>
                        </FormItem>
                        <FormItem label="任务价值点" prop="taskValue">
                            <Input v-model="formValidate.taskValue" number></Input>
                        </FormItem>
                        <FormItem prop="priority">
                            <Select v-model="formValidate.priority" placeholder="优先级">
                                <Option :value="2">高</Option>
                                <Option :value="1">中</Option>
                                <Option :value="0">低</Option>
                            </Select>
                        </FormItem>
                        <FormItem prop="complexity">
                            <Select v-model="formValidate.complexity" placeholder="复杂度">
                                <Option :value="2">复杂</Option>
                                <Option :value="1">一般</Option>
                                <Option :value="0">简单</Option>
                            </Select>
                        </FormItem>
                        <FormItem label="任务预计起止日期">
                            <Row>
                                <Col span="11">
                                    <FormItem prop="expStartTime">
                                        <DatePicker type="date" placeholder="开始时间" v-model="formValidate.expStartTime"></DatePicker>
                                    </FormItem>
                                </Col>
                                <Col span="2" style="height: 40px;"></Col>
                                <Col span="11">
                                    <FormItem prop="expEndTime">
                                        <DatePicker type="date" placeholder="结束时间" v-model="formValidate.expEndTime"></DatePicker>
                                    </FormItem>
                                </Col>
                            </Row>
                        </FormItem>
                        <FormItem label="任务描述" prop="taskDesc">
                            <Input v-model="formValidate.taskDesc" type="textarea" :autosize="{minRows: 4,maxRows: 5}"></Input>
                        </FormItem>
                        <FormItem class="cl">
                            <Button class="fr" type="warning" @click="affirmBtn('formValidate')" shape="circle">确认提交</Button>
                        </FormItem>
                    </Form>
                </TabPane>
                <TabPane label="历史操作日志" name="name2">
                    <ul class="log-list">
                        <li v-for="item in taskLogList" :key="item.id" class="cl">
                            <i class="clock-ico fl"></i>
                            <div class="fl">
                                <span class="operator">{{item.real_name}}</span>
                                <p class="operation">{{item.operate_desc}}</p>
                            </div>
                            <span class="fr time">{{$timeFormat(item.update_time)}}</span>
                        </li>
                    </ul>
                </TabPane>
            </Tabs>
        </Modal>
    </div>
</template>
<script>
    import * as response from '../../../util/response.js'
    import Breadcrumb from '@/components/common/header/Breadcrumb'
    import UserInfor from '@/components/common/header/UserInfor'
    export default {
        data() {
            return {
                editTaskModel: false,
                isShowClose: false,
                transitionNames:['move-right','fade'],
                formValidate: {
                    taskName: '',
                    expTaskTime: '',
                    expStartTime: '',
                    expEndTime: '',
                    priority: '',
                    complexity: '',
                    taskDesc: '',
                },
                ruleValidate: {
                    taskName: [
                        { required: true, message: '任务名称不能为空', trigger: 'blur' },
                        { type: 'string', max: 100, message: '最多输入100个字符', trigger: 'blur' }
                    ],
                    taskValue: [
                        { type: 'number', min: 1, max: 9999999999, message: '请输入10位以下的数字', trigger: 'blur' }
                    ],
                    expTaskTime: [
                        { type: 'number', min: 1, max: 13, message: '请输入1到13的数字', trigger: 'blur' }
                    ],
                    expStartTime: [
                        { required: true, type: 'date', message: '请选择开始日期', trigger: 'change' }
                    ],
                    expEndTime: [
                        { required: true, type: 'date', message: '请选择结束日期', trigger: 'change' }
                    ],
                    priority: [
                        { required: true,type: 'number', message: '请选择优先级', trigger: 'change' }
                    ],
                    complexity: [
                        { required: true,type: 'number', message: '请选择复杂度', trigger: 'change' }
                    ],
                    taskDesc:[{ type: 'string', max: 300, message: '最多输入300个字符', trigger: 'blur' }]
                },
                columns1: [
                    {
                        title: '名称 / 描述',
                        key: 'name',
                        width:250,
                        render: (h, params) => {
                            return h('dl', [
                                h('dt', {
                                    style: {
                                        float: 'left',
                                        width:'40px',
                                        height:'40px',
                                        textAlign:'center',
                                        lineHeight:'40px',
                                        background:'#359FFF',
                                        color:'#fff',
                                        borderRadius:'50px',
                                    },
                                }, params.row.taskName.substring(0, 2)),
                                h('dd',{
                                    slot: 'content',
                                    style: {
                                        width:'150px',
                                        float: 'left',
                                        textAlign:'left',
                                        lineHeight:'20px',
                                        marginLeft:'15px',
                                    },
                                },[
                                    h('p',{
                                        style: {
                                            color:'#393C40',
                                            cursor: 'pointer'
                                        },
                                        on: {
                                            click: () => {
                                                this.editTaskModel = true;
                                                this.formValidate = params.row;
                                                this.taskId = params.row.id;
                                            }
                                        }
                                    }, params.row.taskName),
                                    h('p',{
                                        style: {
                                            height:'40px',
                                            overflow:'hidden',
                                            fontSize:'12px',
                                        },
                                    }, params.row.taskDesc),
                                ]),
                            ]);
                        }
                    },
                    {
                        title: '状态',
                        key: 'taskState',
                        render:(h,params) =>{
                            let text;
                            if(params.row.taskState == 0){
                                text='未开始'
                            }else if(params.row.taskState == 1){
                               text='进行中' 
                            }else if(params.row.taskState == 2){
                               text='待验收' 
                            }else if(params.row.taskState == 3){
                               text='已完成' 
                            }else if(params.row.taskState == 4){
                               text='延期进行中' 
                            }
                            return h('span', text);
                        }
                    },
                    {
                        title: '进度',
                        key: 'taskSchedule',
                        render:(h,params) =>{
                            let progress = params.row.taskSchedule ? params.row.taskSchedule+'%' : 0;
                            return h('span', progress);
                        }
                    },
                    {
                        title: '消耗成本占比',
                        key: ' proportion',
                        render: (h, params) => {
                            let ratio = params.row.useCostRatio ? Math.round(params.row.useCostRatio*100)+'%' : 0;
                            return h('div', [
                                // h('p', {
                                //     style: {
                                //     },
                                // }, ' RMB'),
                                h('div', {
                                    style: {
                                    },
                                },[
                                    h('span', {
                                        style: {
                                            display:'inline-block',
                                            verticalAlign:'middle',
                                            marginRight:'8px',
                                            width:'26px',
                                            height:'26px',
                                            background:'#f5f5f5',
                                            borderRadius:'50px',
                                        },
                                    },''), 
                                    h('span', {
                                        style: {

                                        },
                                    }, ratio),
                                ]),
                                
                            ]);
                        }
                    },
                    {
                        title: '截止日期',
                        key: 'expEndTime',
                        render:(h,params) =>{
                            return h('div',{
                            }, this.$dateFormat2(params.row.expEndTime) )  
                        }
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width:130,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'primary',
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.closeTask(params.row.id);
                                        }
                                    }
                                }, '关闭'),
                            ]);
                        }
                    }
                ],
                data1: [],
                size1:'',
                index:1,
                rows:5,
                packageValue1: [], //级联选择数组
                projectId: '', // 项目id
                taskId: '', // 任务id
                taskLogList: [], // 日志列表
                detailTab: 'name1', // 弹出层里tab切换的value值
            }
        },
        components: {
            Breadcrumb,
            UserInfor
        },
        mounted() {
            
        },
        created(){
    
        },
        methods: {
            // 获取选中的项目
            getSelectedProject(val) {
                this.projectId = localStorage.getItem('projectId') || val;
                this.getPackageTaskList();
                this.getTaskCount();
            },
            // 获取工程包下的任务列表
            getPackageTaskList() {
                var data = {
                    pageNum:this.index,
                    pageSize:this.rows,
                    projectId: this.projectId,
                    stageId: this.$route.query.stageId,
                    workId: this.$route.query.packageId,
                }
                this.$http.get('/task/select-TaskList-By-PageInfo', { params: data })
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.size1 = data.total;
                        this.data1 = data.list;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取工程包下的任务列表失败')
                })
            },
            // 编辑任务弹出层确认
            affirmBtn(type) {
                this.$refs[type].validate((valid) => {
                    if (valid) {
                        this.$http.put(
                            `/task?id=${this.taskId}
                            &taskName=${this.formValidate.taskName}
                            &taskValue=${this.formValidate.taskValue}
                            &expTaskTime=${this.formValidate.expTaskTime}
                            &expStartTime=${this.$dateFormat(this.formValidate.expStartTime)}
                            &expEndTime=${this.$dateFormat(this.formValidate.expEndTime)}
                            &priority=${this.formValidate.priority}
                            &complexity=${this.formValidate.complexity}
                            &taskDesc=${this.formValidate.taskDesc}
                            `
                            )
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                this.$Message.success('编辑任务成功！');
                                this.editTaskModel = false;
                                this.getPackageTaskList();
                            }
                        })
                        .catch(res => {
                            this.$Message.error('编辑任务失败！')
                        });
                    }
                })
            },
             // 关闭弹出层,reset表单（用于添加弹出层）
            closeModal(type) {
                this.$refs[type].resetFields();
            },
            // 关闭弹出层,将tab切回第一项（用于编辑弹出层）
            closeModal2() {
                this.detailTab = 'name1';
            },
            // 工程包下任务列表的分页
            changePackage(params) {
                this.index = params;
                this.getPackageTaskList();
            },
            // 关闭任务
            closeTask(taskId) {
                this.$http.put(`/task?id=${taskId}&taskState=0&state=0`)
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.$Message.success('关闭任务成功');
                        this.getPackageTaskList();
                    }
                })
                .catch(res => {
                    // console.log(res);
                    this.$Message.error('关闭任务失败');
                })
            },
            // 监听任务估时改变时改变任务价值分
            changeTaskValue() {
                if(this.expTaskTime == 0) {
                    this.formValidate.taskValue = this.workValue;
                }else{
                    this.formValidate.taskValue = (this.formValidate.expTaskTime / this.expTaskTime)*this.workValue;
                }
            },
            // 弹出层里的tab切换
            modalTabClick(name) {
                this.detailTab = name;
                if(name == 'name2'){
                    this.getLogList();
                }else {
                    // this.checkTask();
                }
            },
            // 获取操作日志
            getLogList() {
                var data= {
                    taskId: this.taskId,
                }
                this.$http.get('/task/logTaskByTaskId', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.taskLogList = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('查询任务日志失败！')
                }) 
            },
            // echart图表初始化
            drawLine(taskCount){
                // 任务统计
                let myChart = this.$echarts.init(document.getElementById('myChart'))
                // 任务统计
                myChart.setOption({
                    color:['#1DE9B6','#04A9F5','#A389D4','#FF9E22','#ddd'],
                    title : {
                        text: '任务统计',
                        x:'center',
                        textStyle:{
                            fontSize:14,
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    series : [
                        {
                            name: '任务统计',
                            type: 'pie',
                            radius : '50%',
                            center: ['50%', '50%'],
                            data:[
                                {value:taskCount.noStart, name:'未开始'},
                                {value:taskCount.haveInIand, name:'进行中'},
                                {value:taskCount.delayHaveInHand, name:'延期进行中'},
                                {value:taskCount.waitCheck, name:'待验收'},
                                {value:taskCount.completed, name:'已完成'}
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
                // 任务成本消耗
                let myChart2 = this.$echarts.init(document.getElementById('myChart2'))
                // 任务成本消耗
                myChart2.setOption({
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
                    legend: {
                        orient: 'horizontal',
                        bottom: '30',
                        data:['超预算','未超预算']
                    },
                    color:['#1DE9B6','#A389D4'],
                    series: [
                        {
                            // name:'访问来源',
                            type:'pie',
                            radius: ['50%', '70%'],
                            center: ['50%','40%'],
                            avoidLabelOverlap: false,
                            label: {
                                normal: {
                                    show: false,
                                    position: 'center'
                                },
                                emphasis: {
                                    show: true,
                                    textStyle: {
                                        fontSize: '30',
                                        fontWeight: 'bold'
                                    }
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data:[
                                {value: taskCount.overBudget, name:'超预算'},
                                {value: taskCount.onOverBudget, name:'未超预算'},
                            ]
                        }
                    ]
                });
            },
            // 任务统计以及任务成本统计
            getTaskCount() {
                var data= {
                    projectId: this.projectId,
                    stageId: this.$route.query.stageId,
                    workId: this.$route.query.packageId,
                }
                this.$http.get('/task/stateCount-budgetCount', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.drawLine(data);
                    }
                })
                .catch(res => {
                    this.$Message.error('获取任务统计失败！')
                })
            }
        } 
    }
</script>
<style scoped>
    .project-list-table{
        padding:20px 0 0;
        overflow: hidden;
    }
    .stat-box{
        width:49%;
        height:400px;
        float:left;
        border-radius:0;
        padding:20px;
    }
    .myChart{
        width:100%;
        height: 320px;
    }
</style>