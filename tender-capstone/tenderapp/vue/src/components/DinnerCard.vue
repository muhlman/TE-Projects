 <template>
  <section>
    <main class="tile is-ancestor">
      <div class="tile is-parent ml-3 mr-3">
        <div class="box">
          <div class="tile is-child">
            <h1 class="title">
              Create My Invite
            </h1>
            <p class="is-size-5">
              Select your restaurant choices to put to a vote, add some guests
              and let's have a party!
            </p>
            <br />
          </div>
          <!--Occasion-->
          <span class="error" v-if="isError">{{ errorMsg }}</span>
          <h2 class="title is-size-5">What's The Occasion?</h2>
          <b-field>
            <b-input v-model="dinnerInvite.inviteName"></b-input>
          </b-field>

          <!--Date Inputs-->
          <div>
          <span>
            <h2 class="title is-size-5">Reservation Date</h2>
            <input class="mr-3"
              label=" Reservation Date"
              type="date"
              v-model="reservationDate"
            />
          </span>
          <input type="time" v-model="reservationTime" />
          </div>
          <br/>
<div>
          <h2 class="title is-size-5">Voting Deadline</h2>
          <input class="mr-3" id="deadline" type="date" v-model="deadlineDate" />
          <input
            class="date"
            step="900"
            type="time"
            v-model="deadlineTime"
          />
</div>
          <br />
          <!--add invitee fields-->
          <div class="tile is-child">
            <div>
              <b-field
                v-for="(invitee, i) in dinnerInvite.invitees"
                :key="i"
                class="invitee-fields"
                id="invitee"
                v-model="dinnerInvite.invitees"
                label="Who's invited?"
              >
                <button @click="remove(i)" class="delete" id="delete-invitee" />
                <b-input class="mr-2"
                  id="name"
                  type="text"
                  v-model="invitee.name"
                  placeholder="Enter friend's name:"
                />
                <b-input
                  id="email"
                  type="email"
                  v-model="invitee.email"
                  placeholder="Enter friend's email:"
                />
              </b-field>
              <!--Buttons-->
              <div class="tile is-child">
                <span class="add-guests">
                  <b-button rounded type="is-primary" expanded @click="add()">
                    Add Guests</b-button
                  >
                </span>
              </div>
              <br />
              <span class="create-invite">
                <b-button
                  rounded
                  type="is-primary"
                  expanded
                  v-on:click="createInvite"
                >
                  Create Invite</b-button
                >
              </span>
            </div>
          </div>
        </div>
      </div>
    </main>
  </section>
</template>

<script>
import InviteService from "../services/InviteService";

export default {
  name: "create-dinner",

  data() {
    return {
      showWeekNumber: false,
      enableSeconds: false,
      hourFormat: undefined, // Browser locale
      locale: undefined, // Browser locale
      reservationDate: "",
      reservationTime: "",
      deadlineDate: "",
      deadlineTime: "",
      location: "",
      radius: "",
      isError: "",
      errorMsg: "",
      restaurantChoices: [],
      dinnerInvite: {
        creatorId: "",
        inviteName: "",
        reservationDate: "",
        deadline: "",
        invitees: [],
      },
      createdInvite: {},
    };
  },

  methods: {
    createInvite() {
      //Formats reservation date
      this.dinnerInvite.reservationDate =
        this.reservationDate + " " + this.reservationTime;

      //Formats deadline date
      this.dinnerInvite.deadline = this.deadlineDate + " " + this.deadlineTime;

      //validation
      if (this.$store.state.invitation.restaurantChoices.length === 0) {
        this.errorMsg = "Don't forget to add your restaurants";
        this.isError = true;
      } else if (
        this.dinnerInvite.invitees.length === 0 ||
        Object.keys(this.dinnerInvite.invitees[0]).length === 0
      ) {
        this.errorMsg = "";
        this.errorMsg = "Don't forget to add your guests";
        this.isError = true;
      } else if (
        (new Date(this.dinnerInvite.reservationDate) ||
          new Date(this.dinnerInvite.deadline)) < new Date().getTime()
      ) {
        this.errorMsg = "";
        this.errorMsg = "Make sure to set your dates after today's date :)";
        this.isError = true;
      } else if (
        new Date(this.dinnerInvite.reservationDate) <
        new Date(this.dinnerInvite.deadline)
      ) {
        this.errorMsg = "";
        this.errorMsg =
          "Your voting deadline can't be later than your reservation";
        this.isError = true;
      } else {
        //Clears Eror
        this.errorMsg = "";
        this.isError = false;

        //sets up Invite

        this.dinnerInvite.creatorId = this.$store.state.user.id;
        this.$store.commit("CREATE_INVITATION", this.dinnerInvite);

        // Sends invite to API
        InviteService.sendInvite(this.$store.state.invitation).then(
          (response) => {
            this.createdInvite = response.data;
            this.$store.commit("SET_CREATED_INVITE", this.createdInvite);
            console.log(this.$store.state.createdInvite);
            this.$router.push("/confirmation");
          }
        );
      }
    },

    add() {
      this.dinnerInvite.invitees.push({});
    },

    remove(index) {
      this.dinnerInvite.invitees.splice(index, 1);
    },
  },
};
</script>

<style scoped>



.error{
  color:red;
  font-weight:bold;
}
/*
.delete{
  margin-top:.5em;
  margin-right:1em;
  
}







#name.input.is-danger{
  width: 100%;
  height: 40px;
  border-radius: 3px;
  padding-left: 0.5em;
  font-size: 1em;
  font-weight: 600;
 
}
.date{
  margin-top:.3em;
}
h1 {
  font-size: 20pt;
  font-weight: bold;
}
.input-fields {
  padding: 1.5em;
  display: flex;
  flex-direction: column;
}
.add-invitees {
  font-weight: bold;
}
.date {
  font-size: 15pt;
}
.deadline {
  font-size: 15pt;
}
.dinner {
  font-size: 15pt;
  
}



.input-fields {
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  width: 100vw;
  



.headingText p {
  margin-bottom:1em;
}*/
</style>