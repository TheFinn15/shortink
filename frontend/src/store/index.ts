import Vue from "vue";
import Vuex from "vuex";
import axios from 'axios';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    token: '',
    ip: 'localhost',
    port: '25016'
  },
  mutations: {
    async auth(state: any, userInfo: any) {
      const ip = state.ip
      const port = state.port
      await axios({
        method: 'POST',
        url: `http://${ip}:${port}/api/auth`,
        data: {
          login: userInfo.login,
          pwd: userInfo.pwd
        }
      }).then(resp => {
        state.isLogin = resp.data['id_token']
        localStorage['uid'] = resp.data['id_token']
      }).catch(() => {
        state.isLogin = ''
      })
    },
    gets: state => {
      return state.ip
    }
  },
  actions: {},
  modules: {},
  getters: {
    getUrl: state => {
      return [state.ip, state.port]
    },
    async getCurUser(state: any) {
      const ip = state.ip
      const port = state.port
      return await axios({
        method: 'GET',
        url: `http://${ip}:${port}/api/user`,
        headers: {
          Authorization: 'Bearer ' + localStorage['uid']
        }
      }).then(resp => resp.data)
    }
  }
});
