<template>
  <section>
    <div class="loading-gif" v-if="isLoading">
      <center><img src="../assets/loading.gif" /></center>
    </div>
    <main class="tile is-ancestor" v-if="!isLoading">
      <div class="tile is-parent is-4 is-vertical ml-3">
        <div class="tile is-child">
          <div class="tile is-child">
            <br />
            <h1 class="title box has-text-centered">
              "{{ this.invite.inviteName }}"
            </h1>
            <div class="tile is-child box">
              <h2 v-show="!this.invite.deadlinePassed" class="title is-size-5">Leading Restaurant:</h2>
              <h2 v-show="this.invite.deadlinePassed" class="title is-size-5">Winning Restaurant:</h2>
              <h3 class="title is-size-6">{{businessDetails[0].name}}</h3>
            </div>
            <br/>
            <div class="tile is-child box">
              <h2 class="title is-size-5">When:</h2>
              <h3 class="title is-size-6">{{ this.invite.reservationDate | moment("dddd, MMMM Do, YYYY h:mm a")}}</h3>
            </div>
            <br />
            <div class="tile is-child box">
              <h2 class="title is-size-5">Participants:</h2>
              <h3 v-for="invitee in this.friends" v-bind:key="invitee.uniqueId">
                {{ invitee }}
              </h3>
            </div>
            <br />
            <div class="tile is-child box">
              <h2 class="title is-size-5">Voting Deadline:</h2>
              <div class="tile is-child">
                <b-button
                  v-show="!this.invite.deadlinePassed"
                  class="button is-center is-success is-rounded tile is-child has-text-justified has-text-centered"
                  >Voting Open</b-button
                >
                <b-button
                  label="Voting Closed"
                  placeholder="Voting Closed"
                  v-show="this.invite.deadlinePassed"
                  class="button is-center is-danger is-rounded tile is-child has-text-justified has-text-centered"
                />
              </div>
              <br/>
              <h3 class="title is-size-6">{{ this.invite.deadline | moment("dddd, MMMM Do, YYYY h:mm a")}}</h3>
              
            </div>
            <br />
            
          </div>
        </div>
      </div>
      <div class="tile is-parent mr-3">
        <b-collapse
          class="tile is-child"
          animation="slide"
          :open.sync="isOpen"
          aria-id="contentIdForA11y3"
        >
          <template #trigger="props">
            <br />
            <div class="tile is-child box">
              <p v-if="!isOpen" id="finalists" class="title is-child has-text-centered">View Finalists</p>
              <p v-if="isOpen" id="finalists" class="title is-child has-text-centered">Hide Finalists</p>
            </div>
          </template>

          <br />
          <div>
            <restaurant-invite-detail-card
              class="tile is-child is-vertical"
              v-for="restaurant in this.businessDetails"
              v-bind:restaurant="restaurant"
              v-bind:key="restaurant.id"
            />
          </div>
        </b-collapse>
      </div>
    </main>
  </section>
</template>

<script>
import InviteService from "../services/InviteService";
import RestaurantInviteDetailCard from "../components/RestaurantInviteDetailCard";

export default {
  components: { RestaurantInviteDetailCard },
  name: "invite-detail",

  data() {
    return {
      invite: {},
      isLoading: true,
      friends: [],
      businessDetails: [],
      isOpen: false,

      /*invite.businessDetails loop to get businesses*/
    };
  },
  created() {
    InviteService.getInviteById(this.$route.params.id).then((response) => {
      this.invite = response.data;
      this.isLoading = true;
      for (let j = 0; j < this.invite.invitees.length; j++) {
        this.friends.push(this.invite.invitees[j].name);
        if (j < this.invite.invitees.length - 1) {
          this.friends[j] += ", ";
        }
      }
      this.businessDetails = this.invite.businessDetails;
      this.businessDetails.sort(function (a, b) {
        return b.totalThumbsUp - a.totalThumbsUp;
      });
      this.isLoading = false;
    });
  },
};
</script>

<style scoped>
#finalists {
  color: #dc6b67;
}
h2 {
  color:#dc6b67;
}
.loading-gif {
  margin-left: auto;
  margin-right: auto;
}

.loading-gif img {
  width: 400px;
  height: 400px;
}
</style>