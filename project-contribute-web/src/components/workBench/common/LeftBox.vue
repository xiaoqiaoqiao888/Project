<template>
    <div class="work-left-box">
        <search-box></search-box>
        <div class="worker-list">
            <ul>
                <router-link tag="li" :to="`${item.href}`"
                v-for="(item,index) in leftNaveList"
                active-class="active"
                :class="item.iconcls"
                :key="`${item.id}`"
                >
                    <a><i :class="item.iconcls"></i>{{item.text}}</a>
                </router-link>
            </ul>
        </div>
    </div>
</template>
<script>
    import Cookie from 'js-cookie'
    import * as response from '../../../util/response.js'
    import SearchBox from '@/components/workBench/common/SearchBox'
    export default {
        data() {
            return {
                leftNaveList:[
                    // {
                    //     id: 1,
                    //     href: "/WorkBench/Project",
                    //     text: "项目"
                    // },
                    // {
                    //     id: 2,
                    //     href: "/WorkBench/Package",
                    //     text: "工程包"
                    // },
                    // {
                    //     id: 3,
                    //     href: "/WorkBench/Task",
                    //     text: "任务"
                    // },
                    // {
                    //     id: 4,
                    //     href: "/WorkBench/Check",
                    //     text: "验收"
                    // },
                ],
                num:1,
            }
        },
        components: {
            SearchBox
        },
        mounted() {
            this.getNavList();
        },
        created(){
    
        },
        methods: {
            // 获取左侧菜单列表
            getNavList() {
                // console.log(JSON.parse(Cookie.get('userInf')).roleIds)
                let data = {
                    roleIds: JSON.parse(Cookie.get('userInf')).roleIds.join(','), // 传入角色id
                }
                this.$http.get('/sys/role-resource', {params: data})
                .then(res => {
                    let data = response.data(res, this);
                    if(data){
                        for(var i=0;i<data.list.length;i++){
                            if(data.list[i].childList && data.list[i].childList[0].iconcls){
                                this.leftNaveList = data.list[i].childList; // 有iconcls字段的为我的里面的菜单
                            }else{ // 由于目前只有系统和我的的权限，所以取没有iconcls字段的为系统设置的菜单
                                localStorage.setItem('sysResource',JSON.stringify(data.list[i].childList));
                            }
                        }
                        // console.log(this.leftNaveList)
                    }
                })
                .catch(res => {
                    // console.log(res);
                    this.$Message.error('获取角色菜单权限失败！')
                })
            }
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
.worker-list li.active0 i{
    display:inline-block;
    width:20px;
    height:22px;
    background:url(../../../assets/project-icon.svg) no-repeat left center;
    background-size:cover;
    vertical-align: middle;
    margin-top:-3px;
    margin-right:15px;
}
.worker-list li.active i.active0{
    background-image:url(../../../assets/project-icon-focus.svg);
}
.worker-list li.active1 i{
    display:inline-block;
    width:22px;
    height:18px;
    background:url(../../../assets/package-icon.svg) no-repeat left center;
    background-size:cover;
    vertical-align: middle;
    margin-top:-3px;
    margin-right:15px;
}
.worker-list li.active i.active1{
    background-image:url(../../../assets/package-icon-focus.svg);
}
.worker-list li.active2 i{
    display:inline-block;
    width:22px;
    height:18px;
    background:url(../../../assets/task-icon.svg) no-repeat left center;
    background-size:cover;
    vertical-align: middle;
    margin-top:-3px;
    margin-right:15px;
}
.worker-list li.active i.active2{
    background-image:url(../../../assets/task-icon-focus.svg);
}
.worker-list li.active3 i{
    display:inline-block;
    width:22px;
    height:18px;
    background:url(../../../assets/check-icon.svg) no-repeat left center;
    background-size:cover;
    vertical-align: middle;
    margin-top:-3px;
    margin-right:15px;
}
.worker-list li.active i.active3{
    background-image:url(../../../assets/check-icon-focus.svg);
}
.worker-list li.active a{
    color:#FF9E22;
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