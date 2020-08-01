<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
// import "element-ui/lib/theme-chalk/index.css";
import qs from "querystring";
export default {
  name: "app",
  components: {},
  data() {
    return {
      idsTicket: "",
      wechatCode: "",
    };
  },
  methods: {
    initAxios() {
      // 初始化 Axios 配置
      this.$axios.defaults.baseURL = "http://paranoid-root.com/api/";

      // this.$axios.defaults.baseURL = "https://nei.netease.com/api/apimock-v2/235dc61cfe0d6bae8247d2031431814a/";

      // this.$axios.defaults.baseURL = "localhost:18080";
      this.$axios.defaults.headers["x-api-token"] = this.$store.state.token;
    },
    getUserInfo() {},
    // async login() {
    //   // 对接 CAS-We-Can 或者统一身份认证的流程示例，按需修改
    //   // let loading = this.$loading({ fullscreen: true });
    //   if (window.location.search) {
    //     // 如果当前页面参数携带了ticket
    //     let ticket = qs.parse(window.location.search.replace("?", "")).ticket;
    //     if (ticket) {
    //       // 包含 ticket，开启登录流程
    //       let res = await this.$axios.post("/auth", {
    //         ticket,
    //         service: "https://xgbxscwx.seu.edu.cn",
    //         platform: this.$device.platform()
    //       });
    //       if (res.data.success) {
    //         this.$store.commit("token", res.data.result);
    //         window.location.search = "";
    //         return; // 很重要，不要进入后面的流程
    //       } // 执行到此处说明ticket无效，给他清理了就是了
    //     }
    //     window.location.search = "";
    //   }
    //   // let res = await this.$axios.get("/user");
    //   // if (!res.data.success) {
    //   //   // 如果获取失败，就说明 token 无效了
    //   //   // 首先保存当前路由
    //   //   // console.log(this.$route)
    //   //   this.$store.commit('saveUnfinishedRoute', {
    //   //     params:this.$route.params,
    //   //     fullPath:this.$route.fullPath
    //   //     })
    //   //   if(this.$device.isWechat){
    //   //     window.location = `https://xgbxscwx.seu.edu.cn/cas-we-can/login?goto=https://xgbxscwx.seu.edu.cn`
    //   //   } else {
    //   //     window.location = `https://newids.seu.edu.cn/authserver/login?goto=https://xgbxscwx.seu.edu.cn`
    //   //   }
    //   // } else if (this.$store.state.hasUnfinishedRoute) {
    //   //   // token 有效，并且还有未完成的路由
    //   //   loading.close()
    //   //   // console.log(this.$store.state.unfinishedRoute)
    //   //   this.$router.push({path:this.$store.state.unfinishedRoute.fullPath, params:this.$store.state.unfinishedRoute.params})
    //   //   this.$store.commit('clearUnfinishedRoute')
    //   // } else {
    //   //   loading.close()
    //   // }
    // }
  },
  async created() {
    this.initAxios();
    if (this.$store.state.hasLogin) {
      let user;
      try {
        let res = await this.$axios.get("/account/information");
        if (res.data.code === 200) {
          user = res.data.data;
        } else {
          throw res.data.msg;
        }
      } catch (err) {
        if (typeof err === "string") {
          this.$message.error(err);
        } else {
          console.log(err);
          this.$message.error("网络错误");
        }
        this.$store.commit("clearCache");
        window.location.reload();
      }
      this.$store.commit("user", user);
      if (user.authority === 2) {
         this.$store.commit("isAdmin", true);
      }
    }
  },
};
</script>
