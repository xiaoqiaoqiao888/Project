<template>
    <div>
        <div class="cl">
            <Breadcrumb title="系统管理" content="角色管理" />
            <UserInfor />
        </div>
        <div class="cont-bg mlr-20">
            <div class="project-list-table po_re">
                <Tabs :value="activeTab" :animated="false" @on-click="tabClick">
                    <TabPane label="角色成员" name="name1">
                        <div class="operation-box">
                        <Checkbox class="mr-40" @on-change="selectAllCol" v-model="checkAll">全选／返选</Checkbox> 
                        <Button type="primary" @click="deleteRoleMember" class="mr-20">批量移除</Button>
                        <Button type="primary" @click="addModelShow">添加成员</Button>
                        </div>
                        <Table :columns="roles" :data="rolesList" ref="selection" @on-selection-change="roleSel"></Table>
                        <div class="page-box">
                            <Page 
                                :total="Math.floor(size)"
                                :current="Math.floor(index)"
                                :page-size="Math.floor(rows)"
                                @on-change="roleChange"
                                simple>
                            </Page>
                        </div>
                    </TabPane>
                    <TabPane label="功能权限" name="name2" class="po_re">
                        <div class="operation-box">
                            <Form :model="formItem1">
                                <FormItem>
                                    <Tree
                                        :data="resourceList"
                                        @on-check-change="checkedTree"
                                        show-checkbox
                                        multiple
                                        children-key="childList"
                                    >
                                    </Tree>
                                </FormItem>
                                <FormItem class="font-cen"> 
                                    <Button type="warning" @click="updateRoleResource" shape="circle">保存</Button>
                                </FormItem>
                            </Form>
                        </div>
                    </TabPane>
                </Tabs>
            </div>
        </div>
        <!-- 添加成员 -->
        <Modal
            v-model="addModel"
            title="添加成员"
            class-name="vertical-center-modal"
            :closable="isShowClose"
            :width="600"
            @on-cancel = "closeModal"
        >
            <div class="formCheckbox">
                <Row>
                    <Col span="8">
                        <Tree :data="treeSouce" @on-select-change="selectedTree" children-key="sysGroupDTO"></Tree>
                    </Col>
                    <Col span="16">
                        <Checkbox
                        :indeterminate="indeterminate"
                        :value="checkAll"
                        @click.prevent.native="handleCheckAll"
                        class="mb-10">选择成员</Checkbox>
                        <CheckboxGroup v-model="checkAllGroup" @on-change="checkAllGroupChange"> 
                            <Checkbox :label="member.id" v-for="member in members" :key="member.id">
                                <span>{{member.realName}}</span>
                            </Checkbox>
                        </CheckboxGroup>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="warning" @click="saveRoleMember" shape="circle">保存</Button>
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
                index: 1,
                rows: 5,
                size:'',
                addModel:false,
                isShowClose: false,
                indeterminate: false,
                checkAll: false,
                checkAllGroup: [],
                formItem1:{
                    select:'',
                },
                roles: [
                    {
                        type: 'selection',
                        width: 1,
                        align: 'center'
                    },
                    {
                        title: '员工号',
                        key: 'userNo',
                        width: 200
                    },
                    {
                        title: '姓名',
                        key: 'realName',
                        width: 120
                    },
                    {
                        title: '手机号',
                        key: 'tel',
                        width: 120
                    },
                    {
                        title: '邮箱',
                        key: 'email',
                    }
                ],
                rolesList: [],
                treeSouce: [],
                members:[],
                roleId: '', // 角色id
                userIds: '', // 用户id字符串
                checkAll: false, // 全选checkbox的状态
                resourceList: [], // 所有的菜单列表
                resourceIds: [], // 角色菜单id
                activeTab: 'name1', // 当前激活的tab
            }
        },
        components: {
            Breadcrumb,
            UserInfor,
        },
        mounted() {
            // this.roleMembersList();
            this.getResourceList();
        },
        created(){
    
        },
        methods: {
            // 点击tab切换
            tabClick(name) {
                this.activeTab = name;
                if(name == 'name2') {
                    this.getRoleResource()
                }else {
                    this.roleMembersList()
                }
            },
            // 获取所有菜单列表
            getResourceList() {
                this.$http.get('/SysResource/tree-list')
                .then(res => {
                    let data = response.data(res, this);
                    if(data){
                        for(var i=0;i<data.length;i++) {
                            data[i].title = data[i].text;
                            data[i].expand = true;
                            if(data[i].childList){
                                for(var j=0;j<data[i].childList.length;j++) {
                                    data[i].childList[j].title = data[i].childList[j].text;
                                    data[i].childList[j].expand = true;
                                    data[i].childList[j].checked = false; // 先将选中设置为false，后面循环改为true
                                    if(data[i].childList[j].childList){
                                        for(var g=0;g<data[i].childList[j].childList.length;g++){
                                            data[i].childList[j].childList[g].checked = false;
                                            data[i].childList[j].childList[g].title = data[i].childList[j].childList[g].text
                                        }
                                    }
                                }
                            }
                        }
                        this.resourceList = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取菜单列表失败！')
                })
            },
            // 获取角色下的菜单权限
            getRoleResource(roleId) {
                if(roleId){
                    this.roleId = roleId;
                }
                let data = {
                    roleIds: this.roleId,
                }
                this.$http.get('/sys/role-resource', {params: data})
                .then(res => {
                    let data = response.data(res, this);
                    if(data){
                        if(data.list){
                            for(var i=0;i<data.list.length;i++) {
                                for(var r=0;r<this.resourceList.length;r++){
                                    // if(data.list[i].id == this.resourceList[r].id){
                                    //     this.resourceList[r].selected = true;
                                    // }
                                    // 循环比较this.resourceList和获取到的权限列表
                                    // 如果权限列表中有就将this.resourceList中的对应id的数据的checked设置为true，
                                    // 为选中状态
                                    if(data.list[i].childList){
                                        for(var j=0;j<data.list[i].childList.length;j++) {
                                            if(this.resourceList[r].childList){
                                                for(var t=0;t<this.resourceList[r].childList.length;t++){
                                                    if(this.resourceList[r].childList[t].id == data.list[i].childList[j].id){
                                                        this.resourceList[r].childList[t].checked = true;
                                                    }
                                                }
                                            }
                                            if(data.list[i].childList[j].childList){
                                                for(var g=0;g<data.list[i].childList[j].childList.length;g++){
                                                    if(this.resourceList[r].childList[t].childList){
                                                        for(var f=0;f<this.resourceList[r].childList[t].childList.length;f++){
                                                            if(this.resourceList[r].childList[t].childList[f].id == data.list[i].childList[j].childList[g].id){
                                                                this.resourceList[r].childList[t].childList[f].checked = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        console.log(this.resourceList);
                    }
                })
                .catch(res => {
                    console.log(res);
                    this.$Message.error('获取角色菜单权限失败！')
                })
            },
            // 获取角色成员列表
            roleMembersList(roleId){
                if(roleId){
                    this.roleId = roleId;
                }
                let data = {
                    roleId: this.roleId,
                    pageNum: this.index,
                    pageSize: this.rows,
                }
                this.$http.get('/sys-user/by-role-id', {params: data})
                .then(res => {
                    let data = response.data(res, this);
                    if(data){
                        this.rolesList = data.list;
                        this.size = data.total;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取角色成员失败！')
                })
            },
            // 分页
            roleChange(params){
                this.index = params;
                this.roleMembersList();
            },
            // 添加成员
            addModelShow(){
                this.addModel= true;
                this.getDepartmentGroup();
                this.getGroupList();
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
                    this.$Message.error('获取部门组列表失败！')
                })
            },
            // 点击树触发的事件
            selectedTree(data) {
                // console.log(data[0].id);
                this.getGroupList(data[0].id);
            },
            // 查询部门组下的所有成员
            getGroupList(id){
                let data = {
                    groupId: id,
                    roleId: this.roleId
                }
                this.$http.get('/sys-group/group-user-list-no-role', {params: data})
                .then(res => {
                    let data = response.data(res, this);
                    if(data){
                        this.members = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取部门组成员失败！')
                })
            },
            // 角色的添加成员保存
            saveRoleMember() {
                // 获取选中的user id
                let ids = [];
                this.checkAllGroup.forEach ((check) => {
                    if (check) {
                        ids.push(check);
                    }
                });
                console.log(ids)
                // 组装传参
                let data = new FormData();
                data.append('roleId', this.roleId);
                
                if(ids.length){
                    data.append('userIds', ids.join(','));
                }else{
                    this.$Message.info('请选择成员！')
                    return ;
                }
                this.$http.post('/sys-user-role', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('添加成员成功！');
                        this.addModel= false;
                        this.roleMembersList();
                        this.closeModal();
                    }
                })
                .catch(res => {
                    this.$Message.error('添加成员失败！')
                })
            },
            // 批量删除角色成员
            deleteRoleMember() {
                let dataJson = {
                    roleId: this.roleId,
                    userIds: this.userIds,
                }
                this.$http.delete('/sys-user-role', {params: dataJson})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('批量移除角色成员成功！');
                        this.roleMembersList();
                        this.checkAll = false;
                    }
                })
                .catch(res => {
                    this.$Message.error('批量移除角色成员失败！')
                })
            },
            // 全选功能
            selectAllCol(status){
                this.$refs.selection.selectAll(status);
            },
            // 监听表格的选中行，获取批量删除的uesrId
            roleSel(selection) {
                console.log(selection)
                let idArr = [];
                if(selection.length){
                    for(var i=0; i<selection.length; i++){
                        idArr.push(selection[i].id);
                    }
                    this.userIds = idArr.join(',');
                }
            },
            // 弹出层里复选框全选
            handleCheckAll () {
                this.checkAllGroup = [];
                if (this.indeterminate) {
                    this.checkAll = false;
                } else {
                    this.checkAll = !this.checkAll;
                }
                this.indeterminate = false;
                if (this.checkAll) {
                    for(var i=0;i<this.members.length;i++){
                        this.checkAllGroup.push(this.members[i].id)
                    }
                    // console.log(this.checkAllGroup)
                } else {
                    this.checkAllGroup = [];
                }
            },
            // 弹出层里复选框反选
            checkAllGroupChange (data) {
                if (data.length === this.members.length) {
                    this.indeterminate = false;
                    this.checkAll = true;
                } else if (data.length > 0) {
                    this.indeterminate = true;
                    this.checkAll = false;
                } else {
                    this.indeterminate = false;
                    this.checkAll = false;
                }
            },
            // 点击tree 的复选框获取选中的节点
            checkedTree(nodes) {
                this.resourceIds = [];
                for(var i=0;i<nodes.length;i++) {
                    this.resourceIds.push(nodes[i].id)
                }
            },
            // 修改角色的菜单权限的提交按钮
            updateRoleResource(){
                let data = new FormData();
                data.append('roleId', this.roleId);
                data.append('resourceIds', this.resourceIds);
                this.$http.post('/sys/role-resource', data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('更新权限成功！');
                        // this.activeTab = 'name1';
                    }
                })
                .catch(res => {
                    this.$Message.error('更新权限失败！')
                })
            },
            // 关闭弹出层
            closeModal() {
                this.indeterminate = false;
                this.checkAll = false;
                this.checkAllGroup = [];
            },
        } 
    }
</script>
<style lang="less" scoped>
    .operation-box{
        margin: 0 0 15px 59px;
    }
</style>