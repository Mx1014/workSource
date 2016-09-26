package com.everhomes.util;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.jooq.Record;

public class RecordHelper {

	public static <T> T convert(Record record, Class<T> clazz){
		if (record == null) {
			return null;
		}
		
		try {
			T t = clazz.newInstance();
			Field[] thisFields = clazz.getDeclaredFields();
			Field[] superFields = clazz.getSuperclass().getDeclaredFields();
			Field[] fields = Arrays.copyOf(thisFields, thisFields.length + superFields.length);
			System.arraycopy(superFields, 0, fields, thisFields.length, superFields.length);  
			for (Field field : fields) {
				if (field.getModifiers() == 2) {
					String name = field.getName();
					Class<?> type = field.getType();
					field.setAccessible(true);
					field.set(t, getValue(record, name, type));
				}
			}
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}
	
	private static <T> T getValue(Record record, String fieldName, Class<T> clazz){
		String column = camelToUnderline(fieldName);
		try {
			return record.getValue(column,clazz);
		} catch (Exception e) {
			return null;
		}
	}
	
	private static String camelToUnderline(String param){  
	    if (param==null||"".equals(param.trim())){  
	    	return "";  
	    }  
	    int len=param.length();  
	    StringBuilder sb=new StringBuilder(len);  
	    for (int i = 0; i < len; i++) {  
	    	char c=param.charAt(i);  
	    	if (Character.isUpperCase(c)){  
	    		sb.append("_");  
	    		sb.append(Character.toLowerCase(c));  
	    	}else{  
	    		sb.append(c);  
	    	}  
	    }  
	    return sb.toString();  
   }  
}
