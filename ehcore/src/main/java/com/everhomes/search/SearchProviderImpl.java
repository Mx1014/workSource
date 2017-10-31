// @formatter:off
package com.everhomes.search;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.search.SearchErrorCode;
import com.everhomes.util.RuntimeErrorException;

/**
 * 
 * <ul>搜索引擎相关的api
 * 部分方法暂未用到，所以不加到接口中，等有多个index时可开放
 * </ul>
 */
@Component
public class SearchProviderImpl implements SearchProvider {
	private static final int TIME_OUT = 30; // 单位(秒)
	private static final String CHARSET = "UTF-8";

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchProviderImpl.class);

	@Value("${elastic.nodes.hosts}")
	private String nodeHosts;

	@Value("${elastic.nodes.httpports}")
	private String nodePorts;

	@Value("${elastic.index}")
	private String esIndex;

	/**
	 * post请求（主要用来处理复杂查询）
	 * 
	 * @param url
	 * @param json
	 * @return 服务器返回的原始结果
	 */
	public String post(String url, String json) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("url:"+url+", json:"+json);
			}
			return HttpUtils.postJson(url, json, TIME_OUT, CHARSET);
		} catch (IOException e) {
			LOGGER.error("request to elasticsearch error: url=" + url + ", json=" + json);
			throw RuntimeErrorException.errorWith(SearchErrorCode.SCOPE,
					SearchErrorCode.ERROR_REQUEST_ELASTICSEARCH_ERROR, "request to elasticsearch error");
		}
	}

	/**
	 * get请求（主要用来处理主键查询）
	 * 
	 * @param url
	 * @return 服务器返回的原始结果
	 */
	public String get(String url) {
		try {
			return HttpUtils.get(url, null, TIME_OUT, CHARSET);
		} catch (Exception e) {
			LOGGER.error("request to elasticsearch error: url=" + url);
			throw RuntimeErrorException.errorWith(SearchErrorCode.SCOPE,
					SearchErrorCode.ERROR_REQUEST_ELASTICSEARCH_ERROR, "request to elasticsearch error");
		}
	}

	/**
	 * put请求（主要用来处理插入和更新）
	 * 
	 * @param url
	 * @param json
	 * @return 服务器返回的原始结果
	 */
	public String put(String url, String json) {
		try {
			return HttpUtils.putJson(url, json, TIME_OUT, CHARSET);
		} catch (IOException e) {
			LOGGER.error("request to elasticsearch error: url=" + url + ", json=" + json);
			throw RuntimeErrorException.errorWith(SearchErrorCode.SCOPE,
					SearchErrorCode.ERROR_REQUEST_ELASTICSEARCH_ERROR, "request to elasticsearch error");
		}
	}

	/**
	 * delete请求（主要用来处理删除）
	 * 
	 * @param url
	 * @return 服务器返回的原始结果
	 */
	public String delete(String url) {
		try {
			return HttpUtils.delete(url, TIME_OUT, CHARSET);
		} catch (IOException e) {
			LOGGER.error("request to elasticsearch error: url=" + url);
			throw RuntimeErrorException.errorWith(SearchErrorCode.SCOPE,
					SearchErrorCode.ERROR_REQUEST_ELASTICSEARCH_ERROR, "request to elasticsearch error");
		}
	}

	/**
	 * 插入或更新一条记录
	 * 
	 * @param index
	 * @param type
	 * @param id
	 * @param json
	 * @return _version的值
	 */
	public String insertOrUpdate(String index, String type, String id, String json) {
		String result = put("http://"+nodeHosts+":"+nodePorts + "/" + index + "/" + type + "/" + id, json);
		String _version = JSONObject.parseObject(result).getString("_version");
		return _version;
	}

	/**
	 * 插入或更新一条记录
	 * 
	 * @param type
	 * @param id
	 * @param json
	 * @return _version的值
	 */
	@Override
	public String insertOrUpdate(String type, String id, String json) {
		return insertOrUpdate(esIndex, type, id, json);
	}

	/**
	 * 根据_id查找记录
	 * 
	 * @param index
	 * @param type
	 * @param id
	 * @return json格式的记录
	 */
	public String getById(String index, String type, String id) {
		String result = get("http://"+nodeHosts+":"+nodePorts + "/" + index + "/" + type + "/" + id);
		String data = JSONObject.parseObject(result).getString("_source");
		return data;
	}

	/**
	 * 根据_id查找记录
	 * 
	 * @param type
	 * @param id
	 * @return json格式的记录
	 */
	@Override
	public String getById(String type, String id) {
		return getById(esIndex, type, id);
	}

	/**
	 * 根据ID删除一条记录
	 * 
	 * @param index
	 * @param type
	 * @param id
	 * @return _version的值
	 */
	public String deleteById(String index, String type, String id) {
		String result = delete("http://"+nodeHosts+":"+nodePorts + "/" + index + "/" + type + "/" + id);
		String _version = JSONObject.parseObject(result).getString("_version");
		return _version;
	}

	/**
	 * 根据ID删除一条记录
	 * 
	 * @param type
	 * @param id
	 * @return _version的值
	 */
	@Override
	public String deleteById(String type, String id) {
		return deleteById(esIndex, type, id);
	}
	
	/**
	 * delete请求（主要用来处理按条件删除）
	 * 
	 * @param url
	 * @param json
	 * @return 服务器返回的原始结果
	 */
	public String delete(String url, String json) {
		try {
			return HttpUtils.deleteJson(url, json, TIME_OUT, CHARSET);
		} catch (IOException e) {
			LOGGER.error("request to elasticsearch error: url=" + url + ", json=" + json);
			throw RuntimeErrorException.errorWith(SearchErrorCode.SCOPE,
					SearchErrorCode.ERROR_REQUEST_ELASTICSEARCH_ERROR, "request to elasticsearch error");
		}
	}
	
	/**
	 * 清除整个type的数据
	 * @param type
	 * @return 服务器返回的原始结果
	 */
	@Override
	public String clearType(String type){
		return clearType(type, "{\"query\":{\"match_all\":{}}}");
	}
	
	/**
	 * 按条件清除type的数据
	 * @param type
	 * @param json
	 * @return 服务器返回的原始结果
	 */
	public String clearType(String type, String json){
		return clearType(esIndex, type, json);
	}
	
	/**
	 * 
	 * 按条件清除指定index下的某type的数据
	 * @param index
	 * @param type
	 * @param json
	 * @return 服务器返回的原始结果
	 */
	public String clearType(String index, String type, String json){
		return delete("http://"+nodeHosts+":"+nodePorts + "/" + index + "/" + type + "/_query", json);
	}
	
	/**
	 * 批量操作
	 * 
	 * @param index
	 * @param type
	 * @param json
	 *            操作指令和数据
	 * @return 服务器返回的原始结果
	 */
	public String bulk(String index, String type, String json) {
		return post("http://"+nodeHosts+":"+nodePorts + "/" + index + "/" + type + "/_bulk", json);
	}

	/**
	 * 批量操作
	 * 
	 * @param type
	 * @param json
	 *            操作指令和数据
	 * @return 服务器返回的原始结果
	 */
	@Override
	public String bulk(String type, String json) {
		return bulk(esIndex, type, json);
	}

	/**
	 * 根据条件查询
	 * 
	 * @param index
	 * @param type
	 * @param json
	 * @param fields
	 *            需要返回的字段列表，逗号分割
	 * @return 服务器返回的原始结果
	 */
	public String queryRaw(String index, String type, String json, String fields) {
		if (!StringUtils.isEmpty(fields.trim())) {
			fields = fields.replaceAll(" ", "");
			return post("http://"+nodeHosts+":"+nodePorts + "/" + index + "/" + type + "/_search?_source=" + fields, json);
		}
		return post("http://"+nodeHosts+":"+nodePorts + "/" + index + "/" + type + "/_search?", json);
	}

	/**
	 * 根据条件查询
	 * 
	 * @param type
	 * @param json
	 * @param fields
	 *            需要返回的字段列表，逗号分割
	 * @return 服务器返回的原始结果
	 */
	public String queryRaw(String type, String json, String fields) {
		return queryRaw(esIndex, type, json, fields);
	}

	/**
	 * 解析查询结果
	 * 
	 * @param result
	 * @return 从查询结果中解析出hits.hits._source
	 */
	public JSONArray parseList(JSONObject result) {
		JSONArray list = new JSONArray();
		JSONObject topHits = result.getJSONObject("hits");
		if (topHits == null)
			return list;
		JSONArray hits = topHits.getJSONArray("hits");
		if (hits == null)
			return list;
		for (int i = 0; i < hits.size(); i++) {
			JSONObject o = hits.getJSONObject(i).getJSONObject("_source");
			o.put("highlight",hits.getJSONObject(i).getJSONObject("highlight"));
			list.add(o);
		}
		return list;
	}
	
	/**
	 * 按json字符串查询
	 * @param type
	 * @param json
	 * @param fields
	 * @return JSONArray 返回JSONArray格式数据，即_source中的数组
	 */
	@Override
	public JSONArray query(String type, String json, String fields){
		return parseList(JSONObject.parseObject(queryRaw(type, json, fields)));
	}
	/**
	 * 从查询结果中解析sort部分
	 * 
	 * @param result
	 * @return 从查询结果中解析出hits.hits.sort
	 */
	public JSONArray parseSort(JSONObject result) {
		JSONObject topHits = result.getJSONObject("hits");
		if (topHits == null)
			return null;
		JSONArray hits = topHits.getJSONArray("hits");
		if (hits == null)
			return null;
		JSONArray list = new JSONArray();
		for (int i = 0; i < hits.size(); i++) {
			list.add(hits.getJSONObject(i).getJSONArray("sort"));
		}
		return (list.size() == 0) ? null : list;
	}

	/**
	 * 从查询结果中解析出total
	 * 
	 * @param result
	 * @return total值
	 */
	public Long parseTotal(JSONObject result) {
		JSONObject topHits = result.getJSONObject("hits");
		if (topHits == null)
			return 0L;
		return topHits.getLong("total");
	}

	/**
	 * 从hits中取出数据并把距离返回
	 * 
	 * @param hits
	 * @param sortIndex
	 *            表示距离在排序中的位置，从0开始
	 * @return
	 */
	public List<Object> getDistanceFromHits(String hits, int sortIndex) {
		// 返回值
		List<Object> list = new ArrayList<Object>();
		// 把查询返回值转化为集合
		JSONArray hitsArray = JSONArray.parseArray(hits);
		int hitsSize = hitsArray == null ? 0 : hitsArray.size();
		for (int i = 0; i < hitsSize; i++) {
			// jsonObject中包含_source和sort两部分
			JSONObject jsonObject = hitsArray.getJSONObject(i);
			// 取出_source部分并把sort中的距离加入到source中
			JSONObject sourceObject = jsonObject.getJSONObject("_source");
			JSONArray sortObject = jsonObject.getJSONArray("sort");
			sourceObject.put("distanceNumber",
					((BigDecimal) sortObject.get(sortIndex)).multiply(new BigDecimal(1000)).doubleValue());

			list.add(sourceObject);
		}

		return list;
	}

	/**
	 * 按json字符串查询
	 * @param type
	 * @param json
	 * @param fields
	 * @return JSONArray 返回JSONArray格式数据，返回hits中的数组
	 */
	@Override
	public JSONArray queryTopHits(String type, String json, String fields) {
		JSONObject result = JSONObject.parseObject(queryRaw(type, json, fields));
		JSONArray list = new JSONArray();
		JSONObject topHits = result.getJSONObject("hits");
		if (topHits == null)
			return list;
		JSONArray hits = topHits.getJSONArray("hits");
		if (hits == null)
			return list;
		
		return hits;
	}
}
