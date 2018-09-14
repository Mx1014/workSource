package com.everhomes.investmentAd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.investmentAd.InvestmentAdService;
import com.everhomes.rest.investmentAd.InvestmentAdDetailDTO;
import com.everhomes.rest.investmentAd.InvestmentAdGeneralStatus;
import com.everhomes.rest.investmentAd.ChangeInvestmentAdOrderCommand;
import com.everhomes.rest.investmentAd.CreateInvestmentAdCommand;
import com.everhomes.rest.investmentAd.DeleteInvestmentAdCommand;
import com.everhomes.rest.investmentAd.GetInvestmentAdCommand;
import com.everhomes.rest.investmentAd.InvestmentAdAssetType;
import com.everhomes.rest.investmentAd.ListInvestmentAdCommand;
import com.everhomes.rest.investmentAd.ListInvestmentAdResponse;
import com.everhomes.rest.investmentAd.RelatedAssetDTO;
import com.everhomes.rest.investmentAd.UpdateInvestmentAdCommand;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

@Component
public class InvestmentAdServiceImpl implements InvestmentAdService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentAdServiceImpl.class);
	
	@Autowired
	private InvestmentAdProvider investmentAdProvider;
	
	@Override
	public void createInvestmentAd(CreateInvestmentAdCommand cmd) {
		//TODO 加权限
		
		//创建招商广告
		InvestmentAd investmentAd = ConvertHelper.convert(cmd,InvestmentAd.class);
		Long investmentAdId = investmentAdProvider.createInvestmentAd(investmentAd);
		//记录招商广告关联的资产
		List<RelatedAssetDTO> relatedAssets = cmd.getRelatedAssets();
		if (relatedAssets != null && relatedAssets.size() > 0) {
			for (RelatedAssetDTO assetDTO : relatedAssets) {
				InvestmentAdAsset adAsset = new InvestmentAdAsset();
				adAsset.setNamespaceId(cmd.getNamespaceId());
				adAsset.setAdvertisementId(investmentAdId);
				adAsset.setStatus(InvestmentAdGeneralStatus.ACTIVE.getCode());
				adAsset.setCreatorUid(UserContext.currentUserId());
				if (assetDTO.getApartmentId() != null) {
					adAsset.setAssetType(InvestmentAdAssetType.APARTMENT.getCode());
					adAsset.setAssetId(assetDTO.getApartmentId());
				}else{
					adAsset.setAssetType(InvestmentAdAssetType.BUILDING.getCode());
					adAsset.setAssetId(assetDTO.getBuildingId());
				}
				investmentAdProvider.createInvestmentAdAsset(adAsset);
			}
		}
		//记录招商广告关联的轮播图
		
	}

	@Override
	public void deleteInvestmentAd(DeleteInvestmentAdCommand cmd) {
		//TODO 加权限
		
	}
	
	@Override
	public void updateInvestmentAd(UpdateInvestmentAdCommand cmd) {
		//TODO 加权限
		
	}

	@Override
	public ListInvestmentAdResponse listInvestmentAds(ListInvestmentAdCommand cmd) {
		//TODO 加权限
		return null;
	}

	@Override
	public void changeInvestmentAdOrder(ChangeInvestmentAdOrderCommand cmd) {
		//TODO 加权限
		
	}

	@Override
	public InvestmentAdDetailDTO getInvestmentAd(GetInvestmentAdCommand cmd) {
		//TODO 加权限
		return null;
	}

	
}
