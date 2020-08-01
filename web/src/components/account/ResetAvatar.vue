<template >
  <a-modal
    v-model="visible"
    @cancel="handleCancel"
    :closable="closable"
    :maskClosable="maskClosable"
    wrapClassName="resetAvatar"
    :destroyOnClose="destroyOnClose"
  >
    <p slot="title" class="title">
      <span>修改头像</span>
      <a-button shape="circle" class="closeBtn" @click="handleCancel">
        <a-icon type="close" :style="{ fontSize: '20px'}" />
      </a-button>
    </p>
    <a-row type="flex" justify="center">
      <div class="cropper">
        <vueCropper
          ref="cropper"
          :img="option.img"
          :outputSize="option.size"
          :outputType="option.outputType"
          :info="option.info"
          :full="option.full"
          :canMove="option.canMove"
          :canMoveBox="option.canMoveBox"
          :original="option.original"
          :autoCrop="option.autoCrop"
          :autoCropWidth="option.autoCropWidth"
          :autoCropHeight="option.autoCropHeight"
          :fixedBox="option.fixedBox"
          @realTime="realTime"
        ></vueCropper>
      </div>
      <div class="previewBox">
        <div :style="previews.div" class="preview">
          <img :src="previews.url" :style="previews.img" />
        </div>
        <a-row type="flex" justify="center">
          <a-upload :showUploadList="false" :before-upload="beforeUpload" :fileList="fileList">
            <a-button size="small" type="primary">更换头像</a-button>
          </a-upload>
        </a-row>
        <br />
        <a-row>
          <a-button title="放大" shape="circle" size="small" @click="changeScale(1)">
            <a-icon type="plus" style="margin:0 auto;vertical-align: -0.125em;" />
          </a-button>
          <a-button title="缩小" shape="circle" size="small" @click="changeScale(-1)">
            <a-icon type="minus" style="margin:0 auto;vertical-align: -0.125em;" />
          </a-button>
          <a-button title="下载图片" shape="circle" size="small" @click="down('blob')">
            <a-icon type="download" style="margin:0 auto;vertical-align: -0.125em;" />
          </a-button>
          <a-button title="左转90°" shape="circle" size="small" @click="rotateLeft">
            <a-icon type="undo" style="margin:0 auto;vertical-align: -0.125em;" />
          </a-button>
          <a-button title="右转90°" shape="circle" size="small" @click="rotateRight">
            <a-icon type="redo" style="margin:0 auto;vertical-align: -0.125em;" />
          </a-button>
        </a-row>
      </div>
    </a-row>
    <span slot="footer" class="dialog-footer">
      <a-button @click="handleCancel">取 消</a-button>
      <a-button type="primary" @click="saveEditAvatar">确 定</a-button>
    </span>
  </a-modal>
</template>

<script>
import { VueCropper } from "vue-cropper";
export default {
  components: { VueCropper },
  name: "resetAvatar",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    img: {
      type: String,
      default: require("../../assets/images/avatar_default.jpg"),
    },
  },
  data() {
    return {
      closable: false,
      maskClosable: false,
      destroyOnClose: false,
      previews: {},
      option: {
        img: this.img,
        info: true, // 裁剪框的大小信息
        outputSize: 1, // 剪切后的图片质量（0.1-1）
        full: true, // 输出原图比例截图 props名full
        outputType: "png", // 裁剪生成额图片的格式
        canMove: true, // 能否拖动图片
        original: false, // 上传图片是否显示原始宽高
        canMoveBox: true, // 能否拖动截图框
        autoCrop: true, // 是否默认生成截图框
        autoCropWidth: 150,
        autoCropHeight: 150,
        fixedBox: false, // 截图框固定大小
      },
      fileList: [],
    };
  },

  methods: {
    beforeUpload(file) {
      this.fileList = [file];
      this.option.img = window.URL.createObjectURL(file);
      return false;
    },
    handleCancel() {
      this.$emit("close");
    },
    // 保存头像修改
    async saveEditAvatar() {
      this.handleCancel();
      try {
        let formData = new FormData();
        formData.append("multipartFile", this.fileList[0]);
        let config = {
          headers: {
            "Content-Type":
              "multipart/form-data;boundary = " + new Date().getTime(),
          },
        };
        let res = await this.$axios.post(`/resource`, formData, config);
        if (res.data.code !== 200) {
          throw res.data.msg;
        } else {
          this.$message.success("修改成功");
          console.log(res.data);
        }
      } catch (err) {
        if (typeof err === "string") {
          this.$message.error(err);
        } else {
          this.$message.error("网络错误");
        }
      }
      this.finish("blob");
    },
    // 放大/缩小
    changeScale(num) {
      num = num || 1;
      this.$refs.cropper.changeScale(num);
    },
    // 左旋转
    rotateLeft() {
      this.$refs.cropper.rotateLeft();
    },
    // 右旋转
    rotateRight() {
      this.$refs.cropper.rotateRight();
    },
    // 保存上传图片
    finish(type) {
      if (type === "blob") {
        this.$refs.cropper.getCropBlob((data) => {
          this.avatarURL = window.URL.createObjectURL(data);
          this.$store.state.user.avatar = window.URL.createObjectURL(data);
          //访问接口保存到数据库写这儿!
        });
      } else {
        this.$refs.cropper.getCropData((data) => {
          //访问接口保存到数据库写这儿!
        });
      }
    },
    // 实时预览函数
    realTime(data) {
      this.previews = data;
    },
    // 下载图片
    down(type) {
      var aLink = document.createElement("a");
      aLink.download = "author-img";
      if (type === "blob") {
        this.$refs.cropper.getCropBlob((data) => {
          aLink.href = window.URL.createObjectURL(data);
          aLink.click();
        });
      } else {
        this.$refs.cropper.getCropData((data) => {
          aLink.href = data;
          aLink.click();
        });
      }
    },
    // 更换头像--上传本地图片
    uploadImg(event) {
      console.log(event);
      var reader = new FileReader();
      reader.onload = function (event) {
        console.log(event);
        let data;
        if (typeof event.target.result === "object") {
          // 把Array Buffer转化为blob 如果是base64不需要
          data = window.URL.createObjectURL(new Blob([event.target.result]));
        } else {
          data = event.target.result;
        }
        this.option.img = data;
      };
      // 转化为base64
      console.log(reader.readAsDataURL(event.raw));
      reader.readAsDataURL(event.raw);
      // 转化为blob
      // reader.readAsArrayBuffer(file.raw);
    },
  },
  created() {},
};
</script>

<style lang="less" scoped>
.previewBox {
  text-align: center;
  margin-left: 60px;
}

.preview {
  width: 150px;
  height: 150px;
  margin: 0px auto 20px auto;
  border-radius: 50%;
  border: 1px solid #ccc;
  background-color: #ccc;
  overflow: hidden;
}
// .preview::after {
//   background: url("../../assets/images/avatar_default.jpg") no-repeat center;
// }

.cropper {
  width: 260px;
  height: 260px;
}
</style>