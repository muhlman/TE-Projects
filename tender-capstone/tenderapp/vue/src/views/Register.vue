<template>
  <main class="main-section">
    <!-- Container -->
    <div id="register-container">
      <!-- Register Form -->
      <form class="form-register" @submit.prevent="register">
        <!-- Header -->
        <h1 class="h3 mb-3 font-weight-normal">Create Account</h1>

        <!-- Alert message for registration errors -->
        <div class="alert" role="alert" v-if="registrationErrors">
          {{ registrationErrorMsg }}
        </div>

        <!-- First Name -->
        <b-field label="First Name">
          <b-input v-model="user.firstName" required autofocus></b-input>
        </b-field>

        <b-field label="Last Name">
          <b-input v-model="user.lastName" required></b-input>
        </b-field>

        <!-- Email Address -->
        <b-field label="Email Address">
          <b-input type="email" v-model="user.email">
            <i class="fas fa-envelope"></i>
          </b-input>
        </b-field>

        <!--Username Input -->
        <b-field label="Username">
          <b-input maxlength="15" v-model="user.username" required></b-input>
        </b-field>

        <!-- Password Input -->

        <b-field
          label="Password"
          message="Include at least: 8 characters, 1 uppercase letter, 1 lowercase letter, 1 number"
        >
          <b-input
            type="password"
            v-model="user.password"
            v-on:blur="checkPassword"
            password-reveal
            required
          >
          </b-input>
        </b-field>
        <!-- Password Validator -->

        <!-- Password Confirmation -->
        <b-field label="Confirm Password">
          <b-input
            type="password"
            v-model="user.confirmPassword"
            password-reveal
            required
          >
          </b-input>
        </b-field>

        <!-- Have an account link, links back to login page -->
        <router-link :to="{ name: 'login' }"
          >Have an account? <br
        /></router-link>

        <!-- submit button - Create Account -->
        <button type="submit">CREATE MY ACCOUNT</button>
      </form>
    </div>
  </main>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "register",
  data() {
    return {
      user: {
        firstName: "",
        lastName: "",
        email: "",
        username: "",
        password: "",
        confirmPassword: "",
        role: "user",
      },
      password_length: 0,
      contains_eight_characters: false,
      contains_number: false,
      contains_uppercase: false,
      valid_password: false,
      registrationErrors: false,
      registrationErrorMsg: "There were problems registering this user.",
    };
  },
  methods: {
    checkPassword() {
      this.password_length = this.user.password.length;

      if (this.password_length > 7) {
        this.contains_eight_characters = true;
      } else {
        this.contains_eight_characters = false;
      }

      this.contains_number = /\d/.test(this.user.password);
      this.contains_uppercase = /[A-Z]/.test(this.user.password);

      if (
        this.contains_eight_characters === true &&
        this.contains_uppercase === true &&
        this.contains_number === true
      ) {
        this.valid_password = true;
      } else {
        this.valid_password = false;
      }
    },
    register() {
      if (this.valid_password === false) {
        this.registrationErrors = true;
        this.registrationErrorMsg =
          "Your password does not meet the requirements";
      }
      //Checks is passwords match
      else if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = "Password & Confirm Password do not match.";
      } else {
        // if registration comes back successful
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: "/login",
                query: { registration: "success" },
              });
            }
          })

          //catches error
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.data.message === "User Already Exists.") {
              this.registrationErrorMsg = "Username Already Exists. Try Again";
            } else if (response.status === 400) {
              this.registrationErrorMsg = "Bad Request: Validation Errors";
            }
          });
      }
    },

    //When is this used?
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = "There were problems registering this user.";
    },
  },
};
</script>

<style scoped>
.alert {
  color: red;
  padding-bottom: 10px;
  font-weight: 500;
}


button {
  background-color: #dc6b67;
  border: none;
  color: white;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 5px;
  width: 100%;
  margin-top: 30px;
}

h1 {
  text-align: center;
  font-weight: 700;
  font-size: 1.5em;
}
.main-section {
  background-image: url("../assets/tenderBackground.jpg");
  background-size:cover;
 
  display: flex;
  align-items: center;
  width: 100vw;
  height: 100vh;
  justify-content: center;

}
#register-container {
  width: 30%;
  align-self: center;
  background-color: white;
  padding-top: 30px;
  padding-bottom: 30px;
  display: flex;
  justify-content: center;
}
.form-register {
  width: 75%;
}
</style>
