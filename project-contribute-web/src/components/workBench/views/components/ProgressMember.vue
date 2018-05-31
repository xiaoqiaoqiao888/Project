<template>
    <div>
        <div>
            <div>
                <!-- <p class="member-title">参与项目成员统计</p> -->
                <p class="member-count-box cl">
                    <span>超预算：{{memberCount.overtime}}小时</span>
                    <span>实际产生工时：{{memberCount.realTaskTimeNum}}小时</span>
                    <span>截止目前总参与成员：{{memberCount.userNum}}人</span>
                </p>
            </div>
            <Form :model="formMember" inline class="table-top-form mlr-20">
                <FormItem>
                    <Input v-model="formMember.realName" placeholder="姓名"></Input>
                </FormItem>
                <FormItem>
                    <Select v-model="formMember.groupId" placeholder="所属职能组" filterable>
                        <Option v-for="item in groupList" :value="item.id" :key="item.id">{{ item.groupName }}</Option>
                    </Select>
                </FormItem>
                <FormItem>
                    <Button type="primary" @click="secrchBtnMember">查询</Button>
                </FormItem>
            </Form>
            <Table :columns="memberTable" :data="memberList" @on-row-click="enterWorkBack" :row-class-name="rowClassName"></Table>
            <div class="page-box">
                <Page 
                :total="Math.floor(sizeMember)" 
                :current="Math.floor(indexMember)" 
                :page-size="Math.floor(rows)"
                @on-change="changeMember"
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
                    <Breadcrumb title="成员项目" content="工作轨迹追溯" />
                </div>
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
    </div>
</template>
<script>
    import * as response from '../../../../util/response.js'
    import Breadcrumb from '@/components/common/header/Breadcrumb'
    export default {
        data() {
            return {
                transitionNames:['move-right','fade'],
                rows:5,
                indexMember: 1,
                sizeMember: '',
                isShowClose: false,
                formMember: {
                    realName: '',
                    groupId: '',
                },
                memberTable: [
                    {
                        title: '序号',
                        // key: 'userId',
                        type: 'index',
                        width: 80
                    },
                    {
                        title: '员工号',
                        key: 'userNo',
                    },
                    {
                        title: '姓名',
                        key: 'realName',
                        render:(h,params) =>{
                            let text = params.row.realName;
                            let color = params.row.type == 0 ? 'red' : '';
                            return h('div',{
                                style: {
                                    color: color,
                                },
                            }, text)  
                        }
                    },
                    {
                        title: '该项目获得',
                        key: 'taskValueSum',
                    },
                    {
                        title: '预估工时（小时）',
                        key: 'expTaskTimeSum',
                    },
                    {
                        title: '实际耗时（小时）',
                        key: 'relTaskTimeSum',
                    },
                    {
                        title: '提交任务 / 完成任务',
                        key: 'date',
                        render:(h,params) =>{
                            let text = params.row.submitTaskSum+' / '+params.row.finishedTaskSum
                            return h('div',{
                            }, text)  
                        }
                    },
                    // {
                    //     title: '操作',
                    //     key: 'action',
                    //     width:130,
                    //     align: 'center',
                    //     render: (h, params) => {
                    //         return h('div', [
                    //             h('Button', {
                    //                 props: {
                    //                     type: 'primary',
                    //                 },
                    //                 style: {
                    //                     marginRight: '5px'
                    //                 },
                    //                 on: {
                    //                     click: () => {
                                            
                    //                     }
                    //                 }
                    //             }, '工作追溯'),
                    //         ]);
                    //     }
                    // }
                ],
                memberList: [],
                memberCount: {},
                groupList: [],
                taskListOne:[],
                WorkBackModel:false, // 工作追溯弹出层
                workBack: {},
            }
        },
        mounted() {
            this.getOwnedGroupList();
            this.getMemberList();
            this.getMemberCount();
        },
        created(){
    
        },
        components: {
            Breadcrumb,
        },
        methods: {
            // 获取项目成员列表
            getMemberList() {
                let data = new FormData();
                data.append('projectId',parseInt(this.$route.query.id));
                data.append('realName',this.formMember.realName);
                data.append('page',this.indexMember);
                data.append('size',this.rows);
                data.append('groupId',this.formMember.groupId);
                this.$http.post('/project/user/search', data )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.memberList = data.list;
                        this.sizeMember = data.total;
                    }
                })
                .catch(res => {
                    this.$Message.error('查询项目成员列表失败！')
                })
            },
            // 获取所有的职能组
            getOwnedGroupList() {
                let dataJson = {
                    groupLevel: 2
                };
                this.$http.post('/sys-group/list', dataJson)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.groupList = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取职能组列表失败！')
                })
            },
            // 查询项目成员列表
            secrchBtnMember() {
                this.indexMember == 1;
                this.getMemberList();
            },
            // 项目人员分页
            changeMember(params) {
                this.indexMember == params;
                this.getMemberList();
            },
            // 项目成员统计
            getMemberCount() {
                let dataJson ={
                    projectId: parseInt(this.$route.query.id)
                }
                this.$http.get('/project/user/by-project', {params: dataJson} )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.memberCount = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('查询项目成员列表失败！')
                })
            },
            // 工作追溯
            workBackFun(id){
                let data = new FormData();
                data.append('userId',id);
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
            // 为表格中的行加class
            rowClassName (row, index) {
                return 'cur-p';
            },
            // 查看工作追溯
            enterWorkBack(row, index) {
                this.WorkBackModel = true;
                this.workBackFun(row.id);
            },
        } 
    }
</script>
<style scoped>
    .member-title,.member-count-box{
        margin-bottom: 20px;
        margin-left: 20px;
    }
    .member-count-box{
        margin-right: 20px;
    }
    .member-count-box span{
        margin-left: 100px;
        float: right;
    }
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