// @formatter:off
package com.everhomes.aclink;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.*;
import com.everhomes.server.schema.Tables;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class AclinkIpadServiceImpl implements AclinkIpadService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AclinkIpadServiceImpl.class);
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private AclinkIpadProvider aclinkIpadProvider;
	
	@Autowired
	private DoorAccessProvider doorAccessProvider;
	
	@Autowired
	private AclinkServerProvider aclinkServerProvider;

	@Override
	public ListLocalIpadResponse listLocalIpads(CrossShardListingLocator locator, ListLocalIpadCommand cmd) {
		ListLocalIpadResponse resp = new ListLocalIpadResponse();
		List<AclinkIpad> ipads = aclinkIpadProvider.listLocalIpads(locator, cmd);
		List<AclinkIPadDTO> dtos = new ArrayList<AclinkIPadDTO>();
		for(AclinkIpad ipad : ipads){
			AclinkIPadDTO dto = ConvertHelper.convert(ipad, AclinkIPadDTO.class);
			dto.setDoorAccess(ConvertHelper.convert(doorAccessProvider.getDoorAccessById(ipad.getDoorAccessId()),DoorAccessDTO.class));
			dto.setServer(ConvertHelper.convert(aclinkServerProvider.findServerById(ipad.getServerId()),AclinkServerDTO.class));
			dtos.add(dto);
		}
		resp.setAclinkIpads(dtos);
		resp.setNextPageAnchor(locator.getAnchor());
		return resp;
	}

	@Override
	public CreateLocalIpadResponse createLocalIpad(CreateLocalIpadCommand cmd) {
		CreateLocalIpadResponse rsp = new CreateLocalIpadResponse();
		AclinkIpad ipad = new AclinkIpad();
		User user = UserContext.current().getUser();
		ipad.setDoorAccessId(cmd.getDoorAccessId());
		ipad.setEnterStatus(cmd.getEnterStatus());
		ipad.setName(cmd.getName());
		ipad.setServerId(cmd.getServerId());
		ipad.setUuid(cmd.getUuid());
		ipad.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		ipad.setCreatorUid(user.getId());
		ipad.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		ipad.setOperatorUid(user.getId());
		ipad.setLinkStatus((byte) 0);//TODO delete
		try{
			aclinkIpadProvider.createLocalIpad(ipad);
		}catch(Exception e){
			String errorInfo = e.toString();
			LOGGER.info(errorInfo);
			if(errorInfo.contains("u_eh_aclink_ipads_uuid") && errorInfo.contains("Duplicate")){
				throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_UUID_EXISTS, "匹配码已失效");
			}else{
				throw e;
			}
		}
		//内网服务器下属设备有变动,更新服务器的上次操作时间
		AclinkServer server = aclinkServerProvider.findServerById(ipad.getServerId());
		if(server != null){
			server.setOperateTime(ipad.getOperateTime());
			server.setOperatorUid(ipad.getOperatorUid());
			aclinkServerProvider.updateLocalServer(server);
		}
		rsp.setUuid(cmd.getUuid());
		return rsp;
	}

	@Override
	public void updateLocalIpad(UpdateLocalIpadCommand cmd) {
		AclinkIpad ipad = aclinkIpadProvider.findIpadById(cmd.getId());
		User user = UserContext.current().getUser();
		ipad.setName(cmd.getName());
		ipad.setDoorAccessId(cmd.getDoorAccessId());
		ipad.setEnterStatus(cmd.getEnterStatus());
		ipad.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		ipad.setOperatorUid(user.getId());
		aclinkIpadProvider.updateLocalIpad(ipad);
		//内网服务器下属设备有变动,更新服务器的上次操作时间
		AclinkServer server = aclinkServerProvider.findServerById(ipad.getServerId());
		if(server != null){
			server.setOperateTime(ipad.getOperateTime());
			server.setOperatorUid(ipad.getOperatorUid());
			aclinkServerProvider.updateLocalServer(server);
		}
	}

	@Override
	public void updateIpadLogo(UpdateIpadLogoCommand cmd){
		List<AclinkIpad> ipads = new ArrayList<AclinkIpad>();
		User user = UserContext.current().getUser();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		ipads = aclinkIpadProvider.queryLocalIpads(locator, 9999,new ListingQueryBuilderCallback(){

			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
																SelectQuery<? extends Record> query) {

				if(cmd.getOwnerId() != null){
					query.addConditions(Tables.EH_ACLINK_IPADS.OWNER_ID.eq(cmd.getOwnerId()));
				}

				if(cmd.getOwnerType() != null){
					query.addConditions(Tables.EH_ACLINK_IPADS.OWNER_TYPE.eq(cmd.getOwnerType()));
				}
				return query;
			}

		});
		if(null != ipads && !ipads.isEmpty()){
			for(AclinkIpad ipad: ipads){
				if(null != ipad.getId()){
					ipad.setLogoUrl(cmd.getLogoUrl());
					ipad.setLogoUri(cmd.getLogoUri());
					ipad.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					ipad.setOperatorUid(user.getId());
					aclinkIpadProvider.updateLocalIpad(ipad);
					//内网服务器下属设备有变动,更新服务器的上次操作时间
					AclinkServer server = aclinkServerProvider.findServerById(ipad.getServerId());
					if(server != null){
						server.setOperateTime(ipad.getOperateTime());
						server.setOperatorUid(ipad.getOperatorUid());
						aclinkServerProvider.updateLocalServer(server);
					}
				}
			}
		}
	}

	@Override
	public void deleteLocalIpad(Long id) {
		AclinkIpad ipad = aclinkIpadProvider.findIpadById(id);
		User user = UserContext.current().getUser();
		ipad.setStatus((byte) 2);
		ipad.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		ipad.setOperatorUid(user.getId());
		aclinkIpadProvider.updateLocalIpad(ipad);
		
		//内网服务器下属设备有变动,更新服务器的上次操作时间
		AclinkServer server = aclinkServerProvider.findServerById(ipad.getServerId());
		if(server != null){
			server.setOperateTime(ipad.getOperateTime());
			server.setOperatorUid(ipad.getOperatorUid());
			aclinkServerProvider.updateLocalServer(server);
		}
	}
}
