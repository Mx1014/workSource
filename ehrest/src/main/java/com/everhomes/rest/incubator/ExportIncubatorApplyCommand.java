package com.everhomes.rest.incubator;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>applyUserId: 申请人的uid</li>
 *     <li>approveStatus: 审批状态，0-待审批，1-拒绝，2-通过 参考 {@link ApproveStatus}</li>
 *     <li>needReject: 是否需要查询拒绝的数据 0-否，1-是，默认为1查询，此参数是为了适应页面需求（审核中以及通过的）</li>
 *     <li>keyWord: keyWord</li>
 *     <li>orderBy: 排序方式，0-创建时间，1-审核时间</li>
 *     <li>applyType: 申请类型 0-入孵，1-加速，2-入园，不传则不限{@link ApplyType}</li>
 * </ul>
 */
public class ExportIncubatorApplyCommand {


	private Integer namespaceId;
	private Long applyUserId;
	private Byte approveStatus;
	private Byte needReject;
	private String keyWord;
	private Byte orderBy;
	private Byte applyType;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}

	public Byte getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Byte approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Byte getNeedReject() {
		return needReject;
	}

	public void setNeedReject(Byte needReject) {
		this.needReject = needReject;
	}

	public Byte getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Byte orderBy) {
		this.orderBy = orderBy;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Byte getApplyType() {
		return applyType;
	}

	public void setApplyType(Byte applyType) {
		this.applyType = applyType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
