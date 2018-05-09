package com.everhomes.rest.servicehotline;

import java.util.List;
import com.everhomes.discover.ItemType;

/**
 * <ul>
 * 返回值
 * <li>nextPageAnchor: 分页的锚点，下一页开始取数据的位置</li>
 * <li>chatGroupList: 会话列表，参考{@link com.everhomes.rest.asset.TargetDTO}</li>
 * </ul>
 */
public class GetChatGroupListResponse {
	
	private Long nextPageAnchor;
	@ItemType(ChatGroupDTO.class)
	private List<ChatGroupDTO> chatGroupList ;
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<ChatGroupDTO> getChatGroupList() {
		return chatGroupList;
	}
	public void setChatGroupList(List<ChatGroupDTO> chatGroupList) {
		this.chatGroupList = chatGroupList;
	}

	
}
