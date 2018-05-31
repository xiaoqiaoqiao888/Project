<template>
    <div>
        <div class="cl">
            <Breadcrumb title="任务管理" content="我的任务情况" />
            <UserInfor />
        </div>
        <div class="cont-bg mlr-20">
            <div class="project-list-table po_re">
                <Form :model="FormItem" inline class="pos-form" v-if="showQuery">
                    <FormItem>
                        <Input type="text" v-model="FormItem.taskName" placeholder="任务名称">
                        </Input>
                    </FormItem>
                    <FormItem>
                        <Button type="primary" @click="queryTask">查询</Button>
                    </FormItem>
                </Form>
                <Tabs value="name1" @on-click="tabClick" :animated= "false">
                    <TabPane label="可领取的任务" name="name1">
                        <Table :columns="columns1" :data="data1"></Table>
                        <div class="page-box">
                            <Page 
                                :total="Math.floor(size1)" 
                                :current="Math.floor(index1)" 
                                :page-size="Math.floor(rows)"
                                @on-change="allChange" 
                                simple>
                            </Page>
                        </div>
                    </TabPane>
                    <TabPane label="我的任务" name="name2">
                        <!-- 我的任务统计模块儿 -->
                        <div class="task-stat" v-if="this.taskStat.count > 0">
                            <span class="left">{{puncTually}}</span>
                            <span class="left-txt">准时完成的任务</span>
                            <span class="left-num">{{leftNum}}</span>
                            <Progress :percent="puncTuallys" hide-info></Progress>
                            <span class="right">{{noPuncTually}}</span>
                            <span class="right-txt">未准时完成的任务</span>
                            <span class="right-num">{{rightNum}}</span>
                        </div>
                        <Table :columns="columns2" :data="data2"></Table>
                        <div class="page-box">
                            <Page 
                                :total="Math.floor(size2)" 
                                :current="Math.floor(index2)" 
                                :page-size="Math.floor(rows)"
                                @on-change="mineChange" 
                                simple>
                            </Page>
                        </div>
                    </TabPane>
                </Tabs>
            </div>
        </div>
        <!-- 任务详情 -->
        <Modal
            v-model="taskDetailModel"
            title=""
            class-name="fixed-right-modal"
            :closable="isShowClose"
            :transition-names=transitionNames
            :styles="{animationDuration: '1s'}"
            @on-cancel="closeModal"
        >
            <div class="model-cont">
                <Tabs :value="detailTab" @on-click="modalTabClick" :animated= "false">
                    <TabPane label="任务详情" name="name1">
                        <div class="cl detail-item">
                            <span class="fl datail-label">任务名称：</span>
                            <span class="fl datail-cont">{{taskDetail.task_name}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">项目名称：</span>
                            <span class="fl datail-cont">{{taskDetail.project_name}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">项目负责人：</span>
                            <span class="fl datail-cont">{{taskDetail.project_person_name}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">任务预计耗时：</span>
                            <span class="fl datail-cont">{{taskDetail.exp_task_time}}h</span>
                        </div>
                        <div class="cl detail-item" v-if="taskDetail.rel_task_time != undefined">
                            <span class="fl datail-label">任务实际耗时：</span>
                            <span class="fl datail-cont">{{taskDetail.rel_task_time}}h</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">预计起止日期：</span>
                            <span class="fl datail-cont">{{$dateFormat2(taskDetail.exp_start_time)}} 至 {{$dateFormat2(taskDetail.exp_end_time)}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">价值分：</span>
                            <span class="fl datail-cont">{{taskDetail.task_value}}分</span>
                        </div>
                        <div class="cl detail-item" v-if="taskDetail.rel_start_time">
                            <span class="fl datail-label">任务实际开始时间：</span>
                            <span class="fl datail-cont">{{$timeFormat2(taskDetail.rel_start_time)}}</span>
                        </div>
                        <div class="cl detail-item" v-if="taskDetail.rel_end_time">
                            <span class="fl datail-label">任务实际提交时间：</span>
                            <span class="fl datail-cont">{{$timeFormat2(taskDetail.rel_end_time)}}</span>
                        </div>
                        <div class="cl detail-item" v-if="taskDetail.task_person_id">
                            <span class="fl datail-label">负责人：</span>
                            <span class="fl datail-cont">{{taskPrincipal}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">复杂度：</span>
                            <span class="fl datail-cont">{{taskDetail.complexity | formatComplexity}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">优先级：</span>
                            <span class="fl datail-cont">{{taskDetail.priority | formatPriority}}</span>
                        </div>
                        <div class="cl detail-item">
                            <span class="fl datail-label">描述：</span>
                            <span class="fl datail-cont">{{taskDetail.task_desc}}</span>
                        </div>
                        <div class="cl detail-item" v-if="taskDetail.task_person_id">
                            <span class="fl datail-label">进度：</span>
                            <span class="fl datail-cont progress-cont">
                                <Slider v-model="taskDetail.task_schedule"></Slider>
                            </span>
                        </div>
                        <div class="detail-item cl"
                            v-if="taskDetail.task_person_id && !(taskDetail.task_state == 2 || taskDetail.task_state == 3)"
                        >
                            <Button class="fr" type="warning" @click="updateTaskSchedule" shape="circle">确认</Button>
                        </div>
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
            </div>
        </Modal>
    </div>
</template>
<script>
    import Cookie from 'js-cookie'
    import * as response from '../../../util/response.js'
    import Breadcrumb from '@/components/common/header/Breadcrumb'
    import UserInfor from '@/components/common/header/UserInfor'
    export default {
        data() {
            return {
                isShowClose: false,
                transitionNames:['move-right','fade'],
                taskDetailModel:false,
                FormItem:{
                    taskName:'',
                    projectName:'',
                },
                taskDetailForm:{
                    name:'',
                },
                columns1: [
                    {
                        title: '名称',
                        key: 'name',
                        width:180,
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
                                },params.row.project_name),
                                h('dd',{
                                    slot: 'content',
                                    style: {
                                        width:'80px',
                                        float: 'left',
                                        textAlign:'left',
                                        lineHeight:'20px',
                                        marginLeft:'15px',
                                    },
                                },[
                                    h('p',{
                                        style: {
                                            color:'#393C40',
                                            height:'40px',
                                            overflow:'hidden',
                                            cursor:'pointer',
                                        },
                                        on: {
                                            click: () => {
                                                this.checkTask(params.row.id);
                                                this.taskDetailModel = true;
                                            }
                                        }
                                    }, params.row.task_name),
                                    
                                ]),
                            ]);
                        }
                    },
                    {
                        title: '预估用时',
                        key: ' exp_task_time',
                        render: (h, params) => {
                            return h('div', [
                                h('p', {
                                    style: {
                                        color:'#000',
                                        fontSize:'18px',
                                    },
                                },params.row.exp_task_time+'h'),
                                
                                
                            ]);
                        }
                    },
                    {
                        title: '所得价值分',
                        key: 'task_value',
                        render: (h, params) => {
                            return h('div', [
                                h('p', {
                                    style: {
                                        color:'#FF9E22',
                                        fontSize:'18px',
                                    },
                                },params.row.task_value+'分'),
                                
                                
                            ]);
                        }
                    },
                    {
                        title: '优先级',
                        key: 'priority',
                        render: (h, params) => {
                            const row = params.row;
                            const text = row.priority == 0 ? '低' : row.priority == 1 ? '中' : '高';
                            const color = row.priority == 0 ? '#A78ED6' : row.priority == 1 ? '#1DE9B6' : '#FF314A';
                            return h('div', [
                                h('p', {
                                    style: {
                                        color: color,
                                    },
                                },[
                                    h('span',{
                                        style: {
                                            display:'inline-block',
                                            width:'4px',
                                            height:'4px',
                                            borderRadius:'50px',
                                            background: color,
                                            verticalAlign:'middle',
                                            marginRight:'10px',
                                        },
                                    }),
                                    h('span',{
                                        style: {
                                            color: color,
                                        },
                                    }, text), // 高中低
                                ]),
                                
                                
                            ]);
                        }
                    },
                    {
                        title: '复杂度',
                        key: 'complexity',
                        render: (h, params) => {
                            const row = params.row;
                            const text = row.complexity == 0 ? '简单' : row.complexity == 1 ? '一般' : '复杂';
                            const color = row.complexity == 0 ? '#A78ED6' : row.complexity == 1 ? '#1DE9B6' : '#FF314A';
                            return h('div', [
                                h('p', {
                                    style: {
                                        color: color,
                                    },
                                },[
                                    h('span',{
                                        style: {
                                            display:'inline-block',
                                            width:'4px',
                                            height:'4px',
                                            borderRadius:'50px',
                                            background: color,
                                            verticalAlign:'middle',
                                            marginRight:'10px',
                                        },
                                    }),
                                    h('span',{
                                        style: {
                                            color: color,
                                        },
                                    }, text), // 简单 一般
                                ]),
                                
                                
                            ]);
                        }
                    },
                    {
                        title: '截止时间',
                        key: 'exp_end_time',
                        render:(h,params) =>{
                            return h('span',{
                            },this.$dateFormat2(params.row.exp_end_time) )  
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
                                            this.receiveTask(params.row.id, params.row.project_id);
                                        }
                                    }
                                }, '领取'),
                            ]);
                        }
                    }
                ],
                data1: [],
                index1:1,
                rows:5,
                size1:'',
                index2:1,
                size2:'',
                columns2: [
                    {
                        title: '名称',
                        key: 'name',
                        width:180,
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
                                },params.row.project_name),
                                h('dd',{
                                    slot: 'content',
                                    style: {
                                        width:'80px',
                                        float: 'left',
                                        textAlign:'left',
                                        lineHeight:'20px',
                                        marginLeft:'15px',
                                    },
                                },[
                                    h('p',{
                                        style: {
                                            color:'#393C40',
                                            height:'40px',
                                            overflow:'hidden',
                                            cursor:'pointer',
                                        },
                                        on: {
                                            click: () => {
                                                this.checkTask(params.row.id);
                                                this.taskDetailModel = true;
                                            }
                                        }
                                    }, params.row.task_name),
                                    
                                ]),
                            ]);
                        }
                    },
                    {
                        title: '预估用时',
                        key: ' exp_task_time',
                        render: (h, params) => {
                            return h('div', [
                                h('p', {
                                    style: {
                                        color:'#000',
                                        fontSize:'18px',
                                    },
                                },params.row.exp_task_time+'h'),
                                
                                
                            ]);
                        }
                    },
                    {
                        title: '状态',
                        key: 'task_state', // 0.未认领,1.进行中任务,2.待验收3.完成任务4.延期进行中.5.延期
                        render: (h, params) => {
                            let text;
                            if(params.row.task_state == 1){
                                text='进行中任务'
                            }else if(params.row.task_state == 2){
                               text='待验收' 
                            }else if(params.row.task_state == 3){
                               text='完成任务' 
                            }else if(params.row.task_state == 4){
                               text='延期进行中' 
                            }else if(params.row.task_state == 5){
                               text='延期' 
                            }
                            return h('span', text);
                        }
                    },
                    {
                        title: '优先级',
                        key: 'priority',
                        render: (h, params) => {
                            const row = params.row;
                            const text = row.priority == 0 ? '低' : row.priority == 1 ? '中' : '高';
                            const color = row.priority == 0 ? '#A78ED6' : row.priority == 1 ? '#1DE9B6' : '#FF314A';
                            return h('div', [
                                h('p', {
                                    style: {
                                        color: color,
                                    },
                                },[
                                    h('span',{
                                        style: {
                                            display:'inline-block',
                                            width:'4px',
                                            height:'4px',
                                            borderRadius:'50px',
                                            background: color,
                                            verticalAlign:'middle',
                                            marginRight:'10px',
                                        },
                                    }),
                                    h('span',{
                                        style: {
                                            color: color,
                                        },
                                    }, text), // 高中低
                                ]),
                                
                                
                            ]);
                        }
                    },
                    {
                        title: '复杂度',
                        key: 'complexity',
                        render: (h, params) => {
                            const row = params.row;
                            const text = row.complexity == 0 ? '简单' : row.complexity == 1 ? '一般' : '复杂';
                            const color = row.complexity == 0 ? '#A78ED6' : row.complexity == 1 ? '#1DE9B6' : '#FF314A';
                            return h('div', [
                                h('p', {
                                    style: {
                                        color: color,
                                    },
                                },[
                                    h('span',{
                                        style: {
                                            display:'inline-block',
                                            width:'4px',
                                            height:'4px',
                                            borderRadius:'50px',
                                            background: color,
                                            verticalAlign:'middle',
                                            marginRight:'10px',
                                        },
                                    }),
                                    h('span',{
                                        style: {
                                            color: color,
                                        },
                                    }, text), // 简单 一般
                                ]),
                                
                                
                            ]);
                        }
                    },
                    {
                        title: '截止时间',
                        key: 'exp_end_time',
                        render:(h,params) =>{
                            return h('span',{
                            },this.$dateFormat2(params.row.exp_end_time) )  
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
                                        disabled: params.row.task_state == 1
                                            || params.row.task_state == 4
                                            || params.row.task_state == 5 ? false : true
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            if(params.row.task_state == 1 || params.row.task_state == 4 || params.row.task_state == 5){
                                                this.commitTask(params.row.id);
                                            }
                                        }
                                    }
                                }, '确认提交'),
                            ]);
                        }
                    }
                ],
                data2: [],
                showQuery: true,
                taskDetail: {},
                taskPrincipal: '', // 任务负责人
                taskId: '', // 任务id
                taskStat:[],
                taskLogList: [], //任务日志列表
                detailTab: 'name1',
                taskStat:'',
                puncTually:'',//准时完成的百分比
                noPuncTually:'',//未准时完成百分比
                puncTuallys:0,//准时完成的进度条
                leftNum:'',//准时完成的数字
                rightNum:'',//未准时完成的数字
            }
        },
        components: {
            Breadcrumb,
            UserInfor
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
            this.getTaskList();
        },
        created(){
    
        },
        methods: {
            // 个人任务统计
            taskStatistical(){
                let data = new FormData();
                this.$http.post('/task/count-task')
                .then(res => {
                    let data = response.data(res, this);
                    if(data){
                        this.taskStat = data;
                        this.puncTually = Math.round([this.taskStat.count-(this.taskStat.countDelay + this.taskStat.countOvertime)]/this.taskStat.count*100) +'%';
                        this.noPuncTually = Math.round((this.taskStat.countDelay + this.taskStat.countOvertime )/this.taskStat.count*100) +'%';
                        this.puncTuallys = [this.taskStat.count-(this.taskStat.countDelay + this.taskStat.countOvertime)]/this.taskStat.count*100;
                        this.leftNum = this.taskStat.count-(this.taskStat.countDelay + this.taskStat.countOvertime);
                        this.rightNum = this.taskStat.countDelay + this.taskStat.countOvertime;
                    }
                })
                .catch(res => {

                })
            },
            // 查询可领取的任务
            queryTask(){
                this.getTaskList();
            },
            // 点击tab切换
            tabClick(name) {
                if(name == 'name2') {
                    // console.log(JSON.parse(Cookie.get('userInf')).id);
                    this.getTaskList(JSON.parse(Cookie.get('userInf')).id);
                    this.taskStatistical();
                    this.showQuery = false;
                }else {
                    this.getTaskList();
                    this.showQuery = true;
                }
            },
            // 获取任务列表
            getTaskList(personId) { // 传入taskPersonId为我的任务，不传入为可领取的任务
                let data = new FormData();
                data.append('rows', this.rows);
                data.append('taskName', this.FormItem.taskName);
                // data.append('projectName', this.FormItem.projectName);
                if(personId){
                    data.append('page', this.index2);
                    data.append('taskPersonId', personId);
                    data.append('orderByState', 2);
                }else{
                    data.append('page', this.index1);
                    data.append('orderByState', 1);
                }
                this.$http.post('/task/list', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        if(personId){
                            this.data2 = data.list;
                            this.size2 = data.total;
                            console.log(this.data2)
                        }else{
                            this.data1 = data.list;
                            this.size1 = data.total;
                        }
                    }
                })
                .catch(res => {
                    this.$Message.error('获取任务列表失败！')
                })
            },
            // 所有任务的分页
            allChange(params) {
                this.index1 = params;
                this.getTaskList();
            },
            // 我的任务的分页
            mineChange(params) {
                this.index2 = params;
                this.getTaskList(JSON.parse(Cookie.get('userInf')).id);
            },
            // 领取任务
            receiveTask(taskId,projectId) {
                // console.log(JSON.parse(Cookie.get('userInf')).id)
                this.$http.put(`/task?id=${taskId}&taskState=1&state=1&projectId=${projectId}&taskPersonId=${JSON.parse(Cookie.get('userInf')).id}`)
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.$Message.success('领取任务成功');
                        this.getTaskList();
                    }
                })
                .catch(res => {
                    // console.log(res);
                    this.$Message.error('领取任务失败');
                })
            },
            // 提交任务
            commitTask(taskId) {
                this.$http.put(`/task?id=${taskId}&taskState=2&state=1&taskSchedule=100`)
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.$Message.success('提交任务成功');

                        this.getTaskList(JSON.parse(Cookie.get('userInf')).id);
                    }
                })
                .catch(res => {
                    // console.log(res);
                    this.$Message.error('提交任务失败');
                })
            },
            // 查看任务
            checkTask(id) {
                if(id){
                    this.taskId = id;
                }
                let data = {
                    id: this.taskId,
                };
                this.$http.get('/task/taskDetail', {params: data})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.taskDetail = data;
                        if(data.task_person_id){
                            this.getUserList(data.task_person_id)
                        }
                    }
                })
                .catch(res => {
                    this.$Message.error('获取任务详情失败！')
                })
            },
            // id查询用户
            getUserList(userId){
                var data= {
                    id:userId,
                }
                this.$http.get('/sys-user/by-id', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.taskPrincipal = data.realName;
                    }
                })
                .catch(res => {
                    this.$Message.error('查询用户失败！')
                }) 
            },
            // 修改任务进度
            updateTaskSchedule() {
                // console.log(this.$timeFormat(new Date()));
                this.$http.put(`/task?id=${this.taskId}&taskSchedule=${this.taskDetail.task_schedule}`)
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.$Message.success('修改任务成功');
                        this.taskDetailModel = false;
                    }
                })
                .catch(res => {
                    // console.log(res);
                    this.$Message.error('修改任务失败');
                })
            },
            // 弹出层里的tab切换
            modalTabClick(name) {
                this.detailTab = name;
                if(name == 'name2'){
                    this.getLogList();
                }else {
                    this.checkTask();
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
            // 关闭弹出层
            closeModal() {
                this.detailTab = 'name1';
            },
        } 
    }
</script>
<style lang="less" scoped>
    .project-list-table{
        overflow: hidden;
    }
</style>