// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.rest.enterprisemoment.MomentTagDTO;

import java.util.List;

public interface EnterpriseMomentTagProvider {

	void createEnterpriseMomentTag(EnterpriseMomentTag enterpriseMomentTag);

	List<EnterpriseMomentTag> batchCreateEnterpriseMomentTag(Integer namespaceId, Long creatorUid, Long organizationId, List<MomentTagDTO> tagDTOS );

	void batchUpdateEnterpriseMomentTag(Integer namespaceId, Long operatorUid, Long organizationId, List<MomentTagDTO> tagDTOS);

	void batchDeleteEnterpriseMomentTag(Integer namespaceId, Long deleteUid, Long organizationId, List<MomentTagDTO> tagDTOS);

	EnterpriseMomentTag findEnterpriseMomentTagById(Long id);

	List<EnterpriseMomentTag> listEnterpriseMomentTag(Integer namespaceId, Long organizationId);

	boolean isNeedInitTag(Integer namespaceId, Long organizationId);
}