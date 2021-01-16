import Vue from "vue";
import Vuex from "vuex";
import axios from 'axios';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    ip: 'http://localhost',
    port: ':25016',
    userInfo: {},
    newLink: {}
  },
  mutations: {
    async changeStatusOnline(state: any, payload: any) {
      await axios.get(state.ip+state.port+'/api/user', {
        headers: {
          Authorization: 'Bearer ' + localStorage['uid']
        }
      }).then(resp => {
        axios.put(state.ip+state.port+'/api/users/'+resp.data.id, {
          isOnline: payload.state
        }, {
          headers: {
            Authorization: 'Bearer ' + localStorage['uid']
          }
        })
      })
    }
  },
  actions: {
    async editProfileData({state}, payload: any) {
      return await axios.put(state.ip+state.port+'/api/users/'+payload.id, payload, {
        headers: {
          Authorization: 'Bearer ' + localStorage['uid']
        }
      }).then(() => true)
        .catch(() => false)
    },
    async getLiveList({commit, state}) {
      return await axios.get(state.ip+state.port+'/api/link/all')
        .then(resp => resp.data)
    },
    async getCurUser({commit, state}) {
      return await axios.get(state.ip+state.port+'/api/user', {
        headers: {
          Authorization: 'Bearer ' + localStorage['uid']
        }
      }).then(resp => resp.data)
    }
  },
  modules: {},
  getters: {
    async register(context: any) {
      return await axios.post(context.state.ip+context.state.port+'/api/register', {
        fname: context.state.userInfo.fname,
        lname: context.state.userInfo.lname,
        email: context.state.userInfo.email,
        login: context.state.userInfo.login,
        pwd: context.state.userInfo.pwd
      }).then(resp => ({state: true, info: resp.data}))
    },
    async auth(state: any) {
      return await axios.post(state.ip+state.port+'/api/auth', {
        login: state.userInfo.login,
        pwd: state.userInfo.pwd
      }).then(resp => ({state: true, token: resp.data['id_token']}))
    },
    async createShortink(state: any) {
      return await axios.post(state.ip+state.port+'/api/link', {
        encryptLink: state.newLink.encryptLink,
        nativeLink: state.newLink.nativeLink,
        multiple: state.newLink.multiple,
        private: state.newLink.private,
        user: state.newLink.user
      }).then((resp: any) => resp.data)
    },
    async getAllUsers(state: any) {
      return await axios.get(state.ip+state.port+'/api/users/all')
        .then(resp => resp.data)
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
