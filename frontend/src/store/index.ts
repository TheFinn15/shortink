import Vue from "vue";
import Vuex from "vuex";
import axios from 'axios';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    token: ''
  },
  mutations: {
    auth(state: any, userInfo: any) {
      axios({
        method: 'POST',
        url: `http://localhost:25016/api/auth`,
        data: {
          login: userInfo.login,
          pwd: userInfo.pwd
        }
      }).then(resp => {
        localStorage['uid'] = resp.data['id_token']
      }).catch(() => {
        state.isLogin = ''
      })
    }
  },
  actions: {},
  modules: {}
});
