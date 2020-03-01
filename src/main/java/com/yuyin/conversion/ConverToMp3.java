package com.yuyin.conversion;

import com.yuyin.conf.ConfigEntry;

import java.io.*;
import java.util.*;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.yuyin.conf.QuestionInfo;
import com.yuyin.util.ReadFile;
import org.json.JSONObject;

public class ConverToMp3 {
    private static Map<String, List<QuestionInfo>> FILEMAP = new HashMap();
    private static ConverToMp3 converToMp3 = null;
    private ConverToMp3() {
        FILEMAP = ReadFile.readFileToMap();
    }

    public static ConverToMp3 getInstance() {
        if (converToMp3 == null) {
            converToMp3 = new ConverToMp3();
        }
        return converToMp3;
    }

    public void cover() {
        Iterator<Map.Entry<String, List<QuestionInfo>>> iterator = FILEMAP.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<QuestionInfo>> entry = iterator.next();
            String flag = entry.getKey();
            List<QuestionInfo> files = entry.getValue();
            String kaitou = "";
            if ("next".equalsIgnoreCase(flag)) {
                kaitou = "考生请听题， ";
            } else {
                kaitou = "Candidates listen to the questions， ";
            }

            for (int i = 0; i < files.size(); i++) {
                String fileNamePath = ConfigEntry.FILE_OUT_PATH + flag + "//" + flag + (i+1) + ConfigEntry.OUT_TYPE;
                ConfigEntry.HELP_TIP.put(fileNamePath, files.get(i).getHelpTip());
                convertMP3(i,kaitou + files.get(i).getTiMu(),fileNamePath);
            }
        }
    }

    private static void convertMP3(int menOrWo, String str, String writeFile) {
        AipSpeech client = new AipSpeech(ConfigEntry.APP_ID, ConfigEntry.API_KEY, ConfigEntry.SECRET_KEY);
        // 可选：设置网络连接参数，就是超时时间
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        int per = 0;
        switch (menOrWo % 4) {
            case 1 :
                per = 1;
                break;
            case 2 :
                per = 3;
                break;
            case 3 :
                per = 4;
                break;
        }
        // 设置一些可选参数
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", 4);//语速，取值0-9，默认为5中语速      非必选
        options.put("pit", "9");//音调，取值0-9，默认为5中语调      非必选
        options.put("per", per);//发音人选择, 0为女声，1为男声，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女 非必选

        //百度AI开始读取传入的str字符串
        TtsResponse res = client.synthesis(str, "zh", 1, options);

        //服务器返回的内容，合成成功时为null,失败时包含error_no等信息
        JSONObject result = res.getResult();
        if (result != null) {
            System.out.printf("error：" + result.toString()+"----------");
            return;
        }
        //生成的音频数据
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                //将生成的音频输出到指定位置
                Util.writeBytesToFileSystem(data, writeFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            System.out.println(res1.toString());
        }
    }
}
