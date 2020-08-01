<template>
  <a-modal v-model="visible" title="上传" @cancel="handleCancel" :footer="null">
    <a-form id="components-form-demo-validate-other" :form="form" @submit="handleSubmit">
      <a-form-item label="上传作品">
        <a-upload
          v-decorator="[
          'multipart',
          {
            valuePropName: 'multipart',
            getValueFromEvent: normFile,
          },
        ]"
          :file-list="fileList"
          :remove="handleRemove"
          :before-upload="beforeUpload"
          list-type="picture"
        >
          <a-button>
            <a-icon type="upload"></a-icon>点击上传
          </a-button>
        </a-upload>
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="作品名">
        <a-input
          v-decorator="[
          'titlezh',
          {
            rules: [
              {
                required: true,
                message: 'Please input your painting title!',
              },
            ],
          },
        ]"
        />
      </a-form-item>

      <a-form-item v-bind="formItemLayout" label="作者">
        <a-input
          v-decorator="[
          'artistzh',
          {
            rules: [
              {
                required: true,
                message: 'Please input your name!',
              },
            ],
          },
        ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="作品简介">
        <a-input
          v-decorator="[
          'introzh',
          {
            rules: [
              {
                required: false,
              },
            ],
          },
        ]"
        />
      </a-form-item>
      <a-form-item :wrapper-col="{ span: 12, offset: 6 }">
        <a-button type="primary" html-type="submit">Submit</a-button>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script>
function getBase64(img, callback) {
  const reader = new FileReader();
  reader.addEventListener("load", () => callback(reader.result));
  reader.readAsDataURL(img);
}
export default {
  name: "UploadModal",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "validate_other" });
  },
  data() {
    return {
      loading: false,
      imageUrl: "",
      titlezh: "",
      artistzh: "",
      introzh: "",
      fileList: [],
      formItemLayout: {
        labelCol: { span: 6 },
        wrapperCol: { span: 14 },
      },
    };
  },
  methods: {
    handleCancel(e) {
      this.$emit("close");
    },
    handleRemove(file) {
      const index = this.fileList.indexOf(file);
      const newFileList = this.fileList.slice();
      newFileList.splice(index, 1);
      this.fileList = newFileList;
    },
    beforeUpload(file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (e) => {
        this.fileList = [file];
      };
      return false;
    },
    async handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log("Received values of form: ", values);
        }
      });
      let params = new FormData();
      params.append("titlezh", await this.form.getFieldValue("titlezh"));
      params.append("artistzh", await this.form.getFieldValue("artistzh"));
      params.append("introzh", await this.form.getFieldValue("introzh"));
      params.append("visibility", "1");
      params.append("multipartFile", this.fileList[0]);
      console.log(params);
      let config = {
        headers: {
          "Content-Type":
            "multipart/form-data;boundary = " + new Date().getTime(),
        },
      };
      let res;
      try {
        res = await this.$axios.post("/picture/user", params, config);
        if (res.data.code === 200) {
          this.$message.success("上传成功！");
        } else {
          throw res.data.msg;
        }
      } catch (err) {
        if (typeof err === "string") {
          this.$message.success(res.data.msg);
        } else {
          this.$message.error("网络错误");
        }
      }
    },
    normFile(e) {
      console.log("Upload event:", e);
      if (Array.isArray(e)) {
        return e;
      }
      return e && e.multipart;
    },
  },
};
</script>

<style scoped>
.picture-uploader > .ant-upload {
  width: 128px;
  height: 128px;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>