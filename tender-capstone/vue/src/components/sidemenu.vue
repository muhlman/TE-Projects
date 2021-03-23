<template>
<span>

<div id="mySidenav" class="sidenav" v-bind:style="{width: isMenuOpen ? '300px' : '0'}">
  <a href="#" class="closebtn" v-on:click="isMenuOpen=false">&times;</a>
  <a href="#">About</a>
  <a href="#">Services</a>
  <a href="#">Clients</a>
  <a href="#">Contact</a>
</div>

<!-- Use any element to open the sidenav -->

<span  v-on:click="isMenuOpen=true"><button>OPEN</button></span>
<!-- Add all page content inside this div if you want the side nav to push page content to the right (not used if you only want the sidenav to sit on top of the page -->
<div id="main" v-bind:class="{ 'open-main': isMenuOpen }">
  <span  v-on:click="isMenuOpen=true"><button>OPEN</button></span>
   <div class="initial-search" >
        <div class="loading-gif">
          <img src="../assets/loading.gif" />
        </div>
        <div >
           <h1 class="loading-text">Hey {{this.$store.state.user.username}}! <br>
         What would you like to do today?</h1>
        </div>

        

        <form class="search-main" v-on:submit.prevent>
       
       <button class="find-food" v-on:click="searchForRestaurants()" focused>Search For Nearby Restaurants</button>
       
  
             <button class="find-food" v-on:click="invite()" focused>
          Invite Friends Out For Food
          </button>
             <button class="find-food" v-on:click="searchByLocation()" focused>
        View My Profile
          </button>
        </form>
      </div>

</div>



  </span>

<!--
    <section class="login-search">
      <div class="initial-search" >
        <div class="loading-gif">
          <img src="../assets/loading.gif" />
        </div>
        <div >
           <h1 class="loading-text">Hey {{this.$store.state.user.username}}! <br>
         What would you like to do today?</h1>
        </div>

        

        <form class="search-main" v-on:submit.prevent>
       
       <button class="find-food" v-on:click="searchForRestaurants()" focused>Search For Nearby Restaurants</button>
       
  
             <button class="find-food" v-on:click="invite()" focused>
          Invite Friends Out For Food
          </button>
             <button class="find-food" v-on:click="searchByLocation()" focused>
        View My Profile
          </button>
        </form>
      </div>
    </section>

    -->

<!-- <dinner-list /> -->

</template>

<script>


export default {
  name: "home",

  data(){
    return{
        isMenuOpen: false
    }
  },
  methods: {
    searchForRestaurants(){
      this.$router.push({ name: 'restaurantFinder'});
    },
      invite(){
      this.$router.push({ name: 'createInvite'});
    },




  }
};
</script>
<style scoped>



  


.open-main{
  margin-left:250px;
}


.open-nav{
  height: 100%; /* 100% Full-height */
  width: 250px; /* 0 width - change this with JavaScript */
  position: fixed; /* Stay in place */
  z-index: 1; /* Stay on top */
  top: 0; /* Stay at the top */
  left: 0;
  background-color: #111; /* Black*/
  overflow-x: hidden; /* Disable horizontal scroll */
  padding-top: 60px; /* Place content 60px from the top */
  transition: 0.5s; /* 0.5 second transition effect to slide in the sidenav */

}

/* The side navigation menu */
.sidenav {
  height: 100%; /* 100% Full-height */
  width: 0px; /* 0 width - change this with JavaScript */
  position: fixed; /* Stay in place */
  z-index: 1; /* Stay on top */
  top: 0; /* Stay at the top */
  left: 0;
  background-color: #111; /* Black*/
  overflow-x: hidden; /* Disable horizontal scroll */
  padding-top: 60px; /* Place content 60px from the top */
  transition: 0.5s; /* 0.5 second transition effect to slide in the sidenav */
}

/* The navigation menu links */
.sidenav a {
  padding: 8px 8px 8px 32px;
  text-decoration: none;
  font-size: 25px;
  color: #000000;
  display: block;
  transition: 0.3s;
}

/* When you mouse over the navigation links, change their color */
.sidenav a:hover {
  color: #f1f1f1;
}

/* Position and style the close button (top right corner) */
.sidenav .closebtn {
  position: absolute;
  top: 0;
  right: 25px;
  font-size: 36px;
  margin-left: 50px;
}

/* Style page content - use this if you want to push the page content to the right when you open the side navigation */
#main {
  transition: margin-left .5s;
  padding: 20px;
}

/* On smaller screens, where height is less than 450px, change the style of the sidenav (less padding and a smaller font size) */
@media screen and (max-height: 450px) {
  .sidenav {padding-top: 15px;}
  .sidenav a {font-size: 18px;}
}


