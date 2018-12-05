package com.everhomes.contract;

import java.util.ArrayList;
import java.util.List;

public class GetKeywordsUtil02 {
	
	public List<String> getKeywords(String text){
		
		List<String> results = new ArrayList<>();
		
		StringBuffer sb = new StringBuffer();
		char[] charArray = text.toCharArray();
		
		boolean beginFlag = false;
		
		for (int i = 0; i < charArray.length; i++) {
			if ('$'==charArray[i] && '{'==charArray[i+1]) {
				beginFlag = true;
			}
			
			if (beginFlag) {
				sb.append(charArray[i]);
			}
			
			if (beginFlag && '}'==charArray[i]) {
				beginFlag = false;
				results.add(sb.toString());
				sb.setLength(0);
			}
			
		}
		return results;
	}
	
	
	public String getKey(String text){
		
		StringBuffer sb = new StringBuffer();
		char[] charArray = text.toCharArray();
		
		boolean beginFlag = false;
		
		for (int i = 0; i < charArray.length; i++) {
			if ('@'==charArray[i] && '@'==charArray[i+1]) {
				beginFlag = true;
			}
			
			if (beginFlag && '@'!=charArray[i] && '#'!=charArray[i]) {
				sb.append(charArray[i]);
			}
			
			if (beginFlag && '#'==charArray[i] && '#'==charArray[i+1]) {
				beginFlag = false;
			}
			
		}
		return sb.toString();
	}
	

}
