package com.postit.hwabooni.model;

import com.google.firebase.firestore.DocumentId;

public class PlantData {

    @DocumentId
    String id;
    String name;
    String picture;
    String prettyWord;

    public PlantData() {

    }

    public PlantData(String n, String p) {
        name = n;
        picture = p;
    }


    public String getPrettyWord() {
        return prettyWord;
    }

    public void setPrettyWord(String prettyWord) {
        this.prettyWord = prettyWord;
    }


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String myPlantName) {
        this.name = myPlantName;
    }
}
