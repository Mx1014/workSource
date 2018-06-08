package com.everhomes.bundleid_mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BundleidMapperServiceImpl implements BundleidMapperService{

	@Autowired
	private BundleidMapperProvider bundleidMapperProvider;
		
		
	@Override
	public void createBundleidMapper(BundleidMapper bo) {
		//查看是否已存在
		BundleidMapper resultBo = bundleidMapperProvider.findBundleidMapperByParams(bo.getNamespaceId(), bo.getIdentify());
		//存在则先删除再新增
		if(resultBo != null )
		{
			bundleidMapperProvider.deleteBundleidMapper(resultBo);
		}
		bundleidMapperProvider.createBundleidMapper(bo);
		
	}

}
