<template>
  <div>
    <a-modal v-model="visible" title="图像风格迁移" :footer="null" @cancel="closeStyleModal" width="40%">
      <div style="text-align:center">
        <p v-if="this.originImg">
          <img :src="originImg" style="height: 256px;width:auto;" alt />
        </p>
        <div v-else style="background-color:#F5F5F5;width:100%;height:250px;line-height:200px">
          <span style="font-size:60px;">源图</span>
        </div>
        <p>
          <a-row>
            <a-col :span="12">
              <a-upload
                :file-list="fileList"
                :show-upload-list="false"
                :before-upload="beforeUpload"
              >
                <a-button size="small" type="primary" style="margin-top:50px;">上传源图</a-button>
              </a-upload>
            </a-col>
            <a-col :span="12">
              选择你要迁移的风格：
              <br />
              <br />
              <a-select default-value="2" style="width: 80%" @change="styleChange">
                <a-select-option value="2">照片->莫奈</a-select-option>
                <a-select-option value="1">莫奈->照片</a-select-option>
                <a-select-option value="4">照片->梵高</a-select-option>
                <a-select-option value="3">梵高->照片</a-select-option>
                <a-select-option value="6">照片->浮世绘</a-select-option>
                <a-select-option value="5">浮世绘->照片</a-select-option>
                <a-select-option value="8">照片->塞尚</a-select-option>
                <a-select-option value="7">塞尚->照片</a-select-option>
              </a-select>
              <br />
              <br />
              <a-button type="primary" @click="clickStyle">开始迁移</a-button>
            </a-col>
          </a-row>
        </p>
        <img v-if="this.finalImg" style="height: 256px;width:auto;" :src="finalImg" alt />
        <div v-else style="background-color:#F5F5F5;width:100%;height:250px;line-height:200px">
          <span
            style="font-size:60px;"
          >{{this.loading === true?"加载中":this.finished===true?"":"目标图"}}</span>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script>
function getBase64(img, callback) {
  const reader = new FileReader();
  reader.addEventListener("load", () => callback(reader.result));
  reader.readAsDataURL(img);
}
export default {
  name: "StyleModal",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      originImg: "",
      finalImg: "",
      loading: false,
      form: this.$form.createForm(this, { name: "uploadForm" }),
      finished: false,
      fileList: [],
      value: 2,
    };
  },
  methods: {
    closeStyleModal() {
      this.$emit("closeStyleModal");
      this.finished = false;
      this.finalImg = "";
    },
    beforeUpload(file) {
      const isJpgOrPng =
        file.type === "image/jpeg" || file.type === "image/png";
      if (!isJpgOrPng) {
        this.$message.error("You can only upload JPG file!");
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("Image must smaller than 2MB!");
        return false;
      }
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (e) => {
        this.fileList = [file];
      };
      this.originImg = window.URL.createObjectURL(file);
      return false;
    },
    async clickStyle() {
      this.loading = true;
      let params = new FormData();
      params.append("index", this.value);
      params.append("multipartFile", this.fileList[0]);
      let config = {
        headers: {
          "Content-Type":
            "multipart/form-data;boundary = " + new Date().getTime(),
        },
      };
      let res;
      try {
        res = await this.$axios.post("/transfer", params, config);
        if (res.data.code === 200) {
        } else {
          throw res.data.msg;
        }
      } catch (err) {
        if (typeof err === "string") {
          this.$message.success(res.data.msg);
        } else {
          this.$message.success("上传成功！");
        }
      }
      this.finalImg = "data:image/jpeg;base64," + res.data.data;
      this.finished = true;
      this.loading = false;
    },
    styleChange(value) {
      this.value = value;
    },
  },
};
</script>

<style>
</style>