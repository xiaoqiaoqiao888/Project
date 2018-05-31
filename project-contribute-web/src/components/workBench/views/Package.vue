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
                        <span>工程包统计</span>
                        <div id="myChart" class="myChart"></div> 
                    </div>
                </div>
                <div class="stat-box cont-bg ml-1">
                    <div class="public-title">
                        <span>工程包成本消耗</span>
                        <div id="myChart2" class="myChart"></div> 
                    </div>
                </div>
            </div>
            <div class="cont-bg">
                <div class="project-list-table">
                    <Table :columns="columns1" :data="data1" @on-row-click="enterDetail" :row-class-name="rowClassName"></Table>
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
        <!-- 拆分任务 -->
        <Modal
            v-model="editTaskModel"
            title=""
            class-name="fixed-right-modal"
            :closable="isShowClose"
            :transition-names="transitionNames"
            :styles="{animationDuration: '1s'}"
            @on-cancel="closeModal('formValidate')"
        >
            <Tabs :value="detailTab" @on-click="modalTabClick" :animated= "false">
                <TabPane label="拆分任务" name="name1">
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
                                <Option value="2">高</Option>
                                <Option value="1">中</Option>
                                <Option value="0">低</Option>
                            </Select>
                        </FormItem>
                        <FormItem prop="complexity">
                            <Select v-model="formValidate.complexity" placeholder="复杂度">
                                <Option value="2">复杂</Option>
                                <Option value="1">一般</Option>
                                <Option value="0">简单</Option>
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
                    
                </TabPane>
            </Tabs>
        </Modal>
        <!-- 工程包详情 -->
        <Modal
            v-model="workDetailModel"
            title=""
            class-name="fixed-right-modal"
            :closable="isShowClose"
            :transition-names=transitionNames
            :styles="{animationDuration: '1s'}"
            @on-cancel="closeModal2"
        >
            <div class="model-cont">
                <Tabs :value="detailTab" @on-click="modalTabClick2" :animated= "false">
                    <TabPane label="工程包详情" name="name1">
                        <div class="cl detail-item">
                            <span class="fl datail-label">工程包状态：</span>
                            <span class="fl datail-cont">{{workDetail.workState | formatWorkState}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">进度：</span>
                            <span class="fl datail-cont">{{Math.round(workDetail.taskSchedule)}}%</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">工程包名称：</span>
                            <span class="fl datail-cont">{{workDetail.workName}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">项目名称：</span>
                            <span class="fl datail-cont">{{workDetail.projectName}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">项目负责人：</span>
                            <span class="fl datail-cont">{{workDetail.projectUserName}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">预算：</span>
                            <span class="fl datail-cont">{{workDetail.workBudget}}元</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">价值分：</span>
                            <span class="fl datail-cont">{{workDetail.workValue}}分</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">预计起止日期：</span>
                            <span class="fl datail-cont">{{$dateFormat2(workDetail.startTime)}} 至 {{$dateFormat2(workDetail.endTime)}}</span>
                        </div>
                        
                        <div class="cl detail-item">
                            <span class="fl datail-label">实际消耗成本：</span>
                            <span class="fl datail-cont">{{workDetail.workCost}}元</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">实际完成时间：</span>
                            <span class="fl datail-cont">{{workDetail.endSubmitTime ?
                                $timeFormat2(workDetail.endSubmitTime) : ''}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">描述：</span>
                            <span class="fl datail-cont">{{workDetail.workDesc}}</span>
                        </div>
                    </TabPane>
                    <TabPane label="历史操作日志" name="name2">
                        <ul class="log-list">
                            <li v-for="item in packageLogList" :key="item.id" class="cl">
                                <i class="clock-ico fl"></i>
                                <div class="fl">
                                    <span class="operator">{{item.updateUserName}}</span>
                                    <p class="operation">{{item.operateDesc}}</p>
                                </div>
                                <span class="fr time">{{$timeFormat(item.updateTime)}}</span>
                            </li>
                        </ul>
                    </TabPane>
                </Tabs>
            </div>
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
                        { type: 'integer', min: 1, max: 9999999999, message: '请输入10位以下的整数', trigger: 'blur' }
                    ],
                    expTaskTime: [
                        { type: 'integer', min: 1, max: 13, message: '请输入1到13的整数', trigger: 'blur' }
                    ],
                    expStartTime: [
                        { required: true, type: 'date', message: '请选择开始日期', trigger: 'change' }
                    ],
                    expEndTime: [
                        { required: true, type: 'date', message: '请选择结束日期', trigger: 'change' }
                    ],
                    priority: [
                        { required: true, message: '请选择优先级', trigger: 'change' }
                    ],
                    complexity: [
                        { required: true, message: '请选择复杂度', trigger: 'change' }
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
                                    slot: 'content',
                                    style:{
                                        float: 'left',
                                    },
                                },[
                                    h('div',{
                                        style: {
                                            width:'40px',
                                            height:'40px',
                                            margin:'0 auto',
                                            textAlign:'center',
                                            lineHeight:'40px',
                                            background:'#359FFF',
                                            color:'#fff',
                                            borderRadius:'50px',
                                            cursor:'pointer',
                                            letterSpacing:'3px',
                                        },
                                    },params.row.projectName.substring(0, 2)),
                                    h('div',{
                                        style: {
                                            height:'40px',
                                            textAlign:'center',
                                            lineHeight:'40px',
                                            cursor:'pointer',
                                        },
                                    },[
                                        params.row.workState == 0 ?
                                        h('a',{
                                            style:{
                                                display:'inline-block',
                                                color:'#aaaeb3',
                                            },
                                            on: {
                                                click: (e) => {
                                                    e.stopPropagation();
                                                    this.confirmPackage(params.row.id)
                                                }
                                            }
                                        },'确认')
                                        :
                                        h('a',{
                                            style:{
                                                display:'inline-block',
                                                color:'#aaaeb3',
                                            },
                                            on: {
                                                click: (e) => {
                                                    e.stopPropagation();
                                                    this.workId = params.row.id;
                                                    this.stageId = params.row.stageId;
                                                    this.expTaskTime = params.row.expTaskTime;
                                                    this.workValue = params.row.workValue;
                                                    this.splitTask();
                                                }
                                            }
                                        },'拆分'),
                                        h('i',{
                                            style:{
                                                display:'inline-block',
                                                margin:"0 5px",
                                                color:'#aaaeb3',
                                            },
                                        },'/'),
                                        h('a',{
                                            style:{
                                                display:'inline-block',
                                                color:'#aaaeb3',
                                            },
                                            on: {
                                                click:(e) =>{
                                                    // 进入工程包里的任务列表(查看任务)
                                                    e.stopPropagation();
                                                    this.$router.push({path:'/WorkBench/PackageTask',query:{ packageId: params.row.id, stageId: params.row.stageId }})  
                                                }
                                            }
                                        },'查看'),
                                    ]),
                                    
                                ]),
                                h('dd',{
                                    slot: 'content',
                                    style: {
                                        width:'100px',
                                        float: 'left',
                                        textAlign:'left',
                                        lineHeight:'20px',
                                        marginLeft:'15px',
                                        // cursor:'pointer',
                                    },
                                },[
                                    h('p',{
                                        style: {
                                            color:'#393C40',
                                        },
                                    }, params.row.workName),
                                    h('p',{
                                        style: {
                                            height:'40px',
                                            overflow:'hidden',
                                            fontSize:'12px',
                                        },
                                    }, params.row.workDesc),
                                ]),
                            ]);
                        }
                    },
                    // {
                    //     title: '消耗成本占比',
                    //     key: ' proportion',
                    //     render: (h, params) => {
                    //         return h('div', [
                    //             h('p', {
                    //                 style: {
                    //                 },
                    //             }, ' RMB'),
                    //             h('div', {
                    //                 style: {
                    //                 },
                    //             },[
                    //                 h('span', {
                    //                     style: {
                    //                         display:'inline-block',
                    //                         verticalAlign:'middle',
                    //                         marginRight:'8px',
                    //                         width:'26px',
                    //                         height:'26px',
                    //                         background:'#f5f5f5',
                    //                         borderRadius:'50px',
                    //                     },
                    //                 },''), 
                    //                 h('span', {
                    //                     style: {

                    //                     },
                    //                 },'68%'),
                    //             ]),
                                
                    //         ]);
                    //     }
                    // },
                    {
                        title: '状态',
                        key: 'workState',
                        render:(h,params) =>{
                            let text;
                            if(params.row.workState == 0){
                                text='未开始'
                            }else if(params.row.workState == 1){
                               text='进行中' 
                            }else if(params.row.workState == 2){
                               text='待验收' 
                            }else if(params.row.workState == 3){
                               text='已完成' 
                            }else if(params.row.workState == 4){
                               text='延期进行中' 
                            }
                            return h('span', text);
                        }
                    },
                    {
                        title: '是否超预算',
                        key: 'workCostStatus',
                        render:(h,params) =>{
                            let text;
                            if(params.row.workCostStatus == 1){
                                text='未超预算'
                            }else {
                               text='超预算' 
                            }
                            return h('span', text);
                        }
                    },
                    {
                        title: '截止日期',
                        key: 'date',
                        render:(h,params) =>{
                            return h('div',{
                            }, this.$dateFormat2(params.row.endTime) )  
                        }
                    },
                    // {
                    //     title: '操作',
                    //     key: 'action',
                    //     width:130,
                    //     align: 'center',
                    //     render: (h, params) => {
                    //         if(params.row.workState == 0) {
                    //             return h('div', [
                    //                 h('Button', {
                    //                     props: {
                    //                         type: 'primary',
                    //                     },
                    //                     style: {
                    //                         marginRight: '5px'
                    //                     },
                    //                     on: {
                    //                         click: (e) => {
                    //                             e.stopPropagation();
                    //                             this.confirmPackage(params.row.id)
                    //                         }
                    //                     }
                    //                 }, '确认'),
                    //             ]);
                    //         }else {
                    //             return h('div', [
                    //                 h('Button', {
                    //                     props: {
                    //                         type: 'primary',
                    //                     },
                    //                     style: {
                    //                         marginRight: '5px'
                    //                     },
                    //                     on: {
                    //                         click: (e) => {
                    //                             e.stopPropagation();
                    //                             this.workId = params.row.id;
                    //                             this.stageId = params.row.stageId;
                    //                             this.expTaskTime = params.row.expTaskTime;
                    //                             this.workValue = params.row.workValue;
                    //                             this.splitTask();
                    //                         }
                    //                     }
                    //                 }, '拆分任务'),
                    //             ]);
                    //         }
                    //     }
                    // }
                ],
                data1: [],
                size1:'',
                index:1,
                rows:5,
                packageValue1: [],
                // packageData: [{
                //     value: 'beijing',
                //     label: '北京',
                //     children: [
                //         {
                //             value: 'gugong',
                //             label: '故宫'
                //         },
                //         {
                //             value: 'tiantan',
                //             label: '天坛'
                //         },
                //         {
                //             value: 'wangfujing',
                //             label: '王府井'
                //         }
                //     ]
                // }, {
                //     value: 'jiangsu',
                //     label: '江苏',
                //     children: [
                //         {
                //             value: 'nanjing',
                //             label: '南京',
                //             children: [
                //                 {
                //                     value: 'fuzimiao',
                //                     label: '夫子庙',
                //                 }
                //             ]
                //         },
                //         {
                //             value: 'suzhou',
                //             label: '苏州',
                //             children: [
                //                 {
                //                     value: 'zhuozhengyuan',
                //                     label: '拙政园',
                //                 },
                //                 {
                //                     value: 'shizilin',
                //                     label: '狮子林',
                //                 }
                //             ]
                //         }
                //     ],
                // }],
                projectId: '',
                stageId: '',
                workId: '',
                detailTab: 'name1',
                workState: '', // 工程包状态
                costStatus: '', // 是否超预算
                workDetailModel: false, // 工程包详情弹出层
                workDetail: {}, // 工程包详情
                packageLogList: [], // 工程包操作日志列表
                workDetailId: '', // 详情页查看日志所需要的id
                
            }
        },
        components: {
            Breadcrumb,
            UserInfor
        },
        filters: {
            formatWorkState: function (value) {
                let text;
                if(value == 0){
                    text='未开始'
                }else if(value == 1){
                    text='进行中' 
                }else if(value == 2){
                    text='待验收' 
                }else if(value == 3){
                    text='已完成' 
                }else if(value == 4){
                    text='延期进行中' 
                }
                return text;
            },
        },
        mounted() {
            
        },
        created(){
    
        },
        methods: {
            // 获取选中的项目
            getSelectedProject(val) {
                this.projectId = localStorage.getItem('projectId') || val;
                this.getPackageList();
                this.getWorkCount();
            },
            // 获取工程包列表
            getPackageList() {
                var data = {
                    pageNum:this.index,
                    pageSize:this.rows,
                    projectId: this.projectId,
                    workState: this.workState,
                    workCostStatus: this.costStatus,
                }
                this.$http.get('/Work/work-cost-page-list', { params: data })
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.size1 = data.total;
                        this.data1 = data.list;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取工程包列表失败')
                })
            },
            // 拆分任务
            splitTask() {
                if(this.expTaskTime == 0) {
                    this.formValidate.taskValue = this.workValue;
                }else{
                    this.formValidate.taskValue =  Math.round((this.formValidate.expTaskTime / this.expTaskTime)*this.workValue);
                }
                this.editTaskModel = true;
            },
            // 拆分任务弹出层确认
            affirmBtn(type) {
                let data = new FormData();
                data.append('projectId', this.projectId);
                data.append('stageId', this.stageId);
                data.append('workId', this.workId);
                data.append('taskName', this.formValidate.taskName);
                data.append('taskValue', this.formValidate.taskValue);
                data.append('expTaskTime', this.formValidate.expTaskTime);
                data.append('expStartTime', this.$dateFormat(this.formValidate.expStartTime));
                data.append('expEndTime', this.$dateFormat(this.formValidate.expEndTime));
                data.append('priority', this.formValidate.priority);
                data.append('complexity', this.formValidate.complexity);
                data.append('taskDesc', this.formValidate.taskDesc);
                this.$refs[type].validate((valid) => {
                    if (valid) {
                        this.$http.post('/task', data)
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                this.$Message.success('拆分任务成功！');
                                this.editTaskModel = false;
                                this.closeModal(type);
                            }
                        })
                        .catch(res => {
                            this.$Message.error('拆分任务失败！')
                        });
                    }
                })
            },
             // 关闭弹出层
            closeModal(type) {
                this.$refs[type].resetFields();
                this.detailTab = 'name1';
            },
            // 工程包分页
            changePackage(params) {
                this.index = params;
                this.getPackageList();
            },
            // 确认工程包
            confirmPackage(id) {
                let data = new FormData();
                data.append('id', id);
                data.append('workState', 1);
                this.$http.post('/Work/update-work', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('确认工程包成功！');
                        this.getPackageList();
                    }
                })
                .catch(res => {
                    this.$Message.error('确认工程包失败！')
                })
            },
            // 监听任务估时改变时改变任务价值分
            changeTaskValue() {
                console.log(this.expTaskTime)
                if(this.expTaskTime == 0) {
                    this.formValidate.taskValue = this.workValue;
                }else{
                    this.formValidate.taskValue = (this.formValidate.expTaskTime / this.expTaskTime)*this.workValue;
                }
            },
            // 为表格中的行加class
            rowClassName (row, index) {
                return 'cur-p';
            },
            // 查看工程包详情
            enterDetail(row, index) {
                // console.log(row.id)
                this.workDetailModel = true;
                this.workDetailId = row.id;
                var data= {
                    id: row.id,
                }
                this.$http.get('/Work/work-cost-page-list', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.workDetail = data.list[0];
                    }
                })
                .catch(res => {
                    this.$Message.error('获取工程包详情失败！')
                }) 
            },
            // 关闭详情弹出层
            closeModal2() {
                this.detailTab = 'name1';
            },
            // 详情弹出层里的tab切换
            modalTabClick2(name) {
                this.detailTab = name;
                if(name == 'name2'){
                    this.getPackLogList();
                }
            },
            // 获取工程包操作日志
            getPackLogList() {
                var data= {
                    workId: this.workDetailId,
                }
                this.$http.get('/Work/get-log-work', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.packageLogList = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取工程包日志失败！')
                }) 
            },
            // 弹出层里的tab切换
            modalTabClick(name) {
                this.detailTab = name;
            },
            // 工程包统计以及工程包成本统计
            getWorkCount() {
                var data= {
                    projectId: this.projectId,
                }
                this.$http.get('/Work/work-status-count', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.drawLine(data);
                    }
                })
                .catch(res => {
                    // console.log(res);
                    this.$Message.error('获取工程包统计失败！')
                })
            },
            // echart图表初始化
            drawLine(taskCount){
                // if(taskCount.noStart == 0
                //     && taskCount.haveInIand == 0
                //     && taskCount.delayHaveInHand == 0
                //     && taskCount.waitCheck == 0
                //     && taskCount.completed == 0){
                //     return;
                // }
                // 工程包统计
                let myChart = this.$echarts.init(document.getElementById('myChart'))
                // 工程包统计
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
                                {value:taskCount.noStart, name:'未开始', key: 0},
                                {value:taskCount.haveInIand, name:'进行中', key: 1},
                                {value:taskCount.delayHaveInHand, name:'延期进行中', key: 4},
                                {value:taskCount.waitCheck, name:'待验收', key: 2},
                                {value:taskCount.completed, name:'已完成', key: 3}
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
                myChart.on('click', (params) => {
                    console.log(params.data.key);
                    this.workState = params.data.key;
                    this.getPackageList();

                });
                // 工程包成本消耗
                let myChart2 = this.$echarts.init(document.getElementById('myChart2'))
                // 工程包成本消耗
                myChart2.setOption({
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
                    legend: {
                        icon: 'circle',
                        itemWidth: 15,
                        itemHeight: 15,
                        orient: 'horizontal',
                        bottom: '30',
                        data:['超预算','未超预算']
                    },
                    color:['#1DE9B6','#A389D4'],
                    series: [
                        {
                            name:'工程包成本消耗',
                            type:'pie',
                            radius: ['50%', '60%'],
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
                                {value: taskCount.overBudget, name:'超预算', key: 0},
                                {value: taskCount.onOverBudget, name:'未超预算', key: 1},
                            ]
                        }
                    ]
                });
                myChart2.on('click', (params) => {
                    console.log(params.data.key);
                    this.costStatus = params.data.key;
                    this.getPackageList();
                });
            },
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