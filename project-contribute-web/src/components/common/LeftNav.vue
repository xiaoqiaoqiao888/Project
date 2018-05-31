<template>
    <div class="left-nav-box">
        <ul>
            <router-link tag="li" :to="`${item.path}`"
            v-for="(item,index) in leftNaveList"
            exact-active-class="active"
            :class=" 'active' + index"
            :key="`${item.id}`"
            >
                <a>
                    <dl>
                        <dt :class=" 'active' + index"></dt>
                        <dd>{{item.text}}</dd>
                    </dl>
                </a>
            </router-link>
            <!-- <li class="active">
                <router-link to="Project">
                    <dl>
                        <dt class="me-icon"></dt>
                        <dd>我的</dd>
                    </dl>
                </router-link>
            </li>
            <li>
                <router-link to="Package">
                    <dl>
                        <dt class="our-icon"></dt>
                        <dd>我们的</dd>
                    </dl>
                </router-link>
            </li>
            <li>
                <router-link to="">
                    <dl>
                        <dt class="core-icon"></dt>
                        <dd>我的重心</dd>
                    </dl>
                </router-link>
            </li> -->
        </ul>
        <div class="setting-btn"><router-link to=""></router-link></div>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                leftNaveList:[
                    {
                        id: 1,
                        path: "/WorkBench/Project",
                        text: "我的"
                    },
                    {
                        id: 2,
                        path: "/ourCapacity/AbilityList",
                        text: "我们的"
                    },
                    // {
                    //     id: 3,
                    //     path: "/2",
                    //     text: "我的重心"
                    // },
                ],
                num:1,
            }
        },
        components: {
        },
        mounted() {
    
        },
        created(){
    
        },
        methods: {
    
        } 
    }
</script>
<style scoped lang="less">
.left-nav-box{
    text-align: center;
    font-size:16px;
    a{color:#A4AAB3;}
    ul{
        padding-top:20px;
    }
    li{
        margin-top:30px;
    }
    li dt{ 
        margin:0 auto;
        margin-bottom:12px;
    }
    li.active0 dt{
        width:22px;
        height:25px;
        background:url(../../assets/me-icon.svg) no-repeat;
        background-size:cover;
    }
    li.active dt.active0{
        background-image:url(../../assets/me-icon-focus.svg);
    }
    li.active1 dt{
        width:30px;
        height:25px;
        background:url(../../assets/our-icon.svg) no-repeat;
        background-size:cover;
    }
    li.active dt.active1{
        background-image:url(../../assets/our-icon-focus.svg);
    }
    li.active2 dt{
        width:28px;
        height:25px;
        background:url(../../assets/core-icon.svg) no-repeat;
        background-size:cover;
    }
    li.active dt.active2{
        background-image:url(../../assets/core-icon-focus.svg);
    }
    .setting-btn a{
        display:block;
        width:27px;
        height:26px;
        background:url(../../assets/Settings-Icon.svg) no-repeat;
        background-size:cover;
        position:absolute;
        bottom:30px;
        left:50%;
        margin-left:-13.5px;
        
    }
}

</style>