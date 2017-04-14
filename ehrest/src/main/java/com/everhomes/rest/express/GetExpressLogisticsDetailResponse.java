// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>logisticsStatus: 物流状态，参考{@link com.everhomes.rest.express.ExpressLogisticsStatus}</li>
 * <li>expressCompany: 快递公司名称</li>
 * <li>billNo: 快递单号</li>
 * <li>expressLogo: 快递公司logo</li>
 * <li>consumeTime: 耗时，格式3.2表示3天2小时</li>
 * <li>traces: 跟踪信息，参考{@link com.everhomes.rest.express.ExpressTraceDTO}</li>
 * </ul>
 */
public class GetExpressLogisticsDetailResponse {

	private Byte logisticsStatus;

	private String expressCompany;

	private String billNo;

	private String expressLogo;
	
	private String consumeTime;

	@ItemType(ExpressTraceDTO.class)
	private List<ExpressTraceDTO> traces;

	public GetExpressLogisticsDetailResponse() {

	}

	public GetExpressLogisticsDetailResponse(Byte logisticsStatus, String expressCompany, String billNo, String expressLogo, String consumeTime, List<ExpressTraceDTO> traces) {
		super();
		this.logisticsStatus = logisticsStatus;
		this.expressCompany = expressCompany;
		this.billNo = billNo;
		this.expressLogo = expressLogo;
		this.consumeTime = consumeTime;
		this.traces = traces;
	}

	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}

	public Byte getLogisticsStatus() {
		return logisticsStatus;
	}

	public void setLogisticsStatus(Byte logisticsStatus) {
		this.logisticsStatus = logisticsStatus;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getExpressLogo() {
		return expressLogo;
	}

	public void setExpressLogo(String expressLogo) {
		this.expressLogo = expressLogo;
	}

	public List<ExpressTraceDTO> getTraces() {
		return traces;
	}

	public void setTraces(List<ExpressTraceDTO> traces) {
		this.traces = traces;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
