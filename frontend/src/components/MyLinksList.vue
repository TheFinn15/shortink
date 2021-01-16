<template>
  <v-container>
    <v-row>
      <v-col v-for="(item, i) in links" :key="i" sm="12" md="6" lg="4" xl="2">
        <MyLinksItem :link="item" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import MyLinksItem from "@/components/MyLinksItem";
export default {
  name: "MyLinksList",
  components: { MyLinksItem },
  props: ["links"]
};
</script>

<style scoped></style>
