<template>
    <div>
        <div>
            <Form :model="formTask" inline class="table-top-form mlr-20">
                <FormItem>
                    <Input v-model="formTask.name" placeholder="任务名称"></Input>
                </FormItem>
                <FormItem>
                    <Select v-model="formTask.select" placeholder="任务状态" filterable>
                        <!-- <Option value="5">全部</Option> -->
                        <Option value="0">未开始</Option>
                        <Option value="1">进行中</Option>
                        <Option value="2">待验收</Option>
                        <Option value="3">已完成</Option>
                        <Option value="4">延期进行中</Option>
                    </Select>
                </FormItem>
                <FormItem>
                    <Button type="primary" @click="secrchBtnTask">查询</Button>
                </FormItem>
            </Form>
            <Table :columns="taskTable" :data="taskList"></Table>
            <div class="page-box">
                <Page 
                :total="Math.floor(sizeTask)" 
                :current="Math.floor(indexTask)" 
                :page-size="Math.floor(rows)"
                @on-change="changeTask"
                simple></Page>
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
    import * as response from '../../../../util/response.js'
    export default {
        data() {
            return {
                taskDetailModel:false,
                transitionNames:['move-right','fade'],
                rows:5,
                indexTask:1,
                sizeTask:'',
                isShowClose: false,
                formTask:{
                    name:'',
                    select:'',
                },
                taskTable:[
                {
                        title: '名称 / 状态',
                        key: 'sum',
                        width:260,
                        render: (h, params) => {
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
                                        letterSpacing:'3px',
                                    }
                                }, params.row.taskName),
                                h('dd',{
                                    slot: 'content',
                                    style: {
                                        width:'100px',
                                        float: 'left',
                                        textAlign:'left',
                                        lineHeight:'20px',
                                        marginLeft:'15px',
                                        cursor:'pointer',
                                    },
                                    on: {
                                        click: () => {
                                            this.checkTask(params.row.id);
                                            this.taskDetailModel = true;
                                        }
                                    }
                                },[
                                    h('p',{
                                        style: {
                                            color:'#393C40',
                                            width:'100%',
                                            overflow:'hidden',
                                            textOverflow:'ellipsis',
                                            whiteSpace:'nowrap',
                                        },
                                    }, params.row.taskName),

                                    h('p',{
                                        style: {
                                            height:'40px',
                                            fontSize:'12px',
                                            display: '-webkit-box',
                                            webkitBoxOrient: 'vertical',
                                            webkitLineClamp: '2',
                                            overflow: 'hidden',
                                        },
                                    },text),
                                ]),
                            ]);
                        }
                    },
                    {
                        title: '进度',
                        key: ' taskSchedule',
                        render: (h, params) => {
                            return h('div', [
                                // h('p', {
                                //     style: {
                                //     },
                                // },Math.ceil(params.row.stageSchedule) +'%'),
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
                                    },Math.round(params.row.taskSchedule*100) +'%'),
                                ]),
                                
                            ]);
                        }
                    },
                    {
                        title: '消耗成本占比',
                        key: ' useCostRatio',
                        render: (h, params) => {
                            return h('div', [
                                // h('p', {
                                //     style: {
                                //     },
                                // },Math.ceil(params.row.stageSchedule) +'%'),
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
                                    },Math.round(params.row.useCostRatio*100) +'%'),
                                ]),
                                
                            ]);
                        }
                    },
                    {
                        title: '截止时间',
                        key: 'expEndTime',
                        render:(h,params) =>{
                            return h('div',{
                            },this.$dateFormat2(params.row.expEndTime) )  
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
                taskList:[],
                taskDetail: {},
                taskPrincipal: '', // 任务负责人
                taskId: '', // 任务id
                taskLogList: [], //任务日志列表
                detailTab: 'name1',
            }
        },
        mounted() {
            this.taskListFun();
        },
        created(){
    
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
        methods: {
            changeTask(params){
                this.indexTask = params;
                this.taskListFun();
            },
            // 任务列表
            taskListFun(){
                let data= {
                    pageSize: this.rows,
                    projectId: parseInt(this.$route.query.id),
                    pageNum: this.indexTask,
                    taskName:this.formTask.name,
                    taskState:this.formTask.select,
                }
                this.$http.get('/task/select-TaskList-By-PageInfo', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.taskList = data.list;
                        this.sizeTask = data.total;
                        // this.taskFun();
                        // console.log(data)
                    }
                })
                .catch(res => {
                    this.$Message.error('查询工程包统计列表失败！')
                })
            },
            // 任务统计查询按钮
            secrchBtnTask(){
                this.indexTask = 1;
                this.taskListFun();
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
            // 关闭任务
            closeTask(taskId) {
                this.$http.put(`/task?id=${taskId}&taskState=0&state=0`)
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.$Message.success('关闭任务成功');
                        this.taskListFun();
                    }
                })
                .catch(res => {
                    // console.log(res);
                    this.$Message.error('关闭任务失败');
                })
            },
        } 
    }
</script>
<style scoped>
    
</style>