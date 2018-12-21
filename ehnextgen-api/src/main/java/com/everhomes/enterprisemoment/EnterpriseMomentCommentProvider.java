// @formatter:off
package com.everhomes.enterprisemoment;

import java.util.List;

public interface EnterpriseMomentCommentProvider {

    void createEnterpriseMomentComment(EnterpriseMomentComment enterpriseMomentComment);

    void deleteEnterpriseMomentComment(EnterpriseMomentComment enterpriseMomentComment);

    EnterpriseMomentComment findEnterpriseMomentCommentById(Long id);

    List<EnterpriseMomentComment> listEnterpriseMomentCommentsDesc(Integer namespaceId, Long organizationId, Long momentId, Long pageAnchor, Integer pageSize);

    int countEnterpriseMomentComments(Integer namespaceId, Long organizationId, Long momentId);

    List<Long> findCommentUserIds(Integer namespaceId, Long organizationId, Long momentId);

	List<EnterpriseMomentComment> listEnterpriseMomentCommentsAsc(Integer namespaceId, Long organizationId, Long momentId, Long pageAnchor, Integer pageSize);

}