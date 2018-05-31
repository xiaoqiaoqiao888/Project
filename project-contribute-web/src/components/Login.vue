<template>
    <div class="login-container cl">
        <div class="login-left fl">
            <div>
                <img src="../assets/login-bg02.png"/>
                <img src="../assets/login-bg-txt.png" class="img-txt"/>
            </div>
        </div>
        <div class="login-right fl">
            <div class="login-cont">
                <h2>Hello partner!</h2>
                <Form ref="formInline" :model="formInline" :rules="ruleInline">
                    <FormItem prop="user">
                        <Input type="text" v-model="formInline.user" placeholder="UserNo">
                        </Input>
                    </FormItem>
                    <FormItem prop="password">
                        <Input type="password" v-model="formInline.password" placeholder="Password">

                        </Input>
                        <span class="ivu-form-item-error-tip" v-if="isEmpty && errorMsg">{{errorMsg}}</span>
                    </FormItem>
                    <FormItem>
                        <Checkbox v-model="single" style="color:#FA9590">记住密码</Checkbox>
                        <Button type="warning" class="fr" @click="handleSubmit('formInline')">我要做任务</Button>
                    </FormItem>
                </Form>
                <div class="login-bottom">
                    <p>如在登录过程中出现问题，请联系：<br>010-82019000</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import * as response from '../util/response.js'
import Cookie from 'js-cookie'
  export default {
        data () {
            const validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入登录密码'));
                    this.isEmpty = false;
                } else {
                    callback();
                    this.isEmpty = true;
                }
            };
            return {
                single:false,
                formInline: {
                    user: '',
                    password: ''
                },
                ruleInline: {
                    user: [
                        { required: true, message: '请输入员工号', trigger: 'blur' }
                    ],
                    password: [
                        { validator: validatePass, trigger: 'blur' }
                        // { type: 'string', min: 2, message: '登录密码格式不正确', trigger: 'blur' }
                    ]
                },
                errorMsg: '',
                isEmpty: false
            }
        },
        mounted() {
            // 记住密码
            this.rememberPass();
        },
        methods: {
            handleSubmit(name) {
                var data = new FormData();
                data.append('userNo',this.formInline.user);
                data.append('password',this.formInline.password);
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        //判断复选框是否被勾选 勾选则调用配置cookie方法
                        if (this.single == true) {
                            //保存账号名，密码到cookie里
                            Cookie.set('remUser',this.formInline.user);
                            Cookie.set('remPass',this.formInline.password);
                        }else {
                          //清空Cookie
                          Cookie.remove('remUser');
                          Cookie.remove('remPass');
                        }
                        this.$http.post('/login', data)
                        .then(res => {
                            let data = response.data(res, this);
                            if (data) {
                                if(data.user){
                                    this.errorMsg = '';
                                    this.$router.push({ 'path': '/' });
                                    Cookie.set('userInf',data.user);
                                }else{
                                    this.errorMsg = data.message;
                                }
                            }else{
                                this.$Message.error(res.data.status.message);
                            }
                        })
                        .catch(res => {
                            console.log(res);
                            // this.$Message.error('')
                        })
                    } else {
                        // this.$Message.error('Fail!');
                    }
                })
            },
            // 记住密码
            rememberPass(){
                if(Cookie.get('remUser') || Cookie.get('remPass')){
                    this.formInline.user = Cookie.get('remUser');
                    this.formInline.password = Cookie.get('remPass');
                }
            },
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .login-container{
        width:100%;
        height:100%;
        background:url(../assets/login-bg.png) no-repeat;
        background-size:cover;
        position: fixed;
        top:0;
        bottom:0;
    }
    .login-left,.login-right{
        width:50%;
        height:100%;

        display:table;
    }
    .login-left div{
        padding:0 10%;
        text-align: center;
        display: table-cell;
        vertical-align: middle;
    }
    .login-left div img{
        width:100%;
    }
    .login-left div .img-txt{
        width:65%;
        margin:25px auto 0;
    }
    .login-right{
        right:0;
        background:#fff;
    }
    .login-cont{
        padding:0 15% 0 10%;
        display: table-cell;
        vertical-align: middle;
    }
    .login-cont h2{
        font-size: 48px;
        color: #4C4C4C;
        letter-spacing: 0;
        line-height: 90px;
        position: relative;
        text-align:center;
    }
    .login-cont h2::before{
        content: '';
        width:90px;
        height:8px;
        background: #FF9E22;
        position:absolute;
        bottom:0px;
        left:50%;
        margin-left:-52.5px;
    }
    .login-cont .ivu-form-item{
        margin-bottom:30px;
    }
    .login-bottom{
        border-top:1px solid #E2E2E2;
        text-align:center;
        padding-top:20px;
        font-size:16px;
        margin-top:35px;
    }
</style>
