<template>
  <v-card style="margin: 5% 15% 0 15%" flat>
    <v-snackbar v-model="multiLinkDialog">
      dsadadsada
    </v-snackbar>
    <v-text-field
      filled
      rounded
      shaped
      placeholder="Ваша ссылка для сокращения"
      append-icon="create"
      color="#448AFF"
      :rules="urlRule"
      v-model="newShortink"
      @submit="createShortink"
    />
    <v-card
      v-if="newShortink.length > 0"
      rounded="lg"
      elevation="8"
      style="margin: 2%"
    >
      <v-card-subtitle>
        Доп. функции:
      </v-card-subtitle>
      <v-divider />
      <v-container>
        <v-row>
          <v-col>
            <v-switch
              v-model="featuresCreate.multiLink"
              label="Мульти ссылка"
              value="multi-link"
              color="#EF5350"
              hint="Несколько ссылок в ссылке. Через пробел писать"
              persistent-hint
            >
            </v-switch>
          </v-col>
          <v-col>
            <v-switch
              v-model="featuresCreate.privateLink"
              label="Скрытая ссылка"
              value="private-link"
              color="#E53935"
            />
          </v-col>
        </v-row>
      </v-container>
    </v-card>
    <v-card>
      <v-container>
        <v-row>
          <v-col sm="12" md="6" lg="4" xl v-for="i in 5" :key="i">
            <v-card>
              <v-toolbar flat>
                <router-link
                  :to="'/profile/user_' + i"
                  style="color: inherit; text-decoration: none"
                >
                  <v-toolbar-title>
                    <v-avatar color="#1E88E5">
                      <span style="color: white">
                        {{ i }}
                      </span>
                    </v-avatar>
                    User_{{ i }}
                  </v-toolbar-title>
                </router-link>
                <v-spacer />
                <v-btn icon>
                  <v-icon>
                    more_vert
                  </v-icon>
                </v-btn>
              </v-toolbar>
              <v-divider />
              <v-card-text>
                dsadadadd
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component({
  data: () => ({
    list: [],
    multiLinkDialog: false,
    newShortink: "",
    featuresCreate: {
      multiLink: false,
      privateLink: false
    },
    urlRule: [
      (v: any): any =>
        v.match("^https:\\/\\/") !== null || "Введите корректную ссылку"
    ]
  }),
  components: {},
  methods: {
    async createShortink() {
      this.$store.getters
    }
  },
  mounted() {
    console.log("mutata");
  }
})
export default class Home extends Vue {}
</script>
