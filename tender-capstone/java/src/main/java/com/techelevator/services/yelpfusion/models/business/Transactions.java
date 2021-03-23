package com.techelevator.services.yelpfusion.models.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transactions {
    @JsonProperty("transactions")
    private String type;
    @JsonProperty("label")
    private String label;

    public Transactions(String transactions) {

        this.type = transactions;
        this.label = createLabel(this.type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private String createLabel(String tag) {
        return Arrays.stream(tag.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .reduce("", (label, transaction) -> label += transaction + " ")
                .trim();

    }

    public Transactions(String type, String label) {
        this.type = type;
        this.label = label;
    }

    public Transactions() {
        this.label = createLabel(this.type);
    }
}
