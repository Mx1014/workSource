package com.everhomes.hotTag;

import java.util.List;

import com.everhomes.rest.hotTag.DeleteHotTagCommand;
import com.everhomes.rest.hotTag.ListHotTagCommand;
import com.everhomes.rest.hotTag.SetHotTagCommand;
import com.everhomes.rest.hotTag.TagDTO;

public interface HotTagService {
	
	List<TagDTO> listHotTag(ListHotTagCommand cmd);
	
	TagDTO setHotTag(SetHotTagCommand cmd);
	
	void deleteHotTag(DeleteHotTagCommand cmd);

}
