package com.cartshare.RequestBody;

import java.util.*;

public class ProductRequest {

    private String userId;
    private ArrayList<String> storeIDs;
    private String productSKU;
    private String productName;
    private String productImage;
    private String productDescription;
    private String productBrand;
    private String productUnit;
    private String productPrice;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getStoreIDs() {
        return this.storeIDs;
    }

    public void setStoreIDs(ArrayList<String> storeIDs) {
        this.storeIDs = storeIDs;
    }

    public String getProductSKU() {
        return this.productSKU;
    }

    public void setProductSKU(String productSKU) {
        this.productSKU = productSKU;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return this.productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return this.productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductBrand() {
        return this.productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductUnit() {
        return this.productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductPrice() {
        return this.productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

}