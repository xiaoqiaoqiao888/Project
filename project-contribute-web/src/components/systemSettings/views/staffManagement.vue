<template>
    <div>
        <div class="cl">
            <Breadcrumb title="系统设置" content="成员管理" />
            <UserInfor />
        </div>
        <div class="cont-bg mlr-20">
            <div class="mt-20 mlr-20 table-top-form">
                <Form :model="formItem" inline>
                    <FormItem>
                        <Input v-model="formItem.userNo" placeholder="员工号"></Input>
                    </FormItem>
                    <FormItem>
                        <Input v-model="formItem.realName" placeholder="员工姓名"></Input>
                    </FormItem>
                    <FormItem>
                        <Select v-model="formItem.state" placeholder="员工状态">
                            <Option value="1">在职</Option>
                            <Option value="0">已离职</Option>
                        </Select>
                    </FormItem>
                    <FormItem>
                        <Button type="primary" @click="searchMemberList">查询</Button>
                        <Button type="primary" @click="" style="margin-left: 25px">批量导入</Button>
                        <Button type="primary" @click="showMemberModal" style="margin-left: 25px">添加成员</Button>
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
                @on-change="groupChange"
                simple></Page>
            </div>
        </div>
        <!-- 添加成员弹出层 -->
        <Modal
            v-model="addMemberModel"
            title="添加成员"
            class-name="vertical-center-modal"
            :closable="isShowClose"
            @on-cancel="closeModal('formAdd')"
        >
            <Form ref="formAdd" :model="formAdd" :rules="ruleValidate" label-position="top">
                <FormItem prop="userNo">
                    <Input v-model="formAdd.userNo" placeholder="员工号" number></Input>
                </FormItem>
                <FormItem prop="realName">
                    <Input v-model="formAdd.realName" placeholder="姓名" ></Input>
                </FormItem>
                <FormItem prop="email">
                    <Row>
                        <Col span="16">
                            <Input v-model="formAdd.email" placeholder="邮箱" ></Input>
                        </Col>
                        <Col span="2" style="text-align: right;font-size: 14px;">&nbsp;&nbsp;&nbsp;@camelotchina.com</Col>
                    </Row>
                </FormItem>
                <FormItem prop="tel">
                    <Input v-model="formAdd.tel" placeholder="手机号" number></Input>
                </FormItem>
                <FormItem prop="cost">
                    <Row>
                        <Col span="16">
                            <Input v-model="formAdd.cost" placeholder="人员成本" number></Input>
                        </Col>
                        <Col span="3" style="font-size: 14px;">&nbsp;&nbsp;&nbsp;元/月</Col>
                    </Row>
                </FormItem>
                <FormItem>
                    <Select v-model="formAdd.roleIds" multiple placeholder="选择角色">
                        <Option v-for="item in roleList" :value="item.id" :key="item.id">{{ item.roleName }}</Option>
                    </Select>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="warning" @click="affirmBtn('formAdd')" shape="circle">添加成员</Button>
            </div>
        </Modal>
        <!-- 角色授权弹出层 -->
        <Modal
            v-model="addRoleMember"
            title="角色授权"
            class-name="vertical-center-modal"
            :closable="isShowClose"
            @on-cancel="closeModal('roleAddForm')"
        >
            <Form ref="roleAddForm" :model="roleAddForm" label-position="top" style="height:150px;">
                <FormItem>
                    <Select v-model="roleAddForm.roleIds" multiple placeholder="选择角色">
                        <Option v-for="item in roleList" :value="item.id" :key="item.id">{{ item.roleName }}</Option>
                    </Select>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="warning" @click="confirmBtn" shape="circle">确认</Button>
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
            return {
                index:1,
                rows:5,
                size:'',
                isShowClose: false,
                addMemberModel: false,
                formItem:{
                    userNo:'',
                    realName:'',
                    state:'',
                },
                formAdd: {
                    userNo: '',
                    realName: '',
                    email: '',
                    tel: '',
                    cost: '',
                    roleIds: [],
                },
                roleAddForm: {
                    roleIds: []
                },
                ruleValidate: {
                    userNo: [
                        { required: true,type: 'number', min: 1, message: '员工号不能为空', trigger: 'blur' },
                        { type: 'number', max: 9999999999, message: '请输入10位以下的数字', trigger: 'blur' }
                    ],
                    realName: [
                        { required: true, message: '姓名不能为空', trigger: 'blur' },
                        { type: 'string', max: 10, message: '最多输入10个字符', trigger: 'blur' }
                    ],
                    email: [
                        { required: true, message: '邮箱不能为空', trigger: 'blur' },
                        { type: 'string', max: 20, message: '最多输入20个字符', trigger: 'blur' }
                    ],
                    tel: [
                        { required: true, type: 'number', min: 1, message: '手机号不能为空', trigger: 'blur' },
                        { type: 'number', max: 99999999999, message: '请输入正确的手机号', trigger: 'blur' }
                    ],
                    cost: [
                        { required: true, type: 'number', min: 1, message: '人员成本不能为空', trigger: 'blur' },
                        { type: 'number', max: 9999999999, message: '请输入10位以下的数字', trigger: 'blur' }
                    ]
                },
                columns1: [
                    {
                        title: '员工号',
                        key: 'user_no',
                        width:100,
                    },
                    {
                        title: '姓名',
                        key: 'real_name'
                    },
                    {
                        title: '成本',
                        key: 'cost'
                    },
                    {
                        title: '所属部门',
                        key: 'group_name'
                    },
                    {
                        title: '状态',
                        key: 'state',
                        render: (h, params) => {
                            const row = params.row;
                            const text = row.state == 1 ? '在职' : '已离职';
                            return h('span', {
                            }, text);  
                        }
                    },
                    {
                        title: '操作',
                        key: 'action',
                        render: (h, params) => {
                            return h('div', [
                                h('Button',{
                                    props: {
                                        type: 'primary',
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.openAddRoleModal(params.row.id);                                          
                                        }
                                    }
                                },'角色授权'),
                                params.row.state ==1 ? h('Button',{
                                    props: {
                                        type: 'primary',
                                    },
                                    style: {
                                        marginRight: '5px',
                                        marginTop: '10px'
                                    },
                                    on: {
                                        click: () => {
                                            this.fired(params.row.user_no);                                           
                                        }
                                    }
                                },'离职') :''
                            ])
                        }
                    }
                ],
                data1: [],
                roleList: [], // 角色列表
                addRoleMember: false,
                memberId: '', // 成员id
                groupId: '', // 部门id
            } 
        },
        components: {
            Breadcrumb,
            UserInfor,
        },
        mounted() {
            this.getMemberList();
            this.getRoleList();
        },
        created(){
    
        },
        methods: {
            // 获取角色成员列表
            getRoleList() {
                this.$http.get('/sys-role')
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.roleList = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取角色列表失败！')
                })
            },
            // 获取成员列表
            getMemberList(groupId) {
                if(groupId){
                    this.groupId = groupId;
                }
                let dataJson = {
                    pageNum: this.index,
                    pageSize: this.rows,
                    userNo: this.formItem.userNo,
                    realName: this.formItem.realName,
                    state: this.formItem.state,
                    groupId: this.groupId,
                };
                this.$http.post('/sys-group/list-group-user', dataJson)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.data1 = data.list;
                        this.size = data.total;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取成员列表失败！')
                })
            },
            // 添加成员弹出层
            showMemberModal() {
                // console.log(this.groupId)
                if(!this.groupId){
                    this.$Message.info('请选择一个部门！');
                    return ;
                }
                this.addMemberModel = true;
            },
            // 添加新成员提交
            affirmBtn(type){
                let data = new FormData();
                data.append('userNo', this.formAdd.userNo);
                data.append('realName', this.formAdd.realName);
                data.append('email', `${this.formAdd.email}@camelotchina.com`);
                data.append('tel', this.formAdd.tel);
                data.append('cost', this.formAdd.cost);
                data.append('groupId', this.groupId); // 需要传一个部门组id
                if(this.formAdd.roleIds.length){
                    data.append('roleId', this.formAdd.roleIds.join(','));
                }
                this.$refs[type].validate((valid) => {
                    if (valid) {
                        this.$http.post('/sys-user/add', data)
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                this.$Message.success('添加成员成功！');
                                this.getMemberList();
                                this.addMemberModel = false;
                                this.closeModal(type);
                            }
                        })
                        .catch(res => {
                            this.$Message.error('添加成员失败！')
                        })
                    }
                })
            },
            // 打开角色授权弹出层
            openAddRoleModal(id) {
                this.addRoleMember = true;
                this.memberId = id;
                this.roleAddForm.roleIds = []; // 清空数组
                let data = {
                    id: id
                }
                this.$http.get('/sys-role/by-user-id', {params: data})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        if(data.length){
                            for(var i=0;i<data.length;i++){
                                this.roleAddForm.roleIds.push(data[i].id)
                            }
                        }
                    }
                })
                .catch(res => {
                    this.$Message.error('获取成员拥有权限失败！')
                })
            },
            // 角色授权弹出层确认
            confirmBtn() {
                this.$http.put(`/sys-user-role?userId=${this.memberId}&roleIds=${this.roleAddForm.roleIds.join(',')}`)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('角色授权成功！')
                        this.getMemberList();
                        this.addRoleMember = false;
                    }
                })
                .catch(res => {
                    this.$Message.error('角色授权失败！')
                })
            },
            // 分页
            groupChange(params){
                this.index = params;
                this.getMemberList();
            },
            // 查询成员列表
            searchMemberList() {
                this.index = 1;
                this.getMemberList();
            },
            // 关闭弹出层
            closeModal(type) {
                this.$refs[type].resetFields();
            },
            // 离职
            fired(userNo) {
                this.$http.put(`/sys-user/by-id?userNo=${userNo}&state=0`)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('离职操作成功！')
                        this.getMemberList();
                    }
                })
                .catch(res => {
                    this.$Message.error('离职操作失败！')
                })
            },
        }
    }
</script>
<style scoped>
    
</style>