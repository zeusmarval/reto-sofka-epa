package com.zama.reto_sofka.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "Sales")
public class Sales {

    @Id
    private String id;

    @Field("saleDate")
    private Date saleDate;

    @Field("items")
    private List<ItemSales> items;

    @Field("totalValue")
    private double totalValue;

    @Field("storeLocation")
    private String storeLocation;

    @Field("customer")
    private Customer customer;

    @Field("couponUsed")
    private boolean couponUsed;

    @Field("purchaseMethod")
    private String purchaseMethod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public List<ItemSales> getItems() {
        return items;
    }

    public void setItems(List<ItemSales> items) {
        this.items = items;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isCouponUsed() {
        return couponUsed;
    }

    public void setCouponUsed(boolean couponUsed) {
        this.couponUsed = couponUsed;
    }

    public String getPurchaseMethod() {
        return purchaseMethod;
    }

    public void setPurchaseMethod(String purchaseMethod) {
        this.purchaseMethod = purchaseMethod;
    }
}
