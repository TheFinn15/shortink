<template>
  <v-tabs grow>
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
        <v-container>
          <v-row>
            <v-col cols="3">
              <v-text-field
                  label="Имя"
                  v-model="info.fname"
                  shaped
                  outlined
                  readonly
              />
            </v-col>
            <v-col cols="3">
              <v-text-field
                  label="Имя"
                  v-model="info.fname"
                  shaped
                  outlined
                  readonly
              />
            </v-col>
          </v-row>
        </v-container>
      </v-card>
    </v-tab-item>
  </v-tabs>
</template>

<script>
export default {
  name: "Profile",
  data: () => {
    return {
      isAuth: false,
      info: {
        fname: 'l',
        lname: 'l',
        login: 'l',
        email: 'l',
        pwd: 'l',
        img: 'l'
      }
    }
  },
  async mounted() {
    if (localStorage['uid'] !== undefined) {
      if (await this.$store.getters.validateToken) {
        this.isAuth = true
        this.info = await this.$store.getters.getCurUser
      } else {
        for(const item of Object.keys(this.info)) {
          this.info[item] = null
        }
        localStorage.removeItem('uid')
        window.location.reload()
      }
    }
  }
}
</script>

<style scoped>

</style>