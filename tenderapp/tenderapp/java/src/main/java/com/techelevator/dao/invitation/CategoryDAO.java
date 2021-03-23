package com.techelevator.dao.invitation;

import com.techelevator.services.yelpfusion.models.business.FoodCategories;

import java.util.List;

public interface CategoryDAO {

    List<FoodCategories> getFilteredCategory();


}
