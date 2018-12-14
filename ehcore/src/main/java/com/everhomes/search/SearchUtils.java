package com.everhomes.search;

public class SearchUtils {
    //public static final String TOPICINDEXNAME = "everhomesv3";
    public static final String TOPICINDEXTYPE = "topic";
    
    //public static final String GROUPINDEXNAME = "everhomesv3";
    public static final String GROUPINDEXTYPE = "group";
    
    //public static final String COMMUNITYINDEXNAME = "everhomesv3";
    public static final String COMMUNITYINDEXTYPE = "community";
    
    //public static final String ENTERPRISEINDEXNAME = "everhomesv3";
    public static final String ENTERPRISEINDEXTYPE = "enterprise";
    
    public static final String CONFACCOUNTINDEXTYPE = "confaccount";
    
    public static final String ENTERPRISECONTACTINDEXTYPE = "enterprisecontact";
    
    public static final String CONFENTERPRISEINDEXTYPE = "confenterprise";
    
    public static final String CONFORDERINDEXTYPE = "conforder";
    
    public static final String HOTTAGINDEXTYPE = "hottag";

    public static final String NEWS = "news";

    public static final String EQUIPMENTTASKINDEXTYPE = "equipmentTask";

    public static final String EQUIPMENTPLANINDEXTYPE = "equipmentPlan";

    public static final String EQUIPMENTACCESSORYINDEXTYPE = "equipmentAccessory";
    
    public static final String EQUIPMENTINDEXTYPE = "equipment";
    
    public static final String EQUIPMENTSTANDARDINDEXTYPE = "equipmentStandard";
    
    public static final String PMOWNERINDEXTYPE = "pmowner";

    public static final String PM_OWNER_CAR_INDEX_TYPE = "organizationOwnerCar";

    public static final String PMTASK = "pmtask";
    
    public static final String SAREQUEST = "saRequest";
    
    public static final String SETTLEREQUEST = "settleRequest";

    public static final String RESERVEREQUEST = "reserveRequest";

    public static final String EQUIPMENTSTANDARDMAP = "equipmentStandardMap";

    public static final String APARTMENTREQUEST = "apartmentRequest";
    
    public static final String ENERGY_METER = "energyMeter";

    public static final String ENERGY_METER_READING_LOG = "energyMeterReadingLog";
    public static final String QUALITY_TASK = "qualityTask";
    public static final String QUALITY_SAMPLE = "qualitySample";

    public static final String WAREHOUSE = "warehouse";

    public static final String WAREHOUSE_MATERIAL = "warehouseMaterial";

    public static final String WAREHOUSE_MATERIAL_CATEGORY = "warehouseMaterialCategory";

    public static final String WAREHOUSE_STOCK = "warehouseStock";

    public static final String WAREHOUSE_STOCK_LOG = "warehouseStockLog";

    public static final String WAREHOUSE_REQUEST_MATERIAL= "warehouseRequestMaterial";
    public static final String CONTRACT= "contract";
    public static final String ENTERPRISE_CUSTOMER= "enterpriseCustomer";

    public static final String UNIONGROUP_DETAILS= "uniongroupDetails";
    public static final String ENERGY_PLAN= "energyPlan";
    public static final String ENERGY_TASK= "energyTask";
    public static final String PAYMENT_APPLICATION= "paymentApplication";
	public static final String MESSAGE_RECORD= "messageRecord";
    public static final String VISITORSYS= "visitorsys";
    public static final String FREQVISITOR= "freqvisitor";

    public static final String GENERAL_FORM_VALS = "generalFormVals";

    public static Long getLongField(Object o) {
        Long v = -1l;
        String s = "";
        try {
        	if(null == o){
        		return v;
        	}
            v = (Long)o;
            return v;
        }
        catch(Exception e) {
            s = e.getMessage();
        }
    
        try {
            v = new Long((Integer)o);
            return v;
        }
        catch(Exception e) {
            s += e.getMessage();
        }
        
        throw new ClassCastException(s);
    }
}
