package com.everhomes.hotTag;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.namespace.Namespace;
import com.everhomes.rest.hotTag.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.search.HotTagSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class HotTagServiceImpl implements HotTagService{
	private static final Logger LOGGER = LoggerFactory.getLogger(HotTagServiceImpl.class);
	
	@Autowired
	private HotTagProvider hotTagProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
	private HotTagSearcher hotTagSearcher;

	@Override
	public List<TagDTO> listHotTag(ListHotTagCommand cmd) {
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		cmd.setNamespaceId(namespaceId);

		List<TagDTO> tags = new ArrayList<TagDTO>();
		if(cmd.getNamespaceId() == 0){
			tags = hotTagProvider.listDistinctAllHotTag(cmd.getServiceType());
		}else {
			tags = hotTagProvider.listHotTag(cmd.getNamespaceId(), cmd.getServiceType(), pageSize);
		}

		return tags;
	}

	@Override
	public TagDTO setHotTag(SetHotTagCommand cmd) {
		User user = UserContext.current().getUser();

		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		cmd.setNamespaceId(namespaceId);

		HotTags tag = hotTagProvider.findByName(cmd.getNamespaceId(), cmd.getServiceType(), cmd.getName());
		if(tag != null) {
			if(tag.getStatus() == HotTagStatus.ACTIVE.getCode()) {
				LOGGER.error("the tag which name = "+cmd.getName()+" and serviceType = "+cmd.getServiceType()+" is already hot tag!");
				throw RuntimeErrorException
						.errorWith(
								HotTagServiceErrorCode.SCOPE,
								HotTagServiceErrorCode.ERROR_HOTTAG_EXIST,
								localeStringService.getLocalizedString(
										String.valueOf(HotTagServiceErrorCode.SCOPE),
										String.valueOf(HotTagServiceErrorCode.ERROR_HOTTAG_EXIST),
										UserContext.current().getUser().getLocale(),
										"the tag is already hot tag!"));
			} else {
				tag.setStatus(HotTagStatus.ACTIVE.getCode());
				tag.setCreateUid(user.getId());
				tag.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				tag.setDeleteUid(0L);
				tag.setDeleteTime(null);
				tag.setNamespaceId(cmd.getNamespaceId());
				hotTagProvider.updateHotTag(tag);
				
				tag.setHotFlag(HotTagStatus.ACTIVE.getCode());
				hotTagSearcher.feedDoc(tag);
			}
		}
		else {
			tag = new HotTags();
			tag.setNamespaceId(cmd.getNamespaceId());
			tag.setName(cmd.getName());
			tag.setServiceType(cmd.getServiceType());
			tag.setCreateUid(user.getId());
			hotTagProvider.createHotTag(tag);
			
			tag.setHotFlag(HotTagStatus.ACTIVE.getCode());
			hotTagSearcher.feedDoc(tag);
		}
		
		TagDTO dto = ConvertHelper.convert(tag, TagDTO.class);
		return dto;
	}

	@Override
	public void deleteHotTag(DeleteHotTagCommand cmd) {
		User user = UserContext.current().getUser();
		
		HotTags tag = hotTagProvider.findById(cmd.getId());
		tag.setStatus(HotTagStatus.INACTIVE.getCode());
		tag.setDeleteUid(user.getId());
		tag.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		hotTagProvider.updateHotTag(tag);
		
		tag.setHotFlag(HotTagStatus.INACTIVE.getCode());
		hotTagSearcher.feedDoc(tag);
	}

	@Override
	public void deleteHotTagByName(DeleteHotTagByNameCommand cmd) {
		User user = UserContext.current().getUser();

		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		cmd.setNamespaceId(namespaceId);

		HotTags tag = hotTagProvider.findByName(cmd.getNamespaceId(), cmd.getServiceType(), cmd.getName());
		tag.setStatus(HotTagStatus.INACTIVE.getCode());
		tag.setDeleteUid(user.getId());
		tag.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		hotTagProvider.updateHotTag(tag);

		tag.setHotFlag(HotTagStatus.INACTIVE.getCode());
		hotTagSearcher.feedDoc(tag);
	}

}
