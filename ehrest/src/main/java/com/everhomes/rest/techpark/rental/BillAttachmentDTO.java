package com.everhomes.rest.techpark.rental;

import com.everhomes.util.StringHelper;
/**
 * <ul>
*<li>id： id</li>
 * <li>billId：订单id</li>
 * <li>attachmentType: 类型，参考{@link com.everhomes.rest.techpark.rental.admin.AttachmentType}</li>
 * <li>content：内容</li> 
 * </ul>
 */
public class BillAttachmentDTO {
	private Long id;
	private Long billId;
	private Byte attachmentType ;
	private String content;
	
	
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


 
  


	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}






  






	public Byte getAttachmentType() {
		return attachmentType;
	}







	public void setAttachmentType(Byte attachmentType) {
		this.attachmentType = attachmentType;
	}







	public String getContent() {
		return content;
	}







	public void setContent(String content) {
		this.content = content;
	}






	public Long getBillId() {
		return billId;
	}






	public void setBillId(Long billId) {
		this.billId = billId;
	}




 
}
