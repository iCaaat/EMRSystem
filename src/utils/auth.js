import Vue from 'vue';

// 创建全局 Vue 实例作为事件总线
const auth = new Vue();

auth.handleUnauthorized = () => {
    auth.$emit('unauthorized'); // 触发未授权事件
};

export default auth;
