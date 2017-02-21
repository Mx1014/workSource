package com.everhomes.asset;

import com.everhomes.rest.asset.AssetBillTemplateFieldDTO;

import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface AssetProvider {

    void creatAssetBill(AssetBill bill);
    AssetBill findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType);

    List<AssetBillTemplateFieldDTO> findLastVersionTemplateField(Long ownerId, String ownerType, Long targetId, String targetType);
    List<AssetBillTemplateFieldDTO> findTemplateFieldByTemplateVersion(Long ownerId, String ownerType, Long targetId, String targetType, Long templateVersion);


}
