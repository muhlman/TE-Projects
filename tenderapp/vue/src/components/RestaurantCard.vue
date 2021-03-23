<template>
  <span class="columns" >
<!-- Restaurant Card Image -->
    <div class="column is-one-quarter">
      <router-link :to="{ name: 'details', params: { id: restaurant.id } }"
        ><figure class="image is-square"><img :src="restaurant.image_url" 
      /></figure></router-link>
    </div>
<!-- Restaurant Card Image Ends -->
<!-- Restaurant Card Information -->
    <div class="column">
      <!-- Categories -->
        <span
          class="categories-tag"
          v-for="category in this.categories"
          v-bind:key="category"
        >
          {{ category }}
        </span>
<!-- Restaurant Name -->
      <router-link :to="{ name: 'details', params: { id: restaurant.id } }"
        ><h1 class="is-size-3">{{ restaurant.name }}</h1>
      </router-link>
<!-- Stars -->
      <div class="star-rating">
        <star-rating
          :rating="restaurant.rating"
          :read-only="true"
          :increment="0.01"
          :show-rating="false"
          :inline="true"
          :star-size="25"
        />
      </div>
      <!-- Location/Phone Number -->
      <div class="location-phone">
        <p class="has-text-weight-medium" v-for="ln in restaurant.location.display_address"
          v-bind:key="ln.display_address">
          {{ ln }}
        </p>
        <p>{{ restaurant.display_phone }} </p>
        <p class="price">
          Price:
          <span class="has-text-success has-text-weight-bold">{{
            restaurant.price}}</span>
        </p>
      </div>
      <!-- Vote Buttons -->
      <!-- Like -->
      <span class="invite-buttons" v-if="$route.name === 'inviteeView'" v-show="!checkVote">
        <span class="yes-button" v-show="!this.$store.state.currentInvitee.hasVoted">
          <b-button v-on:click="thumbsUp()" type="is-primary" rounded size="is-small" class="m-2" v-show="!hasVoted">
            <i class="far fa-thumbs-up"></i>
            LIKE</b-button
          >
        </span>
         <!-- NAH -->
          <b-button v-on:click="thumbsDown()" type="is-primary" rounded size="is-small" class="m-2" v-show="!hasVoted">
            <i class="far fa-sad-tear"></i>
            NAH</b-button
          >
           <!-- Undo Vote -->
          <b-button v-on:click="undoVote()" type="is-primary" rounded size="is-small" class="m-2" v-show="hasVoted">
            <i class="fas fa-undo-alt"></i>
            Whoops</b-button
          >
      </span>
      <!-- Show how user voted -->
      <div>
        <p class="is-size-5" v-show="showVoteStatus">Your vote: 
      <span v-show="this.vote.thumbs_up">
        <i class="far fa-thumbs-up has-text-success"></i>
      </span>
        <span v-show="this.vote.thumbs_down">
        <i class="far fa-thumbs-down has-text-danger"></i>
      </span></p>
      </div>
       <!-- Call To Order -->
      <span v-if="$route.name !== 'inviteeView'" >
        <span class="call-to-order">
        <a :href="`tel:${restaurant.phone}`">
          <b-button type="is-primary" rounded>
            <i class="fas fa-phone-alt"></i>
            Call to order</b-button>
        </a>
        </span >
        <!-- Add to List -->
         <b-button class="add-to-party" rounded  type="is-primary"
           v-show="this.$store.state.sideMenuToggle" v-on:click="addToList(restaurant)">
           <i class="fas fa-plus"></i>
           Add To Party</b-button>
           </span>
      <!-- Delivery/ TakeOut Tags -->
      <div v-if="$route.name !== 'inviteeView'" class="transactions">
        <b-button size="is-small">
          <i
            class="fas fa-check has-text-success"
            v-if="transactionTypes.includes('pickup')"
          ></i>
          <i
            class="fas fa-times has-text-danger"
            v-if="!transactionTypes.includes('pickup')"
          ></i>
          Takeout</b-button
        >
        <b-button size="is-small">
          <i
            class="fas fa-check has-text-success"
            v-if="transactionTypes.includes('delivery')"
          ></i>
          <i
            class="fas fa-times has-text-danger"
            v-if="!transactionTypes.includes('delivery')"
          ></i>
          Delivery</b-button
        >
        <b-button size="is-small">
          <i
            class="fas fa-check has-text-success"
            v-if="transactionTypes.includes('restaurant_reservation')"
          ></i>
          <i
            class="fas fa-times has-text-danger"
            v-if="!transactionTypes.includes('restaurant_reservation')"
          ></i>
          Reservations</b-button
        >
        <br />
      </div>
    </div>
  </span>
</template>
<script>
import InviteService from '../services/InviteService.js'
import StarRating from "vue-star-rating";
export default {
  name: "restaurant-card",
  components: {
    StarRating,
  },
  props: ["restaurant"],
  data() {
    return {
      transactionTypes: "",
      categories: [],
      hasVoted: false,
      vote: {
        yelpId: this.restaurant.id,
        thumbs_up: false,
        thumbs_down: false,
        inviteId: this.$store.state.currentInvitee.inviteId,
        uniqueId: this.$store.state.currentInvitee.uniqueId,
      }
    }
  },
  created() {
    this.vote.thumbs_up = this.restaurant.inviteeVotes.thumbs_up;
    this.vote.thumbs_down = this.restaurant.inviteeVotes.thumbs_down;
    for (let i = 0; i < this.restaurant.transactions.length; i++) {
      this.transactionTypes +=
        this.restaurant.transactions[i].transactions + " ";
    }
    for (let j = 0; j < this.restaurant.categories.length; j++) {
      this.categories.push(this.restaurant.categories[j].title);
      if (j < this.restaurant.categories.length - 1) {
        this.categories[j] += " | ";
      }
    }
  },
computed: {
    checkVote() {
      const hasVotedState = this.$store.state.currentInvitee.hasVoted === true;
      const attending = this.$store.state.currentInvitee.isAttending !== 'Declined';
      return hasVotedState || !attending;
    },
    showVoteStatus() {
      const voted = this.vote.thumbs_down || this.vote.thumbs_up;
      const attending = this.$store.state.currentInvitee.isAttending !== 'Declined';
      return voted && attending;
    }
  },
  methods: { 
    thumbsUp(){
      this.vote.thumbs_up = true;
      this.vote.thumbs_down = false;
      InviteService.voteThumbsUp(this.vote)
      this.hasVoted = true;
    },
    thumbsDown(){
      this.vote.thumbs_down = true;
      this.vote.thumbs_up = false;
      InviteService.voteThumbsDown(this.vote);
      this.hasVoted = true;
    },
     undoVote(){
      this.vote.thumbs_down = false;
      this.vote.thumbs_up = false;
      InviteService.undoVote(this.vote);
      this.hasVoted = false;
    },
    addToList(restaurant) {
      if(!this.$store.state.invitation.restaurantChoices.includes(restaurant)){
      this.$store.commit("ADD_CHOICE_TO_LIST", restaurant);
      this.$emit('update-list', restaurant);
      console.log(this.$store.state.invitation.restaurantChoices);
      }
    },
  },
};
</script>
<style scoped>

.columns{
  margin-bottom:50px;
  padding:25px 0px  10px 20px;
  width:65%;
  transition: transform .2s;
 
}
.button.is-primary{
  background-color: #dc6b67;
  margin-top:1em;
  margin-right:1.7em;
}
.button.is-fullwidth{
   background-color: #dc6b67;
}



.action-buttons {
 display:flex;
 margin-top:2em;
}
.nah-button .button.is-rounded {
  background-color: #dc6b67;
}
.nah-button .button.is-rounded:hover {
  background-color: #ad5451;
}
.yes-button .button.button.is-rounded {
  background-color: #a5c064;
}
.yes-button .button.button.is-rounded:hover {
  background-color: #9cbd4f;
}
.add-to-list .button.button.is-rounded:hover {
  background-color: #9dbd52;
}
.add-to-party {
  background-color: #81974e;
}
.invite-buttons .button.is-rounded {
  width: 7em;
}
.transactions > button.is-small {
  border: none;
  font-size: 1em;
  font-weight: bold;
  margin-top:-.7rem;
}
.price {
  font-weight: 550;
}
.star-rating {
  padding: 5px 0px;
  display: inline-block;
  width: 0.2em;
}
.categories {
  padding: 10px 0px 0px 0px;
}
.categories-tag {
  color: rgb(179, 179, 179);
}
.transactions {
  margin-top: 25px;
  margin-left: -0.5em;
}


.columns:hover {
  -ms-transform: scale(1.1); /* IE 9 */
  -webkit-transform: scale(1.1); /* Safari 3-8 */
  transform: scale(1.1);

}
.title:hover {
  color: #dc6b67;
}
</style>