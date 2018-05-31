<template>
    <div>
        <div class="cl">
            <Breadcrumb title="验收管理" type="select" @sendSelected="getSelectedProject" />
            <UserInfor />
        </div>
        <div class="cont-bg mlr-20">
            <div class="project-list-table po_re">
                <Tabs value="name1" @on-click="tabClick" :animated="false">
                    <TabPane label="按阶段验收" name="name1">
                        <div class="operation-box">
                           <Checkbox class="mr-40" @on-change="selectAllCol">全选／返选</Checkbox> 
                           <span class="main-color cur-p" @click="checkList(3)">批量验收通过</span>
                        </div>
                        <Table :columns="columns1" :data="data1" ref="selection" @on-selection-change="stageSel"></Table>
                        <div class="page-box">
                            <Page 
                                :total="Math.floor(size1)" 
                                :current="Math.floor(index1)" 
                                :page-size="Math.floor(rows)"
                                @on-change="stageChange" 
                                simple>
                            </Page>
                        </div>
                    </TabPane>
                    <TabPane label="按工程包验收" name="name2" class="po_re">
                        <Form :model="FormItem2" inline class="pos-form pos-form2 table-top-form">
                            <FormItem>
                                <Select v-model="FormItem2.stageId" placeholder="项目阶段">
                                    <Option v-for="item in stageList" :value="item.id" :key="item.id">{{ item.stageName }}</Option>
                                </Select>
                            </FormItem>
                            <FormItem>
                                <Button type="primary" @click="searchTaskList(2)">查询</Button>
                            </FormItem>
                        </Form>
                        <div class="operation-box">
                           <Checkbox class="mr-40" @on-change="selectAllCol2">全选／返选</Checkbox> 
                           <span class="main-color cur-p" @click="checkList(2)">批量验收通过</span>
                        </div>
                        <Table :columns="columns2" :data="data2" ref="selection2" @on-selection-change="packageSel"></Table>
                        <div class="page-box">
                            <Page
                                :total="Math.floor(size2)" 
                                :current="Math.floor(index2)" 
                                :page-size="Math.floor(rows)"
                                @on-change="packageChange"
                                simple>
                            </Page>
                        </div>
                    </TabPane>
                    <TabPane label="按任务验收" name="name3" class="po_re">
                        <Form :model="FormItem3" inline class="pos-form pos-form3 table-top-form">
                            <FormItem>
                                <Select v-model="FormItem3.stageId" placeholder="项目阶段" @on-change="chagePackageList">
                                    <Option v-for="item in stageList" :value="item.id" :key="item.id">{{ item.stageName }}</Option>
                                </Select>
                            </FormItem>
                            <FormItem>
                                <Select v-model="FormItem3.workId" placeholder="工程包">
                                    <Option v-for="item in packageList" :value="item.id" :key="item.id">{{ item.workName }}</Option>
                                </Select>
                            </FormItem>
                            <FormItem>
                                <Button type="primary" @click="searchTaskList(1)">查询</Button>
                            </FormItem>
                        </Form>
                        <div class="operation-box">
                           <Checkbox class="mr-40" @on-change="selectAllCol3">全选／返选</Checkbox> 
                           <span class="main-color cur-p" @click="checkList(1)">批量验收通过</span>
                        </div>
                        <Table :columns="columns3" :data="data3" ref="selection3" @on-selection-change="taskSel"></Table>
                        <div class="page-box">
                            <Page
                                :total="Math.floor(size3)" 
                                :current="Math.floor(index3)" 
                                :page-size="Math.floor(rows)"
                                @on-change="taskChange"
                                simple>
                            </Page>
                        </div>
                    </TabPane>
                </Tabs>
            </div>
        </div>
    </div>
</template>
<script>
    import * as response from '../../../util/response.js'
    import Breadcrumb from '@/components/common/header/Breadcrumb'
    import UserInfor from '@/components/common/header/UserInfor'
    export default {
        data() {
            return {
                projectId: '',
                FormItem2:{
                    stageId:'',
                },
                FormItem3:{
                    stageId:'',
                    workId: ''
                },
                columns1: [
                    {
                        type: 'selection',
                        width: 1,
                        align: 'center'
                    },
                    {
                        title: '阶段名称',
                        key: 'stageName',
                        width: 200,
                        className: 'special-col',
                        render: (h, params) => {
                           return h('div', {
                                style: {
                                    marginLeft: '60px'
                                },
                            }, params.row.stageName)
                        }
                    },
                    {
                        title: '状态',
                        key: ' stageState',
                        render: (h, params) => {
                            const row = params.row;
                            const text = row.stageState == 2 ? '待验收' : '已完成';
                            return h('span', {
                            }, text);  
                        }
                    },
                    {
                        title: '提交时间',
                        key: 'endSubmitTime',
                        render:(h,params) =>{
                            return h('span',{
                            },this.$dateFormat2(params.row.endSubmitTime) )  
                        }
                    },
                    {
                        title: '验收人',
                        key: 'cherealname',
                    },
                    {
                        title: '验收时间',
                        key: 'updateTime',
                        render:(h,params) =>{
                            return h('span',{
                            },this.$dateFormat2(params.row.updateTime) )  
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
                                        disabled: params.row.stageState == 2 ? false : true
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            if(params.row.stageState == 2){
                                                this.confirmCheck(3,params.row.id);
                                            }
                                        }
                                    }
                                }, '确认验收'),
                            ]);
                        }
                    }
                ],
                columns2: [
                    {
                        type: 'selection',
                        width: 1,
                        align: 'center'
                    },
                    {
                        title: '工程包名称',
                        key: 'workName',
                        width: 200,
                        className: 'special-col',
                        render: (h, params) => {
                           return h('div', {
                                style: {
                                    marginLeft: '60px'
                                },
                            }, params.row.workName)
                        }
                    },
                    {
                        title: '状态',
                        key: 'workState',
                        render: (h, params) => {
                           const row = params.row;
                            const text = row.workState == 2 ? '待验收' : '已完成';
                            return h('span', {
                            }, text);  
                        }
                    },
                    {
                        title: '提交时间',
                        key: 'endSubmitTime',
                        render:(h,params) =>{
                            return h('span',{
                            },this.$dateFormat2(params.row.endSubmitTime) )  
                        }
                    },
                    {
                        title: '验收人',
                        key: 'cherealname',
                    },
                    {
                        title: '验收时间',
                        key: 'updateTime',
                        render:(h,params) =>{
                            return h('span',{
                            },this.$dateFormat2(params.row.updateTime) )  
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
                                        disabled: params.row.workState == 2 ? false : true
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            if(params.row.workState == 2){
                                                this.confirmCheck(2,params.row.id);
                                            }
                                        }
                                    }
                                }, '确认验收'),
                            ]);
                        }
                    }
                ],
                columns3: [
                    {
                        type: 'selection',
                        width: 1,
                        align: 'center'
                    },
                    {
                        title: '任务名称',
                        key: 'task_name',
                        width: 200,
                        className: 'special-col',
                        render: (h, params) => {
                           return h('div', {
                                style: {
                                    marginLeft: '60px'
                                },
                            }, params.row.task_name)
                        }
                    },
                    {
                        title: '状态',
                        key: ' task_state',
                        render: (h, params) => {
                            const row = params.row;
                            const text = row.task_state == 2 ? '待验收' : '已完成';
                            return h('span', {
                            }, text);    
                        }
                    },
                    {
                        title: '提交时间',
                        key: 'rel_end_time',
                        render:(h,params) =>{
                            return h('span',{
                            },this.$dateFormat2(params.row.rel_end_time) )  
                        }
                    },
                    {
                        title: '提交人',
                        key: 'comrealname',
                    },
                    {
                        title: '验收人',
                        key: 'cherealname',
                    },
                    {
                        title: '验收时间',
                        key: 'update_time',
                        render:(h,params) =>{
                            return h('span',{
                            },this.$dateFormat2(params.row.update_time) )  
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
                                        disabled: params.row.task_state == 2 ? false : true
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            if(params.row.task_state == 2){
                                                this.confirmCheck(1,params.row.id);
                                            }
                                        }
                                    }
                                }, '确认验收'),
                            ]);
                        }
                    }
                ],
                data1: [],
                data2: [],
                data3: [],
                index1:1,
                rows:5,
                size1:'',
                index2:1,
                size2:'',
                index3:1,
                size3:'',
                ids1: '',
                ids2: '',
                ids3: '',
                stageList: [],
                packageList: [],
            }
        },
        components: {
            Breadcrumb,
            UserInfor,
        },
        mounted() {
            
        },
        created(){
    
        },
        methods: {
            // 获取选中的项目
            getSelectedProject(val) {
                this.projectId = parseInt(localStorage.getItem('projectId')) || parseInt(val);
                this.getCheckTaskList(3);
                this.getStageList();
            },
            // 点击tab切换
            tabClick(name) {
                if(name == 'name2') {
                    this.getCheckTaskList(2)
                }else if(name == 'name3') {
                    this.getCheckTaskList(1)
                }else {
                    this.getCheckTaskList(3)
                }
            },
            // 获取阶段列表
            getStageList() {
                let data ={
                    projectId: this.projectId
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
            // 获取工程包列表
            getPackageList(stageId) {
                let data ={
                    projectId: this.projectId,
                    stageId: stageId  // this.FormItem3.stageId
                }
                this.$http.get('/Work/work-list', {params: data})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.packageList = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取工程包列表失败')
                })
            },
            // 查询验收里的列表
            searchTaskList(status) { // status 1-任务 2-工程包 3-阶段
                if(status == 1){
                    this.index3 = 1;
                }else if(status == 2){
                    this.index2 = 1;
                }else if(status == 3){
                    this.index1 = 1;
                }
                this.getCheckTaskList(status);
            },
            // 获取验收的任务列表
            getCheckTaskList(status) { // status 1-任务 2-工程包 3-阶段
                let dataJson = {
                    status: status,
                    pageSize: this.rows,
                    projectId: this.projectId
                };
                if(status == 1){
                    dataJson.pageNum = this.index3;
                    dataJson.stageId = this.FormItem3.stageId;
                    dataJson.workId = this.FormItem3.workId;
                }else if(status == 2){
                    dataJson.pageNum = this.index2;
                    dataJson.stageId = this.FormItem2.stageId;
                }else if(status == 3){
                    dataJson.pageNum = this.index1;
                }
                this.$http.post('/check-manager/list', dataJson)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        if(status == 1){
                            this.data3 = data.list;
                            this.size3 = data.total;
                        }else if(status == 2){
                            this.data2 = data.list;
                            this.size2 = data.total;
                        }else if(status == 3){
                            this.data1 = data.list;
                            this.size1 = data.total;
                        }
                        console.log(data)
                    }
                })
                .catch(res => {
                    this.$Message.error('获取任务列表失败！')
                })
            },
            // 阶段
            selectAllCol(status){
                this.$refs.selection.selectAll(status);
            },
            // 工程包
             selectAllCol2(status){
                this.$refs.selection2.selectAll(status);
            },
            // 任务
             selectAllCol3(status){
                this.$refs.selection3.selectAll(status);
            },

            // 阶段的分页
            stageChange(params) {
                this.index1 = params;
                this.getCheckTaskList(3);
            },
            // 工程包的分页
            packageChange(params) {
                this.index2 = params;
                this.getCheckTaskList(2);

            },
            // 任务的分页
            taskChange(params) {
                this.index3 = params;
                this.getCheckTaskList(1);
            },
            // 阶段选中的ids
            stageSel(selection) {
                // console.log(selection);
                let idArr = [];
                if(selection.length){
                    for(var i=0; i<selection.length; i++){
                        idArr.push(selection[i].id);
                    }
                    this.ids1 = idArr.join(',');
                }
            },
            // 工程包选中的ids
            packageSel(selection) {
                let idArr = [];
                if(selection.length){
                    for(var i=0; i<selection.length; i++){
                        idArr.push(selection[i].id);
                    }
                    this.ids2 = idArr.join(',');
                }
            },
            // 任务选中的ids
            taskSel(selection) {
                let idArr = [];
                if(selection.length){
                    for(var i=0; i<selection.length; i++){
                        idArr.push(selection[i].id);
                    }
                    this.ids3 = idArr.join(',');
                }
            },
            // 确认验收
            confirmCheck(status,id) {
                let dataJson = {
                    status: status,
                    projectId: this.projectId,
                    trueId: id
                };
                this.$http.put('/check-manager/check-true', dataJson)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('验收成功！');
                        this.getCheckTaskList(status);
                    }
                })
                .catch(res => {
                    this.$Message.error('验收失败！')
                })
            },
            // 批量验收
            checkList(status) {
                let ids;
                if(status == 1){
                    ids = this.ids3;
                }else if(status == 2){
                    ids = this.ids2;
                }else if(status == 3){
                    ids = this.ids1;
                }
                if(ids == ''){
                    this.$Message.error('请至少选择一项验收！');
                    return ;
                }
                let dataJson = {
                    status: status,
                    trueIdStr: ids,
                    projectId: this.projectId
                };
                this.$http.put('/check-manager/check-true-list', dataJson)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        if(status == 1){
                            this.$Message.success('验收任务成功！');
                        }else if(status == 2){
                            this.$Message.success('验收工程包成功！');

                        }else if(status == 3){
                            this.$Message.success('验收阶段成功！');
                            
                        }
                        this.getCheckTaskList(status);
                    }
                })
                .catch(res => {
                    this.$Message.error('验收失败！')
                })
            },
            // 级联选择工程包
            chagePackageList(val) {
                console.log(val);
                this.getPackageList(val);
            }
        } 
    }
</script>
<style lang="less" scoped>
    .operation-box{
        margin: 0 0 15px 59px;
    }
</style>