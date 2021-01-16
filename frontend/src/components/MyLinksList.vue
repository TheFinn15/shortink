<template>
  <v-container>
    <v-row>
      <v-col v-for="(item, i) in links" :key="i">
        <v-card>

        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "MyLinksList",
  props: ["links"]
}
</script>

<style scoped></style>