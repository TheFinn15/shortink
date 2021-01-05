import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import Home from "@/views/Home.vue";
import Profile from "@/views/Profile.vue";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: '/profile/:login',
    name: 'Profiles',
    component: Profile
  }
];

const router = new VueRouter({
  routes
});

export default router;
