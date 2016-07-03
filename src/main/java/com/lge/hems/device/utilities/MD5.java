package com.lge.hems.device.utilities;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;

/**
 * Created by netsga on 2016. 6. 9..
 */
public class MD5 {
    /**
     * 문자열을 MD-5 방식으로 암호화
     * @param txt 암호화 하려하는 문자열
     * @return String
     * @throws Exception
     */
    public static String getEncMD5(String txt) throws Exception {

        StringBuffer sbuf = new StringBuffer();


        MessageDigest mDigest = MessageDigest.getInstance("MD5");
        mDigest.update(txt.getBytes());

        byte[] msgStr = mDigest.digest() ;

        for(int i=0; i < msgStr.length; i++){
            String tmpEncTxt = Integer.toHexString((int)msgStr[i] & 0x00ff) ;
            sbuf.append(tmpEncTxt) ;
        }
        return sbuf.toString() ;
    }
}
