<template>
  <div>
    <a-modal v-model="visible" title="色卡" :footer="null" @cancel="closeColorModal">
        <a-row v-for="color in colors" :key="color.index">
          <a-col :span="12">
              <p :style="color.sty">&nbsp;&nbsp;</p>
          </a-col>
          <a-col :span="12">
            <p :style="color.sty2">&nbsp;{{color.H+","+"rgb:"+color.rgb}}</p>
          </a-col>
        </a-row>
    </a-modal>
  </div>
</template>

<script>
export default {
  name: "ColorModal",
  data() {
    return {
      result: [],
      colors:[],
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    colorStr:{
      type: String,
      default: "",
    }
  },
  methods: {
    closeColorModal() {
      this.$emit("closeColorModal");
    },
    getImageColor() {
      this.result= this.colorStr.split("|");
      console.log(this.result);
      for(let i = 0; i < this.result.length; i++){
        let temp = this.result[i].split(",");
        console.log(temp);
        let color={
          H: temp[3],
          rgb:"("+temp[0]+","+temp[1]+","+temp[2]+")",
          sty:"background-color:"+temp[3],
          sty2:"color:"+temp[3],
        }
        this.colors[i]=color;
        // this.colors[i].H= this.result[i].slice(-7);
        // this.colors[i].rgb="("+this.result[i].substring(0,this.result[i].lastIndexOf(","))+")";
        // this.colors[i].sty="background-color:"+this.colors[i].H;
      }
      console.log(this.colors);
      },
  },
  created() {
    this.getImageColor();
  },
};
</script>

<style>
.closeBtn {
  float: right;
  border: 0;
}
</style>