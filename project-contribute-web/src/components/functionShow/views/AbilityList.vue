<template>
    <div>
        <div class="cl">
            <Breadcrumb title="职能组能力展示" />
            <UserInfor />
        </div>
        <div class="cont-bg mlr-20 min-h">
            <div class="top-form cl">
                <Form :model="formItem" inline>
                    <FormItem class="formCheckbox">
                        <RadioGroup v-model="formItem.period">
                                <Radio label="1"><span>最近一个月</span></Radio>
                                <Radio label="3"><span>最近三个月</span></Radio>
                                <Radio label="6"><span>最近六个月</span></Radio>
                                <Radio label="12"><span>最近一年</span></Radio>
                            </RadioGroup>
                    </FormItem>
                    <FormItem class="fr btn-0">
                        <Button type="primary" @click="secrchList">查询</Button>
                    </FormItem>
                    <FormItem class="fr">
                        <Input v-model="formItem.name" placeholder="员工姓名"></Input>
                    </FormItem>
                </Form>
            </div>
            <div class="mt-20">
                <Table :columns="columns1" :data="data1" class="ourTable"></Table>
            </div>
            <div class="page-box cl">
                <Page 
                :total="Math.floor(size)" 
                :current="Math.floor(index)" 
                :page-size="Math.floor(rows)"
                @on-change="change"
                simple></Page>
            </div>
        </div>
        <!-- 工作追溯 -->
        <Modal
            v-model="WorkBackModel"
            title=""
            class-name="fixed-right-modal"
            :closable="isShowClose"
            :transition-names=transitionNames
            :styles="{animationDuration: '1s'}"
            :width='900'>
            <div class="model-cont">
                <div class="cl">
                    <Breadcrumb title="项目管理" content="工作轨迹追溯" />
                </div>
                <Form :model="formItemModel" inline>
                    <FormItem class="formCheckbox modelCheckbox">
                        <RadioGroup v-model="formItemModel.period" @on-change = "checkBoxFun">
                            <Radio label="1"><span>最近一个月</span></Radio>
                            <Radio label="3"><span>最近三个月</span></Radio>
                            <Radio label="6"><span>最近六个月</span></Radio>
                            <Radio label="12"><span>最近一年</span></Radio>
                        </RadioGroup>
                    </FormItem>
                </Form>
                <div>
                    <Row :gutter="20" class="mb-20">
                        <Col span="6">
                            <div class="card-box line-card-box">
                                <span>总任务</span>
                                <p>{{workBack.allTaskCount}}个</p>
                            </div>
                        </Col>
                        <Col span="6">
                            <div class="card-box">
                                <span>超时任务</span>
                                <p>{{workBack.overTimeTaskCount}}个</p>
                            </div>
                        </Col>
                        <Col span="6">
                            <div class="card-box">
                                <span>耗时<预估任务</span>
                                <p>{{workBack.aheadOfTimeTaskCount}}个</p>
                            </div>
                        </Col>
                        <Col span="6">
                            <div class="card-box">
                                <span>进行中任务</span>
                                <p>{{workBack.haveInHandTaskCount}}个</p>
                            </div>
                        </Col>
                    </Row>
                    <Row :gutter="16">
                        <Col span="6">
                            <div class="card-box line-card-box">
                                <span>延期进行中任务</span>
                                <p>{{workBack.delayHaveInHandTaskCount}}个</p>
                            </div>
                        </Col>
                        <Col span="6">
                            <div class="card-box">
                                <span>待验收任务</span>
                                <p>{{workBack.waitForAcceptanceTaskCount}}个</p>
                            </div>
                        </Col>
                        <Col span="6">
                            <div class="card-box">
                                <span>已完成任务</span>
                                <p>{{workBack.completeTaskCount}}个</p>
                            </div>
                        </Col>
                    </Row>
                </div>
                <Timeline class="timeLine">
                    <TimelineItem v-for="(taskList,key) in taskListOne" :key="taskList.id">
                        <h2>
                            <!-- <span class="title">滴滴清风项目组</span> -->
                            <span class="date">{{key}}</span>
                        </h2>
                        <div class="timeLineTxt" v-for="list in taskList" :key="list.id">
                            <p v-for="taskDetailTwo in list" :key="taskDetailTwo.id">
                                <a @click="taskDetails(taskDetailTwo.id)" style="color:#999;">
                                    <span class="date" v-if="taskDetailTwo.actionTime == taskDetailTwo.rel_start_time ">
                                        {{$timeFormat3(taskDetailTwo.rel_start_time)}}&nbsp;&nbsp;开始{{taskDetailTwo.task_name}}
                                    </span>
                                    <span class="date" v-else-if="taskDetailTwo.actionTime == taskDetailTwo.rel_end_time ">
                                        {{$timeFormat3(taskDetailTwo.rel_end_time)}}&nbsp;&nbsp;提交{{taskDetailTwo.task_name}}
                                    </span>
                                </a>
                            </p>
                        </div>
                    </TimelineItem>
                </Timeline>
            </div>
        </Modal>
        <!-- 价值分明细 -->
        <Modal
            v-model="pointsDetailModel"
            title=""
            class-name="fixed-right-modal"
            :closable="isShowClose"
            :transition-names=transitionNames
            :styles="{animationDuration: '1s'}"
            :width='900'>
            <div class="model-cont">
                <div class="cl">
                    <Breadcrumb title="项目管理" content="价值分明细" />
                </div>
                <div>
                    <Table :columns="columns2" :data="pointers"></Table> 
                    <div class="page-box cl page-box-model">
                        <Page 
                        :total="Math.floor(size1)" 
                        :current="Math.floor(index1)" 
                        :page-size="Math.floor(rows1)"
                        @on-change="change1"
                        simple></Page>
                    </div>
                </div> 
            </div>
        </Modal>
        <!-- 来源 -->
        <Modal
            v-model="sourceDetailModel"
            title=""
            class-name="fixed-right-modal"
            :closable="isShowClose"
            :transition-names=transitionNames
            :styles="{animationDuration: '1s'}"
            :width="700"
        >
            <Tabs value="name1" :animated="false">
                <TabPane label="任务详情" name="name1">
                    <ul class="detailSource cl">
                        <li>
                            <p>
                                任务状态：<span>{{taskDetail.task_state}}</span>
                            </p>
                        </li>
                        <li v-if="taskDetail.advanceTime >= 0">
                            <p>
                                耗时<预估任务：<span>提前{{taskDetail.advanceTime}}小时提交任务</span>
                            </p>
                        </li>
                        <li v-else>
                            <p>
                                超时任务：<span>超时{{Math.abs(taskDetail.advanceTime)}}小时提交任务</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                任务名称：<span>{{taskDetail.task_name}}</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                任务预计耗时：<span>{{taskDetail.exp_task_time}}小时</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                任务实际耗时：<span>{{taskDetail.rel_task_time}}小时</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                预计起止日期：<span>{{$dateFormat(taskDetail.exp_start_time)}}~{{this.$dateFormat(taskDetail.exp_end_time)}}</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                价值分：<span>{{taskDetail.task_value}}分</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                任务实际开始时间：<span>{{$timeFormat(taskDetail.rel_start_time)}}</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                任务实际提交时间：<span>{{$timeFormat(taskDetail.rel_end_time)}}</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                负责人：<span>{{taskDetail.real_name}}</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                复杂度：<span>{{taskDetail.complexity | formatComplexity}}</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                优先级：<span>{{taskDetail.priority | formatPriority}}</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                所属项目：<span>{{taskDetail.project_name}}</span>
                            </p>
                        </li>
                        <li>
                            <p>
                                描述：<span>{{taskDetail.task_desc}}</span>
                            </p>
                        </li>
                    </ul>
                </TabPane>
                <TabPane label="日志" name="name2">
                    <ul class="log-list">
                        <li class="cl" v-for="taskLog in taskLogs" :key="taskLog.id">
                            <i class="clock-ico fl"></i>
                            <div class="fl">
                                <span class="operator">{{taskLog.real_name}}</span>
                                <p class="operation">{{taskLog.operate_desc}}</p>
                            </div>
                            <span class="fr time">{{$timeFormat(taskLog.create_time)}}</span>
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
                index:1,
                rows:5,
                size:'',
                index1:1,
                rows1:5,
                size1:'',
                isShowClose: false,
                transitionNames:['move-right','fade'],
                WorkBackModel:false,
                pointsDetailModel:false,
                sourceDetailModel:false,
                formItem: {
                    period : '',
                    name:'',
                },
                formItemModel:{
                    period: '',
                },
                columns1: [
                    {
                        title: '员工号',
                        key: 'user_no',
                        width:100,
                    },
                    {
                        title: '姓名',
                        key: 'user_name'
                    },
                    {
                        title: '手机号',
                        key: 'tel'
                    },
                    {
                        title: '邮箱',
                        key: 'email'
                    },
                    {
                        title: '价值分',
                        key: 'grade',
                        render: (h, params) => {
                            return h('div',{
                                style: {
                                    fontSize:'18px',
                                    color:'#FF9E22',
                                    cursor:'pointer',
                                },
                                on: {
                                    click:() => {
                                        this.ids = params.row.id;
                                        this.pointsDetailModel = true;
                                        this.valuePointsDetail();
                                    }
                                }
                            },params.row.valuePoints)
                        }
                    },
                    {
                        title: '操作',
                        key: 'action',
                        render: (h, params) => {
                            return h('Button',{
                                props: {
                                    type: 'primary',
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.ids = params.row.id;
                                        this.WorkBackModel = true; 
                                        console.log(this.ids)
                                        this.workBackFun();                                          
                                    }
                                }
                            },'工作追溯')
                        }
                    }
                ],
                data1: [],
                columns2: [
                    {
                        title: '时间',
                        key: 'rel_start_time',
                    },
                    {
                        title: '获取数量',
                        key: 'task_value'
                    },
                    {
                        title: '来源',
                        key: 'source',
                        render: (h, params) => {
                            return h('div', [
                                h('a', {
                                    style:{
                                        color:'#999',
                                    },
                                    on:{
                                        click:()=>{
                                            // this.id = params.row.id;
                                            this.sourceDetailModel = true;
                                            this.pointsDetailModel = false;
                                            this.taskDetailFun(params.row.id);
                                        }
                                    }
                                }, params.row.task_name +'('+'编号'+ params.row.task_code+')'),
                                
                            ]);
                        }
                    },
                ],
                pointers:[],
                workBack:[],
                ids:'',
                taskDetail:[],
                taskListOne:[],
                taskLogs:[],
            } 
        },
        components: {
            Breadcrumb,
            UserInfor,
        },
        filters: {
            formatPriority: function (value) {
                const text = value == 0 ? '低' : value == 1 ? '中' : '高';
                return text;
            },
            formatComplexity: function (value) {
                const text = value == 0 ? '简单' : value == 1 ? '一般' : '复杂';
                return text;
            },
        },
        mounted() {
            // this.getPowerList();
        },
        created(){
    
        },
        methods: {
            taskDetails(id){
                // console.log(id)
                this.sourceDetailModel = true;
                this.WorkBackModel= false;
                this.taskDetailFun(id);
            },
            // 获取能力展示列表
            getPowerList(groupId){
                if(groupId){
                    this.groupId = groupId;
                }
                // console.log(groupId)
                let data = new FormData();
                data.append('page',this.index);
                data.append('rows',this.rows);
                data.append('groupId',this.groupId);
                data.append('realName',this.formItem.name);
                data.append('cycle',this.formItem.period);
                this.$http.post('/ability-exhibition/select-ability-exhibition', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.data1 = data.list;
                        this.size = data.total;
                        // console.log(data)
                    }
                })
                .catch(res => {
                    this.$Message.error('获取能力列表失败！')
                })
            },
            // 查询
            secrchList(){
                this.getPowerList();
            },
            change(params){
                // console;log(params)
                this.index = params;
                this.getPowerList();
            },
            // 工作追溯
            workBackFun(){
                let data = new FormData();
                data.append('userId',this.ids);
                data.append('cycle', this.formItemModel.period);
                this.$http.post('/ability-exhibition/select-task-tracing', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.workBack = data;
                        this.taskListOne = data.taskList;
                    }
                })
                .catch(res => {
                    console.log(res)
                    this.$Message.error('工作追溯获取失败！')
                })
            },
            // 工作追溯勾选时间
            checkBoxFun(){
                this.workBackFun();
            },
            // 价值分明细
            valuePointsDetail(){
                let data = new FormData();
                data.append('sysUserId',this.ids);
                this.$http.post('/ability-exhibition/select-value-points-details',data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.pointers = data.list;
                         console.log(data)
                    }
                })
                .catch(res => {
                    this.$Message.error('获取价值分列表失败')
                })
            },
            change1(params){
                // console;log(params)
                this.index1 = params;
                this.valuePointsDetail(params);
            },
            // 任务详情
            taskDetailFun(id){
                let data = new FormData();
                data.append('taskId',id)
                this.$http.post('/ability-exhibition/select-task-details',data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.taskDetail = data.taskInfo;
                        this.taskLogs = data.taskLog;
                        console.log(this.taskLogs)
                    }
                })
                .catch(res => {
                    this.$Message.error('获取任务详情失败')
                })
            },
        }
    }
</script>
<style scoped>
    .formCheckbox{ width:60%;}
    .btn-0{ margin-right:0;}
    .modelCheckbox{ width:100%;}
    .card-box{
        background: #F3F3F3;
        border-radius: 6px;
        padding:20px;
        font-size:14px;
        color: #AAAEB3;
        text-align: center;
    }
    .card-box p{
        font-size: 24px;
        color: #359FFF;
        margin-top:5px;
    }
    .detailSource li{
        width:50%;
        float:left;
        line-height:30px;
        margin-bottom:25px;
        font-size:14px;
    }
    .line-card-box{
        border-left:2px solid #FF9E22;
        border-radius:0 6px 6px 0;
    }
    .page-box-model ul{
        width:90%
    }
</style>