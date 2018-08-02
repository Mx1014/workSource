// @formatter:off
package com.everhomes.rest.aclink;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>listIpadDtos:ipad列表{@link com.everhomes.rest.aclink.AclinkIPadDTO}</li>
 * <li>listCameraDtos:摄像头列表{@link com.everhomes.rest.aclink.AclinkCameraDTO}</li>
 * <li>listServerKeys:门禁相关aes公钥{@link com.everhomes.rest.aclink.AesServerKeyDTO}</li>
 * <li>listDoorAccessDtos:门禁列表{@link com.everhomes.rest.aclink.listDoorAccessDtos}</li>
 * <li>aclinkServerDto:服务器{@link com.everhomes.rest.aclink.AclinkServerDTO}</li>
 * <li>serverKey:服务器aes公钥(暂时不用){@link com.everhomes.rest.aclink.AesServerKeyDTO}</li>
 * <li>borderIp:borderServer地址,例:ws://10.1.140.27:9090/aclink 或: wss://border-1.zuolin.com/aclink</li>
 * </ul>
 *
 */
public class PairLocalServerResponse {
	private List<AclinkIPadDTO> listIpadDtos;
	private List<AclinkCameraDTO> listCameraDtos;
	private List<AesServerKeyDTO> listServerKeys;
	private List<DoorAccessDTO> listDoorAccessDtos;
	private AclinkServerDTO aclinkServerDto;
	private AesServerKeyDTO serverKey;
	private String borderIp;
	
	public String getBorderIp() {
		return borderIp;
	}
	public void setBorderIp(String borderIp) {
		this.borderIp = borderIp;
	}
	public List<AclinkIPadDTO> getListIpadDtos() {
		return listIpadDtos;
	}
	public void setListIpadDtos(List<AclinkIPadDTO> listIpadDtos) {
		this.listIpadDtos = listIpadDtos;
	}
	public List<AclinkCameraDTO> getListCameraDtos() {
		return listCameraDtos;
	}
	public void setListCameraDtos(List<AclinkCameraDTO> listCameraDtos) {
		this.listCameraDtos = listCameraDtos;
	}
	public List<DoorAccessDTO> getListDoorAccessDtos() {
		return listDoorAccessDtos;
	}
	public void setListDoorAccessDtos(List<DoorAccessDTO> listDoorAccessDtos) {
		this.listDoorAccessDtos = listDoorAccessDtos;
	}
	public AclinkServerDTO getAclinkServerDto() {
		return aclinkServerDto;
	}
	public void setAclinkServerDto(AclinkServerDTO aclinkServerDto) {
		this.aclinkServerDto = aclinkServerDto;
	}
	
	public List<AesServerKeyDTO> getListServerKeys() {
		return listServerKeys;
	}
	public void setListServerKeys(List<AesServerKeyDTO> listServerKeys) {
		this.listServerKeys = listServerKeys;
	}
	public AesServerKeyDTO getServerKey() {
		return serverKey;
	}
	public void setServerKey(AesServerKeyDTO serverKey) {
		this.serverKey = serverKey;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
