<template>
  <a-modal v-model="visible" :title="title" :footer="null" @cancel="closeModel">
    <a-layout-header style="background-color:#ffffff">
      <a-row>
        <a-col :span="22">
          <a-avatar :src="this.avatar"></a-avatar>
          {{this.name}}创建的收藏夹{{this.title}}
          <!-- <a-button v-if="!this.collected" @click="collect()" title="收藏" class="footerLeftBtn"> -->

          <!-- </a-button> -->
        </a-col>
        <a-col :span="2" style="text-align: right">
          <svg
            v-if="!this.collected"
            title="收藏"
            class="collect"
            version="1.1"
            viewBox="0 0 32 32"
            width="10"
            height="10"
            aria-hidden="false"
            fill="currentColor"
          >
            <path d="M14 3h4v26h-4zM29 14v4h-26v-4z" />
          </svg>
          <a-icon v-if="this.public" type="eye"></a-icon>
          <a-icon v-else type="eye-invisible"></a-icon>
        </a-col>
      </a-row>
      <a-row>
        <p>{{this.intro}}</p>
      </a-row>
    </a-layout-header>
    <a-layout-content>
      <Picture v-bind:url="url" v-bind:collection="flag" v-bind:query="cid"></Picture>
    </a-layout-content>
  </a-modal>
</template>

<script>
import Picture from "./Picture";
export default {
  name: "CollectionModal",
  components: {
    Picture,
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    cid: {
      type: String,
      default: "",
    },
    title: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      url: "/collection/picture",
      flag: true,
      collected: false,
      cid: 2,
      liked: false,
      title: "Martin Aagaard",
      uid: 0,
      name: "系统",
      intro: " ",
      visibility: "1",
      avatar: " ",
      public: false,
    };
  },
  methods: {
    closeModel() {
      this.$emit("closeModal");
    },
    async loadInfo() {
      try {
        let res = await this.$axios.get("/collection/detail", {
          params: {
            cid: this.cid,
          },
        });
        if (res.data.code !== 200) {
          throw res.data.msg;
        }
        console.log(res);
        this.collected = res.data.data.collected;
        this.cid = res.data.data.cid;
        this.liked = res.data.data.liked;
        this.title = res.data.data.title;
        this.uid = res.data.data.uid;
        this, (name = res.data.data.name);
        this.intro = res.data.data.intro;
        this.visibility = res.data.data.visibility;
        this.avatar = res.data.data.avatar;
        if (this.visibility === "1") this.public = true;
        else this.public = false;
      } catch (err) {
        if (typeof err === "string") {
          this.$message.error(err);
        } else {
          this.$message.error("网络错误");
        }
      }
    },
    async collect() {
      try {
        let res = await this.$axios.post(
          `/collection?pid=1&cid=${this.cid}&type=2`
        );
        if (res.data.code !== 200) {
          throw res.data.msg;
        } else {
          this.collected = !this.collected;
        }
        console.log(res);
      } catch (err) {
        if (typeof err === "string") {
          this.$message.error(err);
        } else {
          this.$message.error("网络错误");
        }
      }
    },
  },
  mounted() {
    this.loadInfo();
  },
};
</script>

<style>
</style>