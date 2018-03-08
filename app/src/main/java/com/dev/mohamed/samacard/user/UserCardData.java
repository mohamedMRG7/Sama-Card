package com.dev.mohamed.samacard.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by moham on 3/6/2018.
 */

public class UserCardData implements Parcelable {

    private String userName;
    private String companyName;
    private String positionName;
    private String address;
    private String officeNumber;
    private String directCallNum;
    private String activity;
    private String aboutActivity;
    private String email;
    private String facebookAcount;
    private String webSite;
    private String whatsApp;
    private String country;
    private String governorate;
    private String cardType;


    public UserCardData(String userName, String companyName, String positionName,
                        String address, String officeNumber, String directCallNum, String activity,
                        String aboutActivity, String email, String facebookAcount,
                        String webSite, String whatsApp, String country, String governorate, String cardType) {

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
        this.cardType=cardType;
    }


    public UserCardData(String userName, String companyName, String address, String officeNumber
            , String activity, String country, String governorate, String cardType) {
        this.userName = userName;
        this.companyName = companyName;
        this.address = address;
        this.officeNumber = officeNumber;
        this.activity = activity;
        this.country = country;
        this.governorate = governorate;
        this.cardType = cardType;
    }

    public String getUserName() {
        return userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPositionName() {
        return positionName;
    }

    public String getAddress() {
        return address;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public String getDirectCallNum() {
        return directCallNum;
    }

    public String getActivity() {
        return activity;
    }

    public String getAboutActivity() {
        return aboutActivity;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebookAcount() {
        return facebookAcount;
    }

    public String getWebSite() {
        return webSite;
    }

    public String getWhatsApp() {
        return whatsApp;
    }

    public String getCountry() {
        return country;
    }

    public String getGovernorate() {
        return governorate;
    }

    public String getCardType() {
        return cardType;
    }

    public UserCardData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.companyName);
        dest.writeString(this.positionName);
        dest.writeString(this.address);
        dest.writeString(this.officeNumber);
        dest.writeString(this.directCallNum);
        dest.writeString(this.activity);
        dest.writeString(this.aboutActivity);
        dest.writeString(this.email);
        dest.writeString(this.facebookAcount);
        dest.writeString(this.webSite);
        dest.writeString(this.whatsApp);
        dest.writeString(this.country);
        dest.writeString(this.governorate);
        dest.writeString(this.cardType);
    }

    protected UserCardData(Parcel in) {
        this.userName = in.readString();
        this.companyName = in.readString();
        this.positionName = in.readString();
        this.address = in.readString();
        this.officeNumber = in.readString();
        this.directCallNum = in.readString();
        this.activity = in.readString();
        this.aboutActivity = in.readString();
        this.email = in.readString();
        this.facebookAcount = in.readString();
        this.webSite = in.readString();
        this.whatsApp = in.readString();
        this.country = in.readString();
        this.governorate = in.readString();
        this.cardType = in.readString();
    }

    public static final Creator<UserCardData> CREATOR = new Creator<UserCardData>() {
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
