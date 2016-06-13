package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

 /**
 * <ul>
 * <li>userId: 用户id</li>
 * <li>companyId：企业Id</li>
 * </ul>
 */
public class GetPunchLocationCommand {

    private Long userId;
    private Long companyId;

     public Long getUserId() {
         return userId;
     }

     public void setUserId(Long userId) {
         this.userId = userId;
     }

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
