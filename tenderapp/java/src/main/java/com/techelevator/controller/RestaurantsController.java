package com.techelevator.controller;

import com.techelevator.services.yelpfusion.YelpFusion;
import com.techelevator.services.yelpfusion.models.business.Businesses;
import com.techelevator.services.yelpfusion.models.business.BusinessDetails;
import com.techelevator.services.yelpfusion.models.review.Review;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping(value = "/restaurant")
public class RestaurantsController {

    private YelpFusion yelpFusion;

    public RestaurantsController(YelpFusion yelpFusion) {
        this.yelpFusion = yelpFusion;
    }
/**
    @RequestMapping(method = RequestMethod.GET)
    public Businesses[] getBusinessesByLocation(@RequestParam(defaultValue = "") String address,
                                                        @RequestParam(defaultValue = "16100") int radius) {
        return yelpFusion.getBusinessesByLocation(address, radius);

    }
**/

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BusinessDetails getBusinessById(@PathVariable String id) {

        return yelpFusion.getBusinessById(id);

    }


    @RequestMapping(value = "/{id}/reviews", method = RequestMethod.GET)
    public Review[] getReviewsByBusinessById(@PathVariable String id) {

        return yelpFusion.getReviewsByBusinessById(id);

    }
    /**
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public Businesses[] getBusinessesByCategory(@RequestParam(defaultValue = "") String address,
                                                        @RequestParam(defaultValue = "16100") int radius,
                                                        @RequestParam(defaultValue="restaurants") String category) {
        return yelpFusion.getBusinessesByCategory(address, radius, category);

    } **/

    @RequestMapping(method = RequestMethod.GET)
    public Businesses[] filterBusinessByCategory(@RequestParam(defaultValue = "") String address,
                                                @RequestParam(defaultValue = "16100") int radius,
                                                @RequestParam(defaultValue="") int[] categories) {
        return yelpFusion.filterBusinessByCategory(address, radius, categories);

    }
    

}
