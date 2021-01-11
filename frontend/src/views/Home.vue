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
      @input="createShortink"
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
      (v: any) =>
        v.match("^https:\\/\\/") !== null || "Введите корректную ссылку",
      (v: any) =>
        v.match("^http:\\/\\/") !== null || "Введите корректную ссылку"
    ],
    encryptLink: function encryptLink(link: string): string {
      let res = "";

      const protocolPart = new RegExp(link.slice(0, link.indexOf("//")), "i");
      const bodyPart = link.split("/")[2].split(".");
      const pathPart = link.split("/").slice(3);

      if (protocolPart.test("https://")) res += "~(";
      else if (protocolPart.test("http://")) res += "(";

      for (const item of bodyPart) {
        if (item.length < 2) res += item;
        else {
          for (let i = 0; i < item.length; i += 2) {
            res += item[i];
          }
        }
      }

      const tempArr = [];
      for (let j = 0; j < pathPart.length; j++) {
        const item = pathPart[j];
        tempArr.push("/");
        if (item.length < 2) res += item;
        else {
          for (let i = 0; i < item.length; i += 2) {
            tempArr[j] += item[i];
          }
        }
      }
      res += tempArr.join("");
      res += ")";

      return res;
    }
  }),
  components: {},
  methods: {
    async createShortink() {
      if (
        this.$data.newShortink.match("^https:\\/\\/") !== null &&
        this.$data.newShortink.length > 8
      ) {
        this.$store.state.newLink.link = this.$data.newShortink;
        console.log(this.$data.encryptLink(this.$data.newShortink));
        // console.log(await this.$store.getters.encryptLink);
      }
    }
  },
  mounted() {
    console.log("");
  }
})
export default class Home extends Vue {}
</script>
