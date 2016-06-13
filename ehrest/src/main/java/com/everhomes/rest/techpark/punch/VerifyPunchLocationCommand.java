package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>companyId：企业Id</li>
 * <li>latitude: 坐标纬度</li>
 * <li>longitude： 坐标经度</li>
 * </ul>
 */
public class VerifyPunchLocationCommand{
 
    private Long companyId;
    private Double latitude;
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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
