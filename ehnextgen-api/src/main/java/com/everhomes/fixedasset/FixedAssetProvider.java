package com.everhomes.fixedasset;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.fixedasset.FixedAssetStatisticsDTO;
import org.jooq.Condition;

import java.util.List;

public interface FixedAssetProvider {

    List<FixedAssetDefaultCategory> listAllFixedAssetDefaultCategories();

    Integer createFixedAssetCategory(FixedAssetCategory fixedAssetCategory);

    Integer updateFixedAssetCategory(FixedAssetCategory fixedAssetCategory);

    FixedAssetCategory getFixedAssetCategoryById(Integer id);

    List<FixedAssetCategory> getAllSubFixedAssetCategories(String parentPath);

    Integer getMaxDefaultOrderCurrentLevel(Integer namespaceId, String ownerType, Long ownerId, Integer parentId);

    boolean isCategoryNameExistInSameLevel(CheckFixedAssetCategoryNameExistRequest request);

    Integer countFixedAssetCategoriesIgnoreStatus(Integer namespaceId, String ownerType, Long ownerId);

    List<FixedAssetCategory> findFixedAssetCategories(Integer namespaceId, String ownerType, Long ownerId);

    Long createFixedAsset(FixedAsset fixedAsset);

    Long updateFixedAsset(FixedAsset fixedAsset);

    boolean isFixedAssetItemNoExist(CheckFixedAssetItemNoExistRequest request);

    FixedAsset getFixedAssetDetail(Long fixedAssetId, Integer namespaceId, String ownerType, Long ownerId);

    FixedAsset getFixedAssetDetailByItemNum(String itemNum, Integer namespaceId, String ownerType, Long ownerId);

    List<FixedAsset> findFixedAssets(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

    FixedAssetStatisticsDTO getFixedAssetsStatistic(Condition condition);

    Long createFixedAssetOperationLog(FixedAssetOperationLog fixedAssetOperationLog);

    List<FixedAssetOperationLog> getFixedAssetOperationLogs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

}
