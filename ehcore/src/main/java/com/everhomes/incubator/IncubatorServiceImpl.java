package com.everhomes.incubator;


import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.activity.ActivityServiceImpl;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.filedownload.Task;
import com.everhomes.filedownload.TaskService;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.enterprise.CreateEnterpriseCommand;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.incubator.*;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.poll.RepeatFlag;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhIncubatorApplies;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.*;

@Component
public class IncubatorServiceImpl implements IncubatorService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityServiceImpl.class);
	@Autowired
	IncubatorProvider incubatorProvider;
	@Autowired
	UserProvider userProvider;
	@Autowired
	OrganizationService organizationService;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private ConfigurationProvider configProvider;

	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private RolePrivilegeService rolePrivilegeService;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Autowired
	private CoordinationProvider coordinationProvider;

	@Override
	public ListIncubatorApplyResponse listIncubatorApply(ListIncubatorApplyCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		Long applyUserId = cmd.getApplyUserId();
		String keyWord = cmd.getKeyWord();
		Byte approveStatus = cmd.getApproveStatus();
		Byte needReject = cmd.getNeedReject();
		Byte orderBy = cmd.getOrderBy();

		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		Integer pageOffset = 1;
		if (cmd.getPageOffset() != null){
			pageOffset = cmd.getPageOffset();
		}

		List<IncubatorApply>  list = incubatorProvider.listIncubatorApplies(namespaceId, applyUserId, keyWord, approveStatus, needReject,
				pageOffset, pageSize + 1, orderBy, cmd.getApplyType(), cmd.getStartTime(), cmd.getEndTime());

		ListIncubatorApplyResponse response = new ListIncubatorApplyResponse();
		if (list != null && list.size() > pageSize) {
			list.remove(list.size()-1);
			response.setNextPageOffset(pageOffset + 1);
		}


		if(list != null && list.size() > 0){
			List<IncubatorApplyDTO> dtos = new ArrayList<>();
			list.forEach(r ->{
				IncubatorApplyDTO dto = ConvertHelper.convert(r, IncubatorApplyDTO.class);
				populateDto(dto);
				dtos.add(dto);
			});
			response.setDtos(dtos);
		}

		return response;
	}


	@Override
	public List<IncubatorApplyDTO> listMyTeams() {
		List<IncubatorApply> applies = new ArrayList<>();
		List<Long> rootIds = incubatorProvider.listRootIdByUserId(UserContext.currentUserId());
		if(rootIds != null){
			for (Long rootId: rootIds){
				IncubatorApply latestValidApply = incubatorProvider.findLatestValidByRootId(rootId);
				if(latestValidApply != null){
					applies.add(latestValidApply);
				}
			}
		}

		List<IncubatorApplyDTO> dtos = new ArrayList<>();
		applies.forEach(r ->{
			IncubatorApplyDTO dto = ConvertHelper.convert(r, IncubatorApplyDTO.class);
			populateDto(dto);
			dtos.add(dto);
		});
		return dtos;
	}



	@Override
	public void exportIncubatorApply(ExportIncubatorApplyCommand cmd) {
		Map<String, Object> params = new HashMap();

		//如果是null的话会被传成“null”
		if(cmd.getNamespaceId() != null){
			params.put("namespaceId", cmd.getNamespaceId());
		}
		if(cmd.getKeyWord() != null){
			params.put("keyWord", cmd.getKeyWord());
		}
		if(cmd.getApproveStatus() != null){
			params.put("approveStatus", cmd.getApproveStatus());
		}
		if(cmd.getNeedReject() != null){
			params.put("needReject", cmd.getNeedReject());
		}
		if(cmd.getOrderBy() != null){
			params.put("orderBy", cmd.getOrderBy());
		}
		if(cmd.getApplyType() != null){
			params.put("applyType", cmd.getApplyType());
		}
		if(cmd.getStartTime() != null){
			params.put("startTime", cmd.getStartTime());
		}
		if(cmd.getEndTime() != null){
			params.put("endTime", cmd.getEndTime());
		}

		String statusName = ApproveStatus.fromCode(cmd.getApproveStatus()) == null ? "全部": ApproveStatus.fromCode(cmd.getApproveStatus()).getText();
		String fileName = String.format("入驻申请企业_%s_%s.xlsx", statusName, DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));

		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), IncubatorApplyExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());
	}

	@Override
	public IncubatorApplyDTO findIncubatorAppling(FindIncubatorApplingCommand cmd) {
		Long userId = UserContext.currentUserId();
		List<IncubatorApply> incubatorApplies = incubatorProvider.listIncubatorAppling(userId, cmd.getRootId());

		if(incubatorApplies == null || incubatorApplies.size() == 0){
			return null;
		}

		IncubatorApplyDTO dto = ConvertHelper.convert(incubatorApplies.get(0), IncubatorApplyDTO.class);
		populateDto(dto);
		return dto;
	}

	@Override
	public ListIncubatorProjectTypeResponse listIncubatorProjectType() {
		List<IncubatorProjectType> list = incubatorProvider.listIncubatorProjectType();
		List<IncubatorProjectTypeDTO> dtos = new ArrayList<>();
		if(list != null){
			list.forEach(r ->
				dtos.add(ConvertHelper.convert(r, IncubatorProjectTypeDTO.class))
			);
		}

		ListIncubatorProjectTypeResponse response = new ListIncubatorProjectTypeResponse();
		response.setDtos(dtos);
		return response;
	}

	@Override
	public IncubatorApplyDTO addIncubatorApply(AddIncubatorApplyCommand cmd) {

		if(cmd.getNamespaceId() == null || cmd.getCommunityId() == null || cmd.getApplyType() == null){
			LOGGER.error("ERROR_INVALID_PARAMS, namespaceId or communityId or applyType is null, namespaceid={}, communityId={}, applyType", cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getApplyType());
			throw RuntimeErrorException.errorWith(IncubatorServiceErrorCode.SCOPE, IncubatorServiceErrorCode.ERROR_INVALID_PARAMS,
					"ERROR_INVALID_PARAMS");
		}

		User user = UserContext.current().getUser();

		//如果是有parentId的，要判断当前申请记录的树结构中有没有正在审核中的记录
		if(cmd.getParentId() != null){

			IncubatorApply parentApply = incubatorProvider.findIncubatorApplyById(cmd.getParentId());

			List<IncubatorApply> applies = incubatorProvider.listIncubatorAppling(user.getId(), parentApply.getRootId());

			//在一棵树只允许一个申请中的记录。有一种情况是例外的，如果它只有一个申请中的，并且申请中的就是父节点，即是申请中的记录重新申请。
			if(applies != null && applies.size() > 0 && !applies.get(0).getId().equals(cmd.getParentId())){
				LOGGER.error("incubatorAppling id={}", applies.get(0).getId());
				throw RuntimeErrorException.errorWith(IncubatorServiceErrorCode.SCOPE, IncubatorServiceErrorCode.ERROR_HAVE_WAITTING_APPLY,
						"ERROR_HAVE_WAITTING_APPLY");
			}
		}

		IncubatorApply incubatorApply = ConvertHelper.convert(cmd, IncubatorApply.class);

		incubatorApply.setApplyUserId(user.getId());
		incubatorApply.setApproveStatus(ApproveStatus.WAIT.getCode());
		incubatorApply.setCreateTime(new Timestamp(System.currentTimeMillis()));


		coordinationProvider.getNamedLock(CoordinationLocks.PORTAL_PUBLISH.getCode() + user.getId()).enter(()->{


			IncubatorApply sameApply = incubatorProvider.findSameApply(incubatorApply);

			if(sameApply != null){
				LOGGER.error("repeat apply, see you apply history.");
				throw RuntimeErrorException.errorWith(IncubatorServiceErrorCode.SCOPE, IncubatorServiceErrorCode.ERROR_REPEAT_APPLY,
						"repeat apply, see you apply history.");
			}

			dbProvider.execute((status)->{

				if(cmd.getParentId() == null){
					long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhIncubatorApplies.class));
					incubatorApply.setId(id);
					incubatorApply.setRootId(id);
				}else {
					IncubatorApply parent = incubatorProvider.findIncubatorApplyById(cmd.getParentId());
					incubatorApply.setRootId(parent.getRootId());

					//产品要求删除取消的，为了保证数据结构，设置新记录的parentid为grandfather的id
					if(ApproveStatus.fromCode(parent.getApproveStatus()) == ApproveStatus.WAIT){
						incubatorApply.setParentId(parent.getParentId());
						incubatorProvider.deleteIncubatorApplyById(parent.getId());
					}
				}

				incubatorProvider.createIncubatorApply(incubatorApply);

				//保存附件
				saveAttachment(cmd.getBusinessLicenceAttachments(), incubatorApply.getId(), user.getId(), IncubatorApplyAttachmentType.BUSINESS_LICENCE.getCode());
				saveAttachment(cmd.getPlanBookAttachments(), incubatorApply.getId(), user.getId(), IncubatorApplyAttachmentType.PLAN_BOOK.getCode());

				return null;
			});

			return null;
		});

		IncubatorApplyDTO dto = ConvertHelper.convert(incubatorApply, IncubatorApplyDTO.class);
		populateDto(dto);
		return dto;
	}

	private void saveAttachment(List<IncubatorApplyAttachmentDTO> list, Long incubatorApplyId, Long uid, Byte type){
		if(list != null){
			for (int i = 0; i<list.size(); i++){
				IncubatorApplyAttachment attachment = ConvertHelper.convert(list.get(i), IncubatorApplyAttachment.class);
				attachment.setType(type);
				attachment.setCreatorUid(uid);
				attachment.setIncubatorApplyId(incubatorApplyId);
				incubatorProvider.createAttachment(attachment);
			}
		}
	}

	@Override
	public IncubatorApplyDTO updateIncubatorApply(UpdateIncubatorApplyCommand cmd) {
		IncubatorApply apply = incubatorProvider.findIncubatorApplyById(cmd.getId());
		apply.setCheckFlag(cmd.getCheckFlag());
		incubatorProvider.updateIncubatorApply(apply);

		IncubatorApplyDTO dto = ConvertHelper.convert(apply, IncubatorApplyDTO.class);
		populateDto(dto);
		return dto;
	}

	@Override
	public void cancelIncubatorApply(CancelIncubatorApplyCommand cmd) {
		incubatorProvider.deleteIncubatorApplyById(cmd.getId());
	}

	@Override
	public void approveIncubatorApply(ApproveIncubatorApplyCommand cmd) {
		IncubatorApply incubatorApply = incubatorProvider.findIncubatorApplyById(cmd.getApplyId());
		User applyUser = userProvider.findUserById(incubatorApply.getApplyUserId());
		UserIdentifier applyIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(applyUser.getId(), IdentifierType.MOBILE.getCode());
		Community community = communityProvider.findCommunityById(incubatorApply.getCommunityId());
		CommunityGeoPoint communityGeoPoint = communityProvider.findCommunityGeoPointByCommunityId(incubatorApply.getCommunityId());
		incubatorApply.setApproveStatus(cmd.getApproveStatus());
		incubatorApply.setApproveOpinion(cmd.getApproveOpinion());
		incubatorApply.setApproveTime(new Timestamp(System.currentTimeMillis()));
		incubatorApply.setApproveUserId(UserContext.currentUserId());

		boolean haveAgreeFlag = checkExistAgreeByRootId(incubatorApply.getRootId());

		dbProvider.execute((TransactionStatus status) -> {

			//如果不同意或者之前同意过则直接更新记录，不用再重复创建公司了。
			if(ApproveStatus.fromCode(cmd.getApproveStatus()) != ApproveStatus.AGREE || haveAgreeFlag){
				incubatorProvider.updateIncubatorApply(incubatorApply);
			}else {

				//创建公司
				CreateEnterpriseCommand enterpriseCmd = new CreateEnterpriseCommand();
				enterpriseCmd.setName(incubatorApply.getTeamName());
				enterpriseCmd.setNamespaceId(incubatorApply.getNamespaceId());
				enterpriseCmd.setCommunityId(community.getId());
				enterpriseCmd.setAddress(community.getAddress());
				enterpriseCmd.setLatitude(String.valueOf(communityGeoPoint.getLatitude()));
				enterpriseCmd.setLongitude(String.valueOf(communityGeoPoint.getLongitude()));
				OrganizationDTO  organizationDTO = organizationService.createEnterprise(enterpriseCmd);

				//添加当前用户为管理员
				CreateOrganizationAdminCommand adminCommand = new CreateOrganizationAdminCommand();
				adminCommand.setOrganizationId(organizationDTO.getId());
				adminCommand.setContactToken(applyIdentifier.getIdentifierToken());
				adminCommand.setContactName(applyUser.getNickName());
				rolePrivilegeService.createOrganizationAdmin(adminCommand);

				//更新申请记录为成功
				incubatorApply.setOrganizationId(organizationDTO.getId());
				incubatorProvider.updateIncubatorApply(incubatorApply);
			}

			return null;
		});
	}

	@Override
	public IncubatorApplyDTO findIncubatorApply(FindIncubatorApplyCommand cmd) {
		Assert.notNull(cmd.getId());
		IncubatorApply incubatorApply = incubatorProvider.findIncubatorApplyById(cmd.getId());
		IncubatorApplyDTO dto = ConvertHelper.convert(incubatorApply, IncubatorApplyDTO.class);
		populateDto(dto);
		return dto;
	}


	public boolean checkExistAgreeByRootId(Long rootId){
		List<IncubatorApply> incubatorApplies = incubatorProvider.listIncubatorAppliesByRootId(rootId);
		for(IncubatorApply in: incubatorApplies){
			if(ApproveStatus.fromCode(in.getApproveStatus()) == ApproveStatus.AGREE){
				return true;
			}
		}
		return false;
	}


	private void populateDto(IncubatorApplyDTO dto){
		populateApproveUserName(dto);
		populateAttachments(dto);
		populateReApplyFlag(dto);
	}

	private void populateReApplyFlag(IncubatorApplyDTO dto){

		//自己是审核中的是可以申请的
		if(ApproveStatus.fromCode(dto.getApproveStatus()) == ApproveStatus.WAIT){
			dto.setReApplyFlag(TrueOrFalseFlag.TRUE.getCode());
			return;
		}

		//自己不是审核中，一个用户只允许一个审核中的，已经有的话不允许在申请
		List<IncubatorApply> incubatorApplies = incubatorProvider.listIncubatorAppling(dto.getApplyUserId(), dto.getRootId());
		if(incubatorApplies == null || incubatorApplies.size() == 0){
			dto.setReApplyFlag(TrueOrFalseFlag.TRUE.getCode());
		}else {
			dto.setReApplyFlag(TrueOrFalseFlag.FALSE.getCode());
		}

	}

	private void populateApproveUserName(IncubatorApplyDTO dto){
		if(dto.getApproveUserId() != null){
			User approveUser = userProvider.findUserById(dto.getApproveUserId());
			if(approveUser != null){
				dto.setApproveUserName(approveUser.getNickName());
			}
		}
	}

	private void populateAttachments(IncubatorApplyDTO dto){
		List<IncubatorApplyAttachment> businessLicence= incubatorProvider.listAttachmentsByApplyId(dto.getId(), IncubatorApplyAttachmentType.BUSINESS_LICENCE.getCode());
		List<IncubatorApplyAttachmentDTO> businessLicenceDto = new ArrayList<>();
		if(businessLicence != null){
			businessLicence.forEach(r -> {
				businessLicenceDto.add(convertIncubatorApplyAttachment(r));
			});
		}
		dto.setBusinessLicenceAttachments(businessLicenceDto);

		List<IncubatorApplyAttachment> planBook= incubatorProvider.listAttachmentsByApplyId(dto.getId(), IncubatorApplyAttachmentType.PLAN_BOOK.getCode());
		List<IncubatorApplyAttachmentDTO> planBookDto = new ArrayList<>();
		if(planBook != null){
			planBook.forEach(r -> {
				planBookDto.add(convertIncubatorApplyAttachment(r));
			});
		}
		dto.setPlanBookAttachments(planBookDto);
	}

	private IncubatorApplyAttachmentDTO convertIncubatorApplyAttachment(IncubatorApplyAttachment r){
		IncubatorApplyAttachmentDTO temp = ConvertHelper.convert(r, IncubatorApplyAttachmentDTO.class);
		temp.setCreateTime(null);
		String contentUrl = contentServerService.parserUri(temp.getContentUri(), IncubatorApplyAttachment.class.getSimpleName(), 1L);
		temp.setContentUrl(contentUrl);
		return temp;
	}
}
