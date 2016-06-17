package com.everhomes.generate;

import java.util.List;
import java.util.Map;

import com.everhomes.controller.ControllerBase;
import com.everhomes.news.NewsController;

/**
 * 自动生成接口文档
 * 1. 取得所有的接口
 * 2. 取得接口对应的Command和Response（反射）
 * 3. 取得Command和Response对应的注释（读文件）
 * 4. 取得接口上的注释 （读文件）
 * 5. 可以指定Controller
 * 6. 输出为Html
 */
public class GenerateDocs {
	public static void main(String[] args) throws Exception {
		String className = "com.everhomes.news.NewsController";
		go(className);
	}
	
	public static void go(String className) throws Exception{
		Class<?> clazz = Class.forName(className);
		if (clazz.getSuperclass() != ControllerBase.class) {
			System.err.println("传入的类不是 ControllerBase 的子类！");
			return;
		}
		
		List<String> methods = getAllMethodNames(clazz);
	}

	private static List<String> getAllMethodNames(Class<?> clazz) {
		
		return null;
	}
}


class InterfaceModel{
	String url;
	String desc;
	List<ParamModel> paramList;
	List<ParamModel> returnList;
	
	@Override
	public String toString() {
		String returnString;
		return "<b>"+url+"\t"+desc+"\n\t参数\n"+MyUtils.outputParams(paramList)+"\n\t返回值\n"+(MyUtils.isEmpty(returnString = MyUtils.outputParams(returnList))?"OK":returnString);
	}
}

class ParamModel{
	String name;
	String desc;
	List<ParamModel> subList;
	
	public String toString() {
		return "<li>"+name+": "+desc+"</li>"+MyUtils.outputParams(subList);
	}
}

class MyUtils{
	public static boolean isNotEmpty(Object object) {
		return !isEmpty(object);
	}

	public static boolean isEmpty(Object object) {
		if (object == null) {
			return true;
		}
		if (object instanceof String) {
			return ((String) object).length() == 0;
		}
		if (object instanceof List<?>) {
			return ((List<?>) object).size() == 0;
		}
		if (object instanceof Map<?, ?>) {
			return ((Map<?, ?>) object).keySet().isEmpty();
		}

		return false;
	}
	
	public static String outputParams(List<ParamModel> list){
		if (MyUtils.isNotEmpty(list)) {
			StringBuilder sb = new StringBuilder("<ul>");
			list.forEach(s->sb.append(s.toString()));
			return sb.append("</ul>").toString();
		}
		return "";
	}
}