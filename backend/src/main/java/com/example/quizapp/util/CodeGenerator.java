package com.example.quizapp.util;

import java.security.SecureRandom;

public class CodeGenerator {
    private static final String ALPH="ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom R = new SecureRandom();
    public static String code(int len){
        StringBuilder sb=new StringBuilder(len);
        for(int i=0;i<len;i++) sb.append(ALPH.charAt(R.nextInt(ALPH.length())));
        return sb.toString();
    }
}
