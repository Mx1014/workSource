package com.everhomes.hotTag;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.db.DbProvider;
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

	@Autowired
	private DbProvider dbProvider;

	@Override
	public List<TagDTO> listHotTag(ListHotTagCommand cmd) {
		
		if(cmd.getNamespaceId() == null){
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}


		List<TagDTO> tags = hotTagProvider.listHotTag(cmd.getNamespaceId(), cmd.getModuleType(), cmd.getCategoryId(), cmd.getServiceType(), cmd.getPageSize());

		//查询没有标签，则使用0域空间、默认类型，入口为null的数据，即以前默认的数据
		if(tags == null || tags.size() == 0){
			tags = hotTagProvider.listHotTag(0, null,null, cmd.getServiceType(), cmd.getPageSize());
			tags = tags.stream().filter(r -> r.getCategoryId() == null).collect(Collectors.toList());
		}

		return tags;
	}
	@Override
	public ListAllHotTagResponse listAllHotTag(ListAllHotTagCommand cmd) {
		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		Integer pageOffset = cmd.getPageOffset() == null ? 1: cmd.getPageOffset();

		List<TagDTO> tags = hotTagProvider.listDistinctAllHotTag(cmd.getServiceType(), pageSize + 1, pageOffset );

		ListAllHotTagResponse response = new ListAllHotTagResponse();

		if(tags != null && tags.size() > pageSize){
			tags.remove(pageSize);
			response.setNextPageOffset(pageOffset + 1);
		}

		response.setTags(tags);

		return response;
	}

	@Override
	public TagDTO setHotTag(SetHotTagCommand cmd) {
		User user = UserContext.current().getUser();

		if(cmd.getNamespaceId() == null){
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		HotTag tag = hotTagProvider.findByName(cmd.getNamespaceId(), cmd.getModuleType(), cmd.getCategoryId(), cmd.getServiceType(), cmd.getName());
		if(tag != null) {
			tag.setStatus(HotTagStatus.ACTIVE.getCode());
			hotTagProvider.updateHotTag(tag);
			tag.setHotFlag(HotTagStatus.ACTIVE.getCode());
			hotTagSearcher.feedDoc(tag);
		} else {
			tag = ConvertHelper.convert(cmd, HotTag.class);
			tag.setCreateUid(user.getId());
			hotTagProvider.createHotTag(tag);
			
			tag.setHotFlag(HotTagStatus.ACTIVE.getCode());
			hotTagSearcher.feedDoc(tag);
		}
		
		TagDTO dto = ConvertHelper.convert(tag, TagDTO.class);
		return dto;
	}

	@Override
	public void resetHotTag(ResetHotTagCommand cmd) {

		if(cmd.getNamespaceId() == null){
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		dbProvider.execute((status) -> {
			List<TagDTO> oldHotTag = hotTagProvider.listHotTag(cmd.getNamespaceId(), cmd.getModuleType(), cmd.getCategoryId(), cmd.getServiceType(), 1000);
			if (oldHotTag != null) {
				oldHotTag.forEach(r -> {
					//别更新了，又不是用户数据，直接删了多好  add by yanjun 201711241506
					//淡淡的忧伤，provider返回的是dto
					HotTag tag = hotTagProvider.findById(r.getId());
					hotTagProvider.deleteHotTag(tag);

					tag.setHotFlag(HotTagStatus.INACTIVE.getCode());
					hotTagSearcher.feedDoc(tag);
				});
			}

			if (cmd.getNames() != null && cmd.getNames().size() > 0) {
				cmd.getNames().forEach(r -> {
					SetHotTagCommand setCmd = new SetHotTagCommand();
					setCmd.setNamespaceId(cmd.getNamespaceId());
					setCmd.setServiceType(cmd.getServiceType());
					setCmd.setCategoryId(cmd.getCategoryId());
					setCmd.setName(r);
					this.setHotTag(setCmd);

					//如果0域空间(标签库)没有加一条数据，加数据的时候在其他入口加，0域空间、categoryId为null是默认标签
					HotTag tag = hotTagProvider.findByName(0, null,null, cmd.getServiceType(), r);
					if(tag == null){
						setCmd.setNamespaceId(0);
						setCmd.setCategoryId(0L);
						this.setHotTag(setCmd);
					}
				});
			}
			return null;
		});

	}

	@Override
	public void deleteHotTag(DeleteHotTagCommand cmd) {
//		User user = UserContext.current().getUser();
		
		HotTag tag = hotTagProvider.findById(cmd.getId());
//		tag.setStatus(HotTagStatus.INACTIVE.getCode());
//		tag.setDeleteUid(user.getId());
//		tag.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		hotTagProvider.updateHotTag(tag);

		//别更新了，又不是用户数据直接删了多好  add by yanjun 201711241506
		hotTagProvider.deleteHotTag(tag);

		tag.setHotFlag(HotTagStatus.INACTIVE.getCode());
		hotTagSearcher.feedDoc(tag);
	}

	@Override
	public void deleteHotTagByName(DeleteHotTagByNameCommand cmd) {
//		User user = UserContext.current().getUser();

		if(cmd.getNamespaceId() == null){
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		HotTag tag = hotTagProvider.findByName(cmd.getNamespaceId(), cmd.getModuleType(), cmd.getCategoryId(), cmd.getServiceType(), cmd.getName());
		if(tag == null){
			LOGGER.error("the tag which name = "+cmd.getName()+" and serviceType = "+cmd.getServiceType()+" not found!");
			return;
		}
//		tag.setStatus(HotTagStatus.INACTIVE.getCode());
//		tag.setDeleteUid(user.getId());
//		tag.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		hotTagProvider.updateHotTag(tag);

		//别更新了，又不是用户数据直接删了多好  add by yanjun 201711241506
		hotTagProvider.deleteHotTag(tag);

		tag.setHotFlag(HotTagStatus.INACTIVE.getCode());
		hotTagSearcher.feedDoc(tag);
	}

}
