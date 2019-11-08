package com.im.util;

public class ImageTagUtil {
    /** 
     * 经纬度格式  转换
     * @param point 坐标点 
     * @return 
     */ 
    public static String pointToLatlong (String point ) {  
        Double du = Double.parseDouble(point.substring(0, point.indexOf("°")).trim());  
        Double fen = Double.parseDouble(point.substring(point.indexOf("°")+1, point.indexOf("'")).trim());  
        Double miao = Double.parseDouble(point.substring(point.indexOf("'")+1, point.indexOf("\"")).trim());  
        Double duStr = du + fen / 60 + miao / 60 / 60 ;  
        return duStr.toString();  
    } 



}
