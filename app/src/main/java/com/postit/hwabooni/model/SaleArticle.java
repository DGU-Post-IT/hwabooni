package com.postit.hwabooni.model;

import com.google.firebase.firestore.DocumentId;

public class SaleArticle {
    @DocumentId
    String id;
    String plantName;
    int price;
    String picture;

    public SaleArticle() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
