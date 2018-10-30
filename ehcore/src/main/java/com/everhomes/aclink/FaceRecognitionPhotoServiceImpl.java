// @formatter:off
package com.everhomes.aclink;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Component;

import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.aclink.AclinkServerDTO;
import com.everhomes.rest.aclink.AclinkServiceErrorCode;
import com.everhomes.rest.aclink.DataUtil;
import com.everhomes.rest.aclink.DoorAccessOwnerType;
import com.everhomes.rest.aclink.DoorAuthDTO;
import com.everhomes.rest.aclink.DoorAuthStatus;
import com.everhomes.rest.aclink.FaceRecognitionPhotoDTO;
import com.everhomes.rest.aclink.ListFacialRecognitionPhotoByUserResponse;
import com.everhomes.rest.aclink.NotifySyncVistorsCommand;
import com.everhomes.rest.aclink.SetFacialRecognitionPhotoCommand;
import com.everhomes.rest.aclink.SyncLocalPhotoByUserIdCommand;
import com.everhomes.rest.aclink.SyncLocalUserDataCommand;
import com.everhomes.rest.aclink.SyncLocalUserDataResponse;
import com.everhomes.rest.aclink.SyncLocalVistorDataCommand;
import com.everhomes.rest.aclink.SyncLocalVistorDataResponse;
import com.everhomes.rest.aclink.UpdateUserSyncTimeCommand;
import com.everhomes.rest.aclink.UpdateVistorSyncTimeCommand;
import com.everhomes.rest.rpc.server.AclinkRemotePdu;
import com.everhomes.sequence.LocalSequenceGenerator;
import com.everhomes.user.User;
import com.everhomes.user.UserAdminProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

import ch.qos.logback.core.pattern.ConverterUtil;

@Component
public class FaceRecognitionPhotoServiceImpl implements FaceRecognitionPhotoService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FaceRecognitionPhotoServiceImpl.class);
	@Autowired
	FaceRecognitionPhotoProvider faceRecognitionPhotoProvider;
	
	@Autowired
	AclinkServerProvider aclinkServerProvider;
	
	@Autowired
	BorderConnectionProvider borderConnectionProvider;
	
	@Autowired
	DoorAuthProvider doorAuthProvider;
	
	@Autowired
	CommunityProvider communityProvider;
	
	@Autowired
	OrganizationProvider organizationProvider;
	
	@Autowired
	UserAdminProvider userAdminProvider;
	
	@Autowired
	UserProvider userProvider;
	
	@Autowired
	AesUserKeyProvider aesUserKeyProvider;
	
	@Autowired
	DoorAccessService doorAccessService;
	
	@Autowired
	DoorAccessProvider doorAccessProvider;
	
	@Override
	public void setFacialRecognitionPhoto(SetFacialRecognitionPhotoCommand cmd) {
		User user = UserContext.current().getUser();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		if(cmd.getUserType() != null && cmd.getUserType() != 0 && cmd.getAuthId() != null){
			//访客照片,只新增,不更新
			FaceRecognitionPhoto rec = new FaceRecognitionPhoto();
			rec.setImgUri(cmd.getImgUri());
			rec.setImgUrl(cmd.getImgUrl());
			rec.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			rec.setCreatorUid(user.getId());
			rec.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			rec.setOperatorUid(user.getId());
			rec.setAuthId(cmd.getAuthId());
			rec.setUserType(cmd.getUserType());
			rec.setStatus((byte) 1);
			faceRecognitionPhotoProvider.creatFacialRecognitionPhoto(rec);
			return;
		}
		List<FaceRecognitionPhoto> recs = faceRecognitionPhotoProvider.listFacialRecognitionPhotoByUser(locator, user.getId(), 0);
		if(recs != null && recs.size() > 0){
			FaceRecognitionPhoto rec = recs.get(0);
			rec.setImgUri(cmd.getImgUri());
			rec.setImgUrl(cmd.getImgUrl());
			rec.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			rec.setOperatorUid(user.getId());
			faceRecognitionPhotoProvider.updateFacialRecognitionPhoto(rec);
		}else{
			FaceRecognitionPhoto rec = new FaceRecognitionPhoto();
			rec.setImgUri(cmd.getImgUri());
			rec.setImgUrl(cmd.getImgUrl());
			rec.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			rec.setCreatorUid(user.getId());
			rec.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			rec.setOperatorUid(user.getId());
			rec.setUserId(user.getId());
			rec.setStatus((byte) 1);
			faceRecognitionPhotoProvider.creatFacialRecognitionPhoto(rec);
		}
		//同步照片
		SyncLocalPhotoByUserIdCommand syncCmd = new SyncLocalPhotoByUserIdCommand();
		syncCmd.setOwnerId(cmd.getOwnerId());
		syncCmd.setOwnerType(cmd.getOwnerType());
		syncCmd.setUserId(user.getId());
		this.syncLocalPhotoByUserId(syncCmd);
	}

	@Override
	public ListFacialRecognitionPhotoByUserResponse listFacialRecognitionPhotoByUser() {
		User user = UserContext.current().getUser();
		ListFacialRecognitionPhotoByUserResponse rsp = new ListFacialRecognitionPhotoByUserResponse();
		List<FaceRecognitionPhoto> faceRecs = faceRecognitionPhotoProvider.listFacialRecognitionPhotoByUser(new CrossShardListingLocator(), user.getId(), 0);
		List<FaceRecognitionPhotoDTO> listDtos = new ArrayList<FaceRecognitionPhotoDTO>();
		for(FaceRecognitionPhoto rec : faceRecs){
			listDtos.add(ConvertHelper.convert(rec, FaceRecognitionPhotoDTO.class));
		}
		rsp.setListPhoto(listDtos);
		return rsp;
	}

	//根据用户id同步信息,发送通知
	@Override
	public void syncLocalPhotoByUserId(SyncLocalPhotoByUserIdCommand cmd) {
		List<FaceRecognitionPhoto> listPhotos = faceRecognitionPhotoProvider.listFacialRecognitionPhotoByUser(new CrossShardListingLocator(), cmd.getUserId(), 0);

		if(listPhotos == null || listPhotos.size() == 0){
			//门禁授权时也会调用，未上传照片抛异常的话影响其他非人脸识别用户授权
//			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_NOT_FOUND,
//                    "用户照片未上传");
			return;
		}
		List<AclinkServer> listAclinkServer = new ArrayList<AclinkServer>();
		if(cmd.getUserId() != null){
			//暂不区分域空间
			listAclinkServer = aclinkServerProvider.listLocalServersByUserAuth(new CrossShardListingLocator(), cmd.getUserId(), null, 0);
		}else{
			listAclinkServer = aclinkServerProvider.listLocalServers(new CrossShardListingLocator(),
					cmd.getOwnerId(), DoorAccessOwnerType.fromCode(cmd.getOwnerType()), null, 0);
		}
		//消息拼装
		AclinkRemotePdu pdu = new AclinkRemotePdu();
        if(listAclinkServer != null && listAclinkServer.size() > 0){
        	for(AclinkServer server: listAclinkServer){
        		Base64 base64 = new Base64();
        		try {
        			String str = "{\"sync_single_user\":\"" + String.valueOf(cmd.getUserId())+"\"}";
        			byte[] textByte;
        			textByte = str.getBytes("UTF-8");
        			String encodedText = base64.encodeToString(textByte);
        	        pdu.setBody(encodedText);
        		} catch (UnsupportedEncodingException e) {
        			e.printStackTrace();
        		}
        		pdu.setType(1);
        		pdu.setUuid(server.getUuidNum());
            	
            
            	long requestId = LocalSequenceGenerator.getNextSequence();
            	borderConnectionProvider.broadcastToAllBorders(requestId, pdu);
        	}
        }
	}
	
	//同步访客信息,增量,发送通知
	@Override
	public void notifySyncVistorsCommand(NotifySyncVistorsCommand cmd) {
		DoorAccess door = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
		if(door == null || door.getLocalServerId() == null || door.getLocalServerId() == 0){
			//太多场景会用到,找不到就打个日志算了,不抛异常  by liuyilin 20180824
//			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "FR-door not found");
			LOGGER.info("FR-door not found");
			return ;
		}
		AclinkServer server = aclinkServerProvider.findServerById(door.getLocalServerId());
		//消息拼装
		AclinkRemotePdu pdu = new AclinkRemotePdu();
        if(server != null ){
    		Base64 base64 = new Base64();
    		try {
    			String str = "{\"sync_vistor_increment\":\"" + String.valueOf(cmd.getDoorId())+"\"}";
    			byte[] textByte;
    			textByte = str.getBytes("UTF-8");
    			String encodedText = base64.encodeToString(textByte);
    	        pdu.setBody(encodedText);
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}
    		pdu.setType(1);
    		pdu.setUuid(server.getUuidNum());
        	long requestId = LocalSequenceGenerator.getNextSequence();
        	borderConnectionProvider.broadcastToAllBorders(requestId, pdu);
        }else{
        	throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_SERVER_NOT_FOUND, "local server not found");
        }
	}

	@Override
	public SyncLocalUserDataResponse syncLocalUserData(SyncLocalUserDataCommand cmd) {
		SyncLocalUserDataResponse rsp = new SyncLocalUserDataResponse();
		AclinkServer server = aclinkServerProvider.findServerById(cmd.getServerId());
		if(server == null ){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_SERVER_NOT_FOUND, "Server not found");
		}
		
		List<DoorAuth> listDoorAuth = doorAuthProvider.queryValidDoorAuths(new CrossShardListingLocator(), cmd.getUserId(), server.getOwnerId(), server.getOwnerType(), 0);
		List<DoorAuthDTO> listauthDtos = new ArrayList<DoorAuthDTO>();
		User user = userProvider.findUserById(cmd.getUserId());
		if(listDoorAuth != null && listDoorAuth.size() > 0){
			for(DoorAuth auth : listDoorAuth){
				DoorAuthDTO authDto = ConvertHelper.convert(auth, DoorAuthDTO.class);
				AesUserKey aesUserKey = doorAccessService.getAesUserKey(user, auth);
		        if(aesUserKey == null) {
		        	//throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "用户无权限");
		        	continue;
		        }
		        
		        byte[] secret = Base64.decodeBase64(aesUserKey.getSecret());
		        byte[] bPayload = CmdUtil.openDoorCmd(secret);
		        
		        byte[] bSeq = DataUtil.intToByteArray(0);
		        byte[] bLen = DataUtil.intToByteArray(bPayload.length + 6);
		        byte[] mBuf = new byte[bPayload.length + 10];
		        
		        System.arraycopy(bLen, 0, mBuf, 0, bLen.length);
		        System.arraycopy(bSeq, 0, mBuf, 6, bSeq.length);
		        System.arraycopy(bPayload, 0, mBuf, 10, bPayload.length);
				authDto.setLocalAuthKey(Base64.encodeBase64String(mBuf));
				
				listauthDtos.add(authDto);
				if(rsp.getPhone() == null && auth.getAuthType() == 0){
					rsp.setPhone(auth.getPhone());
				}
			}
		}

		if(listauthDtos.size() == 0){
			//没有有效权限
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "用户无权限");
		}
		List<Long> userIds = new ArrayList<Long>();
		userIds.add(cmd.getUserId());
		List<UserIdentifier> listUser = userAdminProvider.listUserIdentifiers(userIds);
		if(listUser != null && listUser.size()>0){
			rsp.setPhone(listUser.get(0).getIdentifierToken());
		}else{
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_NOT_FOUND, "user not found");
		}
		
		
		if(user != null){
			rsp.setUserName(user.getNickName());
		}else{
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_NOT_FOUND, "user not found");
		}
		
		rsp.setListDoorAuth(listauthDtos);
		rsp.setNamesapceId(server.getNamespaceId());
		rsp.setOwnerId(server.getOwnerId());
		List<FaceRecognitionPhoto> listPhoto = faceRecognitionPhotoProvider.listFacialRecognitionPhotoByUser(new CrossShardListingLocator(), cmd.getUserId(), 0);
		if(listPhoto != null && listPhoto.size()>0){
			rsp.setPhotoId(listPhoto.get(0).getId());
			rsp.setPhotoUrl(listPhoto.get(0).getImgUrl());
		}else{
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_NOT_FOUND, "Photo not found");
		}
		if(server.getOwnerType() == DoorAccessOwnerType.COMMUNITY.getCode()) {
			Community c = communityProvider.findCommunityById(server.getOwnerId());
			if(c != null && c.getNamespaceId().equals(server.getNamespaceId())) {
				rsp.setOwnerName(c.getName());
			}
		} else if(server.getOwnerType() == DoorAccessOwnerType.ENTERPRISE.getCode()) {
			Organization org = organizationProvider.findOrganizationById(server.getOwnerId());
			if(org != null && org.getNamespaceId().equals(server.getNamespaceId())) {
				rsp.setOwnerName(org.getName());
			}
		}
		rsp.setUserId(cmd.getUserId());
		return rsp;
	}
	
	@Override
	public SyncLocalVistorDataResponse syncLocalVistorData(SyncLocalVistorDataCommand cmd) {
		return faceRecognitionPhotoProvider.queryVistorPhotoBySync(cmd.getServerId());
	}

	@Override
	public void updateUserSyncTime(UpdateUserSyncTimeCommand cmd) {
		FaceRecognitionPhoto photo = faceRecognitionPhotoProvider.findById(cmd.getPhotoId());
		if(photo != null){
			photo.setSyncTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			faceRecognitionPhotoProvider.updateFacialRecognitionPhoto(photo);
		}else{
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_NOT_FOUND, "Photo not found");
		}
	}

	@Override
	public void updateVistorSyncTimes(UpdateVistorSyncTimeCommand cmd) {
		String[] ids = cmd.getPhotoIds().split(",");
		List<Long> photoIds = new ArrayList<Long>();
		for (String id : ids){
			photoIds.add(Long.valueOf(id));
		}
		List<FaceRecognitionPhoto> listPhotos = faceRecognitionPhotoProvider.findFaceRecognitionPhotoByIds(photoIds);
		Timestamp nowTime = new Timestamp(DateHelper.currentGMTTime().getTime());
		if(listPhotos != null && listPhotos.size() > 0){
			for(FaceRecognitionPhoto photo : listPhotos){
				photo.setSyncTime(nowTime);
			}
			faceRecognitionPhotoProvider.updateFacialRecognitionPhotos(listPhotos);
		}
		
	}

	@Override
	public void invalidVistorSyncState(UpdateVistorSyncTimeCommand cmd) {
		String[] ids = cmd.getPhotoIds().split(",");
		List<Long> photoIds = new ArrayList<Long>();
		for (String id : ids){
			photoIds.add(Long.valueOf(id));
		}
		List<FaceRecognitionPhoto> listPhotos = faceRecognitionPhotoProvider.findFaceRecognitionPhotoByIds(photoIds);
		if(listPhotos != null && listPhotos.size() > 0){
			for(FaceRecognitionPhoto photo : listPhotos){
				photo.setStatus((byte) 0);
			}
			faceRecognitionPhotoProvider.updateFacialRecognitionPhotos(listPhotos);
		}
	}
}
