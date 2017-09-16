package com.everhomes.incubator;


import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.incubator.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
	private ConfigurationProvider configProvider;

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
	public ListIncubatorProjectTypeResponse listIncubatorProject() {
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
		incubatorProvider.createIncubatorApply(incubatorApply);
		IncubatorApplyDTO dto = ConvertHelper.convert(incubatorApply, IncubatorApplyDTO.class);
		populateApproveUserName(dto);
		return dto;
	}

	@Override
	public void approveIncubatorApply(ApproveIncubatorApplyCommand cmd) {
		User user = UserContext.current().getUser();
		IncubatorApply incubatorApply = incubatorProvider.findIncubatorApplyById(cmd.getApplyId());
		incubatorApply.setApplyUserId(user.getId());
		incubatorApply.setApproveStatus(cmd.getApproveStatus());
		incubatorApply.setApproveOpinion(cmd.getApproveOpinion());
		incubatorApply.setApproveTime(new Timestamp(System.currentTimeMillis()));
		incubatorProvider.updateIncubatorApply(incubatorApply);
	}

	@Override
	public IncubatorApplyDTO findIncubatorApplyById(FindIncubatorApplyCommand cmd) {
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
