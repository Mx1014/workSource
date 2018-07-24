package com.everhomes.rest.asset;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>billId: 账单id</li>
 *     <li>certificateNote：上传凭证时附带的留言</li>
 *     <li>certificateUris：上传凭证图片的uri（一次最多允许上传6张）</li>
 * </ul>
 */
public class UploadCertificateCommand {
	
	private Long billId;
	private String certificateNote;
	@ItemType(String.class)
	private List<String> certificateUris;
	
	public UploadCertificateCommand() {
		
	}
	
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public String getCertificateNote() {
		return certificateNote;
	}
	public void setCertificateNote(String certificateNote) {
		this.certificateNote = certificateNote;
	}
	public List<String> getCertificateUris() {
		return certificateUris;
	}
	public void setCertificateUris(List<String> certificateUris) {
		this.certificateUris = certificateUris;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
