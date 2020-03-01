package com.yuyin.conf;

import java.util.HashMap;
import java.util.Map;

public class ConfigEntry {
    //设置APPID/AK/SK，这三个参数是需要我们去百度AI平台申请的（也就是上面说的那三个字符串）
    public static final String APP_ID = "18569917";
    public static final String API_KEY = "a9GBWG1wNdf8LAnUpXFuP3MQ";
    public static final String SECRET_KEY = "lQ1DmZ8ZOxDsXRza2spR8gUSehrg2Tf6";
    public static final String FILE_TYPE = ".txt";
    public static final String OUT_TYPE = ".MP3";
    public static final String FILE_OUT_PATH = "D:\\input\\output\\";

    public static int JIEGOUHUA_COUNT = 0;
    public static int ENGLISH_COUNT = 0;
    public static Map<String, String> HELP_TIP = new HashMap();
}
