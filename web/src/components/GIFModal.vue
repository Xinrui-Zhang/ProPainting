<template>
  <div>
    <a-modal v-model="visible" :footer="null" title="动态展示图片" @cancel="closeGIFModal">
      <a-upload
        action="https://nei.netease.com/api/apimock-v2/235dc61cfe0d6bae8247d2031431814a/"
        list-type="picture-card"
        :file-list="fileList"
        @preview="handlePreview"
        @change="handleChange"
      >
        <div v-if="fileList.length < 8">
          <a-icon type="plus"></a-icon>
          <div class="ant-upload-text">Upload</div>
        </div>
      </a-upload>
      <a-button @click="beginShow">动态显示</a-button>
      <img :src="showImage" />
      <a-modal :visible="previewVisible" :footer="null" @cancel="closePreview">
        <img alt="example" style="width: 100%" :src="previewImage" />
      </a-modal>
    </a-modal>
  </div>
</template>
<script>
function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    // reader.onload = () => resolve(reader.result);
    // reader.onerror = (error) => reject(error);
  });
}
export default {
  name: "GIFModal",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      loading: false,
      previewVisible: false,
      previewImage: "",
      fileList: [],
      index: 0,
      showImage: "",
      timer: "",
    };
  },
  methods: {
    closePreview() {
      this.previewVisible = false;
    },
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj);
      }
      this.previewImage = file.url || file.preview;
      this.previewVisible = true;
    },
    handleChange({ fileList }) {
      console.log(fileList);
      this.fileList = fileList;
    },
    closeGIFModal() {
      this.$emit("closeGIFModal");
      clearInterval(this.timer);
    },
    beginShow() {
      this.timer = setInterval(this.makeGIF, 500);
      this.$once("hook:beforeDestroy", () => {
        clearInterval(timer);
      });
    },
    makeGIF() {
      console.log(this.fileList[this.index]);
      if (this.fileList[this.index]) {
      } else {
        this.index = 0;
      }
      this.showImage = this.fileList[this.index].thumbUrl;
      this.index++;
    },
  },
};
</script>

<style>
/* you can make up upload button and sample style by using stylesheets */
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>