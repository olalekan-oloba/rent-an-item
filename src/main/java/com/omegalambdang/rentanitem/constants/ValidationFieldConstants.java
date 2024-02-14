package com.omegalambdang.rentanitem.constants;

public class ValidationFieldConstants {
    public static final int NAMES_COL_SIZE = 100;
    public static final int EMAIL_COL_SIZE = 255;
    public static final int PHONE_COL_SIZE = 100;
    public static final int ACCOUNT_NAME_COL_SIZE=255;
    public static final int CONTACT_ADDRESS_COL_SIZE = 255;
    public static final int ACCOUNT_NO_COL_SIZE=10;
    public static final int BANK_CODE_SIZE=3;

    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final String DIGIT_REGEX="^\\d+$";

    public static final String PASSWORD_REGEX="^(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0–9\\s])(?=.*\\d).{8,}$";

}
