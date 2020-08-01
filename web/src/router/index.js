import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  // {
  //   path: '/',
  //   name: 'welcome',
  //   component: () => import(/* webpackChunkName: "about" */ '../views/Home.vue')
  // },
  // 主页
  {
    path: '/',
    name: 'home',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName*/'../views/Home.vue')
  },
  {
    path: '/user',
    redirect: '/user/info',
    component: () => import("../views/User.vue"),
    children: [
      {
        path: 'info',
        component: () => import("../components/account/UserInfo.vue")
      },
      {
        path: 'collections',
        component: () => import("../components/Collection.vue")
      },
      {
        path: 'unhandleRequest',
        component: () => import("../components/admin/UnhandleRequest.vue")
      }
    ]
  },
  {
    path: '/user',
    // redirect: '/user/info',
    component: () => import("../views/User.vue"),

  }
]

const router = new VueRouter({
  routes
})

export default router
