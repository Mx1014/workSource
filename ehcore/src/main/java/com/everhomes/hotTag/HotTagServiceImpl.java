package com.everhomes.hotTag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.hotTag.DeleteHotTagCommand;
import com.everhomes.rest.hotTag.HotTagStatus;
import com.everhomes.rest.hotTag.ListHotTagCommand;
import com.everhomes.rest.hotTag.SetHotTagCommand;
import com.everhomes.rest.hotTag.TagDTO;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

@Component
public class HotTagServiceImpl implements HotTagService{
	
	@Autowired
	private HotTagProvider hotTagProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;

	@Override
	public List<TagDTO> listHotTag(ListHotTagCommand cmd) {
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		List<TagDTO> tags = hotTagProvider.listHotTag(cmd.getServiceType(), pageSize);
		return null;
	}

	@Override
	public TagDTO setHotTag(SetHotTagCommand cmd) {
		User user = UserContext.current().getUser();
		
		HotTags tag = new HotTags();
		tag.setName(cmd.getName());
		tag.setServiceType(cmd.getServiceType());
		tag.setCreateUid(user.getId());
		hotTagProvider.createHotTag(tag);
		
		TagDTO dto = ConvertHelper.convert(tag, TagDTO.class);
		return dto;
	}

	@Override
	public void deleteHotTag(DeleteHotTagCommand cmd) {
		User user = UserContext.current().getUser();
		
		HotTags tag = hotTagProvider.findById(cmd.getId());
		tag.setStatus(HotTagStatus.INACTIVE.getCode());
		tag.setDeleteUid(user.getId());
		hotTagProvider.updateHotTag(tag);
	}

}
