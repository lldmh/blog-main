import Vue from 'vue'
import Vuex from 'vuex'
// import * as getters from './getters.js'

Vue.use(Vuex)

/** 状态定义 */
export const state = {
  loading: false,
  themeObj: 0,//主题
  keywords:'',//关键词
  errorImg: 'this.onerror=null;this.src="' + require('../../static/img/tou.jpg') + '"',
  baseURL:'http://localhost:7777/'
  // baseURL:'http://103.152.132.184:7777/'
  // baseURL:'http://10.5.151.19:7777/'
}

export default new Vuex.Store({
    state,
})
