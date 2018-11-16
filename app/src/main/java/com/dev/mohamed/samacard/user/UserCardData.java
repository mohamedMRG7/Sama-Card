package com.dev.mohamed.samacard.user;

import android.os.Parcel;
import android.os.Parcelable;

public class UserCardData implements Parcelable {

    private String aboutActivity;
    private String activity;
    private String address;
    private String cardType;
    private String companyName;
    private String country;
    private String date;
    private String directCallNum;
    private String email;
    private String facebookAcount;
    private String governorate;
    private String isAccepted;
    private String offerImg;
    private String officeNumber;
    private String photoLink;
    private String positionName;
    private String userId;
    private String userName;
    private String webSite;
    private String whatsApp;


    public UserCardData() {
    }

    public UserCardData(String userId, String userName, String companyName, String positionName, String address, String officeNumber, String directCallNum, String activity, String aboutActivity, String email, String facebookAcount, String webSite, String whatsApp, String country, String governorate, String photoLink, String date, String offerImg, String cardType, String isAccepted) {
        this.userId = userId;
        this.userName = userName;
        this.companyName = companyName;
        this.positionName = positionName;
        this.address = address;
        this.officeNumber = officeNumber;
        this.directCallNum = directCallNum;
        this.activity = activity;
        this.aboutActivity = aboutActivity;
        this.email = email;
        this.facebookAcount = facebookAcount;
        this.webSite = webSite;
        this.whatsApp = whatsApp;
        this.country = country;
        this.governorate = governorate;
        this.cardType = cardType;
        this.photoLink = photoLink;
        this.isAccepted = isAccepted;
        this.date = date;
        this.offerImg = offerImg;
    }

    public UserCardData(String userName, String companyName, String address, String officeNumber, String activity, String country, String governorate, String cardType, String photoLink, String date, String offerImg, String userId, String isAccepted) {
        this.userName = userName;
        this.companyName = companyName;
        this.address = address;
        this.officeNumber = officeNumber;
        this.activity = activity;
        this.country = country;
        this.governorate = governorate;
        this.cardType = cardType;
        this.userId = userId;
        this.photoLink = photoLink;
        this.isAccepted = isAccepted;
        this.date = date;
        this.offerImg = offerImg;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOfferImg() {
        return this.offerImg;
    }

    public void setOfferImg(String offerImg) {
        this.offerImg = offerImg;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsAccepted() {
        return this.isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFacebookAcount(String facebookAcount) {
        this.facebookAcount = facebookAcount;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getPhotoLink() {
        return this.photoLink;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public String getPositionName() {
        return this.positionName;
    }

    public String getAddress() {
        return this.address;
    }

    public String getOfficeNumber() {
        return this.officeNumber;
    }

    public String getDirectCallNum() {
        return this.directCallNum;
    }

    public String getActivity() {
        return this.activity;
    }

    public String getAboutActivity() {
        return this.aboutActivity;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFacebookAcount() {
        return this.facebookAcount;
    }

    public String getWebSite() {
        return this.webSite;
    }

    public String getWhatsApp() {
        return this.whatsApp;
    }

    public String getCountry() {
        return this.country;
    }

    public String getGovernorate() {
        return this.governorate;
    }

    public String getCardType() {
        return this.cardType;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.aboutActivity);
        dest.writeString(this.activity);
        dest.writeString(this.address);
        dest.writeString(this.cardType);
        dest.writeString(this.companyName);
        dest.writeString(this.country);
        dest.writeString(this.date);
        dest.writeString(this.directCallNum);
        dest.writeString(this.email);
        dest.writeString(this.facebookAcount);
        dest.writeString(this.governorate);
        dest.writeString(this.isAccepted);
        dest.writeString(this.offerImg);
        dest.writeString(this.officeNumber);
        dest.writeString(this.photoLink);
        dest.writeString(this.positionName);
        dest.writeString(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.webSite);
        dest.writeString(this.whatsApp);
    }

    protected UserCardData(Parcel in) {
        this.aboutActivity = in.readString();
        this.activity = in.readString();
        this.address = in.readString();
        this.cardType = in.readString();
        this.companyName = in.readString();
        this.country = in.readString();
        this.date = in.readString();
        this.directCallNum = in.readString();
        this.email = in.readString();
        this.facebookAcount = in.readString();
        this.governorate = in.readString();
        this.isAccepted = in.readString();
        this.offerImg = in.readString();
        this.officeNumber = in.readString();
        this.photoLink = in.readString();
        this.positionName = in.readString();
        this.userId = in.readString();
        this.userName = in.readString();
        this.webSite = in.readString();
        this.whatsApp = in.readString();
    }

    public static final Parcelable.Creator<UserCardData> CREATOR = new Parcelable.Creator<UserCardData>() {
        @Override
        public UserCardData createFromParcel(Parcel source) {
            return new UserCardData(source);
        }

        @Override
        public UserCardData[] newArray(int size) {
            return new UserCardData[size];
        }
    };
}
