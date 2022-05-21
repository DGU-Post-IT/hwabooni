package com.postit.hwabooni.model;

import com.google.firebase.firestore.DocumentId;

public class PlantTempData {

    @DocumentId
    String id;
    Double temperature;

    PlantTempData(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }


}
