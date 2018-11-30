// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.archives.ArchivesService;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.comment.AttachmentDTO;
import com.everhomes.rest.comment.CommentDTO;
import com.everhomes.rest.comment.OwnerTokenDTO;
import com.everhomes.rest.comment.OwnerType;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.enterprisemoment.CheckAdminCommand;
import com.everhomes.rest.enterprisemoment.CheckAdminResponse;
import com.everhomes.rest.enterprisemoment.ContentType;
import com.everhomes.rest.enterprisemoment.CreateMomentCommand;
import com.everhomes.rest.enterprisemoment.DeleteMomentCommand;
import com.everhomes.rest.enterprisemoment.EditTagsCommand;
import com.everhomes.rest.enterprisemoment.FavouriteDTO;
import com.everhomes.rest.enterprisemoment.GetBannerCommand;
import com.everhomes.rest.enterprisemoment.GetBannerResponse;
import com.everhomes.rest.enterprisemoment.GetMomentDetailCommand;
import com.everhomes.rest.enterprisemoment.LikeMomentCommand;
import com.everhomes.rest.enterprisemoment.ListMomentFavouritesCommand;
import com.everhomes.rest.enterprisemoment.ListMomentFavouritesResponse;
import com.everhomes.rest.enterprisemoment.ListMomentMessagesCommand;
import com.everhomes.rest.enterprisemoment.ListMomentMessagesResponse;
import com.everhomes.rest.enterprisemoment.ListMomentsCommand;
import com.everhomes.rest.enterprisemoment.ListMomentsResponse;
import com.everhomes.rest.enterprisemoment.ListTagsCommand;
import com.everhomes.rest.enterprisemoment.ListTagsResponse;
import com.everhomes.rest.enterprisemoment.MomentDTO;
import com.everhomes.rest.enterprisemoment.MomentMessageDTO;
import com.everhomes.rest.enterprisemoment.MomentScopeDTO;
import com.everhomes.rest.enterprisemoment.MomentTagDTO;
import com.everhomes.rest.enterprisemoment.MommentAttachmentDTO;
import com.everhomes.rest.enterprisemoment.PrivilegeType;
import com.everhomes.rest.enterprisemoment.ScopeType;
import com.everhomes.rest.enterprisemoment.SelectorType;
import com.everhomes.rest.enterprisemoment.UnlikeMomentCommand;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentFavourites;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMoments;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.tachikoma.commons.util.bean.ConvertHelper;
import com.everhomes.techpark.punch.PunchServiceImpl;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EnterpriseMomentServiceImpl implements EnterpriseMomentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PunchServiceImpl.class);
    private static final String NEW_MESSAGE_COUNT_KEY = "ENTERPRISE_MOMENT_%s_%s";

    @Autowired
	private EnterpriseMomentAttachmentProvider enterpriseMomentAttachmentProvider;
	@Autowired
	private EnterpriseMomentProvider enterpriseMomentProvider;
	@Autowired
	private EnterpriseMomentCommentProvider enterpriseMomentCommentProvider;
	@Autowired
	private EnterpriseMomentFavouriteProvider enterpriseMomentFavouriteProvider;
	@Autowired
	private EnterpriseMomentMessageProvider enterpriseMomentMessageProvider;
	@Autowired
	private EnterpriseMomentScopeProvider enterpriseMomentScopeProvider;
	@Autowired
	private EnterpriseMomentTagProvider enterpriseMomentTagProvider;
    @Autowired
    EnterpriseMomentCommentAttachmentProvider commentAttachmentProvider;
	@Autowired
	private ArchivesService archivesService;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private ConfigurationProvider configProvider;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private CoordinationProvider coordinationProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    protected BigCollectionProvider bigCollectionProvider;
    @Autowired
    private UserService userService;
    @Autowired
    UserProvider userProvider;
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private EnterpriseMomentAccessRecordProvider accessRecordProvider;

    private RedisTemplate<String, String> getRedisTemplate(String key){
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate<String, String> redisTemplate = acc.getTemplate(stringRedisSerializer);
        return redisTemplate;
    }

    @Override
	public CheckAdminResponse checkAdmin(CheckAdminCommand cmd) {
		CheckAdminResponse response = new CheckAdminResponse();
		response.setIsAdmin(checkAdmin(cmd.getAppId(),UserContext.getCurrentNamespaceId(),cmd.getOrganizationId(),UserContext.currentUserId()) ? TrueOrFalseFlag.TRUE.getCode() : TrueOrFalseFlag.FALSE.getCode());
		return response;
	}

	private Boolean checkAdmin(Long appId, Integer currentNamespaceId, Long organizationId, Long userId) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		if (resolver.checkSuperAdmin(userId,organizationId)
				|| resolver.checkModuleAppAdmin(currentNamespaceId, organizationId, userId, appId)) {
			return true;
		}
		return false;

	}

	@Override
	public ListMomentsResponse listMoments(ListMomentsCommand cmd) {
		ListMomentsResponse response = new ListMomentsResponse();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor() == null ? Long.MAX_VALUE : cmd.getPageAnchor());

		if (cmd.getPageAnchor() == null){
			cmd.setPageAnchor(Long.MAX_VALUE -1);
			EnterpriseMomentAccessRecord accessRecord = new EnterpriseMomentAccessRecord();
			accessRecord.setLastVisitTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			accessRecord.setNamespaceId(UserContext.getCurrentNamespaceId());
			accessRecord.setOrganizationId(cmd.getOrganizationId());
			accessRecord.setUserId(UserContext.currentUserId());
			accessRecordProvider.insertOrUpdateRecord(accessRecord);
		}
		int pageSize = cmd.getPageSize() == null ? 10 : cmd.getPageSize();

		List<EnterpriseMoment> results = new ArrayList<>();
		Long userId = UserContext.currentUserId();
        Long organizationId = cmd.getOrganizationId();
        Long appId = cmd.getAppId();
        results = listUserEnterpriseMoments(namespaceId, organizationId, userId, appId, cmd.getTagId(), locator, pageSize);
        if (CollectionUtils.isEmpty(results) && enterpriseMomentProvider.countAllStatusEnterpriseMomentByOrganization(namespaceId, cmd.getOrganizationId()) == 0) {
			//如果是查全部，并且第一页 一个都没有查到 新建一个默认的
            results = getDefaultEnterpriseMoments(cmd, namespaceId, locator, pageSize, userId);
        }
        Long nextPageAnchor = null;
        if (results != null && results.size() > pageSize) {
            nextPageAnchor = results.get(results.size() - 1).getId();
            results.remove(results.size() - 1);
        }
		response.setNextPageAnchor(nextPageAnchor);
		response.setNewMessageCount(getUserNewMessageCount(cmd.getOrganizationId(), userId));
		response.setMoments(results.stream().map(this::processMomentDTO).filter(r -> r != null).collect(Collectors.toList()));
		return response;
	}
    /**
     * 范围:1.管理员查全公司的
     * 2.普通用户查看creatorUid是自己的,3查看scope范围包含自己所在公司路径或者包含自己的id的.
     */
    private List<EnterpriseMoment> listUserEnterpriseMoments(Integer namespaceId, Long organizationId, Long userId, Long appId, Long tagId, CrossShardListingLocator locator, int pageSize) {
        List<EnterpriseMoment> results;
        if (checkAdmin(appId, namespaceId, organizationId, userId)) {
			results = enterpriseMomentProvider.listEnterpriseMoments(namespaceId, organizationId, userId, tagId, locator, pageSize + 1);
		} else {
			Set<Long> orgIds = getUserPathOrganizationIds(organizationId, userId);
			results = enterpriseMomentProvider.listEnterpriseMoments(namespaceId, organizationId, userId, orgIds, tagId, locator, pageSize + 1);
		}
        return results;
    }

    private List<EnterpriseMoment> getDefaultEnterpriseMoments(ListMomentsCommand cmd, Integer namespaceId, CrossShardListingLocator locator, int pageSize, Long userId) {
        List<EnterpriseMoment> results;
        //锁住代码,先查询后创建(可能被别的线程先一步创建了)
        results = this.coordinationProvider.getNamedLock(CoordinationLocks.NEW_DEFAULT_ENTERPRISE_MOMENT.getCode() + cmd.getOrganizationId()).enter(() -> {
        	List<EnterpriseMoment> moments = new ArrayList<>();
            if (enterpriseMomentProvider.countAllStatusEnterpriseMomentByOrganization(namespaceId, cmd.getOrganizationId()) > 0) {
                return listUserEnterpriseMoments(namespaceId, cmd.getOrganizationId(), userId, cmd.getAppId(), cmd.getTagId(), locator, pageSize);
            }
            moments.add(createDefaultMoment(cmd.getOrganizationId()));
            return moments;
        }).first();
        return results;
    }

    private EnterpriseMoment createDefaultMoment(Long organizationId) {
        EnterpriseMoment moment = new EnterpriseMoment();
        moment.setCreatorName(localeStringService.getLocalizedString(EnterpriseMomentConstants.ENTERPRISE_DEFAULT_MOMENT,
                EnterpriseMomentConstants.ENTERPRISE_DEFAULT_MOMENT_CREATOR_NAME, UserContext.current().getUser().getLocale(), "系统小助手"));
        moment.setCreatorUid(User.SYSTEM_UID);
        moment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        moment.setContentType(ContentType.TEXT.getCode());
        moment.setContent(localeStringService.getLocalizedString(EnterpriseMomentConstants.ENTERPRISE_DEFAULT_MOMENT,
                EnterpriseMomentConstants.ENTERPRISE_DEFAULT_MOMENT_CONTEXT, UserContext.current().getUser().getLocale(),
                "同事圈是公司员工交流、记录公司成长点滴的地方。在这里可以发表心情、分享经验、寻求帮助，培养融洽、和谐的办公氛围，提升团队凝聚力。"));
        Organization org = organizationProvider.findOrganizationById(organizationId);
        moment.setLocation(org.getName());
        moment.setOrganizationId(organizationId);
        moment.setNamespaceId(UserContext.getCurrentNamespaceId());
        enterpriseMomentProvider.createEnterpriseMoment(moment);
        EnterpriseMomentScope scope = ConvertHelper.convert(moment, EnterpriseMomentScope.class);
        scope.setEnterpriseMomentId(moment.getId());
        scope.setSourceId(organizationId);
        scope.setSourceType(ScopeType.ORGANIZATION.getCode());
        enterpriseMomentScopeProvider.createEnterpriseMomentScope(scope);
        return moment;
	}

    private Integer getUserNewMessageCount(Long organizationId, Long userId) {
        String key = String.format(NEW_MESSAGE_COUNT_KEY, userId, organizationId);
        ValueOperations<String, String> operations = getRedisTemplate(key).opsForValue();
        String count = operations.get(key);
        if (count == null) {
            return 0;
        } else {
            return Integer.valueOf(count);
        }
    }

    private MomentDTO processMomentDTO(EnterpriseMoment enterpriseMoment) {
        return processMomentDTO(enterpriseMoment, true);
    }

    /**
     * @param shouldFetchComments 动态详情页面不需要返回评论列表，设置为false,避免不必要的查询
     */
    private MomentDTO processMomentDTO(EnterpriseMoment enterpriseMoment, boolean shouldFetchComments) {
        try {
            OwnerTokenDTO ownerTokenDTO = new OwnerTokenDTO();
            ownerTokenDTO.setId(enterpriseMoment.getId());
            ownerTokenDTO.setType(OwnerType.ENTERPRISE_MOMENT.getCode());
            MomentDTO dto = ConvertHelper.convert(enterpriseMoment, MomentDTO.class);
            dto.setOwnerToken(WebTokenGenerator.getInstance().toWebToken(ownerTokenDTO));
            dto.setCreateTime(enterpriseMoment.getCreateTime().getTime());
            dto.setCreatorAvatarUrl(userService.getUserAvatarUrl(enterpriseMoment.getCreatorUid()));
            dto.setEditAble(User.SYSTEM_UID == enterpriseMoment.getCreatorUid().longValue() ? TrueOrFalseFlag.FALSE.getCode() : TrueOrFalseFlag.TRUE.getCode());
            dto.setLikeFlag(enterpriseMomentFavouriteProvider.countFavourites(enterpriseMoment.getNamespaceId(), enterpriseMoment.getOrganizationId(), enterpriseMoment.getId(), UserContext.currentUserId()) > 0 ? TrueOrFalseFlag.TRUE.getCode() : TrueOrFalseFlag.FALSE.getCode());
            processMomentDTOScopes(enterpriseMoment, dto);
            processMomentDTOAttachments(enterpriseMoment, dto);
            if (shouldFetchComments) {
                processMomentDTOComments(enterpriseMoment, dto);
            }
            processMomentDTOFavorite(enterpriseMoment, dto);
            return dto;
        } catch (Exception e) {
            LOGGER.error("process Moment DTO error " ,e);
            return null;
        }

    }


    private void processMomentDTOFavorite(EnterpriseMoment enterpriseMoment, MomentDTO dto) {
        dto.setFavourites(new ArrayList<>());
        Integer pageSize = 3;
        List<EnterpriseMomentFavourite> favourites = enterpriseMomentFavouriteProvider.listEnterpriseMomentFavourite(enterpriseMoment.getNamespaceId(), enterpriseMoment.getOrganizationId(), enterpriseMoment.getId(), pageSize);
        if (CollectionUtils.isEmpty(favourites)) {
            return;
        }
        for (EnterpriseMomentFavourite favourite : favourites) {
            dto.getFavourites().add(processMomentFavoriteDTO(favourite));
        }
    }

    private FavouriteDTO processMomentFavoriteDTO(EnterpriseMomentFavourite favourite) {
        FavouriteDTO dto = ConvertHelper.convert(favourite, FavouriteDTO.class);
        dto.setAvatarUrl(userService.getUserAvatarUrl(favourite.getUserId()));
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(favourite.getDetailId());
        dto.setContactName(detail.getContactName());
        return dto;
    }

    private void processMomentDTOComments(EnterpriseMoment moment, MomentDTO dto) {
        List<EnterpriseMomentComment> comments = enterpriseMomentCommentProvider.listEnterpriseMomentCommentsAsc(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId(), null, 30);
        dto.setComments(buildCommentDTO(moment.getNamespaceId(), moment.getOrganizationId(), comments));
    }

    private void processMomentDTOAttachments(EnterpriseMoment enterpriseMoment, MomentDTO dto) {
        dto.setAttachments(new ArrayList<>());
        List<EnterpriseMomentAttachment> attachments = enterpriseMomentAttachmentProvider.listEnterpriseMomentAttachment(enterpriseMoment.getNamespaceId(), enterpriseMoment.getOrganizationId(), enterpriseMoment.getId());
        if (CollectionUtils.isNotEmpty(attachments)) {
            for (EnterpriseMomentAttachment attachment : attachments) {
                dto.getAttachments().add(processMomentAttachmentDTO(attachment));
            }
        }
    }

    private MommentAttachmentDTO processMomentAttachmentDTO(EnterpriseMomentAttachment attachment) {
        MommentAttachmentDTO dto = ConvertHelper.convert(attachment, MommentAttachmentDTO.class);
        dto.setContentUrl(contentServerService.parserUri(attachment.getContentUri()));
        return dto;
    }

    private void processMomentDTOScopes(EnterpriseMoment enterpriseMoment, MomentDTO dto) {
        dto.setScopes(new ArrayList<>());
        List<EnterpriseMomentScope> scopes = enterpriseMomentScopeProvider.listEnterpriseMomentScopeByMomentId(enterpriseMoment.getNamespaceId(), enterpriseMoment.getOrganizationId(), enterpriseMoment.getId());
        for (EnterpriseMomentScope scope : scopes) {
            MomentScopeDTO scopeDTO = processMomentScopeDTO(scope);
            dto.getScopes().add(scopeDTO);
        }
    }

    private MomentScopeDTO processMomentScopeDTO(EnterpriseMomentScope scope) {
        MomentScopeDTO scopeDTO = ConvertHelper.convert(scope, MomentScopeDTO.class);
        if (ScopeType.ORGANIZATION == ScopeType.fromCode(scope.getSourceType())) {
            Organization org = organizationProvider.findOrganizationById(scope.getSourceId());
            scopeDTO.setSourceName(org.getName());
        }else{
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(scope.getSourceId());
            scopeDTO.setSourceName(detail.getContactName());
        }
        return scopeDTO;
    }

    private Set<Long> getUserPathOrganizationIds(Long organizationId, Long userId) {
		OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(userId, organizationId);
		Set<Long> result = new HashSet<>();
		Map<Long, String> dptMap = archivesService.getEmployeeDepartment(detail.getId());
		if(dptMap != null){
			for(Long orgId : dptMap.keySet()){
				Organization org = organizationProvider.findOrganizationById(orgId);
				if (org.getParentId() == null)
					result.add(orgId);
				else {
					String[] idList = org.getPath().split("/");
					for(int i =0 ;i <idList.length ;i++) {
						if(StringUtils.isNotBlank(idList[i]))
							result.add(Long.valueOf(idList[i]));
					}
				}
			}
		}

		return result;
	}

    @Override
    public MomentDTO getMomentDetail(GetMomentDetailCommand cmd) {

        if (!checkUserMomentPrivilege(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), UserContext.currentUserId(),
                cmd.getAppId(), cmd.getMomentId(), PrivilegeType.VIEW)) {
        	throw RuntimeErrorException.errorWith(EnterpriseMomentConstants.ERROR_SCOPE, EnterpriseMomentConstants.ERROR_INVALID_PARAMETER, "没有权限");
        }
        EnterpriseMoment moment = enterpriseMomentProvider.findEnterpriseMomentById(cmd.getMomentId());

        return processMomentDTO(moment, false);
    }


    @Override
	public void deleteMoment(DeleteMomentCommand cmd) {
        if(!checkUserMomentPrivilege(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), UserContext.currentUserId(),
                cmd.getAppId(), cmd.getMomentId(),PrivilegeType.OPERATE)){
            throw RuntimeErrorException.errorWith(EnterpriseMomentConstants.ERROR_SCOPE, EnterpriseMomentConstants.ERROR_INVALID_PARAMETER, "没有权限");
        }
        enterpriseMomentProvider.deleteEnterpriseMoment(cmd.getMomentId());
	}

    private Boolean checkUserMomentPrivilege(Integer currentNamespaceId, Long organizationId, Long userId, Long appId, Long momentId, PrivilegeType privilegeType) {
        EnterpriseMoment moment = enterpriseMomentProvider.findEnterpriseMomentById(momentId);
        if(moment == null){
        	throw RuntimeErrorException.errorWith(EnterpriseMomentConstants.ERROR_SCOPE, EnterpriseMomentConstants.ERROR_NO_MOMENT, "动态已删除");
        }
        if(TrueOrFalseFlag.TRUE == TrueOrFalseFlag.fromCode(moment.getDeleteFlag())){
        	throw RuntimeErrorException.errorWith(EnterpriseMomentConstants.ERROR_SCOPE, EnterpriseMomentConstants.ERROR_MOMENT_DELETED, "动态已删除");
        }
        switch (privilegeType) {
            case VIEW:
                //检查是否在scope内,如果在就返回鉴权成功,如果不在就检查是否为可操作人
                List<EnterpriseMomentScope> scopes = enterpriseMomentScopeProvider.listEnterpriseMomentScopeByMomentId(moment.getNamespaceId(), moment.getOrganizationId(), momentId);
                EnterpriseMomentScope scope = findUserScope(scopes, moment.getOrganizationId(), userId);
                if(scope != null) {
                    return true;
                }
            case OPERATE:
                //管理员和创建人是可操作人
                if (checkAdmin(appId, currentNamespaceId, organizationId, userId)) {
                    return true;
                } else if (moment.getCreatorUid().equals(userId)) {
                    return true;
                }
            default:
                //鉴权失败
                    return false;

        }

    }

    private EnterpriseMomentScope findUserScope(List<EnterpriseMomentScope> scopes, Long organizationId, Long userId) {
        Set<Long> orgIds = getUserPathOrganizationIds(organizationId, userId);
        for (EnterpriseMomentScope scope : scopes) {
            if (ScopeType.ORGANIZATION == ScopeType.fromCode(scope.getSourceType())) {
                if (orgIds.contains(scope.getSourceId())) {
                    return scope;
                }
            }else{
                if (scope.getSourceId().equals(userId)) {
                    return scope;
                }
            }
        }
        return null;
    }


    @Override
	public ListTagsResponse listTags(ListTagsCommand cmd) {
	    List<MomentTagDTO> momentTagDTOs = new ArrayList<>();
		if(cmd.getTagOnlyFlag() == 0){
			momentTagDTOs.add(buildSelectorTypeToMomentTagDTO(SelectorType.ALL, EnterpriseMomentConstants.ALL));
			momentTagDTOs.add(buildSelectorTypeToMomentTagDTO(SelectorType.PUBLISH_BY_SELF, EnterpriseMomentConstants.PUBLISH_BY_SELF));
			momentTagDTOs.add(buildSelectorTypeToMomentTagDTO(SelectorType.FAVOURITE_BY_SELF, EnterpriseMomentConstants.FAVOURITE_BY_SELF));
			momentTagDTOs.add(buildSelectorTypeToMomentTagDTO(SelectorType.COMMENT_BY_SELF, EnterpriseMomentConstants.COMMENT_BY_SELF));
		}
		List<EnterpriseMomentTag> enterpriseMomentTags = enterpriseMomentTagProvider.listEnterpriseMomentTag(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId());
		enterpriseMomentTags = checkAndInitTags(enterpriseMomentTags, UserContext.getCurrentNamespaceId(), cmd.getOrganizationId());
		momentTagDTOs.addAll(enterpriseMomentTags.stream().map(tag -> ConvertHelper.convert(tag, MomentTagDTO.class)).collect(Collectors.toList()));
		ListTagsResponse listTagsResponse = new ListTagsResponse();
		listTagsResponse.setTags(momentTagDTOs);
		return listTagsResponse;
	}

	private List<EnterpriseMomentTag> checkAndInitTags(List<EnterpriseMomentTag> tags, Integer namespaceId, Long organizationId){
    	if(tags.size() > 0){
    		return tags;
	    }
		if(!enterpriseMomentTagProvider.isNeedInitTag(namespaceId, organizationId)){
			return tags;
		}
		Long creatorUid = 0L;
		String local = UserContext.current().getUser().getLocale();
		List<MomentTagDTO> addTags = Arrays.asList(
				buildMomentTagDTO(null, localeStringService.getLocalizedString(EnterpriseMomentConstants.INIT_TAG_SCOPE, EnterpriseMomentConstants.INIT_TAG0, local, "")),
				buildMomentTagDTO(null, localeStringService.getLocalizedString(EnterpriseMomentConstants.INIT_TAG_SCOPE, EnterpriseMomentConstants.INIT_TAG1, local, "")),
				buildMomentTagDTO(null, localeStringService.getLocalizedString(EnterpriseMomentConstants.INIT_TAG_SCOPE, EnterpriseMomentConstants.INIT_TAG2, local, "")),
				buildMomentTagDTO(null, localeStringService.getLocalizedString(EnterpriseMomentConstants.INIT_TAG_SCOPE, EnterpriseMomentConstants.INIT_TAG3, local, "")),
				buildMomentTagDTO(null, localeStringService.getLocalizedString(EnterpriseMomentConstants.INIT_TAG_SCOPE, EnterpriseMomentConstants.INIT_TAG4, local, "")),
				buildMomentTagDTO(null, localeStringService.getLocalizedString(EnterpriseMomentConstants.INIT_TAG_SCOPE, EnterpriseMomentConstants.INIT_TAG5, local, ""))
		);
		enterpriseMomentTagProvider.batchCreateEnterpriseMomentTag(namespaceId, creatorUid, organizationId, addTags);
		return enterpriseMomentTagProvider.listEnterpriseMomentTag(namespaceId, organizationId);
	}

	private MomentTagDTO buildMomentTagDTO(Long id, String name){
		MomentTagDTO momentTagDTO = new MomentTagDTO();
		momentTagDTO.setId(id);
		momentTagDTO.setName(name);
		return momentTagDTO;
	}

	@Override
	public ListTagsResponse editTags(EditTagsCommand cmd) {
		List<MomentTagDTO> updateTagDTOS = cmd.getUpdateTags() == null ? new ArrayList<>() : cmd.getUpdateTags();
		List<MomentTagDTO> deleteTagDTOS = cmd.getDeleteTags() == null ? new ArrayList<>() : cmd.getDeleteTags();
		List<MomentTagDTO> addTagDTOS = cmd.getAddTags() == null ? new ArrayList<>() : cmd.getAddTags();
		momentTagDTONameTrim(updateTagDTOS);
		momentTagDTONameTrim(addTagDTOS);
		//如果修改后标签名为空，则把它从修改列表移动到删除列表
		List<MomentTagDTO> updateNeed2Delete = updateTagDTOS.stream().filter(dto -> dto.getName().isEmpty()).collect(Collectors.toList());
		updateTagDTOS = new ArrayList<>(CollectionUtils.subtract(updateTagDTOS, updateNeed2Delete));
		deleteTagDTOS.addAll(updateNeed2Delete);
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Long organizationId = cmd.getOrganizationId();
		Long userId = UserContext.currentUserId();
        if (!checkAdmin(cmd.getAppId(), namespaceId, organizationId, userId)) {
            throw RuntimeErrorException.errorWith(EnterpriseMomentConstants.ERROR_SCOPE, EnterpriseMomentConstants.ERROR_PRIVILEGE, "没有权限");
        }
		//执行增删改,updateTagDTOSTemp、addTagDTOSTemp变量是为了满足lamda表达式需要的临时变量
		List<MomentTagDTO> updateTagDTOSTemp = updateTagDTOS;
        List<MomentTagDTO> addTagDTOSTemp = addTagDTOS;
		dbProvider.execute(transactionStatus -> {
			enterpriseMomentTagProvider.batchUpdateEnterpriseMomentTag(namespaceId, userId, organizationId, updateTagDTOSTemp);
			enterpriseMomentTagProvider.batchDeleteEnterpriseMomentTag(namespaceId, userId, organizationId, deleteTagDTOS);
			enterpriseMomentTagProvider.batchCreateEnterpriseMomentTag(namespaceId, userId, organizationId, addTagDTOSTemp);
			return null;
		});
		//返回最新标签结果
		ListTagsCommand listTagsCommand = new ListTagsCommand();
		listTagsCommand.setOrganizationId(organizationId);
		listTagsCommand.setTagOnlyFlag((byte)1);
		return listTags(listTagsCommand);
	}

	private void momentTagDTONameTrim(List<MomentTagDTO> momentTagDTOS){
		momentTagDTOS.stream().forEach(dto -> dto.setName(dto.getName().trim()));
	}

	private void throwParameterError(){
		throw RuntimeErrorException.errorWith(EnterpriseMomentConstants.ERROR_SCOPE, EnterpriseMomentConstants.ERROR_INVALID_PARAMETER, "接口参数异常");
	}

    private MomentTagDTO buildSelectorTypeToMomentTagDTO(SelectorType selectorType, int code) {
        MomentTagDTO momentTagDTO = new MomentTagDTO();
        momentTagDTO.setId(selectorType.getCode());
        String selectorText = localeStringService.getLocalizedString(EnterpriseMomentConstants.SELECTOR_TYPE_SCOP, String.valueOf(code), UserContext.current().getUser().getLocale(), "default selector");
        momentTagDTO.setName(selectorText);
        return momentTagDTO;
    }

    @Override
    public MomentDTO createMoment(CreateMomentCommand cmd) {
        checkCreateMomentCommand(cmd);
        EnterpriseMoment enterpriseMoment = ConvertHelper.convert(cmd, EnterpriseMoment.class);
        enterpriseMoment.setNamespaceId(UserContext.getCurrentNamespaceId());
        enterpriseMoment.setCreatorUid(UserContext.currentUserId());
        enterpriseMoment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(enterpriseMoment.getCreatorUid(), enterpriseMoment.getOrganizationId());
        enterpriseMoment.setCreatorName(detail == null ? "" : detail.getContactName());
        EnterpriseMomentTag tag = (cmd.getTagId() == null ? null : enterpriseMomentTagProvider.findEnterpriseMomentTagById(cmd.getTagId()));
        enterpriseMoment.setTagName(tag == null ? null : tag.getName());
        if (cmd.getLatitude() != null && cmd.getLongitude() != null) {
            enterpriseMoment.setGeohash(GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude()));
        }
        enterpriseMomentProvider.createEnterpriseMoment(enterpriseMoment);
        for (MomentScopeDTO scopeDTO : cmd.getScopes()) {
            if (ScopeType.ORGANIZATION == ScopeType.fromCode(scopeDTO.getSourceType())) {
                enterpriseMoment.setIntegralTag1(scopeDTO.getSourceId());
            }
            EnterpriseMomentScope scope = ConvertHelper.convert(scopeDTO, EnterpriseMomentScope.class);
            scope.setNamespaceId(enterpriseMoment.getNamespaceId());
            scope.setEnterpriseMomentId(enterpriseMoment.getId());
            scope.setCreateTime(enterpriseMoment.getCreateTime());
            scope.setCreatorUid(enterpriseMoment.getCreatorUid());
            enterpriseMomentScopeProvider.createEnterpriseMomentScope(scope);
        }
        if (CollectionUtils.isNotEmpty(cmd.getAttachments())) {
            enterpriseMoment.setStringTag1(cmd.getAttachments().get(0).getContentUri());
            for (MommentAttachmentDTO dto : cmd.getAttachments()) {
            	checkAttachmentDTO(dto);
                EnterpriseMomentAttachment attachment = ConvertHelper.convert(dto, EnterpriseMomentAttachment.class);
                attachment.setNamespaceId(enterpriseMoment.getNamespaceId());
                attachment.setEnterpriseMomentId(enterpriseMoment.getId());
                attachment.setCreateTime(enterpriseMoment.getCreateTime());
                attachment.setCreatorUid(enterpriseMoment.getCreatorUid());
                enterpriseMomentAttachmentProvider.createEnterpriseMomentAttachment(attachment);
            }
        }
        enterpriseMomentProvider.updateEnterpriseMoment(enterpriseMoment);
        return processMomentDTO(enterpriseMoment, false);
    }

    private void checkAttachmentDTO(MommentAttachmentDTO dto) {
		if(StringUtils.isNotBlank(dto.getContentName())){
			dto.setContentSuffix(getSuffix(dto.getContentName()));
		}
		if(dto.getContentType() == null){
			dto.setContentType(ContentType.IMAGE.getCode());
		}
	}

	private String getSuffix(String contentName) {
		if(contentName.contains(".")){
			return contentName.substring(contentName.lastIndexOf(".") + 1);
		}
		return null;
	}

	private void checkCreateMomentCommand(CreateMomentCommand cmd) {
        assert cmd.getOrganizationId() != null;
        if (CollectionUtils.isEmpty(cmd.getScopes())) {
            cmd.setScopes(new ArrayList<>());
            MomentScopeDTO scopeDTO = new MomentScopeDTO();
            scopeDTO.setSourceId(cmd.getOrganizationId());
            scopeDTO.setSourceType(ScopeType.ORGANIZATION.getCode());
            cmd.getScopes().add(scopeDTO);
        }
        if (StringUtils.isBlank(cmd.getContent()) && CollectionUtils.isEmpty(cmd.getAttachments())) {
            throw RuntimeErrorException.errorWith(EnterpriseMomentConstants.ERROR_SCOPE, EnterpriseMomentConstants.ERROR_MOMENT_CONTENT_BLANK, "动态内容和图片不能同为空");

        }
    }

	@Override
    public void likeMoment(LikeMomentCommand cmd) {
        // 说明1：不做可见范围的验证，即用户从可见变成不可见时，只要页面不刷新，均可点赞成功
        // 说明2：不管动态是否被删除，都可以点赞成功
        // 说明3：如果动态已删除，点赞成功后不创建相关消息记录
        EnterpriseMoment moment = enterpriseMomentProvider.findEnterpriseMomentById(cmd.getEnterpriseMomentId());
        if (moment == null) {
            return;
        }
        // 系统默认的动态不支持点赞
        if (User.SYSTEM_UID == moment.getCreatorUid().longValue()) {
            return;
        }
        EnterpriseMomentFavourite favourite = new EnterpriseMomentFavourite(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId());
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(UserContext.currentUserId(), cmd.getOrganizationId());
        favourite.setCreatorUid(UserContext.currentUserId());
        favourite.setDetailId(memberDetail.getId());
        favourite.setUserId(UserContext.currentUserId());
        // 当重复点赞时insertCount返回0，保证一个用户只有一条点赞记录
        int insertCount = enterpriseMomentFavouriteProvider.createEnterpriseMomentFavourite(favourite);
        if (insertCount > 0) {
            enterpriseMomentProvider.incrLikeCount(moment.getId(), moment.getNamespaceId(), moment.getOrganizationId(), 1);
            createNewMessageAfterDoFavourite(moment, favourite);
        }
    }

    @Override
    public void unlikeMoment(UnlikeMomentCommand cmd) {
        // 说明1：不做可见范围的验证，即用户从可见变成不可见时，只要页面不刷新，均可取消点赞成功
        // 说明2：不管动态是否被删除，都可以取消点赞成功
        EnterpriseMomentFavourite favourite = enterpriseMomentFavouriteProvider.findEnterpriseMomentFavouriteById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), UserContext.currentUserId(), cmd.getEnterpriseMomentId());
        if (favourite == null) {
            return;
        }
        dbProvider.execute(transactionStatus -> {
            enterpriseMomentFavouriteProvider.deleteEnterpriseMomentFavourite(favourite);
            enterpriseMomentMessageProvider.markSourceDeleteBySourceId(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), cmd.getEnterpriseMomentId(), EhEnterpriseMomentFavourites.class.getSimpleName(), favourite.getId());
            enterpriseMomentProvider.incrLikeCount(cmd.getEnterpriseMomentId(), UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), -1);
            return null;
        });
    }

	@Override
	public ListMomentFavouritesResponse listMomentFavourites(ListMomentFavouritesCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		//获取点赞信息
		List<EnterpriseMomentFavourite> momentFavourites = enterpriseMomentFavouriteProvider.listEnterpriseMomentFavourite(
				cmd.getEnterpriseMomentId(), UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), cmd.getPageAnchor() == null ? Long.MAX_VALUE - 1 : cmd.getPageAnchor(), pageSize + 1);

        if (CollectionUtils.isEmpty(momentFavourites)) {
            return new ListMomentFavouritesResponse(null, new ArrayList<>(0));
        }

        Long nextPageAnchor = null;
        if (momentFavourites.size() > pageSize) {
            nextPageAnchor = momentFavourites.get(momentFavourites.size() - 1).getId();
            momentFavourites.remove(momentFavourites.size() - 1);
        }
        //转换为命令返回对象
        List<FavouriteDTO> favourites = momentFavourites.stream().map(x -> {
            FavouriteDTO favouriteDTO = ConvertHelper.convert(x, FavouriteDTO.class);
            User user = userProvider.findUserById(favouriteDTO.getUserId());
            favouriteDTO.setAvatarUrl(contentServerService.parserUri(user.getAvatar()));
            favouriteDTO.setContactName(organizationService.fixUpUserName(user, cmd.getOrganizationId()));
            favouriteDTO.setCreateTime(x.getCreateTime().getTime());
            return favouriteDTO;
        }).collect(Collectors.toList());
		ListMomentFavouritesResponse listMomentFavouritesResponse = new ListMomentFavouritesResponse(nextPageAnchor, favourites);
		listMomentFavouritesResponse.setTotalCount((long)enterpriseMomentFavouriteProvider.countFavourites(UserContext.getCurrentNamespaceId(),
				cmd.getOrganizationId(), cmd.getEnterpriseMomentId()));
		return listMomentFavouritesResponse;
    }

    @Override
    public ListMomentMessagesResponse listMomentMessages(ListMomentMessagesCommand cmd) {

        ListMomentMessagesResponse response = new ListMomentMessagesResponse();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        int pageSize = (cmd.getPageSize() == null) ? 30 : cmd.getPageSize();
        if (TrueOrFalseFlag.TRUE == TrueOrFalseFlag.fromCode(cmd.getNewMessageFlag())) {
            pageSize = getUserNewMessageCount(cmd.getOrganizationId(), UserContext.currentUserId());
            // 新消息数量置空
            if (pageSize > 30) {
                //超过30条只显示第一页三十条,不超过30条,显示新消息数量
                pageSize = 30;
            }
        }
        //先查询pagesize 然后再看pageAnchor因为这里要清除新消息
        if (cmd.getPageAnchor() == null) {
            //首页的查询要置空新消息
            clearNewMessageCount(UserContext.currentUserId(), cmd.getOrganizationId());
            cmd.setPageAnchor(Long.MAX_VALUE - 1);
        }
        locator.setAnchor(cmd.getPageAnchor());
        List<EnterpriseMomentMessage> results = enterpriseMomentMessageProvider.listEnterpriseMomentMessage(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), UserContext.currentUserId(), locator, pageSize + 1);
        if (CollectionUtils.isEmpty(results)) {
            return response;
        }

        Long nextPageAnchor = null;
        if (results != null && results.size() > pageSize) {
            nextPageAnchor = results.get(results.size() - 1).getId();
            results.remove(results.size() - 1);
        }
        Map<Long, EnterpriseMoment> momentCacheMap = new HashMap<>();
        response.setNextPageAnchor(nextPageAnchor);
        response.setMomentMessages(results.stream().map(r->processMomentMessageDTO(r, momentCacheMap)).collect(Collectors.toList()));
        return response;
    }

    private MomentMessageDTO processMomentMessageDTO(EnterpriseMomentMessage enterpriseMomentMessage, Map<Long, EnterpriseMoment> momentCacheMap) {
        MomentMessageDTO dto = ConvertHelper.convert(enterpriseMomentMessage, MomentMessageDTO.class);
        dto.setSourceDeleteFlag(enterpriseMomentMessage.getSourceDeleteFlag());
        dto.setSourceReplyToUserId(enterpriseMomentMessage.getIntegralTag2());
        dto.setOperatorAvatarUrl(userService.getUserAvatarUrl(dto.getOperatorUid()));
        dto.setOperateTime(enterpriseMomentMessage.getOperateTime() == null ? null : enterpriseMomentMessage.getOperateTime().getTime());
        EnterpriseMoment moment = findMomentByCache(enterpriseMomentMessage.getEnterpriseMomentId(), momentCacheMap);
        if (moment != null && TrueOrFalseFlag.TRUE != TrueOrFalseFlag.fromCode(moment.getDeleteFlag())) {
            dto.setMomentContent(moment.getContent());
        }else{
            dto.setEnterpriseMomentId(null);
            dto.setMomentDeleteFlag(TrueOrFalseFlag.TRUE.getCode());
        }
        if (StringUtils.isNotBlank(moment.getStringTag1())) {
            dto.setMomentAttachmentUrl(contentServerService.parserUri(moment.getStringTag1()));
        }
        return dto;
    }


    private EnterpriseMoment findMomentByCache(Long enterpriseMomentId,
			Map<Long, EnterpriseMoment> momentCacheMap) {
    	EnterpriseMoment moment = momentCacheMap.get(enterpriseMomentId);
    	if(moment == null){
    		moment = enterpriseMomentProvider.findEnterpriseMomentById(enterpriseMomentId);
    		if(moment != null){
    			momentCacheMap.put(enterpriseMomentId, moment);
    		}
    	}
		return moment;
	}

	@Override
    public void incrNewMessageCount(Long userId, Long organizationId, int incr) {
        String key = String.format(NEW_MESSAGE_COUNT_KEY, userId, organizationId);
        ValueOperations<String, String> operations = getRedisTemplate(key).opsForValue();
        Object count = operations.get(key);
        if (count == null) {
            operations.set(key, String.valueOf(incr));
        } else {
            operations.increment(key, incr);
        }
    }

    @Override
    public void clearNewMessageCount(Long userId, Long organizationId) {
        String key = String.format(NEW_MESSAGE_COUNT_KEY, userId, organizationId);
        ValueOperations<String, String> operations = getRedisTemplate(key).opsForValue();
        operations.getOperations().delete(key);
    }

    @Override
    public void createNewMessageAfterDoFavourite(EnterpriseMoment moment, EnterpriseMomentFavourite favourite) {
        // 点赞自己发布的动态或者该动态已被删除则不需要创建历史消息
        if (UserContext.currentUserId().equals(moment.getCreatorUid()) || TrueOrFalseFlag.TRUE == TrueOrFalseFlag.fromCode(moment.getDeleteFlag())) {
            return;
        }
        Long userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ExecutorUtil.submit(() -> {
            LocalEventBus.publish(event -> {
                LocalEventContext context = new LocalEventContext();
                context.setUid(userId);
                context.setNamespaceId(namespaceId);
                event.setContext(context);

                event.setEntityType(EhEnterpriseMoments.class.getSimpleName());
                event.setEntityId(moment.getId());

                event.setEventName(SystemEvent.ENTERPRISE_MOMENT_DO_FAVOURITE.getCode());
                event.addParam("favouriteId", String.valueOf(favourite.getId()));
            });
        });
    }

    @Override
    public List<CommentDTO> buildCommentDTO(Integer namespaceId, Long organizationId, List<EnterpriseMomentComment> comments) {
        if (CollectionUtils.isEmpty(comments)) {
            return new ArrayList<>();
        }
        List<Long> commentIds = comments.stream().map(EnterpriseMomentComment::getId).collect(Collectors.toList());
        Map<Long, List<AttachmentDTO>> attachments = listCommentAttachments(UserContext.getCurrentNamespaceId(), commentIds);
        Map<Long, User> userCache = new HashMap<>();
        List<CommentDTO> results = new ArrayList<>();
        comments.forEach(r -> {
            User creator = getUser(r.getCreatorUid(), userCache);
            CommentDTO dto = com.everhomes.util.ConvertHelper.convert(r, CommentDTO.class);
            dto.setParentCommentId(r.getReplyToCommentId());
            dto.setReplyToUserId(r.getReplyToUserId());
            dto.setCreatorNickName(r.getCreatorName());
            if (creator != null) {
                dto.setCreatorAvatarUrl(contentServerService.parserUri(creator.getAvatar()));
            }
            dto.setAttachments(attachments.get(dto.getId()));
            dto.setCreateTime(r.getCreateTime());
            results.add(dto);
        });
        return results;
    }

	@Override
	public GetBannerResponse getBanner(GetBannerCommand cmd) {
		String banner = configProvider.getValue(ConfigConstants.ENTERPRISE_BANNER, "");
		GetBannerResponse getBannerResponse = new GetBannerResponse();
		getBannerResponse.setBannerUrl(contentServerService.parserUri(banner));
		return getBannerResponse;
	}

	private Map<Long, List<AttachmentDTO>> listCommentAttachments(Integer namespaceId, List<Long> commentIds) {
        List<EnterpriseMomentCommentAttachment> results = commentAttachmentProvider.queryAttachmentsByCommentIds(namespaceId, commentIds);
        if (org.springframework.util.CollectionUtils.isEmpty(results)) {
            return Collections.emptyMap();
        }

        List<AttachmentDTO> attachmentDTOS = results.stream().map(r -> {
            AttachmentDTO dto = com.everhomes.util.ConvertHelper.convert(r, AttachmentDTO.class);
            dto.setOwnerId(r.getCommentId());
            dto.setContentUrl(contentServerService.parserUri(r.getContentUri()));
            return dto;
        }).collect(Collectors.toList());

        return attachmentDTOS.stream().collect(Collectors.groupingBy(AttachmentDTO::getOwnerId));
    }

    private User getUser(Long userId, Map<Long, User> cache) {
        if (cache.containsKey(userId)) {
            return cache.get(userId);
        }
        User user = userProvider.findUserById(userId);
        if (user != null) {
            cache.put(userId, user);
        }
        return user;
    }
}