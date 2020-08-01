<template>
  <div>
    <div class="userInfo">
      <div class="name_email">
        <p class="userName">{{this.user.name}}</p>
        <p class="userEmail">
          <span style="color:rgba(0,0,0,0.85)">电子邮箱：</span>
          {{this.user.email}}
        </p>
        <p class="userPhone">
          <span style="color:rgba(0,0,0,0.85)">手机号：</span>
          {{this.user.phone}}
        </p>
      </div>

      <a-card class="card">
        <a-statistic title="贡献度" :value="this.user.contribution" class="statistic"></a-statistic>
      </a-card>
      <a-card class="card">
        <a-statistic title="积分" :value="this.user.points" class="statistic"></a-statistic>
      </a-card>
    </div>
    <a-divider>
      <HoverMask @click="showAvatar" style="border-radius:48px">
        <a-avatar :size="size" :src="this.user.avatar"></a-avatar>
        <template v-slot:action>
          <svg
            viewBox="64 64 896 896"
            data-icon="tool"
            width="15px"
            height="15px"
            fill="#FFF"
            aria-hidden="true"
            focusable="false"
            class
          >
            <path
              d="M876.6 239.5c-.5-.9-1.2-1.8-2-2.5-5-5-13.1-5-18.1 0L684.2 409.3l-67.9-67.9L788.7 169c.8-.8 1.4-1.6 2-2.5 3.6-6.1 1.6-13.9-4.5-17.5-98.2-58-226.8-44.7-311.3 39.7-67 67-89.2 162-66.5 247.4l-293 293c-3 3-2.8 7.9.3 11l169.7 169.7c3.1 3.1 8.1 3.3 11 .3l292.9-292.9c85.5 22.8 180.5.7 247.6-66.4 84.4-84.5 97.7-213.1 39.7-311.3zM786 499.8c-58.1 58.1-145.3 69.3-214.6 33.6l-8.8 8.8-.1-.1-274 274.1-79.2-79.2 230.1-230.1s0 .1.1.1l52.8-52.8c-35.7-69.3-24.5-156.5 33.6-214.6a184.2 184.2 0 0 1 144-53.5L537 318.9a32.05 32.05 0 0 0 0 45.3l124.5 124.5a32.05 32.05 0 0 0 45.3 0l132.8-132.8c3.7 51.8-14.4 104.8-53.6 143.9z"
            />
          </svg>
          <span style="font-size:15px;">修改头像</span>
        </template>
      </HoverMask>
    </a-divider>
    <div class="myUploads">
      <h3 style="margin-bottom:20px;">
        <span class="title">我的上传</span>
        <span class="count">{{this.user.uploads}}</span>
      </h3>
      <Picture />
    </div>
    <ResetAvatar v-bind:visible="avatarVisible" @close="closeAvatar" :img="this.user.avatar"></ResetAvatar>
  </div>
</template>
<script>
import Picture from "../Picture";
import HoverMask from "../utils/HoverMask";
import ResetAvatar from "./ResetAvatar";
export default {
  name: "UserInfo",
  components: {
    Picture,
    HoverMask,
    ResetAvatar,
  },
  data() {
    return {
      size: 96,
      screenWidth: document.body.clientWidth,
      screenHeight: document.body.clientHeight,
      user: {},
      avatarVisible: false,
    };
  },
  methods: {
    closeAvatar() {
      this.avatarVisible = false;
    },
    showAvatar() {
      this.avatarVisible = true;
    },
  },
  mounted() {
    const that = this;
    window.onresize = () => {
      return (() => {
        window.screenWidth = document.body.clientWidth;
        that.screenWidth = window.screenWidth;
        window.screenHeight = document.body.clientHeight;
        that.screenHeight = window.screenHeight;
        that.imageBoxStyle = `height:${0.8 * window.screenHeight}px`;
      })();
    };
  },
  created() {
    this.user = this.$store.state.user;
  },
};
</script>
<style scoped>
.userInfo {
  margin: 30px 30px 30px 30px;
}
.myUploads {
  margin: 30px 40px 30px 50px;
}
.title {
  font-weight: bold;
  font-size: 20px;
  color: rgba(0, 0, 0, 0.65);
  display: inline-block;
}
.count {
  background: #f7f7f7;
  border: 1px solid #ddd;
  border-radius: 3px;
  color: #777;
  display: inline-block;
  font-size: 12px;
  line-height: 18px;
  height: 20px;
  margin-left: 11px;
  padding: 0 5px;
  position: relative;
  vertical-align: middle;
}
.count::before {
  background-image: url(//s1.hdslb.com/bfs/static/jinkela/space/asserts/icons.png);
  position: absolute;
  background-position: -215px -340px;
  content: "";
  display: block;
  left: -7px;
  top: -3px;
  height: 22px;
  width: 10px;
}
.name_email {
  /* display: inline-block; */
  float: left;
  margin: 10px;
  margin-left: 20px;
}
.userName {
  margin-bottom: 10px;
  font-size: 25px;
  font-weight: bold;
}
.userEmail {
  margin: 0;
  font-size: 15px;
}
.statistics {
  /* display: inline-block; */
  float: right;
}
.card {
  float: right;
  margin-right: 20px;
  margin-bottom: 20px;
  box-shadow: 3px 3px 3px #888888;
}
</style>
