<template>
  <div>
    <a-modal
      v-model="visible"
      :footer="null"
      @cancel="handleCancel"
      centered
      :closable="closable"
      wrapClassName="SubmitModal"
    >
      <p slot="title" class="title">
        <span>申诉</span>
        <a-button shape="circle" class="closeBtn" @click="handleCancel">
          <a-icon type="close" :style="{ fontSize: '20px'}" />
        </a-button>
      </p>
      <a-form :form="form" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
        <a-form-item label="图片名称">
          <a-input
            allow-clear
            placeholder="请输入要申诉的图片名称"
            v-decorator="['title', { rules: [{ required: true, message: '请输入要申诉的图片名称' }],initialValue:this.title }]"
          />
        </a-form-item>
        <a-form-item label="申诉理由">
          <a-textarea
            allow-clear
            placeholder="请输入要申诉的理由"
            :rows="4"
            v-decorator="['content', { rules: [{ required: true, message: '请输入要申诉的理由' }] }]"
          />
        </a-form-item>
        <div style="text-align: right">
          <a-button type="primary" @click="handleSubmit">申诉</a-button>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
export default {
  name: "SubmitModal",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    type: {
      type: String,
      default: "1",
    },
    pid: {
      type: Number,
      default: 1,
    },
    title: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      loading: false,
      imageUrl: "",
      form: this.$form.createForm(this, { name: "submitForm" }),
      closable: false,
    };
  },
  methods: {
    async handleSubmit(e) {
      try {
        await this.form.validateFields();
      } catch (err) {
        return;
      }
      try {
        let res = await this.$axios.post("/request", {
          type: this.type,
          pid: this.pid,
          content: await this.form.getFieldValue("content"),
        });
        if (res.data.code === 200) {
          this.$message.success("申诉提交成功");
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
    handleCancel() {
      this.$emit("closeSubModal");
    },
  },
  created() {},
  watch: {},
};
</script>

<style scoped>
.title {
  font-size: 25px;
  margin: 0;
  .closeBtn {
    float: right;
    border: 0;
  }
}
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