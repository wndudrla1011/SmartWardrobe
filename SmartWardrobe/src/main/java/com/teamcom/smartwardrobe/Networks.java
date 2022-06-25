package com.teamcom.smartwardrobe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by KMW on 2018-02-02.
 */

public class Networks {

    public static String httpConnect(String pUrl, HashMap<String, String> params) {
        URL url = null;
        String str = null;
        try {
            url = new URL(pUrl);
            StringBuilder output = new StringBuilder();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            StringBuffer sbParams = new StringBuffer();

            if (params == null || params.size() == 0)
                sbParams.append("");
                //보낼 데이터가 있으면 파라미터를 채운다.
            else {
                //파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성.
                boolean isAnd = false;

                //파라미터 키 와 값
                Set keys = params.keySet();

                for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                    String key = (String) iterator.next();
                    String value = (String) params.get(key);

                    if (isAnd)
                        sbParams.append("&");

                    sbParams.append(key).append("=").append(value);

                    //파라미터가 2개 이상이면 isAnd를 true를 바꾸고 다음 루프부터 &를 붙인다.
                    if (!isAnd)
                        if (params.size() >= 2)
                            isAnd = true;
                }
            }

            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

                String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링을 저장.
                OutputStream os = conn.getOutputStream();
                os.write(strParams.getBytes("UTF-8"));//출력 스트림에 출력.
                os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링된 모든 출력 바이트를 강제 실행.
                os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

                int resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while (true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }
                    str = output.toString();

                    reader.close();
                    conn.disconnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        str = str.replaceAll("\n","");
        return str;
    }
}
