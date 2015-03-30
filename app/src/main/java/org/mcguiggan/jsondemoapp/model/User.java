package org.mcguiggan.jsondemoapp.model;

import java.util.List;

/**
 * A user from the social data.
 */
public class User {

    private String mId;
    private String mPictureUrl;
    private String mEmail;
    private String mPhoneNumber;
    private String mAbout;
    private List<Friend> mFriends;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getAbout() {
        return mAbout;
    }

    public void setAbout(String about) {
        mAbout = about;
    }

    public List<Friend> getFriends() {
        return mFriends;
    }

    public void setFriends(List<Friend> friends) {
        mFriends = friends;
    }
}
