package com.techelevator.dao.invitation;

import com.techelevator.services.yelpfusion.models.business.FoodCategories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategorySqlDAO implements CategoryDAO {

    private JdbcTemplate jdbcTemplate;

    public CategorySqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate =jdbcTemplate;
    }

    @Override
    public List<FoodCategories> getFilteredCategory() {
        String sql = "SELECT category_id, display_name, search_name FROM food_categories";
        List<FoodCategories> foodCategoriesList = new ArrayList<>();

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            FoodCategories foodCategories = new FoodCategories();
            foodCategories.setCategoryId(results.getInt("category_id"));
            foodCategories.setDisplayName(results.getString("display_name"));
            foodCategories.setSearchName(results.getString("search_name"));
            foodCategoriesList.add(foodCategories);
        }
        return foodCategoriesList;
    }
}
