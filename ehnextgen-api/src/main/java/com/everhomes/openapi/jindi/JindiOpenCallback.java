package com.everhomes.openapi.jindi;

import java.util.List;

import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;

public interface JindiOpenCallback<T> {

	List<T> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, int pageSize);

	List<T> fetchDataByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);

	 Object complementInfo(JindiFetchDataCommand cmd, T src);

}
