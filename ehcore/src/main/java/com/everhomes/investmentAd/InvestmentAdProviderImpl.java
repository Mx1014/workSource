package com.everhomes.investmentAd;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.investmentAd.InvestmentAdProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.investmentAd.InvestmentAdDTO;
import com.everhomes.rest.investmentAd.InvestmentAdGeneralStatus;
import com.everhomes.rest.investmentAd.InvestmentAdSortType;
import com.everhomes.rest.investmentAd.ListInvestmentAdCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhInvestmentAdvertisementAssetsDao;
import com.everhomes.server.schema.tables.daos.EhInvestmentAdvertisementBannersDao;
import com.everhomes.server.schema.tables.daos.EhInvestmentAdvertisementsDao;
import com.everhomes.server.schema.tables.pojos.EhInvestmentAdvertisementAssets;
import com.everhomes.server.schema.tables.pojos.EhInvestmentAdvertisementBanners;
import com.everhomes.server.schema.tables.pojos.EhInvestmentAdvertisements;
import com.everhomes.server.schema.tables.records.EhInvestmentAdvertisementAssetsRecord;
import com.everhomes.server.schema.tables.records.EhInvestmentAdvertisementBannersRecord;
import com.everhomes.server.schema.tables.records.EhInvestmentAdvertisementsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

import ch.qos.logback.core.joran.conditional.ElseAction;

@Component
public class InvestmentAdProviderImpl implements InvestmentAdProvider{

	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentAdProviderImpl.class);
	
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public Long createInvestmentAd(InvestmentAd investmentAd) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisements.class));
		EhInvestmentAdvertisementsDao dao = new EhInvestmentAdvertisementsDao(context.configuration());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhInvestmentAdvertisements.class));
		investmentAd.setId(id);
		investmentAd.setDefaultOrder(id);
		investmentAd.setCreateTime(getCurrentTimestamp());
		investmentAd.setCreatorUid(UserContext.currentUserId());
		dao.insert(investmentAd);
        return id;
	}

	@Override
	public void createInvestmentAdAsset(InvestmentAdAsset adAsset) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisementAssets.class));
		EhInvestmentAdvertisementAssetsDao dao = new EhInvestmentAdvertisementAssetsDao(context.configuration());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhInvestmentAdvertisementAssets.class));
		adAsset.setId(id);
		adAsset.setCreateTime(getCurrentTimestamp());
		adAsset.setCreatorUid(UserContext.currentUserId());
		dao.insert(adAsset);
	}

	@Override
	public void createInvestmentAdBanner(InvestmentAdBanner adBanner) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisementBanners.class));
		EhInvestmentAdvertisementBannersDao dao = new EhInvestmentAdvertisementBannersDao(context.configuration());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhInvestmentAdvertisementBanners.class));
		adBanner.setId(id);
		adBanner.setCreateTime(getCurrentTimestamp());
		adBanner.setCreatorUid(UserContext.currentUserId());
		dao.insert(adBanner);
	}

	@Override
	public void deleteInvestmentAdById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisements.class));
		UpdateQuery<EhInvestmentAdvertisementsRecord> updateQuery = context.updateQuery(Tables.EH_INVESTMENT_ADVERTISEMENTS);
		updateQuery.addValue(Tables.EH_INVESTMENT_ADVERTISEMENTS.STATUS, InvestmentAdGeneralStatus.INACTIVE.getCode());
		updateQuery.addValue(Tables.EH_INVESTMENT_ADVERTISEMENTS.OPERATE_TIME, getCurrentTimestamp());
		updateQuery.addValue(Tables.EH_INVESTMENT_ADVERTISEMENTS.OPERATOR_UID, UserContext.currentUserId());
		updateQuery.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.ID.eq(id));
		updateQuery.execute();
	}

	@Override
	public InvestmentAd findInvestmentAdById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisements.class));
		EhInvestmentAdvertisementsDao dao = new EhInvestmentAdvertisementsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), InvestmentAd.class);
	}
	
	private Timestamp getCurrentTimestamp(){
		return new Timestamp(System.currentTimeMillis());
	}

	@Override
	public void updateInvestmentAd(InvestmentAd investmentAd) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisements.class));
		EhInvestmentAdvertisementsDao dao = new EhInvestmentAdvertisementsDao(context.configuration());
		investmentAd.setOperateTime(getCurrentTimestamp());
		investmentAd.setOperatorUid(UserContext.currentUserId());
		dao.update(investmentAd);
	}

	@Override
	public void deleteInvestmentAdAssetByInvestmentAdId(Long investmentAdId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisementAssets.class));
		UpdateQuery<EhInvestmentAdvertisementAssetsRecord> updateQuery = context.updateQuery(Tables.EH_INVESTMENT_ADVERTISEMENT_ASSETS);
		updateQuery.addValue(Tables.EH_INVESTMENT_ADVERTISEMENT_ASSETS.STATUS, InvestmentAdGeneralStatus.INACTIVE.getCode());
		updateQuery.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENT_ASSETS.ADVERTISEMENT_ID.eq(investmentAdId));
		updateQuery.execute();
	}

	@Override
	public void deleteInvestmentAdBannerByInvestmentAdId(Long investmentAdId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisementBanners.class));
		UpdateQuery<EhInvestmentAdvertisementBannersRecord> updateQuery = context.updateQuery(Tables.EH_INVESTMENT_ADVERTISEMENT_BANNERS);
		updateQuery.addValue(Tables.EH_INVESTMENT_ADVERTISEMENT_BANNERS.STATUS, InvestmentAdGeneralStatus.INACTIVE.getCode());
		updateQuery.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENT_BANNERS.ADVERTISEMENT_ID.eq(investmentAdId));
		updateQuery.execute();
	}

	@Override
	public List<InvestmentAdDTO> listInvestmentAds(ListInvestmentAdCommand cmd) {
		List<InvestmentAdDTO> result = new ArrayList<>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisements.class));
		SelectQuery<EhInvestmentAdvertisementsRecord> query = context.selectQuery(Tables.EH_INVESTMENT_ADVERTISEMENTS);
		
		query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
		query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.STATUS.eq(InvestmentAdGeneralStatus.ACTIVE.getCode()));
		
		if (cmd.getCommunityId()!=null) {
			query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.COMMUNITY_ID.eq(cmd.getCommunityId()));
		}
		if (cmd.getInvestmentStatus()!=null) {
			query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.INVESTMENT_STATUS.eq(cmd.getInvestmentStatus()));
		}
		if (cmd.getInvestmentType()!=null) {
			query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.INVESTMENT_TYPE.eq(cmd.getInvestmentType()));
		}
		if (cmd.getAvailableAreaMin()!=null) {
			query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.AVAILABLE_AREA_MIN.ge(cmd.getAvailableAreaMin()));
		}
		if (cmd.getAvailableAreaMax()!=null) {
			query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.AVAILABLE_AREA_MAX.le(cmd.getAvailableAreaMax()));
		}
		if (cmd.getAssetPriceMin()!=null) {
			query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.ASSET_PRICE_MIN.ge(cmd.getAssetPriceMin()));
		}
		if (cmd.getAssetPriceMax()!=null) {
			query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.ASSET_PRICE_MAX.le(cmd.getAssetPriceMax()));
		}
		if (cmd.getApartmentFloorMin()!=null) {
			query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.APARTMENT_FLOOR_MIN.ge(cmd.getApartmentFloorMin()));
		}
		if (cmd.getApartmentFloorMax()!=null) {
			query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.APARTMENT_FLOOR_MAX.le(cmd.getApartmentFloorMax()));
		}
		if (StringUtils.isNotBlank(cmd.getOrientation())) {
			query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.ORIENTATION.eq(cmd.getOrientation()));
		}
		if (StringUtils.isNotBlank(cmd.getKeywords())) {
			query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENTS.TITLE.like(DSL.concat("%",cmd.getKeywords(),"%")));
		}
		//排序
		if (cmd.getSortField().equals("defaultOrder")) {
			query.addOrderBy(Tables.EH_INVESTMENT_ADVERTISEMENTS.DEFAULT_ORDER);
		}else if(cmd.getSortField().equals("availableAreaMin")){
			query.addOrderBy(Tables.EH_INVESTMENT_ADVERTISEMENTS.AVAILABLE_AREA_MIN);
		}else if (cmd.getSortField().equals("assetPriceMin")) {
			query.addOrderBy(Tables.EH_INVESTMENT_ADVERTISEMENTS.ASSET_PRICE_MIN);
		}
		
		if (cmd.getSortType() == InvestmentAdSortType.ASC.getCode()) {
			query.addOrderBy(DSL.val(1).asc());
		}else if (cmd.getSortType() == InvestmentAdSortType.DESC.getCode()) {
			query.addOrderBy(DSL.val(1).desc());
		}
		
		query.addLimit(cmd.getPageAnchor().intValue(),cmd.getPageSize() + 1);
		
		query.fetch().forEach(r->{
			InvestmentAdDTO dto = new InvestmentAdDTO();
			dto.setId(r.getId());
			dto.setTitle(r.getTitle());
			dto.setInvestmentStatus(r.getInvestmentStatus());
			dto.setAvailableAreaMin(r.getAvailableAreaMin());
			dto.setAvailableAreaMax(r.getAvailableAreaMax());
			dto.setPriceUnit(r.getPriceUnit());
			dto.setAssetPriceMin(r.getAssetPriceMin());
			dto.setAssetPriceMax(r.getAssetPriceMax());
			dto.setApartmentFloorMin(r.getApartmentFloorMin());
			dto.setApartmentFloorMax(r.getApartmentFloorMax());
			dto.setOrientation(r.getOrientation());
			dto.setCreateTime(r.getCreateTime());
			dto.setDefaultOrder(r.getDefaultOrder());
			result.add(dto);
		});
		return result;
	}

	@Override
	public List<InvestmentAdBanner> findBannersByInvestmentAdId(Long investmentAdId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisementBanners.class));
		SelectQuery<EhInvestmentAdvertisementBannersRecord> query = context.selectQuery(Tables.EH_INVESTMENT_ADVERTISEMENT_BANNERS);
		query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENT_BANNERS.ADVERTISEMENT_ID.eq(investmentAdId));
		query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENT_BANNERS.STATUS.eq(InvestmentAdGeneralStatus.ACTIVE.getCode()));
		return  query.fetchInto(InvestmentAdBanner.class);
	}

	@Override
	public List<InvestmentAdAsset> findAssetsByInvestmentAdId(Long investmentAdId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisementAssets.class));
		SelectQuery<EhInvestmentAdvertisementAssetsRecord> query = context.selectQuery(Tables.EH_INVESTMENT_ADVERTISEMENT_ASSETS);
		query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENT_ASSETS.ADVERTISEMENT_ID.eq(investmentAdId));
		query.addConditions(Tables.EH_INVESTMENT_ADVERTISEMENT_ASSETS.STATUS.eq(InvestmentAdGeneralStatus.ACTIVE.getCode()));
		return query.fetchInto(InvestmentAdAsset.class);
	}

	@Override
	public Map<Long, InvestmentAd> mapIdAndInvestmentAd(List<Long> investmentAdIds) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhInvestmentAdvertisements.class));
		List<InvestmentAd> investmentAdList = context.select()
											  .from(Tables.EH_INVESTMENT_ADVERTISEMENTS)
											  .where(Tables.EH_INVESTMENT_ADVERTISEMENTS.ID.in(investmentAdIds))
											  .fetchInto(InvestmentAd.class);
		Map<Long, InvestmentAd> result = new HashMap<>();
		investmentAdList.stream().forEach(a->{
			result.put(a.getId(), a);
		});
		return result;
	}
	
	
	
}
