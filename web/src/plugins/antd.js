import Vue from 'vue';
import 'ant-design-vue/dist/antd.css';
import {
  Avatar,
  BackTop,
  Button,
  Card,
  Checkbox,
  Descriptions,
  Divider,
  Dropdown,
  Form,
  FormModel,
  Icon,
  Input,
  Layout,
  Menu,
  message,
  Modal,
  Row, Col,
  Select,
  Statistic,
  Spin,
  List,
  Upload,
  Pagination,
  Radio
}
  from 'ant-design-vue';
Vue.use(BackTop)
Vue.use(Input);
Vue.use(Layout);
Vue.use(Row);
Vue.use(Col);
Vue.use(Dropdown);
Vue.use(Menu);
Vue.use(Select);
Vue.use(Icon);
Vue.use(Button);
Vue.use(Modal);
Vue.use(FormModel);
Vue.use(Upload);
Vue.use(Form);
Vue.use(Checkbox);
Vue.use(Avatar);
Vue.use(Divider)
Vue.use(Statistic)
Vue.use(Card)
Vue.use(Descriptions)
Vue.use(Spin)
Vue.use(Pagination)
Vue.use(List)
Vue.use(Radio)
Vue.prototype.$form = Form
Vue.prototype.$message = message
const IconFont = Icon.createFromIconfontCN({
  scriptUrl: "//at.alicdn.com/t/font_1952755_6xn7dw6z42c.js"
});
Vue.use(IconFont)