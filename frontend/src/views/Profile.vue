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
                  <v-col sm="12" md="12" lg="6" xl="6" >
                    <v-text-field
                      label="Имя"
                      v-model="info.fname"
                      shaped
                      outlined
                      readonly
                    />
                  </v-col>
                  <v-col sm="12" md="12" lg="6" xl="6">
                    <v-text-field
                      label="Фамилия"
                      v-model="info.lname"
                      shaped
                      outlined
                      readonly
                    />
                  </v-col>
                  <v-col sm="12" md="12" lg="6" xl="6">
                    <v-text-field
                      label="Логин"
                      v-model="info.login"
                      shaped
                      outlined
                      readonly
                    />
                  </v-col>
                  <v-col sm="12" md="12" lg="6" xl="6">
                    <v-text-field
                      label="E-mail"
                      v-model="info.email"
                      shaped
                      outlined
                      readonly
                    />
                  </v-col>
                </v-row>
              </v-col>
              <v-col sm="12" md="3" lg="6" xl="3">
                <v-img :src="info.img" width="250" height="250" />
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
        <v-card shaped style="margin: 5%">

        </v-card>
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
      isInvalidLogin: false,
      info: {
        fname: "l",
        lname: "l",
        login: "l",
        email: "l",
        pwd: "l",
        img: "l"
      }
    };
  },
  async mounted() {
    if (localStorage["uid"] !== undefined) {
      if (await this.$store.getters.validateToken) {
        this.isAuth = true;
        this.info = await this.$store.getters.getCurUser;
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
