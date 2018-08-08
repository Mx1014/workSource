package com.everhomes.rest.servicehotline;

import java.util.List;
import com.everhomes.discover.ItemType;

/**
 * <ul>
 * 返回值
 * <li>nextPageAnchor: 分页的锚点，下一页开始取数据的位置</li>
 * <li>chatRecordList: 聊天记录列表，参考{@link com.everhomes.rest.servicehotline.ChatRecordDTO}</li>
 * </ul>
 */
public class GetChatRecordListResponse {
	
	private Long nextPageAnchor;
	@ItemType(ChatRecordDTO.class)
	private List<ChatRecordDTO> chatRecordList ;
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<ChatRecordDTO> getChatRecordList() {
		return chatRecordList;
	}
	public void setChatRecordList(List<ChatRecordDTO> chatRecordList) {
		this.chatRecordList = chatRecordList;
	}

	
}

