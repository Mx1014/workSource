package com.everhomes.quality;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.weaver.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.quality.QualityService;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.RentalResourceOrder;
import com.everhomes.rentalv2.RentalItem;
import com.everhomes.rentalv2.Rentalv2ServiceImpl;
import com.everhomes.repeat.RepeatService;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.quality.CreatQualityStandardCommand;
import com.everhomes.rest.quality.CreateQualityInspectionTaskCommand;
import com.everhomes.rest.quality.DeleteQualityCategoryCommand;
import com.everhomes.rest.quality.DeleteQualityStandardCommand;
import com.everhomes.rest.quality.DeleteFactorCommand;
import com.everhomes.rest.quality.EvaluationDTO;
import com.everhomes.rest.quality.FactorsDTO;
import com.everhomes.rest.quality.GetGroupMembersCommand;
import com.everhomes.rest.quality.GroupUserDTO;
import com.everhomes.rest.quality.ListEvaluationsCommand;
import com.everhomes.rest.quality.ListEvaluationsResponse;
import com.everhomes.rest.quality.ListQualityCategoriesCommand;
import com.everhomes.rest.quality.ListQualityCategoriesResponse;
import com.everhomes.rest.quality.ListQualityStandardsCommand;
import com.everhomes.rest.quality.ListQualityStandardsResponse;
import com.everhomes.rest.quality.ListQualityInspectionTasksCommand;
import com.everhomes.rest.quality.ListQualityInspectionTasksResponse;
import com.everhomes.rest.quality.ListFactorsCommand;
import com.everhomes.rest.quality.ListFactorsResponse;
import com.everhomes.rest.quality.ListRecordsByTaskIdCommand;
import com.everhomes.rest.quality.ListQualityInspectionLogsCommand;
import com.everhomes.rest.quality.ListQualityInspectionLogsResponse;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.rest.quality.ProcessType;
import com.everhomes.rest.quality.QualityCategoriesDTO;
import com.everhomes.rest.quality.QualityGroupType;
import com.everhomes.rest.quality.QualityInspectionCategoryStatus;
import com.everhomes.rest.quality.QualityInspectionLogDTO;
import com.everhomes.rest.quality.QualityInspectionLogProcessType;
import com.everhomes.rest.quality.QualityInspectionLogType;
import com.everhomes.rest.quality.QualityInspectionTaskDTO;
import com.everhomes.rest.quality.QualityInspectionTaskRecordsDTO;
import com.everhomes.rest.quality.QualityInspectionTaskResult;
import com.everhomes.rest.quality.QualityInspectionTaskReviewResult;
import com.everhomes.rest.quality.QualityInspectionTaskStatus;
import com.everhomes.rest.quality.QualityNotificationTemplateCode;
import com.everhomes.rest.quality.QualityStandardStatus;
import com.everhomes.rest.quality.QualityStandardsDTO;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.quality.QualityTaskType;
import com.everhomes.rest.quality.ReportRectifyResultCommand;
import com.everhomes.rest.quality.ReportVerificationResultCommand;
import com.everhomes.rest.quality.ReviewVerificationResultCommand;
import com.everhomes.rest.quality.StandardGroupDTO;
import com.everhomes.rest.quality.UpdateQualityCategoryCommand;
import com.everhomes.rest.quality.UpdateQualityStandardCommand;
import com.everhomes.rest.quality.UpdateFactorCommand;
import com.everhomes.rest.rentalv2.RentalBillDTO;
import com.everhomes.rest.rentalv2.RentalServiceErrorCode;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.rest.rentalv2.SiteItemDTO;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.rest.repeat.TimeRangeDTO;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.mysql.jdbc.StringUtils;


@Component
public class QualityServiceImpl implements QualityService {
	
	final String downloadDir ="\\download\\";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityServiceImpl.class);
	
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
	
	@Override
	public QualityStandardsDTO creatQualityStandard(CreatQualityStandardCommand cmd) {
		
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
		standard.setCategoryId(cmd.getCategoryId());
		standard.setDescription(cmd.getDescription());
		standard.setCreatorUid(user.getId());
		standard.setOperatorUid(user.getId());
		if(repeat == null) {
			standard.setRepeatSettingId(0L);
		} else {
			standard.setRepeatSettingId(repeat.getId());
		}
			
		qualityProvider.createQualityInspectionStandards(standard);
		
		createQualityInspectionStandardLogs(standard, QualityInspectionLogProcessType.INSERT.getCode(), user.getId());
		
		List<StandardGroupDTO> groupList = cmd.getGroup();
		processStandardGroups(groupList, standard);
		processRepeatSetting(standard);
		QualityStandardsDTO dto = ConvertHelper.convert(standard, QualityStandardsDTO.class);
		return dto;
		
	}
	
	private void createQualityInspectionStandardLogs(QualityInspectionStandards standard, Byte processType, Long userId) {
		
		QualityInspectionLogs log = new QualityInspectionLogs();
		log.setOwnerType(standard.getOwnerType());
		log.setOwnerId(standard.getOwnerId());
		log.setTargetType(QualityInspectionLogType.STANDARD.getCode());
		log.setTargetId(standard.getId());
		log.setProcessType(processType);
		log.setOperatorUid(userId);
		qualityProvider.createQualityInspectionLogs(log);
	}

	@Override
	public QualityStandardsDTO updateQualityStandard(UpdateQualityStandardCommand cmd) {
		
		User user = UserContext.current().getUser();
		
		QualityInspectionStandards standard = verifiedStandardById(cmd.getId());
		standard.setOwnerId(cmd.getOwnerId());
		standard.setOwnerType(cmd.getOwnerType());
		standard.setName(cmd.getName());
		standard.setStandardNumber(cmd.getStandardNumber());
		standard.setDescription(cmd.getDescription());
		standard.setOperatorUid(user.getId());
		qualityProvider.updateQualityInspectionStandards(standard);
		
		createQualityInspectionStandardLogs(standard, QualityInspectionLogProcessType.UPDATE.getCode(), user.getId());
		
		List<StandardGroupDTO> groupList = cmd.getGroup();
		processStandardGroups(groupList, standard);
		processRepeatSetting(standard);
		QualityStandardsDTO dto = ConvertHelper.convert(standard, QualityStandardsDTO.class);
		return dto;
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
        	executiveGroup = new ArrayList<QualityInspectionStandardGroupMap>();
    		reviewGroup = new ArrayList<QualityInspectionStandardGroupMap>();
    		
			for(StandardGroupDTO group : groupList) {
				QualityInspectionStandardGroupMap map = new QualityInspectionStandardGroupMap();
				 map.setStandardId(standard.getId());
				 map.setGroupType(group.getGroupType());
				 map.setGroupId(group.getGroupId());
				 if(group.getInspectorUid() != null)
					 map.setInspectorUid(group.getInspectorUid());
				 qualityProvider.createQualityInspectionStandardGroupMap(map);
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
       
	
	private QualityInspectionStandards verifiedStandardById(Long id) {
		QualityInspectionStandards standard = qualityProvider.findStandardById(id);
		if(standard == null || standard.getStatus() == null || standard.getStatus() == QualityStandardStatus.INACTIVE.getCode()) {
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
		standard.setStatus(QualityStandardStatus.INACTIVE.getCode());
		standard.setOperatorUid(user.getId());
		standard.setDeleterUid(user.getId());
		standard.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		
		qualityProvider.updateQualityInspectionStandards(standard);
		
		createQualityInspectionStandardLogs(standard, QualityInspectionLogProcessType.DELETE.getCode(), user.getId());
		
	}

	@Override
	public ListQualityStandardsResponse listQualityStandards(ListQualityStandardsCommand cmd) {

		Long ownerId = cmd.getOwnerId();
		String ownerType = cmd.getOwnerType();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        
        List<QualityInspectionStandards> standards = qualityProvider.listQualityInspectionStandards(locator, pageSize+1, ownerId, ownerType);
		
		this.qualityProvider.populateStandardsGroups(standards);
		
//		for(QualityInspectionStandards standard : standards) {
//			processRepeatSetting(standard);
//		}
        
        Long nextPageAnchor = null;
        if(standards.size() > pageSize) {
        	standards.remove(standards.size() - 1);
            nextPageAnchor = standards.get(standards.size() - 1).getId();
        }
        
        
        List<QualityStandardsDTO> qaStandards = standards.stream().map((r) -> {
        	
        	QualityStandardsDTO dto = converStandardToDto(r);
        	
        	return dto;
        }).collect(Collectors.toList());
        
        
        return new ListQualityStandardsResponse(nextPageAnchor, qaStandards);
	}

	@Override
	public void updateQualityCategory(UpdateQualityCategoryCommand cmd) {
		User user = UserContext.current().getUser();
		
		if(cmd.getId() == null) {
			QualityInspectionCategories category = new QualityInspectionCategories();
			category.setName(cmd.getName());
			category.setOwnerType(cmd.getOwnerType());
			category.setOwnerId(cmd.getOwnerId());
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
			factor.setOwnerId(cmd.getOwnerId());
			factor.setOwnerType(cmd.getOwnerType());
			factor.setCategoryId(cmd.getCategoryId());
			factor.setGroupId(cmd.getGroupId());
			factor.setWeight(cmd.getWeight());
			factor.setCreatorUid(user.getId());
			
			
			qualityProvider.createQualityInspectionEvaluationFactors(factor);
		} else {
			QualityInspectionEvaluationFactors factor = verifiedFactorById(cmd.getId());
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
	public ListQualityInspectionTasksResponse listQualityInspectionTasks(
			ListQualityInspectionTasksCommand cmd) {
		
		User user = UserContext.current().getUser();
		Long ownerId = cmd.getOwnerId();
		String ownerType = cmd.getOwnerType();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        Timestamp startDate = null;
        Timestamp endDate = null;
        if(cmd.getStartDate() != null) {
        	startDate = new Timestamp(cmd.getStartDate());
        }
        if(cmd.getEndDate() != null) {
        	endDate = new Timestamp(cmd.getEndDate());
        }

        Long currentUid = null;
        boolean timeCompared = false;
        if(cmd.getExecuteFlag() != null) {
        	timeCompared = true;
        	if(cmd.getExecuteFlag() == 1) {
	        	currentUid = user.getId();
        	}
        }
        final Long executeUid = currentUid;
        List<QualityInspectionTasks> tasks = new ArrayList<QualityInspectionTasks>();
        
        if(cmd.getIsReview() != null && cmd.getIsReview() == 1) {
        	List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(user.getId());
        	
        	List<Long>  orgIds = members.stream().map((r) -> {
        		return r.getOrganizationId();
        	}).collect(Collectors.toList());
        	
        	List<Long> standardIds = qualityProvider.listQualityInspectionStandardGroupMapByGroup(
        			orgIds, QualityGroupType.REVIEW_GROUP.getCode());
        	
        	tasks = qualityProvider.listVerificationTasks(locator, pageSize + 1, ownerId, ownerType, 
            		cmd.getTaskType(), executeUid, startDate, endDate, cmd.getGroupId(),
            		cmd.getExecuteStatus(), cmd.getReviewStatus(), timeCompared, standardIds, cmd.getManualFlag());

        	
        } else {
        	tasks = qualityProvider.listVerificationTasks(locator, pageSize + 1, ownerId, ownerType, 
        		cmd.getTaskType(), executeUid, startDate, endDate, cmd.getGroupId(),
        		cmd.getExecuteStatus(), cmd.getReviewStatus(), timeCompared, null, cmd.getManualFlag());
        }
        
        Long nextPageAnchor = null;
        if(tasks.size() > pageSize) {
        	tasks.remove(tasks.size() - 1);
            nextPageAnchor = tasks.get(tasks.size() - 1).getId();
        }
        
        List<QualityInspectionTaskRecords> records = new ArrayList<QualityInspectionTaskRecords>();
        for(QualityInspectionTasks task : tasks) {
        	QualityInspectionTaskRecords record = qualityProvider.listLastRecordByTaskId(task.getId());
        	if(record != null) {
        		task.setRecord(record);
            	records.add(task.getRecord());
        	}
        	
        }

		this.qualityProvider.populateRecordAttachments(records);

		for(QualityInspectionTaskRecords record : records) {
			populateRecordAttachements(record, record.getAttachments());
		}
//		records.stream().map((r) -> {
//			populateRecordAttachements(r, r.getAttachments());
//			return r;
//		});
        
		List<QualityInspectionTaskDTO> dtoList = convertQualityInspectionTaskToDTO(tasks, executeUid);
        
        return new ListQualityInspectionTasksResponse(nextPageAnchor, dtoList);
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
         	
         }).filter(member->member!=null).collect(Collectors.toList());
    	
    	return groupUsers;
	}
	
	private List<QualityInspectionTaskDTO> convertQualityInspectionTaskToDTO(List<QualityInspectionTasks> tasks, final Long executeUid) {
		
		List<QualityInspectionTaskDTO> dtoList = tasks.stream().map((r) -> {
        	
//        	QualityInspectionStandards standard = verifiedStandardById(r.getStandardId());
			
		    // 由于现网存在着大量categoryId为0的任务，故不能直接抛异常，先暂时去掉校验 by lqs 20160715
			//QualityInspectionCategories category = verifiedCategoryById(r.getCategoryId());
		    QualityInspectionCategories category = qualityProvider.findQualityInspectionCategoriesByCategoryId(r.getCategoryId());
		    if(category != null) {
		        r.setCategoryName(getQualityCategoryNamePath(category.getPath()));
		    }
			
        	if(executeUid != null) {
        		if(r.getExecutorId() != null && r.getExecutorId().equals(executeUid)) {
        			r.setTaskFlag(QualityTaskType.VERIFY_TASK.getCode());
        		}else if(r.getOperatorId() != null && r.getOperatorId().equals(executeUid)) {
        			r.setTaskFlag(QualityTaskType.RECTIFY_TASK.getCode());
        		}
        	}
        	
        	QualityInspectionTaskDTO dto = ConvertHelper.convert(r, QualityInspectionTaskDTO.class);  
        	if(category != null) {
        	    dto.setCategoryDescription(category.getDescription());
        	}
        	QualityInspectionStandards standard = qualityProvider.findStandardById(r.getStandardId());
			if(standard != null) {
				dto.setStandardDescription(standard.getDescription());
			} 
        	
        	Organization group = organizationProvider.findOrganizationById(r.getExecutiveGroupId());
			if(group != null)
				dto.setGroupName(group.getName());
        	
//			List<GroupUserDTO> groupUsers = getGroupMembers(r.getExecutiveGroupId(), false);
//        	 
//        	dto.setGroupUsers(groupUsers);
//        	
        	QualityInspectionTaskRecordsDTO recordDto = ConvertHelper.convert(r.getRecord(), QualityInspectionTaskRecordsDTO.class);
        	if(recordDto != null) {
	        	if(recordDto.getTargetId() != null && recordDto.getTargetId() != 0) {
	        		List<OrganizationMember> target = organizationProvider.listOrganizationMembersByUId(recordDto.getTargetId());
	        		if(target != null && target.size() > 0) {
	        			recordDto.setTargetName(target.get(0).getContactName());
	        		}
	        	}
        	}
        	dto.setRecord(recordDto);
        	 
        	OrganizationMember executor = organizationProvider.findOrganizationMemberByOrgIdAndUId(r.getExecutorId(), r.getExecutiveGroupId());
        	if(executor != null) {
        		dto.setExecutorName(executor.getContactName());
        	}
        	
        	if(r.getOperatorId() != null && r.getOperatorId() != 0) {
        		OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(r.getOperatorId(), r.getExecutiveGroupId());
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
        	
        	return dto;
        }).filter(task->task!=null).collect(Collectors.toList());
		return dtoList;
	}
	
	@Override
	public QualityInspectionTaskDTO reportVerificationResult(ReportVerificationResultCommand cmd) {
		User user = UserContext.current().getUser();
		QualityInspectionTasks task = verifiedTaskById(cmd.getTaskId());
		if(task.getStatus() == QualityInspectionTaskStatus.CLOSED.getCode()) {
			LOGGER.error("the task which id="+task.getId()+" is closed!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_TASK_IS_CLOSED,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_TASK_IS_CLOSED),
									UserContext.current().getUser().getLocale(),
									"the task is closed!"));
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
 
		task.setResult(cmd.getVerificationResult());
		task.setExecutiveTime(new Timestamp(System.currentTimeMillis()));
		
		if(cmd.getCategoryId() != null) {
			task.setCategoryId(cmd.getCategoryId());
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
			
		if(QualityInspectionTaskResult.INSPECT_OK.getCode() == cmd.getVerificationResult()) {
			if(QualityInspectionTaskResult.RECTIFIED_OK_AND_WAITING_APPROVAL.getCode() == task.getProcessResult()) {
				
				task.setResult(QualityInspectionTaskResult.RECTIFIED_OK.getCode());
			}
			
			if(QualityInspectionTaskResult.RECTIFY_CLOSED_AND_WAITING_APPROVAL.getCode() == task.getProcessResult()) {
				task.setResult(QualityInspectionTaskResult.RECTIFY_CLOSED.getCode());
			}
			else {
				task.setResult(QualityInspectionTaskResult.INSPECT_OK.getCode());
			}
			
			task.setStatus(QualityInspectionTaskStatus.CLOSED.getCode());
			record.setProcessResult(QualityInspectionTaskResult.INSPECT_OK.getCode());
			record.setProcessType(ProcessType.INSPECT.getCode());
			
		}
		else if(QualityInspectionTaskResult.INSPECT_CLOSE.getCode() == cmd.getVerificationResult()) {
			task.setResult(QualityInspectionTaskResult.INSPECT_CLOSE.getCode());
			task.setStatus(QualityInspectionTaskStatus.CLOSED.getCode());
			record.setProcessResult(QualityInspectionTaskResult.INSPECT_CLOSE.getCode());
			record.setProcessType(ProcessType.INSPECT.getCode());
		}
		else {
			record.setProcessResult(QualityInspectionTaskResult.NONE.getCode());
			record.setProcessType(ProcessType.ASSIGN.getCode());
			task.setStatus(QualityInspectionTaskStatus.RECTIFING.getCode());
		}
		
		if(!StringUtils.isNullOrEmpty(cmd.getOperatorType()) && cmd.getOperatorId() != null
				 && cmd.getEndTime() != null) {
			OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), task.getExecutiveGroupId());
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("userName", operator.getContactName());
		    map.put("taskName", task.getTaskName());
		    map.put("deadline", timeToStr(new Timestamp(cmd.getEndTime())));
			String scope = QualityNotificationTemplateCode.SCOPE;
			int code = QualityNotificationTemplateCode.ASSIGN_TASK_NOTIFY_OPERATOR;
			String locale = "zh_CN";
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			sendMessageToUser(cmd.getOperatorId(), notifyTextForApplicant);
			
			OrganizationMember target = organizationProvider.findOrganizationMemberByOrgIdAndUId(cmd.getOperatorId(), task.getExecutiveGroupId());
			Map<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("operator", operator.getContactName());
			msgMap.put("target", target.getContactName());
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
				String msg = record.getProcessMessage()+attText+cmd.getMessage();
				record.setProcessMessage(msg);
			} else {
				String msg = attText+cmd.getMessage();
				record.setProcessMessage(msg);
			}
			
		}
		
		QualityInspectionTaskDTO dto = updateVerificationTasks(task, record, cmd.getAttachments());
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

		updateVerificationTasks(task, record, null);
		
		if(cmd.getReviewResult() != null 
				&& cmd.getReviewResult() == QualityInspectionTaskReviewResult.UNQUALIFIED.getCode()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("taskNumber", task.getTaskNumber());
			OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), task.getOwnerId());
			if(member != null) {
				map.put("userName", member.getContactName());
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
		if(task.getStatus() == QualityInspectionTaskStatus.CLOSED.getCode()) {
			LOGGER.error("the task which id="+task.getId()+" is closed!");
			throw RuntimeErrorException
					.errorWith(
							QualityServiceErrorCode.SCOPE,
							QualityServiceErrorCode.ERROR_TASK_IS_CLOSED,
							localeStringService.getLocalizedString(
									String.valueOf(QualityServiceErrorCode.SCOPE),
									String.valueOf(QualityServiceErrorCode.ERROR_TASK_IS_CLOSED),
									UserContext.current().getUser().getLocale(),
									"the task is closed!"));
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
		
		if(QualityInspectionTaskResult.RECTIFIED_OK_AND_WAITING_APPROVAL.getCode() == cmd.getRectifyResult()) {
			task.setProcessResult(QualityInspectionTaskResult.RECTIFIED_OK_AND_WAITING_APPROVAL.getCode());
			task.setStatus(QualityInspectionTaskStatus.RECTIFIED_AND_WAITING_APPROVAL.getCode());
			record.setProcessResult(QualityInspectionTaskResult.RECTIFIED_OK_AND_WAITING_APPROVAL.getCode());
			record.setProcessType(ProcessType.RETIFY.getCode());
		}
		else if(QualityInspectionTaskResult.RECTIFY_CLOSED_AND_WAITING_APPROVAL.getCode() == cmd.getRectifyResult()) {
			task.setProcessResult(QualityInspectionTaskResult.RECTIFY_CLOSED_AND_WAITING_APPROVAL.getCode());
			task.setStatus(QualityInspectionTaskStatus.RECTIFY_CLOSED_AND_WAITING_APPROVAL.getCode());
			record.setProcessResult(QualityInspectionTaskResult.RECTIFY_CLOSED_AND_WAITING_APPROVAL.getCode());
			record.setProcessType(ProcessType.RETIFY.getCode());
		}
		else {
			record.setProcessResult(QualityInspectionTaskResult.NONE.getCode());
			record.setProcessType(ProcessType.FORWARD.getCode());
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
		
		if(!StringUtils.isNullOrEmpty(cmd.getOperatorType()) && cmd.getOperatorId() != null
				 && cmd.getEndTime() != null) {
			OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), task.getExecutiveGroupId());
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("userName", operator.getContactName());
		    map.put("taskName", task.getTaskName());
		    map.put("deadline", timeToStr(new Timestamp(cmd.getEndTime())));
			String scope = QualityNotificationTemplateCode.SCOPE;
			int code = QualityNotificationTemplateCode.ASSIGN_TASK_NOTIFY_OPERATOR;
			String locale = "zh_CN";
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			sendMessageToUser(cmd.getOperatorId(), notifyTextForApplicant);
			
			OrganizationMember target = organizationProvider.findOrganizationMemberByOrgIdAndUId(cmd.getOperatorId(), task.getExecutiveGroupId());
			Map<String, Object> msgMap = new HashMap<String, Object>();
		    map.put("operator", operator.getContactName());
		    map.put("target", target.getContactName());
		    map.put("taskName", task.getTaskName());
		    map.put("deadline", timeToStr(new Timestamp(cmd.getEndTime())));
			int msgCode = QualityNotificationTemplateCode.ASSIGN_TASK_MSG;
			String msg = localeTemplateService.getLocaleTemplateString(scope, msgCode, locale, msgMap, "");
			record.setProcessMessage(msg);
		}
		if(cmd.getMessage() != null) {
			
			if(record.getProcessMessage() != null) {
				String msg = record.getProcessMessage()+cmd.getMessage();
				record.setProcessMessage(msg);
			} else {
				String msg = cmd.getMessage();
				record.setProcessMessage(msg);
			}
		}
		
		task.setProcessTime(new Timestamp(System.currentTimeMillis()));

//		processTaskAttachments(user.getId(),  cmd.getAttachments(), task, QualityTaskType.RECTIFY_TASK.getCode());
//		qualityProvider.populateTaskAttachment(task);
		QualityInspectionTaskDTO dto = updateVerificationTasks(task, record, cmd.getAttachments());
		return dto;
	}
	
	@Override
	public void createTaskByStandard(QualityStandardsDTO standard) {
		LOGGER.info("createTaskByStandard: " + standard);
		if(standard.getExecutiveGroup() != null && standard.getExecutiveGroup().size() > 0) {

			long current = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String day = sdf.format(current);
			
			QualityInspectionTasks task = new QualityInspectionTasks();
			
			task.setOwnerType(standard.getOwnerType());
			task.setOwnerId(standard.getOwnerId());
			task.setStandardId(standard.getId());
			task.setTaskName(standard.getName());
			task.setTaskType((byte) 1);
			task.setStatus(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode());
			task.setCategoryId(standard.getCategoryId());
			QualityInspectionCategories category = verifiedCategoryById(standard.getCategoryId());
			task.setCategoryPath(category.getPath());
			for(StandardGroupDTO executiveGroup : standard.getExecutiveGroup()) {
				
				task.setExecutiveGroupId(executiveGroup.getGroupId());
				
				OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(executiveGroup.getInspectorUid()
																	, executiveGroup.getGroupId());
//				List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgIdAndMemberGroup(
//														executiveGroup.getGroupId(), OrganizationMemberGroupType.HECHA.getCode());
				if(member != null) {
					task.setExecutorType(member.getTargetType());
					task.setExecutorId(member.getTargetId());
					List<TimeRangeDTO> timeRanges = repeatService.analyzeTimeRange(standard.getRepeat().getTimeRanges());
					
					if(timeRanges != null && timeRanges.size() > 0) {
						for(TimeRangeDTO timeRange : timeRanges) {
							this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_QUALITY_TASK.getCode()).tryEnter(()-> {
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
								
								Map<String, Object> map = new HashMap<String, Object>();
							    map.put("taskName", task.getTaskName());
							    map.put("deadline", timeToStr(expiredTime));
								String scope = QualityNotificationTemplateCode.SCOPE;
								int code = QualityNotificationTemplateCode.GENERATE_QUALITY_TASK_NOTIFY_EXECUTOR;
								String locale = "zh_CN";
								String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
								sendMessageToUser(member.getTargetId(), notifyTextForApplicant);
							});
						}
							
					}
					
				} else {
					LOGGER.error("the group which id="+executiveGroup.getGroupId()+" don't have any hecha member!");
//					throw RuntimeErrorException
//							.errorWith(
//									QualityServiceErrorCode.SCOPE,
//									QualityServiceErrorCode.ERROR_HECHA_MEMBER_EMPTY,
//									localeStringService.getLocalizedString(
//											String.valueOf(QualityServiceErrorCode.SCOPE),
//											String.valueOf(QualityServiceErrorCode.ERROR_HECHA_MEMBER_EMPTY),
//											UserContext.current().getUser().getLocale(),
//											"the group don't have any hecha member!"));
				
				}
				
			}
		} 
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
	
	private QualityInspectionTaskDTO updateVerificationTasks(QualityInspectionTasks task, 
			QualityInspectionTaskRecords record, List<AttachmentDescriptor> attachmentList) {
		
		qualityProvider.updateVerificationTasks(task);
		qualityProvider.createQualityInspectionTaskRecords(record);
		
		User user = UserContext.current().getUser();
		processRecordAttachments(user.getId(), attachmentList, record);
		
		populateRecordAttachements(record, record.getAttachments());
		
		QualityInspectionTaskRecords lastRecord = qualityProvider.listLastRecordByTaskId(task.getId());
    	if(lastRecord != null) {
    		task.setRecord(lastRecord);
    	}

		this.qualityProvider.populateRecordAttachment(lastRecord);
		
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

	@Override
	public ListQualityCategoriesResponse listQualityCategories(
			ListQualityCategoriesCommand cmd) {
		
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
		if(standard != null &&standard.getStatus() != null && standard.getStatus() == QualityStandardStatus.ACTIVE.getCode()) {
			this.qualityProvider.populateStandardGroups(standard);
			
			QualityStandardsDTO standardDto = converStandardToDto(standard);
			createTaskByStandard(standardDto);
		}
		
	}
	
	private QualityStandardsDTO converStandardToDto(QualityInspectionStandards standard) {
		processRepeatSetting(standard);
		QualityStandardsDTO standardDto = ConvertHelper.convert(standard, QualityStandardsDTO.class);
		RepeatSettingsDTO repeatDto = ConvertHelper.convert(standard.getRepeat(), RepeatSettingsDTO.class);
		
		List<StandardGroupDTO> executiveGroup = standard.getExecutiveGroup().stream().map((r) -> {
        	
			StandardGroupDTO dto = ConvertHelper.convert(r, StandardGroupDTO.class);  
			Organization group = organizationProvider.findOrganizationById(r.getGroupId());
			if(group != null)
				dto.setGroupName(group.getName());
        	
        	return dto;
        }).collect(Collectors.toList());

		List<StandardGroupDTO> reviewGroup = standard.getReviewGroup().stream().map((r) -> {
        	
			StandardGroupDTO dto = ConvertHelper.convert(r, StandardGroupDTO.class);  
			Organization group = organizationProvider.findOrganizationById(r.getGroupId());
			if(group != null)
				dto.setGroupName(group.getName());
			
        	return dto;
        }).collect(Collectors.toList());
		standardDto.setRepeat(repeatDto);
		standardDto.setExecutiveGroup(executiveGroup);
		standardDto.setReviewGroup(reviewGroup);
		
		QualityInspectionCategories category = verifiedCategoryById(standard.getCategoryId());
		standardDto.setCategoryName(category.getName());
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
		
		records.stream().map((r) -> {
			populateRecordAttachements(r, r.getAttachments());
			return r;
		});
		
		List<QualityInspectionTaskRecordsDTO> dtos = records.stream().map((r) -> {
			
			QualityInspectionTaskRecordsDTO dto = ConvertHelper.convert(r, QualityInspectionTaskRecordsDTO.class);
			return dto;
		}).collect(Collectors.toList());
		
		return dtos;
	}

	@Override
	public HttpServletResponse exportInspectionTasks(
			ListQualityInspectionTasksCommand cmd, HttpServletResponse response) {
		User user = UserContext.current().getUser();
		Long ownerId = cmd.getOwnerId();
		String ownerType = cmd.getOwnerType();
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
		Integer pageSize = Integer.MAX_VALUE;
		
		
		List<QualityInspectionTasks> tasks = new ArrayList<QualityInspectionTasks>();
        
        if(cmd.getIsReview() != null && cmd.getIsReview() == 1) {
        	List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(user.getId());
        	
        	List<Long>  orgIds = members.stream().map((r) -> {
        		return r.getOrganizationId();
        	}).collect(Collectors.toList());
        	
        	List<Long> standardIds = qualityProvider.listQualityInspectionStandardGroupMapByGroup(
        			orgIds, QualityGroupType.REVIEW_GROUP.getCode());
        	
        	tasks = qualityProvider.listVerificationTasks(locator, pageSize, ownerId, ownerType, 
            		cmd.getTaskType(), null, startDate, endDate, cmd.getGroupId(),
            		cmd.getExecuteStatus(), cmd.getReviewStatus(), false, standardIds, cmd.getManualFlag());

        	
        } else {
        	tasks = qualityProvider.listVerificationTasks(locator, pageSize, ownerId, ownerType, 
        		cmd.getTaskType(), null, startDate, endDate, cmd.getGroupId(),
        		cmd.getExecuteStatus(), cmd.getReviewStatus(), false, null, cmd.getManualFlag());
        }

		List<QualityInspectionTaskRecords> records = new ArrayList<QualityInspectionTaskRecords>();
        for(QualityInspectionTasks task : tasks) {
        	QualityInspectionTaskRecords record = qualityProvider.listLastRecordByTaskId(task.getId());
        	if(record != null) {
        		task.setRecord(record);
            	records.add(task.getRecord());
        	}
        }

		this.qualityProvider.populateRecordAttachments(records);
		
		records.stream().map((r) -> {
			populateRecordAttachements(r, r.getAttachments());
			return r;
		});
        
		List<QualityInspectionTaskDTO> dtoList = convertQualityInspectionTaskToDTO(tasks, null);
		
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
		if(status.equals(QualityInspectionTaskStatus.RECTIFING))
			return "整改中";
		if(status.equals(QualityInspectionTaskStatus.RECTIFIED_AND_WAITING_APPROVAL))
			return "整改完成";
		if(status.equals(QualityInspectionTaskStatus.RECTIFY_CLOSED_AND_WAITING_APPROVAL))
			return "整改关闭";
		if(status.equals(QualityInspectionTaskStatus.CLOSED.getCode()))
			return "已结束";
		return "";
	}
	
	private String resultToString(Byte result) {

		if(result.equals(QualityInspectionTaskResult.NONE.getCode()))
			return "无";
		if(result.equals(QualityInspectionTaskResult.INSPECT_OK.getCode()))
			return "核查合格";
		if(result.equals(QualityInspectionTaskResult.INSPECT_CLOSE.getCode()))
			return "核查关闭";
		if(result.equals(QualityInspectionTaskResult.INSPECT_DELAY.getCode()))
			return "核查延期";
		if(result.equals(QualityInspectionTaskResult.RECTIFIED_OK.getCode()))
			return "复查合格";
		if(result.equals(QualityInspectionTaskResult.RECTIFY_CLOSED.getCode()))
			return "复查关闭";
		if(result.equals(QualityInspectionTaskResult.RECTIFY_DELAY.getCode()))
			return "复查延期";
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
		
		List<OrganizationDTO> groupDtos = organizationService.listUserRelateOrganizations(namespaceId, user.getId(), OrganizationGroupType.GROUP);
		
		return groupDtos;
	}

	@Override
	public ListQualityInspectionLogsResponse listQualityInspectionLogs(
			ListQualityInspectionLogsCommand cmd) {

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        
        List<QualityInspectionLogs> logs = qualityProvider.listQualityInspectionLogs(cmd.getOwnerType(), cmd.getOwnerId(),
        									cmd.getTargetType(), cmd.getTargetId(), locator, pageSize+1);
        
        Long nextPageAnchor = null;
        if(logs != null && logs.size() > pageSize) {
        	logs.remove(logs.size() - 1);
            nextPageAnchor = logs.get(logs.size() - 1).getId();
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
        	
        	if(QualityInspectionLogType.STANDARD.getCode().equals(r.getTargetType()) && r.getTargetId() != null) {
        		QualityInspectionStandards standard = qualityProvider.findStandardById(r.getTargetId());
        		if(standard != null) {
        			dto.setTargetName(standard.getName());
        		}
        	}
        	return dto;
        }).collect(Collectors.toList());
        
        ListQualityInspectionLogsResponse response = new ListQualityInspectionLogsResponse();
        response.setNextPageAnchor(nextPageAnchor);
        response.setDtos(dtos);
		
		return response;
	}

	@Override
	public QualityInspectionTaskDTO createQualityInspectionTask(CreateQualityInspectionTaskCommand cmd) {
		
		User user = UserContext.current().getUser();
		long current = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String day = sdf.format(current);
		
		QualityInspectionTasks task = new QualityInspectionTasks();
		
		task.setOwnerType(cmd.getOwnerType());
		task.setOwnerId(cmd.getOwnerId());
		task.setTaskName(cmd.getName());
		task.setTaskType((byte) 1);
		task.setStatus(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode());
		task.setExecutiveGroupId(cmd.getGroup().getGroupId());
		task.setExecutiveExpireTime(new Timestamp(cmd.getExecutiveExpireTime()));
		task.setCategoryId(cmd.getCategoryId());
		
		QualityInspectionCategories category = verifiedCategoryById(cmd.getCategoryId());
		task.setCategoryPath(category.getPath());
		//fix bug ： byte to long old:task.setManualFlag((byte) 1);
		task.setManualFlag(1L);
		
		task.setExecutorType(OrganizationMemberTargetType.USER.getCode());
		task.setExecutorId(user.getId());
		
		this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_QUALITY_TASK.getCode()).tryEnter(()-> {
			Timestamp startTime = new Timestamp(current);
			task.setExecutiveStartTime(startTime);
			String taskNum = timestampToStr(new Timestamp(current)) + current;
			task.setTaskNumber(taskNum);
			qualityProvider.createVerificationTasks(task);
			
		});
		
		List<QualityInspectionTasks> tasks = new ArrayList<QualityInspectionTasks>();
		tasks.add(task);
		List<QualityInspectionTaskDTO> dtos = convertQualityInspectionTaskToDTO(tasks, user.getId());
		
		return dtos.get(0);
		
	}

}
