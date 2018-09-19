package com.everhomes.investmentAd;

import java.util.List;
import java.util.Map;

import com.everhomes.rest.investmentAd.InvestmentAdDTO;
import com.everhomes.rest.investmentAd.ListInvestmentAdCommand;

public interface InvestmentAdProvider {

	Long createInvestmentAd(InvestmentAd investmentAd);

	void createInvestmentAdAsset(InvestmentAdAsset adAsset);

	void createInvestmentAdBanner(InvestmentAdBanner adBanner);

	InvestmentAd findInvestmentAdById(Long id);

	void deleteInvestmentAdById(Long id);

	void updateInvestmentAd(InvestmentAd investmentAd);

	void deleteInvestmentAdAssetByInvestmentAdId(Long investmentAdId);

	void deleteInvestmentAdBannerByInvestmentAdId(Long investmentAdId);

	List<InvestmentAd> listInvestmentAds(ListInvestmentAdCommand cmd);

	List<InvestmentAdBanner> findBannersByInvestmentAdId(Long investmentAdId);

	List<InvestmentAdAsset> findAssetsByInvestmentAdId(Long investmentAdId);

	Map<Long, InvestmentAd> mapIdAndInvestmentAd(List<Long> investmentAdIds);

	void changeInvestmentStatus(Long id, Byte investmentStatus);

}
