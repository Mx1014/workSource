package com.everhomes.contentserver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentMediaHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentMediaHelper.class);

    public static final String UPLOAD_IMAGE = "image";      
    public static final String UPLOAD_AUDEO = "audio";      
    public static final String UPLOAD_VIDEO = "video";      
    public static final String UPLOAD_FILE = "file";     
    public static final String UPLOAD_UNKNOW = "unknow";

    private static final Map<String, String> extMap;    
    static {        
        Map<String, String> aMap = new HashMap<String, String>();        
        aMap.put("jpeg", UPLOAD_IMAGE);        
        aMap.put("jpg", UPLOAD_IMAGE);        
        aMap.put("gif", UPLOAD_IMAGE);        
        aMap.put("webp", UPLOAD_IMAGE);        
        aMap.put("png", UPLOAD_IMAGE);        
        aMap.put("bmp", UPLOAD_IMAGE);        
        aMap.put("m4a", UPLOAD_AUDEO + "/m4a");        
        aMap.put("mp3", UPLOAD_AUDEO + "/mp3");        
        aMap.put("pdf", UPLOAD_FILE);        
        aMap.put("doc", UPLOAD_FILE);        
        aMap.put("docx", UPLOAD_FILE);        
        aMap.put("xls", UPLOAD_FILE);        
        aMap.put("xlsx", UPLOAD_FILE);        
        aMap.put("zip", UPLOAD_FILE);        
        extMap = Collections.unmodifiableMap(aMap);    
    }
    
    public static String getContentMediaType(String fileSuffix) {
        if(fileSuffix != null) {
            fileSuffix = fileSuffix.toLowerCase();
        }
        
        String mediaType = extMap.get(fileSuffix);
        if(mediaType == null) {
            mediaType = UPLOAD_UNKNOW;
        }
        
        return mediaType;
    }
}
