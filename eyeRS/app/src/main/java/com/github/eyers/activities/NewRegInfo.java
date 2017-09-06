package com.github.eyers.activities;

/**
 * Created by Nathan Shava on 05-Sep-17.
 */

public class NewRegInfo {

    public static abstract class UserRegistrationInfo {

        //Table name
        public static final String TABLE_NAME = "user_reg";
        //Column names
        public static final String USER_NAME = "user_name";
        public static final String EMAIL_ADD = "user_email";
        public static final String USER_PIN = "user_pin";
        public static final String SECURITY_RESPONSE = "security_response";
    }
}