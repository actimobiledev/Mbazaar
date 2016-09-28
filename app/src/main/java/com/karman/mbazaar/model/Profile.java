package com.karman.mbazaar.model;

/**
 * Created by Admin on 15-09-2016.
 */
public class Profile {
    int user_id;
    String first_name, last_name, profile_image, mobile, email;

    public Profile (int user_id, String first_name, String last_name, String profile_image, String mobile, String email) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_image = profile_image;
        this.mobile = mobile;
        this.email = email;
    }


    public int getUser_id () {
        return user_id;
    }

    public void setUser_id (int user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name () {
        return first_name;
    }

    public void setFirst_name (String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name () {
        return last_name;
    }

    public void setLast_name (String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_image () {
        return profile_image;
    }

    public void setProfile_image (String profile_image) {
        this.profile_image = profile_image;
    }

    public String getMobile () {
        return mobile;
    }

    public void setMobile (String mobile) {
        this.mobile = mobile;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }
}
