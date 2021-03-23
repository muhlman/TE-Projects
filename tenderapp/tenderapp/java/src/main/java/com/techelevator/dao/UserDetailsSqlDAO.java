package com.techelevator.dao;

import com.techelevator.model.User;
import com.techelevator.model.UserDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsSqlDAO implements UserDetailsDAO {
    private JdbcTemplate jdbcTemplate;

    public UserDetailsSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails getDetails(long userId) {
        String sql = "SELECT user_id, address, city, state, zip, default_radius " +
                "FROM user_details " +
                "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            UserDetails userDetails = new UserDetails();
            userDetails = mapRowToUserDetails(results);

            return userDetails;
        } else {
            return new UserDetails();
        }
    }

    @Override
    public UserDetails updateDetails(UserDetails uD) {
        String sql = "UPDATE user_details " +
                "SET address = ?, city = ?, state = ?, zip = ?, default_radius = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, uD.getAddress(), uD.getCity(),
                uD.getState(),uD.getZip(), uD.getDefault_radius(), uD.getUserId());

        uD = this.setActiveCategories(uD);

        return uD;
    }

    @Override
    public void create(long userId) {
        String sql = "INSERT INTO user_details (user_id) " +
                "VALUES (?) RETURNING user_id";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
    }

    @Override
    public List<Integer> getActiveCategories(UserDetails userDetails) {
        List<Integer> activeCategories = new ArrayList<>();
        String sql = "SELECT fc.category_id AS \"category_id\" FROM food_categories AS fc " +
                "JOIN user_categories ON fc.category_id = user_categories.category_id " +
                "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userDetails.getUserId());

        while (results.next()) {
            activeCategories.add(results.getInt("category_id"));
        }

        userDetails.setActiveCategoryId(activeCategories);

        return activeCategories;
    }

    @Override
    public UserDetails setActiveCategories(UserDetails userDetails) {
        List<Integer> activeCategories = new ArrayList<>();
        String removeSql = "DELETE FROM user_categories WHERE user_id = ?";
        jdbcTemplate.update(removeSql, userDetails.getUserId());

        String addSql = "INSERT INTO user_categories (user_id, category_id) VALUES (?,?)";
        for (int category : userDetails.getActiveCategoryId()) {
            jdbcTemplate.update(addSql,userDetails.getUserId(), category);
        }


        String getSql = "SELECT fc.category_id AS \"category_id\" FROM food_categories AS fc " +
                "JOIN user_categories ON fc.category_id = user_categories.category_id " +
                "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(getSql, userDetails.getUserId());

        while (results.next()) {
            activeCategories.add(results.getInt("category_id"));
        }

        userDetails.setActiveCategoryId(activeCategories);

        return userDetails;
    }


    private UserDetails mapRowToUserDetails(SqlRowSet results) {
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(results.getLong("user_id"));
        userDetails.setAddress(results.getString("address"));
        userDetails.setCity(results.getString("city"));
        userDetails.setState(results.getString("state"));
        userDetails.setZip(results.getInt("zip"));
        userDetails.setDefault_radius(results.getInt("default_radius"));

        userDetails.setActiveCategoryId(getActiveCategories(userDetails));

        long userId = results.getLong("user_id");

        userDetails.setSearchCategories(getUserDetailsSearchCategories(userId));

        return userDetails;

    }
    
    private String getUserDetailsSearchCategories(long userId) {
        List<String> searchCategories = new ArrayList<>();
        String searchCategoryString = "";
        int counter = 0;

        String sql = "SELECT display_name FROM food_categories " +
                "JOIN user_categories ON food_categories.category_id = user_categories.category_id " +
                "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()) {
            searchCategories.add(results.getString("display_name"));
        }

        if (searchCategories.size() > 0) {
            for(String searchItem : searchCategories) {
                searchCategoryString += searchItem;
                if (counter < searchCategories.size() - 1) {
                    searchCategoryString += ", ";
                    counter++;
                }
            }
        }

        return searchCategoryString;
    }


}
