<template>
  <section>
    <b-collapse
      class="card"
      animation="slide"
      :open.sync="isOpen"
      aria-id="contentIdForA11y3"
    >
      <template #trigger="props">
        <div>
          <div
            class="card-header"
            role="button"
            aria-controls="contentIdForA11y3"
          >
            <p class="card-header-title is-size-5 has-text-weight-medium">
              {{ dinner.inviteName }}
            </p>
          </div>
        </div>
      </template>

      <div class="card-content">
        <div>
          <div>
            <span class="has-text-weight-semibold">When: </span
            >{{ dinner.reservationDate | moment("dddd, MMMM Do, YYYY h:mm a") }}
            <span class="px-6">
          
          
            <b-button
              label="Voting Open"
              placeholder="Voting Open"
              v-show="!this.dinner.deadlinePassed"
              class="is-success is-small is-rounded"
            />
            <b-button
              label="Voting Closed"
              placeholder="Voting Closed"
              v-show="this.dinner.deadlinePassed"
              class="is-danger is-small is-rounded"
            />
          
        </span>
          </div>
        </div>
        <br />
        <div>
          <p class="has-text-weight-semibold">
            Where:
            <span
              class="has-text-weight-normal"
              v-for="rst in this.restaurants"
              v-bind:key="rst.name"
            >
              {{ rst }}
            </span>
          </p>
        </div>
        <br />
        <div>
          <p class="has-text-weight-semibold">
            Friends:
            <span
              class="has-text-weight-normal"
              v-for="invitee in this.friends"
              v-bind:key="invitee.uniqueId"
            >
              {{ invitee }}
            </span>
          </p>
        </div>
        <br />
        <span>
          <router-link
            :to="{ name: 'inviteDetailsPage', params: { id: dinner.inviteId } }"
          >
            <button class="button is-rounded is-primary" type="is-info" size="is-small" 
              >View Details</button
            >
          </router-link>
        </span>
      </div>
    </b-collapse>
  </section>
</template>
    <!--
    <b-collapse
      class="card"
      animation="slide"
      :open.sync="isOpen"
      aria-id="contentIdForA11y3"
    >
      <template #trigger="props">
          <p class="card-header is-size-4 is-vcentered" >{{ dinner.inviteName }}</p>
          <a class="card-header-icon">
            <b-icon :icon="props.open ? 'menu-down' : 'menu-up'"> </b-icon>
          </a>
        
      </template>

      <div class="card-content">
        <div class="content">
          <div class="columns">
            <div class="column is-three-quarters">
              <span class="has-text-weight-semibold">When: </span
              >{{
                dinner.reservationDate | moment("dddd, MMMM Do, YYYY h:mm a")
              }}
            </div>
            <div
              class="column has-text-left-mobile has-text-right-tablet is-one-quarter is-desktop"
            >
              <b-button
                label="Voting Open"
                placeholder="Voting Open"
                v-show="!this.dinner.deadlinePassed"
                class="is-success is-small"
              />
              <b-button
                label="Voting Closed"
                placeholder="Voting Closed"
                v-show="this.dinner.deadlinePassed"
                class="is-danger is-small"
              />
            </div>

            <div class="column is-full">
              <p class="has-text-weight-semibold">
                Where:
                <span
                  class="has-text-weight-normal"
                  v-for="rst in this.restaurants"
                  v-bind:key="rst.name"
                >
                  {{ rst }}
                </span>
              </p>
            </div>

            <div class="column is-full">
              <p class="has-text-weight-semibold">
                Friends:
                <span
                  class="has-text-weight-normal"
                  v-for="invitee in this.friends"
                  v-bind:key="invitee.uniqueId"
                >
                  {{ invitee }}
                </span>
              </p>
            </div>

            <div class="column is-full">
              <p class="has-text-weight-semibold">
                Friends:
                <span
                  class="has-text-weight-normal"
                  v-for="invitee in this.friends"
                  v-bind:key="invitee.uniqueId"
                >
                  {{ invitee }}
                </span>
              </p>
            </div>
          </div>
          <p class="has-text-weight-semibold">
            Where:
            <span
              class="has-text-weight-normal"
              v-for="rst in this.restaurants"
              v-bind:key="rst.name"
            >
              {{ rst }}
            </span>
          </p>

          <p class="has-text-weight-semibold">
            Friends:
            <span
              class="has-text-weight-normal"
              v-for="invitee in this.friends"
              v-bind:key="invitee.uniqueId"
            >
              {{ invitee }}
            </span>
          </p>

          <br />
          <router-link
            :to="{ name: 'inviteDetailsPage', params: { id: dinner.inviteId } }"
          >
            <b-button class="btn" type="is-info" size="is-small"
              >View Details</b-button
            >
          </router-link>
        </div>
      </div>
    </b-collapse>


  -->  

<script>
export default {
  name: "scheduled-dinner",
  props: ["dinner"],

  data() {
    return {
      restaurants: [],
      friends: [],
      isOpen: false,
    };
  },

  created() {
    for (let j = 0; j < this.dinner.invitees.length; j++) {
      this.friends.push(this.dinner.invitees[j].name);
      if (j < this.dinner.invitees.length - 1) {
        this.friends[j] += ", ";
      }
    }
    for (let j = 0; j < this.dinner.businessDetails.length; j++) {
      this.restaurants.push(this.dinner.businessDetails[j].name);
      if (j < this.dinner.businessDetails.length - 1) {
        this.restaurants[j] += ", ";
      }
    }
  },
};
</script>

<style scoped>
.card-header-title:hover {
  color: #dc6b67;
}

button {
  display:  inline-block;
}

</style>