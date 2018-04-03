package com.everhomes.hotTag;

import java.util.List;

import com.everhomes.rest.hotTag.TagDTO;

public interface HotTagProvider {
	
	List<TagDTO> listHotTag(Integer nameSpaceId, Byte moduleType, Long categoryId, String serviceType, Integer pageSize);
	List<TagDTO> listDistinctAllHotTag(String serviceType, Integer pageSize, Integer pageOffset);
	void updateHotTag(HotTag tag);

	void deleteHotTag(HotTag tag);

	void createHotTag(HotTag tag);
	
	HotTag findById(Long id);
	HotTag findByName(Integer nameSpaceId, Byte moduleType, Long categoryId, String serviceType, String name);

}
