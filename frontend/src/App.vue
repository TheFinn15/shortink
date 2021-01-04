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
          <v-avatar color="#1E88E5" v-bind="attrs" v-on="on">
            A
          </v-avatar>
        </template>
        <v-card>
          <v-card-subtitle>
            KEs
          </v-card-subtitle>
        </v-card>
      </v-menu>
    </v-app-bar>

    <v-main>
      <v-snackbar
          transition="fade-transition"
          top
          timeout="1500"
          v-model="chatAlert"
      >
        <span>Данная функция времена недоступна.</span>
      </v-snackbar>
      <v-dialog>
        <template v-slot:activator>

        </template>
      </v-dialog>
      <v-dialog></v-dialog>
      <router-view/>
    </v-main>

    <footer>
      <v-bottom-navigation v-if="!isAuth" grow :value="bottomNav" color="#448AFF">
        <v-btn to="/">
          <span>
            Лайф-лента
          </span>
          <v-icon>
            list_alt
          </v-icon>
        </v-btn>
        <v-btn to="/cabinet">
          <span>
            Мой кабинет
          </span>
          <v-icon>
            account_box
          </v-icon>
        </v-btn>
        <v-btn @click="() => {
          chatAlert = !chatAlert
          bottomNav = !bottomNav
        }">
          <span>
            Мои чаты
          </span>
          <v-icon>
            chat
          </v-icon>
        </v-btn>
      </v-bottom-navigation>
      <v-bottom-navigation grow :value="bottomNav" color="#448AFF" v-else>
        <v-btn to="/">
          <span>
            Лайф-лента
          </span>
          <v-icon>
            list_alt
          </v-icon>
        </v-btn>
        <v-btn>
            <span>
              Авторизация
            </span>
          <v-icon>
            person_pin
          </v-icon>
        </v-btn>
        <v-btn>
            <span>
              Регистрация
            </span>
          <v-icon>
            person_add
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
    drawer: false,
    createShortink: false,
    bottomNav: 0,
    chatAlert: false
  }),
  computed: {
    isAuth() {
      return localStorage['uid'] !== undefined;
    }
  }
});
</script>
