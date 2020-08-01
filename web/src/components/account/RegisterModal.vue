<template >
  <div>
    <a-modal
      v-if="this.screenWidth >=576"
      :width="registerModalWidth"
      v-model="visible"
      @cancel="handleCancel"
      centered
      :closable="closable"
      :maskClosable="maskClosable"
      wrapClassName="registerModal"
      :destroyOnClose="destroyOnClose"
      :footer="null"
    >
      <p slot="title" class="title">
        <span>注册</span>
        <a-button shape="circle" class="closeBtn" @click="handleCancel">
          <a-icon type="close" :style="{ fontSize: '20px'}" />
        </a-button>
      </p>
      <div class="registerBody">
        <a-form
          :form="form"
          name="注册"
          :scrollToFirstError="scrollToFirstError"
          hideRequiredMark
          :label-col="{span:6}"
          :wrapper-col="{span:16}"
        >
          <a-form-item label="电子邮箱" hasFeedback>
            <a-input v-decorator="['email',{rules:rules.email}]" />
          </a-form-item>
          <a-form-item label="密码" hasFeedback>
            <a-input-password v-decorator="['password',{rules:rules.password}]" />
          </a-form-item>
          <a-form-item label="确认密码" :dependeccies="['password']" hasFeedback>
            <a-input-password v-decorator="['confirm',{rules:rules.confirm}]" />
          </a-form-item>
          <a-form-item label="昵称" hasFeedback>
            <a-input v-decorator="['name',{rules:rules.name}]" />
          </a-form-item>
          <a-form-item label="手机号" hasFeedback>
            <a-input v-decorator="['phoneNum',{rules:rules.phoneNum}]" />
          </a-form-item>
          <a-form-item label="验证码" hasFeedback>
            <a-row :gutter="10">
              <a-col :span="13">
                <a-input v-decorator="['captcha',{rules:rules.captcha}]" />
              </a-col>
              <a-col :span="2">
                <a-button @click="sendCaptcha" :disabled="!disabledCodeBtn">{{codeText}}</a-button>
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item :label-col="{span: 8}" :wrapper-col="{span:24}">
            <a-button type="primary" @click="register" class="registerBtn">{{message}}</a-button>
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
      wrapClassName="mobileRegisterModal"
      :destroyOnClose="destroyOnClose"
      :footer="null"
    >
      <p slot="title" class="title">
        <span>注册</span>
        <a-button shape="circle" class="closeBtn" @click="handleCancel">
          <a-icon type="close" :style="{ fontSize: '20px'}" />
        </a-button>
      </p>
      <div class="registerBody">
        <a-form
          :form="form"
          name="注册"
          :scrollToFirstError="scrollToFirstError"
          hideRequiredMark
          :label-col="{span:24}"
          :wrapper-col="{span:24}"
        >
          <a-form-item label="电子邮箱" hasFeedback>
            <a-input v-decorator="['email',{rules:rules.email}]" />
          </a-form-item>
          <a-form-item label="密码" hasFeedback>
            <a-input-password v-decorator="['password',{rules:rules.password}]" />
          </a-form-item>
          <a-form-item label="确认密码" :dependeccies="['password']" hasFeedback>
            <a-input-password v-decorator="['confirm',{rules:rules.confirm}]" />
          </a-form-item>
          <a-form-item label="昵称" hasFeedback>
            <a-input v-decorator="['name',{rules:rules.name}]" />
          </a-form-item>
          <a-form-item label="手机号" hasFeedback>
            <a-input v-decorator="['phoneNum',{rules:rules.phoneNum}]" />
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
          <a-form-item :label-col="{span: 8}" :wrapper-col="{span:24}">
            <a-button type="primary" @click="register" class="registerBtn">{{message}}</a-button>
          </a-form-item>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script>
import axios from "axios";
export default {
  components: {},
  name: "registerModal",
  props: {
    visible: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      message: "注册",
      registerModalWidth: "400px",
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
            trigger: "blur",
          },
        ],
        name: [
          {
            required: true,
            message: "未输入昵称",
            whitespace: true,
          },
        ],
        phoneNum: [{ required: true, message: "未输入手机号" }],
        captcha: [{ required: true, message: "未输入验证码" }],
      },
      form: this.$form.createForm(this, { name: "registerForm" }),
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
    // 注册操作
    async register() {
      try {
        await this.form.validateFields();
      } catch (err) {
        return;
      }
      let res;
      try {
        res = await this.$axios.post("/account", {
          email: await this.form.getFieldValue("email"),
          password: await this.form.getFieldValue("password"),
          verifyCode: await this.form.getFieldValue("captcha"),
          name: await this.form.getFieldValue("name"),
          phoneNum: await this.form.getFieldValue("phoneNum"),
        });
        if (res.data.code === 200) {
          this.$message.success("注册成功！");
          this.$emit("login");
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
      this.$emit("close");
    },
    enterKey(event) {
      const code = event.keyCode
        ? event.keyCode
        : event.which
        ? event.which
        : event.charCode;
      if (code == 13) {
        this.register();
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
        // res = await this.$axios.post("/account/verifyCode", {
        //   email: this.form.getFieldValue("email"),
        //   purpose: 1,
        // });
        res = await axios.post(
          "http://paranoid-root.com/api/account/verifyCode",
          {
            email: this.form.getFieldValue("email"),
            purpose: 1,
          }
        );
        this.countDown(60);
      } catch (err) {
        console.log(err);
        this.$message.error("网络错误");
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
.registerModal {
  .register {
    text-align: center;
    margin: 0 auto;
    width: 80%;
  }
  .registerBody {
    margin: 30px auto;
    height: 440px;
    .registerBtn {
      display: block;
      margin: 0 auto;
      padding-left: 70px;
      padding-right: 70px;
    }
  }
}
.mobileRegisterModal {
  height: 100%;
  .register {
    text-align: center;
    margin: 0 auto;
    width: 80%;
  }
  .registerBody {
    margin: 30px auto;
    .registerBtn {
      display: block;
      margin: 0 auto;
      padding-left: 70px;
      padding-right: 70px;
    }
  }
}
</style>