package com.everhomes.organization.pm.pay;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class EDSGsonFilter implements ExclusionStrategy{

	private Set<String> skipSet=new HashSet<String>();
	
	private boolean isSkip=true;
	
	public EDSGsonFilter(boolean isSkip,Set<String> skips){
		skipSet=skips;
		this.isSkip=isSkip;
		if(!isSkip){
			skipSet.add("result");
			skipSet.add("body");
			skipSet.add("pageFinder.pageSize");
			skipSet.add("pageFinder.data");
			skipSet.add("pageFinder.rowCount");
			skipSet.add("pageFinder.pageCount");
			skipSet.add("pageFinder.hasPrevious");
			skipSet.add("pageFinder.pageNo");
			skipSet.add("pageFinder.hasNext");
		}
	}
	
	public EDSGsonFilter(boolean isSkip,String... skips){
		this.isSkip=isSkip;
		for(String skip:skips){
			skipSet.add(skip);
		}
		if(!isSkip){
			skipSet.add("result");
			skipSet.add("body");
			skipSet.add("pageFinder.pageSize");
			skipSet.add("pageFinder.data");
			skipSet.add("pageFinder.rowCount");
			skipSet.add("pageFinder.pageCount");
			skipSet.add("pageFinder.hasPrevious");
			skipSet.add("pageFinder.pageNo");
			skipSet.add("pageFinder.hasNext");
		}
	}
	
	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		String fname=this.getName(f);
		if(isSkip){
			return skipSet.contains(f.getName());
		}else{
			return !skipSet.contains(fname);
		}
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	/**
	 * 获取可以用来过滤匹配的属性名
	 * @param f
	 * @return
	 */
	private String getName(FieldAttributes f){
		String parentName = f.getDeclaringClass().getName();
		parentName = parentName.substring(parentName.lastIndexOf(".")+1);
		if("ResultVo".equals(parentName)){
			return f.getName();
		}else{
			return parentName.substring(0, 1).toLowerCase()+parentName.substring(1)+"."+f.getName();
		}
	}
}
