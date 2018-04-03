package com.everhomes.test.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;

public class GsonHelper {
	/**
	 * 把JSON字符串格式化为可读性强的方式
	 * @param uglyJSONString
	 * @return
	 */
	public static String formatJsonString(String uglyJSONString) {
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        return gson.toJson(je);
	}
	
	public static void writeTextFile(String dstFilePath, String str) {
	    BufferedWriter writer = null;
	    
	    try {
	        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dstFilePath), "utf8"));
	        writer.write(str);
	    } catch (Exception e) {
	        throw new IllegalStateException("Failed to write file, filePath=" + dstFilePath, e);
	    } finally {
	        if(writer != null) {
	            try {
	                writer.close();
	            } catch(Exception e) {
	                // Do nothing
	            }
	        }
	    }
	}
    
    public static String readerTextFile(String filePath) {
        BufferedReader reader = null;
        
        StringBuilder strBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf8"));
            String line = null;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                if(line.length() > 0) {
                    strBuilder.append(line);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read file, filePath=" + filePath, e);
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch(Exception e) {
                    // Do nothing
                }
            }
        }
        
        return strBuilder.toString();
    }
}
