package com.everhomes.equipment;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.appurl.AppUrlService;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationJobPosition;
import com.everhomes.organization.OrganizationJobPositionMap;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.pmNotify.PmNotifyConfigurations;
import com.everhomes.pmNotify.PmNotifyProvider;
import com.everhomes.pmNotify.PmNotifyRecord;
import com.everhomes.pmNotify.PmNotifyService;
import com.everhomes.pmtask.PmTaskService;
import com.everhomes.portal.PortalService;
import com.everhomes.repeat.RepeatProvider;
import com.everhomes.repeat.RepeatService;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.acl.ServiceModuleAuthorizationsDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.enterprise.GetAuthOrgByProjectIdAndAppIdCommand;
import com.everhomes.rest.equipment.AdminFlag;
import com.everhomes.rest.equipment.Attachment;
import com.everhomes.rest.equipment.CreateEquipmentCategoryCommand;
import com.everhomes.rest.equipment.CreateEquipmentRepairCommand;
import com.everhomes.rest.equipment.CreateInspectionTemplateCommand;
import com.everhomes.rest.equipment.DeleteEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.DeleteEquipmentCategoryCommand;
import com.everhomes.rest.equipment.DeleteEquipmentPlanCommand;
import com.everhomes.rest.equipment.DeleteEquipmentStandardCommand;
import com.everhomes.rest.equipment.DeleteEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.DeleteEquipmentsCommand;
import com.everhomes.rest.equipment.DeleteInspectionTemplateCommand;
import com.everhomes.rest.equipment.EquipmentAccessoriesDTO;
import com.everhomes.rest.equipment.EquipmentAccessoryMapDTO;
import com.everhomes.rest.equipment.EquipmentAttachmentDTO;
import com.everhomes.rest.equipment.EquipmentCategories;
import com.everhomes.rest.equipment.EquipmentInspectionCategoryDTO;
import com.everhomes.rest.equipment.EquipmentInspectionPlanDTO;
import com.everhomes.rest.equipment.EquipmentInspectionReviewDateDTO;
import com.everhomes.rest.equipment.EquipmentModelType;
import com.everhomes.rest.equipment.EquipmentNotificationTemplateCode;
import com.everhomes.rest.equipment.EquipmentOfflineErrorType;
import com.everhomes.rest.equipment.EquipmentOperateActionType;
import com.everhomes.rest.equipment.EquipmentOperateLogsDTO;
import com.everhomes.rest.equipment.EquipmentOperateObjectType;
import com.everhomes.rest.equipment.EquipmentParameterDTO;
import com.everhomes.rest.equipment.EquipmentPlanStatus;
import com.everhomes.rest.equipment.EquipmentReviewStatus;
import com.everhomes.rest.equipment.EquipmentServiceErrorCode;
import com.everhomes.rest.equipment.EquipmentStandardCommunity;
import com.everhomes.rest.equipment.EquipmentStandardMapDTO;
import com.everhomes.rest.equipment.EquipmentStandardRelationDTO;
import com.everhomes.rest.equipment.EquipmentStandardStatus;
import com.everhomes.rest.equipment.EquipmentStandardsDTO;
import com.everhomes.rest.equipment.EquipmentStatus;
import com.everhomes.rest.equipment.EquipmentTaskAttachmentDTO;
import com.everhomes.rest.equipment.EquipmentTaskDTO;
import com.everhomes.rest.equipment.EquipmentTaskLogs;
import com.everhomes.rest.equipment.EquipmentTaskLogsDTO;
import com.everhomes.rest.equipment.EquipmentTaskOfflineResponse;
import com.everhomes.rest.equipment.EquipmentTaskProcessResult;
import com.everhomes.rest.equipment.EquipmentTaskProcessType;
import com.everhomes.rest.equipment.EquipmentTaskReportDetail;
import com.everhomes.rest.equipment.EquipmentTaskResult;
import com.everhomes.rest.equipment.EquipmentTaskStatus;
import com.everhomes.rest.equipment.EquipmentsDTO;
import com.everhomes.rest.equipment.ExecuteGroupAndPosition;
import com.everhomes.rest.equipment.ExportEquipmentData;
import com.everhomes.rest.equipment.ExportEquipmentsCardCommand;
import com.everhomes.rest.equipment.ExportTaskLogsCommand;
import com.everhomes.rest.equipment.GetInspectionObjectByQRCodeCommand;
import com.everhomes.rest.equipment.ImportDataType;
import com.everhomes.rest.equipment.ImportOwnerCommand;
import com.everhomes.rest.equipment.InspectionItemDTO;
import com.everhomes.rest.equipment.InspectionItemResult;
import com.everhomes.rest.equipment.InspectionStandardMapTargetType;
import com.everhomes.rest.equipment.InspectionTemplateDTO;
import com.everhomes.rest.equipment.ItemResultStat;
import com.everhomes.rest.equipment.ListAbnormalTasksCommand;
import com.everhomes.rest.equipment.ListAttachmentsByEquipmentIdCommand;
import com.everhomes.rest.equipment.ListEquipmentInspectionCategoriesCommand;
import com.everhomes.rest.equipment.ListEquipmentTasksCommand;
import com.everhomes.rest.equipment.ListEquipmentTasksResponse;
import com.everhomes.rest.equipment.ListInspectionTemplatesCommand;
import com.everhomes.rest.equipment.ListLogsByTaskIdCommand;
import com.everhomes.rest.equipment.ListLogsByTaskIdResponse;
import com.everhomes.rest.equipment.ListParametersByStandardIdCommand;
import com.everhomes.rest.equipment.ListRelatedOrgGroupsCommand;
import com.everhomes.rest.equipment.ListTaskByIdCommand;
import com.everhomes.rest.equipment.ListTasksByEquipmentIdCommand;
import com.everhomes.rest.equipment.ListTasksByTokenCommand;
import com.everhomes.rest.equipment.ListUserHistoryTasksCommand;
import com.everhomes.rest.equipment.OfflineEquipmentTaskReportCommand;
import com.everhomes.rest.equipment.OfflineEquipmentTaskReportLog;
import com.everhomes.rest.equipment.OfflineEquipmentTaskReportResponse;
import com.everhomes.rest.equipment.OfflineTaskCountStat;
import com.everhomes.rest.equipment.QRCodeFlag;
import com.everhomes.rest.equipment.ReportEquipmentTaskCommand;
import com.everhomes.rest.equipment.ReviewEquipmentPlanCommand;
import com.everhomes.rest.equipment.ReviewEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.ReviewEquipmentTaskCommand;
import com.everhomes.rest.equipment.ReviewEquipmentTasksCommand;
import com.everhomes.rest.equipment.ReviewResult;
import com.everhomes.rest.equipment.ReviewedTaskStat;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesResponse;
import com.everhomes.rest.equipment.SearchEquipmentInspectionPlansCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardsResponse;
import com.everhomes.rest.equipment.SearchEquipmentTasksCommand;
import com.everhomes.rest.equipment.SearchEquipmentsCommand;
import com.everhomes.rest.equipment.SearchEquipmentsResponse;
import com.everhomes.rest.equipment.SetReviewExpireDaysCommand;
import com.everhomes.rest.equipment.StandardAndStatus;
import com.everhomes.rest.equipment.StandardGroupDTO;
import com.everhomes.rest.equipment.StandardRepeatType;
import com.everhomes.rest.equipment.StandardType;
import com.everhomes.rest.equipment.StatEquipmentTasksCommand;
import com.everhomes.rest.equipment.StatEquipmentTasksResponse;
import com.everhomes.rest.equipment.StatIntervalAllEquipmentTasksCommand;
import com.everhomes.rest.equipment.StatIntervalAllEquipmentTasksResponse;
import com.everhomes.rest.equipment.StatItemResultsInEquipmentTasksCommand;
import com.everhomes.rest.equipment.StatItemResultsInEquipmentTasksResponse;
import com.everhomes.rest.equipment.StatLastDaysEquipmentTasksCommand;
import com.everhomes.rest.equipment.StatLastDaysEquipmentTasksResponse;
import com.everhomes.rest.equipment.StatTodayEquipmentTasksCommand;
import com.everhomes.rest.equipment.StatTodayEquipmentTasksResponse;
import com.everhomes.rest.equipment.Status;
import com.everhomes.rest.equipment.TaskCountDTO;
import com.everhomes.rest.equipment.TasksStatData;
import com.everhomes.rest.equipment.UpdateEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.UpdateEquipmentCategoryCommand;
import com.everhomes.rest.equipment.UpdateEquipmentPlanCommand;
import com.everhomes.rest.equipment.UpdateEquipmentStandardCommand;
import com.everhomes.rest.equipment.UpdateEquipmentsCommand;
import com.everhomes.rest.equipment.UpdateInspectionTemplateCommand;
import com.everhomes.rest.equipment.VerifyEquipmentLocationCommand;
import com.everhomes.rest.equipment.VerifyEquipmentLocationResponse;
import com.everhomes.rest.equipment.findScopeFieldItemCommand;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.module.CheckModuleManageCommand;
import com.everhomes.rest.organization.ListOrganizationContactByJobPositionIdCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.ListOrganizationPersonnelByRoleIdsCommand;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationNaviFlag;
import com.everhomes.rest.parking.ParkingLocalStringCode;
import com.everhomes.rest.pmNotify.DeletePmNotifyParamsCommand;
import com.everhomes.rest.pmNotify.ListPmNotifyParamsCommand;
import com.everhomes.rest.pmNotify.PmNotifyConfigurationStatus;
import com.everhomes.rest.pmNotify.PmNotifyParamDTO;
import com.everhomes.rest.pmNotify.PmNotifyParams;
import com.everhomes.rest.pmNotify.PmNotifyReceiver;
import com.everhomes.rest.pmNotify.PmNotifyReceiverDTO;
import com.everhomes.rest.pmNotify.PmNotifyReceiverList;
import com.everhomes.rest.pmNotify.PmNotifyReceiverType;
import com.everhomes.rest.pmNotify.PmNotifyScopeType;
import com.everhomes.rest.pmNotify.PmNotifyType;
import com.everhomes.rest.pmNotify.ReceiverName;
import com.everhomes.rest.pmNotify.SetPmNotifyParamsCommand;
import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesResponse;
import com.everhomes.rest.pmtask.PmTaskAddressType;
import com.everhomes.rest.pmtask.PmTaskDTO;
import com.everhomes.rest.pmtask.PmTaskFlowStatus;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.rest.quality.ProcessType;
import com.everhomes.rest.quality.QualityGroupType;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.repeat.RepeatServiceErrorCode;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.rest.repeat.TimeRangeDTO;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.rest.varField.FieldDTO;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.EquipmentAccessoriesSearcher;
import com.everhomes.search.EquipmentPlanSearcher;
import com.everhomes.search.EquipmentSearcher;
import com.everhomes.search.EquipmentStandardMapSearcher;
import com.everhomes.search.EquipmentStandardSearcher;
import com.everhomes.search.EquipmentTasksSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTasks;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DownloadUtils;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.QRCodeConfig;
import com.everhomes.util.QRCodeEncoder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.doc.DocUtil;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import com.google.zxing.WriterException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class EquipmentServiceImpl implements EquipmentService {

	final String downloadDir = "\\download\\";

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentServiceImpl.class);

	DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private EquipmentStandardSearcher equipmentStandardSearcher;

	@Autowired
	private EquipmentSearcher equipmentSearcher;

	@Autowired
	private EquipmentAccessoriesSearcher equipmentAccessoriesSearcher;

	@Autowired
	private EquipmentTasksSearcher equipmentTasksSearcher;

	@Autowired
	private CategoryProvider categoryProvider;

	@Autowired
	private EquipmentProvider equipmentProvider;

	@Autowired
	private ConfigurationProvider configProvider;

	@Autowired
	private RepeatService repeatService;

	@Autowired
	private RepeatProvider repeatProvider;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private LocaleStringService localeStringService;

	@Autowired
	private LocaleTemplateService localeTemplateService;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private AclProvider aclProvider;

	@Autowired
	private EquipmentStandardMapSearcher equipmentStandardMapSearcher;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private AppUrlService appUrlService;

	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;

	@Autowired
	private RolePrivilegeService rolePrivilegeService;

	@Autowired
	private PmNotifyProvider pmNotifyProvider;

	@Autowired
	private PmNotifyService pmNotifyService;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private FieldService fieldService;

	@Autowired
	private ImportFileService importFileService;

	@Autowired
	private FieldProvider fieldProvider;

	@Autowired
	private EquipmentPlanSearcher equipmentPlanSearcher;

	@Autowired
	private PortalService portalService;

	@Autowired
	private PmTaskService pmTaskService;

	@Autowired
	private UserService userService;

	@Autowired
	private ServiceModuleService serviceModuleService;

	@Autowired
	private ScheduleProvider scheduleProvider;

	private final  String queueDelay = "pmtaskdelays";
	private final  String queueNoDelay = "pmtasknodelays";

	@Override
	public EquipmentStandardsDTO updateEquipmentStandard(UpdateEquipmentStandardCommand cmd) {
		//auth start
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_STANDARD_UPDATE, cmd.getTargetId());

		User user = UserContext.current().getUser();
		EquipmentInspectionStandards standard = new EquipmentInspectionStandards();

		if (cmd.getId() == null) {
			standard = ConvertHelper.convert(cmd, EquipmentInspectionStandards.class);
			standard.setCreatorUid(user.getId());
			standard.setNamespaceId(cmd.getNamespaceId());
			if (cmd.getEquipments() == null || cmd.getEquipments().size() == 0) {
				standard.setStatus(EquipmentStandardStatus.NOT_COMPLETED.getCode());
			} else {
				standard.setStatus(EquipmentStandardStatus.ACTIVE.getCode());
			}

			createEquipmentStandardItems(standard, cmd);//创建标准的模板表和item关系表
			equipmentProvider.creatEquipmentStandard(standard);
			equipmentStandardSearcher.feedDoc(standard);

			//此处创建公共标准关联表 targetId为null
			if (cmd.getCommunities() != null && cmd.getCommunities().size() > 0) {
				for (Long communityId : cmd.getCommunities()) {
					EquipmentModelCommunityMap map = new EquipmentModelCommunityMap();
					map.setModelId(standard.getId());
					map.setTargetType(standard.getTargetType());
					map.setTargetId(communityId);
					map.setModelType(EquipmentModelType.STANDARD.getCode());
					equipmentProvider.createEquipmentModelCommunityMap(map);
				}
			}

		} else {
			EquipmentInspectionStandards exist  = verifyEquipmentStandard(cmd.getId());
			standard = ConvertHelper.convert(cmd, EquipmentInspectionStandards.class);
			standard.setTargetType(exist.getTargetType());
			standard.setTargetId(exist.getTargetId());

			if (cmd.getEquipments() != null || cmd.getEquipments().size() > 0) {
				standard.setStatus(EquipmentStandardStatus.ACTIVE.getCode());
			} else {
				throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
						EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_EXIST, "标准关联巡检对象为空！");
			}

			if (exist.getTargetId() == 0L && cmd.getTargetId() != null && cmd.getTargetId() != 0L) {
				//项目修改公共标准
				standard.setTargetId(cmd.getTargetId());
				standard.setTargetType(cmd.getTargetType());
				standard.setCreatorUid(UserContext.currentUserId());
				standard.setReferId(cmd.getId());
				//创建标准的模板表和item关系表
				createEquipmentStandardItems(standard, cmd);
				equipmentProvider.creatEquipmentStandard(standard);
				equipmentStandardSearcher.feedDoc(standard);

			} else {
				if ((cmd.getTargetId() == null || cmd.getTargetId() == 0L) && standard.getTargetId() == 0L) {//全部中修改公共标准
					equipmentProvider.deleteModelCommunityMapByModelId(standard.getId(), EquipmentModelType.STANDARD.getCode());
					if (cmd.getCommunities() != null && cmd.getCommunities().size() > 0) {
						for (Long communityId : cmd.getCommunities()) {
							EquipmentModelCommunityMap map = new EquipmentModelCommunityMap();
							map.setModelId(standard.getId());
							map.setTargetType(standard.getTargetType());
							map.setTargetId(communityId);
							map.setModelType(EquipmentModelType.STANDARD.getCode());
							equipmentProvider.createEquipmentModelCommunityMap(map);
						}
					}

				}
				//创建标准的模板表和item关系表
				createEquipmentStandardItems(standard, cmd);
				equipmentProvider.updateEquipmentStandard(standard);
				equipmentStandardSearcher.feedDoc(standard);
			}
			//issue-36509 remove deal this logic for zijing project
//			equipmentProvider.deleteEquipmentPlansMapByStandardId(standard.getId());//删除标准对应的巡检对象列表中对应条目
			equipmentProvider.deleteEquipmentInspectionStandardMapByStandardId(cmd.getId());//删除修改标准相关的巡检对象关联表
		}
		createEquipmentStandardsEquipmentsMap(standard, cmd.getEquipments());//创建新的巡检对象和标准关联表

		return converStandardToDto(standard);
	}

	private void createEquipmentStandardsEquipmentsMap(EquipmentInspectionStandards standard, List<EquipmentsDTO> equipments) {
		if (equipments != null && equipments.size() > 0) {
			EquipmentStandardMap equipmentStandardMap = new EquipmentStandardMap();
			equipmentStandardMap.setStandardId(standard.getId());
			List<EquipmentInspectionEquipments> createdEquipments = new ArrayList<>();
			for (EquipmentsDTO equipment : equipments) {
				equipmentStandardMap.setTargetId(equipment.getId());
				equipmentStandardMap.setTargetType(InspectionStandardMapTargetType.EQUIPMENT.getCode());
				equipmentProvider.createEquipmentStandardMap(equipmentStandardMap);
				equipmentStandardMapSearcher.feedDoc(equipmentStandardMap);
				createdEquipments.add(ConvertHelper.convert(equipment, EquipmentInspectionEquipments.class));
			}
			standard.setEquipments(createdEquipments);
		}
	}

	private void createEquipmentStandardItems(EquipmentInspectionStandards standards, UpdateEquipmentStandardCommand cmd) {
		//这里按照上一版的模式  后台还是新建模板 和模板巡检项关联表
		EquipmentInspectionTemplates templates = new EquipmentInspectionTemplates();
		templates.setName(standards.getName());
		templates.setOwnerId(cmd.getOwnerId());
		templates.setOwnerType(cmd.getOwnerType());
		templates.setNamespaceId(cmd.getNamespaceId());
		Long templateId = equipmentProvider.createEquipmentInspectionTemplates(templates);
		standards.setTemplateId(templateId);
		EquipmentInspectionTemplateItemMap templateItemMap = new EquipmentInspectionTemplateItemMap();
		templateItemMap.setTemplateId(templateId);

		List<EquipmentInspectionItems> items = new ArrayList<>();
		List<InspectionItemDTO> itemDTOS = cmd.getItems();
		if (itemDTOS != null && itemDTOS.size() > 0) {
			for (InspectionItemDTO itemDTO : itemDTOS) {
				EquipmentInspectionItems item = ConvertHelper.convert(itemDTO, EquipmentInspectionItems.class);
				item.setOwnerId(cmd.getOwnerId());
				item.setOwnerType(cmd.getOwnerType());
				Long itemId = equipmentProvider.createEquipmentInspectionItems(item);
				items.add(item);
				// create item template map
				templateItemMap.setItemId(itemId);
				templateItemMap.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				equipmentProvider.createEquipmentInspectionTemplateItemMap(templateItemMap);
			}
		}
		standards.setItems(items);
	}

	private void checkUserPrivilege(Long orgId, Long privilegeId, Long communityId) {
//		ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
//		listServiceModuleAppsCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
//		listServiceModuleAppsCommand.setModuleId(EquipmentConstant.EQUIPMENT_MODULE);
//		ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
//		boolean flag = false;
//		if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
//			flag = userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(),
//					orgId, orgId, privilegeId, apps.getServiceModuleApps().get(0).getId(), null, communityId);
//		}
//		if (!flag) {
//			LOGGER.error("Permission is denied, namespaceId={}, orgId={}, communityId={}," +
//					" privilege={}", UserContext.getCurrentNamespaceId(), orgId, communityId, privilegeId);
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
//					"Insufficient privilege");
//		}
		userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, EquipmentConstant.EQUIPMENT_MODULE, null, null, null, communityId);
	}

	private EquipmentStandardsDTO converStandardToDto(EquipmentInspectionStandards standard) {
		//processRepeatSetting(standard);
		EquipmentStandardsDTO standardDto = ConvertHelper.convert(standard, EquipmentStandardsDTO.class);
//		RepeatSettingsDTO repeatDto = ConvertHelper.convert(standard.getRepeat(), RepeatSettingsDTO.class);
//
//		EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(standardDto.getTemplateId(), standardDto.getOwnerId(), standardDto.getOwnerType());
//		if(template != null) {
//			standardDto.setTemplateName(template.getName());
//		}

		OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(standard.getOperatorUid(),
				standard.getOwnerId());
		if (null != member) {
			standardDto.setOperatorName(member.getContactName());
		}

		/*List<StandardGroupDTO> executiveGroup = new ArrayList<StandardGroupDTO>();
		List<StandardGroupDTO> reviewGroup = new ArrayList<StandardGroupDTO>();
		if(standard.getExecutiveGroup() != null) {
			executiveGroup = standard.getExecutiveGroup().stream().map((r) -> {

				StandardGroupDTO dto = ConvertHelper.convert(r, StandardGroupDTO.class);
				Organization group = organizationProvider.findOrganizationById(r.getGroupId());
				OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(r.getPositionId());
				if(group != null) {
					dto.setGroupName(group.getName());

				}

				if(position != null) {
					if(dto.getGroupName() != null) {
						dto.setGroupName(dto.getGroupName() + "-" + position.getName());
					} else {
						dto.setGroupName(position.getName());

					}
				}

	        	return dto;
	        }).collect(Collectors.toList());
		}


		if(standard.getReviewGroup() != null) {
			reviewGroup = standard.getReviewGroup().stream().map((r) -> {

				StandardGroupDTO dto = ConvertHelper.convert(r, StandardGroupDTO.class);
				Organization group = organizationProvider.findOrganizationById(r.getGroupId());
				OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(r.getPositionId());
				if(group != null) {
					dto.setGroupName(group.getName());

				}

				if(position != null) {
					if(dto.getGroupName() != null) {
						dto.setGroupName(dto.getGroupName() + "-" + position.getName());
					} else {
						dto.setGroupName(position.getName());

					}
				}

	        	return dto;
	        }).collect(Collectors.toList());
		}*/

		//standardDto.setRepeat(repeatDto);
		/*standardDto.setExecutiveGroup(executiveGroup);
		standardDto.setReviewGroup(reviewGroup);*/

		return standardDto;
	}

	private void processStandardGroups(List<StandardGroupDTO> groupList, EquipmentInspectionStandards standard) {

		List<EquipmentInspectionStandardGroupMap> executiveGroup = null;
		List<EquipmentInspectionStandardGroupMap> reviewGroup = null;
		this.equipmentProvider.deleteEquipmentInspectionStandardGroupMapByStandardId(standard.getId());

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("processStandardGroups: deleteEquipmentInspectionStandardGroupMapByStandardId, standardId=" + standard.getId()
					+ "userId = " + UserContext.current().getUser().getId() + "time = " + DateHelper.currentGMTTime()
					+ "new standard groupList = {}" + groupList);
		}

		if (groupList != null && groupList.size() > 0) {
			executiveGroup = new ArrayList<EquipmentInspectionStandardGroupMap>();
			reviewGroup = new ArrayList<EquipmentInspectionStandardGroupMap>();

			for (StandardGroupDTO group : groupList) {
				EquipmentInspectionStandardGroupMap map = new EquipmentInspectionStandardGroupMap();
				map.setStandardId(standard.getId());
				map.setGroupType(group.getGroupType());
				map.setGroupId(group.getGroupId());
				map.setPositionId(group.getPositionId());
				equipmentProvider.createEquipmentInspectionStandardGroupMap(map);
				if (QualityGroupType.EXECUTIVE_GROUP.equals(map.getGroupType())) {
					executiveGroup.add(map);
				}
				if (QualityGroupType.REVIEW_GROUP.equals(map.getGroupType())) {
					reviewGroup.add(map);
				}
			}

			standard.setExecutiveGroup(executiveGroup);
			standard.setReviewGroup(reviewGroup);
		}
	}

	private void inActiveEquipmentStandardRelations(EquipmentStandardMap map) {
//		map.setReviewStatus(EquipmentReviewStatus.INACTIVE.getCode());
//		map.setReviewResult(ReviewResult.INACTIVE.getCode());
		map.setStatus(EquipmentStandardStatus.INACTIVE.getCode());
		equipmentProvider.updateEquipmentStandardMap(map);
		equipmentStandardMapSearcher.feedDoc(map);

	}

	@Override
	public void deleteEquipmentStandard(DeleteEquipmentStandardCommand cmd) {
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_STANDARD_DELETE, cmd.getTargetId());

		User user = UserContext.current().getUser();

		EquipmentInspectionStandards standard = verifyEquipmentStandard(cmd.getStandardId());

		if (EquipmentStandardStatus.INACTIVE.equals(EquipmentStandardStatus.fromStatus(standard.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_STANDARD_ALREADY_DELETED,
					"设备标准已删除");
		}

		standard.setDeleterUid(user.getId());
		standard.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		standard.setOperatorUid(user.getId());
		standard.setStatus(EquipmentStandardStatus.INACTIVE.getCode());
		equipmentProvider.updateEquipmentStandard(standard);
		equipmentStandardSearcher.feedDoc(standard);
		//判断删除情况
		if (cmd.getTargetId() != null && standard.getTargetId() == 0L) {
			//在项目上删除公共标准的情况  其余情况按照原来模式
			equipmentProvider.deleteModelCommunityMapByModelIdAndCommunityId(standard.getId(), cmd.getTargetId(),
					EquipmentModelType.STANDARD.getCode());
		} else {
			standard.setDeleterUid(user.getId());
			standard.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			standard.setOperatorUid(user.getId());
			standard.setStatus(EquipmentStandardStatus.INACTIVE.getCode());
			equipmentProvider.updateEquipmentStandard(standard);
			equipmentStandardSearcher.feedDoc(standard);
			if ((cmd.getTargetId() == null || cmd.getTargetId() == 0) && standard.getTargetId() == 0L) {
				equipmentProvider.deleteModelCommunityMapByModelId(standard.getId(), EquipmentModelType.STANDARD.getCode());
			}

			//删除标准和设备的关联关系
			equipmentProvider.deleteEquipmentInspectionStandardMapByStandardId(standard.getId());
			//inActive与删除标准相关的所有任务和计划  不需要删除计划 计划是通过planId关联巡检对象列表
			equipmentProvider.deleteEquipmentPlansMapByStandardId(standard.getId());
			List<EquipmentStandardMap> maps = equipmentProvider.findByStandardId(standard.getId());
			if (maps != null && maps.size() > 0) {
				for (EquipmentStandardMap map : maps) {
					//inActiveEquipmentStandardRelations(map);
					//设备状态修改为不完整
					EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getTargetId());
					equipment.setStatus(EquipmentStatus.INCOMPLETE.getCode());
					equipmentProvider.updateEquipmentInspectionEquipment(equipment);
				}
			}

			//inactiveTasksByStandardId(standard.getId());
		}
	}

	@Override
	public HttpServletResponse exportEquipmentStandards(SearchEquipmentStandardsCommand cmd, HttpServletResponse response) {

		Integer pageSize = Integer.MAX_VALUE;
		cmd.setPageSize(pageSize);

		SearchEquipmentStandardsResponse standards = equipmentStandardSearcher.query(cmd);
		List<EquipmentStandardsDTO> eqStandards = standards.getEqStandards();

		URL rootPath = EquipmentServiceImpl.class.getResource("/");
		String filePath = rootPath.getPath() + this.downloadDir;
		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();
		filePath = filePath + "EquipmentStandards" + System.currentTimeMillis() + ".xlsx";
		//新建了一个文件
		this.createEquipmentStandardsBook(filePath, eqStandards);

		return download(filePath, response);
	}

	public HttpServletResponse download(String path, HttpServletResponse response) {
		try {
			// path是指欲下载的文件的路径。
			File file = new File(path);
			if (!file.isFile()) {
				LOGGER.info("filename:{} is not a file", path);
			}
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

	public void createEquipmentStandardsBook(String path, List<EquipmentStandardsDTO> dtos) {

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("equipmentStandards");

		this.createEquipmentStandardsBookSheetHead(sheet);

		for (EquipmentStandardsDTO dto : dtos) {
			this.setNewEquipmentStandardsBookRow(sheet, dto);
		}

		try {
			FileOutputStream out = new FileOutputStream(path);

			wb.write(out);
			wb.close();
			out.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}

	}

	private void createEquipmentStandardsBookSheetHead(Sheet sheet) {

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i = -1;
		/*row.createCell(++i).setCellValue("标准编号");
		row.createCell(++i).setCellValue("标准名称");
		row.createCell(++i).setCellValue("类别");
		row.createCell(++i).setCellValue("执行频率");
		row.createCell(++i).setCellValue("开始执行时间");
		row.createCell(++i).setCellValue("时间限制");
		row.createCell(++i).setCellValue("最新更新时间");
		row.createCell(++i).setCellValue("标准来源");
		row.createCell(++i).setCellValue("标准状态");*/
		//V3.0.2
		row.createCell(++i).setCellValue("标准名称");
		row.createCell(++i).setCellValue("周期类型");
		row.createCell(++i).setCellValue("最新更新时间");
		row.createCell(++i).setCellValue("标准来源");
		row.createCell(++i).setCellValue("标准状态");
	}

	private void setNewEquipmentStandardsBookRow(Sheet sheet, EquipmentStandardsDTO dto) {
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		int i = -1;
		//row.createCell(++i).setCellValue(dto.getStandardNumber());
		row.createCell(++i).setCellValue(dto.getName());
		/*row.createCell(++i).setCellValue(StandardType.fromStatus(dto.getStandardType()).getName());
		if(dto.getRepeat() != null) {
			row.createCell(++i).setCellValue(repeatService.getExecutionFrequency(dto.getRepeat()));
			row.createCell(++i).setCellValue(repeatService.getExecuteStartTime(dto.getRepeat()));
			row.createCell(++i).setCellValue(repeatService.getlimitTime(dto.getRepeat()));
		} else {
			row.createCell(++i).setCellValue("");
			row.createCell(++i).setCellValue("");
			row.createCell(++i).setCellValue("");
		}*/
		//v3.0.2中标准导出字段变成如下
		if (StandardRepeatType.NO_REPEAT.equals(StandardRepeatType.fromStatus(dto.getRepeatType())))
			row.createCell(++i).setCellValue(StandardRepeatType.NO_REPEAT.getName());
		if (StandardRepeatType.BY_DAY.equals(StandardRepeatType.fromStatus(dto.getRepeatType())))
			row.createCell(++i).setCellValue(StandardRepeatType.BY_DAY.getName());
		if (StandardRepeatType.BY_WEEK.equals(StandardRepeatType.fromStatus(dto.getRepeatType())))
			row.createCell(++i).setCellValue(StandardRepeatType.BY_WEEK.getName());
		if (StandardRepeatType.BY_MONTH.equals(StandardRepeatType.fromStatus(dto.getRepeatType())))
			row.createCell(++i).setCellValue(StandardRepeatType.BY_MONTH.getName());
		if (StandardRepeatType.BY_YEAR.equals(StandardRepeatType.fromStatus(dto.getRepeatType())))
			row.createCell(++i).setCellValue(StandardRepeatType.BY_YEAR.getName());

		row.createCell(++i).setCellValue(dto.getUpdateTime().toString());
		row.createCell(++i).setCellValue(dto.getStandardSource());
		if (EquipmentStandardStatus.INACTIVE.equals(EquipmentStandardStatus.fromStatus(dto.getStatus())))
			row.createCell(++i).setCellValue("已失效");
		if (EquipmentStandardStatus.NOT_COMPLETED.equals(EquipmentStandardStatus.fromStatus(dto.getStatus())))
			row.createCell(++i).setCellValue("未完成");
		if (EquipmentStandardStatus.ACTIVE.equals(EquipmentStandardStatus.fromStatus(dto.getStatus())))
			row.createCell(++i).setCellValue("正常");

	}

	@Override
	public EquipmentStandardsDTO findEquipmentStandard(DeleteEquipmentStandardCommand cmd) {

		EquipmentInspectionStandards standard = verifyEquipmentStandard(cmd.getStandardId());

		//填充关联设备数equipmentsCount
		processEquipmentsCount(standard);
		//填充执行周期repeat
		//processRepeatSetting(standard);
		//equipmentProvider.populateStandardGroups(standard);
		//填充标准中的设备列表
		equipmentProvider.populateEquipments(standard);
		//填充标准中的巡检项
		equipmentProvider.populateItems(standard);
		//equipmentProvider.populateStandardGroups(standard);

		if (standard.getTargetId() == 0L) {
			List<Long> communityIds = equipmentProvider.listModelCommunityMapByModelId(standard.getId(), EquipmentModelType.STANDARD.getCode());
			List<EquipmentStandardCommunity> communities = new ArrayList<>();
			if (communityIds != null && communityIds.size() > 0) {
				communityIds.forEach((c) -> {
					EquipmentStandardCommunity standardCommunity = new EquipmentStandardCommunity();
					Community community = communityProvider.findCommunityById(c);
					if (community != null) {
						standardCommunity.setCommunityId(community.getId());
						standardCommunity.setCommunityName(community.getName());
					}
					communities.add(standardCommunity);
				});
			}
			standard.setCommunities(communities);
		}
		//add communities for operating in all scope
		if (standard.getTargetId() != 0 && standard.getTargetId() != null) {
			List<EquipmentStandardCommunity> communities = new ArrayList<>();
			EquipmentStandardCommunity standardCommunity = new EquipmentStandardCommunity();
			Community community = communityProvider.findCommunityById(standard.getTargetId());
			if (community != null) {
				standardCommunity.setCommunityId(community.getId());
				standardCommunity.setCommunityName(community.getName());
			}
			communities.add(standardCommunity);
			standard.setCommunities(communities);
		}
		return converStandardToDto(standard);
	}


	private void processRepeatSetting(EquipmentInspectionStandards standard) {
		if (null != standard.getRepeatSettingId() && standard.getRepeatSettingId() != 0) {
			RepeatSettings repeat = repeatService.findRepeatSettingById(standard.getRepeatSettingId());
			standard.setRepeat(repeat);
		}
	}

	private void processEquipmentsCount(EquipmentInspectionStandards standard) {
		List<EquipmentStandardMap> maps = equipmentProvider.findByStandardId(standard.getId());
		int count = 0;
		if (maps != null) {
				/*if(EquipmentReviewStatus.REVIEWED.equals(EquipmentReviewStatus.fromStatus(map.getReviewStatus())) &&
						ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(map.getReviewResult()))) {
					count++;
				}*/
			//count++;
			count = maps.size();
		}

		standard.setEquipmentsCount(count);
	}

	private EquipmentInspectionStandards verifyEquipmentStandard(Long standardId) {

		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(standardId);

		if (standard == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_STANDARD_NOT_EXIST,
					"设备标准不存在");
		}

		return standard;
	}

	@Override
	public void reviewEquipmentStandardRelations(ReviewEquipmentStandardRelationsCommand cmd) {
		User user = UserContext.current().getUser();
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_RELATION_REVIEW, cmd.getTargetId());

		EquipmentStandardMap map = equipmentProvider.findEquipmentStandardMapById(cmd.getId());
		EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());

		if (EquipmentStatus.INACTIVE.equals(EquipmentStatus.fromStatus(equipment.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_ALREADY_DELETED,
					"设备已删除");
		}

		if (map == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_STANDARD_MAP_NOT_EXIST,
					"没有有效的关联关系");
		}


		if (EquipmentReviewStatus.WAITING_FOR_APPROVAL.equals(EquipmentReviewStatus.fromStatus(map.getReviewStatus()))) {
			map.setReviewerUid(user.getId());
			map.setReviewResult(cmd.getReviewResult());
			map.setReviewStatus(EquipmentReviewStatus.REVIEWED.getCode());
			map.setReviewTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

			equipmentProvider.updateEquipmentStandardMap(map);

			equipmentStandardMapSearcher.feedDoc(map);

//			String scope = EquipmentNotificationTemplateCode.SCOPE;
//			String locale = "zh_CN";
//
//			Map<String, Object> notifyMap = new HashMap<String, Object>();
//			notifyMap.put("equipmentName", equipment.getName());
//			int code = 0;
//			if(ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult())))
//				code = EquipmentNotificationTemplateCode.QUALIFIED_EQUIPMENT_NOTIFY_EXECUTOR;
//
//			if(ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult())))
//				code = EquipmentNotificationTemplateCode.UNQUALIFIED_EQUIPMENT_NOTIFY_EXECUTOR;
//
//			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");

			//发消息给管理员
//			List<Long> userIds = getEquipmentManagerIds(equipment);
//			for(Long uId : userIds) {
//				sendMessageToUser(uId, notifyTextForApplicant);
//			}


		} else {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_WAITING_FOR_APPROVAL_CAN_REVIEW,
					"只有待审核的设备-标准关联关系可以审核");
		}

	}

	private List<Long> getEquipmentManagerIds(EquipmentInspectionEquipments equipment) {

		List<Long> uIds = new ArrayList<Long>();

		List<Long> roles = new ArrayList<Long>();
		roles.add(RoleConstants.EQUIPMENT_MANAGER);

		ListOrganizationPersonnelByRoleIdsCommand cmd = new ListOrganizationPersonnelByRoleIdsCommand();

		cmd.setRoleIds(roles);
		cmd.setOrganizationId(equipment.getTargetId());

		ListOrganizationMemberCommandResponse resp = organizationService.listOrganizationPersonnelsByRoleIds(cmd);
		List<OrganizationMemberDTO> members = resp.getMembers();
		if (members != null && members.size() > 0) {
			for (OrganizationMemberDTO member : members) {
				uIds.add(member.getTargetId());
			}
		}

		return uIds;
	}

	@Override
	public void reviewEquipmentInspectionplan(ReviewEquipmentPlanCommand cmd) {
		//check auth
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_PLAN_REVIEW, cmd.getTargetId());

		EquipmentInspectionPlans plan = equipmentProvider.getEquipmmentInspectionPlanById(cmd.getId());
		if (plan == null || EquipmentPlanStatus.INACTIVE.equals(EquipmentPlanStatus.fromStatus(plan.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_PLAN_ALREADY_DELETED,
					"计划不存在或已失效");
		}
		if (EquipmentPlanStatus.WATTING_FOR_APPOVING.equals(EquipmentPlanStatus.fromStatus(plan.getStatus()))) {
			if (ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
				plan.setStatus(EquipmentPlanStatus.QUALIFIED.getCode());
			} else if (ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
				plan.setStatus(EquipmentPlanStatus.UN_QUALIFIED.getCode());
			}
			plan.setReviewerUid(UserContext.currentUserId());
			plan.setReviewTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			equipmentProvider.updateEquipmentInspectionPlan(plan);
			equipmentPlanSearcher.feedDoc(plan);
		}
	}

	@Override
	public void deleteEquipmentStandardRelations(
			DeleteEquipmentStandardRelationsCommand cmd) {
		/*Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_RELATION_DELETE, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);*/
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_RELATION_DELETE, cmd.getTargetId());

		EquipmentStandardMap map = equipmentProvider.findEquipmentStandardMapById(cmd.getId());
		if (map == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_STANDARD_MAP_NOT_EXIST,
					"没有有效的关联关系");
		}

		if (EquipmentReviewStatus.INACTIVE.equals(EquipmentReviewStatus.fromStatus(map.getReviewStatus()))) {
			map.setReviewStatus(EquipmentReviewStatus.DELETE.getCode());
			map.setReviewResult(ReviewResult.NONE.getCode());
			//fix bug #20247
			map.setStatus(EquipmentStandardStatus.INACTIVE.getCode());
			equipmentProvider.updateEquipmentStandardMap(map);
			equipmentStandardMapSearcher.feedDoc(map);
		} else {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_REVIEW_STATUS_ONLY_INACTIVE_CAN_DELETE,
					"只有已失效的设备-标准关联关系可以删除");
		}

	}

	@Override
	public void updateEquipments(UpdateEquipmentsCommand cmd) {
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_UPDATE, cmd.getTargetId());

		User user = UserContext.current().getUser();
		EquipmentInspectionEquipments equipment = null;
		if (cmd.getStatus() != null
				&& EquipmentStatus.IN_MAINTENANCE.equals(EquipmentStatus.fromStatus(cmd.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_STATUS_CANNOT_SET_IN_MAINTENANCE,
					"设备状态后台不能设为维修中");
		}

		List<EquipmentStandardMapDTO> eqStandardMap = cmd.getEqStandardMap();

		if (cmd.getId() == null) {
			createEquipmentInspectionEquipment(cmd, eqStandardMap);
		} else {
			EquipmentInspectionEquipments exist = verifyEquipment(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
			equipment = ConvertHelper.convert(cmd, EquipmentInspectionEquipments.class);
			equipment.setNamespaceId(exist.getNamespaceId());
			equipment.setCreateTime(exist.getCreateTime());
			equipment.setCreatorUid(exist.getCreatorUid());
			equipment.setOperatorUid(user.getId());
			equipment.setQrCodeToken(exist.getQrCodeToken());
			equipment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

			dealEquipmentRelateTime(cmd, equipment);
			checkEquipmentLngAndLat(cmd, equipment);
			equipmentProvider.updateEquipmentInspectionEquipment(equipment);
			equipmentSearcher.feedDoc(equipment);
			//删除计划关联表中的该设备 issue-36509 紫荆要求不删
//			equipmentProvider.deleteEquipmentPlansMapByEquipmentId(equipment.getId());
			List<Long> updateStandardIds = increamentUpdateEquipmentStandardMap(user, equipment, eqStandardMap);

			List<EquipmentStandardMap> maps = equipmentProvider.findByTarget(equipment.getId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
			if (maps != null) {
				for (EquipmentStandardMap map : maps) {
					if (!updateStandardIds.contains(map.getStandardId())) {
						map.setStatus(EquipmentStandardStatus.INACTIVE.getCode());
						equipmentProvider.updateEquipmentStandardMap(map);
						equipmentStandardMapSearcher.feedDoc(map);
						inactivePlansByEquipmentIdAndStandardId(equipment.getId(), map.getStandardId());
					}
				}
			}
			equipmentProvider.deleteEquipmentAccessoryMapByEquipmentId(equipment.getId());
			if (cmd.getEqAccessoryMap() != null) {
				for (EquipmentAccessoryMapDTO map : cmd.getEqAccessoryMap()) {
					map.setEquipmentId(equipment.getId());
					updateEquipmentAccessoryMap(map);
				}
			}
			deleteEquipmentAttachmentsByEquipmentId(equipment.getId());
			if (cmd.getAttachments() != null) {
				for (EquipmentAttachmentDTO attachment : cmd.getAttachments()) {
					attachment.setEquipmentId(equipment.getId());
					updateEquipmentAttachment(attachment, user.getId());
				}
			}
			//增加设备的操作记录
			createEquipmentOperateLogs(equipment.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(),
					equipment.getId(), EquipmentOperateActionType.UPDATE.getCode());
		}
	}

	private void inactivePlansByEquipmentIdAndStandardId(Long equipmentId, Long standardId) {
		equipmentProvider.deletePlanMapByEquipmentIdAndStandardId(equipmentId, standardId);
	}

	private void checkEquipmentLngAndLat(UpdateEquipmentsCommand cmd, EquipmentInspectionEquipments equipment) {
		equipment.setLatitude(cmd.getLatitude());
		equipment.setLongitude(cmd.getLongitude());
		if ((equipment.getLongitude() == null || equipment.getLatitude() == null) && (cmd.getQrCodeFlag() != null && cmd.getQrCodeFlag() == 1)) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_SET_LOCATION,
					"设备没有设置经纬度");
		}
		if (equipment.getLatitude() != null && equipment.getLongitude() != null) {
			String geohash = GeoHashUtils.encode(equipment.getLatitude(), equipment.getLongitude());
			equipment.setGeohash(geohash);
		}
	}

	private List<Long> increamentUpdateEquipmentStandardMap(User user, EquipmentInspectionEquipments equipment, List<EquipmentStandardMapDTO> eqStandardMap) {
		//不带id的create，其他的看map表中的standardId在不在cmd里面 不在的删掉
		List<Long> updateStandardIds = new ArrayList<Long>();
		if (eqStandardMap != null && eqStandardMap.size() > 0) {

			for (EquipmentStandardMapDTO dto : eqStandardMap) {
				List<EquipmentStandardMap> maps = equipmentProvider.findEquipmentStandardMap(dto.getStandardId(),
						equipment.getId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
				LOGGER.debug("equipment standard maps: {}", maps);
				if (maps == null || maps.size() == 0) {
					if (dto.getId() == null) {
						EquipmentStandardMap map = ConvertHelper.convert(dto, EquipmentStandardMap.class);
						map.setTargetId(equipment.getId());
						map.setTargetType(InspectionStandardMapTargetType.EQUIPMENT.getCode());
						map.setCreatorUid(user.getId());

						equipmentProvider.createEquipmentStandardMap(map);
						equipmentStandardMapSearcher.feedDoc(map);

						updateStandardIds.add(map.getStandardId());

					} else {
						EquipmentStandardMap map = equipmentProvider.findEquipmentStandardMap(dto.getId(), dto.getStandardId(),
								dto.getEquipmentId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
						if (map == null) {
							map = ConvertHelper.convert(dto, EquipmentStandardMap.class);
							map.setTargetId(equipment.getId());
							map.setTargetType(InspectionStandardMapTargetType.EQUIPMENT.getCode());
							map.setCreatorUid(user.getId());
							equipmentProvider.createEquipmentStandardMap(map);
							equipmentStandardMapSearcher.feedDoc(map);
						}
						updateStandardIds.add(map.getStandardId());
					}
				} else {
					//删除设备多次重复绑定的标准,仅保留最早绑的那个
					updateStandardIds.add(maps.get(0).getStandardId());
					maps.remove(0);
					LOGGER.debug("equipment standard maps after remove: {}", maps);
					if (maps.size() > 0) {
						maps.forEach(map -> {
							map.setStatus(Status.INACTIVE.getCode());
							equipmentProvider.updateEquipmentStandardMap(map);
							equipmentStandardMapSearcher.feedDoc(map);
						});
					}
				}
			}
		}
		return updateStandardIds;
	}

	private void dealEquipmentRelateTime(UpdateEquipmentsCommand cmd, EquipmentInspectionEquipments equipment) {
		if (cmd.getInstallationTime() != null)
			equipment.setInstallationTime(new Timestamp(cmd.getInstallationTime()));

		if (cmd.getRepairTime() != null)
			equipment.setRepairTime(new Timestamp(cmd.getRepairTime()));

		if (cmd.getBuyTime() != null)
			equipment.setBuyTime(new Timestamp(cmd.getBuyTime()));

		if (cmd.getDiscardTime() != null)
			equipment.setDiscardTime(new Timestamp(cmd.getDiscardTime()));

		if (cmd.getFactoryTime() != null)
			equipment.setFactoryTime(new Timestamp(cmd.getFactoryTime()));
	}

	private void createEquipmentInspectionEquipment(UpdateEquipmentsCommand cmd, List<EquipmentStandardMapDTO> eqStandardMap) {
		EquipmentInspectionEquipments equipment = ConvertHelper.convert(cmd, EquipmentInspectionEquipments.class);
		if ((equipment.getLongitude() == null || equipment.getLatitude() == null) && (cmd.getQrCodeFlag() != null && cmd.getQrCodeFlag() == 1)) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_SET_LOCATION,
					"设备没有设置经纬度");
		}
		String geohash = null;
		if (equipment.getLatitude() != null && equipment.getLongitude() != null) {
			geohash = GeoHashUtils.encode(equipment.getLatitude(), equipment.getLongitude());
		}

		dealEquipmentRelateTime(cmd, equipment);
		Long userId = UserContext.currentUserId();
		equipment.setGeohash(geohash);
		equipment.setCreatorUid(userId);
		equipment.setOperatorUid(userId);
		equipment.setNamespaceId(cmd.getNamespaceId());

		if (cmd.getTargetId() == null || cmd.getTargetId() == 0L) {
			List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(cmd.getOwnerId());
			if (communities != null && communities.size() > 0) {
				for (CommunityDTO community : communities) {
					equipment.setTargetId(community.getId());
					equipment.setTargetType(OwnerType.COMMUNITY.getCode());
					createEquipmentStage(cmd, eqStandardMap, equipment);
				}
			}
		} else {
			createEquipmentStage(cmd, eqStandardMap, equipment);
		}
		//增加设备的操作记录
		createEquipmentOperateLogs(equipment.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(),
				equipment.getId(), EquipmentOperateActionType.INSERT.getCode());
	}

	private void createEquipmentStage(UpdateEquipmentsCommand cmd, List<EquipmentStandardMapDTO> eqStandardMap, EquipmentInspectionEquipments equipment) {
		Long userId = UserContext.currentUserId();
		String tokenString = UUID.randomUUID().toString();
		equipment.setQrCodeToken(tokenString);
		equipmentProvider.creatEquipmentInspectionEquipment(equipment);
		equipmentSearcher.feedDoc(equipment);

		if (eqStandardMap != null && eqStandardMap.size() > 0) {
			for (EquipmentStandardMapDTO dto : eqStandardMap) {
				EquipmentStandardMap map = ConvertHelper.convert(dto, EquipmentStandardMap.class);
				map.setTargetId(equipment.getId());
				map.setTargetType(InspectionStandardMapTargetType.EQUIPMENT.getCode());
				map.setCreatorUid(userId);
				equipmentProvider.createEquipmentStandardMap(map);
				equipmentStandardMapSearcher.feedDoc(map);
			}
		}

		if (cmd.getEqAccessoryMap() != null) {
			for (EquipmentAccessoryMapDTO map : cmd.getEqAccessoryMap()) {
				map.setEquipmentId(equipment.getId());
				updateEquipmentAccessoryMap(map);
			}
		}

		if (cmd.getAttachments() != null) {
			for (EquipmentAttachmentDTO attachment : cmd.getAttachments()) {
				attachment.setEquipmentId(equipment.getId());
				updateEquipmentAttachment(attachment, userId);
			}
		}
	}

	private void createEquipmentOperateLogs(Integer namespaceId, Long ownerId, String ownerType, Long id, byte code) {
		EquipmentInspectionEquipmentLogs operateLog = new EquipmentInspectionEquipmentLogs();
		operateLog.setNamespaceId(namespaceId);
		operateLog.setOwnerId(ownerId);
		operateLog.setOwnerType(ownerType);
		operateLog.setTargetId(id);
		operateLog.setTargetType(EquipmentOperateObjectType.EQUIPMENT.getOperateObjectType());
		operateLog.setProcessType(code);
		equipmentProvider.createEquipmentOperateLogs(operateLog);
	}

	private void populateEquipmentStandards(EquipmentsDTO dto) {
		List<EquipmentStandardMapDTO> equipmentStandardMap = new ArrayList<EquipmentStandardMapDTO>();
		List<EquipmentStandardMap> maps = equipmentProvider.findByTarget(dto.getId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
		if (maps != null && maps.size() > 0) {
			for (EquipmentStandardMap map : maps) {
				EquipmentStandardMapDTO mapdto = ConvertHelper.convert(map, EquipmentStandardMapDTO.class);
				mapdto.setEquipmentId(map.getTargetId());
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(mapdto.getStandardId());
				if (standard != null) {
					mapdto.setStandardName(standard.getName());
					mapdto.setStandardType(standard.getStandardType());
					mapdto.setRepeatType(standard.getRepeatType());
				}
				equipmentStandardMap.add(mapdto);
			}
		}

		dto.setEqStandardMap(equipmentStandardMap);
	}

	private void updateEquipmentParameter(EquipmentParameterDTO dto) {

		EquipmentInspectionEquipmentParameters parameter = ConvertHelper.convert(dto,
				EquipmentInspectionEquipmentParameters.class);

		if (dto.getId() == null) {
			equipmentProvider.creatEquipmentParameter(parameter);
		} else {
			equipmentProvider.updateEquipmentParameter(parameter);
		}

	}

	private void updateEquipmentAccessoryMap(EquipmentAccessoryMapDTO dto) {

		EquipmentInspectionAccessoryMap map = ConvertHelper.convert(dto, EquipmentInspectionAccessoryMap.class);

		if (dto.getEqAccessories() != null) {
			map.setAccessoryId(dto.getEqAccessories().getId());
			equipmentProvider.creatEquipmentAccessoryMap(map);
		}
		//==
		/*if(dto.getId() == null) {
			equipmentProvider.creatEquipmentAccessoryMap(map);
		} else {
			equipmentProvider.updateEquipmentAccessoryMap(map);
		}*/
	}

	private void deleteEquipmentAttachmentsByEquipmentId(Long equipmentId) {
		List<EquipmentInspectionEquipmentAttachments> attachments = equipmentProvider.findEquipmentAttachmentsByEquipmentId(equipmentId);
		if (attachments != null && attachments.size() > 0) {
			attachments.forEach(attachment -> {
				equipmentProvider.deleteEquipmentAttachmentById(attachment.getId());
			});
		}
	}

	private void updateEquipmentAttachment(EquipmentAttachmentDTO dto, Long uid) {

		EquipmentInspectionEquipmentAttachments attachment = ConvertHelper.convert(dto, EquipmentInspectionEquipmentAttachments.class);

//		if(dto.getId() != null) {
//			equipmentProvider.deleteEquipmentAttachmentById(dto.getId());
//		}

		attachment.setCreatorUid(uid);
		equipmentProvider.creatEquipmentAttachment(attachment);
	}

	@Override
	public void deleteEquipments(DeleteEquipmentsCommand cmd) {
		User user = UserContext.current().getUser();

		EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());

		if (EquipmentStatus.INACTIVE.equals(EquipmentStatus.fromStatus(equipment.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_ALREADY_DELETED,
					"设备已删除");
		}
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_DELETE, equipment.getTargetId());

		equipment.setDeleterUid(user.getId());
		equipment.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		equipment.setOperatorUid(user.getId());
		equipment.setStatus(EquipmentStandardStatus.INACTIVE.getCode());

		equipmentProvider.updateEquipmentInspectionEquipment(equipment);
		equipmentSearcher.feedDoc(equipment);

		List<EquipmentStandardMap> maps = equipmentProvider.findByTarget(equipment.getId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
		if (maps != null && maps.size() > 0) {
			for (EquipmentStandardMap map : maps) {
				inActiveEquipmentStandardRelations(map);
			}
		}

		//inactiveTasksByEquipmentId(equipment.getId());
		//新的模式中直接删除计划中设备的关系表即可
		equipmentProvider.deleteEquipmentPlansMapByEquipmentId(equipment.getId());
		//增加设备的操作记录
		createEquipmentOperateLogs(equipment.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(), equipment.getId(), EquipmentOperateActionType.DELETE.getCode());
	}

	/*private void inactiveTasksByStandardId(Long standardId) {
		int pageSize = 200;

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
        	List<EquipmentInspectionTasks> tasks = equipmentProvider.listTasksByStandardId(standardId, locator, pageSize);

            if(tasks.size() > 0) {
                for(EquipmentInspectionTasks task : tasks) {
                	task.setStatus(EquipmentTaskStatus.NONE.getCode());
                	equipmentProvider.updateEquipmentTask(task);

                	equipmentTasksSearcher.feedDoc(task);
                }
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }


	}

	private void inactiveTasksByEquipmentId(Long equipmentId) {
		int pageSize = 200;

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
        	List<EquipmentInspectionTasks> tasks = equipmentProvider.listTasksByEquipmentId(equipmentId, null, null, null, locator, pageSize, null);

            if(tasks.size() > 0) {
                for(EquipmentInspectionTasks task : tasks) {
                	task.setStatus(EquipmentTaskStatus.NONE.getCode());
                	equipmentProvider.updateEquipmentTask(task);

                	equipmentTasksSearcher.feedDoc(task);
                }
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }


	}

	private void inactiveTasks(Long equipmentId, Long standardId) {

		int pageSize = 200;
        List<Long> standardIds = new ArrayList<Long>();
        standardIds.add(standardId);
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
        	List<EquipmentInspectionTasks> tasks = equipmentProvider.listTasksByEquipmentId(equipmentId, standardIds, null, null, locator, pageSize, null);

            if(tasks.size() > 0) {
                for(EquipmentInspectionTasks task : tasks) {
                	task.setStatus(EquipmentTaskStatus.NONE.getCode());
                	equipmentProvider.updateEquipmentTask(task);

                	equipmentTasksSearcher.feedDoc(task);
                }
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }


	}*/

	@Override
	public void exportEquipments(SearchEquipmentsCommand cmd, HttpServletResponse response) {
		List<EquipmentsDTO> dtos = null;
		if (StringUtils.isNotEmpty(cmd.getEquipmentIds())) {
			List<Long> equipmentIds = new ArrayList<>();
			String[] ids = cmd.getEquipmentIds().split(",");
			for (String id : ids) {
				equipmentIds.add(Long.valueOf(id));
			}
			dtos = equipmentIds.stream().map((e) -> ConvertHelper.convert(equipmentProvider.findEquipmentById(e), EquipmentsDTO.class))
					.collect(Collectors.toList());
		} else {
			cmd.setTargetIds(transferCommandForExport(cmd.getTargetIdString()));
			SearchEquipmentsResponse equipments = equipmentSearcher.queryEquipments(cmd);
			dtos = equipments.getEquipment();
		}

		//把DTO中状态代码转换成映射String
		List<ExportEquipmentData> data = dtos.stream().map(this::toExportEquipment).collect(Collectors.toList());
		if (data != null && dtos.size() > 0) {
			String fileName = String.format("巡检对象%s", DateUtil.dateToStr(new java.util.Date(), DateUtil.DATE_TIME_NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(response, fileName, "巡检对象");

			ListFieldCommand listFieldCommand = ConvertHelper.convert(cmd, ListFieldCommand.class);
			List<FieldDTO> fields = fieldService.listFields(listFieldCommand);
			List<String> propertyNames = new ArrayList<>();
			List<String> titleNames = new ArrayList<>();
			List<Integer> titleSizes = new ArrayList<>();
			excelUtils.setNeedSequenceColumn(true);
			//fieldService.listFields();
			for (FieldDTO field : fields) {
				//去除附件 经纬度cell
				if (!(Objects.equals(field.getFieldName(), "attachments") || Objects.equals(field.getFieldName(), "geohash"))) {
					propertyNames.add(field.getFieldName());
					titleNames.add(field.getFieldDisplayName());
					titleSizes.add(20);
				}
			}

			excelUtils.writeExcel(propertyNames, titleNames, titleSizes, data);
		} else {
			throw RuntimeErrorException.errorWith(ParkingLocalStringCode.SCOPE_STRING,
					Integer.parseInt(ParkingLocalStringCode.NO_DATA), "no data");
		}
	}

	private ExportEquipmentData toExportEquipment(EquipmentsDTO equipmentsDTO) {
		ExportEquipmentData data = ConvertHelper.convert(equipmentsDTO, ExportEquipmentData.class);
		//convert status
		if (equipmentsDTO.getQrCodeFlag() != null) {
			if (equipmentsDTO.getQrCodeFlag() == 0) {
				data.setQrCodeFlag("停用");
			} else {
				data.setQrCodeFlag("启用");
			}
		}

		if (equipmentsDTO.getStatus() != null) {
			data.setStatus(EquipmentStatus.fromStatus(equipmentsDTO.getStatus()).getName());
		}
		if (equipmentsDTO.getCategoryId() != null && equipmentsDTO.getCategoryId() != 0) {
			data.setCategoryId(EquipmentCategories.fromStatus(equipmentsDTO.getCategoryId().byteValue()).getName());
		}

		return data;
	}

	/*public void createEquipmentsBook(String path,List<EquipmentsDTO> dtos) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("equipments");

		this.createEquipmentsBookSheetHead(sheet);
		for (EquipmentsDTO dto : dtos ) {
			this.setNewEquipmentsBookRow(sheet, dto);
		}

		try {
			FileOutputStream out = new FileOutputStream(path);

			wb.write(out);
			wb.close();
			out.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}

	}*/

	/*private void createEquipmentsBookSheetHead(Sheet sheet){

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		row.createCell(++i).setCellValue("自编号");
		row.createCell(++i).setCellValue("设备名称");
		row.createCell(++i).setCellValue("设备类型");
		row.createCell(++i).setCellValue("二维码状态");
		row.createCell(++i).setCellValue("设备当前状态");
	}

	private void setNewEquipmentsBookRow(Sheet sheet ,EquipmentsDTO dto){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;
		row.createCell(++i).setCellValue(dto.getCustomNumber());
		row.createCell(++i).setCellValue(dto.getName());
		row.createCell(++i).setCellValue(dto.getCategoryPath());
		if(dto.getQrCodeFlag() == 0)
			row.createCell(++i).setCellValue("停用");
		if(dto.getQrCodeFlag() == 1)
			row.createCell(++i).setCellValue("启用");
//		row.createCell(++i).setCellValue(dto.getStandardName());
		row.createCell(++i).setCellValue(EquipmentStatus.fromStatus(dto.getStatus()).getName());
//		if(EquipmentReviewStatus.DELETE.equals(EquipmentReviewStatus.fromStatus(dto.getStatus()))) {
//			row.createCell(++i).setCellValue("");
//		}
//		if(!EquipmentReviewStatus.DELETE.equals(EquipmentReviewStatus.fromStatus(dto.getStatus()))){
////			row.createCell(++i).setCellValue(EquipmentReviewStatus.fromStatus(dto.getReviewStatus()).getName());
//		}

//		if(ReviewResult.NONE.equals(ReviewResult.fromStatus(dto.getReviewResult())))
//			row.createCell(++i).setCellValue("");
//		if(ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(dto.getReviewResult())))
//			row.createCell(++i).setCellValue("审核通过");
//		if(ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(dto.getReviewResult())))
//			row.createCell(++i).setCellValue("审核不通过");

	}*/

	@Override
	public EquipmentAccessoriesDTO updateEquipmentAccessories(
			UpdateEquipmentAccessoriesCommand cmd) {
		EquipmentInspectionAccessories accessory = ConvertHelper.convert(cmd, EquipmentInspectionAccessories.class);
		if (cmd.getId() == null) {
			accessory.setNamespaceId(cmd.getNamespaceId());
			accessory.setStatus((byte) 1);
			equipmentProvider.creatEquipmentInspectionAccessories(accessory);
		} else {
			EquipmentInspectionAccessories exist = verifyEquipmentAccessories(accessory.getId(), accessory.getOwnerType(), accessory.getOwnerId());
			accessory.setNamespaceId(exist.getNamespaceId());
			equipmentProvider.updateEquipmentInspectionAccessories(accessory);
		}

		equipmentAccessoriesSearcher.feedDoc(accessory);
		return ConvertHelper.convert(accessory, EquipmentAccessoriesDTO.class);
	}

	private EquipmentInspectionAccessories verifyEquipmentAccessories(Long accessoryId, String ownerType, Long ownerId) {

		EquipmentInspectionAccessories accessory = equipmentProvider.findAccessoryById(accessoryId);
		if (accessory == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_ACCESSORY_NOT_EXIST,
					"备品备件不存在");
		}

		return accessory;
	}

	@Override
	public void deleteEquipmentAccessories(DeleteEquipmentAccessoriesCommand cmd) {
		EquipmentInspectionAccessories accessory = verifyEquipmentAccessories(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
		if (accessory.getStatus() == 0) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_ACCESSORY_ALREADY_DELETED,
					"备品备件已失效");
		}

		accessory.setStatus((byte) 0);
		equipmentProvider.updateEquipmentInspectionAccessories(accessory);
		equipmentAccessoriesSearcher.deleteById(accessory.getId());
	}

	@Override
	public HttpServletResponse exportEquipmentAccessories(
			SearchEquipmentAccessoriesCommand cmd, HttpServletResponse response) {
		Integer pageSize = Integer.MAX_VALUE;
		cmd.setPageSize(pageSize);
		cmd.setTargetIds(transferCommandForExport(cmd.getTargetIdString()));
		SearchEquipmentAccessoriesResponse accessories = equipmentAccessoriesSearcher.query(cmd);
		List<EquipmentAccessoriesDTO> dtos = accessories.getAccessories();

		URL rootPath = EquipmentServiceImpl.class.getResource("/");
		String filePath = rootPath.getPath() + this.downloadDir;
		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();
		filePath = filePath + "EquipmentAccessories" + System.currentTimeMillis() + ".xlsx";
		//新建了一个文件
		this.createEquipmentAccessoriesBook(filePath, dtos);

		return download(filePath, response);
	}

	private List<Long> transferCommandForExport(String targetIdString) {
		if(StringUtils.isNotBlank(targetIdString)){
			String[] targetIds = targetIdString.split(",");
			return Arrays.stream(targetIds).map(Long::valueOf).collect(Collectors.toList());
		}
		return null;
	}

	private void createEquipmentAccessoriesBook(String path, List<EquipmentAccessoriesDTO> dtos) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("equipmentAccessories");

		this.createEquipmentAccessoriesBookSheetHead(sheet);
		for (EquipmentAccessoriesDTO dto : dtos) {
			this.setNewEquipmentAccessoriesBookRow(sheet, dto);
		}

		try {
			FileOutputStream out = new FileOutputStream(path);

			wb.write(out);
			wb.close();
			out.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}

	}

	private void createEquipmentAccessoriesBookSheetHead(Sheet sheet) {

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i = -1;
		row.createCell(++i).setCellValue("所属管理处");
		row.createCell(++i).setCellValue("备品名称");
		row.createCell(++i).setCellValue("生产厂商");
		row.createCell(++i).setCellValue("备品型号");
		row.createCell(++i).setCellValue("规格");
		row.createCell(++i).setCellValue("存放地点");
	}

	private void setNewEquipmentAccessoriesBookRow(Sheet sheet, EquipmentAccessoriesDTO dto) {
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		int i = -1;
		row.createCell(++i).setCellValue(dto.getTargetName());
		row.createCell(++i).setCellValue(dto.getName());
		row.createCell(++i).setCellValue(dto.getManufacturer());
		row.createCell(++i).setCellValue(dto.getModelNumber());
		row.createCell(++i).setCellValue(dto.getSpecification());
		row.createCell(++i).setCellValue(dto.getLocation());

	}

	private Timestamp addDays(Timestamp now, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, days);
		return new Timestamp(calendar.getTimeInMillis());
	}

	@Override
	public EquipmentTaskDTO reportEquipmentTask(ReportEquipmentTaskCommand cmd) {

		User user = UserContext.current().getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		EquipmentInspectionTasks task = verifyEquipmentTask(cmd.getTaskId(), cmd.getOwnerType(), cmd.getOwnerId());
		//对接物业报修 所有上报任务都是状态已完成
//		Timestamp laterTime = DateUtils.getLaterTime(task.getExecutiveExpireTime(), task.getProcessExpireTime());
//		if(EquipmentTaskStatus.WAITING_FOR_EXECUTING.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))
//				 && laterTime.before(now)) {
//			equipmentProvider.closeTask(task);
//		} else if(EquipmentTaskStatus.IN_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))
//				 && task.getProcessExpireTime() != null && task.getProcessExpireTime().before(now)) {
//			equipmentProvider.closeTask(task);
//		}
//		if (EquipmentTaskStatus.WAITING_FOR_EXECUTING.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))
//				&& task.getExecutiveExpireTime().before(now)) {
//			equipmentProvider.closeTask(task);
//			equipmentTasksSearcher.feedDoc(task);
//		}

		if (EquipmentTaskStatus.CLOSE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
			LOGGER.error("task is closed");
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_CLOSE,
					"该任务已关闭");
		}

		if (EquipmentTaskStatus.NONE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
			LOGGER.error("task is inactive");
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_INACTIVE,
					"该任务已失效");
		}

		//process_time operator_type operator_id
		if (EquipmentTaskStatus.WAITING_FOR_EXECUTING.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
			//改为统一设置
			SetReviewExpireDaysCommand command = new SetReviewExpireDaysCommand();
			command.setCommunityId(task.getTargetId());
			command.setNamespaceId(task.getNamespaceId());
			EquipmentInspectionReviewDateDTO date = listReviewExpireDays(command);
			if (date != null) {
				task.setReviewExpiredDate(addDays(now, date.getReviewExpiredDays()));
			} else {
				//没有审批过期时间的情况
				task.setReviewExpiredDate(addDays(now, Integer.MAX_VALUE - 1));
			}

			EquipmentInspectionTasksLogs log = new EquipmentInspectionTasksLogs();
			log.setTaskId(task.getId());
			log.setOperatorType(OwnerType.USER.getCode());
			log.setOperatorId(user.getId());

			task.setStatus(EquipmentTaskStatus.CLOSE.getCode());
			task.setExecutiveTime(now);
			task.setExecutorType(OwnerType.USER.getCode());
			task.setExecutorId(user.getId());
			log.setProcessType(EquipmentTaskProcessType.COMPLETE.getCode());//日志记录类型
			if (task.getExecutiveExpireTime() == null || now.before(task.getExecutiveExpireTime())) {
				//task.setResult(EquipmentTaskResult.COMPLETE_OK.getCode());
				log.setProcessResult(EquipmentTaskProcessResult.COMPLETE_OK.getCode());
			} else {
				//task.setResult(EquipmentTaskResult.COMPLETE_DELAY.getCode());
				log.setProcessResult(EquipmentTaskProcessResult.COMPLETE_DELAY.getCode());
			}

			EquipmentTaskDTO dto = null;
			equipmentProvider.updateEquipmentTask(task);
			equipmentTasksSearcher.feedDoc(task);
			//多个设备巡检详情上报(一个任务支持多个设备)
			for (int i = 0; i < cmd.getEquipmentTaskReportDetails().size(); i++) {
				EquipmentTaskReportDetail reportDetail = cmd.getEquipmentTaskReportDetails().get(i);
				if (reportDetail.getMessage() != null) {
					log.setProcessMessage(reportDetail.getMessage());
				}
				//任务多设备log表增加设备id
				log.setEquipmentId(cmd.getEquipmentTaskReportDetails().get(i).getEquipmentId());
				dto = updateEquipmentTasksAttachmentAndLogs(task, log, cmd.getEquipmentTaskReportDetails().get(i).getAttachments());

				List<InspectionItemResult> itemResults = reportDetail.getItemResults();
				if (itemResults != null && itemResults.size() > 0) {

					for (InspectionItemResult itemResult : itemResults) {
						EquipmentInspectionItemResults result = ConvertHelper.convert(itemResult, EquipmentInspectionItemResults.class);
						result.setTaskLogId(log.getId());
						result.setCommunityId(task.getTargetId());
						//这里因为一个任务对应多个设备了 需要增加设备id来确定log是哪个设备的  之前是通过taskid确认因为一对一
						result.setEquipmentId(reportDetail.getEquipmentId());
						result.setStandardId(reportDetail.getStandardId());
						result.setInspectionCategoryId(task.getInspectionCategoryId());
						result.setNamespaceId(task.getNamespaceId());
						equipmentProvider.createEquipmentInspectionItemResults(result);
					}
				}
			}
			return dto;
		} else {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_NOT_WAITING_EXECUTE_OR_IN_MAINTENANCE,
					"只有待执行的任务可以上报");
		}

	}

	private EquipmentTaskDTO updateEquipmentTasksAttachmentAndLogs(EquipmentInspectionTasks task,
																   EquipmentInspectionTasksLogs log, List<Attachment> attachmentList) {

//		equipmentProvider.updateEquipmentTask(task);
//		equipmentTasksSearcher.feedDoc(task);

		log.setInspectionCategoryId(task.getInspectionCategoryId());
		log.setCommunityId(task.getTargetId());
		log.setNamespaceId(task.getNamespaceId());
		equipmentProvider.createEquipmentInspectionTasksLogs(log);

		User user = UserContext.current().getUser();
		processLogAttachments(user.getId(), attachmentList, log);

		List<EquipmentInspectionTasks> tasks = new ArrayList<EquipmentInspectionTasks>();
		tasks.add(task);
		List<EquipmentTaskDTO> dtos = convertEquipmentTasksToDTO(tasks);
		if (dtos != null && dtos.size() > 0)
			return dtos.get(0);

		return null;
	}

	private List<EquipmentTaskDTO> convertEquipmentTasksToDTO(List<EquipmentInspectionTasks> tasks) {
		if (tasks != null && tasks.size() > 0) {
			return tasks.stream().
					map(this::convertEquipmentTaskToDTO).
					filter(Objects::nonNull).
					collect(Collectors.toList());
		}
		return null;
	}

	private EquipmentTaskDTO convertEquipmentTaskToDTO(EquipmentInspectionTasks task) {
		long startTime = System.currentTimeMillis();
		EquipmentTaskDTO dto = ConvertHelper.convert(task, EquipmentTaskDTO.class);

		EquipmentInspectionPlans plan = equipmentProvider.getEquipmmentInspectionPlanById(task.getPlanId());
		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
		if (plan != null) {
			dto.setTaskType(plan.getPlanType());
		} else if (standard != null) {
			dto.setTaskType(standard.getStandardType());
		}
		List<EquipmentInspectionEquipmentPlanMap> planMaps = equipmentProvider.getEquipmentInspectionPlanMap(task.getPlanId());
		List<EquipmentStandardRelationDTO> equipmentStandardRelations = new ArrayList<>();
		if (planMaps != null && planMaps.size() > 0) {
			for (EquipmentInspectionEquipmentPlanMap map : planMaps) {
				EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getEquipmentId());
				EquipmentStandardRelationDTO relationDTO = new EquipmentStandardRelationDTO();
				relationDTO.setEquipmentName(equipment.getName());
				relationDTO.setLocation(equipment.getLocation());
				relationDTO.setEquipmentId(equipment.getId());
				relationDTO.setStandardId(map.getStandardId());
				relationDTO.setQrCodeFlag(equipment.getQrCodeFlag());
				equipmentStandardRelations.add(relationDTO);
			}
		}
		//兼容之前的版本任务
		if (dto.getEquipments() == null || dto.getEquipments().size() == 0) {
			EquipmentInspectionEquipments equipment = null;
			if (task.getEquipmentId() != null) {
				equipment = equipmentProvider.findEquipmentById(task.getEquipmentId());
			}
			if (null != equipment) {
				EquipmentStandardRelationDTO relationDTO = new EquipmentStandardRelationDTO();
				relationDTO.setEquipmentName(equipment.getName());
				relationDTO.setLocation(equipment.getLocation());
				equipmentStandardRelations.add(relationDTO);
			}
		}
		dto.setEquipments(equipmentStandardRelations);

		if (task.getExecutorId() != null && task.getExecutorId() != 0) {
			//总公司分公司 by xiongying20170328
			List<OrganizationMember> executors = organizationProvider.listOrganizationMembersByUId(task.getExecutorId());
//            	OrganizationMember executor = organizationProvider.findOrganizationMemberByUIdAndOrgId(task.getExecutorId(), task.getOwnerId());
			if (executors != null && executors.size() > 0) {
				dto.setExecutorName(executors.get(0).getContactName());
			}
		}

		if (task.getOperatorId() != null && task.getOperatorId() != 0) {
			List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(task.getOperatorId());
			if (operators != null && operators.size() > 0) {
				dto.setOperatorName(operators.get(0).getContactName());
			}
		}

		if (task.getReviewerId() != null && task.getReviewerId() != 0) {
			List<OrganizationMember> reviewers = organizationProvider.listOrganizationMembersByUId(task.getReviewerId());
			if (reviewers != null && reviewers.size() > 0) {
				dto.setReviewerName(reviewers.get(0).getContactName());
			}
		}

		long endTime = System.currentTimeMillis();

		LOGGER.debug("TrackUserRelatedCost: convertEquipmentTaskToDTO elapse: " + (endTime - startTime));

		return dto;
	}

	private void processLogAttachments(long userId, List<Attachment> attachmentList, EquipmentInspectionTasksLogs log) {
		List<EquipmentInspectionTasksAttachments> results = null;

		if (attachmentList != null) {
			results = new ArrayList<>();

			EquipmentInspectionTasksAttachments attachment = null;
			for (Attachment descriptor : attachmentList) {
				attachment = new EquipmentInspectionTasksAttachments();
				attachment.setCreatorUid(userId);
				attachment.setLogId(log.getId());
				attachment.setTaskId(log.getTaskId());
				attachment.setContentType(descriptor.getContentType());
				attachment.setContentUri(descriptor.getContentUri());
				attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

				try {
					this.equipmentProvider.createEquipmentInspectionTasksAttachment(attachment);
					results.add(attachment);
				} catch (Exception e) {
					LOGGER.error("Failed to save the attachment, userId=" + userId
							+ ", attachment=" + attachment, e);
				}
			}
			log.setAttachments(results);
		}
	}

	private void populateLogAttachements(EquipmentInspectionTasksLogs log, List<EquipmentInspectionTasksAttachments> attachmentList) {

		if (attachmentList == null || attachmentList.size() == 0) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The log attachment list is empty, logId=" + log.getId());
			}
		} else {
			for (EquipmentInspectionTasksAttachments attachment : attachmentList) {
				populateLogAttachement(log, attachment);
			}
		}
	}

	private void populateLogAttachement(EquipmentInspectionTasksLogs log, EquipmentInspectionTasksAttachments attachment) {

		if (attachment == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The log attachment is null, logId=" + log.getId());
			}
		} else {

			String contentUri = attachment.getContentUri();
			if (contentUri != null && contentUri.length() > 0) {
				try {
					String url = contentServerService.parserUri(contentUri, EntityType.USER.getCode(), UserContext.current().getUser().getId());
					attachment.setContentUrl(convertAttachmentURL(url));
				} catch (Exception e) {
					LOGGER.error("Failed to parse attachment uri, logId=" + log.getId() + ", attachmentId=" + attachment.getId(), e);
				}
			} else {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn("The content uri is empty, attchmentId=" + attachment.getId());
				}
			}
		}
	}

	@Override
	public void reviewEquipmentTask(ReviewEquipmentTaskCommand cmd) {
		User user = UserContext.current().getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		EquipmentInspectionTasks task = verifyEquipmentTask(cmd.getTaskId(), cmd.getOwnerType(), cmd.getOwnerId());

//		if((EquipmentTaskStatus.CLOSE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))
//				 || EquipmentTaskStatus.NEED_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(task.getStatus())))
//				 && task.getReviewExpiredDate() != null && task.getReviewExpiredDate().before(now)) {
//			equipmentProvider.closeReviewTasks(task);
//		}


//		if((EquipmentTaskStatus.CLOSE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))
//				 || EquipmentTaskStatus.NEED_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(task.getStatus())))
//				&& ReviewResult.REVIEW_DELAY.equals(EquipmentTaskResult.fromStatus(task.getReviewResult()))) {
//			LOGGER.error("task is closed");
//			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
//					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_CLOSE,
//				"该任务已关闭");
//		}
//		if((EquipmentTaskStatus.CLOSE.equals(EquipmentTaskStatus.fromStatus(task.getStatus())))
//				&& task.getReviewExpiredDate() != null && task.getReviewExpiredDate().before(now)) {
//			//判断审批过期
//			equipmentProvider.closeReviewTasks(task);
//		}

		if (EquipmentTaskStatus.NONE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
			LOGGER.error("task is inactive");
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_INACTIVE,
					"该任务已失效");
		}

		EquipmentInspectionTasksLogs log = new EquipmentInspectionTasksLogs();
		log.setTaskId(task.getId());
		log.setOperatorType(OwnerType.USER.getCode());
		log.setOperatorId(user.getId());

		//task.setReviewResult(cmd.getReviewResult());
		task.setReviewerId(user.getId());
		task.setReviewTime(new Timestamp(System.currentTimeMillis()));

		log.setProcessType(ProcessType.REVIEW.getCode());
		if (ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
			log.setProcessResult(EquipmentTaskProcessResult.REVIEW_QUALIFIED.getCode());
		}
		if (ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
			log.setProcessResult(EquipmentTaskProcessResult.REVIEW_UNQUALIFIED.getCode());
		}
		//0:none, 1: qualified, 2: unqualified
		if (EquipmentTaskStatus.CLOSE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {

			if (ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
				task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
			} else if (ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
				task.setStatus(EquipmentTaskStatus.QUALIFIED.getCode());
			}
//			if(ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult())) &&
//					(EquipmentTaskResult.COMPLETE_DELAY.equals(EquipmentTaskResult.fromStatus(task.getResult())) ||
//							EquipmentTaskResult.COMPLETE_OK.equals(EquipmentTaskResult.fromStatus(task.getResult())))) {
//				task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
//				task.setResult(EquipmentTaskResult.NONE.getCode());
//			}
//
//			if(ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult())) &&
//					(EquipmentTaskResult.NEED_MAINTENANCE_DELAY_COMPLETE_DELAY.equals(EquipmentTaskResult.fromStatus(task.getResult())) ||
//							EquipmentTaskResult.NEED_MAINTENANCE_DELAY_COMPLETE_OK.equals(EquipmentTaskResult.fromStatus(task.getResult())) ||
//							EquipmentTaskResult.NEED_MAINTENANCE_OK_COMPLETE_OK.equals(EquipmentTaskResult.fromStatus(task.getResult())) ||
//							EquipmentTaskResult.NEED_MAINTENANCE_OK_COMPLETE_DELAY.equals(EquipmentTaskResult.fromStatus(task.getResult())))) {
//				task.setStatus(EquipmentTaskStatus.IN_MAINTENANCE.getCode());
//			}
		}

//		else if(EquipmentTaskStatus.NEED_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
//
//			if(ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
//				task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
//				task.setResult(EquipmentTaskResult.NONE.getCode());
//			}
//
//			else if(ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
//				task.setStatus(EquipmentTaskStatus.IN_MAINTENANCE.getCode());
//			}
//		}

//     维修人 维修时间 维修类型对接物业报修后去除
//		if(cmd.getOperatorType() != null) {
//			task.setOperatorType(cmd.getOperatorType());
//			log.setTargetType(cmd.getOperatorType());
//		}
//
//		if(cmd.getOperatorId() != null) {
//			task.setOperatorId(cmd.getOperatorId());
//			log.setTargetId(cmd.getOperatorId());
//		}
//
//		if(cmd.getEndTime() != null) {
//			task.setProcessExpireTime(new Timestamp(cmd.getEndTime()));
//			log.setProcessEndTime(task.getProcessExpireTime());
//		}

//		if(!StringUtils.isEmpty(cmd.getOperatorType()) && cmd.getOperatorId() != null
//				 && cmd.getEndTime() != null) {
////			OrganizationMember reviewer = organizationProvider.findOrganizationMemberByUIdAndOrgId(task.getReviewerId(), task.getOwnerId());
////			OrganizationMember operator = organizationProvider.findOrganizationMemberByUIdAndOrgId(task.getOperatorId(), task.getExecutiveGroupId());
//			List<OrganizationMember> reviewers = organizationProvider.listOrganizationMembersByUId(task.getReviewerId());
//			List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(task.getOperatorId());
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("reviewerName", reviewers.get(0).getContactName());
//			//以下都可以去除 暂时保留   v3.0.3
//			map.put("operatorName", operators.get(0).getContactName());
//			map.put("deadline", timeToStr(new Timestamp(cmd.getEndTime())));
//
//			String scope = EquipmentNotificationTemplateCode.SCOPE;
//			String locale = "zh_CN";
//			int msgCode = EquipmentNotificationTemplateCode.ASSIGN_TASK_MSG;
//			String msg = localeTemplateService.getLocaleTemplateString(scope, msgCode, locale, map, "");
//			log.setProcessMessage(msg);
//
//			Map<String, Object> notifyMap = new HashMap<String, Object>();
//			notifyMap.put("deadline", timeToStr(new Timestamp(cmd.getEndTime())));
//			int code = EquipmentNotificationTemplateCode.ASSIGN_TASK_NOTIFY_OPERATOR;
//			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
//			//sendMessageToUser(cmd.getOperatorId(), notifyTextForApplicant);
//		}
		equipmentProvider.updateEquipmentTask(task);
		equipmentTasksSearcher.feedDoc(task);
		updateEquipmentTasksAttachmentAndLogs(task, log, null);
	}

	@Override
	public void reviewEquipmentTasks(ReviewEquipmentTasksCommand cmd) {
		//可以合并为一个接口 新模式 后面再改
		cmd.getTaskIds().forEach(taskId -> {
			ReviewEquipmentTaskCommand command = new ReviewEquipmentTaskCommand();
			command.setEndTime(cmd.getEndTime());
			command.setOperatorId(cmd.getOperatorId());
			command.setOperatorType(cmd.getOperatorType());
			command.setOwnerId(cmd.getOwnerId());
			command.setOwnerType(cmd.getOwnerType());
			command.setReviewResult(cmd.getReviewResult());
			command.setTaskId(taskId);
			reviewEquipmentTask(command);
		});
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

	private String timeToStr(Timestamp time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(time);
	}

	@Override
	public void createEquipmentTask(DeleteEquipmentsCommand cmd) {

		List<EquipmentStandardMap> maps = equipmentProvider.listQualifiedEquipmentStandardMap(cmd.getEquipmentId());

		if (maps != null && maps.size() > 0) {
			for (EquipmentStandardMap map : maps) {
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(map.getStandardId());
				EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getTargetId());
				if (standard == null || standard.getStatus() == null
						|| !EquipmentStandardStatus.ACTIVE.equals(EquipmentStandardStatus.fromStatus(standard.getStatus()))) {
					LOGGER.info("EquipmentInspectionScheduleJob standard is not exist or active! standardId = " + map.getStandardId());
					continue;
				} else if (equipment == null || !EquipmentStatus.IN_USE.equals(EquipmentStatus.fromStatus(equipment.getStatus()))) {
					LOGGER.info("EquipmentInspectionScheduleJob equipment is not exist or active! equipmentId = " + map.getTargetId());
					continue;
				} else {
					boolean isRepeat = repeatService.isRepeatSettingActive(standard.getRepeatSettingId());
					LOGGER.info("EquipmentInspectionScheduleJob: standard id = " + standard.getId()
							+ "repeat setting id = " + standard.getRepeatSettingId() + "is repeat setting active: " + isRepeat);
					if (isRepeat) {
						this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_EQUIPMENT_TASK.getCode()).tryEnter(() -> {
							creatTaskByStandard(equipment, standard);
						});
					}
				}
			}
		} else {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("createEquipmentTask：equipment not in use. equipmentId = " + cmd.getEquipmentId());
			}

			return;
		}
	}

	@Override
	public void creatTaskByStandard(EquipmentInspectionEquipments equipment, EquipmentInspectionStandards standard) {
//		equipmentProvider.populateStandardGroups(standard);
//		EquipmentStandardsDTO standardDto = converStandardToDto(standard);
//		EquipmentInspectionTasks task = new EquipmentInspectionTasks();
//		task.setOwnerType(equipment.getOwnerType());
//		task.setOwnerId(equipment.getOwnerId());
//		task.setNamespaceId(equipment.getNamespaceId());
//		task.setTargetId(equipment.getTargetId());
//		task.setTargetType(equipment.getTargetType());
//		task.setInspectionCategoryId(equipment.getInspectionCategoryId());
//		task.setStandardId(standardDto.getId());
//		task.setEquipmentId(equipment.getId());
//		task.setTaskNumber(standardDto.getStandardNumber());
//		task.setStandardType(standardDto.getStandardType());
////		task.setExecutiveGroupType(equipment.getTargetType());
////		task.setExecutiveGroupId(equipment.getTargetId());
//		task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
//		task.setResult(EquipmentTaskResult.NONE.getCode());
//		task.setReviewResult(ReviewResult.NONE.getCode());
//
//		StandardType type = StandardType.fromStatus(standardDto.getStandardType());
//
//		List<TimeRangeDTO> timeRanges = repeatService.analyzeTimeRange(standardDto.getRepeat().getTimeRanges());
////		for(StandardGroupDTO executiveGroup : standardDto.getExecutiveGroup()) {
////
////			task.setExecutiveGroupId(executiveGroup.getGroupId());
////			task.setPositionId(executiveGroup.getPositionId());
//
//		if(timeRanges != null && timeRanges.size() > 0) {
//			if (LOGGER.isInfoEnabled()) {
//				LOGGER.info("creatTaskByStandard, timeRanges = " + timeRanges);
//			}
//			long current = System.currentTimeMillis();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//			String day = sdf.format(current);
//			int i = 0;
//			for (TimeRangeDTO timeRange : timeRanges) {
//				i++;
//
//				String duration = timeRange.getDuration();
//				String start = timeRange.getStartTime();
//				String str = day + " " + start;
//				Timestamp startTime = strToTimestamp(str);
//				Timestamp expiredTime = repeatService.getEndTimeByAnalyzeDuration(startTime, duration);
//				task.setExecutiveStartTime(startTime);
//				task.setExecutiveExpireTime(expiredTime);
//				long now = System.currentTimeMillis();
//				//标准名称+巡检/保养+当日日期6位(年的后两位+月份+天)+两位序号（系统从01开始生成）
//				//改为：设备名称+巡检/保养+当日日期6位(年的后两位+月份+天)+两位序号（系统从01开始生成）by xiongying20170927
//				if (i < 10) {
//
//					String taskName = equipment.getName() + type.getName() +
//							timestampToStr(new Timestamp(now)).substring(2) + "0" + i;
//					task.setTaskName(taskName);
//				} else {
//					String taskName = equipment.getName() + type.getName() +
//							timestampToStr(new Timestamp(now)).substring(2) + i;
//					task.setTaskName(taskName);
//				}
//				equipmentProvider.creatEquipmentTask(task);
//				equipmentTasksSearcher.feedDoc(task);
//
////				启动提醒
//				ListPmNotifyParamsCommand command = new ListPmNotifyParamsCommand();
//				command.setCommunityId(task.getTargetId());
//				command.setNamespaceId(task.getNamespaceId());
//				List<PmNotifyParamDTO> paramDTOs = listPmNotifyParams(command);
//				if(paramDTOs != null && paramDTOs.size() > 0) {
//					for (PmNotifyParamDTO notifyParamDTO : paramDTOs) {
//						List<PmNotifyReceiverDTO> receivers = notifyParamDTO.getReceivers();
//						if(receivers != null && receivers.size() > 0) {
//							PmNotifyRecord record = ConvertHelper.convert(notifyParamDTO, PmNotifyRecord.class);
//							PmNotifyReceiverList receiverList = new PmNotifyReceiverList();
//							List<PmNotifyReceiver> pmNotifyReceivers = new ArrayList<>();
//							receivers.forEach(receiver -> {
//								PmNotifyReceiver pmNotifyReceiver = new PmNotifyReceiver();
//								if(receiver != null) {
//									pmNotifyReceiver.setReceiverType(receiver.getReceiverType());
//									if(receiver.getReceivers() != null) {
//										List<Long> ids = receiver.getReceivers().stream().map(receiverName -> {
//											return receiverName.getId();
//										}).collect(Collectors.toList());
//										pmNotifyReceiver.setReceiverIds(ids);
//									}
//									pmNotifyReceivers.add(pmNotifyReceiver);
//								}
//							});
//							receiverList.setReceivers(pmNotifyReceivers);
//							record.setReceiverJson(receiverList.toString());
//							record.setOwnerType(EntityType.EQUIPMENT_TASK.getCode());
//							record.setOwnerId(task.getId());
//
//							//notify_time
//							PmNotifyType notify = PmNotifyType.fromCode(record.getNotifyType());
//							switch (notify) {
//								case BEFORE_START:
//									Timestamp starttime = minusMinutes(task.getExecutiveStartTime(), notifyParamDTO.getNotifyTickMinutes());
//									record.setNotifyTime(starttime);
//									break;
//								case BEFORE_DELAY:
//									Timestamp delaytime = minusMinutes(task.getExecutiveExpireTime(), notifyParamDTO.getNotifyTickMinutes());
//									record.setNotifyTime(delaytime);
//									break;
//								case AFTER_DELAY:
//									record.setNotifyTime(task.getExecutiveExpireTime());
//									break;
//								default:
//									break;
//							}
//							pmNotifyService.pushPmNotifyRecord(record);
//						}
//
//					}
//				}
//			}
//		}
	}

	private Timestamp minusMinutes(Timestamp startTime, int minus) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		calendar.add(Calendar.MINUTE, -minus);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());
		return time;
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

	private EquipmentInspectionTasks verifyEquipmentTask(Long taskId, String ownerType, Long ownerId) {
//		EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(taskId, ownerType, ownerId);
		// 总公司分公司 add by xiongying 20170328
		EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(taskId);

		if (task == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_NOT_EXIST,
					"任务不存在");
		}

		return task;
	}

	//	@Scheduled(cron = "0 0 7 * * ? ")
	@Override
	public void sendTaskMsg(Long startTime, Long endTime, Byte groupType) {
//			long current = System.currentTimeMillis();//当前时间毫秒数
//			long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数

//			if (LOGGER.isInfoEnabled()) {
//				LOGGER.info("sendTaskMsg, zero = " + current);
//			}

		List<EquipmentInspectionTasks> tasks = equipmentProvider.listTodayEquipmentInspectionTasks(startTime, endTime, groupType);
//		CronDateUtils.getCron(tasks.get(0).getExecutiveStartTime());

		if (tasks != null && tasks.size() > 0) {
			for (EquipmentInspectionTasks task : tasks) {
				Boolean notifyFlag = configurationProvider.getBooleanValue(task.getNamespaceId(), ConfigConstants.EQUIPMENT_TASK_NOTIFY_FLAG, false);
				if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(groupType))) {
					notifyFlag = configurationProvider.getBooleanValue(task.getNamespaceId(), ConfigConstants.EQUIPMENT_TASK_NOTIFY_DALAY, false);
					//只有过期未执行的要通知
					if (!EquipmentTaskStatus.WAITING_FOR_EXECUTING.equals(task.getStatus())) {
						notifyFlag = false;
					}
				}

				//五分钟后启动通知
				if (notifyFlag) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("deadline", timeToStr(task.getExecutiveExpireTime()));
					int code = EquipmentNotificationTemplateCode.GENERATE_EQUIPMENT_TASK_NOTIFY;
					if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(groupType))) {
						code = EquipmentNotificationTemplateCode.EQUIPMENT_TASK_DELAY;
						map.put("taskName", task.getTaskName());
					}

					String scope = EquipmentNotificationTemplateCode.SCOPE;

					String locale = "zh_CN";
					String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
					List<EquipmentInspectionStandardGroupMap> maps = equipmentProvider.listEquipmentInspectionStandardGroupMapByStandardIdAndGroupType(task.getStandardId(), groupType);

					for (EquipmentInspectionStandardGroupMap executiveGroup : maps) {
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
								LOGGER.info("sendTaskMsg, groupType = {}" + groupType + ", group = {}" + executiveGroup + ", members = {}" + members);
							}

							if (members != null) {
								for (OrganizationMember member : members) {
									sendMessageToUser(member.getTargetId(), notifyTextForApplicant);
								}
							}
						}
					}
					//审阅的要通知超管和模块管理员
					if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(groupType))) {
						ListServiceModuleAdministratorsCommand command = new ListServiceModuleAdministratorsCommand();
						command.setOrganizationId(task.getOwnerId());
						command.setOwnerId(task.getOwnerId());
						command.setOwnerType(EntityType.ORGANIZATIONS.getCode());
						List<OrganizationContactDTO> admins = rolePrivilegeService.listOrganizationSuperAdministrators(command);
						if (admins != null && admins.size() > 0) {
							for (OrganizationContactDTO admin : admins) {
								sendMessageToUser(admin.getTargetId(), notifyTextForApplicant);
							}
						}

						ListServiceModuleAdministratorsCommand moduleCommand = new ListServiceModuleAdministratorsCommand();
						moduleCommand.setOrganizationId(task.getOwnerId());
						moduleCommand.setOwnerId(task.getOwnerId());
						moduleCommand.setOwnerType(EntityType.ORGANIZATIONS.getCode());
						moduleCommand.setModuleId(20800L);
						List<ServiceModuleAuthorizationsDTO> moduleAdmins = rolePrivilegeService.listServiceModuleAdministrators(moduleCommand);
						if (moduleAdmins != null && moduleAdmins.size() > 0) {
							for (ServiceModuleAuthorizationsDTO moduleAdmin : moduleAdmins) {
								sendMessageToUser(moduleAdmin.getTargetId(), notifyTextForApplicant);
							}
						}
					}
				}
			}

		}
	}

	@Override
	public VerifyEquipmentLocationResponse verifyEquipmentLocation(VerifyEquipmentLocationCommand cmd) {

//		WebTokenGenerator webToken = WebTokenGenerator.getInstance();
//		EquipmentQrCodeTokenDTO qrCodeToken = webToken.fromWebToken(cmd.getQrCodeToken(), EquipmentQrCodeTokenDTO.class);
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentByQrCodeToken(cmd.getQrCodeToken());

		if (equipment == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_EXIST,
					"设备不存在");
		}

		if (!equipment.getId().equals(cmd.getEquipmentId())) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_QRCODE,
					"二维码和任务设备不对应");
		}


		if (equipment.getLongitude() == null || equipment.getLatitude() == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_SET_LOCATION,
					"设备没有设置经纬度");
		}

		double distance = (double) configProvider.getIntValue("equipment.verify.distance", 100);

		if (caculateDistance(cmd.getLongitude(), cmd.getLatitude(),
				equipment.getLongitude(), equipment.getLatitude()) < distance) {
			return null;
		} else {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_USER_NOT_IN_AREA,
					"不在设备附近");
		}
	}

	private EquipmentInspectionEquipments verifyEquipment(Long equipmentId, String ownerType, Long ownerId) {

		//改用namespaceId by xiongying20170328
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(equipmentId);

		if (equipment == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_EXIST,
					"设备不存在");
		}

		return equipment;
	}

	/*** return 两个坐标之间的距离 单位 米 */
	private double caculateDistance(double longitude1, double latitude1,
									double longitude2, double latitude2) {
		double radianLat1 = angle2Radian(latitude1);
		double radianLat2 = angle2Radian(latitude2);
		double differenceLat = radianLat1 - radianLat2;
		double differenceLng = angle2Radian(longitude1)
				- angle2Radian(longitude2);

		double s = 2 * Math.asin(Math.sqrt(Math.pow(
				Math.sin(differenceLat / 2), 2)
				+ Math.cos(radianLat1)
				* Math.cos(radianLat2)
				* Math.pow(Math.sin(differenceLng / 2), 2)));
		s = s * 6378137.0D;
		s = Math.round(s * 10000) / 10000;

		return s;
	}

	private double angle2Radian(double angle) {
		return angle * Math.PI / 180.0;
	}

	@Override
	public HttpServletResponse exportEquipmentTasks(
			SearchEquipmentTasksCommand cmd, HttpServletResponse response) {
		Integer pageSize = Integer.MAX_VALUE;
		cmd.setPageSize(pageSize);
		cmd.setTargetIds(transferCommandForExport(cmd.getTargetIdString()));

		ListEquipmentTasksResponse tasks = equipmentTasksSearcher.query(cmd);
		List<EquipmentTaskDTO> dtos = tasks.getTasks();

		URL rootPath = EquipmentServiceImpl.class.getResource("/");
		String filePath = rootPath.getPath() + this.downloadDir;
		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();
		filePath = filePath + "EquipmentTasks" + System.currentTimeMillis() + ".xlsx";
		//新建了一个文件
		this.createEquipmentTasksBook(filePath, dtos);

		return download(filePath, response);
	}

	public void createEquipmentTasksBook(String path, List<EquipmentTaskDTO> dtos) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("equipmentTasks");

		this.createEquipmentTasksBookSheetHead(sheet);
		for (EquipmentTaskDTO dto : dtos) {
			this.setNewEquipmentTasksBookRow(sheet, dto);
		}

		try {
			FileOutputStream out = new FileOutputStream(path);

			wb.write(out);
			wb.close();
			out.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}

	}

	private void createEquipmentTasksBookSheetHead(Sheet sheet) {

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i = -1;
		row.createCell(++i).setCellValue("任务名称");
		row.createCell(++i).setCellValue("类型");
		//row.createCell(++i).setCellValue("巡检设备");
		row.createCell(++i).setCellValue("开始时间");
		row.createCell(++i).setCellValue("截止时间");
		row.createCell(++i).setCellValue("设备位置");
		row.createCell(++i).setCellValue("任务状态");
		row.createCell(++i).setCellValue("完成时间");
		row.createCell(++i).setCellValue("执行人");
	}

	private void setNewEquipmentTasksBookRow(Sheet sheet, EquipmentTaskDTO dto) {
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		int i = -1;
		row.createCell(++i).setCellValue(dto.getTaskName());
		if (null != dto.getTaskType() && null != StandardType.fromStatus(dto.getTaskType()))
			row.createCell(++i).setCellValue(StandardType.fromStatus(dto.getTaskType()).getName());
		if (null != dto.getExecutiveStartTime())
			row.createCell(++i).setCellValue(dto.getExecutiveStartTime().toLocalDateTime().format(dateSF));
		if (dto.getProcessExpireTime() != null) {
			row.createCell(++i).setCellValue(dto.getProcessExpireTime().toLocalDateTime().format(dateSF));
		} else {
			if (null != dto.getExecutiveExpireTime())
				row.createCell(++i).setCellValue(dto.getExecutiveExpireTime().toLocalDateTime().format(dateSF));
		}

		row.createCell(++i).setCellValue(dto.getEquipmentLocation());

		if (null != dto.getStatus() && null != EquipmentTaskStatus.fromStatus(dto.getStatus()))
			row.createCell(++i).setCellValue(EquipmentTaskStatus.fromStatus(dto.getStatus()).getName());

		if (dto.getProcessTime() != null) {
			row.createCell(++i).setCellValue(dto.getProcessTime().toLocalDateTime().format(dateSF));
			row.createCell(++i).setCellValue(dto.getOperatorName());
		} else {
			if (null != dto.getExecutiveTime())
				row.createCell(++i).setCellValue(dto.getExecutiveTime().toString());
			row.createCell(++i).setCellValue(dto.getExecutorName());
		}


	}

	@Override
	public ListLogsByTaskIdResponse listLogsByTaskId(ListLogsByTaskIdCommand cmd) {
		List<EquipmentInspectionTasks> tasks = new ArrayList<>();
		if (cmd.getTaskId() != null && cmd.getTaskId().size() > 0) {
			cmd.getTaskId().forEach((t) -> tasks.add(verifyEquipmentTask(t, cmd.getOwnerType(), cmd.getOwnerId())));
		}

		CrossShardListingLocator locator = new CrossShardListingLocator();
		int pageSize = Integer.MAX_VALUE - 1;
		//0: none, 1: complete, 2: complete maintenance, 3: review,
		List<EquipmentTaskLogs> logsList = new ArrayList<>();
		ListLogsByTaskIdResponse response = new ListLogsByTaskIdResponse();

		tasks.forEach((task -> {
//			EquipmentInspectionPlans plan = new EquipmentInspectionPlans();
//			List<Long> equipmentIds = new ArrayList<>();
//			if (cmd.getEquipmentId() == null) {
//				if (task.getPlanId() != null && task.getPlanId() != 0L) {
//					plan = equipmentProvider.getEquipmmentInspectionPlanById(task.getPlanId());
//					if (null != plan) {
//						List<EquipmentInspectionEquipmentPlanMap> planMaps = equipmentProvider.getEquipmentInspectionPlanMap(plan.getId());
//						if (planMaps != null && planMaps.size() > 0) {
//							planMaps.forEach((r) -> equipmentIds.add(r.getEquipmentId()));
//						}
//					}
//				} else if (task.getPlanId() == null || task.getPlanId() == 0) {
//					//兼容
//					equipmentIds.add(task.getEquipmentId());
//				}
//			} else {
//				equipmentIds.addAll(new ArrayList<>(Collections.singletonList(cmd.getEquipmentId())));
//			}
			List<EquipmentInspectionTasksLogs> logs = equipmentProvider.listLogsByTaskId(locator, pageSize + 1,
					task.getId(), cmd.getProcessType(), null);
			//展示最新一次的任务日志记录
			logs = getLatestTaskLogs(logs);

			List<EquipmentTaskLogsDTO> dtos = new ArrayList<>();
			//为了查看特定设备详情和批量审阅的总览  增加以下
			if (cmd.getEquipmentId() != null) {
				dtos = processEquipmentTaskLogsDTOS(logs);
			} else {
				if (logs != null && logs.size() > 0)
//					dtos = logs.stream().map((r) -> {
//						EquipmentTaskLogsDTO dto = ConvertHelper.convert(r, EquipmentTaskLogsDTO.class);
//						populateItemResultToTasklog(r, dto);
//						return dto;
//					}).collect(Collectors.toList());
					dtos = processEquipmentTaskLogsDTOS(logs);
				//增加正常异常数
				calculateAbnormalCount(dtos);
			}
			EquipmentTaskLogs taskLogs = new EquipmentTaskLogs();
			taskLogs.setExecuteEndTime(task.getExecutiveExpireTime());
			taskLogs.setExecuteStartTime(task.getExecutiveStartTime());
			taskLogs.setTaskNumber(task.getTaskNumber());
			taskLogs.setTaskName(task.getTaskName());
			taskLogs.setLogs(dtos);
			logsList.add(taskLogs);
		}));

		response.setTaskLogs(logsList);
		return response;
	}

	private List<EquipmentInspectionTasksLogs> getLatestTaskLogs(List<EquipmentInspectionTasksLogs> logs) {
		Map<String, EquipmentInspectionTasksLogs> logsMap = new ConcurrentHashMap<>();
		if (logs != null && logs.size() > 0) {
			logs.forEach((log) -> logsMap.putIfAbsent(log.getEquipmentId().toString() + log.getStandardId().toString(), log));
			return new ArrayList<>(logsMap.values());
		}
		return null;
	}

	private void calculateAbnormalCount(List<EquipmentTaskLogsDTO> dtos) {
		if (dtos != null && dtos.size() > 0) {
			for (EquipmentTaskLogsDTO dto : dtos) {
				Integer abnormalCount = 0;
				Integer normalCount = 0;
				for (InspectionItemResult result : dto.getItemResults()) {
					if (result.getNormalFlag() == 0) {
						abnormalCount++;
					}
					if (result.getNormalFlag() == 1) {
						normalCount++;
					}
				}
				dto.setAbnormalCount(abnormalCount);
				dto.setNormalCount(normalCount);
			}
		}
	}

	private List<EquipmentTaskLogsDTO> processEquipmentTaskLogsDTOS(List<EquipmentInspectionTasksLogs> logs) {
		if (logs != null && logs.size() > 0) {
			return logs.stream().map((r) -> {

				EquipmentTaskLogsDTO dto = ConvertHelper.convert(r, EquipmentTaskLogsDTO.class);

				populateItemResultToTasklog(r, dto);

				//总公司 分公司 by xiongying20170328
				if (r.getOperatorId() != null && r.getOperatorId() != 0) {
					List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(r.getOperatorId());
					if (operators != null && operators.size() > 0) {
						dto.setOperatorName(operators.get(0).getContactName());
					}
				}

				if (r.getTargetId() != null && r.getTargetId() != 0) {
					List<OrganizationMember> targets = organizationProvider.listOrganizationMembersByUId(r.getTargetId());
					if (targets != null && targets.size() > 0) {
						dto.setTargetName(targets.get(0).getContactName());
					}
				}

				List<EquipmentInspectionTasksAttachments> attachmentLists = equipmentProvider.listTaskAttachmentsByLogId(dto.getId());
				if (attachmentLists != null && attachmentLists.size() > 0) {
					populateLogAttachements(r, attachmentLists);
					List<EquipmentTaskAttachmentDTO> attachments = new ArrayList<EquipmentTaskAttachmentDTO>();
					for (EquipmentInspectionTasksAttachments attachment : attachmentLists) {
						EquipmentTaskAttachmentDTO attDto = ConvertHelper.convert(attachment, EquipmentTaskAttachmentDTO.class);
						attachments.add(attDto);
					}
					dto.setAttachments(attachments);
				}
				//这里改成任务完成状态需要拿到当前最近的一条任务审批记录  （之前是完成  需维修  维修完成）
				if (EquipmentTaskProcessType.COMPLETE.equals(EquipmentTaskProcessType.fromStatus(dto.getProcessType()))) {
					EquipmentInspectionTasksLogs reviewLog = equipmentProvider.getNearestReviewLogAfterProcess(dto.getTaskId(), dto.getId());
					if (null == reviewLog) {
						dto.setReviewResult(ReviewResult.NONE.getCode());
					}
					if (reviewLog != null) {
						if (EquipmentTaskProcessResult.REVIEW_QUALIFIED.equals(EquipmentTaskProcessResult.fromStatus(reviewLog.getProcessResult()))) {
							dto.setReviewResult(ReviewResult.QUALIFIED.getCode());
						} else if (EquipmentTaskProcessResult.REVIEW_UNQUALIFIED.equals(EquipmentTaskProcessResult.fromStatus(reviewLog.getProcessResult()))) {
							dto.setReviewResult(ReviewResult.UNQUALIFIED.getCode());
						}
					}
				}
				return dto;
			}).collect(Collectors.toList());
		}
		return null;

	}

	private void populateItemResultToTasklog(EquipmentInspectionTasksLogs log, EquipmentTaskLogsDTO dto) {
		List<EquipmentInspectionItemResults> itemResults = equipmentProvider.findEquipmentInspectionItemResultsByLogId(dto.getId());

		List<InspectionItemResult> results = new ArrayList<>();
		EquipmentInspectionEquipments equipment = new EquipmentInspectionEquipments();
		if (itemResults != null && itemResults.size() > 0) {
			results = itemResults.stream()
					.map(result -> {
						InspectionItemResult itemResult = ConvertHelper.convert(result, InspectionItemResult.class);
						EquipmentInspectionItems itemDetail = equipmentProvider.findEquipmentInspectionItem(itemResult.getItemId());
						if (itemDetail != null) {
							itemResult.setValueJason(itemDetail.getValueJason());
						}
						return itemResult;
					}).collect(Collectors.toList());

		}
		if (log.getEquipmentId() != null && log.getEquipmentId() != 0) {
			equipment = equipmentProvider.findEquipmentById(log.getEquipmentId());
			if (equipment != null) {
				dto.setEquipmentName(equipment.getName());
				dto.setLocation(equipment.getLocation());
				dto.setEquipmentId(equipment.getId());
			}
		}
		dto.setItemResults(results);
	}

//	private void populateTaskType(EquipmentInspectionTasks task, ListLogsByTaskIdResponse response) {
//		//兼容之前的通过标准来拿任务类型
//		if (task.getPlanId() != null && task.getPlanId() != 0) {
//			EquipmentInspectionPlans plan = equipmentProvider.getEquipmmentInspectionPlanById(task.getPlanId());
//			if (plan != null) {
//				response.setTaskType(plan.getPlanType());
//			}
//		} else {
//			EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
//			if (standard != null) {
//				response.setTaskType(standard.getStandardType());
//			}
//		}
//	}

	@Override
	public ImportDataResponse importEquipmentStandards(ImportOwnerCommand cmd, MultipartFile mfile,
													   Long userId) {
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_STANDARD_UPDATE, cmd.getTargetId());
		ImportDataResponse importDataResponse = importData(cmd, mfile, userId, ImportDataType.EQUIPMENT_STANDARDS.getCode());
		return importDataResponse;
	}

	@Override
	public ImportDataResponse importEquipments(ImportOwnerCommand cmd, MultipartFile mfile, Long userId) {
		/*Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_UPDATE, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);*/
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_UPDATE, cmd.getTargetId());
		ImportDataResponse response = new ImportDataResponse();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

			if (null == resultList || resultList.isEmpty()) {
				LOGGER.error("File content is empty...userId=" + userId);
				throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
						localeStringService.getLocalizedString(String.valueOf(UserServiceErrorCode.SCOPE),
								String.valueOf(UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL),
								UserContext.current().getUser().getLocale(), "File content is empty"));
			}
			List<EquipmentInspectionEquipments> equipments = handleImportEquipmentsData(cmd, resultList);
			List<String> erroLogs = importEquipmentsData(equipments, cmd);

			response.setTotalCount((long) resultList.size() - 1);
			response.setFailCount((long) erroLogs.size());
			response.setLogs(erroLogs);
		} catch (IOException e) {
			LOGGER.error("File can not be resolved...");
			e.printStackTrace();
		}

		return response;

	}

	private List<String> importEquipmentsData(List<EquipmentInspectionEquipments> datas, ImportOwnerCommand cmd) {
		List<String> erroLogs = new ArrayList<String>();
		Integer namespaceId = cmd.getNamespaceId();
		Long userId = UserContext.currentUserId();
		for (EquipmentInspectionEquipments equipment : datas) {

			equipment.setNamespaceId(namespaceId);
			equipment.setOwnerType(cmd.getOwnerType());
			equipment.setOwnerId(cmd.getOwnerId());
			equipment.setTargetType(cmd.getTargetType());
			equipment.setTargetId(cmd.getTargetId());
			equipment.setInspectionCategoryId(cmd.getInspectionCategoryId());
			//物业巡检V3.0.1 动态excel中设置好设备状态 删除
			//equipment.setStatus(EquipmentStatus.INCOMPLETE.getCode());
			String tokenString = UUID.randomUUID().toString();
			equipment.setQrCodeToken(tokenString);
			equipment.setCreatorUid(userId);
			equipment.setOperatorUid(userId);
			LOGGER.info("add equipment");
			equipmentProvider.creatEquipmentInspectionEquipment(equipment);
			equipmentSearcher.feedDoc(equipment);
		}

		return erroLogs;
	}

	private List<EquipmentInspectionEquipments> handleImportEquipmentsData(ImportOwnerCommand cmd, List resultList) {

		ListFieldCommand listFieldCommand = ConvertHelper.convert(cmd, ListFieldCommand.class);
		// field 和 name 对应关系DTO
		List<FieldDTO> fieldDTO = fieldService.listFields(listFieldCommand);

		try {
			return getImportEquipmentData(resultList, fieldDTO);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("importEquipmentErro", e);
		}
		return null;

	}

	private List<EquipmentInspectionEquipments> getImportEquipmentData(List resultList, List<FieldDTO> fieldsDTO) throws Exception {

		List<EquipmentInspectionEquipments> objList = new ArrayList<>();
		//获取导入Excel的的title
		/*RowResult titleNames = (RowResult)resultList.get(0);
		Map<String,String> tileMap = titleNames.getCells();
		List<String> tileNameList = (List<String>) tileMap.values();*/
		//记录excel中的field顺序
		List<String> fieldOrders = new ArrayList<>();
		for (FieldDTO field : fieldsDTO) {
			//去除附件 和 经纬度
			//if (!(field.getFieldName().equals("attachments") && field.getFieldName().equals("geohash"))){
			fieldOrders.add(field.getFieldName());
			//}
		}
		int flag = 0;
		//因为有注意事项  一行 从第二行开始
		for (int i = 2; i < resultList.size(); i++) {

			EquipmentInspectionEquipments eq = new EquipmentInspectionEquipments();

			RowResult r = (RowResult) resultList.get(i);

			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getA()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getB()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getC()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getD()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getE()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getF()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getG()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getH()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getI()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getJ()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getK()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getL()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getM()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getN()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getO()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getP()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getQ()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getR()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getS()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getT()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getU()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getV()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getW()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getX()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getY()));
				flag++;
			}
			if (flag < fieldOrders.size()) {
				setToObj(fieldOrders.get(flag), eq, trim(r.getZ()));
				flag++;
			}
			flag = 0;
			objList.add(eq);
		}

		return objList;
	}

	private void setToObj(String fieldName, Object dto, Object value) throws NoSuchFieldException, IntrospectionException, InvocationTargetException, IllegalAccessException {
		Class<?> clz = dto.getClass().getSuperclass();
		Object val = value;
		if (Objects.equals(fieldName, "attachments") || Objects.equals(fieldName, "geohash")) {
			return;
		}
		String type = clz.getDeclaredField(fieldName).getType().getSimpleName();
		LOGGER.info("export equipments setToObj type={}", type);
		if (StringUtils.isEmpty((String) value)) {
			val = null;
		} else {
			switch (type) {
				case "BigDecimal":
					val = new BigDecimal((String) value);
					break;
				case "Long":
					if (fieldName.equals("categoryId")) {
						val = EquipmentCategories.fromName((String) value).getCode();
						//此处兼容旧版本物业巡检 冗余设备类型名称
						PropertyDescriptor pd = new PropertyDescriptor("categoryPath", clz);
						Method writeMethod = pd.getWriteMethod();
						writeMethod.invoke(dto, value);
					} else {
						val = Long.parseLong((String) value);
					}
					break;
				case "Timestamp":
					if (((String) value).length() < 1) {
						val = null;
						break;
					}
					java.util.Date date = new java.util.Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						date = sdf.parse((String) value);
					} catch (ParseException e) {
						val = null;
						break;
					}

					val = new Timestamp(date.getTime());
					break;
				case "Integer":
					val = Integer.parseInt((String) value);
					break;
				case "Byte":
					//转换成byte类型入库
					if (fieldName.equals("status")) {
						val = EquipmentStatus.fromName((String) value).getCode();
					}
					if (fieldName.equals("qrCodeFlag")) {
						if ("启用".equals((String) value)) {
							val = Byte.parseByte("1");
						} else if ("停用".equals((String) value)) {
							val = Byte.parseByte("0");
						}
					}
					//val = Byte.parseByte((String)value);
					break;
				case "String":
					if (((String) val).trim().length() < 1) {
						val = null;
						break;
					}
			}
		}
		PropertyDescriptor pd = new PropertyDescriptor(fieldName, clz);
		Method writeMethod = pd.getWriteMethod();
		writeMethod.invoke(dto, val);

	}

	private String trim(String string) {
		if (string != null) {
			return string.trim();
		}
		return "";
	}

	@Override
	public ImportDataResponse importEquipmentAccessories(ImportOwnerCommand cmd, MultipartFile mfile,
														 Long userId) {
		ImportDataResponse importDataResponse = importData(cmd, mfile, userId, ImportDataType.EQUIPMENT_ACCESSORIES.getCode());
		return importDataResponse;
	}

	private ImportDataResponse importData(ImportOwnerCommand cmd, MultipartFile mfile,
										  Long userId, String dataType) {
		ImportDataResponse importDataResponse = new ImportDataResponse();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

			if (null == resultList || resultList.isEmpty()) {
				LOGGER.error("File content is empty。userId=" + userId);
				throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
						"File content is empty");
			}
			LOGGER.debug("Start import data...,total:" + resultList.size());

			List<String> errorDataLogs = null;
			//导入数据，返回导入错误的日志数据集
			if (StringUtils.equals(dataType, ImportDataType.EQUIPMENT_STANDARDS.getCode())) {
				errorDataLogs = importEquipmentStandardsData(cmd, convertToStrList(resultList, 6), userId);
			}

//			if(StringUtils.equals(dataType, ImportDataType.EQUIPMENTS.getCode())) {
//				errorDataLogs = importEquipmentsData(cmd, convertEquipmentToStrList(resultList), userId);
//			}

			if (StringUtils.equals(dataType, ImportDataType.EQUIPMENT_ACCESSORIES.getCode())) {
				errorDataLogs = importEquipmentAccessoriesData(cmd, convertToStrList(resultList, 5), userId);
			}

			LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
			if (null == errorDataLogs || errorDataLogs.isEmpty()) {
				LOGGER.debug("Data import all success...");
			} else {
				//记录导入错误日志
				for (String log : errorDataLogs) {
					LOGGER.error(log);
				}
			}

			importDataResponse.setTotalCount((long) resultList.size() - 1);
			importDataResponse.setFailCount((long) errorDataLogs.size());
			importDataResponse.setLogs(errorDataLogs);
		} catch (IOException e) {
			LOGGER.error("File can not be resolved...");
			e.printStackTrace();
		}
		return importDataResponse;
	}

	private List<String> importEquipmentStandardsData(ImportOwnerCommand cmd, List<String> list, Long userId) {
		List<String> errorDataLogs = new ArrayList<String>();

		Integer namespaceId = cmd.getNamespaceId();

		for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
				EquipmentInspectionStandards standard = new EquipmentInspectionStandards();
				if (StringUtils.isNotBlank(s[0]) && !"null".equals(s[0])) {
					standard.setStandardNumber(s[0]);
				} else {
					standard.setStandardNumber("");
				}

				if (StringUtils.isNotBlank(s[1]) && !"null".equals(s[1])) {
					standard.setName(s[1]);
				} else {
					standard.setName("");
				}

				standard.setStandardType(StandardType.fromName(s[2]).getCode());

				if (StringUtils.isNotBlank(s[3]) && !"null".equals(s[3])) {
					standard.setStandardSource(s[3]);
				} else {
					standard.setStandardSource("");
				}

				if (StringUtils.isNotBlank(s[4]) && !"null".equals(s[4])) {
					standard.setDescription(s[4]);
				} else {
					standard.setDescription("");
				}

				if (StringUtils.isNotBlank(s[5]) && !"null".equals(s[5])) {
					standard.setRemarks(s[5]);
				} else {
					standard.setRemarks("");
				}

				standard.setOwnerType(cmd.getOwnerType());
				standard.setOwnerId(cmd.getOwnerId());
				standard.setTargetId(cmd.getTargetId());
				standard.setTargetType(cmd.getTargetType());
				standard.setInspectionCategoryId(cmd.getInspectionCategoryId());
				standard.setStatus(EquipmentStandardStatus.NOT_COMPLETED.getCode());
				standard.setNamespaceId(namespaceId);
				standard.setCreatorUid(userId);
				standard.setOperatorUid(userId);
				LOGGER.info("add standard");
				equipmentProvider.creatEquipmentStandard(standard);
				equipmentStandardSearcher.feedDoc(standard);
				return null;
			});
		}
		return errorDataLogs;

	}

//	private List<String> importEquipmentsData(ImportOwnerCommand cmd, List<String> list, Long userId){
//		List<String> errorDataLogs = new ArrayList<String>();
//		Integer namespaceId = UserContext.getCurrentNamespaceId();
//		for (String str : list) {
//			String[] s = str.split("\\|\\|");
////			dbProvider.execute((TransactionStatus status) -> {
//				EquipmentInspectionEquipments equipment = new EquipmentInspectionEquipments();
//				equipment.setCustomNumber(s[1]);
//				equipment.setName(s[2]);
//				equipment.setEquipmentModel(s[3]);
//				equipment.setParameter(s[4]);
//				equipment.setManufacturer(s[5]);
//				if(!StringUtils.isBlank(s[6])) {
//					equipment.setInstallationTime(dateStrToTimestamp(s[6]));
//				}
//				if(!StringUtils.isBlank(s[7])) {
//					equipment.setRepairTime(dateStrToTimestamp(s[7]));
//				}
//				equipment.setLocation(s[8]);
//				equipment.setQuantity(Long.valueOf(s[9]));
//				if(!StringUtils.isEmpty(s[10]) && !"null".equals(s[10])) {
//					equipment.setRemarks(s[10]);
//				}
//				equipment.setNamespaceId(namespaceId);
//				equipment.setOwnerType(cmd.getOwnerType());
//				equipment.setOwnerId(cmd.getOwnerId());
//				equipment.setTargetType(cmd.getTargetType());
//				equipment.setTargetId(cmd.getTargetId());
//				equipment.setInspectionCategoryId(cmd.getInspectionCategoryId());
//				equipment.setStatus(EquipmentStatus.INCOMPLETE.getCode());
//				String tokenString = UUID.randomUUID().toString();
//				equipment.setQrCodeToken(tokenString);
//				equipment.setCreatorUid(userId);
//				equipment.setOperatorUid(userId);
//				LOGGER.info("add equipment");
//				equipmentProvider.creatEquipmentInspectionEquipment(equipment);
//				equipmentSearcher.feedDoc(equipment);
////				return null;
////			});
//		}
//		return errorDataLogs;
//
//	}

	private Timestamp dateStrToTimestamp(String str) {
		LocalDate localDate = LocalDate.parse(str, dateSF);
		Timestamp ts = new Timestamp(Date.valueOf(localDate).getTime());
		return ts;
	}

	private List<String> importEquipmentAccessoriesData(ImportOwnerCommand cmd, List<String> list, Long userId) {
		List<String> errorDataLogs = new ArrayList<String>();

		//Integer namespaceId = UserContext.getCurrentNamespaceId();
		Integer namespaceId = cmd.getNamespaceId();

		for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
				EquipmentInspectionAccessories accessory = new EquipmentInspectionAccessories();
				accessory.setName(s[0]);
				accessory.setManufacturer(s[1]);
				;
				accessory.setModelNumber(s[2]);
				;
				accessory.setSpecification(s[3]);
				;
				accessory.setLocation(s[4]);
				;
				accessory.setNamespaceId(namespaceId);
				accessory.setOwnerType(cmd.getOwnerType());
				accessory.setOwnerId(cmd.getOwnerId());
				accessory.setTargetType(cmd.getTargetType());
				accessory.setTargetId(cmd.getTargetId());
				accessory.setStatus((byte) 1);

				LOGGER.info("add equipment accessory");
				equipmentProvider.creatEquipmentInspectionAccessories(accessory);
				equipmentAccessoriesSearcher.feedDoc(accessory);
				return null;
			});
		}
		return errorDataLogs;

	}

	private List<String> convertToStrList(List list, int column) {
		List<String> result = new ArrayList<String>();
		boolean firstRow = true;
		for (Object o : list) {
			if (firstRow) {
				firstRow = false;
				continue;
			}
			RowResult r = (RowResult) o;
			StringBuffer sb = new StringBuffer();
			sb.append(r.getA()).append("||");
			sb.append(r.getB()).append("||");
			sb.append(r.getC()).append("||");
			sb.append(r.getD()).append("||");
			sb.append(r.getE()).append("||");
			if (column == 6)
				sb.append(r.getF()).append("||");
			if (column == 10) {
				sb.append(r.getG()).append("||");
				sb.append(r.getH()).append("||");
				sb.append(r.getI()).append("||");
				sb.append(r.getJ()).append("||");
			}


			result.add(sb.toString());
		}
		return result;
	}

	private List<String> convertEquipmentToStrList(List list) {
		List<String> result = new ArrayList<String>();
		int firstRow = 0;
		for (Object o : list) {
			if (firstRow < 4) {
				firstRow++;
				continue;
			}
			RowResult r = (RowResult) o;
			StringBuffer sb = new StringBuffer();
			sb.append(r.getA()).append("||");
			sb.append(r.getB()).append("||");
			sb.append(r.getC()).append("||");
			sb.append(r.getD()).append("||");
			sb.append(r.getE()).append("||");
			sb.append(r.getF()).append("||");
			sb.append(r.getG()).append("||");
			sb.append(r.getH()).append("||");
			sb.append(r.getI()).append("||");
			sb.append(r.getJ()).append("||");
			sb.append(r.getK()).append("||");


			result.add(sb.toString());
		}
		return result;
	}

	@Override
	public List<CategoryDTO> listEquipmentsCategories() {

		List<CategoryDTO> dtos = new ArrayList<CategoryDTO>();
		List<Category> categories = categoryProvider.listActionCategories(CategoryConstants.CATEGORY_ID_EQUIPMENT_TYPE);

		if (categories != null && categories.size() > 0) {
			dtos = categories.stream().map((r) -> {
				CategoryDTO dto = ConvertHelper.convert(r, CategoryDTO.class);
				return dto;
			}).collect(Collectors.toList());
		}
		return dtos;
	}

	private Timestamp addMonths(Timestamp now, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.MONTH, months);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());

		return time;
	}

	private Timestamp addWeek(Timestamp now, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, days);
		return new Timestamp(calendar.getTimeInMillis());
	}

	private ListEquipmentTasksResponse listDelayTasks(ListEquipmentTasksCommand cmd) {
		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
		int pageSize = cmd.getPageSize() == null ? Integer.MAX_VALUE - 1 : cmd.getPageSize();
		if (null == cmd.getPageAnchor()) {
			cmd.setPageAnchor(0L);
		}
		Integer offset = cmd.getPageAnchor().intValue();
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		//是否是管理员
		boolean isAdmin = false;
		List<RoleAssignment> resources = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOwnerId(), EntityType.USER.getCode(), user.getId());
		if (null != resources && 0 != resources.size()) {
			for (RoleAssignment resource : resources) {
				if (resource.getRoleId() == RoleConstants.ENTERPRISE_SUPER_ADMIN
						|| resource.getRoleId() == RoleConstants.ENTERPRISE_ORDINARY_ADMIN
						|| resource.getRoleId() == RoleConstants.PM_SUPER_ADMIN
						|| resource.getRoleId() == RoleConstants.PM_ORDINARY_ADMIN) {
					isAdmin = true;
					userId = 0L;
					break;
				}
			}
		}
		//只展示近一周
		//Timestamp startTime = addMonths(new Timestamp(System.currentTimeMillis()), -1);
		Timestamp startTime = addWeek(new Timestamp(System.currentTimeMillis()), -7);
		List<EquipmentInspectionTasks> tasks = null;
		if (isAdmin) {
			tasks = equipmentProvider.listDelayTasks(cmd.getInspectionCategoryId(), null, cmd.getTargetType(),
					cmd.getTargetId(), offset, pageSize, AdminFlag.YES.getCode(), startTime);
		}
		if (!isAdmin) {
			List<Long> planIds = new ArrayList<>();
			List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
			List<EquipmentInspectionPlanGroupMap> maps = equipmentProvider.listEquipmentInspectionPlanGroupMapByGroupAndPosition(groupDtos, null);
			if (maps != null && maps.size() > 0) {
				for (EquipmentInspectionPlanGroupMap r : maps) {
					planIds.add(r.getPlanId());
				}
			}
			tasks = equipmentProvider.listDelayTasks(cmd.getInspectionCategoryId(), planIds, cmd.getTargetType(),
					cmd.getTargetId(), offset, pageSize, AdminFlag.NO.getCode(), startTime);
		}

		if (tasks.size() > pageSize) {
			tasks.remove(tasks.size() - 1);
			response.setNextPageAnchor((long) (offset + 1));
		}

		Set<Long> taskEquipmentIds = tasks.stream().
				map(EhEquipmentInspectionTasks::getEquipmentId).
				filter(Objects::nonNull).collect(Collectors.toSet());


		Map<Long, EquipmentInspectionEquipments> equipmentsMap = equipmentProvider.listEquipmentsById(taskEquipmentIds);
		List<EquipmentTaskDTO> dtos = tasks.stream().map(r -> {
			EquipmentTaskDTO dto = ConvertHelper.convert(r, EquipmentTaskDTO.class);
			EquipmentInspectionEquipments equipment = equipmentsMap.get(dto.getEquipmentId());
			if (equipment != null) {
				dto.setEquipmentLocation(equipment.getLocation());
				dto.setQrCodeFlag(equipment.getQrCodeFlag());
				dto.setEquipmentName(equipment.getName());
			}
			return dto;
		}).filter(Objects::nonNull).collect(Collectors.toList());

		response.setTasks(dtos);
		return response;
	}

	@Override
	public ListEquipmentTasksResponse listEquipmentTasks(ListEquipmentTasksCommand cmd) {

		//delay  tasks can remove with offline  version
		if (cmd.getTaskStatus() != null && cmd.getTaskStatus().size() == 1
				&& EquipmentTaskStatus.DELAY.equals(EquipmentTaskStatus.fromStatus(cmd.getTaskStatus().get(0)))) {
			return listDelayTasks(cmd);
		}

		if (cmd.getTaskStatus() != null && cmd.getTaskStatus().size() == 1 && EquipmentTaskStatus.PERSONAL_DONE.equals(EquipmentTaskStatus.fromStatus(cmd.getTaskStatus().get(0)))) {
			return listPersonalDoneTasks(cmd);
		}
		Timestamp lastSyncTime = null;
		if (cmd.getLastSyncTime() != null) {
			lastSyncTime = dateStrToTimestamp(cmd.getLastSyncTime());
		}
		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
		int pageSize = cmd.getPageSize() == null ? Integer.MAX_VALUE - 1 : cmd.getPageSize();

		if (null == cmd.getPageAnchor()) {
			cmd.setPageAnchor(0L);
		}
		Long offset = cmd.getPageAnchor();

		List<String> targetTypes = new ArrayList<>();
		List<Long> targetIds = new ArrayList<>();
		if (cmd.getTargetType() != null)
			targetTypes.add(cmd.getTargetType());
		if (cmd.getTargetId() != null)
			targetIds.add(cmd.getTargetId());

		Long userId = UserContext.currentUserId();
		//是否是管理员
		boolean isAdmin = checkCurrentUserisAdmin(cmd.getOwnerId(), userId);

		List<EquipmentInspectionTasks> allTasks = null;
		if (isAdmin) {
			allTasks = getAdminEquipmentInspectionTasks(cmd, lastSyncTime, response, pageSize, offset, targetTypes, targetIds);
		}
		if (!isAdmin) {
			allTasks = getNoAdminEquipmentInspectionTasks(cmd, lastSyncTime, response, pageSize, offset, targetTypes, targetIds, userId);
		}

		if (allTasks.size() > pageSize) {
			allTasks.remove(allTasks.size() - 1);
			response.setNextPageAnchor(allTasks.get(allTasks.size() - 1).getId());
		}

		List<EquipmentTaskDTO> dtos = new ArrayList<>();
		if (allTasks.size() > 0) {
			dtos = allTasks.stream().map((r) ->
					ConvertHelper.convert(r, EquipmentTaskDTO.class))
					.collect(Collectors.toList());
		}
		response.setTasks(dtos);

		return response;

	}

	private ListEquipmentTasksResponse listPersonalDoneTasks(ListEquipmentTasksCommand cmd) {

		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
		int pageSize = cmd.getPageSize() == null ? Integer.MAX_VALUE - 1 : cmd.getPageSize();
		if (null == cmd.getPageAnchor()) {
			cmd.setPageAnchor(0L);
		}
		Timestamp startTime = addMonths(new Timestamp(System.currentTimeMillis()), -6);
		Integer offset = cmd.getPageAnchor().intValue();
		List<EquipmentInspectionTasks> tasks = equipmentProvider.listPersonalDoneTasks(cmd.getTargetId(), cmd.getInspectionCategoryId(), pageSize + 1, offset, startTime);
		List<EquipmentTaskDTO> dtos = new ArrayList<>();
		if (tasks != null && tasks.size() > 0) {
			dtos = tasks.stream().map((r) -> {
				EquipmentTaskDTO dto = ConvertHelper.convert(r, EquipmentTaskDTO.class);
				dto.setStatus(EquipmentTaskStatus.PERSONAL_DONE.getCode());
				return dto;
			}).collect(Collectors.toList());
			response.setTasks(dtos);
		}
		if (tasks != null && tasks.size() > pageSize) {
			response.setNextPageAnchor(cmd.getPageAnchor() + 1);
		} else {
			response.setNextPageAnchor(null);
		}

		return response;
	}

	private List<EquipmentInspectionTasks> getNoAdminEquipmentInspectionTasks(ListEquipmentTasksCommand cmd, Timestamp lastSyncTime, ListEquipmentTasksResponse response, int pageSize, Long offset, List<String> targetTypes, List<Long> targetIds, Long userId) {
		List<EquipmentInspectionTasks> allTasks;
		List<Long> executePlanIds = new ArrayList<>();
		List<Long> reviewPlanIds = new ArrayList<>();
		List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
		//get execute and review members from plans  20180129
		List<EquipmentInspectionPlanGroupMap> maps = equipmentProvider.listEquipmentInspectionPlanGroupMapByGroupAndPosition(groupDtos, null);
		if (maps != null && maps.size() > 0) {
			for (EquipmentInspectionPlanGroupMap r : maps) {
				if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(r.getGroupType()))) {
					reviewPlanIds.add(r.getPlanId());
				}
				if (QualityGroupType.EXECUTIVE_GROUP.equals(QualityGroupType.fromStatus(r.getGroupType()))) {
					executePlanIds.add(r.getPlanId());
				}
			}
		}

		String cacheKey = convertListEquipmentInspectionTasksCache(cmd.getTaskStatus(), cmd.getInspectionCategoryId(), targetTypes, targetIds,
				executePlanIds, reviewPlanIds, offset, pageSize, lastSyncTime, userId);
		LOGGER.info("listEquipmentInspectionTasks is not Admin  cacheKey = {}", cacheKey);
		allTasks = equipmentProvider.listEquipmentInspectionTasksUseCache(cmd.getTaskStatus(), cmd.getInspectionCategoryId(),
				targetTypes, targetIds, executePlanIds, reviewPlanIds, offset, pageSize + 1, cacheKey, AdminFlag.NO.getCode(), lastSyncTime);
		if (cmd.getTaskStatus().size() > 1) {
			populateTaskStatusCount(cmd, executePlanIds, reviewPlanIds, AdminFlag.NO.getCode(), response, targetTypes, targetIds);
		} else {
			populateReviewTaskStatusCount(cmd, executePlanIds, reviewPlanIds, AdminFlag.NO.getCode(), response, targetTypes, targetIds);
		}
		return allTasks;
	}

	private List<EquipmentInspectionTasks> getAdminEquipmentInspectionTasks(ListEquipmentTasksCommand cmd, Timestamp lastSyncTime, ListEquipmentTasksResponse response, int pageSize, Long offset, List<String> targetTypes, List<Long> targetIds) {
		List<EquipmentInspectionTasks> allTasks;
		String cacheKey = convertListEquipmentInspectionTasksCache(cmd.getTaskStatus(), cmd.getInspectionCategoryId(),
				targetTypes, targetIds, null, null, offset, pageSize, lastSyncTime, 0L);
		LOGGER.info("listEquipmentInspectionTasks is  Admin  cacheKey = {}", cacheKey);

		allTasks = equipmentProvider.listEquipmentInspectionTasksUseCache(cmd.getTaskStatus(), cmd.getInspectionCategoryId(),
				targetTypes, targetIds, null, null, offset, pageSize + 1, cacheKey, AdminFlag.YES.getCode(), lastSyncTime);

		if (cmd.getTaskStatus().size() > 1) {
			populateTaskStatusCount(cmd, null, null, AdminFlag.YES.getCode(), response, targetTypes, targetIds);
		} else {
			populateReviewTaskStatusCount(cmd, null, null, AdminFlag.YES.getCode(), response, targetTypes, targetIds);
		}
		return allTasks;
	}

	private void populateReviewTaskStatusCount(ListEquipmentTasksCommand cmd, List<Long> executePlanIds, List<Long> reviewPlanIds, Byte isAdmin, ListEquipmentTasksResponse response, List<String> targetTypes, List<Long> targetIds) {
		equipmentProvider.populateReviewTaskStatusCount(executePlanIds, reviewPlanIds, isAdmin, response, (loc, query) -> {
			if (targetTypes.size() > 0)
				query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_TYPE.in(targetTypes));

			if (targetIds.size() > 0)
				query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID.in(targetIds));

			if (cmd.getInspectionCategoryId() != null) {
				query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.INSPECTION_CATEGORY_ID.eq(cmd.getInspectionCategoryId()));
			}
			return query;
		});
	}

	private void populateTaskStatusCount(ListEquipmentTasksCommand cmd, List<Long> executePlanIds, List<Long> reviewPlanIds, Byte adminFlag, ListEquipmentTasksResponse response, List<String> targetTypes, List<Long> targetIds) {
		equipmentProvider.populateTodayTaskStatusCount(executePlanIds, reviewPlanIds, adminFlag, response, (loc, query) -> {
			if (targetTypes.size() > 0)
				query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_TYPE.in(targetTypes));

			if (targetIds.size() > 0)
				query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID.in(targetIds));

			if (cmd.getInspectionCategoryId() != null) {
				query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.INSPECTION_CATEGORY_ID.eq(cmd.getInspectionCategoryId()));
			}
			return query;
		});
	}

	private boolean checkCurrentUserisAdmin(Long ownerId, Long userId) {
		Boolean adminAssign = false;
		List<RoleAssignment> resources = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), ownerId, EntityType.USER.getCode(), userId);
		if (null != resources && 0 != resources.size()) {
			for (RoleAssignment resource : resources) {
				if (resource.getRoleId() == RoleConstants.ENTERPRISE_SUPER_ADMIN
						|| resource.getRoleId() == RoleConstants.ENTERPRISE_ORDINARY_ADMIN
						|| resource.getRoleId() == RoleConstants.PM_SUPER_ADMIN
						|| resource.getRoleId() == RoleConstants.PM_ORDINARY_ADMIN) {
//					return true;
					adminAssign = true;
				}
			}
		}
		Boolean isAdmin = checkAdmin(ownerId, null, UserContext.getCurrentNamespaceId());
		return (adminAssign || isAdmin);
	}

	//用于替换旧的admin验证
	private Boolean checkAdmin(Long ownerId, String ownerType,Integer namespaceId) {
		ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
		listServiceModuleAppsCommand.setNamespaceId(namespaceId);
		listServiceModuleAppsCommand.setModuleId(EquipmentConstant.EQUIPMENT_MODULE);
		ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
		CheckModuleManageCommand checkModuleManageCommand = new CheckModuleManageCommand();
		checkModuleManageCommand.setModuleId(EquipmentConstant.EQUIPMENT_MODULE);
		checkModuleManageCommand.setOrganizationId(ownerId);
		checkModuleManageCommand.setOwnerType(ownerType);
		checkModuleManageCommand.setUserId(UserContext.currentUserId());
		if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
			checkModuleManageCommand.setAppId(apps.getServiceModuleApps().get(0).getOriginId());
		}
		return serviceModuleService.checkModuleManage(checkModuleManageCommand) != 0;
	}


	private String convertListEquipmentInspectionTasksCache(List<Byte> taskStatus, Long inspectionCategoryId, List<String> targetType, List<Long> targetId,
															List<Long> executeStandardIds, List<Long> reviewStandardIds, Long offset, Integer pageSize, Timestamp lastSyncTime, Long userId) {

		StringBuilder sb = new StringBuilder();
		if (inspectionCategoryId == null) {
			inspectionCategoryId = -1L;
		}

		sb.append(userId);
		sb.append("-community-");
		if (targetId != null && targetId.size() > 0) {
			sb.append(targetId.get(0));
		}
		sb.append(taskStatus);
		sb.append("-");
		sb.append(inspectionCategoryId);
		sb.append("-");
		sb.append(offset);
		sb.append(pageSize);
		sb.append("-");
		if (executeStandardIds == null || executeStandardIds.size() == 0) {
			sb.append("all");
		} else {
			Collections.sort(executeStandardIds);
			sb.append(executeStandardIds);
		}
		if (reviewStandardIds == null || reviewStandardIds.size() == 0) {
			sb.append("all");
		} else {
			Collections.sort(reviewStandardIds);
			sb.append(reviewStandardIds);
		}
		if (lastSyncTime != null) {
			sb.append(lastSyncTime.toString());
		}

		return sb.toString();
	}

//	@Override
//	public ListEquipmentTasksResponse listEquipmentTasks(
//			ListEquipmentTasksCommand cmd) {
//
//		long startTime = System.currentTimeMillis();
//		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
//		User user = UserContext.current().getUser();
//
//		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
////        CrossShardListingLocator locator = new CrossShardListingLocator();
////        locator.setAnchor(cmd.getPageAnchor());
//		if(null == cmd.getPageAnchor()) {
//			cmd.setPageAnchor(0L);
//		}
//		Integer offset = cmd.getPageAnchor().intValue();
//
//        List<EquipmentInspectionTasks> tasks = new ArrayList<EquipmentInspectionTasks>();
//        List<String> targetTypes = new ArrayList<String>();
//        List<Long> targetIds = new ArrayList<Long>();
//        if(cmd.getTargetType() != null)
//        	targetTypes.add(cmd.getTargetType());
//        if(cmd.getTargetId() != null)
//        	targetIds.add(cmd.getTargetId());
//
//        //是否是管理员
//        boolean isAdmin = false;
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
//		if(isAdmin) {
//			tasks = equipmentProvider.listEquipmentInspectionTasks(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getInspectionCategoryId(), targetTypes, targetIds, null, offset, pageSize + 1);
//		} else {
//			List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
//			if(cmd.getIsReview() != null && TaskType.REVIEW_TYPE.equals(TaskType.fromStatus(cmd.getIsReview()))) {
//
//				List<EquipmentInspectionStandardGroupMap> maps = equipmentProvider.listEquipmentInspectionStandardGroupMapByGroupAndPosition(groupDtos, QualityGroupType.REVIEW_GROUP.getCode());
//				if(maps != null && maps.size() > 0) {
//					List<Long> standardIds = maps.stream().map(r->{
//						return r.getStandardId();
//					}).collect(Collectors.toList());
//					tasks = equipmentProvider.listEquipmentInspectionReviewTasks(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getInspectionCategoryId(), targetTypes, targetIds, standardIds, offset, pageSize + 1);
//				}
//
//			} else {
//				//产品修改需求，生成任务仅根据标准周期生成 与选择了多少部门岗位无关 所以根据用户关联的部门岗位先去eh_equipment_inspection_standard_group_map查出standardIds再根据standardIds来查 by xiongying20170213
////				tasks = equipmentProvider.listEquipmentInspectionTasks(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getInspectionCategoryId(), targetTypes, targetIds, groupDtos, offset, pageSize + 1);
//				List<EquipmentInspectionStandardGroupMap> maps = equipmentProvider.listEquipmentInspectionStandardGroupMapByGroupAndPosition(groupDtos, QualityGroupType.EXECUTIVE_GROUP.getCode());
//				if(maps != null && maps.size() > 0) {
//					List<Long> standardIds = maps.stream().map(r->{
//						return r.getStandardId();
//					}).collect(Collectors.toList());
//					tasks = equipmentProvider.listEquipmentInspectionTasks(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getInspectionCategoryId(), targetTypes, targetIds, standardIds, offset, pageSize + 1);
//				}
//
//			}
//		}
//        if(tasks.size() > pageSize) {
//        	tasks.remove(tasks.size() - 1);
//        	response.setNextPageAnchor((long) (offset + 1));
//        }
//
////    	List<EquipmentTaskDTO> dtos = convertEquipmentTasksToDTO(tasks);
////				tasks.stream().map(r -> {
////        	EquipmentTaskDTO dto = convertEquipmentTaskToDTO(r);
////        	return dto;
////        }).filter(r->r!=null).collect(Collectors.toList());
//		long startTime2 = System.currentTimeMillis();
//		List<EquipmentTaskDTO> dtos = tasks.stream().map(r -> {
//			EquipmentTaskDTO dto = ConvertHelper.convert(r, EquipmentTaskDTO.class);
//			EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(r.getEquipmentId());
//        	if(equipment != null) {
//				dto.setEquipmentLocation(equipment.getLocation());
//				dto.setQrCodeFlag(equipment.getQrCodeFlag());
//			}
//			return dto;
//		}).filter(r->r!=null).collect(Collectors.toList());
//
//		response.setTasks(dtos);
//
//		long endTime = System.currentTimeMillis();
//		LOGGER.debug("TrackUserRelatedCost: listEquipmentTasks total elapse:{}, convertEquipmentTaskDTO resultSize:{}, convert time:{}" ,
//				(endTime - startTime), tasks.size(), (endTime - startTime2));
//		return response;
//	}


	private List<ExecuteGroupAndPosition> listUserRelateGroups() {
		Long startTime = System.currentTimeMillis();
		User user = UserContext.current().getUser();

		List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(user.getId());
		if (members == null || members.size() == 0) {
			return new ArrayList<>();
		}

		List<ExecuteGroupAndPosition> groupDtos = new ArrayList<ExecuteGroupAndPosition>();
		for (OrganizationMember member : members) {
			Organization organization = organizationProvider.findOrganizationById(member.getOrganizationId());

			if (organization != null) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("listUserRelateGroups, organizationId=" + organization.getId());
				}
				if (OrganizationGroupType.JOB_POSITION.equals(OrganizationGroupType.fromCode(organization.getGroupType()))) {
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

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("listUserRelateGroups, groupDtos = {}", groupDtos);
		}

		Long endTime = System.currentTimeMillis();
		LOGGER.debug("TrackUserRelatedCost: listUserRelateGroups userId = " + user.getId() + ", elapse=" + (endTime - startTime));
		return groupDtos;
	}

	@Override
	public EquipmentsDTO findEquipment(DeleteEquipmentsCommand cmd) {

//		EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());
		//分公司查不到总公司的设备 by xiongying20170323
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(cmd.getEquipmentId());
		if (equipment == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_EXIST,
					"设备不存在");
		}
		EquipmentsDTO dto = ConvertHelper.convert(equipment, EquipmentsDTO.class);
		Community community = communityProvider.findCommunityById(dto.getTargetId());
		String communityName = null;
		if (community != null) {
			communityName = community.getName();
			dto.setTargetName(communityName);
		}
		List<EquipmentAccessoryMapDTO> eqAccessoryMap = new ArrayList<EquipmentAccessoryMapDTO>();

		List<EquipmentInspectionAccessoryMap> map = equipmentProvider.listAccessoryMapByEquipmentId(dto.getId());
		if (null != map) {
			for (EquipmentInspectionAccessoryMap acMap : map) {
				EquipmentAccessoryMapDTO mapDto = ConvertHelper.convert(acMap, EquipmentAccessoryMapDTO.class);
				mapDto.setCommunityName(communityName);
				EquipmentInspectionAccessories accessory = equipmentProvider.findAccessoryById(acMap.getAccessoryId());
				EquipmentAccessoriesDTO accessoryDto = ConvertHelper.convert(accessory, EquipmentAccessoriesDTO.class);
				Organization target = organizationProvider.findOrganizationById(accessoryDto.getTargetId());
				if (target != null) {
					accessoryDto.setTargetName(target.getName());
				}
				mapDto.setEqAccessories(accessoryDto);
				eqAccessoryMap.add(mapDto);
			}
		}
		dto.setEqAccessoryMap(eqAccessoryMap);
		populateEquipmentStandards(dto);

		ListAttachmentsByEquipmentIdCommand command = new ListAttachmentsByEquipmentIdCommand();
		command.setEquipmentId(dto.getId());
		//command.setAttachmentType((byte) 1);
		List<EquipmentAttachmentDTO> attachments = listAttachmentsByEquipmentId(command);
		dto.setAttachments(attachments);

		return dto;
	}

	@Override
	public List<EquipmentParameterDTO> listParametersByEquipmentId(
			DeleteEquipmentsCommand cmd) {
		List<EquipmentInspectionEquipmentParameters> paras = equipmentProvider.listParametersByEquipmentId(cmd.getEquipmentId());

		if (paras == null || paras.size() == 0) {
			return null;
		}

		List<EquipmentParameterDTO> dtos = paras.stream().map(r -> {
			EquipmentParameterDTO dto = ConvertHelper.convert(r, EquipmentParameterDTO.class);
			return dto;
		}).collect(Collectors.toList());

		return dtos;

	}

	private String convertAttachmentURL(String url) {
		return url + "&w=600" + "&h=800";
	}

	@Override
	public List<EquipmentAttachmentDTO> listAttachmentsByEquipmentId(
			ListAttachmentsByEquipmentIdCommand cmd) {
		List<EquipmentInspectionEquipmentAttachments> attachments = equipmentProvider.listAttachmentsByEquipmentId(cmd.getEquipmentId(), cmd.getAttachmentType());

		if (attachments == null || attachments.size() == 0) {
			return null;
		}

		List<EquipmentAttachmentDTO> dtos = attachments.stream().map(r -> {
			EquipmentAttachmentDTO dto = ConvertHelper.convert(r, EquipmentAttachmentDTO.class);
			String contentUri = r.getContentUri();
			if (contentUri != null && contentUri.length() > 0) {
				try {
					String url = contentServerService.parserUri(contentUri, EntityType.USER.getCode(), UserContext.current().getUser().getId());
					dto.setContentUrl(convertAttachmentURL(url));
				} catch (Exception e) {
					LOGGER.error("Failed to parse attachment uri, equipmentId=" + r.getEquipmentId() + ", attachmentId=" + r.getId(), e);
				}
			} else {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn("The content uri is empty, attchmentId=" + r.getId());
				}
			}

			return dto;
		}).collect(Collectors.toList());

		return dtos;
	}

	@Override
	public List<OrganizationDTO> listRelatedOrgGroups(ListRelatedOrgGroupsCommand cmd) {
		User user = UserContext.current().getUser();
		boolean isAdmin = false;
		List<RoleAssignment> resources = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOwnerId(), EntityType.USER.getCode(), user.getId());
		if (null != resources && 0 != resources.size()) {
			for (RoleAssignment resource : resources) {
				if (resource.getRoleId() == RoleConstants.ENTERPRISE_SUPER_ADMIN
						|| resource.getRoleId() == RoleConstants.ENTERPRISE_ORDINARY_ADMIN
						|| resource.getRoleId() == RoleConstants.PM_SUPER_ADMIN
						|| resource.getRoleId() == RoleConstants.PM_ORDINARY_ADMIN) {
					isAdmin = true;
					break;
				}
			}
		}

		List<String> groupTypes = new ArrayList<String>();
		groupTypes.add(OrganizationGroupType.GROUP.getCode());

		if (isAdmin) {
			List<OrganizationDTO> orgs = organizationService.listAllChildrenOrganizationMenusWithoutMenuStyle(cmd.getOwnerId(),
					groupTypes, OrganizationNaviFlag.HIDE_NAVI.getCode());

			return orgs;
		} else {
			List<OrganizationDTO> groupDtos = organizationService.listUserRelateOrganizations(cmd.getNamespaceId(),
					user.getId(), OrganizationGroupType.GROUP);
			List<OrganizationDTO> dtos = new ArrayList<OrganizationDTO>();

			if (null == groupDtos || groupDtos.size() == 0) {
				return null;
			} else {
				for (OrganizationDTO group : groupDtos) {
					List<RoleAssignment> res = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), group.getId(), EntityType.USER.getCode(), user.getId());
					if (null != res && 0 != res.size()) {
						for (RoleAssignment resource : res) {
							if (resource.getRoleId() == RoleConstants.EQUIPMENT_MANAGER) {
								dtos.add(group);
								break;
							}
						}
					}
				}
			}
			return dtos;
		}
	}

	@Override
	public ListEquipmentTasksResponse listTasksByEquipmentId(ListTasksByEquipmentIdCommand cmd) {
		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		Timestamp startTime = null;
		Timestamp endTime = null;
		if (cmd.getStartTime() != null) {
			startTime = new Timestamp(cmd.getStartTime());
		}
		if (cmd.getExpireTime() != null) {
			endTime = new Timestamp(cmd.getExpireTime());
		}

		//List<EquipmentInspectionTasks> tasks = equipmentProvider.listTasksByEquipmentId(cmd.getEquipmentId(),
		// standardIds, startTime, endTime, locator, pageSize+1, null);
		//之前任务表中有设备id  V3.0.2中改为根据planId关联任务的巡检对象
		List<EquipmentInspectionEquipmentPlanMap> planMaps = equipmentProvider.listPlanMapByEquipmentId(cmd.getEquipmentId());
		List<EquipmentInspectionTasks> tasks = new ArrayList<>();
		if (planMaps != null && planMaps.size() > 0) {
			List<Long> planIds = new ArrayList<>();
			planIds = planMaps.stream()
					.map(EquipmentInspectionEquipmentPlanMap::getPlanId)
					.collect(Collectors.toList());
			tasks = equipmentProvider.listTaskByPlanMaps(planIds, startTime, endTime, locator, pageSize + 1, cmd.getTaskStatus());
		} else {
//			List<Long> standardIds = null;
//			if (cmd.getTaskType() != null) {
//				standardIds = equipmentProvider.listStandardIdsByType(cmd.getTaskType());
//			}
//			tasks = equipmentProvider.listTasksByEquipmentId(cmd.getEquipmentId(),
//					standardIds, startTime, endTime, locator, pageSize + 1, null);
			return null;
		}
		if (tasks.size() > pageSize) {
			tasks.remove(tasks.size() - 1);
			response.setNextPageAnchor(tasks.get(tasks.size() - 1).getId());
		} else {
			response.setNextPageAnchor(null);
		}

		List<EquipmentTaskDTO> dtos = tasks.stream().map(this::convertEquipmentTaskToDTO).filter(Objects::nonNull).collect(Collectors.toList());

		response.setTasks(dtos);

		return response;
	}

	@Override
	public EquipmentAccessoriesDTO findEquipmentAccessoriesById(DeleteEquipmentAccessoriesCommand cmd) {

		EquipmentInspectionAccessories accessory = verifyEquipmentAccessories(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
		if (accessory.getStatus() == 0) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_ACCESSORY_ALREADY_DELETED,
					"备品备件已失效");
		}

		EquipmentAccessoriesDTO dto = ConvertHelper.convert(accessory, EquipmentAccessoriesDTO.class);
		return dto;
	}

	@Override
	public EquipmentTaskDTO listTaskById(ListTaskByIdCommand cmd) {
//		EquipmentInspectionTasks task = verifyEquipmentTask(cmd.getTaskId(), cmd.getOwnerType(), cmd.getOwnerId());
		//分公司拿不到ownerId为总公司的数据 by xiongying20170323
		EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(cmd.getTaskId());

		if (task == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_NOT_EXIST,
					"任务不存在");
		}
		//List<EquipmentStandardRelationDTO> equipments = new ArrayList<>();
//		DeleteEquipmentPlanCommand delCommand = new DeleteEquipmentPlanCommand();
//		delCommand.setId(task.getPlanId());
//		delCommand.setOwnerId(cmd.getOwnerId());
//		delCommand.setOwnerType(cmd.getOwnerType());
//		if (getEquipmmentInspectionPlanById(delCommand) != null)
//			equipments = getEquipmmentInspectionPlanById(delCommand)
//					.getEquipmentStandardRelations();

		EquipmentTaskDTO dto = convertEquipmentTaskToDTO(task);
		return dto;
	}

	@Override
	public void createEquipmentCategory(CreateEquipmentCategoryCommand cmd) {

		Integer namespaceId = cmd.getNamespaceId();
		Long parentId = cmd.getParentId();
		String path = "";
		Category category = getEquipmentCategory(parentId);
		path = category.getPath() + "/" + cmd.getName();

		category = categoryProvider.findCategoryByNamespaceAndName(parentId, namespaceId, null, null, cmd.getName());
//		category = categoryProvider.findCategoryByPath(namespaceId, path);
		if (category != null) {
			LOGGER.error("equipment category have been in existing");
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE, EquipmentServiceErrorCode.ERROR_CATEGORY_EXIST,
					"equipment category have been in existing");
		}


		category = new Category();
		category.setCreateTime(new Timestamp(System.currentTimeMillis()));
		category.setDefaultOrder(0);
		category.setName(cmd.getName());
		category.setNamespaceId(namespaceId);
		category.setPath(path);
		category.setParentId(parentId);
		category.setStatus(CategoryAdminStatus.ACTIVE.getCode());
		categoryProvider.createCategory(category);


	}

	@Override
	public void updateEquipmentCategory(UpdateEquipmentCategoryCommand cmd) {
		Category category = getEquipmentCategory(cmd.getId());
		category.setName(cmd.getName());

		Category parent = getEquipmentCategory(category.getParentId());
		String path = parent.getPath() + "/" + cmd.getName();
		category.setPath(path);
		categoryProvider.updateCategory(category);
	}

	@Override
	public void deleteEquipmentCategory(DeleteEquipmentCategoryCommand cmd) {
		Category category = getEquipmentCategory(cmd.getId());
		category.setStatus(CategoryAdminStatus.INACTIVE.getCode());
		categoryProvider.updateCategory(category);
	}

	private Category getEquipmentCategory(Long categoryId) {
		Category category = categoryProvider.findCategoryById(categoryId);
		if (category == null) {
			LOGGER.error("equipment category not found, categoryId={}", categoryId);
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE, EquipmentServiceErrorCode.ERROR_EQUIPMENT_CATEGORY_NULL,
					"equipment category not found");
		}
		return category;
	}

	@Override
	public List<InspectionItemDTO> listParametersByStandardId(ListParametersByStandardIdCommand cmd) {
		EquipmentInspectionStandards standard = verifyEquipmentStandard(cmd.getStandardId());

		EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(standard.getTemplateId(),
				cmd.getOwnerId(), cmd.getOwnerType());
		//解决之前的删除导致的任务查不到巡检template问题
		if (template == null) {
//			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
//					EquipmentServiceErrorCode.ERROR_TEMPLATE_NOT_EXIST,
// 				"巡检项不存在");
			return null;
		}
		InspectionTemplateDTO dto = ConvertHelper.convert(template, InspectionTemplateDTO.class);

		return listTemplateItems(dto, cmd.getStandardId());
	}

	@Override
	public void createInspectionTemplate(CreateInspectionTemplateCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_ITEM_CREATE, cmd.getTargetId());

		EquipmentInspectionTemplates template = ConvertHelper.convert(cmd, EquipmentInspectionTemplates.class);
		template.setCreatorUid(UserContext.current().getUser().getId());
		Long templateId = equipmentProvider.createEquipmentInspectionTemplates(template);

		List<InspectionItemDTO> items = cmd.getItems();
		if (items != null && items.size() > 0) {
			EquipmentInspectionTemplateItemMap map = new EquipmentInspectionTemplateItemMap();
			map.setTemplateId(templateId);
			for (InspectionItemDTO dto : items) {
				EquipmentInspectionItems item = ConvertHelper.convert(dto, EquipmentInspectionItems.class);
				Long itemId = equipmentProvider.createEquipmentInspectionItems(item);

				map.setItemId(itemId);
				equipmentProvider.createEquipmentInspectionTemplateItemMap(map);
			}
			if (cmd.getCommunities() != null && cmd.getCommunities().size() > 0) {
				//此处创建公共标准关联表 targetId为null为公共标准
				for (Long communityId : cmd.getCommunities()) {
					EquipmentModelCommunityMap communityMap = new EquipmentModelCommunityMap();
					communityMap.setModelId(template.getId());
					communityMap.setTargetType(template.getTargetType());
					communityMap.setTargetId(communityId);
					communityMap.setModelType(EquipmentModelType.TEMPLATE.getCode());
					equipmentProvider.createEquipmentModelCommunityMap(communityMap);
				}
			}
		}


	}

	@Override
	public void updateInspectionTemplate(UpdateInspectionTemplateCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_ITEM_UPDATE, cmd.getTargetId());
		EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType());
		if (template == null || Status.INACTIVE.equals(Status.fromStatus(template.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_TEMPLATE_NOT_EXIST,
					"模板不存在");
		}

		//cmd.getTargetId表示当前操作是在项目还是全部，template.getTargetId是用来判断是否为公共标准如果在项目上修改公共的则创建副本
		if (template.getTargetId() == 0L && cmd.getTargetId() != null) {
			EquipmentInspectionTemplates templatesCopy = ConvertHelper.convert(cmd, EquipmentInspectionTemplates.class);
			templatesCopy.setReferId(cmd.getId());
			templatesCopy.setCreatorUid(UserContext.currentUserId());
			equipmentProvider.createEquipmentInspectionTemplates(templatesCopy);
			//创建item和模板map表关系
			for (InspectionItemDTO item : cmd.getItems()) {
				EquipmentInspectionItems copyItem = ConvertHelper.convert(item, EquipmentInspectionItems.class);
				Long copyItemId = equipmentProvider.createEquipmentInspectionItems(copyItem);
				EquipmentInspectionTemplateItemMap map = new EquipmentInspectionTemplateItemMap();
				map.setTemplateId(templatesCopy.getId());
				map.setItemId(copyItemId);
				equipmentProvider.createEquipmentInspectionTemplateItemMap(map);
			}
		} else {

			if (!template.getName().equals(cmd.getName())) {
				template.setName(cmd.getName());
				equipmentProvider.updateEquipmentInspectionTemplates(template);
			}
			//增加应用项目列表修改 getCommunities不为空 getTargetId 为空则是在全部中修改template
			if (cmd.getTargetId() == null) {
				equipmentProvider.deleteModelCommunityMapByModelId(template.getId(), EquipmentModelType.TEMPLATE.getCode());
				if (cmd.getCommunities() != null && cmd.getCommunities().size() > 0) {
					for (Long communityId : cmd.getCommunities()) {
						EquipmentModelCommunityMap communityMap = new EquipmentModelCommunityMap();
						communityMap.setModelId(template.getId());
						communityMap.setTargetType(template.getTargetType());
						communityMap.setTargetId(communityId);
						communityMap.setModelType(EquipmentModelType.TEMPLATE.getCode());
						equipmentProvider.createEquipmentModelCommunityMap(communityMap);
					}
				}
			}

			List<InspectionItemDTO> updateItems = cmd.getItems();
			List<EquipmentInspectionTemplateItemMap> maps = equipmentProvider.listEquipmentInspectionTemplateItemMap(template.getId());
			if (updateItems == null || updateItems.size() == 0) {
				if (maps != null && maps.size() > 0) {
					for (EquipmentInspectionTemplateItemMap map : maps) {
						equipmentProvider.deleteEquipmentInspectionTemplateItemMap(map.getId());
					}
				}
			} else {
				List<Long> updateItemIds = new ArrayList<Long>();

				//cmd item 不带id的create，其他的看map表中的itemId在不在cmd里面 不在的删掉
				for (InspectionItemDTO dto : updateItems) {
					if (dto.getId() == null) {

						EquipmentInspectionItems item = ConvertHelper.convert(dto, EquipmentInspectionItems.class);
						Long itemId = equipmentProvider.createEquipmentInspectionItems(item);
						EquipmentInspectionTemplateItemMap map = new EquipmentInspectionTemplateItemMap();
						map.setTemplateId(template.getId());
						map.setItemId(itemId);
						equipmentProvider.createEquipmentInspectionTemplateItemMap(map);
					} else {
						EquipmentInspectionItems item = ConvertHelper.convert(dto, EquipmentInspectionItems.class);
						equipmentProvider.updateEquipmentInspectionItems(item);

						updateItemIds.add(dto.getId());
					}
				}
				// check maps is null for nullPointException
				if (maps != null && maps.size() > 0) {
					for (EquipmentInspectionTemplateItemMap map : maps) {
						if (!updateItemIds.contains(map.getItemId())) {
							equipmentProvider.deleteEquipmentInspectionTemplateItemMap(map.getId());
						}
					}
				}
			}
		}


	}

	@Override
	public void deleteInspectionTemplate(DeleteInspectionTemplateCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_ITEM_DELETE, cmd.getTargetId());
		EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(cmd.getId(),
				cmd.getOwnerId(), cmd.getOwnerType());
		if (template == null || Status.INACTIVE.equals(Status.fromStatus(template.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_TEMPLATE_NOT_EXIST,
					"模板不存在");
		}
		//增加判断template删除情况判断   以下为在项目上删除公共模板
		if (template.getTargetId() == 0L && cmd.getTargetId() != null) {
			equipmentProvider.deleteModelCommunityMapByModelIdAndCommunityId(cmd.getId(), cmd.getTargetId(),
					EquipmentModelType.TEMPLATE.getCode());
		} else {
			template.setStatus(Status.INACTIVE.getCode());
			template.setDeleteUid(UserContext.current().getUser().getId());
			template.setDeleteTime(new Timestamp(System.currentTimeMillis()));
			equipmentProvider.updateEquipmentInspectionTemplates(template);
			if (cmd.getTargetId() == null) {
				equipmentProvider.deleteModelCommunityMapByModelId(template.getId(), EquipmentModelType.TEMPLATE.getCode());
			}

			List<EquipmentInspectionStandards> standards = equipmentProvider.listEquipmentInspectionStandardsByTemplateId(template.getId());
			if (standards != null && standards.size() > 0) {
				for (EquipmentInspectionStandards standard : standards) {
					standard.setTemplateId(0L);
					equipmentProvider.updateEquipmentStandard(standard);
				}
			}
		}
	}

	@Override
	public InspectionTemplateDTO findInspectionTemplate(
			DeleteInspectionTemplateCommand cmd) {
		EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(cmd.getId(),
				cmd.getOwnerId(), cmd.getOwnerType());
		if (template == null || Status.INACTIVE.equals(Status.fromStatus(template.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_TEMPLATE_NOT_EXIST,
					"巡检项不存在");
		}
		InspectionTemplateDTO dto = ConvertHelper.convert(template, InspectionTemplateDTO.class);
		List<InspectionItemDTO> items = listTemplateItems(dto, null);
		if (items != null && items.size() > 0) {
			dto.setItems(items);
		}
		if (template.getTargetId() == 0L) {
			dto.setCommunities(equipmentProvider.listModelCommunityMapByModelId(template.getId(), EquipmentModelType.TEMPLATE.getCode()));
		}

		return dto;
	}

	private List<InspectionItemDTO> listTemplateItems(InspectionTemplateDTO template, Long standardId) {
		List<EquipmentInspectionTemplateItemMap> maps = equipmentProvider.listEquipmentInspectionTemplateItemMap(template.getId());
		if (maps != null && maps.size() > 0) {
			List<InspectionItemDTO> items = new ArrayList<>();
			for (EquipmentInspectionTemplateItemMap map : maps) {

				EquipmentInspectionItems item = equipmentProvider.findEquipmentInspectionItem(map.getItemId());
				if (item != null) {
					InspectionItemDTO itemDto = ConvertHelper.convert(item, InspectionItemDTO.class);
					itemDto.setStandardId(standardId);
					items.add(itemDto);
				}
			}

			return items;
		}

		return null;
	}

	@Override
	public List<InspectionTemplateDTO> listInspectionTemplates(
			ListInspectionTemplatesCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_ITEM_LIST, cmd.getTargetId());
		List<InspectionTemplateDTO> dtos = new ArrayList<InspectionTemplateDTO>();
		//增加判断为全部里面查看还是项目中查看
		List<EquipmentInspectionTemplates> templates = new ArrayList<>();
		if (cmd.getTargetId() != null && cmd.getTargetId() == 0L) {
			//这个是全部里面查看
			templates = equipmentProvider.listInspectionTemplates(cmd.getNamespaceId(), cmd.getName(), null);
		} else {
			//先查出来属于项目自定义的模板
			templates = equipmentProvider.listInspectionTemplates(cmd.getNamespaceId(), cmd.getName(), cmd.getTargetId());
			//获取所有有referId的集合
			List<Long> removeIds = new ArrayList<>();
			if (templates != null && templates.size() > 0) {
				templates.stream()
						.filter(t -> t.getReferId() != null && t.getReferId() != 0)
						.forEach(t -> removeIds.add(t.getReferId()));
			}

			//referId的公共模板去掉
			List<EquipmentModelCommunityMap> templatesMap = equipmentProvider.listModelCommunityMapByCommunityId(cmd.getTargetId(), EquipmentModelType.TEMPLATE.getCode());
			if (templatesMap.size() > 0) {
				for (EquipmentModelCommunityMap map : templatesMap) {
					if (!removeIds.contains(map.getModelId())) {
						EquipmentInspectionTemplates modelTemplates = equipmentProvider.findEquipmentInspectionTemplate(map.getModelId(), cmd.getOwnerId(), cmd.getOwnerType());
						if (templates == null) {
							templates = new ArrayList<>();
						}
						templates.add(modelTemplates);
					}
				}
			}
		}

		if (templates != null && templates.size() > 0) {
			for (EquipmentInspectionTemplates template : templates) {
				InspectionTemplateDTO dto = ConvertHelper.convert(template, InspectionTemplateDTO.class);
				if (template.getTargetId() == 0L) {
					List<Long> communities = equipmentProvider.listModelCommunityMapByModelId(template.getId(), EquipmentModelType.TEMPLATE.getCode());
					if (communities != null && communities.size() > 0) {
						dto.setCommunities(communities);
					}
					dto.setTargetId(template.getTargetId());
				}
				if (cmd.getTargetId() == null || cmd.getTargetId() == 0) {
					Community community = communityProvider.findCommunityById(template.getTargetId());
					if (community != null)
						dto.setTargetName(community.getName());
				}
				dtos.add(dto);
			}
		}
		return dtos;
	}

	@Override
	public ListEquipmentTasksResponse listTasksByToken(ListTasksByTokenCommand cmd) {

		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentByQrCodeToken(cmd.getQrCodeToken());

		if (equipment == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_EXIST,
					"设备不存在");
		}
		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
		User user = UserContext.current().getUser();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<EquipmentInspectionTasks> tasks = new ArrayList<EquipmentInspectionTasks>();

		boolean isAdmin = false;
		List<RoleAssignment> resources = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), equipment.getOwnerId(), EntityType.USER.getCode(), user.getId());
		if (null != resources && 0 != resources.size()) {
			for (RoleAssignment resource : resources) {
				if (resource.getRoleId() == RoleConstants.ENTERPRISE_SUPER_ADMIN
						|| resource.getRoleId() == RoleConstants.ENTERPRISE_ORDINARY_ADMIN
						|| resource.getRoleId() == RoleConstants.PM_SUPER_ADMIN
						|| resource.getRoleId() == RoleConstants.PM_ORDINARY_ADMIN) {
					isAdmin = true;
					break;
				}
			}
		}

		if (!isAdmin) {
			List<RoleAssignment> res = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), equipment.getTargetId(), EntityType.USER.getCode(), user.getId());
			if (null != res && 0 != res.size()) {
				for (RoleAssignment resource : res) {
					if (resource.getRoleId() == RoleConstants.EQUIPMENT_MANAGER) {
						isAdmin = true;
						break;
					}
				}
			}
		}


		if (isAdmin) {
			List<Byte> taskStatus = new ArrayList<Byte>();
			taskStatus.add(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
			taskStatus.add(EquipmentTaskStatus.NEED_MAINTENANCE.getCode());
			taskStatus.add(EquipmentTaskStatus.IN_MAINTENANCE.getCode());
			//tasks = equipmentProvider.listTasksByEquipmentId(equipment.getId(), null, null, null, locator, pageSize+1, taskStatus);
			List<EquipmentInspectionEquipmentPlanMap> planMaps = equipmentProvider.listPlanMapByEquipmentId(equipment.getId());
			List<Long> planIds = new ArrayList<>();
			if (planMaps != null && planMaps.size() > 0) {
				planIds = planMaps.stream()
						.map(EquipmentInspectionEquipmentPlanMap::getPlanId)
						.collect(Collectors.toList());
			}
			if (planIds == null || planIds.size() == 0) {
				return null;
			}
			tasks = equipmentProvider.listTaskByPlanMaps(planIds, null, null, locator, pageSize + 1, taskStatus);
		} else {
			//扫码任务做权限控制 只能扫出设备下有执行权限的任务
			List<StandardAndStatus> standards = new ArrayList<>();
			List<Byte> executeTaskStatus = new ArrayList<Byte>();
			executeTaskStatus.add(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());

			List<Byte> reviewTaskStatus = new ArrayList<Byte>();
			reviewTaskStatus.add(EquipmentTaskStatus.CLOSE.getCode());

			List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
			//List<EquipmentInspectionStandardGroupMap> maps = equipmentProvider.listEquipmentInspectionStandardGroupMapByGroupAndPosition(groupDtos, null);
			List<EquipmentInspectionPlanGroupMap> maps = equipmentProvider.listEquipmentInspectionPlanGroupMapByGroupAndPosition(groupDtos, null);
			if (maps != null && maps.size() > 0) {
				for (EquipmentInspectionPlanGroupMap r : maps) {
					StandardAndStatus standardAndStatus = new StandardAndStatus();
					//减少新建类使用上一版标准
					standardAndStatus.setPlanId(r.getPlanId());
					if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(r.getGroupType()))) {
						standardAndStatus.setTaskStatus(reviewTaskStatus);
					}
					if (QualityGroupType.EXECUTIVE_GROUP.equals(QualityGroupType.fromStatus(r.getGroupType()))) {
						standardAndStatus.setTaskStatus(executeTaskStatus);
					}
					standards.add(standardAndStatus);
				}
			}
			tasks = equipmentProvider.listTasksByEquipmentIdAndStandards(standards, null, null, locator, pageSize + 1);
		}

		if (tasks.size() > pageSize) {
			tasks.remove(tasks.size() - 1);
			response.setNextPageAnchor(tasks.get(tasks.size() - 1).getId());
		}
		Timestamp ts = new Timestamp(DateHelper.currentGMTTime().getTime());
		List<EquipmentTaskDTO> dtos = tasks.stream().map(r -> {
			//扫出来的任务要是当前时间能执行的任务 by xiongying20170417
			if (r.getProcessExpireTime() == null) {
				r.setProcessExpireTime(new Timestamp(0L));
			}
			if (ts.after(r.getExecutiveStartTime())
					&& (ts.before(r.getExecutiveExpireTime()) || ts.before(r.getProcessExpireTime()))) {
				return convertEquipmentTaskToDTO(r);
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());

		response.setTasks(dtos);

		return response;
	}

	@Override
	public List<EquipmentInspectionCategoryDTO> listEquipmentInspectionCategories(ListEquipmentInspectionCategoriesCommand cmd) {
		List<EquipmentInspectionCategories> categories = equipmentProvider.listEquipmentInspectionCategories(cmd.getOwnerId(), cmd.getNamespaceId());
		List<EquipmentInspectionCategoryDTO> dtos = new ArrayList<EquipmentInspectionCategoryDTO>();
		if (categories != null && categories.size() > 0) {
			dtos = categories.stream().map(r ->
					ConvertHelper.convert(r, EquipmentInspectionCategoryDTO.class))
					.collect(Collectors.toList());
		}
		return dtos;
	}

	@Override
	public EquipmentsDTO getInspectionObjectByQRCode(GetInspectionObjectByQRCodeCommand cmd) {

		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentByQrCodeToken(cmd.getQrCodeToken());

		if (equipment == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_EXIST,
					"巡检对象不存在");
		}
		return ConvertHelper.convert(equipment, EquipmentsDTO.class);
	}

	@Override
	public ListEquipmentTasksResponse listUserHistoryTasks(ListUserHistoryTasksCommand cmd) {
		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
		Long uId = UserContext.current().getUser().getId();
		Set<Long> taskIds = equipmentProvider.listRecordsTaskIdByOperatorId(uId, cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		List<Long> taskIdlist = new ArrayList<Long>();
		taskIdlist.addAll(taskIds);
		Collections.sort(taskIdlist);
		Collections.reverse(taskIdlist);

		if (taskIdlist.size() > pageSize) {
			taskIdlist.subList(0, pageSize - 1);
			response.setNextPageAnchor(taskIdlist.get(taskIdlist.size() - 1));
		}
		List<EquipmentInspectionTasks> tasks = equipmentProvider.listTaskByIds(taskIdlist);
		List<EquipmentTaskDTO> dtoList = tasks.stream().map(task ->
				ConvertHelper.convert(task, EquipmentTaskDTO.class))
				.collect(Collectors.toList());
		response.setTasks(dtoList);

		return response;
	}

	@Override
	public StatEquipmentTasksResponse statEquipmentTasks(StatEquipmentTasksCommand cmd) {
		StatEquipmentTasksResponse response = new StatEquipmentTasksResponse();

		int offset = cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		List<TaskCountDTO> tasks = equipmentProvider.statEquipmentTasks(cmd.getTargetId(), cmd.getTargetType(), cmd.getInspectionCategoryId(), cmd.getStartTime(), cmd.getEndTime(),
				offset, pageSize + 1);
		if (tasks != null && tasks.size() > pageSize) {
			tasks.remove(tasks.size() - 1);
			response.setNextPageAnchor(offset + 1);
		}

		if (tasks != null) {
			for (TaskCountDTO task : tasks) {
				Community community = communityProvider.findCommunityById(task.getTargetId());
				if (community != null) {
					task.setTargetName(community.getName());
				}
				Double maintanceRate = ((double) (task.getCompleteMaintance() + task.getInMaintance())) / task.getTaskCount();
				task.setMaintanceRate(maintanceRate);
			}
		}
		response.setTasks(tasks);
		return response;
	}

	public void exportEquipmentsCard(ExportEquipmentsCardCommand cmd, HttpServletResponse response) {
		List<Long> equipmentIds = new ArrayList<>();
		if (!StringUtils.isEmpty(cmd.getIds())) {
			String[] ids = cmd.getIds().split(",");
			for (String id : ids) {
				equipmentIds.add(Long.valueOf(id));
			}
		}
		LOGGER.info("equipmentIds: {}", equipmentIds);
		List<EquipmentInspectionEquipments> equipments = equipmentProvider.listEquipmentsById(equipmentIds);
		List<EquipmentsDTO> dtos = equipments.stream().map(equipment ->
				ConvertHelper.convert(equipment, EquipmentsDTO.class))
				.collect(Collectors.toList());

		String filePath = cmd.getFilePath();
		if (StringUtils.isEmpty(cmd.getFilePath())) {
			URL rootPath = EquipmentServiceImpl.class.getResource("/");
			filePath = rootPath.getPath() + this.downloadDir;
			File file = new File(filePath);
			if (!file.exists())
				file.mkdirs();

		}

//		return download(filePath,response);

		DocUtil docUtil = new DocUtil();
		List<String> files = new ArrayList<>();
		if (dtos.size() % 2 == 1) {
			EquipmentsDTO dto = dtos.get(dtos.size() - 1);

			Map<String, Object> dataMap = createEquipmentCardDoc(dto);

//			GetAppInfoCommand command = new GetAppInfoCommand();
//			command.setNamespaceId(dto.getNamespaceId());
//			command.setOsType(OSType.Android.getCode());
//			AppUrlDTO appUrlDTO = appUrlService.getAppInfo(command);
//			if (LOGGER.isDebugEnabled()) {
//				LOGGER.debug("app logo url : {}", appUrlDTO.getLogoUrl());
//			}
//			if (appUrlDTO.getLogoUrl() != null) {
//				dataMap.put("shenyeLogo", docUtil.getUrlImageStr(appUrlDTO.getLogoUrl()));
//			}

			if (QRCodeFlag.ACTIVE.equals(QRCodeFlag.fromStatus(dto.getQrCodeFlag()))) {
				ByteArrayOutputStream out = generateQRCode(Base64.encodeBase64String(dto.getQrCodeToken().getBytes()));
				byte[] data = out.toByteArray();
				BASE64Encoder encoder = new BASE64Encoder();
				dataMap.put("qrCode", encoder.encode(data));
			}

			String savePath = filePath + dto.getId() + "-" + dto.getName() + ".doc";
			docUtil.createDoc(dataMap, "shenye", savePath);

			if (StringUtils.isEmpty(cmd.getFilePath())) {
//				download(savePath,response);
				files.add(savePath);
			}
			dtos.remove(dtos.size() - 1);
		}

		for (int i = 0; i < dtos.size(); i = i + 2) {
			EquipmentsDTO dto1 = dtos.get(i);
			EquipmentsDTO dto2 = dtos.get(i + 1);
//			DocUtil docUtil=new DocUtil();
			Map<String, Object> dataMap = createTwoEquipmentCardDoc(dto1, dto2);

//			GetAppInfoCommand command = new GetAppInfoCommand();
//			command.setNamespaceId(dto1.getNamespaceId());
//			command.setOsType(OSType.Android.getCode());
//			AppUrlDTO appUrlDTO = appUrlService.getAppInfo(command);
//			if (appUrlDTO.getLogoUrl() != null) {
//				dataMap.put("shenyeLogo1", docUtil.getUrlImageStr(appUrlDTO.getLogoUrl()));
//				dataMap.put("shenyeLogo2", docUtil.getUrlImageStr(appUrlDTO.getLogoUrl()));
//			}

			if (QRCodeFlag.ACTIVE.equals(QRCodeFlag.fromStatus(dto1.getQrCodeFlag()))) {
				ByteArrayOutputStream out = generateQRCode(Base64.encodeBase64String(dto1.getQrCodeToken().getBytes()));
				byte[] data = out.toByteArray();
				BASE64Encoder encoder = new BASE64Encoder();
				dataMap.put("qrCode1", encoder.encode(data));
			}

			if (QRCodeFlag.ACTIVE.equals(QRCodeFlag.fromStatus(dto2.getQrCodeFlag()))) {
				ByteArrayOutputStream out = generateQRCode(Base64.encodeBase64String(dto2.getQrCodeToken().getBytes()));
				byte[] data = out.toByteArray();
				BASE64Encoder encoder = new BASE64Encoder();
				dataMap.put("qrCode2", encoder.encode(data));
//				dataMap.put("qrCode2", data.toString());
			}

			String savePath = filePath + dto1.getId() + "-" + dto1.getName() +
					"-" + dto2.getId() + "-" + dto2.getName() + ".doc";

			docUtil.createDoc(dataMap, "shenye2", savePath);
			if (StringUtils.isEmpty(cmd.getFilePath())) {
//				download(savePath,response);
				files.add(savePath);
			}
		}
		if (StringUtils.isEmpty(cmd.getFilePath())) {
			if (files.size() > 1) {
				String zipPath = filePath + System.currentTimeMillis() + "EquipmentCard.zip";
				LOGGER.info("filePath:{}, zipPath:{}", filePath, zipPath);
				DownloadUtils.writeZip(files, zipPath);
				download(zipPath, response);
			} else if (files.size() == 1) {
				download(files.get(0), response);
			}

		}

	}

	private Timestamp getDayBegin(Calendar todayStart, int period) {
		/*cal.add(Calendar.DATE, period);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 001);
		return new Timestamp(cal.getTimeInMillis());*/
		todayStart.add(Calendar.DATE, period);
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return new Timestamp(todayStart.getTime().getTime());
	}

	private Timestamp getDayEnd(Calendar todayEnd, int period) {
		/*cal.add(Calendar.DATE, period);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return new Timestamp(cal.getTimeInMillis());*/
		todayEnd.add(Calendar.DATE, period);
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return new Timestamp(todayEnd.getTime().getTime());
	}

	@Override
	public StatTodayEquipmentTasksResponse statTodayEquipmentTasks(StatTodayEquipmentTasksCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_STAT_PANDECT, cmd.getTargetId());

		Calendar cal = Calendar.getInstance();
		if (cmd.getDateTime() == null) {
			cmd.setDateTime(DateHelper.currentGMTTime().getTime());
		}
		cal.setTime(new Timestamp(cmd.getDateTime()));

		TasksStatData stat = equipmentProvider.statDaysEquipmentTasks(cmd.getTargetId(), cmd.getTargetType(),
				cmd.getInspectionCategoryId(), getDayBegin(cal, 0), getDayEnd(cal, 0), cmd.getNamespaceId());
		//增加统计维修中和维修总数
		equipmentProvider.statInMaintanceTaskCount(stat, getDayBegin(cal, 0), getDayEnd(cal, 0), cmd);
		ReviewedTaskStat reviewStat = equipmentProvider.statDaysReviewedTasks(cmd.getTargetId(),
				cmd.getInspectionCategoryId(), getDayBegin(cal, 0), getDayEnd(cal, 0), cmd.getNamespaceId());
		StatTodayEquipmentTasksResponse response = ConvertHelper.convert(stat, StatTodayEquipmentTasksResponse.class);
		response.setReviewQualified(reviewStat.getQualifiedTasks());
		response.setReviewUnqualified(reviewStat.getUnqualifiedTasks());
		response.setCurrentWaitingForExecuting(stat.getWaitingForExecuting());
		response.setCurrentWaitingForApproval(stat.getCompleteWaitingForApproval());
		response.setReviewed(response.getReviewQualified() + response.getReviewUnqualified());
		return response;
	}

	@Override
	public StatLastDaysEquipmentTasksResponse statLastDaysEquipmentTasks(StatLastDaysEquipmentTasksCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_STAT_PANDECT, cmd.getTargetId());

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		Calendar endDay = Calendar.getInstance();
		TasksStatData statTasks = equipmentProvider.statDaysEquipmentTasks(cmd.getTargetId(), cmd.getTargetType(),
				cmd.getInspectionCategoryId(), getDayBegin(cal, -cmd.getLastDays()), getDayEnd(endDay, 0), cmd.getNamespaceId());
		//增加统计维修中和维修总数
//		StatTodayEquipmentTasksCommand command = ConvertHelper.convert(cmd, StatTodayEquipmentTasksCommand.class);
//		equipmentProvider.statInMaintanceTaskCount(statTasks, getDayBegin(cal, 0), getDayEnd(cal, 0), command);
		ReviewedTaskStat reviewStat = equipmentProvider.statDaysReviewedTasks(cmd.getTargetId(),
				cmd.getInspectionCategoryId(), getDayBegin(cal, -cmd.getLastDays()), getDayEnd(endDay, 0), cmd.getNamespaceId());

		StatLastDaysEquipmentTasksResponse response = new StatLastDaysEquipmentTasksResponse();
		response.setCompleteInspection(statTasks.getCompleteInspectionTasks());
		response.setCompleteMaintance(statTasks.getCompleteMaintance());
		response.setReviewUnqualified(reviewStat.getUnqualifiedTasks());
		response.setReviewQualified(reviewStat.getQualifiedTasks());
		response.setReviewed(response.getReviewQualified() + response.getReviewUnqualified());
		return response;
	}

	@Override
	public StatIntervalAllEquipmentTasksResponse statIntervalAllEquipmentTasks(StatIntervalAllEquipmentTasksCommand cmd) {

		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_STAT_ALLTASK, cmd.getTargetId());

		Timestamp begin = null;
		Timestamp end = null;
		if (cmd.getStartTime() != null) {
			begin = new Timestamp(cmd.getStartTime());
		}
		if (cmd.getEndTime() != null) {
			end = new Timestamp((cmd.getEndTime()));
		}
		TasksStatData statTasks = equipmentProvider.statDaysEquipmentTasks(cmd.getTargetId(), cmd.getTargetType(),
				cmd.getInspectionCategoryId(), begin, end, cmd.getNamespaceId());
		StatTodayEquipmentTasksCommand command = ConvertHelper.convert(cmd, StatTodayEquipmentTasksCommand.class);
		equipmentProvider.statInMaintanceTaskCount(statTasks, begin, end, command);
		ReviewedTaskStat reviewStat = equipmentProvider.statDaysReviewedTasks(cmd.getTargetId(),
				cmd.getInspectionCategoryId(), begin, end, cmd.getNamespaceId());

		StatIntervalAllEquipmentTasksResponse response = ConvertHelper.convert(statTasks, StatIntervalAllEquipmentTasksResponse.class);
		response.setReviewUnqualified(reviewStat.getUnqualifiedTasks());
		response.setReviewQualified(reviewStat.getQualifiedTasks());
		response.setReviewedTasks(response.getReviewUnqualified() + response.getReviewQualified());
		response.setUnReviewedTasks(statTasks.getCompleteWaitingForApproval());
		response.setReviewTasks(response.getUnReviewedTasks() + response.getReviewedTasks());
		response.setCompleteInspection(statTasks.getCompleteWaitingForApproval());

		Double maintanceRate = response.getCompleteInspection().equals(0L) ? 0.00 : (double) response.getCompleteMaintance() / (double) response.getCompleteInspection();
		response.setMaintanceRate(maintanceRate);
		Double delayInspectionRate = (response.getCompleteInspection() + response.getDelayInspection()) == 0L ? 0.00 : (double) response.getDelayInspection() / (double) (response.getCompleteInspection() + response.getDelayInspection());
		response.setDelayInspectionRate(delayInspectionRate);
		Double reviewQualifiedRate = response.getReviewedTasks().equals(0L) ? 0.00 : (double) response.getReviewQualified() / (double) response.getReviewedTasks();
		response.setReviewQualifiedRate(reviewQualifiedRate);
		Double reviewDalayRate = response.getReviewTasks().equals(0L) ? 0.00 : (double) response.getReviewDelayTasks() / (double) response.getReviewTasks();
		response.setReviewDalayRate(reviewDalayRate);

		return response;
	}

	@Override
	public StatItemResultsInEquipmentTasksResponse statItemResultsInEquipmentTasks(StatItemResultsInEquipmentTasksCommand cmd) {
		StatItemResultsInEquipmentTasksResponse response = new StatItemResultsInEquipmentTasksResponse();
//		itemResultStat: 巡检参数统计 参考ItemResultStat
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(cmd.getEquipmentId());
		if (equipment != null) {
			response.setEquipmentName(equipment.getName());
			response.setCustomNumber(equipment.getCustomNumber());
			response.setLocation(equipment.getLocation());
		}

		Timestamp begin = null;
		Timestamp end = null;
		if (cmd.getStartTime() != null) {
			begin = new Timestamp(cmd.getStartTime());
		}
		if (cmd.getEndTime() != null) {
			end = new Timestamp((cmd.getEndTime()));
		}
		List<ItemResultStat> results = equipmentProvider.statItemResults(cmd.getEquipmentId(), cmd.getStandardId(),
				begin, end);
		results.forEach(result -> {
			EquipmentInspectionItems item = equipmentProvider.findEquipmentInspectionItem(result.getItemId());
			if (item != null) {
				result.setValueJason(item.getValueJason());
				result.setValueType(item.getValueType());
			}
		});
		response.setItemResultStat(results);
		return response;
	}

	@Override
	public ListEquipmentTasksResponse listAbnormalTasks(ListAbnormalTasksCommand cmd) {
		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
		List<EquipmentInspectionTasks> tasks = equipmentProvider.listTaskByIds(cmd.getAbnormalTaskIds());
		if (tasks != null && tasks.size() > 0) {
			List<EquipmentTaskDTO> dtos = tasks.stream().map(task -> {
				EquipmentTaskDTO dto = ConvertHelper.convert(task, EquipmentTaskDTO.class);
				EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(task.getEquipmentId());
				if (equipment != null) {
					dto.setEquipmentName(equipment.getName());
					dto.setEquipmentLocation(equipment.getLocation());
				}
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
				if (standard != null) {
					dto.setTaskType(standard.getStandardType());
				}
				if (task.getExecutorId() != null && task.getExecutorId() != 0) {
					List<OrganizationMember> executors = organizationProvider.listOrganizationMembersByUId(task.getExecutorId());
					if (executors != null && executors.size() > 0) {
						dto.setExecutorName(executors.get(0).getContactName());
					}
				}
				return dto;
			}).collect(Collectors.toList());

			response.setTasks(dtos);
		}
		return response;
	}

	private ByteArrayOutputStream generateQRCode(String qrToken) {
		ByteArrayOutputStream out = null;
		try {
			BufferedImage image = QRCodeEncoder.createQrCode(qrToken, 270, 270, null);
			out = new ByteArrayOutputStream();
			ImageIO.write(image, QRCodeConfig.FORMAT_PNG, out);
		} catch (IOException | WriterException e) {
			e.printStackTrace();
		}

		return out;
	}

	private Map<String, Object> createEquipmentCardDoc(EquipmentsDTO dto) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("sequenceNo", dto.getSequenceNo());
		dataMap.put("versionNo", dto.getVersionNo());
		dataMap.put("name", dto.getName());
		dataMap.put("equipmentModel", dto.getEquipmentModel());
		dataMap.put("parameter", dto.getParameter());
		dataMap.put("customNumber", dto.getCustomNumber());
		dataMap.put("manufacturer", dto.getManufacturer());
		dataMap.put("manager", dto.getManager());
		dataMap.put("status", EquipmentStatus.fromStatus(dto.getStatus()).getName());

		return dataMap;
	}

	private Map<String, Object> createTwoEquipmentCardDoc(EquipmentsDTO dto1, EquipmentsDTO dto2) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("sequenceNo1", dto1.getSequenceNo());
		dataMap.put("versionNo1", dto1.getVersionNo());
		dataMap.put("name1", dto1.getName());
		dataMap.put("equipmentModel1", dto1.getEquipmentModel());
		dataMap.put("parameter1", dto1.getParameter());
		dataMap.put("customNumber1", dto1.getCustomNumber());
		dataMap.put("manufacturer1", dto1.getManufacturer());
		dataMap.put("manager1", dto1.getManager());
		dataMap.put("status1", EquipmentStatus.fromStatus(dto1.getStatus()).getName());

		dataMap.put("sequenceNo2", dto2.getSequenceNo());
		dataMap.put("versionNo2", dto2.getVersionNo());
		dataMap.put("name2", dto2.getName());
		dataMap.put("equipmentModel2", dto2.getEquipmentModel());
		dataMap.put("parameter2", dto2.getParameter());
		dataMap.put("customNumber2", dto2.getCustomNumber());
		dataMap.put("manufacturer2", dto2.getManufacturer());
		dataMap.put("manager2", dto2.getManager());
		dataMap.put("status2", EquipmentStatus.fromStatus(dto2.getStatus()).getName());

		return dataMap;
	}

	private PmNotifyParamDTO convertPmNotifyConfigurationsToDTO(Integer namespaceId, PmNotifyConfigurations configuration) {
		PmNotifyParamDTO param = ConvertHelper.convert(configuration, PmNotifyParamDTO.class);
		String receiverJson = configuration.getReceiverJson();
		if (StringUtils.isNotBlank(receiverJson)) {
			PmNotifyReceiverList receiverList = (PmNotifyReceiverList) StringHelper.fromJsonString(receiverJson, PmNotifyReceiverList.class);
			if (receiverList != null && receiverList.getReceivers() != null && receiverList.getReceivers().size() > 0) {
//				param.setReceivers(receiverList.getReceivers());
				List<PmNotifyReceiverDTO> receiverDTOs = new ArrayList<>();
				receiverList.getReceivers().forEach(receiver -> {
					PmNotifyReceiverDTO dto = new PmNotifyReceiverDTO();
					dto.setReceiverType(receiver.getReceiverType());
					if (PmNotifyReceiverType.ORGANIZATION.equals(PmNotifyReceiverType.fromCode(receiver.getReceiverType()))) {
						List<Organization> organizations = organizationProvider.listOrganizationsByIds(receiver.getReceiverIds());
						if (organizations != null && organizations.size() > 0) {
							List<ReceiverName> dtoReceivers = new ArrayList<ReceiverName>();
							organizations.forEach(organization -> {
								ReceiverName receiverName = new ReceiverName();
								receiverName.setId(organization.getId());
								receiverName.setName(organization.getName());
								dtoReceivers.add(receiverName);
							});
							dto.setReceivers(dtoReceivers);
						}
					} else if (PmNotifyReceiverType.ORGANIZATION_MEMBER.equals(PmNotifyReceiverType.fromCode(receiver.getReceiverType()))) {
						if (receiver.getReceiverIds() != null) {
//							List<OrganizationMember> members = organizationProvider.listOrganizationMembersByIds(receiver.getReceiverIds());
							if (receiver.getReceiverIds() != null && receiver.getReceiverIds().size() > 0) {
								List<ReceiverName> dtoReceivers = new ArrayList<>();
								receiver.getReceiverIds().forEach(uId -> {
									List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(uId);
									if (members != null && members.size() > 0) {
										ReceiverName receiverName = new ReceiverName();
										receiverName.setId(members.get(0).getId());
										receiverName.setName(members.get(0).getContactName());
										receiverName.setContactToken(members.get(0).getContactToken());
										dtoReceivers.add(receiverName);
									}
								});
								dto.setReceivers(dtoReceivers);
							}
						}
					}
					receiverDTOs.add(dto);
				});
				param.setReceivers(receiverDTOs);
			}
		}
		return param;
	}

	@Override
	public void deletePmNotifyParams(DeletePmNotifyParamsCommand cmd) {
		if (cmd.getId() == null) {
			return;
		}
		Byte scopeType = PmNotifyScopeType.NAMESPACE.getCode();
		Long scopeId = cmd.getNamespaceId().longValue();
		if (cmd.getCommunityId() != null && cmd.getCommunityId() != 0L) {
			scopeType = PmNotifyScopeType.COMMUNITY.getCode();
			scopeId = cmd.getCommunityId();
		}
		String ownerType = null;
		if (EntityType.QUALITY_TASK.equals(EntityType.fromCode(cmd.getOwnerType()))) {
			ownerType = EntityType.QUALITY_TASK.getCode();
		} else {
			ownerType = EntityType.EQUIPMENT_TASK.getCode();
		}
		// delete data by id ,not add targetId and type
		PmNotifyConfigurations configuration = pmNotifyProvider.findScopePmNotifyConfiguration(cmd.getId(), ownerType, scopeType, scopeId);
		if (configuration != null) {
			configuration.setStatus(PmNotifyConfigurationStatus.INVAILD.getCode());
			pmNotifyProvider.updatePmNotifyConfigurations(configuration);
		}
	}

	@Override
	public List<PmNotifyParamDTO> listPmNotifyParams(ListPmNotifyParamsCommand cmd) {
		Byte scopeType = PmNotifyScopeType.NAMESPACE.getCode();
		Long scopeId = cmd.getNamespaceId().longValue();
		Long targetId = cmd.getTargetId();
		String targetType = cmd.getTargetType();
		if (cmd.getCommunityId() != null && cmd.getCommunityId() != 0L) {
			scopeType = PmNotifyScopeType.COMMUNITY.getCode();
			scopeId = cmd.getCommunityId();
			targetId = null;
			targetType = null;
		}
		String ownerType = null;
		if (EntityType.QUALITY_TASK.equals(EntityType.fromCode(cmd.getOwnerType()))) {
			ownerType = EntityType.QUALITY_TASK.getCode();
		} else {
			ownerType = EntityType.EQUIPMENT_TASK.getCode();
		}
		List<PmNotifyConfigurations> configurations = pmNotifyProvider.listScopePmNotifyConfigurations(ownerType, scopeType, scopeId, targetId,targetType);
		if (configurations != null && configurations.size() > 0) {
			return configurations.stream()
					.map(configuration -> convertPmNotifyConfigurationsToDTO(cmd.getNamespaceId(), configuration))
					.collect(Collectors.toList());
		} else {
			//scopeType是community的情况下 如果拿不到数据，则返回该域空间下的设置 ps以后可以再else一下 域空间的没有返回all的
			//现在换成项目上查看的时候 organizationId 为空，在scope 为namespaceId 的时候 按照管理公司过滤
			if (PmNotifyScopeType.COMMUNITY.equals(PmNotifyScopeType.fromCode(scopeType))) {
				scopeType = PmNotifyScopeType.NAMESPACE.getCode();
				scopeId = cmd.getNamespaceId().longValue();
				List<PmNotifyConfigurations> namespaceConfigurations = pmNotifyProvider.listScopePmNotifyConfigurations(ownerType, scopeType, scopeId, cmd.getTargetId(), cmd.getTargetType());
				if (namespaceConfigurations != null && namespaceConfigurations.size() > 0) {
					return namespaceConfigurations.stream()
							.map(configuration -> convertPmNotifyConfigurationsToDTO(cmd.getNamespaceId(), configuration))
							.collect(Collectors.toList());
				}
			}
		}

		return null;
	}

	@Override
	public void setPmNotifyParams(SetPmNotifyParamsCommand cmd) {
		if (cmd.getParams() != null && cmd.getParams().size() > 0) {
			for (PmNotifyParams params : cmd.getParams()) {
				PmNotifyConfigurations configuration = ConvertHelper.convert(params, PmNotifyConfigurations.class);
				if(EntityType.QUALITY_TASK.equals(EntityType.fromCode(cmd.getOwnerType()))){
					configuration.setOwnerType(EntityType.QUALITY_TASK.getCode());
				}else {
					configuration.setOwnerType(EntityType.EQUIPMENT_TASK.getCode());
				}
				if (params.getCommunityId() != null && params.getCommunityId() != 0L) {
					configuration.setScopeId(params.getCommunityId());
					configuration.setScopeType(PmNotifyScopeType.COMMUNITY.getCode());
				} else {
					configuration.setScopeId(params.getNamespaceId().longValue());
					configuration.setScopeType(PmNotifyScopeType.NAMESPACE.getCode());
				}
				List<PmNotifyReceiver> receivers = params.getReceivers();
				if (receivers != null && receivers.size() > 0) {
					PmNotifyReceiverList receiverList = new PmNotifyReceiverList();
					receiverList.setReceivers(receivers);
					configuration.setReceiverJson(receiverList.toString());
				}
				// this filed means ownerId and ownerType
				configuration.setTargetId(cmd.getTargetId());
				configuration.setTargetType(cmd.getTargetType());
				if (params.getId() == null) {
					pmNotifyProvider.createPmNotifyConfigurations(configuration);
				} else {
					Byte scopeType = PmNotifyScopeType.NAMESPACE.getCode();
					Long scopeId = params.getNamespaceId().longValue();
					if (params.getCommunityId() != null && params.getCommunityId() != 0L) {
						scopeType = PmNotifyScopeType.COMMUNITY.getCode();
						scopeId = params.getCommunityId();
					}
					PmNotifyConfigurations exist = pmNotifyProvider.findScopePmNotifyConfiguration(params.getId(), cmd.getOwnerType(), scopeType, scopeId);
					if (exist != null) {
						configuration.setCreateTime(exist.getCreateTime());
						pmNotifyProvider.updatePmNotifyConfigurations(configuration);
					} else {
						pmNotifyProvider.createPmNotifyConfigurations(configuration);
					}
				}
			}
		}

	}

	@Override
	public void setReviewExpireDays(SetReviewExpireDaysCommand cmd) {
		if (cmd.getReviewExpiredDays() != null) {
			EquipmentInspectionReviewDate reviewDate = new EquipmentInspectionReviewDate();
			reviewDate.setOwnerType(EntityType.EQUIPMENT_TASK.getCode());
			reviewDate.setReviewExpiredDays(cmd.getReviewExpiredDays());
			reviewDate.setTargetId(cmd.getTargetId());
			reviewDate.setTargetType(cmd.getTargetType());
			Long targetId = cmd.getTargetId();
			String targetType = cmd.getTargetType();
			if (cmd.getCommunityId() != null && cmd.getCommunityId() != 0L) {
				reviewDate.setScopeId(cmd.getCommunityId());
				reviewDate.setScopeType(PmNotifyScopeType.COMMUNITY.getCode());
				targetId = null;
				targetType = null;
			} else {
				reviewDate.setScopeId(cmd.getNamespaceId().longValue());
				reviewDate.setScopeType(PmNotifyScopeType.NAMESPACE.getCode());
			}
			equipmentProvider.deleteReviewExpireDaysByScope(reviewDate.getScopeType(), reviewDate.getScopeId(),targetId,targetType);
			if (cmd.getId() == null) {
				reviewDate.setStatus(PmNotifyConfigurationStatus.VAILD.getCode());
				equipmentProvider.createReviewExpireDays(reviewDate);
			} else {
				reviewDate.setId(cmd.getId());
				reviewDate.setStatus(PmNotifyConfigurationStatus.VAILD.getCode());
				equipmentProvider.updateReviewExpireDays(reviewDate);
			}
		}
	}

	@Override
	public void deleteReviewExpireDays(SetReviewExpireDaysCommand cmd) {

		if (cmd.getId() == null) {
			return;
		}
		Byte scopeType = PmNotifyScopeType.NAMESPACE.getCode();
		Long scopeId = cmd.getNamespaceId().longValue();
		if (cmd.getCommunityId() != null && cmd.getCommunityId() != 0L) {
			scopeType = PmNotifyScopeType.COMMUNITY.getCode();
			scopeId = cmd.getCommunityId();
		}
		EquipmentInspectionReviewDate reviewDate = equipmentProvider.getEquipmentInspectiomExpireDaysById(cmd.getId());
		if (reviewDate != null) {
			if (!reviewDate.getScopeId().equals(scopeId) && !reviewDate.getScopeType().equals(scopeType)) {
				//项目自定义删除
				reviewDate.setReferId(reviewDate.getId());
				reviewDate.setScopeId(scopeId);
				reviewDate.setScopeType(scopeType);
				reviewDate.setStatus(PmNotifyConfigurationStatus.VAILD.getCode());
				equipmentProvider.createReviewExpireDays(reviewDate);
			} else {
				reviewDate.setStatus(PmNotifyConfigurationStatus.INVAILD.getCode());
				equipmentProvider.updateReviewExpireDays(reviewDate);
			}
			if (PmNotifyScopeType.NAMESPACE.equals(PmNotifyScopeType.fromCode(scopeType))) {
				equipmentProvider.deleteReviewExpireDaysByReferId(reviewDate.getId());
			}
		}
	}

	@Override
	public EquipmentInspectionReviewDateDTO listReviewExpireDays(SetReviewExpireDaysCommand cmd) {
		Byte scopeType = PmNotifyScopeType.NAMESPACE.getCode();
		Long scopeId = cmd.getNamespaceId().longValue();
		Long targetId = cmd.getTargetId();
		String targetType = cmd.getTargetType();
		if (cmd.getCommunityId() != null && cmd.getCommunityId() != 0L) {
			scopeType = PmNotifyScopeType.COMMUNITY.getCode();
			scopeId = cmd.getCommunityId();
			//only namespace scope will use organization id
			cmd.setTargetId(null);
			cmd.setTargetType(null);
		}
		List<EquipmentInspectionReviewDate> reviewDate = equipmentProvider.getEquipmentInspectiomExpireDays(scopeId, scopeType,cmd.getTargetId(),cmd.getTargetType());
		List<Long> referId = new ArrayList<>();
		if (reviewDate != null && reviewDate.size() > 0) {
			reviewDate.forEach((r) -> {
				if (r.getReferId() != null && r.getReferId() != 0L) {
					referId.add(r.getReferId());
				}
			});
			// make community has their own configurations
			reviewDate.removeIf((r) -> r.getReferId() != 0L && r.getReferId() != null);
		}
		if (reviewDate != null && reviewDate.size() > 0) {
			return ConvertHelper.convert(reviewDate.get(0), EquipmentInspectionReviewDateDTO.class);
		} else {
			//scopeType是community的情况下 如果拿不到数据，则返回该域空间下的管理公司的设置
			if (PmNotifyScopeType.COMMUNITY.equals(PmNotifyScopeType.fromCode(scopeType))) {
				scopeType = PmNotifyScopeType.NAMESPACE.getCode();
				scopeId = cmd.getNamespaceId().longValue();
				reviewDate = equipmentProvider.getEquipmentInspectiomExpireDays(scopeId, scopeType,targetId,targetType);
				if (reviewDate != null && reviewDate.size() > 0) {
					if (!referId.contains(reviewDate.get(0).getId())) {
						return ConvertHelper.convert(reviewDate.get(0), EquipmentInspectionReviewDateDTO.class);
					}
				}
			}
		}

		return null;
	}

	@Override
	public Set<Long> getTaskGroupUsers(Long taskId, byte groupType) {
		//这里从原来标准和人员组关系变更为计划和人员组关系  -- V3.0.3
		EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(taskId);
		//List<EquipmentInspectionStandardGroupMap> maps = equipmentProvider.listEquipmentInspectionStandardGroupMapByStandardIdAndGroupType(task.getStandardId(), groupType);
		List<EquipmentInspectionPlanGroupMap> maps = equipmentProvider.listEquipmentInspectionPlanGroupMapByPlanIdAndGroupType(task.getPlanId(), groupType);
		if (maps != null && maps.size() > 0) {
			Set<Long> userIds = new HashSet<>();
			maps.forEach(map -> {
				if (map.getPositionId() == null || map.getPositionId() == 0L) {
					List<OrganizationMember> members = organizationProvider.listOrganizationMembers(map.getGroupId(), null);
					if (members != null) {
						for (OrganizationMember member : members) {
							userIds.add(member.getTargetId());
						}
					}
				} else {
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

	@Override
	public FieldItemDTO findScopeFieldItemByFieldItemId(findScopeFieldItemCommand cmd) {
		return ConvertHelper.convert(fieldProvider.findScopeFieldItemByBusinessValue(cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getCommunityId(), cmd.getModuleName(), cmd.getFieldId(), cmd.getBusinessValue()), FieldItemDTO.class);
	}


	/*@Override
	public void distributeTemplates() {
		//对于模板新增项目类型  需要把之前不同域空间模板默认分发到所有的项目
		List<Integer> allNameSpaces = equipmentProvider.listDistinctNameSpace();
		for (Integer namespaceId : allNameSpaces) {
			//获取当前域空间所有的项目
			List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
			//获取当前域空间所有的巡检模板
			List<EquipmentInspectionTemplates> templates = equipmentProvider.listInspectionTemplates(namespaceId, null, null);
			for (EquipmentInspectionTemplates template : templates) {
				for (Community community : communities) {
					EquipmentModelCommunityMap map = new EquipmentModelCommunityMap();
					map.setModelId(template.getId());
					map.setModelType(EquipmentModelType.TEMPLATE.getCode());
					map.setTargetId(community.getId());
					map.setTargetType("community");
					equipmentProvider.createEquipmentModelCommunityMap(map);
				}
			}
		}
	}*/

	@Override
	public EquipmentInspectionPlanDTO createEquipmentsInspectionPlan(UpdateEquipmentPlanCommand cmd) {
		//check auth
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_PLAN_CREATE, cmd.getTargetId());

		EquipmentInspectionPlans plan = ConvertHelper.convert(cmd, EquipmentInspectionPlans.class);
		User user = UserContext.current().getUser();
		plan.setCreatorUid(user.getId());
		plan.setNamespaceId(cmd.getNamespaceId());
		if (cmd.getEquipmentStandardRelations() == null || cmd.getEquipmentStandardRelations().size() == 0
				|| cmd.getRepeatSettings() == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					RepeatServiceErrorCode.ERROR_REPEAT_SETTING_NOT_EXIST,
					"计划周期为空");
		} else {
			plan.setStatus(EquipmentPlanStatus.WATTING_FOR_APPOVING.getCode());
		}
		//创建巡检计划的执行周期
		RepeatSettings repeatSettings = ConvertHelper.convert(cmd.getRepeatSettings(), RepeatSettings.class);
		if (cmd.getRepeatSettings().getStartDate() != null)
			repeatSettings.setStartDate(new Date(cmd.getRepeatSettings().getStartDate()));
		if (cmd.getRepeatSettings().getEndDate() != null)
			repeatSettings.setEndDate(new Date(cmd.getRepeatSettings().getEndDate()));

		repeatSettings.setCreatorUid(user.getId());
		repeatService.createRepeatSettings(repeatSettings);
		plan.setRepeatSettingId(repeatSettings.getId());

		//创建巡检计划
		EquipmentInspectionPlans createdPlan = equipmentProvider.createEquipmentInspectionPlans(plan);
		equipmentPlanSearcher.feedDoc(plan);
		//创建计划巡检对象标准关联
		List<EquipmentStandardRelationDTO> equipmentStandardRelation = new ArrayList<>();
		if (cmd.getEquipmentStandardRelations() != null && cmd.getEquipmentStandardRelations().size() > 0) {
			for (EquipmentStandardRelationDTO relation : cmd.getEquipmentStandardRelations()) {
				EquipmentInspectionEquipmentPlanMap equipmentPlanMap = new EquipmentInspectionEquipmentPlanMap();
				equipmentPlanMap.setDefaultOrder(relation.getOrder());
				equipmentPlanMap.setNamespaceId(cmd.getNamespaceId());
				equipmentPlanMap.setOwnerId(cmd.getOwnerId());
				equipmentPlanMap.setOwnerType(cmd.getOwnerType());
				equipmentPlanMap.setPlanId(createdPlan.getId());
				equipmentPlanMap.setTargetId(cmd.getTargetId());
				equipmentPlanMap.setTargetType(cmd.getTargetType());
				equipmentPlanMap.setStandardId(relation.getStandardId());
				equipmentPlanMap.setEquipmentId(relation.getEquipmentId());

				equipmentProvider.createEquipmentPlanMaps(equipmentPlanMap);
				equipmentStandardRelation.add(relation);
			}
		}
		//createdPlan.setEquipmentStandardRelations(equipmentStandardRelation);
		//巡检计划增加审批和执行人员
		List<StandardGroupDTO> groupList = cmd.getGroupList();
		equipmentProvider.deleteEquipmentInspectionPlanGroupMapByPlanId(plan.getId());
		processPlanGroups(groupList, createdPlan);

		EquipmentInspectionPlanDTO planDTO = ConvertHelper.convert(createdPlan, EquipmentInspectionPlanDTO.class);
		planDTO.setEquipmentStandardRelations(equipmentStandardRelation);
		return planDTO;
	}

	private void processPlanGroups(List<StandardGroupDTO> groupList, EquipmentInspectionPlans plan) {
		List<EquipmentInspectionPlanGroupMap> executiveGroup = null;
		List<EquipmentInspectionPlanGroupMap> reviewGroup = null;

		if (groupList != null && groupList.size() > 0) {
			executiveGroup = new ArrayList<>();
			reviewGroup = new ArrayList<>();

			for (StandardGroupDTO group : groupList) {
				EquipmentInspectionPlanGroupMap map = new EquipmentInspectionPlanGroupMap();
				map.setPlanId(plan.getId());
				map.setGroupType(group.getGroupType());
				map.setGroupId(group.getGroupId());
				map.setPositionId(group.getPositionId());
				equipmentProvider.createEquipmentInspectionPlanGroupMap(map);
				if (QualityGroupType.EXECUTIVE_GROUP.equals(QualityGroupType.fromStatus(map.getGroupType()))) {
					executiveGroup.add(map);
				}
				if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(map.getGroupType()))) {
					reviewGroup.add(map);
				}
			}
		}
		plan.setExecutiveGroup(executiveGroup);
		plan.setReviewGroup(reviewGroup);
	}

	@Override
	public EquipmentInspectionPlanDTO updateEquipmentInspectionPlan(UpdateEquipmentPlanCommand cmd) {

		//check auth
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_PLAN_UPDATE, cmd.getTargetId());

		EquipmentInspectionPlans exist = verifyEquipmentInspectionPlan(cmd.getId());
		EquipmentInspectionPlans updatePlan = null;

		EquipmentInspectionPlans plan = ConvertHelper.convert(cmd, EquipmentInspectionPlans.class);
		if (cmd.getEquipmentStandardRelations() == null || cmd.getEquipmentStandardRelations().size() == 0
				|| cmd.getRepeatSettings() == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE, RepeatServiceErrorCode.ERROR_REPEAT_SETTING_NOT_EXIST,
					"执行周期或者关联设备为空!");
		} else {
			plan.setStatus(EquipmentPlanStatus.WATTING_FOR_APPOVING.getCode());
		}
		plan.setCreateTime(exist.getCreateTime());
		plan.setCreatorUid(exist.getCreatorUid());
		plan.setNamespaceId(exist.getNamespaceId());

		//创建巡检计划的执行周期
		repeatService.deleteRepeatSettingsById(cmd.getRepeatSettings().getId());
		RepeatSettings repeatSettings = ConvertHelper.convert(cmd.getRepeatSettings(), RepeatSettings.class);
		if (cmd.getRepeatSettings().getStartDate() != null)
			repeatSettings.setStartDate(new Date(cmd.getRepeatSettings().getStartDate()));
		if (cmd.getRepeatSettings().getEndDate() != null)
			repeatSettings.setEndDate(new Date(cmd.getRepeatSettings().getEndDate()));

		repeatSettings.setCreatorUid(UserContext.currentUserId());
		repeatService.createRepeatSettings(repeatSettings);
		plan.setRepeatSettingId(repeatSettings.getId());
		//plan.setPlanMainId(exist.getPlanMainId());

		equipmentProvider.updateEquipmentInspectionPlan(plan);
		updatePlan = plan;
		//创建计划巡检对象标准关联
		List<EquipmentStandardRelationDTO> equipmentStandardRelation = new ArrayList<>();
		equipmentProvider.deleteEquipmentInspectionPlanMap(plan.getId());
		for (EquipmentStandardRelationDTO relation : cmd.getEquipmentStandardRelations()) {
			EquipmentInspectionEquipmentPlanMap equipmentPlanMap = new EquipmentInspectionEquipmentPlanMap();
			equipmentPlanMap.setDefaultOrder(relation.getOrder());
			equipmentPlanMap.setNamespaceId(cmd.getNamespaceId());
			equipmentPlanMap.setOwnerId(cmd.getOwnerId());
			equipmentPlanMap.setOwnerType(cmd.getOwnerType());
			equipmentPlanMap.setPlanId(updatePlan.getId());
			equipmentPlanMap.setTargetId(cmd.getTargetId());
			equipmentPlanMap.setTargetType(cmd.getTargetType());
			equipmentPlanMap.setStandardId(relation.getStandardId());
			equipmentPlanMap.setEquipmentId(relation.getEquipmentId());

			equipmentProvider.createEquipmentPlanMaps(equipmentPlanMap);
			equipmentStandardRelation.add(relation);
		}

		equipmentProvider.deleteEquipmentInspectionPlanGroupMapByPlanId(plan.getId());
		//巡检计划增加审批和执行人员
		processPlanGroups(cmd.getGroupList(), plan);
		//inActive 所有关联任务当前时间节点之前任务继续   状态变成待审核
		//inActiveTaskByPlanId(plan.getId());
		//updatePlan.setEquipmentStandardRelations(equipmentStandardRelation);

		EquipmentInspectionPlanDTO planDTO = ConvertHelper.convert(updatePlan, EquipmentInspectionPlanDTO.class);
		planDTO.setEquipmentStandardRelations(equipmentStandardRelation);
		return planDTO;
	}

	private void inActiveTaskByPlanId(Long planId) {

		equipmentProvider.updateEquipmentTaskByPlanId(planId);
		int pageSize = 200;
		CrossShardListingLocator locator = new CrossShardListingLocator();
		unscheduleRelatedJobAndStatus(planId);
		for (; ; ) {
			List<EquipmentInspectionTasks> tasks = equipmentProvider.listTasksByPlanId(planId, locator, pageSize);
			LOGGER.debug("inActiveTaskByPlanId tasks size={}", tasks.size());
			if (tasks.size() > 0) {
				for (EquipmentInspectionTasks task : tasks) {
					equipmentTasksSearcher.deleteById(task.getId());
				}
			}

			if (locator.getAnchor() == null) {
				break;
			}
		}

	}

	private void unscheduleRelatedJobAndStatus(Long planId) {
		// unschedule related task notify job
		int pageSize = 200;
		CrossShardListingLocator locator = new CrossShardListingLocator();
		try {
			for (; ; ) {
                List<Long> recordIds = equipmentProvider.listNotifyRecordByPlanId(planId, locator, pageSize);
                if (recordIds != null && recordIds.size() > 0) {
                    recordIds.forEach((r) -> {
                        scheduleProvider.unscheduleJob(queueDelay + r.toString());
                        scheduleProvider.unscheduleJob(queueNoDelay + r.toString());
                        // invalidate notify records in case core reboot and restore job from it on exception
						pmNotifyProvider.invalidateNotifyRecord(recordIds);

                    });
                }
                if (locator.getAnchor() == null) {
                    break;
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}

	}

	private EquipmentInspectionPlans verifyEquipmentInspectionPlan(Long id) {

		EquipmentInspectionPlans plan = equipmentProvider.getEquipmmentInspectionPlanById(id);

		if (EquipmentPlanStatus.INACTIVE.equals(EquipmentPlanStatus.fromStatus(plan.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_PLAN_ALREADY_DELETED,
					"巡检计划已经删除");
		}
		return plan;
	}

	@Override
	public EquipmentInspectionPlanDTO getEquipmmentInspectionPlanById(DeleteEquipmentPlanCommand cmd) {

		EquipmentInspectionPlans equipmentInspectionPlan = equipmentProvider.getEquipmmentInspectionPlanById(cmd.getId());
		if (equipmentInspectionPlan == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_PLAN_NOT_EXIST, "计划不存在");
		}

		//填充计划的执行组审批组
		equipmentProvider.populatePlanGroups(equipmentInspectionPlan);
		//填充计划的执行周期
		equipmentInspectionPlan.setRepeatSettings(repeatProvider.findRepeatSettingById(equipmentInspectionPlan.getRepeatSettingId()));
		//这里填充relation关系表
		return convertPlanToDto(equipmentInspectionPlan);
	}

	private EquipmentInspectionPlanDTO convertPlanToDto(EquipmentInspectionPlans equipmentInspectionPlan) {
		EquipmentInspectionPlanDTO planDTO = ConvertHelper.convert(equipmentInspectionPlan, EquipmentInspectionPlanDTO.class);
		RepeatSettingsDTO repeatSettingsDTO = ConvertHelper.convert(equipmentInspectionPlan.getRepeatSettings(), RepeatSettingsDTO.class);
		planDTO.setRepeatSettings(repeatSettingsDTO);

		//填充巡检计划相关的巡检对象(需排序)
		processEquipmentInspectionObjectsByPlanId(equipmentInspectionPlan.getId(), planDTO);

		List<StandardGroupDTO> executiveGroup = new ArrayList<>();
		List<StandardGroupDTO> reviewGroup = new ArrayList<>();
		if (equipmentInspectionPlan.getExecutiveGroup() != null) {
			executiveGroup = equipmentInspectionPlan.getExecutiveGroup().stream().map((r) -> {

				StandardGroupDTO dto = ConvertHelper.convert(r, StandardGroupDTO.class);
				Organization group = organizationProvider.findOrganizationById(r.getGroupId());

				if (group != null) {
					dto.setGroupName(group.getName());
				}
				if (r.getPositionId() != null && r.getPositionId() != 0) {
					OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(r.getPositionId());
					if (position != null) {
						if (dto.getGroupName() != null) {
							dto.setGroupName(dto.getGroupName() + "-" + position.getName());
						} else {
							dto.setGroupName(position.getName());
						}
					} else {
						Organization organization = organizationProvider.findOrganizationById(r.getPositionId());
						String positionName = null;
						if (organization != null) {
							positionName = organization.getName();
						}
						if (dto.getGroupName() != null) {
							dto.setGroupName(dto.getGroupName() + "-" + positionName);
						} else {
							dto.setGroupName(positionName);
						}
					}
				}
				return dto;
			}).collect(Collectors.toList());
		}

		if (equipmentInspectionPlan.getReviewGroup() != null) {
			reviewGroup = equipmentInspectionPlan.getReviewGroup().stream().map((r) -> {

				StandardGroupDTO dto = ConvertHelper.convert(r, StandardGroupDTO.class);
				Organization group = organizationProvider.findOrganizationById(r.getGroupId());
				OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(r.getPositionId());
				if (group != null) {
					dto.setGroupName(group.getName());
				}

				if (position != null) {
					if (dto.getGroupName() != null) {
						dto.setGroupName(dto.getGroupName() + "-" + position.getName());
					} else {
						dto.setGroupName(position.getName());
					}
				} else if (r.getPositionId() != null && r.getPositionId() != 0) {
					Organization organization = organizationProvider.findOrganizationById(r.getPositionId());
					String positionName = null;
					if (organization != null) {
						positionName = organization.getName();
					}
					if (dto.getGroupName() != null) {
						dto.setGroupName(dto.getGroupName() + "-" + positionName);
					} else {
						dto.setGroupName(positionName);
					}
				}
				return dto;
			}).collect(Collectors.toList());
		}

		planDTO.setReviewGroup(reviewGroup);
		planDTO.setExecutiveGroup(executiveGroup);
		return planDTO;
	}

	@Override
	public void deleteEquipmentInspectionPlan(DeleteEquipmentPlanCommand cmd) {
		//check auth
		checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_PLAN_DELETE, cmd.getTargetId());

		EquipmentInspectionPlans exist = equipmentProvider.getEquipmmentInspectionPlanById(cmd.getId());
		exist.setStatus(EquipmentPlanStatus.INACTIVE.getCode());
		exist.setDeleterUid(UserContext.currentUserId());
		exist.setDeleterUid(UserContext.currentUserId());
		//根据id删除巡检计划 置状态
		equipmentProvider.updateEquipmentInspectionPlan(exist);
		//刪除巡检计划巡检对象关联
		equipmentProvider.deleteEquipmentInspectionPlanMap(cmd.getId());
		// 删除巡检计划关联审批检查组
		equipmentProvider.deleteEquipmentInspectionPlanGroupMapByPlanId(exist.getId());
		//删除repeatSetting  不删也可
		//repeatService.deleteRepeatSettingsById(exist.getRepeatSettingId());
		//删除所有此计划产生的任务
		ExecutorUtil.submit(()-> inActiveTaskByPlanId(cmd.getId()));
//		inActiveTaskByPlanId(cmd.getId());
		equipmentPlanSearcher.deleteById(cmd.getId());
	}

	@Override
	public void createTaskByPlan(DeleteEquipmentPlanCommand cmd) {
		EquipmentInspectionPlans plan = equipmentProvider.getEquipmmentInspectionPlanById(cmd.getId());
		if (plan == null || plan.getStatus() == null
				|| EquipmentPlanStatus.INACTIVE.equals(EquipmentPlanStatus.fromStatus(plan.getStatus()))) {
			LOGGER.info("EquipmentInspectionScheduleJob standard is not exist or active! planId = " + cmd.getId());
		} else {
			boolean isRepeat = repeatService.isRepeatSettingActive(plan.getRepeatSettingId());
			LOGGER.info("EquipmentInspectionScheduleJob: plan id = " + plan.getId()
					+ "repeat setting id = " + plan.getRepeatSettingId() + "is repeat setting active: " + isRepeat);
			if (isRepeat) {
				this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_EQUIPMENT_TASK.getCode()).tryEnter(() -> {
					createEquipmentTaskByPlan(plan);
				});
			}
		}
	}

	@Override
	public void createEquipmentTaskByPlan(EquipmentInspectionPlans plan) {
		//根据巡检计划创建巡检任务
		EquipmentInspectionTasks task = new EquipmentInspectionTasks();
		task.setOwnerType(plan.getOwnerType());
		task.setOwnerId(plan.getOwnerId());
		task.setNamespaceId(plan.getNamespaceId());
		task.setTargetId(plan.getTargetId());
		task.setTargetType(plan.getTargetType());
		task.setInspectionCategoryId(plan.getInspectionCategoryId());
		task.setTaskNumber(plan.getPlanNumber());
		task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
		task.setResult(EquipmentTaskResult.NONE.getCode());
		task.setReviewResult(ReviewResult.NONE.getCode());
		task.setPlanId(plan.getId());
		plan.setRepeatSettings(repeatService.findRepeatSettingById(plan.getRepeatSettingId()));
		List<TimeRangeDTO> timeRanges = repeatService.analyzeTimeRange(plan.getRepeatSettings().getTimeRanges());
		if (timeRanges != null && timeRanges.size() > 0) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("creatTaskByPlan, timeRanges = " + timeRanges);
			}
			long current = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String day = sdf.format(current);
			int i = 0;
			for (TimeRangeDTO timeRange : timeRanges) {
				i++;
				String duration = timeRange.getDuration();
				String start = timeRange.getStartTime();
				String str = day + " " + start;
				Timestamp startTime = strToTimestamp(str);
				Timestamp expiredTime = repeatService.getEndTimeByAnalyzeDuration(startTime, duration);
				task.setExecutiveStartTime(startTime);
				task.setExecutiveExpireTime(expiredTime);
				long now = System.currentTimeMillis();
				//计划名称+巡检/保养+当日日期6位(年的后两位+月份+天)+两位序号（系统从01开始生成）by xiongying20170927
				if (i < 10) {
					String taskName = plan.getName() + plan.getPlanType() +
							timestampToStr(new Timestamp(now)).substring(2) + "0" + i;
					task.setTaskName(taskName);
				} else {
					String taskName = plan.getName() + plan.getPlanType() +
							timestampToStr(new Timestamp(now)).substring(2) + i;
					task.setTaskName(taskName);
				}
				equipmentProvider.creatEquipmentTask(task);
				equipmentTasksSearcher.feedDoc(task);

				// 启动提醒 此处需要集成工作流 从执行节点拿到执行人员  (暂时取消工作流)
				ListPmNotifyParamsCommand command = new ListPmNotifyParamsCommand();
				command.setCommunityId(task.getTargetId());
				command.setNamespaceId(task.getNamespaceId());
				command.setOwnerType(EntityType.EQUIPMENT_TASK.getCode());
				OrganizationDTO organization  = getAuthOrgByProjectIdAndModuleId(task.getTargetId(), task.getNamespaceId(), EquipmentConstant.EQUIPMENT_MODULE);
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
							record.setOwnerType(EntityType.EQUIPMENT_TASK.getCode());
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
	}

	private void processEquipmentInspectionObjectsByPlanId(Long planId, EquipmentInspectionPlanDTO planDTO) {

		List<EquipmentInspectionEquipmentPlanMap> planMaps = equipmentProvider.getEquipmentInspectionPlanMap(planId);
		List<EquipmentStandardRelationDTO> relationDTOS = new ArrayList<>();
		if (planMaps != null && planMaps.size() > 0) {
			for (EquipmentInspectionEquipmentPlanMap map : planMaps) {
				EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getEquipmentId());
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(map.getStandardId());

				EquipmentStandardRelationDTO relations = new EquipmentStandardRelationDTO();
				//根据id查询计划的详情巡检对象栏  只需要如下几个字段
				if (equipment != null) {
					relations.setEquipmentName(equipment.getName());
					relations.setEquipmentId(equipment.getId());
					relations.setLocation(equipment.getLocation());
					relations.setQrCodeFlag(equipment.getQrCodeFlag());
					relations.setQrCodeToken(equipment.getQrCodeToken());
					relations.setLatitude(equipment.getLatitude());
					relations.setLongitude(equipment.getLongitude());
					relations.setVerifyDistance(configProvider.getIntValue("equipment.verify.distance", 100));
					relations.setSequenceNo(equipment.getSequenceNo());
//					equipmentStandardMaps = equipmentProvider.
//							findEquipmentStandardMap(standard.getId(), equipment.getId(),
//									InspectionStandardMapTargetType.EQUIPMENT.getCode());
				}
				if (standard != null) {
					relations.setStandardName(standard.getName());
					relations.setStandardId(standard.getId());
					relations.setRepeatType(standard.getRepeatType());
				}

				relations.setOrder(map.getDefaultOrder());
				relations.setPlanId(planId);
				relations.setId(map.getId());
				relationDTOS.add(relations);
			}
			//sort equipmentStandardRelations
			relationDTOS.sort(Comparator.comparingLong(EquipmentStandardRelationDTO::getOrder));
		}
		//process  EquipmentStandardRelation to plan
		planDTO.setEquipmentStandardRelations(relationDTOS);

	}

	@Override
	public void exportInspectionPlans(SearchEquipmentInspectionPlansCommand cmd, HttpServletResponse response) {
		List<EquipmentInspectionPlanDTO> dtos = equipmentPlanSearcher.query(cmd).getEquipmentInspectionPlans();
		List<EquipmentInspectionPlanDTO> plans = dtos.stream().map(this::toExportPlans).collect(Collectors.toList());
		if (plans != null && plans.size() > 0) {
			String fileName = String.format("巡检计划%s", DateUtil.dateToStr(new java.util.Date(), DateUtil.DATE_TIME_NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(response, fileName, "巡检计划列表");

			List<String> propertyNames = Arrays.asList("targetName", "planNumber", "planType", "name", "status");
			List<String> titleNames = Arrays.asList("项目名称", "计划编号", "计划类型", "状态");
			List<Integer> titleSizes = Arrays.asList(20, 20, 20, 20);
			excelUtils.setNeedSequenceColumn(true);

			excelUtils.writeExcel(propertyNames, titleNames, titleSizes, plans);
		} else {
			throw RuntimeErrorException.errorWith(ParkingLocalStringCode.SCOPE_STRING,
					Integer.parseInt(ParkingLocalStringCode.NO_DATA), "no data");
		}
	}

	private EquipmentInspectionPlanDTO toExportPlans(EquipmentInspectionPlanDTO plan) {
		StandardType planType = StandardType.fromStatus(plan.getPlanType());
		if (planType != null)
			plan.setStringPlanType(planType.getName());
		EquipmentPlanStatus statuType = EquipmentPlanStatus.fromStatus(plan.getStatus());
		if (statuType != null)
			plan.setStringStatus(statuType.getName());
		return plan;
	}

	@Override
	public List<EquipmentStandardRelationDTO> listEquipmentStandardRelationsByTaskId(ListTaskByIdCommand cmd) {
		EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(cmd.getTaskId());
		if (task != null) {
			if (task.getPlanId() != null && task.getPlanId() != 0L) {
				DeleteEquipmentPlanCommand delcmd = new DeleteEquipmentPlanCommand();
				delcmd.setOwnerType(cmd.getOwnerType());
				delcmd.setId(task.getPlanId());
				delcmd.setOwnerId(cmd.getOwnerId());
				EquipmentInspectionPlanDTO planDTO = getEquipmmentInspectionPlanById(delcmd);
				if (planDTO != null)
					return planDTO.getEquipmentStandardRelations();
			} else {
				//兼容旧任务
				List<EquipmentStandardRelationDTO> equipmentRelations = new ArrayList<>();
				EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(task.getEquipmentId());
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
				if (equipment != null && standard != null) {
					EquipmentStandardRelationDTO relationDTO = new EquipmentStandardRelationDTO();
					relationDTO.setQrCodeFlag(equipment.getQrCodeFlag());
					relationDTO.setLocation(equipment.getLocation());
					relationDTO.setEquipmentName(equipment.getName());
					relationDTO.setStandardId(standard.getId());
					equipmentRelations.add(relationDTO);
				}
				return equipmentRelations;
			}
		}
		return null;
	}

	@Override
	public void syncStandardToEqiupmentPlan() {
		EquipmentInspectionEquipments equipment = new EquipmentInspectionEquipments();
		EquipmentInspectionStandards standards = new EquipmentInspectionStandards();
		List<EquipmentStandardMap> maps = new ArrayList<>();
		List<EquipmentInspectionStandardGroupMap> groupMaps = new ArrayList<>();

		maps = equipmentProvider.listAllActiveEquipmentStandardMap();
		for (EquipmentStandardMap map : maps) {
			equipment = equipmentProvider.findEquipmentById(map.getTargetId());
			standards = equipmentProvider.findStandardById(map.getStandardId());
			EquipmentInspectionPlans plan = new EquipmentInspectionPlans();
			if (equipment != null) {
				plan.setNamespaceId(equipment.getNamespaceId());
				plan.setName(equipment.getName());
				plan.setTargetId(equipment.getTargetId());
				plan.setTargetType(equipment.getTargetType());
				plan.setPlanNumber(equipment.getCustomNumber());
			}
			if (standards != null) {
				if (EquipmentReviewStatus.REVIEWED.equals(EquipmentReviewStatus.fromStatus(map.getReviewStatus()))
						&& ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(map.getReviewResult()))) {
					plan.setStatus(EquipmentPlanStatus.QUALIFIED.getCode());
				} else {
					plan.setStatus(EquipmentPlanStatus.WATTING_FOR_APPOVING.getCode());
				}
				plan.setPlanType(standards.getStandardType());
				plan.setRepeatSettingId(standards.getRepeatSettingId());
				plan.setInspectionCategoryId(standards.getInspectionCategoryId());
				plan.setOwnerType(standards.getOwnerType());
				plan.setOwnerId(standards.getOwnerId());
			}
			equipmentProvider.createEquipmentInspectionPlans(plan);
			EquipmentInspectionEquipmentPlanMap planMap = new EquipmentInspectionEquipmentPlanMap();
			if (equipment != null) {
				planMap.setEquipmentId(equipment.getId());
				planMap.setTargetId(equipment.getTargetId());
				planMap.setTargetType(equipment.getTargetType());
			}
			planMap.setPlanId(plan.getId());
			if (standards != null) {
				planMap.setStandardId(standards.getId());
				planMap.setNamespaceId(standards.getNamespaceId());
				planMap.setOwnerId(standards.getOwnerId());
				planMap.setOwnerType(standards.getOwnerType());
			}
			equipmentProvider.createEquipmentPlanMaps(planMap);

			if (standards != null)
				groupMaps = equipmentProvider.listEquipmentInspectionStandardGroupMapByStandardId(standards.getId());
			EquipmentInspectionPlanGroupMap planGroupMap = new EquipmentInspectionPlanGroupMap();
			groupMaps.forEach((m) -> {
				planGroupMap.setPlanId(plan.getId());
				planGroupMap.setCreateTime(m.getCreateTime());
				planGroupMap.setGroupId(m.getGroupId());
				planGroupMap.setGroupType(m.getGroupType());
				planGroupMap.setPositionId(m.getPositionId());
				equipmentProvider.createEquipmentInspectionPlanGroupMap(planGroupMap);
			});
		}
		LOGGER.info("sync for standard_equipment_map to eqiupmentInspectionPlan task ok.....");
		transferTasksPlanIds();
	}

	private void transferTasksPlanIds() {
		List<EquipmentInspectionEquipmentPlanMap> planMaps = equipmentProvider.listEquipmentPlanMaps();
		if (planMaps != null && planMaps.size() > 0) {
			planMaps.forEach((map) -> equipmentProvider.transferPlanIdForTasks(map.getEquipmentId(), map.getStandardId(), map.getPlanId()));
		}
		//update tasks status
		equipmentProvider.batchUpdateUnusedTaskStatus();

	}

	@Override
	public EquipmentTaskOfflineResponse listEquipmentTasksDetails(ListEquipmentTasksCommand cmd) {
		//当前登录人的任务信息
		ListEquipmentTasksResponse response = listEquipmentTasks(cmd);
		EquipmentTaskOfflineResponse offlineResponse = new EquipmentTaskOfflineResponse();
		//设备标准关联表 设备id 标准id
		List<EquipmentStandardRelationDTO> equipments = new ArrayList<>();
		//巡检item表包含standardId
		List<InspectionItemDTO> items = new ArrayList<>();

		obtainTodayTaskCount(cmd, response, offlineResponse);
		offlineResponse.setNextPageAnchor(response.getNextPageAnchor());

		List<EquipmentTaskDTO> tasks = response.getTasks();
		tasks.forEach((task) -> {
			setTaskMaxUpdatedTime(task);
			EquipmentInspectionPlans plan = equipmentProvider.getEquipmmentInspectionPlanById(task.getPlanId());
			if (plan != null) {
				EquipmentInspectionPlanDTO planDTO = ConvertHelper.convert(plan, EquipmentInspectionPlanDTO.class);
				//填充巡检计划相关的巡检对象(需排序)
				processEquipmentInspectionObjectsByPlanId(task.getPlanId(), planDTO);
				if (planDTO.getEquipmentStandardRelations() != null)
					equipments.addAll(planDTO.getEquipmentStandardRelations());
			} else {
				//兼容旧任务 可以去掉了
				EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(task.getEquipmentId());
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
				if (equipment != null && standard != null) {
					EquipmentStandardRelationDTO relationDTO = new EquipmentStandardRelationDTO();
					relationDTO.setQrCodeFlag(equipment.getQrCodeFlag());
					relationDTO.setLatitude(equipment.getLatitude());
					relationDTO.setLongitude(equipment.getLongitude());
					relationDTO.setVerifyDistance(configProvider.getIntValue("equipment.verify.distance", 100));
					relationDTO.setLocation(equipment.getLocation());
					relationDTO.setEquipmentName(equipment.getName());
					relationDTO.setStandardId(standard.getId());
					List<EquipmentStandardMap> equipmentStandardMaps = equipmentProvider.findEquipmentStandardMap(standard.getId(), equipment.getId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
					if (equipmentStandardMaps != null) {
						relationDTO.setId(equipmentStandardMaps.get(0).getId());
					}
					equipments.add(relationDTO);
				}
			}
		});

		offlineResponse.setOfflineTasks(tasks);
		offlineResponse.setEquipments(equipments);
		ListParametersByStandardIdCommand listParametersByStandardIdCommand = new ListParametersByStandardIdCommand();
		//remove duplicated
		Set<Long> standardIds = new HashSet<>();
		equipments.forEach((relation) -> standardIds.add(relation.getStandardId()));
		standardIds.forEach((k) -> {
			listParametersByStandardIdCommand.setStandardId(k);
			List<InspectionItemDTO> itemDTOS = listParametersByStandardId(listParametersByStandardIdCommand);
			if (itemDTOS != null && itemDTOS.size() > 0) {
				items.addAll(itemDTOS.stream().sorted(Comparator.comparing(InspectionItemDTO::getDefaultOrder)).collect(Collectors.toList()));
			}
		});

		offlineResponse.setItems(new ArrayList<>(items));
		syncGroupOfflineData(offlineResponse, cmd);
		syncRepairCategoryData(offlineResponse, cmd);
		return offlineResponse;
	}

	private void obtainTodayTaskCount(ListEquipmentTasksCommand cmd, ListEquipmentTasksResponse response, EquipmentTaskOfflineResponse offlineResponse) {
		OfflineTaskCountStat todayComplete = new OfflineTaskCountStat();
		todayComplete.setCount(response.getTodayCompleteCount());
		todayComplete.setId(Long.valueOf(new SimpleDateFormat("yyMMddhhmmssSSS").format(DateHelper.currentGMTTime())) * 1000);
		todayComplete.setTargetId(cmd.getTargetId());

		OfflineTaskCountStat todayTaskCount = new OfflineTaskCountStat();
		todayTaskCount.setCount(response.getTotayTasksCount());
		todayTaskCount.setId(Long.valueOf(new SimpleDateFormat("yyMMddhhmmssSSS").format(DateHelper.currentGMTTime())) * 10000);
		todayTaskCount.setTargetId(cmd.getTargetId());

		offlineResponse.setTodayCompleteCount(new ArrayList<>(Collections.singletonList(todayComplete)));
		offlineResponse.setTodayTasksCount(new ArrayList<>(Collections.singletonList(todayTaskCount)));
	}

	private void setTaskMaxUpdatedTime(EquipmentTaskDTO task) {
		List<Long> timeList = new ArrayList<>();
		if (task.getCreateTime() != null) {
			timeList.add(task.getCreateTime().getTime());
		}
		if (task.getExecutiveTime() != null) {
			timeList.add(task.getExecutiveTime().getTime());
		}
		if (task.getReviewTime() != null) {
			timeList.add(task.getReviewTime().getTime());
		}
		if (task.getProcessTime() != null) {
			timeList.add(task.getProcessTime().getTime());
		}
		if (timeList.size() > 0) {
			List<Long> result = timeList.stream().sorted(Comparator.comparing(Long::longValue).reversed()).collect(Collectors.toList());
			task.setLastSyncTime(new Timestamp(result.get(0)).toLocalDateTime().format(dateSF));
		}
	}

	private void syncRepairCategoryData(EquipmentTaskOfflineResponse offlineResponse, ListEquipmentTasksCommand cmd) {
		ListTaskCategoriesCommand categoriesCommand = new ListTaskCategoriesCommand();
		categoriesCommand.setPageSize(Integer.MAX_VALUE - 1);
		categoriesCommand.setTaskCategoryId(6L);
		categoriesCommand.setNamespaceId(cmd.getNamespaceId());
		categoriesCommand.setOwnerId(cmd.getTargetId());
		categoriesCommand.setOwnerType(cmd.getTargetType());
		ListTaskCategoriesResponse categories = pmTaskService.listTaskCategories(categoriesCommand);
		offlineResponse.setRepiarCategories(categories.getRequests());
	}

	private void syncGroupOfflineData(EquipmentTaskOfflineResponse response, ListEquipmentTasksCommand cmd) {
		ListFieldGroupCommand groupCommand = new ListFieldGroupCommand();
		groupCommand = ConvertHelper.convert(cmd, ListFieldGroupCommand.class);
		response.setGroups(fieldService.listFieldGroups(groupCommand));
	}

	@Override
	public OfflineEquipmentTaskReportResponse offlineEquipmentTaskReport(OfflineEquipmentTaskReportCommand cmd) {
		OfflineEquipmentTaskReportResponse offlineReportResponse = new OfflineEquipmentTaskReportResponse();
		List<OfflineEquipmentTaskReportLog> taskReportLogs = processEquipmentInspectionTasksAndResults(cmd);
		List<OfflineEquipmentTaskReportLog> repairLogs = processEquipmentRepairTasks(cmd.getEquipmentRepairReportDetail());
		offlineReportResponse.setTaskLogs(taskReportLogs);
		offlineReportResponse.setRepairLogs(repairLogs);
		return offlineReportResponse;
	}

	private List<OfflineEquipmentTaskReportLog> processEquipmentInspectionTasksAndResults(OfflineEquipmentTaskReportCommand command) {
		//need  sync  tasks list
		Long ownerId = command.getOwnerId();
		String ownerType = command.getOwnerType();
		List<OfflineEquipmentTaskReportLog> reportLogs = new ArrayList<>();
		List<Long> taskIds = new ArrayList<>();
		Map<Long, EquipmentInspectionTasks> tasksMap = new HashMap<>();
		if (command.getEquipmentTaskReportDetails() != null && command.getEquipmentTaskReportDetails().size() > 0) {
			command.getEquipmentTaskReportDetails().forEach((t) -> taskIds.add(t.getTaskId()));
		}

		if (taskIds.size() > 0) {
			taskIds.forEach((r) -> {
				EquipmentInspectionTasks task = null;
				OfflineEquipmentTaskReportLog reportLog = null;
				try {
					task = verifyEquipmentTask(r, ownerType, ownerId);
					tasksMap.put(r, task);
				} catch (Exception e) {
					LOGGER.error("equipmentInspection task  not exist, id = {}", r);
					e.printStackTrace();
					reportLog = getOfflineEquipmentTaskReportLogObject(r, ErrorCodes.ERROR_GENERAL_EXCEPTION,
							EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_NOT_EXIST, EquipmentOfflineErrorType.INEPECT_TASK.getCode());
				}

				if (task != null && EquipmentTaskStatus.WAITING_FOR_EXECUTING.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
					SetReviewExpireDaysCommand expireDaysCommand = new SetReviewExpireDaysCommand();
					expireDaysCommand.setCommunityId(task.getTargetId());
					expireDaysCommand.setNamespaceId(task.getNamespaceId());
					expireDaysCommand.setTargetId(task.getOwnerId());
					expireDaysCommand.setTargetType(task.getOwnerType());
					EquipmentInspectionReviewDateDTO date = listReviewExpireDays(expireDaysCommand);
					if (date != null) {
						task.setReviewExpiredDate(addDays(new Timestamp(DateHelper.currentGMTTime().getTime()), date.getReviewExpiredDays()));
					} else {
						task.setReviewExpiredDate(null);
					}
					task.setStatus(EquipmentTaskStatus.CLOSE.getCode());
					task.setExecutiveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					task.setExecutorType(OwnerType.USER.getCode());
					task.setExecutorId(UserContext.currentUserId());

					try {
						equipmentProvider.updateEquipmentTask(task);
					} catch (Exception e) {
						LOGGER.error("equipmentInspection task update failed, id = {}", r);
						LOGGER.error("equipmentInspection task update failed, ", e);
						OfflineEquipmentTaskReportLog logObject = getOfflineEquipmentTaskReportLogObject(r, ErrorCodes.ERROR_GENERAL_EXCEPTION,
								EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_SYNC_ERROR, EquipmentOfflineErrorType.INEPECT_TASK.getCode());
						reportLogs.add(logObject);
					}
					equipmentTasksSearcher.feedDoc(task);
				}
				if (reportLog != null) {
					reportLogs.add(reportLog);
				}
			});
		}
		reportLogs.addAll(processOfflineTaskLogsAndReportDetails(command.getEquipmentTaskReportDetails(), tasksMap));
		return reportLogs;
	}

	private OfflineEquipmentTaskReportLog getOfflineEquipmentTaskReportLogObject(Long errorId, int errorCode, int errorDescription, Byte errorType) {
		OfflineEquipmentTaskReportLog reportLog = new OfflineEquipmentTaskReportLog();
		reportLog.setErrorIds(errorId);
		reportLog.setErrorCode(errorCode);
		reportLog.setErrorType(errorType);
		reportLog.setErrorDescription(localeStringService.getLocalizedString(String.valueOf(EquipmentServiceErrorCode.SCOPE),
				String.valueOf(errorDescription),
				UserContext.current().getUser().getLocale(), "equipment inspection task  sync error"));
		return reportLog;
	}

	private List<OfflineEquipmentTaskReportLog> processOfflineTaskLogsAndReportDetails(List<EquipmentTaskReportDetail> equipmentTaskReportDetails, Map<Long, EquipmentInspectionTasks> tasksMap) {
		//reserve relations of  taskId and reportDetails
		Map<Long, List<EquipmentTaskReportDetail>> taskAndDetailsMap = new HashMap<>();
		if (equipmentTaskReportDetails != null && equipmentTaskReportDetails.size() > 0) {
			if (tasksMap.keySet().size() > 0) {
				tasksMap.keySet().forEach((taskId) -> taskAndDetailsMap.put(taskId, getTaskDetailListByTaskId(taskId, equipmentTaskReportDetails)));
			}
		}
		List<OfflineEquipmentTaskReportLog> reportLogs = new ArrayList<>();
		if (tasksMap != null) {
			for (EquipmentInspectionTasks task : tasksMap.values()) {
				//EquipmentInspectionTasks task = verifyEquipmentTask(taskId, null, null);
				EquipmentInspectionTasksLogs log = new EquipmentInspectionTasksLogs();
				log.setTaskId(task.getId());
				log.setOperatorType(OwnerType.USER.getCode());
				log.setOperatorId(UserContext.currentUserId());
				log.setProcessType(EquipmentTaskProcessType.COMPLETE.getCode());
				if (task.getExecutiveExpireTime() == null || task.getExecutiveTime().before(task.getExecutiveExpireTime())) {
					log.setProcessResult(EquipmentTaskProcessResult.COMPLETE_OK.getCode());
				} else {
					log.setProcessResult(EquipmentTaskProcessResult.COMPLETE_DELAY.getCode());
				}
				List<EquipmentTaskReportDetail> reportDetails = taskAndDetailsMap.get(task.getId());
				reportDetails.forEach((reportDetail) -> {
					if (reportDetail.getMessage() != null) {
						log.setProcessMessage(reportDetail.getMessage());
					}
					//process  attachements and   logs
					log.setEquipmentId(reportDetail.getEquipmentId());
					log.setStandardId(reportDetail.getStandardId());
					updateEquipmentTasksAttachmentAndLogs(task, log, reportDetail.getAttachments());

					List<InspectionItemResult> itemResults = reportDetail.getItemResults();
					if (itemResults != null && itemResults.size() > 0) {
						for (InspectionItemResult itemResult : itemResults) {
							EquipmentInspectionItemResults result = ConvertHelper.convert(itemResult, EquipmentInspectionItemResults.class);
							result.setTaskLogId(log.getId());
							result.setCommunityId(task.getTargetId());
							result.setEquipmentId(reportDetail.getEquipmentId());
							result.setStandardId(reportDetail.getStandardId());
							result.setInspectionCategoryId(task.getInspectionCategoryId());
							result.setNamespaceId(task.getNamespaceId());
							equipmentProvider.createEquipmentInspectionItemResults(result);
						}
					}
				});
				OfflineEquipmentTaskReportLog reportLog = new OfflineEquipmentTaskReportLog();
				reportLog.setSucessIds(task.getId());
				reportLogs.add(reportLog);
			}
		}
		return reportLogs;
	}

	private List<EquipmentTaskReportDetail> getTaskDetailListByTaskId(Long taskId, List<EquipmentTaskReportDetail> equipmentTaskReportDetails) {
		List<EquipmentTaskReportDetail> details = new ArrayList<>();
		if (equipmentTaskReportDetails != null && equipmentTaskReportDetails.size() > 0) {
			equipmentTaskReportDetails.forEach((detail) -> {
				if (detail.getTaskId().equals(taskId)) {
					details.add(detail);
				}
			});
			return details;
		}
		return null;
	}

	private List<OfflineEquipmentTaskReportLog> processEquipmentRepairTasks(List<CreateEquipmentRepairCommand> equipmentRepairReportDetail) {
		List<OfflineEquipmentTaskReportLog> repairSyncLogs = new ArrayList<>();
		if (equipmentRepairReportDetail != null && equipmentRepairReportDetail.size() > 0) {
			equipmentRepairReportDetail.forEach((detail) -> {
				List<OfflineEquipmentTaskReportLog> reportLog = createRepairsTask(detail);
				if (reportLog != null)
					repairSyncLogs.addAll(reportLog);
			});
		}
		return repairSyncLogs;
	}

	@Override
	public List<OfflineEquipmentTaskReportLog> createRepairsTask(CreateEquipmentRepairCommand cmd) {

		List<OfflineEquipmentTaskReportLog> logs = new ArrayList<>();

		EquipmentInspectionTasks tasks = null;
		OfflineEquipmentTaskReportLog reportLog = null;
		try {
			tasks = verifyEquipmentTask(cmd.getTaskId(), null, null);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("equipmentInspection task  not exist, id = {}", cmd.getTaskId());
			reportLog = getOfflineEquipmentTaskReportLogObject(cmd.getTaskId(), ErrorCodes.ERROR_GENERAL_EXCEPTION,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_NOT_EXIST, EquipmentOfflineErrorType.REPAIR_TASK.getCode());
			logs.add(reportLog);
		}
		if (tasks != null) {
			//报修后log表中需维修状态保留
			EquipmentInspectionTasksLogs tasksLog = new EquipmentInspectionTasksLogs();
			tasksLog.setEquipmentId(cmd.getEquipmentId());
			tasksLog.setStandardId(cmd.getStandardId());
			tasksLog.setOperatorType(OwnerType.USER.getCode());
			tasksLog.setOperatorId(UserContext.currentUserId());
			tasksLog.setInspectionCategoryId(tasks.getInspectionCategoryId());
			tasksLog.setCommunityId(tasks.getTargetId());
			tasksLog.setNamespaceId(cmd.getNamespaceId());
			tasksLog.setTaskId(cmd.getTaskId());
			//提交给报修
			tasksLog.setProcessType(EquipmentTaskProcessType.NEED_MAINTENANCE.getCode());
			tasksLog.setProcessMessage(cmd.getContent());
			tasksLog.setMaintanceType(cmd.getMaintanceType());
			tasksLog.setMaintanceStatus(PmTaskFlowStatus.ACCEPTING.getCode());
			tasksLog.setProcessResult(EquipmentTaskProcessResult.NONE.getCode());
			EquipmentInspectionTasks task = tasks;

//			dbProvider.execute((TransactionStatus status) -> {
			try {
				//调用物业报修
				EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), null, null);
				CreateTaskCommand repairCommand = new CreateTaskCommand();
				repairCommand = ConvertHelper.convert(cmd, CreateTaskCommand.class);
				repairCommand.setOwnerId(cmd.getTargetId());
				repairCommand.setOwnerType(cmd.getTargetType());
				repairCommand.setAddress(equipment.getLocation());
				repairCommand.setAddressType(PmTaskAddressType.FAMILY.getCode());
				repairCommand.setReferId(cmd.getEquipmentId());
				repairCommand.setReferType(EquipmentConstant.EQUIPMENT_REPAIR);
				repairCommand.setTaskCategoryId(cmd.getCategoryId());
				repairCommand.setFlowOrganizationId(cmd.getOwnerId());
				repairCommand.setOrganizationId(cmd.getOwnerId());

				List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(UserContext.currentUserId());
				if (members != null && members.size() > 0) {
					repairCommand.setRequestorName(members.get(0).getContactName());
					repairCommand.setRequestorPhone(members.get(0).getContactToken());
				}
				LOGGER.info("create repair tasks command ={}", repairCommand);
				PmTaskDTO pmTaskDTO = pmTaskService.createTask(repairCommand);
				tasksLog.setPmTaskId(pmTaskDTO.getId());
				tasksLog.setFlowCaseId(pmTaskDTO.getFlowCaseId());
				equipmentProvider.createEquipmentInspectionTasksLogs(tasksLog);
				//update equipment status to inMaintance
				equipmentProvider.updateEquipmentStatus(cmd.getEquipmentId(), EquipmentStatus.IN_MAINTENANCE.getCode());
				OfflineEquipmentTaskReportLog successLogs = new OfflineEquipmentTaskReportLog();
				successLogs.setSucessIds(task.getId());
				logs.add(successLogs);
			} catch (Exception e) {
				LOGGER.error("Sync Repair Tasks Erro, TaskId = {}", task.getId());
				LOGGER.error("Sync Repair Tasks Erro " + e);
				OfflineEquipmentTaskReportLog repairLogs = getOfflineEquipmentTaskReportLogObject(task.getId(), ErrorCodes.ERROR_GENERAL_EXCEPTION,
						EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_SYNC_ERROR, EquipmentOfflineErrorType.REPAIR_TASK.getCode());
				logs.add(repairLogs);
			}
			// return null;
//            });
		}

		return logs;
	}


	@Override
	public List<EquipmentOperateLogsDTO> listOperateLogs(DeleteEquipmentsCommand cmd) {
		List<EquipmentInspectionEquipmentLogs> logs = equipmentProvider.listEquipmentOperateLogsByTargetId(cmd.getEquipmentId());
		if (logs != null && logs.size() > 0) {
			return logs.stream().map((r) -> {
						EquipmentOperateLogsDTO logsDTO = ConvertHelper.convert(r, EquipmentOperateLogsDTO.class);
						List<OrganizationMember> member = organizationProvider.listOrganizationMembersByUId(r.getOperatorUid());
						if (member != null && member.size() > 0) {
							logsDTO.setOperatorName(member.get(0).getContactName());
						} else {
							User user = userProvider.findUserById(r.getOperatorUid());
							if (user != null)
								logsDTO.setOperatorName(user.getNickName());
						}
						return logsDTO;
					}
			).collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public void updateEquipmentStatus(DeleteEquipmentsCommand cmd) {
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(cmd.getEquipmentId());
		if (equipment == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_EXIST, "设备不存在！");
		}
		equipment.setStatus(EquipmentStatus.DISCARDED.getCode());
		equipmentProvider.updateEquipmentStatus(cmd.getEquipmentId(), EquipmentStatus.DISCARDED.getCode());
		equipmentSearcher.feedDoc(equipment);
		List<EquipmentStandardMap> maps = equipmentProvider.findByTarget(equipment.getId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
		if (maps != null && maps.size() > 0) {
			for (EquipmentStandardMap map : maps) {
				inActiveEquipmentStandardRelations(map);
			}
		}

		equipmentProvider.deleteEquipmentPlansMapByEquipmentId(equipment.getId());
		//增加设备的操作记录
		createEquipmentOperateLogs(equipment.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(), equipment.getId(), EquipmentOperateActionType.UPDATE.getCode());
	}

	@Override
	public void startCrontabTask() {
		LOGGER.info("================================================ starting equipment  manual job... ");
		closeDelayTasks();
		createTaskByPlan();
	}

	private void createTaskByPlan() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("EquipmentInspectionScheduleJob:createTaskByPlan.....");
		}

		List<EquipmentInspectionPlans> plans = equipmentProvider.listQualifiedEquipmentInspectionPlans();
		if (plans != null && plans.size() > 0) {
			LOGGER.info("createTaskByPlan.....plan size = {}" + plans.size());
			for (EquipmentInspectionPlans plan : plans) {
				if (checkPlanRepeat(plan)) {
					LOGGER.info("EquipmentInspectionScheduleJob: createEquipmentTaskByPlan.");
					createEquipmentTaskByPlan(plan);
					plan.setLastCreateTasktime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					equipmentProvider.updateEquipmentInspectionPlan(plan);
				}
			}
		}
	}

	private boolean checkPlanRepeat(EquipmentInspectionPlans plan) {
		boolean isRepeat = repeatService.isRepeatSettingActive(plan.getRepeatSettingId());
		LOGGER.info("checkPlanRepeat: plans  id = " + plan.getId()
				+ "repeat setting id = " + plan.getRepeatSettingId() + "is repeat setting active: " + isRepeat);
		return isRepeat;
	}

	private void closeDelayTasks() {
		LOGGER.info("EquipmentInspectionScheduleJob: close delay tasks.");
		equipmentProvider.closeDelayTasks();

		LOGGER.info("EquipmentInspectionScheduleJob: close expired review tasks.");
		equipmentProvider.closeExpiredReviewTasks();
	}


	@Override
	public HttpServletResponse exportTaskLogs(ExportTaskLogsCommand cmd, HttpServletResponse response) {
		ListingLocator locator = new ListingLocator();
		int pageSize = Integer.MAX_VALUE - 1;
		List<EquipmentInspectionTasksLogs> logs = equipmentProvider.listLogsByTaskId(locator, pageSize + 1,
				Long.valueOf(cmd.getTaskId()), Collections.singletonList(EquipmentTaskProcessType.COMPLETE.getCode()), null);
		logs = getLatestTaskLogs(logs);

		List<EquipmentInspectionTasksLogs> repairLogs = equipmentProvider.listLogsByTaskId(locator, pageSize + 1,
				Long.valueOf(cmd.getTaskId()), Collections.singletonList(EquipmentTaskProcessType.NEED_MAINTENANCE.getCode()), null);
		repairLogs = getLatestTaskLogs(repairLogs);

		EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(Long.valueOf(cmd.getTaskId()));

		List<EquipmentTaskLogsDTO> taskLogsDTO = new ArrayList<>();
		taskLogsDTO = processEquipmentTaskLogsDTOS(logs);

		URL rootPath = EquipmentServiceImpl.class.getResource("/");
		String filePath = rootPath.getPath() + this.downloadDir;
		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();
		filePath = filePath + "equipment_task_logs" + System.currentTimeMillis() + ".xlsx";
		//新建了一个文件
		this.createEquipmentTaskLogsBook(filePath, taskLogsDTO, task, repairLogs);

		return download(filePath, response);

	}

	private void createEquipmentTaskLogsBook(String filePath, List<EquipmentTaskLogsDTO> taskLogsDTO,EquipmentInspectionTasks task,List<EquipmentInspectionTasksLogs> repairLogs) {
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet taskSheet = wb.createSheet("任务信息");
		Sheet taskLogSheet = wb.createSheet("巡检记录");
		Sheet repairLogSheet = wb.createSheet("维修记录");
		taskSheet.setDefaultColumnWidth(20 * 256);
		taskLogSheet.setDefaultColumnWidth(20 * 256);
		repairLogSheet.setDefaultColumnWidth(20 * 256);
		// task info
		setNewEquipmentTaskInfoHeadAndBookRow(taskSheet, task);

		//task logs
		this.createEquipmentTaskLogsBookSheetHead(taskLogSheet);
		if (taskLogsDTO != null && taskLogsDTO.size() > 0) {
			taskLogsDTO.forEach((dto) -> setNewEquipmentTaskLogsBookRow(taskLogSheet, dto));
		}
		// repair logs
		this.createEquipmentRepairLogsBookSheetHead(repairLogSheet);
		if (repairLogs != null && repairLogs.size() > 0){
			repairLogs.forEach((dto) -> setNewEquipmentRepairLogsBookRow(repairLogSheet, dto));
		}

		try {
			FileOutputStream out = new FileOutputStream(filePath);
			wb.write(out);
			wb.close();
			out.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}
	}

	private void createEquipmentRepairLogsBookSheetHead(Sheet repairLogSheet) {
		Row row = repairLogSheet.createRow(repairLogSheet.getLastRowNum());
		int i = -1;
		row.createCell(++i).setCellValue("服务类型");
		row.createCell(++i).setCellValue("服务地点");
		row.createCell(++i).setCellValue("服务内容");
		row.createCell(++i).setCellValue("状态");
	}

	private void setNewEquipmentTaskInfoHeadAndBookRow(Sheet taskSheet, EquipmentInspectionTasks task) {
		Row row = taskSheet.createRow(taskSheet.getLastRowNum());
		int i = -1;
		row.createCell(++i).setCellValue("任务名称");
		row.createCell(++i).setCellValue("类型");
		row.createCell(++i).setCellValue("开始时间");
		row.createCell(++i).setCellValue("截止时间");
		row.createCell(++i).setCellValue("设备位置");
		row.createCell(++i).setCellValue("任务状态");
		row.createCell(++i).setCellValue("完成时间");
		row.createCell(++i).setCellValue("执行人");
		EquipmentTaskDTO taskDTO = convertTaskInfo(task);
		setNewEquipmentTasksBookRow(taskSheet,taskDTO);
	}

	private EquipmentTaskDTO convertTaskInfo(EquipmentInspectionTasks task) {
		if (task != null) {
			EquipmentInspectionPlans plan = null;
			EquipmentTaskDTO dto = ConvertHelper.convert(task, EquipmentTaskDTO.class);
			if (task.getPlanId() != null && task.getPlanId() != 0L) {
				plan = equipmentProvider.getEquipmmentInspectionPlanById(task.getPlanId());
				if (null != plan) {
					EquipmentInspectionPlanDTO plansDTO = ConvertHelper.convert(plan, EquipmentInspectionPlanDTO.class);
					processEquipmentInspectionObjectsByPlanId(plan.getId(), plansDTO);
					dto.setPlanDescription(plansDTO.getRemarks());
					dto.setTaskType(plansDTO.getPlanType());
					dto.setEquipments(plansDTO.getEquipmentStandardRelations());
				}
			}
			if (task.getExecutorId() != null && task.getExecutorId() != 0) {
				List<OrganizationMember> executors = organizationProvider.listOrganizationMembersByUId(task.getExecutorId());
				if (executors != null && executors.size() > 0) {
					dto.setExecutorName(executors.get(0).getContactName());
				}
			}
			return dto;
		}
		return null;
	}


	private void setNewEquipmentRepairLogsBookRow(Sheet sheet, EquipmentInspectionTasksLogs dto) {
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		int i = -1;
		row.createCell(++i).setCellValue(dto.getMaintanceType());
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(dto.getEquipmentId());
		String location = "";
		if (equipment != null) {
			location = equipment.getLocation();
		}
		row.createCell(++i).setCellValue(location);
		row.createCell(++i).setCellValue(dto.getProcessMessage());
		row.createCell(++i).setCellValue(PmTaskFlowStatus.fromCode(dto.getMaintanceStatus()).getDescription());
	}

	private void setNewEquipmentTaskLogsBookRow(Sheet sheet, EquipmentTaskLogsDTO dto) {
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
//		HSSFCellStyle style=workbook.createCellStyle();
//		style.setWrapText(true);//自动换行
		int i = -1;
		if (dto.getItemResults() != null && dto.getItemResults().size() > 0) {
			for (InspectionItemResult result : dto.getItemResults()) {
				row.createCell(++i).setCellValue(dto.getEquipmentName());
				row.createCell(++i).setCellValue("检查项名称：" + result.getItemName() +
						" 类型：" + (result.getItemValueType() == 0 ? " 确认 " : " 记录 ") +
						(result.getItemValue() == null ? "" : result.getItemValue()) + (result.getItemUnit() == null ? "" : result.getItemUnit()));
				row.createCell(++i).setCellValue(result.getNormalFlag() == 1 ? "正常" : "异常");
				row = sheet.createRow(sheet.getLastRowNum() + 1);
				i = -1;
			}
		}
	}

	private void createEquipmentTaskLogsBookSheetHead(Sheet sheet) {
		Row row = sheet.createRow(sheet.getLastRowNum());
		int i = -1;
		row.createCell(++i).setCellValue("设备名称");
		row.createCell(++i).setCellValue("检查项");
		row.createCell(++i).setCellValue("检查结果");
	}

	@Override
	public OrganizationDTO getAuthOrgByProjectIdAndModuleId(Long communityId,Integer namespaceId,Long moduleId) {
		ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
		listServiceModuleAppsCommand.setNamespaceId(namespaceId);
		listServiceModuleAppsCommand.setModuleId(moduleId);
		ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
		if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
			GetAuthOrgByProjectIdAndAppIdCommand command = new GetAuthOrgByProjectIdAndAppIdCommand();
			command.setAppId(apps.getServiceModuleApps().get(0).getId());
			command.setProjectId(communityId);
			return  organizationService.getAuthOrgByProjectIdAndAppId(command);
		}
		return null;
	}
}
