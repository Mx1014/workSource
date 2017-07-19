// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>hotlineFlag : 热线 在app端否显示，参考 {@link com.everhomes.rest.express.ExpressShowType}</li>
 * <li>nextPageAnchor : 下一页锚点</li>
 * <li>hotlineDTO : 热线列表，参考 {@link com.everhomes.rest.express.ExpressHotlineDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class ListExpressHotlinesResponse {
	private Byte hotlineFlag;
	private Long nextPageAnchor;
	
	@ItemType(ExpressHotlineDTO.class)
	private List<ExpressHotlineDTO> hotlineDTO;
	
	public Byte getHotlineFlag() {
		return hotlineFlag;
	}

	public void setHotlineFlag(Byte hotlineFlag) {
		this.hotlineFlag = hotlineFlag;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ExpressHotlineDTO> getHotlineDTO() {
		return hotlineDTO;
	}

	public void setHotlineDTO(List<ExpressHotlineDTO> hotlineDTO) {
		this.hotlineDTO = hotlineDTO;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
