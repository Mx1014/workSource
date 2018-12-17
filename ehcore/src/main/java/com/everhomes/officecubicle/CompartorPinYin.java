package com.everhomes.officecubicle;

import java.util.Comparator;

import com.everhomes.rest.officecubicle.admin.SpaceForAppDTO;

import net.sourceforge.pinyin4j.PinyinHelper;

public class CompartorPinYin implements Comparator<SpaceForAppDTO>{
	@Override    
    public int compare(SpaceForAppDTO o1, SpaceForAppDTO o2) {    
        return convertToPinYinString(o1.getSpaceName()).compareTo(convertToPinYinString(o2.getSpaceName()));    
    } 
	private String convertToPinYinString(String str){    
        
        StringBuilder sb=new StringBuilder();    
        String[] arr=null;  
        
        for(int i=0;i<str.length();i++){    
            arr=PinyinHelper.toHanyuPinyinStringArray(str.charAt(i));    
            if(arr!=null && arr.length>0){    
                for (String string : arr) {    
                    sb.append(string);    
                }    
            }    
        }    
            
        return sb.toString();    
    } 
}
