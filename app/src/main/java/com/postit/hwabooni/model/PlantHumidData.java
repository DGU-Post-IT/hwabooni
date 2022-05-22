package com.postit.hwabooni.model;

import com.google.firebase.firestore.DocumentId;

public class PlantHumidData {

    @DocumentId
    String id;
    Double humidity;

    PlantHumidData(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }


}
