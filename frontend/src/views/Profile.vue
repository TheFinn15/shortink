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
        <v-card shaped style="margin: 5%">
          <v-card-title style="justify-content: center; display: flex">
            <v-hover v-slot="{ hover }" v-if="isAuth">
              <v-btn
                color="success"
                left
                absolute
                outlined
                rounded
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
                    append-outer-icon="mdi-camera"
                    prepend-icon=""
                    v-if="isAuth"
                    @input="checkChanges"
                  />
                </v-card>
              </v-col>
              <v-spacer />
            </v-row>
          </v-container>
        </v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card shaped style="margin: 2% 5%">
          <v-card-title style="justify-content: center; display: flex">
            Мои активные ссылки
          </v-card-title>
          <v-divider />
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
export default {
  name: "Profile",
  data: () => {
    return {
      isAuth: false,
      profileHaveChanges: false,
      isInvalidLogin: false,
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
    async applyChanges() {
      console.log("");
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
        this.isAuth = true;
        this.info = await this.$store.dispatch("getCurUser");
      } else {
        for (const item of Object.keys(this.info)) {
          this.info[item] = null;
        }
        localStorage.removeItem("uid");
        window.location.reload();
      }
    } else {
      const users = await this.$store.getters.getAllUsers;
      const curProfile = this.$route.fullPath.split("/");
      const res = users.filter(
        i => i.login === curProfile[curProfile.length - 1]
      );
      if (res.length > 0) {
        this.info = res[0];
      } else {
        this.isInvalidLogin = true;
      }
    }
  }
};
</script>

<style scoped></style>
