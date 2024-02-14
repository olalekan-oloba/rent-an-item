package com.omegalambdang.rentanitem.feature.account;

import lombok.Getter;

public enum Gender {

    MALE (Constants.MALE_VALUE,"Male"),
    FEMALE(Constants.FEMALE_VALUE,"Female");

    @Getter
    private final String code;
    @Getter
    String display;

    Gender(String code, String display) {
        this.code=code;
        this.display = display;
    }
    public String getDisplay() {
        return display;
    }

    public static class Constants {
        public static final String MALE_VALUE = "MALE";
        public static final String FEMALE_VALUE = "FEMALE";

    }

}
