<template>
  <v-card style="margin: 5% 15% 0 15%" flat>
    <v-snackbar top color="#1976D2" shaped v-model="createLinkDialog">
      Ссылка верная ?
      <template v-slot:action>
        <v-btn text @click="cancelCreateLink">
          Отменить
        </v-btn>
        <v-btn text @click="createShortink">
          Готово
        </v-btn>
      </template>
    </v-snackbar>
    <v-snackbar
      timeout="1500"
      top
      :color="alertInfo[0]"
      shaped
      v-model="alertCreateLink"
    >
      {{ alertInfo[1] }}
    </v-snackbar>
    <v-text-field
      filled
      rounded
      shaped
      placeholder="Ваша ссылка для сокращения"
      color="#448AFF"
      append-icon="create"
      :rules="urlRule"
      v-model="newShortink"
      @input="duplicateShortinkExist"
      @keydown.enter="createLinkDialog = true"
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
              :disabled="!isAuth"
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
              :disabled="!isAuth"
            />
          </v-col>
        </v-row>
      </v-container>
    </v-card>
    <v-card>
      <v-container>
        <v-row>
          <v-col
            sm="12"
            md="6"
            lg="4"
            xl="4"
            v-for="(item, i) in liveList"
            :key="i"
          >
            <v-card>
              <v-toolbar flat>
                <router-link
                  v-if="item.user !== null"
                  :to="'/profile/' + item.user.login"
                  style="color: inherit; text-decoration: none"
                >
                  <v-toolbar-title>
                    <v-badge
                      dot
                      :color="item.user.isOnline ? 'success' : 'red'"
                      overlap
                      offset-y="10"
                      :title="item.user.isOnline ? 'Онлайн' : 'Не в сети'"
                    >
                      <v-avatar color="#1E88E5">
                        <span style="color: white">
                          {{ item.user.login[0] }}
                        </span>
                      </v-avatar>
                    </v-badge>
                    {{ item.user.login }}
                  </v-toolbar-title>
                </router-link>
                <v-toolbar-title v-else>
                  <v-avatar color="#1E88E5">
                    <span style="color: white">
                      A
                    </span>
                  </v-avatar>
                  Anonymous
                </v-toolbar-title>
                <v-spacer />
                <v-tooltip top>
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn icon v-on="on" v-bind="attrs">
                      <v-icon>
                        access_time
                      </v-icon>
                    </v-btn>
                  </template>
                  <span>
                    Дата создания: <br />
                    {{ item.createdDate }}
                  </span>
                </v-tooltip>
                <v-menu offset-y rounded="lg">
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn icon v-on="on" v-bind="attrs">
                      <v-icon>
                        more_vert
                      </v-icon>
                    </v-btn>
                  </template>
                  <v-list>
                    <v-list-item-group>
                      <v-list-item @click="goToNativeLink(item.nativeLink)">
                        <v-list-item-icon>
                          <v-icon>
                            call_made
                          </v-icon>
                        </v-list-item-icon>
                        <v-list-item-title>
                          Перейти по ссылке
                        </v-list-item-title>
                      </v-list-item>
                      <v-list-item :disabled="!goToChatsEnable" to="/chats">
                        <v-list-item-icon>
                          <v-icon>
                            message
                          </v-icon>
                        </v-list-item-icon>
                        <v-list-item-title>
                          Перейти в чат
                        </v-list-item-title>
                      </v-list-item>
                    </v-list-item-group>
                  </v-list>
                </v-menu>
              </v-toolbar>
              <v-divider />
              <v-chip-group>
                <v-tooltip left>
                  <template v-slot:activator="{ on, attrs }">
                    <div
                      v-on="on"
                      v-bind="attrs"
                      v-if="item.multiple || item.private"
                    >
                      <v-chip
                        class="ma-2"
                        outlined
                        color="#1E88E5"
                        v-if="item.multiple"
                      >
                        Multiple
                      </v-chip>
                      <v-chip
                        class="ma-2"
                        outlined
                        color="#1E88E5"
                        v-if="item.private"
                      >
                        Private
                      </v-chip>
                    </div>
                    <v-chip
                      v-on="on"
                      v-bind="attrs"
                      class="ma-2"
                      outlined
                      color="red"
                      v-else
                    >
                      Обычная
                    </v-chip>
                  </template>
                  <span>
                    Вид ссылки
                  </span>
                </v-tooltip>
              </v-chip-group>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import axios from "axios";

@Component({
  data: () => ({
    liveList: [],
    isAuth: localStorage["uid"] !== undefined,
    shortinkIsExist: false,
    reRenderPage: false,
    createLinkDialog: false,
    alertCreateLink: false,
    alertInfo: ["success", ""],
    newShortink: "",
    goToChatsEnable: localStorage["uid"] !== undefined,
    featuresCreate: {
      multiLink: false,
      privateLink: false
    },
    urlRule: [
      (v: any) =>
        v.match("^https:\\/\\/|^http:\\/\\/") !== null ||
        "Введите корректную ссылку"
    ],
    encryptLink: function(link: string): string {
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
  async updated() {
    if (this.$data.reRenderPage) {
      console.log("update");
    }
    // this.$data.liveList = await this.$store.getters.getLiveList;
  },
  methods: {
    goToNativeLink(link: string) {
      window.location.href = link;
    },
    cancelCreateLink() {
      this.$data.createLinkDialog = false;
      this.$data.newShortink = "";
      this.$data.featuresCreate.multiLink = false;
      this.$data.featuresCreate.privateLink = false;
    },
    duplicateShortinkExist() {
      const link = this.$data.liveList.filter(
        (i: any) => i.nativeLink === this.$data.newShortink && i.user === null
      );
      if (link.length > 0) {
        this.$data.shortinkIsExist = true;
        for (let i = this.$data.newShortink.length; i > 0; i--) {
          setTimeout(() => {
            this.$data.newShortink = this.$data.newShortink.replace(
              this.$data.newShortink[i],
              ""
            );
          }, 600);
          setTimeout(() => {
            if (i <= 1) {
              this.$data.newShortink = "";
              for (let i = 0; i < link[0].encryptLink.length; i++) {
                setTimeout(() => {
                  this.$data.newShortink += link[0].encryptLink[i];
                }, 600);
              }
            }
          }, 650);
        }
      }
    },
    async createShortink() {
      if (
        this.$data.newShortink.match("^https:\\/\\/|^http:\\/\\/") !== null &&
        this.$data.newShortink.length > 8 &&
        !this.$data.shortinkIsExist
      ) {
        this.$data.createLinkDialog = false;
        let user;
        if (localStorage["uid"] !== undefined) {
          user = await this.$store.getters.getCurUser;
        }
        const encryptLink =
          "https://shortink.com/" +
          this.$data.encryptLink(this.$data.newShortink);

        this.$store.state.newLink = {
          encryptLink: encryptLink,
          nativeLink: this.$data.newShortink,
          multiple: Boolean(this.$data.featuresCreate.multiLink),
          private: Boolean(this.$data.featuresCreate.privateLink),
          user: user !== undefined ? { id: user.id } : null
        };
        const createdLink = await this.$store.getters.createShortink;
        if (createdLink["native_link"] !== undefined) {
          for (let i = this.$data.newShortink.length; i > 0; i--) {
            setTimeout(() => {
              this.$data.newShortink = this.$data.newShortink.replace(
                this.$data.newShortink[i],
                ""
              );
            }, 600);
            setTimeout(() => {
              if (i <= 1) {
                this.$data.newShortink = "";
                for (let i = 0; i < encryptLink.length; i++) {
                  setTimeout(() => {
                    this.$data.newShortink += encryptLink[i];
                  }, 600);
                }
              }
            }, 650);
          }

          this.$data.alertCreateLink = true;
          this.$data.alertInfo[1] = "Шортер был успешно создан";
          this.$data.newShortink = "";
          this.$data.featuresCreate.multiLink = false;
          this.$data.featuresCreate.privateLink = false;

          await axios
            .get(
              this.$store.state.ip + this.$store.state.port + "/api/link/all"
            )
            .then(resp => (this.$data.liveList = resp.data));
        } else if (createdLink["status"] !== undefined) {
          this.$data.alertCreateLink = true;
          this.$data.alertInfo[0] = "red";
          this.$data.alertInfo[1] = "Данный шортер уже существует у вас";
        } else {
          this.$data.alertCreateLink = true;
          this.$data.alertInfo[0] = "red";
          this.$data.alertInfo[1] = "Ошибка при создание шортера";
        }
      }
    }
  },
  async mounted() {
    this.$data.liveList = await this.$store.getters.getLiveList;
  }
})
export default class Home extends Vue {}
</script>
