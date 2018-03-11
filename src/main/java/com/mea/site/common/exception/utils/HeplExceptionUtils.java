package com.mea.site.common.exception.utils;

/**
 * Created by lenovo on 2018/2/10.
 */
public class HeplExceptionUtils {

    public  static void throwsExcetion(String msg,Class clazz){
        new Exception(clazz.getName()+msg);
    }
}
