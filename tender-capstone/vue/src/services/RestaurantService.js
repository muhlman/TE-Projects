import axios from 'axios';




export default {
    list() {
        return axios.get('/restaurant');
    },
    getRestaurants(location,radius,categories) {
        return axios.get('/restaurant/', {
            params: {
                address: location,
                radius: radius,
                categories: categories 
            }
        });
    },
    getTheRestaurant(id) {
        return axios.get(`/restaurant/${id}`);
    },
    getRestaurantByCategory(category) {
        return axios.get('/category', {
            params: {
                address: location,
                category: category
            }
        })
    },

    getAvailableCategories(){
        return axios.get('/categories');
    }
}