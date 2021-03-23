<template>
<div class="main">
  

<invitee/>

  </div>
</template>

<script>

import Invitee from '../components/Invitee.vue'
export default {
  name: "invitee-view",
  components: { Invitee },

  data(){

    return{
      vote:{
        inviteId:"",
        yelpId:"",
        thumbsUp:false,
        thumbsDown:false,
      },
      inviteeVotes: []

    }
  }

}
</script>

<style>

</style>