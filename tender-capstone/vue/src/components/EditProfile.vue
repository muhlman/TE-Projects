<template>
<div>
  <main class="columns is-centered">

    <div class="column is-half">
    <div id="edit-profile-container">
      <div id="leftside" class="card">
        <form class="form-edit-profile">
          <h1 class="h3 mb-3 font-weight normal">Edit Profile</h1>
          <div class="alert" role="alert" v-if="editProfileErrors">
            {{ editProfileErrorMsg }}
          </div>
          <b-field label="First Name">
            <b-input v-model="user.firstName" required></b-input>
          </b-field>
          <b-field label="Last Name">
            <b-input v-model="user.lastName" required></b-input>
          </b-field>
          <b-field label="Email Address">
            <b-input v-model="user.email">
              <i class="fas fa-envelope"></i>
            </b-input>
          </b-field>
          <b-field label="Username">
            <b-input maxLength="15" v-model="user.username" required></b-input>
          </b-field>
          <b-field label="Address">
            <b-input v-model="user.userDetails.address"> </b-input>
          </b-field>
          <b-field label="City">
            <b-input v-model="user.userDetails.city"> </b-input>
          </b-field>
          <b-field label="State">
            <b-input v-model="user.userDetails.state"> </b-input>
          </b-field>
          <b-field label="Zip Code">
            <b-input v-model="user.userDetails.zip"></b-input>
          </b-field>
        </form>
 
</div>
      
        <div class="column">
         <div> 
        <h1 class="h3 mb-3 font-weight normal">Edit Favorite Categories</h1>
        </div>
        <div class="column-check">
         <label
            class="c-container"
            v-for="category in this.foodCategories"
            v-bind:key="category.categoryId"
            >{{ category.displayName }}
            <input
              type="checkbox"
              checked="checked"
              v-bind:id="category.categoryId"
              v-bind:value="category.categoryId"
              v-model.number="userCategories"
            />
            <span class="checkmark"></span>
          </label>
   

</div>
        </div>
      </div>
    </div>

  </main>
  <div class="container">
      <b-button class="is-rounded button is-primary" rounded type="submit" v-on:click.prevent="editProfile">Save Changes</b-button>
      </div>
  </div>
</template>
<script>
import RestaurantService from "../services/RestaurantService.js";
import AuthService from "../services/AuthService.js";
export default {
  name: "edit-profile",
  data() {
    return {
      user: {},
      userCategories: [],
      foodCategories: [],
      editProfileErrors: false,
      editProfileErrorMsg: "There was an issue editing your profile,",
    };
  },
  methods: {
    editProfile() {
      this.user.userDetails.activeCategoryId = this.userCategories;
      AuthService.updateUser(this.user).then((response) => {
        this.$store.commit("UPDATE_USER", response.data);
        this.user = this.$store.state.user;
        this.$router.push("/profile");
      });
    },
  },
  created() {
    this.user = this.$store.state.user;
    this.userCategories = this.$store.state.user.userDetails.activeCategoryId;
    RestaurantService.getAvailableCategories().then((response) => {
      this.foodCategories = response.data;
    });
  },
};
</script>
<style scoped>


.container{
  justify-content:center;
  margin-left:250px;
}

h1 {
  text-align: center;
  font-weight: 700;
  font-size: 1.5em;
}
.main-section {
  background-color: #fdf2f2;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100vw;
  height: 100vh;
  justify-content: center;
}
#edit-profile-container {
  align-self: center;
  background-color: white;
  display: flex;
  justify-content: center;
  margin: 20px;
}
.form-edit-profile {
  margin: 20px;
  padding: 10px;
}
.column {
  float: left;
  justify-content: space-evenly;
  align-content: space-around;
  
}
.container input {
  display: flex;
  padding: 2px;
  margin-top: 6px;
  margin-right: 5px;
  cursor: pointer;
}
span.checkmark {
  display: flex;
  flex-basis: auto;
  justify-content: space-evenly;
  padding: 5px;
}

.is-primary {
    background-color: #dc6b67;
}

#leftside {
  width: 30vw;
  padding: 15px;
}
#rightside {
  width: 30vw;
}
.container {
  display: flex;
  flex-direction: row;
  position: relative;
  cursor: pointer;
  font-size: 1em;
  font-weight: bold;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
.center {
  display: flex;
  align-content: center;
  justify-content: center;
  padding-top: 25%;
  padding-right: 5%;
  width: 50%;
  margin: auto;
}
button {
  display: inline-block;
}

/* Checkbox */
.column {
  display:flex;
  flex-wrap: wrap;
  justify-content: left;
}

.c-container {
  padding-right:30px;
  display: flex;
  position: relative;
  padding-left: 30px;
  margin-bottom: 12px;
  cursor: pointer;
  font-size: 1em;
  font-weight: bold;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

.c-container input {
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
}

/* Create a custom checkbox ------------------- */
.checkmark {
  position: absolute;
  top: 0;
  left: 0;
  height: 25px;
  width: 25px;
  border-style: solid;
  border-width: 0.1em;
  border-color: #1e1e32;
}

/* On mouse-over, add a grey background color */
.c-container:hover input ~ .checkmark {
  background-color: #ccc;
}

/* When the checkbox is checked, add a blue background */
.c-container input:checked ~ .checkmark {
  background-color: #dc6b67;
}

/* Create the checkmark/indicator (hidden when not checked) */
.checkmark:after {
  content: "";
  position: absolute;
  display: none;
}

/* Show the checkmark when checked */
.c-container input:checked ~ .checkmark:after {
  display: block;
}

/* Style the checkmark/indicator */
.c-container .checkmark:after {
  left: 9px;
  top: 5px;
  width: 5px;
  height: 10px;
  border: solid white;
  border-width: 0 3px 3px 0;
  -webkit-transform: rotate(45deg);
  -ms-transform: rotate(45deg);
  transform: rotate(45deg);
}

/*Checkbox Ends*/

</style>