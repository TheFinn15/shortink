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
            v-else-if="userInfo.img === '' || userInfo.img === null"
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
                  v-model="registerForm.lname"
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
// TODO: Полное управление ссылками и предпросмотр;
// TODO: Мульти ссылки(скрещивание ссылок в одну, с возможностью перехода на каждую из них;
// TODO: Лайф-лента ссылок ( 50% готово );
// TODO: Метод шифровки для ссылок и также мульти-ссылок ( 50% готово ).
// TODO: Просмотр профилей других юзеров ( 50% готово )
// TODO: ЧАТ ПОЛЬЗОВАТЕЛЕЙ (не точно)
import Vue from "vue";

export default Vue.extend({
  name: "App",
  components: {},
  data: () => ({
    bottomNav: "live-list",
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
      lname: null,
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
    async doRegister() {
      this.$store.state.userInfo = this.registerForm;
      const register = await this.$store.getters.register;
      this.loaderForm = true;
      if (register.state) {
        const auth = await this.$store.getters.auth;
        if (auth.state) {
          localStorage["uid"] = auth.token;
          this.userInfo = await this.$store.dispatch("getCurUser");
          await this.$store.commit("changeStatusOnline", { state: true });
        }
      }
      setTimeout(() => {
        if (localStorage["uid"] !== undefined) {
          this.alertState = true;
          this.alertInfo[0] = "Успешнная регистрация";
          this.alertInfo[1] = "success";
          this.registerForm.state = false;
          this.loaderForm = false;
          window.location.reload();
        } else {
          this.alertState = true;
          this.alertInfo[0] = "Ошибка при регистрации";
          this.alertInfo[1] = "red";
          this.loaderForm = false;
        }
      }, 2500);
    },
    async doAuth() {
      const auth = await this.$store.dispatch("auth", this.authForm);
      this.loaderForm = true;
      console.log(auth);
      if (auth.state) {
        localStorage["uid"] = auth.token;
        this.userInfo = await this.$store.dispatch("getCurUser");
        await this.$store.commit("changeStatusOnline", { state: true });

        setTimeout(() => {
          this.alertState = true;
          this.alertInfo[0] = "Успешнная авторизация";
          this.alertInfo[1] = "success";
          this.authForm.state = false;
          this.loaderForm = false;
          window.location.reload();
        }, 1800);
      } else {
        setTimeout(() => {
          this.alertState = true;
          this.alertInfo[0] = "Ошибка при авторизации";
          this.alertInfo[1] = "red";
          this.loaderForm = false;
        }, 1800);
      }
    },
    async doLogout() {
      this.loaderForm = true;
      await this.$store.commit("changeStatusOnline", { state: false });
      for (const item in this.userInfo) {
        this.$data.userInfo[item] = "";
      }
      setTimeout(() => {
        this.isAuth = false;
        this.loaderForm = false;
        localStorage.removeItem("uid");
        window.location.reload();
      }, 1000);
    },
    doBuyPremium() {
      console.log("");
    }
  },
  async mounted() {
    if (localStorage["uid"] !== undefined) {
      if (await this.$store.getters.validateToken) {
        this.isAuth = true;
        this.userInfo = await this.$store.dispatch("getCurUser");
      } else {
        localStorage.removeItem("uid");
        window.location.reload();
      }
    }
    if (this.$route.path === "/") this.bottomNav = "live-list";
    if (this.$route.path.split("/")[1] === "profile")
      this.bottomNav = "cabinet";
    if (this.$route.path === "/chats") this.bottomNav = "chats";
  }
});
</script>
