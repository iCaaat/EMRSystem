import Vue from 'vue'
import VueRouter from 'vue-router'
// 视图组价
import AppContainer from "@/views/AppContainer.vue";
import Cookies from "js-cookie";

Vue.use(VueRouter)


const routes = [
    // ==========登录==========
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login/Login.vue')
  },

    // ==========主页=========
  {
    path: '/',
    name: 'h',
    component: AppContainer,
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'home',
        component: () => import('@/views/home/MainPage.vue')
      },
      {
        path: 'users',
        name: 'users',
        component: () => import('@/views/user/UserTable.vue')
      },
      {
        path: 'AddUser',
        name: 'AddUser',
        component: () => import('@/views/user/AddUser.vue')
      },
      {
        path: 'PersonalCenter',
        name: 'PersonalCenter',
        component: () => import('@/views/person/PersonalCenter.vue')
      },
      {
        path: 'AppMain',
        name: 'AppMain',
        component: () => import('@/components/AppMain.vue')
      }
    ]
  },

    // ==========404页面=========
  {
    path: '*',
    component: () => import('@/views/404.vue')
  }

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    return next();
  }
  const userCookie = Cookies.get('user');
  if (!userCookie && to.path !== '/login') {
    return next('/login');
  }
  next();
})


export default router
