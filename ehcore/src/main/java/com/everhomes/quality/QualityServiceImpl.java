package com.everhomes.quality;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.quality.QualityService;
import com.everhomes.repeat.RepeatService;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.OrganizationMemberGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.quality.CreatQualityStandardCommand;
import com.everhomes.rest.quality.DeleteQualityCategoryCommand;
import com.everhomes.rest.quality.DeleteQualityStandardCommand;
import com.everhomes.rest.quality.DeleteFactorCommand;
import com.everhomes.rest.quality.EvaluationDTO;
import com.everhomes.rest.quality.FactorsDTO;
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
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.rest.quality.ProcessType;
import com.everhomes.rest.quality.QualityCategoriesDTO;
import com.everhomes.rest.quality.QualityGroupType;
import com.everhomes.rest.quality.QualityInspectionCategoryStatus;
import com.everhomes.rest.quality.QualityInspectionTaskDTO;
import com.everhomes.rest.quality.QualityInspectionTaskRecordsDTO;
import com.everhomes.rest.quality.QualityInspectionTaskResult;
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
	
	@Override
	public QualityStandardsDTO creatQualityStandard(CreatQualityStandardCommand cmd) {
		
		User user = UserContext.current().getUser();
		
		RepeatSettings repeat = ConvertHelper.convert(cmd.getRepeat(), RepeatSettings.class);
		repeat.setCreatorUid(user.getId());
		repeatService.createRepeatSettings(repeat);

		 
		QualityInspectionStandards standard = new QualityInspectionStandards();
		standard.setOwnerType(cmd.getOwnerType());
		standard.setOwnerId(cmd.getOwnerId());
		standard.setStandardNumber(cmd.getStandardNumber());
		standard.setName(cmd.getName());
		standard.setCategoryId(cmd.getCategoryId());
		standard.setDescription(cmd.getDescription());
		standard.setCreatorUid(user.getId());
		standard.setOperatorUid(user.getId());
		standard.setRepeatSettingId(repeat.getId());
		 
		qualityProvider.createQualityInspectionStandards(standard);
		
		List<StandardGroupDTO> groupList = cmd.getGroup();
		processStandardGroups(groupList, standard);
		processRepeatSetting(standard);
		QualityStandardsDTO dto = ConvertHelper.convert(standard, QualityStandardsDTO.class);
		return dto;
		
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
		if(standard == null) {
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
				QualityInspectionCategories parent = verifiedCategoryById(cmd.getId());
				category.setParentId(cmd.getParentId());
				category.setPath(parent.getPath()+"/"+cmd.getName());
			} else {
				category.setPath("/"+cmd.getName());
			}
			
			qualityProvider.createQualityInspectionCategories(category);
		} else {
			QualityInspectionCategories category = verifiedCategoryById(cmd.getId());
			category.setName(cmd.getName());
			category.setOwnerType(cmd.getOwnerType());
			category.setOwnerId(cmd.getOwnerId());
			if(cmd.getParentId() != null) {
				QualityInspectionCategories parent = verifiedCategoryById(cmd.getId());
				category.setParentId(cmd.getParentId());
				category.setPath(parent.getPath()+"/"+cmd.getName());
			} else {
				category.setPath("/"+cmd.getName());
			}
			
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListQualityInspectionTasksResponse listQualityInspectionTasks(
			ListQualityInspectionTasksCommand cmd) {
		
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
        
        List<QualityInspectionTasks> tasks = qualityProvider.listVerificationTasks(locator, pageSize + 1, ownerId, ownerType, 
        		cmd.getTaskType(), cmd.getExecuteUid(), startDate, endDate, 
        		cmd.getGroupId(), cmd.getExecuteStatus(), cmd.getReviewStatus());
        
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
		
        Long nextPageAnchor = null;
        if(tasks.size() > pageSize) {
        	tasks.remove(tasks.size() - 1);
            nextPageAnchor = tasks.get(tasks.size() - 1).getId();
        }
        
        
        List<QualityInspectionTaskDTO> dtoList = tasks.stream().map((r) -> {
        	
        	QualityInspectionStandards standard = verifiedStandardById(r.getStandardId());
        	QualityInspectionCategories category = verifiedCategoryById(standard.getCategoryId());
        	r.setCategoryName(category.getName());
        	
        	if(cmd.getExecuteUid() != null) {
        		if(r.getExecutorId() != null && r.getExecutorId().equals(cmd.getExecuteUid())) {
        			r.setTaskFlag(QualityTaskType.VERIFY_TASK.getCode());
        		}else if(r.getOperatorId() != null && r.getOperatorId().equals(cmd.getExecuteUid())) {
        			r.setTaskFlag(QualityTaskType.RECTIFY_TASK.getCode());
        		}
        	}
        	
        	QualityInspectionTaskDTO dto = ConvertHelper.convert(r, QualityInspectionTaskDTO.class);  
        	List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(r.getExecutiveGroupId());
        	
        	 List<GroupUserDTO> groupUsers = members.stream().map((mem) -> {
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
             }).filter(member->member!=null).collect(Collectors.toList());
        	 
        	 dto.setGroupUsers(groupUsers);
        	 
        	 QualityInspectionTaskRecordsDTO recordDto = ConvertHelper.convert(r.getRecord(), QualityInspectionTaskRecordsDTO.class);
        	 dto.setRecord(recordDto);
        	
        	return dto;
        }).collect(Collectors.toList());
        
        List<Long> groupIds = tasks.stream().map((r) -> {
        	
        	return r.getExecutiveGroupId();
        }).collect(Collectors.toList());
        
        List<OrganizationMember> members = organizationProvider.getOrganizationMemberByOrgIds(groupIds, OrganizationMemberStatus.ACTIVE);
//        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(cmd.getGroupId());
        
        List<GroupUserDTO> groupUsers = members.stream().map((r) -> {
        	if(OrganizationMemberTargetType.USER.getCode().equals(r.getTargetType()) 
        			&& r.getTargetId() != null && r.getTargetId() != 0) {
        		GroupUserDTO dto = new GroupUserDTO();
            	dto.setUserId(r.getTargetId());
            	dto.setUserName(r.getContactName());
            	dto.setContact(r.getContactToken());
            	dto.setEmployeeNo(r.getEmployeeNo());
            	return dto;
        	} else {
        		return null;
        	}
        }).filter(member->member!=null).collect(Collectors.toList());
        
        return new ListQualityInspectionTasksResponse(nextPageAnchor, dtoList);
	}
	
	@Override
	public QualityInspectionTaskDTO reportVerificationResult(ReportVerificationResultCommand cmd) {
		User user = UserContext.current().getUser();
		QualityInspectionTasks task = verifiedTaskById(cmd.getTaskId());
		QualityInspectionTaskRecords record = new QualityInspectionTaskRecords();
		record.setTaskId(task.getId());
		record.setOperatorType(OwnerType.USER.getCode());
		record.setOperatorId(user.getId());
 
		task.setResult(cmd.getVerificationResult());
		task.setExecutiveTime(new Timestamp(System.currentTimeMillis()));
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
		    map.put("deadline", new Timestamp(cmd.getEndTime()));
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
			msgMap.put("deadline", new Timestamp(cmd.getEndTime()));
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
			String msg = record.getProcessMessage()+attText+cmd.getMessage();
			record.setProcessMessage(msg);
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
		task.setReviewTime(new Timestamp(System.currentTimeMillis()));
		
		record.setProcessType(ProcessType.REVIEW.getCode());
		record.setProcessResult(cmd.getReviewResult());

		updateVerificationTasks(task, record, null);

		
	}

	@Override
	public QualityInspectionTaskDTO reportRectifyResult(ReportRectifyResultCommand cmd) {
		User user = UserContext.current().getUser();
		QualityInspectionTasks task = verifiedTaskById(cmd.getTaskId());
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
		    map.put("deadline", cmd.getEndTime());
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
		    map.put("deadline", cmd.getEndTime());
			int msgCode = QualityNotificationTemplateCode.ASSIGN_TASK_MSG;
			String msg = localeTemplateService.getLocaleTemplateString(scope, msgCode, locale, msgMap, "");
			record.setProcessMessage(msg);
		}
		if(cmd.getMessage() != null) {
			String msg = record.getProcessMessage()+cmd.getMessage();
			record.setProcessMessage(msg);
		}
		
		task.setProcessTime(new Timestamp(System.currentTimeMillis()));

//		processTaskAttachments(user.getId(),  cmd.getAttachments(), task, QualityTaskType.RECTIFY_TASK.getCode());
//		qualityProvider.populateTaskAttachment(task);
		QualityInspectionTaskDTO dto = updateVerificationTasks(task, record, cmd.getAttachments());
		return dto;
	}
	
	@Override
	public void createTaskByStandard(QualityStandardsDTO standard) {
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
			for(StandardGroupDTO executiveGroup : standard.getExecutiveGroup()) {
				
				task.setExecutiveGroupId(executiveGroup.getGroupId());
				
				List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgIdAndMemberGroup(
														executiveGroup.getGroupId(), OrganizationMemberGroupType.HECHA.getCode());
				if(members != null) {
					task.setExecutorType(members.get(0).getTargetType());
					task.setExecutorId(members.get(0).getTargetId());
					List<TimeRangeDTO> timeRanges = repeatService.analyzeTimeRange(standard.getRepeat().getTimeRanges());
					
					if(timeRanges != null && timeRanges.size() > 0) {
						for(TimeRangeDTO timeRange : timeRanges) {
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
						    map.put("deadline", expiredTime);
							String scope = QualityNotificationTemplateCode.SCOPE;
							int code = QualityNotificationTemplateCode.GENERATE_QUALITY_TASK_NOTIFY_EXECUTOR;
							String locale = "zh_CN";
							String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
							sendMessageToUser(members.get(0).getTargetId(), notifyTextForApplicant);
						}
					}
					
				} else {
					LOGGER.error("the group which id="+executiveGroup.getGroupId()+" don't have any hecha member!");
					throw RuntimeErrorException
							.errorWith(
									QualityServiceErrorCode.SCOPE,
									QualityServiceErrorCode.ERROR_HECHA_MEMBER_EMPTY,
									localeStringService.getLocalizedString(
											String.valueOf(QualityServiceErrorCode.SCOPE),
											String.valueOf(QualityServiceErrorCode.ERROR_HECHA_MEMBER_EMPTY),
											UserContext.current().getUser().getLocale(),
											"the group don't have any hecha member!"));
				
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
		
		QualityInspectionTaskDTO dto = ConvertHelper.convert(task, QualityInspectionTaskDTO.class);
		QualityInspectionTaskRecordsDTO recordDto = ConvertHelper.convert(task.getRecord(), QualityInspectionTaskRecordsDTO.class);
   	 	dto.setRecord(recordDto);
		return dto;
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
        
        List<QualityInspectionCategories> qaCategories = qualityProvider.listQualityInspectionCategories(locator, pageSize+1, ownerId, ownerType);
		
        Long nextPageAnchor = null;
        if(qaCategories.size() > pageSize) {
        	qaCategories.remove(qaCategories.size() - 1);
            nextPageAnchor = qaCategories.get(qaCategories.size() - 1).getId();
        }
        
        List<QualityCategoriesDTO> categories = qaCategories.stream().map((r) -> {
        	
        	QualityCategoriesDTO dto = ConvertHelper.convert(r, QualityCategoriesDTO.class);  
        	return dto;
        }).collect(Collectors.toList());
        
        
        return new ListQualityCategoriesResponse(nextPageAnchor, categories);
	}

	@Override
	public void createTaskByStandardId(Long id) {
		QualityInspectionStandards standard = verifiedStandardById(id);
		this.qualityProvider.populateStandardGroups(standard);
		
		QualityStandardsDTO standardDto = converStandardToDto(standard);
		createTaskByStandard(standardDto);
		
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

	@Scheduled(cron="0 0 3 1 * ?" )
	@Override
	public void createEvaluations() {
		
		List<QualityInspectionTasks> closedTasks = qualityProvider.listClosedTask();
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
		
		if(task.getStatus() == QualityInspectionTaskResult.INSPECT_DELAY.getCode() 
				|| task.getStatus() == QualityInspectionTaskResult.RECTIFY_DELAY.getCode()) {
			QualityInspectionStandards standard = verifiedStandardById(task.getStandardId());
			QualityInspectionEvaluationFactors factor = qualityProvider.findQualityInspectionFactorByGroupIdAndCategoryId(
					task.getExecutiveGroupId(), standard.getCategoryId());
			if(factor != null) {
				score = score - factor.getWeight();
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

}
