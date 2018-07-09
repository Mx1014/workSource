//@formatter:off
package com.everhomes.rest.asset;

/**
 *<ul>
 * <li>showEnergy: 1：展示，0：不展示</li>
 *</ul>
 */
public class JudgeShowEnergyResponse {
    private Byte showEnergy;

	public Byte getShowEnergy() {
		return showEnergy;
	}

	public void setShowEnergy(Byte showEnergy) {
		this.showEnergy = showEnergy;
	}
    
}
