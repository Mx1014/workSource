package com.everhomes.util;

import org.springframework.util.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinHelper {

	/**
	 * 中文转拼音
	 * @param inputString
	 * @return
	 */
	public static String getPinYin(String inputString) {
	       if(inputString == null || inputString.isEmpty()) {
	           return "";
	       }
	       
	       HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	       format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	       format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	       format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

	       char[] input = inputString.trim().toCharArray();
	       StringBuffer output = new StringBuffer("");

	       try {
	           for (int i = 0; i < input.length; i++) {
	               if (Character.toString(input[i]).matches("[\u4E00-\u9FA5]+")) {
	                   String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
	                   output.append(temp[0]);
	                   output.append(" ");
	               } else
	                   output.append(Character.toString(input[i]));
	           }
	       } catch (BadHanyuPinyinOutputFormatCombination e) {
	           e.printStackTrace();
	       }
	       return output.toString();
	}
	
	/**
	 * 获取拼音的全部首字母
	 * @param pinyin
	 * @return
	 */
	public static String getFullCapitalInitial(String pinyin) {
	      if(StringUtils.isEmpty(pinyin)){
	    	  return pinyin;
	      }
	      
	      String[] pinyins = pinyin.split(" ");
	      
	      if(pinyins.length < 2){
	    	  return pinyin;
	      }
	      
	      String capitalInitial = "";
	      for (String string : pinyins) {
	    	  if(StringUtils.isEmpty(string) || StringUtils.isEmpty(string.trim())){
	    		  capitalInitial += string;
		      }else{
		    	String initial = string.toUpperCase();
		  		
		  		char[] input = initial.toCharArray();
		  		
		  		initial = String.valueOf(input[0]);
		  		
		  		if(!initial.matches("[A-Z]")){
		  			capitalInitial += string;
		  		}else{
		  			capitalInitial += initial;
		  		}
		      }
	      }
	      return capitalInitial;
	}
	
	
	
	/**
	 * 获取首个字母大写
	 * @param pinyin
	 * @return
	 */
	public static String getCapitalInitial(String pinyin){
		
//		pinyin = getPinYin(pinyin);
		
		if(StringUtils.isEmpty(pinyin)){
			return "~";
		}
		
		pinyin = pinyin.trim();
		
		if(StringUtils.isEmpty(pinyin)){
			return "~";
		}
		
		pinyin = pinyin.toUpperCase();
		
		char[] input = pinyin.toCharArray();
		
		pinyin = String.valueOf(input[0]);
		
		if(!pinyin.matches("[A-Z]")){
			pinyin = "~";
		}
		
		return pinyin;
	}
	
	public static void main(String[] args) {
		System.out.println(getPinYin("颜少凡").replaceAll(" ", ""));
		System.out.println(getFullCapitalInitial(getPinYin("颜少凡")));
	}

}
