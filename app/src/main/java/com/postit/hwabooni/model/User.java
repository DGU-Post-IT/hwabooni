package com.postit.hwabooni.model;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;

public class User {

    @DocumentId
    String id;
    String name;
    ArrayList<String> friend;

    public User() {
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

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }
}
