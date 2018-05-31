<template>
    <div class="work-left-box">
        <h2 class="system-title">管理部门</h2>
        <div class="cl">
            <Button type="primary" @click="addFirstGroup" class="fr mb-10">添加</Button>
        </div>
        <div class="tree-box" v-if="groupList.length">
            <Tree :data="groupList" :render="renderContent" children-key="sysGroupDTO"></Tree>
        </div>
        <Modal
            v-model="addGroupModel"
            title="添加部门"
            class-name="vertical-center-modal"
            :closable="isShowClose"
            @on-cancel="closeModal('formGroupAdd')"
        >
            <Form ref="formGroupAdd" :model="formGroupAdd" :rules="ruleValidate" label-position="top">
                <FormItem prop="groupName">
                    <Input v-model="formGroupAdd.groupName" placeholder="部门名称" ></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="warning" @click="addGroup('formGroupAdd')" shape="circle">添加部门</Button>
            </div>
        </Modal>
        <Modal
            v-model="editGroupModel"
            title="编辑部门"
            class-name="vertical-center-modal"
            :closable="isShowClose"
            @on-cancel="closeModal('formEditGroup')"
        >
            <Form ref="formEditGroup" :model="formEditGroup" :rules="ruleValidate" label-position="top">
                <FormItem prop="groupName">
                    <Input v-model="formEditGroup.groupName" placeholder="部门名称" ></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="warning" @click="editGroup('formEditGroup')" shape="circle">确认</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
    import * as response from '../../../util/response.js'
    export default {
        data() {
            return {
                formGroupAdd: {
                    groupName: '',
                },
                isShowClose: false,
                ruleValidate: {
                    groupName: [
                        { required: true, message: '部门名称不能为空', trigger: 'blur' },
                        { type: 'string', max: 20, message: '最多输入20个字符', trigger: 'blur' }
                    ],
                },
                addGroupModel: false,
                groupLevel: '', // 部门级别
                addParentId: '',
                formEditGroup: {
                    groupName: '',
                },
                groupEditId: '', // 编辑时的部门id
                editpParentId: '', // 编辑时的pId
                groupList: [],
                buttonProps: {
                    type: 'ghost',
                    size: 'small',
                },
                editGroupModel: false,
                treeId: '', // 树节点id
            }
        },
        components: {
        },
        mounted() {
            this.getGroupList();
        },
        created(){
    
        },
        methods: {
            renderContent (h, { root, node, data }) {
                return h('span', {
                    style: {
                        display: 'inline-block',
                        width: '100%'
                    }
                }, [
                    h('span', [
                        // h('Icon', {
                        //     props: {
                        //         type: 'ios-paper-outline'
                        //     },
                        //     style: {
                        //         marginRight: '8px',
                        //     }
                        // }),
                        h('span',{
                            style: {
                                cursor: 'pointer',
                            },
                            class:{
                                'ivu-tree-title-selected': data.id == this.treeId
                            },
                            on: {
                                click: () => {
                                    this.$emit('refreshTable',data.id);
                                    this.treeId = data.id;
                                }
                            }
                        }, data.groupName)
                    ]),
                    h('span', {
                        style: {
                            display: 'inline-block',
                            float: 'right',
                            marginRight: '16px'
                        }
                    }, [
                        h('Button', {
                            props: Object.assign({}, this.buttonProps, {
                                icon: 'android-add'
                            }),
                            style: {
                                marginRight: '8px'
                            },
                            on: {
                                click: () => {
                                    this.addParentId = data.id;
                                    if(data.groupLevel==1){
                                        this.addGroupModal(2);
                                    }else if(data.groupLevel==2) {
                                        this.addGroupModal(3);
                                    }else if(data.groupLevel==3){
                                        this.$Message.info('最多添加三级部门！')
                                    }
                                }
                            }
                        }),
                        h('Button', {
                            props: Object.assign({}, this.buttonProps, {
                                icon: 'edit'
                            }),
                            style: {
                                marginRight: '8px'
                            },
                            on: {
                                click: () => {
                                    this.editGroupModel = true;
                                    this.formEditGroup.groupName = data.groupName
                                    this.groupEditId = data.id;
                                    this.editpParentId = data.parentId;
                                }
                            }
                        }),
                        h('Button', {
                            props: Object.assign({}, this.buttonProps, {
                                icon: 'android-delete'
                            }),
                            on: {
                                click: () => { 
                                    this.$Modal.confirm({
                                        title: '删除部门',
                                        content: '部门一旦删除，将无法查看，确认删除吗？',
                                        onOk: () => {
                                            this.deleGroup(data.id);
                                        },
                                        onCancel: () => {
                                            
                                        }
                                    });
                                }
                            }
                        })
                    ])
                ]);
            },
            // 添加一级部门
            addFirstGroup() {
                this.addParentId = 0;
                this.addGroupModel = true;
                this.groupLevel = 1;
            },
            // 添加部门弹出层
            addGroupModal(level) {
                this.addGroupModel = true;
                this.groupLevel = level;
            },
            // 获取部门列表
            getGroupList() {
                this.$http.get('/sys-group/tree-list')
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.groupList = data;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取部门列表失败！')
                })
            },
            // 添加部门提交按钮
            addGroup(type) {
                let dataJson = {
                    groupName: this.formGroupAdd.groupName,
                    groupLevel: this.groupLevel,
                    parentId: this.addParentId
                };
                this.$refs[type].validate((valid) => {
                    if (valid) {
                        this.$http.post('/sys-group', dataJson)
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                this.$Message.success('添加部门成功！');
                                this.addGroupModel = false;
                                this.getGroupList();
                            }
                        })
                        .catch(res => {
                            this.$Message.error('添加部门失败！')
                        })
                    }
                })
            },
            // 删除部门
            deleGroup(groupId) {
                let dataJson = {
                    id: groupId
                }
                this.$http.delete('/sys-group', {params: dataJson})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Message.success('删除部门成功！');
                        this.getGroupList();
                    }
                })
                .catch(res => {
                    this.$Message.error('删除部门失败！')
                })
            },
            // 编辑部门提交
            editGroup(type) {
                let dataJson = {
                    id: this.groupEditId,
                    parentId: this.editpParentId,
                    groupName: this.formEditGroup.groupName
                }
                this.$refs[type].validate((valid) => {
                    if (valid) {
                        this.$http.post('/sys-group/update', dataJson)
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                this.$Message.success('编辑部门成功！');
                                this.editGroupModel = false;
                                this.getGroupList();
                            }
                        })
                        .catch(res => {
                            this.$Message.error('编辑部门失败！')
                        })
                    }
                })
            },
            // 关闭弹出层
            closeModal(type) {
                this.$refs[type].resetFields();
            },
        } 
    }
</script>
<style scoped>
.work-left-box{
    width:100%;
}
</style>