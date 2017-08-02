package com.everhomes.rest.contract;

/**
 * <ul>
 *     <li>id: 合同id</li>
 *     <li>partyAId: 合同甲方id</li>
 *     <li>reviewResult: 审批结果  4 审批通过 5 审批不通过 参考{@link com.everhomes.rest.contract.ContractStatus}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public class ReviewContractCommand {

    private Long id;

    private Long partyAId;

    private Byte reviewResult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPartyAId() {
        return partyAId;
    }

    public void setPartyAId(Long partyAId) {
        this.partyAId = partyAId;
    }

    public Byte getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Byte reviewResult) {
        this.reviewResult = reviewResult;
    }
}
