<template>
  <div id="head" style="position:fixed">
    <loginModal
      v-if="loginVisible"
      v-bind:visible="loginVisible"
      @close="closeLoginModal"
      @register="showRegisterModal"
      @reset="showResetPwdModal"
    ></loginModal>
    <registerModal
      v-if="registerVisible"
      v-bind:visible="registerVisible"
      @close="closeRegisterModal"
      @login="showLoginModal"
    ></registerModal>
    <resetPwdModal v-if="resetVisible" v-bind:visible="resetVisible" @close="closeResetPwdModal"></resetPwdModal>
    <UploadModal v-bind:visible="uploadVisible" @close="closeUploadModal"></UploadModal>
    <StyleModal v-bind:visible="styleVisible" @closeStyleModal="closeStyleModal"></StyleModal>
    <GIFModal v-bind:visible="gifVisible" @closeGIFModal="closeGIFModal"></GIFModal>
    <a-row
      style="background-color:#5e7987;width:100%;margin:0;padding:0;left:0;right:0;position:fixed"
    >
      <a-col :span="4" style="color:rgba(255, 255, 255,0.85); font-size: 28px;text-align:center">
        <span style="cursor:pointer;" @click="backHome">ProPainting</span>
      </a-col>
      <a-col :span="3">
        <a-select default-value="1" style="width: 80%" @change="handleChange">
          <a-select-option value="1">经典画作</a-select-option>
          <a-select-option value="2">绘画参考</a-select-option>
          <a-select-option value="3">图集</a-select-option>
          <a-select-option value="4">3D模型</a-select-option>
        </a-select>
      </a-col>
      <a-col :span="8">
        <a-input-search placeholder="请输入你想查找的图片信息" style="width: 70%" @search="onSearch" />
      </a-col>
      <a-col :span="9" style="text-align:right">
        <div v-if="hasLogin" class="icons-list" style="vertical-align:-0.5em">
          <icon-font
            type="iconstyle"
            style="color:rgba(255, 255, 255,0.85);fontSize:28px;vertical-align:-0.25em"
            @click="showStyleModal"
            title="图像风格迁移"
          ></icon-font>&nbsp;
          <icon-font
            type="icondonghua"
            style="color:rgba(255, 255, 255,0.85);fontSize:28px;vertical-align:-0.25em"
            @click="showGIFModal"
            title="动图生成"
          ></icon-font>
          <a-icon
            type="upload"
            style="color:rgba(255, 255, 255,0.85);fontSize:28px;vertical-align:-0.25em"
            @click="showUploadModal"
            title="上传图片"
          ></a-icon>&nbsp;
          <!-- <i style="font-size: 28px; vertical-align: -0.125em;" title="注销">
            <svg
              t="1595161757002"
              class="icon"
              style="height:28px;width:28px;cursor:pointer;"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="2343"
              id="mx_n_1595161757003"
              data-spm-anchor-id="a313x.7781069.0.i3"
              @click="logout"
            >
              <path
                d="M918.4 489.6l-160-160c-12.8-12.8-32-12.8-44.8 0s-12.8 32 0 44.8l105.6 105.6H512c-19.2 0-32 12.8-32 32s12.8 32 32 32h307.2l-105.6 105.6c-12.8 12.8-12.8 32 0 44.8 6.4 6.4 12.8 9.6 22.4 9.6s16-3.2 22.4-9.6l160-163.2s0-3.2 3.2-3.2c9.6-9.6 9.6-28.8-3.2-38.4zM832 736c-19.2 0-32 12.8-32 32v64c0 19.2-12.8 32-32 32H224c-19.2 0-32-12.8-32-32V192c0-19.2 12.8-32 32-32h544c19.2 0 32 12.8 32 32v64c0 19.2 12.8 32 32 32s32-12.8 32-32V192c0-54.4-41.6-96-96-96H224C169.6 96 128 137.6 128 192v640c0 54.4 41.6 96 96 96h544c54.4 0 96-41.6 96-96v-64c0-19.2-12.8-32-32-32z"
                p-id="2344"
                fill="#E6E6E6"
              />
            </svg>
          </i>-->
          <a-dropdown>
            <span class="user">
              <a-avatar :size="avatarSize" :src="this.user.avatar" @click="userClick" title="用户信息"></a-avatar>
              <svg
                viewBox="0 0 1024 1024"
                focusable="false"
                class
                data-icon="caret-down"
                width="1em"
                height="1em"
                aria-hidden="true"
              >
                <path
                  d="M840.4 300H183.6c-19.7 0-30.7 20.8-18.5 35l328.4 380.8c9.4 10.9 27.5 10.9 37 0L858.9 335c12.2-14.2 1.2-35-18.5-35z"
                  fill="#E6E6E6"
                />
              </svg>
            </span>
            <a-menu slot="overlay" :arrow="arrow">
              <a-menu-item @click="toUserInfo">个人信息</a-menu-item>
              <a-menu-item @click="toCollections">我的收藏</a-menu-item>
              <a-divider style="margin:0 0;"></a-divider>
              <a-menu-item @click="logout">注销账户</a-menu-item>
            </a-menu>
          </a-dropdown>
        </div>
        <div v-else style="font-size:20px;margin-right:30px;">
          <button @click="showLoginModal" class="loginBtn" type="dashed" ghost>登录</button>&nbsp;
          <button @click="showRegisterModal" class="registerBtn" type="dashed" ghost>注册</button>
        </div>
      </a-col>
    </a-row>
    <a-row
      v-if="flag"
      style="top:64px;width:100%;margin:0;padding:0;left:0;right:0;position:fixed;"
    >
      <a-menu class="choose" v-model="current" mode="horizontal" @click="handleClick">
        <a-menu-item key="1">经典画作</a-menu-item>
        <a-menu-item key="2">绘画参考</a-menu-item>
        <a-menu-item key="3">3D模型</a-menu-item>
        <a-menu-item key="4">图集</a-menu-item>
      </a-menu>
    </a-row>
  </div>
</template>
<script>
import { Icon } from "ant-design-vue";
import LoginModal from "../components/account/LoginModal";
import RegisterModal from "../components/account/RegisterModal";
import ResetPwdModal from "../components/account/ResetPwdModal";
import UploadModal from "../components/UploadModal";
import StyleModal from "../components/StyleModal";
import GIFModal from "../components/GIFModal";
const IconFont = Icon.createFromIconfontCN({
  scriptUrl: "//at.alicdn.com/t/font_1952755_hyzl2v7r97i.js",
});
export default {
  name: "Header",
  props: {
    flag: {
      type: Boolean,
      default: true,
    },
  },
  components: {
    loginModal: LoginModal,
    registerModal: RegisterModal,
    resetPwdModal: ResetPwdModal,
    UploadModal,
    IconFont,
    StyleModal,
    GIFModal,
  },
  data() {
    return {
      toLogin: false,
      current: ["1"],
      loginVisible: false,
      registerVisible: false,
      resetVisible: false,
      uploadVisible: false,
      styleVisible: false,
      gifVisible: false,
      user: {},
      avatarSize: 48,
      arrow: true,
      url: "/picture/master/outline",
      selectKey: "1",
      not3D: true,
      notCollection: true,
      res: [],
      echartsOption: {},
    };
  },
  computed: {
    hasLogin() {
      return this.$store.state.hasLogin;
    },
  },
  methods: {
    toUserInfo() {
      this.$router.push("/user/info");
    },
    toCollections() {
      this.$router.push({
        path: "/user/collections",
        query: { id: "/collection/collection" },
      });
    },
    backHome() {
      this.$router.push("/");
    },
    showLoginModal() {
      this.loginVisible = true;
      this.registerVisible = false;
    },
    closeLoginModal() {
      this.loginVisible = false;
    },
    showRegisterModal() {
      this.loginVisible = false;
      this.registerVisible = true;
    },
    closeRegisterModal() {
      this.registerVisible = false;
    },
    showResetPwdModal() {
      this.loginVisible = false;
      this.resetVisible = true;
    },
    closeResetPwdModal() {
      this.resetVisible = false;
    },
    showUploadModal() {
      this.uploadVisible = true;
    },
    closeUploadModal() {
      this.uploadVisible = false;
    },
    showStyleModal() {
      this.styleVisible = true;
    },
    closeStyleModal() {
      this.styleVisible = false;
    },
    showGIFModal() {
      this.gifVisible = true;
    },
    closeGIFModal() {
      this.gifVisible = false;
    },
    onSearch(value) {
      if (this.selectKey === "1") {
        this.url = "/picture/master/outline";
        this.not3D = true;
        this.notCollection = true;
      } else if (this.selectKey === "2") {
        this.url = "/picture/refer/outline";
        this.not3D = true;
        this.notCollection = true;
      } else if (this.selectKey === "3") {
        this.url = "/collection/outline";
        this.not3D = true;
        this.notCollection = false;
      } else if (this.selectKey === "4") {
        this.url = "/3dmodel";
        this.not3D = false;
        this.notCollection = true;
      }
      this.$emit("onSearch", {
        url: this.url,
        query: value,
        not3D: this.not3D,
        notCollection: this.notCollection,
      });
    },
    handleClick(e) {
      console.log(e);
      if (e.key === "1") {
        this.url = "/picture/master/outline";
        this.not3D = true;
        this.notCollection = true;
      } else if (e.key === "2") {
        this.url = "/picture/refer/outline";
        this.not3D = true;
        this.notCollection = true;
      } else if (e.key === "3") {
        this.url = "/3dmodel";
        this.not3D = false;
        this.notCollection = true;
      } else if (e.key === "4") {
        this.url = "/collection/outline";
        this.not3D = true;
        this.notCollection = false;
      }
      this.$emit("onSearch", {
        url: this.url,
        query: "",
        not3D: this.not3D,
        notCollection: this.notCollection,
      });
    },
    handleChange(key) {
      this.selectKey = key;
    },
    userClick() {
      this.$router.push("/user");
    },
    logout() {
      this.$store.commit("clearCache");
      window.location.reload();
    },
  },
  async created() {
    this.user = this.$store.state.user;
  },
};
</script>
<style>
/* .choose {
  display: none;
}
#head:hover .choose {
  display: block;
} */
/* #head {
  background-color: #5a5a5a;
} */
.icons-list >>> .anticon {
  color: rgba(255, 255, 255, 0.85);
  margin-right: 6px;
  font-size: 24px;
}
.user {
  margin-right: 30px;
  margin-left: 10px;
  cursor: pointer;
  display: inline-block;
  text-align: center;
  width: 100px;
}
.user:hover {
  background-color: #6495ed;
}
.registerBtn {
  background-color: #e2e1e4;
  background: linear-gradient(
    rgba(226, 225, 228, 0.85),
    rgba(205, 209, 211, 0.85)
  );
  color: #5a5a5a;
  outline: none;
  font-size: 18px;
  font-weight: bolder;
  border-style: solid;
  border-width: 1px;
  line-height: 30px;
  /* box-shadow: inset 0px 1px 0px rgba(255, 255, 255, 0.3),
    0 1px 2px rgba(0, 0, 0, 0.15); */
  border-radius: 200px;
  font-weight: 300;
  text-align: center;
  height: 40px;
  padding: 0 25px;
  appearance: none;
  cursor: pointer;
  border: none;
  box-sizing: border-box;
  transition-property: all;
  transition-duration: 0.3s;
}
.registerBtn:active {
  /* border-color: #8bc220;
  background: #a1d243; */
  color: #ffffff;
  text-shadow: 0 1px 0 rgba(255, 255, 255, 0.3);
  text-decoration: none;
  transition-duration: 0s;
  border-style: solid;
  border-width: 1px;
  line-height: 30px;
  border-radius: 200px;
}
.loginBtn {
  outline: none;
  font-size: 18px;
  background: rgba(52, 165, 248, 0.85);
  border-color: #088ef0;
  border-style: solid;
  color: rgba(255, 255, 255, 0.75);
  border-width: 1px;
  line-height: 30px;
  /* box-shadow: inset 0px 1px 0px rgba(255, 255, 255, 0.3),
    0 1px 2px rgba(0, 0, 0, 0.15); */
  border-radius: 200px;
  font-weight: 300;
  text-align: center;
  height: 40px;
  padding: 0 25px;
  appearance: none;
  cursor: pointer;
  border: none;
  box-sizing: border-box;
  transition-property: all;
  transition-duration: 0.3s;
}
.loginBtn:active {
  border-color: #0880d7;
  background: #2798eb;
  color: #0880d7;
  text-shadow: 0 1px 0 rgba(255, 255, 255, 0.3);
  text-decoration: none;
  transition-duration: 0s;
  border-style: solid;
  border-width: 1px;
  line-height: 30px;
  border-radius: 200px;
}
</style>