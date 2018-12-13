package com.everhomes.contract.template;

public class ContractTemplateFactory {
	
	public static final String ADJUSTS = "adjusts";
	public static final String FREES = "frees";
	public static final String APARTMENTS = "apartments";
	public static final String CHARGINGITEMS = "chargingItems";
	public static final String CONTRACT = "contract";
	public static final String ENTERPRISECUSTOMER = "enterpriseCustomer";
	public static final String INVESTMENTPROMOTION = "investmentPromotion";
	
	public static ContractTemplateHandler createContractTemplateHandler(String type) {
        switch (type) {
            case ADJUSTS:
            	return new AdjustsContractTemplate();
            case FREES:
                return new FreesContractTemplate();
            case APARTMENTS:
            	return new ApartmentsContractTemplate();
            case CHARGINGITEMS:	
            	return new ChargingItemsContractTemplate();
            case CONTRACT:
            	return new DefaultContractTemplate();
            case ENTERPRISECUSTOMER:	
            	return new EnterpriseCustomerContractTemplate();
            case INVESTMENTPROMOTION:
            	return new InvestmentPromotionContractTemplate();
            default:
                return null;
        }
    }
}
