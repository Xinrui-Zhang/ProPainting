   <template>
  <div class="model">
    <a-row>
      <a-col :span="11">
        <!-- <iframe width="640" height="360" src="https://sketchfab.com/models/dGUrytaktlDeNudCEGKk31oTJY/embed" frameborder="0" allowfullscreen mozallowfullscreen="true" webkitallowfullscreen="true" onmousewheel=""></iframe> <p style="font-size: 13px; font-weight: normal; margin: 5px; color: #4A4A4A;"> <a href="https://sketchfab.com/models/dGUrytaktlDeNudCEGKk31oTJY?utm_source=oembed&utm_medium=embed&utm_campaign=dGUrytaktlDeNudCEGKk31oTJY" target="_blank" style="font-weight: bold; color: #1CAAD9;">Maison d&#39;artiste</a> by <a href="https://sketchfab.com/klaasnienhuis?utm_source=oembed&utm_medium=embed&utm_campaign=dGUrytaktlDeNudCEGKk31oTJY" target="_blank" style="font-weight: bold; color: #1CAAD9;">Klaas Nienhuis</a> on <a href="https://sketchfab.com?utm_source=oembed&utm_medium=embed&utm_campaign=dGUrytaktlDeNudCEGKk31oTJY" target="_blank" style="font-weight: bold; color: #1CAAD9;">Sketchfab</a> </p> -->
        <div v-for="model in modelList1" :key="model.name">
          <!-- <iframe
            width="640"
            height="360"
            :src="model.src"
            frameborder="0"
            allowfullscreen
            mozallowfullscreen="true"
            webkitallowfullscreen="true"
            onmousewheel
          ></iframe>-->
          <!-- <p style="font-size: 13px; font-weight: normal; margin: 5px; color: #4A4A4A;">
            <a
              :href="model.href"
              target="_blank"
              style="font-weight: bold; color: #1CAAD9;"
            >{{model.name}}</a>
          </p>-->
          <img :src="model.imgSrc" style="width:100%;" @click="clickPic(model)" />
          <p></p>
        </div>
      </a-col>
      <a-col :span="11" offset="1">
        <div v-for="model in modelList2" :key="model.name">
          <img :src="model.imgSrc" style="width:100%;" @click="clickPic(model)" />
          <!-- <iframe
            width="640"
            height="360"
            :src="model.src"
            frameborder="0"
            allowfullscreen
            mozallowfullscreen="true"
            webkitallowfullscreen="true"
            onmousewheel
          ></iframe>
          <p style="font-size: 13px; font-weight: normal; margin: 5px; color: #4A4A4A;">
            <a
              :href="model.href"
              target="_blank"
              style="font-weight: bold; color: #1CAAD9;"
            >{{model.name}}</a>
          </p>-->
          <p></p>
        </div>
      </a-col>
    </a-row>
    <a-modal
      :title="this.model.name"
      :visible="modelVisible"
      :footer="null"
      @cancel="closeModel"
      width="690px"
    >
      <iframe
        width="640"
        height="360"
        :src="this.model.src"
        frameborder="0"
        allowfullscreen
        mozallowfullscreen="true"
        webkitallowfullscreen="true"
        onmousewheel
      ></iframe>
    </a-modal>
  </div>
</template>

<script>
export default {
  name: "Model3D",
  data() {
    return {
      modelList1: [],
      modelList2: [],
      modelVisible: false,
      model: {
        name: "apple",
        src: "",
      },
      currentPage: 1,
    };
  },
  props: {
    url: {
      type: String,
      default: "/3dmodel",
    },
    query: {
      type: String,
      default: "",
    },
    flag: {
      type: Boolean,
      default: true,
    },
  },
  methods: {
    async loadModel(flag) {
      let res;
      try {
        res = await this.$axios.get(this.url, {
          params: {
            q: this.query,
            pageIndex: this.currentPage,
            pageSize: 30,
          },
        });
      } catch (err) {
        console.log(err);
        this.$message.error("网络错误");
      }
      console.log(res);
      if (res.data.totalPage === this.currentPage) this.isEnough = true;
      else this.isEnough = false;
      if (flag === false) {
        this.modelList1 = [];
        this.modelList2 = [];
      }
      for (let i = 0; i < res.data.data.length; i++) {
        let op = res.data.data[i];
        if (i % 2 === 0) {
          let model1 = {
            name: op.name,
            src: op.embedUrl,
            imgSrc: op.thumbnail,
          };
          this.modelList1.push(model1);
        } else {
          let model2 = {
            name: op.name,
            src: op.embedUrl,
            imgSrc: op.thumbnail,
          };
          this.modelList2.push(model2);
        }
      }
    },
    clickPic(model) {
      this.model.name = model.name;
      this.model.src = model.src;
      this.modelVisible = true;
    },
    closeModel() {
      this.modelVisible = false;
    },
  },
  mounted() {
    this.loadModel(false);
  },
  computed: {
    listenChange() {
      const { url, query } = this;
      return { url, query };
    },
  },
  watch: {
    listenChange(newV, oldV) {
      this.loadModel(this.flag);
    },
  },
};
</script>

<style>
.closeBtn {
  float: right;
  border: 0;
}
</style>
