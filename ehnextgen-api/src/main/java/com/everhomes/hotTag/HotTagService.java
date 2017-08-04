package com.everhomes.hotTag;

import java.util.List;

import com.everhomes.rest.hotTag.*;

public interface HotTagService {
	
	List<TagDTO> listHotTag(ListHotTagCommand cmd);
	
	TagDTO setHotTag(SetHotTagCommand cmd);
	
	void deleteHotTag(DeleteHotTagCommand cmd);

	void deleteHotTagByName(DeleteHotTagByNameCommand cmd);


}
