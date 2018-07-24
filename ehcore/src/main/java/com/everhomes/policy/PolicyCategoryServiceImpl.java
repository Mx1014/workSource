package com.everhomes.policy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PolicyCategoryServiceImpl implements PolicyCategoryService {

    @Autowired
    PolicyCategoryProvider policyCategoryProvider;

    @Override
    public void setPolicyCategory(Long policyId,List<Long> categoryIds) {
        List<PolicyCategory> ctgs = policyCategoryProvider.searchCategoryByPolicyId(policyId,null);
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
            if (null != r){
                PolicyCategory bean = new PolicyCategory();
                bean.setPolicyId(policyId);
                bean.setCategoryId(r);
                bean.setActiveFlag((byte)1);
                policyCategoryProvider.createCategory(bean);
            }
        });
    }

    @Override
    public void deletePolicyCategoryByPolicyId(Long policyId) {
        List<PolicyCategory> beans = policyCategoryProvider.searchCategoryByPolicyId(policyId,null);
        beans.forEach(r->policyCategoryProvider.deleteCategory(r));
    }

    @Override
    public List<PolicyCategory> searchPolicyCategoryByPolicyId(Long policyId,Byte actFlag) {
        return policyCategoryProvider.searchCategoryByPolicyId(policyId,actFlag);
    }

    @Override
    public List<PolicyCategory> searchPolicyByCategory(Long category) {
        return policyCategoryProvider.searchPolicyByCategory(category);
    }
}
