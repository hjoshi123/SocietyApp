package com.hacks.societyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("num_items")
    @Expose
    private Integer numItems;
    @SerializedName("total_value")
    @Expose
    private Integer totalValue;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("order_date")
    @Expose
    private String orderDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNumItems() {
        return numItems;
    }

    public void setNumItems(Integer numItems) {
        this.numItems = numItems;
    }

    public Integer getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Integer totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public class Item {
        @SerializedName("rate")
        @Expose
        private Double rate;
        @SerializedName("gst_rate")
        @Expose
        private Double gstRate;
        @SerializedName("item_description")
        @Expose
        private String itemDescription;
        @SerializedName("item_code")
        @Expose
        private String itemCode;
        @SerializedName("quantity")
        @Expose
        private Double quantity;

        public Double getRate() {
            return rate;
        }

        public void setRate(Double rate) {
            this.rate = rate;
        }

        public Double getGstRate() {
            return gstRate;
        }

        public void setGstRate(Double gstRate) {
            this.gstRate = gstRate;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
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
}
