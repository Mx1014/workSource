package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>videoConfAccounts: videoConfAccounts信息，参考{@link com.everhomes.rest.videoconf.VideoConfAccountDTO}</li>
 * </ul>
 */
public class ListVideoConfAccountResponse {

	private Long nextPageAnchor;
	
	@ItemType(VideoConfAccountDTO.class)
    private List<VideoConfAccountDTO> videoConfAccounts;
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<VideoConfAccountDTO> getVideoConfAccounts() {
		return videoConfAccounts;
	}

	public void setVideoConfAccounts(List<VideoConfAccountDTO> videoConfAccounts) {
		this.videoConfAccounts = videoConfAccounts;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
