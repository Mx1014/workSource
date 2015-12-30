package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/***
 * 
 * id: 主键id
 *
 */
public class DeleteReservationConfCommand {
	
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
