package com.postit.hwabooni.model;

import java.util.ArrayList;
import java.util.Date;

public class User {

    String name;
    String email;
    ArrayList<String> follower;
    ArrayList<String> requested;
    String phone;
    //Date birthdate;
    String sex;
    int age;
    String address;
    String fcm;

    public User() {    }

    public User(String name, String phone, int age, String email, String sex, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.sex = sex;
        this.address = address;
        //this.birthdate = birthdate;

        this.follower = new ArrayList<>();
        this.requested = new ArrayList<>();

    }

    public int getAge() {   return age;  }

    public void setAge(int age) {  this.age = age; }

    public String getAddress() { return address;  }

    public void setAddress(String address) { this.address = address;  }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getFollower() {
        return follower;
    }

    public void setFollower(ArrayList<String> follower) {
        this.follower = follower;
    }

    public ArrayList<String> getRequested() {
        return requested;
    }

    public void setRequested(ArrayList<String> requested) {
        this.requested = requested;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
