<template>
  <section>
    <div class="loading-gif" v-if="isLoading">
      <center><img src="../assets/loading.gif" /></center>
    </div>
    <div>
      <br />
      <div class="tile is-ancestor mx-3 " v-if="!isLoading">
        <div class="tile is-parent is-4">
          
          <div class="tile is-child box has-text-centered">
            <div>
            <span
              class="has-text-weight-medium mb-3"
              v-for="category in this.categories"
              v-bind:key="category"
            >
              {{ category }}
            </span>
            <h1 class="title">{{ restaurant.name }}</h1>
          </div>

              <img class="mt-1" v-bind:src="restaurant.image_url" />
            
          </div>
        </div>
        <div class="tile is-parent is-4">
          <div class="tile is-child is-justify-content-center box has-text-centered">
            <span class="has-text-weight-bold"
              >Price:
              <span class="has-text-success">{{ restaurant.price }} </span>
            </span>
            <div class="stars">
              <star-rating
                class="stars"
                :rating="restaurant.rating"
                :read-only="true"
                :increment="0.01"
                :show-rating="false"
                :star-size="25"
              />
              <a
                class="has-text-weight-semibold"
                :href="restaurant.url"
                target="_blank"
              >
                View reviews on
                <img
                  height="12px"
                  width="12px"
                  src="../assets/yelp.png"
                />Yelp</a
              >
            </div>

            <div></div>
            <div>
              <span
                class="has-text-weight-medium"
                v-for="ln in restaurant.location.display_address"
                v-bind:key="ln.display_address"
              >
                {{ ln }}
              </span>
              <br />
            </div>

            <div>
              <span class="has-text-weight-medium"
                >{{ restaurant.display_phone }}
              </span>
            </div>

            <div class="transactions has-addons mt-3 mb-3 mr-3">
              <b-button size="is-small mr-1">
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
              <b-button size="is-small mr-1">
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
            </div>
            <div class="call-to-action">
              <a class="mr-3" :href="`tel:${restaurant.phone}`">
                <b-button type="is-primary" rounded size="is-small">
                  <i class="fas fa-phone-alt"></i>
                  Call to order</b-button
                >
              </a>
              <a
                class="m-5"
                :href="`https://www.google.com/maps/dir/${
                  this.$store.state.searchDirections
                }/${restaurant.location.display_address
                  .toString()
                  .split(' ')
                  .join('+')}/`"
                target="_blank"
              >
                <b-button rounded size="is-small">
                  <i class="fas fa-route"></i>
                  Get Directions</b-button
                >
              </a>
            </div>
          </div>
        </div>
        <div class="tile is-parent is-4">
        <div class="tile is-child box has-text-centered">
          <p class="has-text-weight-bold mt-2 mb-1">Hours:</p>
          <p v-for="hours in restaurant.hours" v-bind:key="hours.hours_type">
            <span
              class="has-text-weight-semibold"
              v-for="open in hours.open"
              v-bind:key="open.day"
            >
              <span
                v-if="open.day === 6"
                :class="{ 'has-text-success': restaurant.day == 6 }"
                >Sunday: {{ open.start }} - {{ open.end }}<br
              /></span>
              <span
                v-if="open.day === 0"
                :class="{ 'has-text-success': restaurant.day == 0 }"
                >Monday: {{ open.start }} - {{ open.end }}<br
              /></span>
              <span
                v-if="open.day === 1"
                :class="{ 'has-text-success': restaurant.day == 1 }"
                >Tuesday: {{ open.start }} - {{ open.end }}<br
              /></span>
              <span
                v-if="open.day === 2"
                :class="{ 'has-text-success': restaurant.day == 2 }"
                >Wednesday: {{ open.start }} - {{ open.end }}<br
              /></span>
              <span
                v-if="open.day === 3"
                :class="{ 'has-text-success': restaurant.day == 3 }"
                >Thursday: {{ open.start }} - {{ open.end }}<br
              /></span>
              <span
                v-if="open.day === 4"
                :class="{ 'has-text-success': restaurant.day == 4 }"
                >Friday: {{ open.start }} - {{ open.end }}<br
              /></span>
              <span
                v-if="open.day === 5"
                :class="{ 'has-text-success': restaurant.day == 5 }"
                >Saturday: {{ open.start }} - {{ open.end }}<br
              /></span>
            </span>
          </p>
          <p v-for="hours in restaurant.hours" v-bind:key="hours.hours_type">
            <b-button
              class="mt-4 is-rounded"
              v-bind:type="{
                'is-danger': !hours.is_open_now,
                'is-success': hours.is_open_now,
              }"
            >
              <span v-if="!hours.is_open_now">Currently Closed</span>
              <span v-else>Currently Open</span>
            </b-button>
          </p>
        </div>
      </div>
    </div>
    </div>
  </section>
</template>

<script>
import RestaurantService from "../services/RestaurantService";
import StarRating from "vue-star-rating";

export default {
  name: "restaurant-detail",
  props: ["isInitialSearch"],
  components: {
    StarRating,
  },
  data() {
    return {
      restaurant: {},
      isLoading: true,
      directionsAddress: [],
      transactionTypes: "",
      categories: [],
      category: "",
    };
  },
  created() {
    RestaurantService.getTheRestaurant(this.$route.params.id).then(
      (response) => {
        this.restaurant = response.data;
        this.isLoading = false;

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
      }
    );
  },
};
</script>

<style scoped>
.stars {
  justify-content: center;
}
img {
  max-height: 200px;
}
</style>