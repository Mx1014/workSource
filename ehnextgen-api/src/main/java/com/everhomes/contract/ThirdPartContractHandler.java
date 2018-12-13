package com.everhomes.contract;

import com.everhomes.rest.contract.*;

import java.util.List;

/**
 * Created by ying.xiong on 2017/11/22.
 */
public interface ThirdPartContractHandler {
    String CONTRACT_PREFIX = "thirdpartcontract-";
    void syncContractsFromThirdPart(String pageOffset, String version, String communityIdentifier, Long taskId, Long categoryId, Byte contractApplicationScene);
	void syncBillsFromThirdPart(String pageOffset, String date, String communityIdentifier, Long taskId,
			Long categoryId, Byte contractApplicationScene);
}
