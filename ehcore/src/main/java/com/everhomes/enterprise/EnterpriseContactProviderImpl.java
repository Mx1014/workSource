package com.everhomes.enterprise;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;

@Component
public class EnterpriseContactProviderImpl implements EnterpriseContactProvider {
    @Autowired
    private DbProvider dbProvider;
    
    public void createContact(EnterpriseContact contact) {
        
    }
    
    public void updateContact(EnterpriseContact contact) {
        
    }
    
    public void deleteContactById(Long id) {
        
    }
    
    public EnterpriseContact getContactById(Long id) {
        return null;
    }
    
    public List<EnterpriseContact> queryContact() {
        return null;
    }
}
