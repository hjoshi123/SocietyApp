package com.hacks.societyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Items {
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("rate")
    @Expose
    private Double rate;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("item_description")
    @Expose
    private String itemDescription;
    @SerializedName("gst_rate")
    @Expose
    private Double gstRate;
    @SerializedName("measurement_unit")
    @Expose
    private String measurementUnit;
    @SerializedName("max_allowed")
    @Expose
    private Integer maxAllowed;
    @SerializedName("item_code")
    @Expose
    private String itemCode;
    @SerializedName("quantity")
    @Expose
    private Double quantity;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Double getGstRate() {
        return gstRate;
    }

    public void setGstRate(Double gstRate) {
        this.gstRate = gstRate;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public Integer getMaxAllowed() {
        return maxAllowed;
    }

    public void setMaxAllowed(Integer maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
