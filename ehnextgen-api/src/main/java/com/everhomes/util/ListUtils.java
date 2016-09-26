package com.everhomes.util;

import java.util.List;

public class ListUtils {
	public static <T> boolean isEmpty(List<T> list){
		if (list == null || list.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static <T> boolean isNotEmpty(List<T> list){
		return !isEmpty(list);
	}
}
