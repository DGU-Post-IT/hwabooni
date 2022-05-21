package com.postit.hwabooni.model;

import android.widget.TextView;
import com.google.firebase.firestore.DocumentId;

public class PlantData {

    @DocumentId
    String id;
    String myPlantName;
    String myPlantPicture;
    String prettyWord;

    public PlantData() {

    }


    public String getPrettyWord() {
        return prettyWord;
    }

    public void setPrettyWord(String prettyWord) {
        this.prettyWord = prettyWord;
    }


    public String getMyPlantPicture() {
        return myPlantPicture;
    }

    public void setMyPlantPicture(String myPlantPicture) {
        this.myPlantPicture = myPlantPicture;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmyPlantName() {
        return myPlantName;
    }

    public void setmyPlantName(String myPlantName) {
        this.myPlantName = myPlantName;
    }
}
