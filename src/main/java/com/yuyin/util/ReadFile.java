package com.yuyin.util;

import com.yuyin.conf.ConfigEntry;
import com.yuyin.conf.QuestionInfo;

import java.io.*;
import java.util.*;

public class ReadFile {

    private final static String INPUT_FILE_PATH = "D://input//input" + ConfigEntry.FILE_TYPE;
    private static String readFile() {
        InputStream fileInput = null;
        BufferedReader reader = null;
        int len = 0;
        StringBuffer str = new StringBuffer("");
        try {
            System.out.println(INPUT_FILE_PATH);
            fileInput =  new FileInputStream(new File(INPUT_FILE_PATH));
            reader = new BufferedReader(new InputStreamReader(fileInput, "UTF-8"));

            String line = null;
            while ((line = reader.readLine()) != null) {
                if (len !=0) {
                    str.append("\r\n"+line);
                } else {
                    str.append(line);
                }
                len ++;
            }
        } catch (IOException e) {
            System.out.println("read file faile");
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str.toString();
    }

    private static Map<String, List<QuestionInfo>> changeStrToMap(String input) {
        Map<String, List<QuestionInfo>> map = new HashMap();
        List<QuestionInfo> jiegouhuas = new ArrayList<>();
        List<QuestionInfo> yingyus = new ArrayList<>();

        System.out.println(input);
        String[] alls = input.split( "\\n\\r\\n" );
        for (String s : alls) {
            System.out.println(s);
            System.out.println("---------");
        }
        String[] jiegouhua_next = alls[0].split(System.lineSeparator());
        ConfigEntry.JIEGOUHUA_COUNT = jiegouhua_next.length;
        for (String jiegouhua : jiegouhua_next) {
            String[] jg = jiegouhua.split(">>");
            jiegouhuas.add(new QuestionInfo(jg[0], jg[1]));
        }
        String[] yingyu_pre = alls[1].split(System.lineSeparator());
        ConfigEntry.ENGLISH_COUNT = yingyu_pre.length;
        for (String yingyu : yingyu_pre) {
            String[] yy = yingyu.split(">>");
           yingyus.add(new QuestionInfo(yy[0], yy[1]));
        }
        map.put("next", jiegouhuas);
        map.put("pre", yingyus);
        return map;
    }

    public static Map<String, List<QuestionInfo>> readFileToMap() {
        return changeStrToMap(readFile());
    }
}
