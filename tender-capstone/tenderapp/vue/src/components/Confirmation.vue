<template>
  <div class="main">
    <div class="container mb-4 box">
      <div class="">
        <h1 class="title has-text-weight-semibold has-text-centered">Yay! Your invitation is all set. Here is your confirmation:</h1>
        <h2 class="is-size-2 has-text-weight has-text-centered">{{ invitation.inviteName }}</h2>
      </div>
    </div>
    <div class="container mb-4 box">
      <div >
        <h3 class="is-size-5 has-text-weight-bold has-text-centered">Reservation Date: 
        <span class="is-size-5 has-text-weight-medium">
          {{
            invitation.reservationDate | moment("dddd, MMMM Do, YYYY h:mm a")
          }}
        </span></h3>
        <h3 class="is-size-5 has-text-weight-bold has-text-centered" >Voting Deadline: 
        <span class="is-size-5 has-text-weight-medium">{{ invitation.deadline | moment("dddd, MMMM Do, YYYY h:mm a") }}</span></h3>
      </div>
    </div>
    <div class="container mb-4 box">
      <div class="">
        <h3 class="is-size-4 has-text-weight-bold">Friends List</h3>
        <p>
          Send each friend on your list their unique voting link to submit their
          restaurant vote
        </p><br>
        <span
          v-for="invitee in invitation.invitees"
          v-bind:key="invitee.uniqueId"
        >
          <div class="">
            <p class="is-size-5 has-text-weight-semibold">Invite Link for {{ invitee.name }}</p>
          </div>
          <div class="field is-grouped">
            <p class="control is-expanded">
              <input
                :id="invitee.uniqueId"
                class="input"
                type="text"
                :value="
                  'https://www.tenderinvite.com/invite/' + invitee.uniqueId
                "
                readonly
              />
            </p>
            <p class="control">
              <button
                class="button"
                @click.stop.prevent="copyUrl(invitee.uniqueId)"
              >
                <span class="icon mr-1"> <i class="far fa-copy"></i> </span>Copy
              </button>
            </p>
          </div>
          <br>
        </span>
      </div>
    </div>
    <div class="container box">
<br>
        <p class="is-size-2 has-text-weight-bold has-text-centered">Restaurant Choices</p><br>
        <div class="choices">
        <div class="has-text-centered"
          v-for="restaurant in invitation.businessDetails"
          v-bind:key="restaurant.yelpId"
        >
         
           <p class="is-size-5">{{ restaurant.name }}</p>
         
      
          
              <img :src="restaurant.image_url"  class="image"/>
            
         
        
      
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      invitation: {},
    };
  },
  created() {
    this.invitation = this.$store.state.createdInvite;
    // DELETE the api call below.
  
    // DELETE ABOVE
  },
  methods: {
    copyUrl(uniqueId) {
      const idToCopy = "#" + uniqueId;
      var copyText = document.querySelector(idToCopy);
      copyText.select();
      document.execCommand("copy");
    },
  },
};
</script>

<style scoped>

.image{
  width:250px;
  height:250px;
  object-fit:cover;
  margin-right:50px;
}

.choices{
  margin-left:45px;
  display:flex;
  flex-wrap:wrap;
 

 
  

}
.main{
  margin-top:50px;
}

h2, span{
  color:#1e1e32;
}
h3{
color:#8faf44;
}

</style>