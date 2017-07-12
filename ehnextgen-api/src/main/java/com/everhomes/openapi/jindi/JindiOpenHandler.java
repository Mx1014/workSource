package com.everhomes.openapi.jindi;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.openapi.jindi.JindiDataResponse;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.rest.openapi.jindi.JindiOpenConstant;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;

public interface JindiOpenHandler {
	String JINDI_OPEN_HANDLER = "jindi_open_handler_";
	String fetchData(JindiFetchDataCommand cmd);
	
	default Integer getPageSize(Integer pageSize){
		if (pageSize == null) {
			return JindiOpenConstant.PAGE_SIZE;
		}
		return pageSize;
	}
	
	default <T> String superFetchData(JindiFetchDataCommand cmd, JindiOpenCallback<T> callback) {
		try {
			Integer pageSize = getPageSize(cmd.getPageSize());
			//pageAnchor和nextPageAnchor只有在同一个时间戳有未取完的数据时才有用
			Long nextPageAnchor = null;
			Byte hasMore = null;
			List<T> resultList = new ArrayList<>();
			//如果有锚点，则说明上次请求数据未取完，本次继续取当前时间戳的数据
			if (cmd.getPageAnchor() != null) {
				List<T> thisList = callback.fetchDataByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getBeginTime(), cmd.getPageAnchor(), pageSize);
				resultList.addAll(thisList);
				//如果当前时间戳还未取完，则直接返回并添加下次锚点
				if (resultList.size() > pageSize) {
					resultList.remove(resultList.size()-1);
					T last = resultList.get(resultList.size()-1);
					nextPageAnchor = (Long) last.getClass().getMethod("getId").invoke(last);
					
					hasMore = (byte)1;
				}else {
					//如果正好取完，则没有锚点了
					//如果当前时间戳取完了，还不够，那就需要从当前时间戳之后再取一些补充到pageSize大小
					//此处无代码，方便说明逻辑
				}
			}
			
			//如果没有锚点，或者上述过程未取满或正好取满，则到此处，如果是正好取满，则这里会再取一条，看看有没有多余的数据了
			if (hasMore == null  && resultList.size() <= pageSize) {
				pageSize = pageSize - resultList.size();
				List<T> thisList = callback.fetchDataByUpdateTime(cmd.getNamespaceId(), cmd.getBeginTime(), pageSize);
				if (thisList.size() > pageSize) {
					//如果此处数据大于pageSize说明，还有数据
					if (thisList.size()>=2) {
						T last = thisList.get(thisList.size()-1);
						Method getUpdateTimeMethod = last.getClass().getMethod("getUpdateTime");
						if ( ((Timestamp)getUpdateTimeMethod.invoke(last)).getTime() == ((Timestamp)getUpdateTimeMethod.invoke(thisList.get(thisList.size()-2))).getTime()) {
							//如果最后两条数据的更新时间一样，说明这个时间戳还没有取完数据，下次还要从这个时间戳开始取，需要设置一下锚点
							nextPageAnchor = (Long) last.getClass().getMethod("getId").invoke(thisList.get(thisList.size()-2));
						}
					}
					thisList.remove(thisList.size()-1);
					hasMore = (byte)1;
				}else {
					//到这里，说明数据已经取完了
					//此处无代码，方便说明逻辑
				}
				resultList.addAll(thisList);
			}
			
			List<Object> dataList = resultList.stream().map(r->callback.complementInfo(cmd, r)).collect(Collectors.toList());

			JindiDataResponse response = new JindiDataResponse();
			response.setHasMore(hasMore);
			response.setNextPageAnchor(nextPageAnchor);
			response.setDataList(dataList);
			
			return JSON.toJSONString(response);
		} catch (Exception e) {
			throw new RuntimeErrorException("super fetch data error", e);
		}
		
	}

	default User getUser(Long userId) {
		if (userId == null) {
			return new User();
		}
		UserProvider userProvider = PlatformContext.getComponent(UserProvider.class);
		User user = userProvider.findUserById(userId);
		if (user == null) {
			return new User();
		}
		List<UserIdentifier> userIdentifierList = userProvider.listUserIdentifiersOfUser(userId);
		if (userIdentifierList != null && userIdentifierList.size() > 0) {
			user.setIdentifierToken(userIdentifierList.get(0).getIdentifierToken());
		}
		return user;
	}
	
	
}
