<template>
    <div>
       <div class="cl">
           <Breadcrumb title="项目管理" content="我的项目情况" />
           <UserInfor @refreshList="onRefresList"  />
        </div>
       <div class="cont-bg mlr-20">
            <div class="project-list-table po_re">
                <Form :model="FormItem" inline class="pos-form">
                    <FormItem>
                        <Input type="text" v-model="FormItem.name" placeholder="请输入项目名称">
                        </Input>
                    </FormItem>
                    <FormItem>
                        <Button type="primary" @click="queryBtn">查询</Button>
                    </FormItem>
                </Form>
                <Tabs value="name1" :animated="false" @on-click="getList">
                    <TabPane :label="label0" name="name1">
                        <Table :columns="notStartedTable" :data="notStarted"></Table>
                        <div class="page-box">
                            <Page 
                            :total="Math.floor(size0)" 
                            :current="Math.floor(index0)" 
                            :page-size="Math.floor(rows0)"
                            @on-change="change0"
                            simple></Page>
                        </div>
                    </TabPane>
                    <TabPane :label="label1" name="name2" >
                        <Table :columns="underWayTable" :data="underWay"></Table>
                        <div class="page-box">
                            <Page :total="Math.floor(size1)" 
                            :current="Math.floor(index1)" 
                            :page-size="Math.floor(rows1)"
                            @on-change="change1"
                            simple></Page>
                        </div>
                    </TabPane>
                    <TabPane :label="label2" name="name3" >
                        <Table :columns="postponeTable" :data="postpone"></Table>
                        <div class="page-box">
                            <Page :total="Math.floor(size2)" 
                            :current="Math.floor(index2)" 
                            :page-size="Math.floor(rows2)"
                            @on-change="change2"
                            simple></Page>
                        </div>
                    </TabPane>
                    <TabPane :label="label3" name="name4">
                        <Table :columns="stocksTable" :data="stocks"></Table>
                        <div class="page-box">
                            <Page :total="Math.floor(size3)" 
                            :current="Math.floor(index3)" 
                            :page-size="Math.floor(rows3)"
                            @on-change="change3"
                            simple></Page>
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
                index0:1,
                rows0:5,
                size0:'',
                label0: (h) => {
                    return h('div', {

                    },'未开始(' + this.size0 +')')
                },
                label1: (h) => {
                    return h('div', {

                    },'进行中(' + this.size1 +')')
                },
                label2: (h) => {
                    return h('div', {

                    },'延期进行中(' + this.size2 +')')
                },
                label3: (h) => {
                    return h('div', {

                    },'已完成(' + this.size3 +')')
                },
                index1:1,
                rows1:5,
                size1:'',
                index2:1,
                rows2:5,
                size2:'',
                index3:1,
                rows3:5,
                size3:'',
                
                FormItem:{
                    name:'',
                },
                notStartedTable: [
                    {
                        title: '名称 / 描述',
                        key: 'sum',
                        width:260,
                        render: (h, params) => {
                            return h('dl', [
                                h('dt', {
                                    style: {
                                        float: 'left',
                                        width:'40px',
                                        height:'40px',
                                        textAlign:'center',
                                        lineHeight:'40px',
                                        background:'#359FFF',
                                        color:'#fff',
                                        borderRadius:'50px',
                                        letterSpacing:'3px',
                                    }
                                }, params.row.projectName),
                                h('dd',{
                                    slot: 'content',
                                    style: {
                                        width:'150px',
                                        float: 'left',
                                        textAlign:'left',
                                        lineHeight:'20px',
                                        marginLeft:'15px',
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
                                    }, params.row.projectName),
                                    h('p',{
                                        style: {
                                            height:'40px',
                                            fontSize:'12px',
                                            display: '-webkit-box',
                                            webkitBoxOrient: 'vertical',
                                            webkitLineClamp: '2',
                                            overflow: 'hidden',
                                        },
                                    },params.row.projectDesc),
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
                                },params.row.projectBudget+' RMB'),
                                // h('div', {
                                //     style: {
                                //     },
                                // },[
                                //     h('span', {
                                //         style: {
                                //             display:'inline-block',
                                //             verticalAlign:'middle',
                                //             marginRight:'8px',
                                //             width:'26px',
                                //             height:'26px',
                                //             background:'#f5f5f5',
                                //             borderRadius:'50px',
                                //         },
                                //     },''), 
                                //     h('span', {
                                //         style: {

                                //         },
                                //     },'68%'),
                                // ]),
                                
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
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.$router.push({path:'/WorkBench/StageProject',query:{ id: params.row.id }})
                                            console.log(params.row.id)
                                        }
                                    }
                                }, '划分阶段'),
                            ]);
                        }
                    }
                ],
                notStarted: [],
                underWayTable:[
                    {
                        title: '名称 / 描述',
                        key: 'sum',
                        width:260,
                        render: (h, params) => {
                            return h('dl', [
                                h('dt', {
                                    style: {
                                        float: 'left',
                                        width:'40px',
                                        height:'40px',
                                        textAlign:'center',
                                        lineHeight:'40px',
                                        background:'#359FFF',
                                        color:'#fff',
                                        borderRadius:'50px',
                                        letterSpacing:'3px',
                                    }
                                }, params.row.projectName),
                                h('dd',{
                                    slot: 'content',
                                    style: {
                                        width:'150px',
                                        float: 'left',
                                        textAlign:'left',
                                        lineHeight:'20px',
                                        marginLeft:'15px',
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
                                    }, params.row.projectName),
                                    h('p',{
                                        style: {
                                            height:'40px',
                                            fontSize:'12px',
                                            display: '-webkit-box',
                                            webkitBoxOrient: 'vertical',
                                            webkitLineClamp: '2',
                                            overflow: 'hidden',
                                        },
                                    },params.row.projectDesc),
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
                                },params.row.projectBudget+' RMB'),
                                // h('div', {
                                //     style: {
                                //     },
                                // },[
                                //     h('span', {
                                //         style: {
                                //             display:'inline-block',
                                //             verticalAlign:'middle',
                                //             marginRight:'8px',
                                //             width:'26px',
                                //             height:'26px',
                                //             background:'#f5f5f5',
                                //             borderRadius:'50px',
                                //         },
                                //     },''), 
                                //     h('span', {
                                //         style: {

                                //         },
                                //     },'68%'),
                                // ]),
                                
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
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.$router.push({path:'/WorkBench/StageProject',query:{ id: params.row.id}})                                            
                                        }
                                    }
                                }, '划分阶段'),
                            ]);
                        }
                    }
                ],
                underWay: [],
                postponeTable:[
                    {
                        title: '名称 / 描述',
                        key: 'sum',
                        width:260,
                        render: (h, params) => {
                            return h('dl', [
                                h('dt', {
                                    style: {
                                        float: 'left',
                                        width:'40px',
                                        height:'40px',
                                        textAlign:'center',
                                        lineHeight:'40px',
                                        background:'#359FFF',
                                        color:'#fff',
                                        borderRadius:'50px',
                                        letterSpacing:'3px',
                                    }
                                }, params.row.projectName),
                                h('dd',{
                                    slot: 'content',
                                    style: {
                                        width:'150px',
                                        float: 'left',
                                        textAlign:'left',
                                        lineHeight:'20px',
                                        marginLeft:'15px',
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
                                    }, params.row.projectName),
                                    h('p',{
                                        style: {
                                            height:'40px',
                                            fontSize:'12px',
                                            display: '-webkit-box',
                                            webkitBoxOrient: 'vertical',
                                            webkitLineClamp: '2',
                                            overflow: 'hidden',
                                        },
                                    },params.row.projectDesc),
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
                                },params.row.projectBudget+' RMB'),
                                // h('div', {
                                //     style: {
                                //     },
                                // },[
                                //     h('span', {
                                //         style: {
                                //             display:'inline-block',
                                //             verticalAlign:'middle',
                                //             marginRight:'8px',
                                //             width:'26px',
                                //             height:'26px',
                                //             background:'#f5f5f5',
                                //             borderRadius:'50px',
                                //         },
                                //     },''), 
                                //     h('span', {
                                //         style: {

                                //         },
                                //     },'68%'),
                                // ]),
                                
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
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.$router.push({path:'/WorkBench/StageProject',query:{ id: params.row.id }})                                            
                                        }
                                    }
                                }, '划分阶段'),
                            ]);
                        }
                    }
                ],
                postpone:[],
                stocksTable:[
                    {
                        title: '名称 / 描述',
                        key: 'sum',
                        width:260,
                        render: (h, params) => {
                            return h('dl', [
                                h('dt', {
                                    style: {
                                        float: 'left',
                                        width:'40px',
                                        height:'40px',
                                        textAlign:'center',
                                        lineHeight:'40px',
                                        background:'#359FFF',
                                        color:'#fff',
                                        borderRadius:'50px',
                                        letterSpacing:'3px',
                                    }
                                }, params.row.projectName),
                                h('dd',{
                                    slot: 'content',
                                    style: {
                                        width:'150px',
                                        float: 'left',
                                        textAlign:'left',
                                        lineHeight:'20px',
                                        marginLeft:'15px',
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
                                    }, params.row.projectName),
                                    h('p',{
                                        style: {
                                            height:'40px',
                                            fontSize:'12px',
                                            display: '-webkit-box',
                                            webkitBoxOrient: 'vertical',
                                            webkitLineClamp: '2',
                                            overflow: 'hidden',
                                        },
                                    },params.row.projectDesc),
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
                                },params.row.projectBudget+' RMB'),
                                // h('div', {
                                //     style: {
                                //     },
                                // },[
                                //     h('span', {
                                //         style: {
                                //             display:'inline-block',
                                //             verticalAlign:'middle',
                                //             marginRight:'8px',
                                //             width:'26px',
                                //             height:'26px',
                                //             background:'#f5f5f5',
                                //             borderRadius:'50px',
                                //         },
                                //     },''), 
                                //     h('span', {
                                //         style: {

                                //         },
                                //     },'68%'),
                                // ]),
                                
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
                    //                         this.$router.push({path:'/WorkBench/StageProject',query:{ id: params.row.id ,budget:params.row.projectBudget}})                                            
                    //                     }
                    //                 }
                    //             }, '划分阶段'),
                    //         ]);
                    //     }
                    // }
                ],
                stocks:[],
                
            }
        },
        components: {
            Breadcrumb,
            UserInfor
        },
        mounted() {
            this.getProjectList0();
            this.getProjectList1();
            this.getProjectList2();
            this.getProjectList3();
        },
        created(){
    
        },
        methods: {
            // 子组件调用父组件刷新方法
            onRefresList () {
                this.getProjectList0()
            },
            // 获取项目列表未开始
            getProjectList0(){
                var data = {
                    pageNum:this.index0,
                    pageSize:this.rows0,
                    projectState:0, //0-未开始 1-进行中 4-延期进行中 3-已完成
                    projectName:this.FormItem.name,
                }
                this.$http.get('/project/list', { params: data })
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.size0 = data.total;
                        this.notStarted = data.list;
                        
                    }
                })
                .catch(res => {
                    console.log(res);
                    this.$Message.error('获取列表失败')
                })
            },
            // 分页0
            change0(params) {
                this.index0 = params;
                this.getProjectList0(params);
                
            },
            // 获取项目列表进行中
            getProjectList1(){
                var data = {
                    pageNum:this.index1,
                    pageSize:this.rows1,
                    projectState:1, //0-未开始 1-进行中 4-延期进行中 3-已完成
                    projectName:this.FormItem.name,
                }
                this.$http.get('/project/list', { params: data })
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.size1 = data.total;
                        this.underWay = data.list;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取列表失败')
                })
            },
            // 分页1
            change1(params) {
                this.index1 = params;
                this.getProjectList1(params);
            },
            // 获取项目列表延期
            getProjectList2(){
                var data = {
                    pageNum:this.index2,
                    pageSize:this.rows2,
                    projectState:4, //0-未开始 1-进行中 4-延期进行中 3-已完成 
                    projectName:this.FormItem.name,
                }
                this.$http.get('/project/list', { params: data })
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.size2 = data.total;
                        this.postpone = data.list;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取列表失败')
                })
            },
            // 分页2
            change2(params) {
                this.index2 = params;
                this.getProjectList2(params);
            },
            // 获取项目列表延期
            getProjectList3(){
                var data = {
                    pageNum:this.index3,
                    pageSize:this.rows3,
                    projectState:3, //0-未开始 1-进行中 4-延期进行中 3-已完成 
                    projectName:this.FormItem.name,
                }
                this.$http.get('/project/list', { params: data })
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.size3 = data.total;
                        this.stocks = data.list;
                    }
                })
                .catch(res => {
                    this.$Message.error('获取列表失败')
                })
            },
            // 分页3
            change3(params) {
                this.index3 = params;
                this.getProjectList3(params);
            },
            // 点击切换触发的事件
            getList(){
                this.index0 = 1;
                this.index1 = 1;
                this.index2 = 1;
                this.index3 = 1;
                this.getProjectList0();
                this.getProjectList1();
                this.getProjectList2();
                this.getProjectList3();
            },
            queryProjectList(){
                var data = {
                    projectName:this.FormItem.name,
                }
                this.$http.get('/project/list-condition', { params: data })
                .then(res => {
                    var data = response.data(res, this);
                    if (data) {
                        this.size0 = data.total;
                        this.notStarted = data.list;
                        
                    }
                })
                .catch(res => {
                    this.$Message.error('获取列表失败')
                })
            },
            // 查询按钮
            queryBtn(){
                this.getProjectList0();
                this.getProjectList1();
                this.getProjectList2();
                this.getProjectList3();
            },
        } 
    }
</script>
<style scoped>
    .project-list-table{
        min-height:500px;
    }
</style>