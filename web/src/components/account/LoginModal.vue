<template >
  <div>
    <a-modal
      v-if="this.screenWidth >=576"
      :width="loginModalWidth"
      v-model="visible"
      @cancel="handleCancel"
      centered
      :closable="closable"
      :maskClosable="maskClosable"
      wrapClassName="loginModal"
      :destroyOnClose="destroyOnClose"
    >
      <p slot="title" class="title">
        <span>登录</span>
        <a-button shape="circle" class="closeBtn" @click="handleCancel">
          <a-icon type="close" :style="{ fontSize: '20px'}" />
        </a-button>
      </p>
      <template slot="footer">
        <div>
          <p class="register">
            没有账号？
            <a-button type="link" class="toRegister" @click="register()">快点击注册吧！</a-button>
          </p>
        </div>
      </template>
      <div class="loginBody">
        <a-form-model ref="ruleForm" :model="user" :rules="rules">
          <p class="emailLabel">电子邮箱</p>
          <a-form-model-item ref="email" prop="email" class="inputs">
            <div>
              <a-input
                type="email"
                size="large"
                v-model="user.email"
                @blur="() => {$refs.email.onFieldBlur()}"
                @change="() => {$refs.email.onFieldChange()}"
              >
                <a-icon slot="suffix" @click="clearEmail" type="close-circle"></a-icon>
              </a-input>
            </div>
          </a-form-model-item>
          <p class="passwordLabel">
            密码
            <a-button type="link" class="forget" @click="forget()">忘记密码？</a-button>
          </p>
          <a-form-model-item ref="password" prop="password" class="inputs">
            <div>
              <a-input-password
                size="large"
                v-model="user.password"
                @blur="() => {$refs.password.onFieldBlur()}"
                @change="() => {$refs.password.onFieldChange()}"
              ></a-input-password>
            </div>
          </a-form-model-item>
          <a-button
            type="primary"
            class="loginBtn"
            size="large"
            @click="login()"
            :loading="confirmLoading"
          >{{message}}</a-button>
        </a-form-model>
      </div>
    </a-modal>
    <a-modal
      v-if="this.screenWidth <576"
      width="100%"
      v-model="visible"
      @cancel="handleCancel"
      centered
      :maskClosable="maskClosable"
      wrapClassName="mobileLoginModal"
      :destroyOnClose="destroyOnClose"
    >
      <span slot="title" class="title">登录</span>
      <template slot="footer">
        <div>
          <p class="register">
            没有账号？
            <a-button type="link" class="toRegister" @click="register()">快点击注册吧！</a-button>
          </p>
        </div>
      </template>
      <div class="loginBody">
        <a-form-model ref="ruleForm" :model="user" :rules="rules">
          <p class="emailLabel">电子邮箱</p>
          <a-form-model-item ref="email" prop="email" class="inputs">
            <div>
              <a-input
                type="email"
                size="large"
                v-model="user.email"
                @blur="() => {$refs.email.onFieldBlur()}"
                @change="() => {$refs.email.onFieldChange()}"
              >
                <a-icon slot="suffix" @click="clearEmail" type="close-circle"></a-icon>
              </a-input>
            </div>
          </a-form-model-item>
          <p class="passwordLabel">
            密码
            <a-button type="link" class="forget" @click="forget()">忘记密码？</a-button>
          </p>
          <a-form-model-item ref="password" prop="password" class="inputs">
            <div>
              <a-input-password
                size="large"
                v-model="user.password"
                @blur="() => {$refs.password.onFieldBlur()}"
                @change="() => {$refs.password.onFieldChange()}"
              ></a-input-password>
            </div>
          </a-form-model-item>
          <a-button
            type="primary"
            class="loginBtn"
            size="large"
            @click="login()"
            :loading="confirmLoading"
          >{{message}}</a-button>
        </a-form-model>
      </div>
    </a-modal>
  </div>
</template>

<script>
export default {
  components: {},
  name: "LoginModal",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      message: "登录",
      loginModalWidth: "400px",
      maskClosable: false,
      screenWidth: document.body.clientWidth,
      confirmLoading: false,
      closable: false,
      user: {
        email: undefined,
        password: undefined,
      },
      rules: {
        email: [
          {
            required: true,
            message: "请输入电子邮箱",
            trigger: ["blur", "change"],
          },
          {
            pattern: "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$",
            message: "邮箱格式不正确",
            trigger: ["blur"],
          },
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: ["blur", "change"],
          },
          {
            whitespace: true,
            message: "请勿输入空格",
            trigger: ["blur"],
          },
        ],
      },
      destroyOnClose: true,
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
        // this.screenWidth >= 556
        //   ? (this.loginModalWidth = "400px")
        //   : (this.loginModalWidth = "90%");
      })();
    };
  },
  methods: {
    // 跳转注册
    register() {
      this.$emit("register");
    },
    // 登录操作
    async login() {
      this.message = "";
      this.confirmLoading = true;
      await this.$refs.ruleForm.validate(async (valid) => {
        if (valid) {
          try {
            // 登录请求
            let res = await this.$axios.post("/account/login", {
              email: this.user.email,
              password: this.user.password,
            });
            if (res.data.code === 200) {
              this.$store.commit("token", res.data.data);
              this.$store.commit("hasLogin", true);
              window.location.reload();
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
          } finally {
            this.message = "登录";
            this.confirmLoading = false;
          }
        } else {
          this.message = "登录";
          this.confirmLoading = false;
          return false;
        }
      });
    },
    // 找回密码
    forget() {
      this.$emit("reset");
    },
    // 清除电子邮箱输入
    clearEmail() {
      this.user.email = "";
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
        this.login();
      }
    },
    enterKeyupDestroyed() {
      document.removeEventListener("keyup", this.enterKey);
    },
    enterKeyup() {
      document.addEventListener("keyup", this.enterKey);
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
.loginModal {
  .register {
    text-align: center;
    margin: 0 auto;
    width: 80%;
    .toRegister {
      color: #1caad9;
      padding: 0;
    }
  }
  .loginBody {
    margin: 30px auto;
    height: 280px;
    .inputs {
      text-align: left;
      margin: 0 auto;
      width: 80%;
    }
    .emailLabel {
      margin-left: 35px;
      margin-top: 20px;
      margin-bottom: 8px;
      font-size: 16px;
    }
    .passwordLabel {
      margin-left: 35px;
      margin-top: 20px;
      margin-bottom: 0;
      font-size: 16px;
      .forget {
        color: gray;
        font-size: 8px;
        float: right;
        margin-right: 15px;
      }
    }
    .loginBtn {
      display: block;
      margin: 50px auto;
      padding-left: 70px;
      padding-right: 70px;
    }
  }
}
.mobileLoginModal {
  height: 100%;
  .register {
    text-align: center;
    margin: 0 auto;
    width: 80%;
    .toRegister {
      color: #1caad9;
      padding: 0;
    }
  }
  .loginBody {
    margin: 30px auto;
    height: 280px;
    .inputs {
      text-align: left;
      margin: 0 auto;
      width: 80%;
    }
    .emailLabel {
      margin-left: 35px;
      margin-top: 20px;
      margin-bottom: 8px;
      font-size: 16px;
    }
    .passwordLabel {
      margin-left: 35px;
      margin-top: 20px;
      margin-bottom: 0;
      font-size: 16px;
      .forget {
        color: gray;
        font-size: 8px;
        float: right;
        margin-right: 15px;
      }
    }
    .loginBtn {
      display: block;
      margin: 50px auto;
      padding-left: 70px;
      padding-right: 70px;
    }
  }
}
</style>