package com.everhomes.cooperation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.cooperation.NewCooperationCommand;
import com.everhomes.util.ConvertHelper;
//import com.everhomes.cooperation.admin.CooperationDTO;
//import com.everhomes.cooperation.admin.ListCooperationAdminCommand;
//import com.everhomes.cooperation.admin.ListCooperationAdminCommandResponse;

@Component
public class CooperationServiceImpl implements CooperationService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CooperationServiceImpl.class);
	@Autowired
	CooperationProvider cooperationProvider;
	@Autowired
	ConfigurationProvider configurationProvider;

	@Override
	public void newCooperation(NewCooperationCommand cmd) {
		// TODO Auto-generated method stub
		// 类型强制为电话
		cmd.setContactType((byte) 0);
		LOGGER.debug("new Cooperation begin insert , " + cmd.toString() + "");
		CooperationRequests cooperationRequests = ConvertHelper.convert(cmd,
				CooperationRequests.class);
		cooperationProvider.newCooperation(cooperationRequests);
	}

//	@Override
//	public ListCooperationAdminCommandResponse listCooperation(
//			ListCooperationAdminCommand cmd) {
////		User user = UserContext.current().getUser();
////		long userId = user.getId();
//		if (cmd.getKeyword() == null)
//			cmd.setKeyword("");
//		if (cmd.getCooperationType() == null)
//			return null;
//		final long pageSize = cmd.getPageSize() == null ? this.configurationProvider
//				.getIntValue("pagination.page.size",
//						AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd
//				.getPageSize();
//		long pageOffset = cmd.getPageOffset() == null ? 1L : cmd
//				.getPageOffset();
//		long offset = PaginationHelper.offsetFromPageOffset(pageOffset,
//				pageSize);
//		List<CooperationDTO> result = cooperationProvider
//				.listCooperation(cmd.getKeyword(), cmd.getCooperationType(),
//						offset, pageSize)
//				.stream()
//				.map((CooperationRequests r) -> {
//					CooperationDTO dto = ConvertHelper.convert(r,
//							CooperationDTO.class);
//					return dto;
//				}).collect(Collectors.toList());
//		ListCooperationAdminCommandResponse response = new ListCooperationAdminCommandResponse();
//		if (result != null && result.size() >= pageSize) {
//			response.setNextPageOffset((int) pageOffset + 1);
//		}
//		response.setRequests(result);
//		return response;
//	}
}
