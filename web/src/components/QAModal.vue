<template>
  <a-modal v-model="visible" @cancel="handleCancel" :footer="null">
    <a-layout-content>
      <div>
        <div v-for="message in messages" :key="message.index">
          <p
            v-if="message.role"
            style="border:10px solid;border-radius: 10px; border-color: #5a5a5a; background-color:#5a5a5a;color:#c4cbcf"
          >&nbsp;{{message.content}}</p>
          <p
            v-else
            style="text-align:right; border:10px solid; border-radius: 10px; border-color:#c4cbcf; background-color:#c4cbcf;color:#5a5a5a"
          >{{message.content}}&nbsp;</p>
        </div>
      </div>
    </a-layout-content>
    <a-layout-footer>
      <a-input-search placeholder="请输入你的问题" @search="onSearch">
        <a-button slot="enterButton">发送</a-button>
      </a-input-search>
    </a-layout-footer>
  </a-modal>
</template>

<script>
export default {
  name: "QAModal",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      messages: [
        {
          content:
            "你好！，这里是ProPainting的小助手，在使用过程中你有任何问题都可以来问我哦",
          role: true,
        },
      ],
    };
  },
  methods: {
    handleCancel(e) {
      this.messages = [
        {
          content:
            "你好！，这里是ProPainting的小助手，在使用过程中你有任何问题都可以来问我哦",
          role: true,
        },
      ];
      this.$emit("close");
    },
    async onSearch(value) {
      this.messages.push({ content: value, role: false });
      try {
        let res;
        res = await this.$axios.get("/qa", {
          params: {
            question: value,
          },
        });
        if (res.data.code === 200) {
          if(res.data.data===""){this.messages.push({ content: "不好意思这个问题我还需要再学一学哦", role: true });}
          else{this.messages.push({ content: res.data.data, role: true });}
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
  },
};
</script>

<style>
</style>