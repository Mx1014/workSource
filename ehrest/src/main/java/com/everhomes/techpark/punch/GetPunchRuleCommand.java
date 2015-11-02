package com.everhomes.techpark.punch;

import com.everhomes.util.StringHelper;


/**
* <ul>  
* <li>companyId：企业Id</li>
* </ul>
*/
public class GetPunchRuleCommand {
 
     private Long companyId;
    
    
     public Long getCompanyId() {
         return companyId;
     }

     public void setCompanyId(Long companyId) {
         this.companyId = companyId;
     }

     @Override
     public String toString() {
         return StringHelper.toJsonString(this);
     }

 }
