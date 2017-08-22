package com.everhomes.hotTag;

import java.util.List;

import com.everhomes.rest.hotTag.TagDTO;

public interface HotTagProvider {
	
	List<TagDTO> listHotTag(Integer nameSpaceId, String serviceType, Integer pageSize);
	List<TagDTO> listDistinctAllHotTag(String serviceType, Integer pageSize, Integer pageOffset);
	void updateHotTag(HotTags tag);
	void createHotTag(HotTags tag);
	
	HotTags findById(Long id);
	HotTags findByName(Integer nameSpaceId, String serviceType, String name);

}
