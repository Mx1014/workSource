package com.everhomes.policy;

import java.util.List;

public interface PolicyCategoryService {
    void setPolicyCategory(Long policyId,List<Long> categoryIds);
    void deletePolicyCategoryByPolicyId(Long policyId);
    List<PolicyCategory> searchPolicyCategoryByPolicyId(Long policyId,Byte actFlag);
    List<PolicyCategory> searchPolicyByCategory(Long category);
}
