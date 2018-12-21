// @formatter:off
package com.everhomes.aclink;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.border.Border;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.border.BorderProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.aclink.AclinkCameraDTO;
import com.everhomes.rest.aclink.AclinkConnectingCommand;
import com.everhomes.rest.aclink.AclinkDisconnectedCommand;
import com.everhomes.rest.aclink.AclinkEnterStatus;
import com.everhomes.rest.aclink.AclinkIPadDTO;
import com.everhomes.rest.aclink.AclinkListLocalServersCommand;
import com.everhomes.rest.aclink.AclinkServerDTO;
import com.everhomes.rest.aclink.AclinkServerRelDTO;
import com.everhomes.rest.aclink.AclinkServiceErrorCode;
import com.everhomes.rest.aclink.AesServerKeyDTO;
import com.everhomes.rest.aclink.CreateLocalServersCommand;
import com.everhomes.rest.aclink.CreateMarchUUIDCommand;
import com.everhomes.rest.aclink.CreateMarchUUIDResponse;
import com.everhomes.rest.aclink.DeleteLocalServerCommand;
import com.everhomes.rest.aclink.DoorAccessDTO;
import com.everhomes.rest.aclink.DoorAccessLinkStatus;
import com.everhomes.rest.aclink.DoorAccessOwnerType;
import com.everhomes.rest.aclink.DoorAccessStatus;
import com.everhomes.rest.aclink.GetLocalServerAddressResponse;
import com.everhomes.rest.aclink.ListAclinkServersResponse;
import com.everhomes.rest.aclink.ListLocalCamerasCommand;
import com.everhomes.rest.aclink.ListLocalIpadCommand;
import com.everhomes.rest.aclink.PairLocalServerResponse;
import com.everhomes.rest.aclink.QueryServerRelationsCommand;
import com.everhomes.rest.aclink.QueryServerRelationsResponse;
import com.everhomes.rest.aclink.SyncLocalServerResponse;
import com.everhomes.rest.aclink.UpdateCameraIpadBatchCommand;
import com.everhomes.rest.aclink.ListLocalServerByOrgCommand;
import com.everhomes.rest.aclink.ListLocalServerByOrgResponse;
import com.everhomes.rest.aclink.LocalDoorAccessDTO;
import com.everhomes.rest.aclink.UpdateLocalServersCommand;
import com.everhomes.rest.rpc.server.AclinkRemotePdu;
import com.everhomes.sequence.LocalSequenceGenerator;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class AclinkServerServiceImpl implements AclinkServerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AclinkServerServiceImpl.class);

	@Autowired
	private AclinkServerProvider aclinkServerProvider;

	@Autowired
	private AclinkIpadProvider aclinkIpadProvider;
	
	@Autowired
	private AclinkCameraProvider aclinkCameraProvider;

	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private DoorAccessProvider doorAccessProvider;
	
	@Autowired
	private AesServerKeyProvider aesServerKeyProvider;
	
	@Autowired
    private BorderConnectionProvider borderConnectionProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private BorderProvider borderProvider;

	@Override
	public String generateUUID(CreateMarchUUIDCommand cmd) {
		char[] charArr = new char[6];
		int i = 0;
		while (i < 6) {
			int f = (int) (Math.random() * 3);
			if (f == 0)
				charArr[i] = (char) ('A' + Math.random() * 26);
			else if (f == 1)
				charArr[i] = (char) ('a' + Math.random() * 26);
			else
				charArr[i] = (char) ('0' + Math.random() * 10);
			i++;
		}
		String resStr = new String(charArr);
		if (cmd.getUuidType() == (byte) 0) {
			if (aclinkServerProvider.listLocalServers(new CrossShardListingLocator(), null, null, resStr, 0).size() > 0) {
				LOGGER.error("uuid is already existed, trying agian ... ");
				return this.generateUUID(cmd);
			}
		} else if (cmd.getUuidType() == (byte) 1) {
			ListLocalIpadCommand qryIpadCmd = new ListLocalIpadCommand();
			qryIpadCmd.setUuid(resStr);
			if (aclinkIpadProvider.listLocalIpads(new CrossShardListingLocator(), qryIpadCmd).size() > 0){
				LOGGER.error("uuid is already existed, trying agian ... ");
				return this.generateUUID(cmd);
			}
		}
		return resStr;
	}

	// 查找某园区下的本地服务器
	@Override
	public ListAclinkServersResponse listLocalServers(AclinkListLocalServersCommand cmd) {
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		ListAclinkServersResponse resp = new ListAclinkServersResponse();
		cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
		List<AclinkServer> servers= aclinkServerProvider.queryLocalServers(locator, cmd);
		List<Long> serverIds = new ArrayList<Long>();
		if (servers != null && servers.size() > 0){
			List<AclinkServerDTO> dtos = servers.stream().map((r)-> {
				serverIds.add(r.getId());
				return ConvertHelper.convert(r, AclinkServerDTO.class);
			}).collect(Collectors.toList());
			
			resp.setAclinkServers(dtos);
		}
		List<DoorAccess> doors= doorAccessProvider.queryDoorAccesss(new CrossShardListingLocator(), 0, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_DOOR_ACCESS.LOCAL_SERVER_ID.in(serverIds));
				query.addConditions(Tables.EH_DOOR_ACCESS.STATUS.eq(DoorAccessStatus.ACTIVE.getCode()));
				return query;
			}
		});
		if (doors != null && doors.size() > 0) {
			doors.stream().forEach(door -> {
				AclinkServerDTO serverDTO = resp.getAclinkServers().stream()
						.filter(r -> r.getId().equals(door.getLocalServerId())).findFirst().get();
				if(serverDTO.getListDoorAccess() == null){
					serverDTO.setListDoorAccess(new ArrayList<LocalDoorAccessDTO>());
				}
				serverDTO.getListDoorAccess().add(ConvertHelper.convert(door, LocalDoorAccessDTO.class));
			});
		}
		resp.setNextPageAnchor(locator.getAnchor());
		return resp;
	}

	// 查找某园区下的本地服务器
	@Override
	public List<AclinkServerDTO> listLocalServers(CrossShardListingLocator locator, Long ownerId,
			DoorAccessOwnerType ownerType, String uuid, int count) {
		List<AclinkServer> servers = aclinkServerProvider.listLocalServers(locator, ownerId, ownerType, uuid, count);
		List<AclinkServerDTO> dtos = new ArrayList<AclinkServerDTO>();
		for (AclinkServer server : servers) {
			AclinkServerDTO dto = ConvertHelper.convert(server, AclinkServerDTO.class);
			List<DoorAccess> listDoorAccess = doorAccessProvider.listDoorAccessByServerId(server.getId(), 0);
			List<LocalDoorAccessDTO> listDoorAccessDto = new ArrayList<LocalDoorAccessDTO>();
			if(listDoorAccess != null && listDoorAccess.size()>0){
				for(DoorAccess da: listDoorAccess){
					listDoorAccessDto.add(ConvertHelper.convert(da, LocalDoorAccessDTO.class));
				}
				dto.setListDoorAccess(listDoorAccessDto);
			}
			
//			DoorAccessDTO doorDto = ConvertHelper.convert(doorAccessProvider.getDoorAccessById(server.getDoorAccessId()),DoorAccessDTO.class);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public void createLocalServer(CreateLocalServersCommand cmd) {
		AclinkServer server = new AclinkServer();
		User user = UserContext.current().getUser();
		server.setUuid(cmd.getUuid().toLowerCase());
		server.setName(cmd.getName());
		server.setOwnerId(cmd.getOwnerId());
		server.setOwnerType(cmd.getOwnerType());
		server.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		server.setCreatorUid(user.getId());
		server.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		server.setOperatorUid(user.getId());
		server.setAesServerKey(AclinkUtils.generateAESKey());
		try{
			aclinkServerProvider.createLocalServer(server);
		}catch(Exception e){
			String errorInfo = e.toString();
			LOGGER.info(errorInfo);
			if(errorInfo.contains("u_eh_aclink_servers_uuid") && errorInfo.contains("Duplicate")){
				throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_UUID_EXISTS, "匹配码已失效");
			}else{
				throw e;
			}
		}
	}

	@Override
	public void updateLocalServer(UpdateLocalServersCommand cmd) {
		AclinkServer server = aclinkServerProvider.findServerById(cmd.getId());
		server.setName(cmd.getName());
		server.setUuid(cmd.getUuid().toLowerCase());
		aclinkServerProvider.updateLocalServer(server);
	}

	@Override
	public void deleteLocalServer(DeleteLocalServerCommand cmd) {
		CrossShardListingLocator locator = new CrossShardListingLocator();
		ListLocalCamerasCommand qryCmd = new ListLocalCamerasCommand();
		qryCmd.setServerId(cmd.getId());
		List<AclinkCamera> cameras = aclinkCameraProvider.listLocalCameras(locator, qryCmd);
		if(cameras != null && cameras.size() > 0){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_RELATION_EXISTS, "服务器有关联设备，不可删除");
		}
		ListLocalIpadCommand qryIpadCmd = new ListLocalIpadCommand();
		qryIpadCmd.setServerId(cmd.getId());
		List<AclinkIpad> ipads = aclinkIpadProvider.listLocalIpads(locator, qryIpadCmd);
		if(ipads != null && ipads.size()>0){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_RELATION_EXISTS, "服务器有关联设备，不可删除");
		}
		AclinkServer server = aclinkServerProvider.findServerById(cmd.getId());
		server.setStatus((byte) 2);
		aclinkServerProvider.updateLocalServer(server);;
	}

	@Override
	public AclinkServerDTO findLocalServerById(Long id) {
		AclinkServer server = aclinkServerProvider.findServerById(id);
		return ConvertHelper.convert(server, AclinkServerDTO.class);
	}

	@Override
	public QueryServerRelationsResponse queryServerRelations(QueryServerRelationsCommand cmd) {
		//锚点不处理
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<AclinkServerRelDTO> listRels = new ArrayList<AclinkServerRelDTO>();
		QueryServerRelationsResponse rsp = new QueryServerRelationsResponse();
		AclinkServer server = aclinkServerProvider.findServerById(cmd.getServerId());
		if(server == null){
			return rsp;
		}
		
		DoorAccess door = new DoorAccess();
		if(cmd.getDoorAccessId() != null){
			door = doorAccessProvider.getDoorAccessById(cmd.getDoorAccessId());
			if(door != null && door.getLocalServerId() == server.getId()){
				if(door.getLocalServerId() != server.getId()){
					return rsp;
				}
				AclinkServerRelDTO doorRelDTO = new AclinkServerRelDTO();
				doorRelDTO.setId(door.getId());
				doorRelDTO.setActiveStatus(door.getStatus());
				doorRelDTO.setDeviceName(door.getName());
				doorRelDTO.setDeviceType((byte) 0);
				doorRelDTO.setDoorAcessId(door.getId());
				doorRelDTO.setLinkStatus(door.getLinkStatus());
				doorRelDTO.setServerId(cmd.getServerId());
				doorRelDTO.setServerIP(server.getIpAddress());
				doorRelDTO.setActiveDate(door.getCreateTime());
				// TODO set doorAccess version
				listRels.add(doorRelDTO);
			}
		}else{
			List<DoorAccess> listDoors = doorAccessProvider.listDoorAccessByServerId(cmd.getServerId(), 0);
			for(DoorAccess paramDoor : listDoors){
				AclinkServerRelDTO doorRelDTO = new AclinkServerRelDTO();
				doorRelDTO.setId(paramDoor.getId());
				doorRelDTO.setActiveStatus(paramDoor.getStatus());
				doorRelDTO.setDeviceName(paramDoor.getName());
				doorRelDTO.setDeviceType((byte) 0);
				doorRelDTO.setDoorAcessId(paramDoor.getId());
				doorRelDTO.setLinkStatus(paramDoor.getLinkStatus());
				doorRelDTO.setServerId(cmd.getServerId());
				doorRelDTO.setServerIP(server.getIpAddress());
				doorRelDTO.setActiveDate(paramDoor.getCreateTime());
				// TODO set doorAccess version
				listRels.add(doorRelDTO);
			}
		}
		
		List<AclinkIpad> listIpads = aclinkIpadProvider.listLocalIpads(locator, ConvertHelper.convert(cmd, ListLocalIpadCommand.class));
		ListLocalCamerasCommand qryCameraCmd = ConvertHelper.convert(cmd, ListLocalCamerasCommand.class);
		List<AclinkCamera> listCameras = aclinkCameraProvider.listLocalCameras(new CrossShardListingLocator(), qryCameraCmd);
		for(AclinkIpad ipad : listIpads){
			AclinkServerRelDTO ipadRelDTO = new AclinkServerRelDTO();
			ipadRelDTO.setId(ipad.getId());
			ipadRelDTO.setActiveStatus(ipad.getStatus());
			ipadRelDTO.setDeviceName(ipad.getName());
			ipadRelDTO.setDeviceType((byte) 1);
			ipadRelDTO.setDoorAcessId(ipad.getDoorAccessId());
			ipadRelDTO.setLinkStatus(ipad.getLinkStatus());
			ipadRelDTO.setServerId(cmd.getServerId());
			ipadRelDTO.setServerIP(server.getIpAddress());
			ipadRelDTO.setActiveDate(ipad.getActiveTime());
			ipadRelDTO.setEnterStatus(ipad.getEnterStatus());
			if(door == null){
				DoorAccess da = doorAccessProvider.getDoorAccessById(ipad.getDoorAccessId());
				ipadRelDTO.setDoorAccessName(da.getDisplayName());
			}else{
				ipadRelDTO.setDoorAccessName(door.getDisplayName());
			}
			
			listRels.add(ipadRelDTO);
		}
		for(AclinkCamera camera : listCameras){
			AclinkServerRelDTO cameraRelDTO = new AclinkServerRelDTO();
			cameraRelDTO.setId(camera.getId());
			cameraRelDTO.setActiveStatus(camera.getStatus());
			cameraRelDTO.setDeviceName(camera.getName());
			cameraRelDTO.setDeviceType((byte) 2);
			cameraRelDTO.setDoorAcessId(camera.getDoorAccessId());
			cameraRelDTO.setLinkStatus(camera.getLinkStatus());
			cameraRelDTO.setServerId(cmd.getServerId());
			cameraRelDTO.setServerIP(server.getIpAddress());
			cameraRelDTO.setEnterStatus(camera.getEnterStatus());
			if(door == null){
				DoorAccess da = doorAccessProvider.getDoorAccessById(camera.getDoorAccessId());
				cameraRelDTO.setDoorAccessName(da.getDisplayName());
			}else{
				cameraRelDTO.setDoorAccessName(door.getDisplayName());
			}
			listRels.add(cameraRelDTO);
		}
		rsp.setListAclinkServerRels(listRels);
		return rsp;
	}

	@Override
	public SyncLocalServerResponse syncLocalServer(Long id) {
		List<DoorAccess> listDoors = doorAccessProvider.listDoorAccessByServerId(id, 0);
		ListLocalIpadCommand qryIpadCmd = new ListLocalIpadCommand();
		qryIpadCmd.setServerId(id);
		List<AclinkIpad> listIpads = aclinkIpadProvider.listLocalIpads(new CrossShardListingLocator(0), qryIpadCmd);
		ListLocalCamerasCommand qryCmd = new ListLocalCamerasCommand();
		qryCmd.setServerId(id);
		List<AclinkCamera> listCameras = aclinkCameraProvider.listLocalCameras(new CrossShardListingLocator(), qryCmd);
		SyncLocalServerResponse rsp = new SyncLocalServerResponse();
		List<DoorAccessDTO> doorDTOs = new ArrayList<DoorAccessDTO>();
		List<AclinkIPadDTO> iPadDTOs = new ArrayList<AclinkIPadDTO>();
		List<AclinkCameraDTO> cameraDTOs = new ArrayList<AclinkCameraDTO>();
		if(listDoors != null && listDoors.size()>0){
			for(DoorAccess door : listDoors){
				doorDTOs.add(ConvertHelper.convert(door, DoorAccessDTO.class));
			}
		}
		if(listIpads != null && listIpads.size() > 0){
			for(AclinkIpad ipad : listIpads){
				iPadDTOs.add(ConvertHelper.convert(ipad, AclinkIPadDTO.class));
			}
		}
		if(listCameras != null && listCameras.size() > 0){
			for(AclinkCamera camera : listCameras){
				cameraDTOs.add(ConvertHelper.convert(camera, AclinkCameraDTO.class));
			}
		}
		AclinkServer server = aclinkServerProvider.findServerById(id);
		AclinkRemotePdu pdu = new AclinkRemotePdu();
		Base64 base64 = new Base64();
		
		try {
			String str = "{\"sync_server\":\"" + String.valueOf(id)+"\"}";
			byte[] textByte;
			textByte = str.getBytes("UTF-8");
			//编码
			String encodedText = base64.encodeToString(textByte);
	        pdu.setBody(encodedText);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
        pdu.setType(1);
        pdu.setUuid(server.getUuidNum());
        long requestId = LocalSequenceGenerator.getNextSequence();
        borderConnectionProvider.broadcastToAllBorders(requestId, pdu);
		rsp.setListDoorAccess(doorDTOs);
		rsp.setListIpad(iPadDTOs);
		rsp.setListCamera(cameraDTOs);
		return rsp;
	}

	@Override
	public PairLocalServerResponse pairLocalServer(String uuid, String ipAddress, String version) {
		PairLocalServerResponse rsp = new PairLocalServerResponse();
		StringBuffer macAddress = new StringBuffer(uuid);
		if (uuid.length() == 12){
			//mac地址补上冒号
			for(int i = 1; i <6; i++){
				macAddress.insert(3*i-1, ":");
			}
			uuid = macAddress.toString();
		}
		AclinkServer server = aclinkServerProvider.findLocalServersByUuid(uuid);
		if(server == null ){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_UUID, "匹配码错误");
		}
		if(ipAddress != null && !ipAddress.isEmpty()){
			//设置ip地址
			server.setIpAddress(ipAddress);
			aclinkServerProvider.updateLocalServer(server);
		}
		//上次同步时间比上次更新/新增时间要晚,TODO	是否需要,待定 
//		if(server.getSyncTime() != null && server.getSyncTime().getTime() > server.getOperateTime().getTime()){
//			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_LATEST_DATA, "已同步至最新数据");
//		}
		
		ListLocalIpadCommand qryIpadCmd = new ListLocalIpadCommand();
		qryIpadCmd.setServerId(server.getId());
		List<AclinkIpad> listIpad = aclinkIpadProvider.listLocalIpads(new CrossShardListingLocator(), qryIpadCmd);
		ListLocalCamerasCommand qryCmd = new ListLocalCamerasCommand();
		qryCmd.setServerId(server.getId());
		List<AclinkCamera> listCamera = aclinkCameraProvider.listLocalCameras(new CrossShardListingLocator(), qryCmd);
		List<DoorAccess> listDoorAccess = doorAccessProvider.listDoorAccessByServerId(server.getId(), 0);
		
		List<DoorAccessDTO> doorAccessDtos = new ArrayList<DoorAccessDTO>();
		List<AesServerKeyDTO> serverKeyDtos = new ArrayList<AesServerKeyDTO>();
		List<AclinkIPadDTO> ipadDtos = new ArrayList<AclinkIPadDTO>();
		List<AclinkCameraDTO> cameraDtos = new ArrayList<AclinkCameraDTO>();
		
		AclinkServerDTO serverDto = ConvertHelper.convert(server, AclinkServerDTO.class);
		//服务器所属机构名称
		if(server.getOwnerType() == DoorAccessOwnerType.COMMUNITY.getCode()) {
			Community c = communityProvider.findCommunityById(server.getOwnerId());
			if(c != null) {
				serverDto.setOwnerName(c.getName());
			}
		} else if(server.getOwnerType() == DoorAccessOwnerType.ENTERPRISE.getCode()) {
			Organization org = organizationProvider.findOrganizationById(server.getOwnerId());
			if(org != null && org.getNamespaceId().equals(server.getNamespaceId())) {
				serverDto.setOwnerName(org.getName());
			}
		}
		
		if(listDoorAccess != null && listDoorAccess.size()>0){
			for(DoorAccess da : listDoorAccess){
				DoorAccessDTO doorDto = ConvertHelper.convert(da, DoorAccessDTO.class);
				doorDto.setLocalUUid(da.getUuid());
				doorDto.setGroupId(da.getGroupid());
				//把公钥返回给内网服务器,安全性待优化
				doorDto.setLocalAesKey(da.getAesIv());
				doorAccessDtos.add(doorDto);
				AesServerKey serverKey = aesServerKeyProvider.queryAesServerKeyByDoorId(doorDto.getId(), 1L);//secretVer 默认1
				if(serverKey != null){
					serverKeyDtos.add(ConvertHelper.convert(serverKey, AesServerKeyDTO.class));
				}
			}
		}
		if(listIpad != null && listIpad.size()>0){
			for(AclinkIpad ipad : listIpad){
				ipadDtos.add(ConvertHelper.convert(ipad, AclinkIPadDTO.class));
			}
		}
		if(listCamera != null && listCamera.size()>0){
			for(AclinkCamera camera : listCamera){
				cameraDtos.add(ConvertHelper.convert(camera, AclinkCameraDTO.class));
			}
		}
		
		
		
		serverDto.setLocalServerKey(server.getAesServerKey());
		List<Border> borders = borderProvider.listAllBorders();
		//把云服务器borderServer的地址及端口找到,供内网服务器连接  by liuyilin 20180707
		if(borders != null && borders.size() > 0){
	        String borderUrl = borders.get(0).getPublicAddress();
	        if(borders.get(0).getPublicPort().equals(443)) {
	        	rsp.setBorderIp("wss://" + borderUrl + "/aclink");
	        } else {
	        	rsp.setBorderIp("ws://" + borderUrl + ":" + borders.get(0).getPublicPort() + "/aclink");
	        }
		}
		rsp.setAclinkServerDto(serverDto);
		rsp.setListCameraDtos(cameraDtos);
		rsp.setListDoorAccessDtos(doorAccessDtos);
		rsp.setListIpadDtos(ipadDtos);
		rsp.setListServerKeys(serverKeyDtos);
		return rsp;
	}

	@Override
	public AclinkServerDTO onServerConnecting(AclinkConnectingCommand cmd) {
		StringBuffer macAddress = new StringBuffer(cmd.getUuid());
		if (cmd.getUuid().length() == 12){
			//mac地址补上冒号
			for(int i = 1; i <6; i++){
				macAddress.insert(3*i-1, ":");
			}
			cmd.setUuid(macAddress.toString());
		}
		AclinkServer server = aclinkServerProvider.findLocalServersByUuid(cmd.getUuid());
		if(server == null ){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_UUID, "匹配码不匹配");
		}
		String base64 = cmd.getEncryptBase64().replace("_", "/").replace("-", "+");
        byte[] key = Base64.decodeBase64(server.getAesServerKey());
        byte[] data = Base64.decodeBase64(base64);
        String decodeResult = "";
        try {
            byte[] rb = AESUtil.decrypt(data, key);
            byte[] newArray = Arrays.copyOfRange(rb, 16, 32);
            decodeResult = new String(newArray);
        } catch (Exception e) {
            LOGGER.error("decrypt error", e);
        }
        
        if( (decodeResult == "") || (decodeResult.indexOf(server.getUuidNum()) != 0) ) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "Auth error");
        }
        server.setLinkStatus(DoorAccessLinkStatus.SUCCESS.getCode());
        aclinkServerProvider.updateLocalServer(server);
		return ConvertHelper.convert(server, AclinkServerDTO.class);
	}
	
	@Override
	public AclinkServerDTO onServerDisconnecting(AclinkDisconnectedCommand cmd) {
		StringBuffer macAddress = new StringBuffer(cmd.getUuid());
		if (cmd.getUuid().length() == 12){
			//mac地址补上冒号
			for(int i = 1; i <6; i++){
				macAddress.insert(3*i-1, ":");
			}
			cmd.setUuid(macAddress.toString());
		}
		AclinkServer server = aclinkServerProvider.findLocalServersByUuid(cmd.getUuid());
		if(server == null ){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_UUID, "匹配码不匹配");
		}
        server.setLinkStatus(DoorAccessLinkStatus.FAILED.getCode());
        aclinkServerProvider.updateLocalServer(server);
		return ConvertHelper.convert(server, AclinkServerDTO.class);
	}

	@Override
	public void updateServerSyncTime(Long serverId) {
		AclinkServer server = aclinkServerProvider.findServerById(serverId);
		if(server != null){
			server.setSyncTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			aclinkServerProvider.updateLocalServer(server);
		}else{
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_SERVER_NOT_FOUND, "服务器不存在");
		}
		
	}

	@Override
	public ListLocalServerByOrgResponse listLocalServerByOrg(ListLocalServerByOrgCommand cmd) {
		ListLocalServerByOrgResponse rsp = new ListLocalServerByOrgResponse();
		List<AclinkServer> listServer = aclinkServerProvider.listLocalServers(new CrossShardListingLocator(), cmd.getOwnerId(), DoorAccessOwnerType.fromCode(cmd.getOwnerType()), null, 0);
		List<AclinkServerDTO> listDto = new ArrayList<AclinkServerDTO>();
		if(listServer != null && listServer.size() > 0){
			for(AclinkServer server : listServer){
				listDto.add(ConvertHelper.convert(server, AclinkServerDTO.class));
			}
			rsp.setListServer(listDto);
		}
		return rsp;
	}
	
	@Override
	public void updateCameraIpadBatch(UpdateCameraIpadBatchCommand cmd) {
		DoorAccess da = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
		if(da == null){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "door is not found");
		}
		//TODO 非空判断,原门禁没有关联服务器,现在也没
		if(null !=da.getLocalServerId() && da.getLocalServerId() != cmd.getServerId()){
			if(cmd.getServerId() != null || cmd.getServerId() != 0L){
				AclinkServer server = aclinkServerProvider.findServerById(cmd.getServerId());
				if(server == null){
					throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_SERVER_NOT_FOUND, "server is not found");
				}
			}
			da.setLocalServerId(cmd.getServerId());
			doorAccessProvider.updateDoorAccess(da);
		}
		
		
		ListLocalCamerasCommand qryCameraCmd = new ListLocalCamerasCommand();
		qryCameraCmd.setDoorAccessId(cmd.getDoorId());
		List<AclinkCamera> qryCameras = aclinkCameraProvider.listLocalCameras(new CrossShardListingLocator(), qryCameraCmd);
		
		List<Long> reqOutCameraIds = cmd.getExternalCameraIds() == null ? new ArrayList<Long>() : cmd.getExternalCameraIds();
		List<Long> reqInCameraIds = cmd.getInternalCameraIds() == null ? new ArrayList<Long>() : cmd.getInternalCameraIds();

		List<Long> reqCameraIds = new ArrayList<Long>();
		reqCameraIds.addAll(reqOutCameraIds);
		reqCameraIds.addAll(reqInCameraIds);
		
		List<AclinkCamera> updateCameras = new ArrayList<AclinkCamera>();
		
		for(AclinkCamera qryCamera : qryCameras){
			if(reqCameraIds.indexOf(qryCamera.getId()) < 0){
				qryCamera.setDoorAccessId(0L);
				qryCamera.setServerId(0L);
				updateCameras.add(qryCamera);
			}
		}
		
		if(reqCameraIds.size() != 0){
			List<AclinkCamera> reqCameras = aclinkCameraProvider.listLocalCamerasByIds(reqCameraIds);
			for(AclinkCamera reqCamera : reqCameras){
				if(reqOutCameraIds.indexOf(reqCamera.getId()) >= 0){
					reqCamera.setEnterStatus(AclinkEnterStatus.OUT.getCode());
				}else if(reqInCameraIds.indexOf(reqCamera.getId()) >=0 ){
					reqCamera.setEnterStatus(AclinkEnterStatus.IN.getCode());
				}
				reqCamera.setDoorAccessId(cmd.getDoorId());
				reqCamera.setServerId(cmd.getServerId());
				updateCameras.add(reqCamera);
			}
		}
		
		aclinkCameraProvider.updateCameraBatch(updateCameras);
		
		ListLocalIpadCommand qryIpadCmd = new ListLocalIpadCommand();
		qryIpadCmd.setDoorAccessId(cmd.getDoorId());
		List<AclinkIpad> qryIpads = aclinkIpadProvider.listLocalIpads(new CrossShardListingLocator(), qryIpadCmd);
		
		List<Long> reqOutIpadIds = cmd.getExternalIpadIds() == null ? new ArrayList<Long>() : cmd.getExternalIpadIds();
		List<Long> reqInIpadIds = cmd.getInternalIpadIds() == null ? new ArrayList<Long>() : cmd.getInternalIpadIds();

		List<Long> reqIpadIds = new ArrayList<Long>();
		reqIpadIds.addAll(reqOutIpadIds);
		reqIpadIds.addAll(reqInIpadIds);
		
		List<AclinkIpad> updateIpads = new ArrayList<AclinkIpad>();
		
		for(AclinkIpad qryIpad : qryIpads){
			if(reqIpadIds.indexOf(qryIpad.getId()) < 0){
				qryIpad.setDoorAccessId(0L);
				qryIpad.setServerId(0L);
				updateIpads.add(qryIpad);
			}
		}
		
		if(reqIpadIds.size() != 0){
			List<AclinkIpad> reqIpads = aclinkIpadProvider.listLocalIpadByIds(reqIpadIds);
			for(AclinkIpad reqIpad : reqIpads){
				if(reqOutIpadIds.indexOf(reqIpad.getId()) >= 0){
					reqIpad.setEnterStatus(AclinkEnterStatus.OUT.getCode());
				}else if(reqInIpadIds.indexOf(reqIpad.getId()) >=0 ){
					reqIpad.setEnterStatus(AclinkEnterStatus.IN.getCode());
				}
				reqIpad.setDoorAccessId(cmd.getDoorId());
				reqIpad.setServerId(cmd.getServerId());
				updateIpads.add(reqIpad);
			}
		}
		
		aclinkIpadProvider.updateIpadBatch(updateIpads);
		
	}

	@Override
	public GetLocalServerAddressResponse getLocalServerAddressByIpad(String uuid) {
		GetLocalServerAddressResponse rsp = new GetLocalServerAddressResponse();
		List<AclinkIpad> ipads = aclinkIpadProvider.queryLocalIpads(new CrossShardListingLocator(),0,new ListingQueryBuilderCallback(){

			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {
				if(uuid != null){
					query.addConditions(Tables.EH_ACLINK_IPADS.UUID.eq(uuid));
				}
                return query;
			}
		});
		//异常处理可优化
		if(ipads == null || ipads.size() == 0 || ipads.get(0).getServerId() == null){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_UUID_EXISTS, "匹配码已失效");
		}
		if(!StringUtils.isEmpty(ipads.get(0).getLogoUrl())){
			rsp.setLogoUrl(ipads.get(0).getLogoUrl());
		}
		
		AclinkServer server = aclinkServerProvider.findServerById(ipads.get(0).getServerId());
		if(server == null || server.getIpAddress() == null){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_SERVER_NOT_FOUND, "未关联内网服务器");
		}
		rsp.setIpAddress(server.getIpAddress().concat(":8000"));//默认8000端口,可优化
		return rsp;
	}
}
