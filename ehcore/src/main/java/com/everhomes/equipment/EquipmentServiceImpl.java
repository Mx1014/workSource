package com.everhomes.equipment;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;


import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;


import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.appurl.AppUrlService;
import com.everhomes.forum.Attachment;
import com.everhomes.pmNotify.PmNotifyConfigurations;
import com.everhomes.pmNotify.PmNotifyProvider;
import com.everhomes.pmNotify.PmNotifyRecord;
import com.everhomes.pmNotify.PmNotifyService;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.ServiceModuleAuthorizationsDTO;
import com.everhomes.rest.appurl.AppUrlDTO;
import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.rest.equipment.*;
import com.everhomes.rest.pmNotify.*;
import com.everhomes.user.*;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.rest.equipment.*;

import com.everhomes.util.*;

import com.everhomes.util.doc.DocUtil;
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


import com.everhomes.acl.AclProvider;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationJobPosition;
import com.everhomes.organization.OrganizationJobPositionMap;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.repeat.RepeatService;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.category.CategoryDTO;

import com.everhomes.rest.forum.AttachmentDTO;

import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.ListOrganizationContactByJobPositionIdCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.ListOrganizationPersonnelByRoleIdsCommand;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationNaviFlag;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.rest.quality.ProcessType;
import com.everhomes.rest.quality.QualityGroupType;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.repeat.RepeatServiceErrorCode;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.rest.repeat.TimeRangeDTO;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;import com.everhomes.search.EquipmentAccessoriesSearcher;
import com.everhomes.search.EquipmentSearcher;
import com.everhomes.search.EquipmentStandardMapSearcher;
import com.everhomes.search.EquipmentStandardSearcher;
import com.everhomes.search.EquipmentTasksSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.rental.RentalServiceImpl;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import sun.misc.BASE64Encoder;

@Component
public class EquipmentServiceImpl implements EquipmentService {
	
	final String downloadDir ="\\download\\";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentServiceImpl.class);

	DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy/MM/dd");
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

	@Override
	public EquipmentStandardsDTO updateEquipmentStandard(
			UpdateEquipmentStandardCommand cmd) {
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_STANDARD_UPDATE, 0L);
		if(cmd.getTargetId() != null) {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		}


		User user = UserContext.current().getUser();
		RepeatSettings repeat = null;
		EquipmentInspectionStandards standard = null;
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("updateEquipmentStandard: userId = " + user.getId() + "time = " + DateHelper.currentGMTTime()
					+ "UpdateEquipmentStandardCommand cmd = {}" + cmd);
		}
		if(cmd.getId() == null) {
			standard = ConvertHelper.convert(cmd, EquipmentInspectionStandards.class);
			standard.setCreatorUid(user.getId());
			standard.setOperatorUid(user.getId());
			standard.setNamespaceId(UserContext.getCurrentNamespaceId());
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
			EquipmentInspectionStandards exist = verifyEquipmentStandard(cmd.getId());
			standard = ConvertHelper.convert(cmd, EquipmentInspectionStandards.class);
			standard.setRepeatSettingId(exist.getRepeatSettingId());
			standard.setStatus(exist.getStatus());
			standard.setOperatorUid(user.getId());
			standard.setNamespaceId(UserContext.getCurrentNamespaceId());
			
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
					if(EquipmentReviewStatus.REVIEWED.equals(EquipmentReviewStatus.fromStatus(map.getReviewStatus()))) {
						unReviewEquipmentStandardRelations(map);
					}
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

	private void unReviewEquipmentStandardRelations(EquipmentStandardMap map) {
		map.setReviewStatus(EquipmentReviewStatus.WAITING_FOR_APPROVAL.getCode());
		map.setReviewResult(ReviewResult.NONE.getCode());
		equipmentProvider.updateEquipmentStandardMap(map);
		equipmentStandardMapSearcher.feedDoc(map);

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

		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("processStandardGroups: deleteEquipmentInspectionStandardGroupMapByStandardId, standardId=" + standard.getId()
			+ "userId = " + UserContext.current().getUser().getId() + "time = " + DateHelper.currentGMTTime()
			+ "new standard groupList = {}" + groupList);
		}
        
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
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_STANDARD_DELETE, 0L);
		if(cmd.getTargetId() != null) {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		}

		User user = UserContext.current().getUser();

		EquipmentInspectionStandards standard = verifyEquipmentStandard(cmd.getStandardId());

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
			if ( !file.isFile() ) {
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
		if(dto.getRepeat() != null) {
			row.createCell(++i).setCellValue(repeatService.getExecutionFrequency(dto.getRepeat()));
			row.createCell(++i).setCellValue(repeatService.getExecuteStartTime(dto.getRepeat()));
			row.createCell(++i).setCellValue(repeatService.getlimitTime(dto.getRepeat()));
		} else {
			row.createCell(++i).setCellValue("");
			row.createCell(++i).setCellValue("");
			row.createCell(++i).setCellValue("");
		}

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
		
		EquipmentInspectionStandards standard = verifyEquipmentStandard(cmd.getStandardId());
		
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
	
	private EquipmentInspectionStandards verifyEquipmentStandard(Long standardId) {

		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(standardId);
		
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
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_RELATION_REVIEW, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);

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
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_RELATION_DELETE, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
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
	public void updateEquipments(UpdateEquipmentsCommand cmd) {
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_UPDATE, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);

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
			equipment.setNamespaceId(UserContext.getCurrentNamespaceId());
		
			
			if(cmd.getTargetId() == null || cmd.getTargetId() == 0L) {
				List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(cmd.getOwnerId());
				if(communities != null && communities.size() > 0) {
					for(CommunityDTO community : communities) {
						equipment.setTargetId(community.getId());
						equipment.setTargetType(OwnerType.COMMUNITY.getCode());
						
						String tokenString = UUID.randomUUID().toString();
						equipment.setQrCodeToken(tokenString);
						equipmentProvider.creatEquipmentInspectionEquipment(equipment);
						equipmentSearcher.feedDoc(equipment);
						
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
					}
				}
			} else {
				String tokenString = UUID.randomUUID().toString();
				equipment.setQrCodeToken(tokenString);
				equipmentProvider.creatEquipmentInspectionEquipment(equipment);
				equipmentSearcher.feedDoc(equipment);
				
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
				
				List<EquipmentAttachmentDTO> attachments = new ArrayList<EquipmentAttachmentDTO>();
			    List<EquipmentAccessoryMapDTO> eqAccessoryMap = new ArrayList<EquipmentAccessoryMapDTO>();
				
				if(cmd.getEqAccessoryMap() != null) {
					for(EquipmentAccessoryMapDTO map : cmd.getEqAccessoryMap()) {
						map.setEquipmentId(equipment.getId());
						updateEquipmentAccessoryMap(map);
						eqAccessoryMap.add(map);
					}
				}

				deleteEquipmentAttachmentsByEquipmentId(equipment.getId());
				if(cmd.getAttachments() != null) {
					for(EquipmentAttachmentDTO attachment : cmd.getAttachments()) {
						attachment.setEquipmentId(equipment.getId());
						updateEquipmentAttachment(attachment, user.getId());
						attachments.add(attachment);
					}
				}
			}
			
			
		} else {
			EquipmentInspectionEquipments exist = verifyEquipment(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
			equipment = ConvertHelper.convert(cmd, EquipmentInspectionEquipments.class);
			equipment.setGeohash(exist.getGeohash());
			equipment.setQrCodeToken(exist.getQrCodeToken());
			equipment.setNamespaceId(UserContext.getCurrentNamespaceId());
			
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
			equipmentSearcher.feedDoc(equipment);

			//不带id的create，其他的看map表中的standardId在不在cmd里面 不在的删掉
			List<Long> updateStandardIds = new ArrayList<Long>();
			if(eqStandardMap != null && eqStandardMap.size() > 0) {

				for(EquipmentStandardMapDTO dto : eqStandardMap) {
					List<EquipmentStandardMap> maps = equipmentProvider.findEquipmentStandardMap(dto.getStandardId(),
							equipment.getId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
					LOGGER.debug("equipment standard maps: {}", maps);
					if(maps == null || maps.size() == 0) {
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
					else if(maps.size() > 0) {
						//删除设备多次重复绑定的标准,仅保留最早绑的那个
						updateStandardIds.add(maps.get(0).getStandardId());
						maps.remove(0);
						LOGGER.debug("equipment standard maps after remove: {}", maps);
						if(maps.size() > 0) {
							maps.forEach(map -> {
								map.setReviewStatus(EquipmentReviewStatus.INACTIVE.getCode());
								equipmentProvider.updateEquipmentStandardMap(map);
								equipmentStandardMapSearcher.feedDoc(map);
							});
						}

					}

				}


				
			}

			List<EquipmentStandardMap> maps = equipmentProvider.findByTarget(equipment.getId(), InspectionStandardMapTargetType.EQUIPMENT.getCode());
			for(EquipmentStandardMap map : maps) {
				if(!updateStandardIds.contains(map.getStandardId())) {
					map.setReviewStatus(EquipmentReviewStatus.INACTIVE.getCode());
					equipmentProvider.updateEquipmentStandardMap(map);
					equipmentStandardMapSearcher.feedDoc(map);

					inactiveTasks(equipment.getId(), map.getStandardId());
				}
			}
			
			List<EquipmentAttachmentDTO> attachments = new ArrayList<EquipmentAttachmentDTO>();
		    List<EquipmentAccessoryMapDTO> eqAccessoryMap = new ArrayList<EquipmentAccessoryMapDTO>();
			
			if(cmd.getEqAccessoryMap() != null) {
				for(EquipmentAccessoryMapDTO map : cmd.getEqAccessoryMap()) {
					map.setEquipmentId(equipment.getId());
					updateEquipmentAccessoryMap(map);
					eqAccessoryMap.add(map);
				}
			}
			deleteEquipmentAttachmentsByEquipmentId(equipment.getId());
			if(cmd.getAttachments() != null) {
				for(EquipmentAttachmentDTO attachment : cmd.getAttachments()) {
					attachment.setEquipmentId(equipment.getId());
					updateEquipmentAttachment(attachment, user.getId());
					attachments.add(attachment);
				}
			}
			
		}
		
//		equipmentSearcher.feedDoc(equipment);
//		
////		EquipmentQrCodeTokenDTO qrCodeToken = new EquipmentQrCodeTokenDTO();
////		qrCodeToken.setEquipmentId(equipment.getId());
////		qrCodeToken.setOwnerId(equipment.getOwnerId());
////		qrCodeToken.setOwnerType(equipment.getOwnerType());
////		qrCodeToken.setUpdateTime(equipment.getUpdateTime());
////		String tokenString = WebTokenGenerator.getInstance().toWebToken(qrCodeToken);
//		String tokenString = UUID.randomUUID().toString();
//		equipment.setQrCodeToken(tokenString);
//		equipmentProvider.updateEquipmentInspectionEquipment(equipment);
		
//		EquipmentsDTO dto = ConvertHelper.convert(equipment, EquipmentsDTO.class);
//		Organization group = organizationProvider.findOrganizationById(dto.getTargetId());
//		if(group != null)
//			dto.setTargetName(group.getName());
//
////		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(equipment.getStandardId(), equipment.getOwnerType(), equipment.getOwnerId());
////        if(standard != null) {
////        	dto.setStandardName(standard.getName());
////        }
//		
//		dto.setAttachments(attachments);
//		dto.setEqAccessoryMap(eqAccessoryMap);
//		
//		populateEquipmentStandards(dto);
		
//		return dto;
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

	private void deleteEquipmentAttachmentsByEquipmentId(Long equipmentId) {
		List<EquipmentInspectionEquipmentAttachments> attachments = equipmentProvider.findEquipmentAttachmentsByEquipmentId(equipmentId);
		if(attachments != null && attachments.size() > 0) {
			attachments.forEach(attachment -> {
				equipmentProvider.deleteEquipmentAttachmentById(attachment.getId());
			});
		}
	}

	private void updateEquipmentAttachment(EquipmentAttachmentDTO dto, Long uid) {
		
		EquipmentInspectionEquipmentAttachments attachment = ConvertHelper.convert(dto, 
				EquipmentInspectionEquipmentAttachments.class);
		
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

		if(EquipmentStatus.INACTIVE.equals(EquipmentStatus.fromStatus(equipment.getStatus()))) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_ALREADY_DELETED,
 				"设备已删除");
		}
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_DELETE, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), equipment.getTargetId(), cmd.getOwnerId(), privilegeId);

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
		LOGGER.info("filePath:{}", filePath);
		
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
			accessory.setNamespaceId(UserContext.getCurrentNamespaceId());
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

//		EquipmentInspectionAccessories accessory = equipmentProvider.findAccessoryById(accessoryId, ownerType, ownerId);
		EquipmentInspectionAccessories accessory = equipmentProvider.findAccessoryById(accessoryId);
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
		
		URL rootPath = EquipmentServiceImpl.class.getResource("/");
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
					result.setCommunityId(task.getTargetId());
					result.setStandardId(task.getStandardId());
					result.setEquipmentId(task.getEquipmentId());
					result.setInspectionCategoryId(task.getInspectionCategoryId());
					result.setNamespaceId(task.getNamespaceId());
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

		log.setInspectionCategoryId(task.getInspectionCategoryId());
		log.setCommunityId(task.getTargetId());
		log.setNamespaceId(task.getNamespaceId());
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
		long startTime = System.currentTimeMillis();
		EquipmentTaskDTO dto = ConvertHelper.convert(task, EquipmentTaskDTO.class);  

//总公司 分公司 by xiongying20170328
		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
//		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId(),
//				task.getOwnerType(), task.getOwnerId());
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
    	
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(task.getEquipmentId());
//		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(task.getEquipmentId(), task.getOwnerType(), task.getOwnerId());
        if(null != equipment) {
        	dto.setEquipmentName(equipment.getName());
        	dto.setEquipmentLocation(equipment.getLocation());
        	dto.setQrCodeFlag(equipment.getQrCodeFlag());
			dto.setPictureFlag(equipment.getPictureFlag());
        }
        
        Organization group = organizationProvider.findOrganizationById(task.getExecutiveGroupId());
		OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(task.getPositionId());
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

		if(task.getExecutorId() != null && task.getExecutorId() != 0) {
			//总公司分公司 by xiongying20170328
			List<OrganizationMember> executors = organizationProvider.listOrganizationMembersByUId(task.getExecutorId());
//            	OrganizationMember executor = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getExecutorId(), task.getOwnerId());
			if(executors != null && executors.size() > 0) {
				dto.setExecutorName(executors.get(0).getContactName());
			}
		}

		if(task.getOperatorId() != null && task.getOperatorId() != 0) {
			List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(task.getOperatorId());
			if(operators != null && operators.size() > 0) {
				dto.setOperatorName(operators.get(0).getContactName());
			}
		}

		if(task.getReviewerId() != null && task.getReviewerId() != 0) {
			List<OrganizationMember> reviewers = organizationProvider.listOrganizationMembersByUId(task.getReviewerId());
			if(reviewers != null && reviewers.size() > 0) {
				dto.setReviewerName(reviewers.get(0).getContactName());
			}
		}
    	
//    	if(task.getExecutorId() != null && task.getExecutorId() != 0) {
//        	OrganizationMember executor = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getExecutorId(), task.getOwnerId());
//        	if(executor != null) {
//        		dto.setExecutorName(executor.getContactName());
//        	}
//    	}
//
//    	if(task.getOperatorId() != null && task.getOperatorId() != 0) {
//    		OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getOperatorId(), task.getOwnerId());
//        	if(operator != null) {
//        		dto.setOperatorName(operator.getContactName());
//        	}
//    	}
    	
//    	if(task.getReviewerId() != null && task.getReviewerId() != 0) {
//    		OrganizationMember reviewers = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getReviewerId(), task.getOwnerId());
//        	if(reviewers != null) {
//        		dto.setReviewerName(reviewers.getContactName());
//        	}
//    	}

		long endTime = System.currentTimeMillis();

		LOGGER.debug("TrackUserRelatedCost: convertEquipmentTaskToDTO elapse: " + (endTime - startTime));

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
					 attachment.setContentUrl(convertAttachmentURL(url));
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
//			OrganizationMember reviewer = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getReviewerId(), task.getOwnerId());
//			OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getOperatorId(), task.getExecutiveGroupId());
			List<OrganizationMember> reviewers = organizationProvider.listOrganizationMembersByUId(task.getReviewerId());
			List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(task.getOperatorId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("reviewerName", reviewers.get(0).getContactName());
			map.put("operatorName", operators.get(0).getContactName());
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

	@Override
	public void reviewEquipmentTasks(ReviewEquipmentTasksCommand cmd) {
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
		task.setNamespaceId(equipment.getNamespaceId());
		task.setTargetId(equipment.getTargetId());
		task.setTargetType(equipment.getTargetType());
		task.setInspectionCategoryId(equipment.getInspectionCategoryId());
		task.setStandardId(standardDto.getId());
		task.setEquipmentId(equipment.getId());
		task.setTaskNumber(standardDto.getStandardNumber());
		task.setStandardType(standardDto.getStandardType());
//		task.setExecutiveGroupType(equipment.getTargetType());
//		task.setExecutiveGroupId(equipment.getTargetId());
		task.setStatus(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
		task.setResult(EquipmentTaskResult.NONE.getCode());
		task.setReviewResult(ReviewResult.NONE.getCode());
		
		StandardType type = StandardType.fromStatus(standardDto.getStandardType());
		
		List<TimeRangeDTO> timeRanges = repeatService.analyzeTimeRange(standardDto.getRepeat().getTimeRanges());
//		for(StandardGroupDTO executiveGroup : standardDto.getExecutiveGroup()) {
//
//			task.setExecutiveGroupId(executiveGroup.getGroupId());
//			task.setPositionId(executiveGroup.getPositionId());
				
		if(timeRanges != null && timeRanges.size() > 0) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("creatTaskByStandard, timeRanges = " + timeRanges);
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
				//标准名称+巡检/保养+当日日期6位(年的后两位+月份+天)+两位序号（系统从01开始生成）
				if (i < 10) {

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

//				启动提醒
				ListPmNotifyParamsCommand command = new ListPmNotifyParamsCommand();
				command.setCommunityId(task.getTargetId());
				command.setNamespaceId(task.getNamespaceId());
				List<PmNotifyParamDTO> paramDTOs = listPmNotifyParams(command);
				if(paramDTOs != null && paramDTOs.size() > 0) {
					for (PmNotifyParamDTO notifyParamDTO : paramDTOs) {
						List<PmNotifyReceiverDTO> receivers = notifyParamDTO.getReceivers();
						if(receivers != null && receivers.size() > 0) {
							PmNotifyRecord record = ConvertHelper.convert(notifyParamDTO, PmNotifyRecord.class);
							PmNotifyReceiverList receiverList = new PmNotifyReceiverList();
							List<PmNotifyReceiver> pmNotifyReceivers = new ArrayList<>();
							receivers.forEach(receiver -> {
								PmNotifyReceiver pmNotifyReceiver = new PmNotifyReceiver();
								pmNotifyReceiver.setReceiverType(receiver.getReceiverType());
								List<Long> ids = receiver.getReceivers().stream().map(receiverName -> {
									return receiverName.getId();
								}).collect(Collectors.toList());
								pmNotifyReceiver.setReceiverIds(ids);
								pmNotifyReceivers.add(pmNotifyReceiver);
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
		
		if(task == null) {
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
				if(QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(groupType))) {
					notifyFlag = configurationProvider.getBooleanValue(task.getNamespaceId(), ConfigConstants.EQUIPMENT_TASK_NOTIFY_DALAY, false);
					//只有过期未执行的要通知
					if(!EquipmentTaskStatus.WAITING_FOR_EXECUTING.equals(task.getStatus())) {
						notifyFlag = false;
					}
				}

				//五分钟后启动通知
				if(notifyFlag) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("deadline", timeToStr(task.getExecutiveExpireTime()));
					int code = EquipmentNotificationTemplateCode.GENERATE_EQUIPMENT_TASK_NOTIFY;
					if(QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(groupType))) {
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
								LOGGER.info("sendTaskMsg, groupType = {}" + groupType +", group = {}" + executiveGroup + ", members = {}" + members);
							}

							if (members != null) {
								for (OrganizationMember member : members) {
									sendMessageToUser(member.getTargetId(), notifyTextForApplicant);
								}
							}
						}
					}
					//审阅的要通知超管和模块管理员
					if(QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(groupType))) {
						ListServiceModuleAdministratorsCommand command = new ListServiceModuleAdministratorsCommand();
						command.setOrganizationId(task.getOwnerId());
						command.setOwnerId(task.getOwnerId());
						command.setOwnerType(EntityType.ORGANIZATIONS.getCode());
						List<OrganizationContactDTO> admins = rolePrivilegeService.listOrganizationSuperAdministrators(command);
						if(admins != null && admins.size() > 0) {
							for(OrganizationContactDTO admin : admins) {
								sendMessageToUser(admin.getTargetId(), notifyTextForApplicant);
							}
						}

						ListServiceModuleAdministratorsCommand moduleCommand = new ListServiceModuleAdministratorsCommand();
						moduleCommand.setOrganizationId(task.getOwnerId());
						moduleCommand.setOwnerId(task.getOwnerId());
						moduleCommand.setOwnerType(EntityType.ORGANIZATIONS.getCode());
						moduleCommand.setModuleId(20800L);
						List<ServiceModuleAuthorizationsDTO> moduleAdmins = rolePrivilegeService.listServiceModuleAdministrators(moduleCommand);
						if(moduleAdmins != null && moduleAdmins.size() > 0) {
							for(ServiceModuleAuthorizationsDTO moduleAdmin : moduleAdmins) {
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

//		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(equipmentId, ownerType, ownerId);

		//改用namespaceId by xiongying20170328
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(equipmentId, UserContext.getCurrentNamespaceId());
		
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
        
        EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
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

			//总公司 分公司 by xiongying20170328
			if(r.getOperatorId() != null && r.getOperatorId() != 0) {
				List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(r.getOperatorId());
				if(operators != null && operators.size() > 0) {
					dto.setOperatorName(operators.get(0).getContactName());
				}
			}

			if(r.getTargetId() != null && r.getTargetId() != 0) {
				List<OrganizationMember> targets = organizationProvider.listOrganizationMembersByUId(r.getTargetId());
				if(targets != null && targets.size() > 0) {
					dto.setTargetName(targets.get(0).getContactName());
				}
			}
//        	if(r.getOperatorId() != null && r.getOperatorId() != 0) {
//        		OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(r.getOperatorId(), task.getOwnerId());
//            	if(operator != null) {
//            		dto.setOperatorName(operator.getContactName());
//            	}
//        	}
//        	if(r.getTargetId() != null && r.getTargetId() != 0) {
//        		OrganizationMember target = organizationProvider.findOrganizationMemberByOrgIdAndUId(r.getTargetId(), task.getExecutiveGroupId());
//            	if(target != null) {
//            		dto.setTargetName(target.getContactName());
//            	}
//        	}
        	
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
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_STANDARD_UPDATE, 0L);
		if(cmd.getTargetId() != null) {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		}
		ImportDataResponse importDataResponse = importData(cmd, mfile, userId, ImportDataType.EQUIPMENT_STANDARDS.getCode());
		return importDataResponse;
	}

	@Override
	public ImportDataResponse importEquipments(ImportOwnerCommand cmd, MultipartFile mfile, Long userId) {
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_UPDATE, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
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

		Integer namespaceId = UserContext.getCurrentNamespaceId();

		for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
				EquipmentInspectionStandards standard = new EquipmentInspectionStandards();
				if(StringUtils.isNotBlank(s[0]) && !"null".equals(s[0])) {
					standard.setStandardNumber(s[0]);
				} else {
					standard.setStandardNumber("");
				}

				if(StringUtils.isNotBlank(s[1]) && !"null".equals(s[1])) {
					standard.setName(s[1]);
				} else {
					standard.setName("");
				}

				standard.setStandardType(StandardType.fromName(s[2]).getCode());

				if(StringUtils.isNotBlank(s[3]) && !"null".equals(s[3])) {
					standard.setStandardSource(s[3]);
				} else {
					standard.setStandardSource("");
				}

				if(StringUtils.isNotBlank(s[4]) && !"null".equals(s[4])) {
					standard.setDescription(s[4]);
				} else {
					standard.setDescription("");
				}

				if(StringUtils.isNotBlank(s[5]) && !"null".equals(s[5])) {
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
	
	private List<String> importEquipmentsData(ImportOwnerCommand cmd, List<String> list, Long userId){
		List<String> errorDataLogs = new ArrayList<String>();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		for (String str : list) {
			String[] s = str.split("\\|\\|");
//			dbProvider.execute((TransactionStatus status) -> {
				EquipmentInspectionEquipments equipment = new EquipmentInspectionEquipments();
				equipment.setCustomNumber(s[1]);
				equipment.setName(s[2]);
				equipment.setEquipmentModel(s[3]);
				equipment.setParameter(s[4]);
				equipment.setManufacturer(s[5]);
				if(!StringUtils.isBlank(s[6])) {
					equipment.setInstallationTime(dateStrToTimestamp(s[6]));
				}
				if(!StringUtils.isBlank(s[7])) {
					equipment.setRepairTime(dateStrToTimestamp(s[7]));
				}
				equipment.setLocation(s[8]);
				equipment.setQuantity(Long.valueOf(s[9]));
				if(!StringUtils.isEmpty(s[10]) && !"null".equals(s[10])) {
					equipment.setRemarks(s[10]);
				}
				equipment.setNamespaceId(namespaceId);
				equipment.setOwnerType(cmd.getOwnerType());
				equipment.setOwnerId(cmd.getOwnerId());
				equipment.setTargetType(cmd.getTargetType());
				equipment.setTargetId(cmd.getTargetId());
				equipment.setInspectionCategoryId(cmd.getInspectionCategoryId());
				equipment.setStatus(EquipmentStatus.INCOMPLETE.getCode());
				String tokenString = UUID.randomUUID().toString();
				equipment.setQrCodeToken(tokenString);
				equipment.setCreatorUid(userId);
				equipment.setOperatorUid(userId);
				LOGGER.info("add equipment");
				equipmentProvider.creatEquipmentInspectionEquipment(equipment);
				equipmentSearcher.feedDoc(equipment);
//				return null;
//			});
		}
		return errorDataLogs;
		
	}

	private Timestamp dateStrToTimestamp(String str) {
		LocalDate localDate = LocalDate.parse(str,dateSF);
		Timestamp ts = new Timestamp(Date.valueOf(localDate).getTime());
		return ts;
	}
	
	private List<String> importEquipmentAccessoriesData(ImportOwnerCommand cmd, List<String> list, Long userId){
		List<String> errorDataLogs = new ArrayList<String>();

		Integer namespaceId = UserContext.getCurrentNamespaceId();

		for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
				EquipmentInspectionAccessories accessory = new EquipmentInspectionAccessories();
				accessory.setName(s[0]);
				accessory.setManufacturer(s[1]);;
				accessory.setModelNumber(s[2]);;
				accessory.setSpecification(s[3]);;
				accessory.setLocation(s[4]);;
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
			sb.append(r.getK()).append("||");

			
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

	private Timestamp addMonths(Timestamp now, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.MONTH, months);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());

		return time;
	}

	private ListEquipmentTasksResponse listDelayTasks(ListEquipmentTasksCommand cmd) {
		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
		int pageSize = cmd.getPageSize() == null ? Integer.MAX_VALUE - 1 : cmd.getPageSize();
		if(null == cmd.getPageAnchor()) {
			cmd.setPageAnchor(0L);
		}
		Integer offset = cmd.getPageAnchor().intValue();
		User user = UserContext.current().getUser();
		Long userId = user.getId();
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
					userId =0L;
					break;
				}
			}
		}
		//只展示近一个月的
		Timestamp startTime = addMonths(new Timestamp(System.currentTimeMillis()), -1);
		List<EquipmentInspectionTasks> tasks = null;
		if(isAdmin) {
			tasks = equipmentProvider.listDelayTasks(cmd.getInspectionCategoryId(), null, cmd.getTargetType(),
						cmd.getTargetId(), offset, pageSize, AdminFlag.YES.getCode(), startTime);
		}
		if(!isAdmin) {
			List<Long> standards = new ArrayList<>();
			List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
			List<EquipmentInspectionStandardGroupMap> maps = equipmentProvider.listEquipmentInspectionStandardGroupMapByGroupAndPosition(groupDtos, null);
			if (maps != null && maps.size() > 0) {
				for (EquipmentInspectionStandardGroupMap r : maps) {
						standards.add(r.getStandardId());
				}
			}
			tasks = equipmentProvider.listDelayTasks(cmd.getInspectionCategoryId(), standards, cmd.getTargetType(),
						cmd.getTargetId(), offset, pageSize, AdminFlag.NO.getCode(), startTime);
		}

		if (tasks.size() > pageSize) {
			tasks.remove(tasks.size() - 1);
			response.setNextPageAnchor((long) (offset + 1));
		}

		Set<Long> taskEquipmentIds = tasks.stream().map(r -> {
			return r.getEquipmentId();
		}).filter(r -> r != null).collect(Collectors.toSet());


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
		}).filter(r -> r != null).collect(Collectors.toList());

		response.setTasks(dtos);
		return response;
	}

	@Override
	public ListEquipmentTasksResponse listEquipmentTasks(
			ListEquipmentTasksCommand cmd) {
		if(cmd.getTaskStatus() != null && cmd.getTaskStatus().size() == 1
				&& EquipmentTaskStatus.DELAY.equals(EquipmentTaskStatus.fromStatus(cmd.getTaskStatus().get(0)))) {
			return listDelayTasks(cmd);
		}

		long startTime = System.currentTimeMillis();
		ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
		User user = UserContext.current().getUser();
		
		int pageSize = cmd.getPageSize() == null ? Integer.MAX_VALUE - 1 : cmd.getPageSize();

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

		Long userId = user.getId();
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
					userId =0L;
					break;
				}
			}
		}
		List<EquipmentInspectionTasks> allTasks = null;

//		Organization organization = organizationProvider.findOrganizationById(cmd.getOwnerId());
//		List<Long> ownerIds = new ArrayList<>();
//
//		if(organization != null) {
//			String[] path = organization.getPath().split("/");
//			for (int i = path.length - 1; i > 0; i--) {
//				ownerIds.add(Long.valueOf(path[i]));
//			}
//		}

		if(isAdmin) {

			String cacheKey = convertListEquipmentInspectionTasksCache(cmd.getTaskStatus(), cmd.getInspectionCategoryId(),
					targetTypes, targetIds, null, null, offset, userId);
			allTasks = equipmentProvider.listEquipmentInspectionTasksUseCache(cmd.getTaskStatus(), cmd.getInspectionCategoryId(),
					targetTypes, targetIds, null, null, offset, pageSize + 1, cacheKey, AdminFlag.YES.getCode());

		}
		if(!isAdmin) {
			List<Long> executeStandardIds = new ArrayList<>();
			List<Long> reviewStandardIds = new ArrayList<>();
			List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
			List<EquipmentInspectionStandardGroupMap> maps = equipmentProvider.listEquipmentInspectionStandardGroupMapByGroupAndPosition(groupDtos, null);
			if (maps != null && maps.size() > 0) {

				for (EquipmentInspectionStandardGroupMap r : maps) {
					if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(r.getGroupType()))) {
						reviewStandardIds.add(r.getStandardId());
					}
					if (QualityGroupType.EXECUTIVE_GROUP.equals(QualityGroupType.fromStatus(r.getGroupType()))) {
						executeStandardIds.add(r.getStandardId());
					}
				}
			}



			String cacheKey = convertListEquipmentInspectionTasksCache(cmd.getTaskStatus(), cmd.getInspectionCategoryId(), targetTypes, targetIds,
					executeStandardIds, reviewStandardIds, offset, userId);


			allTasks = equipmentProvider.listEquipmentInspectionTasksUseCache(cmd.getTaskStatus(), cmd.getInspectionCategoryId(),
					targetTypes, targetIds, executeStandardIds, reviewStandardIds, offset, pageSize + 1, cacheKey, AdminFlag.NO.getCode());


		}


		if (allTasks.size() > pageSize) {
			allTasks.remove(allTasks.size() - 1);
			response.setNextPageAnchor((long) (offset + 1));
		}

		Timestamp current = new Timestamp(System.currentTimeMillis());
		tasks = allTasks.stream().map(r -> {
			if ((EquipmentTaskStatus.WAITING_FOR_EXECUTING.equals(EquipmentTaskStatus.fromStatus(r.getStatus()))
					|| EquipmentTaskStatus.IN_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(r.getStatus())))
					&& r.getExecutiveExpireTime() != null && current.before(r.getExecutiveExpireTime())) {
				return r;
			} else if (EquipmentTaskStatus.IN_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(r.getStatus())) &&
					r.getProcessExpireTime() != null && current.before(r.getProcessExpireTime())) {
				return r;
			} else if (EquipmentTaskStatus.NEED_MAINTENANCE.equals(EquipmentTaskStatus.fromStatus(r.getStatus()))
					|| EquipmentTaskStatus.CLOSE.equals(EquipmentTaskStatus.fromStatus(r.getStatus()))) {
				return r;
			}

			return null;
		}).filter(r -> r != null).collect(Collectors.toList());


		long startTime2 = System.currentTimeMillis();
		Set<Long> taskEquipmentIds = tasks.stream().map(r -> {
			return r.getEquipmentId();
		}).filter(r -> r != null).collect(Collectors.toSet());


		Map<Long, EquipmentInspectionEquipments> equipmentsMap = equipmentProvider.listEquipmentsById(taskEquipmentIds);
		List<EquipmentTaskDTO> dtos = tasks.stream().map(r -> {
			EquipmentTaskDTO dto = ConvertHelper.convert(r, EquipmentTaskDTO.class);
			EquipmentInspectionEquipments equipment = equipmentsMap.get(dto.getEquipmentId());
//			EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(r.getEquipmentId());
			if (equipment != null) {
				dto.setEquipmentLocation(equipment.getLocation());
				dto.setQrCodeFlag(equipment.getQrCodeFlag());
				dto.setEquipmentName(equipment.getName());
			}

			return dto;
		}).filter(r -> r != null).collect(Collectors.toList());

		response.setTasks(dtos);

		long endTime = System.currentTimeMillis();
		LOGGER.debug("TrackUserRelatedCost: listEquipmentTasks total elapse:{}, convertEquipmentTaskDTO resultSize:{}, convert time:{}",
				(endTime - startTime), tasks.size(), (endTime - startTime2));
		return response;

	}


	private String convertListEquipmentInspectionTasksCache(List<Byte> taskStatus, Long inspectionCategoryId,List<String> targetType, List<Long> targetId,
				List<Long> executeStandardIds, List<Long> reviewStandardIds, Integer offset, Long userId) {

		StringBuilder sb = new StringBuilder();

		if(inspectionCategoryId == null) {
			inspectionCategoryId = -1L;
		}

		sb.append(userId);
//		sb.append("-");
//		sb.append(ownerType);
//		sb.append("-");
//		sb.append(ownerIds);
		sb.append("-community-");
		if(targetId != null && targetId.size() > 0) {
			sb.append(targetId.get(0));
		}
		sb.append(taskStatus);
		sb.append("-");
		sb.append(inspectionCategoryId);
		sb.append("-");
		sb.append(offset);
		sb.append("-");
		if(executeStandardIds == null) {
			sb.append("all");
		} else {
			Collections.sort(executeStandardIds);
			sb.append(executeStandardIds);
		}

		if(reviewStandardIds == null) {
			sb.append("all");
		} else {
			Collections.sort(reviewStandardIds);
			sb.append(reviewStandardIds);
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
		if(members == null || members.size() == 0) {
			return new ArrayList<ExecuteGroupAndPosition>();
		}
		
		List<ExecuteGroupAndPosition> groupDtos = new ArrayList<ExecuteGroupAndPosition>();
		for(OrganizationMember member : members) {
			Organization organization = organizationProvider.findOrganizationById(member.getOrganizationId());
			
			if(organization != null) {
				if(LOGGER.isInfoEnabled()) {
	                LOGGER.info("listUserRelateGroups, organizationId=" + organization.getId());
	            }
				if(OrganizationGroupType.JOB_POSITION.equals(OrganizationGroupType.fromCode(organization.getGroupType()))) {

					List<OrganizationJobPositionMap> maps = organizationProvider.listOrganizationJobPositionMaps(organization.getId());
					if(LOGGER.isInfoEnabled()) {
		                LOGGER.info("listUserRelateGroups, organizationId = {}, OrganizationJobPositionMaps = {}" , organization.getId(), maps);
		            }
					
					if(maps != null && maps.size() > 0) {
						for(OrganizationJobPositionMap map : maps) {
							ExecuteGroupAndPosition group = new ExecuteGroupAndPosition();
							group.setGroupId(organization.getParentId());//具体岗位所属的部门公司组等 by xiongying20170619
							group.setPositionId(map.getJobPositionId());
							groupDtos.add(group);

//							Organization groupOrg = organizationProvider.findOrganizationById(map.getOrganizationId());
//							if(groupOrg != null) {
//								//取path后的第一个路径 为顶层公司 by xiongying 20170323
								String[] path = organization.getPath().split("/");
								Long organizationId = Long.valueOf(path[1]);
								ExecuteGroupAndPosition topGroup = new ExecuteGroupAndPosition();
								topGroup.setGroupId(organizationId);
								topGroup.setPositionId(map.getJobPositionId());
								groupDtos.add(topGroup);
//							}

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
	public EquipmentsDTO findEquipment(DeleteEquipmentsCommand cmd) {

//		EquipmentInspectionEquipments equipment = verifyEquipment(cmd.getEquipmentId(), cmd.getOwnerType(), cmd.getOwnerId());
		//分公司查不到总公司的设备 by xiongying20170323
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(cmd.getEquipmentId());
		if(equipment == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_NOT_EXIST,
					"设备不存在");
		}
		EquipmentsDTO dto = ConvertHelper.convert(equipment, EquipmentsDTO.class);
		Community community = communityProvider.findCommunityById(dto.getTargetId());
		if(community != null)
			dto.setTargetName(community.getName());
//		Organization group = organizationProvider.findOrganizationById(dto.getTargetId());
//		if(group != null)
//			dto.setTargetName(group.getName());
		
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

	private String convertAttachmentURL(String url) {
		String[] urls = url.split("&");
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < urls.length; i++) {
			if(i == 0) {
				sb.append(urls[i]);
			} else if(i < urls.length-2) {
				sb.append("&");
				sb.append(urls[i]);
			} else if(i == urls.length-2) {
				sb.append("&w=600");
			} else if(i == urls.length-1) {
				sb.append("&h=800");
			}

		}

		return sb.toString();
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
					 dto.setContentUrl(convertAttachmentURL(url));
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
//		EquipmentInspectionTasks task = verifyEquipmentTask(cmd.getTaskId(), cmd.getOwnerType(), cmd.getOwnerId());
		//分公司拿不到ownerId为总公司的数据 by xiongying20170323
		EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(cmd.getTaskId());

		if(task == null) {
			throw RuntimeErrorException.errorWith(EquipmentServiceErrorCode.SCOPE,
					EquipmentServiceErrorCode.ERROR_EQUIPMENT_TASK_NOT_EXIST,
					"任务不存在");
		}
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
		EquipmentInspectionStandards standard = verifyEquipmentStandard(cmd.getStandardId());

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
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_ITEM_CREATE, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		
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
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_ITEM_UPDATE, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
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
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_ITEM_DELETE, 0L);
		userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
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
//		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_ITEM_LIST, 0L);
//		userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		List<InspectionTemplateDTO> dtos = new ArrayList<InspectionTemplateDTO>();
		List<EquipmentInspectionTemplates> templates = equipmentProvider.listInspectionTemplates(UserContext.getCurrentNamespaceId(), cmd.getName());
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
			//扫码任务做权限控制 只能扫出设备下有执行权限的任务
			List<StandardAndStatus> standards = new ArrayList<>();
			List<Byte> executeTaskStatus = new ArrayList<Byte>();
			executeTaskStatus.add(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
			executeTaskStatus.add(EquipmentTaskStatus.IN_MAINTENANCE.getCode());

			List<Byte> reviewTaskStatus = new ArrayList<Byte>();
			reviewTaskStatus.add(EquipmentTaskStatus.NEED_MAINTENANCE.getCode());

			List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
			List<EquipmentInspectionStandardGroupMap> maps = equipmentProvider.listEquipmentInspectionStandardGroupMapByGroupAndPosition(groupDtos, null);
			if (maps != null && maps.size() > 0) {
				for (EquipmentInspectionStandardGroupMap r : maps) {
					StandardAndStatus standardAndStatus = new StandardAndStatus();
					standardAndStatus.setStandardId(r.getStandardId());
					if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(r.getGroupType()))) {
						standardAndStatus.setTaskStatus(reviewTaskStatus);
					}
					if (QualityGroupType.EXECUTIVE_GROUP.equals(QualityGroupType.fromStatus(r.getGroupType()))) {
						standardAndStatus.setTaskStatus(executeTaskStatus);
					}
					standards.add(standardAndStatus);
				}
			}
			tasks = equipmentProvider.listTasksByEquipmentIdAndStandards(equipment.getId(), standards, null, null, locator, pageSize+1);
		}
        
		if(tasks.size() > pageSize) {
        	tasks.remove(tasks.size() - 1);
        	response.setNextPageAnchor(tasks.get(tasks.size() - 1).getId());
        }
		Timestamp ts = new Timestamp(DateHelper.currentGMTTime().getTime());
    	List<EquipmentTaskDTO> dtos = tasks.stream().map(r -> {
			//扫出来的任务要是当前时间能执行的任务 by xiongying20170417
			if(r.getProcessExpireTime() == null) {
				r.setProcessExpireTime(new Timestamp(0L));
			}
			if(ts.after(r.getExecutiveStartTime())
					&& (ts.before(r.getExecutiveExpireTime()) || ts.before(r.getProcessExpireTime()))) {
				EquipmentTaskDTO dto = convertEquipmentTaskToDTO(r);
				return dto;
			}
        	return null;
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
       
		List<EquipmentInspectionTasks> tasks = equipmentProvider.listTaskByIds(taskIdlist);

		List<EquipmentTaskDTO> dtoList = tasks.stream().map(task -> {
			EquipmentTaskDTO dto = ConvertHelper.convert(task, EquipmentTaskDTO.class);
			return dto;
		}).collect(Collectors.toList());
		response.setTasks(dtoList);
		return response;
	}

	@Override
	public StatEquipmentTasksResponse statEquipmentTasks(StatEquipmentTasksCommand cmd) {
		StatEquipmentTasksResponse response = new StatEquipmentTasksResponse();
		
		int offset = cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		List<TaskCountDTO> tasks = equipmentProvider.statEquipmentTasks(cmd.getOwnerId(), cmd.getOwnerType(), 
				cmd.getTargetId(), cmd.getTargetType(), cmd.getInspectionCategoryId(), cmd.getStartTime(), cmd.getEndTime(),
				offset, pageSize+1);
		if(tasks != null && tasks.size() > pageSize) {
			tasks.remove(tasks.size() - 1);
			response.setNextPageAnchor(offset + 1);
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

	public void exportEquipmentsCard(ExportEquipmentsCardCommand cmd, HttpServletResponse response) {
		List<Long> equipmentIds = new ArrayList<>();
		if(!StringUtils.isEmpty(cmd.getIds())) {
			String[] ids = cmd.getIds().split(",");
			for(String id : ids) {
				equipmentIds.add(Long.valueOf(id));
			}
		}
		LOGGER.info("equipmentIds: {}", equipmentIds);
		List<EquipmentInspectionEquipments> equipments = equipmentProvider.listEquipmentsById(equipmentIds);
		List<EquipmentsDTO> dtos = equipments.stream().map(equipment -> {
			EquipmentsDTO dto = ConvertHelper.convert(equipment, EquipmentsDTO.class);
			return dto;
		}).collect(Collectors.toList());

		String filePath = cmd.getFilePath();
		if(StringUtils.isEmpty(cmd.getFilePath())) {
			URL rootPath = EquipmentServiceImpl.class.getResource("/");
			filePath = rootPath.getPath() + this.downloadDir ;
			File file = new File(filePath);
			if(!file.exists())
				file.mkdirs();

		}

//		return download(filePath,response);

		DocUtil docUtil=new DocUtil();
		List<String> files = new ArrayList<>();
		if(dtos.size() % 2 == 1) {
			EquipmentsDTO dto = dtos.get(dtos.size() - 1);
			
			Map<String, Object> dataMap=createEquipmentCardDoc(dto);

			GetAppInfoCommand command = new GetAppInfoCommand();
			command.setNamespaceId(dto.getNamespaceId());
			command.setOsType(OSType.Android.getCode());
			AppUrlDTO appUrlDTO = appUrlService.getAppInfo(command);
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("app logo url : {}", appUrlDTO.getLogoUrl());
			}
			if(appUrlDTO.getLogoUrl() != null) {
				dataMap.put("shenyeLogo", docUtil.getUrlImageStr(appUrlDTO.getLogoUrl()));
			}

			if(QRCodeFlag.ACTIVE.equals(QRCodeFlag.fromStatus(dto.getQrCodeFlag()))) {
				ByteArrayOutputStream out = generateQRCode(Base64.encodeBase64String(dto.getQrCodeToken().getBytes()));
				byte[] data=out.toByteArray();
				BASE64Encoder encoder=new BASE64Encoder();
				dataMap.put("qrCode", encoder.encode(data));
			}

			String savePath = filePath + dto.getId()+ "-" + dto.getName() + ".doc";
			docUtil.createDoc(dataMap, "shenye", savePath);

			if(StringUtils.isEmpty(cmd.getFilePath())) {
//				download(savePath,response);
				files.add(savePath);
			}
			dtos.remove(dtos.size() - 1);
		}

		for(int i = 0; i <dtos.size(); i = i+2) {
			EquipmentsDTO dto1 = dtos.get(i);
			EquipmentsDTO dto2 = dtos.get(i+1);
//			DocUtil docUtil=new DocUtil();
			Map<String, Object> dataMap=createTwoEquipmentCardDoc(dto1, dto2);

			GetAppInfoCommand command = new GetAppInfoCommand();
			command.setNamespaceId(dto1.getNamespaceId());
			command.setOsType(OSType.Android.getCode());
			AppUrlDTO appUrlDTO = appUrlService.getAppInfo(command);
			if(appUrlDTO.getLogoUrl() != null) {
				dataMap.put("shenyeLogo1", docUtil.getUrlImageStr(appUrlDTO.getLogoUrl()));
				dataMap.put("shenyeLogo2", docUtil.getUrlImageStr(appUrlDTO.getLogoUrl()));
			}

			if(QRCodeFlag.ACTIVE.equals(QRCodeFlag.fromStatus(dto1.getQrCodeFlag()))) {
				ByteArrayOutputStream out = generateQRCode(Base64.encodeBase64String(dto1.getQrCodeToken().getBytes()));
				byte[] data=out.toByteArray();
				BASE64Encoder encoder=new BASE64Encoder();
				dataMap.put("qrCode1", encoder.encode(data));
			}

			if(QRCodeFlag.ACTIVE.equals(QRCodeFlag.fromStatus(dto2.getQrCodeFlag()))) {
				ByteArrayOutputStream out = generateQRCode(Base64.encodeBase64String(dto2.getQrCodeToken().getBytes()));
				byte[] data=out.toByteArray();
				BASE64Encoder encoder=new BASE64Encoder();
				dataMap.put("qrCode2", encoder.encode(data));
//				dataMap.put("qrCode2", data.toString());
			}

			String savePath = filePath + dto1.getId()+ "-" + dto1.getName() +
					"-" + dto2.getId()+ "-" + dto2.getName() + ".doc";

			docUtil.createDoc(dataMap, "shenye2", savePath);
			if(StringUtils.isEmpty(cmd.getFilePath())) {
//				download(savePath,response);
				files.add(savePath);
			}
		}
		if(StringUtils.isEmpty(cmd.getFilePath())) {
			if(files.size() > 1) {
				String zipPath = filePath + System.currentTimeMillis() + "EquipmentCard.zip";
				LOGGER.info("filePath:{}, zipPath:{}",filePath,zipPath);
				DownloadUtils.writeZip(files, zipPath);
				download(zipPath,response);
			} else if(files.size() == 1) {
				download(files.get(0),response);
			}

		}

	}

	private Timestamp getDayBegin(Calendar cal, int period) {
		cal.add(Calendar.DATE, period);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 001);
		return new Timestamp(cal.getTimeInMillis());
	}

	private Timestamp getDayEnd(Calendar cal, int period) {
		cal.add(Calendar.DATE, period);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return new Timestamp(cal.getTimeInMillis());
	}

	@Override
	public StatTodayEquipmentTasksResponse statTodayEquipmentTasks(StatTodayEquipmentTasksCommand cmd) {
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_STAT_PANDECT, 0L);
		if(cmd.getTargetId() == null) {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
		}

		Calendar cal = Calendar.getInstance();
		if(cmd.getDateTime() == null) {
			cmd.setDateTime(DateHelper.currentGMTTime().getTime());
		}
		cal.setTime(new Timestamp(cmd.getDateTime()));

		TasksStatData stat = equipmentProvider.statDaysEquipmentTasks(cmd.getTargetId(), cmd.getTargetType(),
				cmd.getInspectionCategoryId(), getDayBegin(cal, 0), getDayEnd(cal, 0));
		ReviewedTaskStat reviewStat = equipmentProvider.statDaysReviewedTasks(cmd.getTargetId(),
				cmd.getInspectionCategoryId(), getDayBegin(cal, 0), getDayEnd(cal, 0));
		StatTodayEquipmentTasksResponse response = ConvertHelper.convert(stat, StatTodayEquipmentTasksResponse.class);
		response.setReviewQualified(reviewStat.getQualifiedTasks());
		response.setReviewUnqualified(reviewStat.getUnqualifiedTasks());
		response.setCurrentWaitingForExecuting(response.getWaitingForExecuting() + response.getInMaintance());
		response.setCurrentWaitingForApproval(response.getCompleteWaitingForApproval() + response.getCompleteMaintanceWaitingForApproval() + response.getNeedMaintanceWaitingForApproval());
		response.setReviewed(response.getReviewQualified() + response.getReviewUnqualified());
		return response;
	}

	@Override
	public StatLastDaysEquipmentTasksResponse statLastDaysEquipmentTasks(StatLastDaysEquipmentTasksCommand cmd) {
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_STAT_PANDECT, 0L);
		if(cmd.getTargetId() == null) {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		TasksStatData statTasks = equipmentProvider.statDaysEquipmentTasks(cmd.getTargetId(), cmd.getTargetType(),
				cmd.getInspectionCategoryId(), getDayBegin(cal, -cmd.getLastDays()), getDayEnd(cal, 0));
		ReviewedTaskStat reviewStat = equipmentProvider.statDaysReviewedTasks(cmd.getTargetId(),
				cmd.getInspectionCategoryId(), getDayBegin(cal, -cmd.getLastDays()), getDayEnd(cal, 0));

		StatLastDaysEquipmentTasksResponse response = new StatLastDaysEquipmentTasksResponse();
		response.setComplete(statTasks.getComplete());
		response.setCompleteInspection(statTasks.getCompleteInspection());
		response.setCompleteMaintance(statTasks.getCompleteMaintance());
		response.setReviewUnqualified(reviewStat.getUnqualifiedTasks());
		response.setReviewQualified(reviewStat.getQualifiedTasks());
		response.setReviewed(response.getReviewQualified()+response.getReviewUnqualified());
		return response;
	}

	@Override
	public StatIntervalAllEquipmentTasksResponse statIntervalAllEquipmentTasks(StatIntervalAllEquipmentTasksCommand cmd) {
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_STAT_ALLTASK, 0L);
		if(cmd.getTargetId() == null) {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
		}
		Timestamp begin = null;
		Timestamp end = null;
		if(cmd.getStartTime() != null) {
			begin = new Timestamp(cmd.getStartTime());
		}
		if(cmd.getEndTime() != null) {
			end = new Timestamp((cmd.getEndTime()));
		}
		TasksStatData statTasks = equipmentProvider.statDaysEquipmentTasks(cmd.getTargetId(), cmd.getTargetType(),
				cmd.getInspectionCategoryId(), begin, end);
		ReviewedTaskStat reviewStat = equipmentProvider.statDaysReviewedTasks(cmd.getTargetId(),
				cmd.getInspectionCategoryId(), begin, end);

		StatIntervalAllEquipmentTasksResponse response = ConvertHelper.convert(statTasks, StatIntervalAllEquipmentTasksResponse.class);
		response.setReviewUnqualified(reviewStat.getUnqualifiedTasks());
		response.setReviewQualified(reviewStat.getQualifiedTasks());
		response.setReviewedTasks(response.getReviewUnqualified() + response.getReviewQualified());
		response.setUnReviewedTasks(statTasks.getNeedMaintanceWaitingForApproval() + statTasks.getCompleteWaitingForApproval());
		response.setReviewTasks(response.getUnReviewedTasks() + response.getReviewedTasks());

		Double maintanceRate = response.getComplete().equals(0L) ? 0.00 : (double)response.getCompleteMaintance()/(double)response.getComplete();
		response.setMaintanceRate(maintanceRate);
		Double delayRate = (response.getComplete()+response.getDelay()) == 0L ? 0.00 : (double)response.getDelay()/(double)(response.getComplete()+response.getDelay());
		response.setDelayRate(delayRate);
		Double reviewQualifiedRate = response.getReviewedTasks().equals(0L) ? 0.00 : (double)response.getReviewQualified()/(double)response.getReviewedTasks();
		response.setReviewQualifiedRate(reviewQualifiedRate);
		Double reviewDalayRate = response.getReviewTasks().equals(0L) ? 0.00 : (double)response.getReviewDelayTasks()/(double)response.getReviewTasks();
		response.setReviewDalayRate(reviewDalayRate);

		return response;
	}

	@Override
	public StatItemResultsInEquipmentTasksResponse statItemResultsInEquipmentTasks(StatItemResultsInEquipmentTasksCommand cmd) {
		StatItemResultsInEquipmentTasksResponse response = new StatItemResultsInEquipmentTasksResponse();
//		itemResultStat: 巡检参数统计 参考ItemResultStat
		EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(cmd.getEquipmentId());
		if(equipment != null) {
			response.setEquipmentName(equipment.getName());
			response.setCustomNumber(equipment.getCustomNumber());
			response.setLocation(equipment.getLocation());
		}

		Timestamp begin = null;
		Timestamp end = null;
		if(cmd.getStartTime() != null) {
			begin = new Timestamp(cmd.getStartTime());
		}
		if(cmd.getEndTime() != null) {
			end = new Timestamp((cmd.getEndTime()));
		}
		List<ItemResultStat> results = equipmentProvider.statItemResults(cmd.getEquipmentId(), cmd.getStandardId(),
				begin, end);
		results.forEach(result -> {
			EquipmentInspectionItems item = equipmentProvider.findEquipmentInspectionItem(result.getItemId());
			if(item != null) {
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
		if(tasks != null && tasks.size() > 0) {
			List<EquipmentTaskDTO> dtos =tasks.stream().map(task -> {
				EquipmentTaskDTO dto = ConvertHelper.convert(task, EquipmentTaskDTO.class);
				EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(task.getEquipmentId());
				if(equipment != null) {
					dto.setEquipmentName(equipment.getName());
					dto.setEquipmentLocation(equipment.getLocation());
				}
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
				if(standard != null) {
					dto.setTaskType(standard.getStandardType());
				}
				if(task.getExecutorId() != null && task.getExecutorId() != 0) {
					List<OrganizationMember> executors = organizationProvider.listOrganizationMembersByUId(task.getExecutorId());
					if(executors != null && executors.size() > 0) {
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}

		return out;
	}

	private Map<String, Object> createEquipmentCardDoc(EquipmentsDTO dto) {
		Map<String, Object> dataMap=new HashMap<String, Object>();
		dataMap.put("sequenceNo", dto.getSequenceNo());
		dataMap.put("versionNo", dto.getVersionNo());
		dataMap.put("name", dto.getName());
		dataMap.put("equipmentModel", dto.getEquipmentModel());
		dataMap.put("parameter", dto.getParameter());
		dataMap.put("customNumber", dto.getCustomNumber());
		dataMap.put("manufacturer", dto.getManufacturer());
		dataMap.put("manager", dto.getManager());
		dataMap.put("status",EquipmentStatus.fromStatus(dto.getStatus()).getName());

		return dataMap;
	}

	private Map<String, Object> createTwoEquipmentCardDoc(EquipmentsDTO dto1, EquipmentsDTO dto2) {
		Map<String, Object> dataMap=new HashMap<String, Object>();
		dataMap.put("sequenceNo1", dto1.getSequenceNo());
		dataMap.put("versionNo1", dto1.getVersionNo());
		dataMap.put("name1", dto1.getName());
		dataMap.put("equipmentModel1", dto1.getEquipmentModel());
		dataMap.put("parameter1", dto1.getParameter());
		dataMap.put("customNumber1", dto1.getCustomNumber());
		dataMap.put("manufacturer1", dto1.getManufacturer());
		dataMap.put("manager1", dto1.getManager());
		dataMap.put("status1",EquipmentStatus.fromStatus(dto1.getStatus()).getName());

		dataMap.put("sequenceNo2", dto2.getSequenceNo());
		dataMap.put("versionNo2", dto2.getVersionNo());
		dataMap.put("name2", dto2.getName());
		dataMap.put("equipmentModel2", dto2.getEquipmentModel());
		dataMap.put("parameter2", dto2.getParameter());
		dataMap.put("customNumber2", dto2.getCustomNumber());
		dataMap.put("manufacturer2", dto2.getManufacturer());
		dataMap.put("manager2", dto2.getManager());
		dataMap.put("status2",EquipmentStatus.fromStatus(dto2.getStatus()).getName());

		return dataMap;
	}

	private PmNotifyParamDTO convertPmNotifyConfigurationsToDTO(Integer namespaceId, PmNotifyConfigurations configuration) {
		PmNotifyParamDTO param = ConvertHelper.convert(configuration, PmNotifyParamDTO.class);
		String receiverJson = configuration.getReceiverJson();
		if(StringUtils.isNotBlank(receiverJson)) {
			PmNotifyReceiverList receiverList = (PmNotifyReceiverList) StringHelper.fromJsonString(receiverJson, PmNotifyReceiverList.class);
			if(receiverList != null && receiverList.getReceivers() != null && receiverList.getReceivers().size() > 0) {
//				param.setReceivers(receiverList.getReceivers());
				List<PmNotifyReceiverDTO> receiverDTOs = new ArrayList<>();
				receiverList.getReceivers().forEach(receiver -> {
					PmNotifyReceiverDTO dto = new PmNotifyReceiverDTO();
					dto.setReceiverType(receiver.getReceiverType());
					if(PmNotifyReceiverType.ORGANIZATION.equals(PmNotifyReceiverType.fromCode(receiver.getReceiverType()))) {
						List<Organization> organizations = organizationProvider.listOrganizationsByIds(receiver.getReceiverIds());
						if(organizations != null && organizations.size() > 0) {
							List<ReceiverName> dtoReceivers = new ArrayList<ReceiverName>();
							organizations.forEach(organization -> {
								ReceiverName receiverName = new ReceiverName();
								receiverName.setId(organization.getId());
								receiverName.setName(organization.getName());
								dtoReceivers.add(receiverName);
							});
							dto.setReceivers(dtoReceivers);
						}
					} else if(PmNotifyReceiverType.USER.equals(PmNotifyReceiverType.fromCode(receiver.getReceiverType()))) {
						List<User> users = userProvider.listUserByIds(namespaceId, receiver.getReceiverIds());
						if(users != null && users.size() > 0) {
							List<ReceiverName> dtoReceivers = new ArrayList<ReceiverName>();
							users.forEach(user -> {
								ReceiverName receiverName = new ReceiverName();
								receiverName.setId(user.getId());
								receiverName.setName(user.getNickName());
								dtoReceivers.add(receiverName);
							});
							dto.setReceivers(dtoReceivers);
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
		if(cmd.getId() == null ) {
			return ;
		}
		Byte scopeType = PmNotifyScopeType.NAMESPACE.getCode();
		Long scopeId = cmd.getNamespaceId().longValue();
		if(cmd.getCommunityId() != null && cmd.getCommunityId() != 0L) {
			scopeType = PmNotifyScopeType.COMMUNITY.getCode();
			scopeId = cmd.getCommunityId();
		}
		PmNotifyConfigurations configuration = pmNotifyProvider.findScopePmNotifyConfiguration(cmd.getId(), EntityType.EQUIPMENT_TASK.getCode(), scopeType, scopeId);
		if(configuration != null) {
			configuration.setStatus(PmNotifyConfigurationStatus.INVAILD.getCode());
			pmNotifyProvider.updatePmNotifyConfigurations(configuration);
		}
	}

	@Override
	public List<PmNotifyParamDTO> listPmNotifyParams(ListPmNotifyParamsCommand cmd) {
		Byte scopeType = PmNotifyScopeType.NAMESPACE.getCode();
		Long scopeId = cmd.getNamespaceId().longValue();
		if(cmd.getCommunityId() != null && cmd.getCommunityId() != 0L) {
			scopeType = PmNotifyScopeType.COMMUNITY.getCode();
			scopeId = cmd.getCommunityId();
		}
		List<PmNotifyConfigurations> configurations = pmNotifyProvider.listScopePmNotifyConfigurations(EntityType.EQUIPMENT_TASK.getCode(), scopeType, scopeId);
		if(configurations != null && configurations.size() > 0) {
			List<PmNotifyParamDTO> params = configurations.stream().map(configuration -> {
				return convertPmNotifyConfigurationsToDTO(cmd.getNamespaceId(), configuration);
			}).collect(Collectors.toList());
			return params;
		} else {
			//scopeType是community的情况下 如果拿不到数据，则返回该域空间下的设置 ps以后可以再else一下 域空间的没有返回all的
			if(PmNotifyScopeType.COMMUNITY.equals(PmNotifyScopeType.fromCode(scopeType))) {
				scopeType = PmNotifyScopeType.NAMESPACE.getCode();
				scopeId = cmd.getNamespaceId().longValue();
				List<PmNotifyConfigurations> namespaceConfigurations = pmNotifyProvider.listScopePmNotifyConfigurations(EntityType.EQUIPMENT_TASK.getCode(), scopeType, scopeId);
				if(namespaceConfigurations != null && namespaceConfigurations.size() > 0) {
					List<PmNotifyParamDTO> params = namespaceConfigurations.stream().map(configuration -> {
						return convertPmNotifyConfigurationsToDTO(cmd.getNamespaceId(), configuration);
					}).collect(Collectors.toList());
					return params;
				}
			}
		}

		return null;
	}

	@Override
	public void setPmNotifyParams(SetPmNotifyParamsCommand cmd) {
		if(cmd.getParams() != null && cmd.getParams().size() > 0) {
			for(PmNotifyParams params : cmd.getParams()) {
				PmNotifyConfigurations configuration = ConvertHelper.convert(params, PmNotifyConfigurations.class);
				configuration.setOwnerType(EntityType.EQUIPMENT_TASK.getCode());
				if(params.getCommunityId() != null && params.getCommunityId() != 0L) {
					configuration.setScopeId(params.getCommunityId());
					configuration.setScopeType(PmNotifyScopeType.COMMUNITY.getCode());
				} else {
					configuration.setScopeId(params.getNamespaceId().longValue());
					configuration.setScopeType(PmNotifyScopeType.NAMESPACE.getCode());
				}
				List<PmNotifyReceiver> receivers = params.getReceivers();
				if(receivers != null && receivers.size() > 0) {
					PmNotifyReceiverList receiverList = new PmNotifyReceiverList();
					receiverList.setReceivers(receivers);
					configuration.setReceiverJson(receiverList.toString());
				}

				if(params.getId() == null) {
					pmNotifyProvider.createPmNotifyConfigurations(configuration);
				} else {
					Byte scopeType = PmNotifyScopeType.NAMESPACE.getCode();
					Long scopeId = params.getNamespaceId().longValue();
					if(params.getCommunityId() != null && params.getCommunityId() != 0L) {
						scopeType = PmNotifyScopeType.COMMUNITY.getCode();
						scopeId = params.getCommunityId();
					}
					PmNotifyConfigurations exist = pmNotifyProvider.findScopePmNotifyConfiguration(params.getId(), EntityType.EQUIPMENT_TASK.getCode(), scopeType, scopeId);
					if(exist != null) {
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
	public Set<Long> getTaskGroupUsers(Long taskId, byte groupType) {
		EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(taskId);
		List<EquipmentInspectionStandardGroupMap> maps = equipmentProvider.listEquipmentInspectionStandardGroupMapByStandardIdAndGroupType(task.getStandardId(), groupType);
		if(maps != null && maps.size() > 0) {
			Set<Long> userIds = new HashSet<>();
			maps.forEach(map -> {
				if(map.getPositionId() == null || map.getPositionId() == 0L) {
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
}
