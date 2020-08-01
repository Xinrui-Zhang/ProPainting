<template >
  <div>
    <a-modal
      v-if="this.screenWidth >=576"
      :width="resetPwdModalWidth"
      v-model="visible"
      @cancel="handleCancel"
      centered
      :closable="closable"
      :maskClosable="maskClosable"
      wrapClassName="resetPwdModal"
      :destroyOnClose="destroyOnClose"
      :footer="null"
    >
      <p slot="title" class="title">
        <span>修改密码</span>
        <a-button shape="circle" class="closeBtn" @click="handleCancel">
          <a-icon type="close" :style="{ fontSize: '20px'}" />
        </a-button>
      </p>
      <div class="resetPwdBody">
        <a-form
          :form="form"
          name="修改密码"
          :scrollToFirstError="scrollToFirstError"
          hideRequiredMark
          :label-col="{span:6}"
          :wrapper-col="{span:16}"
        >
          <a-form-item label="电子邮箱" hasFeedback>
            <a-input v-decorator="['email',{rules:rules.email}]" />
          </a-form-item>
          <a-form-item label="验证码" hasFeedback>
            <a-row :gutter="10">
              <a-col :span="13">
                <a-form-item :label-col="{span: 8}" :wrapper-col="{span:24}">
                  <a-input v-decorator="['captcha',{rules:rules.captcha}]" />
                </a-form-item>
              </a-col>
              <a-col :span="2">
                <a-button @click="sendCaptcha" :disabled="!disabledCodeBtn">{{codeText}}</a-button>
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item label="密码" hasFeedback>
            <a-input-password v-decorator="['password',{rules:rules.password}]" />
          </a-form-item>
          <a-form-item label="确认密码" :dependeccies="['password']" hasFeedback>
            <a-input-password v-decorator="['confirm',{rules:rules.confirm}]" />
          </a-form-item>

          <a-form-item :label-col="{span: 8}" :wrapper-col="{span:24}">
            <a-button type="primary" @click="resetPwd" class="resetPwdBtn">{{message}}</a-button>
          </a-form-item>
        </a-form>
      </div>
    </a-modal>
    <a-modal
      v-if="this.screenWidth <576"
      width="100%"
      v-model="visible"
      @cancel="handleCancel"
      centered
      :closable="closable"
      :maskClosable="maskClosable"
      wrapClassName="mobileresetPwdModal"
      :destroyOnClose="destroyOnClose"
      :footer="null"
    >
      <p slot="title" class="title">
        <span>修改密码</span>
        <a-button shape="circle" class="closeBtn" @click="handleCancel">
          <a-icon type="close" :style="{ fontSize: '20px'}" />
        </a-button>
      </p>
      <div class="resetPwdBody">
        <a-form
          :form="form"
          name="修改密码"
          :scrollToFirstError="scrollToFirstError"
          hideRequiredMark
          :label-col="{span:24}"
          :wrapper-col="{span:24}"
        >
          <a-form-item label="电子邮箱" hasFeedback>
            <a-input v-decorator="['email',{rules:rules.email}]" />
          </a-form-item>
          <a-form-item label="验证码" hasFeedback>
            <a-row :gutter="10">
              <a-col :span="13">
                <a-form-item :label-col="{span: 8}" :wrapper-col="{span:24}">
                  <a-input v-decorator="['captcha',{rules:rules.captcha}]" />
                </a-form-item>
              </a-col>
              <a-col :span="2">
                <a-button @click="sendCaptcha" :disabled="!disabledCodeBtn">{{codeText}}</a-button>
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item label="密码" hasFeedback>
            <a-input-password v-decorator="['password',{rules:rules.password}]" />
          </a-form-item>
          <a-form-item label="确认密码" :dependeccies="['password']" hasFeedback>
            <a-input-password v-decorator="['confirm',{rules:rules.confirm}]" />
          </a-form-item>

          <a-form-item :label-col="{span: 8}" :wrapper-col="{span:24}">
            <a-button type="primary" @click="resetPwd" class="resetPwdBtn">{{message}}</a-button>
          </a-form-item>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script>
export default {
  components: {},
  name: "resetPwdModal",
  props: {
    visible: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      message: "确认",
      resetPwdModalWidth: "400px",
      maskClosable: false,
      screenWidth: document.body.clientWidth,
      confirmLoading: false,
      closable: false,
      destroyOnClose: true,
      scrollToFirstError: true,
      rules: {
        email: [
          {
            type: "email",
            message: "不符合电子邮箱格式",
          },
          {
            required: true,
            message: "未输入电子邮箱",
          },
        ],
        password: [
          {
            required: true,
            message: "未输入密码",
          },
        ],
        confirm: [
          {
            required: true,
            message: "未输入确认密码",
          },
          {
            validator: (rule, value) => {
              if (!value || this.form.getFieldValue("password") === value) {
                return Promise.resolve();
              }
              return Promise.reject("与密码不符");
            },
          },
        ],
        captcha: [{ required: true, message: "未输入验证码" }],
      },
      form: this.$form.createForm(this, { name: "resetPwdForm" }),
      disabledCodeBtn: true,
      codeText: "获取验证码",
    };
  },
  created() {
    // 绑定enter事件
    this.enterKeyup();
  },
  destroyed() {
    // 销毁enter事件
    this.enterKeyupDestroyed();
  },
  mounted() {
    const that = this;
    window.onresize = () => {
      return (() => {
        window.screenWidth = document.body.clientWidth;
        that.screenWidth = window.screenWidth;
      })();
    };
  },
  methods: {
    // 修改密码操作
    resetPwd() {
      this.form.validateFields();
      console.log(this.form.getFieldValue("email"));
      console.log(this.form.getFieldValue("password"));
      console.log(this.form.getFieldValue("name"));
      console.log(this.form.getFieldValue("phoneNum"));
      console.log(this.form.getFieldValue("captcha"));
    },
    handleCancel() {
      this.$emit("close");
    },
    enterKey(event) {
      const code = event.keyCode
        ? event.keyCode
        : event.which
        ? event.which
        : event.charCode;
      if (code == 13) {
        this.resetPwd();
      }
    },
    enterKeyupDestroyed() {
      document.removeEventListener("keyup", this.enterKey);
    },
    enterKeyup() {
      document.addEventListener("keyup", this.enterKey);
    },
    async sendCaptcha() {
      let res;
      try {
        res = await this.$axios.post("/account/verifyCode", {
          email: this.form.getFieldValue("email"),
          purpose: 2,
        });
        console.log(res);
      } catch (err) {
        console.log(err);
        this.$message.error("网络错误");
      }
      if (res) {
        this.countDown(60);
      }
    },
    countDown(time) {
      if (time === 0) {
        this.disabledCodeBtn = true;
        this.codeText = "获取验证码";
        return;
      } else {
        this.disabledCodeBtn = false;
        this.codeText = "重新发送(" + time + ")";
        time--;
      }
      setTimeout(() => {
        this.countDown(time);
      }, 1000);
    },
  },
};
</script>

<style lang="less" scoped>
.title {
  font-size: 25px;
  margin: 0;
  .closeBtn {
    float: right;
    border: 0;
  }
}
.resetPwdModal {
  .resetPwd {
    text-align: center;
    margin: 0 auto;
    width: 80%;
  }
  .resetPwdBody {
    margin: 30px auto;
    .resetPwdBtn {
      display: block;
      margin: 0 auto;
      padding-left: 70px;
      padding-right: 70px;
    }
  }
}
.mobileresetPwdModal {
  height: 100%;
  .resetPwd {
    text-align: center;
    margin: 0 auto;
    width: 80%;
  }
  .resetPwdBody {
    margin: 30px auto;
    .resetPwdBtn {
      display: block;
      margin: 0 auto;
      padding-left: 70px;
      padding-right: 70px;
    }
  }
}
</style>