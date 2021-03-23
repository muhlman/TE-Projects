<template>
  <div class="main">
   
    <section class="has-text-centered" v-if="this.isLoading">
      <div>
        <p class="is-size-3 has-text-weight-semibold">Loading...</p>
      </div>
      <div class="loading-gif">
        <img src="../assets/loading.gif" />
      </div>
    </section>

    <div class="columns is-vertical">

      <div class="column">
      <div class="welcome-message" v-if="!invitee.deadlinePassed && !isLoading">
        <p class="is-size-2 has-text-weight-semibold">
          Hey, <span class="is-capitalized">{{ invitee.name }}</span>
        </p>
        <p class="is-size-3 has-text-weight-semibold">
          You've been invited out for food!
        </p>
        <p class="is-size-3 has-text-weight-semibold">
          Place your vote on which restaurant to visit
        </p>
        <br /><br />
        <p>
          <span class="is-size-3 has-text-weight-semibold"
            >Voting Deadline: </span
          >{{ invitee.deadline | moment("dddd, MMMM Do, YYYY h:mm a") }}
        </p>
        <p>
          <span class="is-size-3 has-text-weight-semibold"
            >Reservation Date: </span
          >{{ invitee.reservationDate | moment("dddd, MMMM Do, YYYY h:mm a") }}
        </p>
        <div>
          <span v-show="invitee.isAttending === 'Pending'">
            <b-button
              type="is-primary"
              rounded
              size="is-medium"
              class="m-2"
              v-on:click="isAttending('Attending')"
            >
              <i class="far fa-thumbs-up"></i>
              Yes I'll be there!</b-button
            >

            <b-button
              type="is-primary"
              rounded
              size="is-medium"
              class="m-2"
              v-on:click="isAttending('Declined')"
            >
              <i class="far fa-sad-tear"></i>
              Sorry Maybe next time!</b-button
            >
          </span>

          <span v-show="invitee.isAttending !== 'Pending'">
            <p>
              <span class="is-size-3 has-text-weight-semibold"
                >RSVP Status: </span
              >{{ invitee.isAttending }}
            </p>
            <b-button
              type="is-primary"
              rounded
              size="is-medium"
              class="m-2"
              v-on:click="isAttending('Pending')"
            >
              <i class="fas fa-exchange-alt"></i>
              Change my Status</b-button
            >
          </span>
        </div>

        <div v-show="invitee.isAttending !== 'Declined'">
          <span>
            <b-button
              type="is-primary"
              rounded
              size="is-medium"
              class="m-2"
              v-on:click="hasVoted(true)"
              v-show="!this.invitee.hasVoted"
            >
              <i class="fas fa-check"></i>
              I'm done voting!</b-button
            >
          </span>

          <span>
            <b-button
              type="is-primary"
              rounded
              size="is-small"
              class="m-2"
              v-on:click="hasVoted(false)"
              v-show="this.invitee.hasVoted"
            >
              <i class="fas fa-times"></i>
              Hey, let me revote!</b-button
            >
          </span>
        </div>
      </div>
      </div>
    </div>
    <center v-if="!invitee.deadlinePassed && !isLoading"><restaurant-card
            v-for="restaurant in this.businessDetails"
            :key="restaurant.id"
            :restaurant="restaurant"
            class="card"
          /></center>

    <div v-if="errorMsg">Invite not Found!!!!!!!!!</div>
    <div v-if="invitee.deadlinePassed">
      <h1>Sorry, the voting period for this invite has passed.</h1>
    </div>
  </div>
</template>

<script>
import InviteService from "../services/InviteService";
import RestaurantCard from "./RestaurantCard.vue";

export default {
  components: { RestaurantCard },
  data() {
    return {
      invitee: {},
      isLoading: true,
      errorMsg: false,
      businessDetails: [],
      currentTime: "",
      deadlinePassed: "",
      undoVote: {
        inviteId: 1,
        yelpId: "",
        uniqueId: "",
      },
    };
  },
  created() {
    InviteService.getInvitee(this.$route.params.uniqueId).then((response) => {
      this.invitee = response.data;

      if (this.invitee.uniqueId === null) {
        this.errorMsg = true;
      }

      this.businessDetails = this.invitee.businessDetails;
      this.invitee.businessDetails = null;
      this.$store.commit("SET_CURRENT_INVITEE", this.invitee);
      this.isLoading = false;
      this.undoVote.uniqueId = this.invitee.uniqueId;
      this.undoVote.inviteId = this.invitee.inviteId;
    });
  },

  methods: {
    isAttending(status) {
      this.invitee.isAttending = status;
      this.$store.commit("SET_CURRENT_INVITEE", this.invitee);
      InviteService.updateInvitee(this.invitee);
      if (status === "Declined") {
        InviteService.undoAllVotes(this.undoVote);
      }
    },
    hasVoted(status) {
      this.invitee.hasVoted = status;
      this.$store.commit("SET_CURRENT_INVITEE", this.invitee);
      InviteService.updateInvitee(this.invitee);
    },
  },
};
</script>

<style scoped>



.columns{
  justify-content:center;
  align-items:center;

}
.main {
  min-height: 100%;
}
h1 {
  font-size: 2.7vw;
  text-align: center;
}

h2 {
  font-size: 2.3vw;
  font-weight: 600;
  text-align: center;
}

h3 {
  font-size: 1.7vw;
  font-weight: bold;
  text-align: center;
}

p {
  font-size: 1.5vw;
  text-align: center;
}
.main {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.welcome-message {
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  margin-bottom: 2em;
}

.restaurants {
  display: flex;
  flex-direction: column;
}
.button.is-primary {
  background-color: #dc6b67;
}
.button.is-primary:hover {
  background-color: #d6544f;
}

.card {
  margin-top: 3em;
}
</style>