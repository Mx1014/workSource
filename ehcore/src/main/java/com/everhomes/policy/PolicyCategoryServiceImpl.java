package com.everhomes.policy;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PolicyCategoryServiceImpl implements PolicyCategoryService {

    @Autowired
    PolicyCategoryProvider policyCategoryProvider;

    @Override
    public void setPolicyCategory(Long policyId,List<Long> categoryIds) {
        List<PolicyCategory> ctgs = policyCategoryProvider.searchCategoryByPolicyId(policyId);
        ctgs.forEach(r -> {
            if(!categoryIds.contains(r.getCategoryId())){
                r.setActiveFlag((byte)0);
            } else {
                r.setActiveFlag((byte)1);
                categoryIds.remove(r.getCategoryId());
            }
            policyCategoryProvider.updateCategory(r);
        });
        categoryIds.forEach(r -> {
            PolicyCategory bean = new PolicyCategory();
            bean.setPolicyId(policyId);
            bean.setCategoryId(r);
            bean.setActiveFlag((byte)1);
            policyCategoryProvider.createCategory(bean);
        });
    }

    @Override
    public void deletePolicyCategoryByPolicyId(Long policyId) {
        List<PolicyCategory> beans = policyCategoryProvider.searchCategoryByPolicyId(policyId);
        beans.forEach(r->policyCategoryProvider.deleteCategory(r));
    }

    @Override
    public List<PolicyCategory> searchPolicyCategoryByPolicyId(Long policyId) {
        return policyCategoryProvider.searchCategoryByPolicyId(policyId);
    }
}
