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
      this.isAuth = true
      Promise.resolve(await this.$store.getters.getCurUser).then(result => (this.info = result))
    }
  }
}
</script>

<style scoped>

</style>