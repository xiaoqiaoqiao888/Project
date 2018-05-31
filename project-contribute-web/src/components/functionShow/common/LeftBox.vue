<template>
    <div class="work-left-box">
        <search-box></search-box>
        <div class="worker-list">
            <ul>
                <li
                    v-for="(item,index) in leftNaveList"
                    :class="{'active': activeIndex == index}"
                    :key="`${item.id}`"
                    @click="selectGroup(item.id, index)"
                >
                    <a><i></i>{{item.text}}</a>
                </li>
            </ul>
        </div>
    </div>
</template>
<script>
    import * as response from '../../../util/response.js'
    import SearchBox from '@/components/workBench/common/SearchBox'
    export default {
        data() {
            return {
                leftNaveList: [],
                activeIndex: 0,
            }
        },
        components: {
            SearchBox
        },
        mounted() {
            this.getGroupList();
        },
        created(){
    
        },
        methods: {
            // 获取职能组列表
            getGroupList() {
                let dataJson = {
                    groupLevel: 2
                };
                this.$http.post('/sys-group/list', dataJson)
                .then(res => {
                    let data = response.data(res, this);
                    this.leftNaveList = [];
                    if (data) {
                        for(var i=0;i<data.length;i++) {
                            this.leftNaveList.push({
                                text: data[i].groupName,
                                id: data[i].id,
                            })
                        }
                        this.$emit('refreshTable', data[0].id)
                    }
                })
                .catch(res => {
                    this.$Message.error('获取职能组列表失败！')
                })
            },
            // 选择部门组
            selectGroup(id, index) {
                this.$emit('refreshTable', id)
                this.activeIndex = index;
            },
        } 
    }
</script>
<style scoped>
.work-left-box{
    width:100%;
}
.worker-list li{
    position:relative;
}
.worker-list li a{
    width:100%;
    padding:20px 10%;
    line-height: 25px;
}
.worker-list li i{
    display:inline-block;
    width:4px;
    height:4px;
    background:#aaa;
    vertical-align: middle;
    margin-right:20px;
}
.worker-list li.active a{
    color:#FF9E22;
}
.worker-list li.active i{
    background:#FF9E22;
}
.worker-list li.active::before{
    content: '';
    display:block;
    width:3px;
    height:25px;
    background:#FF9E22 ;
    position:absolute;
    right:0;
    top: 50%;
    margin-top:-12.5px;

}
</style>