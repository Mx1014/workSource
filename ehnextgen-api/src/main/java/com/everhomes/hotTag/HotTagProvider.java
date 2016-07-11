package com.everhomes.hotTag;

import java.util.List;

import com.everhomes.rest.hotTag.TagDTO;

public interface HotTagProvider {
	
	List<TagDTO> listHotTag(String serviceType, Integer pageSize);
	void updateHotTag(HotTags tag);
	void createHotTag(HotTags tag);
	
	HotTags findById(Long id);
	HotTags findByName(String serviceType, String name);

}
