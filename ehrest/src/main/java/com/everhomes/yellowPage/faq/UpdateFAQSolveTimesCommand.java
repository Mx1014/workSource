package com.everhomes.yellowPage.faq;

import com.everhomes.rest.yellowPage.AllianceCommonCommand;

/**
 * <ul>
 * <li>namespaceId: namespaceId</li>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>type: 服务联盟类型</li>
 * <li>FAQId: 问题id</li>
 * <li>solveStaus: 0-未解决 1-解决</li>
 * </ul>
 **/
public class UpdateFAQSolveTimesCommand extends AllianceCommonCommand{
	
	private Long FAQId;
	private Byte solveStaus;
	
	public Long getFAQId() {
		return FAQId;
	}
	public void setFAQId(Long fAQId) {
		FAQId = fAQId;
	}
	public Byte getSolveStaus() {
		return solveStaus;
	}
	public void setSolveStaus(Byte solveStaus) {
		this.solveStaus = solveStaus;
	}
}
