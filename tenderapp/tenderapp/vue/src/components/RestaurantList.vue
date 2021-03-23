 <template>
     <section class="middle">
      <div class="restaurant-list">
        <div class="loading-gif">
          <img src="../assets/loading.gif" />
        </div>

        <div  class="result-list">
          <h1 class="title">
            Here are the restaurants we found near 
          </h1>

            
          <restaurant-card
            v-for="restaurant in restaurants"
            v-bind:key="restaurant.id"
            v-bind:restaurant="restaurant"
            class="card" 
            />
    
       
    

          <!-- Restaurant List Body Ends -->

          <div class="right"></div>

      
        </div>
      </div>
    </section>
</template>
<script>

import RestaurantCard from "../components/RestaurantCard";

export default {
  name: "restaurant-list",
  components: { RestaurantCard },

  props: ["restaurants", "restaurant"],
  
  data() {
    return {
    
    };
  },
  computed: {},
  methods: {},
}
</script>
<style scoped>


.loading-gif {
  display: flex;
  justify-content: center;
  height: 80vh;
}

.loading-gif img {
  width: 400px;
  height: 400px;
  object-fit: contain;
}

.result-list {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.card {
  width: 60%;
  margin-top: 25px;
  margin-bottom: 25px;
  padding: 50px;
}
</style>