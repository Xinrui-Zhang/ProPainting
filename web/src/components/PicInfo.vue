<template >
  <div>
    <a-modal
      v-model="visible"
      @cancel="handleCancel"
      centered
      :closable="closable"
      wrapClassName="PicInfo"
      :destroyOnClose="destroyOnClose"
      :footer="null"
      :header="null"
      :bodyStyle="{padding:0}"
      :maskClosable="maskClosable"
    >
      <div :style="bodyStyle">
        <p class="title">
          <span>{{image.title}}</span>
          <a-button
            shape="circle"
            class="closeBtn"
            @click="handleCancel"
            style="background:rgba(255,255,255,0)"
          >
            <a-icon type="close" :style="{ fontSize: '20px'}" />
          </a-button>
        </p>
        <p>Published by {{image.artist}}</p>

        <p style="margin-top:30px">{{image.intro}}</p>
        <p style="margin-top:50px">
          <a-statistic :value="this.image.like" class="statistic">
            <p slot="title" style="align-items:center;display:flex;">
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
                  fill="rgba(0,0,0,0.65)"
                />
              </svg>
              <span style="margin-left:5px;color:rgba(0,0,0,0.65);">喜欢的人</span>
            </p>
          </a-statistic>
          <a-statistic :value="this.image.collect" class="statistic" style="margin-left:30px;">
            <p slot="title" style="align-items:center;display:flex;">
              <svg
                t="1595391346692"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="5385"
                width="20"
                height="20"
              >
                <path
                  d="M957.258525 404.23316c-3.78431-11.352931-13.589115-19.609609-25.458088-21.501764l-270.406182-41.799429L540.640349 82.394759c-5.332437-11.180917-16.513355-18.40551-28.898371-18.40551 0 0 0 0 0 0-12.385016 0-23.565933 7.052579-28.898371 18.40551l-121.78599 258.021166L90.135394 381.183269c-11.868974 1.720141-21.673778 9.976818-25.630102 21.32975s-1.032085 23.909961 7.396607 32.510667L268.342012 637.140265 221.38216 921.995632c-2.064169 12.040988 3.096254 24.25399 13.073072 31.306568 9.976818 7.052579 23.221905 7.740635 33.88678 1.892155L510.193852 822.227448l241.335797 133.826978c4.816395 2.580212 10.148833 3.956325 15.48127 3.956325 0.172014 0 0.516042 0 0.688056 0 17.717453 0 31.994625-14.277171 31.994625-31.994625 0-3.956325-0.688056-7.740635-2.064169-11.352931l-44.895683-278.662859 196.956157-201.256509C958.118596 428.143121 961.042836 415.586091 957.258525 404.23316z"
                  p-id="5386"
                  fill="rgba(0,0,0,0.65)"
                />
              </svg>
              <span style="margin-left:5px;color:rgba(0,0,0,0.65);">收藏的人</span>
            </p>
          </a-statistic>
        </p>
        <a-divider></a-divider>
        <a-descriptions layout="vertical">
          <a-descriptions-item
            label="图片类型"
          >{{image.type ==='3'?"大师作品":image.type === '2'?"参考图片":"用户作品"}}</a-descriptions-item>
          <a-descriptions-item v-if="image.style !== undefined" label="风格流派">{{image.style}}</a-descriptions-item>
          <a-descriptions-item v-if="image.genre !== undefined" label="题材">{{image.genre}}</a-descriptions-item>
          <a-descriptions-item v-if="image.tech !== undefined" label="画材">{{image.tech}}</a-descriptions-item>
          <a-descriptions-item v-if="image.tag !== undefined" label="标签">{{image.tag}}</a-descriptions-item>
          <a-descriptions-item label="分辨率">
            <span style="font-size:20px;font-weight:bold;">{{image.width}} × {{image.height}}</span>
          </a-descriptions-item>
        </a-descriptions>
        <p v-if="image.refer">
          <a :href="image.refer" style="float:right;">参考链接</a>
        </p>
      </div>
    </a-modal>
  </div>
</template>

<script>
export default {
  components: {},
  name: "PicInfo",
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    image: {
      type: Object,
      default: () => {
        return {
          collected: true,
          liked: true,
          title: "moon",
          titlezh: "月",
          type: "3",
          url:
            "https://images.unsplash.com/photo-1594949397838-98d5c2ad5561?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80",
          width: 3648,
          height: 5472,
          artistzh: "Vino Li",
          artist: "Vino Li",
          sttle: 0,
          aid: "001",
          avatar: "https://static.myseu.cn/avatar.jpg",
          genre: 0,
          tech: 0,
          introzh: "Published on July 15, 2020",
          intro: "Published on July 15, 2020",
          state: 1,
          visibility: 1,
          tag: "",
          refer: "https://unsplash.com/photos/EYnzEFJ6UmY",
          like: 22,
          collect: 213
        };
      }
    }
  },
  data() {
    return {
      screenWidth: document.body.clientWidth,
      closable: false,
      destroyOnClose: true,
      maskClosable: false,
      bodyStyle: `height:100%;width:100%;padding:24px;background-image:linear-gradient(rgba(255, 255, 255, 0.8),white ),url(${this.image.url})`
    };
  },
  mounted() {
    const that = this;
    window.onresize = () => {
      return (() => {
        window.screenWidth = document.body.clientWidth;
        that.screenWidth = window.screenWidth;
      })();
    };
  },
  methods: {
    handleCancel() {
      this.$emit("closePicInfo");
    }
  },
  watch: {
    image: {
      handler(newValue, oldValue) {
        this.bodyStyle = `height:100%;width:100%;padding:24px;background-image:linear-gradient(rgba(255, 255, 255, 0.8),white ),url(${this.image.url})`;
      },
      deep: true,
      immediate: true
    }
  }
};
</script>

<style lang="less" scoped>
.title {
  font-size: 22px;
  margin: 0;
  height: 100%;
  width: 100%;
  .closeBtn {
    float: right;
    border: 0;
  }
}
.statistic {
  display: inline-block;
  // float: right;
}
</style>