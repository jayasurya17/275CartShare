package com.cartshare.utils;

public class Alphanumeric {
    
    public boolean isAlphaNumeric(String s) {
        return s != null && s.matches("^[a-zA-Z0-9]*$");
    }

}