package com.everhomes.buttscript;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface ButtScriptLastCommitProvider {

    /**
     * 通过主键查询信息
     * @param id
     * @return
     */
    ButtScriptLastCommit getButtScriptLastCommitById(Long id);

    /**
     * 新增
     * @param bo
     */
    void crteateButtScriptLastCommit(ButtScriptLastCommit bo);

    /**
     * 更新
     * @param bo
     */
    void updateButtScriptLastCommit(ButtScriptLastCommit bo);


    /**
     * 删除
     * @param bo
     */
    void deleteButtScriptLastCommit(ButtScriptLastCommit bo);

    /**
     * 根据域空间和分类来查询
     * @param namespaceId
     * @param infoType
     * @return
     */
    ButtScriptLastCommit getButtScriptLastCommit(Integer namespaceId , String infoType);


    List<ButtScriptLastCommit> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);
}
