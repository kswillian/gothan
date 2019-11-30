package com.kaminski.gothan.util;

public class Validation {

    public static boolean validateName(String name){
        return name.matches("^[a-zA-ZáÁéÉíÍóÓúÚçÇãÃõÕ ]{2,80}$");
    }

    public static boolean validateEmail(String email){
        return email.matches("^([0-9a-zA-Z]+([_.-]?[0-9a-zA-Z]+)*@[0-9a-zA-Z]+[0-9,a-z,A-Z,.,-]*(.){1}[a-zA-Z]{2,4})+$");
    }
}
