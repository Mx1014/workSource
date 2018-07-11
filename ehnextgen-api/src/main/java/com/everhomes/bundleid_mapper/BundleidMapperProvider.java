package com.everhomes.bundleid_mapper;

/**
 * 
 * @author huanglm 20180607
 *
 */
public interface BundleidMapperProvider {
	
	/**
	 * 新建BundleidMapper
	 * @param bo
	 */
	void createBundleidMapper(BundleidMapper bo );
	
	/**
	 * 删除BundleidMapper
	 * @param bo
	 */
	void deleteBundleidMapper(BundleidMapper bo);
	
	/**
	 * 通过namespaceId 与 identify 查找 BundleidMapper 
	 * @param namespaceId
	 * @param identify
	 * @return
	 */	
	BundleidMapper findBundleidMapperByParams(Integer namespaceId,String identify);
}
