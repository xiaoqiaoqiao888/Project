<template>
    <div class="personal-center fr">
        <div class="message-box fl">
            <i class="dot"></i>
        </div>
        <div class="add-project fl" @click="openAddProject"></div>
        <div class="split-line fl"></div>
        <Dropdown placement="bottom-end" class="fl">
            <a href="javascript:void(0)" class="avatar-box">
                <img class="user-img" src="../../../assets/userImg.jpg" alt="用户头像" />
                <Icon type="arrow-down-b"></Icon>
            </a>
            <DropdownMenu slot="list">
                <DropdownItem name="personal">个人中心</DropdownItem>
                <DropdownItem name="password">密码设置</DropdownItem>
                <DropdownItem name="role" v-if="isShowSetting"><router-link :to="rolePath">{{roleText}}</router-link></DropdownItem>
                <DropdownItem name="client" v-if="isShowSetting"><router-link :to="clientPath">{{clientText}}</router-link></DropdownItem>
                <DropdownItem name="logout" @click.native="logout">退出</DropdownItem>
            </DropdownMenu>
        </Dropdown>
        <!-- 添加项目弹出层 -->
        <Modal
            v-model="addProjectModel"
            title="新建项目"
            class-name="vertical-center-modal"
            :closable="isShowClose"
            @on-cancel="closeModal('formAdd')"
        >
            <Form ref="formAdd" :model="formAdd" :rules="ruleValidate" label-position="top">
                <FormItem prop="projectName">
                    <Input v-model="formAdd.projectName" placeholder="项目名称"></Input>
                </FormItem>
                <FormItem prop="projectBudget">
                    <Input v-model="formAdd.projectBudget" placeholder="项目预算" number></Input>
                </FormItem>
                <FormItem label="项目实施周期">
                    <Row>
                        <Col span="11">
                            <FormItem prop="startTime">
                                <DatePicker
                                    type="date"
                                    placeholder="开始时间"
                                    v-model="formAdd.startTime"
                                >
                                </DatePicker>
                            </FormItem>
                        </Col>
                        <Col span="2" style="height: 40px;"></Col>
                        <Col span="11">
                            <FormItem prop="endTime">
                                <DatePicker
                                    type="date"
                                    placeholder="结束时间"
                                    v-model="formAdd.endTime"
                                >
                                </DatePicker>
                            </FormItem>
                        </Col>
                    </Row>
                </FormItem>
                <FormItem label="项目描述" prop="projectDesc">
                    <Input v-model="formAdd.projectDesc" type="textarea" :autosize="{minRows: 4,maxRows: 5}"></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="warning" @click="affirmBtn('formAdd')" shape="circle">创建新项目</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
import * as response from '../../../util/response.js'
import Cookie from 'js-cookie'
    export default {
        components : {
        },
        data () {
            return {
                addProjectModel: false,
                isShowClose: false,
                formAdd: {
                    projectName: '',
                    projectBudget: '',
                    startTime: '',
                    endTime: '',
                    projectDesc: '',
                },
                ruleValidate: {
                    projectName: [
                        { required: true, message: '项目名称不能为空', trigger: 'blur' },
                        { type: 'string', max: 50, message: '最多输入50个字符', trigger: 'blur' }
                    ],
                    projectBudget: [
                        { required: true, type: 'number', min: 1, message: '项目预算不能为空', trigger: 'blur' },
                        { type: 'number', max: 9999999999, message: '请输入10位以下的数字', trigger: 'blur' }
                    ],
                    startTime: [
                        { required: true, type: 'date', message: '请选择开始日期', trigger: 'change' }
                    ],
                    endTime: [
                        { required: true, type: 'date', message: '请选择结束日期', trigger: 'change' }
                    ],
                    projectDesc:[{ type: 'string', max: 300, message: '最多输入300个字符', trigger: 'blur' }]
                },
                isShowSetting: localStorage.getItem('sysResource') ? true : false,
                roleText: localStorage.getItem('sysResource') ? 
                    JSON.parse(localStorage.getItem('sysResource'))[0].text : '', // 由后台取数据，填充角色管理和人员管理
                rolePath: localStorage.getItem('sysResource') ? 
                    JSON.parse(localStorage.getItem('sysResource'))[0].href : '', // 由后台取数据，填充角色管理和人员管理
                clientText: localStorage.getItem('sysResource') ? 
                    JSON.parse(localStorage.getItem('sysResource'))[1].text : '', // 由后台取数据，填充角色管理和人员管理
                clientPath: localStorage.getItem('sysResource') ? 
                    JSON.parse(localStorage.getItem('sysResource'))[1].href : '', // 由后台取数据，填充角色管理和人员管理
            }
        },
        methods: {
            // 打开新建项目弹出层
            openAddProject() {
                this.addProjectModel = true;
            },
            // 弹出层确认
            affirmBtn(type) {
                this.addProject(type);
            },
            // 添加项目
            addProject(type) {
                let data = new FormData();
                data.append('projectName', this.formAdd.projectName);
                data.append('projectBudget', this.formAdd.projectBudget);
                data.append('startTime',this.$dateFormat(this.formAdd.startTime));
                data.append('endTime', this.$dateFormat(this.formAdd.endTime));
                data.append('projectDesc', this.formAdd.projectDesc);
                // 项目编码暂时由前端传入，规则为pmt+new Date()
                data.append('projectCode', `pmt${new Date().getTime()}`);
                this.$refs[type].validate((valid) => {
                    if (valid) {
                        this.$http.post('/project/add', data)
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                this.$Message.success('添加栏目成功!');
                                this.addProjectModel = false;
                                this.$emit('refreshList');
                                this.closeModal(type);
                            }
                        })
                        .catch(res => {
                            this.$Message.error('添加项目失败！')
                        })
                    }
                })
            },
            // 关闭弹出层
            closeModal(type) {
                this.$refs[type].resetFields();
            },
            // 个人中心
            personalCenter() {

            },
            // 密码设置
            passwordSetting() {

            },
            // 角色管理
            roleManage() {

            },
            // 人员管理
            clientMange() {

            },
            // 退出
            logout() {
                this.$http.get('/logout')
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        Cookie.remove('userInf')
                        Cookie.remove('Authorization')
                        localStorage.removeItem('projectId')
                        localStorage.removeItem('sysResource')
                        this.$router.push({ path:'/Login'})
                    }
                })
                .catch(res => {
                    this.$Message.error('退出失败！')
                })
                
            },
        },
        mounted() {
            
        }
    }
</script>
<style lang="less" scoped>
.personal-center {
  margin-top: 32px;
  margin-right: 10px;
  .message-box {
    width: 20px;
    height: 14px;
    background: url(../../../assets/Email-Icons.svg) no-repeat;
    position: relative;
    cursor: pointer;
    margin-right: 49px;
    margin-top: 16px;
    .dot {
      position: absolute;
      top: -4px;
      right: -4px;
      width: 6px;
      height: 6px;
      background: #ce57d0;
      border-radius: 50%;
    }
  }
  .add-project {
    width: 36px;
    height: 36px;
    background: #ff9e22 url(../../../assets/add.svg) no-repeat center center;
    box-shadow: 0 10px 10px 0 rgba(0, 0, 0, 0.1);
    margin-right: 28px;
    border-radius: 50%;
    margin-top: 4px;
    cursor: pointer;
  }
  .split-line {
    width: 2px;
    height: 26px;
    background: #e9e9e9;
    margin-right: 24px;
    margin-top: 10px;
  }
  .avatar-box {
    margin-right: 30px;
  }
  .user-img {
    width: 44px;
    height: 44px;
    border-radius: 50%;
    box-shadow: 0 10px 10px 0 rgba(0, 0, 0, 0.1);
    margin-right: 6px;
  }
}
</style>