// @formatter:off
package com.everhomes.aclink;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.AclinkCameraDTO;
import com.everhomes.rest.aclink.AclinkServerDTO;
import com.everhomes.rest.aclink.CreateLocalCamerasCommand;
import com.everhomes.rest.aclink.DoorAccessDTO;
import com.everhomes.rest.aclink.DoorAccessLinkStatus;
import com.everhomes.rest.aclink.DoorAccessOwnerType;
import com.everhomes.rest.aclink.DoorAccessType;
import com.everhomes.rest.aclink.ListAclinkServersResponse;
import com.everhomes.rest.aclink.ListLocalCamerasCommand;
import com.everhomes.rest.aclink.ListLocalCamerasResponse;
import com.everhomes.rest.aclink.QueryAclinkCamerasCommand;
import com.everhomes.rest.aclink.UpdateLocalCamerasCommand;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AclinkCameraServiceImpl implements AclinkCameraService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AclinkCameraServiceImpl.class);
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private AclinkCameraProvider aclinkCameraProvider;
	
	@Autowired
	private DoorAccessProvider doorAccessProvider;
	
	@Autowired
	private AclinkServerProvider aclinkServerProvider;


	@Override
	public void createLocalCamera(CreateLocalCamerasCommand cmd) {
		AclinkCamera camera = new AclinkCamera();
		User user = UserContext.current().getUser();
		camera.setDoorAccessId(cmd.getDoorAccessId());
		camera.setEnterStatus(cmd.getEnterStatus());
		camera.setIpAddress(cmd.getIpAddress());
		camera.setName(cmd.getName());
		camera.setServerId(cmd.getServerId());
		camera.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		camera.setCreatorUid(user.getId());
		camera.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		camera.setOperatorUid(user.getId());
		camera.setLinkStatus((byte) 2);//TODO delete
		camera.setKeyCode(cmd.getKeyCode());
		camera.setAccount(cmd.getAccount());
		camera.setOwnerId(cmd.getOwnerId());
		camera.setOwnerType(cmd.getOwnerType());
		aclinkCameraProvider.createLocalCamera(camera);
		//内网服务器下属设备有变动,更新服务器的上次操作时间
		AclinkServer server = aclinkServerProvider.findServerById(camera.getServerId());
		if(server != null){
			server.setOperateTime(camera.getOperateTime());
			server.setOperatorUid(camera.getOperatorUid());
			aclinkServerProvider.updateLocalServer(server);
		}
	}

	@Override
	public void updateLocalCamera(UpdateLocalCamerasCommand cmd) {
		AclinkCamera camera = aclinkCameraProvider.findCameraById(cmd.getId());
		User user = UserContext.current().getUser();
		camera.setName(cmd.getName());
		camera.setDoorAccessId(cmd.getDoorAccessId());
		camera.setEnterStatus(cmd.getEnterStatus());
		camera.setIpAddress(cmd.getIpAddress());
		camera.setServerId(cmd.getServerId());
		camera.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		camera.setOperatorUid(user.getId());
		camera.setKeyCode(cmd.getKeyCode());
		camera.setAccount(cmd.getAccount());
		aclinkCameraProvider.updateLocalCamera(camera);
		//内网服务器下属设备有变动,更新服务器的上次操作时间
		AclinkServer server = aclinkServerProvider.findServerById(camera.getServerId());
		if(server != null){
			server.setOperateTime(camera.getOperateTime());
			server.setOperatorUid(camera.getOperatorUid());
			aclinkServerProvider.updateLocalServer(server);
		}
	}

	@Override
	public void deleteLocalCameras(Long id) {
		User user = UserContext.current().getUser();
		AclinkCamera camera = aclinkCameraProvider.findCameraById(id);
		camera.setStatus((byte) 2);
		camera.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		camera.setOperatorUid(user.getId());
		aclinkCameraProvider.updateLocalCamera(camera);
		//内网服务器下属设备有变动,更新服务器的上次操作时间
		AclinkServer server = aclinkServerProvider.findServerById(camera.getServerId());
		if(server != null){
			server.setOperateTime(camera.getOperateTime());
			server.setOperatorUid(camera.getOperatorUid());
			aclinkServerProvider.updateLocalServer(server);
		}
	}

	@Override
	public ListLocalCamerasResponse listLocalCameras(ListLocalCamerasCommand cmd) {
		ListLocalCamerasResponse resp = new ListLocalCamerasResponse();
		cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<AclinkCamera> cameras = aclinkCameraProvider.listLocalCameras(locator, cmd);
		List<AclinkCameraDTO> dtos = new ArrayList<AclinkCameraDTO>();
		for(AclinkCamera camera : cameras){
			AclinkCameraDTO dto = ConvertHelper.convert(camera, AclinkCameraDTO.class);
			dto.setDoorAccess(ConvertHelper.convert(doorAccessProvider.getDoorAccessById(camera.getDoorAccessId()),DoorAccessDTO.class));
			dto.setServer(ConvertHelper.convert(aclinkServerProvider.findServerById(camera.getServerId()),AclinkServerDTO.class));
			dtos.add(dto);
		}
		resp.setAclinkCameras(dtos);
		resp.setNextPageAnchor(locator.getAnchor());
		return resp;
	}
}
