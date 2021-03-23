package com.techelevator.services.yelpfusion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.techelevator.model.invitation.InviteeVotes;
import com.techelevator.services.PropertiesReader;
import com.techelevator.services.yelpfusion.models.business.BusinessesWrapper;
import com.techelevator.services.yelpfusion.models.business.Businesses;
import com.techelevator.services.yelpfusion.models.business.BusinessDetails;
import com.techelevator.services.yelpfusion.models.review.Review;
import com.techelevator.services.yelpfusion.models.review.ReviewWrapper;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class YelpFusion {
    private static final String ACCESS_TOKEN =
            PropertiesReader.getProperty("yelpkey");

    private RestTemplate restTemplate = new RestTemplate();

    private JdbcTemplate jdbcTemplate;


    public YelpFusion(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;


    }

    public Businesses[] getBusinessesByLocation(String address, int radius) {
        BusinessesWrapper resultList = null;
        if (radius == 0) {
            radius = 5000;
        }

        String endpointURL = "https://api.yelp.com/v3/businesses/search?categories=restaurants&location=\""
                + address + "\"&radius=" + radius;

        resultList = restTemplate.exchange(endpointURL, HttpMethod.GET, makeAuthEntity(), BusinessesWrapper.class).getBody();

        return resultList.getBusinesses();

    }

    public BusinessDetails getBusinessById(String Id) {
        String endpointURL = "https://api.yelp.com/v3/businesses/" + Id;

        BusinessDetails business = restTemplate.exchange(endpointURL, HttpMethod.GET, makeAuthEntity(), BusinessDetails.class).getBody();
        business.setReviews(this.getReviewsByBusinessById(business.getId()));

        return business;

    }
    public List<BusinessDetails> getBusinessDetailsById(List<String> yelpId, String uniqueId) {
        List<BusinessDetails> businessDetails= new ArrayList<>();
    	for(String id : yelpId) {
    	String endpointURL = "https://api.yelp.com/v3/businesses/" + id;
    	BusinessDetails details = new BusinessDetails();
    	details = restTemplate.exchange(endpointURL, HttpMethod.GET, makeAuthEntity(), BusinessDetails.class).getBody();
    	details.setInviteeVotes(this.getVotesByInvitee(id, uniqueId));
    	businessDetails.add(details);
    	}

    	return businessDetails;
    }

    public List<BusinessDetails> getBusinessDetailsForInvite(List<String> yelpId) {
        List<BusinessDetails> businessDetails= new ArrayList<>();
        for(String id : yelpId) {
            String endpointURL = "https://api.yelp.com/v3/businesses/" + id;
            BusinessDetails details = new BusinessDetails();
            details = restTemplate.exchange(endpointURL, HttpMethod.GET, makeAuthEntity(), BusinessDetails.class).getBody();
            businessDetails.add(details);
        }

        return businessDetails;
    }

    public Review[] getReviewsByBusinessById(String Id) {
        ReviewWrapper reviews;
        String endpointURL = "https://api.yelp.com/v3/businesses/" + Id + "/reviews";

        reviews = restTemplate.exchange(endpointURL, HttpMethod.GET, makeAuthEntity(), ReviewWrapper.class).getBody();

        return reviews.getReviews();

    }

    public Businesses[] getBusinessesByCategory(String address, int radius, String category) {
        BusinessesWrapper resultList = null;
        if (radius == 0) {
            radius = 5000;
        }

        String endpointURL = "https://api.yelp.com/v3/businesses/search?categories=" + category + "&location=\""
                + address + "\"&radius=" + radius;

        resultList = restTemplate.exchange(endpointURL, HttpMethod.GET, makeAuthEntity(), BusinessesWrapper.class).getBody();

        return resultList.getBusinesses();

    }

    public Businesses[] filterBusinessByCategory(String address, int radius, int[] categoryList) {
        BusinessesWrapper resultList = null;
        String yelpSearchCategory = "";
        if (categoryList.length > 0) {
            yelpSearchCategory = this.getYelpCategories(categoryList);
        }

        String endpointURL = "https://api.yelp.com/v3/businesses/search?categories=restaurants&location=\""
                + address + "\"&radius=" + radius;

        if (!yelpSearchCategory.isEmpty()) {
            endpointURL = "https://api.yelp.com/v3/businesses/search?categories=" + yelpSearchCategory + "&location=\""
                    + address + "\"&radius=" + radius;
        }

        resultList = restTemplate.exchange(endpointURL, HttpMethod.GET, makeAuthEntity(), BusinessesWrapper.class).getBody();

        return resultList.getBusinesses();

    }


    private String getYelpCategories(int[] categoryList) {

        int listCounter = 0;
        String searchCategories = "";
        if (categoryList.length > 0) {
            for (int category : categoryList) {
                searchCategories += String.valueOf(category);
                if (listCounter < categoryList.length - 1) {
                    searchCategories += ",";
                }
                listCounter++;
            }
        }

        String yelpSearchCategory = getYelpFriendlyCategories(searchCategories);

        return yelpSearchCategory;

    }

    private String getYelpFriendlyCategories(String searchCategories){
        String yelpSearchCategory = "";


        if (!searchCategories.isBlank()) {
            List<String> yelpSearchCategories = new ArrayList<>();
            int counter = 0;
            SqlRowSet results = jdbcTemplate.queryForRowSet
                    ("SELECT search_name from food_categories WHERE category_id IN (" + searchCategories + ")");
            while (results.next()) {
                yelpSearchCategories.add(results.getString("search_name"));
            }
            if (yelpSearchCategories.size() > 0) {
                for (String searchItem : yelpSearchCategories) {
                    yelpSearchCategory += searchItem;
                    if (counter < yelpSearchCategories.size() - 1) {
                        yelpSearchCategory += ",";
                    }
                    counter++;
                }
            }
        }

        return yelpSearchCategory;

    }

    private InviteeVotes getVotesByInvitee(String yelpId, String uniqueID) {
        InviteeVotes inviteeVote = new InviteeVotes();

        String sql ="SELECT invite_id, invitee_unique_id, yelp_id, thumbs_up, thumbs_down FROM invitee_vote WHERE invitee_unique_id = ? AND yelp_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, uniqueID, yelpId);
        while (results.next()) {
            inviteeVote.setYelpId(results.getString("yelp_id"));
            inviteeVote.setInviteId(results.getInt("invite_id"));
            inviteeVote.setUniqueId(results.getString("invitee_unique_id"));
            inviteeVote.setThumbs_up(results.getBoolean("thumbs_up"));
            inviteeVote.setThumbs_down(results.getBoolean("thumbs_down"));
        }

        return inviteeVote;

    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ACCESS_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
