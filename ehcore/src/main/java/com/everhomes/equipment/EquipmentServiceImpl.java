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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;































































































































































































import javax.servlet.http.HttpServletResponse;


import com.everhomes.util.*;
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































































































































































































import com.alibaba.fastjson.JSONArray;
import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Role;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
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
import com.everhomes.organization.OrganizationJobPosition;
import com.everhomes.organization.OrganizationJobPositionMap;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.quality.QualityInspectionCategories;
import com.everhomes.quality.QualityInspectionStandardGroupMap;
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
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.equipment.CreatEquipmentStandardCommand;
import com.everhomes.rest.equipment.CreateEquipmentCategoryCommand;
import com.everhomes.rest.equipment.CreateInspectionTemplateCommand;
import com.everhomes.rest.equipment.DeleteEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.DeleteEquipmentCategoryCommand;
import com.everhomes.rest.equipment.DeleteEquipmentStandardCommand;
import com.everhomes.rest.equipment.DeleteEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.DeleteEquipmentsCommand;
import com.everhomes.rest.equipment.DeleteInspectionTemplateCommand;
import com.everhomes.rest.equipment.EquipmentAccessoriesDTO;
import com.everhomes.rest.equipment.EquipmentAccessoryMapDTO;
import com.everhomes.rest.equipment.EquipmentAttachmentDTO;
import com.everhomes.rest.equipment.EquipmentInspectionCategoryDTO;
import com.everhomes.rest.equipment.EquipmentNotificationTemplateCode;
import com.everhomes.rest.equipment.EquipmentParameterDTO;
import com.everhomes.rest.equipment.EquipmentQrCodeTokenDTO;
import com.everhomes.rest.equipment.EquipmentReviewStatus;
import com.everhomes.rest.equipment.EquipmentServiceErrorCode;
import com.everhomes.rest.equipment.EquipmentStandardMapDTO;
import com.everhomes.rest.equipment.EquipmentStandardStatus;
import com.everhomes.rest.equipment.EquipmentStandardsDTO;
import com.everhomes.rest.equipment.EquipmentStatus;
import com.everhomes.rest.equipment.EquipmentTaskAttachmentDTO;
import com.everhomes.rest.equipment.EquipmentTaskDTO;
import com.everhomes.rest.equipment.EquipmentTaskLogsDTO;
import com.everhomes.rest.equipment.EquipmentTaskProcessResult;
import com.everhomes.rest.equipment.EquipmentTaskProcessType;
import com.everhomes.rest.equipment.EquipmentTaskResult;
import com.everhomes.rest.equipment.EquipmentTaskStatus;
import com.everhomes.rest.equipment.EquipmentsDTO;
import com.everhomes.rest.equipment.ExecuteGroupAndPosition;
import com.everhomes.rest.equipment.GetInspectionObjectByQRCodeCommand;
import com.everhomes.rest.equipment.ImportDataType;
import com.everhomes.rest.equipment.ImportOwnerCommand;
import com.everhomes.rest.equipment.InspectionItemDTO;
import com.everhomes.rest.equipment.InspectionItemResult;
import com.everhomes.rest.equipment.InspectionStandardMapTargetType;
import com.everhomes.rest.equipment.InspectionTemplateDTO;
import com.everhomes.rest.equipment.ListAttachmentsByEquipmentIdCommand;
import com.everhomes.rest.equipment.ListEquipmentInspectionCategoriesCommand;
import com.everhomes.rest.equipment.ListEquipmentTasksCommand;
import com.everhomes.rest.equipment.ListInspectionTemplatesCommand;
import com.everhomes.rest.equipment.ListParametersByStandardIdCommand;
import com.everhomes.rest.equipment.ListRelatedOrgGroupsCommand;
import com.everhomes.rest.equipment.ListTaskByIdCommand;
import com.everhomes.rest.equipment.ListTasksByEquipmentIdCommand;
import com.everhomes.rest.equipment.ListTasksByTokenCommand;
import com.everhomes.rest.equipment.ListUserHistoryTasksCommand;
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
import com.everhomes.rest.equipment.StandardGroupDTO;
import com.everhomes.rest.equipment.StandardType;
import com.everhomes.rest.equipment.StatEquipmentTasksCommand;
import com.everhomes.rest.equipment.StatEquipmentTasksResponse;
import com.everhomes.rest.equipment.Status;
import com.everhomes.rest.equipment.TaskCountDTO;
import com.everhomes.rest.equipment.TaskType;
import com.everhomes.rest.equipment.UpdateEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.UpdateEquipmentCategoryCommand;
import com.everhomes.rest.equipment.UpdateEquipmentStandardCommand;
import com.everhomes.rest.equipment.UpdateEquipmentsCommand;
import com.everhomes.rest.equipment.UpdateInspectionTemplateCommand;
import com.everhomes.rest.equipment.VerifyEquipmentLocationCommand;
import com.everhomes.rest.equipment.VerifyEquipmentLocationResponse;
import com.everhomes.rest.forum.AttachmentDTO;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.ListOrganizationAdministratorCommand;
import com.everhomes.rest.organization.ListOrganizationContactByJobPositionIdCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.ListOrganizationPersonnelByRoleIdsCommand;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationMenuResponse;
import com.everhomes.rest.organization.OrganizationNaviFlag;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.quality.CountTasksResponse;
import com.everhomes.rest.quality.EvaluationDTO;
import com.everhomes.rest.quality.GroupUserDTO;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.rest.quality.ProcessType;
import com.everhomes.rest.quality.QualityGroupType;
import com.everhomes.rest.quality.QualityInspectionTaskDTO;
import com.everhomes.rest.quality.QualityInspectionTaskRecordsDTO;
import com.everhomes.rest.quality.QualityInspectionTaskResult;
import com.everhomes.rest.quality.QualityInspectionTaskReviewResult;
import com.everhomes.rest.quality.QualityInspectionTaskStatus;
import com.everhomes.rest.quality.QualityNotificationTemplateCode;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.quality.QualityStandardsDTO;
import com.everhomes.rest.quality.QualityTaskType;
import com.everhomes.rest.repeat.RepeatExpressionDTO;
import com.everhomes.rest.repeat.RepeatServiceErrorCode;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.rest.repeat.TimeRangeDTO;
import com.everhomes.rest.techpark.punch.PunchServiceErrorCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;import com.everhomes.search.EquipmentAccessoriesSearcher;
import com.everhomes.search.EquipmentSearcher;
import com.everhomes.search.EquipmentStandardMapSearcher;
import com.everhomes.search.EquipmentStandardSearcher;
import com.everhomes.search.EquipmentTasksSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.rental.RentalServiceImpl;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.videoconf.ConfOrders;

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
	
	@Autowired
	private EquipmentStandardMapSearcher equipmentStandardMapSearcher;
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private CommunityProvider communityProvider;

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
			
			if(EquipmentStandardStatus.NOT_COMPLETED.equals(EquipmentStandardStatus.fromStatus(standard.getStatus()))) {
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
			
			List<EquipmentStandardMap> maps = equipmentProvider.findByStandardId(standard.getId());
			if(maps != null && maps.size() > 0) {
				for(EquipmentStandardMap map : maps) {
					inActiveEquipmentStandardRelations(map);
				}
			}
			
			inactiveTasksByStandardId(standard.getId());
			
		}
		
		processRepeatSetting(standard);
		
		List<StandardGroupDTO> groupList = cmd.getGroup();
		processStandardGroups(groupList, standard);
		
		equipmentStandardSearcher.feedDoc(standard);
		
		EquipmentStandardsDTO dto = converStandardToDto(standard);
		return dto;
	}
	
	private EquipmentStandardsDTO converStandardToDto(EquipmentInspectionStandards standard) {
		processRepeatSetting(standard);
		EquipmentStandardsDTO standardDto = ConvertHelper.convert(standard, EquipmentStandardsDTO.class);
		RepeatSettingsDTO repeatDto = ConvertHelper.convert(standard.getRepeat(), RepeatSettingsDTO.class);
		
		EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(standardDto.getTemplateId(), standardDto.getOwnerId(), standardDto.getOwnerType());
		if(template != null) {
			standardDto.setTemplateName(template.getName());
		}
		
		OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(standard.getOperatorUid(), 
				standard.getOwnerId());
		if(null != member) {
			standardDto.setOperatorName(member.getContactName());
		}
			
		List<StandardGroupDTO> executiveGroup = new ArrayList<StandardGroupDTO>();
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
						dto.setGroupName(dto.getGroupName() + "-");
					}
					dto.setGroupName(dto.getGroupName() + position.getName());
				}
	        	
	        	return dto;
	        }).collect(Collectors.toList());
		}
		

		if(standard.getReviewGroup() != null) {
			reviewGroup = standard.getReviewGroup().stream().map((r) -> {
	        	
				StandardGroupDTO dto = ConvertHelper.convert(r, StandardGroupDTO.class);  
				Organization group = organizationProvider.findOrganizationById(r.getGroupId());
				if(group != null)
					dto.setGroupName(group.getName());
				
	        	return dto;
	        }).collect(Collectors.toList());
		}
		
		standardDto.setRepeat(repeatDto);
		standardDto.setExecutiveGroup(executiveGroup);
		standardDto.setReviewGroup(reviewGroup);
		
		return standardDto;
	}
	
	private void processStandardGroups(List<StandardGroupDTO> groupList, EquipmentInspectionStandards standard) {
        
        List<EquipmentInspectionStandardGroupMap> executiveGroup = null;
		List<EquipmentInspectionStandardGroupMap> reviewGroup = null;
        this.equipmentProvider.deleteEquipmentInspectionStandardGroupMapByStandardId(standard.getId());
        
        if(groupList != null && groupList.size() >0) {
        	executiveGroup = new ArrayList<EquipmentInspectionStandardGroupMap>();
    		reviewGroup = new ArrayList<EquipmentInspectionStandardGroupMap>();
    		
			for(StandardGroupDTO group : groupList) {
				EquipmentInspectionStandardGroupMap map = new EquipmentInspectionStandardGroupMap();
				 map.setStandardId(standard.getId());
				 map.setGroupType(group.getGroupType());
				 map.setGroupId(group.getGroupId());
				 map.setPositionId(group.getPositionId());
				 equipmentProvider.createEquipmentInspectionStandardGroupMap(map);
				 if(QualityGroupType.EXECUTIVE_GROUP.equals(map.getGroupType())) {
					 executiveGroup.add(map);
				 }
				 if(QualityGroupType.REVIEW_GROUP.equals(map.getGroupType())) {
					 reviewGroup.add(map);
				 }
			}
			
			standard.setExecutiveGroup(executiveGroup);
			standard.setReviewGroup(reviewGroup);
		}
	}
	
	private void inActiveEquipmentStandardRelations(EquipmentStandardMap map) {
		map.setReviewStatus(EquipmentReviewStatus.INACTIVE.getCode());
		map.setReviewResult(ReviewResult.INACTIVE.getCode());
		equipmentProvider.updateEquipmentStandardMap(map);
		equipmentStandardMapSearcher.feedDoc(map);
		
	}

	@Override
	public void deleteEquipmentStandard(DeleteEquipmentStandardCommand cmd) {
		
		User user = UserContext.current().getUser();

		EquipmentInspectionStandards standard = verifyEquipmentStandard(cmd.getStandardId(), cmd.getOwnerType(), cmd.getOwnerId());

		if(EquipmentStandardStatus.INACTIVE.equals(EquipmentStandardStatus.fromStatus(standard.getStatus()))) {
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
		
		List<EquipmentStandardMap> maps = equipmentProvider.findByStandardId(standard.getId());
		if(maps != null && maps.size() > 0) {
			for(EquipmentStandardMap map : maps) {
				inActiveEquipmentStandardRelations(map);
			}
		}
		
		inactiveTasksByStandardId(standard.getId());
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
		row.createCell(++i).setCellValue(dto.getUpdateTime().toString());
		row.createCell(++i).setCellValue(dto.getStandardSource());
		if(EquipmentStandardStatus.INACTIVE.equals(EquipmentStandardStatus.fromStatus(dto.getStatus())))
			row.createCell(++i).setCellValue("已失效");
		if(EquipmentStandardStatus.NOT_COMPLETED.equals(EquipmentStandardStatus.fromStatus(dto.getStatus())))
			row.createCell(++i).setCellValue("未完成");
		if(EquipmentStandardStatus.ACTIVE.equals(EquipmentStandardStatus.fromStatus(dto.getStatus())))
			row.createCell(++i).setCellValue("正常");
		
	}
	
	@Override
	public EquipmentStandardsDTO findEquipmentStandard(DeleteEquipmentStandardCommand cmd) {
		
		EquipmentInspectionStandards standard = verifyEquipmentStandard(cmd.getStandardId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		//填充关联设备数equipmentsCount
		processEquipmentsCount(standard);
		//填充执行周期repeat
		processRepeatSetting(standard);
		
		equipmentProvider.populateStandardGroups(standard);
		EquipmentStandardsDTO dto = converStandardToDto(standard);
		
		return dto;
	}
	
	
	private void processRepeatSetting(EquipmentInspectionStandards standard) {
		if(null != standard.getRepeatSettingId() && standard.getRepeatSettingId() != 0) {
			RepeatSettings repeat = repeatService.findRepeatSettingById(standard.getRepeatSettingId());
			standard.setRepeat(repeat);
		}
	}
	
	private void processEquipmentsCount(EquipmentInspectionStandards standard) {
		List<EquipmentStandardMap> maps = equipmentProvider.findByStandardId(standard.getId());
		int count = 0;
		if(maps != null) {
			for(EquipmentStandardMap map : maps) {
				if(EquipmentReviewStatus.REVIEWED.equals(EquipmentReviewStatus.fromStatus(map.getReviewStatus())) &&
						ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(map.getReviewResult()))) {
					count++;
				}
			}
		}
		
		standard.setEquipmentsCount(count);
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
		
		EquipmentStandardMap map = equipmentProvider.findEquipmentStandardMapById(cmd.getId());
		EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		if(EquipmentStatus.INACTIVE.equals(EquipmentStatus.fromStatus(equipment.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_ALREADY_DELETED,
 				"设备已删除");
		}
		
		if(map == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_STANDARD_MAP_NOT_EXIST,
 				"没有有效的关联关系");
		}
		
		if(EquipmentReviewStatus.WAITING_FOR_APPROVAL.equals(EquipmentReviewStatus.fromStatus(map.getReviewStatus()))) {
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
//		EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());
		
//		if(EquipmentStatus.fromStatus(equipment.getStatus()) == EquipmentStatus.INACTIVE) {
//			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
//					EquipmentServiceErrorCode.ERROR_EQUIPMENT_ALREADY_DELETED,
// 				"设备已删除");
//		}
		
		EquipmentStandardMap map = equipmentProvider.findEquipmentStandardMapById(cmd.getId());
		if(map == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_STANDARD_MAP_NOT_EXIST,
 				"没有有效的关联关系");
		}
		
		if(EquipmentReviewStatus.INACTIVE.equals(EquipmentReviewStatus.fromStatus(map.getReviewStatus()))) {
			map.setReviewStatus(EquipmentReviewStatus.DELETE.getCode());
			map.setReviewResult(ReviewResult.NONE.getCode());
			equipmentProvider.updateEquipmentStandardMap(map);
			equipmentStandardMapSearcher.feedDoc(map);
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
				&& EquipmentStatus.IN_MAINTENANCE.equals(EquipmentStatus.fromStatus(cmd.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_STATUS_CANNOT_SET_IN_MAINTENANCE,
 				"设备状态后台不能设为维修中");
		}
		
		List<EquipmentStandardMapDTO> eqStandardMap = cmd.getEqStandardMap();
		
		if(cmd.getId()  == null) {
			equipment = ConvertHelper.convert(cmd, EquipmentInspectionEquipments.class);
			if(equipment.getLongitude() == null || equipment.getLatitude() == null ) {
				throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
						EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_SET_LOCATION,
	 				"设备没有设置经纬度");
			}
			String geohash=GeoHashUtils.encode(equipment.getLatitude(), equipment.getLongitude());
			
			if(cmd.getInstallationTime() != null)
				equipment.setInstallationTime(new Timestamp(cmd.getInstallationTime()));
			
			if(cmd.getRepairTime() != null)
				equipment.setRepairTime(new Timestamp(cmd.getRepairTime()));
			
			equipment.setGeohash(geohash);
			equipment.setCreatorUid(user.getId());
			equipment.setOperatorUid(user.getId());
		
			equipmentProvider.creatEquipmentInspectionEquipment(equipment);
			
			if(eqStandardMap != null && eqStandardMap.size() > 0) {
				for(EquipmentStandardMapDTO dto : eqStandardMap) {
					EquipmentStandardMap map = ConvertHelper.convert(dto, EquipmentStandardMap.class);
					map.setTargetId(equipment.getId());
					map.setTargetType(InspectionStandardMapTargetType.EQUIPMENT.getCode());
					map.setReviewerUid(0L);
					map.setReviewTime(null);
					map.setReviewResult(ReviewResult.NONE.getCode());
					map.setReviewStatus(EquipmentReviewStatus.WAITING_FOR_APPROVAL.getCode());
					map.setCreatorUid(user.getId());
					
					equipmentProvider.createEquipmentStandardMap(map);
					equipmentStandardMapSearcher.feedDoc(map);
				}
			}
			
		} else {
			EquipmentInspectionEquipments exist = verifyEquipment(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
			equipment = ConvertHelper.convert(cmd, EquipmentInspectionEquipments.class);
			equipment.setGeohash(exist.getGeohash());
			
			if(cmd.getInstallationTime() != null)
				equipment.setInstallationTime(new Timestamp(cmd.getInstallationTime()));
			
			if(cmd.getRepairTime() != null)
				equipment.setRepairTime(new Timestamp(cmd.getRepairTime()));
			
			
			if(exist.getLatitude() != null && equipment.getLongitude() != null) {
				if(!exist.getLatitude().equals(equipment.getLatitude()) || !equipment.getLongitude().equals(exist.getLongitude()) ) {
					throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
							EquipmentServiceErrorCode.ERROR_EQUIPMENT_LOCATION_CANNOT_MODIFY,
		 				"设备经纬度不能修改");
				}
			} else {
				equipment.setLatitude(cmd.getLatitude());
				equipment.setLongitude(cmd.getLongitude());
				if(equipment.getLongitude() == null || equipment.getLatitude() == null ) {
					throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
							EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_SET_LOCATION,
		 				"设备没有设置经纬度");
				}
				
				String geohash=GeoHashUtils.encode(equipment.getLatitude(), equipment.getLongitude());
				equipment.setGeohash(geohash);
			}
			
			equipment.setOperatorUid(user.getId());
			equipment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			equipmentProvider.updateEquipmentInspectionEquipment(equipment);
			
			
			if(eqStandardMap != null && eqStandardMap.size() > 0) {
				//不带id的create，其他的看map表中的standardId在不在cmd里面 不在的删掉 
				List<Long> updateStandardIds = new ArrayList<Long>();
				
				for(EquipmentStandardMapDTO dto : eqStandardMap) {
					if(dto.getId() == null) {
						EquipmentStandardMap map = ConvertHelper.convert(dto, EquipmentStandardMap.class);
						map.setTargetId(equipment.getId());
						map.setTargetType(InspectionStandardMapTargetType.EQUIPMENT.getCode());
						map.setReviewerUid(0L);
						map.setReviewTime(null);
						map.setReviewResult(ReviewResult.NONE.getCode());
						map.setReviewStatus(EquipmentReviewStatus.WAITING_FOR_APPROVAL.getCode());
						map.setCreatorUid(user.getId());
						
						equipmentProvider.createEquipmentStandardMap(map);
						equipmentStandardMapSearcher.feedDoc(map);
						
						updateStandardIds.add(map.getStandardId());
					} else {
						EquipmentStandardMap map = equipmentProvider.findEquipmentStandardMap(dto.getId(), dto.getStandardId(),
								 dto.getEquipmentId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
						if(map == null) {
							map = ConvertHelper.convert(dto, EquipmentStandardMap.class);
							map.setTargetId(equipment.getId());
							map.setTargetType(InspectionStandardMapTargetType.EQUIPMENT.getCode());
							map.setReviewerUid(0L);
							map.setReviewTime(null);
							map.setReviewResult(ReviewResult.NONE.getCode());
							map.setReviewStatus(EquipmentReviewStatus.WAITING_FOR_APPROVAL.getCode());
							map.setCreatorUid(user.getId());
							
							equipmentProvider.createEquipmentStandardMap(map);
							equipmentStandardMapSearcher.feedDoc(map);
						}
						
						updateStandardIds.add(map.getStandardId());
					}
				}
				
				List<EquipmentStandardMap> maps = equipmentProvider.findByTarget(equipment.getId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
				for(EquipmentStandardMap map : maps) {
					if(!updateStandardIds.contains(map.getStandardId())) {
						map.setStatus(Status.INACTIVE.getCode());
						map.setDeleterUid(user.getId());
						map.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						equipmentProvider.updateEquipmentStandardMap(map);
						equipmentStandardMapSearcher.feedDoc(map);
						
						inactiveTasks(equipment.getId(), map.getStandardId());
					}
				}
				
			}
			
		}
		
		equipmentSearcher.feedDoc(equipment);
		
//		EquipmentQrCodeTokenDTO qrCodeToken = new EquipmentQrCodeTokenDTO();
//		qrCodeToken.setEquipmentId(equipment.getId());
//		qrCodeToken.setOwnerId(equipment.getOwnerId());
//		qrCodeToken.setOwnerType(equipment.getOwnerType());
//		qrCodeToken.setUpdateTime(equipment.getUpdateTime());
//		String tokenString = WebTokenGenerator.getInstance().toWebToken(qrCodeToken);
		String tokenString = UUID.randomUUID().toString();
		equipment.setQrCodeToken(tokenString);
		equipmentProvider.updateEquipmentInspectionEquipment(equipment);
		
	    List<EquipmentAttachmentDTO> attachments = new ArrayList<EquipmentAttachmentDTO>();
	    List<EquipmentAccessoryMapDTO> eqAccessoryMap = new ArrayList<EquipmentAccessoryMapDTO>();
		
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

//		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(equipment.getStandardId(), equipment.getOwnerType(), equipment.getOwnerId());
//        if(standard != null) {
//        	dto.setStandardName(standard.getName());
//        }
		
		dto.setAttachments(attachments);
		dto.setEqAccessoryMap(eqAccessoryMap);
		
		populateEquipmentStandards(dto);
		
		return dto;
	}
	
	private void populateEquipmentStandards(EquipmentsDTO dto) {
		List<EquipmentStandardMapDTO> equipmentStandardMap = new ArrayList<EquipmentStandardMapDTO>();
		List<EquipmentStandardMap> maps = equipmentProvider.findByTarget(dto.getId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
		if(maps != null && maps.size() > 0) {
			for(EquipmentStandardMap map : maps) {
				EquipmentStandardMapDTO mapdto = ConvertHelper.convert(map, EquipmentStandardMapDTO.class);
				mapdto.setEquipmentId(map.getTargetId());
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(mapdto.getStandardId());
				if(standard != null) {
					mapdto.setStandardName(standard.getName());
					OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(mapdto.getReviewerUid(), 
							standard.getOwnerId());
					if(null != member) {
						mapdto.setReviewerName(member.getContactName());
					}
				}
				equipmentStandardMap.add(mapdto);
				
			}
		}
		
		dto.setEqStandardMap(equipmentStandardMap);
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

		if(EquipmentStatus.INACTIVE.equals(EquipmentStatus.fromStatus(equipment.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_ALREADY_DELETED,
 				"设备已删除");
		}
		
		equipment.setDeleterUid(user.getId());
		equipment.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		equipment.setOperatorUid(user.getId());
		equipment.setStatus(EquipmentStandardStatus.INACTIVE.getCode());
		
		equipmentProvider.updateEquipmentInspectionEquipment(equipment);
		equipmentSearcher.feedDoc(equipment);
		
		List<EquipmentStandardMap> maps = equipmentProvider.findByTarget(equipment.getId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
		if(maps != null && maps.size() > 0) {
			for(EquipmentStandardMap map : maps) {
				inActiveEquipmentStandardRelations(map);
			}
		}
		
		inactiveTasksByEquipmentId(equipment.getId());
		
	}
	
	private void inactiveTasksByStandardId(Long standardId) {
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

	private Timestamp addDays(Timestamp now, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, days);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());
		
		return time;
	}



	@Override
	public EquipmentTaskDTO reportEquipmentTask(ReportEquipmentTaskCommand cmd) {
		
		User user = UserContext.current().getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		EquipmentInspectionTasks task = verifyEquipmentTask(cmd.getTaskId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		Timestamp laterTime = DateUtils.getLaterTime(task.getExecutiveExpireTime(), task.getProcessExpireTime());
		if(EquipmentTaskStatus.WAITING_FOR_EXECUTING.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))
				 && laterTime.before(now)) {
			equipmentProvider.closeTask(task);
		} else if(EquipmentTaskStatus.IN_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))
				 && task.getProcessExpireTime() != null && task.getProcessExpireTime().before(now)) {
			equipmentProvider.closeTask(task);
		}
		
		if(EquipmentTaskStatus.CLOSE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
			LOGGER.error("task is closed");
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_CLOSE,
 				"该任务已关闭");
		}
		
		if(EquipmentTaskStatus.NONE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
			LOGGER.error("task is inactive");
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_INACTIVE,
 				"该任务已失效");
		}
		
		//process_time operator_type operator_id
		if(EquipmentTaskStatus.WAITING_FOR_EXECUTING.equals(EquipmentTaskStatus.fromStatus(task.getStatus())) 
				|| EquipmentTaskStatus.IN_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
			EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
			if(standard != null) {
				task.setReviewExpiredDate(addDays(now, standard.getReviewExpiredDays()));
			}
			
			
			EquipmentInspectionTasksLogs log = new EquipmentInspectionTasksLogs();
			log.setTaskId(task.getId());
			log.setOperatorType(OwnerType.USER.getCode());
			log.setOperatorId(user.getId());
	 
			task.setReviewResult(ReviewResult.NONE.getCode());
			if(EquipmentTaskResult.COMPLETE_OK.equals(EquipmentTaskResult.fromStatus(cmd.getVerificationResult()))) {
				
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
			
			else if(EquipmentTaskResult.NEED_MAINTENANCE_OK.equals(EquipmentTaskResult.fromStatus(cmd.getVerificationResult()))) {
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
			
			else if(EquipmentTaskResult.NEED_MAINTENANCE_OK_COMPLETE_OK.equals(EquipmentTaskResult.fromStatus(cmd.getVerificationResult()))) {
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
			
			else if(EquipmentTaskResult.NEED_MAINTENANCE_DELAY_COMPLETE_OK.equals(EquipmentTaskResult.fromStatus(cmd.getVerificationResult()))) {
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
			
			EquipmentTaskDTO dto = updateEquipmentTasks(task, log, cmd.getAttachments());
			List<InspectionItemResult> itemResults = cmd.getItemResults();
			if(itemResults != null && itemResults.size() > 0) {
				for(InspectionItemResult itemResult : itemResults) {
					EquipmentInspectionItemResults result = ConvertHelper.convert(itemResult, EquipmentInspectionItemResults.class);
					result.setTaskLogId(log.getId());
					equipmentProvider.createEquipmentInspectionItemResults(result);
				}
			}
			
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
            
            EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(standard.getTemplateId(), standard.getOwnerId(), standard.getOwnerType());
    		if(template != null) {
    			dto.setTemplateId(template.getId());
    			dto.setTemplateName(template.getName());
    		}
		} 
    	
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(task.getEquipmentId(), task.getOwnerType(), task.getOwnerId());
        if(null != equipment) {
        	dto.setEquipmentName(equipment.getName());
        	dto.setEquipmentLocation(equipment.getLocation());
        	dto.setQrCodeFlag(equipment.getQrCodeFlag());
        }
        
        Organization group = organizationProvider.findOrganizationById(task.getExecutiveGroupId());
		OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(task.getPositionId());
		if(group != null) {
			dto.setGroupName(group.getName());
			
		} 
		
		if(position != null) {
			if(dto.getGroupName() != null) {
				dto.setGroupName(dto.getGroupName() + "-");
			}
			dto.setGroupName(dto.getGroupName() + position.getName());
		}
    	
    	if(task.getExecutorId() != null && task.getExecutorId() != 0) {
        	OrganizationMember executor = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getExecutorId(), task.getOwnerId());
        	if(executor != null) {
        		dto.setExecutorName(executor.getContactName());
        	}
    	}
    	
    	if(task.getOperatorId() != null && task.getOperatorId() != 0) {
    		OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getOperatorId(), task.getOwnerId());
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
		Timestamp now = new Timestamp(System.currentTimeMillis());
		EquipmentInspectionTasks task = verifyEquipmentTask(cmd.getTaskId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		if((EquipmentTaskStatus.CLOSE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))
				 || EquipmentTaskStatus.NEED_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) 
				 && task.getReviewExpiredDate() != null && task.getReviewExpiredDate().before(now)) {
			equipmentProvider.closeReviewTasks(task);
		} 
		
		
		if((EquipmentTaskStatus.CLOSE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))
				 || EquipmentTaskStatus.NEED_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) 
				&& ReviewResult.REVIEW_DELAY.equals(EquipmentTaskResult.fromStatus(task.getReviewResult()))) {
			LOGGER.error("task is closed");
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_CLOSE,
				"该任务已关闭");
		}
		
		if(EquipmentTaskStatus.NONE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
			LOGGER.error("task is inactive");
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_INACTIVE,
				"该任务已失效");
		}
		
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
		if(EquipmentTaskStatus.CLOSE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
			
			if(ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult())) &&
					(EquipmentTaskResult.COMPLETE_DELAY.equals(EquipmentTaskResult.fromStatus(task.getResult())) || 
							EquipmentTaskResult.COMPLETE_OK.equals(EquipmentTaskResult.fromStatus(task.getResult())))) {
				task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
				task.setResult(EquipmentTaskResult.NONE.getCode());
			}
			
			if(ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult())) &&
					(EquipmentTaskResult.NEED_MAINTENANCE_DELAY_COMPLETE_DELAY.equals(EquipmentTaskResult.fromStatus(task.getResult())) || 
							EquipmentTaskResult.NEED_MAINTENANCE_DELAY_COMPLETE_OK.equals(EquipmentTaskResult.fromStatus(task.getResult())) || 
							EquipmentTaskResult.NEED_MAINTENANCE_OK_COMPLETE_OK.equals(EquipmentTaskResult.fromStatus(task.getResult())) || 
							EquipmentTaskResult.NEED_MAINTENANCE_OK_COMPLETE_DELAY.equals(EquipmentTaskResult.fromStatus(task.getResult())))) {
				task.setStatus(EquipmentTaskStatus.IN_MAINTENANCE.getCode());
			}
		}
		
		else if(EquipmentTaskStatus.NEED_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(task.getStatus()))) {
			
			if(ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
				task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
				task.setResult(EquipmentTaskResult.NONE.getCode());
			}
			
			else if(ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(cmd.getReviewResult()))) {
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

		List<EquipmentStandardMap> maps = equipmentProvider.listQualifiedEquipmentStandardMap(cmd.getEquipmentId());
		
		if(maps != null && maps.size() > 0) {
			for(EquipmentStandardMap map : maps) {
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(map.getStandardId());
				EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getTargetId());
				if(standard == null || standard.getStatus() == null
						|| !EquipmentStandardStatus.ACTIVE.equals(EquipmentStandardStatus.fromStatus(standard.getStatus()))) {
					LOGGER.info("EquipmentInspectionScheduleJob standard is not exist or active! standardId = " + map.getStandardId());
					continue;
				} else if(equipment == null || !EquipmentStatus.IN_USE.equals(EquipmentStatus.fromStatus(equipment.getStatus()))) {
						LOGGER.info("EquipmentInspectionScheduleJob equipment is not exist or active! equipmentId = " + map.getTargetId());
						continue;
					
				} else {
					boolean isRepeat = repeatService.isRepeatSettingActive(standard.getRepeatSettingId());
					LOGGER.info("EquipmentInspectionScheduleJob: standard id = " + standard.getId() 
							+ "repeat setting id = "+ standard.getRepeatSettingId() + "is repeat setting active: " + isRepeat);
					if(isRepeat) {
						this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_EQUIPMENT_TASK.getCode()).tryEnter(()-> {
							creatTaskByStandard(equipment, standard);
						});
					}
				}
			}
		}
			
		 else {
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("createEquipmentTask：equipment not in use. equipmentId = " + cmd.getEquipmentId());
			}
			
			return ;
		}
		
	}
	
	@Override
	public void creatTaskByStandard(EquipmentInspectionEquipments equipment, EquipmentInspectionStandards standard) {
		equipmentProvider.populateStandardGroups(standard);
		EquipmentStandardsDTO standardDto = converStandardToDto(standard);
		EquipmentInspectionTasks task = new EquipmentInspectionTasks();
		task.setOwnerType(equipment.getOwnerType());
		task.setOwnerId(equipment.getOwnerId());
		task.setTargetId(equipment.getTargetId());
		task.setTargetType(equipment.getTargetType());
		task.setInspectionCategoryId(equipment.getInspectionCategoryId());
		task.setStandardId(standardDto.getId());
		task.setEquipmentId(equipment.getId());
		task.setTaskNumber(standardDto.getStandardNumber());
		task.setStandardType(standardDto.getStandardType());
		task.setExecutiveGroupType(equipment.getTargetType());
		task.setExecutiveGroupId(equipment.getTargetId());
		task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
		task.setResult(EquipmentTaskResult.NONE.getCode());
		task.setReviewResult(ReviewResult.NONE.getCode());
		
		StandardType type = StandardType.fromStatus(standardDto.getStandardType());
		
		List<TimeRangeDTO> timeRanges = repeatService.analyzeTimeRange(standardDto.getRepeat().getTimeRanges());
		for(StandardGroupDTO executiveGroup : standardDto.getExecutiveGroup()) {
			
			task.setExecutiveGroupId(executiveGroup.getGroupId());
			task.setPositionId(executiveGroup.getPositionId());
				
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
					//标准名称+巡检/保养+当日日期6位(年的后两位+月份+天)+两位序号（系统从01开始生成）
					if(i <10) {
						
						String taskName = standard.getName() + type.getName() + 
								timestampToStr(new Timestamp(now)).substring(2) + "0" + i;
						task.setTaskName(taskName);
					} else {
						String taskName = standard.getName() + type.getName() + 
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
					
					if(executiveGroup.getPositionId() != null) {
						ListOrganizationContactByJobPositionIdCommand command = new ListOrganizationContactByJobPositionIdCommand();
						command.setOrganizationId(executiveGroup.getGroupId());
						command.setJobPositionId(executiveGroup.getPositionId());
						List<OrganizationContactDTO> contacts = organizationService.listOrganizationContactByJobPositionId(command);
						if(contacts != null && contacts.size() > 0) {
							for(OrganizationContactDTO contact : contacts) {
								sendMessageToUser(contact.getTargetId(), notifyTextForApplicant);
							}
						}
					} else {
						List<OrganizationMember> members = organizationProvider.listOrganizationMembers(executiveGroup.getGroupId(), null);
						if(members != null) {
							for(OrganizationMember member : members) {
								sendMessageToUser(member.getTargetId(), notifyTextForApplicant);
							}
						}
					}
					
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
	public VerifyEquipmentLocationResponse verifyEquipmentLocation(VerifyEquipmentLocationCommand cmd) {

//		WebTokenGenerator webToken = WebTokenGenerator.getInstance();
//		EquipmentQrCodeTokenDTO qrCodeToken = webToken.fromWebToken(cmd.getQrCodeToken(), EquipmentQrCodeTokenDTO.class);
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentByQrCodeToken(cmd.getQrCodeToken());
		
		if(equipment == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_EXIST,
 				"设备不存在");
		}
		
		if(!equipment.getId().equals(cmd.getEquipmentId())) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_QRCODE,
 				"二维码和任务设备不对应");
		}
		
		
		if(equipment.getLongitude() == null || equipment.getLatitude() == null ) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_SET_LOCATION,
 				"设备没有设置经纬度");
		}
		
		double distance = (double)configProvider.getIntValue("equipment.verify.distance", 100);
		
		if(caculateDistance(cmd.getLongitude(), cmd.getLatitude(), 
				equipment.getLongitude(), equipment.getLatitude()) < distance) {
			return null;
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
		if(null != dto.getTaskType()  && null != StandardType.fromStatus(dto.getTaskType()))
			row.createCell(++i).setCellValue(StandardType.fromStatus(dto.getTaskType()).getName());
		row.createCell(++i).setCellValue(dto.getEquipmentName());
		if(null != dto.getExecutiveStartTime())
			row.createCell(++i).setCellValue(dto.getExecutiveStartTime().toString());
		if(dto.getProcessExpireTime() != null) {
			row.createCell(++i).setCellValue(dto.getProcessExpireTime().toString());
		} else {
			if(null != dto.getExecutiveExpireTime())
				row.createCell(++i).setCellValue(dto.getExecutiveExpireTime().toString());
		}
		
		row.createCell(++i).setCellValue(dto.getEquipmentLocation());
		
		if(null != dto.getStatus() && null != EquipmentTaskStatus.fromStatus(dto.getStatus()))
			row.createCell(++i).setCellValue(EquipmentTaskStatus.fromStatus(dto.getStatus()).getName());
		
		if(ReviewResult.NONE.equals(ReviewResult.fromStatus(dto.getReviewResult())))
			row.createCell(++i).setCellValue("");
		if(ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(dto.getReviewResult())))
			row.createCell(++i).setCellValue("审核通过");
		if(ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(dto.getReviewResult())))
			row.createCell(++i).setCellValue("审核不通过");
		
		if(null != dto.getResult() && null != EquipmentTaskResult.fromStatus(dto.getResult()))
			row.createCell(++i).setCellValue(EquipmentTaskResult.fromStatus(dto.getResult()).getName());
		
		if(dto.getProcessTime() != null) {
			row.createCell(++i).setCellValue(dto.getProcessTime().toString());
			row.createCell(++i).setCellValue(dto.getOperatorName());
		} else {
			if(null != dto.getExecutiveTime())
				row.createCell(++i).setCellValue(dto.getExecutiveTime().toString());
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
		if(null == logs) {
			List<EquipmentTaskLogsDTO> dtos = new ArrayList<EquipmentTaskLogsDTO>();
			response.setLogs(dtos);
			return response;
		}
		
		Long nextPageAnchor = null;
        if(logs.size() > pageSize) {
        	logs.remove(logs.size() - 1);
            nextPageAnchor = logs.get(logs.size() - 1).getId();
        }
        
        response.setNextPageAnchor(nextPageAnchor);
        
        EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId(),
				task.getOwnerType(), task.getOwnerId());
		if(standard != null) {
			response.setTaskType(standard.getStandardType());
		} 
		
		EquipmentTaskDTO taskDto = convertEquipmentTaskToDTO(task);
		
		
        List<EquipmentTaskLogsDTO> dtos = logs.stream().map((r) -> {
        	
        	EquipmentTaskLogsDTO dto = ConvertHelper.convert(r, EquipmentTaskLogsDTO.class);
        	dto.setTemplateId(taskDto.getTemplateId());
        	dto.setTemplateName(taskDto.getTemplateName());
        	
        	List<EquipmentInspectionItemResults> itemResults = equipmentProvider.findEquipmentInspectionItemResultsByLogId(dto.getId());
        	
        	List<InspectionItemResult> results = new ArrayList<InspectionItemResult>();
        	if(itemResults != null && itemResults.size() > 0) {
        		results = itemResults.stream().map(result -> {
        			return ConvertHelper.convert(result, InspectionItemResult.class);
        		}).collect(Collectors.toList());
        	}
        	dto.setItemResults(results);
        	
        	if(r.getOperatorId() != null && r.getOperatorId() != 0) {
        		OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(r.getOperatorId(), task.getOwnerId());
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
        	
        	List<EquipmentInspectionTasksAttachments> attachmentLists = equipmentProvider.listTaskAttachmentsByLogId(dto.getId());
        	if(attachmentLists != null && attachmentLists.size() > 0) {
	        	populateLogAttachements(r, attachmentLists);
	        	List<EquipmentTaskAttachmentDTO> attachments = new ArrayList<EquipmentTaskAttachmentDTO>();
	        	for(EquipmentInspectionTasksAttachments attachment :  attachmentLists) {
	        		EquipmentTaskAttachmentDTO attDto = ConvertHelper.convert(attachment, EquipmentTaskAttachmentDTO.class);
	        		attachments.add(attDto);
	        	}
	        	dto.setAttachments(attachments);
        	}
        	
        	if(EquipmentTaskProcessType.COMPLETE.equals(EquipmentTaskProcessType.fromStatus(dto.getProcessType())) 
        			|| EquipmentTaskProcessType.COMPLETE_MAINTENANCE.equals(EquipmentTaskProcessType.fromStatus(dto.getProcessType()))
        			|| EquipmentTaskProcessType.NEED_MAINTENANCE.equals(EquipmentTaskProcessType.fromStatus(dto.getProcessType()))) {
        		EquipmentInspectionTasksLogs reviewLog =  equipmentProvider.getNearestReviewLogAfterProcess(dto.getTaskId(), dto.getId());
        		if(null == reviewLog) {
        			dto.setReviewResult(ReviewResult.NONE.getCode());
        		}
        		if(reviewLog != null) {
        			if(EquipmentTaskProcessResult.REVIEW_QUALIFIED.equals(EquipmentTaskProcessResult.fromStatus(reviewLog.getProcessResult()))) {
        				dto.setReviewResult(ReviewResult.QUALIFIED.getCode());
        			}
        			else if(EquipmentTaskProcessResult.REVIEW_UNQUALIFIED.equals(EquipmentTaskProcessResult.fromStatus(reviewLog.getProcessResult()))) {
        				dto.setReviewResult(ReviewResult.UNQUALIFIED.getCode());
        			}
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
				errorDataLogs = importEquipmentsData(cmd, convertEquipmentToStrList(resultList), userId);
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
				standard.setInspectionCategoryId(cmd.getInspectionCategoryId());
				standard.setStatus(EquipmentStandardStatus.NOT_COMPLETED.getCode());
				
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
	
	private List<String> importEquipmentsData(ImportOwnerCommand cmd, List<String> list, Long userId){
		List<String> errorDataLogs = new ArrayList<String>();

		for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
				EquipmentInspectionEquipments equipment = new EquipmentInspectionEquipments();
				equipment.setCustomNumber(s[1]);
				equipment.setName(s[2]);
				equipment.setEquipmentModel(s[3]);
				equipment.setParameter(s[4]);
				equipment.setManufacturer(s[5]);
				equipment.setLocation(s[7]);
				equipment.setQuantity(Long.valueOf(s[8]));
				equipment.setRemarks(s[9]);

				equipment.setOwnerType(cmd.getOwnerType());
				equipment.setOwnerId(cmd.getOwnerId());
				equipment.setTargetType(cmd.getTargetType());
				equipment.setTargetId(cmd.getTargetId());
				equipment.setInspectionCategoryId(cmd.getInspectionCategoryId());
				equipment.setStatus(EquipmentStatus.INCOMPLETE.getCode());
				
				equipment.setCreatorUid(userId);
				equipment.setOperatorUid(userId);
				LOGGER.info("add equipment");
				equipmentProvider.creatEquipmentInspectionEquipment(equipment);
				equipmentSearcher.feedDoc(equipment);
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
			if(column == 6)
				sb.append(r.getF()).append("||");
			if(column == 10) {
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
			if(firstRow < 4){
				firstRow++;
				continue;
			}
			RowResult r = (RowResult)o;
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
//        CrossShardListingLocator locator = new CrossShardListingLocator();
//        locator.setAnchor(cmd.getPageAnchor());
		if(null == cmd.getPageAnchor()) {
			cmd.setPageAnchor(0L);
		}
		Integer offset = cmd.getPageAnchor().intValue();
        
        List<EquipmentInspectionTasks> tasks = new ArrayList<EquipmentInspectionTasks>();
        List<String> targetTypes = new ArrayList<String>();
        List<Long> targetIds = new ArrayList<Long>();
        if(cmd.getTargetType() != null)
        	targetTypes.add(cmd.getTargetType());
        if(cmd.getTargetId() != null)
        	targetIds.add(cmd.getTargetId());
        
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
			tasks = equipmentProvider.listEquipmentInspectionTasks(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getInspectionCategoryId(), targetTypes, targetIds, null, offset, pageSize + 1);
		} else {
			List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
			if(cmd.getIsReview() != null && TaskType.REVIEW_TYPE.equals(TaskType.fromStatus(cmd.getIsReview()))) {
				
				List<EquipmentInspectionStandardGroupMap> maps = equipmentProvider.listEquipmentInspectionStandardGroupMapByGroupAndPosition(groupDtos);
				if(maps != null && maps.size() > 0) {
					List<Long> standardIds = maps.stream().map(r->{
						return r.getStandardId();
					}).collect(Collectors.toList());
					tasks = equipmentProvider.listEquipmentInspectionReviewTasks(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getInspectionCategoryId(), targetTypes, targetIds, standardIds, offset, pageSize + 1);
				}
				
			} else {
				tasks = equipmentProvider.listEquipmentInspectionTasks(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getInspectionCategoryId(), targetTypes, targetIds, groupDtos, offset, pageSize + 1);
			}
		}
        if(tasks.size() > pageSize) {
        	tasks.remove(tasks.size() - 1);
        	response.setNextPageAnchor((long) (offset + 1));
        }
        
        
    	List<EquipmentTaskDTO> dtos = tasks.stream().map(r -> {
        	EquipmentTaskDTO dto = convertEquipmentTaskToDTO(r);
        	return dto;
        }).filter(r->r!=null).collect(Collectors.toList());
        
		response.setTasks(dtos);
		
		return response;
	}
	
	private List<ExecuteGroupAndPosition> listUserRelateGroups() {
		User user = UserContext.current().getUser();

		List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(user.getId());
		if(members == null || members.size() == 0) {
			return new ArrayList<ExecuteGroupAndPosition>();
		}
		
		List<ExecuteGroupAndPosition> groupDtos = new ArrayList<ExecuteGroupAndPosition>();
		members.stream().map(r -> {
			Organization organization = organizationProvider.findOrganizationById(r.getOrganizationId());
			if(organization == null) {
				return null;
			}
			
			if(OrganizationGroupType.JOB_POSITION.equals(OrganizationGroupType.fromCode(organization.getGroupType()))) {
				List<OrganizationJobPositionMap> maps = organizationProvider.listOrganizationJobPositionMaps(organization.getId());
				if(maps != null && maps.size() > 0) {
					for(OrganizationJobPositionMap map : maps) {
						ExecuteGroupAndPosition group = new ExecuteGroupAndPosition();
						group.setGroupId(map.getOrganizationId());
						group.setPositionId(map.getJobPositionId());
						groupDtos.add(group);
						
						group.setGroupId(0L);
						group.setPositionId(map.getJobPositionId());
						groupDtos.add(group);
					}
						
				}
			} else {
				ExecuteGroupAndPosition group = new ExecuteGroupAndPosition();
				group.setGroupId(organization.getId());
				group.setPositionId(0L);
				groupDtos.add(group);
			}
			return null;
		});
		return groupDtos;
	}
	
	@Override
	public EquipmentsDTO findEquipment(DeleteEquipmentsCommand cmd) {

		EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());
		EquipmentsDTO dto = ConvertHelper.convert(equipment, EquipmentsDTO.class);
		Organization group = organizationProvider.findOrganizationById(dto.getTargetId());
		if(group != null)
			dto.setTargetName(group.getName());
		
//		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(equipment.getStandardId(), equipment.getOwnerType(), equipment.getOwnerId());
//        if(standard != null) {
//        	dto.setStandardName(standard.getName());
//        }
        
        List<EquipmentAccessoryMapDTO> eqAccessoryMap = new ArrayList<EquipmentAccessoryMapDTO>();

        List<EquipmentInspectionAccessoryMap> map = equipmentProvider.listAccessoryMapByEquipmentId(dto.getId());
        if(null != map) {
        	for(EquipmentInspectionAccessoryMap acMap : map) {
        		
        		EquipmentAccessoryMapDTO mapDto = ConvertHelper.convert(acMap, EquipmentAccessoryMapDTO.class);
        		EquipmentInspectionAccessories accessory = equipmentProvider.findAccessoryById(acMap.getAccessoryId());
        		EquipmentAccessoriesDTO accessoryDto = ConvertHelper.convert(accessory, EquipmentAccessoriesDTO.class);
        		Organization target = organizationProvider.findOrganizationById(accessoryDto.getTargetId());
        		if(target != null) {
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
        command.setAttachmentType((byte) 1);
        List<EquipmentAttachmentDTO> attachments = listAttachmentsByEquipmentId(command);
        dto.setAttachments(attachments);
		
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
        
        Timestamp startTime = null;
        Timestamp endTime = null;
        if(cmd.getStartTime() != null) {
        	startTime = new Timestamp(cmd.getStartTime());
        }
        if(cmd.getExpireTime() != null) {
        	endTime = new Timestamp(cmd.getExpireTime());
        }
        
		List<EquipmentInspectionTasks> tasks = equipmentProvider.listTasksByEquipmentId(cmd.getEquipmentId(), standardIds, startTime, endTime, locator, pageSize+1, null);
		
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

	@Override
	public EquipmentAccessoriesDTO findEquipmentAccessoriesById(
			DeleteEquipmentAccessoriesCommand cmd) {

		EquipmentInspectionAccessories accessory = verifyEquipmentAccessories(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
		if(accessory.getStatus() == 0) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_ACCESSORY_ALREADY_DELETED,
 				"备品备件已失效");
		}
		
		EquipmentAccessoriesDTO dto = ConvertHelper.convert(accessory, EquipmentAccessoriesDTO.class);
		return dto;
	}

	@Override
	public EquipmentTaskDTO listTaskById(ListTaskByIdCommand cmd) {
		EquipmentInspectionTasks task = verifyEquipmentTask(cmd.getTaskId(), cmd.getOwnerType(), cmd.getOwnerId());
		EquipmentTaskDTO dto = convertEquipmentTaskToDTO(task);
		
		return dto;
	}

	@Override
	public void createEquipmentCategory(CreateEquipmentCategoryCommand cmd) {

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Long parentId = cmd.getParentId();
		String path = "";
		Category category = getEquipmentCategory(parentId);
		path = category.getPath() + "/" + cmd.getName();
		
		category = categoryProvider.findCategoryByNamespaceAndName(parentId, namespaceId, cmd.getName());
//		category = categoryProvider.findCategoryByPath(namespaceId, path);
		if(category != null) {
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
		if(category == null) {
			LOGGER.error("equipment category not found, categoryId={}", categoryId);
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE, EquipmentServiceErrorCode.ERROR_EQUIPMENT_CATEGORY_NULL,
					"equipment category not found");
		}
		return category;
	}

	@Override
	public List<InspectionItemDTO> listParametersByStandardId(
			ListParametersByStandardIdCommand cmd) {
		EquipmentInspectionStandards standard = verifyEquipmentStandard(cmd.getStandardId(), cmd.getOwnerType(), cmd.getOwnerId());
	
		EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(standard.getTemplateId(), 
				cmd.getOwnerId(), cmd.getOwnerType());
		if(template == null || Status.INACTIVE.equals(Status.fromStatus(template.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_TEMPLATE_NOT_EXIST,
 				"模板不存在");
		}
		InspectionTemplateDTO dto = ConvertHelper.convert(template, InspectionTemplateDTO.class);
		
		List<InspectionItemDTO> items = listTemplateItems(dto);
		return items;
	}

	@Override
	public void createInspectionTemplate(CreateInspectionTemplateCommand cmd) {
		
		EquipmentInspectionTemplates template = ConvertHelper.convert(cmd, EquipmentInspectionTemplates.class);
		template.setCreatorUid(UserContext.current().getUser().getId());
		Long templateId = equipmentProvider.createEquipmentInspectionTemplates(template);
		
		List<InspectionItemDTO> items = cmd.getItems();
		if(items != null && items.size() > 0) {
			EquipmentInspectionTemplateItemMap map = new EquipmentInspectionTemplateItemMap();
			map.setTemplateId(templateId);
			for(InspectionItemDTO dto : items) {
				EquipmentInspectionItems item = ConvertHelper.convert(dto, EquipmentInspectionItems.class);
				Long itemId = equipmentProvider.createEquipmentInspectionItems(item);
				
				map.setItemId(itemId);
				equipmentProvider.createEquipmentInspectionTemplateItemMap(map);
			}
		}
		
		
	}

	@Override
	public void updateInspectionTemplate(UpdateInspectionTemplateCommand cmd) {
		EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType());
		if(template == null || Status.INACTIVE.equals(Status.fromStatus(template.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_TEMPLATE_NOT_EXIST,
 				"模板不存在");
		}
		if(!template.getName().equals(cmd.getName())) {
			template.setName(cmd.getName());
			equipmentProvider.updateEquipmentInspectionTemplates(template);
		}
		
		List<InspectionItemDTO> updateItems = cmd.getItems();
		List<EquipmentInspectionTemplateItemMap> maps = equipmentProvider.listEquipmentInspectionTemplateItemMap(template.getId());
		if(updateItems == null || updateItems.size() == 0) {
			if(maps != null && maps.size() > 0) {
				for(EquipmentInspectionTemplateItemMap map : maps) {
					equipmentProvider.deleteEquipmentInspectionTemplateItemMap(map.getId());
				}
			}
		} else {
			List<Long> updateItemIds = new ArrayList<Long>();
			
			//cmd item 不带id的create，其他的看map表中的itemId在不在cmd里面 不在的删掉
			for(InspectionItemDTO dto : updateItems) {
				if(dto.getId() == null) {
					
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
			
			for(EquipmentInspectionTemplateItemMap map : maps) {
				if(!updateItemIds.contains(map.getItemId())) {
					equipmentProvider.deleteEquipmentInspectionTemplateItemMap(map.getId());
				}
			}
		}
		
		
	}

	@Override
	public void deleteInspectionTemplate(DeleteInspectionTemplateCommand cmd) {
		EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(cmd.getId(), 
				cmd.getOwnerId(), cmd.getOwnerType());
		if(template == null || Status.INACTIVE.equals(Status.fromStatus(template.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_TEMPLATE_NOT_EXIST,
 				"模板不存在");
		}
		
		template.setStatus(Status.INACTIVE.getCode());
		template.setDeleteUid(UserContext.current().getUser().getId());
		template.setDeleteTime(new Timestamp(System.currentTimeMillis()));
		equipmentProvider.updateEquipmentInspectionTemplates(template);
		
		List<EquipmentInspectionStandards> standards = equipmentProvider.listEquipmentInspectionStandardsByTemplateId(template.getId());
		if(standards != null && standards.size() > 0) {
			for(EquipmentInspectionStandards standard : standards) {
				standard.setTemplateId(0L);
				equipmentProvider.updateEquipmentStandard(standard);
			}
		}
	}

	@Override
	public InspectionTemplateDTO findInspectionTemplate(
			DeleteInspectionTemplateCommand cmd) {
		EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(cmd.getId(), 
				cmd.getOwnerId(), cmd.getOwnerType());
		if(template == null || Status.INACTIVE.equals(Status.fromStatus(template.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_TEMPLATE_NOT_EXIST,
 				"模板不存在");
		}
		InspectionTemplateDTO dto = ConvertHelper.convert(template, InspectionTemplateDTO.class);
		List<InspectionItemDTO> items = listTemplateItems(dto);
		if(items != null && items.size() > 0) {
			dto.setItems(items);
		}
		
		return dto;
	}
	
	private List<InspectionItemDTO> listTemplateItems(InspectionTemplateDTO template) {
		List<EquipmentInspectionTemplateItemMap> maps = equipmentProvider.listEquipmentInspectionTemplateItemMap(template.getId());
		if(maps != null && maps.size() > 0) {
			List<InspectionItemDTO> items = new ArrayList<InspectionItemDTO>();
			for(EquipmentInspectionTemplateItemMap map : maps) {
				
				EquipmentInspectionItems item = equipmentProvider.findEquipmentInspectionItem(map.getItemId());
				if(item != null) {
					InspectionItemDTO itemDto = ConvertHelper.convert(item, InspectionItemDTO.class);
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
		List<InspectionTemplateDTO> dtos = new ArrayList<InspectionTemplateDTO>();
		List<EquipmentInspectionTemplates> templates = equipmentProvider.listInspectionTemplates(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getName());
		if(templates != null && templates.size() > 0) {
			for(EquipmentInspectionTemplates template : templates) {
				InspectionTemplateDTO dto = ConvertHelper.convert(template, InspectionTemplateDTO.class);
				dtos.add(dto);
			}
		}
		return dtos;
	}

	@Override
	public ListEquipmentTasksResponse listTasksByToken(
			ListTasksByTokenCommand cmd) {

		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentByQrCodeToken(cmd.getQrCodeToken());
		
		if(equipment == null) {
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
		 
		if(!isAdmin) {
			List<RoleAssignment> res = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), equipment.getTargetId(), EntityType.USER.getCode(), user.getId());
			if(null != res && 0 != res.size()){
				for (RoleAssignment resource : res) {
					if(resource.getRoleId() == RoleConstants.EQUIPMENT_MANAGER) {
						isAdmin = true;
						break;
					}
				}
			}
		}
		
		
		if(isAdmin) {
			List<Byte> taskStatus = new ArrayList<Byte>();
	        taskStatus.add(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
	        taskStatus.add(EquipmentTaskStatus.NEED_MAINTENANCE.getCode());
	        taskStatus.add(EquipmentTaskStatus.IN_MAINTENANCE.getCode());
			tasks = equipmentProvider.listTasksByEquipmentId(equipment.getId(), null, null, null, locator, pageSize+1, taskStatus);
			
		} else {
				List<Byte> taskStatus = new ArrayList<Byte>();
		        taskStatus.add(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
		        taskStatus.add(EquipmentTaskStatus.IN_MAINTENANCE.getCode());
				tasks = equipmentProvider.listTasksByEquipmentId(equipment.getId(), null, null, null, locator, pageSize+1, taskStatus);
		}
        
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

	@Override
	public List<EquipmentInspectionCategoryDTO> listEquipmentInspectionCategories(ListEquipmentInspectionCategoriesCommand cmd) {
		List<EquipmentInspectionCategories> categories = equipmentProvider.listEquipmentInspectionCategories(cmd.getOwnerId(), UserContext.getCurrentNamespaceId());
		List<EquipmentInspectionCategoryDTO> dtos = new ArrayList<EquipmentInspectionCategoryDTO>();
		if(categories != null && categories.size() > 0) {
			dtos = categories.stream().map(r -> {
				EquipmentInspectionCategoryDTO dto = ConvertHelper.convert(r, EquipmentInspectionCategoryDTO.class);
				return dto;
			}).collect(Collectors.toList());
		}
		
		return dtos;
	}

	@Override
	public EquipmentsDTO getInspectionObjectByQRCode(GetInspectionObjectByQRCodeCommand cmd) {

		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentByQrCodeToken(cmd.getQrCodeToken());
		
		if(equipment == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_EXIST,
 				"设备不存在");
		} 
		
		EquipmentsDTO dto = ConvertHelper.convert(equipment, EquipmentsDTO.class);
		
		return dto;
	}

	@Override
	public ListEquipmentTasksResponse listUserHistoryTasks(ListUserHistoryTasksCommand cmd) {
		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
		Long uId = UserContext.current().getUser().getId();
		Set<Long> taskIds = equipmentProvider.listRecordsTaskIdByOperatorId(uId, cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		List<Long> taskIdlist = new ArrayList<Long>();

        for(final Long value : taskIds){

        	taskIdlist.add(value);

        }

        Collections.sort(taskIdlist);
        Collections.reverse(taskIdlist);
        
        if(taskIdlist.size() > pageSize) {
        	taskIdlist.subList(0,pageSize-1);
        	response.setNextPageAnchor(taskIdlist.get(taskIdlist.size()-1));
        }
       
//		List<EquipmentInspectionTasks> tasks = equipmentProvider.listTaskByIds(taskIdlist);
//		List<QualityInspectionTaskRecords> records = new ArrayList<QualityInspectionTaskRecords>();
//        for(QualityInspectionTasks task : tasks) {
//        	QualityInspectionTaskRecords record = equipmentProvider.listLastRecordByTaskId(task.getId());
//        	if(record != null) {
//        		task.setRecord(record);
//            	records.add(task.getRecord());
//        	}
//        	
//        }
//
//		this.qualityProvider.populateRecordAttachments(records);
//		this.qualityProvider.populateRecordItemResults(records);
//
//		for(QualityInspectionTaskRecords record : records) {
//			populateRecordAttachements(record, record.getAttachments());
//		}
//        
//		List<QualityInspectionTaskDTO> dtoList = convertQualityInspectionTaskToDTO(tasks, uId);
//		response.setTasks(dtoList);
		return response;
	}

	@Override
	public StatEquipmentTasksResponse statEquipmentTasks(StatEquipmentTasksCommand cmd) {
		StatEquipmentTasksResponse response = new StatEquipmentTasksResponse();
		
		int offset = cmd.getOffset() == null ? 0 : cmd.getOffset();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		List<TaskCountDTO> tasks = equipmentProvider.statEquipmentTasks(cmd.getOwnerId(), cmd.getOwnerType(), 
				cmd.getTargetId(), cmd.getTargetType(), cmd.getInspectionCategoryId(), cmd.getStartTime(), cmd.getEndTime(),
				offset, pageSize+1);
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

				EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(task.getEquipmentId());
				if(equipment != null) {
					task.setEquipmentName(equipment.getName());
				}
				
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
				if(standard != null) {
					task.setStandardName(standard.getName());
				}
				Double maintanceRate =  ((double)(task.getCompleteMaintance() + task.getInMaintance() + task.getNeedMaintance()))/task.getTaskCount();
				task.setMaintanceRate(maintanceRate);
				
			}
		}
		response.setTasks(tasks);
		return response;
	}

}
