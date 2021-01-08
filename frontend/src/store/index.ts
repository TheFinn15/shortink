import Vue from "vue";
import Vuex from "vuex";
import axios from 'axios';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    ip: 'http://localhost',
    port: ':25016',
    userInfo: {}
  },
  mutations: {
    async auth(state: any) {
      await axios.post(state.ip+state.port+'/api/auth', {
        login: state.userInfo.login,
        pwd: state.userInfo.pwd
      }).then(resp => {
        localStorage['uid'] = resp.data['id_token']
      })
    }
  },
  actions: {
    async register (context: any) {
      await axios.post(context.state.ip+context.state.port+'/api/register', {
        fname: context.state.userInfo.fname,
        lname: context.state.userInfo.lname,
        email: context.state.userInfo.email,
        login: context.state.userInfo.login,
        pwd: context.state.userInfo.pwd
      }).then(() => {
        context.commit('auth')
      })
    }
  },
  modules: {},
  getters: {
    async getAllUsers(state: any) {
      return await axios.get(state.ip+state.port+'/api/users/all')
        .then(resp => resp.data)
    },
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
