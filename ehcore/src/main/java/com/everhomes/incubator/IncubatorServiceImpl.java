package com.everhomes.incubator;


import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.enterprise.CreateEnterpriseCommand;
import com.everhomes.rest.incubator.*;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class IncubatorServiceImpl implements IncubatorService {

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

		List<IncubatorApply>  list = incubatorProvider.listIncubatorApplies(namespaceId, applyUserId, keyWord, approveStatus, needReject, pageOffset, pageSize + 1, orderBy);

		ListIncubatorApplyResponse response = new ListIncubatorApplyResponse();
		if (list != null && list.size() > pageSize) {
			list.remove(list.size()-1);
			response.setNextPageOffset(pageOffset + 1);
		}


		if(list != null && list.size() > 0){
			List<IncubatorApplyDTO> dtos = new ArrayList<>();
			list.forEach(r ->{
				IncubatorApplyDTO dto = ConvertHelper.convert(r, IncubatorApplyDTO.class);
				populateApproveUserName(dto);
				dtos.add(dto);
			});
			response.setDtos(dtos);
		}

		return response;
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
		IncubatorApply incubatorApply = ConvertHelper.convert(cmd, IncubatorApply.class);
		User user = UserContext.current().getUser();
		incubatorApply.setApplyUserId(user.getId());
		incubatorApply.setApproveStatus(ApproveStatus.WAIT.getCode());
		incubatorProvider.createIncubatorApply(incubatorApply);
		IncubatorApplyDTO dto = ConvertHelper.convert(incubatorApply, IncubatorApplyDTO.class);
		populateApproveUserName(dto);
		return dto;
	}

	@Override
	public void approveIncubatorApply(ApproveIncubatorApplyCommand cmd) {
		IncubatorApply incubatorApply = incubatorProvider.findIncubatorApplyById(cmd.getApplyId());
		User applyUser = userProvider.findUserById(incubatorApply.getApplyUserId());
		UserIdentifier applyIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(applyUser.getId(), IdentifierType.MOBILE.getCode());
		Community community = communityProvider.findCommunityById(applyUser.getCommunityId());
		CommunityGeoPoint communityGeoPoint = communityProvider.findCommunityGeoPointByCommunityId(applyUser.getCommunityId());
		incubatorApply.setApproveStatus(cmd.getApproveStatus());
		incubatorApply.setApproveOpinion(cmd.getApproveOpinion());
		incubatorApply.setApproveTime(new Timestamp(System.currentTimeMillis()));

		if(cmd.getApproveStatus().byteValue() == ApproveStatus.AGREE.getCode()){
			dbProvider.execute((TransactionStatus status) -> {
				//1、更新申请记录为成功
				incubatorProvider.updateIncubatorApply(incubatorApply);

				//2、创建公司
				CreateEnterpriseCommand enterpriseCmd = new CreateEnterpriseCommand();
				enterpriseCmd.setName(incubatorApply.getTeamName());
				enterpriseCmd.setNamespaceId(incubatorApply.getNamespaceId());
				enterpriseCmd.setCommunityId(community.getId());
				enterpriseCmd.setAddress(community.getAddress());
				enterpriseCmd.setLatitude(String.valueOf(communityGeoPoint.getLatitude()));
				enterpriseCmd.setLongitude(String.valueOf(communityGeoPoint.getLongitude()));
				OrganizationDTO  organizationDTO = organizationService.createEnterprise(enterpriseCmd);

				//3、添加当前用户为管理员
				CreateOrganizationAdminCommand adminCommand = new CreateOrganizationAdminCommand();
				adminCommand.setOrganizationId(organizationDTO.getId());
				adminCommand.setContactToken(applyIdentifier.getIdentifierToken());
				adminCommand.setContactName(applyUser.getNickName());
				rolePrivilegeService.createOrganizationAdmin(adminCommand);

				return null;
			});
		}else {
			incubatorProvider.updateIncubatorApply(incubatorApply);
		}
	}

	@Override
	public IncubatorApplyDTO findIncubatorApply(FindIncubatorApplyCommand cmd) {
		Assert.notNull(cmd.getId());
		IncubatorApply incubatorApply = incubatorProvider.findIncubatorApplyById(cmd.getId());
		IncubatorApplyDTO dto = ConvertHelper.convert(incubatorApply, IncubatorApplyDTO.class);
		populateApproveUserName(dto);
		return dto;
	}

	private void populateApproveUserName(IncubatorApplyDTO dto){
		if(dto.getApproveUserId() != null){
			User approveUser = userProvider.findUserById(dto.getApproveUserId());
			if(approveUser != null){
				dto.setApproveUserName(approveUser.getNickName());
			}
		}
	}
}
