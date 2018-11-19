package com.everhomes.remind;

import com.everhomes.category.Category;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.remind.BatchSortRemindCategoryCommand;
import com.everhomes.rest.remind.BatchSortRemindCommand;
import com.everhomes.rest.remind.CreateOrUpdateRemindCategoryCommand;
import com.everhomes.rest.remind.CreateOrUpdateRemindCommand;
import com.everhomes.rest.remind.DeleteRemindCategoryCommand;
import com.everhomes.rest.remind.DeleteRemindCommand;
import com.everhomes.rest.remind.GetCurrentUserDetailIdCommand;
import com.everhomes.rest.remind.GetCurrentUserDetailIdResponse;
import com.everhomes.rest.remind.GetRemindCategoryColorsResponse;
import com.everhomes.rest.remind.GetRemindCategoryCommand;
import com.everhomes.rest.remind.GetRemindCommand;
import com.everhomes.rest.remind.GetRemindSettingsResponse;
import com.everhomes.rest.remind.ListRemindCategoriesCommand;
import com.everhomes.rest.remind.ListRemindCategoriesResponse;
import com.everhomes.rest.remind.ListRemindResponse;
import com.everhomes.rest.remind.ListSelfRemindCommand;
import com.everhomes.rest.remind.ListShareRemindCommand;
import com.everhomes.rest.remind.ListSharingPersonsCommand;
import com.everhomes.rest.remind.ListSharingPersonsResponse;
import com.everhomes.rest.remind.RemindCategoryDTO;
import com.everhomes.rest.remind.RemindDTO;
import com.everhomes.rest.remind.RemindErrorCode;
import com.everhomes.rest.remind.RemindRepeatType;
import com.everhomes.rest.remind.RemindSettingDTO;
import com.everhomes.rest.remind.RemindStatus;
import com.everhomes.rest.remind.SelfRemindDetailActionData;
import com.everhomes.rest.remind.ShareMemberDTO;
import com.everhomes.rest.remind.ShareMemberSourceType;
import com.everhomes.rest.remind.SharingPersonDTO;
import com.everhomes.rest.remind.SubscribeShareRemindCommand;
import com.everhomes.rest.remind.SubscribeStatus;
import com.everhomes.rest.remind.TrackRemindDetailActionData;
import com.everhomes.rest.remind.UnSubscribeShareRemindCommand;
import com.everhomes.rest.remind.UpdateRemindStatusCommand;
import com.everhomes.rest.remind.UpdateRemindStatusResponse;
import com.everhomes.scheduler.CalendarRemindScheduleJob;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.tables.pojos.EhRemindCategoryDefaultShares;
import com.everhomes.server.schema.tables.pojos.EhRemindShares;
import com.everhomes.share.ShareService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javassist.runtime.DotClass;

@Service
public class RemindServiceImpl implements RemindService  {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemindServiceImpl.class);
    private static final String DEFAULT_CATEGORY_NAME = "默认分类";
    private static final int FETCH_SIZE = 2000;
    private static final int PLAN_DATE_CALCULATE_MAX_LOOP = 365;

    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private RemindProvider remindProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private MessagingService messagingService;
    private ExecutorService bgThreadPool = Executors.newFixedThreadPool(1);
    @Autowired
    private ScheduleProvider scheduleProvider;
    @Autowired
    private CalendarRemindScheduleJob calendarRemindScheduleJob;
    
    @Override
    public GetRemindCategoryColorsResponse getRemindCategoryColors() {
        String colours = configurationProvider.getValue(ConfigConstants.REMIND_COLOUR_LIST, "");
        GetRemindCategoryColorsResponse response = new GetRemindCategoryColorsResponse();
        response.setColours(Arrays.asList(colours.split(",")));
        return response;
    }

    @Override
    public GetRemindSettingsResponse listRemindSettings() {
        List<RemindSetting> settings = remindProvider.findRemindSettings();
        List<RemindSettingDTO> types = settings.stream().filter(r->checkVersion(r.getAppVersion())).map(r -> {
            RemindSettingDTO dto = new RemindSettingDTO();
            dto.setRemindTypeId(r.getId());
            dto.setRemindDisplayName(r.getName());
            dto.setOffsetDay(r.getOffsetDay());
            dto.setFixTime(r.getFixTime() != null ? r.getFixTime().getTime() : null);
            dto.setDefaultOrder(r.getDefaultOrder());
            return dto;
        }).collect(Collectors.toList());

        GetRemindSettingsResponse response = new GetRemindSettingsResponse();
        response.setRemindTypes(types);
        return response;
    }
    /**
     * 根据设置的分隔版本号 比如"5.8.0;5.9.0"
     * 则如果app版本号和setting版本号都不到5.8.0
     * 或者超过5.8.0不到5.9.0
     * 或者都超过5.9.0 返回为true 否则为false
     * */
    private boolean checkVersion(String settingVersionStr) {
        if (null != settingVersionStr) {
            try {
                Version settingVer = Version.fromVersionString(settingVersionStr);
                String sgemenConfig = configurationProvider.getValue(ConfigConstants.REMIND_VERSION_SEGMEN, "5.9.0");
                String[] versionSegs = StringUtils.split(sgemenConfig, ";");
                //如果UserContext 获取不到version或者version不合法,就当做最新版本.用versionSegs的最后一个版本
                Version requestVersion = Version.fromVersionString(versionSegs[versionSegs.length-1]);
                try{
                	//0.0.0是网页请求之类的,设置为最新版本
                	if(!UserContext.current().getVersion().equals("0.0.0")){
                		requestVersion = Version.fromVersionString(UserContext.current().getVersion());
                	}
                }catch(InvalidParameterException e){
                	requestVersion = Version.fromVersionString(versionSegs[versionSegs.length-1]);
                }
                for (int i = 0; i < versionSegs.length; i++) {
                    Version segmenVersion = Version.fromVersionString(versionSegs[i]);
                    long segmenValue = Version.encodeValue(segmenVersion.getMajor(), segmenVersion.getMinor(), segmenVersion.getRevision());
                    //如果请求的版本号小于分隔版本 : 提醒的版本小于分隔版本则为true;否则为false
                    if(Version.encodeValue(requestVersion.getMajor(), requestVersion.getMinor(), requestVersion.getRevision()) < segmenValue){
                        if (Version.encodeValue(settingVer.getMajor(), settingVer.getMinor(), settingVer.getRevision()) < segmenValue) {
                            return true;
                        }else{
                            return false;
                        }
                    }else {
                        //如果请求版本号不小于分隔版本, setting的小于分隔版本 也为假
                        if (Version.encodeValue(settingVer.getMajor(), settingVer.getMinor(), settingVer.getRevision()) < segmenValue) {
                            return false;
                        }
                    }
                }
                //循环所有分隔版本请求版本号和setting的版本号都不小于,则都是新版本则为真
                return true;
            }catch(Exception e ){
                LOGGER.error("检测version出错了", e);
                return false;
            }
        }
        return false;
    }

    @Override
    public void evictRemindSettingsCache() {
        remindProvider.evictRemindSettingsCache();
    }

    @Override
    public Long createOrUpdateRemindCategory(CreateOrUpdateRemindCategoryCommand cmd) {
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        checkCategoryNameExist(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName(), cmd.getId());

        String shareShortDisplay = "无";
        if (!CollectionUtils.isEmpty(cmd.getShareToMembers())) {
            shareShortDisplay = String.format("%s等%d人", cmd.getShareToMembers().get(0).getSourceName(), cmd.getShareToMembers().size());
        }

        if (cmd.getId() == null) {
            Integer nextOrder = remindProvider.getNextCategoryDefaultOrder(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId());

            RemindCategory remindCategory = new RemindCategory();
            remindCategory.setNamespaceId(namespaceId);
            remindCategory.setOwnerType(cmd.getOwnerType());
            remindCategory.setOwnerId(cmd.getOwnerId());
            remindCategory.setName(cmd.getName());
            remindCategory.setUserId(UserContext.currentUserId());
            remindCategory.setColour(cmd.getColour());
            remindCategory.setDefaultOrder(nextOrder != null ? nextOrder : Integer.valueOf(1));
            remindCategory.setShareShortDisplay(shareShortDisplay);

            dbProvider.execute(transactionStatus -> {
                remindProvider.createRemindCategory(remindCategory);
                remindProvider.batchCreateRemindCategoryDefaultShare(buildDefaultShares(cmd.getShareToMembers(), remindCategory.getId()));
                return null;
            });
            return remindCategory.getId();
        }

        RemindCategory existRemindCategory = remindProvider.getRemindCategory(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId(), cmd.getId());
        if (existRemindCategory != null) {
            existRemindCategory.setName(cmd.getName());
            existRemindCategory.setColour(cmd.getColour());
            existRemindCategory.setShareShortDisplay(shareShortDisplay);
            dbProvider.execute(transactionStatus -> {
                remindProvider.updateRemindCategory(existRemindCategory);
                List<RemindCategoryDefaultShare> categoryShares = remindProvider.findShareMemberDetailsByCategoryId(existRemindCategory.getId());
                remindProvider.deleteRemindCategoryDefaultSharesByCategoryId(existRemindCategory.getId());
                remindProvider.batchCreateRemindCategoryDefaultShare(buildDefaultShares(cmd.getShareToMembers(), existRemindCategory.getId()));
                //给修改的category里面新增的共享人发信息
                if(!CollectionUtils.isEmpty(cmd.getShareToMembers())){
                	List<Remind> reminds = remindProvider.findRemindsByCategoryId(existRemindCategory.getId());
                	if(CollectionUtils.isEmpty(reminds))
                		return null;
                	for(ShareMemberDTO shareToMember : cmd.getShareToMembers()){
                		if(!checkCategoryShareExists(categoryShares, shareToMember)){
                			for(Remind remind : reminds){
                				if(remind.getStatus().equals(RemindStatus.DONE.getCode())){
                					continue;
                				}
	                			Remind trackRemind = convertRemindShareToMSGRemind(remind, shareToMember);
	                            sendTrackMessageOnBackGround(remind.getPlanDescription(), Arrays.asList(trackRemind), RemindModifyType.CREATE_SUBSCRIBE);
                			}
                		}
                	}
                } 

                return null;
            });
            return existRemindCategory.getId();
        }
        return Long.valueOf(0);
    }

    private boolean checkCategoryShareExists(List<RemindCategoryDefaultShare> categoryShares,
			ShareMemberDTO shareToMember) {
    	if (CollectionUtils.isEmpty(categoryShares)) {
            return false;
        }
    	for(RemindCategoryDefaultShare share: categoryShares){
    		if(share.getSharedSourceId().equals(shareToMember.getSourceId()) && share.getSharedSourceId().equals(shareToMember.getSourceId()))
    			return true;
    	}
		return false;
	}

	private void checkCategoryNameExist(Integer namespaceId, String ownerType, Long ownerId, String name, Long categoryId) {
        CheckRemindCategoryNameExistRequest request = new CheckRemindCategoryNameExistRequest(categoryId, namespaceId, ownerType, ownerId, UserContext.currentUserId(), name);
        boolean isExist = remindProvider.checkCategoryNameExist(request);
        if (isExist) {
            throw RuntimeErrorException
                    .errorWith(
                            RemindErrorCode.SCOPE,
                            RemindErrorCode.REMIND_CATEGORY_NAME_EXIST_ERROR,
                            localeStringService.getLocalizedString(
                                    String.valueOf(RemindErrorCode.SCOPE),
                                    String.valueOf(RemindErrorCode.REMIND_CATEGORY_NAME_EXIST_ERROR),
                                    UserContext.current().getUser().getLocale(),
                                    "The category name has already existed"));
        }
    }

    private List<EhRemindCategoryDefaultShares> buildDefaultShares(List<ShareMemberDTO> shareMemberDTOS, Long categoryId) {
        if (CollectionUtils.isEmpty(shareMemberDTOS)) {
            return Collections.emptyList();
        }
        List<EhRemindCategoryDefaultShares> defaultShares = new ArrayList<>();
        shareMemberDTOS.stream().forEach(shareMember -> {
            EhRemindCategoryDefaultShares remindCategoryDefaultShare = new EhRemindCategoryDefaultShares();
            remindCategoryDefaultShare.setRemindCategoryId(categoryId);
            remindCategoryDefaultShare.setSharedSourceId(shareMember.getSourceId());
            remindCategoryDefaultShare.setSharedSourceType(shareMember.getSourceType());
            remindCategoryDefaultShare.setSharedContractName(shareMember.getSourceName());
            defaultShares.add(remindCategoryDefaultShare);
        });
        return defaultShares;
    }

    private List<EhRemindShares> buildRemindShares(List<ShareMemberDTO> shareMemberDTOS, Remind remind, List<ShareMemberDTO>  historyShareReminds) {
    	return buildRemindShares(shareMemberDTOS, remind, historyShareReminds, true);
    }
    private List<EhRemindShares> buildRemindShares(List<ShareMemberDTO> shareMemberDTOS, Remind remind, List<ShareMemberDTO>  historyShareReminds, Boolean sendCreateRemindFlag) {
        if (CollectionUtils.isEmpty(shareMemberDTOS)) {
            return Collections.emptyList();
        }
        //2018-10-23 5.10.0 增加创建日程向共享人发送提醒
        List<Remind> trackReminds = new ArrayList<>();
        List<EhRemindShares> shares = new ArrayList<>();
        shareMemberDTOS.stream().forEach(shareMember -> {
            RemindShare remindShare = new RemindShare();
            remindShare.setRemindId(remind.getId());
            remindShare.setNamespaceId(remind.getNamespaceId());
            remindShare.setOwnerType(remind.getOwnerType());
            remindShare.setOwnerId(remind.getOwnerId());
            remindShare.setOwnerUserId(UserContext.currentUserId());
            remindShare.setOwnerContractName(remind.getContactName());
            remindShare.setSharedSourceId(shareMember.getSourceId());
            remindShare.setSharedSourceType(shareMember.getSourceType());
            remindShare.setSharedSourceName(shareMember.getSourceName());
            shares.add(remindShare);
            if(ShareMemberSourceType.MEMBER_DETAIL == ShareMemberSourceType.fromCode(shareMember.getSourceType())){
            	//对于要发共享消息提醒的
            	if(sendCreateRemindFlag){
	            	if(checkShareRemindInHistory(historyShareReminds, shareMember)){
	            		//已经在历史版本里存在的,就不发消息
	            	}else{
			            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(shareMember.getSourceId());
			            if(null != detail && !detail.getTargetId().equals(0L)){
				            Remind trackRemind = processSendMsgTrackRemind(remind, detail);
				            trackReminds.add(trackRemind);
			            }
	            	}
            	}
            }
        });
        
        if(sendCreateRemindFlag && RemindStatus.fromCode(remind.getStatus()) != RemindStatus.DONE){
        	sendTrackMessageOnBackGround(remind.getPlanDescription(), trackReminds, RemindModifyType.CREATE_SUBSCRIBE);
        }
        return shares;
    }

	private boolean checkShareRemindInHistory(List<ShareMemberDTO> historyShareReminds, RemindCategoryDefaultShare cs) {
		if (CollectionUtils.isEmpty(historyShareReminds)) {
            return false;
        }
    	for(ShareMemberDTO share: historyShareReminds){
    		if(share.getSourceType().equals(cs.getSharedSourceType()) && share.getSourceId().equals(cs.getSharedSourceId()))
    			return true;
    	}
		return false;
	}

	private Remind processSendMsgTrackRemind(Remind remind, OrganizationMemberDetails detail) {
		Remind trackRemind = ConvertHelper.convert(remind, Remind.class);
		trackRemind.setUserId(detail.getTargetId());
		trackRemind.setTrackRemindId(remind.getId());
		trackRemind.setTrackContractName(remind.getContactName());
		trackRemind.setTrackRemindUserId(remind.getUserId());
		return trackRemind;
	}

	private Remind convertRemindShareToMSGRemind(Remind remind, ShareMemberDTO shareToMember) {
		Remind trackRemind = ConvertHelper.convert(remind, Remind.class);
		OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(shareToMember.getSourceId());
		if(null != detail && !detail.getTargetId().equals(0L)){
		    trackRemind.setUserId(detail.getTargetId());
		    trackRemind.setTrackRemindId(remind.getId());
		    trackRemind.setTrackContractName(remind.getContactName());
		    trackRemind.setTrackRemindUserId(remind.getUserId());
		    
		}
		return trackRemind;
	}

    private boolean checkShareRemindInHistory(List<ShareMemberDTO>  historyShareReminds, ShareMemberDTO shareMember) { 
    	if (CollectionUtils.isEmpty(historyShareReminds)) {
            return false;
        }
    	for(ShareMemberDTO share: historyShareReminds){
    		if(share.getSourceType().equals(shareMember.getSourceType()) && share.getSourceId().equals(shareMember.getSourceId()))
    			return true;
    	}
		return false;
	}

	@Override
    public void batchSortRemindCategories(BatchSortRemindCategoryCommand cmd) {
        if (CollectionUtils.isEmpty(cmd.getSortedRemindCategoryIds())) {
            return;
        }
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        List<RemindCategory> myAllCategoriesDefaultOrderDesc = remindProvider.findRemindCategories(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId());

        List<RemindCategory> targetSortedRemindCategories = reSortedCategories(cmd.getSortedRemindCategoryIds(), myAllCategoriesDefaultOrderDesc);
        if (CollectionUtils.isEmpty(targetSortedRemindCategories)) {
            return;
        }

        this.coordinationProvider.getNamedLock(CoordinationLocks.REMIND_CATEGORY_SORTING.getCode() + UserContext.currentUserId()).enter(() -> {
            // defaultOrder  值越小排在越后面，所以倒序循环
            int defaultOrder = 1;
            for (int i = targetSortedRemindCategories.size() - 1; i >= 0; i--) {
                RemindCategory remindCategory = targetSortedRemindCategories.get(i);
                remindCategory.setDefaultOrder(defaultOrder++);
                remindProvider.updateRemindCategory(remindCategory);
            }
            return null;
        });

    }

    private List<RemindCategory> reSortedCategories(List<Long> targetSortedCategoryIds, List<RemindCategory> originSortedCategories) {
        if (CollectionUtils.isEmpty(targetSortedCategoryIds) || CollectionUtils.isEmpty(originSortedCategories)) {
            return Collections.emptyList();
        }

        Map<Long, RemindCategory> map = new LinkedHashMap<>();
        originSortedCategories.forEach(remind -> {
            map.put(remind.getId(), remind);
        });

        List<RemindCategory> reSortedRemindCategories = new ArrayList<>(originSortedCategories.size());
        for (int i = 0; i < targetSortedCategoryIds.size(); i++) {
            // 按目标顺序存放
            Long remindCategoryId = targetSortedCategoryIds.get(i);
            if (remindCategoryId == null) {
                continue;
            }
            if (map.containsKey(remindCategoryId)) {
                reSortedRemindCategories.add(map.remove(remindCategoryId));
            }
        }
        // 不在targetSortedCategoryIds的数据按原有顺序插入到前面
        if (!map.isEmpty()) {
            reSortedRemindCategories.addAll(0, map.values());
        }
        return reSortedRemindCategories;
    }


    @Override
    public void deleteRemindCategory(DeleteRemindCategoryCommand cmd) {
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        RemindCategory remindCategory = remindProvider.getRemindCategory(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId(), cmd.getId());
        if (remindCategory == null) {
            return;
        }

        checkRemindCategoryIsLastOne(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId());

        List<Remind> reminds = remindProvider.findRemindsByCategoryId(remindCategory.getId());

        // 获取即将被删除的分类下的日程的被关注记录，用于推送消息
        List<Long> trackRemindIds = reminds.stream().map(remind -> {
            return remind.getId();
        }).collect(Collectors.toList());

        List<Remind> trackReminds = remindProvider.findRemindsByTrackRemindIds(trackRemindIds);

        dbProvider.execute(transactionStatus -> {
            remindProvider.deleteRemindCategory(remindCategory);
            remindProvider.deleteRemindCategoryDefaultSharesByCategoryId(remindCategory.getId());
            remindProvider.deleteRemindsByCategoryId(remindCategory.getId());
            remindProvider.deleteRemindsByTrackRemindId(trackRemindIds);
            deleteRemindRedis(trackReminds);
            return null;
        });

        // 给关注日程的人员推送日程被删除的消息
        sendTrackMessageOnBackGround(null, trackReminds, RemindModifyType.DELETE);
    }

    private void checkRemindCategoryIsLastOne(Integer namespaceId, String ownerType, Long ownerId, Long userId) {
        if (remindProvider.countUserRemindCategories(namespaceId, ownerType, ownerId, userId) <= 1) {
            throw RuntimeErrorException
                    .errorWith(
                            RemindErrorCode.SCOPE,
                            RemindErrorCode.REMIND_CATEGORY_DELETE_LAST_ONE_ERROR,
                            localeStringService.getLocalizedString(
                                    String.valueOf(RemindErrorCode.SCOPE),
                                    String.valueOf(RemindErrorCode.REMIND_CATEGORY_DELETE_LAST_ONE_ERROR),
                                    UserContext.current().getUser().getLocale(),
                                    "The last one can not be deleted"));
        }
    }

    @Override
    public RemindCategoryDTO getRemindCategoryDetail(GetRemindCategoryCommand cmd) {
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        RemindCategory remindCategory = remindProvider.getRemindCategory(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId(), cmd.getId());
        if (remindCategory == null) {
            return null;
        }
        return convertToRemindCategoryDTO(remindCategory, true);
    }

    private RemindCategoryDTO convertToRemindCategoryDTO(RemindCategory remindCategory, boolean queryShares) {
        RemindCategoryDTO remindCategoryDTO = new RemindCategoryDTO();
        remindCategoryDTO.setId(remindCategory.getId());
        remindCategoryDTO.setName(remindCategory.getName());
        remindCategoryDTO.setColour(remindCategory.getColour());
        remindCategoryDTO.setDefaultOrder(remindCategory.getDefaultOrder());
        remindCategoryDTO.setShareShortDisplay(remindCategory.getShareShortDisplay());

        if (!queryShares) {
            // 为了提高性能，列表查询时不查询共享人信息
            return remindCategoryDTO;
        }

        List<RemindCategoryDefaultShare> shares = remindProvider.findShareMemberDetailsByCategoryId(remindCategoryDTO.getId());
        if (CollectionUtils.isEmpty(shares)) {
            remindCategoryDTO.setShareShortDisplay("无");
            return remindCategoryDTO;
        }
        List<ShareMemberDTO> shareMemberDTOS = new ArrayList<>();
        List<EhRemindCategoryDefaultShares> deleteDefaultShares = new ArrayList<>();
        shares.forEach(defaultShare -> {
            if (ShareMemberSourceType.MEMBER_DETAIL == ShareMemberSourceType.fromCode(defaultShare.getSharedSourceType())) {
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(defaultShare.getSharedSourceId());
                if (detail != null) {
                    ShareMemberDTO shareMemberDTO = new ShareMemberDTO();
                    shareMemberDTO.setSourceId(defaultShare.getSharedSourceId());
                    shareMemberDTO.setSourceType(defaultShare.getSharedSourceType());
                    shareMemberDTO.setSourceName(detail.getContactName());

                    if (detail.getTargetId() > 0) {
                        shareMemberDTO.setContactAvatar(getUserAvatar(detail.getTargetId()));
                    }
                    shareMemberDTOS.add(shareMemberDTO);
                } else {
                    deleteDefaultShares.add(defaultShare);
                }
            }
        });

        if (!CollectionUtils.isEmpty(shareMemberDTOS)) {
            remindCategoryDTO.setShareShortDisplay(String.format("%s等%d人", shareMemberDTOS.get(0).getSourceName(), shareMemberDTOS.size()));
        } else {
            remindCategoryDTO.setShareShortDisplay("无");
        }

        // 移除不存在的数据
        if (!CollectionUtils.isEmpty(deleteDefaultShares)) {
            dbProvider.execute(transactionStatus -> {
                remindCategory.setShareShortDisplay(remindCategoryDTO.getShareShortDisplay());
                remindProvider.updateRemindCategory(remindCategory);
                remindProvider.batchDeleteRemindCategoryDefaultShares(deleteDefaultShares);
                return null;
            });
        }

        remindCategoryDTO.setShareToMembers(shareMemberDTOS);
        return remindCategoryDTO;
    }

    @Override
    public ListRemindCategoriesResponse listRemindCategories(ListRemindCategoriesCommand cmd) {
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        cmd.setUserId(cmd.getUserId() != null ? cmd.getUserId() : UserContext.currentUserId());
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        ListRemindCategoriesResponse response = new ListRemindCategoriesResponse();

        List<RemindCategory> categories = getRemindCategories(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getUserId());
        if (CollectionUtils.isEmpty(categories)) {
            return response;
        }
        List<RemindCategoryDTO> remindCategoryDTOS = new ArrayList<>(categories.size());
        categories.forEach(remindCategory -> {
            remindCategoryDTOS.add(convertToRemindCategoryDTO(remindCategory, true));
        });

        response.setCategories(remindCategoryDTOS);
        return response;
    }

    private List<RemindCategory> getRemindCategories(Integer namespaceId, String ownerType, Long ownerId, Long userId) {
        List<RemindCategory> categories = remindProvider.findRemindCategories(namespaceId, ownerType, ownerId, userId);
        if (CollectionUtils.isEmpty(categories)) {
            this.coordinationProvider.getNamedLock(CoordinationLocks.DEFAULT_REMIND_CATEGORY_ADD.getCode() + userId).enter(() -> {
                // 获得锁以后进行二次判断
                List<RemindCategory> categoryList = remindProvider.findRemindCategories(namespaceId, ownerType, ownerId, userId);
                if (CollectionUtils.isEmpty(categoryList)) {
                    createDefaultCategory(namespaceId, ownerType, ownerId, userId);
                }
                return null;
            });
            categories = remindProvider.findRemindCategories(namespaceId, ownerType, ownerId, userId);
        }
        return categories;
    }

    @Override
    public ListSharingPersonsResponse listSharingPersons(ListSharingPersonsCommand cmd) {
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        cmd.setUserId(cmd.getUserId() != null ? cmd.getUserId() : UserContext.currentUserId());
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(cmd.getUserId(), cmd.getOwnerId());

        ListSharingPersonsResponse response = new ListSharingPersonsResponse();
        if (member == null || member.getDetailId() == null) {
            return response;
        }

        List<SharingPersonDTO> sharingPersonDTOS = remindProvider.findSharingPersonsBySourceId(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), ShareMemberSourceType.MEMBER_DETAIL.getCode(), member.getDetailId());
        response.setPersons(sharingPersonDTOS);
        return response;
    }

    @Override
    public Long createOrUpdateRemind(CreateOrUpdateRemindCommand cmd) {
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (cmd.getRepeatType() == null) {
            cmd.setRepeatType(RemindRepeatType.NONE.getCode());
        }
        if (cmd.getPlanDate() != null && cmd.getPlanDate() <= 0) {
            cmd.setPlanDate(null);
        }
        if (cmd.getId() == null) {
            return createRemind(cmd);
        }

        Remind existRemind = remindProvider.getRemindDetail(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId(), cmd.getId());
        if (existRemind != null) {
            return updateRemind(cmd, existRemind);
        } else {
            // 如果id指定的日程已经被删除，则新建一个，同时状态和被删除的日程保持一致
            cmd.setId(null);
            return createRemind(cmd);
        }
    }

    private Long createRemind(CreateOrUpdateRemindCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        cmd.setStatus(cmd.getStatus() == null ? RemindStatus.UNDO.getCode() : cmd.getStatus());
        String shareShortDisplay = "无";
        List<RemindCategoryDefaultShare> shares = remindProvider.findShareMemberDetailsByCategoryId(cmd.getRemindCategoryId());
        int shareCount = 0;
        String shareName = "";
        if (!CollectionUtils.isEmpty(shares)) {
        	shareName = shares.get(0).getSharedContractName();
            shareCount += shares.size();
        }
        if (!CollectionUtils.isEmpty(cmd.getShareToMembers())) {
        	if("".equals(shareName)){
        		shareName = shares.get(0).getSharedContractName();
        	}
            shareCount += cmd.getShareToMembers().size();
        }
        shareShortDisplay = String.format("%s等%d人", shareName, shareCount);

        RemindSetting remindSetting = null;
        if (cmd.getRemainTypeId() != null) {
            remindSetting = remindProvider.getRemindSettingById(cmd.getRemainTypeId());
        }
        String remindType = remindSetting != null ? remindSetting.getName() : null;
        Timestamp planDate = null;
        if (cmd.getPlanDate() != null) {
            planDate = new Timestamp(cmd.getPlanDate() + (cmd.getPlanTime() == null ? 0 : cmd.getPlanTime()));
        }
        RemindRepeatType repeatType = RemindRepeatType.fromCode(cmd.getRepeatType());

        // 创建，编辑，完成，未完成，加入我的日程等操作的日程排在列表最前面
        Integer nextOrder = remindProvider.getNextRemindDefaultOrder(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId(), cmd.getStatus());
        Integer order = nextOrder != null ? nextOrder : Integer.valueOf(1);
        Remind remind = new Remind();
        remind.setNamespaceId(namespaceId);
        remind.setOwnerType(cmd.getOwnerType());
        remind.setOwnerId(cmd.getOwnerId());
        remind.setUserId(UserContext.currentUserId());
        remind.setContactName(getContractNameByUserId(UserContext.currentUserId(), cmd.getOwnerId()));
        remind.setRemindCategoryId(cmd.getRemindCategoryId());
        remind.setPlanDate(planDate);
        remind.setPlanDescription(cmd.getPlanDescription());
        remind.setRemindTypeId(cmd.getRemainTypeId());
        remind.setRemindType(remindType);
        remind.setRemindTime(buildRemindTime(planDate, remindSetting));
        remind.setDefaultOrder(order);
        remind.setShareShortDisplay(shareShortDisplay);
        remind.setShareCount(shareCount);
        remind.setExpectDayOfMonth(getExceptDayOfMonth(planDate));
        remind.setRemindSummary(getRemindSummary(planDate, RemindRepeatType.fromCode(cmd.getRepeatType())));
        remind.setRepeatType(repeatType.getCode());
        remind.setStatus(cmd.getStatus());
        
        dbProvider.execute(transactionStatus -> {
            remindProvider.createRemind(remind);
            setRemindRedis(remind);
            remindProvider.batchCreateRemindShare(buildRemindShares(cmd.getShareToMembers(), remind, null));
            return null;
        });

        if(remind.getRemindCategoryId() != null){
            List<Remind> trackReminds = new ArrayList<>();
            List<RemindCategoryDefaultShare> categoryShareReminds = remindProvider.findShareMemberDetailsByCategoryId(remind.getRemindCategoryId());
            if(!CollectionUtils.isEmpty(categoryShareReminds)){
            	categoryShareReminds.forEach(r->{trackReminds.add(convertRemindShareToMSGRemind(remind, r));});
            }
			sendTrackMessageOnBackGround(remind.getPlanDescription(), trackReminds, RemindModifyType.CREATE_SUBSCRIBE);
        }
        
        return remind.getId();
    }

    private Remind convertRemindShareToMSGRemind(Remind remind, RemindCategoryDefaultShare remindShare) {
    	Remind trackRemind = ConvertHelper.convert(remind, Remind.class);
		OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(remindShare.getSharedSourceId());
		if(null != detail && !detail.getTargetId().equals(0L)){
		    trackRemind.setUserId(detail.getTargetId());
		    trackRemind.setTrackRemindId(remind.getId());
		    trackRemind.setTrackContractName(remind.getContactName());
		    trackRemind.setTrackRemindUserId(remind.getUserId());
		    
		}
		return trackRemind;
	}

	/**
     * updateRemind方法没有更新状态，状态的更新走updateRemindStatus接口
     */
    private Long updateRemind(CreateOrUpdateRemindCommand cmd, Remind existRemind) {
    	//再信息没被改变之前先查历史的共享人
    	Long oldCategoryId = existRemind.getRemindCategoryId();
        List<ShareMemberDTO> historyShareReminds = findRemindShares(existRemind.getId(), existRemind.getRemindCategoryId());
        
        String originPlanDescription = existRemind.getPlanDescription();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        String shareShortDisplay = "无";
        int shareCount = 0;
        List<RemindCategoryDefaultShare> shares = remindProvider.findShareMemberDetailsByCategoryId(cmd.getRemindCategoryId());
        if (!CollectionUtils.isEmpty(shares)) {
            shareShortDisplay = String.format("%s等%d人", shares.get(0).getSharedContractName(), shares.size());
            shareCount += shares.size();
        }
        if (!CollectionUtils.isEmpty(cmd.getShareToMembers())) {
            shareShortDisplay = String.format("%s等%d人", cmd.getShareToMembers().get(0).getSourceName(), cmd.getShareToMembers().size());
            shareCount += cmd.getShareToMembers().size();
        }

        RemindSetting remindSetting = null;
        if (cmd.getRemainTypeId() != null) {
            remindSetting = remindProvider.getRemindSettingById(cmd.getRemainTypeId());
        }
        String remindType = remindSetting != null ? remindSetting.getName() : null;
        Timestamp planDate = null ;
        if (cmd.getPlanDate() != null) {
            planDate = new Timestamp(cmd.getPlanDate() + (cmd.getPlanTime() == null ? 0 : cmd.getPlanTime()));
        }
        RemindRepeatType repeatType = RemindRepeatType.fromCode(cmd.getRepeatType());

        // 创建，编辑，完成，未完成，加入我的日程等操作的日程排在列表最前面
        Integer nextOrder = remindProvider.getNextRemindDefaultOrder(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId(), existRemind.getStatus());
        Integer order = nextOrder != null ? nextOrder : Integer.valueOf(1);
        existRemind.setRemindCategoryId(cmd.getRemindCategoryId());
        existRemind.setPlanDate(planDate);
        existRemind.setPlanDescription(cmd.getPlanDescription());
        existRemind.setRemindTypeId(cmd.getRemainTypeId());
        existRemind.setRemindType(remindType);
        existRemind.setRemindTime(buildRemindTime(planDate, remindSetting));
        // 如果修改后的提醒时间大于当前时间，则重置act_remind_time字段=null，这样才会再次发送日程提醒
        if (existRemind.getActRemindTime() != null
                && existRemind.getRemindTime() != null
                && existRemind.getRemindTime().after(new Timestamp(DateHelper.currentGMTTime().getTime()))) {
            existRemind.setActRemindTime(null);
        }
        existRemind.setRepeatType(repeatType.getCode());
        existRemind.setDefaultOrder(order);
        existRemind.setShareCount(shareCount);
        existRemind.setShareShortDisplay(shareShortDisplay);
        existRemind.setExpectDayOfMonth(getExceptDayOfMonth(planDate));
        existRemind.setRemindSummary(getRemindSummary(planDate, RemindRepeatType.fromCode(cmd.getRepeatType())));

        List<Remind> trackReminds = remindProvider.findRemindsByTrackRemindIds(Collections.singletonList(existRemind.getId()));
        List<Remind> unSubscribeReminds = new ArrayList<>();
        dbProvider.execute(transactionStatus -> {
            remindProvider.updateRemind(existRemind);
            setRemindRedis(existRemind);
            
            remindProvider.deleteRemindSharesByRemindId(existRemind.getId());
			remindProvider.batchCreateRemindShare(buildRemindShares(cmd.getShareToMembers(), existRemind, historyShareReminds));
            Iterator<Remind> iterator = trackReminds.iterator();
            while (iterator.hasNext()) { 
            	Remind trackRemind = iterator.next();
                OrganizationMemberDetails member = organizationProvider.findOrganizationMemberDetailsByTargetId(trackRemind.getUserId(), trackRemind.getOwnerId());
                if (!shareMemberDTOcontainsShareId(cmd.getShareToMembers(), member.getId())) {
                    //取消共享,删日程发消息
                    remindProvider.deleteRemind(trackRemind);
                    deleteRemindRedis(trackRemind);
                    unSubscribeReminds.add(trackRemind);
                    iterator.remove();
                }
            }
            updateTrackReminds(trackReminds, existRemind, false);
          //修改category也要发消息
            if(existRemind.getRemindCategoryId() != null && existRemind.getRemindCategoryId().equals(oldCategoryId)){
            	List<RemindCategoryDefaultShare> categoryShares = remindProvider.findShareMemberDetailsByCategoryId(existRemind.getRemindCategoryId());
            	if(!CollectionUtils.isEmpty(categoryShares)){
            		for(RemindCategoryDefaultShare cs : categoryShares){
            			if(checkShareRemindInHistory(historyShareReminds, cs)){
    	            		//已经在历史版本里存在的,就不发消息
    	            	}else{
    			            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(cs.getSharedSourceId());
    			            if(null != detail && !detail.getTargetId().equals(0L)){
    				            Remind trackRemind = processSendMsgTrackRemind(existRemind, detail);
    				            trackReminds.add(trackRemind);
    			            }
    	            	}
            		}
            	}
            }
            return null;
        });

        sendTrackMessageOnBackGround(originPlanDescription, trackReminds, RemindModifyType.SETTING_UPDATE);

        return existRemind.getId();
    }
    
    private List<ShareMemberDTO> findRemindShares(Long remindId, Long categoryId) {
		List<ShareMemberDTO> result = new ArrayList<>();
		List<RemindShare> historyShareReminds1 = remindProvider.findShareMemberDetailsByRemindId(remindId);
		if(!CollectionUtils.isEmpty(historyShareReminds1)){
			for(RemindShare rs: historyShareReminds1){
				ShareMemberDTO dto = new ShareMemberDTO();
				dto.setSourceId(rs.getSharedSourceId());
				dto.setSourceType(rs.getSharedSourceType());
				dto.setSourceName(rs.getSharedSourceName());
				result.add(dto);
			}
		}
		if(categoryId != null){
			List<RemindCategoryDefaultShare> historyShareReminds2 = remindProvider.findShareMemberDetailsByCategoryId(categoryId);
			if(!CollectionUtils.isEmpty(historyShareReminds2)){
				for(RemindCategoryDefaultShare rs: historyShareReminds2){
					ShareMemberDTO dto = new ShareMemberDTO();
					dto.setSourceId(rs.getSharedSourceId());
					dto.setSourceType(rs.getSharedSourceType());
					dto.setSourceName(rs.getSharedContractName());
					result.add(dto);
				}
			}
		}
		return result;
	}

	private boolean remindTimeIsToday(Remind remind){
    	return DateStatisticHelper.isSameDay(DateHelper.currentGMTTime(),remind.getRemindTime());
    }
    
    private void deleteRemindRedis(Remind remind){
    	if(remindTimeIsToday(remind)){
    		calendarRemindScheduleJob.cancel(remind.getId());
    	}
    }

    private void deleteRemindRedis(List<Remind> reminds){
    	if(CollectionUtils.isEmpty(reminds))
    		return;
    	for(Remind remind : reminds){
	    	if(remindTimeIsToday(remind)){
	    		calendarRemindScheduleJob.cancel(remind.getId());
	    	}
    	}
    }

    private void setRemindRedis(Remind remind){
    	if(remindTimeIsToday(remind)){
    		if(RemindStatus.fromCode(remind.getStatus()) == RemindStatus.DONE){
    			calendarRemindScheduleJob.cancel(remind.getId());
    		}
    		else{    			
    			calendarRemindScheduleJob.set(remind.getId(), remind.getRemindTime().getTime(), remind);
    		}
    	}
    }
    
    private boolean shareMemberDTOcontainsShareId(List<ShareMemberDTO> shareToMembers, Long id) {
    	if(CollectionUtils.isEmpty(shareToMembers))
    		return false;
        for (ShareMemberDTO dto : shareToMembers) {
            if (dto.getSourceId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteRemind(DeleteRemindCommand cmd) {
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Remind remind = remindProvider.getRemindDetail(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId(), cmd.getId());
        if (remind == null) {
            return;
        }
        List<Remind> trackReminds = remindProvider.findRemindsByTrackRemindIds(Collections.singletonList(remind.getId()));

        dbProvider.execute(transactionStatus -> {
            remindProvider.deleteRemind(remind);
            deleteRemindRedis(remind);
            remindProvider.deleteRemindSharesByRemindId(remind.getId());
            deleteRemindRedis(trackReminds);
            remindProvider.deleteRemindsByTrackRemindId(Collections.singletonList(remind.getId()));
            return null;
        });

    }

    @Override
    public RemindDTO getRemindDetail(GetRemindCommand cmd) {
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        cmd.setUserId(cmd.getUserId() != null ? cmd.getUserId() : UserContext.currentUserId());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Remind remind = remindProvider.getRemindDetail(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getUserId(), cmd.getId());
        if (remind == null || !checkRemindShare(remind)) {
            throw RuntimeErrorException
                    .errorWith(
                            RemindErrorCode.SCOPE,
                            RemindErrorCode.TRACK_REMIND_NOT_EXIST_ERROR,
                            localeStringService.getLocalizedString(
                                    String.valueOf(RemindErrorCode.SCOPE),
                                    String.valueOf(RemindErrorCode.TRACK_REMIND_NOT_EXIST_ERROR),
                                    UserContext.current().getUser().getLocale(),
                                    "The remind is not exist"));
        }
        String shareColour = configurationProvider.getValue(ConfigConstants.REMIND_COLOUR_SHARE, "");
        return convertToRemindDTO(remind, shareColour, true);
    }

    private RemindDTO convertToRemindDTO(Remind remind, String shareColour, boolean queryShares) {
        RemindDTO remindDTO = ConvertHelper.convert(remind, RemindDTO.class);
        if (remind.getPlanDate() != null) {
            remindDTO.setPlanTime(DateStatisticHelper.getDateTimeLong(remind.getPlanDate()));
            //plandate只保留日期部分的时间戳
            remindDTO.setPlanDate(remind.getPlanDate().getTime() - remindDTO.getPlanTime());
        }
        //提醒如果有track对象则去找track对象的提醒设置
        Integer remindSettingId = remind.getRemindTypeId();
        if(remind.getTrackRemindId() != null){
        	Remind trackRemind = remindProvider.getRemindById(remind.getTrackRemindId()); 
        	remindSettingId = trackRemind.getRemindTypeId();
        }
        RemindSetting remindSetting = null;
        if (remindSettingId != null) {
            remindSetting = remindProvider.getRemindSettingById(remindSettingId);
            if (remindSetting.getFixTime() != null) {
                remindDTO.setPlanTime(DateStatisticHelper.getDateTimeLong(remindSetting.getFixTime()));
            }
        }
        remindDTO.setRemindDisplayName(remind.getRemindType());
        if (remind.getRemindTime() != null) {
            remindDTO.setRemindTime(remind.getRemindTime().getTime());
        }
        remindDTO.setOwnerUserId(remind.getUserId());
        remindDTO.setTrackStatus(SubscribeStatus.SUBSCRIBE.getCode());

        if (Long.compare(UserContext.currentUserId(), remind.getUserId()) == 0) {
            // 日程的userId是当前登录人的情况
            if (remind.getTrackRemindId() != null && remind.getTrackRemindId() > 0) {
                // 说明我关注了该日程
                remindDTO.setContactName(remind.getTrackContractName());
                remindDTO.setOwnerUserId(remind.getTrackRemindUserId());
                remindDTO.setTrackStatus(SubscribeStatus.SUBSCRIBE.getCode());
                remindDTO.setDisplayColour(shareColour);
                remindDTO.setDisplayCategoryName(remind.getTrackContractName() + "的日程");
            } else if (remind.getRemindCategoryId() != null && remind.getRemindCategoryId() > 0) {
                RemindCategory remindCategory = remindProvider.getRemindCategory(remind.getNamespaceId(), remind.getOwnerType(), remind.getOwnerId(), remind.getUserId(), remind.getRemindCategoryId());
                if (remindCategory != null) {
                    remindDTO.setDisplayCategoryName(remindCategory.getName());
                    remindDTO.setDisplayColour(remindCategory.getColour());
                    List<RemindCategoryDefaultShare> shares = remindProvider.findShareMemberDetailsByCategoryId(remind.getRemindCategoryId());
                    if(!CollectionUtils.isEmpty(shares)){
                        remindDTO.setCategoryShareMembers(shares.stream().map(this::convertCateogryShareMemberDTO).collect(Collectors.toList()));

                    }
                }
            }
        } else {
            Remind subscribedRemind = remindProvider.getSubscribeRemind(remind.getNamespaceId(), remind.getOwnerType(), remind.getOwnerId(), UserContext.currentUserId(), remind.getId());
            remindDTO.setTrackStatus(subscribedRemind == null ? SubscribeStatus.UN_SUBSCRIBE.getCode() : SubscribeStatus.SUBSCRIBE.getCode());
            remindDTO.setDisplayColour(shareColour);
            remindDTO.setDisplayCategoryName(remind.getContactName() + "的日程");
        }


        if (!queryShares) {
            return remindDTO;
        }

        List<RemindShare> shares = remindProvider.findShareMemberDetailsByRemindId(remind.getId());
        if (CollectionUtils.isEmpty(shares)) {
            remindDTO.setShareShortDisplay("无");
            return remindDTO;
        }

        remindDTO.setShareToMembers(shares.stream().map(this::convertShareMemberDTO).collect(Collectors.toList()));

        String shareShortDisplay = String.format("%s等%d人", remindDTO.getShareToMembers().get(0).getSourceName(), remindDTO.getShareToMembers().size());
        remindDTO.setShareShortDisplay(shareShortDisplay);
        return remindDTO;
    }

	private ShareMemberDTO convertCateogryShareMemberDTO(RemindCategoryDefaultShare r) {
		ShareMemberDTO shareMemberDTO = new ShareMemberDTO();
		shareMemberDTO.setSourceId(r.getSharedSourceId());
		shareMemberDTO.setSourceType(r.getSharedSourceType());
		shareMemberDTO.setSourceName(r.getSharedContractName());
		if (ShareMemberSourceType.MEMBER_DETAIL == ShareMemberSourceType.fromCode(r.getSharedSourceType())) {
		    OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(r.getSharedSourceId());
		    if (detail != null && detail.getTargetId() > 0) {
		        shareMemberDTO.setContactAvatar(getUserAvatar(detail.getTargetId()));
		    }
		}
		return shareMemberDTO;
	}
	private ShareMemberDTO convertShareMemberDTO(RemindShare r) {
		ShareMemberDTO shareMemberDTO = new ShareMemberDTO();
		shareMemberDTO.setSourceId(r.getSharedSourceId());
		shareMemberDTO.setSourceType(r.getSharedSourceType());
		shareMemberDTO.setSourceName(r.getSharedSourceName());
		if (ShareMemberSourceType.MEMBER_DETAIL == ShareMemberSourceType.fromCode(r.getSharedSourceType())) {
		    OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(r.getSharedSourceId());
		    if (detail != null && detail.getTargetId() > 0) {
		        shareMemberDTO.setContactAvatar(getUserAvatar(detail.getTargetId()));
		    }
		}
		return shareMemberDTO;
	}

    @Override
    public void batchSortReminds(BatchSortRemindCommand cmd) {
        if (CollectionUtils.isEmpty(cmd.getSortedRemindIds())) {
            return;
        }
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        QuerySelfRemindsCondition queryCondition = new QuerySelfRemindsCondition();
        queryCondition.setNamespaceId(namespaceId);
        queryCondition.setOwnerType(cmd.getOwnerType());
        queryCondition.setOwnerId(cmd.getOwnerId());
        queryCondition.setUserId(UserContext.currentUserId());
        queryCondition.setStatus(RemindStatus.UNDO.getCode());

        List<Remind> myAllRemindsDefaultOrderDesc = remindProvider.findSelfReminds(queryCondition);

        List<Remind> targetSortedReminds = reSortedReminds(cmd.getSortedRemindIds(), myAllRemindsDefaultOrderDesc);
        if (CollectionUtils.isEmpty(targetSortedReminds)) {
            return;
        }

        this.coordinationProvider.getNamedLock(CoordinationLocks.REMIND_SORTING.getCode() + UserContext.currentUserId()).enter(() -> {
            // defaultOrder  值越小排在越后面，所以倒序循环
            int defaultOrder = 1;
            for (int i = targetSortedReminds.size() - 1; i >= 0; i--) {
                Remind remind = targetSortedReminds.get(i);
                remind.setDefaultOrder(defaultOrder++);
                remindProvider.updateRemind(remind);
                setRemindRedis(remind);
            }
            return null;
        });
    }

    private List<Remind> reSortedReminds(List<Long> targetSortedRemindIds, List<Remind> originSortedReminds) {
        if (CollectionUtils.isEmpty(targetSortedRemindIds) || CollectionUtils.isEmpty(originSortedReminds)) {
            return Collections.emptyList();
        }

        Map<Long, Remind> map = new LinkedHashMap<>();
        originSortedReminds.forEach(remind -> {
            map.put(remind.getId(), remind);
        });

        List<Remind> reSortedReminds = new ArrayList<>(originSortedReminds.size());
        for (int i = 0; i < targetSortedRemindIds.size(); i++) {
            // 按目标顺序存放
            Long remindId = targetSortedRemindIds.get(i);
            if (remindId == null) {
                continue;
            }
            if (map.containsKey(remindId)) {
                reSortedReminds.add(map.remove(remindId));
            }
        }
        // 不在targetSortedRemindIds的数据按原有顺序插入到前面
        if (!map.isEmpty()) {
            reSortedReminds.addAll(0, map.values());
        }
        return reSortedReminds;
    }

    @Override
    public ListRemindResponse listSelfReminds(ListSelfRemindCommand cmd) {
        if (cmd.getPageOffset() == null || cmd.getPageOffset() == 0) {
            cmd.setPageOffset(1);
        }
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());

        createRemindDemo(UserContext.getCurrentNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId());

        QuerySelfRemindsCondition queryCondition = ConvertHelper.convert(cmd, QuerySelfRemindsCondition.class);
        queryCondition.setNamespaceId(UserContext.getCurrentNamespaceId());
        queryCondition.setUserId(UserContext.currentUserId());

        List<Remind> reminds = remindProvider.findSelfReminds(queryCondition);
        if (CollectionUtils.isEmpty(reminds)) {
            return new ListRemindResponse();
        }
        ListRemindResponse response = new ListRemindResponse();
        if (cmd.getPageSize() != null && reminds.size() == cmd.getPageSize()) {
            response.setNextPageOffset(cmd.getPageOffset() + 1);
        }
        String shareColour = configurationProvider.getValue(ConfigConstants.REMIND_COLOUR_SHARE, "");
        List<RemindDTO> dtos = reminds.stream().map(remind -> {
            return convertToRemindDTO(remind, shareColour, false);
        }).collect(Collectors.toList());

        response.setDtos(dtos);
        return response;
    }

    @Override
    public ListRemindResponse listShareReminds(ListShareRemindCommand cmd) {
        if (cmd.getPageOffset() == null || cmd.getPageOffset() == 0) {
            cmd.setPageOffset(1);
        }
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        QueryShareRemindsCondition queryRequest = ConvertHelper.convert(cmd, QueryShareRemindsCondition.class);
        queryRequest.setNamespaceId(UserContext.getCurrentNamespaceId());
        queryRequest.setShareUserId(cmd.getShareUserId());
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(UserContext.currentUserId(), cmd.getOwnerId());
        if (member != null) {
            queryRequest.setCurrentUserDetailId(member.getDetailId());
        }

        List<Remind> reminds = remindProvider.findShareReminds(queryRequest);
        if (CollectionUtils.isEmpty(reminds)) {
            return new ListRemindResponse();
        }
        ListRemindResponse response = new ListRemindResponse();
        if (cmd.getPageOffset() != null && cmd.getPageSize() != null && reminds.size() == cmd.getPageSize()) {
            response.setNextPageOffset(cmd.getPageOffset() + 1);
        }
        String shareColour = configurationProvider.getValue(ConfigConstants.REMIND_COLOUR_SHARE, "");
        List<RemindDTO> dtos = reminds.stream().map(remind -> {
            return convertToRemindDTO(remind, shareColour, false);
        }).collect(Collectors.toList());

        response.setDtos(dtos);
        return response;
    }

    @Override
    public UpdateRemindStatusResponse updateRemindStatus(UpdateRemindStatusCommand cmd) {
        UpdateRemindStatusResponse response = new UpdateRemindStatusResponse();
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Remind remind = remindProvider.getRemindDetail(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId(), cmd.getRemindId());
        if (remind == null) {
            throw RuntimeErrorException
                    .errorWith(
                            RemindErrorCode.SCOPE,
                            RemindErrorCode.TRACK_REMIND_NOT_EXIST_ERROR,
                            localeStringService.getLocalizedString(
                                    String.valueOf(RemindErrorCode.SCOPE),
                                    String.valueOf(RemindErrorCode.TRACK_REMIND_NOT_EXIST_ERROR),
                                    UserContext.current().getUser().getLocale(),
                                    "The remind is not exist"));
        }
        //如果日程状态已经改变了,就直接返回
        if(remind.getStatus().equals(cmd.getStatus())){
        	return response;
        }
        Integer nextOrder = remindProvider.getNextRemindDefaultOrder(namespaceId, remind.getOwnerType(), remind.getOwnerId(), UserContext.currentUserId(), cmd.getStatus());
        Integer order = nextOrder != null ? nextOrder : Integer.valueOf(1);

        RemindRepeatType originRepeatType = RemindRepeatType.fromCode(remind.getRepeatType());
        remind.setStatus(cmd.getStatus());
        remind.setDefaultOrder(order);

        RemindModifyType remindModifyType = RemindModifyType.STATUS_UNDO;
        if (RemindStatus.DONE == RemindStatus.fromCode(cmd.getStatus())) {
            remind.setRepeatType(RemindRepeatType.NONE.getCode());
            remindModifyType = RemindModifyType.STATUS_DONE;
        }

        List<Remind> trackReminds = remindProvider.findRemindsByTrackRemindIds(Collections.singletonList(remind.getId()));

        Remind repeat = dbProvider.execute(transactionStatus -> {
            remindProvider.updateRemind(remind);
            setRemindRedis(remind);
            Remind repeatRemind = createRepeatRemind(remind, originRepeatType);
            updateTrackReminds(trackReminds, remind, true);
            return repeatRemind;
        });

        sendTrackMessageOnBackGround(remind.getPlanDescription(), trackReminds, remindModifyType);

        if (repeat != null) {
            response.setRepeatRemindDTO(convertToRemindDTO(repeat, null, false));
        }
        response.setOriginRemindDTO(convertToRemindDTO(remind, null, false));
        return response;
    }

    private boolean checkRemindShare(Remind remind) {
    	//验证是否为日程所有人
    	if(remind.getUserId().equals(UserContext.currentUserId()))
    		return true;
    	//验证是否为在日程分类共享人
    	if(remind.getRemindCategoryId() != null){
    		List<RemindCategoryDefaultShare> shares = remindProvider.findShareMemberDetailsByCategoryId(remind.getRemindCategoryId());
    		if(!CollectionUtils.isEmpty(shares))
    			for(RemindCategoryDefaultShare share : shares){
    				OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(share.getSharedSourceId());
    	            if (detail != null && detail.getTargetId().equals(UserContext.currentUserId())) {
    	                 return true;
    	            }
    			}
    	}
    	//验证是否为日程共享人
		List<RemindShare> shares = remindProvider.findShareMemberDetailsByRemindId(remind.getId());
		if(!CollectionUtils.isEmpty(shares))
			for(RemindShare share : shares){
				OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(share.getSharedSourceId());
	            if (detail != null && detail.getTargetId().equals(UserContext.currentUserId())) {
	                 return true;
	            }
			} 
		//验证不通过
		return false;
	}

	@Override
    public void subscribeShareRemind(SubscribeShareRemindCommand cmd) {
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Remind subscribedRemind = remindProvider.getSubscribeRemind(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId(), cmd.getRemindId());
        if (subscribedRemind != null) {
            // 已经关注过，不需要操作数据直接返回
            return;
        }

        Remind remind = remindProvider.getRemindDetail(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getRemindUserId(), cmd.getRemindId());
        if (remind == null) {
            throw RuntimeErrorException
                    .errorWith(
                            RemindErrorCode.SCOPE,
                            RemindErrorCode.TRACK_REMIND_NOT_EXIST_ERROR,
                            localeStringService.getLocalizedString(
                                    String.valueOf(RemindErrorCode.SCOPE),
                                    String.valueOf(RemindErrorCode.TRACK_REMIND_NOT_EXIST_ERROR),
                                    UserContext.current().getUser().getLocale(),
                                    "The remind is not exist"));
        }
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(UserContext.currentUserId(), cmd.getOwnerId());

        if (!remindProvider.checkRemindShareToUser(member.getDetailId(), remind.getId(), remind.getRemindCategoryId())) {
            throw RuntimeErrorException
                    .errorWith(
                            RemindErrorCode.SCOPE,
                            RemindErrorCode.REMIND_CANCEL_SHARE_ERROR,
                            localeStringService.getLocalizedString(
                                    String.valueOf(RemindErrorCode.SCOPE),
                                    String.valueOf(RemindErrorCode.REMIND_CANCEL_SHARE_ERROR),
                                    UserContext.current().getUser().getLocale(),
                                    "No permission"));
        }
        createSubscribeRemind(remind, member.getContactName(), UserContext.currentUserId());
    }
    /**创建关注者的日程提醒*/
    private void createSubscribeRemind(Remind remind, String contactName, Long userId) {

        // 创建，编辑，完成，未完成，加入我的日程等操作的日程排在列表最前面
        Integer nextOrder = remindProvider.getNextRemindDefaultOrder(remind.getNamespaceId(), remind.getOwnerType(), remind.getOwnerId(), UserContext.currentUserId(), null);
        Remind subscribe = new Remind();
        subscribe.setNamespaceId(remind.getNamespaceId());
        subscribe.setOwnerType(remind.getOwnerType());
        subscribe.setOwnerId(remind.getOwnerId());
        subscribe.setUserId(userId);
        subscribe.setPlanDescription(remind.getPlanDescription());
        subscribe.setRemindCategoryId(Long.valueOf(0));
        subscribe.setPlanDate(remind.getPlanDate());
        subscribe.setStatus(remind.getStatus());
        subscribe.setShareShortDisplay("无");
        subscribe.setShareCount(0);
        subscribe.setContactName(contactName);
        subscribe.setTrackRemindId(remind.getId());
        subscribe.setTrackRemindUserId(remind.getUserId());
        subscribe.setTrackContractName(remind.getContactName());
        subscribe.setDefaultOrder(nextOrder != null ? nextOrder : Integer.valueOf(1));
        remindProvider.createRemind(subscribe);
        setRemindRedis(subscribe);

    }

    @Override
    public void unSubscribeShareRemind(UnSubscribeShareRemindCommand cmd) {
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Remind remind = remindProvider.getSubscribeRemind(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), UserContext.currentUserId(), cmd.getRemindId());
        if (remind == null) {
            return;
        }
        remindProvider.deleteRemind(remind);
        deleteRemindRedis(remind);
    }

    @Override
    public GetCurrentUserDetailIdResponse getCurrentUserContactSimpleInfo(GetCurrentUserDetailIdCommand cmd) {
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(UserContext.currentUserId(), cmd.getOrganizationId());
        if (detail == null) {
            return null;
        }
        GetCurrentUserDetailIdResponse response = new GetCurrentUserDetailIdResponse();
        response.setUserId(detail.getTargetId());
        response.setDetailId(detail.getId());
        response.setContactName(detail.getContactName());
        response.setContactToken(detail.getContactToken());
        return response;
    }

    private String getContractNameByUserId(Long userId, Long organizationId) {
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, organizationId);
        if (member != null) {
            return member.getContactName();
        }
        return null;
    }

    private void createDefaultCategory(Integer namespaceId, String ownerType, Long ownerId, Long userId) {
        RemindCategory remindCategory = new RemindCategory();
        remindCategory.setNamespaceId(namespaceId);
        remindCategory.setOwnerType(ownerType);
        remindCategory.setOwnerId(ownerId);
        remindCategory.setDefaultOrder(1);
        remindCategory.setUserId(userId);
        remindCategory.setName(DEFAULT_CATEGORY_NAME);
        remindCategory.setShareShortDisplay("无");
        remindProvider.createRemindCategory(remindCategory);
    }

    private void createRemindDemo(Integer namespaceId, String ownerType, Long ownerId, Long userId) {
        boolean isCreated = remindProvider.checkRemindDemoHasCreated(namespaceId, ownerType, ownerId, userId);
        if (isCreated) {
            return;
        }
        List<RemindCategory> categories = getRemindCategories(namespaceId, ownerType, ownerId, userId);
        if (CollectionUtils.isEmpty(categories)) {
            return;
        }
        Remind remind = new Remind();
        remind.setNamespaceId(namespaceId);
        remind.setOwnerType(ownerType);
        remind.setOwnerId(ownerId);
        remind.setUserId(userId);
        remind.setPlanDescription("这是一个日程案例");
        remind.setPlanDate(Timestamp.valueOf("2018-01-01 00:00:00.0"));
        remind.setRemindCategoryId(categories.get(0).getId());
        remind.setDefaultOrder(1);
        remind.setContactName(getContractNameByUserId(userId, ownerId));
        remind.setShareCount(0);
        remind.setShareShortDisplay("无");

        RemindDemoCreateLog remindDemoCreateLog = new RemindDemoCreateLog();
        remindDemoCreateLog.setNamespaceId(namespaceId);
        remindDemoCreateLog.setOwnerType(ownerType);
        remindDemoCreateLog.setOwnerId(ownerId);
        remindDemoCreateLog.setUserId(userId);

        this.coordinationProvider.getNamedLock(CoordinationLocks.REMIND_DEMO_ADD.getCode() + userId).enter(() -> {
            // 获得锁后进行二次判断
            boolean isCreated2 = remindProvider.checkRemindDemoHasCreated(namespaceId, ownerType, ownerId, userId);
            if (!isCreated2) {
                dbProvider.execute(transactionStatus -> {
                    remindProvider.createRemind(remind);
                    setRemindRedis(remind);
                    remindProvider.createRemindDemoCreateLog(remindDemoCreateLog);
                    return null;
                });
            }
            return null;
        });

    }

    public String getUserAvatar(Long userId) {
        User user = userProvider.findUserById(userId);
        if (null != user) {
            return contentServerService.parserUri(user.getAvatar());
        }
        return "";
    }
    /**根据remindSetting 和 planDate 生成提醒时间*/
    private Timestamp buildRemindTime(Timestamp planDate, RemindSetting remindSetting) {
        if (planDate == null || remindSetting == null) {
            return null;
        } 
        Calendar calendar = getPlanDateCalendar(planDate,remindSetting);

        calendar.add(Calendar.DATE, remindSetting.getOffsetDay() * (-1));
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis()- (remindSetting.getBeforeTime() ==null?0:remindSetting.getBeforeTime()));
    }

    private Calendar getPlanDateCalendar(Timestamp planDate, RemindSetting remindSetting) {
    	Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(planDate.getTime());
        if(null != remindSetting.getFixTime()){
        	Calendar setting = Calendar.getInstance();
        	calendar.set(Calendar.HOUR_OF_DAY, setting.get(Calendar.HOUR));
        	calendar.set(Calendar.MINUTE, setting.get(Calendar.MINUTE));
        	calendar.set(Calendar.SECOND, setting.get(Calendar.SECOND));        	
        }
        return calendar;
		
	}

	/**
     * 创建重复的日程(同时创建track自己的重复日程)
     * */
    private Remind createRepeatRemind(Remind originRemind, RemindRepeatType repeatType) {
        if (RemindStatus.DONE != RemindStatus.fromCode(originRemind.getStatus())
                || RemindRepeatType.NONE == repeatType
                || originRemind.getPlanDate() == null) {
            return null;
        }

        // 创建，编辑，完成，未完成，加入我的日程等操作的日程排在列表最前面
        Integer nextOrder = remindProvider.getNextRemindDefaultOrder(originRemind.getNamespaceId(), originRemind.getOwnerType(), originRemind.getOwnerId(), originRemind.getUserId(), RemindStatus.UNDO.getCode());

        RemindSetting remindSetting = null;
        if (originRemind.getRemindTypeId() != null) {
            remindSetting = remindProvider.getRemindSettingById(originRemind.getRemindTypeId());
        }

        Remind repeatRemind = new Remind();
        repeatRemind.setPlanDescription(originRemind.getPlanDescription());
        repeatRemind.setPlanDate(calculateRepeatPlanDate(originRemind.getPlanDate(), repeatType, originRemind.getExpectDayOfMonth()));
        repeatRemind.setRemindTime(buildRemindTime(repeatRemind.getPlanDate(), remindSetting));
        repeatRemind.setRemindSummary(originRemind.getRemindSummary());
        repeatRemind.setExpectDayOfMonth(originRemind.getExpectDayOfMonth());
        repeatRemind.setRepeatType(repeatType.getCode());
        repeatRemind.setShareCount(0);
        repeatRemind.setShareShortDisplay("无");
        repeatRemind.setRemindCategoryId(originRemind.getRemindCategoryId());
        repeatRemind.setContactName(originRemind.getContactName());
        repeatRemind.setDefaultOrder(nextOrder != null ? nextOrder : Integer.valueOf(1));
        repeatRemind.setNamespaceId(originRemind.getNamespaceId());
        repeatRemind.setOwnerType(originRemind.getOwnerType());
        repeatRemind.setOwnerId(originRemind.getOwnerId());
        repeatRemind.setRemindTypeId(originRemind.getRemindTypeId());
        repeatRemind.setRemindType(originRemind.getRemindType());
        repeatRemind.setUserId(originRemind.getUserId());
        repeatRemind.setStatus(RemindStatus.UNDO.getCode());


        List<RemindShare> originRemindShares = remindProvider.findShareMemberDetailsByRemindId(originRemind.getId());

        List<RemindCategoryDefaultShare> shares = remindProvider.findShareMemberDetailsByCategoryId(originRemind.getRemindCategoryId());
        int shareCount = 0;
        if (!CollectionUtils.isEmpty(shares)) {
            String shareShortDisplay = String.format("%s等%d人", shares.get(0).getSharedContractName(), shares.size());
            repeatRemind.setShareShortDisplay(shareShortDisplay);
            shareCount += shares.size();
        }
        List<ShareMemberDTO> shareMembers = new ArrayList<>();
        originRemindShares.forEach(remindShare -> {
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(remindShare.getSharedSourceId());
            if (detail != null) {
                ShareMemberDTO shareMemberDTO = new ShareMemberDTO();
                shareMemberDTO.setSourceType(remindShare.getSharedSourceType());
                shareMemberDTO.setSourceId(remindShare.getSharedSourceId());
                shareMemberDTO.setSourceName(remindShare.getSharedSourceName());
                shareMembers.add(shareMemberDTO);
            }
        });

        if (!CollectionUtils.isEmpty(shareMembers)) {
            String shareShortDisplay = String.format("%s等%d人", shareMembers.get(0).getSourceName(), shareMembers.size());
            repeatRemind.setShareShortDisplay(shareShortDisplay);
            shareCount += shareMembers.size();
        }
        repeatRemind.setShareCount(shareCount);
        dbProvider.execute(transactionStatus -> {
            List<Remind> originSubscribeReminds = remindProvider.findRemindsByTrackRemindIds(Collections.singletonList(originRemind.getId()));
            remindProvider.createRemind(repeatRemind);
            setRemindRedis(repeatRemind);

            remindProvider.batchCreateRemindShare(buildRemindShares(shareMembers, repeatRemind, null, false));
            //origin日程点完成后,origin日程被追踪的日程也会重复一份
            originSubscribeReminds.forEach(remind -> {
                createSubscribeRemind(repeatRemind, remind.getContactName(), remind.getUserId());
            });

            return null;
        });
        return repeatRemind;
    }

    private Timestamp calculateRepeatPlanDate(Timestamp originPlanDate, RemindRepeatType repeatType, Byte expectDayOfMonth) {
        LocalDateTime originPlanDateTime = LocalDateTime.ofInstant(originPlanDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newPlanDateTime = null;
        LocalDateTime nextPlanDateTime1 = originPlanDateTime;   // 下一期日程时间
        boolean isStop = false;
        int count = 0;
        do {
            switch (repeatType) {
                case DAILY:
                    nextPlanDateTime1 = nextPlanDateTime1.plus(1, ChronoUnit.DAYS);
                    break;
                case WEEKLY:
                    nextPlanDateTime1 = nextPlanDateTime1.plus(1, ChronoUnit.WEEKS);
                    break;
                case MONTHLY:
                    // 比如重复日程起初设置的日期是31号，经过没有31日的月份以后，日期会发生变化，校正的目的是有31日的月份应该还是31日，其它月份是最后一天
                    nextPlanDateTime1 = correctExpectDayOfMonth(nextPlanDateTime1.plus(1, ChronoUnit.MONTHS), expectDayOfMonth);
                    break;
                case YEARLY:
                    nextPlanDateTime1 = nextPlanDateTime1.plus(1, ChronoUnit.YEARS);
                    break;
                default:
                    return null;
            }

            if (nextPlanDateTime1.isAfter(now)) {
                newPlanDateTime = nextPlanDateTime1;
                isStop = true;
            } else if (++count >= PLAN_DATE_CALCULATE_MAX_LOOP) {
                // 避免因为一些未预见的场景，导致无限循环
                isStop = true;
            }
        } while (!isStop);


        if (newPlanDateTime == null) {
            return null;
        }

        return new Timestamp(Date.from(newPlanDateTime.atZone(ZoneId.systemDefault()).toInstant()).getTime());
    }

    private LocalDateTime correctExpectDayOfMonth(LocalDateTime nextPlanDateTime, Byte expectDayOfMonth) {
        if (expectDayOfMonth == null) {
            return nextPlanDateTime;
        }
        // 日期矫正
        int lastDayOfThisMonth = nextPlanDateTime.toLocalDate().lengthOfMonth();
        int dayOfMonth = nextPlanDateTime.getDayOfMonth();
        if (lastDayOfThisMonth >= expectDayOfMonth && dayOfMonth < expectDayOfMonth) {
            return nextPlanDateTime.plusDays(expectDayOfMonth - dayOfMonth);
        }
        return nextPlanDateTime;
    }

    private Byte getExceptDayOfMonth(Timestamp planDate) {
        if (planDate == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.ofInstant(planDate.toInstant(), ZoneId.systemDefault());
        return (byte) localDateTime.getDayOfMonth();
    }

    private String getRemindSummary(Timestamp planDate, RemindRepeatType repeatType) {
        if (planDate == null) {
            return "";
        }
        String remindSummary = "";
        LocalDateTime localDateTime = LocalDateTime.ofInstant(planDate.toInstant(), ZoneId.systemDefault());
        switch (repeatType) {
            case DAILY:
                remindSummary = "每天";
                break;
            case WEEKLY:
                remindSummary = WeekDayDisplay.fromCode((byte) localDateTime.getDayOfWeek().getValue()).name();
                break;
            case MONTHLY:
                remindSummary = String.format("每月%d日", localDateTime.getDayOfMonth());
                break;
            case YEARLY:
                remindSummary = String.format("每年%d月%d日", localDateTime.getMonthValue(), localDateTime.getDayOfMonth());
                break;
            default:
                remindSummary = String.format("%d月%d日", localDateTime.getMonthValue(), localDateTime.getDayOfMonth());
        }
        return remindSummary;
    }

    private void sendTrackMessageOnBackGround(String originPlanDescription, List<Remind> trackReminds, RemindModifyType remindModifyType) {
        if (CollectionUtils.isEmpty(trackReminds)) {
            return;
        }
        bgThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                trackReminds.forEach(trackRemind -> {
                    sendTrackMessage(originPlanDescription, trackRemind, remindModifyType);
                });
            }
        });
    }

    private void sendTrackMessage(String originPlanDescription, Remind trackRemind, RemindModifyType modifyType) {
        String content = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("trackContractName", trackRemind.getTrackContractName());
        map.put("planDescription", trackRemind.getPlanDescription());
        boolean isDeleted = false;
        switch (modifyType) {
            case DELETE:
                content = localeTemplateService.getLocaleTemplateString(RemindContants.MSG_SCOPE,
                		RemindContants.MSG_DELETE, RemindContants.LOCALE, map, "");
                isDeleted = true;
                //删除消息不发了  日程提醒1.4
                return;
            case STATUS_UNDO:
                content = localeTemplateService.getLocaleTemplateString(RemindContants.MSG_SCOPE,
                		RemindContants.MSG_STATUS_UNDO, RemindContants.LOCALE, map, "");
                break;
            case STATUS_DONE:
                content = localeTemplateService.getLocaleTemplateString(RemindContants.MSG_SCOPE,
                		RemindContants.MSG_STATUS_DONE, RemindContants.LOCALE, map, "");
                break;
            case UN_SUBSCRIBE:
                content = localeTemplateService.getLocaleTemplateString(RemindContants.MSG_SCOPE,
                		RemindContants.MSG_UN_SUBSCRIBE, RemindContants.LOCALE, map, "");
                isDeleted = true;
                //取消共享的消息不发了 日程提醒1.4
                return;
            case CREATE_SUBSCRIBE:
                content = localeTemplateService.getLocaleTemplateString(RemindContants.MSG_SCOPE,
                		RemindContants.MSG_CREATE_SUBSCRIBE, RemindContants.LOCALE, map, "");
                break;
            default:
                content = localeTemplateService.getLocaleTemplateString(RemindContants.MSG_SCOPE,
                		RemindContants.MSG_SETTING_UPDATE, RemindContants.LOCALE, map, "");
                break;
        }
        sendMessage(trackRemind.getUserId(), content, trackRemind, isDeleted);
    }
    
    @Override
    public void sendRemindMessage(Remind remind) {
    	SimpleDateFormat timeDF = new SimpleDateFormat("HH:mm");
    	RemindSetting remindSetting = null;
    	//兼容5.9.0之前的 需要取remindSetting 的时间
    	remindSetting = remindProvider.getRemindSettingById(remind.getRemindTypeId());
    	Calendar  calendar = getPlanDateCalendar(remind.getPlanDate(), remindSetting);
        String content = String.format("%s，%s %s", remind.getPlanDescription(), remind.getRemindSummary(), timeDF.format(calendar.getTime()));
        sendMessage(remind.getUserId(), content, remind, false);
    }

    private void sendMessage(Long receiveUserId, String content, Remind remind, boolean isDeleted) {
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content.toString());
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiveUserId)));

        //  set the route
        if (!isDeleted) {
            String url = null;
            if (remind.getTrackRemindId() != null && remind.getTrackRemindId() > 0) {
                TrackRemindDetailActionData actionData = new TrackRemindDetailActionData();
                actionData.setOrganizationId(remind.getOwnerId());
                actionData.setRemindId(remind.getTrackRemindId());
                actionData.setRemindUserId(remind.getTrackRemindUserId());
                url = RouterBuilder.build(Router.SHARED_CALENDAR_REMIND_DETAIL, actionData);
            } else {
                SelfRemindDetailActionData actionData = new SelfRemindDetailActionData();
                actionData.setOrganizationId(remind.getOwnerId());
                actionData.setRemindId(remind.getId());
                url = RouterBuilder.build(Router.SELF_CALENDAR_REMIND_DETAIL, actionData);
            }
            RouterMetaObject metaObject = new RouterMetaObject();
            metaObject.setUrl(url);
            Map<String, String> meta = new HashMap<>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
            meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
            meta.put(MessageMetaConstant.MESSAGE_SUBJECT, localeStringService.getLocalizedString(RemindContants.MSG_SCOPE, 
            		RemindContants.MSG_SUBJECT, RemindContants.LOCALE, "日程提醒"));
            message.setMeta(meta);
        }

        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(receiveUserId),
                message,
                MessagingConstants.MSG_FLAG_STORED_PUSH.getCode()
        );
    }

    private void updateTrackReminds(List<Remind> trackReminds, Remind originRemind, boolean isStatusChanged) {
        if (!CollectionUtils.isEmpty(trackReminds)) {
            trackReminds.forEach(trackRemind -> {
                trackRemind.setPlanDate(originRemind.getPlanDate());
                trackRemind.setPlanDescription(originRemind.getPlanDescription());
                trackRemind.setStatus(originRemind.getStatus());
                if (isStatusChanged) {
                    Integer nextOrder = remindProvider.getNextRemindDefaultOrder(trackRemind.getNamespaceId(), trackRemind.getOwnerType(), trackRemind.getOwnerId(), trackRemind.getUserId(), originRemind.getStatus());
                    trackRemind.setDefaultOrder(nextOrder != null ? nextOrder : Integer.valueOf(1));
                }
                remindProvider.updateRemind(trackRemind);
                setRemindRedis(trackRemind);
            });
        }
    }

}
