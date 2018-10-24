package com.everhomes.yellowPage.faq;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>infos : 字符串list 分别代表开工单 聊天路由 常见问题 kefu电话</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月24日
 */
public class GetSquareCardInfosResponse {
	
	private List<String> infos;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public List<String> getInfos() {
		return infos;
	}
	public void setInfos(List<String> infos) {
		this.infos = infos;
	}
}
