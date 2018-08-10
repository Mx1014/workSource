package com.everhomes.parking.chean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {

    public static String sendPost(String urlstring, String paramstring) {
        String result = "";
        HttpURLConnection connection = null;
        try {

            URL url = new URL(urlstring);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            // connection.setConnectTimeout(1000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.getOutputStream().write(paramstring.getBytes("UTF-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            int code = connection.getResponseCode();
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String tempLine = reader.readLine();
            StringBuilder buider = new StringBuilder();
            while (tempLine != null) {
                buider.append(tempLine);
                tempLine = reader.readLine();
            }
            result = buider.toString();
            reader.close();
            in.close();
        } catch (MalformedURLException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return result;
    }
}
