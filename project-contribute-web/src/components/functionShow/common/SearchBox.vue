<template>
    <div class="search-box">
        <AutoComplete
        v-model="value1"
        :data="data1"
        @on-search="handleSearch1"
        placeholder="搜索 ..."
        class="search-box-input"></AutoComplete>
    </div>
</template>
<script>
    export default {
        data () {
            return {
                value1: '',
                data1: []
            }
        },
        methods: {
            handleSearch1 (value) {
                this.data1 = !value ? [] : [
                    value,
                    value + value,
                    value + value + value
                ];
            }
        }
    }
</script>
<style scoped>
.search-box{
    width:90%;
    margin:0 auto;
}
</style>