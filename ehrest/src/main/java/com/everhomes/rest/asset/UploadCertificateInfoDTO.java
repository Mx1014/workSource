//@formatter:off
package com.everhomes.rest.asset;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billId:账单id</li>
 * <li>certificateNote:上传凭证的留言</li>
 * <li>uploadCertificateDTOList:该账单下包含的凭证图片信息(包含图片uri和url)</li>
 *</ul>
 */
public class UploadCertificateInfoDTO {
	
	private Long billId;
	private String certificateNote;
	@ItemType(UploadCertificateDTO.class)
	private List<UploadCertificateDTO> uploadCertificateDTOList;
	
	public UploadCertificateInfoDTO() {

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

	public List<UploadCertificateDTO> getUploadCertificateDTOList() {
		return uploadCertificateDTOList;
	}

	public void setUploadCertificateDTOList(List<UploadCertificateDTO> uploadCertificateDTOList) {
		this.uploadCertificateDTOList = uploadCertificateDTOList;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
