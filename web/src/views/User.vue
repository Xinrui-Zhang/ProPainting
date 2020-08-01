  <template>
  <div style="height:100%;">
    <div class="container" v-if="meteor">
      <div id="mask"></div>
      <div id="sky"></div>
    </div>
    <imageDetail :visible="imageDetailVisible" :pid="pid" @close="closeImageDetail"></imageDetail>
    <keep-alive>
      <a-layout id="user">
        <a-layout-header :style="{zIndex: 1, width: '100%'}">
          <Header :flag="headerFlag" />
        </a-layout-header>
        <a-layout>
          <!-- <a-layout-sider style="background-color:#FFF;" :width="siderWidth"> -->
          <a-layout-sider
            style="background-color:#FFF;border:0.5px solid #E8E8E8;"
            :width="siderWidth"
          >
            <a-menu @click="onSelect" :selectedKeys="selectedKeys" mode="inline" class="sider">
              <a-menu-item
                key="/info"
                style="height: 60px;line-height: 60px;font-size: 20px;font-weight: bold;text-align:center;"
              >个人信息</a-menu-item>
              <a-menu-item
                key="/collections"
                style="height: 60px;line-height: 60px;font-size: 20px;font-weight: bold;text-align:center;"
              >已创建图集</a-menu-item>
              <a-menu-item
                key="/collection/collection"
                style="height: 60px;line-height: 60px;font-size: 20px;font-weight: bold;text-align:center;"
              >收藏图集</a-menu-item>
              <a-menu-item
                key="/unhandleRequest"
                style="height: 60px;line-height: 60px;font-size: 20px;font-weight: bold;text-align:center;"
                v-if="this.$store.state.isAdmin"
              >未处理申诉</a-menu-item>
            </a-menu>
          </a-layout-sider>
          <a-layout style="background:white">
            <a-layout-content :style="contentStyle">
              <!-- <userInfo v-if="selectedKeys[0] === '/info'" /> -->
              <!-- <Picture v-if="selectedKeys[0] === '/collections'" /> -->
              <router-view></router-view>
            </a-layout-content>
          </a-layout>
        </a-layout>
      </a-layout>
    </keep-alive>
  </div>
</template>
<script>
import ImageDetail from "../components/ImageDetail";
import Header from "../components/Header";
import Picture from "../components/Picture";
import Collection from "../components/Collection";
import { stars, meteors, addChild, requestAnimation } from "../utils/meteor";
export default {
  name: "Home",
  components: {
    imageDetail: ImageDetail,
    Header,
    Picture,
    Collection
  },
  data() {
    return {
      screenWidth: document.body.clientWidth,
      screenHeight: document.body.clientHeight,
      device: { isDesktop: true, isMobile: true, isWechat: false },
      siderWidth: "250px",
      imageDetailVisible: false,
      pid: "this is a pid",
      headerFlag: false,
      selectedKeys: [
        this.$route.path.substr(this.$route.path.lastIndexOf("/")),
      ],
      timer: undefined,
      meteor:
        this.$route.path.substr(this.$route.path.lastIndexOf("/")) === "/info",
      // user: {
      //   uid: "111",
      //   name: "Vino Li",
      //   avatar:
      //     "https://static.myseu.cn/avatar1.jpg",
      //   email: "213183580@seu.edu.cn",
      //   phone: "18851101635",
      //   contribution: 1123,
      //   points: 5589,
      //   authority: "1",
      //   uploads: 9
      // },
      contentStyle: "margin:16px 16px;",
      _this: this,
    };
  },
  watch: {
    "$route.path": function (newV, oldV) {
      this.meteor =
        this.$route.path.substr(this.$route.path.lastIndexOf("/")) === "/info";
      if (this.meteor) {
        this.timer = setInterval(() => {
          try {
            meteors();
          } catch (err) {
            clearInterval(this.timer);
          }
        }, 1000);
      } else {
        clearInterval(this.timer);
      }
    },
  },
  methods: {
    onSelect({ key, keyPath }) {
      if(key == "/collections"){
        this.$router.push({path:"/user/collections",query:{id:"/collection"}})
      }
      else if(key == "/collection/collection"){
        this.$router.push({path:"/user/collections",query:{id:"/collection/collection"}})
      }
      else {
        this.$router.push("/user" + key);
      }      
      this.selectedKeys = [key];
    },
    closeImageDetail() {
      this.imageDetailVisible = false;
    },
    showImageDetail() {
      this.imageDetailVisible = true;
    },
  },
  created() {
    if (this.meteor) {
      this.timer = setInterval(() => {
        try {
          meteors();
        } catch (err) {
          clearInterval(this.timer);
        }
      }, 1000);
    } else {
      clearInterval(this.timer);
    }
  },
  mounted() {
    const that = this;
    window.onresize = () => {
      return (() => {
        window.screenWidth = document.body.clientWidth;
        that.screenWidth = window.screenWidth;
        window.screenHeight = document.body.clientHeight;
        that.screenHeight = window.screenHeight;
      })();
    };
  },
};
</script>
<style>
#user .logo {
  width: 120px;
  height: 31px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px 24px 16px 0;
  float: left;
}
#title {
  color: rgba(255, 255, 255, 0.85);
}
.container {
  position: relative;
  height: 100%;
}
/* 遮罩层 */

#mask {
  position: absolute;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  z-index: 900;
}
/* 天空背景 */

#sky {
  width: 100%;
  height: 100%;
}
/* 流星 */

#sky .star {
  display: block;
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: #fff;
  top: 100px;
  left: 400px;
  position: absolute;
  transform-origin: 100% 0;
  animation: star-ani 6s infinite ease-out;
  box-shadow: 0 0 5px 5px rgba(255, 255, 255, 0.3);
  opacity: 0;
  z-index: 2;
}

.star::after {
  /* content: "";
  display: block;
  border: solid;
  border-width: 2px 0 2px 80px;
  border-color: transparent transparent transparent rgba(255, 255, 255, 1);
  border-radius: 2px 0 0 2px;
  transform: rotate(-45deg);
  transform-origin: 0 0 0;
  box-shadow: 0 0 20px rgba(255, 255, 255, 0.3); */
  content: "";
  display: block;
  top: 0px;
  left: 4px;
  border: 0px solid #fff;
  border-width: 0px 90px 2px 90px;
  border-color: transparent transparent transparent rgba(255, 255, 255, 0.3);
  transform: rotate(-45deg) translate3d(1px, 3px, 0);
  box-shadow: 0 0 1px 0 rgba(255, 255, 255, 0.1);
  transform-origin: 0% 100%;
  animation: shooting-ani 3s infinite ease-out;
}
/* .pink {
  background: #ff5a99;
}
.pink:after {
  border-color: transparent transparent transparent #ff5a99;
} */
.pink {
  top: 30px;
  left: 395px;
  background: #ff5a99;
  animation-delay: 5s;
  -webkit-animation-delay: 5s;
  -moz-animation-delay: 5s;
}
.pink:after {
  border-color: transparent transparent transparent #ff5a99;
  animation-delay: 5s;
  -webkit-animation-delay: 5s;
  -moz-animation-delay: 5s;
}
.blue {
  background: cyan;
}
.blue:after {
  border-color: transparent transparent transparent cyan;
}
.yellow {
  background: #ffcd5c;
}
.yellow:after {
  border-color: transparent transparent transparent #ffcd5c;
}
</style>