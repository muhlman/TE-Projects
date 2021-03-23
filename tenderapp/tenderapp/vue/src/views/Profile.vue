<template>

  <div class="tile is-ancestor mx-2 my-2">
    <div class="tile is-parent is-3">
      <user-profile />
    </div>

    <div class="tile is-parent is-variable">
      <dinner-list />
    </div>
  </div>

</template>

<script>
import DinnerList from "../components/DinnerList.vue";
import UserProfile from "../components/UserProfile.vue";
export default {
  name: "profile",
  components: {
    UserProfile,
    DinnerList,
  },

  created() {},
};
</script>


<style scoped>
</style>