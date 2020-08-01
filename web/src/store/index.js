import Vue from 'vue'
import Vuex from 'vuex'
import VuexPersistence from 'vuex-persist'

const vuexLocalStorage = new VuexPersistence({
  storage: window.localStorage
})

Vue.use(Vuex)

export default new Vuex.Store({
  plugins: [vuexLocalStorage.plugin],
  state: {
    token: '',
    hasUnfinishedRoute: false,
    unfinishedRoute: {},
    user: {},
    hasLogin: false,
    isAdmin: false
  },
  mutations: {
    saveUnfinishedRoute(state, payload) {
      state.hasUnfinishedRoute = true
      state.unfinishedRoute = payload
    },
    clearUnfinishedRoute(state) {
      state.hasUnfinishedRoute = false
    },
    clearCache(state) {
      state.token = '',
        state.hasUnfinishedRoute = false,
        state.unfinishedRoute = {},
        state.user = {}
      state.hasLogin = false
    },
    token(state, payload) {
      state.token = payload
    },
    user(state, payload) {
      state.user = payload
    },
    hasLogin(state, payload) {
      state.hasLogin = payload
    },
    isAdmin(state, payload) {
      state.isAdmin = payload
    },
  },
  actions: {
  },
  modules: {
  }
})
