<template>
    <div class="cl">
        <div class="cont-bg left-list fl">
            <div class="cl">
                <Breadcrumb title="项目管理" type="select" @sendSelected="getSelectedProject" />
            </div>
            <div class="btn-group">
                <ButtonGroup>
                    <Button class="first-btn cl" @click.native="startProject" v-if="projectDetail.projectState == 0">
                        <Icon class="clock-icon"></Icon>
                        开始项目
                    </Button>
                    <Button v-else @click="closeProjectBtn" class="first-btn">关闭项目</Button>
                    <!-- <Button class="first-btn cl" @click.native="startProject">
                        <Icon class="clock-icon"></Icon>
                        开始项目
                    </Button>
                    <Button @click="closeProjectBtn" class="line-btn">关闭项目</Button> -->
                    <Button class="line-btn" @click.native="showDivideModel('formValidate')">划分</Button>
                    <Button @click="projectOverview">项目概览</Button>
                </ButtonGroup>
            </div>
            <div>
                <div class="detail-project">
                    <div class="edit-btn">
                        <a @click="showEditProjectModel('editProjectForm')">编辑</a>
                    </div>
                    <ul class="cl">
                        <li>
                            <span>项目负责人：</span>
                            <span class="project-leader">{{projectPrincipal}}</span>
                            <span>预计起止日期：</span>
                            <span>{{$dateFormat2(projectDetail.startTime)}} 至 {{$dateFormat2(projectDetail.endTime)}}</span>
                        </li>
                        <li class="fl-no">
                            <span>项目预算：</span>
                            <span>{{projectDetail.projectBudget}}RMB</span>
                        </li>
                        <li class="fl-no max-li">
                            <span>项目描述：</span>
                            <span>{{projectDetail.projectDesc}}</span>
                        </li>
                    </ul>
                </div>
                <Table :columns="stageTable" :data="stage" class="project-table"></Table>
                <div class="page-box">
                    <Page 
                    :total="Math.floor(size)" 
                    :current="Math.floor(index)" 
                    :page-size="Math.floor(rows)"
                    @on-change="change"
                    simple></Page>
                </div>
            </div>
        </div>
        <div class="right-stat fl">
            <div class="cl">
                <UserInfor />
            </div>
            <div class="mlr-20">
                <div class="right-title">
                    阶段预算统计
                </div>
                <div id="myChart" class="myChart"></div>
                <div class="font-cen">
                    <Button type="warning" class="big-btn">追加预算</Button>
                </div>
            </div>
        </div>
        <!-- 划分阶段弹出层 -->
        <Modal
            v-model="divideModel"
            title="添加阶段"
            class-name="vertical-center-modal"
            :closable="isShowClose"
        >
            <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" label-position="top">
                <FormItem prop="name">
                    <Input v-model="formValidate.name" placeholder="阶段名称"></Input>
                </FormItem>
                <FormItem prop="budget">
                    <Input type="text" v-model="formValidate.budget" placeholder="阶段预算" number></Input>
                </FormItem>
                <FormItem label="阶段实施周期">
                    <Row>
                        <Col span="11">
                            <FormItem prop="startDate">
                                <DatePicker type="date" placeholder="开始时间" v-model="formValidate.startDate"></DatePicker>
                            </FormItem>
                        </Col>
                        <Col span="2" style="height: 40px;"></Col>
                        <Col span="11">
                            <FormItem prop="endDate">
                                <DatePicker type="date" placeholder="结束时间" v-model="formValidate.endDate"></DatePicker>
                            </FormItem>
                        </Col>
                    </Row>
                </FormItem>
                <FormItem label="阶段描述" prop="brief">
                    <Input v-model="formValidate.brief" type="textarea" :autosize="{minRows: 4,maxRows: 5}"></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="warning" @click="divideSaveBtn('formValidate')" shape="circle">保存</Button>
            </div>
        </Modal>
        <!-- 编辑项目 -->
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
                <TabPane label="编辑项目" name="name1">
                    <Form ref="editProjectForm" :model="editProjectForm" :rules="editProjectValidate" label-position="top">
                            <FormItem prop="name">
                                <Input v-model="editProjectForm.name" placeholder="项目名称"></Input>
                            </FormItem>
                            <FormItem prop="creator">
                                <div>项目负责人：{{editProjectForm.creator}}</div>
                            </FormItem>
                            <FormItem prop="budget">
                                <Input type="text" v-model="editProjectForm.budget" placeholder="项目预算" number></Input>
                            </FormItem>
                            <FormItem label="项目起止日期">
                                <Row>
                                    <Col span="11">
                                        <FormItem prop="startDate">
                                            <DatePicker type="date" placeholder="开始时间" v-model="editProjectForm.startDate"></DatePicker>
                                        </FormItem>
                                    </Col>
                                    <Col span="2" style="height: 40px;"></Col>
                                    <Col span="11">
                                        <FormItem prop="endDate">
                                            <DatePicker type="date" placeholder="结束时间" v-model="editProjectForm.endDate"></DatePicker>
                                        </FormItem>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="项目描述" prop="brief">
                                <Input v-model="editProjectForm.brief" type="textarea" :autosize="{minRows: 4,maxRows: 5}"></Input>
                            </FormItem>
                            <FormItem label="核心人员" prop="">
                                <Button type="primary" @click="showProjectMemberModal" shape="circle">添加人员</Button>
                                 <Tag
                                    type="dot"
                                    v-for="item in editProjectForm.projectUserList"
                                    :key="item.id" :name="item.userId"
                                    closable
                                    @on-close="deletePersonConfirm"
                                >
                                    {{ item.realUserName }}
                                </Tag>
                            </FormItem>
                            <FormItem class="cl">
                                <Button class="fr" type="warning" @click="editProjectBtn('editProjectForm')" shape="circle">确认提交</Button>
                            </FormItem>
                        </Form>
                </TabPane>
                <TabPane label="日志" name="name2">
                    <ul class="log-list">
                        <li v-for="item in projectLogList" :key="item.id" class="cl">
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
                    <Form ref="stageForm" :model="stageForm" :rules="stageValidate" label-position="top">
                        <FormItem prop="name">
                            <Input v-model="stageForm.name" placeholder="阶段名称"></Input>
                        </FormItem>
                        <FormItem>
                            <div>项目负责人：{{stageForm.creator}}</div>
                        </FormItem>
                        <FormItem prop="budget">
                            <Input type="text" v-model="stageForm.budget" placeholder="阶段预算" number></Input>
                        </FormItem>
                        <FormItem label="阶段实施周期">
                            <Row>
                                <Col span="11">
                                    <FormItem prop="startDate">
                                        <DatePicker type="date" placeholder="开始时间" v-model="stageForm.startDate"></DatePicker>
                                    </FormItem>
                                </Col>
                                <Col span="2" style="height: 40px;"></Col>
                                <Col span="11">
                                    <FormItem prop="endDate">
                                        <DatePicker type="date" placeholder="结束时间" v-model="stageForm.endDate"></DatePicker>
                                    </FormItem>
                                </Col>
                            </Row>
                        </FormItem>
                        <FormItem label="阶段描述" prop="brief">
                            <Input v-model="stageForm.brief" type="textarea" :autosize="{minRows: 4,maxRows: 5}"></Input>
                        </FormItem>
                        <FormItem class="cl">
                            <Button class="fr" type="warning" @click="editStageBtn('stageForm')" shape="circle">确认提交</Button>
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
         <!-- 添加项目核心人员弹出层 -->
        <Modal
            v-model="projectMemModal"
            title="选择成员"
            class-name="vertical-center-modal"
            :closable="isShowClose"
            width="700"
        >
            <div class="formCheckbox">
                <Row>
                    <Col span="8">
                        <Tree :data="treeSouce" @on-select-change="selectedTree" children-key="sysGroupDTO"></Tree>
                    </Col>
                    <Col span="16">
                        <p class="member-tit">选择成员</p>
                        <Form :model="formMemberItem" inline>
                            <FormItem>
                                <Input v-model="formMemberItem.realName" placeholder="员工姓名"></Input>
                            </FormItem>
                            <FormItem>
                                <Button type="primary" @click="searchMemberList">查询</Button>
                            </FormItem>
                        </Form>
                        <CheckboxGroup v-model="checkAllGroup"> 
                            <Row v-for="member in members" :key="member.id" class="mb-10">
                                <Col span="9">
                                    <Checkbox class="mt-5" :label="member.id">
                                        <span>{{member.realName}}</span>
                                    </Checkbox>
                                </Col>
                                <Col span="15">
                                    <DatePicker
                                        type="daterange"
                                        placement="bottom-end"
                                        style="width: 100%;"
                                        placeholder="维护占用日期段"
                                        v-model="member.memberDate"
                                    >
                                    </DatePicker>
                                </Col>
                            </Row>
                        </CheckboxGroup>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="warning" @click="addProjectMember" shape="circle">确认</Button>
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
            // 拆分阶段校验预算
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
            // 编辑项目校验预算
            const validateProjectBudget = (rule, value, callback) => {
                let minVal =  this.batesUser;
                if(!value){
                    callback(new Error('预算不能为空'));
                }else if(!Number.isInteger(value)){
                    callback(new Error('请输入数字'));
                }else{
                    if (value < minVal) {
                        callback(new Error('项目预算不能小于已拆分的阶段预算总和'));
                    } else {
                        callback();
                    }
                }
            };
            return {
                index:1,
                rows:5,
                size:'',
                editProjectModel:false,
                isShowClose: false,
                stageModel:false,
                splitModel:false,
                divideModel:false,
                transitionNames:['move-right','fade'],
                projectDetail:{},//项目详情
                projectPrincipal: '',// 项目负责人
                formValidate: {
                    budget:'',
                    startDate: '',
                    endDate: '',
                    name: '',
                    brief:'',
                },
                ruleValidate: {
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
                // splitform:{
                //     name:'',
                //     budget:'',
                //     cost:'',
                //     startDate: '',
                //     endDate: '',
                // },
                // splitRuleValidate:{
                //     name: [
                //         { required: true, message: '项目名称不能为空', trigger: 'blur' },
                //         { type: 'string', max: 50, message: '最多输入50个字符', trigger: 'blur' }
                //     ],
                //     budget:[
                //         { required: true, message: '项目名称不能为空', trigger: 'blur' },
                //         { type: 'string', max: 50, message: '最多输入50个字符', trigger: 'blur' }
                //     ],
                //     cost: [
                //         { required: true, message: '项目名称不能为空', trigger: 'blur' },
                //         { type: 'string', max: 50, message: '最多输入50个字符', trigger: 'blur' }
                //     ],
                //     startDate: [
                //         { required: true, type: 'date', message: '请选择开始日期', trigger: 'change' }
                //     ],
                //     endDate: [
                //         { required: true, type: 'date', message: '请选择结束日期', trigger: 'change' }
                //     ],
                //     brief:[]
                // },
                stageForm:{
                    startDate: '',
                    endDate: '',
                    name: '',
                    budget:'',
                },
                stageValidate: {
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
                editProjectForm:{
                    startDate: '',
                    endDate: '',
                    name: '',
                    budget:'',
                    creator:'',
                    projectUserList: [],
                },
                editProjectValidate: {
                    name: [
                        { required: true, message: '项目名称不能为空', trigger: 'blur' },
                        { type: 'string', max: 50, message: '最多输入50个字符', trigger: 'blur' }
                    ],
                    budget:[
                        { validator: validateProjectBudget, trigger: 'blur' },
                    ],
                    startDate: [
                        { required: true, type: 'date', message: '请选择开始日期', trigger: 'change' }
                    ],
                    endDate: [
                        { required: true, type: 'date', message: '请选择结束日期', trigger: 'change' }
                    ],
                    creator:[{ type: 'string', max: 300, message: '最多输入300个字符', trigger: 'blur' }]
                },
                stageTable: [
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
                                    },params.row.stageName),
                                    h('div',{
                                        style: {
                                            height:'40px',
                                            textAlign:'center',
                                            lineHeight:'40px',
                                            cursor:'pointer',
                                        },
                                    },[
                                        h('a',{
                                            style:{
                                                display:'inline-block',
                                                color:'#aaaeb3',
                                            },
                                            on: {
                                                click: () => {
                                                    this.$router.push({path:'/WorkBench/PackageBag',query:{ projectId: this.$route.query.id,stageId: params.row.id,budget:params.row.stageBudget }})
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
                                                click:() =>{
                                                    this.stage.id = params.row.id;
                                                    this.delectStage();
                                                }
                                            }
                                        },'关闭'),
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
                                        cursor:'pointer',
                                    },
                                },[
                                    h('p',{
                                        style: {
                                            color:'#393C40',
                                            width:'100%',
                                            overflow:'hidden',
                                            textOverflow:'ellipsis',
                                            whiteSpace:'nowrap',
                                        },
                                        on: {
                                            click: () => {
                                                this.stageModel = true;
                                                this.getStageList();
                                                this.stageForm.id = params.row.id;
                                                this.stageForm.name = params.row.stageName;
                                                this.stageForm.budget = params.row.stageBudget;
                                                this.editStageBudget = params.row.stageBudget;
                                                this.stageForm.startDate = this.$dateFormat(params.row.startTime);
                                                this.stageForm.endDate = this.$dateFormat(params.row.endTime);
                                                this.stageForm.brief = params.row.stageDesc;
                                            }
                                        }
                                    },params.row.stageName),
                                    h('p',{
                                        style: {
                                            height:'40px',
                                            fontSize:'12px',
                                            display: '-webkit-box',
                                            webkitBoxOrient: 'vertical',
                                            webkitLineClamp: '2',
                                            overflow: 'hidden',
                                        },
                                    },params.row.stageDesc),
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
                                },params.row.stageBudget +'RMB'),
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
                                    },[
                                        h('Circle',{
                                        })
                                    ]), 
                                    h('span', {
                                        style: {

                                        },
                                    },Math.round((params.row.stageBudget)/(this.projectDetail.projectBudget)*100)+'%'),
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
                    }
                ],
                stage: [],
                projectMemModal: false, // 添加项目人员弹出层
                treeSouce: [], // 项目人员弹出层里的部门树形结构
                formMemberItem: {
                    realName: '',
                },
                members: [], // 添加核心成员弹出层里的人员列表
                checkAllGroup: [], //复选框里checkgroup的值
                localMembers: [], // 本地存储成员列表，以用于前端查询
                projectLogList: [], // 项目日志列表
                stageLogList: [], // 项目日志列表
                detailTab: 'name1',
                batesUser: 0, // 已使用预算
                editStageBudget: '', // 编辑阶段的预算
            }
        },
        components: {
            Breadcrumb,
            UserInfor
        },
        mounted() {
            // console.log(this.$route.query.budget)
            this.getStageList();
            this.projectDetial();
            // this.statFun();
        },
        created(){
    
        },
        methods: {
            // 获取选中的项目
            getSelectedProject(val) {
                this.projectId = parseInt(localStorage.getItem('projectId')) || parseInt(val);
                // this.getCheckTaskList(3);
                this.getStageList();
                // console.log(this.projectId)
            },
            // 项目概览
            projectOverview(){
                this.$router.push({path:'/WorkBench/totalProgress',query:{ id: this.$route.query.id }})
            },
            // 阶段预算统计
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
                                },
                                formatter:{
                                    // percent:'number'
                                },
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data:[
                                {value:batesUser, name:'已使用预算'},
                                {value:this.projectDetail.projectBudget - batesUser, name:'剩余预算'},
                            ]
                        }
                    ]
                });
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
                        this.drawLine(data);
                        // this.getStageList();
                    }else { // 返回是空，就默认为0
                        this.drawLine(0);
                    }
                })
                .catch(res => {
                    this.$Message.error('获取阶段统计失败')
                })
            },
            //划分阶段
            showDivideModel(name){
               this.divideModel = true; 
               this.$refs[name].resetFields();
            },
            //阶段列表
            getStageList(){
                let data = new FormData();
                data.append('projectId',this.$route.query.id);
                data.append('pageSize',this.rows);
                data.append('pageNum',this.index);
                // data.append('stageBudget',this.$route.query.budget);
                // console.log(stageBudget)
                this.$http.post('/state/list-page', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.size = data.total;
                        this.stage = data.list; 
                        this.statFun();
                        // console.log(data)
                    }
                })
                .catch(res => {
                    this.$Message.error('获取阶段列表失败')
                })
            },
            change(params){
                this.index = params;
                this.getStageList()
            },
            // 删除阶段
            delectStage(){
                let data = new FormData();
                data.append('id',this.stage.id); 
                data.append('projectId',this.$route.query.id);
                this.$http.post('/state/delete', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.getStageList();

                    }
                })
                .catch(res => {
                    this.$Message.error('删除阶段失败！')
                })
            },
            // 划分保存
            divideSaveBtn(type){
                let data = new FormData();
                // console.log(this.$route)
                data.append('projectId',this.$route.query.id);
                data.append('stageName',this.formValidate.name);
                data.append('stageBudget',this.formValidate.budget);
                data.append('startTime',this.$dateFormat(this.formValidate.startDate));
                data.append('endTime',this.$dateFormat(this.formValidate.endDate));
                data.append('stageDesc',this.formValidate.brief);
                // console.log(data)
                this.$refs[type].validate((valid) => {
                    if (valid) {
                        this.$http.post('/state', data)
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                this.$Message.success('划分阶段成功!');
                                this.divideModel = false;
                                this.getStageList();
                                this.projectDetial();
                                this.closeModal(type);
                            }
                        })
                        .catch(res => {
                            this.$Message.error('划分阶段失败！')
                        })
                    }
                }) 
            },
            //编辑按钮
            showEditProjectModel(name){
                this.$refs[name].resetFields();
                this.editProjectModel = true;
                this.projectDetial();
            },
            // 开始项目
            startProject(){
                var data = new FormData();
                data.append('id',this.$route.query.id);
                // console.log(this.$route.query.id)
                this.$http.post('/project/start-project', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        // this.btnBlock = false;
                        this.$Message.success('项目开始成功！');
                        this.projectDetial();

                    }
                })
                .catch(res => {
                    this.$Message.error('项目开始失败！')
                })
            },
            // 项目详情
            projectDetial(){
                var data= {
                }
                this.$http.get('/project/'+this.$route.query.id )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.projectDetail = data;
                        this.getUserList(data.createBy);
                        this.editProjectForm.name = data.projectName;
                        this.editProjectForm.budget = data.projectBudget;
                        this.editProjectForm.startDate =  this.$dateFormat(data.startTime);
                        this.editProjectForm.endDate =this.$dateFormat(data.endTime);
                        this.editProjectForm.brief = data.projectDesc;
                        this.editProjectForm.creator = data.realName;
                        this.editProjectForm.projectUserList = data.projectUserList;
                        
                        // console.log(data)
                    }
                })
                .catch(res => {
                    this.$Message.error('获取项目详情失败')
                })
            },
            // id查询用户
            getUserList(name){
                var data= {
                    id:name,
                }
                // console.log(data)
                this.$http.get('/sys-user/by-id', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.projectPrincipal = data.realName;
                        this.editProjectForm.creator = data.realName;
                        this.stageForm.creator = data.realName;
                    }
                })
                .catch(res => {
                    this.$Message.error('查询用户失败！')
                }) 
            },
            // 编辑项目
            editProjectList(name){
                let data = new FormData();
                data.append('id',this.$route.query.id);
                data.append('projectName',this.editProjectForm.name);
                data.append('projectBudget',this.editProjectForm.budget);
                data.append('startTime',this.$dateFormat(this.editProjectForm.startDate));
                data.append('endTime',this.$dateFormat(this.editProjectForm.endDate));
                data.append('projectDesc',this.editProjectForm.brief);
                this.$http.post('/project/update', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('编辑项目成功！');
                        this.projectDetial();

                    }
                })
                .catch(res => {
                    this.$Message.error('编辑项目失败！')
                })
                
            },
            // 编辑项目提交
            editProjectBtn(name){
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.editProjectList();
                        this.editProjectModel = false;
                        this.getStageList();
                    } else {
                        
                    }
                }) 
            },
            // 编辑阶段
            editStageList(){
                let data = new FormData();
                data.append('id',this.stageForm.id);
                data.append('projectId',this.$route.query.id);
                data.append('stageName',this.stageForm.name);
                data.append('stageBudget',this.stageForm.budget);
                data.append('startTime',this.$dateFormat(this.stageForm.startDate));
                data.append('endTime',this.$dateFormat(this.stageForm.endDate));
                data.append('stageDesc',this.stageForm.brief);
                // console.log(data)
                this.$http.post('/state/put', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.getStageList();
                        this.stageModel = false;
                        this.$Message.success('编辑阶段成功！');
                    }
                })
                .catch(res => {
                    // console.log(res)
                    this.$Message.error('编辑阶段失败！')
                })

            },
            // 编辑阶段提交按钮
            editStageBtn(name){
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.editStageList(); 
                        this.stageModel=false;  
                    } else {
                    }
                }) 
            },
            // 项目关闭
            endProjectList(){
                var data = new FormData();
                data.append('id',this.$route.query.id);
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
            // 关闭项目按钮
            closeProjectBtn(){
                this.endProjectList();
            },
            // 添加核心人员弹出层
            showProjectMemberModal() {
                this.projectMemModal = true;
                this.getDepartmentGroup();
                this.getGroupMemberList();
            },
             // 获取部门组列表
            getDepartmentGroup(){
                let data = {}
                this.$http.get('/sys-group/tree-list', {params: data})
                .then(res => {
                    let data = response.data(res, this);
                    if(data){
                        for(var i=0;i<data.length;i++) {
                            data[i].title = data[i].groupName;
                            data[i].expand = true;
                            if(data[i].sysGroupDTO){
                                for(var j=0;j<data[i].sysGroupDTO.length;j++) {
                                    data[i].sysGroupDTO[j].title = data[i].sysGroupDTO[j].groupName;
                                    data[i].sysGroupDTO[j].expand = true;
                                    if(data[i].sysGroupDTO[j].sysGroupDTO){
                                        for(var g=0;g<data[i].sysGroupDTO[j].sysGroupDTO.length;g++){
                                            data[i].sysGroupDTO[j].sysGroupDTO[g].title = data[i].sysGroupDTO[j].sysGroupDTO[g].groupName
                                        }
                                    }
                                }
                            }
                        }
                        this.treeSouce = data;
                    }
                })
                .catch(res => {
                    // console.log(res);
                    this.$Message.error('获取部门组列表失败！')
                })
            },
            // 点击树触发的事件
            selectedTree(data) {
                // console.log(data[0].id);
                this.getGroupMemberList(data[0].id);
            },
            // 添加项目成员提交
            addProjectMember() {
                // console.log(this.checkAllGroup);
                let data = [];
                for(var i=0;i<this.checkAllGroup.length;i++){
                    for(var j=0;j<this.members.length;j++){
                        if(this.members[j].id == this.checkAllGroup[i]){
                            if(!(this.members[j].memberDate[0]) || !(this.members[j].memberDate[1])){
                                this.$Message.info('请选择占用日期段！');
                                return ;
                            }
                            data.push({
                                projectId: parseInt(this.$route.query.id),
                                userId: this.members[j].id,
                                inTime: this.$dateFormat(this.members[j].memberDate[0]),
                                outTime: this.$dateFormat(this.members[j].memberDate[1]),
                            })
                        }
                    }
                }
                
                // console.log(data)
                this.$http.post('/project/add-core-user', data)
                .then(res => {
                    let data = response.data(res, this);
                    if(data){
                        this.$Message.success('添加成员成功！');
                        this.projectDetial();
                        this.projectMemModal = false;
                    }
                })
                .catch(res => {
                    this.$Message.error('添加成员失败！')
                })
                
            },
            // 根据用户姓名查询成员(前端查询)
            searchMemberList() {
                if(this.formMemberItem.realName == ''){
                    this.members = this.localMembers;
                }else{
                    this.members = [];
                    for(var i=0;i<this.localMembers.length;i++){
                        if(this.localMembers[i].realName.indexOf(this.formMemberItem.realName) != -1){
                            this.members.push(this.localMembers[i])
                        }
                    }
                }
            },
            // 查询部门组下的所有成员
            getGroupMemberList(id){
                let data = {
                    groupId: id,
                }
                this.$http.get('/sys-group/group-user-list-no-project', {params: data})
                .then(res => {
                    let data = response.data(res, this);
                    if(data){
                        for(var i=0;i<data.length;i++){ // 为每条数据增加一个日期字段,方便datepicker循环遍历
                            data[i].memberDate = [];
                        }
                        this.members = data;
                        this.localMembers = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取部门组成员失败！')
                })
            },
            // 删除核心人员确认提示
            deletePersonConfirm(event, name) {
                this.$Modal.confirm({
                    title: '删除核心人员',
                    content: '核心人员一旦删除，将无法查看，确认删除吗？',
                    onOk: () => {
                        this.deletePerson(name);
                    },
                    onCancel: () => {
                        
                    }
                });
                
            },
            // 确认删除核心人员
            deletePerson(id){
                let dataJson = {
                    userId: id,
                    projectId: this.$route.query.id
                }
                this.$http.delete('/project/core-user', {params: dataJson})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('删除核心人员成功！');
                        this.projectDetial();
                    }
                })
                .catch(res => {
                    this.$Message.error('删除核心人员失败！')
                })
            },
            // 项目弹出层里的tab切换
            modalTabClick(name) {
                this.detailTab = name;
                if(name == 'name2'){
                    this.getProjectLogList();
                }else {
                    this.projectDetial();
                }
            },
            // 获取项目操作日志
            getProjectLogList() {
                var data= {
                    projectId: this.$route.query.id,
                }
                this.$http.get('/project/log/query', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.projectLogList = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('查询项目日志失败！')
                }) 
            },
            // 关闭添加弹出层
            closeModal(type) {
                this.$refs[type].resetFields();
                this.detailTab = 'name1';
            },
            // 关闭编辑弹出层
            closeModal2() {
                this.detailTab = 'name1';
            },
            // 阶段弹出层里的tab切换
            modalTabClick2(name) {
                this.detailTab = name;
                if(name == 'name2'){
                    this.getStageLogList();
                }
            },
            // 获取阶段操作日志
            getStageLogList() {
                var data= {
                    stageId: this.stageForm.id,
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
    .detail-project .fl-no{ width:95%;}
    .detail-project .max-li{ 
        max-height:60px;
        overflow:hidden;
        margin-bottom:30px;
    }
    .detail-project .edit-btn{
        position:absolute;
        right:5%;
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
    .formCheckbox{
        min-height: 350px;
    }
    .member-tit{
        font-size: 14px;
        margin-bottom: 15px;
    }
</style>