package com.everhomes.rest.goods;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>p1 : p1</li>
 * <li>currentPMId : 管理公司id</li>
 * <li>currentProjectId : 当前项目id</li>
 * <li>appId : 应用的originId</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月7日
 */
public class FindGoodCommand extends GetGoodListCommand{
	
	private String tag1;
	private String tag2;
	private String tag3;
	private String tag4;
	private String tag5;
	private String goodTag;
	
	public String getTag1() {
		return tag1;
	}

	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}

	public String getTag2() {
		return tag2;
	}

	public void setTag2(String tag2) {
		this.tag2 = tag2;
	}

	public String getTag3() {
		return tag3;
	}

	public void setTag3(String tag3) {
		this.tag3 = tag3;
	}

	public String getTag4() {
		return tag4;
	}

	public void setTag4(String tag4) {
		this.tag4 = tag4;
	}

	public String getTag5() {
		return tag5;
	}

	public void setTag5(String tag5) {
		this.tag5 = tag5;
	}

	public String getGoodTag() {
		return goodTag;
	}

	public void setGoodTag(String goodTag) {
		this.goodTag = goodTag;
	}
}
