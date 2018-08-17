//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billId:账单id</li>
 * <li>dateStr:账期</li>
 * <li>dateStrBegin:账单开始时间，参与排序</li>
 * <li>dateStrEnd:账单结束时间，参与排序</li>
 * <li>contractNum:合同编号</li>
 * <li>buildingName: 楼栋名称</li>
 * <li>apartmentName: 门牌名称</li>
 * <li>noticeTel:催缴电话</li>
 * <li>customerTel:客户手机号</li>
 * <li>targetName:客户名称</li>
 * <li>targetId:客户id</li>
 * <li>targetType:客户type</li>
 * <li>billGroupDTO:账单组，包括减免项和收费项目的集合，参考{@link com.everhomes.rest.asset.BillGroupDTO}</li>
 * <li>certificateNote:客户上传缴费凭证后的留言</li>
 * <li>billStatus:账单缴费状态（0：未缴，1：已缴）</li>
 * <li>uploadCertificateDTOList:上传的所有缴费凭证的图片地址</li>
 * <li>assetPaymentBillAttachmentList: 附件数据，参考{@link com.everhomes.rest.asset.AssetPaymentBillAttachment}</li>
 *</ul>
 */
public class ListBillDetailResponse {
    private Long billId;
    private Long billGroupId;
    private String dateStr;
    private String dateStrBegin;
    private String dateStrEnd;
    private String buildingName;
    private String apartmentName;
    private String noticeTel;
    private String customerTel;
    private String targetName;
    private String targetType;
    private Long targetId;
    @ItemType(BillGroupDTO.class)
    private BillGroupDTO billGroupDTO;
    private String contractNum;
    private String invoiceNum;
    //add by tangcen
    private String certificateNote;
    private Byte billStatus;
	@ItemType(UploadCertificateDTO.class)
	private List<UploadCertificateDTO> uploadCertificateDTOList;
	//新增附件
    private List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList;
	
    public Byte getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(Byte billStatus) {
		this.billStatus = billStatus;
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

	public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getDateStr() {
        return dateStr;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }


    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getNoticeTel() {
        return noticeTel;
    }

    public void setNoticeTel(String noticeTel) {
        this.noticeTel = noticeTel;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public BillGroupDTO getBillGroupDTO() {
        return billGroupDTO;
    }

    public void setBillGroupDTO(BillGroupDTO billGroupDTO) {
        this.billGroupDTO = billGroupDTO;
    }

    public ListBillDetailResponse() {

    }

	public String getDateStrBegin() {
		return dateStrBegin;
	}

	public void setDateStrBegin(String dateStrBegin) {
		this.dateStrBegin = dateStrBegin;
	}

	public String getDateStrEnd() {
		return dateStrEnd;
	}

	public void setDateStrEnd(String dateStrEnd) {
		this.dateStrEnd = dateStrEnd;
	}

	public String getCustomerTel() {
		return customerTel;
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	public List<AssetPaymentBillAttachment> getAssetPaymentBillAttachmentList() {
		return assetPaymentBillAttachmentList;
	}

	public void setAssetPaymentBillAttachmentList(List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList) {
		this.assetPaymentBillAttachmentList = assetPaymentBillAttachmentList;
	}
}
