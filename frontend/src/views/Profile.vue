<template>
  <div>
    <v-tabs grow v-if="!isInvalidLogin">
      <v-tab>
        ИНФОРМАЦИЯ
      </v-tab>
      <v-tab v-if="isAuth">
        Мои ссылки
      </v-tab>
      <v-tab v-if="isAuth">
        Настройки
      </v-tab>
      <v-tab-item>
        <v-snackbar
          top
          v-model="alertState"
          shaped
          :color="alertInfo[0]"
          timeout="1500"
        >
          <template v-slot:action="{ on }">
            <v-btn text v-on="on" @click="alertState = false">
              <span>
                Закрыть
              </span>
            </v-btn>
          </template>
          <span>
            {{ alertInfo[1] }}
          </span>
        </v-snackbar>
        <v-card style="margin: 5%" rounded>
          <v-card-title style="justify-content: center; display: flex">
            <v-hover v-slot="{ hover }" v-if="isAuth">
              <v-btn
                color="success"
                absolute
                left
                outlined
                rounded
                large
                :disabled="!profileHaveChanges"
                @click="applyChanges"
              >
                <v-icon>
                  check_circle
                </v-icon>
                <span v-if="hover">
                  Сохранить изменения
                </span>
              </v-btn>
            </v-hover>
            Личные данные
            <v-btn
              color="indigo"
              right
              absolute
              fab
              outlined
              :disabled="!isAuth"
              :title="'Отправить сообщение ' + info.login"
            >
              <v-icon>
                message
              </v-icon>
            </v-btn>
          </v-card-title>
          <v-divider />
          <v-container>
            <v-row>
              <v-spacer />
              <v-col sm="12" md="3" lg="6" xl="3">
                <v-row>
                  <v-col sm="12" md="12" lg="6" xl="6">
                    <v-text-field
                      label="Имя"
                      v-model="info.fname"
                      @input="checkChanges"
                      shaped
                      outlined
                      :readonly="!isAuth"
                    />
                  </v-col>
                  <v-col sm="12" md="12" lg="6" xl="6">
                    <v-text-field
                      label="Фамилия"
                      v-model="info.lname"
                      @input="checkChanges"
                      shaped
                      outlined
                      :readonly="!isAuth"
                    />
                  </v-col>
                  <v-col sm="12" md="12" lg="6" xl="6">
                    <v-text-field
                      label="Логин"
                      v-model="info.login"
                      @input="checkChanges"
                      shaped
                      outlined
                      :readonly="!isAuth"
                    />
                  </v-col>
                  <v-col sm="12" md="12" lg="6" xl="6">
                    <v-text-field
                      label="E-mail"
                      v-model="info.email"
                      @input="checkChanges"
                      shaped
                      outlined
                      :readonly="!isAuth"
                    />
                  </v-col>
                </v-row>
              </v-col>
              <v-col sm="12" md="3" lg="6" xl="3">
                <v-card max-width="250" flat>
                  <v-img :src="info.img" width="250" height="250" />
                  <v-file-input
                    append-icon="mdi-camera"
                    prepend-icon=""
                    v-if="isAuth"
                    @change="renderImg"
                    @click:clear="returnOldImg"
                  />
                </v-card>
              </v-col>
              <v-spacer />
            </v-row>
          </v-container>
          <v-btn
            color="red"
            left
            bottom
            outlined
            fab
            @click="undoChanges"
            v-if="profileHaveChanges"
            style="margin: 2%"
            title="Вернуть по-умолчанию"
          >
            <v-icon>
              undo
            </v-icon>
          </v-btn>
        </v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card rounded style="margin: 2% 5%">
          <v-card-title style="justify-content: center; display: flex">
            <v-btn
              fab
              outlined
              color="#448AFF"
              absolute
              left
              title="Обновить ссылки"
              @click="doUpdateMyLinks"
            >
              <v-icon>
                refresh
              </v-icon>
            </v-btn>
            Мои активные ссылки
          </v-card-title>
          <v-divider />
          <MyLinksList :links="myLinks" />
        </v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card shaped style="margin: 5%"> </v-card>
      </v-tab-item>
    </v-tabs>
    <div v-else style="margin: 5%">
      <v-icon style="justify-content: center; display: flex">
        warning
      </v-icon>
      <v-card-title style="justify-content: center; display: flex">
        Неверный логин
      </v-card-title>
    </div>
  </div>
</template>

<script>
import MyLinksList from "@/components/MyLinksList";

export default {
  name: "Profile",
  components: { MyLinksList },
  data: () => {
    return {
      isAuth: false,
      profileHaveChanges: false,
      isInvalidLogin: false,
      alertInfo: ["success", ""],
      alertState: false,
      myLinks: [],
      info: {
        fname: null,
        lname: null,
        login: null,
        email: null,
        pwd: null,
        img: null
      }
    };
  },
  methods: {
    async doUpdateMyLinks() {
      for (let i = 0; i < this.$data.myLinks.length; i++) {
        console.log(this.$data.myLinks[i]);
        this.$data.myLinks[i].previewImg = await this.$store.dispatch(
          "updateImg",
          this.$data.myLinks[i]
        );
      }
    },
    async getUserByLogin() {
      const users = await this.$store.getters.getAllUsers;
      const curProfile = this.$route.fullPath.split("/");
      const res = users.filter(
        i => i.login === curProfile[curProfile.length - 1]
      );
      console.log(res);
      if (res.length > 0) {
        this.info = res[0];
      } else {
        this.isInvalidLogin = true;
      }
    },
    async undoChanges() {
      for (const item of Object.keys(this.$data.info)) {
        this.$data.info[item] = (await this.$store.dispatch("getCurUser"))[
          item
        ];
      }
      this.$data.profileHaveChanges = false;
    },
    async returnOldImg() {
      this.$data.profileHaveChanges = false;
      this.$data.info.img = (await this.$store.dispatch("getCurUser"))["img"];
    },
    renderImg(ev) {
      if (!this.$data.profileHaveChanges) {
        const reader = new FileReader();
        reader.onload = e => {
          this.info.img = e.target.result;
        };
        reader.readAsDataURL(ev);
        this.$data.profileHaveChanges = true;
      }
    },
    async applyChanges() {
      const changeData = await this.$store.dispatch(
        "editProfileData",
        this.$data.info
      );
      if (changeData) {
        this.alertState = true;
        this.alertInfo[1] = "Данные были изменены";
        this.profileHaveChanges = false;
      } else {
        this.alertState = true;
        this.alertInfo[0] = "red";
        this.alertInfo[1] = "Ошибка сервера. Попробуйте позже";
      }
    },
    async checkChanges() {
      const userInfo = await this.$store.dispatch("getCurUser");
      if (
        userInfo.fname !== this.info.fname ||
        userInfo.img !== this.info.img ||
        userInfo.lname !== this.info.lname ||
        userInfo.login !== this.info.login ||
        userInfo.email !== this.info.email
      ) {
        this.profileHaveChanges = true;
      } else if (
        userInfo.fname === this.info.fname ||
        userInfo.img === this.info.img ||
        userInfo.lname === this.info.lname ||
        userInfo.login === this.info.login ||
        userInfo.email === this.info.email
      ) {
        this.profileHaveChanges = false;
      }
    }
  },
  async mounted() {
    if (localStorage["uid"] !== undefined) {
      if (await this.$store.getters.validateToken) {
        const decodedToken = JSON.parse(
          atob(localStorage["uid"].split(".")[1])
        );
        const path = this.$route.path.split("/");
        if (decodedToken["sub"] === path[path.length - 1]) {
          this.isAuth = true;
          this.info = await this.$store.dispatch("getCurUser");
          const links = await this.$store.dispatch("getLiveList");
          this.myLinks = links
            .filter(i => i.user !== null)
            .filter(i => i.user.login === decodedToken["sub"]);
        } else {
          await this.getUserByLogin();
        }
      } else {
        for (const item of Object.keys(this.info)) {
          this.info[item] = null;
        }
        localStorage.removeItem("uid");
        window.location.reload();
      }
    } else {
      await this.getUserByLogin();
    }
  }
};
</script>

<style scoped></style>
