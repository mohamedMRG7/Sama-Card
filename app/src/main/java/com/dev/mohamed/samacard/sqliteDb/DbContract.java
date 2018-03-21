package com.dev.mohamed.samacard.sqliteDb;

import android.provider.BaseColumns;

/**
 * Created by mohamed on 3/19/18.
 */

public class DbContract {





    public static class CardDataEntry implements BaseColumns
    {

        public static final String TABLE_NAME="cards";


        public static final String USER_ID="userid";
        public static final String USER_NAME="userName";
        public static final String COMPANY_NAME="companyName";
        public static final String POSITION="position";
        public static final String ADDRESS="adress";
        public static final String OFFICIAL_NUMBER="officeNumber";
        public static final String DIRECT_NUMBER="directNumber";
        public static final String ACTIVITY="activity";
        public static final String ABOUT_ACTITIY="aboutActivity";
        public static final String EMAIL="email";
        public static final String FACEBOOK_PAGE="facebookPage";
        public static final String WEBSITE="webSite";
        public static final String WHATSAPP="whatsapp";
        public static final String COUNTRY="country";
        public static final String GOVERNORATE="governorate";
        public static final String CARDTYPE="cardType";
        public static final String PHOTO_LINK="photoLink";

    }
}
