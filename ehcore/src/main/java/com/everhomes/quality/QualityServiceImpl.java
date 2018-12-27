package com.everhomes.quality;

import com.everhomes.acl.AclProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.equipment.EquipmentService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationJobPosition;
import com.everhomes.organization.OrganizationJobPositionMap;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.pmNotify.PmNotifyRecord;
import com.everhomes.pmNotify.PmNotifyService;
import com.everhomes.portal.PortalService;
import com.everhomes.rentalv2.Rentalv2ServiceImpl;
import com.everhomes.repeat.RepeatService;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.equipment.EquipmentServiceErrorCode;
import com.everhomes.rest.equipment.OfflineEquipmentTaskReportLog;
import com.everhomes.rest.equipment.ReviewResult;
import com.everhomes.rest.equipment.Status;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.module.CheckModuleManageCommand;
import com.everhomes.rest.organization.ListOrganizationContactByJobPositionIdCommand;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.pmNotify.DeletePmNotifyParamsCommand;
import com.everhomes.rest.pmNotify.ListPmNotifyParamsCommand;
import com.everhomes.rest.pmNotify.PmNotifyParamDTO;
import com.everhomes.rest.pmNotify.PmNotifyReceiver;
import com.everhomes.rest.pmNotify.PmNotifyReceiverDTO;
import com.everhomes.rest.pmNotify.PmNotifyReceiverList;
import com.everhomes.rest.pmNotify.PmNotifyType;
import com.everhomes.rest.pmNotify.ReceiverName;
import com.everhomes.rest.pmNotify.SetPmNotifyParamsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.quality.BatchUpdateQualitySpecificationsCommand;
import com.everhomes.rest.quality.CountSampleTaskCommunityScoresCommand;
import com.everhomes.rest.quality.CountSampleTaskScoresCommand;
import com.everhomes.rest.quality.CountSampleTaskScoresResponse;
import com.everhomes.rest.quality.CountSampleTaskSpecificationItemScoresCommand;
import com.everhomes.rest.quality.CountSampleTaskSpecificationItemScoresResponse;
import com.everhomes.rest.quality.CountSampleTasksCommand;
import com.everhomes.rest.quality.CountSampleTasksResponse;
import com.everhomes.rest.quality.CountScoresCommand;
import com.everhomes.rest.quality.CountScoresResponse;
import com.everhomes.rest.quality.CountScoresSpecificationDTO;
import com.everhomes.rest.quality.CountTasksCommand;
import com.everhomes.rest.quality.CountTasksResponse;
import com.everhomes.rest.quality.CreatQualityStandardCommand;
import com.everhomes.rest.quality.CreateQualityInspectionTaskCommand;
import com.everhomes.rest.quality.CreateQualitySpecificationCommand;
import com.everhomes.rest.quality.CreateSampleQualityInspectionCommand;
import com.everhomes.rest.quality.CurrentUserInfoDTO;
import com.everhomes.rest.quality.DeleteFactorCommand;
import com.everhomes.rest.quality.DeleteQualityCategoryCommand;
import com.everhomes.rest.quality.DeleteQualitySpecificationCommand;
import com.everhomes.rest.quality.DeleteQualityStandardCommand;
import com.everhomes.rest.quality.DeleteUserQualityInspectionTaskTemplateCommand;
import com.everhomes.rest.quality.EvaluationDTO;
import com.everhomes.rest.quality.ExecuteGroupAndPosition;
import com.everhomes.rest.quality.FactorsDTO;
import com.everhomes.rest.quality.FindQualityInspectionTaskCommand;
import com.everhomes.rest.quality.FindSampleQualityInspectionCommand;
import com.everhomes.rest.quality.GetQualitySpecificationCommand;
import com.everhomes.rest.quality.GroupUserDTO;
import com.everhomes.rest.quality.ListEvaluationsCommand;
import com.everhomes.rest.quality.ListEvaluationsResponse;
import com.everhomes.rest.quality.ListFactorsCommand;
import com.everhomes.rest.quality.ListFactorsResponse;
import com.everhomes.rest.quality.ListQualityCategoriesCommand;
import com.everhomes.rest.quality.ListQualityCategoriesResponse;
import com.everhomes.rest.quality.ListQualityInspectionLogsCommand;
import com.everhomes.rest.quality.ListQualityInspectionLogsResponse;
import com.everhomes.rest.quality.ListQualityInspectionTasksCommand;
import com.everhomes.rest.quality.ListQualityInspectionTasksResponse;
import com.everhomes.rest.quality.ListQualitySpecificationsCommand;
import com.everhomes.rest.quality.ListQualitySpecificationsResponse;
import com.everhomes.rest.quality.ListQualityStandardsCommand;
import com.everhomes.rest.quality.ListQualityStandardsResponse;
import com.everhomes.rest.quality.ListRecordsByTaskIdCommand;
import com.everhomes.rest.quality.ListSampleQualityInspectionCommand;
import com.everhomes.rest.quality.ListSampleQualityInspectionResponse;
import com.everhomes.rest.quality.ListSampleQualityInspectionTasksCommand;
import com.everhomes.rest.quality.ListUserHistoryTasksCommand;
import com.everhomes.rest.quality.ListUserQualityInspectionTaskTemplatesCommand;
import com.everhomes.rest.quality.OfflineDeleteTablesInfo;
import com.everhomes.rest.quality.OfflineJobPositionDTO;
import com.everhomes.rest.quality.OfflineQualityTaskCommand;
import com.everhomes.rest.quality.OfflineReportDetailDTO;
import com.everhomes.rest.quality.OfflineSampleQualityInspectionResponse;
import com.everhomes.rest.quality.OfflineTaskCount;
import com.everhomes.rest.quality.OfflineTaskReportCommand;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.rest.quality.ProcessType;
import com.everhomes.rest.quality.QualityCategoriesDTO;
import com.everhomes.rest.quality.QualityGroupType;
import com.everhomes.rest.quality.QualityInspectionCategoryStatus;
import com.everhomes.rest.quality.QualityInspectionLogDTO;
import com.everhomes.rest.quality.QualityInspectionLogProcessType;
import com.everhomes.rest.quality.QualityInspectionLogType;
import com.everhomes.rest.quality.QualityInspectionSpecificationDTO;
import com.everhomes.rest.quality.QualityInspectionSpecificationItemResultsDTO;
import com.everhomes.rest.quality.QualityInspectionStandardGroupMapDTO;
import com.everhomes.rest.quality.QualityInspectionTaskAttachmentDTO;
import com.everhomes.rest.quality.QualityInspectionTaskDTO;
import com.everhomes.rest.quality.QualityInspectionTaskRecordsDTO;
import com.everhomes.rest.quality.QualityInspectionTaskResult;
import com.everhomes.rest.quality.QualityInspectionTaskReviewResult;
import com.everhomes.rest.quality.QualityInspectionTaskStatus;
import com.everhomes.rest.quality.QualityNotificationTemplateCode;
import com.everhomes.rest.quality.QualityOfflineTaskDetailsResponse;
import com.everhomes.rest.quality.QualityOfflineTaskReportResponse;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.quality.QualityStandardStatus;
import com.everhomes.rest.quality.QualityStandardsDTO;
import com.everhomes.rest.quality.QualityTaskType;
import com.everhomes.rest.quality.ReportRectifyResultCommand;
import com.everhomes.rest.quality.ReportSpecificationItemResultsDTO;
import com.everhomes.rest.quality.ReportVerificationResultCommand;
import com.everhomes.rest.quality.ReviewReviewQualityStandardCommand;
import com.everhomes.rest.quality.ReviewVerificationResultCommand;
import com.everhomes.rest.quality.SampleCommunity;
import com.everhomes.rest.quality.SampleCommunitySpecification;
import com.everhomes.rest.quality.SampleGroupDTO;
import com.everhomes.rest.quality.SampleQualityInspectionDTO;
import com.everhomes.rest.quality.SampleTaskScoreDTO;
import com.everhomes.rest.quality.ScoreDTO;
import com.everhomes.rest.quality.ScoreGroupByTargetDTO;
import com.everhomes.rest.quality.SearchQualityTasksCommand;
import com.everhomes.rest.quality.SpecificationApplyPolicy;
import com.everhomes.rest.quality.SpecificationInspectionType;
import com.everhomes.rest.quality.SpecificationItemScores;
import com.everhomes.rest.quality.SpecificationScopeCode;
import com.everhomes.rest.quality.StandardGroupDTO;
import com.everhomes.rest.quality.TaskCountDTO;
import com.everhomes.rest.quality.UpdateFactorCommand;
import com.everhomes.rest.quality.UpdateQualityCategoryCommand;
import com.everhomes.rest.quality.UpdateQualitySpecificationCommand;
import com.everhomes.rest.quality.UpdateQualityStandardCommand;
import com.everhomes.rest.quality.UpdateSampleQualityInspectionCommand;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.rest.repeat.TimeRangeDTO;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.search.QualityInspectionSampleSearcher;
import com.everhomes.search.QualityTaskSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.mysql.jdbc.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class QualityServiceImpl implements QualityService {

	final String downloadDir ="\\download\\";

	private static final Logger LOGGER = LoggerFactory.getLogger(QualityServiceImpl.class);

	DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private QualityProvider qualityProvider;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private LocaleStringService localeStringService;

	@Autowired
	private RepeatService repeatService;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private LocaleTemplateService localeTemplateService;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private AclProvider aclProvider;

	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;

	@Autowired
	private QualityInspectionSampleSearcher sampleSearcher;

	@Autowired
	private QualityTaskSearcher taskSearcher;

	@Autowired
	private ConfigurationProvider configProvider;

	@Autowired
	private PortalService portalService;

	@Autowired
	private ServiceModuleService serviceModuleService;

	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private PmNotifyService pmNotifyService;

	@Override
	public QualityStandardsDTO creatQualityStandard(CreatQualityStandardCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_STANDARD_CREATE, cmd.getTargetId());

		User user = UserContext.current().getUser();
		RepeatSettings repeat = null;
		if(cmd.getRepeat() !=null) {
			repeat = ConvertHelper.convert(cmd.getRepeat(), RepeatSettings.class);
			if(cmd.getRepeat().getStartDate() != null)
				repeat.setStartDate(new Date(cmd.getRepeat().getStartDate()));
			if(cmd.getRepeat().getEndDate() != null)
				repeat.setEndDate(new Date(cmd.getRepeat().getEndDate()));

			repeat.setCreatorUid(user.getId());
			repeatService.createRepeatSettings(repeat);
		}

		QualityInspectionStandards standard = new QualityInspectionStandards();
		standard.setOwnerType(cmd.getOwnerType());
		standard.setOwnerId(cmd.getOwnerId());
		standard.setStandardNumber(cmd.getStandardNumber());
		standard.setName(cmd.getName());
		standard.setDescription(cmd.getDescription());
		standard.setCreatorUid(user.getId());
		standard.setOperatorUid(user.getId());
		standard.setNamespaceId(cmd.getNamespaceId());
		if(repeat == null) {
			standard.setRepeatSettingId(0L);
		} else {
			standard.setRepeatSettingId(repeat.getId());
		}
//		if(cmd.getTargetId() != null && cmd.getTargetType() != null) {
			standard.setTargetId(cmd.getTargetId());
			standard.setTargetType(cmd.getTargetType());

			qualityProvider.createQualityInspectionStandards(standard);
			createQualityInspectionStandardLogs(standard, QualityInspectionLogProcessType.INSERT.getCode(),
					user.getId(),cmd.getNamespaceId());

			List<StandardGroupDTO> groupList = cmd.getGroup();
			processStandardGroups(groupList, standard);
			processRepeatSetting(standard);
			processStandardSpecification(standard, cmd.getSpecificationIds(),cmd.getNamespaceId());

			QualityStandardsDTO dto = ConvertHelper.convert(standard, QualityStandardsDTO.class);
			convertSpecificationToDTO(standard, dto);
			return dto;
	}

	private void createQualityInspectionStandardLogs(QualityInspectionStandards standard, Byte processType,
													 Long userId,Integer namespaceId) {

		QualityInspectionLogs log = new QualityInspectionLogs();
		//log.setNamespaceId(UserContext.getCurrentNamespaceId());
		log.setNamespaceId(namespaceId);
		log.setOwnerType(standard.getOwnerType());
		log.setScopeId(standard.getTargetId());
		log.setOwnerId(standard.getOwnerId());
		log.setTargetType(QualityInspectionLogType.STANDARD.getCode());
		log.setTargetId(standard.getId());
		log.setProcessType(processType);
		log.setOperatorUid(userId);
		qualityProvider.createQualityInspectionLogs(log);
	}

	@Override
	public QualityStandardsDTO updateQualityStandard(UpdateQualityStandardCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_STANDARD_UPDATE, cmd.getTargetId());

		User user = UserContext.current().getUser();
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("updateQualityStandard: userId = " + user.getId() + "time = " + DateHelper.currentGMTTime()
					+ "UpdateQualityStandardCommand cmd = {}" + cmd);
		}

		QualityInspectionStandards standard = verifiedStandardById(cmd.getId());
		//standard.setNamespaceId(user.getNamespaceId());
		standard.setNamespaceId(cmd.getNamespaceId());
		standard.setOwnerId(cmd.getOwnerId());
		standard.setOwnerType(cmd.getOwnerType());
		standard.setTargetId(cmd.getTargetId());
		standard.setTargetType(cmd.getTargetType());
		standard.setName(cmd.getName());
		standard.setStandardNumber(cmd.getStandardNumber());
		standard.setDescription(cmd.getDescription());
		standard.setOperatorUid(user.getId());
		//要求重新审批
		standard.setReviewerUid(0L);
		standard.setReviewResult(ReviewResult.NONE.getCode());
		standard.setStatus(QualityStandardStatus.WAITING.getCode());
		updateStandardRepeatSetting(standard, cmd.getRepeat());
		qualityProvider.updateQualityInspectionStandards(standard);

		createQualityInspectionStandardLogs(standard, QualityInspectionLogProcessType.UPDATE.getCode(),
				user.getId(), cmd.getNamespaceId());

		List<StandardGroupDTO> groupList = cmd.getGroup();
		processStandardGroups(groupList, standard);
		processRepeatSetting(standard);
		processStandardSpecification(standard, cmd.getSpecificationIds(), cmd.getNamespaceId());
		QualityStandardsDTO dto = ConvertHelper.convert(standard, QualityStandardsDTO.class);
		convertSpecificationToDTO(standard, dto);
		return dto;
	}

	private void updateStandardRepeatSetting(QualityInspectionStandards standard, RepeatSettingsDTO repeat) {
		repeatService.deleteRepeatSettingsById(standard.getRepeatSettingId());
		RepeatSettings repeatSetting = ConvertHelper.convert(repeat, RepeatSettings.class);
		repeatService.createRepeatSettings(repeatSetting);
		standard.setRepeatSettingId(repeatSetting.getId());
	}

	private void convertSpecificationToDTO(QualityInspectionStandards standard, QualityStandardsDTO dto) {
		List<QualityInspectionSpecificationDTO> specifications = new ArrayList<QualityInspectionSpecificationDTO>();
		if(standard.getSpecifications() != null && standard.getSpecifications().size() > 0) {
			for(QualityInspectionSpecifications specification : standard.getSpecifications()) {
				if(specification != null) {
					if(!StringUtils.isNullOrEmpty(specification.getPath())) {
						String path = getSpecificationNamePath(specification.getPath(), specification.getOwnerType(), specification.getOwnerId());
						specification.setPath(path);
					}

					specifications.add(ConvertHelper.convert(specification, QualityInspectionSpecificationDTO.class));
				}
			}
			dto.setSpecifications(specifications);
		}
	}

	private void processStandardSpecification(QualityInspectionStandards standard, List<Long> specificationIds, Integer namespaceId) {
		List<QualityInspectionSpecifications> specifications = new ArrayList<QualityInspectionSpecifications>();
		qualityProvider.deleteQualityInspectionStandardSpecificationMapByStandardId(standard.getId());

		if(specificationIds != null && specificationIds.size() > 0) {
			User user = UserContext.current().getUser();
//			Integer namespaceId = UserContext.current().getNamespaceId();
			 for(Long specificationId : specificationIds) {
				 QualityInspectionStandardSpecificationMap map = new QualityInspectionStandardSpecificationMap();
				  map.setStandardId(standard.getId());
				  map.setSpecificationId(specificationId);
				  map.setCreatorUid(user.getId());
				  map.setNamespaceId(namespaceId);
				  qualityProvider.createQualityInspectionStandardSpecificationMap(map);

				  QualityInspectionSpecifications specification =
						  qualityProvider.findSpecificationById(specificationId, standard.getOwnerType(), standard.getOwnerId());
				  specifications.add(specification);
			 }

			 standard.setSpecifications(specifications);
		}
	}

	private void processRepeatSetting(QualityInspectionStandards standard) {
		RepeatSettings repeat = repeatService.findRepeatSettingById(standard.getRepeatSettingId());
		standard.setRepeat(repeat);
	}
	private void processStandardGroups(List<StandardGroupDTO> groupList, QualityInspectionStandards standard) {

        List<QualityInspectionStandardGroupMap> executiveGroup = null;
		List<QualityInspectionStandardGroupMap> reviewGroup = null;
        this.qualityProvider.deleteQualityInspectionStandardGroupMapByStandardId(standard.getId());

        if(groupList != null && groupList.size() >0) {
        	executiveGroup = new ArrayList<>();
    		reviewGroup = new ArrayList<>();

			for (StandardGroupDTO group : groupList) {
				QualityInspectionStandardGroupMap map = new QualityInspectionStandardGroupMap();
				map.setStandardId(standard.getId());
				map.setGroupType(group.getGroupType());
				map.setGroupId(group.getGroupId());
				map.setPositionId(group.getPositionId());
				if (group.getInspectorUid() != null) {
					map.setInspectorUid(group.getInspectorUid());
					if (null == map.getGroupId()) {
						List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(group.getInspectorUid());
						if (members != null && members.size() > 0) {
							map.setGroupId(members.get(0).getOrganizationId());
						}
					}

				}
				qualityProvider.createQualityInspectionStandardGroupMap(map);
				if (QualityGroupType.EXECUTIVE_GROUP.equals(QualityGroupType.fromStatus(map.getGroupType()))) {
					executiveGroup.add(map);
				}
				if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(map.getGroupType()))) {
					reviewGroup.add(map);
				}
			}

			standard.setExecutiveGroup(executiveGroup);
			standard.setReviewGroup(reviewGroup);
		}
	}


	private QualityInspectionStandards verifiedStandardById(Long id) {
		QualityInspectionStandards standard = qualityProvider.findStandardById(id);
//		if(standard == null || standard.getStatus() == null || standard.getStatus() == QualityStandardStatus.INACTIVE.getCode()) {
		//fix bug 1123  #15610
		if(standard == null || standard.getStatus() == null) {
			LOGGER.error("the standard which id="+id+" don't exist!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_STANDARD_NOT_EXIST,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_STANDARD_NOT_EXIST),
									UserContext.current().getUser().getLocale(),
									"the standard don't exist!"));
		}
		return standard;
	}

	@Override
	public void deleteQualityStandard(DeleteQualityStandardCommand cmd) {

		User user = UserContext.current().getUser();
		QualityInspectionStandards standard = verifiedStandardById(cmd.getStandardId());

		checkUserPrivilege(standard.getOwnerId(), PrivilegeConstants.QUALITY_STANDARD_DELETE, standard.getTargetId());

		standard.setStatus(QualityStandardStatus.INACTIVE.getCode());
		standard.setOperatorUid(user.getId());
		standard.setDeleterUid(user.getId());
		standard.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		qualityProvider.updateQualityInspectionStandards(standard);

		createQualityInspectionStandardLogs(standard, QualityInspectionLogProcessType.DELETE.getCode(),
				user.getId(), cmd.getNamespaceId());

	}

	@Override
	public ListQualityStandardsResponse listQualityStandards(ListQualityStandardsCommand cmd) {
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_STANDARD_LIST, cmd.getTargetId());

		Long ownerId = cmd.getOwnerId();
		String ownerType = cmd.getOwnerType();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<QualityInspectionStandards> standards = new ArrayList<>();
		if (cmd.getTargetId() != null && cmd.getTargetId() != 0L) {
			standards = qualityProvider.listQualityInspectionStandards(locator, pageSize + 1,
					ownerId, ownerType, cmd.getTargetType(), Collections.singletonList(cmd.getTargetId()), cmd.getReviewResult(),cmd.getPlanCondition());
		} else if(cmd.getTargetIds()!=null && cmd.getTargetIds().size()>0){
			standards = qualityProvider.listQualityInspectionStandards(locator, pageSize + 1,
					ownerId, ownerType, null, cmd.getTargetIds(), cmd.getReviewResult(),cmd.getPlanCondition());
		}

		this.qualityProvider.populateStandardsGroups(standards);
		this.qualityProvider.populateStandardsSpecifications(standards);
        Long nextPageAnchor = null;
        if(standards.size() > pageSize) {
        	standards.remove(standards.size() - 1);
            nextPageAnchor = standards.get(standards.size() - 1).getId();
        }


        List<QualityStandardsDTO> qaStandards = standards.stream().map((r) -> {

			QualityStandardsDTO dto = converStandardToDto(r);
//			if (r.getTargetId() == 0) {
//				dto.setCommunities(qualityProvider.listQualityModelCommunityIdsMapByModelId(r.getId(),QualityModelType.STANDARD.getCode()));
//			}

        	return dto;
        }).collect(Collectors.toList());


        return new ListQualityStandardsResponse(nextPageAnchor, qaStandards);
	}

	@Override
	public void updateQualityCategory(UpdateQualityCategoryCommand cmd) {
		/*Long privilegeId = configProvider.getLongValue(QualityConstant.QUALITY_CATEGORY_UPDATE, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);*/

		//checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_CATEGORY_UPDATE, null);

		User user = UserContext.current().getUser();

		if(cmd.getId() == null) {
			QualityInspectionCategories category = new QualityInspectionCategories();
			category.setName(cmd.getName());
			category.setOwnerType(cmd.getOwnerType());
			category.setOwnerId(cmd.getOwnerId());
			category.setNamespaceId(user.getNamespaceId());//暂时不用的api
			category.setStatus(QualityInspectionCategoryStatus.ACTIVE.getCode());
			category.setCreatorUid(user.getId());
			if(cmd.getParentId() != null) {
				QualityInspectionCategories parent = verifiedCategoryById(cmd.getParentId());
				category.setParentId(cmd.getParentId());
				category.setPath(parent.getPath()+"/");
			} else {
				category.setPath("/");
			}

			if(cmd.getScore() != null)
				category.setScore(cmd.getScore());

			if(cmd.getDescription() != null)
				category.setDescription(cmd.getDescription());

			qualityProvider.createQualityInspectionCategories(category);
		} else {
			QualityInspectionCategories category = verifiedCategoryById(cmd.getId());
			category.setName(cmd.getName());
			category.setNamespaceId(user.getNamespaceId());
			category.setOwnerType(cmd.getOwnerType());
			category.setOwnerId(cmd.getOwnerId());
			if(cmd.getParentId() != null) {
				QualityInspectionCategories parent = verifiedCategoryById(cmd.getParentId());
				category.setParentId(cmd.getParentId());
				category.setPath(parent.getPath()+"/" + category.getId());
			} else {
				category.setPath("/" + category.getId());
			}

			if(cmd.getScore() != null)
				category.setScore(cmd.getScore());

			if(cmd.getDescription() != null)
				category.setDescription(cmd.getDescription());

			qualityProvider.updateQualityInspectionCategories(category);
		}

	}

	@Override
	public void deleteQualityCategory(DeleteQualityCategoryCommand cmd) {
		QualityInspectionCategories category = verifiedCategoryById(cmd.getCategoryId());

		/*Long privilegeId = configProvider.getLongValue(QualityConstant.QUALITY_CATEGORY_DELETE, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(null, null, category.getOwnerId(), privilegeId);*/
		//checkUserPrivilege(category.getOwnerId(), PrivilegeConstants.QUALITY_CATEGORY_DELETE, null);

		List<QualityInspectionStandards> standards = qualityProvider.findStandardsByCategoryId(cmd.getCategoryId());
		if(standards != null && standards.size() > 0) {
			LOGGER.error("the category which id="+cmd.getCategoryId()+" has active standard!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_CATEGORY_HAS_STANDARD,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_CATEGORY_HAS_STANDARD),
									UserContext.current().getUser().getLocale(),
									"the category has active standard!"));
		}
		category.setStatus(QualityInspectionCategoryStatus.DISABLED.getCode());
		qualityProvider.updateQualityInspectionCategories(category);
	}

	private QualityInspectionCategories verifiedCategoryById(Long categoryId) {
		QualityInspectionCategories category = qualityProvider.findQualityInspectionCategoriesByCategoryId(categoryId);
		if(category == null) {
			LOGGER.error("the category which id="+categoryId+" don't exist!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_CATEGORY_NOT_EXIST,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_CATEGORY_NOT_EXIST),
									UserContext.current().getUser().getLocale(),
									"the category don't exist!"));
		}
		return category;
	}

	@Override
	public ListFactorsResponse listFactors(ListFactorsCommand cmd) {
		Long ownerId = cmd.getOwnerId();
		String ownerType = cmd.getOwnerType();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<QualityInspectionEvaluationFactors> factors = qualityProvider.listQualityInspectionEvaluationFactors(locator, pageSize+1, ownerId, ownerType);

        Long nextPageAnchor = null;
        if(factors.size() > pageSize) {
        	factors.remove(factors.size() - 1);
            nextPageAnchor = factors.get(factors.size() - 1).getId();
        }

        List<FactorsDTO> factorsDto = factors.stream().map((r) -> {

        	FactorsDTO dto = ConvertHelper.convert(r, FactorsDTO.class);
        	Organization org = organizationProvider.findOrganizationById(r.getGroupId());
        	if(org != null) {
        		dto.setGroupName(org.getName());
        	}
//        	QualityInspectionCategories category = verifiedCategoryById(r.getCategoryId());
        	QualityInspectionCategories category = qualityProvider.findQualityInspectionCategoriesByCategoryId(r.getCategoryId());
		    if(category != null) {
		    	dto.setCategoryName(category.getName());
		    }
        	return dto;
        }).collect(Collectors.toList());


        return new ListFactorsResponse(nextPageAnchor, factorsDto);
	}

	@Override
	public void updateFactor(UpdateFactorCommand cmd) {

		User user = UserContext.current().getUser();

		if(cmd.getId() == null) {
			QualityInspectionEvaluationFactors factor = new QualityInspectionEvaluationFactors();
			factor.setNamespaceId(user.getNamespaceId());
			factor.setOwnerId(cmd.getOwnerId());
			factor.setOwnerType(cmd.getOwnerType());
			factor.setCategoryId(cmd.getCategoryId());
			factor.setGroupId(cmd.getGroupId());
			factor.setWeight(cmd.getWeight());
			factor.setCreatorUid(user.getId());


			qualityProvider.createQualityInspectionEvaluationFactors(factor);
		} else {
			QualityInspectionEvaluationFactors factor = verifiedFactorById(cmd.getId());
			factor.setNamespaceId(user.getNamespaceId());
			factor.setOwnerId(cmd.getOwnerId());
			factor.setOwnerType(cmd.getOwnerType());
			factor.setCategoryId(cmd.getCategoryId());
			factor.setGroupId(cmd.getGroupId());
			factor.setWeight(cmd.getWeight());

			qualityProvider.updateQualityInspectionEvaluationFactors(factor);
		}

	}

	private QualityInspectionEvaluationFactors verifiedFactorById(Long factorId) {
		QualityInspectionEvaluationFactors factor = qualityProvider.findQualityInspectionFactorById(factorId);
		if(factor == null) {
			LOGGER.error("the factor which id="+factorId+" don't exist!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_FACTOR_NOT_EXIST,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_FACTOR_NOT_EXIST),
									UserContext.current().getUser().getLocale(),
									"the factor don't exist!"));
		}
		return factor;
	}

	@Override
	public void deleteFactor(DeleteFactorCommand cmd) {
		qualityProvider.deleteQualityInspectionEvaluationFactors(cmd.getId());

	}

	@Override
	public ListEvaluationsResponse listEvaluations(ListEvaluationsCommand cmd) {
		Long ownerId = cmd.getOwnerId();
		String ownerType = cmd.getOwnerType();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        Timestamp startTime = null;
        Timestamp endTime = null;
        if(cmd.getStartTime() != null) {
        	startTime = new Timestamp(cmd.getStartTime());
        }
        if(cmd.getEndTime() != null) {
        	endTime = new Timestamp(cmd.getEndTime());
        }

        List<QualityInspectionEvaluations> evaluations = qualityProvider.listQualityInspectionEvaluations(locator, pageSize + 1,
        		ownerId, ownerType, startTime, endTime);


        Long nextPageAnchor = null;
        if(evaluations.size() > pageSize) {
        	evaluations.remove(evaluations.size() - 1);
            nextPageAnchor = evaluations.get(evaluations.size() - 1).getId();
        }


        List<EvaluationDTO> dtoList = evaluations.stream().map((r) -> {

        	EvaluationDTO dto = ConvertHelper.convert(r, EvaluationDTO.class);

        	return dto;
        }).collect(Collectors.toList());


        return new ListEvaluationsResponse(nextPageAnchor, dtoList);
	}

	@Override
	public HttpServletResponse exportEvaluations(ListEvaluationsCommand cmd,
			HttpServletResponse response) {

		Timestamp startTime = null;
        Timestamp endTime = null;
        if(cmd.getStartTime() != null) {
        	startTime = new Timestamp(cmd.getStartTime());
        }
        if(cmd.getEndTime() != null) {
        	endTime = new Timestamp(cmd.getEndTime());
        }
		int totalCount = qualityProvider.countInspectionEvaluations(cmd.getOwnerId(), cmd.getOwnerType(), startTime, endTime);
		if (totalCount == 0)
			return response;

		CrossShardListingLocator locator = new CrossShardListingLocator();
		Integer pageSize = Integer.MAX_VALUE;
		List<QualityInspectionEvaluations> evaluations = qualityProvider.listQualityInspectionEvaluations(locator, pageSize,
				cmd.getOwnerId(), cmd.getOwnerType(), startTime, endTime);

		List<EvaluationDTO> dtoList = evaluations.stream().map((r) -> {

        	EvaluationDTO dto = ConvertHelper.convert(r, EvaluationDTO.class);

        	return dto;
        }).collect(Collectors.toList());

		URL rootPath = Rentalv2ServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + this.downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		filePath = filePath + "Evaluations"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件
		this.createEvaluationsBook(filePath, dtoList);

		return download(filePath,response);
	}

	public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

            // 读取完成删除文件
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        } catch (IOException ex) {
 			LOGGER.error(ex.getMessage());
 			throw RuntimeErrorException.errorWith(QualityServiceErrorCode.SCOPE,
 					QualityServiceErrorCode.ERROR_DOWNLOAD_EXCEL,
 					ex.getLocalizedMessage());

        }
        return response;
    }

	public void createEvaluationsBook(String path,List<EvaluationDTO> dtos) {
		if (null == dtos || dtos.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("evaluations");

		this.createEvaluationsBookSheetHead(sheet);
		for (EvaluationDTO dto : dtos ) {
			this.setNewEvaluationsBookRow(sheet, dto);
		}

		try {
			FileOutputStream out = new FileOutputStream(path);

			wb.write(out);
			wb.close();
			out.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(QualityServiceErrorCode.SCOPE,
					QualityServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}

	}

	private void createEvaluationsBookSheetHead(Sheet sheet){

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		row.createCell(++i).setCellValue("业务组名称");
		row.createCell(++i).setCellValue("绩效得分");
	}

	private void setNewEvaluationsBookRow(Sheet sheet ,EvaluationDTO dto){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;
		row.createCell(++i).setCellValue(dto.getGroupName());
		row.createCell(++i).setCellValue(dto.getScore());

	}

	@Override
	public QualityInspectionTaskDTO findQualityInspectionTask(FindQualityInspectionTaskCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(),PrivilegeConstants.QUALITY_TASK_LIST,cmd.getTargetId());
		QualityInspectionTasks task = qualityProvider.findVerificationTaskById(cmd.getTaskId());
		if(task != null)
			return ConvertHelper.convert(task, QualityInspectionTaskDTO.class);
		return null;
	}

	@Override
	public CurrentUserInfoDTO getCurrentUserInfo() {
		//fix bug #15603 暂时没有公共API
		Long currentUserId = UserContext.current().getUser().getId();
		OrganizationMember organizationMember = organizationProvider.listOrganizationMembers(currentUserId).get(0);
		return ConvertHelper.convert(organizationMember, CurrentUserInfoDTO.class);
	}

	@Override
	public ListQualityInspectionTasksResponse listQualityInspectionTasks(ListQualityInspectionTasksCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(),PrivilegeConstants.QUALITY_TASK_LIST,cmd.getTargetId());

		User user = UserContext.current().getUser();
		//Long targetId = cmd.getTargetId();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		if(null == cmd.getPageAnchor()) {
			cmd.setPageAnchor(0L);
		}
		Integer offset = cmd.getPageAnchor().intValue();
        Timestamp startDate = null;
        Timestamp endDate = null;
        if(cmd.getStartDate() != null) {
        	startDate = new Timestamp(cmd.getStartDate());
        }
        if(cmd.getEndDate() != null) {
        	endDate = new Timestamp(cmd.getEndDate());
        }

		//新后台对接权限修改
		boolean isAdmin = checkAdmin(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getNamespaceId());
		LOGGER.info("listQualityInspectionTasks: checkAdmin:{}" , isAdmin);

		List<QualityInspectionTasks> tasks = new ArrayList<>();
		ListQualityInspectionTasksResponse response = new ListQualityInspectionTasksResponse();
		//查找当日已执行任务数
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateHelper.currentGMTTime());
		Timestamp todayBegin = getDayBegin(cal);
		if (isAdmin) {
			//管理员查询所有任务 减少了下参数数量
			tasks = qualityProvider.listVerificationTasksRefactor(offset, pageSize, startDate, endDate,
					null, null, null, (loc, query) -> {
				listTasksQueryBuilder(cmd, query);
				return null;
			});

			qualityProvider.getTodayTaskCountStat(response,null, null, null,todayBegin,(loc, query) -> {
				listTasksCountQueryBuilder(cmd, query);
				return null;
			});
		} else {
			List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
			List<QualityInspectionStandardGroupMap> maps = qualityProvider.listQualityInspectionStandardGroupMapByGroupAndPosition(groupDtos);
			if (maps != null && maps.size() > 0) {
				List<Long> executeStandardIds = new ArrayList<>();
				List<Long> reviewStandardIds = new ArrayList<>();
				for (QualityInspectionStandardGroupMap r : maps) {
					if (QualityGroupType.EXECUTIVE_GROUP.equals(QualityGroupType.fromStatus(r.getGroupType()))) {
						executeStandardIds.add(r.getStandardId());
					} else if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(r.getGroupType()))) {
						reviewStandardIds.add(r.getStandardId());
					}
				}
				tasks = qualityProvider.listVerificationTasksRefactor(offset, pageSize, startDate, endDate,
						executeStandardIds, reviewStandardIds, groupDtos, (loc, query) -> {
					listTasksQueryBuilder(cmd, query);
					return null;
				});
				qualityProvider.getTodayTaskCountStat(response,executeStandardIds, reviewStandardIds, groupDtos,todayBegin,(loc, query) -> {
					listTasksCountQueryBuilder(cmd, query);
					return null;
				});
			}
		}
		Long nextPageAnchor = null;
		if (tasks.size() > pageSize) {
			tasks.remove(tasks.size() - 1);
			nextPageAnchor = (long) (offset + 1);
		}

		List<QualityInspectionTaskRecords> records = new ArrayList<QualityInspectionTaskRecords>();
		for (QualityInspectionTasks task : tasks) {
			QualityInspectionTaskRecords record = qualityProvider.listLastRecordByTaskId(task.getId());
			if (record != null) {
				task.setRecord(record);
				records.add(task.getRecord());
			}
		}
		this.qualityProvider.populateRecordAttachments(records);
		this.qualityProvider.populateRecordItemResults(records);

		for (QualityInspectionTaskRecords record : records) {
			populateRecordAttachements(record, record.getAttachments());
		}

//		Set<Long> taskIds =  qualityProvider.listRecordsTaskIdByOperatorId(user.getId(), todayBegin, targetId);
		List<QualityInspectionTaskDTO> dtoList = convertQualityInspectionTaskToDTO(tasks, user.getId());

		response.setNextPageAnchor(nextPageAnchor);
		response.setTasks(dtoList);
//		response.setTodayExecutedCount(0);
//		if(taskIds != null) {
//			response.setTodayExecutedCount(taskIds.size());
//		}
        return response;
	}

	private void listTasksCountQueryBuilder(ListQualityInspectionTasksCommand cmd, SelectQuery<? extends Record> query) {
		if (cmd.getOwnerId()!= null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.OWNER_ID.eq(cmd.getOwnerId()));
		if (cmd.getOwnerType() != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.OWNER_TYPE.eq(cmd.getOwnerType()));
		if (cmd.getTargetId() != null && cmd.getTargetId() != 0L) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_ID.eq(cmd.getTargetId()));
		} else if (cmd.getTargetIds() != null && cmd.getTargetIds().size() > 0) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_ID.in(cmd.getTargetIds()));
		}
		if (!StringUtils.isNullOrEmpty(cmd.getTargetType()))
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_TYPE.eq(cmd.getTargetType()));
		if (cmd.getTaskType() != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TASK_TYPE.eq(cmd.getTaskType()));
		if (cmd.getExecuteStatus() != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.in(cmd.getExecuteStatus()));
		if (cmd.getManualFlag() != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.MANUAL_FLAG.eq(Long.valueOf(cmd.getManualFlag())));
		if (cmd.getNamespaceId() != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
		if (cmd.getExecuteFlag()!=null && cmd.getExecuteFlag() == 1) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime()))
					.or(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.isNull())
					.or(Tables.EH_QUALITY_INSPECTION_TASKS.PROCESS_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime()))));
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.eq(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode()));
		}
	}

	private void listTasksQueryBuilder(ListQualityInspectionTasksCommand cmd, SelectQuery<? extends Record> query) {
//		if (cmd.getOwnerId()!= null)
//            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.OWNER_ID.eq(cmd.getOwnerId()));
//		if (cmd.getOwnerType() != null)
//            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.OWNER_TYPE.eq(cmd.getOwnerType()));
		if (cmd.getTargetId() != null && cmd.getTargetId() != 0L) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_ID.eq(cmd.getTargetId()));
		} else if (cmd.getTargetIds() != null && cmd.getTargetIds().size() > 0) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_ID.in(cmd.getTargetIds()));
		}
		if (!StringUtils.isNullOrEmpty(cmd.getTargetType()))
            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_TYPE.eq(cmd.getTargetType()));
		if (cmd.getTaskType() != null)
            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TASK_TYPE.eq(cmd.getTaskType()));
		if (cmd.getExecuteStatus() != null)
            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.in(cmd.getExecuteStatus()));
		if (cmd.getManualFlag() != null)
            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.MANUAL_FLAG.eq(Long.valueOf(cmd.getManualFlag())));
		if (cmd.getNamespaceId() != null)
            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
		if (!StringUtils.isNullOrEmpty(cmd.getTaskName()))
            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TASK_NAME.like("%"+cmd.getTaskName()+"%"));

		if (cmd.getExecuteFlag()!=null && cmd.getExecuteFlag() == 1) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime()))
					.or(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.isNull())
					.or(Tables.EH_QUALITY_INSPECTION_TASKS.PROCESS_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime()))));
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.eq(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode()));
		}
		if (cmd.getLatestUpdateTime() != null)
            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.gt(cmd.getLatestUpdateTime())
					.or(Tables.EH_QUALITY_INSPECTION_TASKS.REVIEW_TIME.gt(cmd.getLatestUpdateTime()))
                    .or(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_TIME.gt(cmd.getLatestUpdateTime()))
                    .or(Tables.EH_QUALITY_INSPECTION_TASKS.PROCESS_TIME.gt(cmd.getLatestUpdateTime())));
	}

	private Timestamp getDayBegin(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 001);
		return new Timestamp(cal.getTimeInMillis());
	}

	private List<ExecuteGroupAndPosition> listUserRelateGroups() {
		Long startTime = System.currentTimeMillis();
		User user = UserContext.current().getUser();

		List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(user.getId());
		if(members == null || members.size() == 0) {
			return new ArrayList<>();
		}

		List<ExecuteGroupAndPosition> groupDtos = new ArrayList<>();
		for(OrganizationMember member : members) {
			Organization organization = organizationProvider.findOrganizationById(member.getOrganizationId());

			if(organization != null) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("listUserRelateGroups, organizationId=" + organization.getId());
				}
				if(OrganizationGroupType.JOB_POSITION.equals(OrganizationGroupType.fromCode(organization.getGroupType()))) {
					/*List<OrganizationJobPositionMap> maps = organizationProvider.listOrganizationJobPositionMaps(organization.getId());
					if(LOGGER.isInfoEnabled()) {
						LOGGER.info("listUserRelateGroups, OrganizationJobPositionMaps = {}" + maps);
					}

					if(maps != null && maps.size() > 0) {
						for(OrganizationJobPositionMap map : maps) {
							ExecuteGroupAndPosition group = new ExecuteGroupAndPosition();
							group.setGroupId(organization.getParentId());//具体岗位所属的部门公司组等 by xiongying20170619
							group.setPositionId(map.getJobPositionId());
							groupDtos.add(group);

//							Organization groupOrg = organizationProvider.findOrganizationById(map.getOrganizationId());
//							if(groupOrg != null) {
//								group.setGroupId(groupOrg.getDirectlyEnterpriseId());
//								group.setPositionId(map.getJobPositionId());
//								groupDtos.add(group);
//							}
							//取path后的第一个路径 为顶层公司 by xiongying 20170323
							String[] path = organization.getPath().split("/");
							Long organizationId = Long.valueOf(path[1]);
							ExecuteGroupAndPosition topGroup = new ExecuteGroupAndPosition();
							topGroup.setGroupId(organizationId);
							topGroup.setPositionId(map.getJobPositionId());
							groupDtos.add(topGroup);
						}

					}*/
					//部门岗位
					ExecuteGroupAndPosition departmentGroup = new ExecuteGroupAndPosition();
					departmentGroup.setGroupId(organization.getParentId());
					departmentGroup.setPositionId(organization.getId());
					groupDtos.add(departmentGroup);

					//通用岗位
					List<OrganizationJobPositionMap> maps = organizationProvider.listOrganizationJobPositionMaps(organization.getId());
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("listUserRelateGroups, organizationId = {}, OrganizationJobPositionMaps = {}", organization.getId(), maps);
					}

					if (maps != null && maps.size() > 0) {
						for (OrganizationJobPositionMap map : maps) {
							String[] path = organization.getPath().split("/");
							Long organizationId = Long.valueOf(path[1]);
							ExecuteGroupAndPosition topGroup = new ExecuteGroupAndPosition();
							topGroup.setGroupId(organizationId);
							topGroup.setPositionId(map.getJobPositionId());
							groupDtos.add(topGroup);
						}

					}
				} else {
					ExecuteGroupAndPosition group = new ExecuteGroupAndPosition();
					group.setGroupId(organization.getId());
					group.setPositionId(0L);
					groupDtos.add(group);
				}
			}
		}

		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("listUserRelateGroups, groupDtos = {}" , groupDtos);
		}

		Long endTime = System.currentTimeMillis();
		LOGGER.debug("TrackUserRelatedCost: listUserRelateGroups userId = " + user.getId() + ", elapse=" + (endTime - startTime));
		return groupDtos;
	}

	@Override
	public List<GroupUserDTO> getGroupMembers(Long groupId, boolean isAll) {
		User current = UserContext.current().getUser();
		List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(groupId);
    	List<GroupUserDTO> groupUsers = members.stream().map((mem) -> {
    		if(isAll) {
    			if(OrganizationMemberTargetType.USER.getCode().equals(mem.getTargetType())
             			&& mem.getTargetId() != null && mem.getTargetId() != 0) {
             		GroupUserDTO user = new GroupUserDTO();
             		user.setOperatorType(mem.getTargetType());
             		user.setUserId(mem.getTargetId());
                 	user.setUserName(mem.getContactName());
                 	user.setContact(mem.getContactToken());
                 	user.setEmployeeNo(mem.getEmployeeNo());
                 	return user;
             	} else {
             		return null;
             	}
    		} else {
    			if(OrganizationMemberTargetType.USER.getCode().equals(mem.getTargetType())
             			&& mem.getTargetId() != null && mem.getTargetId() != 0 && !mem.getTargetId().equals(current.getId())) {
             		GroupUserDTO user = new GroupUserDTO();
             		user.setOperatorType(mem.getTargetType());
             		user.setUserId(mem.getTargetId());
                 	user.setUserName(mem.getContactName());
                 	user.setContact(mem.getContactToken());
                 	user.setEmployeeNo(mem.getEmployeeNo());
                 	return user;
             	} else {
             		return null;
             	}
    		}

         }).filter(Objects::nonNull).collect(Collectors.toList());

    	return groupUsers;
	}

	private List<QualityInspectionTaskDTO> convertQualityInspectionTaskToDTO(List<QualityInspectionTasks> tasks, final Long executeUid) {

		List<QualityInspectionTaskDTO> dtoList = tasks.stream().map((r) -> {

//        	QualityInspectionStandards standard = verifiedStandardById(r.getStandardId());

		    // 由于现网存在着大量categoryId为0的任务，故不能直接抛异常，先暂时去掉校验 by lqs 20160715
			//QualityInspectionCategories category = verifiedCategoryById(r.getCategoryId());

		    QualityInspectionSpecifications category =
					qualityProvider.findSpecificationById(r.getCategoryId(), r.getOwnerType(), r.getOwnerId());
		    if(category != null) {
		        r.setCategoryName(getSpecificationNamePath(category.getPath(), category.getOwnerType(), category.getOwnerId()));
		    }

        	if(executeUid != null) {
        		r.setTaskFlag(QualityTaskType.VERIFY_TASK.getCode());
        		if(r.getOperatorId() != null && r.getOperatorId().equals(executeUid)) {
        			r.setTaskFlag(QualityTaskType.RECTIFY_TASK.getCode());
        		}
        	}

        	QualityInspectionTaskDTO dto = ConvertHelper.convert(r, QualityInspectionTaskDTO.class);

			//塞执行组名
//			String executiveGroupName = getGroupName(r.getExecutiveGroupId(), r.getExecutivePositionId(),r.get);
//			dto.setExecutiveGroupName(executiveGroupName);
			Community community = communityProvider.findCommunityById(r.getTargetId());
			if(community != null) {
				dto.setTargetName(community.getName());
			}

        	if(category != null) {
        	    dto.setCategoryDescription(category.getDescription());
        	}
        	QualityInspectionStandards standard = qualityProvider.findStandardById(r.getStandardId());
			if(standard != null) {
				dto.setStandardDescription(standard.getDescription());
				qualityProvider.populateStandardGroups(standard);
				if(standard.getExecutiveGroup() != null) {
					standard.getExecutiveGroup().forEach((executiveGroup) -> {
						StringBuilder sb = new StringBuilder();
						Organization group = organizationProvider.findOrganizationById(executiveGroup.getGroupId());
						OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(executiveGroup.getPositionId());
						if(group != null) {
							sb.append(group.getName());
						}

						if(position != null) {
							if(sb.length() > 0) {
								sb.append("-");
								sb.append(position.getName());
							} else {
								sb.append(position.getName());

							}
						}

						if(sb.length() > 0) {
							if(dto.getGroupName() != null) {
								dto.setGroupName(dto.getGroupName() + "," + sb.toString());
							} else {
								dto.setGroupName(sb.toString());
							}
						}

					});
					dto.setExecutiveGroupName(dto.getGroupName());
				}

				//兼容之前specification没进到task里面的情况

				if(category == null) {
					QualityInspectionStandardSpecificationMap map = qualityProvider.getMapByStandardId(standard.getId());
					if(map != null) {
						QualityInspectionSpecifications specification = qualityProvider.getSpecificationById(map.getSpecificationId());
						if (specification != null) {
							dto.setCategoryName(getSpecificationNamePath(specification.getPath(), specification.getOwnerType(), specification.getOwnerId()));
							dto.setCategoryId(specification.getId());
						}
					}

				}
			}

//        	Organization group = organizationProvider.findOrganizationById(r.getExecutiveGroupId());
//
//			if(group != null) {
//				dto.setGroupName(group.getName());
//				if(r.getExecutivePositionId() != null) {
//					//岗位名+组名共同组成groupname
//					OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(r.getExecutivePositionId());
//					if(position != null) {
//						dto.setGroupName(group.getName() + "-" + position.getName());
//					}
//				}
//			}
//			Organization group = organizationProvider.findOrganizationById(r.getExecutiveGroupId());
//			OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(r.getExecutivePositionId());
//			if(group != null) {
//				dto.setGroupName(group.getName());
//
//			}
//
//			if(position != null) {
//				if(dto.getGroupName() != null) {
//					dto.setGroupName(dto.getGroupName() + "-" + position.getName());
//				} else {
//					dto.setGroupName(position.getName());
//
//				}
//			}
//
//			List<GroupUserDTO> groupUsers = getGroupMembers(r.getExecutiveGroupId(), false);

//        	dto.setGroupUsers(groupUsers);

			if(r.getRecord() != null) {
	        	QualityInspectionTaskRecordsDTO recordDto = ConvertHelper.convert(r.getRecord(), QualityInspectionTaskRecordsDTO.class);
	        	if(recordDto != null) {
		        	if(recordDto.getTargetId() != null && recordDto.getTargetId() != 0) {
		        		List<OrganizationMember> target = organizationProvider.listOrganizationMembersByUId(recordDto.getTargetId());
		        		if(target != null && target.size() > 0) {
		        			recordDto.setTargetName(target.get(0).getContactName());
		        		}
		        	}
	        	}
	        	if(r.getRecord().getAttachments() != null) {
		        	List<QualityInspectionTaskAttachmentDTO> attachments = r.getRecord().getAttachments().stream().map((attach) -> ConvertHelper.convert(attach, QualityInspectionTaskAttachmentDTO.class)).collect(Collectors.toList());
					if (recordDto != null) {
						recordDto.setAttachments(attachments);
					}
				}
				if (r.getRecord().getItemResults() != null) {
					List<QualityInspectionSpecificationItemResultsDTO> results = r.getRecord().getItemResults().stream().map((result) -> ConvertHelper.convert(result, QualityInspectionSpecificationItemResultsDTO.class)).collect(Collectors.toList());
					if (recordDto != null) {
						recordDto.setItemResults(results);
					}
				}
				dto.setRecord(recordDto);
			}

			if(r.getExecutorId() != null && r.getExecutorId() != 0) {
				OrganizationMember executor = organizationProvider.findOrganizationMemberByUIdAndOrgId(r.getExecutorId(), r.getOwnerId());
	        	if(executor != null) {
	        		dto.setExecutorName(executor.getContactName());
	        	}
			}


        	if(r.getOperatorId() != null && r.getOperatorId() != 0) {
        		OrganizationMember operator = organizationProvider.findOrganizationMemberByUIdAndOrgId(r.getOperatorId(), r.getOwnerId());
            	if(operator != null) {
            		dto.setOperatorName(operator.getContactName());
            	}
        	}

        	if(r.getReviewerId() != null && r.getReviewerId() != 0) {
        		List<OrganizationMember> reviewers = organizationProvider.listOrganizationMembersByUId(r.getReviewerId());
            	if(reviewers != null && reviewers.size() > 0) {
            		dto.setReviewerName(reviewers.get(0).getContactName());
            	}
        	}

        	if(r.getCreatorUid() != null && r.getCreatorUid() != 0) {
        		List<OrganizationMember> creator = organizationProvider.listOrganizationMembersByUId(r.getCreatorUid());
            	if(creator != null && creator.size() > 0) {
            		dto.setCreatorName(creator.get(0).getContactName());
            	}
        	}

        	return dto;
        }).filter(Objects::nonNull).collect(Collectors.toList());
		return dtoList;
	}

	@Override
	public QualityInspectionTaskDTO reportVerificationResult(ReportVerificationResultCommand cmd) {
		Long taskId = 0L;
		if(cmd.getCreateTask()!=null){
			QualityInspectionTaskDTO createdTask = createQualityInspectionTask(cmd.getCreateTask());
			taskId = createdTask.getId();
		}else {
			taskId = cmd.getTaskId();
		}
		User user = UserContext.current().getUser();
		QualityInspectionTasks task = verifiedTaskById(taskId);
		if(!QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.equals(QualityInspectionTaskStatus.fromStatus(task.getStatus()))) {
			LOGGER.error("the task which id="+task.getId()+" can not execute!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_TASK_IS_CLOSED,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_TASK_IS_CLOSED),
									UserContext.current().getUser().getLocale(),
									"the task can not execute!"));
		}
		if(cmd.getOperatorId() != null && cmd.getOperatorId() == user.getId()) {
			LOGGER.error("cannot assign to oneself!" + cmd.getOperatorId());
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_ASSIGN_TO_ONESELF,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_ASSIGN_TO_ONESELF),
									UserContext.current().getUser().getLocale(),
									"cannot assign to oneself!"));
		}
		QualityInspectionTaskRecords record = new QualityInspectionTaskRecords();
		record.setTaskId(task.getId());
		record.setOperatorType(OwnerType.USER.getCode());
		record.setOperatorId(user.getId());

		task.setExecutiveTime(new Timestamp(System.currentTimeMillis()));
		task.setExecutorType(OrganizationMemberTargetType.USER.getCode());
		task.setExecutorId(user.getId());
		if(cmd.getOperatorType() != null) {
			task.setOperatorType(cmd.getOperatorType());
			record.setTargetType(cmd.getOperatorType());
		}

		if(cmd.getOperatorId() != null) {
			task.setOperatorId(cmd.getOperatorId());
			record.setTargetId(cmd.getOperatorId());
		}

		if(cmd.getEndTime() != null) {
			task.setProcessExpireTime(new Timestamp(cmd.getEndTime()));
			record.setProcessEndTime(task.getProcessExpireTime());
		}

		if(QualityInspectionTaskResult.CORRECT.getCode() == cmd.getVerificationResult()) {
//			if(QualityInspectionTaskResult.RECTIFIED_OK_AND_WAITING_APPROVAL.getCode() == task.getProcessResult()) {
//
//				task.setResult(QualityInspectionTaskResult.RECTIFIED_OK.getCode());
//			}
//
//			if(QualityInspectionTaskResult.RECTIFY_CLOSED_AND_WAITING_APPROVAL.getCode() == task.getProcessResult()) {
//				task.setResult(QualityInspectionTaskResult.RECTIFY_CLOSED.getCode());
//			}
//			else {
//				task.setResult(QualityInspectionTaskResult.INSPECT_OK.getCode());
//			}

			task.setStatus(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode());
			task.setResult(QualityInspectionTaskResult.CORRECT.getCode());
			record.setProcessResult(QualityInspectionTaskResult.CORRECT.getCode());
			record.setProcessType(ProcessType.INSPECT.getCode());

		}
		else if(QualityInspectionTaskResult.INSPECT_COMPLETE.getCode() == cmd.getVerificationResult()) {
			task.setResult(QualityInspectionTaskResult.INSPECT_COMPLETE.getCode());
			task.setStatus(QualityInspectionTaskStatus.EXECUTED.getCode());
			record.setProcessResult(QualityInspectionTaskResult.INSPECT_COMPLETE.getCode());
			record.setProcessType(ProcessType.INSPECT.getCode());
		}
//		else {
//			record.setProcessResult(QualityInspectionTaskResult.NONE.getCode());
//			record.setProcessType(ProcessType.ASSIGN.getCode());
//			task.setStatus(QualityInspectionTaskStatus.RECTIFING.getCode());
//		}

		if(!StringUtils.isNullOrEmpty(cmd.getOperatorType()) && cmd.getOperatorId() != null
				 && cmd.getEndTime() != null) {
			//总公司 分公司 在分公司通讯录而不在总公司通讯录中时可能查无此人 by xiongying20170329
			List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(user.getId());
//			OrganizationMember operator = organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(), task.getOwnerId());
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("userName", operators.get(0).getContactName());
		    map.put("taskName", task.getTaskName());
		    map.put("deadline", timeToStr(new Timestamp(cmd.getEndTime())));
			String scope = QualityNotificationTemplateCode.SCOPE;
			int code = QualityNotificationTemplateCode.ASSIGN_TASK_NOTIFY_OPERATOR;
			String locale = "zh_CN";
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			sendMessageToUser(cmd.getOperatorId(), notifyTextForApplicant);

			//总公司 分公司 在分公司通讯录而不在总公司通讯录中时可能查无此人 by xiongying20170329
			List<OrganizationMember> targets = organizationProvider.listOrganizationMembersByUId(cmd.getOperatorId());
//			OrganizationMember target = organizationProvider.findOrganizationMemberByUIdAndOrgId(cmd.getOperatorId(), task.getOwnerId());
			Map<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("operator", operators.get(0).getContactName());
			msgMap.put("target", targets.get(0).getContactName());
			msgMap.put("taskName", task.getTaskName());
			msgMap.put("deadline", timeToStr(new Timestamp(cmd.getEndTime())));
			int msgCode = QualityNotificationTemplateCode.ASSIGN_TASK_MSG;
			String msg = localeTemplateService.getLocaleTemplateString(scope, msgCode, locale, msgMap, "");
			record.setProcessMessage(msg);
		}

		if(cmd.getMessage() != null) {
			String attText = localeStringService.getLocalizedString(
					String.valueOf(QualityServiceErrorCode.SCOPE),
					String.valueOf(QualityServiceErrorCode.ATTACHMENT_TEXT),
					UserContext.current().getUser().getLocale(),
					"text:");
			if(record.getProcessMessage() != null) {
				String msg = record.getProcessMessage()  + "<br/>" + attText+cmd.getMessage();
				record.setProcessMessage(msg);
			} else {
				String msg = attText+cmd.getMessage();
				record.setProcessMessage(msg);
			}

		}

		QualityInspectionTaskDTO dto = updateVerificationTasks(task, record, cmd.getAttachments(), cmd.getItemResults(),cmd.getNamespaceId());
		return dto;

	}



	@Override
	public void reviewVerificationResult(ReviewVerificationResultCommand cmd) {
		User user = UserContext.current().getUser();
		QualityInspectionTasks task = verifiedTaskById(cmd.getTaskId());

		QualityInspectionTaskRecords record = new QualityInspectionTaskRecords();
		record.setTaskId(task.getId());
		record.setOperatorType(OwnerType.USER.getCode());
		record.setOperatorId(user.getId());

		task.setReviewResult(cmd.getReviewResult());
		task.setReviewerId(user.getId());
		task.setReviewTime(new Timestamp(System.currentTimeMillis()));

		record.setProcessType(ProcessType.REVIEW.getCode());
		record.setProcessResult(cmd.getReviewResult());

		updateVerificationTasks(task, record, null, null,cmd.getNamespaceId());

		if(cmd.getReviewResult() != null && cmd.getReviewResult() == QualityInspectionTaskReviewResult.UNQUALIFIED.getCode()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("taskNumber", task.getTaskNumber());
			//总公司 分公司 在分公司通讯录而不在总公司通讯录中时可能查无此人 by xiongying20170329
			List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(user.getId());
//			OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(), task.getOwnerId());
			if(members != null) {
				map.put("userName", members.get(0).getContactName());
			} else {
				map.put("userName", user.getNickName());
			}
			String scope = QualityNotificationTemplateCode.SCOPE;
			int code = QualityNotificationTemplateCode.UNQUALIFIED_TASK_NOTIFY_EXECUTOR;
			String locale = "zh_CN";
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			sendMessageToUser(task.getExecutorId(), notifyTextForApplicant);
		}
	}

	@Override
	public QualityInspectionTaskDTO reportRectifyResult(ReportRectifyResultCommand cmd) {
		User user = UserContext.current().getUser();
		QualityInspectionTasks task = verifiedTaskById(cmd.getTaskId());
		if(!QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.equals(QualityInspectionTaskStatus.fromStatus(task.getStatus()))) {
			LOGGER.error("the task which id="+task.getId()+" can not execute!");
			throw RuntimeErrorException
					.errorWith(QualityServiceErrorCode.SCOPE, QualityServiceErrorCode.ERROR_TASK_IS_CLOSED,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_TASK_IS_CLOSED),
									UserContext.current().getUser().getLocale(),
									"the task can not execute!"));
		}
		if(cmd.getOperatorId() != null && cmd.getOperatorId() == user.getId()) {
			LOGGER.error("cannot assign to oneself!" + cmd.getOperatorId());
			throw RuntimeErrorException
					.errorWith(QualityServiceErrorCode.SCOPE, QualityServiceErrorCode.ERROR_ASSIGN_TO_ONESELF,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_ASSIGN_TO_ONESELF),
									UserContext.current().getUser().getLocale(),
									"cannot assign to oneself!"));
		}
		QualityInspectionTaskRecords record = new QualityInspectionTaskRecords();
		record.setTaskId(task.getId());
		record.setOperatorType(OwnerType.USER.getCode());
		record.setOperatorId(user.getId());

		if(QualityInspectionTaskResult.CORRECT.getCode() == cmd.getRectifyResult()) {
			task.setResult(QualityInspectionTaskResult.CORRECT.getCode());
			task.setStatus(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode());
			record.setProcessResult(QualityInspectionTaskResult.NONE.getCode());
			record.setProcessType(ProcessType.FORWARD.getCode());
		}
		else if(QualityInspectionTaskResult.CORRECT_COMPLETE.getCode() == cmd.getRectifyResult()) {
			task.setResult(QualityInspectionTaskResult.CORRECT_COMPLETE.getCode());
			task.setStatus(QualityInspectionTaskStatus.EXECUTED.getCode());
			record.setProcessResult(QualityInspectionTaskResult.CORRECT_COMPLETE.getCode());
			record.setProcessType(ProcessType.RETIFY.getCode());
		}

		if(cmd.getOperatorType() != null) {
			task.setOperatorType(cmd.getOperatorType());
			record.setTargetType(cmd.getOperatorType());
		}

		if(cmd.getOperatorId() != null) {
			task.setOperatorId(cmd.getOperatorId());
			record.setTargetId(cmd.getOperatorId());
		}

		if(cmd.getEndTime() != null) {
			task.setProcessExpireTime(new Timestamp(cmd.getEndTime()));
			record.setProcessEndTime(task.getProcessExpireTime());
		}

		if (!StringUtils.isNullOrEmpty(cmd.getOperatorType()) && cmd.getOperatorId() != null && cmd.getEndTime() != null) {
			List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(user.getId());
			String notifyTextForApplicant = getNotifyTextForApplicant(operators, task.getTaskName(), cmd.getEndTime());
			sendMessageToUser(cmd.getOperatorId(), notifyTextForApplicant);

			String msg = getLocalRecordMessage(operators, task.getTaskName(),cmd.getOperatorId(), cmd.getEndTime());
			record.setProcessMessage(msg);
		}

		if(cmd.getMessage() != null) {
			if(record.getProcessMessage() != null) {
				String msg = record.getProcessMessage() + "<br/>" + cmd.getMessage();
				record.setProcessMessage(msg);
			} else {
				String msg = cmd.getMessage();
				record.setProcessMessage(msg);
			}
		}

		task.setProcessTime(new Timestamp(System.currentTimeMillis()));

//		processTaskAttachments(user.getId(),  cmd.getAttachments(), task, QualityTaskType.RECTIFY_TASK.getCode());
//		qualityProvider.populateTaskAttachment(task);
		QualityInspectionTaskDTO dto = updateVerificationTasks(task, record, cmd.getAttachments(), null,cmd.getNamespaceId());
		return dto;
	}

	private String getLocalRecordMessage(List<OrganizationMember> operators, String taskName, Long operatorId, Long endTime) {
		//总公司 分公司 在分公司通讯录而不在总公司通讯录中时可能查无此人 by xiongying20170329
		List<OrganizationMember> targets = organizationProvider.listOrganizationMembersByUId(operatorId);
//			OrganizationMember target = organizationProvider.findOrganizationMemberByUIdAndOrgId(cmd.getOperatorId(), task.getOwnerId());
		Map<String, Object> msgMap = new HashMap<>();
		if (operators != null && operators.size() > 0) {
			msgMap.put("operator", operators.get(0).getContactName());
		}
		msgMap.put("target", targets.get(0).getContactName());
		msgMap.put("taskName", taskName);
		msgMap.put("deadline", timeToStr(new Timestamp(endTime)));
		int msgCode = QualityNotificationTemplateCode.ASSIGN_TASK_MSG;
		String scope = QualityNotificationTemplateCode.SCOPE;
		String locale = "zh_CN";
		return localeTemplateService.getLocaleTemplateString(scope, msgCode, locale, msgMap, "");
	}

	private String getNotifyTextForApplicant(List<OrganizationMember> operators, String taskName, Long endTime) {
		Map<String, Object> map = new HashMap<>();
		if (operators != null && operators.size() > 0) {
			map.put("userName", operators.get(0).getContactName());
		}
		map.put("taskName", taskName);
		map.put("deadline", timeToStr(new Timestamp(endTime)));
		String scope = QualityNotificationTemplateCode.SCOPE;
		int code = QualityNotificationTemplateCode.ASSIGN_TASK_NOTIFY_OPERATOR;
		String locale = "zh_CN";
		return localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
	}

	//	@Scheduled(cron = "0 0 7 * * ? ")
	@Override
	public void sendTaskMsg(Long startTime, Long endTime) {
//		this.coordinationProvider.getNamedLock(CoordinationLocks.WARNING_QUALITY_TASK.getCode()).tryEnter(()-> {
//			long current = System.currentTimeMillis();//当前时间毫秒数
//			long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
//
//			if (LOGGER.isInfoEnabled()) {
//				LOGGER.info("sendTaskMsg, zero = " + zero);
//			}
			List<QualityInspectionTasks> tasks = qualityProvider.listTodayQualityInspectionTasks(startTime, endTime);

			if (tasks != null && tasks.size() > 0) {
				for (QualityInspectionTasks task : tasks) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("taskName", task.getTaskName());
					map.put("deadline", timeToStr(task.getExecutiveExpireTime()));
					String scope = QualityNotificationTemplateCode.SCOPE;
					int code = QualityNotificationTemplateCode.GENERATE_QUALITY_TASK_NOTIFY_EXECUTOR;
					String locale = "zh_CN";
					String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

					List<QualityInspectionStandardGroupMap> maps = qualityProvider.listQualityInspectionStandardGroupMapByStandardIdAndGroupType(task.getStandardId(), QualityGroupType.EXECUTIVE_GROUP.getCode());

					for (QualityInspectionStandardGroupMap executiveGroup : maps) {
						if (executiveGroup.getPositionId() != null) {
							ListOrganizationContactByJobPositionIdCommand command = new ListOrganizationContactByJobPositionIdCommand();
							command.setOrganizationId(executiveGroup.getGroupId());
							command.setJobPositionId(executiveGroup.getPositionId());
							List<OrganizationContactDTO> contacts = organizationService.listOrganizationContactByJobPositionId(command);
							if (LOGGER.isInfoEnabled()) {
								LOGGER.info("sendTaskMsg, executiveGroup = {}" + executiveGroup + "contacts = {}" + contacts);
							}

							if (contacts != null && contacts.size() > 0) {
								for (OrganizationContactDTO contact : contacts) {
									sendMessageToUser(contact.getTargetId(), notifyTextForApplicant);
								}
							}
						} else {
							List<OrganizationMember> members = organizationProvider.listOrganizationMembers(executiveGroup.getGroupId(), null);
							if (LOGGER.isInfoEnabled()) {
								LOGGER.info("sendTaskMsg, executiveGroup = {}" + executiveGroup + "members = {}" + members);
							}

							if (members != null) {
								for (OrganizationMember member : members) {
									sendMessageToUser(member.getTargetId(), notifyTextForApplicant);
								}
							}
						}
					}
				}

			}
//		});
	}

	@Override
	public void createTaskByStandard(QualityStandardsDTO standard) {
		LOGGER.info("createTaskByStandard: " + standard);
		if(standard.getExecutiveGroup() != null && standard.getExecutiveGroup().size() > 0) {

			long current = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String day = sdf.format(current);

			QualityInspectionTasks task = new QualityInspectionTasks();
			task.setNamespaceId(standard.getNamespaceId());
			task.setOwnerType(standard.getOwnerType());
			task.setOwnerId(standard.getOwnerId());
			task.setTargetType(standard.getTargetType());
			task.setTargetId(standard.getTargetId());
			task.setStandardId(standard.getId());
			if(standard.getSpecifications() != null && standard.getSpecifications().size() > 0) {
				task.setCategoryId(standard.getSpecifications().get(0).getId());
				task.setCategoryName(standard.getSpecifications().get(0).getName());
				task.setCategoryPath(standard.getSpecifications().get(0).getPath());
			}

			task.setTaskName(standard.getName());
			task.setTaskType((byte) 1);
			task.setStatus(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode());
			List<TimeRangeDTO> timeRanges = repeatService.analyzeTimeRange(standard.getRepeat().getTimeRanges());

			if (timeRanges != null && timeRanges.size() > 0) {
				for (TimeRangeDTO timeRange : timeRanges) {
					this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_QUALITY_TASK.getCode()).tryEnter(() -> {
						String duration = timeRange.getDuration();
						String start = timeRange.getStartTime();
						String str = day + " " + start;
						Timestamp startTime = strToTimestamp(str);
						Timestamp expiredTime = repeatService.getEndTimeByAnalyzeDuration(startTime, duration);
						task.setExecutiveStartTime(startTime);
						task.setExecutiveExpireTime(expiredTime);
						long now = System.currentTimeMillis();
						String taskNum = timestampToStr(new Timestamp(now)) + now;
						task.setTaskNumber(taskNum);
						qualityProvider.createVerificationTasks(task);
						taskSearcher.feedDoc(task);
					});
				}

			}

			ListPmNotifyParamsCommand command = new ListPmNotifyParamsCommand();
			command.setCommunityId(task.getTargetId());
			command.setNamespaceId(task.getNamespaceId());
			command.setOwnerType(EntityType.QUALITY_TASK.getCode());
			OrganizationDTO organization  = equipmentService.getAuthOrgByProjectIdAndModuleId(task.getTargetId(), task.getNamespaceId(), QualityConstant.QUALITY_MODULE);
			if(organization!=null){
				// here targetId means organization id for searching notify params
				command.setTargetId(organization.getId());
				command.setTargetType(EntityType.ORGANIZATIONS.getCode());
			}
			//此处为拿到自定义通知参数的接收人ids
			List<PmNotifyParamDTO> paramDTOs = listPmNotifyParams(command);
			if (paramDTOs != null && paramDTOs.size() > 0) {
				for (PmNotifyParamDTO notifyParamDTO : paramDTOs) {
					List<PmNotifyReceiverDTO> receivers = notifyParamDTO.getReceivers();
					if (receivers != null && receivers.size() > 0) {
						PmNotifyRecord record = ConvertHelper.convert(notifyParamDTO, PmNotifyRecord.class);
						PmNotifyReceiverList receiverList = new PmNotifyReceiverList();
						List<PmNotifyReceiver> pmNotifyReceivers = new ArrayList<>();
						receivers.forEach(receiver -> {
							PmNotifyReceiver pmNotifyReceiver = new PmNotifyReceiver();
							if (receiver != null) {
								pmNotifyReceiver.setReceiverType(receiver.getReceiverType());
								if (receiver.getReceivers() != null) {
									List<Long> ids = receiver.getReceivers()
											.stream().map(ReceiverName::getId)
											.collect(Collectors.toList());
									pmNotifyReceiver.setReceiverIds(ids);
								}
								pmNotifyReceivers.add(pmNotifyReceiver);
							}
						});
						receiverList.setReceivers(pmNotifyReceivers);
						record.setReceiverJson(receiverList.toString());
						record.setOwnerType(EntityType.QUALITY_TASK.getCode());
						record.setOwnerId(task.getId());

						//notify_time
						PmNotifyType notify = PmNotifyType.fromCode(record.getNotifyType());
						switch (notify) {
							case BEFORE_START:
								Timestamp starttime = minusMinutes(task.getExecutiveStartTime(), notifyParamDTO.getNotifyTickMinutes());
								record.setNotifyTime(starttime);
								break;
							case BEFORE_DELAY:
								Timestamp delaytime = minusMinutes(task.getExecutiveExpireTime(), notifyParamDTO.getNotifyTickMinutes());
								record.setNotifyTime(delaytime);
								break;
							case AFTER_DELAY:
								record.setNotifyTime(task.getExecutiveExpireTime());
								break;
							default:
								break;
						}
						pmNotifyService.pushPmNotifyRecord(record);
					}
				}
			}
		}
	}

	private Timestamp minusMinutes(Timestamp startTime, Integer minus) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		calendar.add(Calendar.MINUTE, -minus);
		return new Timestamp(calendar.getTimeInMillis());
	}


	private String timeToStr(Timestamp time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(time);
	}

	private String timestampToStr(Timestamp time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String str = sdf.format(time);
		return str;
	}

	private Timestamp strToTimestamp(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

		Timestamp ts = null;
		try {
			ts = new Timestamp(sdf.parse(str).getTime());
		} catch (ParseException e) {
			LOGGER.error("validityPeriod data format is not yyyymmdd.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"validityPeriod data format is not yyyymmdd.");
		}

		return ts;
	}

	private void sendMessageToUser(Long userId, String content) {
		MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}

	private QualityInspectionTasks verifiedTaskById(Long taskId) {
		Timestamp current = new Timestamp(DateHelper.currentGMTTime().getTime());
		QualityInspectionTasks task = qualityProvider.findVerificationTaskById(taskId);
		if(task == null) {
			LOGGER.error("the task which id="+taskId+" don't exist!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_TASK_NOT_EXIST,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_TASK_NOT_EXIST),
									UserContext.current().getUser().getLocale(),
									"the task don't exist!"));
		} else {
			if(task.getExecutiveExpireTime() != null && task.getExecutiveExpireTime().before(current)) {
				qualityProvider.closeTask(task);
			}
		}
		return task;
	}

	private QualityInspectionTaskDTO updateVerificationTasks(QualityInspectionTasks task, QualityInspectionTaskRecords record,
			List<AttachmentDescriptor> attachmentList, List<ReportSpecificationItemResultsDTO> itemResults,Integer namespaceId) {

		qualityProvider.updateVerificationTasks(task);
		taskSearcher.feedDoc(task);
		qualityProvider.createQualityInspectionTaskRecords(record);

		User user = UserContext.current().getUser();
		processRecordAttachments(user.getId(), attachmentList, record);

		populateRecordAttachements(record, record.getAttachments());

		String ownerType = task.getOwnerType();
		Long ownerId = task.getOwnerId();
		String targetType = task.getTargetType();
		Long targetId = task.getTargetId();
		Long taskId = task.getId();
		Long recordId = record.getId();

		processSpecificationItemResults(itemResults, ownerId, ownerType, targetId, targetType, task, recordId,namespaceId);

		QualityInspectionTaskRecords lastRecord = qualityProvider.listLastRecordByTaskId(task.getId());
		this.qualityProvider.populateRecordAttachment(lastRecord);
		this.qualityProvider.populateRecordItemResult(lastRecord);

		if(lastRecord != null) {
    		task.setRecord(lastRecord);
    	}

		List<QualityInspectionTasks> tasks = new ArrayList<QualityInspectionTasks>();
		tasks.add(task);
		List<QualityInspectionTaskDTO> dtos = convertQualityInspectionTaskToDTO(tasks, user.getId());
		//ConvertHelper.convert(task, QualityInspectionTaskDTO.class);
//		QualityInspectionTaskRecordsDTO recordDto = ConvertHelper.convert(task.getRecord(), QualityInspectionTaskRecordsDTO.class);
//   	 	dto.setRecord(recordDto);
		if(dtos != null && dtos.size() > 0)
			return dtos.get(0);

		return null;
	}

	private void processRecordAttachments(long userId, List<AttachmentDescriptor> attachmentList, QualityInspectionTaskRecords record) {
        List<QualityInspectionTaskAttachments> results = null;

        if(attachmentList != null) {
            results = new ArrayList<QualityInspectionTaskAttachments>();

            QualityInspectionTaskAttachments attachment = null;
            for(AttachmentDescriptor descriptor : attachmentList) {
                attachment = new QualityInspectionTaskAttachments();
                attachment.setCreatorUid(userId);
                attachment.setRecordId(record.getId());
                attachment.setContentType(descriptor.getContentType());
                attachment.setContentUri(descriptor.getContentUri());
                attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

                try {
                	this.qualityProvider.createQualityInspectionTaskAttachments(attachment);
                    results.add(attachment);
                } catch(Exception e) {
                    LOGGER.error("Failed to save the attachment, userId=" + userId
                        + ", attachment=" + attachment, e);
                }
            }
            record.setAttachments(results);
        }
    }

	private void populateRecordAttachements(QualityInspectionTaskRecords record, List<QualityInspectionTaskAttachments> attachmentList) {

		 if(attachmentList == null || attachmentList.size() == 0) {
	            if(LOGGER.isInfoEnabled()) {
	                LOGGER.info("The record attachment list is empty, recordId=" + record.getId());
	            }
		 } else {
	            for(QualityInspectionTaskAttachments attachment : attachmentList) {
	            	populateRecordAttachement(record, attachment);
	            }
		 }
	 }

	 private void populateRecordAttachement(QualityInspectionTaskRecords record, QualityInspectionTaskAttachments attachment) {

		 if(attachment == null) {
			 if(LOGGER.isInfoEnabled()) {
				 LOGGER.info("The record attachment is null, recordId=" + record.getId());
			 }
		 } else {

			 String contentUri = attachment.getContentUri();
			 if(contentUri != null && contentUri.length() > 0) {
				 try{
					 String url = contentServerService.parserUri(contentUri, EntityType.USER.getCode(), UserContext.current().getUser().getId());
					 attachment.setContentUrl(url);
				 }catch(Exception e){
					 LOGGER.error("Failed to parse attachment uri, recordId=" + record.getId() + ", attachmentId=" + attachment.getId(), e);
				 }
			 } else {
				 if(LOGGER.isWarnEnabled()) {
					 LOGGER.warn("The content uri is empty, attchmentId=" + attachment.getId());
				 }
			 }
		 }
	 }

	 private void processSpecificationItemResults(List<ReportSpecificationItemResultsDTO> itemResults, Long ownerId, String ownerType,
			 Long targetId, String targetType, QualityInspectionTasks task, Long recordId,Integer namespaceId) {

		 if(itemResults != null && itemResults.size() > 0) {
			 Long uid = UserContext.current().getUser().getId();
			 //Integer namespaceId = UserContext.getCurrentNamespaceId();

			 for(ReportSpecificationItemResultsDTO itemResult : itemResults) {
				 QualityInspectionSpecificationItemResults result = ConvertHelper.convert(itemResult, QualityInspectionSpecificationItemResults.class);
				 result.setOwnerType(ownerType);
				 result.setOwnerId(ownerId);
				 result.setTargetId(targetId);
				 result.setTargetType(targetType);
				 result.setTaskId(task.getId());
				 result.setSampleId(task.getParentId());
				 result.setTaskRecordId(recordId);
				 result.setTotalScore(result.getItemScore() * result.getQuantity());
				 result.setCreatorUid(uid);
				 result.setNamespaceId(namespaceId);

				 qualityProvider.createSpecificationItemResults(result);
			 }
		 }

	 }

	@Override
	public ListQualityCategoriesResponse listQualityCategories(
			ListQualityCategoriesCommand cmd) {
		/*Long privilegeId = configProvider.getLongValue(QualityConstant.QUALITY_CATEGORY_LIST, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);*/

		//checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_CATEGORY_LIST,cmd.getTargetId());

		Long ownerId = cmd.getOwnerId();
		String ownerType = cmd.getOwnerType();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        if(cmd.getParentId() == null)
        	cmd.setParentId(0L);
        List<QualityInspectionCategories> qaCategories = qualityProvider.listQualityInspectionCategories(locator, pageSize+1, ownerId, ownerType, cmd.getParentId());

        Long nextPageAnchor = null;
        if(qaCategories.size() > pageSize) {
        	qaCategories.remove(qaCategories.size() - 1);
            nextPageAnchor = qaCategories.get(qaCategories.size() - 1).getId();
        }

        List<QualityCategoriesDTO> categories = qaCategories.stream().map((r) -> {

        	QualityCategoriesDTO dto = ConvertHelper.convert(r, QualityCategoriesDTO.class);
        	dto.setPath(getQualityCategoryNamePath(dto.getPath()));

        	List<QualityInspectionCategories> categoryTree = qualityProvider.listQualityInspectionCategoriesByPath(r.getPath());
        	List<QualityCategoriesDTO> treeDtos = categoryTree.stream().map((t) -> {
        		QualityCategoriesDTO treeDto = ConvertHelper.convert(t, QualityCategoriesDTO.class);
        		treeDto.setPath(getQualityCategoryNamePath(treeDto.getPath()));

        		return treeDto;
        	}).collect(Collectors.toList());


        	dto = this.getQualityCategorynMenu(treeDtos, dto);
        	return dto;
        }).collect(Collectors.toList());


        return new ListQualityCategoriesResponse(nextPageAnchor, categories);
	}

	private QualityCategoriesDTO getQualityCategorynMenu(List<QualityCategoriesDTO> categories, QualityCategoriesDTO dto){

		List<QualityCategoriesDTO> categoryChildrens = new ArrayList<QualityCategoriesDTO>();

		for (QualityCategoriesDTO category : categories) {
			if(dto.getId().equals(category.getParentId())){
				QualityCategoriesDTO categoryDTO= getQualityCategorynMenu(categories, category);
				categoryChildrens.add(categoryDTO);
			}
		}
		dto.setChildrens(categoryChildrens);

		return dto;
	}

	private String getQualityCategoryNamePath(String path) {
		path = path.substring(1,path.length());
		String[] pathIds = path.split("/");
		StringBuilder sb = new StringBuilder();
		for(String pathId : pathIds) {
			Long categoryId = Long.valueOf(pathId);
			QualityInspectionCategories category = this.verifiedCategoryById(categoryId);
			sb.append("/" + category.getName());
		}

		String namePath = sb.toString();
		return namePath;
	}

	@Override
	public void createTaskByStandardId(Long id) {
		LOGGER.info("createTaskByStandardId:" + id);
		QualityInspectionStandards standard = qualityProvider.findStandardById(id);
		if(standard != null &&standard.getStatus() != null
				&& QualityStandardStatus.ACTIVE.equals(QualityStandardStatus.fromStatus(standard.getStatus()))) {
			this.qualityProvider.populateStandardGroups(standard);
			this.qualityProvider.populateStandardSpecifications(standard);

			QualityStandardsDTO standardDto = converStandardToDto(standard);
			createTaskByStandard(standardDto);
		}

	}

	private String getGroupName(Long groupId, Long positionId,Long inspectorUid) {
		StringBuilder sb = new StringBuilder();
		Organization group = organizationProvider.findOrganizationById(groupId);
		OrganizationJobPosition position = null;
		if (group != null) {
			sb.append(group.getName());
		}
		Organization departPosition = organizationProvider.findOrganizationById(positionId);
		if (departPosition != null) {
			position = new OrganizationJobPosition();
			position.setName(departPosition.getName());
		}else {
			position = organizationProvider.findOrganizationJobPositionById(positionId);
		}

		if(position != null) {
			if(sb.length() > 0) {
				sb.append("-");
				sb.append(position.getName());
			} else {
				sb.append(position.getName());

			}
		}
		if (inspectorUid != null && inspectorUid != 0) {
			List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(inspectorUid);
			if (members != null && members.size() > 0) {
				sb.append(members.get(0).getContactName());
			}
		}
			return sb.toString();
	}


	private QualityStandardsDTO converStandardToDto(QualityInspectionStandards standard) {
		processRepeatSetting(standard);
		QualityStandardsDTO standardDto = ConvertHelper.convert(standard, QualityStandardsDTO.class);
		RepeatSettingsDTO repeatDto = ConvertHelper.convert(standard.getRepeat(), RepeatSettingsDTO.class);

		if(standardDto.getTargetId() != null) {
			Community community = communityProvider.findCommunityById(standardDto.getTargetId());
			if(community != null) {
				standardDto.setTargetName(community.getName());
			}
		}

		List<StandardGroupDTO> executiveGroup = standard.getExecutiveGroup().stream().map((r) -> {

			StandardGroupDTO dto = ConvertHelper.convert(r, StandardGroupDTO.class);

			String groupName = getGroupName(r.getGroupId(), r.getPositionId(),r.getInspectorUid());
			dto.setGroupName(groupName);
        	return dto;
        }).collect(Collectors.toList());

		List<StandardGroupDTO> reviewGroup = standard.getReviewGroup().stream().map((r) -> {

			StandardGroupDTO dto = ConvertHelper.convert(r, StandardGroupDTO.class);
			String groupName = getGroupName(r.getGroupId(), r.getPositionId(),r.getInspectorUid());
			dto.setGroupName(groupName);

        	return dto;
        }).collect(Collectors.toList());

		standardDto.setRepeat(repeatDto);
		standardDto.setExecutiveGroup(executiveGroup);
		standardDto.setReviewGroup(reviewGroup);
		convertSpecificationToDTO(standard, standardDto);

		return standardDto;
	}

	private Timestamp addMonth(Timestamp startTime, int period) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		calendar.add(Calendar.MONTH, period);

		Timestamp time = new Timestamp(calendar.getTimeInMillis());

		return time;
	}

	@Scheduled(cron="0 55 3 1 * ?" )
	@Override
	public void createEvaluations() {
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("createEvaluations" + new Timestamp(DateHelper.currentGMTTime().getTime()));
		}

		Timestamp current = new Timestamp(DateHelper.currentGMTTime().getTime());
		List<QualityInspectionTasks> closedTasks = qualityProvider.listClosedTask(addMonth(current, -1),current);
		Map<Long, QualityInspectionEvaluations> map = new HashMap<Long, QualityInspectionEvaluations>();

		for (QualityInspectionTasks task :  closedTasks) {
			QualityInspectionEvaluations eva = map.get(task.getExecutiveGroupId());
			if(eva == null) {
				QualityInspectionEvaluations evaluation = new QualityInspectionEvaluations();
				evaluation.setOwnerType(task.getOwnerType());
				evaluation.setOwnerId(task.getOwnerId());
				String dateStr = timestampToStr(new Timestamp(DateHelper.currentGMTTime().getTime()));
				evaluation.setDateStr(dateStr);
				evaluation.setGroupId(task.getExecutiveGroupId());
				Organization org = organizationProvider.findOrganizationById(task.getExecutiveGroupId());
				if(org != null) {
					evaluation.setGroupName(org.getName());
				}
				Double score = (double) 100;
				score = calculateScore(task, score);
				evaluation.setScore(score);

				map.put(task.getExecutiveGroupId(), evaluation);
			} else {
				Double score = eva.getScore();
				score = calculateScore(task, score);
				eva.setScore(score);

				map.put(task.getExecutiveGroupId(), eva);
			}
		}

		Set<Long> set = map.keySet();
		for (Long s:set) {
			QualityInspectionEvaluations ev = map.get(s);

			qualityProvider.createQualityInspectionEvaluations(ev);
		}

	}

	private Double calculateScore(QualityInspectionTasks task, Double score) {

//		if(task.getStatus() == QualityInspectionTaskResult.INSPECT_DELAY.getCode()
//				|| task.getStatus() == QualityInspectionTaskResult.RECTIFY_DELAY.getCode()) {
//			QualityInspectionStandards standard = verifiedStandardById(task.getStandardId());
//			QualityInspectionStandards standard = qualityProvider.findStandardById(task.getStandardId());
//			if(standard == null) {
//				LOGGER.error("the standard which id="+task.getStandardId()+" don't exist!");
//				return null;
//			}
//			QualityInspectionEvaluationFactors factor = qualityProvider.findQualityInspectionFactorByGroupIdAndCategoryId(
//					task.getExecutiveGroupId(), standard.getCategoryId());
//			if(factor != null) {
//				score = score - factor.getWeight();
//			}
//		}

		String path = task.getCategoryPath();
		path = path.substring(1,path.length());
		String[] pathIds = path.split("/");
		Long factorCategoryId = Long.valueOf(pathIds[0]);
		Long scoreCategoryId = Long.valueOf(pathIds[pathIds.length-1]);

		QualityInspectionCategories scoreCategory = qualityProvider.findQualityInspectionCategoriesByCategoryId(scoreCategoryId);

		if(scoreCategory != null) {
			QualityInspectionEvaluationFactors factor = qualityProvider.findQualityInspectionFactorByGroupIdAndCategoryId(
					task.getExecutiveGroupId(), factorCategoryId);

			if(factor != null) {
				score = score - factor.getWeight() * scoreCategory.getScore();
			}
		}

		return score;
	}

	@Override
	public List<QualityInspectionTaskRecordsDTO> listRecordsByTaskId(
			ListRecordsByTaskIdCommand cmd) {

		List<QualityInspectionTaskRecords> records = qualityProvider.listRecordsByTaskId(cmd.getTaskId());
		if(records == null || records.size() == 0) {
			return null;
		}
		this.qualityProvider.populateRecordAttachments(records);
		this.qualityProvider.populateRecordItemResults(records);

		records.stream().map((r) -> {
			populateRecordAttachements(r, r.getAttachments());
			return r;
		});

		List<QualityInspectionTaskRecordsDTO> dtos = records.stream().map((r) -> {

			QualityInspectionTaskRecordsDTO dto = ConvertHelper.convert(r, QualityInspectionTaskRecordsDTO.class);

			if(r.getAttachments() != null) {
				List<QualityInspectionTaskAttachmentDTO> attachments = r.getAttachments().stream().map((attach) -> {
					QualityInspectionTaskAttachmentDTO attachment = ConvertHelper.convert(attach, QualityInspectionTaskAttachmentDTO.class);
					return attachment;
				}).collect(Collectors.toList());

				dto.setAttachments(attachments);
			}

			if(r.getItemResults() != null) {
				List<QualityInspectionSpecificationItemResultsDTO> results = r.getItemResults().stream().map((result) -> {
					QualityInspectionSpecificationItemResultsDTO itemResult = ConvertHelper.convert(result, QualityInspectionSpecificationItemResultsDTO.class);
					return itemResult;
				}).collect(Collectors.toList());

				dto.setItemResults(results);
			}

			return dto;
		}).collect(Collectors.toList());

		return dtos;
	}

	@Override
	public HttpServletResponse exportInspectionTasks(
			ListQualityInspectionTasksCommand cmd, HttpServletResponse response) {
		//Long privilegeId = configProvider.getLongValue(QualityConstant.QUALITY_TASK_LIST, 0L);
		/*if(cmd.getTargetId() == 0L) {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
		}*/
		checkUserPrivilege(cmd.getOwnerId(),PrivilegeConstants.QUALITY_TASK_LIST,cmd.getTargetId());

		User user = UserContext.current().getUser();
		Long ownerId = cmd.getOwnerId();
		String ownerType = cmd.getOwnerType();
		Long targetId = cmd.getTargetId();
		String targetType = cmd.getTargetType();
        Timestamp startDate = null;
        Timestamp endDate = null;
        if(cmd.getStartDate() != null) {
        	startDate = new Timestamp(cmd.getStartDate());
        }
        if(cmd.getEndDate() != null) {
        	endDate = new Timestamp(cmd.getEndDate());
        }

		int totalCount = qualityProvider.countVerificationTasks(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTaskType(), null,
				startDate, endDate, cmd.getGroupId(), cmd.getExecuteStatus(), cmd.getReviewStatus());
		if (totalCount == 0)
			return response;

		CrossShardListingLocator locator = new CrossShardListingLocator();
		Integer pageSize = Integer.MAX_VALUE-2;
		cmd.setPageAnchor(locator.getAnchor());
		cmd.setPageSize(pageSize);
//
//		List<QualityInspectionTasks> tasks = new ArrayList<QualityInspectionTasks>();
//
//        if(cmd.getIsReview() != null && cmd.getIsReview() == 1) {
//        	List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(user.getId());
//
//        	List<Long>  orgIds = members.stream().map((r) -> {
//        		return r.getOrganizationId();
//        	}).collect(Collectors.toList());
//        	List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
//
//			List<Long> standardIds = qualityProvider.listQualityInspectionStandardGroupMapByGroup(groupDtos, QualityGroupType.REVIEW_GROUP.getCode());
//
//        	tasks = qualityProvider.listVerificationTasks(locator, pageSize, ownerId, ownerType, targetId, targetType,
//            		cmd.getTaskType(), null, startDate, endDate, null,
//            		cmd.getExecuteStatus(), cmd.getReviewStatus(), false, standardIds, cmd.getManualFlag());
//
//
//        } else {
//        	tasks = qualityProvider.listVerificationTasks(locator, pageSize, ownerId, ownerType, targetId, targetType,
//        		cmd.getTaskType(), null, startDate, endDate, null,
//        		cmd.getExecuteStatus(), cmd.getReviewStatus(), false, null, cmd.getManualFlag());
//        }
//
//		List<QualityInspectionTaskRecords> records = new ArrayList<QualityInspectionTaskRecords>();
//        for(QualityInspectionTasks task : tasks) {
//        	QualityInspectionTaskRecords record = qualityProvider.listLastRecordByTaskId(task.getId());
//        	if(record != null) {
//        		task.setRecord(record);
//            	records.add(task.getRecord());
//        	}
//        }
//
//		this.qualityProvider.populateRecordAttachments(records);
//
//		records.stream().map((r) -> {
//			populateRecordAttachements(r, r.getAttachments());
//			return r;
//		});


		ListQualityInspectionTasksResponse taskResponse = listQualityInspectionTasks(cmd);
//		List<QualityInspectionTaskDTO> dtoList = convertQualityInspectionTaskToDTO(tasks, null);
		List<QualityInspectionTaskDTO> dtoList = taskResponse.getTasks();

		URL rootPath = Rentalv2ServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + this.downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		filePath = filePath + "InspectionTasks"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件
		this.createInspectionTasksBook(filePath, dtoList);

		return download(filePath,response);
	}

	public void createInspectionTasksBook(String path,List<QualityInspectionTaskDTO> dtos) {
		if (null == dtos || dtos.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("inspectionTask");

		this.createInspectionTasksBookSheetHead(sheet);
		for (QualityInspectionTaskDTO dto : dtos ) {
			this.setNewInspectionTasksBookRow(sheet, dto);
		}

		try {
			FileOutputStream out = new FileOutputStream(path);

			wb.write(out);
			wb.close();
			out.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(QualityServiceErrorCode.SCOPE,
					QualityServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}

	}

	private void createInspectionTasksBookSheetHead(Sheet sheet){

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		row.createCell(++i).setCellValue("任务编号");
		row.createCell(++i).setCellValue("任务名称");
		row.createCell(++i).setCellValue("所属业务组");
		row.createCell(++i).setCellValue("所属类型");
		row.createCell(++i).setCellValue("核查人员");
		row.createCell(++i).setCellValue("跟进人");
		row.createCell(++i).setCellValue("执行时间");
		row.createCell(++i).setCellValue("截止时间");
		row.createCell(++i).setCellValue("执行状态");
		row.createCell(++i).setCellValue("最终结果");
		row.createCell(++i).setCellValue("审阅状态");
		row.createCell(++i).setCellValue("审阅结果");
		row.createCell(++i).setCellValue("审阅人");
		row.createCell(++i).setCellValue("是否手动添加");
	}

	private void setNewInspectionTasksBookRow(Sheet sheet ,QualityInspectionTaskDTO dto){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;
		row.createCell(++i).setCellValue(dto.getTaskNumber());
		row.createCell(++i).setCellValue(dto.getTaskName());
		row.createCell(++i).setCellValue(dto.getGroupName());
		row.createCell(++i).setCellValue(dto.getCategoryName());
		row.createCell(++i).setCellValue(dto.getExecutorName());
		row.createCell(++i).setCellValue(dto.getOperatorName());
		row.createCell(++i).setCellValue(dto.getExecutiveStartTime().toString());
		if(dto.getExecutiveExpireTime() != null) {
			row.createCell(++i).setCellValue(dto.getExecutiveExpireTime().toString());
		}
		else {
			row.createCell(++i).setCellValue("");
		}

		if(dto.getStatus() != null) {
			row.createCell(++i).setCellValue(statusToString(dto.getStatus()));
		}

		else {
			row.createCell(++i).setCellValue("");
		}


		if(dto.getResult() != null) {
			row.createCell(++i).setCellValue(resultToString(dto.getResult()));
		}

		else {
			row.createCell(++i).setCellValue("");
		}


		if(dto.getReviewResult() != null) {
			if(dto.getReviewResult().equals(QualityInspectionTaskReviewResult.NONE.getCode())) {
				row.createCell(++i).setCellValue("待审阅");
				row.createCell(++i).setCellValue("无");
			}
			if(dto.getReviewResult().equals(QualityInspectionTaskReviewResult.QUALIFIED.getCode())) {
				row.createCell(++i).setCellValue("已审阅");
				row.createCell(++i).setCellValue("合格");
			}
			if(dto.getReviewResult().equals(QualityInspectionTaskReviewResult.UNQUALIFIED.getCode())) {
				row.createCell(++i).setCellValue("已审阅");
				row.createCell(++i).setCellValue("不合格");
			}
		} else {
			row.createCell(++i).setCellValue("");
			row.createCell(++i).setCellValue("");
		}

		row.createCell(++i).setCellValue(dto.getReviewerName());

		if(dto.getManualFlag() != null) {
			if(dto.getManualFlag() == 0)
				row.createCell(++i).setCellValue("否");

			if(dto.getManualFlag() == 1)
				row.createCell(++i).setCellValue("是");

		} else {
			row.createCell(++i).setCellValue("");
		}



	}

	private String statusToString(Byte status) {

		if(status.equals(QualityInspectionTaskStatus.NONE.getCode()))
			return "无";
		if(status.equals(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode()))
			return "待执行";
		if(status.equals(QualityInspectionTaskStatus.EXECUTED.getCode()))
			return "已执行";
		if(status.equals(QualityInspectionTaskStatus.DELAY.getCode()))
			return "已延误";
		return "";
	}

	private String resultToString(Byte result) {

		if(result.equals(QualityInspectionTaskResult.NONE.getCode()))
			return "无";
		if(result.equals(QualityInspectionTaskResult.CORRECT.getCode()))
			return "整改中";
		if(result.equals(QualityInspectionTaskResult.INSPECT_COMPLETE.getCode()))
			return "核查合格";
		if(result.equals(QualityInspectionTaskResult.CORRECT_COMPLETE.getCode()))
			return "整改合格";
		if(result.equals(QualityInspectionTaskResult.INSPECT_DELAY.getCode()))
			return "核查延期";
		if(result.equals(QualityInspectionTaskResult.CORRECT_DELAY.getCode()))
			return "整改延期";
		return "";
	}

	@Override
	public void closeDelayTasks() {
		LOGGER.info("close delay tasks.");
		qualityProvider.closeDelayTasks();
	}

	@Override
	public void createTasks() {
		List<QualityInspectionStandards> activeStandards = qualityProvider.listActiveStandards();

		for(QualityInspectionStandards standard : activeStandards) {
			boolean isRepeat = repeatService.isRepeatSettingActive(standard.getRepeatSettingId());
			if(isRepeat) {
				createTaskByStandardId(standard.getId());
			}

		}
	}

	@Override
	public List<OrganizationDTO> listUserRelateOrgGroups() {

		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		List<OrganizationDTO> groupDtos = organizationService.listUserRelateOrganizations(namespaceId, user.getId(), OrganizationGroupType.DEPARTMENT);

		return groupDtos;
	}

	@Override
	public ListQualityInspectionLogsResponse listQualityInspectionLogs(ListQualityInspectionLogsCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_UPDATELOG_LIST, cmd.getScopeId());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<Long> scopeIds = new ArrayList<>();
		if (cmd.getTargetId() != null) {
			scopeIds = Collections.singletonList(cmd.getScopeId());
		} else {
			scopeIds = cmd.getScopeIds();
		}
		List<QualityInspectionLogs> logs = qualityProvider.listQualityInspectionLogs(cmd.getOwnerType(), null,
				null, cmd.getTargetId(), scopeIds, locator, pageSize + 1);

        Long nextPageAnchor = null;
        if(logs != null && logs.size() > pageSize) {
        	logs.remove(logs.size() - 1);
            nextPageAnchor = logs.get(logs.size() - 1).getId();
        }
		if (logs == null) {
			logs = new ArrayList<>();
		}
		List<QualityInspectionLogDTO> dtos = logs.stream().map((r) -> {

        	QualityInspectionLogDTO dto = ConvertHelper.convert(r, QualityInspectionLogDTO.class);
        	dto.setOperateType(r.getProcessType());
        	dto.setOperatorId(r.getOperatorUid());
        	dto.setOperateTime(r.getCreateTime());
        	if(r.getOperatorUid() != null ) {
        		User user = userProvider.findUserById(r.getOperatorUid());
        		if(user != null) {
        			dto.setOperatorName(user.getNickName());
        		}
        	}

			if (QualityInspectionLogType.STANDARD.getCode().equals(r.getTargetType()) && r.getTargetId() != null) {
				QualityInspectionStandards standard = qualityProvider.findStandardById(r.getTargetId());
				if (standard != null) {
					if (cmd.getScopeId() == null || cmd.getScopeId() == 0L) {
						//全部中查看
						dto.setTargetName(standard.getName());
					} else {
						//项目中查看
						if (standard.getTargetId().equals(cmd.getScopeId()))
							dto.setTargetName(standard.getName());
					}
				}
			}
			return dto;
        }).collect(Collectors.toList());

        //现在要求有项目分类 去掉dto中targetName为空的 也就是上一步中根据qualityProvider.findStandardById(r.getTargetId());
		//dtos.removeIf((r) -> r.getTargetName() == null);
//		if (dtos.size() < pageSize)
//			nextPageAnchor = null;
		ListQualityInspectionLogsResponse response = new ListQualityInspectionLogsResponse();
        response.setNextPageAnchor(nextPageAnchor);
        response.setDtos(dtos);

		return response;
	}

	private QualityInspectionSpecifications verifiedSpecificationById(Long specificationId, String ownerType, Long ownerId,Integer namespaceId) {
		QualityInspectionSpecifications specification = qualityProvider.findSpecificationById(specificationId, ownerType, ownerId);
		if(specification == null) {
			LOGGER.error("the specification which id="+specificationId+" don't exist!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_CATEGORY_NOT_EXIST,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_CATEGORY_NOT_EXIST),
									UserContext.current().getUser().getLocale(),
									"the specification don't exist!"));
		}
		return specification;
	}

	private String getSpecificationNamePath(String path, String ownerType, Long ownerId) {
		path = path.substring(1,path.length());
		String[] pathIds = path.split("/");
		StringBuilder sb = new StringBuilder();
		for(String pathId : pathIds) {
			Long specificationId = Long.valueOf(pathId);
			QualityInspectionSpecifications specification = qualityProvider.getSpecificationById(specificationId);
			sb.append("/" + specification.getName());
		}

		String namePath = sb.toString();
		return namePath;
	}

	@Override
	public QualityInspectionTaskDTO createQualityInspectionTask(CreateQualityInspectionTaskCommand cmd) {
		User user = UserContext.current().getUser();
		long current = System.currentTimeMillis();

		if(cmd.getSampleId() != null) {
			QualityInspectionSamples sample = qualityProvider.findQualityInspectionSample(cmd.getSampleId(), cmd.getOwnerType(), cmd.getOwnerId());
			if(sample == null || new Timestamp(current).after(sample.getEndTime())) {
				LOGGER.error("sample = {}", sample);
				throw RuntimeErrorException
						.errorWith(
								QualityServiceErrorCode.SCOPE,
								QualityServiceErrorCode.ERROR_SAMPLE_CANNOT_CREATE_TASK,
								localeStringService.getLocalizedString(
										String.valueOf(QualityServiceErrorCode.SCOPE),
										String.valueOf(QualityServiceErrorCode.ERROR_SAMPLE_CANNOT_CREATE_TASK),
										UserContext.current().getUser().getLocale(),
										"sample is not exist or is closed!"));
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String day = sdf.format(current);

		QualityInspectionTasks task = new QualityInspectionTasks();
		task.setNamespaceId(UserContext.getCurrentNamespaceId());
		//task.setNamespaceId(cmd.getNamespaceId());
		task.setOwnerType(cmd.getOwnerType());
		task.setOwnerId(cmd.getOwnerId());
		task.setTargetId(cmd.getTargetId());
		task.setTargetType(cmd.getTargetType());
		task.setCreatorUid(user.getId());
		task.setTaskName(cmd.getName());
		task.setTaskType((byte) 1);
		task.setStatus(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode());
		if(cmd.getGroup() != null) {
			task.setExecutiveGroupId(cmd.getGroup().getGroupId());
			task.setExecutivePositionId(cmd.getGroup().getPositionId());
		}

		task.setExecutiveExpireTime(new Timestamp(cmd.getExecutiveExpireTime()));
		task.setCategoryId(cmd.getSpecificationId());

		QualityInspectionSpecifications specification =
				verifiedSpecificationById(cmd.getSpecificationId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getNamespaceId());
		task.setCategoryPath(specification.getPath());
		//fix bug ： byte to long old:task.setManualFlag((byte) 1);
		task.setManualFlag(1L);
		if(cmd.getSampleId() != null) {
			task.setParentId(cmd.getSampleId());
			task.setManualFlag(2L);//绩效考核创建任务标识
		}

		this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_QUALITY_TASK.getCode()).tryEnter(()-> {
			Timestamp startTime = new Timestamp(current);
			task.setExecutiveStartTime(startTime);
			String taskNum = timestampToStr(new Timestamp(current)) + current;
			task.setTaskNumber(taskNum);
			qualityProvider.createVerificationTasks(task);
			taskSearcher.feedDoc(task);

//			if(cmd.getTemplateFlag()) {
//				QualityInspectionTaskTemplates template = ConvertHelper.convert(task, QualityInspectionTaskTemplates.class);
//				if(cmd.getTemplateId() != null) {
//					QualityInspectionTaskTemplates exist = qualityProvider.findQualityInspectionTaskTemplateById(cmd.getTemplateId());
//					if(exist != null) {
//						template.setId(cmd.getTemplateId());
//						qualityProvider.updateQualityInspectionTaskTemplates(template);
//					} else {
//						qualityProvider.createQualityInspectionTaskTemplates(template);
//					}
//				} else {
//					qualityProvider.createQualityInspectionTaskTemplates(template);
//				}
//			}
		});

		List<QualityInspectionTasks> tasks = new ArrayList<>();
		tasks.add(task);
		List<QualityInspectionTaskDTO> dtos = convertQualityInspectionTaskToDTO(tasks, user.getId());

		return dtos.get(0);

	}

	@Override
	public void reviewQualityStandard(ReviewReviewQualityStandardCommand cmd) {
		//Long privilegeId = configProvider.getLongValue(QualityConstant.QUALITY_STANDARDREVIEW_REVIEW, 0L);
		/*if(cmd.getTargetId() == 0L) {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
		}*/
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_STANDARDREVIEW_REVIEW, cmd.getTargetId());

		QualityInspectionStandards standard = qualityProvider.findStandardById(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId());
		standard.setReviewResult(cmd.getReviewResult());
		standard.setReviewerUid(UserContext.current().getUser().getId());
		standard.setReviewTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		if(ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
			standard.setStatus(QualityStandardStatus.ACTIVE.getCode());
		} else {
			standard.setStatus(QualityStandardStatus.INACTIVE.getCode());
		}
		qualityProvider.updateQualityInspectionStandards(standard);
	}

	@Override
	public void createQualitySpecification(CreateQualitySpecificationCommand cmd) {
		//ScopeCode： 0 表示全部  1表示项目
		if(SpecificationInspectionType.CATEGORY.equals(SpecificationInspectionType.fromStatus(cmd.getInspectionType()))) {
			checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_CATEGORY_CREATE, cmd.getScopeId());
		} else {
			checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_SPECIFICATION_CREATE, cmd.getScopeId());
		}

		QualityInspectionSpecifications specification = ConvertHelper.convert(cmd, QualityInspectionSpecifications.class);
		//specification.setNamespaceId(UserContext.getCurrentNamespaceId());
		specification.setNamespaceId(cmd.getNamespaceId());
		specification.setCreatorUid(UserContext.current().getUser().getId());
		specification.setApplyPolicy(SpecificationApplyPolicy.ADD.getCode());
		if(cmd.getParentId() != null) {
			QualityInspectionSpecifications parent =
					verifiedSpecificationById(cmd.getParentId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getNamespaceId());
			specification.setParentId(cmd.getParentId());
			specification.setPath(parent.getPath()+"/");
		} else {
			specification.setPath("/");
		}

		qualityProvider.createQualitySpecification(specification);

	}

	@Override
	public void updateQualitySpecification(UpdateQualitySpecificationCommand cmd) {
		QualityInspectionSpecifications specification =
				verifiedSpecificationById(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getNamespaceId());
		if(SpecificationInspectionType.CATEGORY.equals(SpecificationInspectionType.fromStatus(specification.getInspectionType()))) {
			checkUserPrivilege(cmd.getOwnerId(),PrivilegeConstants.QUALITY_CATEGORY_UPDATE,cmd.getScopeId());
		} else {
			checkUserPrivilege(cmd.getOwnerId(),PrivilegeConstants.QUALITY_SPECIFICATION_UPDATE,cmd.getScopeId());
		}

		if(specification.getScopeId().equals(cmd.getScopeId())) {

			specification.setName(cmd.getName());
			specification.setDescription(cmd.getDescription());
			specification.setScore(cmd.getScore());
			specification.setWeight(cmd.getWeight());
			//quliaty-offline
			specification.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			specification.setUpdateUid(UserContext.currentUserId());

			qualityProvider.updateQualitySpecification(specification);
		} else {
			if(SpecificationScopeCode.COMMUNITY.equals(SpecificationScopeCode.fromCode(cmd.getScopeCode()))
					&& !specification.getScopeId().equals(cmd.getScopeId())) {
				QualityInspectionSpecifications newSpecification = ConvertHelper.convert(cmd, QualityInspectionSpecifications.class);
				newSpecification.setNamespaceId(cmd.getNamespaceId());
				newSpecification.setApplyPolicy(SpecificationApplyPolicy.MODIFY.getCode());
				newSpecification.setReferId(specification.getId());
				newSpecification.setCreatorUid(UserContext.current().getUser().getId());
				newSpecification.setInspectionType(specification.getInspectionType());

				if(cmd.getParentId() != null) {
					QualityInspectionSpecifications parent =
							verifiedSpecificationById(cmd.getParentId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getNamespaceId());
					newSpecification.setParentId(cmd.getParentId());
					newSpecification.setPath(parent.getPath()+"/");
				} else {
					newSpecification.setPath("/");
				}
				qualityProvider.createQualitySpecification(newSpecification);

			}
		}
	}

	@Override
	public void deleteQualitySpecification(DeleteQualitySpecificationCommand cmd) {

		QualityInspectionSpecifications specification = verifiedSpecificationById(cmd.getSpecificationId(), cmd.getOwnerType(), cmd.getOwnerId(),null);
		if(SpecificationInspectionType.CATEGORY.equals(SpecificationInspectionType.fromStatus(specification.getInspectionType()))) {
			checkUserPrivilege(cmd.getOwnerId(),PrivilegeConstants.QUALITY_CATEGORY_DELETE,cmd.getScopeId());
		} else {
			checkUserPrivilege(cmd.getOwnerId(),PrivilegeConstants.QUALITY_SPECIFICATION_DELETE,cmd.getScopeId());
		}

		List<QualityInspectionSpecifications> childrens = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), specification.getScopeCode(), specification.getScopeId(), specification.getId(), null);
		if (childrens != null && childrens.size() > 0) {
			throw RuntimeErrorException.errorWith(QualityServiceErrorCode.SCOPE,
					QualityServiceErrorCode.ERROR_DELETE_SPECIFICATION,
					localeStringService.getLocalizedString(
							String.valueOf(QualityServiceErrorCode.SCOPE),
							String.valueOf(QualityServiceErrorCode.ERROR_DELETE_SPECIFICATION),
							UserContext.current().getUser().getLocale(),
							"delete is not allowed!"));
		}

		if(SpecificationScopeCode.fromCode(specification.getScopeCode()).equals(SpecificationScopeCode.fromCode(cmd.getScopeCode()))
				&& specification.getScopeId().equals(cmd.getScopeId())) {
			specification.setStatus(QualityStandardStatus.INACTIVE.getCode());
			specification.setDeleteUid(UserContext.currentUserId());
			specification.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			qualityProvider.updateQualitySpecification(specification);

			qualityProvider.inactiveQualityInspectionStandardSpecificationMapBySpecificationId(specification.getId());
		} else {
			if(SpecificationScopeCode.COMMUNITY.equals(SpecificationScopeCode.fromCode(cmd.getScopeCode()))
					&& !specification.getScopeId().equals(cmd.getScopeId())) {
				QualityInspectionSpecifications newSpecification = ConvertHelper.convert(cmd, QualityInspectionSpecifications.class);
				newSpecification.setApplyPolicy(SpecificationApplyPolicy.DELETE.getCode());
				newSpecification.setReferId(specification.getId());
				newSpecification.setCreatorUid(UserContext.current().getUser().getId());
				// add path for del action
				newSpecification .setPath(specification.getPath()+"/");
				newSpecification.setParentId(specification.getParentId());
				newSpecification.setInspectionType(specification.getInspectionType());
				qualityProvider.createQualitySpecification(newSpecification);
				// change newSpecification to sepcification
				qualityProvider.inactiveQualityInspectionStandardSpecificationMapBySpecificationId(specification.getId());
			}else{
				specification.setStatus(QualityStandardStatus.INACTIVE.getCode());
				specification.setDeleteUid(UserContext.currentUserId());
				specification.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				qualityProvider.updateQualitySpecification(specification);

				qualityProvider.inactiveQualityInspectionStandardSpecificationMapBySpecificationId(specification.getId());
			}
		}
	}

	@Override
	public ListQualitySpecificationsResponse listQualitySpecifications(ListQualitySpecificationsCommand cmd) {
		if(SpecificationInspectionType.SPECIFICATION.equals(SpecificationInspectionType.fromStatus(cmd.getInspectionType()))){
			checkUserPrivilege(cmd.getOwnerId(),PrivilegeConstants.QUALITY_SPECIFICATION_LIST,cmd.getScopeId());
		} else {
			checkUserPrivilege(cmd.getOwnerId(),PrivilegeConstants.QUALITY_CATEGORY_LIST,cmd.getScopeId());
		}

		ListQualitySpecificationsResponse response = new ListQualitySpecificationsResponse();
		List<QualityInspectionSpecifications> specifications = new ArrayList<>();
		List<QualityInspectionSpecifications> scopeSpecifications = new ArrayList<>();


		//先查全公司的该节点下的所有子节点,再查该项目下的所有子节点
		QualityInspectionSpecifications parent = new QualityInspectionSpecifications();
		if(cmd.getParentId() == null || cmd.getParentId() == 0) {
			parent.setId(0L);
			parent.setReferId(0L);
			// 管理公司对应的全部模板
			specifications = qualityProvider.listAllChildrenSpecifications("/%", cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.ALL.getCode(), 0L, cmd.getInspectionType());
			if(SpecificationScopeCode.COMMUNITY.equals(SpecificationScopeCode.fromCode(cmd.getScopeCode()))) {
				scopeSpecifications = qualityProvider.listAllChildrenSpecifications("/%", null, null, cmd.getScopeCode(), cmd.getScopeId(), cmd.getInspectionType());
			}else {
				// add all communities data without ownerId condition
				specifications.addAll(qualityProvider.listAllCommunitiesChildrenSpecifications("/%", null, null, cmd.getScopeIds(), cmd.getInspectionType()));
			}

		} else {
			parent = verifiedSpecificationById(cmd.getParentId(), cmd.getOwnerType(), cmd.getOwnerId(),null);
			specifications = qualityProvider.listAllChildrenSpecifications(parent.getPath() + "/%", cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.ALL.getCode(), 0L, cmd.getInspectionType());
			if(SpecificationScopeCode.COMMUNITY.equals(SpecificationScopeCode.fromCode(cmd.getScopeCode()))) {
				scopeSpecifications = qualityProvider.listAllChildrenSpecifications(parent.getPath() + "/%", cmd.getOwnerType(), cmd.getOwnerId(), cmd.getScopeCode(), cmd.getScopeId(), cmd.getInspectionType());
			}else {
				specifications.addAll(qualityProvider.listAllCommunitiesChildrenSpecifications(parent.getPath() +"/%", null, null,cmd.getScopeIds(), cmd.getInspectionType()));
			}
		}
		List<QualityInspectionSpecificationDTO> dtos = new ArrayList<>();
		//只有在项目中才会处理ALL和SCOPE
		if (SpecificationScopeCode.COMMUNITY.equals(SpecificationScopeCode.fromCode(cmd.getScopeCode()))){
			dtos = dealWithScopeSpecifications(specifications, scopeSpecifications);
		}else {
			//在全部中查看需要去除 SpecificationApplyPolicy.DELETE 因为只是记录optionType
			specifications.removeIf(s -> SpecificationApplyPolicy.DELETE.equals(SpecificationApplyPolicy.fromCode(s.getApplyPolicy())));
			dtos = dealWithScopeSpecifications(specifications, scopeSpecifications);
		}

		QualityInspectionSpecificationDTO parentDto = ConvertHelper.convert(parent, QualityInspectionSpecificationDTO.class);
		parentDto = processQualitySpecificationTree(dtos, parentDto);
		dtos = parentDto.getChildrens();
		//offline support
		if (cmd.getLastSyncTime() != null) {
			Timestamp syncTime = dateStrToTimestamp(cmd.getLastSyncTime());
			dtos.removeIf((dto) -> dto.getCreateTime().before(syncTime)
					&& dto.getUpdateTime().before(syncTime) && dto.getDeleteTime().before(syncTime));
		}

		//返回数据中添加所属项目
		processSepcificationScopeName(dtos);
		List<QualityInspectionSpecificationDTO> result = null;
		if (dtos.size() > 0) {
			result = dtos.stream().sorted(Comparator.comparing(QualityInspectionSpecificationDTO::getId))
							.collect(Collectors.toList());
		}

		response.setSpecifications(result);
		return response;
	}

	private void processSepcificationScopeName(List<QualityInspectionSpecificationDTO> dtos) {
		for (QualityInspectionSpecificationDTO specification : dtos) {
			if (specification.getScopeId() != 0L) {
				Community community = communityProvider.findCommunityById(specification.getScopeId());
				if (community != null)
					specification.setScopeName(community.getName());
			}
		}
	}

	private List<QualityInspectionSpecificationDTO> dealWithScopeSpecifications(List<QualityInspectionSpecifications> specifications, List<QualityInspectionSpecifications> scopeSpecifications) {

		List<QualityInspectionSpecificationDTO> dtos = new ArrayList<>();

		List<Long> scopeDeleteSpecifications = new ArrayList<Long>();
		Map<Long, QualityInspectionSpecificationDTO> scopeModifySpecifications = new HashMap<Long, QualityInspectionSpecificationDTO>();

		if(scopeSpecifications != null && scopeSpecifications.size() > 0) {
			for(QualityInspectionSpecifications scopeSpecification : scopeSpecifications) {
				if(SpecificationApplyPolicy.DELETE.equals(SpecificationApplyPolicy.fromCode(scopeSpecification.getApplyPolicy()))) {
					scopeDeleteSpecifications.add(scopeSpecification.getReferId());
				}
				if(SpecificationApplyPolicy.MODIFY.equals(SpecificationApplyPolicy.fromCode(scopeSpecification.getApplyPolicy()))) {
					QualityInspectionSpecificationDTO dto = ConvertHelper.convert(scopeSpecification, QualityInspectionSpecificationDTO.class);
					scopeModifySpecifications.put(scopeSpecification.getReferId(), dto);
				}
				if(SpecificationApplyPolicy.ADD.equals(SpecificationApplyPolicy.fromCode(scopeSpecification.getApplyPolicy()))) {
					QualityInspectionSpecificationDTO dto = ConvertHelper.convert(scopeSpecification, QualityInspectionSpecificationDTO.class);
					dtos.add(dto);
				}

			}
		}

		if(specifications != null && specifications.size() > 0) {
			for(QualityInspectionSpecifications specification : specifications) {
				QualityInspectionSpecificationDTO dto = ConvertHelper.convert(specification, QualityInspectionSpecificationDTO.class);

				//被删除的结点排除掉
				if(scopeDeleteSpecifications.contains(dto.getId())) {
					continue;
				}
				//被修改的结点用修改后的
				else if(scopeModifySpecifications.containsKey(dto.getId())) {
					dto = scopeModifySpecifications.get(dto.getId());
				}

				dtos.add(dto);
			}
		}

		return dtos;
	}

	/**
	 * 处理类型的树状结构
	 */
	private QualityInspectionSpecificationDTO processQualitySpecificationTree(List<QualityInspectionSpecificationDTO> dtos, QualityInspectionSpecificationDTO dto) {

		List<QualityInspectionSpecificationDTO> specificationChildren = new ArrayList<QualityInspectionSpecificationDTO>();

		for (QualityInspectionSpecificationDTO specification : dtos) {
			if(dto.getReferId().equals(0L)) {
				if(dto.getId().equals(specification.getParentId())){
					QualityInspectionSpecificationDTO specificationDTO= processQualitySpecificationTree(dtos, specification);
					specificationChildren.add(specificationDTO);
				}
			} else {//修改的结点要把被修改结点的子树接过去
				if(dto.getId().equals(specification.getParentId()) || dto.getReferId().equals(specification.getParentId())){
					QualityInspectionSpecificationDTO specificationDTO= processQualitySpecificationTree(dtos, specification);
					specificationChildren.add(specificationDTO);
				}
			}
		}
		dto.setChildrens(specificationChildren);

		return dto;
	}

	private List<CountScoresSpecificationDTO> listChildSpecificationDTOs(CountScoresCommand cmd) {
		List<CountScoresSpecificationDTO> dtos = new ArrayList<>();

		List<QualityInspectionSpecifications> specifications = new ArrayList<QualityInspectionSpecifications>();
		//查第一级的子结点
		if(cmd.getSpecificationId() == null || cmd.getSpecificationId() == 0L) {
			specifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.ALL.getCode(), 0L, 0L, null);
		} else {
			QualityInspectionSpecifications parent = verifiedSpecificationById(cmd.getSpecificationId(), cmd.getOwnerType(), cmd.getOwnerId(),null);
			specifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.ALL.getCode(), 0L, parent.getId(), null);
		}
		//只查一个小区时
		if(cmd.getTargetIds() != null && cmd.getTargetIds().size() == 1) {
			List<QualityInspectionSpecifications> scopeSpecifications = new ArrayList<QualityInspectionSpecifications>();

			if(cmd.getSpecificationId() == null || cmd.getSpecificationId() == 0L) {
				scopeSpecifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(),SpecificationScopeCode.COMMUNITY.getCode(), cmd.getTargetIds().get(0), 0L, null);
			} else {
				scopeSpecifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.COMMUNITY.getCode(), cmd.getTargetIds().get(0), cmd.getSpecificationId(), null);
			}

			dealWithScopeSpecifications(specifications, scopeSpecifications).forEach(scopeSpecification -> {
				CountScoresSpecificationDTO dto = new CountScoresSpecificationDTO();
				dto.setSpecificationId(scopeSpecification.getId());
				dto.setSpecificationName(scopeSpecification.getName());
				dto.setSpecificationInspectionType(scopeSpecification.getInspectionType());
				dto.setSpecificationDescription(scopeSpecification.getDescription());
				dto.setSpecificationScore(scopeSpecification.getScore());
				dto.setSpecificationWeight(scopeSpecification.getWeight());

				dtos.add(dto);
			});

		} else {
			List<QualityInspectionSpecifications> scopeSpecifications = qualityProvider.listAddAndModifyChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.COMMUNITY.getCode(), cmd.getTargetIds(), cmd.getSpecificationId(), null);
			specifications.addAll(scopeSpecifications);
			specifications.forEach(specification -> {
				CountScoresSpecificationDTO dto = new CountScoresSpecificationDTO();
				dto.setSpecificationId(specification.getId());
				dto.setSpecificationName(specification.getName());
				dto.setSpecificationInspectionType(specification.getInspectionType());
				dto.setSpecificationDescription(specification.getDescription());
				dto.setSpecificationScore(specification.getScore());
				dto.setSpecificationWeight(specification.getWeight());

				dtos.add(dto);
			});
		}
		return dtos;
	}

	@Override
	public CountScoresResponse countScores(CountScoresCommand cmd) {

		Long targetId = null;
		//add for new backend  20180124
		if (cmd.getAllFlag() != null && cmd.getAllFlag() == 1) {
			List<Long> targetIds = null;
			List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(cmd.getOwnerId());
			if (communities != null && communities.size() > 0) {
				targetIds = communities.stream().map(CommunityDTO::getId)
						.collect(Collectors.toList());
			}
			cmd.setTargetIds(targetIds);
		}
		if (cmd.getTargetIds() != null && cmd.getTargetIds().size() > 0)
			targetId = cmd.getTargetIds().get(0);

		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_STAT_SCORE, targetId);

		CountScoresResponse response = new CountScoresResponse();
		//查列 add by xiongying 20170425
		List<CountScoresSpecificationDTO> specificationDTOs = listChildSpecificationDTOs(cmd);
		response.setSpecifications(specificationDTOs);
		List<ScoreGroupByTargetDTO> scoresByTarget= new ArrayList<ScoreGroupByTargetDTO>();

		List<QualityInspectionSpecifications> specifications = new ArrayList<QualityInspectionSpecifications>();

		//查第一级的子结点
		if(cmd.getSpecificationId() == null || cmd.getSpecificationId() == 0L) {
			specifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.ALL.getCode(), 0L, 0L, null);
		} else {
			QualityInspectionSpecifications parent = verifiedSpecificationById(cmd.getSpecificationId(), cmd.getOwnerType(), cmd.getOwnerId(),null);
			specifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.ALL.getCode(), 0L, parent.getId(), null);
		}

		Map<Long,List<QualityInspectionSpecificationDTO>> targetSpecifitionTree = new HashMap<Long,List<QualityInspectionSpecificationDTO>>();

		if(cmd.getTargetIds() != null && cmd.getTargetIds().size() > 0) {
			for(Long target : cmd.getTargetIds()) {
				List<QualityInspectionSpecifications> scopeSpecifications = new ArrayList<QualityInspectionSpecifications>();

				if(cmd.getSpecificationId() == null || cmd.getSpecificationId() == 0L) {
					scopeSpecifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.COMMUNITY.getCode(), target, 0L, null);
				} else {
					scopeSpecifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.COMMUNITY.getCode(), target, cmd.getSpecificationId(), null);
				}

				//获得每个项目的specification tree
//				targetSpecifitionTree.put(target, dealWithScopeSpecifications(specifications, scopeSpecifications));

				List<QualityInspectionSpecificationDTO> specificationTree = dealWithScopeSpecifications(specifications, scopeSpecifications);

				ScoreGroupByTargetDTO scoreGroupDto = new ScoreGroupByTargetDTO();
				scoreGroupDto.setTargetId(target);
				Community community = communityProvider.findCommunityById(target);
				if(community != null) {
					scoreGroupDto.setTargetName(community.getName());
					scoreGroupDto.setBuildArea(community.getAreaSize());
				}

				if(specificationTree != null && specificationTree.size() > 0) {
					List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
					for(QualityInspectionSpecificationDTO dto : specificationTree) {
						ScoreDTO score = new ScoreDTO();
						if(dto.getId() == null || dto.getId() == 0L) {
							String superiorPath = "%";
							score = qualityProvider.countScores(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), target, superiorPath, cmd.getStartTime(), cmd.getEndTime(), null);
						} else {
							QualityInspectionSpecifications parent = verifiedSpecificationById(dto.getId(), cmd.getOwnerType(), cmd.getOwnerId(),null);
							String superiorPath = parent.getPath() + "%";
							score = qualityProvider.countScores(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), target, superiorPath, cmd.getStartTime(), cmd.getEndTime(), null);
						}

						if(score != null) {
							score.setSpecificationId(dto.getId());
							score.setSpecificationName(dto.getName());
							score.setSpecificationInspectionType(dto.getInspectionType());
							score.setSpecificationScore(dto.getScore());
							score.setSpecificationWeight(dto.getWeight());

							score.setSpecificationDescription(dto.getDescription());

							//规范事项的话直接显示扣了多少分 by xiongying20170123
							if(SpecificationInspectionType.SPECIFICATION_ITEM.equals(SpecificationInspectionType.fromStatus(score.getSpecificationInspectionType()))) {
								score.setScore(score.getScore());
							} else {
								Double mark = (score.getSpecificationScore() - score.getScore()) * score.getSpecificationWeight();
								score.setScore(mark);
							}

						}
						scores.add(score);
					}

					scoreGroupDto.setScores(scores);
				}
				scoresByTarget.add(scoreGroupDto);
			}
		}

		response.setScores(scoresByTarget);
		return response;
	}

	@Override
	public CountTasksResponse countTasks(CountTasksCommand cmd) {
		/*Long privilegeId = configProvider.getLongValue(QualityConstant.QUALITY_STAT_TASK, 0L);
		if(cmd.getTargetId() != null) {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		}*/
		//针对检查统计和任务数统计 检查统计里面没有具体的项目id
		if (cmd.getCheckFlag() == null || !cmd.getCheckFlag().equals(true))
			checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_STAT_TASK, cmd.getTargetId());


		CountTasksResponse response = new CountTasksResponse();

		int offset = cmd.getOffset() == null ? 0 : cmd.getOffset();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		List<TaskCountDTO> tasks = qualityProvider.countTasks(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getTargetType(), cmd.getTargetId(), cmd.getStartTime(), cmd.getEndTime(),
				offset, pageSize+1, cmd.getSampleId());

		if(tasks != null && tasks.size() > pageSize) {
			tasks.remove(tasks.size() - 1);
			response.setOffset(offset + 1);
		}

		if(tasks != null) {
			for(TaskCountDTO task : tasks) {
				Community community = communityProvider.findCommunityById(task.getTargetId());
				if(community != null) {
					task.setTargetName(community.getName());
				}

				Double correctionRate =  ((double)(task.getInCorrection() + task.getDelayCorrection() + task.getCompleteCorrection()))/task.getTaskCount();
				task.setCorrectionRate(correctionRate);

				Double delayRate =  ((double)(task.getDelayCorrection() + task.getDelayInspection()))/task.getTaskCount();
				task.setDelayRate(delayRate);
			}
		}
		response.setTasks(tasks);
		return response;
	}

	@Override
	public QualityInspectionSpecificationDTO getQualitySpecification(
			GetQualitySpecificationCommand cmd) {
//		Long privilegeId = configProvider.getLongValue(QualityConstant.QUALITY_SPECIFICATION_LIST, 0L);
//		userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		QualityInspectionSpecifications specification = verifiedSpecificationById(cmd.getSpecificationId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getNamespaceId());
		QualityInspectionSpecificationDTO dto = ConvertHelper.convert(specification, QualityInspectionSpecificationDTO.class);
		List<QualityInspectionSpecifications> children = new ArrayList<QualityInspectionSpecifications>();
		List<QualityInspectionSpecificationDTO> childDTOs = new ArrayList<QualityInspectionSpecificationDTO>();

		if(SpecificationApplyPolicy.ADD.equals(SpecificationApplyPolicy.fromCode(dto.getApplyPolicy()))) {
			children = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), null, null, dto.getId(), (byte)2);
		}

		if(SpecificationApplyPolicy.MODIFY.equals(SpecificationApplyPolicy.fromCode(dto.getApplyPolicy()))) {
			children = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), null, null, dto.getReferId(), (byte)2);
		}

		if(children != null && children.size() > 0) {
			for(QualityInspectionSpecifications child : children) {
				QualityInspectionSpecificationDTO childDto = ConvertHelper.convert(child, QualityInspectionSpecificationDTO.class);
				childDTOs.add(childDto);
			}
		}

		dto.setChildrens(childDTOs);
		return dto;
	}

	@Override
	public ListQualityInspectionTasksResponse listUserHistoryTasks(ListUserHistoryTasksCommand cmd) {
		ListQualityInspectionTasksResponse response = new ListQualityInspectionTasksResponse();

		Long uId = UserContext.current().getUser().getId();
//		Set<Long> taskIds = qualityProvider.listRecordsTaskIdByOperatorId(uId, cmd.getPageAnchor());
		Set<Long> taskIds = new HashSet<>();
//		List<Long> taskIdlist = new ArrayList<Long>();
//        for(final Long value : taskIds){
//
//        	taskIdlist.add(value);
//
//        }
//
//        Collections.sort(taskIdlist);
//		if(cmd.getPageAnchor() != null && cmd.getPageAnchor() != 0L) {
//			if(taskIdlist.contains(cmd.getPageAnchor())) {
//				int last = taskIdlist.indexOf(cmd.getPageAnchor());
//				taskIdlist = taskIdlist.subList(0, last);
//			}
//		}
//
//        Collections.reverse(taskIdlist);
		List<QualityInspectionTaskRecords> taskRecords = qualityProvider.listRecordsByOperatorId(uId, null);
		Map<Long, Timestamp> taskRecordTime = new HashMap<>();
		if(taskRecords != null ) {
			Long startContainTime = System.currentTimeMillis();
			for(QualityInspectionTaskRecords record : taskRecords) {
//				if(!taskIdlist.contains(record.getTaskId())) {
//					taskIdlist.add(record.getTaskId());
//				}

				Timestamp recordTime = taskRecordTime.get(record.getTaskId());
				if(recordTime == null) {
					taskRecordTime.put(record.getTaskId(),record.getCreateTime());
				}else if(recordTime.before(record.getCreateTime())) {
					taskRecordTime.put(record.getTaskId(),record.getCreateTime());
				}
			}

			taskIds = taskRecordTime.keySet();
			Long endContainTime = System.currentTimeMillis();
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("listUserHistoryTasks list contain taskRecords size = {}, taskIds size = {}, elapse = {}",
						taskRecords.size(), taskIds.size(), endContainTime-startContainTime);
			}
		}

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		Map<Long, Timestamp> sortedMap = new LinkedHashMap<Long, Timestamp>();
		List<Map.Entry<Long, Timestamp>> list = new ArrayList<Map.Entry<Long, Timestamp>>(taskRecordTime.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<Long, Timestamp>>() {
			public int compare(Map.Entry<Long, Timestamp> o1,
							   Map.Entry<Long, Timestamp> o2) {
				if(o2.getValue().after(o1.getValue()))
					return 1;
				return -1;
			}
		});

        if(taskIds.size() > pageSize) {
			list = list.subList(0,pageSize);
			response.setNextPageAnchor(list.get(list.size()-1).getKey());

//			taskIdlist = taskIdlist.subList(0,pageSize);
//        	response.setNextPageAnchor(taskIdlist.get(taskIdlist.size()-1));
        }

		Iterator<Map.Entry<Long, Timestamp>> iter = list.iterator();
		Map.Entry<Long, Timestamp> tmpEntry = null;
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}

		taskIds = sortedMap.keySet();

		List<QualityInspectionTasks> tasks = qualityProvider.listTaskByIds(taskIds);
		//去掉cache之后 改为一把取record 如果还是有问题那么不是cache的错 可能是record没有塞进去
		Map<Long, QualityInspectionTaskRecords> recordsMap = qualityProvider.listLastRecordByTaskIds(taskIds);

		List<QualityInspectionTaskRecords> records = new ArrayList<>();
        for(QualityInspectionTasks task : tasks) {
			QualityInspectionTaskRecords record = recordsMap.get(task.getId());
			if(record != null) {
				task.setRecord(recordsMap.get(task.getId()));
				records.add(recordsMap.get(task.getId()));
			}
        }

		this.qualityProvider.populateRecordAttachments(records);
		this.qualityProvider.populateRecordItemResults(records);

		for(QualityInspectionTaskRecords record : records) {
			populateRecordAttachements(record, record.getAttachments());
		}

		List<QualityInspectionTaskDTO> dtoList = convertQualityInspectionTaskToDTO(tasks, uId);
		Map<Long, QualityInspectionTaskDTO> tasksMap = new HashMap<>();
		dtoList.forEach(dto -> {
			tasksMap.put(dto.getId(), dto);
		});

		List<QualityInspectionTaskDTO> sortDto = sortedMap.entrySet().stream().map(map -> tasksMap.get(map.getKey())).collect(Collectors.toList());
		response.setTasks(sortDto);
		return response;
	}

	@Override
	public ListQualityInspectionTasksResponse listUserQualityInspectionTaskTemplates(
			ListUserQualityInspectionTaskTemplatesCommand cmd) {

		ListQualityInspectionTasksResponse response = new ListQualityInspectionTasksResponse();

		Long uId = UserContext.current().getUser().getId();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

		List<QualityInspectionTaskTemplates> templates = qualityProvider.listUserQualityInspectionTaskTemplates(locator, pageSize+1, uId);

		Long nextPageAnchor = null;
        if(templates.size() > pageSize) {
        	templates.remove(templates.size() - 1);
            nextPageAnchor = templates.get(templates.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);

        if(templates.size() > 0) {
        	List<QualityInspectionTasks> tasks = templates.stream().map(r -> {
        		QualityInspectionTasks task = ConvertHelper.convert(r, QualityInspectionTasks.class);

        		return task;
        	}).collect(Collectors.toList());

        	List<QualityInspectionTaskDTO> dtos = convertQualityInspectionTaskToDTO(tasks, null);

        	response.setTasks(dtos);
        }

		return response;
	}

	@Override
	public void deleteUserQualityInspectionTaskTemplate(
			DeleteUserQualityInspectionTaskTemplateCommand cmd) {
		QualityInspectionTaskTemplates template = qualityProvider.findQualityInspectionTaskTemplateById(cmd.getTemplateId());
		if(template != null) {
			if(template.getCreatorUid().equals(UserContext.current().getUser().getId())) {
				qualityProvider.deleteQualityInspectionTaskTemplates(cmd.getTemplateId());
			} else {
				LOGGER.info("template creator is " + template.getCreatorUid() + ", not equals to current user " + UserContext.current().getUser().getId() + " !");
				throw RuntimeErrorException
				.errorWith(
						QualityServiceErrorCode.SCOPE,
						QualityServiceErrorCode.ERROR_TEMPLATE_CREATOR,
						localeStringService.getLocalizedString(
								String.valueOf(QualityServiceErrorCode.SCOPE),
								String.valueOf(QualityServiceErrorCode.ERROR_TEMPLATE_CREATOR),
								UserContext.current().getUser().getLocale(),
								"the template creator is not current user!"));
			}

		} else {
			LOGGER.info("template is not exist !");
			throw RuntimeErrorException
			.errorWith(
					QualityServiceErrorCode.SCOPE,
					QualityServiceErrorCode.ERROR_TEMPLATE_NOT_EXIST,
					localeStringService.getLocalizedString(
							String.valueOf(QualityServiceErrorCode.SCOPE),
							String.valueOf(QualityServiceErrorCode.ERROR_TEMPLATE_NOT_EXIST),
							UserContext.current().getUser().getLocale(),
							"the template don't exist!"));
		}
	}

	@Override
	public SampleQualityInspectionDTO createSampleQualityInspection(CreateSampleQualityInspectionCommand cmd) {

		checkManager(cmd.getOwnerId(), cmd.getOwnerType(),cmd.getNamespaceId());

		QualityInspectionSamples sample = ConvertHelper.convert(cmd, QualityInspectionSamples.class);
		Long uid = UserContext.current().getUser().getId();
//		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Integer namespaceId = cmd.getNamespaceId();
		sample.setStartTime(new Timestamp(cmd.getStartTime()));
		sample.setEndTime(new Timestamp(cmd.getEndTime()));
		sample.setCreatorUid(uid);
		sample.setNamespaceId(namespaceId);
		sample.setStatus(Status.ACTIVE.getCode());
		qualityProvider.createQualityInspectionSample(sample);
		sampleSearcher.feedDoc(sample);
		LOGGER.debug("createSampleQualityInspection feedDoc complete time:{}"+System.currentTimeMillis());

		if(cmd.getCommunityIds() != null && cmd.getCommunityIds().size() > 0) {
			cmd.getCommunityIds().forEach(communityId -> {
				QualityInspectionSampleCommunityMap map = new QualityInspectionSampleCommunityMap();
				map.setNamespaceId(namespaceId);
				map.setSampleId(sample.getId());
				map.setCommunityId(communityId);
				qualityProvider.createQualityInspectionSampleCommunityMap(map);
			});
		}

		if(cmd.getExecuteGroupAndPositionList() != null && cmd.getExecuteGroupAndPositionList().size() > 0) {
			cmd.getExecuteGroupAndPositionList().forEach(execute -> {
				QualityInspectionSampleGroupMap map = new QualityInspectionSampleGroupMap();
				map.setNamespaceId(namespaceId);
				map.setSampleId(sample.getId());
				map.setOrganizationId(execute.getGroupId());
				map.setPositionId(execute.getPositionId());
				qualityProvider.createQualityInspectionSampleGroupMap(map);
			});
		}

		SampleQualityInspectionDTO dto = convertQualityInspectionSampleToDTO(sample);
		return dto;
	}

	@Override
	public void deleteSampleQualityInspection(FindSampleQualityInspectionCommand cmd) {

		checkManager(cmd.getOwnerId(), cmd.getOwnerType(),cmd.getNamespaceId());

		List<QualityInspectionTasks> tasks = qualityProvider.listTaskByParentId(cmd.getId());
		if(tasks != null && tasks.size() > 0) {
			LOGGER.error("the sample which id="+cmd.getId()+" has generate tasks!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_SAMPLE_HAS_TASK,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_SAMPLE_HAS_TASK),
									UserContext.current().getUser().getLocale(),
									"the sample has generate tasks!"));

		}
		QualityInspectionSamples sample = qualityProvider.findQualityInspectionSample(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
		if(sample == null || Status.INACTIVE.equals(Status.fromStatus(sample.getStatus()))) {
			LOGGER.error("the sample which id="+cmd.getId()+" is not exist!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_SAMPLE_NOT_EXIST,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_SAMPLE_NOT_EXIST),
									UserContext.current().getUser().getLocale(),
									"the sample is not exist!"));
		}
		sample.setStatus(Status.INACTIVE.getCode());
		sample.setDeleteUid(UserContext.current().getUser().getId());
		sample.setDeleteTime(new Timestamp(System.currentTimeMillis()));
		qualityProvider.updateQualityInspectionSample(sample);
		sampleSearcher.feedDoc(sample);

	}

	@Override
	public SampleQualityInspectionDTO findSampleQualityInspection(FindSampleQualityInspectionCommand cmd) {
		QualityInspectionSamples sample = qualityProvider.findQualityInspectionSample(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
		if(sample == null || Status.INACTIVE.equals(Status.fromStatus(sample.getStatus()))) {
			LOGGER.error("the sample which id="+cmd.getId()+" is not exist!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_SAMPLE_NOT_EXIST,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_SAMPLE_NOT_EXIST),
									UserContext.current().getUser().getLocale(),
									"the sample is not exist!"));
		}
		SampleQualityInspectionDTO dto = convertQualityInspectionSampleToDTO(sample);
		return dto;
	}

	@Override
	public ListSampleQualityInspectionResponse listSampleQualityInspection(ListSampleQualityInspectionCommand cmd) {

		User user = UserContext.current().getUser();
		Long ownerId = cmd.getOwnerId();
		String ownerType = cmd.getOwnerType();
		Long communityId = cmd.getCommunityId();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		//是否是管理员
//		boolean isAdmin = false;
//		List<RoleAssignment> resources = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOwnerId(), EntityType.USER.getCode(), user.getId());
//		if(null != resources && 0 != resources.size()){
//			for (RoleAssignment resource : resources) {
//				if(resource.getRoleId() == RoleConstants.ENTERPRISE_SUPER_ADMIN
//						|| resource.getRoleId() == RoleConstants.ENTERPRISE_ORDINARY_ADMIN
//						|| resource.getRoleId() == RoleConstants.PM_SUPER_ADMIN
//						|| resource.getRoleId() == RoleConstants.PM_ORDINARY_ADMIN) {
//					isAdmin = true;
//					break;
//				}
//			}
//		}
		boolean isAdmin = checkAdmin(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getNamespaceId());
		List<QualityInspectionSamples> samples = new ArrayList<>();

		if(isAdmin) {
			//管理员查询所有检查
			samples = qualityProvider.
					listActiveQualityInspectionSamples(locator, pageSize + 1, ownerType, ownerId, null, communityId, cmd.getLastUpdateSyncTime());
		} else {
			List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
			List<QualityInspectionSampleGroupMap> maps = qualityProvider.listQualityInspectionSampleGroupMapByOrgAndPosition(groupDtos);
			if(maps != null && maps.size() > 0) {
				List<Long> sampleIds = maps.stream().map(QualityInspectionSampleGroupMap::getSampleId).collect(Collectors.toList());

				samples = qualityProvider.
						listActiveQualityInspectionSamples(locator, pageSize + 1, ownerType, ownerId, sampleIds, communityId, cmd.getLastUpdateSyncTime());
			}
		}

		Long nextPageAnchor = null;
		if(samples.size() > pageSize) {
			samples.remove(samples.size() - 1);
			nextPageAnchor = samples.get(samples.size() - 1).getId();
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		List<SampleQualityInspectionDTO> sampleQualityInspectionDTOList = samples.stream().map(sample -> {
			SampleQualityInspectionDTO dto = ConvertHelper.convert(sample, SampleQualityInspectionDTO.class);
			List<SampleCommunity> sampleCommunities = new ArrayList<>();
			SampleCommunity sc = new SampleCommunity();
			sc.setSampleId(sample.getId());
			sc.setCommunityId(cmd.getCommunityId());
			sc.setCommunityName(community.getName());
			sampleCommunities.add(sc);
			dto.setSampleCommunities(sampleCommunities);

			return dto;
		}).collect(Collectors.toList());

		ListSampleQualityInspectionResponse response = new ListSampleQualityInspectionResponse();
		response.setNextPageAnchor(nextPageAnchor);
		response.setSampleQualityInspectionDTOList(sampleQualityInspectionDTOList);

		return response;
	}

	@Override
	public SampleQualityInspectionDTO updateSampleQualityInspection(UpdateSampleQualityInspectionCommand cmd) {
		QualityInspectionSamples exist = qualityProvider.findQualityInspectionSample(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
		if(exist == null || Status.INACTIVE.equals(Status.fromStatus(exist.getStatus()))) {
			LOGGER.error("the sample which id="+cmd.getId()+" is not exist!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_SAMPLE_NOT_EXIST,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_SAMPLE_NOT_EXIST),
									UserContext.current().getUser().getLocale(),
									"the sample is not exist!"));
		}

		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(now.after(exist.getStartTime())) {
			LOGGER.error("the sample which id="+cmd.getId()+" has start!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_SAMPLE_START,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_SAMPLE_START),
									UserContext.current().getUser().getLocale(),
									"the sample has start!"));
		}

		QualityInspectionSamples sample = ConvertHelper.convert(cmd, QualityInspectionSamples.class);
		sample.setStartTime(new Timestamp(cmd.getStartTime()));
		sample.setEndTime(new Timestamp(cmd.getEndTime()));
		sample.setStatus(exist.getStatus());
		sample.setNamespaceId(exist.getNamespaceId());
		sample.setSampleNumber(exist.getSampleNumber());
		sample.setCreateTime(exist.getCreateTime());
		sample.setCreatorUid(exist.getCreatorUid());
		qualityProvider.updateQualityInspectionSample(sample);
		sampleSearcher.feedDoc(sample);

		List<QualityInspectionSampleCommunityMap> sampleCommunityMaps = qualityProvider.findQualityInspectionSampleCommunityMapBySample(sample.getId());
		List<Long> communityIds = new ArrayList<>();
		if(sampleCommunityMaps != null && sampleCommunityMaps.size() > 0) {
			sampleCommunityMaps.forEach(map -> {
				communityIds.add(map.getCommunityId());
			});
		}

		//看map表中的communityId在不在cmd里面
		if(cmd.getCommunityIds() != null && cmd.getCommunityIds().size() > 0) {
			cmd.getCommunityIds().forEach(communityId -> {
				if(communityIds.contains(communityId)) {
					communityIds.remove(communityId);
					return;
				}
				//不在map表中的create
				QualityInspectionSampleCommunityMap map = new QualityInspectionSampleCommunityMap();
				map.setNamespaceId(sample.getNamespaceId());
				map.setSampleId(sample.getId());
				map.setCommunityId(communityId);
				qualityProvider.createQualityInspectionSampleCommunityMap(map);
			});
		}

		//不在cmd的删掉
		if(communityIds.size() > 0) {
			communityIds.forEach(communityId -> {
				QualityInspectionSampleCommunityMap map = qualityProvider.findQualityInspectionSampleCommunityMapBySampleAndCommunity(sample.getId(), communityId);
				qualityProvider.deleteQualityInspectionSampleCommunityMap(map);
			});
		}

		List<QualityInspectionSampleGroupMap> sampleGroupMaps = qualityProvider.findQualityInspectionSampleGroupMapBySample(sample.getId());
		List<ExecuteGroupAndPosition> sampleGroupAndPositions = new ArrayList<>();
		if(sampleGroupMaps != null && sampleGroupMaps.size() > 0) {
			sampleGroupMaps.forEach(map -> {
				ExecuteGroupAndPosition eap = new ExecuteGroupAndPosition();
				eap.setPositionId(map.getPositionId());
				eap.setGroupId(map.getOrganizationId());
				sampleGroupAndPositions.add(eap);
			});
		}

		if(cmd.getExecuteGroupAndPositionList() != null && cmd.getExecuteGroupAndPositionList().size() > 0) {
			cmd.getExecuteGroupAndPositionList().forEach(execute -> {
				if(sampleGroupAndPositions.contains(execute)) {
					sampleGroupAndPositions.remove(execute);
					return;
				}
				QualityInspectionSampleGroupMap map = new QualityInspectionSampleGroupMap();
				map.setNamespaceId(sample.getNamespaceId());
				map.setSampleId(sample.getId());
				map.setOrganizationId(execute.getGroupId());
				map.setPositionId(execute.getPositionId());
				qualityProvider.createQualityInspectionSampleGroupMap(map);
			});
		}

		if(sampleGroupAndPositions.size() > 0) {
			sampleGroupAndPositions.forEach(gap -> {
				QualityInspectionSampleGroupMap map = qualityProvider.findQualityInspectionSampleGroupMapBySampleAndOrg(sample.getId(), gap.getGroupId(), gap.getPositionId());
				qualityProvider.deleteQualityInspectionSampleGroupMap(map);
			});
		}

		SampleQualityInspectionDTO dto = convertQualityInspectionSampleToDTO(sample);
		return dto;
	}

	private SampleQualityInspectionDTO convertQualityInspectionSampleToDTO(QualityInspectionSamples sample) {
		SampleQualityInspectionDTO dto = ConvertHelper.convert(sample, SampleQualityInspectionDTO.class);
		OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(sample.getCreatorUid(), sample.getOwnerId());
		if(member != null) {
			dto.setCreatorName(member.getContactName());
		}
		List<QualityInspectionSampleCommunityMap> sampleCommunityMaps = qualityProvider.findQualityInspectionSampleCommunityMapBySample(sample.getId());
		if(sampleCommunityMaps != null && sampleCommunityMaps.size() > 0) {
			List<SampleCommunity> sampleCommunities = new ArrayList<>();
			List<Long> communityIds = sampleCommunityMaps.stream().map(QualityInspectionSampleCommunityMap::getCommunityId).collect(Collectors.toList());
			Map<Long, Community> communityMap = communityProvider.listCommunitiesByIds(communityIds);
			sampleCommunityMaps.forEach(map -> {
				SampleCommunity sc = new SampleCommunity();
				sc.setSampleId(sample.getId());
				sc.setCommunityId(map.getCommunityId());
				Community community = communityMap.get(map.getCommunityId());
				sc.setCommunityName(community.getName());
				sampleCommunities.add(sc);
			});

			dto.setSampleCommunities(sampleCommunities);
		}
		List<QualityInspectionSampleGroupMap> sampleGroupMaps = qualityProvider.findQualityInspectionSampleGroupMapBySample(sample.getId());

		if(sampleGroupMaps != null && sampleGroupMaps.size() > 0) {
			List<SampleGroupDTO> sampleGroupDTOs = new ArrayList<>();
			sampleGroupMaps.forEach(map -> {
				SampleGroupDTO sampleGroupDTO = new SampleGroupDTO();
				sampleGroupDTO.setOrganizationId(map.getOrganizationId());
				sampleGroupDTO.setPositionId(map.getPositionId());
				Organization group = organizationProvider.findOrganizationById(map.getOrganizationId());
				OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(map.getPositionId());
				if(group != null) {
					sampleGroupDTO.setOrganizationName(group.getName());
				}

				if(position != null) {
					if(sampleGroupDTO.getOrganizationName() != null) {
						sampleGroupDTO.setOrganizationName(sampleGroupDTO.getOrganizationName() + "-" + position.getName());
					} else {
						sampleGroupDTO.setOrganizationName(position.getName());

					}
				}
				sampleGroupDTOs.add(sampleGroupDTO);
			});
			dto.setSampleGroupDTOs(sampleGroupDTOs);
		}

		return dto;
	}

	@Override
	public ListQualityInspectionTasksResponse listSampleQualityInspectionTasks(ListSampleQualityInspectionTasksCommand cmd) {
		SearchQualityTasksCommand command = ConvertHelper.convert(cmd, SearchQualityTasksCommand.class);
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		Long anchor = 0L;
		if(cmd.getPageAnchor() != null) {
			anchor = cmd.getPageAnchor();
		}
		command.setTargetId(cmd.getCommunityId());
		command.setTargetName(cmd.getCommunityName());
		command.setPageSize(pageSize);
		command.setPageAnchor(anchor);
		List<Long> taskIds = taskSearcher.query(command);

		ListQualityInspectionTasksResponse response = new ListQualityInspectionTasksResponse();
		if(taskIds.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
			taskIds.remove(taskIds.size() - 1);
        }
		List<QualityInspectionTasks> tasks = qualityProvider.listTaskByIds(taskIds);
		List<QualityInspectionTaskRecords> records = new ArrayList<QualityInspectionTaskRecords>();
		for(QualityInspectionTasks task : tasks) {
			QualityInspectionTaskRecords record = qualityProvider.listLastRecordByTaskId(task.getId());
			if(record != null) {
				task.setRecord(record);
				records.add(task.getRecord());
			}

		}

		this.qualityProvider.populateRecordAttachments(records);
		this.qualityProvider.populateRecordItemResults(records);

		for(QualityInspectionTaskRecords record : records) {
			populateRecordAttachements(record, record.getAttachments());
		}

		List<QualityInspectionTaskDTO> dtoList = convertQualityInspectionTaskToDTO(tasks, UserContext.current().getUser().getId());
		response.setTasks(dtoList);
		QualityInspectionSamples sample = qualityProvider.findQualityInspectionSample(cmd.getSampleId(), cmd.getOwnerType(), cmd.getOwnerId());
		if(sample != null) {
			response.setSampleName(sample.getName());
			response.setStartTime(sample.getStartTime());
			response.setEndTime(sample.getEndTime());
		}

		Integer communityCount = qualityProvider.getSampleCommunities(cmd.getSampleId());
		response.setCommunityCount(communityCount);
		return response;
	}

	@Override
	public CountScoresResponse countSampleTaskCommunityScores(CountSampleTaskCommunityScoresCommand cmd) {
		CountScoresResponse response = new CountScoresResponse();
		//查列
		CountScoresCommand command = ConvertHelper.convert(cmd, CountScoresCommand.class);
		List<CountScoresSpecificationDTO> specificationDTOs = listChildSpecificationDTOs(command);
		response.setSpecifications(specificationDTOs);
		List<ScoreGroupByTargetDTO> scoresByTarget= new ArrayList<ScoreGroupByTargetDTO>();

		List<QualityInspectionSpecifications> specifications = new ArrayList<QualityInspectionSpecifications>();

		//查第一级的子结点
		if(cmd.getSpecificationId() == null || cmd.getSpecificationId() == 0L) {
			specifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.ALL.getCode(), 0L, 0L, null);
		} else {
			QualityInspectionSpecifications parent = verifiedSpecificationById(cmd.getSpecificationId(), null, null,null);
			specifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.ALL.getCode(), 0L, parent.getId(), null);
		}

		Map<Long,List<QualityInspectionSpecificationDTO>> targetSpecifitionTree = new HashMap<Long,List<QualityInspectionSpecificationDTO>>();

		if(cmd.getTargetIds() != null && cmd.getTargetIds().size() > 0) {
			for(Long target : cmd.getTargetIds()) {
				List<QualityInspectionSpecifications> scopeSpecifications = new ArrayList<QualityInspectionSpecifications>();

				if(cmd.getSpecificationId() == null || cmd.getSpecificationId() == 0L) {
					scopeSpecifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.COMMUNITY.getCode(), target, 0L, null);
				} else {
					scopeSpecifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.COMMUNITY.getCode(), target, cmd.getSpecificationId(), null);
				}

				//获得每个项目的specification tree
//				targetSpecifitionTree.put(target, dealWithScopeSpecifications(specifications, scopeSpecifications));

				List<QualityInspectionSpecificationDTO> specificationTree = dealWithScopeSpecifications(specifications, scopeSpecifications);

				ScoreGroupByTargetDTO scoreGroupDto = new ScoreGroupByTargetDTO();
				scoreGroupDto.setTargetId(target);
				scoreGroupDto.setTotalScore(0D);
				Community community = communityProvider.findCommunityById(target);
				if(community != null) {
					scoreGroupDto.setTargetName(community.getName());
					//add for order
					//换成areaSize字段 20171208
					//scoreGroupDto.setBuildArea(community.getBuildArea());
					scoreGroupDto.setBuildArea(community.getAreaSize());
				}

				if(specificationTree != null && specificationTree.size() > 0) {
					List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
					for(QualityInspectionSpecificationDTO dto : specificationTree) {
						ScoreDTO score = new ScoreDTO();
						if(dto.getId() == null || dto.getId() == 0L) {
							String superiorPath = "%";
							score = qualityProvider.countScores(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), target, superiorPath, null, null, cmd.getSampleId());
						} else {
							QualityInspectionSpecifications parent = verifiedSpecificationById(dto.getId(), cmd.getOwnerType(), cmd.getOwnerId(),null);
							String superiorPath = parent.getPath() + "%";
							score = qualityProvider.countScores(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), target, superiorPath, null, null, cmd.getSampleId());
						}

						if(score != null) {
							score.setSpecificationId(dto.getId());
							score.setSpecificationName(dto.getName());
							score.setSpecificationInspectionType(dto.getInspectionType());
							score.setSpecificationScore(dto.getScore());
							score.setSpecificationWeight(dto.getWeight());

							score.setSpecificationDescription(dto.getDescription());

							//规范事项的话直接显示扣了多少分 by xiongying20170123
							if(SpecificationInspectionType.SPECIFICATION_ITEM.equals(SpecificationInspectionType.fromStatus(score.getSpecificationInspectionType()))) {
								score.setScore(score.getScore());
							} else {
								Double mark = (score.getSpecificationScore() - score.getScore()) * score.getSpecificationWeight();
								score.setScore(mark);
							}
						}
						scores.add(score);
					}

					scoreGroupDto.setScores(scores);
					//fix order
					Double totalScore = 0D;
					if (scores.size() > 0) {
						for (ScoreDTO score : scores) {
							if (score.getScore() == null) {
								score.setScore(0D);
							}
							totalScore = totalScore + score.getScore();
						}
					}
					scoreGroupDto.setTotalScore(totalScore);
				}
				scoresByTarget.add(scoreGroupDto);
			}
		}
		if (scoresByTarget.size() > 0) {
			// 现网数据有很多建筑面积null
			scoresByTarget.forEach((s) -> {
				if (s.getBuildArea() == null)
					s.setBuildArea(0D);
				if (s.getTotalScore() == null)
					s.setTotalScore(0D);
			});
			//sort  scoreByTarget
			LOGGER.debug("scoresByTarget:{}", scoresByTarget);
			scoresByTarget.sort((o1, o2) -> {
				if (!o1.getTotalScore().equals(o2.getTotalScore())) {
					return o2.getTotalScore().compareTo(o1.getTotalScore());
				} else {
					return o2.getBuildArea().compareTo(o1.getBuildArea());
				}
			});
			//add orderId for  ScoresByTarget
			Integer previousOrder = 1;
			for (int i = 0; i < scoresByTarget.size(); i++) {
				if (i == 0) {
					scoresByTarget.get(i).setOrderId(previousOrder);
				} else {
					if (scoresByTarget.get(i).getBuildArea().equals(scoresByTarget.get(i - 1).getBuildArea()) &&
							scoresByTarget.get(i).getTotalScore().equals(scoresByTarget.get(i - 1).getTotalScore())) {
						scoresByTarget.get(i).setOrderId(previousOrder);
					} else {
						scoresByTarget.get(i).setOrderId(++previousOrder);
					}

				}
			}

		}

		response.setScores(scoresByTarget);
		//计算平均分 临时解决平均分的问题
		calculateAverageScore(response);
		return response;
	}

	private void calculateAverageScore(CountScoresResponse response) {
		Double sum = 0D;
		Double averageScore = 0D;
		if (response.getScores() != null && response.getScores().size() > 0) {
			for (ScoreGroupByTargetDTO score : response.getScores()) {
				sum = sum + score.getTotalScore();
			}
			averageScore = sum / response.getScores().size();
		}

		response.setAverageScore((double) Math.round(1.00*averageScore*100)/100);
	}

	@Override
	public CountSampleTasksResponse countSampleTasks(CountSampleTasksCommand cmd) {
		QualityInspectionSampleScoreStat stat = qualityProvider.findQualityInspectionSampleScoreStat(cmd.getSampleId());
		if(stat != null) {
			getNewestScoreStat(stat);
		} else {
			stat = new QualityInspectionSampleScoreStat();
//			stat.setNamespaceId(UserContext.getCurrentNamespaceId());
			stat.setNamespaceId(cmd.getNamespaceId());
			List<QualityInspectionSampleCommunityMap> communityMaps = qualityProvider.findQualityInspectionSampleCommunityMapBySample(cmd.getSampleId());
			if(communityMaps != null) {
				stat.setCommunityCount(communityMaps.size());
			} else {
				stat.setCommunityCount(0);
			}

			stat.setOwnerId(cmd.getOwnerId());
			stat.setOwnerType(cmd.getOwnerType());
			stat.setSampleId(cmd.getSampleId());
			stat.setTaskCount(0);
			stat.setCorrectionCount(0);
			stat.setCorrectionQualifiedCount(0);
			stat.setDeductScore(0.0);
			stat.setHighestScore(100.0);
			stat.setLowestScore(100.0);

			getNewestScoreStat(stat);
		}
		CountSampleTasksResponse response = ConvertHelper.convert(stat, CountSampleTasksResponse.class);
		if(stat.getCorrectionCount() == 0) {
			response.setCorrectionRate(0.0);
		} else {
			Double correctionRate = 1.00*stat.getCorrectionQualifiedCount()/stat.getCorrectionCount();
			response.setCorrectionRate(correctionRate);
		}
		Double averageScore = (100*stat.getCommunityCount() - stat.getDeductScore())/stat.getCommunityCount();
		if(averageScore < 0) {
			averageScore = 0.00;
		}
		response.setAverageScore((double)Math.round(1.00*averageScore*100)/100);
		QualityInspectionSamples sample = qualityProvider.findQualityInspectionSample(cmd.getSampleId(), cmd.getOwnerType(), cmd.getOwnerId());
		if(sample != null) {
			response.setName(sample.getName());
		}

		return response;
	}

	@Override
	public CountSampleTaskScoresResponse countSampleTaskScores(CountSampleTaskScoresCommand cmd) {
		// 不基于项目  但是使用项目id校验权限
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.QUALITY_STAT_SAMPLE, cmd.getTargetId());
		CountSampleTaskScoresResponse response = sampleSearcher.queryCount(cmd);
		List<SampleTaskScoreDTO> dtos = response.getSampleTasks();
		if(dtos != null && dtos.size() > 0) {
			response.setSampleTasks(dtos.stream().map(dto -> {
				QualityInspectionSampleScoreStat scoreStat = getSampleScoreStat(dto.getId(), dto.getOwnerType(),
						dto.getOwnerId(),cmd.getNamespaceId());
				dto.setCommunityCount(scoreStat.getCommunityCount());
				dto.setHighestScore(scoreStat.getHighestScore());
				dto.setLowestScore(scoreStat.getLowestScore());
				Double averageScore = (100*scoreStat.getCommunityCount() - scoreStat.getDeductScore())/scoreStat.getCommunityCount();
				if(averageScore < 0) {
					averageScore = 0.0;
				}
				dto.setAverageScore((double)Math.round(1.00*averageScore*100)/100);

				return dto;
			}).collect(Collectors.toList()));
		}
//		response.setSampleTasks(dtos);
		return response;
	}

	private QualityInspectionSampleScoreStat calculateTasks(QualityInspectionSampleScoreStat scoreStat, Timestamp now) {
		List<QualityInspectionTasks> tasks = qualityProvider.listQualityInspectionTasksBySample(scoreStat.getSampleId(), scoreStat.getUpdateTime(), now);

		if(tasks != null) {
			LOGGER.info("calculateTasks tasks: {}", tasks);
			scoreStat.setTaskCount(scoreStat.getTaskCount() + tasks.size());
			Integer correctionCount = 0;
			Integer correctionQualifiedCount = 0;
			for(QualityInspectionTasks task : tasks) {
				if(QualityInspectionTaskResult.CORRECT.equals(QualityInspectionTaskResult.fromStatus(task.getResult()))
						|| QualityInspectionTaskResult.CORRECT_COMPLETE.equals(QualityInspectionTaskResult.fromStatus(task.getResult()))
						|| QualityInspectionTaskResult.CORRECT_DELAY.equals(QualityInspectionTaskResult.fromStatus(task.getResult()))) {
					correctionCount ++;
				}
				if(QualityInspectionTaskResult.CORRECT_COMPLETE.equals(QualityInspectionTaskResult.fromStatus(task.getResult()))) {
					correctionQualifiedCount ++;
				}
			}
			scoreStat.setCorrectionCount(scoreStat.getCorrectionCount() + correctionCount);
			Integer scoreStatCorrectionQualifiedCount = scoreStat.getCorrectionQualifiedCount() == null ? 0:scoreStat.getCorrectionQualifiedCount();
			scoreStat.setCorrectionQualifiedCount(correctionQualifiedCount + scoreStatCorrectionQualifiedCount);
		}

		return scoreStat;
	}

	private List<QualityInspectionSpecificationItemResults> getNewestScoreStat(QualityInspectionSampleScoreStat scoreStat) {
		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		calculateTasks(scoreStat, now);
		Map<Long, Double> querycommunitySpecificationStats = qualityProvider.listCommunityScore(scoreStat.getSampleId());
		if(querycommunitySpecificationStats.size() == 0) {
			QualityInspectionSamples sample = qualityProvider.findQualityInspectionSample(scoreStat.getSampleId(), scoreStat.getOwnerType(), scoreStat.getOwnerId());
			List<QualityInspectionSampleCommunityMap> communityMaps = qualityProvider.findQualityInspectionSampleCommunityMapBySample(scoreStat.getSampleId());
			if(communityMaps != null && communityMaps.size() > 0) {
				communityMaps.forEach(map -> {
					createQualityInspectionSampleCommunitySpecificationStat(map.getCommunityId(), sample);
				});
			}
			querycommunitySpecificationStats = qualityProvider.listCommunityScore(scoreStat.getSampleId());
		}
		Map<Long, Double> communitySpecificationStats = querycommunitySpecificationStats;
		//时间段内的扣分项
		List<QualityInspectionSpecificationItemResults> results = qualityProvider.listSpecifitionItemResultsBySampleId(scoreStat.getSampleId(), scoreStat.getUpdateTime(), now);

		LOGGER.info("deduct sample id:{}, start time : {}, results: {}",scoreStat.getSampleId(), scoreStat.getUpdateTime(), results);
		if (results != null && results.size() > 0) {
			List<Long> categoryIds = new ArrayList<>();
			results.forEach(result -> {
				String path = result.getSpecificationPath();
				String[] paths = path.split("/");
				categoryIds.add(Long.valueOf(paths[1]));
			});
			LOGGER.info("categoryIds: {}", categoryIds);
			//一把取出涉及到的类型
			Map<Long, QualityInspectionSpecifications> categories = qualityProvider.listSpecificationByIds(categoryIds);
			results.forEach(result -> {
				Double weight = 1.0;
				for(Long categoryId : categoryIds) {
					if(result.getSpecificationPath().contains(categoryId.toString())) {
						LOGGER.info("contains categoryid: {}", categoryId);
						if(categories.get(categoryId) != null) {
							LOGGER.info("get category by id: {}", categoryId);
							weight = categories.get(categoryId).getWeight();
						}
					}
				}

				//扣分等于实际扣分乘以占比
				LOGGER.info("result: {}, weight: {}", result, weight);
				Double statScore = communitySpecificationStats.get(result.getTargetId());
				if(statScore != null) {
					scoreStat.setDeductScore(scoreStat.getDeductScore() + (result.getTotalScore() * weight));
					statScore = statScore + (result.getTotalScore() * weight);
					communitySpecificationStats.put(result.getTargetId(), statScore);
				}

			});
		}
		//按map的value排序 第一个和最后一个是扣分最少和最多的项目
		Map<Long, Double> result = new LinkedHashMap<>();
		communitySpecificationStats.entrySet().stream().sorted(Comparator.comparing(e->e.getValue()))
				.forEach(e ->result.put(e.getKey(), e.getValue()));

		if(result != null && result.size() > 0) {
			scoreStat.setHighestScore(100-result.entrySet().iterator().next().getValue());
			if(scoreStat.getHighestScore() < 0) {
				scoreStat.setHighestScore(0.0);
			}
			Iterator<Map.Entry<Long, Double>> iterator = result.entrySet().iterator();
			Map.Entry<Long, Double> tail = null;
			while (iterator.hasNext()) {
				tail = iterator.next();
			}
			scoreStat.setLowestScore(100-tail.getValue());
			if(scoreStat.getLowestScore() < 0) {
				scoreStat.setLowestScore(0.0);
			}
		}

		return results;
	}

//	//按map的value排序
//	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
//		Map<K, V> result = new LinkedHashMap<>();
//		Stream<Map.Entry<K, V>> st = map.entrySet().stream();
//
//		st.sorted(Comparator.comparing(e -> e.getValue())).forEach(e -> result.put(e.getKey(), e.getValue()));
//
//		return result;
//	}

	private void createQualityInspectionSampleCommunitySpecificationStat(Long communityId, QualityInspectionSamples sample) {
		//查每个项目的第一级类型
		List<QualityInspectionSpecifications> specifications = qualityProvider.listChildrenSpecifications(sample.getOwnerType(), sample.getOwnerId(), SpecificationScopeCode.ALL.getCode(), 0L, 0L, SpecificationInspectionType.CATEGORY.getCode());
		List<QualityInspectionSpecifications> scopeSpecifications = qualityProvider.listChildrenSpecifications(sample.getOwnerType(), sample.getOwnerId(), SpecificationScopeCode.COMMUNITY.getCode(), communityId, 0L, SpecificationInspectionType.CATEGORY.getCode());
		List<QualityInspectionSpecificationDTO> dtos = dealWithScopeSpecifications(specifications, scopeSpecifications);
		if(dtos != null && dtos.size() > 0) {
			dtos.forEach(dto -> {
				QualityInspectionSampleCommunitySpecificationStat scss = new QualityInspectionSampleCommunitySpecificationStat();
				scss.setNamespaceId(sample.getNamespaceId());
				scss.setOwnerId(sample.getOwnerId());
				scss.setOwnerType(sample.getOwnerType());
				scss.setSampleId(sample.getId());
				scss.setCommunityId(communityId);
				scss.setSpecificationId(dto.getId());
				scss.setSpecificationPath(dto.getPath());
				scss.setDeductScore(0.0);
				qualityProvider.createQualityInspectionSampleCommunitySpecificationStat(scss);
			});
		}
	}

	//定时任务 扫上次到现在的task和itemresult表新建或者更新eh_quality_inspection_sample_score_stat
	@Override
	public void updateSampleScoreStat() {
		List<QualityInspectionSamples> samples = qualityProvider.listActiveQualityInspectionSamples(null);
		if(samples != null && samples.size() > 0) {
			Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
			Map<Long, QualityInspectionSamples> samplesMap =  new HashMap<>();
			samples.forEach(sample -> {
				samplesMap.put(sample.getId(), sample);
			});
			List<Long> sampleIds = samples.stream().map(QualityInspectionSamples::getId).collect(Collectors.toList());
			LOGGER.info("total sampleIds : {}", sampleIds);
			//查eh_quality_inspection_sample_community_specification_stat 没有的循环添加
			Map<Long, List<QualityInspectionSampleCommunitySpecificationStat>> sampleCommunitySpecificationStat = qualityProvider.listCommunitySpecifitionStatBySampleId(sampleIds);
			Map<Long, QualityInspectionSamples> unStatSamples = new HashMap<>();
			unStatSamples.putAll(samplesMap);
			if(sampleCommunitySpecificationStat != null && sampleCommunitySpecificationStat.size() > 0) {
				sampleCommunitySpecificationStat.entrySet().forEach(scss -> {
					unStatSamples.remove(scss.getKey());
				});
			}
			LOGGER.info("sampleIds not in eh_quality_inspection_sample_community_specification_stat: {}", unStatSamples);
			if(unStatSamples != null && unStatSamples.size() > 0) {
				unStatSamples.entrySet().forEach(unStatSample -> {
					List<QualityInspectionSampleCommunityMap> scms = qualityProvider.findQualityInspectionSampleCommunityMapBySample(unStatSample.getKey());
					if(scms != null && scms.size() > 0) {
						scms.forEach(scm -> {
							QualityInspectionSamples sample = unStatSample.getValue();
							createQualityInspectionSampleCommunitySpecificationStat(scm.getCommunityId(), sample);
						});
					}
				});
			}

			//查统计表
			Map<Long, QualityInspectionSamples> unStatSampleScore= new HashMap<>();
			unStatSampleScore.putAll(samplesMap);
			Map<Long, QualityInspectionSampleScoreStat> sampleScoreStatMaps = qualityProvider.getQualityInspectionSampleScoreStat(sampleIds);
			if(sampleScoreStatMaps != null && sampleScoreStatMaps.size() > 0) {
				//更新统计表
				sampleScoreStatMaps.entrySet().forEach(sampleScoreStatMap -> {
					QualityInspectionSampleScoreStat stat = sampleScoreStatMap.getValue();
					unStatSampleScore.remove(sampleScoreStatMap.getKey());
					samplesMap.remove(sampleScoreStatMap.getKey());
					List<QualityInspectionSpecificationItemResults> results = getNewestScoreStat(stat);
					stat.setUpdateTime(now);
					qualityProvider.updateQualityInspectionSampleScoreStat(stat);
					updateSampleCommunitySpecificationStat(results, now);
				});
			}
			LOGGER.info("sampleIds not in EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT: {}", unStatSampleScore);
			if(unStatSampleScore != null && unStatSampleScore.size() > 0) {
				unStatSampleScore.entrySet().forEach(unStatSample -> {
					QualityInspectionSamples sample = unStatSample.getValue();
					Long sampleId = unStatSample.getKey();
					QualityInspectionSampleScoreStat stat = new QualityInspectionSampleScoreStat();
					List<QualityInspectionSampleCommunityMap> communityMap = qualityProvider.findQualityInspectionSampleCommunityMapBySample(sampleId);
					stat.setNamespaceId(sample.getNamespaceId());
					stat.setTaskCount(0);
					stat.setCorrectionCount(0);
					stat.setOwnerType(sample.getOwnerType());
					stat.setOwnerId(sample.getOwnerId());
					stat.setSampleId(sampleId);
					stat.setDeductScore(0.0);
					stat.setHighestScore(100.0);
					stat.setLowestScore(100.0);
					if(communityMap != null && communityMap.size() > 0) {
						stat.setCommunityCount(communityMap.size());
					} else {
						stat.setCommunityCount(0);
					}
					List<QualityInspectionSpecificationItemResults> results = getNewestScoreStat(stat);
					qualityProvider.createQualityInspectionSampleScoreStat(stat);
					updateSampleCommunitySpecificationStat(results, now);
				});
			}

//			if(samplesMap != null && samplesMap.size() > 0) {
//				samplesMap.entrySet().forEach(sampleMap -> {
//					QualityInspectionSamples sample = sampleMap.getValue();
//					Long sampleId = sampleMap.getKey();
//					List<QualityInspectionSampleCommunityMap> communityMap = qualityProvider.findQualityInspectionSampleCommunityMapBySample(sampleId);
//					QualityInspectionSampleScoreStat stat = new QualityInspectionSampleScoreStat();
//					stat.setNamespaceId(sample.getNamespaceId());
//					stat.setTaskCount(0);
//					stat.setCorrectionCount(0);
//					stat.setOwnerType(sample.getOwnerType());
//					stat.setOwnerId(sample.getOwnerId());
//					stat.setSampleId(sampleId);
//					stat.setDeductScore(0.0);
//					stat.setHighestScore(100.0);
//					stat.setLowestScore(100.0);
//					if(communityMap != null && communityMap.size() > 0) {
//						stat.setCommunityCount(communityMap.size());
//					} else {
//						stat.setCommunityCount(0);
//					}
//					List<QualityInspectionSpecificationItemResults> results = getNewestScoreStat(stat);
//					qualityProvider.createQualityInspectionSampleScoreStat(stat);
//					updateSampleCommunitySpecificationStat(results, now);
//				});
//			}
		}
	}

	//定时任务 扫上次到现在的task和itemresult表新建或者更新eh_quality_inspection_sample_community_specification_stat的数据
	public void updateSampleCommunitySpecificationStat(List<QualityInspectionSpecificationItemResults> results, Timestamp now) {
		if(results != null) {
			Map<SampleCommunitySpecification, Double> statMaps = new HashMap<>();
			results.forEach(result -> {
				String specification = result.getSpecificationPath();
				String[] specificationIds = specification.substring(1,specification.length()).split("/");

				SampleCommunitySpecification scs = new SampleCommunitySpecification();
				scs.setCommunityId(result.getTargetId());
				scs.setSampleId(result.getSampleId());
				scs.setSpecificationId(Long.valueOf(specificationIds[0]));
				scs.setOwnerId(result.getOwnerId());
				scs.setOwnerType(result.getOwnerType());
				scs.setNamespaceId(result.getNamespaceId());

				if(statMaps.get(scs) == null) {
					statMaps.put(scs, result.getTotalScore());
				} else {
					statMaps.put(scs, statMaps.get(scs) + result.getTotalScore());
				}
			});

			if(statMaps != null && statMaps.size() > 0) {
				statMaps.entrySet().forEach(statMap -> {
					SampleCommunitySpecification scs = statMap.getKey();
					Double deductScore = statMap.getValue();
					QualityInspectionSampleCommunitySpecificationStat stat = qualityProvider.findBySampleCommunitySpecification(scs.getSampleId(),scs.getCommunityId(),scs.getSpecificationId());
					if(stat != null) {
						stat.setDeductScore(stat.getDeductScore() + deductScore);
						stat.setUpdateTime(now);
						qualityProvider.updateQualityInspectionSampleCommunitySpecificationStat(stat);
					} else {
						QualityInspectionSampleCommunitySpecificationStat newStat = ConvertHelper.convert(scs, QualityInspectionSampleCommunitySpecificationStat.class);
						newStat.setDeductScore(deductScore);
						qualityProvider.createQualityInspectionSampleCommunitySpecificationStat(newStat);

					}
				});
			}
		}
	}


	private QualityInspectionSampleScoreStat getSampleScoreStat(Long sampleId, String ownerType, Long ownerId, Integer namespaceId) {
		QualityInspectionSampleScoreStat scoreStat = qualityProvider.findQualityInspectionSampleScoreStat(sampleId);
		if(scoreStat != null) {
			getNewestScoreStat(scoreStat);
		} else {
			scoreStat = new QualityInspectionSampleScoreStat();
			//scoreStat.setNamespaceId(UserContext.getCurrentNamespaceId());
			scoreStat.setNamespaceId(namespaceId);
			List<QualityInspectionSampleCommunityMap> communityMaps = qualityProvider.findQualityInspectionSampleCommunityMapBySample(sampleId);
			if(communityMaps != null) {
				scoreStat.setCommunityCount(communityMaps.size());
			} else {
				scoreStat.setCommunityCount(0);
			}

			scoreStat.setOwnerId(ownerId);
			scoreStat.setOwnerType(ownerType);
			scoreStat.setSampleId(sampleId);
			scoreStat.setTaskCount(0);
			scoreStat.setCorrectionCount(0);
			scoreStat.setCorrectionQualifiedCount(0);
			scoreStat.setDeductScore(0.0);
			scoreStat.setHighestScore(100.0);
			scoreStat.setLowestScore(100.0);

			getNewestScoreStat(scoreStat);
		}
		return scoreStat;
	}

	@Override
	public CountSampleTaskSpecificationItemScoresResponse countSampleTaskSpecificationItemScores(CountSampleTaskSpecificationItemScoresCommand cmd) {
		CountSampleTaskSpecificationItemScoresResponse response = new CountSampleTaskSpecificationItemScoresResponse();
		List<SpecificationItemScores> itemScores = new ArrayList<>();
		QualityInspectionSampleScoreStat sampleScoreStat = qualityProvider.findQualityInspectionSampleScoreStat(cmd.getSampleId());
		Map<Long, Double> specificationScore = qualityProvider.listSpecificationScore(cmd.getSampleId());
		if(specificationScore == null || specificationScore.size() == 0) {
			List<QualityInspectionSampleCommunityMap> scms = qualityProvider.findQualityInspectionSampleCommunityMapBySample(cmd.getSampleId());
			if(scms != null && scms.size() > 0) {
				scms.forEach(scm -> {
//							查每个项目的第一级类型
					List<QualityInspectionSpecifications> specifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.ALL.getCode(), 0L, 0L, SpecificationInspectionType.CATEGORY.getCode());
					List<QualityInspectionSpecifications> scopeSpecifications = qualityProvider.listChildrenSpecifications(cmd.getOwnerType(), cmd.getOwnerId(), SpecificationScopeCode.COMMUNITY.getCode(), scm.getCommunityId(), 0L, SpecificationInspectionType.CATEGORY.getCode());
					List<QualityInspectionSpecificationDTO> dtos = dealWithScopeSpecifications(specifications, scopeSpecifications);
					if(dtos != null && dtos.size() > 0) {
						dtos.forEach(dto -> {
							specificationScore.put(dto.getId(), 0.0);
						});
					}
				});
			}
		}
		Timestamp lastTime = null;
		if(sampleScoreStat != null) {
			lastTime = sampleScoreStat.getUpdateTime();
		}
		List<QualityInspectionSpecificationItemResults> itemResults = qualityProvider.listSpecifitionItemResultsBySampleId(cmd.getSampleId(), lastTime, new Timestamp(System.currentTimeMillis()));
		List<Double> scoreList = new ArrayList<>();
		if(itemResults != null && itemResults.size() > 0) {
			itemResults.forEach(itemResult -> {
				String specificationPath = itemResult.getSpecificationPath();
				specificationPath = specificationPath.substring(1,specificationPath.length());
				String[] pathIds = specificationPath.split("/");

				Long rootSpecification = Long.valueOf(pathIds[0]);
				if(specificationScore.get(rootSpecification) != null) {
					specificationScore.put(rootSpecification, specificationScore.get(rootSpecification) + itemResult.getTotalScore());
				} else {
					specificationScore.put(rootSpecification, itemResult.getTotalScore());
				}
			});
		}

		specificationScore.entrySet().forEach(score -> {
			scoreList.add(score.getValue());
		});
		Double totalScore = 0.0;
		for(Double score : scoreList) {
			totalScore = totalScore + score;
		}

		Double totaldeducted = totalScore;
		if(specificationScore != null && specificationScore.size() > 0) {
			specificationScore.entrySet().forEach(map -> {
				Long specificationId = map.getKey();
				Double deductedScore = map.getValue();
				QualityInspectionSpecifications specification = qualityProvider.findSpecificationById(specificationId, cmd.getOwnerType(), cmd.getOwnerId());
				SpecificationItemScores score = new SpecificationItemScores();
				score.setSpecificationId(specificationId);
				score.setSpecificationDeducted(deductedScore);
				if(Double.compare(totaldeducted, 0.0) == 0) {
					score.setSpecificationDeductedProportion(0.0);
				} else {
					score.setSpecificationDeductedProportion(deductedScore/totaldeducted);
				}

				if(specification != null) {
					score.setSpecificationName(specification.getName());
				}

				itemScores.add(score);
			});
		}

		response.setItemScores(itemScores);
		return response;
	}
	private void checkUserPrivilege(Long orgId, Long privilegeId, Long communityId) {
		/*ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
		listServiceModuleAppsCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
		listServiceModuleAppsCommand.setModuleId(QualityConstant.QUALITY_MODULE);
		ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
		boolean flag = false;
		if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
			flag = userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(),
					orgId, orgId, privilegeId, apps.getServiceModuleApps().get(0).getId(), null, communityId);
			if (!flag) {
				LOGGER.error("Permission is denied, namespaceId={}, orgId={}, communityId={}," +
						" privilege={}", UserContext.getCurrentNamespaceId(), orgId, communityId, privilegeId);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
						"Insufficient privilege");
			}
		}*/
		userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, QualityConstant.QUALITY_MODULE, null, null, null,communityId);


	}

	private void checkManager(Long ownerId, String ownerType,Integer namespaceId) {
		ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
		//listServiceModuleAppsCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
		listServiceModuleAppsCommand.setNamespaceId(namespaceId);
		listServiceModuleAppsCommand.setModuleId(QualityConstant.QUALITY_MODULE);
		ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
		CheckModuleManageCommand checkModuleManageCommand = new CheckModuleManageCommand();
		checkModuleManageCommand.setModuleId(QualityConstant.QUALITY_MODULE);
		checkModuleManageCommand.setOrganizationId(ownerId);
		checkModuleManageCommand.setOwnerType(ownerType);
		checkModuleManageCommand.setUserId(UserContext.currentUserId());
		if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
			checkModuleManageCommand.setAppId(apps.getServiceModuleApps().get(0).getOriginId());
		}
		if (serviceModuleService.checkModuleManage(checkModuleManageCommand) == 0) {
			LOGGER.error("Permission is denied, namespaceId={}, orgId={},", namespaceId, ownerId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
					"权限不足");
		}
	}
	//用于替换旧的admin验证
	private Boolean checkAdmin(Long ownerId, String ownerType,Integer namespaceId) {
		ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
		listServiceModuleAppsCommand.setNamespaceId(namespaceId);
		listServiceModuleAppsCommand.setModuleId(QualityConstant.QUALITY_MODULE);
		ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
		CheckModuleManageCommand checkModuleManageCommand = new CheckModuleManageCommand();
		checkModuleManageCommand.setModuleId(QualityConstant.QUALITY_MODULE);
		checkModuleManageCommand.setOrganizationId(ownerId);
		checkModuleManageCommand.setOwnerType(ownerType);
		checkModuleManageCommand.setUserId(UserContext.currentUserId());
		if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
			checkModuleManageCommand.setAppId(apps.getServiceModuleApps().get(0).getOriginId());
		}
		return serviceModuleService.checkModuleManage(checkModuleManageCommand) != 0;
	}
	@Override
	public HttpServletResponse exportSampleTaskCommunityScores(CountSampleTaskCommunityScoresCommand cmd, HttpServletResponse httpResponse) {
		//处理targetId  String类型的
		processStringTargetIds(cmd);

		CountScoresResponse dataResponse = countSampleTaskCommunityScores(cmd);
		URL rootPath = QualityServiceImpl.class.getResource("/");
		String filePath = rootPath.getPath() + this.downloadDir;
		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();
		filePath = filePath + "qualityScores" + System.currentTimeMillis() + ".xlsx";
		//新建了一个文件
		this.createQualityScoreBook(filePath, dataResponse);

		return download(filePath, httpResponse);

	}

	private void processStringTargetIds(CountSampleTaskCommunityScoresCommand cmd) {
		//解析
		if (cmd.getTargetIdString() != null) {
			String targetStrings[] = cmd.getTargetIdString().split(",");
			List<Long> targetIds = new ArrayList<>();
			for (String targetId : targetStrings) {
				targetIds.add(Long.parseLong(targetId));
			}
			cmd.setTargetIds(targetIds);
		}
	}


	private void createQualityScoreBook(String path, CountScoresResponse dataResponse) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("qualityScores");

		this.createQualityScoreBookSheetHead(sheet, dataResponse.getSpecifications());
		List<ScoreGroupByTargetDTO> scores = dataResponse.getScores();
		for (ScoreGroupByTargetDTO dto : scores) {
			this.setNewQualityScoreBookRow(sheet, dto);
		}

		try {
			FileOutputStream out = new FileOutputStream(path);
			wb.write(out);
			wb.close();
			out.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(QualityServiceErrorCode.SCOPE,
					QualityServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}

	}

	private void createQualityScoreBookSheetHead(Sheet sheet, List<CountScoresSpecificationDTO> specifications) {

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i = -1;
		row.createCell(++i).setCellValue("排名");
		row.createCell(++i).setCellValue("项目名称");
		row.createCell(++i).setCellValue("项目面积(平米)");
		row.createCell(++i).setCellValue("加权得分");
		row.createCell(++i).setCellValue("加权扣分");
		for (CountScoresSpecificationDTO score : specifications) {
			row.createCell(++i).setCellValue(score.getSpecificationName()+score.getSpecificationWeight()*100+"%");
		}
	}

	private void setNewQualityScoreBookRow(Sheet sheet, ScoreGroupByTargetDTO dto) {
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		int i = -1;
		row.createCell(++i).setCellValue(dto.getOrderId());
		row.createCell(++i).setCellValue(dto.getTargetName());
		row.createCell(++i).setCellValue(dto.getBuildArea());
		row.createCell(++i).setCellValue(dto.getTotalScore());
		row.createCell(++i).setCellValue(dto.getTotalScore() > 100 ? dto.getTotalScore() - 100 : 100 - dto.getTotalScore());

		List<ScoreDTO> scores = dto.getScores();
		for (ScoreDTO score : scores) {
			row.createCell(++i).setCellValue(score.getScore());
		}
	}

	@Override
	public QualityOfflineTaskDetailsResponse getOfflineTaskDetail(ListQualityInspectionTasksCommand cmd) {
		//deal lastSyncTime  dast String to Timestamp
		if (cmd.getLastSyncTime() != null) {
			cmd.setLatestUpdateTime(dateStrToTimestamp(cmd.getLastSyncTime()));
		}
		// get personal related tasks detail
		ListQualityInspectionTasksResponse tasksResponse = listQualityInspectionTasks(cmd);

		QualityOfflineTaskDetailsResponse offlineTaskDetailsResponse = new QualityOfflineTaskDetailsResponse();
		offlineTaskDetailsResponse.setTasks(tasksResponse.getTasks());
		OfflineTaskCount executed = new OfflineTaskCount();
		// obtain today executed and total task count
		setTaskCountStat(cmd, tasksResponse, offlineTaskDetailsResponse, executed);
		offlineTaskDetailsResponse.setNextPageAnchor(tasksResponse.getNextPageAnchor());

		List<QualityInspectionSpecificationDTO> specifications = new ArrayList<>();
		List<Long> parentSpecificationIds = new ArrayList<>();
		List<QualityInspectionStandards> standards = new ArrayList<>();

		if(tasksResponse.getTasks()!=null && tasksResponse.getTasks().size()>0) {
			tasksResponse.getTasks().forEach((task) -> {
				// for offline support
				getMaxUpdatedTimeForTasks(task);
				QualityInspectionSpecifications specification = qualityProvider.getSpecificationById(task.getCategoryId());
				if (specification != null) {
					specifications.add(ConvertHelper.convert(specification, QualityInspectionSpecificationDTO.class));
				}
				QualityInspectionStandards standard = qualityProvider.findStandardById(task.getStandardId());
				if(standard!=null){
					standards.add(standard);
				}
			});
		}
		//remove duplicated specifications
		Map<Long, QualityInspectionSpecificationDTO> specificationDTOMap = new HashMap<>();
		specifications.forEach((s)-> specificationDTOMap.putIfAbsent(s.getId(), s));
		List<QualityInspectionSpecificationDTO> specificationList = null;
		if(specificationDTOMap.size()>0){
			specificationList = new ArrayList<>(specificationDTOMap.values());
			specificationList.forEach((s)->parentSpecificationIds.add(s.getId()));
		}
		//get children specifications which inspection type are two
		populateSpecificationDetails(specificationList, parentSpecificationIds);
		offlineTaskDetailsResponse.setSpecifications(specificationList);
		//处理组织架构人员
		processOrganizationsAndMembers(offlineTaskDetailsResponse, standards);
		//增加计划和groupId的关系表
		processPlansAndGroupRelations(offlineTaskDetailsResponse, standards);
		//增加类型
		processSpecifications(offlineTaskDetailsResponse, cmd.getTargetId(), cmd.getOwnerId());
		return offlineTaskDetailsResponse;
	}

	private void getMaxUpdatedTimeForTasks(QualityInspectionTaskDTO task) {
		List<Long> timestampList = new ArrayList<>();
		timestampList.add(task.getCreateTime().getTime());
		if (task.getExecutiveTime() != null) {
            timestampList.add(task.getExecutiveTime().getTime());
        }
		if (task.getProcessTime() != null) {
            timestampList.add(task.getProcessTime().getTime());
        }
		if (task.getReviewTime() != null) {
            timestampList.add(task.getReviewTime().getTime());
        }
		List<Long> temp = timestampList.stream()
                .sorted(Comparator.comparing(Long::longValue).reversed())
                .collect(Collectors.toList());
		if(temp!=null && temp.size()>0){
            task.setLastSyncTime(new Timestamp(temp.get(0)).toLocalDateTime().format(dateSF));
        }
	}

	private void setTaskCountStat(ListQualityInspectionTasksCommand cmd, ListQualityInspectionTasksResponse tasksResponse, QualityOfflineTaskDetailsResponse offlineTaskDetailsResponse, OfflineTaskCount executed) {
		executed.setId(Long.valueOf(new SimpleDateFormat("yyMMddhhmmssSSS").format(DateHelper.currentGMTTime())) * 10000);
		executed.setTargetId(cmd.getTargetId());
		executed.setCount(tasksResponse.getTodayExecutedCount().longValue());
		executed.setType((byte)0);
		OfflineTaskCount totalTaskCount = new OfflineTaskCount();
		totalTaskCount.setId(Long.valueOf(new SimpleDateFormat("yyMMddhhmmssSSS").format(DateHelper.currentGMTTime())) * 1000);
		totalTaskCount.setTargetId(cmd.getTargetId());
		totalTaskCount.setCount(tasksResponse.getTodayTotalCount().longValue());
		totalTaskCount.setType((byte)1);

		offlineTaskDetailsResponse.setTaskCount(Arrays.asList(totalTaskCount,executed));
	}

	private void processSpecifications(QualityOfflineTaskDetailsResponse offlineTaskDetailsResponse, Long targetId, Long ownerId) {
		//先查全公司的该节点下的所有子节点,再查该项目下的所有子节点
		QualityInspectionSpecifications parent = new QualityInspectionSpecifications();
		List<QualityInspectionSpecifications> specifications = new ArrayList<>();
		List<QualityInspectionSpecifications> scopeSpecifications = new ArrayList<>();
		parent.setId(0L);
		parent.setReferId(0L);
		specifications = qualityProvider.listAllChildrenSpecifications("/%", null, null, SpecificationScopeCode.ALL.getCode(), 0L, (byte) 0);
		scopeSpecifications = qualityProvider.listAllChildrenSpecifications("/%", null, null, SpecificationScopeCode.COMMUNITY.getCode(), targetId, (byte) 0);

		List<QualityInspectionSpecificationDTO> dtos = new ArrayList<>();
		//只有在项目中才会处理ALL和SCOPE
		dtos = dealWithScopeSpecifications(specifications, scopeSpecifications);

		QualityInspectionSpecificationDTO parentDto = ConvertHelper.convert(parent, QualityInspectionSpecificationDTO.class);
		parentDto = processQualitySpecificationTree(dtos, parentDto);
		dtos = parentDto.getChildrens();

		List<QualityInspectionSpecificationDTO> categories = new ArrayList<>();
		if (dtos != null && dtos.size() > 0) {
			addChildrensToCategories(dtos, categories);
		}
		offlineTaskDetailsResponse.setCategories(categories);
	}

	private void addChildrensToCategories(List<QualityInspectionSpecificationDTO> specificationDTOS, List<QualityInspectionSpecificationDTO> categories) {
		if (specificationDTOS != null && specificationDTOS.size() > 0) {
			specificationDTOS.forEach((s) -> {
				categories.add(s);
				if (s.getChildrens() != null && s.getChildrens().size() > 0) {
					addChildrensToCategories(s.getChildrens(), categories);
				}
			});
		}
	}

	/**
	 * 增加任务中的计划id和groupId
	 * @param offlineTaskDetailsResponse  离线结果
	 * @param standards  计划
	 */
	private void processPlansAndGroupRelations(QualityOfflineTaskDetailsResponse offlineTaskDetailsResponse, List<QualityInspectionStandards> standards) {
		List<QualityInspectionStandardGroupMapDTO> groupMapDTOS = new ArrayList<>();
		List<QualityInspectionStandardGroupMap> groupMaps = new ArrayList<>();
		if(standards!=null && standards.size()>0){
			standards.forEach((standard)-> groupMaps.addAll(standard.getExecutiveGroup()));
			groupMaps.forEach((groupMap)-> groupMapDTOS.add(ConvertHelper.convert(groupMap,QualityInspectionStandardGroupMapDTO.class)));
		}
		offlineTaskDetailsResponse.setGroupMaps(groupMapDTOS);
	}

	/**
	 * 返回所有任务相关的组织菜单和人员
	 *
	 * @param offlineTaskDetailsResponse 离线结果
	 */
	private void processOrganizationsAndMembers(QualityOfflineTaskDetailsResponse offlineTaskDetailsResponse, List<QualityInspectionStandards> standards) {
		if (standards != null && standards.size() > 0) {
			qualityProvider.populateStandardsGroups(standards);
			List<OrganizationDTO> organizationList = new ArrayList<>();
			List<OrganizationMemberDTO> memberList = new ArrayList<>();
			List<OfflineJobPositionDTO> jobPositions = new ArrayList<>();
			//先去重  standard里面的groupId相同的
			List<QualityInspectionStandardGroupMap> executiveGroups = removeDuplicatedStandardGroups(standards);
			if (executiveGroups != null && executiveGroups.size() > 0) {
				executiveGroups.forEach((executiveGroup) -> {
					if ((executiveGroup.getPositionId() == null || executiveGroup.getPositionId() == 0) && executiveGroup.getInspectorUid() == 0) {
						Organization group = organizationProvider.findOrganizationById(executiveGroup.getGroupId());
						List<String> groupTypes = new ArrayList<>();
						groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
						groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
						groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
						String groupPath = "";
						if (group != null) {
							groupPath = group.getPath();
						}
						List<Organization> organizations = organizationProvider.listOrganizationByGroupTypesAndPath(groupPath + "%", groupTypes, null, null, Integer.MAX_VALUE - 1);
						if (organizations != null) {
							organizations.forEach((o) -> {
								organizationList.add(ConvertHelper.convert(o, OrganizationDTO.class));
								List<OrganizationMember> members = organizationProvider.listOrganizationMembers(o.getId(), null);
								if (members != null && members.size() > 0) {
									memberList.addAll(members.stream().map((m) -> ConvertHelper.convert(m, OrganizationMemberDTO.class)).collect(Collectors.toList()));
								}
							});
						}
					} else if (executiveGroup.getInspectorUid() != 0) {
						List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(executiveGroup.getInspectorUid());
						if (members != null && members.size() > 0) {
							Organization group = organizationProvider.findOrganizationById(members.get(0).getOrganizationId());
							memberList.add(ConvertHelper.convert(members.get(0), OrganizationMemberDTO.class));
							if (group != null){
								organizationList.add(ConvertHelper.convert(group, OrganizationDTO.class));
							}
						}
					} else {
						//岗位下所有的人
						ListOrganizationContactByJobPositionIdCommand command = new ListOrganizationContactByJobPositionIdCommand();
						command.setOrganizationId(executiveGroup.getGroupId());
						command.setJobPositionId(executiveGroup.getPositionId());
						//包含岗位id和人员关系 targetID 为人员id
						List<OrganizationContactDTO> contacts = organizationService.listOrganizationContactByJobPositionId(command);
						List<OfflineJobPositionDTO> positions = new ArrayList<>();
						if (contacts != null && contacts.size() > 0) {
							contacts.forEach((c) -> {
								OfflineJobPositionDTO positionDTO = ConvertHelper.convert(c, OfflineJobPositionDTO.class);
								positionDTO.setStandardId(executiveGroup.getStandardId());
								positions.add(positionDTO);
							});
							jobPositions.addAll(positions);
						}
					}
				});
			}
			offlineTaskDetailsResponse.setOrganizations(organizationList);
			offlineTaskDetailsResponse.setOrganizationMembers(memberList);
			offlineTaskDetailsResponse.setJobPositions(jobPositions);
		}
	}

	private List<QualityInspectionStandardGroupMap> removeDuplicatedStandardGroups(List<QualityInspectionStandards> standards) {
		if (standards != null && standards.size() > 0) {
			List<QualityInspectionStandardGroupMap> executiveGroupMaps = new ArrayList<>();
			standards.forEach((standard) -> {
				if (standard.getExecutiveGroup() != null && standard.getExecutiveGroup().size() > 0) {
					executiveGroupMaps.addAll(standard.getExecutiveGroup());
				}
			});
			Map<String, QualityInspectionStandardGroupMap> groupDTOMap = new HashMap<>();
			if (executiveGroupMaps.size() > 0) {
				executiveGroupMaps.forEach((map) -> groupDTOMap.putIfAbsent(map.getGroupId().toString() +"-"+map.getPositionId().toString(), map));
			}
			if(groupDTOMap.size()>0){
				return new ArrayList<>(groupDTOMap.values());
			}
		}
		return null;
	}

	/**
	 * 增加具体类型下的规范
	 * @param specifications  总结果集合包括类型和规范
	 * @param parentSpecificationIds  父级
	 */
	private void populateSpecificationDetails(List<QualityInspectionSpecificationDTO> specifications, List<Long> parentSpecificationIds) {
		if (specifications != null && parentSpecificationIds != null && parentSpecificationIds.size() > 0) {
			List<QualityInspectionSpecificationDTO> qualityInspectionSpecificationDTOS = new ArrayList<>();// items
			parentSpecificationIds.forEach((d) -> {
				GetQualitySpecificationCommand command = new GetQualitySpecificationCommand();
				command.setSpecificationId(d);
				qualityInspectionSpecificationDTOS.add(getQualitySpecification(command));
			});
			List<QualityInspectionSpecificationDTO> details = new ArrayList<>();
			qualityInspectionSpecificationDTOS.forEach(q -> {
				if (q.getChildrens() != null && q.getChildrens().size() > 0)
					details.addAll(q.getChildrens());
			});
			if (details.size() > 0) {
				specifications.addAll(details);
			}
		}
	}

	private Timestamp dateStrToTimestamp(String str) {
		LocalDate localDate = LocalDate.parse(str,dateSF);
		return  new Timestamp(Date.valueOf(localDate).getTime());
	}

	@Override
	public OfflineSampleQualityInspectionResponse getOfflineSampleQualityInspection(ListSampleQualityInspectionCommand cmd) {
		OfflineSampleQualityInspectionResponse offlineResponse = new OfflineSampleQualityInspectionResponse();
		//处理时间戳
		cmd.setLastUpdateSyncTime(dateStrToTimestamp(cmd.getLastSyncTime()));
		ListSampleQualityInspectionResponse sampleQualityInspectionResponse = listSampleQualityInspection(cmd);
		List<SampleQualityInspectionDTO> sampleQualityInspections = new ArrayList<>();
		if (sampleQualityInspectionResponse != null){
			sampleQualityInspections = sampleQualityInspectionResponse.getSampleQualityInspectionDTOList();
		}
		//绩效任务列表 offline  Timestamp
		sampleQualityInspections.forEach((s) -> {
			if (s.getDeleteTime() == null) {
				if (s.getUpdateTime() == null) {
					s.setLastSyncTime(s.getCreateTime().toLocalDateTime().format(dateSF));
				} else {
					s.setLastSyncTime(s.getUpdateTime().toLocalDateTime().format(dateSF));
				}
			} else {
				s.setLastSyncTime(s.getDeleteTime().toLocalDateTime().format(dateSF));
			}
		});

		offlineResponse.setSampleQualityInspections(sampleQualityInspections);

        //绩效选择类型及规范缓存
		ListQualitySpecificationsCommand specificationsCommand = new ListQualitySpecificationsCommand();
		specificationsCommand.setScopeCode(SpecificationScopeCode.COMMUNITY.getCode());
		specificationsCommand.setScopeId(cmd.getCommunityId());
		specificationsCommand.setOwnerId(cmd.getOwnerId());
		specificationsCommand.setOwnerType(cmd.getOwnerType());
		specificationsCommand.setInspectionType(SpecificationInspectionType.CATEGORY.getCode());
		//这里增加了按syncTime过滤  offline  所有的类型
		ListQualitySpecificationsResponse listQualitySpecificationsResponse = listQualitySpecifications(specificationsCommand);
		if (listQualitySpecificationsResponse != null)
		offlineResponse.setSpecifications(listQualitySpecificationsResponse.getSpecifications());

		//拿到所有类型的id  从id拿到下面的规范items
		List<Long> parentIds = new ArrayList<>();
		if (listQualitySpecificationsResponse != null && listQualitySpecificationsResponse.getSpecifications() != null) {
			listQualitySpecificationsResponse.getSpecifications().forEach((s) -> {
				parentIds.add(s.getId());
				if(s.getChildrens()!=null){
					addIds(parentIds,s.getChildrens());
				}
			});
		}
		//拿所有的规范
		List<QualityInspectionSpecifications> qualityInspectionSpecifications = qualityProvider.listSpecifitionByParentIds(parentIds);
		List<QualityInspectionSpecificationDTO> specificationsDetail = new ArrayList<>();
		List<Long> detailsId = new ArrayList<>();
		if (qualityInspectionSpecifications != null && qualityInspectionSpecifications.size() > 0) {
			qualityInspectionSpecifications.forEach((q) -> {
						specificationsDetail.add(ConvertHelper.convert(q, QualityInspectionSpecificationDTO.class));
						detailsId.add(q.getId());
					}
			);
		}
		offlineResponse.getSpecifications().addAll(specificationsDetail);//所有的类型和规范放在一起
		List<QualityInspectionSpecificationDTO> qualityInspectionSpecificationDTOS = new ArrayList<>();// items
		detailsId.forEach((d)->{
			GetQualitySpecificationCommand command = new GetQualitySpecificationCommand();
			command.setSpecificationId(d);
			qualityInspectionSpecificationDTOS.add(getQualitySpecification(command));
		});

		//只保留子节点详细内容 子节点为items 父节点为规范
		List<QualityInspectionSpecificationDTO> details = new ArrayList<>();
		qualityInspectionSpecificationDTOS.forEach(q->{
			if (q.getChildrens() != null && q.getChildrens().size() > 0)
				details.addAll(q.getChildrens());
		});
		//offlineResponse.setSpecificationsDetail(details);
		offlineResponse.getSpecifications().addAll(details);


		//需要单独返回删除的列表
		List<QualityInspectionSpecifications> deletedSpecifications =
				qualityProvider.listDeletedSpecifications(cmd.getCommunityId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getLastUpdateSyncTime());
		OfflineDeleteTablesInfo offlineDeleteTablesInfo = new OfflineDeleteTablesInfo();
		List<Long> deleteIds = new ArrayList<>();
		deletedSpecifications.forEach((s)-> deleteIds.add(s.getId()));
		offlineDeleteTablesInfo.setTableName("Specifications");
		offlineDeleteTablesInfo.setDeleteIds(deleteIds);
		offlineResponse.setDeletedSpecifications(offlineDeleteTablesInfo);

		//最终结果处理 lastSyncTime
		dealLastSyncTime(offlineResponse,cmd.getLastUpdateSyncTime());

		return offlineResponse;
	}

	private void dealLastSyncTime(OfflineSampleQualityInspectionResponse offlineResponse, Timestamp lastUpdateSyncTime) {
		List<QualityInspectionSpecificationDTO> specifications = offlineResponse.getSpecifications();
		List<QualityInspectionSpecificationDTO> effectiveSpecifications = new ArrayList<>();
		if (specifications != null && specifications.size() > 0) {
			specifications.forEach((s) -> {
				if (!(s.getCreateTime().before(lastUpdateSyncTime) &&
						s.getUpdateTime().before(lastUpdateSyncTime))) {
					effectiveSpecifications.add(s);
				}
			});
			offlineResponse.setSpecifications(effectiveSpecifications);
		}
	}

	private void addIds(List<Long> parentIds, List<QualityInspectionSpecificationDTO> specifications) {
		specifications.forEach((s) -> {
			parentIds.add(s.getId());
			if (s.getChildrens() != null && s.getChildrens().size() > 0) {
				addIds(parentIds, s.getChildrens());
			}
		});
	}

	@Override
	public List<QualityInspectionSpecificationDTO> batchUpdateQualitySpecification(BatchUpdateQualitySpecificationsCommand cmd) {
		GetQualitySpecificationCommand getQualitySpecificationCommand = new GetQualitySpecificationCommand();
		getQualitySpecificationCommand.setSpecificationId(cmd.getParentId());
		List<QualityInspectionSpecificationDTO> specifications = null;
		QualityInspectionSpecificationDTO qualityInspectionSpecificationDTO = getQualitySpecification(getQualitySpecificationCommand);
		if (qualityInspectionSpecificationDTO != null && qualityInspectionSpecificationDTO.getChildrens() != null) {
			specifications = qualityInspectionSpecificationDTO.getChildrens();
		}
		if (specifications != null && specifications.size() > 0){
			specifications.forEach((s) -> {
				//支持离线 置状态删除
				QualityInspectionSpecifications specification = qualityProvider.findSpecificationById(s.getId(), null, null);
				specification.setStatus(QualityStandardStatus.INACTIVE.getCode());
				specification.setDeleteUid(UserContext.currentUserId());
				specification.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				qualityProvider.updateQualitySpecification(specification);
			});
		}
		if (cmd.getSpecifications() != null && cmd.getSpecifications().size() > 0) {
			cmd.getSpecifications().forEach(this::createQualitySpecification);
		}
		return null;
	}

	@Override
	public QualityStandardsDTO getQualityStandards(DeleteQualityStandardCommand cmd) {
		QualityInspectionStandards standard = qualityProvider.findStandardById(cmd.getStandardId());
		if(standard == null || standard.getStatus() == null) {
			LOGGER.error("the standard which id="+cmd.getStandardId()+" don't exist!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_STANDARD_NOT_EXIST,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_STANDARD_NOT_EXIST),
									UserContext.current().getUser().getLocale(),
									"the standard don't exist!"));
		}
		List<QualityInspectionStandards> standards = new ArrayList<>();
		standards.add(standard);
		this.qualityProvider.populateStandardsGroups(standards);
		this.qualityProvider.populateStandardsSpecifications(standards);
		return  converStandardToDto(standard);
	}

	@Override
	public QualityOfflineTaskReportResponse offlineTaskReport(OfflineTaskReportCommand cmd) {
		List<OfflineEquipmentTaskReportLog> taskReportLog = new ArrayList<>();
		QualityOfflineTaskReportResponse reportResponse = new QualityOfflineTaskReportResponse();

		if (cmd.getTasks() != null && cmd.getTasks().size() > 0) {
			Map<Long, OfflineReportDetailDTO> taskDetailMaps = getTaskDetailMaps(cmd.getOfflineReportDetail());
			cmd.getTasks().forEach((task) -> {
				OfflineEquipmentTaskReportLog log = null;
				log = syncTaskInfoToServer(task, taskDetailMaps);
				if (log != null) {
					taskReportLog.add(log);
				}
			});
		}
		reportResponse.setTaskReportLogs(taskReportLog);
		return reportResponse;
	}
	private OfflineEquipmentTaskReportLog getOfflineQualityTaskReportLogObject(Long errorId, int errorCode, int errorDescription,Byte errorType) {
		OfflineEquipmentTaskReportLog reportLog = new OfflineEquipmentTaskReportLog();
		reportLog.setErrorIds(errorId);
		reportLog.setErrorCode(errorCode);
		reportLog.setErrorType(errorType);
		reportLog.setErrorDescription(localeStringService.getLocalizedString(String.valueOf(EquipmentServiceErrorCode.SCOPE),
				String.valueOf(errorDescription),
				UserContext.current().getUser().getLocale(), "quality inspection task  sync error"));
		return reportLog;
	}

	private Map<Long, OfflineReportDetailDTO> getTaskDetailMaps(List<OfflineReportDetailDTO> taskReportDetails) {
		Map<Long, OfflineReportDetailDTO> taskDetailMaps = new HashMap<>();
		taskReportDetails.forEach((task) -> taskDetailMaps.put(task.getTaskId(), task));
		return taskDetailMaps;
	}

	private OfflineEquipmentTaskReportLog syncTaskInfoToServer(OfflineQualityTaskCommand taskDTO, Map<Long, OfflineReportDetailDTO> taskDetailMaps) {
		QualityInspectionTasks task = null;
		try {
			task = verifiedTaskById(taskDTO.getId());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("syncTaskInfoToServer Erro:{}", e);
			return getOfflineQualityTaskReportLogObject(taskDTO.getId(), ErrorCodes.ERROR_GENERAL_EXCEPTION,
					QualityServiceErrorCode.ERROR_TASK_NOT_EXIST, QualityTaskType.VERIFY_TASK.getCode());
		}

		QualityInspectionTaskRecords record = new QualityInspectionTaskRecords();
		record.setTaskId(task.getId());
		record.setOperatorType(OwnerType.USER.getCode());
		record.setOperatorId(UserContext.currentUserId());

		//客户端增加VerificationResult 辨别是核查完成、转发、整改完成
		if (QualityInspectionTaskResult.CORRECT.getCode() == taskDTO.getVerificationResult()) {
			if (QualityInspectionTaskResult.CORRECT.equals(QualityInspectionTaskResult.fromStatus(task.getResult()))) {
				//转发
				task.setStatus(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode());
				task.setResult(QualityInspectionTaskResult.CORRECT.getCode());
				if (taskDTO.getOperatorType() != null) {
					task.setOperatorType(taskDTO.getOperatorType());
					record.setTargetType(taskDTO.getOperatorType());
				}

				if (taskDTO.getOperatorId() != null) {
					task.setOperatorId(taskDTO.getOperatorId());
					record.setTargetId(taskDTO.getOperatorId());
				}

				if (taskDTO.getProcessExpireTime() != null) {
					task.setProcessExpireTime(new Timestamp(taskDTO.getProcessExpireTime()));
					record.setProcessEndTime(task.getProcessExpireTime());
				}
				record.setProcessResult(QualityInspectionTaskResult.NONE.getCode());
				record.setProcessType(ProcessType.FORWARD.getCode());
//				//operators 当前操作用户名称
//				List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(UserContext.currentUserId());
//				String notifyTextForApplicant = getNotifyTextForApplicant(operators, task.getTaskName(), taskDTO.getProcessExpireTime().getTime());
//				//taskDTO.getOperatorId() 转发目标用户id
//				sendMessageToUser(taskDTO.getOperatorId(), notifyTextForApplicant);
//
//				String msg = getLocalRecordMessage(operators, task.getTaskName(), taskDTO.getOperatorId(), taskDTO.getProcessExpireTime().getTime());
//				record.setProcessMessage(msg);
			}/* else {
				//待整改
				task.setStatus(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode());
				task.setResult(QualityInspectionTaskResult.CORRECT.getCode());
				record.setProcessResult(QualityInspectionTaskResult.NONE.getCode());
				//record.setProcessType(ProcessType.RETIFY.getCode());
			}*/
		} else if (QualityInspectionTaskResult.CORRECT_COMPLETE.getCode() == taskDTO.getVerificationResult()) {
			//整改完成
			task.setStatus(QualityInspectionTaskStatus.EXECUTED.getCode());
			task.setResult(QualityInspectionTaskResult.CORRECT_COMPLETE.getCode());
			record.setProcessResult(QualityInspectionTaskResult.CORRECT_COMPLETE.getCode());
			record.setProcessType(ProcessType.RETIFY.getCode());
		} else if (QualityInspectionTaskResult.INSPECT_COMPLETE.getCode() == taskDTO.getVerificationResult()) {
			//核查完成
			task.setExecutiveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			task.setExecutorType(OrganizationMemberTargetType.USER.getCode());
			task.setExecutorId(UserContext.currentUserId());
			task.setResult(QualityInspectionTaskResult.INSPECT_COMPLETE.getCode());
			task.setStatus(QualityInspectionTaskStatus.EXECUTED.getCode());
			record.setProcessResult(QualityInspectionTaskResult.INSPECT_COMPLETE.getCode());
			record.setProcessType(ProcessType.INSPECT.getCode());
		}
		//具体结果集
		OfflineReportDetailDTO reportDetailDTO = taskDetailMaps.get(task.getId());
		// send message to processor and bind record and message to record
		if (!StringUtils.isNullOrEmpty(taskDTO.getOperatorType()) && taskDTO.getOperatorId() != null && taskDTO.getProcessExpireTime() != null) {
			sendMessageToProcessor(task, taskDTO, record);
		} else {
			proccessNoForwardTaskMessage(record, reportDetailDTO);
		}
		try {
			updateVerificationTasks(task, record, reportDetailDTO.getAttachments(), reportDetailDTO.getItemResults(), task.getNamespaceId());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("updateTasks task record and details erro :{}", e);
			return getOfflineQualityTaskReportLogObject(task.getId(), ErrorCodes.ERROR_GENERAL_EXCEPTION,
					QualityServiceErrorCode.ERROR_OFFLINE_SYNC_TASK, QualityTaskType.VERIFY_TASK.getCode());
		}
		return null;
	}

	private void proccessNoForwardTaskMessage(QualityInspectionTaskRecords record, OfflineReportDetailDTO reportDetailDTO) {
		//这里是针对非转发的任务的 processMessage 处理
		if (reportDetailDTO.getMessage() != null) {
            String attText = localeStringService.getLocalizedString(
                    String.valueOf(QualityServiceErrorCode.SCOPE),
                    String.valueOf(QualityServiceErrorCode.ATTACHMENT_TEXT),
                    UserContext.current().getUser().getLocale(),
                    "text:");
            if (record.getProcessMessage() != null) {
                String msg = record.getProcessMessage() + "<br/>" + attText + reportDetailDTO.getMessage();
                record.setProcessMessage(msg);
            } else {
                String msg = attText + reportDetailDTO.getMessage();
                record.setProcessMessage(msg);
            }
        }
	}

	private void sendMessageToProcessor(QualityInspectionTasks task, OfflineQualityTaskCommand taskDTO, QualityInspectionTaskRecords record) {
		//ASSIGN_TASK_NOTIFY_OPERATOR
		List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(UserContext.currentUserId());
		Map<String, Object> map = new HashMap<>();
		map.put("userName", operators.get(0).getContactName());
		map.put("taskName", task.getTaskName());
		map.put("deadline", timeToStr(new Timestamp(taskDTO.getProcessExpireTime())));
		String scope = QualityNotificationTemplateCode.SCOPE;
		int code = QualityNotificationTemplateCode.ASSIGN_TASK_NOTIFY_OPERATOR;
		String locale = "zh_CN";
		String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
		sendMessageToUser(taskDTO.getOperatorId(), notifyTextForApplicant);

		//ASSIGN_TASK_MSG
		List<OrganizationMember> targets = organizationProvider.listOrganizationMembersByUId(taskDTO.getOperatorId());
		Map<String, Object> msgMap = new HashMap<>();
		msgMap.put("operator", operators.get(0).getContactName());
		msgMap.put("target", targets.get(0).getContactName());
		msgMap.put("taskName", task.getTaskName());
		map.put("deadline", timeToStr(new Timestamp(taskDTO.getProcessExpireTime())));
		int msgCode = QualityNotificationTemplateCode.ASSIGN_TASK_MSG;
		String msg = localeTemplateService.getLocaleTemplateString(scope, msgCode, locale, msgMap, "");
		record.setProcessMessage(msg);
	}

	@Override
	public void startCrontabTask() {
		LOGGER.info("================================================ starting quality manual job... ");
		qualityProvider.closeDelayTasks();

		List<QualityInspectionStandards> activeStandards = qualityProvider.listActiveStandards();

		for (QualityInspectionStandards standard : activeStandards) {
			boolean isRepeat = false;
			try {
				isRepeat = repeatService.isRepeatSettingActive(standard.getRepeatSettingId());
			} catch (Exception e) {
				LOGGER.info("repeatSetting  analyzed erro :", e.getMessage());
			}
			LOGGER.info("QualityInspectionScheduleJob: standard id = " + standard.getId()
					+ "repeat setting id = " + standard.getRepeatSettingId() + "is repeat setting active: " + isRepeat);
			if (isRepeat) {
				createTaskByStandardId(standard.getId());
			}

		}
	}

	@Override
	public void deletePmNotifyParams(DeletePmNotifyParamsCommand cmd) {
		equipmentService.deletePmNotifyParams(cmd);

	}

	@Override
	public List<PmNotifyParamDTO> listPmNotifyParams(ListPmNotifyParamsCommand cmd) {
		return  equipmentService.listPmNotifyParams(cmd);
	}

	@Override
	public void setPmNotifyParams(SetPmNotifyParamsCommand cmd) {
		equipmentService.setPmNotifyParams(cmd);
	}

	@Override
	public Set<Long> getTaskGroupUsers(Long ownerId) {
		QualityInspectionTasks tasks = qualityProvider.findVerificationTaskById(ownerId);
		List<QualityInspectionStandardGroupMap> groupMaps = qualityProvider.listPlanGroupMapsByPlanId(tasks.getStandardId());
		//增加具体到个人
		if(groupMaps!=null && groupMaps.size()>0){
			Set<Long> userIds = new HashSet<>();
			groupMaps.forEach(map -> {
				//部门
				if (map.getPositionId() == null || map.getPositionId() == 0L) {
					if (map.getGroupId() != null && map.getGroupId() != 0L) {
						List<OrganizationMember> members = organizationProvider.listOrganizationMembers(map.getGroupId(), null);
						if (members != null) {
							for (OrganizationMember member : members) {
								userIds.add(member.getTargetId());
							}
						} else {
							//增加具体到个人
							userIds.add(map.getInspectorUid());
						}
					}

				} else {
					//通用岗位
					ListOrganizationContactByJobPositionIdCommand command = new ListOrganizationContactByJobPositionIdCommand();
					command.setOrganizationId(map.getGroupId());
					command.setJobPositionId(map.getPositionId());
					List<OrganizationContactDTO> contacts = organizationService.listOrganizationContactByJobPositionId(command);

					if (contacts != null && contacts.size() > 0) {
						for (OrganizationContactDTO contact : contacts) {
							userIds.add(contact.getTargetId());
						}
					}
				}
			});
			return userIds;
		}
		return null;
	}
}
