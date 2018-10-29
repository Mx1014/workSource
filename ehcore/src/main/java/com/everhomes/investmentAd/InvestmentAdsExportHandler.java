package com.everhomes.investmentAd;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.contract.ContractErrorCode;
import com.everhomes.rest.investmentAd.InvestmentAdAssetType;
import com.everhomes.rest.investmentAd.InvestmentAdErrorCode;
import com.everhomes.rest.investmentAd.InvestmentStatus;
import com.everhomes.rest.investmentAd.InvestmentType;
import com.everhomes.rest.investmentAd.ListInvestmentAdCommand;
import com.everhomes.rest.investmentAd.RelatedAssetDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.ExcelUtils;

import ch.qos.logback.core.joran.conditional.ElseAction;

@Component
public class InvestmentAdsExportHandler implements FileDownloadTaskHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentAdsExportHandler.class);
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FileDownloadTaskService fileDownloadTaskService;
	
	@Autowired
	private InvestmentAdProvider investmentAdProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Override
	public void beforeExecute(Map<String, Object> params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Map<String, Object> params) {
		//前端传过来的所有数据信息
        String userStr =  String.valueOf(params.get("UserContext"));
        User user = (User) StringHelper.fromJsonString(userStr, User.class);
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        OutputStream outputStream ;
    	
        String cmdStr = String.valueOf( params.get("ListInvestmentAdCommand"));
         
    	ListInvestmentAdCommand cmd = (ListInvestmentAdCommand)StringHelper.fromJsonString(cmdStr, ListInvestmentAdCommand.class);
		cmd.setPageAnchor(0L);
		cmd.setPageSize(10000);
    	user.setNamespaceId(cmd.getNamespaceId());
        UserContext.setCurrentUser(user);
    	
        outputStream = generateOutputStream(cmd,taskId);
        
        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
        taskService.processUpdateTask(taskId, fileLocationDTO);
	}

	private OutputStream generateOutputStream(ListInvestmentAdCommand cmd, Long taskId) {
		List<String> propertyNames = new ArrayList<String>();
        List<String> titleName = new ArrayList<String>();
        List<Integer> titleSize = new ArrayList<Integer>();
        createExcelTemplate(propertyNames, titleName, titleSize);
        
        List<Map<String, String>> dataList = new ArrayList<>();
        List<InvestmentAd> investmentAds = investmentAdProvider.listInvestmentAds(cmd);
        if (investmentAds != null && investmentAds.size() > 0) {
        	for (InvestmentAd investmentAd : investmentAds) {
            	Map<String, String> data = fillInData(investmentAd);
            	dataList.add(data);
    		}
        	Community community = communityProvider.findCommunityById(cmd.getCommunityId());
    		if (community == null) {
    			LOGGER.error("Community does not exist.");
    			throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, "Community is not exist.");
    		}
    		String fileName = String.format("房源招商广告信息_%s_%s", community.getName(), com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH))+ ".xlsx";
			ExcelUtils excelUtils = new ExcelUtils(null, fileName, "房源招商广告");
			return excelUtils.getOutputStream(propertyNames, titleName, titleSize, dataList);
        }else {
			throw errorWith(InvestmentAdErrorCode.SCOPE, InvestmentAdErrorCode.ERROR_NO_DATA, "no data.");
		}
	}
	
	private void createExcelTemplate(List<String> propertyNames,List<String> titleName,List<Integer> titleSize){
		propertyNames.add("title");
        titleName.add("标题");
        titleSize.add(20);
        propertyNames.add("investmentType");
        titleName.add("招商类型");
        titleSize.add(20);
        propertyNames.add("investmentStatus");
        titleName.add("招商状态");
        titleSize.add(20);
        propertyNames.add("availableArea");
        titleName.add("面积(㎡)");
        titleSize.add(20);
        propertyNames.add("assetPrice");
        titleName.add("租金（元/㎡*月）");
        titleSize.add(20);
        propertyNames.add("apartmentFloor");
        titleName.add("楼层");
        titleSize.add(20);
        propertyNames.add("orientation");
        titleName.add("朝向");
        titleSize.add(20);
        propertyNames.add("address");
        titleName.add("地址");
        titleSize.add(20);
        propertyNames.add("contactPhone");
        titleName.add("咨询电话");
        titleSize.add(20);
        propertyNames.add("description");
        titleName.add("描述");
        titleSize.add(20);
        propertyNames.add("relatedAssets");
        titleName.add("关联楼宇房源");
        titleSize.add(20);
	}
	
	private Map<String, String> fillInData(InvestmentAd investmentAd){
		Map<String, String> detail = new HashMap<String, String>();
    	
    	detail.put("title", investmentAd.getTitle());
    	
    	if (investmentAd.getInvestmentType() == InvestmentType.RENT.getCode()) {
    		detail.put("investmentType", InvestmentType.RENT.getContent());
		}else if (investmentAd.getInvestmentType() == InvestmentType.SALE.getCode()) {
			detail.put("investmentType", InvestmentType.SALE.getContent());
		}else {
			detail.put("investmentType", null);
		}
    	
    	if (investmentAd.getInvestmentStatus() == InvestmentStatus.ONLINE.getCode()) {
    		detail.put("investmentStatus", InvestmentStatus.ONLINE.getContent());
		}else if (investmentAd.getInvestmentStatus() == InvestmentStatus.OFFLINE.getCode()) {
			detail.put("investmentStatus", InvestmentStatus.OFFLINE.getContent());
		}else if (investmentAd.getInvestmentStatus() == InvestmentStatus.RENTED.getCode()) {
			detail.put("investmentStatus", InvestmentStatus.RENTED.getContent());
		}else {
			detail.put("investmentStatus", null);
		}
    	
    	BigDecimal availableAreaMin = investmentAd.getAvailableAreaMin();
		BigDecimal availableAreaMax = investmentAd.getAvailableAreaMax();
		if (availableAreaMin != null && availableAreaMax != null) {
			//这种操作的原因是，如果是整数的话，BigDecimal转为字符串时会保留两位小数，如100转为字符串时会成为100.00
			//这种现象导致了issue-38616
			if (isInteger(availableAreaMin)) {
				availableAreaMin = availableAreaMin.setScale(0);
			}
			if (isInteger(availableAreaMax)) {
				availableAreaMax = availableAreaMax.setScale(0);
			}
    		detail.put("availableArea", availableAreaMin + "-" + availableAreaMax);
		}else if (availableAreaMin!=null) {
			if (isInteger(availableAreaMin)) {
				availableAreaMin = availableAreaMin.setScale(0);
			}
			detail.put("availableArea", availableAreaMin.toString());
		}else if(availableAreaMax!=null){
			if (isInteger(availableAreaMax)) {
				availableAreaMax = availableAreaMax.setScale(0);
			}
			detail.put("availableArea", availableAreaMax.toString());
		}else {
			detail.put("availableArea", null);
		}
    	
    	BigDecimal assetPriceMin = investmentAd.getAssetPriceMin();
		BigDecimal assetPriceMax = investmentAd.getAssetPriceMax();
		if (assetPriceMin!=null && assetPriceMax!=null) {
			if (isInteger(assetPriceMin)) {
				assetPriceMin = assetPriceMin.setScale(0);
			}
			if (isInteger(assetPriceMax)) {
				assetPriceMax = assetPriceMax.setScale(0);
			}
    		detail.put("assetPrice", assetPriceMin + "-" + assetPriceMax);
		}else if (assetPriceMin!=null) {
			if (isInteger(assetPriceMin)) {
				assetPriceMin = assetPriceMin.setScale(0);
			}
			detail.put("assetPrice", assetPriceMin.toString());
		}else if(assetPriceMax!=null){
			if (isInteger(assetPriceMax)) {
				assetPriceMax = assetPriceMax.setScale(0);
			}
			detail.put("assetPrice", assetPriceMax.toString());
		}else {
			detail.put("assetPrice", null);
		}	
		
    	if (investmentAd.getApartmentFloorMin()!=null && investmentAd.getApartmentFloorMax()!=null) {
    		detail.put("apartmentFloor", investmentAd.getApartmentFloorMin() + "-" + investmentAd.getApartmentFloorMax());
		}else if (investmentAd.getApartmentFloorMin()!=null) {
			detail.put("apartmentFloor", investmentAd.getApartmentFloorMin().toString());
		}else if(investmentAd.getApartmentFloorMax()!=null){
			detail.put("apartmentFloor", investmentAd.getApartmentFloorMax().toString());
		}else {
			detail.put("apartmentFloor", null);
		}
    	
    	detail.put("orientation", investmentAd.getOrientation());
    	detail.put("address", investmentAd.getAddress());
    	detail.put("contactPhone", investmentAd.getContactPhone());
    	detail.put("description", investmentAd.getDescription());
    	
    	List<InvestmentAdAsset> investmentAdAssets = investmentAdProvider.findAssetsByInvestmentAdId(investmentAd.getId());
		if (investmentAdAssets != null && investmentAdAssets.size() > 0) {
			StringBuilder relatedAssetsSB = new StringBuilder();
			for (InvestmentAdAsset investmentAdAsset : investmentAdAssets) {
				if (InvestmentAdAssetType.BUILDING.equals(InvestmentAdAssetType.fromCode(investmentAdAsset.getAssetType()))) {
					Building building = communityProvider.findBuildingById(investmentAdAsset.getAssetId());
					relatedAssetsSB.append(building.getName() + ",");
				}else if (InvestmentAdAssetType.APARTMENT.equals(InvestmentAdAssetType.fromCode(investmentAdAsset.getAssetType()))) {
					Address address = addressProvider.findAddressById(investmentAdAsset.getAssetId());
					relatedAssetsSB.append(address.getAddress() + ",");
				}
			}
			String relatedAssetsStr = relatedAssetsSB.toString();
			detail.put("relatedAssets", relatedAssetsStr.substring(0,relatedAssetsStr.length()-1));
		}
		return detail;
	}
	
	private boolean isInteger(BigDecimal num) {
		BigDecimal intNum = new BigDecimal(num.intValue());
		if (intNum.compareTo(num) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public void commit(Map<String, Object> params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterExecute(Map<String, Object> params) {
		// TODO Auto-generated method stub
		
	}

}
