<template>
  <b-navbar :spaced="true">
    <template #brand>
      <b-navbar-item tag="router-link" :to="{ name: 'restaurantFinder' }">
        <img src="../assets/TenderTextAndIcon.png" />
      </b-navbar-item>
    </template>
    <template #end>
      <div class="buttons">
        <b-navbar-item
          tag="router-link"
          v-if="$route.name !== 'restaurantFinder' && $store.state.token != ''"
          class="navlink"
          v-bind:to="{ name: 'restaurantFinder' }"
          >Home</b-navbar-item
        >
        <b-navbar-item
          tag="router-link"
          class="navlink"
          v-bind:to="{ name: 'profile' }"
          v-if="$store.state.token != ''"
          >My Invites</b-navbar-item
        >
        <b-navbar-item
          tag="router-link"
          v-bind:to="{ name: 'logout' }"
          v-if="$store.state.token != ''"
          class="navlink"
        >
          Logout</b-navbar-item
        >
        <!-- Show for invitee page if user logged out -->
         <b-navbar-item
          tag="router-link"
          v-bind:to="{ name: 'login' }"
          v-if="$store.state.token === ''"
          class="navlink"
        >
          Login</b-navbar-item
        >
         <b-navbar-item
          tag="router-link"
          v-bind:to="{ name: 'register' }"
          v-if="$store.state.token === ''"
          class="navlink"
        >
          Register</b-navbar-item
        >
      </div>
    </template>
  </b-navbar>
</template>

<script>
export default {};
</script>

<style scoped>
.navlink {
  font-size: 1.2em;
  text-transform: uppercase;
  font-weight: bold;
  color: #dc6b67;
}

.navlink:hover {
  color: #3c0f49;
}

.navbar-item img {
      max-height: 2.5rem;
}
</style>