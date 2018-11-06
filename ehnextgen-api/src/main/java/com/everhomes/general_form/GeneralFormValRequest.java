package com.everhomes.general_form;

import com.everhomes.server.schema.tables.pojos.EhGeneralFormValRequests;
import com.everhomes.util.StringHelper;

public class GeneralFormValRequest extends EhGeneralFormValRequests {

    private static final long serialVersionUID = 1051198528678316009L;
    
    public Long getTransformStatus(){
        return GeneralFormValRequestCustomField.TRANSFORM_STATUS.getIntegralValue(this);
    }
    
    public void setTransformStatus(Long transformStatus) {
    	GeneralFormValRequestCustomField.TRANSFORM_STATUS.setIntegralValue(this, transformStatus);
    }
    
    public Long getInvestmentAdId(){
        return GeneralFormValRequestCustomField.INVESTMENT_AD_ID.getIntegralValue(this);
    }
    
    public void setInvestmentAdId(Long investmentAdId) {
    	GeneralFormValRequestCustomField.INVESTMENT_AD_ID.setIntegralValue(this, investmentAdId);
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
