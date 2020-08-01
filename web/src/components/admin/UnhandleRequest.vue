  <template>
  <div style="height:100%;">
    <a-list item-layout="vertical" :data-source="listData">
      <a-list-item slot="renderItem" key="item.name" slot-scope="item, index">
        <a slot="actions" @click="handle(item.request.rid,'通过')">通过</a>
        <a slot="actions" @click="handle(item.request.rid,'不通过')">不通过</a>
        <img
          slot="extra"
          width="272"
          alt="logo"
          :src="item.url"
          @click="clickPic(item.request.pid,item.img)"
        />
        <a-list-item-meta>
          <span slot="title">{{ item.request.name }}</span>
          <a-avatar size="large" slot="avatar" :src="item.request.avatar" />
        </a-list-item-meta>
        {{ item.request.content }}
      </a-list-item>
    </a-list>
    <a-pagination
      style="float:right;"
      v-model="current"
      :total="total"
      :pageSize="3"
      @change="handleChange"
    />
    <ImageDetail
      :visible="imageDetailVisible"
      :targetPid="this.targetPid"
      :image="image"
      @close="closeImageDetail"
      @show="showPicInfo"
    ></ImageDetail>
    <PicInfo :visible="picInfoVisible" @closePicInfo="closePicInfo" :image="image"></PicInfo>
  </div>
</template>
<script>
import ImageDetail from "../ImageDetail";
import PicInfo from "../PicInfo";
export default {
  name: "Home",
  components: {
    ImageDetail,
    PicInfo,
  },
  data() {
    return {
      total: undefined,
      imageDetailVisible: false,
      picInfoVisible: false,
      listData: [],
      targetPid: 0,
      image: {},
      currentURL: "",
      total: 23,
      current: 1,
      pageSize: 3,
    };
  },
  methods: {
    closePicInfo() {
      this.picInfoVisible = false;
    },
    showPicInfo() {
      this.picInfoVisible = true;
    },
    closeImageDetail() {
      this.imageDetailVisible = false;
    },
    showImageDetail() {
      this.image = {
        collected: false,
        liked: false,
        title: `张心睿上传的第${this.targetPid}张图片`,
        titlezh: `张心睿上传的第${this.targetPid}张图片`,
        type: "3",
        url: this.currentURL,
        width: 800,
        height: 600,
        artistzh: "张心睿",
        artist: "张心睿",
        style: "1",
        aid: "002",
        avatar: require("../../assets/images/zxr_avatar.jpg"),
        genre: "1",
        tech: "1",
        introzh: "Published on July 21, 2020",
        intro:
          "这是张心睿上传的一张图片的介绍，图片的内容是一个带着黑色帽子穿着黑色上衣扎着棕色马尾辫，有着棕色瞳孔并且闪烁着星星般的光芒的女孩",
        state: 1,
        visibility: 1,
        tag: "",
        refer: "https://github.com/Xinrui-Zhang",
        like: 1123,
        collect: 5166487,
      };
      this.imageDetailVisible = true;
    },
    clickPic(pid, url) {
      this.targetPid = pid;
      this.currentURL = url;
      this.showImageDetail();
    },
    handle(rid, message) {
      console.log(rid, message);
    },
    async load() {
      try {
        let res = await this.$axios.get(
          `/request?pageIndex=${this.current}&pageSize=${this.pageSize}`
        );
        if (res.data.code === 200) {
          this.total = res.data.data.total;
          this.listData = [];
          this.listData = res.data.data.map((Element) => {
            return {
              request: Element.request,
              url: Element.url,
            };
          });
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
      }
    },
    handleChange: async function (page) {
      this.current = page;
      await this.load();
    },
  },
  async created() {
    await this.load();
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
</style>