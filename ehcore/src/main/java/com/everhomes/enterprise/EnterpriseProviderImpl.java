package com.everhomes.enterprise;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;
import com.everhomes.group.GroupProvider;

@Component
public class EnterpriseProviderImpl implements EnterpriseProvider {
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private GroupProvider groupProvider;
    
    //TODO enterprise field
    public void createEnterprise(Enterprise enterprise) {
        
    }
    
    public void updateEnterprise(Enterprise enterprise) {
        
    }
    
    public Enterprise getEnterpriseById(Long id) {
        return null;
    }
    
    public void deleteEnterpriseById(Long id) {
        
    }
    
    public List<Enterprise> queryEnterprise() {
        return null;
    }
    
    public void createEnterpriseCommunityMap(EnterpriseCommunityMap ec) {
        
    }
    
    public void updateEnterpriseCommunityMap(EnterpriseCommunityMap ec) {
        
    }
    
    public void deleteEnterpriseCommunityMapById(Long id) {
        
    }
    
    public List<EnterpriseCommunityMap> queryEnterpriseCommunityMap() {
        return null;
    }
}
