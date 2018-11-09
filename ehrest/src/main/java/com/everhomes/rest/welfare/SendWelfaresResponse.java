package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>参数:
 * <li>checkStatus: 为null或者为0则没问题,为1则说明接受者在职校验失败 为2说明余额不足发放失败 为3说明其它未定义的原因发放失败</li>
 * <li>dismissSenderUid:发放者userId-如果有值说明发放者已离职</li>
 * <li>dismissSenderDetailId: 发放者detailId</li>
 * <li>dismissReceivers: 接收人列表 -在列表里的就是校验失败已离职的人 参考{@link WelfareReceiverDTO}</li>
 * </ul>
 */
public class SendWelfaresResponse {
	private Byte checkStatus;
    private Long dismissSenderUid;
    private Long dismissSenderDetailId;
    private List<WelfareReceiverDTO> dismissReceivers;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Byte getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Byte checkStatus) {
		this.checkStatus = checkStatus;
	}

	public List<WelfareReceiverDTO> getDismissReceivers() {
		return dismissReceivers;
	}

	public void setDismissReceivers(List<WelfareReceiverDTO> dismissReceivers) {
		this.dismissReceivers = dismissReceivers;
	}

	public Long getDismissSenderDetailId() {
		return dismissSenderDetailId;
	}

	public void setDismissSenderDetailId(Long dismissSenderDetailId) {
		this.dismissSenderDetailId = dismissSenderDetailId;
	}

	public Long getDismissSenderUid() {
		return dismissSenderUid;
	}

	public void setDismissSenderUid(Long dismissSenderUid) {
		this.dismissSenderUid = dismissSenderUid;
	}
}
