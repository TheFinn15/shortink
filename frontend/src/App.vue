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
      <v-progress-linear
        :active="loaderForm"
        :indeterminate="loaderForm"
        absolute
        bottom
        color="#E3F2FD"
      />
      <v-btn icon>
        <v-icon>
          translate
        </v-icon>
      </v-btn>
      <v-spacer />
      <v-menu transition="fade-transition" offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-avatar
            color="#1976D2"
            v-bind="attrs"
            v-on="on"
            title="Anonymous"
            v-if="!isAuth"
          >
            A
          </v-avatar>
          <v-avatar
            v-if="isAuth && userInfo.img === null"
            color="#1976D2"
            v-bind="attrs"
            v-on="on"
            :title="userInfo.fname + ' ' + userInfo.lname"
          >
            {{ userInfo.fname[0] }}
          </v-avatar>
          <v-avatar v-else v-bind="attrs" v-on="on">
            <v-img :src="userInfo.img" />
          </v-avatar>
        </template>
        <v-card>
          <v-card-subtitle v-if="!isAuth">
            Не найдено
          </v-card-subtitle>
          <v-card-subtitle v-else>
            {{ userInfo.fname }} {{ userInfo.lname }}
          </v-card-subtitle>
          <v-divider />
          <v-list nav>
            <v-list-item-group v-if="!isAuth">
              <v-list-item @click="authForm.state = true">
                <v-list-item-icon>
                  <v-icon>
                    person_pin
                  </v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>
                    Авторизация
                  </v-list-item-title>
                </v-list-item-content>
              </v-list-item>
              <v-list-item @click="registerForm.state = true">
                <v-list-item-icon>
                  <v-icon>
                    person_add
                  </v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>
                    Регистрация
                  </v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
            <v-list-item-group v-else>
              <v-list-item @click="doBuyPremium">
                <v-list-item-icon>
                  <v-icon>
                    attach_money
                  </v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>
                    Оформить подписку
                  </v-list-item-title>
                </v-list-item-content>
              </v-list-item>
              <v-list-item @click="doLogout">
                <v-list-item-icon>
                  <v-icon>
                    exit_to_app
                  </v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>
                    Выйти
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
        <span>
          Войдите в свой кабинет для начала.
        </span>
      </v-snackbar>
      <v-snackbar
        transition="fade-transition"
        top
        timeout="1500"
        v-model="alertState"
        :color="alertInfo[1]"
      >
        {{ alertInfo[0] }}
      </v-snackbar>
      <v-dialog v-model="authForm.state" width="480">
        <v-card>
          <v-card-title style="justify-content: center; display: flex">
            Авторизация
          </v-card-title>
          <v-divider />
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field label="Логин" outlined v-model="authForm.login" />
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
            <v-btn outlined block color="success" @click="doAuth">
              ВОЙТИ
            </v-btn>
          </v-container>
        </v-card>
      </v-dialog>
      <v-dialog v-model="registerForm.state" width="480">
        <v-card>
          <v-card-title style="justify-content: center; display: flex">
            Регистрация
          </v-card-title>
          <v-divider />
          <v-container>
            <v-row>
              <v-col cols="6">
                <v-text-field
                  label="Имя"
                  v-model="registerForm.fname"
                  outlined
                />
              </v-col>
              <v-col cols="6">
                <v-text-field
                  label="Фамилия"
                  v-model="registerForm.sname"
                  outlined
                />
              </v-col>
              <v-col cols="6">
                <v-text-field
                  label="Логин"
                  v-model="registerForm.login"
                  outlined
                />
              </v-col>
              <v-col cols="6">
                <v-text-field
                  label="E-mail"
                  v-model="registerForm.email"
                  outlined
                />
              </v-col>
              <v-col cols="6">
                <v-text-field
                  type="password"
                  label="Пароль"
                  v-model="registerForm.pwd"
                  outlined
                />
              </v-col>
              <v-col cols="6">
                <v-text-field
                  type="password"
                  label="Подтвердите пароль"
                  v-model="registerForm.repeatPwd"
                  outlined
                />
              </v-col>
            </v-row>
            <v-btn color="success" outlined block @click="doRegister">
              Создать аккаунт
            </v-btn>
          </v-container>
        </v-card>
      </v-dialog>
      <router-view />
    </v-main>

    <footer>
      <v-bottom-navigation :value="bottomNav" grow color="#448AFF" app>
        <v-btn to="/" value="live-list">
          <span>
            Лайф-лента
          </span>
          <v-icon>
            list_alt
          </v-icon>
        </v-btn>
        <v-btn
          @click="goToCabinetNonAuth"
          :disabled="isDisableBtn"
          v-if="!isAuth"
        >
          <span>
            Мой кабинет
          </span>
          <v-icon>
            account_box
          </v-icon>
        </v-btn>
        <v-btn :to="'/profile/' + userInfo.login" value="cabinet" v-else>
          <span>
            Мой кабинет
          </span>
          <v-icon>
            account_box
          </v-icon>
        </v-btn>
        <v-btn disabled value="chats">
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
    bottomNav: "",
    goToErr: false,
    isAuth: false,
    isDisableBtn: false,
    loaderForm: false,
    userInfo: {
      fname: "l",
      lname: "l",
      login: "l",
      email: "l",
      pwd: "l",
      img: "l"
    },
    authForm: {
      state: false,
      login: null,
      pwd: null
    },
    registerForm: {
      state: false,
      fname: null,
      sname: null,
      email: null,
      login: null,
      pwd: null,
      repeatPwd: null
    },
    alertState: false,
    alertInfo: ["", "success"]
  }),
  methods: {
    goToCabinetNonAuth() {
      this.goToErr = true;
      this.isDisableBtn = true;
    },
    doRegister() {
      this.$store.state.userInfo = this.registerForm;
      this.$store.dispatch("register");
      this.loaderForm = true;
      setTimeout(() => {
        if (localStorage["uid"] !== undefined) {
          this.alertState = true;
          this.alertInfo[0] = "Успешнная регистрация";
          this.alertInfo[1] = "success";
          this.registerForm.state = false;
          this.loaderForm = false;
        } else {
          this.alertState = true;
          this.alertInfo[0] = "Ошибка при регистрации";
          this.alertInfo[1] = "red";
          this.loaderForm = false;
        }
      }, 2500);
    },
    doAuth() {
      this.$store.state.userInfo = this.authForm;
      this.$store.commit("auth");
      this.loaderForm = true;
      setTimeout(() => {
        if (localStorage["uid"] !== undefined) {
          this.alertState = true;
          this.alertInfo[0] = "Успешнная авторизация";
          this.alertInfo[1] = "success";
          this.authForm.state = false;
          this.loaderForm = false;
        } else {
          this.alertState = true;
          this.alertInfo[0] = "Ошибка при авторизации";
          this.alertInfo[1] = "red";
          this.loaderForm = false;
        }
      }, 2500);
    },
    doLogout() {
      this.loaderForm = true;
      setTimeout(() => {
        this.isAuth = false;
        this.loaderForm = false;
      }, 1000);
    }
  },
  async mounted() {
    if (localStorage["uid"] !== undefined) {
      // eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJUZXN0ZXIyMzQiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjA5OTYxNzY3fQ.jjJ6gJxmFoTFXH2E0zl5TU3rrQNj4o3GlZIreduaI9Vj8Mw1dAemBGAvcxyfgHeefDN9OoyECLwFxHevd8zrwg
      if (await this.$store.getters.validateToken) {
        this.isAuth = true;
        this.userInfo = await this.$store.getters.getCurUser;
      } else {
        localStorage.removeItem("uid");
        window.location.reload();
      }
    }
    if (this.$route.path === "/") this.bottomNav = "live-list";
    if (this.$route.path === "/profile/" + this.userInfo.login)
      this.bottomNav = "cabinet";
    if (this.$route.path === "/chats") this.bottomNav = "chats";
  }
});
</script>
