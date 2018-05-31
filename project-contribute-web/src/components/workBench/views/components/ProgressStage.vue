<template>
    <div>
        <div>
            <Form :model="stageform" inline class="table-top-form mlr-20">
                <FormItem>
                    <Input v-model="stageform.name" placeholder="阶段名称"></Input>
                </FormItem>
                <FormItem>
                    <Select v-model="stageform.select" placeholder="阶段状态" filterable>
                            <Option value="0">未开始</Option>
                            <Option value="1">进行中</Option>
                            <Option value="2">待验收</Option>
                            <Option value="3">已完成</Option>
                            <Option value="4">延期进行中</Option>
                    </Select>
                </FormItem>
                <FormItem>
                    <Button type="primary" @click="secrchBtnStage">查询</Button>
                    <Button type="primary" class="mlr-20"
                    @click="stageModelBtn">划分阶段</Button>
                </FormItem>
            </Form>
            <Table :columns="stageTable" :data="stageList"></Table>
            <div class="page-box">
                <Page 
                :total="Math.floor(sizeStage)" 
                :current="Math.floor(indexStage)" 
                :page-size="Math.floor(rows)"
                @on-change="changeStage"
                simple></Page>
            </div>
        </div>
        <!-- 划分阶段弹出层 -->
        <Modal
            v-model="divideModel"
            title="添加阶段"
            class-name="vertical-center-modal"
            :closable="isShowClose"
        >
            <Form ref="stageFormValidate" :model="stageFormValidate" :rules="stageRuleValidate" label-position="top">
                <FormItem prop="name">
                    <Input v-model="stageFormValidate.name" placeholder="阶段名称"></Input>
                </FormItem>
                <FormItem prop="budget">
                    <Input type="text" v-model="stageFormValidate.budget" placeholder="阶段预算" number></Input>
                </FormItem>
                <FormItem label="阶段实施周期">
                    <Row>
                        <Col span="11">
                            <FormItem prop="startDate">
                                <DatePicker type="date" placeholder="开始时间" v-model="stageFormValidate.startDate"></DatePicker>
                            </FormItem>
                        </Col>
                        <Col span="2" style="height: 40px;"></Col>
                        <Col span="11">
                            <FormItem prop="endDate">
                                <DatePicker type="date" placeholder="结束时间" v-model="stageFormValidate.endDate"></DatePicker>
                            </FormItem>
                        </Col>
                    </Row>
                </FormItem>
                <FormItem label="阶段描述" prop="brief">
                    <Input v-model="stageFormValidate.brief" type="textarea" :autosize="{minRows: 4,maxRows: 5}"></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="warning" shape="circle" @click="divideSaveBtn('stageFormValidate')">保存</Button>
            </div>
        </Modal>
        <!-- 编辑阶段 -->
        <Modal
            v-model="stageModel"
            title=""
            class-name="fixed-right-modal"
            :closable="isShowClose"
            :transition-names=transitionNames
            :styles="{animationDuration: '1s'}"
            @on-cancel="closeModal2"
        >
            <Tabs :value="detailTab" @on-click="modalTabClick2" :animated= "false">
                <TabPane label="编辑阶段" name="name1">
                    <Form ref="editStageForm" :model="editStageForm" :rules="editStageValidate" label-position="top">
                        <FormItem prop="name">
                            <Input v-model="editStageForm.name" placeholder="阶段名称"></Input>
                        </FormItem>
                        <!-- <FormItem>
                            <div>项目负责人：{{editStageForm.creator}}</div>
                        </FormItem> -->
                        <FormItem prop="budget">
                            <Input type="text" v-model="editStageForm.budget" placeholder="阶段预算" number></Input>
                        </FormItem>
                        <FormItem label="阶段实施周期">
                            <Row>
                                <Col span="11">
                                    <FormItem prop="startDate">
                                        <DatePicker type="date" placeholder="开始时间" v-model="editStageForm.startDate"></DatePicker>
                                    </FormItem>
                                </Col>
                                <Col span="2" style="height: 40px;"></Col>
                                <Col span="11">
                                    <FormItem prop="endDate">
                                        <DatePicker type="date" placeholder="结束时间" v-model="editStageForm.endDate"></DatePicker>
                                    </FormItem>
                                </Col>
                            </Row>
                        </FormItem>
                        <FormItem label="阶段描述" prop="brief">
                            <Input v-model="editStageForm.brief" type="textarea" :autosize="{minRows: 4,maxRows: 5}"></Input>
                        </FormItem>
                        <FormItem class="cl">
                            <Button class="fr" type="warning" @click="editStageBtn('editStageForm')" shape="circle">确认提交</Button>
                        </FormItem>
                    </Form>
                </TabPane>
                <TabPane label="日志" name="name2">
                    <ul class="log-list">
                        <li v-for="item in stageLogList" :key="item.id" class="cl">
                            <i class="clock-ico fl"></i>
                            <div class="fl">
                                <span class="operator">{{item.realName}}</span>
                                <p class="operation">{{item.operateDesc}}</p>
                            </div>
                            <span class="fr time">{{$timeFormat(item.updateTime)}}</span>
                        </li>
                    </ul>
                </TabPane>
            </Tabs>
        </Modal>
    </div>
</template>
<script>
    import * as response from '../../../../util/response.js'
    export default {
        data() {
            const validateBudget = (rule, value, callback) => {
                let minVal =  this.projectDetail.projectBudget - this.batesUser;
                if(!value){
                    callback(new Error('预算不能为空'));
                }else if(!Number.isInteger(value)){
                    callback(new Error('请输入数字'));
                }else{
                    if (value > minVal) {
                        callback(new Error('阶段预算不能大于项目剩余预算'));
                    } else {
                        callback();
                    }
                }
            };
            // 编辑阶段校验预算
            const validateEditBudget = (rule, value, callback) => {
                // console.log(this.editStageBudget)
                let minVal =  this.projectDetail.projectBudget - this.batesUser+this.editStageBudget;
                if(!value){
                    callback(new Error('预算不能为空'));
                }else if(!Number.isInteger(value)){
                    callback(new Error('请输入数字'));
                }else{
                    if (value > minVal) {
                        callback(new Error('阶段预算不能大于项目剩余预算'));
                    } else {
                        callback();
                    }
                }
            };
            return {
                detailTab: '', // 弹出层里的tab切换
                transitionNames:['move-right','fade'],
                indexStage:1,
                rows:5,
                sizeStage:'',
                isShowClose: false,
                divideModel:false,
                stageModel:false,
                stageFormValidate: {
                    budget:'',
                    startDate: '',
                    endDate: '',
                    name: '',
                    brief:'',
                },
                stageRuleValidate: {
                    name: [
                        { required: true, message: '阶段名称不能为空', trigger: 'blur' },
                    ],
                    budget:[
                        // { required: true, message: '预算不能为空', trigger: 'blur' },
                        { validator: validateBudget, trigger: 'blur'}
                    ],
                    startDate: [
                        { required: true, type: 'date', message: '请选择开始日期', trigger: 'change' }
                    ],
                    endDate: [
                        { required: true, type: 'date', message: '请选择结束日期', trigger: 'change' }
                    ],
                    brief:[]
                },
                editStageForm:{
                    startDate: '',
                    endDate: '',
                    name: '',
                    budget:'',
                    brief: '',
                },
                editStageValidate: {
                    name: [
                        { required: true, message: '项目名称不能为空', trigger: 'blur' },
                        { type: 'string', max: 50, message: '最多输入50个字符', trigger: 'blur' }
                    ],
                    budget:[
                        { validator: validateEditBudget, trigger: 'blur' },
                    ],
                    startDate: [
                        { required: true, type: 'date', message: '请选择开始日期', trigger: 'change' }
                    ],
                    endDate: [
                        { required: true, type: 'date', message: '请选择结束日期', trigger: 'change' }
                    ],
                },
                stageform:{
                    name:'',
                    select:'',
                },
                stageTable:[
                    {
                        title: '名称 / 状态',
                        key: 'sum',
                        width:260,
                        render: (h, params) => {
                            let text;
                            if(params.row.stageState == 0){
                                text='未开始'
                            }else if(params.row.stageState == 1){
                               text='进行中' 
                            }else if(params.row.stageState == 2){
                               text='待验收' 
                            }else if(params.row.stageState == 3){
                               text='已完成' 
                            }else if(params.row.stageState == 4){
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
                                }, params.row.stageName),
                                h('dd',{
                                    slot: 'content',
                                    style: {
                                        width:'100px',
                                        float: 'left',
                                        textAlign:'left',
                                        lineHeight:'20px',
                                        marginLeft:'15px',
                                    },
                                },[
                                    h('p',{
                                        style: {
                                            color:'#393C40',
                                            width:'100%',
                                            overflow:'hidden',
                                            textOverflow:'ellipsis',
                                            whiteSpace:'nowrap',
                                            cursor:'pointer',
                                        },
                                        on:{
                                            click:() => {
                                                this.stageModel = true;
                                                this.editStageForm.id = params.row.id;
                                                this.editStageForm.name = params.row.stageName;
                                                this.editStageForm.budget = params.row.stageBudget;
                                                this.editStageBudget = params.row.stageBudget;
                                                this.editStageForm.startDate = this.$dateFormat(params.row.startTime);
                                                this.editStageForm.endDate = this.$dateFormat(params.row.endTime);
                                                this.editStageForm.brief = params.row.stageDesc;
                                            }
                                        },
                                    }, params.row.stageName),

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
                        key: ' plan',
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
                                    },Math.round(params.row.plan*100) +'%'),
                                ]),
                                
                            ]);
                        }
                    },
                    {
                        title: '消耗成本占比',
                        key: ' cost',
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
                                    },Math.round(params.row.cost*100) +'%'),
                                ]),
                                
                            ]);
                        }
                    },
                    {
                        title: '截止时间',
                        key: 'date',
                        render:(h,params) =>{
                            return h('div',{
                            },this.$dateFormat2(params.row.endTime) )  
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
                                            this.delectStage(params.row.id);
                                        }
                                    }
                                }, '关闭'),
                            ]);
                        }
                    }
                ],
                stageList:[], // 阶段列表
                stageLogList: [], // 阶段日志列表
                editStageBudget: '', // 编辑阶段的预算
                batesUser: 0, // 已使用预算
                projectDetail: {}, // 项目详情
            }
        },
        mounted() {
            this.getProjectBudget();
            this.statFun();
            this.stageListFun();
        },
        created(){
    
        },
        methods: {
            // 阶段列表
            stageListFun(){
                let data = new FormData();
                data.append('projectId',parseInt(this.$route.query.id));
                data.append('pageSize',this.rows);
                data.append('pageNum',this.indexStage);
                data.append('stageName',this.stageform.name);
                data.append('stageState',this.stageform.select);
                this.$http.post('/state/list-page', data )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.stageList = data.list;
                        this.sizeStage = data.total;
                        // this.stageFun();
                        // console.log(data)
                    }
                })
                .catch(res => {
                    this.$Message.error('查询阶段统计列表失败！')
                })
            },
            // 查询阶段列表
            secrchBtnStage() {
                this.indexStage = 1;
                this.stageListFun();
            },
            //阶段划分
            stageModelBtn(){
                this.divideModel = true;
            },
            // 划分保存
            divideSaveBtn(type){
                let data = new FormData();
                // console.log(this.$route)
                data.append('projectId',this.$route.query.id);
                data.append('stageName',this.stageFormValidate.name);
                data.append('stageBudget',this.stageFormValidate.budget);
                data.append('startTime',this.$dateFormat(this.stageFormValidate.startDate));
                data.append('endTime',this.$dateFormat(this.stageFormValidate.endDate));
                data.append('stageDesc',this.stageFormValidate.brief);
                // console.log(data)
                this.$refs[type].validate((valid) => {
                    if (valid) {
                        this.$http.post('/state', data)
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                this.$Message.success('划分阶段成功!');
                                this.divideModel = false;
                                this.stageListFun();
                                // this.projectDetial();
                                // this.closeModal(type);
                            }
                        })
                        .catch(res => {
                            this.$Message.error('划分阶段失败！')
                        })
                    }
                }) 
            },
            // 删除阶段
            delectStage(id){
                let data = new FormData();
                data.append('id',id); 
                data.append('projectId',this.$route.query.id);
                this.$http.post('/state/delete', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.stageListFun();
                    }
                })
                .catch(res => {
                    this.$Message.error('删除阶段失败！')
                })
            },
            // 关闭编辑弹出层
            closeModal2() {
                this.detailTab = 'name1';
            },
            // 分页
            changeStage(params){
                this.indexStage = params;
                this.stageListFun();
            },
            // 编辑阶段弹出层里的tab切换
            modalTabClick2(name) {
                this.detailTab = name;
                if(name == 'name2'){
                    this.getStageLogList();
                }
            },
            // 获取阶段操作日志
            getStageLogList() {
                var data= {
                    stageId: this.editStageForm.id,
                }
                this.$http.get('/state/stage-log', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.stageLogList = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('查询阶段日志失败！')
                }) 
            },
            // 编辑阶段提交
            editStageBtn(name){
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        let data = new FormData();
                        data.append('id',this.editStageForm.id);
                        data.append('projectId',this.$route.query.id);
                        data.append('stageName',this.editStageForm.name);
                        data.append('stageBudget',this.editStageForm.budget);
                        data.append('startTime',this.$dateFormat(this.editStageForm.startDate));
                        data.append('endTime',this.$dateFormat(this.editStageForm.endDate));
                        data.append('stageDesc',this.editStageForm.brief);
                        // console.log(data)
                        this.$http.post('/state/put', data)
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                this.stageListFun();
                                this.stageModel = false;
                                this.$Message.success('编辑阶段成功！');
                            }
                        })
                        .catch(res => {
                            // console.log(res)
                            this.$Message.error('编辑阶段失败！')
                        }) 
                    }
                }) 
            },
            // 阶段统计
            statFun(){
                let data = new FormData();
                data.append('projectId',this.$route.query.id)
                this.$http.post('/state/state-sum', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.batesUser = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取阶段统计失败')
                })
            },
            // 获取项目总预算
            getProjectBudget(){
                this.$http.get('/project/'+this.$route.query.id )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.projectDetail = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取项目详情失败')
                })
            },
        } 
    }
</script>
<style scoped>
    
</style>