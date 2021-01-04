<template>
  <v-app>
    <v-app-bar app color="#448AFF" dark>
      <v-icon x-large>
        link
      </v-icon>
      <v-card-title>
        <router-link to="/" style="color: inherit; text-decoration: none">
          Shortink
        </router-link>
      </v-card-title>
      <v-btn icon>
        <v-icon>
          translate
        </v-icon>
      </v-btn>
      <v-dialog>
        <template v-slot:activator="{on, attrs}">
          <v-btn icon
                 title="Создать сокращенную ссылку"
                 v-on="on" v-bind="attrs">
            <v-icon>
              create
            </v-icon>
          </v-btn>
        </template>
        <v-card>
          <v-card-title>
            Новая ссылка
          </v-card-title>
        </v-card>
      </v-dialog>
      <v-spacer/>
      <v-menu transition="fade-transition" offset-y>
        <template v-slot:activator="{on, attrs}">
          <v-avatar color="#1E88E5" v-bind="attrs" v-on="on" title="Anonymous">
            A
          </v-avatar>
        </template>
        <v-card>
          <v-card-subtitle>
            Не найдено
          </v-card-subtitle>
          <v-divider/>
          <v-list nav>
            <v-list-item-group>
              <v-list-item>
                <v-list-item-icon>
                  <v-icon>
                    person_pin
                  </v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title @click="authForm.state = true">
                    Авторизация
                  </v-list-item-title>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-icon>
                  <v-icon>
                    person_add
                  </v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title @click="registerForm.state = true">
                    Регистрация
                  </v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-card>
      </v-menu>
    </v-app-bar>

    <v-main>
      <v-snackbar
          transition="fade-transition"
          top
          timeout="1500"
          v-model="goToErr"
          color="indigo"
      >
        <span>Войдите в свой кабинет для начала.</span>
      </v-snackbar>
      <v-snackbar
          transition="fade-transition"
          top
          timeout="1500"
          v-model="authForm.alertState"
          :color="authForm.alertInfo[1]"
      >
        {{authForm.alertInfo[0]}}
      </v-snackbar>
      <v-dialog v-model="authForm.state" width="480">
        <v-card>
          <v-card-title style="justify-content: center; display: flex">
            Авторизация
          </v-card-title>
          <v-divider/>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                    label="Логин"
                    outlined
                    v-model="authForm.login"
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12">
                <v-text-field
                    type="password"
                    label="Пароль"
                    outlined
                    v-model="authForm.pwd"
                />
              </v-col>
            </v-row>
            <v-btn block color="success" @click="doAuth">
              ВОЙТИ
            </v-btn>
          </v-container>
        </v-card>
      </v-dialog>
      <router-view/>
    </v-main>

    <footer>
      <v-bottom-navigation :value="bottomNav" grow color="#448AFF">
        <v-btn to="/">
          <span>
            Лайф-лента
          </span>
          <v-icon>
            list_alt
          </v-icon>
        </v-btn>
        <v-btn @click="goToCabinet">
          <span>
            Мой кабинет
          </span>
          <v-icon>
            account_box
          </v-icon>
        </v-btn>
        <v-btn disabled>
          <span>
            Мои чаты
          </span>
          <v-icon>
            chat
          </v-icon>
        </v-btn>
      </v-bottom-navigation>
    </footer>
  </v-app>
</template>

<script lang="ts">
// TODO: Сокращатель ссылок:
// TODO: Полное управление ссылками и предпросмотр;
// TODO: Мульти ссылки(скрещивание ссылок в одну, с возможностью перехода на каждую из них;
// TODO: Лайф-лента ссылок;
// TODO: Метод шифровки для ссылок и также мульти-ссылок.
// TODO: Просмотр профилей других юзеров
// TODO: ЧАТ ПОЛЬЗОВАТЕЛЕЙ (не точно)
import Vue from "vue";

export default Vue.extend({
  name: "App",
  components: {},
  data: () => ({
    createShortink: false,
    bottomNav: 0,
    goToErr: false,
    authForm: {
      state: false,
      login: null,
      pwd: null,
      alertState: false,
      alertInfo: ['', 'success']
    },
    registerForm: {
      state: false,
      fname: null,
      sname: null,
      email: null,
      login: null,
      pwd: null,
      alertState: false,
      alertInfo: ['', 'success']
    }
  }),
  methods: {
    goToCabinet() {
      if (localStorage['uid'] !== undefined) {
        this.$router.push('/cabinet')
      } else {
        this.$data.goToErr = true
      }
    },
    doAuth() {
      this.$store.commit('auth', {
        login: this.$data.authForm.login,
        pwd: this.$data.authForm.pwd
      })
      if (localStorage['uid'] === undefined) {
        this.$data.authForm.alertState = true
        this.$data.authForm.alertInfo[0] = 'Ошибка при авторизации'
        this.$data.authForm.alertInfo[1] = 'red'
      } else {
        this.$data.authForm.alertState = true
        this.$data.authForm.alertInfo[0] = 'Успешнная авторизация'
        this.$data.authForm.alertInfo[1] = 'success'
      }
    }
  },
  mounted() {
    if (this.$route.path === '/') this.$data.bottomNav = 0
    if (this.$route.path === '/cabinet') this.$data.bottomNav = 1
    if (this.$route.path === '/chats') this.$data.bottomNav = 2
  }
});
</script>
