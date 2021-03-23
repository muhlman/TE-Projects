package com.techelevator.controller;

import com.techelevator.dao.UserDetailsDAO;
import com.techelevator.dao.invitation.CategoryDAO;
import com.techelevator.model.UserDetails;
import com.techelevator.services.yelpfusion.models.business.FoodCategories;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryDAO categoryDAO;

    public CategoryController(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<FoodCategories> getListOfCategories() {
        return categoryDAO.getFilteredCategory();
    }

}
