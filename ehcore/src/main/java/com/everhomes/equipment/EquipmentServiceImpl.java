package com.everhomes.equipment;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;




















































































































import javax.servlet.http.HttpServletResponse;




















































































































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




















































































































import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Role;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Building;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.discover.ItemType;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.quality.QualityInspectionCategories;
import com.everhomes.quality.QualityInspectionStandards;
import com.everhomes.quality.QualityInspectionTaskAttachments;
import com.everhomes.quality.QualityInspectionTaskRecords;
import com.everhomes.quality.QualityInspectionTasks;
import com.everhomes.quality.QualityService;
import com.everhomes.repeat.RepeatService;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.acl.admin.RoleDTO;
import com.everhomes.rest.address.CommunityAdminStatus;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.equipment.CreatEquipmentStandardCommand;
import com.everhomes.rest.equipment.DeleteEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.DeleteEquipmentStandardCommand;
import com.everhomes.rest.equipment.DeleteEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.DeleteEquipmentsCommand;
import com.everhomes.rest.equipment.EquipmentAccessoriesDTO;
import com.everhomes.rest.equipment.EquipmentAccessoryMapDTO;
import com.everhomes.rest.equipment.EquipmentAttachmentDTO;
import com.everhomes.rest.equipment.EquipmentNotificationTemplateCode;
import com.everhomes.rest.equipment.EquipmentParameterDTO;
import com.everhomes.rest.equipment.EquipmentQrCodeTokenDTO;
import com.everhomes.rest.equipment.EquipmentReviewStatus;
import com.everhomes.rest.equipment.EquipmentServiceErrorCode;
import com.everhomes.rest.equipment.EquipmentStandardStatus;
import com.everhomes.rest.equipment.EquipmentStandardsDTO;
import com.everhomes.rest.equipment.EquipmentStatus;
import com.everhomes.rest.equipment.EquipmentTaskDTO;
import com.everhomes.rest.equipment.EquipmentTaskLogsDTO;
import com.everhomes.rest.equipment.EquipmentTaskProcessResult;
import com.everhomes.rest.equipment.EquipmentTaskProcessType;
import com.everhomes.rest.equipment.EquipmentTaskResult;
import com.everhomes.rest.equipment.EquipmentTaskStatus;
import com.everhomes.rest.equipment.EquipmentsDTO;
import com.everhomes.rest.equipment.ImportDataType;
import com.everhomes.rest.equipment.ImportOwnerCommand;
import com.everhomes.rest.equipment.ListAttachmentsByEquipmentIdCommand;
import com.everhomes.rest.equipment.ListEquipmentTasksCommand;
import com.everhomes.rest.equipment.ListRelatedOrgGroupsCommand;
import com.everhomes.rest.equipment.ListTasksByEquipmentIdCommand;
import com.everhomes.rest.equipment.ReviewResult;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesResponse;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsResponse;
import com.everhomes.rest.equipment.SearchEquipmentStandardsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardsResponse;
import com.everhomes.rest.equipment.SearchEquipmentTasksCommand;
import com.everhomes.rest.equipment.ListEquipmentTasksResponse;
import com.everhomes.rest.equipment.SearchEquipmentsCommand;
import com.everhomes.rest.equipment.SearchEquipmentsResponse;
import com.everhomes.rest.equipment.ListLogsByTaskIdCommand;
import com.everhomes.rest.equipment.ListLogsByTaskIdResponse;
import com.everhomes.rest.equipment.ReportEquipmentTaskCommand;
import com.everhomes.rest.equipment.ReviewEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.ReviewEquipmentTaskCommand;
import com.everhomes.rest.equipment.StandardType;
import com.everhomes.rest.equipment.UpdateEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.UpdateEquipmentStandardCommand;
import com.everhomes.rest.equipment.UpdateEquipmentsCommand;
import com.everhomes.rest.equipment.VerifyEquipmentLocationCommand;
import com.everhomes.rest.forum.AttachmentDTO;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.ListOrganizationAdministratorCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationMenuResponse;
import com.everhomes.rest.organization.OrganizationNaviFlag;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.quality.EvaluationDTO;
import com.everhomes.rest.quality.GroupUserDTO;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.rest.quality.ProcessType;
import com.everhomes.rest.quality.QualityInspectionTaskDTO;
import com.everhomes.rest.quality.QualityInspectionTaskRecordsDTO;
import com.everhomes.rest.quality.QualityInspectionTaskResult;
import com.everhomes.rest.quality.QualityInspectionTaskReviewResult;
import com.everhomes.rest.quality.QualityInspectionTaskStatus;
import com.everhomes.rest.quality.QualityNotificationTemplateCode;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.quality.QualityStandardsDTO;
import com.everhomes.rest.quality.QualityTaskType;
import com.everhomes.rest.quality.StandardGroupDTO;
import com.everhomes.rest.repeat.RepeatExpressionDTO;
import com.everhomes.rest.repeat.RepeatServiceErrorCode;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.rest.repeat.TimeRangeDTO;
import com.everhomes.rest.techpark.punch.PunchServiceErrorCode;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;import com.everhomes.search.EquipmentAccessoriesSearcher;
import com.everhomes.search.EquipmentSearcher;
import com.everhomes.search.EquipmentStandardSearcher;
import com.everhomes.search.EquipmentTasksSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.rental.RentalServiceImpl;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

@Component
public class EquipmentServiceImpl implements EquipmentService {
	
	final String downloadDir ="\\download\\";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentServiceImpl.class);
	
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
	private QualityService qualityService;

	@Override
	public EquipmentStandardsDTO updateEquipmentStandard(
			UpdateEquipmentStandardCommand cmd) {
		
		User user = UserContext.current().getUser();
		RepeatSettings repeat = null;
		EquipmentInspectionStandards standard = null;
		
		if(cmd.getId() == null) {
			standard = ConvertHelper.convert(cmd, EquipmentInspectionStandards.class);
			standard.setCreatorUid(user.getId());
			standard.setOperatorUid(user.getId());

			if(cmd.getRepeat() == null) {
				throw RuntimeErrorException.errorWith(RepeatServiceErrorCode.SCOPE,
						RepeatServiceErrorCode.ERROR_REPEAT_SETTING_NOT_EXIST,
	 				"执行周期为空");
			}
			if(cmd.getRepeat() !=null) {
				repeat = ConvertHelper.convert(cmd.getRepeat(), RepeatSettings.class);
				if(cmd.getRepeat().getStartDate() != null)
					repeat.setStartDate(new Date(cmd.getRepeat().getStartDate()));
				if(cmd.getRepeat().getEndDate() != null)
					repeat.setEndDate(new Date(cmd.getRepeat().getEndDate()));
				
				repeat.setCreatorUid(user.getId());
				repeatService.createRepeatSettings(repeat);
				
				standard.setRepeatSettingId(repeat.getId());
				standard.setStatus(EquipmentStandardStatus.ACTIVE.getCode());
			}
			equipmentProvider.creatEquipmentStandard(standard);
			
		} else {
			EquipmentInspectionStandards exist = verifyEquipmentStandard(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
			standard = ConvertHelper.convert(cmd, EquipmentInspectionStandards.class);
			standard.setRepeatSettingId(exist.getRepeatSettingId());
			standard.setStatus(exist.getStatus());
			standard.setOperatorUid(user.getId());
			
			if(EquipmentStandardStatus.fromStatus(standard.getStatus()) == EquipmentStandardStatus.NOT_COMPLETED) {
				if(cmd.getRepeat() == null) {
					throw RuntimeErrorException.errorWith(RepeatServiceErrorCode.SCOPE,
							RepeatServiceErrorCode.ERROR_REPEAT_SETTING_NOT_EXIST,
		 				"执行周期为空");
				}
				if(cmd.getRepeat() !=null) {
					repeat = ConvertHelper.convert(cmd.getRepeat(), RepeatSettings.class);
					if(cmd.getRepeat().getStartDate() != null)
						repeat.setStartDate(new Date(cmd.getRepeat().getStartDate()));
					if(cmd.getRepeat().getEndDate() != null)
						repeat.setEndDate(new Date(cmd.getRepeat().getEndDate()));
					
					repeat.setCreatorUid(user.getId());
					repeatService.createRepeatSettings(repeat);
					
					standard.setRepeatSettingId(repeat.getId());
					standard.setStatus(EquipmentStandardStatus.ACTIVE.getCode());
				}
			}
			equipmentProvider.updateEquipmentStandard(standard);
			
			List<EquipmentInspectionEquipments> equipments = equipmentProvider.findEquipmentByStandardId(standard.getId());
			if(equipments != null && equipments.size() > 0) {
				for(EquipmentInspectionEquipments equipment : equipments) {
					inActiveEquipmentStandardRelations(equipment);
				}
			}
			
		}
		
		processRepeatSetting(standard);
		equipmentStandardSearcher.feedDoc(standard);
		EquipmentStandardsDTO dto = ConvertHelper.convert(standard, EquipmentStandardsDTO.class);
		return dto;
	}
	
	private void inActiveEquipmentStandardRelations(EquipmentInspectionEquipments equipment) {
		equipment.setReviewStatus(EquipmentReviewStatus.INACTIVE.getCode());
		equipmentProvider.updateEquipmentInspectionEquipment(equipment);
		equipmentSearcher.feedDoc(equipment);
	}

	@Override
	public void deleteEquipmentStandard(DeleteEquipmentStandardCommand cmd) {
		
		User user = UserContext.current().getUser();

		EquipmentInspectionStandards standard = verifyEquipmentStandard(cmd.getStandardId(), cmd.getOwnerType(), cmd.getOwnerId());

		if(EquipmentStandardStatus.fromStatus(standard.getStatus()) == EquipmentStandardStatus.INACTIVE) {
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
		
		List<EquipmentInspectionEquipments> equipments = equipmentProvider.findEquipmentByStandardId(standard.getId());
		if(equipments != null && equipments.size() > 0) {
			for(EquipmentInspectionEquipments equipment : equipments) {
				inActiveEquipmentStandardRelations(equipment);
			}
		}
	}

	@Override
	public HttpServletResponse exportEquipmentStandards(
			SearchEquipmentStandardsCommand cmd, HttpServletResponse response) {

		Integer pageSize = Integer.MAX_VALUE;
		cmd.setPageSize(pageSize);
		
		SearchEquipmentStandardsResponse standards = equipmentStandardSearcher.query(cmd);
		List<EquipmentStandardsDTO> eqStandards = standards.getEqStandards();
		
		URL rootPath = RentalServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + this.downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		filePath = filePath + "EquipmentStandards"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件
		this.createEquipmentStandardsBook(filePath, eqStandards);
		
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
	
	public void createEquipmentStandardsBook(String path,List<EquipmentStandardsDTO> dtos) {
		if (null == dtos || dtos.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("equipmentStandards");
		
		this.createEquipmentStandardsBookSheetHead(sheet);
		for (EquipmentStandardsDTO dto : dtos ) {
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
	
	private void createEquipmentStandardsBookSheetHead(Sheet sheet){

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		row.createCell(++i).setCellValue("标准编号");
		row.createCell(++i).setCellValue("标准名称");
		row.createCell(++i).setCellValue("类别");
		row.createCell(++i).setCellValue("执行频率");
		row.createCell(++i).setCellValue("开始执行时间");
		row.createCell(++i).setCellValue("时间限制");
		row.createCell(++i).setCellValue("最新更新时间");
		row.createCell(++i).setCellValue("标准来源");
		row.createCell(++i).setCellValue("标准状态");
	}
	
	private void setNewEquipmentStandardsBookRow(Sheet sheet ,EquipmentStandardsDTO dto){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;
		row.createCell(++i).setCellValue(dto.getStandardNumber());
		row.createCell(++i).setCellValue(dto.getName());
		row.createCell(++i).setCellValue(StandardType.fromStatus(dto.getStandardType()).getName());
		row.createCell(++i).setCellValue(repeatService.getExecutionFrequency(dto.getRepeat()));
		row.createCell(++i).setCellValue(repeatService.getExecuteStartTime(dto.getRepeat()));
		row.createCell(++i).setCellValue(repeatService.getlimitTime(dto.getRepeat()));
		row.createCell(++i).setCellValue(dto.getUpdateTime());
		row.createCell(++i).setCellValue(dto.getStandardSource());
		if(EquipmentStandardStatus.fromStatus(dto.getStatus()) == EquipmentStandardStatus.INACTIVE)
			row.createCell(++i).setCellValue("已失效");
		if(EquipmentStandardStatus.fromStatus(dto.getStatus()) == EquipmentStandardStatus.NOT_COMPLETED)
			row.createCell(++i).setCellValue("未完成");
		if(EquipmentStandardStatus.fromStatus(dto.getStatus()) == EquipmentStandardStatus.ACTIVE)
			row.createCell(++i).setCellValue("正常");
		
	}
	
	@Override
	public EquipmentStandardsDTO findEquipmentStandard(DeleteEquipmentStandardCommand cmd) {
		
		EquipmentInspectionStandards standard = verifyEquipmentStandard(cmd.getStandardId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		//填充关联设备数equipmentsCount
		processEquipmentsCount(standard);
		//填充执行周期repeat
		processRepeatSetting(standard);
		
		EquipmentStandardsDTO dto = ConvertHelper.convert(standard, EquipmentStandardsDTO.class);
		return dto;
	}
	
	private void processRepeatSetting(EquipmentInspectionStandards standard) {
		RepeatSettings repeat = repeatService.findRepeatSettingById(standard.getRepeatSettingId());
		standard.setRepeat(repeat);
	}
	
	private void processEquipmentsCount(EquipmentInspectionStandards standard) {
		List<EquipmentInspectionEquipments> equipments = equipmentProvider.findEquipmentByStandardId(standard.getId());
		if(equipments != null) {
			standard.setEquipmentsCount(equipments.size());
		}
	}
	
	private EquipmentInspectionStandards verifyEquipmentStandard(Long standardId, String ownerType, Long ownerId) {

		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(standardId, ownerType, ownerId);
		
		if(standard == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_STANDARD_NOT_EXIST,
 				"设备标准不存在");
		}
		
		return standard;
	}

	@Override
	public void reviewEquipmentStandardRelations(
			ReviewEquipmentStandardRelationsCommand cmd) {
		
		User user = UserContext.current().getUser();
		
		EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		if(EquipmentStatus.fromStatus(equipment.getStatus()) == EquipmentStatus.INACTIVE) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_ALREADY_DELETED,
 				"设备已删除");
		}
		
		if(EquipmentReviewStatus.fromStatus(equipment.getReviewStatus()) == EquipmentReviewStatus.WAITING_FOR_APPROVAL) {
			equipment.setReviewerUid(user.getId());
			equipment.setReviewResult(cmd.getReviewResult());
			equipment.setReviewStatus(EquipmentReviewStatus.REVIEWED.getCode());
			equipment.setReviewTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			
			equipmentProvider.updateEquipmentInspectionEquipment(equipment);
			
			equipmentSearcher.feedDoc(equipment);
			
			String scope = EquipmentNotificationTemplateCode.SCOPE;
			String locale = "zh_CN";
			
			Map<String, Object> notifyMap = new HashMap<String, Object>();
			notifyMap.put("equipmentName", equipment.getName());
			int code = 0;
			if(ReviewResult.fromStatus(cmd.getReviewResult()) == ReviewResult.QUALIFIED)
				code = EquipmentNotificationTemplateCode.QUALIFIED_EQUIPMENT_NOTIFY_EXECUTOR;
			
			if(ReviewResult.fromStatus(cmd.getReviewResult()) == ReviewResult.UNQUALIFIED)
				code = EquipmentNotificationTemplateCode.UNQUALIFIED_EQUIPMENT_NOTIFY_EXECUTOR;
			
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
			
			//发消息给管理员
			List<Long> userIds = getEquipmentManagerIds(equipment);
			for(Long uId : userIds) {
				sendMessageToUser(uId, notifyTextForApplicant);
			}
			
			
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
		
		ListOrganizationAdministratorCommand cmd = new ListOrganizationAdministratorCommand();
		
		cmd.setRoleIds(roles);
		cmd.setOrganizationId(equipment.getTargetId());
		
		ListOrganizationMemberCommandResponse resp = organizationService.listOrganizationPersonnelsByRoleIds(cmd);
		List<OrganizationMemberDTO> members = resp.getMembers();
		if(members != null && members.size() > 0) {
			for(OrganizationMemberDTO member : members) {
				uIds.add(member.getTargetId());
			}
		}
		
		return uIds;
	}
	
	@Override
	public void deleteEquipmentStandardRelations(
			DeleteEquipmentStandardRelationsCommand cmd) {
		EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		if(EquipmentStatus.fromStatus(equipment.getStatus()) == EquipmentStatus.INACTIVE) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_ALREADY_DELETED,
 				"设备已删除");
		}
		
		if(EquipmentReviewStatus.fromStatus(equipment.getReviewStatus()) == EquipmentReviewStatus.INACTIVE) {
			equipment.setReviewStatus(EquipmentReviewStatus.DELETE.getCode());
			equipment.setReviewResult(ReviewResult.NONE.getCode());
			equipment.setStandardId(0L);
			equipmentProvider.updateEquipmentInspectionEquipment(equipment);
			equipmentSearcher.feedDoc(equipment);
		} else {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_REVIEW_STATUS_ONLY_INACTIVE_CAN_DELETE,
 				"只有已失效的设备-标准关联关系可以删除");
		}
		
	}

	@Override
	public EquipmentsDTO updateEquipments(UpdateEquipmentsCommand cmd) {

		User user = UserContext.current().getUser();
		EquipmentInspectionEquipments equipment = null; 
		if(cmd.getStatus() != null 
				&& EquipmentStatus.fromStatus(cmd.getStatus()) == EquipmentStatus.IN_MAINTENANCE) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_STATUS_CANNOT_SET_IN_MAINTENANCE,
 				"设备状态后台不能设为维修中");
		}
		
		if(cmd.getId()  == null) {
			equipment = ConvertHelper.convert(cmd, EquipmentInspectionEquipments.class);
			if(equipment.getLongitude() == null || equipment.getLatitude() == null ) {
				throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
						EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_SET_LOCATION,
	 				"设备没有设置经纬度");
			}

			equipment.setReviewerUid(0L);
			equipment.setReviewTime(null);
			equipment.setReviewResult(ReviewResult.NONE.getCode());
			equipment.setReviewStatus(EquipmentReviewStatus.WAITING_FOR_APPROVAL.getCode());
			
			String geohash=GeoHashUtils.encode(equipment.getLatitude(), equipment.getLongitude());
			equipment.setGeohash(geohash);
			equipment.setCreatorUid(user.getId());
			equipment.setOperatorUid(user.getId());
			
			equipmentProvider.creatEquipmentInspectionEquipment(equipment);
			
		} else {
			EquipmentInspectionEquipments exist = verifyEquipment(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
			equipment = ConvertHelper.convert(cmd, EquipmentInspectionEquipments.class);
			equipment.setGeohash(exist.getGeohash());
			
			if(equipment.getStandardId() != exist.getStandardId()) {
				equipment.setReviewerUid(0L);
				equipment.setReviewTime(null);
				equipment.setReviewResult(ReviewResult.NONE.getCode());
				equipment.setReviewStatus(EquipmentReviewStatus.WAITING_FOR_APPROVAL.getCode());
			}
			
			if(equipment.getLatitude() != exist.getLatitude() || equipment.getLongitude() != exist.getLongitude()) {
				throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
						EquipmentServiceErrorCode.ERROR_EQUIPMENT_LOCATION_CANNOT_MODIFY,
	 				"设备经纬度不能修改");
			}
			
			equipment.setOperatorUid(user.getId());
			equipment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			equipmentProvider.updateEquipmentInspectionEquipment(equipment);
		}
		
		equipmentSearcher.feedDoc(equipment);
		
		EquipmentQrCodeTokenDTO qrCodeToken = new EquipmentQrCodeTokenDTO();
		qrCodeToken.setEquipmentId(equipment.getId());
		qrCodeToken.setOwnerId(equipment.getOwnerId());
		qrCodeToken.setOwnerType(equipment.getOwnerType());
		qrCodeToken.setUpdateTime(equipment.getUpdateTime());
		String tokenString = WebTokenGenerator.getInstance().toWebToken(qrCodeToken);
		equipment.setQrCodeToken(tokenString);
		equipmentProvider.updateEquipmentInspectionEquipment(equipment);
		
	    List<EquipmentAttachmentDTO> attachments = new ArrayList<EquipmentAttachmentDTO>();
	    List<EquipmentParameterDTO> eqParameter = new ArrayList<EquipmentParameterDTO>();
	    List<EquipmentAccessoryMapDTO> eqAccessoryMap = new ArrayList<EquipmentAccessoryMapDTO>();
		if(cmd.getEqParameter() != null) {
			for(EquipmentParameterDTO para : cmd.getEqParameter()) {
				para.setEquipmentId(equipment.getId());
				updateEquipmentParameter(para);
				eqParameter.add(para);
			}
		}
		if(cmd.getEqAccessoryMap() != null) {
			for(EquipmentAccessoryMapDTO map : cmd.getEqAccessoryMap()) {
				map.setEquipmentId(equipment.getId());
				updateEquipmentAccessoryMap(map);
				eqAccessoryMap.add(map);
			}
		}
		
		if(cmd.getAttachments() != null) {
			for(EquipmentAttachmentDTO attachment : cmd.getAttachments()) {
				attachment.setEquipmentId(equipment.getId());
				updateEquipmentAttachment(attachment, user.getId());
				attachments.add(attachment);
			}
		}
		
		EquipmentsDTO dto = ConvertHelper.convert(equipment, EquipmentsDTO.class);
		Organization group = organizationProvider.findOrganizationById(dto.getTargetId());
		if(group != null)
			dto.setTargetName(group.getName());
		
		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(equipment.getStandardId(), equipment.getOwnerType(), equipment.getOwnerId());
        if(standard != null) {
        	dto.setStandardName(standard.getName());
        }
		
		dto.setAttachments(attachments);
		dto.setEqAccessoryMap(eqAccessoryMap);
		dto.setEqParameter(eqParameter);
		
		return dto;
	}
	
	private void updateEquipmentParameter(EquipmentParameterDTO dto) {
		
		EquipmentInspectionEquipmentParameters parameter = ConvertHelper.convert(dto,
				EquipmentInspectionEquipmentParameters.class);
		
		if(dto.getId() == null) {
			equipmentProvider.creatEquipmentParameter(parameter);
		} else {
			equipmentProvider.updateEquipmentParameter(parameter);
		}
		
	}
	
	private void updateEquipmentAccessoryMap(EquipmentAccessoryMapDTO dto) {
		
		EquipmentInspectionAccessoryMap map = ConvertHelper.convert(dto, EquipmentInspectionAccessoryMap.class);
		
		if(dto.getEqAccessories() != null)
			map.setAccessoryId(dto.getEqAccessories().getId());
		
		if(dto.getId() == null) {
			equipmentProvider.creatEquipmentAccessoryMap(map);
		} else {
			equipmentProvider.updateEquipmentAccessoryMap(map);
		}
	}
	
	private void updateEquipmentAttachment(EquipmentAttachmentDTO dto, Long uid) {
		
		EquipmentInspectionEquipmentAttachments attachment = ConvertHelper.convert(dto, 
				EquipmentInspectionEquipmentAttachments.class);
		
		if(dto.getId() != null) {
			equipmentProvider.deleteEquipmentAttachmentById(dto.getId());
		} 

		attachment.setCreatorUid(uid);
		equipmentProvider.creatEquipmentAttachment(attachment);
	}

	@Override
	public void deleteEquipments(DeleteEquipmentsCommand cmd) {
		
		User user = UserContext.current().getUser();

		EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());

		if(EquipmentStatus.fromStatus(equipment.getStatus()) == EquipmentStatus.INACTIVE) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_ALREADY_DELETED,
 				"设备已删除");
		}
		
		equipment.setDeleterUid(user.getId());
		equipment.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		equipment.setOperatorUid(user.getId());
		equipment.setStatus(EquipmentStandardStatus.INACTIVE.getCode());
		
		equipmentProvider.updateEquipmentInspectionEquipment(equipment);
		
		inActiveEquipmentStandardRelations(equipment);
	}

	@Override
	public HttpServletResponse exportEquipments(SearchEquipmentsCommand cmd,
			HttpServletResponse response) {
		Integer pageSize = Integer.MAX_VALUE;
		cmd.setPageSize(pageSize);
		
		SearchEquipmentsResponse equipments = equipmentSearcher.queryEquipments(cmd);
		List<EquipmentsDTO> dtos = equipments.getEquipment();
		
		URL rootPath = RentalServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + this.downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		filePath = filePath + "Equipments"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件
		this.createEquipmentsBook(filePath, dtos);
		
		return download(filePath,response);
	}
	
	public void createEquipmentsBook(String path,List<EquipmentsDTO> dtos) {
		if (null == dtos || dtos.size() == 0)
			return;
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
		
	}
	
	private void createEquipmentsBookSheetHead(Sheet sheet){

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		row.createCell(++i).setCellValue("所属管理处");
		row.createCell(++i).setCellValue("设备名称");
		row.createCell(++i).setCellValue("设备类型");
		row.createCell(++i).setCellValue("二维码状态");
		row.createCell(++i).setCellValue("巡检/保养标准");
		row.createCell(++i).setCellValue("设备当前状态");
		row.createCell(++i).setCellValue("审批状态");
		row.createCell(++i).setCellValue("审批结果");
	}
	
	private void setNewEquipmentsBookRow(Sheet sheet ,EquipmentsDTO dto){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;
		row.createCell(++i).setCellValue(dto.getTargetName());
		row.createCell(++i).setCellValue(dto.getName());
		row.createCell(++i).setCellValue(dto.getCategoryPath());
		if(dto.getQrCodeFlag() == 0)
			row.createCell(++i).setCellValue("停用");
		if(dto.getQrCodeFlag() == 1)
			row.createCell(++i).setCellValue("启用");
		row.createCell(++i).setCellValue(dto.getStandardName());
		row.createCell(++i).setCellValue(EquipmentStatus.fromStatus(dto.getStatus()).getName());
		if(EquipmentReviewStatus.fromStatus(dto.getStatus()) == EquipmentReviewStatus.DELETE) {
			row.createCell(++i).setCellValue("");
		}
		if(EquipmentReviewStatus.fromStatus(dto.getStatus()) != EquipmentReviewStatus.DELETE){
			row.createCell(++i).setCellValue(EquipmentReviewStatus.fromStatus(dto.getReviewStatus()).getName());
		}
		
		if(ReviewResult.fromStatus(dto.getReviewResult()) == ReviewResult.NONE)
			row.createCell(++i).setCellValue("");
		if(ReviewResult.fromStatus(dto.getReviewResult()) == ReviewResult.QUALIFIED)
			row.createCell(++i).setCellValue("审核通过");
		if(ReviewResult.fromStatus(dto.getReviewResult()) == ReviewResult.UNQUALIFIED)
			row.createCell(++i).setCellValue("审核不通过");
		
	}

	@Override
	public EquipmentAccessoriesDTO updateEquipmentAccessories(
			UpdateEquipmentAccessoriesCommand cmd) {
		EquipmentInspectionAccessories accessory = ConvertHelper.convert(cmd, EquipmentInspectionAccessories.class);
		if(cmd.getId() == null) {
			accessory.setStatus((byte) 1);
			equipmentProvider.creatEquipmentInspectionAccessories(accessory);
		} else {
			verifyEquipmentAccessories(accessory.getId(), accessory.getOwnerType(), accessory.getOwnerId());
			equipmentProvider.updateEquipmentInspectionAccessories(accessory);
		}
		
		equipmentAccessoriesSearcher.feedDoc(accessory);
		EquipmentAccessoriesDTO dto = ConvertHelper.convert(accessory, EquipmentAccessoriesDTO.class);
		return dto;
	}
	
	private EquipmentInspectionAccessories verifyEquipmentAccessories(Long accessoryId, String ownerType, Long ownerId) {

		EquipmentInspectionAccessories accessory = equipmentProvider.findAccessoryById(accessoryId, ownerType, ownerId);
		
		if(accessory == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_ACCESSORY_NOT_EXIST,
 				"备品备件不存在");
		}
		
		return accessory;
	}

	@Override
	public void deleteEquipmentAccessories(DeleteEquipmentAccessoriesCommand cmd) {
		EquipmentInspectionAccessories accessory = verifyEquipmentAccessories(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
		if(accessory.getStatus() == 0) {
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
		
		SearchEquipmentAccessoriesResponse accessories = equipmentAccessoriesSearcher.query(cmd);
		List<EquipmentAccessoriesDTO> dtos = accessories.getAccessories();
		
		URL rootPath = RentalServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + this.downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		filePath = filePath + "EquipmentAccessories"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件
		this.createEquipmentAccessoriesBook(filePath, dtos);
		
		return download(filePath,response);
	}

	public void createEquipmentAccessoriesBook(String path,List<EquipmentAccessoriesDTO> dtos) {
		if (null == dtos || dtos.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("equipmentAccessories");
		
		this.createEquipmentAccessoriesBookSheetHead(sheet);
		for (EquipmentAccessoriesDTO dto : dtos ) {
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
	
	private void createEquipmentAccessoriesBookSheetHead(Sheet sheet){

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		row.createCell(++i).setCellValue("所属管理处");
		row.createCell(++i).setCellValue("备品名称");
		row.createCell(++i).setCellValue("生产厂商");
		row.createCell(++i).setCellValue("备品型号");
		row.createCell(++i).setCellValue("规格");
		row.createCell(++i).setCellValue("存放地点");
	}
	
	private void setNewEquipmentAccessoriesBookRow(Sheet sheet ,EquipmentAccessoriesDTO dto){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;
		row.createCell(++i).setCellValue(dto.getTargetName());
		row.createCell(++i).setCellValue(dto.getName());
		row.createCell(++i).setCellValue(dto.getManufacturer());
		row.createCell(++i).setCellValue(dto.getModelNumber());
		row.createCell(++i).setCellValue(dto.getSpecification());
		row.createCell(++i).setCellValue(dto.getLocation());
		
	}


	@Override
	public EquipmentTaskDTO reportEquipmentTask(ReportEquipmentTaskCommand cmd) {
		
		User user = UserContext.current().getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		EquipmentInspectionTasks task = verifyEquipmentTask(cmd.getTaskId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		//process_time operator_type operator_id
		if(EquipmentTaskStatus.fromStatus(task.getStatus()) == EquipmentTaskStatus.WAITING_FOR_EXECUTING 
				|| EquipmentTaskStatus.fromStatus(task.getStatus()) == EquipmentTaskStatus.IN_MAINTENANCE) {
			
			EquipmentInspectionTasksLogs log = new EquipmentInspectionTasksLogs();
			log.setTaskId(task.getId());
			log.setOperatorType(OwnerType.USER.getCode());
			log.setOperatorId(user.getId());
	 
			if(EquipmentTaskResult.fromStatus(cmd.getVerificationResult()) == EquipmentTaskResult.COMPLETE_OK) {
				
				task.setStatus(EquipmentTaskStatus.CLOSE.getCode());
				task.setExecutiveTime(now);
				task.setExecutorType(OwnerType.USER.getCode());
				task.setExecutorId(user.getId());
				log.setProcessType(EquipmentTaskProcessType.COMPLETE.getCode());
				if(task.getExecutiveExpireTime() == null || now.before(task.getExecutiveExpireTime())) {
					task.setResult(EquipmentTaskResult.COMPLETE_OK.getCode());
					log.setProcessResult(EquipmentTaskProcessResult.COMPLETE_OK.getCode());
				} else {
					task.setResult(EquipmentTaskResult.COMPLETE_DELAY.getCode());
					log.setProcessResult(EquipmentTaskProcessResult.COMPLETE_DELAY.getCode());
				}
				
			}
			
			else if(EquipmentTaskResult.fromStatus(cmd.getVerificationResult())
					== EquipmentTaskResult.NEED_MAINTENANCE_OK) {
				task.setStatus(EquipmentTaskStatus.NEED_MAINTENANCE.getCode());
				task.setExecutiveTime(now);
				task.setExecutorType(OwnerType.USER.getCode());
				task.setExecutorId(user.getId());
				log.setProcessType(EquipmentTaskProcessType.NEED_MAINTENANCE.getCode());
				if(task.getExecutiveExpireTime() == null || now.before(task.getExecutiveExpireTime())) {
					task.setResult(EquipmentTaskResult.NEED_MAINTENANCE_OK.getCode());
					log.setProcessResult(EquipmentTaskProcessResult.NEED_MAINTENANCE_OK.getCode());
				} else {
					task.setResult(EquipmentTaskResult.NEED_MAINTENANCE_DELAY.getCode());
					log.setProcessResult(EquipmentTaskProcessResult.NEED_MAINTENANCE_DELAY.getCode());
				}
			}
			
			else if(EquipmentTaskResult.fromStatus(cmd.getVerificationResult())
					== EquipmentTaskResult.NEED_MAINTENANCE_OK_COMPLETE_OK) {
				task.setStatus(EquipmentTaskStatus.CLOSE.getCode());
				task.setProcessTime(now);
				task.setOperatorType(OwnerType.USER.getCode());
				task.setOperatorId(user.getId());
				log.setProcessType(EquipmentTaskProcessType.COMPLETE_MAINTENANCE.getCode());
				if(task.getProcessExpireTime() == null || now.before(task.getProcessExpireTime())) {
					task.setResult(EquipmentTaskResult.NEED_MAINTENANCE_OK_COMPLETE_OK.getCode());
					log.setProcessResult(EquipmentTaskProcessResult.NEED_MAINTENANCE_OK_COMPLETE_OK.getCode());
				} else {
					task.setResult(EquipmentTaskResult.NEED_MAINTENANCE_OK_COMPLETE_DELAY.getCode());
					log.setProcessResult(EquipmentTaskProcessResult.NEED_MAINTENANCE_OK_COMPLETE_DELAY.getCode());
				}
			}
			
			else if(EquipmentTaskResult.fromStatus(cmd.getVerificationResult())
					== EquipmentTaskResult.NEED_MAINTENANCE_DELAY_COMPLETE_OK) {
				task.setStatus(EquipmentTaskStatus.CLOSE.getCode());
				task.setProcessTime(now);
				task.setOperatorType(OwnerType.USER.getCode());
				task.setOperatorId(user.getId());
				log.setProcessType(EquipmentTaskProcessType.COMPLETE_MAINTENANCE.getCode());
				if(task.getProcessExpireTime() == null || now.before(task.getProcessExpireTime())) {
					task.setResult(EquipmentTaskResult.NEED_MAINTENANCE_DELAY_COMPLETE_OK.getCode());
					log.setProcessResult(EquipmentTaskProcessResult.NEED_MAINTENANCE_DELAY_COMPLETE_OK.getCode());
				} else {
					task.setResult(EquipmentTaskResult.NEED_MAINTENANCE_DELAY_COMPLETE_DELAY.getCode());
					log.setProcessResult(EquipmentTaskProcessResult.NEED_MAINTENANCE_DELAY_COMPLETE_DELAY.getCode());
				}
			}
			
			if(cmd.getMessage() != null) {
				
				log.setProcessMessage(cmd.getMessage());
			}
			
			if(cmd.getEqParameters() != null) {
				String paraText = localeStringService.getLocalizedString(
						String.valueOf(EquipmentServiceErrorCode.SCOPE),
						String.valueOf(EquipmentServiceErrorCode.EQUIPMENT_PARAMETER_RECORD),
						UserContext.current().getUser().getLocale(),
						"设备参数:");
				StringBuilder sb = new StringBuilder();
				for(EquipmentParameterDTO para : cmd.getEqParameters()) {
					sb.append(para.getParameterName() + ": " + para.getParameterValue() + para.getParameterUnit() + "   ");
				}
				
				String paraValue = paraText + sb;
				log.setParameterValue(paraValue);
			}
			
			EquipmentTaskDTO dto = updateEquipmentTasks(task, log, cmd.getAttachments());
			
			return dto;
		
		} else {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_NOT_WAITING_EXECUTE_OR_IN_MAINTENANCE,
 				"只有待执行和维修中的任务可以上报");
		}
		
	}
	
	private EquipmentTaskDTO updateEquipmentTasks(EquipmentInspectionTasks task, 
			EquipmentInspectionTasksLogs log, List<AttachmentDescriptor> attachmentList) {
		
		equipmentProvider.updateEquipmentTask(task);
		equipmentTasksSearcher.feedDoc(task);
		equipmentProvider.createEquipmentInspectionTasksLogs(log);
		
		User user = UserContext.current().getUser();
		processLogAttachments(user.getId(), attachmentList, log);
		
		List<EquipmentInspectionTasks> tasks = new ArrayList<EquipmentInspectionTasks>();
		tasks.add(task);
		List<EquipmentTaskDTO> dtos = convertEquipmentTasksToDTO(tasks); 
		if(dtos != null && dtos.size() > 0)
			return dtos.get(0);
		
		return null;
	}
	
	private List<EquipmentTaskDTO> convertEquipmentTasksToDTO(List<EquipmentInspectionTasks> tasks) {
		
		List<EquipmentTaskDTO> dtoList = tasks.stream().map((r) -> {
        	
			EquipmentTaskDTO dto = convertEquipmentTaskToDTO(r);  
        	return dto;
        }).filter(task->task!=null).collect(Collectors.toList());
		
		return dtoList;
	}
	
	private EquipmentTaskDTO convertEquipmentTaskToDTO(EquipmentInspectionTasks task) {
		
		EquipmentTaskDTO dto = ConvertHelper.convert(task, EquipmentTaskDTO.class);  


		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId(),
				task.getOwnerType(), task.getOwnerId());
		if(standard != null) {
			dto.setStandardDescription(standard.getDescription());
			dto.setStandardName(standard.getName());
            dto.setTaskType(standard.getStandardType());
		} 
    	
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(task.getEquipmentId(), task.getOwnerType(), task.getOwnerId());
        if(null != equipment) {
        	dto.setEquipmentName(equipment.getName());
        	dto.setEquipmentLocation(equipment.getLocation());
        }
        
    	Organization group = organizationProvider.findOrganizationById(task.getExecutiveGroupId());
		if(group != null)
			dto.setGroupName(group.getName());
    	
    	if(task.getExecutorId() != null && task.getExecutorId() != 0) {
        	OrganizationMember executor = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getExecutorId(), task.getExecutiveGroupId());
        	if(executor != null) {
        		dto.setExecutorName(executor.getContactName());
        	}
    	}
    	
    	if(task.getOperatorId() != null && task.getOperatorId() != 0) {
    		OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getOperatorId(), task.getExecutiveGroupId());
        	if(operator != null) {
        		dto.setOperatorName(operator.getContactName());
        	}
    	}
    	
    	if(task.getReviewerId() != null && task.getReviewerId() != 0) {
    		OrganizationMember reviewers = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getReviewerId(), task.getOwnerId());
        	if(reviewers != null) {
        		dto.setReviewerName(reviewers.getContactName());
        	}
    	}
    	
    	return dto;
	}
	
	private void processLogAttachments(long userId, List<AttachmentDescriptor> attachmentList, EquipmentInspectionTasksLogs log) {
        List<EquipmentInspectionTasksAttachments> results = null;
        
        if(attachmentList != null) {
            results = new ArrayList<EquipmentInspectionTasksAttachments>();
        	
            EquipmentInspectionTasksAttachments attachment = null;
            for(AttachmentDescriptor descriptor : attachmentList) {
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
                } catch(Exception e) {
                    LOGGER.error("Failed to save the attachment, userId=" + userId 
                        + ", attachment=" + attachment, e);
                }
            }
            log.setAttachments(results);
        }
    }
	
	private void populateLogAttachements(EquipmentInspectionTasksLogs log, List<EquipmentInspectionTasksAttachments> attachmentList) {
		 
		 if(attachmentList == null || attachmentList.size() == 0) {
	            if(LOGGER.isInfoEnabled()) {
	                LOGGER.info("The log attachment list is empty, logId=" + log.getId());
	            }
		 } else {
	            for(EquipmentInspectionTasksAttachments attachment : attachmentList) {
	            	populateLogAttachement(log, attachment);
	            }
		 }
	 }
	 
	 private void populateLogAttachement(EquipmentInspectionTasksLogs log, EquipmentInspectionTasksAttachments attachment) {
       
		 if(attachment == null) {
			 if(LOGGER.isInfoEnabled()) {
				 LOGGER.info("The log attachment is null, logId=" + log.getId());
			 }
		 } else {
			 
			 String contentUri = attachment.getContentUri();
			 if(contentUri != null && contentUri.length() > 0) {
				 try{
					 String url = contentServerService.parserUri(contentUri, EntityType.USER.getCode(), UserContext.current().getUser().getId());
					 attachment.setContentUrl(url);
				 }catch(Exception e){
					 LOGGER.error("Failed to parse attachment uri, logId=" + log.getId() + ", attachmentId=" + attachment.getId(), e);
				 }
			 } else {
				 if(LOGGER.isWarnEnabled()) {
					 LOGGER.warn("The content uri is empty, attchmentId=" + attachment.getId());
				 }
			 }
		 }
	 }

	@Override
	public void reviewEquipmentTask(ReviewEquipmentTaskCommand cmd) {
		User user = UserContext.current().getUser();
		EquipmentInspectionTasks task = verifyEquipmentTask(cmd.getTaskId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		EquipmentInspectionTasksLogs log = new EquipmentInspectionTasksLogs();
		log.setTaskId(task.getId());
		log.setOperatorType(OwnerType.USER.getCode());
		log.setOperatorId(user.getId());
		
		task.setReviewResult(cmd.getReviewResult());
		task.setReviewerId(user.getId());
		task.setReviewTime(new Timestamp(System.currentTimeMillis()));
		
		log.setProcessType(ProcessType.REVIEW.getCode());
		if(ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
			log.setProcessResult(EquipmentTaskProcessResult.REVIEW_QUALIFIED.getCode());
		}
		if(ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
			log.setProcessResult(EquipmentTaskProcessResult.REVIEW_UNQUALIFIED.getCode());
		}
		//0:none, 1: qualified, 2: unqualified
		if(EquipmentTaskStatus.fromStatus(task.getStatus()) == EquipmentTaskStatus.CLOSE) {
			
			if(cmd.getReviewResult() == 2 &&
					(EquipmentTaskResult.fromStatus(task.getResult()) == EquipmentTaskResult.COMPLETE_DELAY || 
					EquipmentTaskResult.fromStatus(task.getResult()) == EquipmentTaskResult.COMPLETE_OK)) {
				task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
				task.setResult(EquipmentTaskResult.NONE.getCode());
			}
			
			if(cmd.getReviewResult() == 2 &&
					(EquipmentTaskResult.fromStatus(task.getResult()) == EquipmentTaskResult.NEED_MAINTENANCE_DELAY_COMPLETE_DELAY || 
					EquipmentTaskResult.fromStatus(task.getResult()) == EquipmentTaskResult.NEED_MAINTENANCE_DELAY_COMPLETE_OK || 
					EquipmentTaskResult.fromStatus(task.getResult()) == EquipmentTaskResult.NEED_MAINTENANCE_OK_COMPLETE_OK || 
					EquipmentTaskResult.fromStatus(task.getResult()) == EquipmentTaskResult.NEED_MAINTENANCE_OK_COMPLETE_DELAY)) {
				task.setStatus(EquipmentTaskStatus.IN_MAINTENANCE.getCode());
			}
		}
		
		else if(EquipmentTaskStatus.fromStatus(task.getStatus()) == EquipmentTaskStatus.NEED_MAINTENANCE) {
			
			if(cmd.getReviewResult() == 2) {
				task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
				task.setResult(EquipmentTaskResult.NONE.getCode());
			}
			
			else if(cmd.getReviewResult() == 1) {
				task.setStatus(EquipmentTaskStatus.IN_MAINTENANCE.getCode());
			}
		}
		
		
		if(cmd.getOperatorType() != null) {
			task.setOperatorType(cmd.getOperatorType());
			log.setTargetType(cmd.getOperatorType());
		}
			
		if(cmd.getOperatorId() != null) {
			task.setOperatorId(cmd.getOperatorId());
			log.setTargetId(cmd.getOperatorId());
		}
			
		if(cmd.getEndTime() != null) {
			task.setProcessExpireTime(new Timestamp(cmd.getEndTime()));
			log.setProcessEndTime(task.getProcessExpireTime());
		}
		
		if(!StringUtils.isEmpty(cmd.getOperatorType()) && cmd.getOperatorId() != null
				 && cmd.getEndTime() != null) {
			OrganizationMember reviewer = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getReviewerId(), task.getOwnerId());
			OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getOperatorId(), task.getExecutiveGroupId());
	    	
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("reviewerName", reviewer.getContactName());
			map.put("operatorName", operator.getContactName());
			map.put("deadline", timeToStr(new Timestamp(cmd.getEndTime())));
			
			String scope = EquipmentNotificationTemplateCode.SCOPE;
			String locale = "zh_CN";
			int msgCode = EquipmentNotificationTemplateCode.ASSIGN_TASK_MSG;
			String msg = localeTemplateService.getLocaleTemplateString(scope, msgCode, locale, map, "");
			log.setProcessMessage(msg);
			
			Map<String, Object> notifyMap = new HashMap<String, Object>();
			notifyMap.put("deadline", timeToStr(new Timestamp(cmd.getEndTime())));
			int code = EquipmentNotificationTemplateCode.ASSIGN_TASK_NOTIFY_OPERATOR;
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
			sendMessageToUser(cmd.getOperatorId(), notifyTextForApplicant);
		}
		
		updateEquipmentTasks(task, log, null);
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
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());
		if(equipment == null) {
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("createEquipmentTask：equipment not exist. equipmentId = " + cmd.getEquipmentId());
			}
			
			return ;
		}
		if(EquipmentStatus.fromStatus(equipment.getStatus()) == EquipmentStatus.IN_USE) {
			if(equipment.getStandardId() != null && equipment.getStandardId() != 0 &&
					ReviewResult.fromStatus(equipment.getReviewResult()) == ReviewResult.QUALIFIED) {
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(equipment.getStandardId(), cmd.getOwnerType(), cmd.getOwnerId());
				if(standard == null || standard.getStatus() == null
						|| EquipmentStandardStatus.fromStatus(standard.getStatus()) != EquipmentStandardStatus.ACTIVE) {
					if(LOGGER.isInfoEnabled()) {
						LOGGER.info("createEquipmentTask：standard is not exist or active. standardId = " + equipment.getStandardId());
					}
					
					return ;
				}
				this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_EQUIPMENT_TASK.getCode()).tryEnter(()-> {
					creatTaskByStandard(equipment, standard);
				});
			} else {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("createEquipmentTask：equipment not related with standard. equipmentId = " + cmd.getEquipmentId());
				}
				
				return ;
			}
			
		} else {
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("createEquipmentTask：equipment not in use. equipmentId = " + cmd.getEquipmentId());
			}
			
			return ;
		}
		
	}
	
	@Override
	public void creatTaskByStandard(EquipmentInspectionEquipments equipment, EquipmentInspectionStandards standard) {
		converStandardToDto(standard);
		EquipmentInspectionTasks task = new EquipmentInspectionTasks();
		task.setOwnerType(equipment.getOwnerType());
		task.setOwnerId(equipment.getOwnerId());
		task.setStandardId(equipment.getStandardId());
		task.setEquipmentId(equipment.getId());
		task.setTaskNumber(standard.getStandardNumber());
		task.setStandardType(standard.getStandardType());
		task.setExecutiveGroupType(equipment.getTargetType());
		task.setExecutiveGroupId(equipment.getTargetId());
		task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
		task.setResult(EquipmentTaskResult.NONE.getCode());
		
		StandardType type = StandardType.fromStatus(standard.getStandardType());
		
		List<TimeRangeDTO> timeRanges = repeatService.analyzeTimeRange(standard.getRepeat().getTimeRanges());
		
		if(timeRanges != null && timeRanges.size() > 0) {
			long current = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String day = sdf.format(current);
			int i = 0;
			for(TimeRangeDTO timeRange : timeRanges) {
				i++;
				
				String duration = timeRange.getDuration();
				String start = timeRange.getStartTime();
				String str = day + " " + start;
				Timestamp startTime = strToTimestamp(str);
				Timestamp expiredTime = repeatService.getEndTimeByAnalyzeDuration(startTime, duration);
				task.setExecutiveStartTime(startTime);
				task.setExecutiveExpireTime(expiredTime);
				long now = System.currentTimeMillis();
				//设备名称+巡检/保养+当日日期6位(年的后两位+月份+天)+两位序号（系统从01开始生成）
				if(i <10) {
					
					String taskName = equipment.getName() + type.getName() + 
							timestampToStr(new Timestamp(now)).substring(2) + "0" + i;
					task.setTaskName(taskName);
				} else {
					String taskName = equipment.getName() + type.getName() + 
							timestampToStr(new Timestamp(now)).substring(2) + i;
					task.setTaskName(taskName);
				}
				
				
				equipmentProvider.creatEquipmentTask(task);
				equipmentTasksSearcher.feedDoc(task);
				
				Map<String, Object> map = new HashMap<String, Object>();
			    map.put("deadline", timeToStr(expiredTime));
				String scope = EquipmentNotificationTemplateCode.SCOPE;
				int code = EquipmentNotificationTemplateCode.GENERATE_EQUIPMENT_TASK_NOTIFY;
				String locale = "zh_CN";
				String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				
				//发消息给所有组成员
				List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(equipment.getTargetId());
				for(OrganizationMember member : members) {
					sendMessageToUser(member.getTargetId(), notifyTextForApplicant);
				}
				
			}
				
		}
		

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
	
	private EquipmentStandardsDTO converStandardToDto(EquipmentInspectionStandards standard) {
		processRepeatSetting(standard);
		EquipmentStandardsDTO standardDto = ConvertHelper.convert(standard, EquipmentStandardsDTO.class);
		RepeatSettingsDTO repeatDto = ConvertHelper.convert(standard.getRepeat(), RepeatSettingsDTO.class);
		
		standardDto.setRepeat(repeatDto);
		
		return standardDto;
	} 
	
	private EquipmentInspectionTasks verifyEquipmentTask(Long taskId, String ownerType, Long ownerId) {
		EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(taskId, ownerType, ownerId);
		
		if(task == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_NOT_EXIST,
 				"任务不存在");
		}
		
		return task;
	}

	@Override
	public void verifyEquipmentLocation(VerifyEquipmentLocationCommand cmd) {

		WebTokenGenerator webToken = WebTokenGenerator.getInstance();
		EquipmentQrCodeTokenDTO qrCodeToken = webToken.fromWebToken(cmd.getQrCodeToken(), EquipmentQrCodeTokenDTO.class);
		
		EquipmentInspectionEquipments equipment = verifyEquipment(qrCodeToken.getEquipmentId(), 
				qrCodeToken.getOwnerType(), qrCodeToken.getOwnerId());
		
		if(equipment.getLongitude() == null || equipment.getLatitude() == null ) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_SET_LOCATION,
 				"设备没有设置经纬度");
		}
		
		double distance = (double)configProvider.getIntValue("equipment.verify.distance", 100);
		
		if(caculateDistance(cmd.getLongitude(), cmd.getLatitude(), 
				equipment.getLongitude(), equipment.getLatitude()) < distance) {
			return;
		} else {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_USER_NOT_IN_AREA,
 				"不在设备附近");
		}
	}
	
	private EquipmentInspectionEquipments verifyEquipment(Long equipmentId, String ownerType, Long ownerId) {

		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(equipmentId, ownerType, ownerId);
		
		if(equipment == null) {
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
		
		ListEquipmentTasksResponse tasks = equipmentTasksSearcher.query(cmd);
		List<EquipmentTaskDTO> dtos = tasks.getTasks();
		
		URL rootPath = RentalServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + this.downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		filePath = filePath + "EquipmentTasks"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件
		this.createEquipmentTasksBook(filePath, dtos);
		
		return download(filePath,response);
	}
	
	public void createEquipmentTasksBook(String path,List<EquipmentTaskDTO> dtos) {
		if (null == dtos || dtos.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("equipmentTasks");
		
		this.createEquipmentTasksBookSheetHead(sheet);
		for (EquipmentTaskDTO dto : dtos ) {
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
	
	private void createEquipmentTasksBookSheetHead(Sheet sheet){

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		row.createCell(++i).setCellValue("任务名称");
		row.createCell(++i).setCellValue("类型");
		row.createCell(++i).setCellValue("巡检设备");
		row.createCell(++i).setCellValue("开始时间");
		row.createCell(++i).setCellValue("截止时间");
		row.createCell(++i).setCellValue("设备位置");
		row.createCell(++i).setCellValue("任务状态");
		row.createCell(++i).setCellValue("审核状态");
		row.createCell(++i).setCellValue("任务结果");
		row.createCell(++i).setCellValue("完成时间");
		row.createCell(++i).setCellValue("执行人");
	}
	
	private void setNewEquipmentTasksBookRow(Sheet sheet ,EquipmentTaskDTO dto){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;
		row.createCell(++i).setCellValue(dto.getTaskName());
		row.createCell(++i).setCellValue((StandardType.fromStatus(dto.getTaskType()).getName()));
		row.createCell(++i).setCellValue(dto.getEquipmentName());
		
		row.createCell(++i).setCellValue(dto.getExecutiveStartTime());
		if(dto.getProcessExpireTime() != null) {
			row.createCell(++i).setCellValue(dto.getProcessExpireTime());
		} else {
			row.createCell(++i).setCellValue(dto.getExecutiveExpireTime());
		}
		
		row.createCell(++i).setCellValue(dto.getEquipmentLocation());
		row.createCell(++i).setCellValue(EquipmentTaskStatus.fromStatus(dto.getStatus()).getName());
		if(ReviewResult.fromStatus(dto.getReviewResult()) == ReviewResult.NONE)
			row.createCell(++i).setCellValue("");
		if(ReviewResult.fromStatus(dto.getReviewResult()) == ReviewResult.QUALIFIED)
			row.createCell(++i).setCellValue("审核通过");
		if(ReviewResult.fromStatus(dto.getReviewResult()) == ReviewResult.UNQUALIFIED)
			row.createCell(++i).setCellValue("审核不通过");
		
		row.createCell(++i).setCellValue(EquipmentTaskResult.fromStatus(dto.getResult()).getName());
		
		if(dto.getProcessExpireTime() != null) {
			row.createCell(++i).setCellValue(dto.getProcessTime());
			row.createCell(++i).setCellValue(dto.getOperatorName());
		} else {
			row.createCell(++i).setCellValue(dto.getExecutiveTime());
			row.createCell(++i).setCellValue(dto.getExecutorName());
		}
		
		
	}

	@Override
	public ListLogsByTaskIdResponse listLogsByTaskId(
			ListLogsByTaskIdCommand cmd) {
		EquipmentInspectionTasks task = verifyEquipmentTask(cmd.getTaskId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<EquipmentInspectionTasksLogs> logs = equipmentProvider.listLogsByTaskId(locator, pageSize + 1, task.getId(), cmd.getProcessType());
		
		ListLogsByTaskIdResponse response = new ListLogsByTaskIdResponse();
		Long nextPageAnchor = null;
        if(logs.size() > pageSize) {
        	logs.remove(logs.size() - 1);
            nextPageAnchor = logs.get(logs.size() - 1).getId();
        }
        
        response.setNextPageAnchor(nextPageAnchor);
        
        List<EquipmentTaskLogsDTO> dtos = logs.stream().map((r) -> {
        	
        	EquipmentTaskLogsDTO dto = ConvertHelper.convert(r, EquipmentTaskLogsDTO.class);
        	if(r.getOperatorId() != null && r.getOperatorId() != 0) {
        		OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(r.getOperatorId(), task.getExecutiveGroupId());
            	if(operator != null) {
            		dto.setOperatorName(operator.getContactName());
            	}
        	}
        	if(r.getTargetId() != null && r.getTargetId() != 0) {
        		OrganizationMember target = organizationProvider.findOrganizationMemberByOrgIdAndUId(r.getTargetId(), task.getExecutiveGroupId());
            	if(target != null) {
            		dto.setTargetName(target.getContactName());
            	}
        	}
        	return dto;
        }).collect(Collectors.toList());
        
        response.setLogs(dtos);
		return response;
	}

	@Override
	public ImportDataResponse importEquipmentStandards(ImportOwnerCommand cmd, MultipartFile mfile,
			Long userId) {
		ImportDataResponse importDataResponse = importData(cmd, mfile, userId, ImportDataType.EQUIPMENT_STANDARDS.getCode());
		return importDataResponse;
	}

	@Override
	public ImportDataResponse importEquipments(ImportOwnerCommand cmd, MultipartFile mfile, Long userId) {
		ImportDataResponse importDataResponse = importData(cmd, mfile, userId, ImportDataType.EQUIPMENTS.getCode());
		return importDataResponse;
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
			
			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty。userId="+userId);
				throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
						"File content is empty");
			}
			LOGGER.debug("Start import data...,total:" + resultList.size());
			
			List<String> errorDataLogs = null;
			//导入数据，返回导入错误的日志数据集
			if(StringUtils.equals(dataType, ImportDataType.EQUIPMENT_STANDARDS.getCode())) {
				errorDataLogs = importEquipmentStandardsData(cmd, convertToStrList(resultList, 6), userId);
			}
			
			if(StringUtils.equals(dataType, ImportDataType.EQUIPMENTS.getCode())) {
				errorDataLogs = importEquipmentsData(cmd, convertToStrList(resultList, 7), userId);
			}
			
			if(StringUtils.equals(dataType, ImportDataType.EQUIPMENT_ACCESSORIES.getCode())) {
				errorDataLogs = importEquipmentAccessoriesData(cmd, convertToStrList(resultList, 5), userId);
			}
			
			LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
			if(null == errorDataLogs || errorDataLogs.isEmpty()){
				LOGGER.debug("Data import all success...");
			}else{
				//记录导入错误日志
				for (String log : errorDataLogs) {
					LOGGER.error(log);
				}
			}
			
			importDataResponse.setTotalCount((long)resultList.size()-1);
			importDataResponse.setFailCount((long)errorDataLogs.size());
			importDataResponse.setLogs(errorDataLogs);
		} catch (IOException e) {
			LOGGER.error("File can not be resolved...");
			e.printStackTrace();
		}
		return importDataResponse;
	}
	
	private List<String> importEquipmentStandardsData(ImportOwnerCommand cmd, List<String> list, Long userId){
		List<String> errorDataLogs = new ArrayList<String>();


		for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
				EquipmentInspectionStandards standard = new EquipmentInspectionStandards();
				standard.setStandardNumber(s[0]);
				standard.setName(s[1]);
				standard.setStandardType(StandardType.fromName(s[2]).getCode());
				standard.setStandardSource(s[3]);
				standard.setDescription(s[4]);
				standard.setRemarks(s[5]);

				standard.setOwnerType(cmd.getOwnerType());
				standard.setOwnerId(cmd.getOwnerId());
				standard.setStatus(EquipmentStandardStatus.NOT_COMPLETED.getCode());
				
				LOGGER.info("add standard");
				equipmentProvider.creatEquipmentStandard(standard);
				return null;
			});
		}
		return errorDataLogs;
		
	}
	
	private List<String> importEquipmentsData(ImportOwnerCommand cmd, List<String> list, Long userId){
		List<String> errorDataLogs = new ArrayList<String>();

		for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
				EquipmentInspectionEquipments equipment = new EquipmentInspectionEquipments();
				equipment.setName(s[0]);
				equipment.setManufacturer(s[1]);
				equipment.setEquipmentModel(s[2]);
				equipment.setLocation(s[3]);
				equipment.setInitialAssetValue(s[4]);
				equipment.setManager(s[5]);
				equipment.setRemarks(s[6]);

				equipment.setOwnerType(cmd.getOwnerType());
				equipment.setOwnerId(cmd.getOwnerId());
				equipment.setTargetType(cmd.getTargetType());
				equipment.setTargetId(cmd.getTargetId());
				equipment.setStatus(EquipmentStatus.INCOMPLETE.getCode());
				
				LOGGER.info("add standard");
				equipmentProvider.creatEquipmentInspectionEquipment(equipment);
				return null;
			});
		}
		return errorDataLogs;
		
	}
	
	private List<String> importEquipmentAccessoriesData(ImportOwnerCommand cmd, List<String> list, Long userId){
		List<String> errorDataLogs = new ArrayList<String>();


		for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
				EquipmentInspectionAccessories accessory = new EquipmentInspectionAccessories();
				accessory.setName(s[0]);
				accessory.setManufacturer(s[1]);;
				accessory.setModelNumber(s[2]);;
				accessory.setSpecification(s[3]);;
				accessory.setLocation(s[4]);;

				accessory.setOwnerType(cmd.getOwnerType());
				accessory.setOwnerId(cmd.getOwnerId());
				accessory.setTargetType(cmd.getTargetType());
				accessory.setTargetId(cmd.getTargetId());
				accessory.setStatus((byte) 1);
				
				LOGGER.info("add equipment accessory");
				equipmentProvider.creatEquipmentInspectionAccessories(accessory);
				return null;
			});
		}
		return errorDataLogs;
		
	}
	
	private List<String> convertToStrList(List list, int column) {
		List<String> result = new ArrayList<String>();
		boolean firstRow = true;
		for (Object o : list) {
			if(firstRow){
				firstRow = false;
				continue;
			}
			RowResult r = (RowResult)o;
			StringBuffer sb = new StringBuffer();
			sb.append(r.getA()).append("||");
			sb.append(r.getB()).append("||");
			sb.append(r.getC()).append("||");
			sb.append(r.getD()).append("||");
			sb.append(r.getE()).append("||");
			if(column > 5)
				sb.append(r.getF()).append("||");
			if(column > 6)
				sb.append(r.getG()).append("||");
			
			result.add(sb.toString());
		}
		return result;
	}

	@Override
	public List<CategoryDTO> listEquipmentsCategories() {
		
		List<CategoryDTO> dtos = new ArrayList<CategoryDTO>();
		List<Category> categories = categoryProvider.listActionCategories(CategoryConstants.CATEGORY_ID_EQUIPMENT_TYPE);
		
		if(categories != null && categories.size() > 0) {
			dtos = categories.stream().map((r) -> {
				CategoryDTO dto = ConvertHelper.convert(r, CategoryDTO.class);
				return dto;
			}).collect(Collectors.toList());
		}
		return dtos;
	}

	@Override
	public ListEquipmentTasksResponse listEquipmentTasks(
			ListEquipmentTasksCommand cmd) {

		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
		User user = UserContext.current().getUser();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        
        List<EquipmentInspectionTasks> tasks = new ArrayList<EquipmentInspectionTasks>();
      
        //是否是管理员
        boolean isAdmin = false;
		List<RoleAssignment> resources = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOwnerId(), EntityType.USER.getCode(), user.getId());
		if(null != resources && 0 != resources.size()){
			for (RoleAssignment resource : resources) {
				if(resource.getRoleId() == RoleConstants.ENTERPRISE_SUPER_ADMIN 
						|| resource.getRoleId() == RoleConstants.ENTERPRISE_ORDINARY_ADMIN
						|| resource.getRoleId() == RoleConstants.PM_SUPER_ADMIN 
						|| resource.getRoleId() == RoleConstants.PM_ORDINARY_ADMIN) {
					isAdmin = true;
					break;
				}
			}
		}
		if(isAdmin) {
			tasks = equipmentProvider.listEquipmentInspectionTasks(cmd.getOwnerType(), cmd.getOwnerId(), null, null, locator, pageSize + 1);
		} else {
			List<OrganizationDTO> groupDtos = qualityService.listUserRelateOrgGroups();
			List<String> targetTypes = new ArrayList<String>();
			List<Long> targetIds = new ArrayList<Long>();
			if(groupDtos != null && groupDtos.size() > 0) {
				for(OrganizationDTO dto : groupDtos) {
					targetTypes.add(dto.getGroupType());
					targetIds.add(dto.getId());
				}
			}
			
			tasks = equipmentProvider.listEquipmentInspectionTasks(cmd.getOwnerType(), cmd.getOwnerId(), targetTypes, targetIds, locator, pageSize + 1);
		}
        if(tasks.size() > pageSize) {
        	tasks.remove(tasks.size() - 1);
        	response.setNextPageAnchor(tasks.get(tasks.size() - 1).getId());
        }
        
        
        if(isAdmin) {
        	List<EquipmentTaskDTO> dtos = tasks.stream().map(r -> {
	        	EquipmentTaskDTO dto = convertEquipmentTaskToDTO(r);
	        	return dto;
	        }).filter(r->r!=null).collect(Collectors.toList());
	        
			response.setTasks(dtos);
        } else {
        	
        	List<EquipmentTaskDTO> dtos = tasks.stream().map(r -> {
        		boolean isGroupAdmin = false;
        		List<RoleAssignment> res = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), r.getExecutiveGroupId(), EntityType.USER.getCode(), user.getId());
				if(null != res && 0 != res.size()){
					for (RoleAssignment resource : res) {
						if(resource.getRoleId() == RoleConstants.EQUIPMENT_MANAGER) {
							isGroupAdmin = true;
							break;
						}
					}
				}
	        	EquipmentTaskDTO dto = null;
	        	
	        	if(isGroupAdmin) {
	        		dto = convertEquipmentTaskToDTO(r);
	        	} else {
	        	
		        	if(EquipmentTaskStatus.fromStatus(r.getStatus()) == EquipmentTaskStatus.WAITING_FOR_EXECUTING) {
		
		        		dto = convertEquipmentTaskToDTO(r);
		        	}
		        	if(EquipmentTaskStatus.fromStatus(r.getStatus()) == EquipmentTaskStatus.IN_MAINTENANCE && 
		        			r.getOperatorId() == user.getId()) {
		        		
		        		dto = convertEquipmentTaskToDTO(r);
		        	}
	        	}
	
	        	return dto;
	        }).filter(r->r!=null).collect(Collectors.toList());
	        
			response.setTasks(dtos);
        }
		return response;
	}

	@Override
	public EquipmentsDTO findEquipment(DeleteEquipmentsCommand cmd) {

		EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());
		EquipmentsDTO dto = ConvertHelper.convert(equipment, EquipmentsDTO.class);
		Organization group = organizationProvider.findOrganizationById(dto.getTargetId());
		if(group != null)
			dto.setTargetName(group.getName());
		
		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(equipment.getStandardId(), equipment.getOwnerType(), equipment.getOwnerId());
        if(standard != null) {
        	dto.setStandardName(standard.getName());
        }
		
		return dto;
	}

	@Override
	public List<EquipmentParameterDTO> listParametersByEquipmentId(
			DeleteEquipmentsCommand cmd) {
		List<EquipmentInspectionEquipmentParameters> paras = equipmentProvider.listParametersByEquipmentId(cmd.getEquipmentId());
	
		if(paras == null || paras.size() == 0) {
			return null;
		}
		
		List<EquipmentParameterDTO> dtos = paras.stream().map(r -> {
			EquipmentParameterDTO dto = ConvertHelper.convert(r, EquipmentParameterDTO.class);
			return dto;
		}).collect(Collectors.toList());
		
		return dtos;
	
	}

	@Override
	public List<EquipmentAttachmentDTO> listAttachmentsByEquipmentId(
			ListAttachmentsByEquipmentIdCommand cmd) {
		List<EquipmentInspectionEquipmentAttachments> attachments = equipmentProvider.listAttachmentsByEquipmentId(cmd.getEquipmentId(), cmd.getAttachmentType());
	
		if(attachments == null || attachments.size() == 0) {
			return null;
		}
		
		List<EquipmentAttachmentDTO> dtos = attachments.stream().map(r -> {
			EquipmentAttachmentDTO dto = ConvertHelper.convert(r, EquipmentAttachmentDTO.class);
			String contentUri = r.getContentUri();
			 if(contentUri != null && contentUri.length() > 0) {
				 try{
					 String url = contentServerService.parserUri(contentUri, EntityType.USER.getCode(), UserContext.current().getUser().getId());
					 dto.setContentUrl(url);
				 }catch(Exception e){
					 LOGGER.error("Failed to parse attachment uri, equipmentId=" + r.getEquipmentId() + ", attachmentId=" + r.getId(), e);
				 }
			 } else {
				 if(LOGGER.isWarnEnabled()) {
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
		if(null != resources && 0 != resources.size()){
			for (RoleAssignment resource : resources) {
				if(resource.getRoleId() == RoleConstants.ENTERPRISE_SUPER_ADMIN 
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
		
		if(isAdmin) {
			List<OrganizationDTO> orgs = organizationService.listAllChildrenOrganizationMenusWithoutMenuStyle(cmd.getOwnerId(), 
					groupTypes, OrganizationNaviFlag.HIDE_NAVI.getCode());
			
			return orgs;
		} else {
			List<OrganizationDTO> groupDtos = organizationService.listUserRelateOrganizations(UserContext.getCurrentNamespaceId(), 
					user.getId(), OrganizationGroupType.GROUP);
			List<OrganizationDTO> dtos = new ArrayList<OrganizationDTO>();
			
			if(null == groupDtos || groupDtos.size() == 0) {
				return null;
			} else {
				for(OrganizationDTO group : groupDtos) {
					List<RoleAssignment> res = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), group.getId(), EntityType.USER.getCode(), user.getId());
					if(null != res && 0 != res.size()){
						for (RoleAssignment resource : res) {
							if(resource.getRoleId() == RoleConstants.EQUIPMENT_MANAGER) {
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
	public ListEquipmentTasksResponse listTasksByEquipmentId(
			ListTasksByEquipmentIdCommand cmd) {
		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        
        List<Long> standardIds = null;
        if(cmd.getTaskType() != null) {
        	standardIds = equipmentProvider.listStandardIdsByType(cmd.getTaskType());
        }
        
		List<EquipmentInspectionTasks> tasks = equipmentProvider.listTasksByEquipmentId(cmd.getEquipmentId(), standardIds, locator, pageSize+1);
		
		if(tasks.size() > pageSize) {
        	tasks.remove(tasks.size() - 1);
        	response.setNextPageAnchor(tasks.get(tasks.size() - 1).getId());
        }
        
    	List<EquipmentTaskDTO> dtos = tasks.stream().map(r -> {
        	EquipmentTaskDTO dto = convertEquipmentTaskToDTO(r);
        	return dto;
        }).filter(r->r!=null).collect(Collectors.toList());
        
		response.setTasks(dtos);
				
		return response;
	}

}
