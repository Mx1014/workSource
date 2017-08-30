package com.everhomes.rest.contract;

/**
 * <ul>
 *     <li>id: 合同id</li>
 *     <li>partyAId: 合同甲方id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public class FindContractCommand {

    private Long id;

    private Long partyAId;

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
}
