<template>
  <div>
    <input type="text" v-model="invitation.location" />
    <input type="text" v-model="invitation.radius" />

    <input type="date" v-model="date" />
    <input type="time" v-model="time" />

    <input type="date" v-model="deadlineDate" />
    <input type="time" v-model="deadlineTime" />
    <button v-on:click="sendInvitation">send</button>
  </div>
</template>

<script>
import InviteService from "../services/InviteService";

export default {
  data() {
    return {
      deadlineDate: "",
      deadlineTime: "",
      date: "",
      time: "",
      invitation: {
        location: "",
        radius: "",
        creatorId: "",
        deadline: "",
        username: "",
        reservationDate: "",
      },
    };
  },
  methods: {
    sendInvitation() {
      this.invitation.reservationDate = this.invitedatetime;
      this.invitation.deadline = this.deadlinedatetime;
      InviteService.sendInvite(this.invitation);
    },
  },

  computed: {
    invitedatetime() {
      return this.date + " " + this.time;
    },
    deadlinedatetime() {
      this.deadline = this.deadlineDate + " " + this.deadlineTime;
    },
  },
  
};
</script>

<style>
</style>