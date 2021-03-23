<template>
  <!-- Container -->
  <div>
    <!-- Login Form -->
    <form class="form-signin" v-on:submit.prevent="login">
      <!--Heading -->
      <h1 class="h3 mb-3 font-weight-normal">SIGN IN</h1>

      <!-- Danger Alert when credentials are invalid -->
      <div class="alert" role="alert" v-if="invalidCredentials">
        Invalid username and password
      </div>

      <!-- Success Alert when registration in successful -->
      <div
        class="alert alert-success"
        role="alert"
        v-if="this.$route.query.registration"
      >
        Thank you for registering, please sign in.
      </div>

      <!-- Username input -->
      <b-field label="Username">
        <b-input
          type="text"
          v-model="user.username"
          id="username"
          class="form-control"
          required
        ></b-input>
      </b-field>

      <!-- Password input -->
      <b-field label="Password">
        <b-input
          type="password"
          v-model="user.password"
          required
          password-reveal
        >
        </b-input>
      </b-field>

      <!-- Links the Need an account button to the registration page -->
      <router-link
        :to="{ name: 'register' }"
        id="need-account"
        v-on:click="register"
        >Need an account?</router-link
      >

      <!-- Submit button -->
      <button type="submit">SIGN IN</button>
    </form>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "loginForm",
  components: {},
  data() {
    return {
      isRegister: false,
      user: {
        username: "",
        password: "",
      },
      invalidCredentials: false,
    };
  },
  methods: {
    register() {
      this.isRegister = true;
    },
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status == 200) {
            // sets token and user info
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
           
            this.$router.push("/");
          }
        })
        .catch((error) => {
          const response = error.response;

          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
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
h1 {
  font-weight: 700;
  font-size: 1em;
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
#login-container {
  display: flex;
  justify-content: center;
  

}

.form-signin {
  display: flex;
  flex-direction: column;
}

#need-account {
  padding-top: 20px;
  padding-bottom: 20px;
}
</style>


