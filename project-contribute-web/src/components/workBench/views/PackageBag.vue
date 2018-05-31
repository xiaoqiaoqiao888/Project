<template>
    <div class="cl">
        <div class="cont-bg left-list fl">
            <div class="cl">
                <Breadcrumb title="项目管理" content="我的项目情况" />
            </div>
            <div class="btn-group">
                <ButtonGroup>
                    <Button class="first-btn cl" @click.native="startProject" v-if="projectDetail.projectState == 0">
                        <Icon class="clock-icon"></Icon>
                        开始项目
                    </Button>
                    <Button v-else @click="endProjectList" class="first-btn">关闭项目</Button>
                    <Button class="line-btn" @click.native="splitProjectBtn">拆分</Button>
                    <Button @click="projectOverview">项目概览</Button>
                </ButtonGroup>
            </div>
            <div>
                <div class="detail-project">
                    <div class="edit-btn">
                        <a @click="showEditProjectModel">编辑</a>
                    </div>
                    <ul class="cl">
                        <li>
                            <span>项目负责人：</span>
                            <span class="project-leader">{{projectPrincipal}}</span>
                            <span>预计起止日期：</span>
                            <span>{{$dateFormat2(stageDetail.startTime)}} 至 {{$dateFormat2(stageDetail.endTime)}}</span>
                        </li>
                        <li class="fl-no">
                            <span>阶段预算：</span>
                            <span>{{stageDetail.stageBudget}}RMB</span>
                        </li>
                        <li class="fl-no max-li">
                            <span>阶段描述：</span>
                            <span>{{stageDetail.stageDesc}}</span>
                        </li>
                    </ul>
                </div>
                <div class="mlr-20">
                    <Form :model="checkForm" inline class="table-top-form">
                        <FormItem>
                            <Input type="text" v-model="checkForm.workName" placeholder="工程包名称">
                            </Input>
                        </FormItem>
                        <FormItem>
                            <Select v-model="checkForm.workType" placeholder="请选择工程包类型">
                                <Option value="1">UI设计</Option>
                                <Option value="2">前端开发</Option>
                                <Option value="3">后台开发</Option>
                                <Option value="4">需求分析</Option>
                                <Option value="5">产品测试</Option>
                            </Select>
                        </FormItem>
                        <FormItem>
                            <Button type="primary" @click="searchPackageList">查询</Button>
                        </FormItem>
                    </Form>
                </div>
                <Table :columns="columns1" :data="data1"></Table>
                <div class="page-box">
                    <Page
                        :total="Math.floor(size1)" 
                        :current="Math.floor(index1)" 
                        :page-size="Math.floor(rows)"
                        @on-change="packageChange" 
                        simple>
                    </Page>
                </div>
            </div>
        </div>
        <div class="right-stat fl">
            <div class="cl">
                <UserInfor />
            </div>
            <div class="mlr-20">
                <div class="right-title">
                    工程包预算统计
                </div>
                <div id="myChart" class="myChart"></div>
                <div class="font-cen">
                    <Button type="warning" class="big-btn">追加预算</Button>
                </div>
            </div>
        </div>
        <!-- 拆分工程包弹出层 -->
        <Modal
            v-model="splitPackModel"
            title="拆分工程包"
            class-name="vertical-center-modal"
            :closable="isShowClose"
        >
            <Form ref="splitformValidate" :model="splitformValidate" :rules="ruleValidate" label-position="top">
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
        <!-- 编辑阶段 -->
        <Modal
            v-model="editProjectModel"
            title=""
            class-name="fixed-right-modal"
            :closable="isShowClose"
            :transition-names=transitionNames
            :styles="{animationDuration: '1s'}"
            @on-cancel="closeModal2"
        >
            <Tabs :value="detailTab" @on-click="modalTabClick" :animated= "false">
                <TabPane label="编辑阶段" name="name1">
                    <Form ref="editProjectForm" :model="editProjectForm" :rules="editProjectValidate" label-position="top">
                            <FormItem prop="projectName">
                                <Input v-model="editProjectForm.projectName" placeholder="项目名称"></Input>
                            </FormItem>
                            <FormItem prop="creator">
                                <div>项目负责人：{{projectPrincipal}}</div>
                            </FormItem>
                            <FormItem prop="projectBudget">
                                <Input type="text" placeholder="阶段预算" v-model="editProjectForm.projectBudget" number></Input>
                            </FormItem>
                            <FormItem label="阶段起止日期">
                                <Row>
                                    <Col span="11">
                                        <FormItem prop="startTime">
                                            <DatePicker type="date" placeholder="开始时间" v-model="editProjectForm.startTime"></DatePicker>
                                        </FormItem>
                                    </Col>
                                    <Col span="2" style="height: 40px;"></Col>
                                    <Col span="11">
                                        <FormItem prop="endTime">
                                            <DatePicker type="date" placeholder="结束时间" v-model="editProjectForm.endTime"></DatePicker>
                                        </FormItem>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="阶段描述" prop="projectDesc">
                                <Input v-model="editProjectForm.projectDesc" type="textarea" :autosize="{minRows: 4,maxRows: 5}"></Input>
                            </FormItem>
                            <FormItem class="cl">
                                <Button class="fr" type="warning" @click="editProjectBtn('editProjectForm')" shape="circle">确认提交</Button>
                            </FormItem>
                        </Form>
                </TabPane>
                <TabPane label="历史操作日志" name="name2">
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
                        <FormItem prop="workName">
                            <Input v-model="editPackageForm.workName" placeholder="工程包名称"></Input>
                        </FormItem>
                        <FormItem prop="creator">
                            <div>项目负责人：{{projectPrincipal}}</div>
                        </FormItem>
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
    import * as response from '../../../util/response.js'
    import Breadcrumb from '@/components/common/header/Breadcrumb'
    import UserInfor from '@/components/common/header/UserInfor'
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
            // 编辑阶段校验预算
            const validateStageBudget = (rule, value, callback) => {
                let minVal =  this.batesUser;
                if(!value){
                    callback(new Error('预算不能为空'));
                }else if(!Number.isInteger(value)){
                    callback(new Error('请输入数字'));
                }else{
                    if (value < minVal) {
                        callback(new Error('阶段预算不能小于已拆分的工程包预算总和'));
                    } else {
                        callback();
                    }
                }
            };
            return {
                splitPackModel:false,// 拆分工程包弹出层
                isShowClose: false,
                editProjectModel:false,
                transitionNames:['move-right','fade'],
                checkForm:{
                    workName:'',
                    workType:'',
                },
                splitformValidate:{
                    packName: '',
                    packBudget: '',
                    packType: '',
                    startDate: '',
                    endDate: '',
                    brief: '',
                    packValue: ''
                },
                ruleValidate: {
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
                editPackageForm:{
                    workName:'',
                    workBudget:'',
                    workValue:'',
                    workType:'',
                    startTime:'',
                    endTime:'',
                    workDesc:'',

                },
                
                editPackageValidate: {
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
                columns1: [
                    {
                        title: '名称 / 描述',
                        key: 'name',
                        width:200,
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
                                        // cursor:'pointer',
                                    },
                                    on: {
                                        click: () => {
                                            // this.editPackageModel = true;
                                            // this.openEditPackageModal(params.row.id);
                                        }
                                    }
                                },params.row.workName && params.row.workName.length>2 ? params.row.workName.substring(0, 2) : params.row.workName),
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
                                        },
                                    }, params.row.workName),
                                    h('p',{
                                        style: {
                                            height:'40px',
                                            overflow:'hidden',
                                            fontSize:'12px',
                                        },
                                    },params.row.workDesc),
                                ]),
                            ]);
                        }
                    },
                    {
                        title: '预算',
                        key: ' budget',
                        render: (h, params) => {
                            return h('div', [
                                h('p', {
                                    style: {
                                    },
                                },params.row.workBudget+' RMB'),
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
                                    },Math.ceil((params.row.workBudget)/(this.stageDetail.stageBudget)*100)+'%'),
                                    // params.row.stageBudget/this.$route.query.budget*100+'%'
                                ]),
                                
                            ]);
                        }
                    },
                    {
                        title: '起始日期',
                        key: 'date',
                        render:(h,params) =>{
                            return h('div',{
                            },this.$dateFormat2(params.row.startTime) 
                            + ' 至 ' + this.$dateFormat2(params.row.endTime) )  
                        }
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width:200,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'primary',
                                        disabled: params.row.workState==0 ? false : true
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            if(params.row.workState==0){
                                                this.$Modal.confirm({
                                                    title: '关闭工程包',
                                                    content: '工程一旦关闭，将无法查看，确认关闭吗？',
                                                    onOk: () => {
                                                        this.closePackage(params.row.id);
                                                    },
                                                    onCancel: () => {
                                                        
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }, '关闭'),
                            ]);
                        }
                    }
                ],
                data1: [],
                editProjectForm: {},
                editProjectValidate: {
                    projectName: [
                        { required: true, message: '项目名称不能为空', trigger: 'blur' },
                        { type: 'string', max: 50, message: '最多输入50个字符', trigger: 'blur' }
                    ],
                    projectBudget: [
                        { validator: validateStageBudget, trigger: 'blur' },
                    ],
                    startDate: [
                        { required: true, type: 'date', message: '请选择开始日期', trigger: 'change' }
                    ],
                    endDate: [
                        { required: true, type: 'date', message: '请选择结束日期', trigger: 'change' }
                    ],
                    brief:[{ type: 'string', max: 300, message: '最多输入300个字符', trigger: 'blur' }]
                },
                size1: '',
                index1: 1,
                rows: 5,
                stageDetail:{}, // 项目详情
                projectPrincipal: '', // 项目负责人
                editPackageModel:false,
                batesUser: 0,
                projectDetail:{},
                stageLogList: [], //阶段日志列表
                detailTab: 'name1',
                packageLogList: [], //工程包日志列表
                editPackageBudget: '', // 编辑工程包预算
            }
        },
        components: {
            Breadcrumb,
            UserInfor
        },
        mounted() {
            this.getStageDetail();
            this.getPackageList();
            this.projectDetial();
        },
        created(){
    
        },
        methods: {
            // 项目概览
            projectOverview(){
                this.$router.push({path:'/WorkBench/totalProgress',query:{ id: this.$route.query.projectId }})
            },
            // 项目详情
            projectDetial(){
                var data= {

                }
                this.$http.get('/project/'+this.$route.query.projectId )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.projectDetail = data;
                        // this.getUserList(data.createBy);
                        // this.editProjectForm.name = data.projectName;
                        // this.editProjectForm.budget = data.projectBudget;
                        // this.editProjectForm.startDate =  this.$dateFormat(data.startTime);
                        // this.editProjectForm.endDate =this.$dateFormat(data.endTime);
                        // this.editProjectForm.brief = data.projectDesc;
                        // this.editProjectForm.creator = data.realName;
                        
                        
                        // console.log(data)
                    }
                })
                .catch(res => {
                    this.$Message.error('获取项目详情失败')
                })
            },
            // 工程包预算统计
            drawLine(batesUser){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart'))
                // 绘制图表
                myChart.setOption({
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
                        data:['已使用预算','剩余预算']
                    },
                    color:['#04A9F5','#ECEDEF'],
                    series: [
                        {
                            name:'访问来源',
                            type:'pie',
                            radius: ['58%', '70%'],
                            center: ['50%','30%'],
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
                                {value: batesUser, name:'已使用预算'},
                                {value: this.stageDetail.stageBudget - batesUser, name:'剩余预算'},
                            ]
                        }
                    ]
                });
            },
            // 工程包统计
            statFun(){
                let data = {
                    stageId:this.$route.query.stageId,
                }
                this.$http.get('/Work/work-budget-statistics', {params:data})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.batesUser = data
                        this.drawLine(data);
                    }else { // 返回是空，就默认为0
                        this.drawLine(0);
                    }
                })
                .catch(res => {
                    this.$Message.error('获取工程包统计失败！')
                })
            },
             // 开始项目
            startProject(){
                var data = new FormData();
                data.append('id',this.$route.query.projectId);
                this.$http.post('/project/start-project', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        // this.btnBlock = false;
                        this.$Message.success('项目开始成功！');
                        this.getStageDetail();

                    }
                })
                .catch(res => {
                    this.$Message.error('项目开始失败！')
                })
            },
            // 项目关闭
            endProjectList(){
                var data = new FormData();
                data.append('id',this.$route.query.projectId);
                this.$http.post('/project/end-project', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('项目关闭成功！');  
                    }
                })
                .catch(res => {
                    this.$Message.error('项目关闭失败！')
                })
            },
            // 获取阶段详情
            getStageDetail() {
                var data= {
                    id: this.$route.query.stageId,
                    projectId:this.$route.query.projectId,
                }
                this.$http.get('/state/state-by-id',{ params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.getUserList(data.createBy);
                        this.stageDetail = data;
                        this.editProjectForm.projectName = data.stageName;
                        this.editProjectForm.projectBudget = data.stageBudget;
                        this.editProjectForm.startTime =  this.$dateFormat(data.startTime);
                        this.editProjectForm.endTime =this.$dateFormat(data.endTime);
                        this.editProjectForm.projectDesc = data.stageDesc;
                        // this.editProjectForm.creator = data.realName;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取阶段详情失败')
                })
            },
            // id查询用户
            getUserList(name) {
                var data= {
                    id:name,
                }
                // console.log(data)
                this.$http.get('/sys-user/by-id', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.projectPrincipal = data.realName;
                    }
                })
                .catch(res => {
                    this.$Message.error('查询用户失败！')
                }) 
            },
            //编辑项目弹出层
            showEditProjectModel(){
                this.editProjectModel = true;
                this.getStageDetail();
            },
            // 编辑阶段提交
            editProjectBtn(type){
                let data = new FormData();
                data.append('id',this.$route.query.stageId);
                data.append('projectId',this.$route.query.projectId);
                data.append('stageName',this.editProjectForm.projectName);
                data.append('stageBudget',this.editProjectForm.projectBudget);
                data.append('startTime',this.$dateFormat(this.editProjectForm.startTime));
                data.append('endTime',this.$dateFormat(this.editProjectForm.endTime));
                data.append('stageDesc',this.editProjectForm.projectDesc);
                this.$refs[type].validate((valid) => {
                    if (valid) {
                        this.$http.post('/state/put', data)
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                this.getStageDetail();
                                this.$Message.success('编辑阶段成功！');
                                this.editProjectModel = false;

                            }
                        })
                        .catch(res => {
                            this.$Message.error('编辑阶段失败！')
                        })
                    }
                })
            },
            // 拆分工程包弹出层
            splitProjectBtn() {
              this.splitPackModel = true;  
            },
            // 编辑按钮
            showEditProjectModel() {
                this.editProjectModel = true;
            },
            // 创建工程包
            createPackage (type) {
                let data = new FormData();
                data.append('projectId', parseInt(this.$route.query.projectId));
                data.append('stageId', this.$route.query.stageId);
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
                                this.getPackageList();
                                this.$Message.success('添加工程包成功！');
                                this.splitPackModel = false;
                                this.$refs[type].resetFields();
                            }
                        })
                        .catch(res => {
                            this.$Message.error('添加工程包失败！')
                        })
                    }
                })
            },
            // 获取工程包列表
            getPackageList() {
                let data= {
                    pageSize: this.rows,
                    projectId: parseInt(this.$route.query.projectId),
                    stageId: this.$route.query.stageId,
                    pageNum: this.index1,
                    workName: this.checkForm.workName,
                    workType: this.checkForm.workType,
                }
                this.$http.get('/Work/work-cost-page-list', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.data1 = data.list;
                        this.size1 = data.total;
                        this.statFun();
                    }
                })
                .catch(res => {
                    this.$Message.error('查询用户失败！')
                })
            },
            // 工程包分页
            packageChange(params) {
                this.index1 = params;
                this.getPackageList(params);
            },
            // // 修改工程包提交按钮
            // editPackageBtn(name){
            //     this.$refs[name].validate((valid) => {
            //         if (valid) {
            //             this.openEditPackageModal();
            //         } else {
            //                 }
            //     })
            // },
            // 编辑工程包弹出层
            openEditPackageModal(name) {
                let data = new FormData();
                data.append('id',this.editPackageForm.id);
                data.append('projectId',this.$route.query.projectId);
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
                                this.getPackageList();
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
            closePackage(id) {
                let dataJson = {
                    ids: id
                }
                this.$http.delete('/Work/delete-work', {params: dataJson})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('关闭工程包成功！');
                        this.getPackageList();
                    }
                })
                .catch(res => {
                    this.$Message.error('关闭工程包失败！')
                })
            },
            // 条件查询工程包列表
            searchPackageList() {
                this.index1 = 1;
                this.getPackageList();
            },
            // 阶段弹出层里的tab切换
            modalTabClick(name) {
                this.detailTab = name;
                if(name == 'name2'){
                    this.getStageLogList();
                }else {
                    this.getStageDetail();
                }
            },
            // 获取阶段操作日志
            getStageLogList() {
                var data= {
                    stageId: this.$route.query.stageId,
                }
                this.$http.get('/state/stage-log', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.stageLogList = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取阶段日志失败！')
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
        } 
    }
</script>
<style scoped>
    .left-list .breadcrumb{
        margin-bottom:0px !important;
    }
    .left-list{
        width:55%;
        height:100%;
        background:#fff;
        border-right:1px solid #f3f3f3;
        border-radius:0;
        bottom:0;
        position: fixed;
    }
    .detail-project{
        margin:20px;
        color:#999;
        position: relative;
    }
    .detail-project li{
        line-height:20px;
        width:100%;
        margin-bottom:5px;
        color:#999;
    }
    .detail-project li .project-leader{
        margin-right:100px;
    }
    .detail-project .fl-no{ float:none; width:95%;}
    .detail-project .max-li{ 
        max-height:60px;
        overflow:hidden;
        margin-bottom:30px;
    }
    .detail-project .edit-btn{
        position:absolute;
        right:20px;
        top:0px;
        line-height:20px;
    }
    .detail-project .edit-btn a{
        color:#ABAFB4;
        font-size:14px;
     }
    .right-stat{
        width:21.2%;
        position:fixed;
        right:0;
    }
    .myChart{
        width:80%;
        margin:0 auto;
        height:300px;
    }
</style>