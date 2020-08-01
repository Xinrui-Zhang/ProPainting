<template >
  <div>
    <Collection
      v-if="collectionVisible"
      :visible="collectionVisible"
      @close="closeCollection"
      @collect="collect"
      @cancel="closeCollection"
      :pid="targetPid"
    ></Collection>
    <ColorModal
      v-if="colorVisible"
      :visible="colorVisible"
      :picPid="this.image.pid"
      :colorStr="this.colorStr"
      @closeColorModal="closeColorModal"
      @cancel="closeColorModal"
    ></ColorModal>
    <SubmitModal
      v-if="subVisible"
      :visible="subVisible"
      @closeSubModal="closeSubModal"
      :type="this.image.type"
      :pid="this.image.pid"
      :title="this.image.title"
    ></SubmitModal>
    <!-- <PicInfo :visible="picInfoVisible" @closePicInfo="closePicInfo" :image="this.image"></PicInfo> -->
    <a-modal
      v-if="this.screenWidth >=576"
      width="90%"
      v-model="visible"
      @cancel="handleCancel"
      centered
      :closable="closable"
      :maskClosable="maskClosable"
      :keyboard="false"
      wrapClassName="imageDetail"
      :destroyOnClose="destroyOnClose"
      :footer="null"
    >
      <p slot="title" class="title">
        <a-avatar :src="this.image.avatar"></a-avatar>
        <span style="fontSize:18px">{{this.image.artist}}</span>
        <a-button shape="circle" class="headerBtn" @click="handleCancel">
          <a-icon type="close" :style="{ fontSize: '20px'}" />
        </a-button>
      </p>
      <div class="imageBox" :style="imageBoxStyle">
        <img :src="this.image.url" class="image" />
        <!-- <img src="../assets/images/1.jpg" class="image" /> -->
      </div>
      <a-button v-if="!image.liked" @click="like(targetPid)" title="我喜欢" class="footerLeftBtn">
        <svg
          class="BWSrD"
          version="1.1"
          viewBox="0 0 32 32"
          width="20"
          height="20"
          aria-hidden="false"
          fill="currentColor"
        >
          <path
            d="M17.4 29c-.8.8-2 .8-2.8 0l-12.3-12.8c-3.1-3.1-3.1-8.2 0-11.4 3.1-3.1 8.2-3.1 11.3 0l2.4 2.8 2.3-2.8c3.1-3.1 8.2-3.1 11.3 0 3.1 3.1 3.1 8.2 0 11.4l-12.2 12.8z"
          />
        </svg>
      </a-button>
      <a-button v-if="image.liked" @click="cancelLike(targetPid)" title="不喜欢" class="footerLeftBtn">
        <svg
          class="BWSrD"
          version="1.1"
          viewBox="0 0 32 32"
          width="20"
          height="20"
          aria-hidden="false"
        >
          <path
            d="M17.4 29c-.8.8-2 .8-2.8 0l-12.3-12.8c-3.1-3.1-3.1-8.2 0-11.4 3.1-3.1 8.2-3.1 11.3 0l2.4 2.8 2.3-2.8c3.1-3.1 8.2-3.1 11.3 0 3.1 3.1 3.1 8.2 0 11.4l-12.2 12.8z"
            fill="#FF1493"
          />
        </svg>
      </a-button>
      <a-button v-if="!image.collected" @click="showCollection" title="收藏" class="footerLeftBtn">
        <svg
          class="collect"
          version="1.1"
          viewBox="0 0 32 32"
          width="20"
          height="20"
          aria-hidden="false"
          fill="currentColor"
        >
          <path d="M14 3h4v26h-4zM29 14v4h-26v-4z" />
        </svg>
      </a-button>
      <a-button
        v-if="image.collected"
        @click="cancelCollect(targetPid)"
        title="取消收藏"
        class="footerLeftBtn"
      >
        <svg
          class="collect"
          version="1.1"
          viewBox="0 0 32 32"
          width="20"
          height="20"
          aria-hidden="false"
        >
          <path d="M14 3h4v26h-4zM29 14v4h-26v-4z" fill="#00A1D6" />
        </svg>
      </a-button>
      <a-button title="下载" class="footerLeftBtn">
        <a :href="image.url" download style="color:#5a5a5a">
          <a-icon type="download" style="fontSize:20px"></a-icon>
        </a>
      </a-button>
      <a-button title="图片信息" class="footerRightBtn" @click="showPicInfo">
        <svg
          t="1595387347162"
          class="icon"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="4557"
          width="20"
          height="20"
          fill="currentColor"
        >
          <path
            d="M694.857143 768v73.142857q0 14.857143-10.857143 25.714286t-25.714286 10.857143H365.714286q-14.857143 0-25.714286-10.857143t-10.857143-25.714286v-73.142857q0-14.857143 10.857143-25.714286t25.714286-10.857143h36.571428V512h-36.571428q-14.857143 0-25.714286-10.857143t-10.857143-25.714286V402.285714q0-14.857143 10.857143-25.714285t25.714286-10.857143h219.428571q14.857143 0 25.714286 10.857143t10.857143 25.714285v329.142857h36.571428q14.857143 0 25.714286 10.857143t10.857143 25.714286zM621.714286 109.714286v109.714285q0 14.857143-10.857143 25.714286t-25.714286 10.857143H438.857143q-14.857143 0-25.714286-10.857143t-10.857143-25.714286V109.714286q0-14.857143 10.857143-25.714286t25.714286-10.857143h146.285714q14.857143 0 25.714286 10.857143t10.857143 25.714286z"
            p-id="4558"
          />
        </svg>
        <span class="text">Info</span>
      </a-button>
      <a-dropdown placement="topLeft">
        <a-button title="更多" class="footerRightBtn">
          <svg
            t="1595387210636"
            class="icon"
            viewBox="0 0 1024 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="3697"
            width="20"
            height="20"
            fill="currentColor"
          >
            <path
              d="M147.01175 430.890704c-44.791136 0-81.108273 36.303834-81.108273 81.109296 0 44.778856 36.316114 81.108273 81.108273 81.108273 44.792159 0 81.109296-36.329417 81.109296-81.108273C228.121046 467.194538 191.804932 430.890704 147.01175 430.890704zM511.999488 430.890704c-44.791136 0-81.108273 36.303834-81.108273 81.109296 0 44.778856 36.316114 81.108273 81.108273 81.108273 44.792159 0 81.109296-36.329417 81.109296-81.108273C593.108784 467.194538 556.791647 430.890704 511.999488 430.890704zM876.987227 430.890704c-44.791136 0-81.108273 36.303834-81.108273 81.109296 0 44.778856 36.316114 81.108273 81.108273 81.108273s81.108273-36.329417 81.108273-81.108273C958.094476 467.194538 921.778362 430.890704 876.987227 430.890704z"
              p-id="3698"
            />
          </svg>
        </a-button>
        <a-menu slot="overlay" :arrow="arrow">
          <a-menu-item @click="showColorModal">
            <div style="align-items: center;display: flex;">
              <a-icon type="database" style="fontSize:20px"></a-icon>
              <span style="margin-left:5px">生成色卡</span>
            </div>
          </a-menu-item>
          <a-menu-item @click="showSubModal">
            <div style="align-items: center;display: flex;">
              <a-icon type="exclamation-circle" style="fontSize:20px"></a-icon>
              <span style="margin-left:5px">图片投诉</span>
            </div>
          </a-menu-item>
        </a-menu>
      </a-dropdown>
      <div class="clear"></div>
    </a-modal>
  </div>
</template>

<script>
import SubmitModal from "./SubmitModal";
import ColorModal from "./ColorModal";
import PicInfo from "./PicInfo";
import Collection from "./account/AddCollection";
import { Affix } from "ant-design-vue";
export default {
  components: {
    SubmitModal,
    ColorModal,
    PicInfo,
    Collection,
  },
  name: "ImageDetail",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    targetPid: {
      type: Number,
      default: 1,
    },
    image: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      message: "登录",
      maskClosable: false,
      screenWidth: document.body.clientWidth,
      screenHeight: document.body.clientHeight,
      closable: false,
      imageBoxStyle: "",
      destroyOnClose: true,
      colorVisible: false,
      subVisible: false,
      picInfoVisible: false,
      arrow: true,
      imgSize: ["800x360", "1920x1080", "2400x1350"],
      collectionVisible: false,
      colorStr:"",
    };
  },
  created() {
    this.imageBoxStyle = `height:${0.8 * document.body.clientHeight}px`;
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
  methods: {
    handleCancel() {
      this.$emit("close");
    },
    async collect() {
      this.closeCollection();
      this.image.collected = true;
    },
    cancelCollect(pid) {
      console.log(pid);
      this.image.collected = !this.image.collected;
    },
    async like(pid) {
      try {
        // let res = await this.$axios.post(`/admire?pid=${pid}&like=0&type=1`);
        let res = await this.$axios.post(`/admire?pid=${pid}&like=0&type=1`, {
          pid: pid,
          like: 0,
          type: "1",
        });
        if (res.data.code !== 200) {
          throw res.data.msg;
        } else {
          this.image.liked = !this.image.liked;
        }
      } catch (err) {
        if (typeof err === "string") {
          this.$message.error(err);
        } else {
          this.$message.error("网络错误");
        }
      }
    },
    async cancelLike(pid) {
      try {
        let res = await this.$axios.post(`/admire?pid=${pid}&like=1&type=1`, {
          pid: pid,
          like: 0,
          type: "1",
        });
        if (res.data.code !== 200) {
          throw res.data.msg;
        } else {
          this.image.liked = !this.image.liked;
        }
      } catch (err) {
        if (typeof err === "string") {
          this.$message.error(err);
        } else {
          this.$message.error("网络错误");
        }
      }
      this.load();
    },
    closeColorModal() {
      this.colorVisible = false;
    },
    async showColorModal() {
      try {
        let res = await this.$axios.get(
          `/card`, {
          params: {
            pid: this.image.pid
          },});
          this.colorStr = res.data.data;
        if (res.data.code === 200) {
          console.log("success");
        } else {
          throw res.data.msg;
        }}catch (err) {
        if (typeof err === "string") {
          this.$message.error(err);
        } else {
          this.$message.error("网络错误");
        }
      }
      this.colorVisible = true;
    },
    closeSubModal() {
      this.subVisible = false;
    },
    showSubModal() {
      this.subVisible = true;
    },
    closePicInfo() {
      this.picInfoVisible = false;
    },
    showPicInfo() {
      this.$emit("show");
      this.picInfoVisible = true;
    },
    closeCollection() {
      this.collectionVisible = false;
    },
    showCollection() {
      this.collectionVisible = true;
    },
    async load() {
      try {
        let res = await this.$axios.get(
          `/picture/detail?pid=${this.targetPid}`
        );
        if (res.data.code !== 200) {
          throw res.data.msg;
        } else {
          this.image = res.data.data[0];
        }
      } catch (err) {
        if (typeof err === "string") {
          this.$message.error(err);
        } else {
          this.$message.error("网络错误");
        }
      }
    },
  },
};
</script>

<style lang="less" scoped>
.title {
  margin: 0;
}
.imageDetail {
  .clear {
    clear: both;
  }
  .headerBtn {
    float: right;
    border: 0;
  }
  .footerLeftBtn {
    font-size: 8px;
    float: left;
    margin: 5px 5px 0 5px;
  }
  .footerLeftBtn:focus {
    color: rgba(0, 0, 0, 0.65);
    border: 1px solid transparent;
    border-top-color: rgb(217, 217, 217);
    border-top-style: solid;
    border-top-width: 1px;
    border-right-color: rgb(217, 217, 217);
    border-right-style: solid;
    border-right-width: 1px;
    border-bottom-color: rgb(217, 217, 217);
    border-bottom-style: solid;
    border-bottom-width: 1px;
    border-left-color: rgb(217, 217, 217);
    border-left-style: solid;
    border-left-width: 1px;
    border-image-source: initial;
    border-image-slice: initial;
    border-image-width: initial;
    border-image-outset: initial;
    border-image-repeat: initial;
  }
  .footerLeftBtn:hover {
    color: #40a9ff;
  }

  .footerRightBtn {
    align-items: center;
    display: flex;
    float: right;
    margin: 5px 5px 0 5px;
    .text {
      font-size: 14px;
      font-weight: bold;
    }
  }
  .footerRightBtn:focus {
    color: rgba(0, 0, 0, 0.65);
    border: 1px solid transparent;
    border-top-color: rgb(217, 217, 217);
    border-top-style: solid;
    border-top-width: 1px;
    border-right-color: rgb(217, 217, 217);
    border-right-style: solid;
    border-right-width: 1px;
    border-bottom-color: rgb(217, 217, 217);
    border-bottom-style: solid;
    border-bottom-width: 1px;
    border-left-color: rgb(217, 217, 217);
    border-left-style: solid;
    border-left-width: 1px;
    border-image-source: initial;
    border-image-slice: initial;
    border-image-width: initial;
    border-image-outset: initial;
    border-image-repeat: initial;
  }
  .footerRightBtn:hover {
    color: #40a9ff;
  }
  .imageBox {
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 500px;
    .image {
      height: 100%;
      width: auto;
    }
  }
}
</style>