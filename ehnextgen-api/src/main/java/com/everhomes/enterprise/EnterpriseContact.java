package com.everhomes.enterprise;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseContacts;
import com.everhomes.util.StringHelper;

public class EnterpriseContact extends EhEnterpriseContacts {
	
	
	
    /**
     * 
     */
    private static final long serialVersionUID = -5189948016191405937L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
    public String getApplyGroup() {
        return EnterpriseContactCustomField.APPLYGROUP.getStringValue(this);
    }
    
    public void setApplyGroup(String applyGroup) {
        EnterpriseContactCustomField.APPLYGROUP.setStringValue(this, applyGroup);
    }

    public String getSex() {
        return EnterpriseContactCustomField.SEX.getStringValue(this);
    }
    
    public void setSex(String sex) {
        EnterpriseContactCustomField.SEX.setStringValue(this, sex);
    }

    public String getEmployeeNo() {
        return EnterpriseContactCustomField.EMPLOYEENO.getStringValue(this);
    }
    
    public void setEmployeeNo(String employeeNo) {
        EnterpriseContactCustomField.EMPLOYEENO.setStringValue(this, employeeNo);
    }
    
}
