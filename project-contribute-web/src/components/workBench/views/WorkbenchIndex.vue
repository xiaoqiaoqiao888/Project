<template>
    <div>
        <div class="cl">
            <Breadcrumb title="项目管理" content="我的项目情况" />
            <UserInfor/>
        </div>
        <div>
            <Row type="flex" justify="space-around" class="card-row">
                <Col span="7">
                    <div class="cont-bg card-box">
                        <div class="title cl">
                            <span>未开始项目（2）</span>
                            <router-link to="/WorkBench/Project" class="more-img fr">
                                <img src="../../../assets/more.svg"/>
                            </router-link>
                        </div>
                        <div>
                            <dl class="cl">
                                <dd class="img-dd">
                                    <!-- <img src="../../../assets/Arrow-Icon-down.svg"/> -->
                                    <img src="../../../assets/Arrow-Icon-up.svg"/>
                                </dd>
                                <dd class="rmb-dd">
                                    <p class="rmb-p">2000</p>
                                    <p>RMB</p>
                                </dd>
                                <!-- <dd class="per-dd">
                                    <span>30%</span>
                                </dd> -->
                            </dl>
                            <Progress :percent="25" hide-info class="red-progress"></Progress>
                        </div>
                    </div>
                </Col>
                <Col span="7">
                    <div class="cont-bg card-box">
                        <div class="title cl">
                            <span>进行中...（2）</span>
                            <router-link to="/WorkBench/Project" class="more-img fr">
                                <img src="../../../assets/more.svg"/>
                            </router-link>
                        </div>
                        <div>
                            <dl class="cl">
                                <dd class="img-dd">
                                    <!-- <img src="../../../assets/Arrow-Icon-down.svg"/> -->
                                    <img src="../../../assets/Arrow-Icon-up.svg"/>
                                </dd>
                                <dd class="rmb-dd">
                                    <p class="rmb-p">2000</p>
                                    <p>RMB</p>
                                </dd>
                                <dd class="per-dd">
                                    <span>30%</span>
                                </dd>
                            </dl>
                            <Progress :percent="25" hide-info></Progress>
                        </div>
                    </div>
                </Col>
                <Col span="7">
                    <div class="cont-bg card-box">
                        <div class="title cl">
                            <span>延期进行中...（2）</span>
                            <router-link to="/WorkBench/Project" class="more-img fr">
                                <img src="../../../assets/more.svg"/>
                            </router-link>
                        </div>
                        <div>
                            <dl class="cl">
                                <dd class="img-dd">
                                    <!-- <img src="../../../assets/Arrow-Icon-down.svg"/> -->
                                    <img src="../../../assets/Arrow-Icon-up.svg"/>
                                </dd>
                                <dd class="rmb-dd">
                                    <p class="rmb-p">2000</p>
                                    <p>RMB</p>
                                </dd>
                                <dd class="per-dd">
                                    <span>30%</span>
                                </dd>
                            </dl>
                            <Progress :percent="25" hide-info></Progress>
                        </div>
                    </div>
                </Col>
            </Row>
            <Row type="flex" justify="space-around" class="card-row">
                <Col span="7">
                    <div class="cont-bg card-box">
                        <div class="title cl">
                            <span>已完成项目（2）</span>
                            <router-link to="/WorkBench/Project" class="more-img fr">
                                <img src="../../../assets/more.svg"/>
                            </router-link>
                        </div>
                        <div>
                            <dl class="cl">
                                <dd class="img-dd">
                                    <!-- <img src="../../../assets/Arrow-Icon-down.svg"/> -->
                                    <img src="../../../assets/Arrow-Icon-up.svg"/>
                                </dd>
                                <dd class="rmb-dd">
                                    <p class="rmb-p">2000</p>
                                    <p>RMB</p>
                                </dd>
                                <dd class="per-dd">
                                    <span>30%</span>
                                </dd>
                            </dl>
                            <Progress :percent="25" hide-info></Progress>
                        </div>
                    </div>
                </Col>
                <Col span="7"></Col>
                <Col span="7"></Col>
            </Row>
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
                index:'',
                rows:'',
                size:'',  
                projectList:[], 
            }
        },
        components: {
            Breadcrumb,
            UserInfor
        },
        mounted() {
            // this.getProjectList();
        },
        created(){
    
        },
        methods: {
            // 获取项目列表
            getProjectList(){
                var data = {
                    // projectState:0, //0-未开始 1-进行中 4-延期进行中 3-已完成
                }
                this.$http.get('/project/status-count', { params: data })
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.size = data.total;
                        this.projectList = data.list;
                        
                    }
                })
                .catch(res => {
                    console.log(res);
                    this.$Message.error('获取列表失败')
                })
            }
            
        } 
    }
</script>
<style scoped>
    
</style>