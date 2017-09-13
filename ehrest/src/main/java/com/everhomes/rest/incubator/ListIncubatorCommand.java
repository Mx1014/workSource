package com.everhomes.rest.incubator;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>applyUserId: 申请人的uid</li>
 *     <li>approveStatus: 审批状态，0-待审批，1-拒绝，2-通过</li>
 *     <li>needReject: 是否需要查询拒绝的数据 0-否，1-是，默认为1查询</li>
 *     <li>keyWord: keyWord</li>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>pageSize: 数量</li>
 *     <li>orderBy: 排序方式，0-创建时间，1-审核时间</li>
 * </ul>
 */
public class ListIncubatorCommand {


	private Integer namespaceId;
	private Long applyUserId;
	private Byte approveStatus;
	private Byte needReject;
	private String keyWord;
	private Long nextPageAnchor;
	private Integer pageSize;
	private Byte orderBy;

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

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
