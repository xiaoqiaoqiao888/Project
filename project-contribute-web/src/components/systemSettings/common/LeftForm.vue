<template>
    <div class="work-left-box">
        <h2 class="title">系统默认角色</h2>
        <Form :model="formDynamic">
            <FormItem class="top-input-form">
                <Row>
                    <Col span="14">
                        <Input type="text" v-model="formDynamic.value" placeholder="请输入角色名称"></Input>
                    </Col>
                    <Col span="8" offset="2">
                        <Button type="primary" size="small" @click ="addRoles">添加</Button>
                    </Col>
                </Row>
            </FormItem>
            <div class="item-list-box">
                <FormItem 
                    v-for="(item,index) in formDynamic.items"
                    :key = "item.id"
                >
                    <Row>
                        <Col span="10">
                            <Tag type="dot" :class="{active: activeIndex==index}" @click.native="tagClick(item.id, index)">{{item.roleName}}</Tag>
                        </Col>
                        <Col span="4" offset="1">
                            <Button type="text" @click="deleteRole(item.id)">删除</Button>
                        </Col>
                        <!-- <Col span="4" offset="1">
                            <Button type="text">编辑</Button>
                        </Col> -->
                    </Row>
                </FormItem>
            </div>
        </Form>
    </div>
</template>
<script>
    import * as response from '../../../util/response.js'
    export default {
        data() {
            return {
                activeIndex:0,
                formDynamic: {
                    value:'',
                    items: [],
                } 
            }
        },
        components: {
        },
        mounted() {
            this.allRolesList();
        },
        created(){
    
        },
        methods: {
            // 点击角色
            tagClick(id,index){
                this.$emit('rolesListTable',id);  
                this.activeIndex = index;
            },
            // 查看所有角色
            allRolesList(){
                let data = {
                }
                this.$http.get('/sys-role')
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.formDynamic.items = data;
                        this.$emit('rolesListTable',data[0].id);
                    }
                })
                .catch(res => {
                    this.$Message.error('获取角色列表失败！')
                })
            },
            // 添加角色
            addRoles(){
                let data = new FormData();
                data.append('roleName',this.formDynamic.value);
                this.$http.post('/sys-role',data)
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                       this.allRolesList();
                       this.formDynamic.value = '';
                    }
                })
                .catch(res => {
                    this.$Message.error('获取角色列表失败！')
                })
                
            },
            // 删除角色
            deleteRole(id){
                let data = {
                    id:id, 
                }
                this.$http.delete('/sys-role', {params: data})
                .then(res => {
                    let data = response.data(res, this);
                    if (data) {
                        this.$Modal.confirm({
                            title: '删除角色',
                            content: '确认要删除角色吗？',
                            onOk: () => {
                                this.allRolesList();
                            },
                            onCancel: () => {
                                
                            }
                        });
                        
                    }
                })
                .catch(res => {
                    this.$Message.error('删除角色失败！')
                })
            },
        } 
    }
</script>
<style scoped>
.work-left-box{
    width:100%;
}
.title{
    font-size: 14px;
    letter-spacing: 0.67px;
    line-height: 28px;
    margin: 20px 0 40px 20px;
}
.top-input-form{
    padding-left:20px;
    padding-right:20px;
}
.item-list-box{
    width:100%;
    max-height:480px;
    overflow-y:auto;
    padding-left:20px;
    
}

</style>