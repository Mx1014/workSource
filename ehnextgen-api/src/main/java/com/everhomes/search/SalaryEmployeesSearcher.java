// @formatter:off
package com.everhomes.search;

import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.salary.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface SalaryEmployeesSearcher {
	void deleteById(Long id);

//	void bulkUpdate(List<UniongroupMemberDetail> uniongroupMemberDetails);

//	void feedDoc(UniongroupMemberDetail uniongroupMemberDetail);

	void syncUniongroupDetailsIndexs();

//	void syncUniongroupDetailsAtOrg(Organization org, String groupType);

//	List query(SearchUniongroupDetailCommand cmd);

	void deleteAll();

	void refresh();
}