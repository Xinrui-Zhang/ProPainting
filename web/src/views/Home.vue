<template>
  <div>
    <imageDetail :visible="imageDetailVisible" :pid="pid" @close="closeImageDetail"></imageDetail>
    <QAModal :visible="QAvisible" @close="closeQAModal"></QAModal>
    <keep-alive>
      <a-layout id="components-layout-demo-fixed">
        <a-layout-header :style="{ position: 'fixed', zIndex: 1, width: '100%'}">
          <Header @onSearch="onSearch"></Header>
        </a-layout-header>
        <a-layout-content :style="{ padding: '0 50px', marginTop: '64px' }">
          <Collection v-if="notCollection===false" v-bind:query="query" v-bind:flag="flag"></Collection>
          <Picture
            v-else-if="not3D"
            ref="pic"
            v-bind:url="url"
            v-bind:query="query"
            v-bind:flag="flag"
          ></Picture>
          <Model3D v-else ref="model" v-bind:url="url" v-bind:query="query" v-bind:flag="flag"></Model3D>
          <a-icon type="customer-service" style="position:fixed;top:30%;right:1%; font-size: 28px"  @click="showQAModal"></a-icon>
        </a-layout-content>
        <a-layout-footer :style="{ textAlign: 'center' }"><a href="https://github.com/Xinrui-Zhang/ProPainting" style="color:#5a5a5a">ProPainting ©2020 Created by 07 <a-icon type="github"></a-icon></a></a-layout-footer>
      </a-layout>
    </keep-alive>
  </div>
</template>
<script>
import ImageDetail from "../components/ImageDetail";
import Header from "../components/Header";
import Picture from "../components/Picture";
import GIFModal from "../components/GIFModal";
import UploadModal from "../components/UploadModal";
import SubmitModal from "../components/SubmitModal";
import ColorModal from "../components/ColorModal";
import Model3D from "../components/Model3D";
import Collection from "../components/Collection";
import QAModal from "../components/QAModal";
export default {
  name: "Home",
  components: {
    imageDetail: ImageDetail,
    Header,
    Picture,
    GIFModal,
    UploadModal,
    SubmitModal,
    ColorModal,
    Model3D,
    Collection,
    QAModal,
  },
  data() {
    return {
      device: { isDesktop: true, isMobile: true, isWechat: false },
      imageDetailVisible: false,
      pid: "this is a pid",
      url: "/picture/master/outline",
      query: "",
      flag: true,
      not3D: true,
      notCollection: true,
      QAvisible:false,
    };
  },
  methods: {
    closeImageDetail() {
      this.imageDetailVisible = false;
    },
    showImageDetail() {
      this.imageDetailVisible = true;
    },
    showQAModal() {
      this.QAvisible = true;
    },
    closeQAModal() {
      this.QAvisible = false;
    },
    onSearch(value) {
      this.not3D = value.not3D;
      this.notCollection = value.notCollection;
      this.url = value.url;
      this.query = value.query;
      this.flag = false;
    },
  },
  mounted() {},
  mounted() {},
};
</script>
<style>
#components-layout-demo-fixed .logo {
  width: 120px;
  height: 31px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px 24px 16px 0;
  float: left;
}
#title {
  color: rgba(255, 255, 255, 0.85);
}
</style>