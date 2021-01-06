import Vue from "vue";
import Vuex from "vuex";
import axios from 'axios';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    token: '',
    ip: 'http://localhost',
    port: ':25016'
  },
  mutations: {
    async auth(state: any, userInfo: any) {
      await axios.post(state.ip+state.port+'/api/auth', {
        login: userInfo.login,
        pwd: userInfo.pwd
      }).then(resp => {
        state.isLogin = resp.data['id_token']
        localStorage['uid'] = resp.data['id_token']
      })
    }
  },
  actions: {},
  modules: {},
  getters: {
    async getCurUser(state: any) {
      return await axios.get(state.ip+state.port+'/api/user', {
        headers: {
          Authorization: 'Bearer ' + localStorage['uid']
        }
      }).then(resp => resp.data)
    },
    async validateToken(state: any) {
      return await axios.get(state.ip+state.port+'/api/check-auth', {
        headers: {
          token: localStorage['uid']
        }
      }).then(resp => resp.data.status === 200)
    }
  }
});
