package com.kaminski.gothan.util;

public class Base64 {

    public static String encodeBase64(String t){
        return android.util.Base64.encodeToString(t.getBytes(), android.util.Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodeBase64(String t){
        return new String(android.util.Base64.decode(t, android.util.Base64.DEFAULT));
    }
}
