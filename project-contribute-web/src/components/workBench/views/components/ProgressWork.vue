<template>
    <div>
        <div>
            <Form :model="checkForm" inline class="table-top-form mlr-20">
                <FormItem>
                    <Input v-model="checkForm.workName" placeholder="工程包名称"></Input>
                </FormItem>
                <FormItem>
                    <Select v-model="checkForm.workState" placeholder="工程包状态" filterable>
                        <!-- <Option value="5">全部</Option> -->
                        <Option value="0">未开始</Option>
                        <Option value="1">进行中</Option>
                        <Option value="2">待验收</Option>
                        <Option value="3">已完成</Option>
                        <Option value="4">延期进行中</Option>
                    </Select>
                </FormItem>
                <FormItem>
                    <Button type="primary" @click="secrchBtnStage">查询</Button>
                    <Button type="primary" class="mlr-20" @click="splitWorkBtn">划分工程包</Button>
                </FormItem>
            </Form>
            <Table :columns="packageTable" :data="packageList"></Table>
            <div class="page-box">
                <Page 
                :total="Math.floor(sizePackage)" 
                :current="Math.floor(indexPackage)" 
                :page-size="Math.floor(rows)"
                @on-change="changePackage"
                simple></Page>
            </div>
        </div>
        <!-- 拆分工程包弹出层 -->
        <Modal
            v-model="splitPackModel"
            title="拆分工程包"
            class-name="vertical-center-modal"
            :closable="isShowClose"
            @on-cancel="closeModal('splitformValidate')"
        >
            <Form ref="splitformValidate" :model="splitformValidate" :rules="ruleValidate" label-position="top">
                <FormItem prop="stageId">
                    <Select v-model="splitformValidate.stageId" placeholder="阶段名称" filterable @on-change="changeBudget">
                        <Option v-for="item in stageList" :value="item.id" :key="item.id">{{ item.stageName }}</Option>
                    </Select>
                </FormItem>
                <FormItem prop="packName">
                    <Input v-model="splitformValidate.packName" placeholder="工程包名称"></Input>
                </FormItem>
                <FormItem prop="packBudget">
                    <Input v-model="splitformValidate.packBudget" placeholder="工程包预算" number></Input>
                </FormItem>
                <FormItem prop="packValue">
                    <Input v-model="splitformValidate.packValue" placeholder="价值分" number></Input>
                </FormItem>
                <FormItem prop="packType">
                    <Select v-model="splitformValidate.packType" placeholder="工程包类型">
                        <Option value="1">UI设计</Option>
                        <Option value="2">前端开发</Option>
                        <Option value="3">后台开发</Option>
                        <Option value="4">需求分析</Option>
                        <Option value="5">产品测试</Option>
                    </Select>
                </FormItem>
                <FormItem label="实施周期">
                    <Row>
                        <Col span="11">
                            <FormItem prop="startDate">
                                <DatePicker type="date" placeholder="开始时间" v-model="splitformValidate.startDate"></DatePicker>
                            </FormItem>
                        </Col>
                        <Col span="2" style="height: 40px;"></Col>
                        <Col span="11">
                            <FormItem prop="endDate">
                                <DatePicker type="date" placeholder="结束时间" v-model="splitformValidate.endDate"></DatePicker>
                            </FormItem>
                        </Col>
                    </Row>
                </FormItem>
                <FormItem label="工程包描述" prop="brief">
                    <Input v-model="splitformValidate.brief" type="textarea" :autosize="{minRows: 4,maxRows: 5}"></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="warning" @click="createPackage('splitformValidate')" shape="circle">创建工程包</Button>
            </div>
        </Modal>
        <!-- 编辑工程包 -->
        <Modal
            v-model="editPackageModel"
            title=""
            class-name="fixed-right-modal"
            :closable="isShowClose"
            :transition-names=transitionNames
            :styles="{animationDuration: '1s'}"
            @on-cancel="closeModal2"
        >
            <Tabs :value="detailTab" @on-click="modalTabClick2" :animated= "false">
                <TabPane label="工程包" name="name1" >
                     <Form ref="editPackageForm" :model="editPackageForm" :rules="editPackageValidate" label-position="top">
                        <FormItem prop="stageId">
                            <Select v-model="editPackageForm.stageId" placeholder="阶段名称" filterable @on-change="changeBudget">
                                <Option v-for="item in stageList" :value="item.id" :key="item.id">{{ item.stageName }}</Option>
                            </Select>
                        </FormItem>
                        <FormItem prop="workName">
                            <Input v-model="editPackageForm.workName" placeholder="工程包名称"></Input>
                        </FormItem>
                        <!-- <FormItem prop="creator">
                            <div>项目负责人：{{projectPrincipal}}</div>
                        </FormItem> -->
                        <FormItem prop="workBudget">
                            <Input type="text" placeholder="工程包预算" v-model="editPackageForm.workBudget" number></Input>
                        </FormItem>
                        <FormItem prop="workValue">
                            <Input v-model="editPackageForm.workValue" placeholder="价值分" number></Input>
                        </FormItem>
                        <FormItem prop="workType">
                            <Select v-model="editPackageForm.workType" placeholder="工程包类型">
                                <Option :value="1">UI设计</Option>
                                <Option :value="2">前端开发</Option>
                                <Option :value="3">后台开发</Option>
                                <Option :value="4">需求分析</Option>
                                <Option :value="5">产品测试</Option>
                                <!-- <Option v-for="item in editPackageForm.workType" 
                                :value="item.value" 
                                :key="item.value"
                                >{{ item.label }}</Option> -->
                            </Select>
                        </FormItem>
                        <FormItem label="实施周期">
                            <Row>
                                <Col span="11">
                                    <FormItem prop="startTime">
                                        <DatePicker type="date" placeholder="开始时间" v-model="editPackageForm.startTime"></DatePicker>
                                    </FormItem>
                                </Col>
                                <Col span="2" style="height: 40px;"></Col>
                                <Col span="11">
                                    <FormItem prop="endTime">
                                        <DatePicker type="date" placeholder="结束时间" v-model="editPackageForm.endTime"></DatePicker>
                                    </FormItem>
                                </Col>
                            </Row>
                        </FormItem>
                        <FormItem label="工程包描述" prop="workDesc">
                            <Input v-model="editPackageForm.workDesc" type="textarea" :autosize="{minRows: 4,maxRows: 5}"></Input>
                        </FormItem>
                        <FormItem class="cl">
                            <Button class="fr" type="warning" @click="openEditPackageModal('editPackageForm')" shape="circle">确认提交</Button>
                        </FormItem>
                    </Form>      
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
        </Modal>
    </div>
</template>
<script>
    import * as response from '../../../../util/response.js'
    export default {
        data() {
            // 拆分工程包预算校验
            const validateBudget = (rule, value, callback) => {
                let minVal =  this.stageDetail.stageBudget - this.batesUser;
                if(!value){
                    callback(new Error('预算不能为空'));
                }else if(!Number.isInteger(value)){
                    callback(new Error('请输入数字'));
                }else{
                    if (value > minVal) {
                        callback(new Error('工程包预算不能大于阶段剩余预算'));
                    } else {
                        callback();
                    }
                }
            };
            // 编辑工程包校验预算
            const validateEditBudget = (rule, value, callback) => {
                // console.log(this.editStageBudget)
                let minVal =  this.stageDetail.stageBudget - this.batesUser + this.editPackageBudget;
                if(!value){
                    callback(new Error('预算不能为空'));
                }else if(!Number.isInteger(value)){
                    callback(new Error('请输入数字'));
                }else{
                    if (value > minVal) {
                        callback(new Error('工程包预算不能大于阶段剩余预算'));
                    } else {
                        callback();
                    }
                }
            };
            return {
                transitionNames:['move-right','fade'],
                rows:5,
                indexPackage:1,
                sizePackage:'',
                isShowClose: false,
                checkForm:{
                    workName:'',
                    workState:'',
                },
                editPackageForm:{
                    stageId: '',
                    workName:'',
                    workBudget:'',
                    workValue:'',
                    workType:'',
                    startTime:'',
                    endTime:'',
                    workDesc:'',

                },
                packageTable:[
                    {
                        title: '名称 / 状态',
                        key: 'sum',
                        width:260,
                        render: (h, params) => {
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
                                }, params.row.workName),
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
                                            this.editPackageModel = true;
                                            this.editPackageForm = params.row;
                                            this.editPackageForm.workName =params.row.workName;
                                            this.editPackageForm.workBudget =params.row.workBudget;
                                            this.editPackageBudget = params.row.workBudget;
                                            this.editPackageForm.workType = params.row.workType;
                                            this.editPackageForm.startTime= new Date(this.$dateFormat(params.row.startTime));
                                            this.editPackageForm.endTime= new Date(this.$dateFormat(params.row.endTime));
                                            this.editPackageForm.workValue =params.row.workValue;
                                            this.editPackageForm.workDesc = params.row.workDesc;
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
                                    }, params.row.workName),

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
                                    },Math.round(params.row.taskSchedule) +'%'),
                                ]),
                                
                            ]);
                        }
                    },
                    {
                        title: '消耗成本占比',
                        key: ' workCost',
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
                                    },Math.round(params.row.workCost) +'%'),
                                ]),
                                
                            ]);
                        }
                    },
                    {
                        title: '截止时间',
                        key: 'endTime',
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
                                            this.closePackage(params.row.id,params.row.stageId)
                                        }
                                    }
                                }, '关闭'),
                            ]);
                        }
                    }
                ],
                packageList:[],
                detailTab: 'name1',
                packageLogList: [], //工程包日志列表
                editPackageBudget: '', // 编辑工程包预算
                batesUser: 0,
                editPackageModel:false,
                splitPackModel: false,
                splitformValidate:{
                    stageId: '',
                    packName: '',
                    packBudget: '',
                    packType: '',
                    startDate: '',
                    endDate: '',
                    brief: '',
                    packValue: ''
                },
                ruleValidate: {
                    stageId: [
                        { required: true,type: 'number', message: '请选择阶段', trigger: 'change' }
                    ],
                    packName: [
                        { required: true, message: '项目名称不能为空', trigger: 'blur' },
                        { type: 'string', max: 50, message: '最多输入50个字符', trigger: 'blur' }
                    ],
                    packBudget: [
                        // { type: 'number', min: 1, max: 9999999999, message: '请输入10位以下的数字', trigger: 'blur' }
                        { validator: validateBudget, trigger: 'blur'}
                    ],
                    packValue: [
                        { type: 'number', min: 1, max: 9999999999, message: '请输入10位以下的数字', trigger: 'blur' }
                    ],
                    packType: [
                        { required: true, message: '请选择工程包类型', trigger: 'change' }
                    ],
                    startDate: [
                        { required: true, type: 'date', message: '请选择开始日期', trigger: 'change' }
                    ],
                    endDate: [
                        { required: true, type: 'date', message: '请选择结束日期', trigger: 'change' }
                    ],
                    brief:[{ type: 'string', max: 300, message: '最多输入300个字符', trigger: 'blur' }]
                },
                editPackageValidate: {
                    stageId: [
                        { required: true,type: 'number', message: '请选择阶段', trigger: 'change' }
                    ],
                    workName: [
                        { required: true, message: '项目名称不能为空', trigger: 'blur' },
                        { type: 'string', max: 50, message: '最多输入50个字符', trigger: 'blur' }
                    ],
                    workBudget: [
                        { validator: validateEditBudget, trigger: 'blur' },
                    ],
                    workValue: [
                        { type: 'number', min: 1, max: 9999999999, message: '请输入10位以下的数字', trigger: 'blur' }
                    ],
                    workType: [
                        { required: true,type: 'number', message: '请选择工程包类型', trigger: 'change' }
                    ],
                    startTime: [
                        { required: true, type: 'date', message: '请选择开始日期', trigger: 'change' }
                    ],
                    endTime: [
                        { required: true, type: 'date', message: '请选择结束日期', trigger: 'change' }
                    ],
                    workDesc:[{ type: 'string', max: 300, message: '最多输入300个字符', trigger: 'blur' }]
                },
                stageDetail:{}, // 阶段详情
                stageList: [], // 阶段列表

            }
        },
        mounted() {
            this.packageListFun();
            this.getStageList();
        },
        created(){
    
        },
        methods: {
            // 获取阶段列表
            getStageList() {
                let data ={
                    projectId: this.$route.query.id
                }
                this.$http.get('/state/all-stages', {params: data})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.stageList = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取阶段列表失败')
                })   
            },
            // 分页
            changePackage(params){
                this.indexPackage = params;
                this.packageListFun();
            },

            // 工程包列表
            packageListFun(){
                let data= {
                    pageSize: this.rows,
                    projectId: parseInt(this.$route.query.id),
                    pageNum: this.indexPackage,
                    workName:this.checkForm.workName,
                    workState:this.checkForm.workState,
                }
                this.$http.get('/Work/work-cost-page-list', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.packageList = data.list;
                        this.sizePackage = data.total;
                        // this.packageFun();
                        // console.log(data)
                    }
                })
                .catch(res => {
                    this.$Message.error('查询工程包统计列表失败！')
                })
            },
            // 工程包统计查询按钮
            secrchBtnStage(){
                this.indexPackage = 1;
                this.packageListFun();
            },
            // 拆分工程包
            splitWorkBtn() {
              this.splitPackModel = true;  
            },
             // 创建工程包
            createPackage (type) {
                let data = new FormData();
                data.append('projectId', parseInt(this.$route.query.id));
                data.append('stageId', this.splitformValidate.stageId);
                data.append('startTime', this.$dateFormat(this.splitformValidate.startDate));
                data.append('endTime', this.$dateFormat(this.splitformValidate.endDate));
                data.append('workBudget', this.splitformValidate.packBudget);
                data.append('workDesc', this.splitformValidate.brief);
                data.append('workName', this.splitformValidate.packName);
                data.append('workType', parseInt(this.splitformValidate.packType));
                data.append('workValue', this.splitformValidate.packValue);
                this.$refs[type].validate((valid) => {
                    if (valid) {
                        this.$http.post('/Work/add-work', data)
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                this.packageListFun();
                                this.$Message.success('添加工程包成功！');
                                this.splitPackModel = false;
                                this.closeModal(type);
                            }
                        })
                        .catch(res => {
                            this.$Message.error('添加工程包失败！')
                        })
                    }
                })
            },
            closeModal(type) {
                this.$refs[type].resetFields();
            },
            // 编辑工程包弹出层提交
            openEditPackageModal(name) {
                let data = new FormData();
                data.append('id',this.editPackageForm.id);
                data.append('projectId',this.$route.query.id);
                data.append('workName',this.editPackageForm.workName);
                data.append('workBudget',this.editPackageForm.workBudget);
                data.append('workType',this.editPackageForm.workType);
                data.append('startTime',new Date(this.$dateFormat(this.editPackageForm.startTime)));
                data.append('endTime',new Date(this.$dateFormat(this.editPackageForm.endTime)));
                data.append('workValue',this.editPackageForm.workValue);
                data.append('workDesc',this.editPackageForm.workDesc);
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.$http.post('/Work/update-work', data )
                        .then(res => {
                            var data = response.data(res, this);
                            if (data) {
                                this.packageListFun();
                                this.editPackageModel = false; 
                                this.$Message.success('编辑工程包成功！')
                                console.log(data)
                            }
                        })
                        .catch(res => {
                            this.$Message.error('编辑工程包失败！')
                        })
                    }
                })     
            },
            // 关闭工程包
            closePackage(id,stageId) {
                let dataJson = {
                    ids: id
                }
                this.$http.delete('/Work/delete-work', {params: dataJson})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('关闭工程包成功！');
                        this.packageListFun();
                        this.changeBudget(stageId); // 刷新预算
                    }
                })
                .catch(res => {
                    this.$Message.error('关闭工程包失败！')
                })
            },
            // 关闭编辑弹出层
            closeModal2() {
                this.detailTab = 'name1';
            },
            // 工程包弹出层里的tab切换
            modalTabClick2(name) {
                this.detailTab = name;
                if(name == 'name2'){
                    this.getPackLogList();
                }
            },
            // 获取工程包操作日志
            getPackLogList() {
                var data= {
                    workId: this.editPackageForm.id,
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
            // 添加或者编辑里改变select查询工程包已用预算和阶段总预算，以限制工程包预算
            changeBudget(val){
                // console.log(val);
                this.statFun(val);
                this.getStageDetail(val);
            },
            // 工程包统计
            statFun(stageId){
                let data = {
                    stageId: stageId,
                }
                this.$http.get('/Work/work-budget-statistics', {params:data})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.batesUser = data
                    }
                })
                .catch(res => {
                    this.$Message.error('获取工程包统计失败！')
                })
            },
            // 获取阶段详情
            getStageDetail(stageId) {
                var data= {
                    id: stageId,
                    projectId:this.$route.query.id,
                }
                this.$http.get('/state/state-by-id',{ params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.stageDetail = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取阶段详情失败')
                })
            },
        } 
    }
</script>
<style scoped>
    
</style>