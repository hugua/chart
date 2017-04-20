package com.chen;

/**
 * Created by chen on 2017/4/19.
 */
public class Test {
    public static void main(String[] args) {
        String tmp = "A ART N";
        int index = tmp.indexOf(" ");
        String tmp1= tmp.substring(0,index);
        String tmp2 = tmp.substring(index +1 );
        System.out.println(tmp1);
        System.out.println(tmp2);
    }
}
