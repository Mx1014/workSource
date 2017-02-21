package com.everhomes.asset;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface AssetProvider {

    void creatAssetBill(AssetBill bill);
    AssetBill findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType);

}
