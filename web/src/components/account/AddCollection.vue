<template >
  <div>
    <a-modal
      v-model="visible"
      @cancel="handleCancel"
      centered
      :closable="false"
      :maskClosable="false"
      wrapClassName="CollectionModal"
      :footer="null"
    >
      <p slot="title" class="title">
        <span>收藏</span>
        <a-button shape="circle" class="closeBtn" @click="handleCancel">
          <a-icon type="close" :style="{ fontSize: '20px'}" />
        </a-button>
      </p>
      <p>
        <input v-model="name" />
        <a-button @click="addNew">新增收藏夹</a-button>
      </p>
      <a-list item-layout="horizontal" :data-source="data">
        <a-list-item slot="renderItem" slot-scope="item, index">
          <a-list-item-meta :description="item.collection.intro">
            <span slot="title">{{ item.collection.title }}</span>
            <a-avatar
              slot="avatar"
              src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAYAAACtWK6eAAANk0lEQVR4Xu2da8hlVR3Gf/OhpIFKmAwTzbKLqN1IigLFMaWp7GJZfpAsK9CEsjKnVIgyKLPGLlaSUWpNHyIR7SqZqFlfulhNpYn5JTJHKfJDQXQ1/s4+9M5p3vesZ5291tl77WfBi4zvs26/Zz3vOvvstc/ZhIsJmMC6BDaZjQmYwPoEHBCvDhPYgIAD4uVhAg6I14AJ5BHwDpLHzbUmQsABmYjRnmYeAQckj5trTYSAAzIRoz3NPAIOSB4315oIAQdkIkZ7mnkEHJA8bq41EQJjD8hhwFOAw4EtE/FsitO8E7gDiP9WLWMKyPHANiBC8dQuFJur0nJnqyZwO/A+4IZaAxl6QA4GTgFeCxxTC4r7GTyBU4FraoxyqAF5WReKCMdjaoBwH6MjcCDwQOlRDzEgHwYuKD1xtz96ApcC55WexZACsh/wbeCE0pN2+00QuBWI69KiZSgBieuL7wCPLjpbN94Sgb/UePk9hICcC8R26WICKoHi67d4BwtmvBW4RaVivQl0BIqv3+IdbGDl42u8C+Gl1DSB4uu3eAcb2BM7R+wgLiaQS6D4+i3ewToz91u5uUvC9dYSKL5+i3ewDz/fAnzBPptADwSKr9/iHcxBeFR34OxJS8K5D4hzOWt/dottvh/4gFinNq+U4XkeKZQyNbUNPx34cuZYZ9V2ANuXbCOqe2H1ALHHJgbpR+2AXA+8KhNqnLs5E/hGZv35aoM0JGNunkcGtNQqNQPyLGBX6sDmdFcCce3SZ/HC6pPm8m0N0o+aAflgd5ZfRRln/+N0b99lkIZkTNLzyICWWqVmQOKJsCNTB7ZGF+ez/ppRb1EVL6xFhOr+fpB+1ApIPAH42wzeJR+MGaQhGYw8jwxoqVVqBeSl3Wnd1HGF7irgzUoFUeuFJQIrLB+kH7UC8nbgMhHwG4CdYh1FPkhDlAl0Ws8jA1pqlVoBiXBESJRyVOFPsfDCUtworx2kH7UCEg9Dxcus1BL3POKZ45JlkIZkTNjzyICWWqVWQOICPS7UU8t3gZekijN1XliZ4ApVG6QftQLykAg1zkhdJNZR5YM0RJ2Ej8xkEBOqOCACLKAWL2VUDrpCS9TWMtw7iGiMIHdABFiq1AHRiNXipYzKAVFoidpahnsHEY0R5A6IAEuVOiAasVq8lFE5IAotUVvLcO8gojGC3AERYKlSB0QjVouXMioHRKElamsZ7h1ENEaQOyACLFXqgGjEavFSRuWAKLREbS3DvYOIxghyB0SApUodEI1YLV7KqBwQhZaorWW4dxDRGEHugAiwVKkDohGrxUsZlQOi0BK1tQz3DiIaI8gdEAGWKnVANGK1eCmjckAUWqK2luHeQURjBLkDIsBSpQ6IRqwWL2VUDohCS9TWMtw7iGiMIHdABFiq1AHRiNXipYzKAVFoidpahnsHEY0R5A6IAEuVOiAasb54xRcJ/U3rel21A9ITyH0105fhi4boHeR/hJ4BXAzcBHxqEbiE3zsgCZByJQ6IRm5ZXvHlQRGOI7puPwO8G/iHNoy91A7IEvAWVV3W8EXtz37vHWRPEOLbfR85By2+/+SCJb5cyAFJXYUZOgdEg5bDK+p8Fjh7g67uBi4ErtWG87DaAcmAllolx/DUttfqprqDxBcGfQJ4cQK0/3Q7yUcTtGslDogITJE7IAot7ZMVTwFisR+mdcHngbOEOg6IAEuVOiAasVRecU0R1xu55ebuJVm89FpUHJBFhJb4farhS3TxcNUpvcT6Yk/fjPU74B3A1xfAd0CWXZ0b1HdANLgb8YqXUvF11cdpTS5Ux270kQ1UDshChPkCB0Rjtx6vk4DPAQdrzSWrvwScsY7aAUnGqAsdEI3ZvnidB3xMayZL/UPgNOD3c7UdkCycaZUckDROM9U8r76uN1JHcT/wRuDGNRUckFR6GToHRIM24/WE7qbeC7XqvanfueYclwPSG9b/b8gB0eAGrxcBXwUO0Kr2rr4CeKvvpPfOda8GHRCN79uAOGA4lHIbEPdM4jsdlVLLd2VMg9wJa4Fq5T6IYvhG2ngWJJ4J6aP8MWM3q+W7Mj8HRKA11G+5FaawrvQO4HzgxO5GYB9tqm04IInEaoHyDrLHkG924biz8+ec7iZgX7tJou3+tt6hgXJA9jw9+F7g73PmvLwLyVGppvWgq/WHURmqX2IJtFp6iRXXG/GS6rIN5h/H4uM4ySsERstIHZBEerVATXUHmV1vfCvBj0d0d+TjgGLpUst3ZR7eQQRaLewgcb3xHuAuYd4hrXFd4oAkmlIL1NR2kLjeiGfQ/53ow7wsDj9eApS6LqnluzJ97yACrbHuICnXG6kYng7sKHRd4oAkulAL1BR2kLje2A7Ep5T0WT5Z4H5JLd8VDt5BBFpj20HieuNc4B5hjoo0jrjE8+193S9xQBLp1wLV8g4Sf+Hflch7Gdk24NKerktq+a7M1zuIQGsMO0hcb8TDUpcL81pW+uTumPuy90sckEQnaoEa4g7yOuBriZzmZXG9EbvG9zLrL1stdpJ4SZdTYuzx+cBDK95BBEdq7CD7Aw8KY5pJ41NG4mZefOrIKks8C/LxjOuSGmxzuDggArVaJl4HnCyMK952jXeqhlLi4a04wqLcLzkeuHUoE1gzDgdEMKVWQF4D7AQ2LxhbXG/EY67xqYdDK4d01yWvThjYV4DTE3SrkDggAvVaAYkhbQWuBg5dZ3w/6+6KD/Gv7tohx533ONqyXqn1bptg815SB0QgVzMgMazndCdu4+XWft047+3+MsdLmGW+v0OY9tLSY7vro/hc4FnZBcRLyYuWbr1sAw6IwLd2QGZDi+/uOBrYDdw3omDMo90CHNT9z18J3FcpdUAE+qsKiDBES3sm4IAIQB0QAVYjUgdEMNIBEWA1InVABCMdEAFWI1IHRDDSARFgNSJ1QAQjHRABViNSB0Qw0gERYDUidUAEIx0QAVYjUgdEMNIBEWA1InVABCMdEAFWI1IHRDDSARFgNSJ1QAQjHRABViNSB0Qw0gERYDUidUAEIx0QAVYjUgdEMNIBEWA1InVABCMdEAFWI1IHRDDSARFgNSJ1QAQjHRABViNSB0Qw0gERYDUidUAEIx0QAVYjUgdEMNIBEWA1InVABCMdEAFWI1IHRDDSARFgNSJ1QAQjHRABViNSB0Qw0gERYDUidUAEIx0QAVYjUgdEMNIBEWA1InVABCMdEAFWI1IHRDDSARFgNSJ1QAQjHRABViNSB0Qw0gERYDUidUAEIx0QAVYjUgdEMNIBEWA1InVABCMdEAFWI1IHRDDSARFgNSJ1QAQjHRABViNSB0Qw0gERYDUidUAEIx0QAVYjUgdEMNIBEWA1InVABCMdEAFWI1IHRDDSARFgNSJ1QAQjHRABViNSB0Qw0gERYDUidUAEIx0QAVYjUgdEMNIBEWA1InVABCMdEAFWI1IHRDDSARFgNSJ1QAQjHRABViNSB0Qw0gERYDUidUAEIyMg3xf0lo6fwHFA+K6UTYo4R1u8g25QD+UMznVMYAGB4uu3eAcOiBd5QQLF12/xDhyQgsvDTRdfv8U7cEC8igsSKL5+i3fggBRcHm66+Pot3oED4lVckEDx9Vu8Awek4PJw08XXb/EOHBCv4oIEiq/f4h04IAWXh5suvn6Ld+CAeBUXJFB8/RbvwAEpuDzcdPH1W7wDB8SruCCB4uu3eAc9B+R+4Bfdzy4g/u3SBgEfVlzSxx3A9iXbcPXhEvBx90xvHgBeCfw4s76rjYOAA5Lh05+BLRn1XGV8BByQDM+eD/wko56rjI+AAyJ6dglwvljH8vEScEBE754H/FSsY/l4CTggondx7RHXIC7TIOCACD7HO1cHCnpLx0/AARE8/BHwAkFv6fgJTDogfwAOEjy8Gzhc0Fs6fgKTDshtwLGCh/8CNgP/FOpYOm4Ckw7IVcAZon9HAr8R61g+XgKTDkjO5OPcVZy/cpkGgZw1UvywbfEOOm/fBFyZ4fMRwF0Z9VxlfAQmHZATgJsyPPsl8OyMeq4yPgKTDkjY9QPgmAzfPg2ck1HPVcZFYPIBORu4PNOze4CzgJsz67va8AlMPiCPBeIl0xOX8Op64NfAz4G4mRj3V1zaIDD5gISNF/uEbhureQCzuBc4pPQ4ar2LNZvHM7tdpPS83H77BK4BTi09zdoBifnkbKWlObj98RGocp9sFQEJK24Bto7PE494QAS2ATeWHs+qArI/8GDpybn9pgnE4xDxWETRsqqAxKROBq4rOjs33iqBeLPnwhqTW2VAYn5nAlfUmKj7aIZAvMX/3FqzWXVAYp6HAtcCR9eatPsZNYGnAXHjuEoZQkBmE/1QrW2zCll3UoJAHDmKo0fVypACEpN+fXekJOfMVjVo7qg6gT8BceA1TmJULUMLyGzyJwGndT9VgbizwRFY6WcyDzUgM5fiznsE5cTuWuWAwdnnAZUgEJ9JcDtwA7CzRAepbQ49IPPzeFwXlLiwj0OPcQDSpR0CEYr42T2UKY0tIEPh5nFMhIADMhGjPc08Ag5IHjfXmggBB2QiRnuaeQQckDxurjURAg7IRIz2NPMIOCB53FxrIgQckIkY7WnmEXBA8ri51kQIOCATMdrTzCPggORxc62JEHBAJmK0p5lH4L9oX8nnRx0hZwAAAABJRU5ErkJggg=="
            />
          </a-list-item-meta>
          <a-button style="border:0" @click="sure(item)" shape="circle" size="large">
            <a-icon type="plus" style="width:20px;height:20px;"></a-icon>
          </a-button>
        </a-list-item>
      </a-list>
    </a-modal>
  </div>
</template>

<script>
export default {
  components: {},
  name: "addCollection",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    pid: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      message: "确定",
      screenWidth: document.body.clientWidth,
      data: [],
      name: "",
      value: 0,
    };
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
    handleCancel() {
      this.$emit("close");
    },
    async addNew() {
      try {
        let res = await this.$axios.post("/create", {
          title: this.name,
          visibility: "1",
          intro: "简介",
        });
        console.log(res.data);
        if (res.data.code !== 200) {
          throw res.data.msg;
        } else {
          await this.loadCollection();
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
    async loadCollection() {
      try {
        let res = await this.$axios.get(
          "/collection?pageIndex=1&pageSize=1000"
        );
        console.log(res.data.data);
        if (res.data.code !== 200) {
          throw res.data.msg;
        } else {
          this.data = res.data.data;
        }
      } catch (err) {
        if (typeof err === "string") {
          this.$message.error(err);
        } else {
          console.log(err);
          this.$message.error(网络错误);
        }
      }
    },
    async sure(item) {
      try {
        let res = await this.$axios.post("/collection", {
          pid: this.pid,
          cid: item.collection.cid,
          type: "1",
        });
        if (res.data.code !== 200) {
          throw res.data.msg;
        } else {
          this.$emit("collect");
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
  },
  async created() {
    await this.loadCollection();
  },
};
</script>
  
<style lang="less" scoped>
.title {
  font-size: 22px;
  margin: 0;
  height: 100%;
  width: 100%;
  .closeBtn {
    float: right;
    border: 0;
  }
}
</style>