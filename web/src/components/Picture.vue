<template>
  <div class="picture">
    <a-back-top></a-back-top>
    <a-row type="flex" justify="start" align="top">
      <a-col id="pic" :span="7" offset="1">
        <div v-for="img in imgList1" :key="img.index">
          <div :style="img.style">
          <img  :src="img.url" style="width: 100%" alt="" @click="clickPic(img)" />
        </div>
          <p></p>
        </div>
      </a-col>
      <a-col :span="7" offset="1">
        <div v-for="img in imgList2" :key="img.index">
          <div :style="img.style">
          <img :src="img.url" style="width: 100%" alt="" @click="clickPic(img)" />
          </div>
          <p></p>
        </div>
      </a-col>
      <a-col :span="7" offset="1">
        <div v-for="img in imgList3" :key="img.index">
          <div :style="img.style">
          <img :src="img.url" style="width: 100%" alt="" @click="clickPic(img)" />
          </div>
          <p></p>
        </div>
      </a-col>
    </a-row>
    <a-button v-if="isEnough===false" @click="morePic" type="default" block>
      点击查看更多
      <a-icon type="down"></a-icon>
    </a-button>
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
import ImageDetail from "./ImageDetail";
import PicInfo from "./PicInfo";
import qs from "qs";
export default {
  name: "Picture",
  props: {
    url: {
      type: String,
      default: "/picture/master/outline",
    },
    query: {
      type: String,
      default: "Aagaard",
    },
    flag: {
      type: Boolean,
      default: true,
    },
    collection:{
      type: Boolean,
      default: false,
    }
  },
  components: {
    ImageDetail,
    PicInfo,
  },
  data() {
    return {
      picVisible: false,
      imageDetailVisible: false,
      picInfoVisible: false,
      targetPid: 1,
      currentURL: "",
      picPid: 0,
      imgList1: [],
      imgList2: [],
      imgList3: [],
      currentPage: 1,
      isEnough: false,
      picture: {
        liked: false,
        pid: 78129,
        title: "qW8mXOYcSq",
        titlezh: "Z2apZgdmK1",
        type: "QKIYPE2YAz",
        url: "http://nrs.jruazma.wxnxnqn",
        width: 800,
        height: 600,
        artistzh: "eho37oIYfB",
        artist: "JKymYbAv5q",
        style: "0",
        aid: 78407,
        avatar: "nMhTdlMZF4",
        genre: "0",
        tech: "0",
        introzh: "aaaaaaa",
        intro: "8sPGl6pNim",
        state: "2",
        visibility: "1",
        tag: "d4qnR9a2Ji",
        refer: "Xu3IixKPOk",
        like: 0,
      },
      listHeight1: 0,
      listHeight2: 0,
      listHeight3: 0,
      image: {},
      color:["#d1c2d3","#a7a8bd","#70887d","#ad9e5f","#826b48"]
    };
  },
  methods: {
    closeImageDetail() {
      this.imageDetailVisible = false;
    },
    async showImageDetail() {
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
      this.imageDetailVisible = true;
    },
    //读取图片
    async loadImage(flag) {
      let res;
      try {
        if(this.collection===false)
        {res = await this.$axios.get(this.url, {
          params: {
            q: this.query,
            pageIndex: this.currentPage,
            pageSize: 30,
          },
        });}
        else{
          res = await this.$axios.get(this.url, {
          params: {
            cid: this.query,
            pageIndex: this.currentPage,
            pageSize: 30,
          },
        });
        }
        if (res.data.code !== 200 && res.data.data) {
          throw res.data.msg;
        }
      } catch (err) {
        if (typeof err === "string") {
          this.$message.error(err);
        } else {
          console.log(err);
          this.$message.error("网络错误");
        }
        return;
      }
      console.log(res);
      if (res.data.totalPage === this.currentPage) this.isEnough = true;
      else this.isEnough = false;
      if (flag === false) {
        this.imgList1 = [];
        this.imgList2 = [];
        this.imgList3 = [];
        this.listHeight1 = 0;
        this.listHeight2 = 0;
        this.listHeight3 = 0;
        this.currentPage = 1;
      }
      for (let i = 0; i < res.data.data.pageList.length; i++) {
        let op = res.data.data.pageList[i];
        var temp = document.getElementById('pic');
        var tool = temp.offsetWidth;//102 注意没有单位
        var num = Math.floor(Math.random()*5);
        let img = {
          pid: op.pid,
          url: op.url,
          width: op.width,
          height: op.height/op.width*tool,
          style:"background-color:"+this.color[num]+"; height:"+op.height/op.width*tool+"px",
        };
        if (this.listHeight1 <= this.listHeight3) {
          if (this.listHeight1 <= this.listHeight2) {
            this.listHeight1 += op.height / op.width;
            this.imgList1.push(img);
          } else {
            this.listHeight2 += op.height / op.width;
            this.imgList2.push(img);
          }
        } else if (this.listHeight2 <= this.listHeight3) {
          this.listHeight2 += op.height / op.width;
          this.imgList2.push(img);
        } else {
          this.listHeight3 += op.height / op.width;
          this.imgList3.push(img);
        }
        // console.log(i);
        // let img1 = {
        //   pid: res.data.data.pageList[i].pid,
        //   url: res.data.data.pageList[i].url
        // };
        // this.imgList1.push(img1);
        // i++;
        // let img2 = {
        //   pid: res.data.data.pageList[i].pid,
        //   url: res.data.data.pageList[i].url
        // };
        // this.imgList2.push(img2);
        // i++;
        // let img3 = {
        //   pid: res.data.data.pageList[i].pid,
        //   url: res.data.data.pageList[i].url
        // };
        // this.imgList3.push(img3);
        // i++;
      }
    },
    morePic() {
      this.currentPage++;
      this.loadImage(true);
    },
    clickPic(img) {
      this.targetPid = img.pid;
      this.currentURL = img.url;
      console.log(img.url);
      this.showImageDetail();
    },
    closePicInfo() {
      this.picInfoVisible = false;
    },
    showPicInfo() {
      this.picInfoVisible = true;
    },
  },
  mounted() {},
  computed: {
    listenChange() {
      const { url, query } = this;
      return { url, query };
    },
  },
  watch: {
    listenChange(newV, oldV) {
      this.loadImage(this.flag);
    },
  },
  created() {
    this.loadImage(this.flag);
  },
  //  updated(){
  //    this.$nextTick(()=>{
  //      this.loadImage();
  //    });
  //   console.log("update");
  //   this.loadImage();
  // },
  // // updated() {
  //
  // },
  destroyed() {
    this.imgList1 = [];
    this.imgList2 = [];
    this.imgList3 = [];
  },
};
</script>
<style scoped>
</style>
