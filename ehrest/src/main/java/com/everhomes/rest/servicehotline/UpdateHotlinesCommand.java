package com.everhomes.rest.servicehotline;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * <ul> 
 *  
 * <li>hotlines: 热线列表{@link UpdateHotlineCommand}</li>   
 * </ul>
 */
public class UpdateHotlinesCommand {
	@ItemType(UpdateHotlineCommand.class)
	private List<UpdateHotlineCommand> hotlines;

	public List<UpdateHotlineCommand> getHotlines() {
		return hotlines;
	}

	public void setHotlines(List<UpdateHotlineCommand> hotlines) {
		this.hotlines = hotlines;
	}
}
