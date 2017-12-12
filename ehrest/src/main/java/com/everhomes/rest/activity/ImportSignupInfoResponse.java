//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>total: total</li>
 *     <li>fail: fail</li>
 *     <li>success: success</li>
 *     <li>update: update</li>
 *     <li>fileLocation: fileLocation {@link com.everhomes.rest.contentserver.CsFileLocationDTO}</li>
 * </ul>
 */
public class ImportSignupInfoResponse {

	private Integer total;
	private Integer fail;
	private Integer success;
	private Integer update;
	@ItemType(CsFileLocationDTO.class)
	private CsFileLocationDTO fileLocation;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getFail() {
		return fail;
	}

	public void setFail(Integer fail) {
		this.fail = fail;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public Integer getUpdate() {
		return update;
	}

	public void setUpdate(Integer update) {
		this.update = update;
	}

	public CsFileLocationDTO getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(CsFileLocationDTO fileLocation) {
		this.fileLocation = fileLocation;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
