<template>
  <a-row>
    <CollectionModal :visible="ModalVisible" :cid="cid" :title="title" @closeModal="closeModal"></CollectionModal>
    <a-col :span="7" offset="1">
      <div v-for="collection in collectionList1" :key="collection.index">
        <div
          v-if="collection.isAlone"
          @click="clickCollection(collection)"
          style="border-style:solid; border-width:5px; border-color:#5e7987;"
        >
          <div
            style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
          >
            <img
              :src="collection.coverUrl[0]"
              style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
            />
          </div>
        </div>
        <div
          v-else
          @click="clickCollection(collection)"
          style="border-style:solid; border-width:5px; border-color:#5e7987;"
        >
          <a-row>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[0]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[1]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[2]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[3]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
          </a-row>
        </div>
        <p style="text-align: center;">{{collection.title}}</p>
        <br />
      </div>
    </a-col>
    <a-col :span="7" offset="1">
      <div v-for="collection in collectionList2" :key="collection.title">
        <div
          v-if="collection.isAlone"
          @click="clickCollection(collection)"
          style="border-style:solid; border-width:5px; border-color:#5e7987;"
        >
          <div
            style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
          >
            <img
              :src="collection.coverUrl[0]"
              style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
            />
          </div>
        </div>
        <div
          v-else
          @click="clickCollection(collection)"
          style="border-style:solid; border-width:5px; border-color:#5e7987;"
        >
          <a-row>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[0]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[1]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[2]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[3]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
          </a-row>
        </div>
        <p style="text-align: center;">{{collection.title}}</p>
        <br />
      </div>
    </a-col>
    <a-col :span="7" offset="1">
      <div v-for="collection in collectionList3" :key="collection.title">
        <div
          v-if="collection.isAlone"
          @click="clickCollection(collection)"
          style="border-style:solid; border-width:5px; border-color:#5e7987;"
        >
          <div
            style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
          >
            <img
              :src="collection.coverUrl[0]"
              style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
            />
          </div>
        </div>
        <div
          v-else
          @click="clickCollection(collection)"
          style="border-style:solid; border-width:5px; border-color:#5e7987;"
        >
          <a-row>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[0]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[1]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[2]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
            <a-col :span="12">
              <div
                style="position:relative;
                width:100%;
                height:0;
                padding-top:100%;"
              >
                <img
                  :src="collection.coverUrl[3]"
                  style="position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;"
                />
              </div>
            </a-col>
          </a-row>
        </div>
        <p style="text-align: center;">{{collection.title}}</p>
        <br />
      </div>
    </a-col>
    <a-button v-if="isEnough===false" @click="moreColl" type="default" block>
      点击查看更多
      <a-icon type="down"></a-icon>
    </a-button>
  </a-row>
</template>

<script>
import CollectionModal from "./CollectionModal";
export default {
  title: "Collection",
  components: {
    CollectionModal,
  },
  data() {
    return {
      collectionList1: [],
      collectionList2: [],
      collectionList3: [],
      ModalVisible: false,
      cid: "1",
      title: "",
      currentPage: 1,
      isEnough: false,
    };
  },
  props: {
    query: {
      type: String,
      default: "",
    },
  },
  computed: {
    listenChange() {
      const { query } = this;
      return { query };
    },
  },
  watch: {
    listenChange(newV, oldV) {
      this.loadCollection(false);
    },
  },
  methods: {
    async loadCollection(flag) {
      let res;
      try {
        if (this.$route.query.id == "/collection") {
          res = await this.$axios.get(this.$route.query.id, {
            params: {
              pageIndex: this.currentPage,
              pageSize: 30,
            },
          });
          var arr = res.data.data;
        } else if (this.$route.query.id == "/collection/collection") {
          res = await this.$axios.get("/collection/collection", {
            params: {
              pageIndex: this.currentPage,
              pageSize: 30,
            },
          });
          var arr = res.data.data.pageList;
          if (res.data.totalPage === this.currentPage) this.isEnough = true;
          else this.isEnough = false;
        } else {
          res = await this.$axios.get("/collection/outline", {
            params: {
              q: this.query,
              pageIndex: this.currentPage,
              pageSize: 30,
            },
          });
          var arr = res.data.data.pageList;
          if (res.data.totalPage === this.currentPage) this.isEnough = true;
          else this.isEnough = false;
        }
        console.log(res);
        console.log(arr);
        if (res.data.code !== 200 && res.data.data) {
          throw res.data.msg;
        }
        if (flag === false) {
          this.collectionList1 = [];
          this.collectionList2 = [];
          this.collectionList3 = [];
          this.currentPage = 1;
        }
        for (let i = 0; i < arr.length; ) {
          let op = arr[i];
          let collection1 = {
            title: op.title,
            coverUrl: op.urls.split("|"),
            isAlone: false,
            cid: op.cid,
          };
          if (op.urls.split("|").length === 1) collection1.isAlone = true;
          this.collectionList1.push(collection1);
          i++;
          op = arr[i];
          let collection2 = {
            title: op.title,
            coverUrl: op.urls.split("|"),
            isAlone: false,
            cid: op.cid,
          };
          if (op.urls.split("|").length === 1) collection2.isAlone = true;
          this.collectionList2.push(collection2);
          i++;
          op = arr[i];
          let collection3 = {
            title: op.title,
            coverUrl: op.urls.split("|"),
            isAlone: false,
            cid: op.cid,
          };
          if (op.urls.split("|").length === 1) collection3.isAlone = true;
          this.collectionList3.push(collection3);
          i++;
        }
      } catch (err) {
        // if (typeof err === "string") {
        //   this.$message.error(err);
        // } else {
        //   console.log(err);
        //   this.$message.error("网络错误");
        // }
        return;
      }
    },
    clickCollection(collection) {
      this.cid = collection.cid;
      this.ModalVisible = true;
      this.title = collection.title;
    },
    closeModal() {
      this.ModalVisible = false;
    },
    moreColl() {
      this.currentPage++;
      this.flag = true;
      this.loadCollection(true);
    },
  },
  mounted() {
    this.loadCollection(true);
  },
};
</script>

<style>
</style>