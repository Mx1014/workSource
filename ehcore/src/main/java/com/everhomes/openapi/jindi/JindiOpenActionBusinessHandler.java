package com.everhomes.openapi.jindi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.rest.openapi.jindi.JindiActionBusinessDTO;
import com.everhomes.rest.openapi.jindi.JindiActionType;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.statistics.transaction.StatTransaction;
import com.everhomes.statistics.transaction.StatTransactionProvider;
import com.everhomes.user.User;

/**
 * 
 * <ul>
 * 抓取电商数据
 * </ul>
 */
@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.ACTION_CODE+JindiActionType.JindiActionTypeCode.ACTION_TYPE_BUSINESS_CODE)
public class JindiOpenActionBusinessHandler implements JindiOpenHandler {

	@Autowired
	private StatTransactionProvider statTransactionProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<StatTransaction>() {

			@Override
			public List<StatTransaction> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
					Long pageAnchor, int pageSize) {
				return statTransactionProvider.listBusinessByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getBeginTime(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<StatTransaction> fetchDataByUpdateTime(Integer namespaceId, Long timestamp,
					int pageSize) {
				return statTransactionProvider.listBusinessByUpdateTime(cmd.getNamespaceId(), cmd.getBeginTime(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, StatTransaction src) {
				User user = getUser(src.getPayerUid());
				Long communityId = src.getCommunityId();
				if (communityId != null && communityId.longValue() == 0L) {
					communityId = null;
				}
				JindiActionBusinessDTO data = new JindiActionBusinessDTO();
				data.setId(src.getId());
				data.setUserId(src.getPayerUid());
				data.setUserName(user.getNickName());
				data.setPhone(user.getIdentifierToken());
				data.setCommunityId(src.getCommunityId());
				data.setTransactionNo(src.getTransactionNo());
				data.setPaidTime(src.getPaidTime());
				data.setPaidChannel(src.getPaidChannel());
				data.setPaidAmount(src.getPaidAmount());
				data.setCreateTime(src.getCreateTime());
				data.setUpdateTime(src.getUpdateTime()==null?src.getCreateTime():src.getUpdateTime());
				
				if (communityId != null) {
					Community community = communityProvider.findCommunityById(src.getCommunityId());
					data.setCommunityName(community.getName());
				}
				
				return data;
			}
		});
	}
	
}
