<template>
    <div>
        <div>
            <Form :model="editProjectForm" :label-width="80" inline class="mlr-20">
                <FormItem label="项目名称">
                    <Input v-model="editProjectForm.name" placeholder="请输入项目名称"></Input>
                </FormItem>
                <FormItem label="项目负责人">
                    <Input v-model="editProjectForm.people" disabled></Input>
                </FormItem>
                <FormItem label="项目预算">
                    <Input v-model="editProjectForm.budget"></Input>
                </FormItem>
                <FormItem label="起始日期">
                    <Row>
                        <Col span="11">
                            <FormItem prop="startDate">
                                <DatePicker type="date" placeholder="开始时间" v-model="editProjectForm.startDate"></DatePicker>
                            </FormItem>
                        </Col>
                        <!-- <Col span="1" style="height: 40px;"></Col> -->
                        <Col span="11">
                            <FormItem prop="endDate">
                                <DatePicker type="date" placeholder="结束时间" v-model="editProjectForm.endDate"></DatePicker>
                            </FormItem>
                        </Col>
                    </Row>
                </FormItem>
                <Row>
                    <Col span="24">
                        <FormItem label="项目描述">
                            <Input type="textarea" v-model="editProjectForm.desc" class="textarea-detail"></Input>     
                        </FormItem>
                    </Col>
                </Row>
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
            </Form>
                
        </div>
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
    import * as response from '../../../../util/response.js'
    export default {
        data() {
            return {
                transitionNames:['move-right','fade'],
                isShowClose: false,
                editProjectForm:{
                    name:'',
                    people:'',
                    startDate:'',
                    endDate:'',
                    budget:'',
                    desc:''
                },
                projectDetail:{},//项目详情
                projectPrincipal: '',// 项目负责人
                projectMemModal: false, // 添加项目人员弹出层
                treeSouce: [], // 项目人员弹出层里的部门树形结构
                formMemberItem: {
                    realName: '',
                },
                members: [], // 添加核心成员弹出层里的人员列表
                checkAllGroup: [], //复选框里checkgroup的值
                localMembers: [], // 本地存储成员列表，以用于前端查询
            }
        },
        mounted() {
            this.projectDetial();
        },
        created(){
    
        },
        methods: {
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
                        this.editProjectForm.desc = data.projectDesc;
                        this.editProjectForm.people = data.realName;
                        this.editProjectForm.projectUserList = data.projectUserList;
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
                this.$http.get('/sys-user/by-id', { params:data } )
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.projectPrincipal = data.realName;
                        this.editProjectForm.people = data.realName;
                    }
                })
                .catch(res => {
                    this.$Message.error('查询用户失败！')
                }) 
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
        } 
    }
</script>
<style scoped>
    .formCheckbox{
        min-height: 350px;
    }
    .member-tit{
        font-size: 14px;
        margin-bottom: 15px;
    }
</style>