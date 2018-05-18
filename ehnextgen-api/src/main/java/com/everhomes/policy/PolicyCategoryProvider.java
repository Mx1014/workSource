package com.everhomes.policy;

import java.util.List;

public interface PolicyCategoryProvider {
    PolicyCategory createCategory(PolicyCategory bean);
    PolicyCategory updateCategory(PolicyCategory bean);
    void deleteCategory(PolicyCategory bean);
    List<PolicyCategory> searchCategoryByPolicyId(Long policyId,Byte actFlag);
    List<PolicyCategory> searchPolicyByCategory(Long category);
}
