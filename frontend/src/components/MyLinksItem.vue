<template>
  <v-card max-width="350" color="inherit">
    <v-hover>
      <template v-slot:default="{ hover }">
        <v-card>
          <v-img
            :src="'data:image/jpeg;base64,' + link.previewImg"
            :alt="nameLink(link.nativeLink)"
            width="350"
            height="350"
            @click="showOverlay = !showOverlay"
            v-if="link.previewImg !== null"
          />
          <div v-else style="padding-top: 50%; width: 350px; height: 350px">
            <v-icon
              style="justify-content: center; align-content: end; display: flex"
            >
              no_photography
            </v-icon>
            <v-card-subtitle style="justify-content: center; display: flex">
              Картинка отсутствует
            </v-card-subtitle>
          </div>
          <v-card-title @click="showOverlay = !showOverlay">
            {{ nameLink(link.nativeLink) }}
          </v-card-title>
          <v-divider />
          <v-chip-group>
            <div v-if="link.multiple || link.private">
              <v-chip
                class="ma-2"
                outlined
                color="#1E88E5"
                v-if="link.multiple"
              >
                Мульти
              </v-chip>
              <v-chip class="ma-2" outlined color="#1E88E5" v-if="link.private">
                Приватная
              </v-chip>
            </div>
            <v-chip class="ma-2" outlined color="red" v-else>
              Обычная
            </v-chip>
          </v-chip-group>
          <v-fade-transition>
            <v-overlay v-if="hover" absolute>
              <v-btn
                fab
                color="inherit"
                title="Перейти по ссылке"
                :href="link.nativeLink"
                target="_blank"
              >
                <v-icon>
                  north_east
                </v-icon>
              </v-btn>
              <v-btn
                v-if="link.previewImg !== null"
                fab
                color="inherit"
                title="Предпросмотр на весь экран"
                @click="dialogFullscreenImg = true"
              >
                <v-icon>
                  fullscreen
                </v-icon>
              </v-btn>
            </v-overlay>
          </v-fade-transition>
        </v-card>
      </template>
    </v-hover>
    <v-dialog v-model="dialogFullscreenImg">
      <v-img
        :src="'data:image/jpeg;base64,' + link.previewImg"
        :alt="nameLink(link.nativeLink)"
      />
    </v-dialog>
  </v-card>
</template>

<script>
export default {
  name: "MyLinksItem",
  props: ["link"],
  data: () => {
    return {
      dialogFullscreenImg: false,
      nameLink: link => {
        return link.split("/")[2];
      }
    };
  },
  methods: {}
};
</script>

<style scoped></style>
