package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 下午7:07:02
 */

/**
 * <ul>
 *     <li>dateStr:账期</li>
 *     <li>dateStrBegin:账单开始时间</li>
 *     <li>dateStrEnd:账单结束时间</li>
 *     <li>dateStrDue:出账单日</li>
 *     <li>dueDayDeadline:最晚还款日</li>
 * </ul>
 */
public class AssetBillDateDTO {

    private String dateStr;
    private String dateStrBegin;
    private String dateStrEnd;
    private String dateStrDue;
    private String dueDayDeadline;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getDateStrBegin() {
		return dateStrBegin;
	}

	public void setDateStrBegin(String dateStrBegin) {
		this.dateStrBegin = dateStrBegin;
	}

	public String getDateStrEnd() {
		return dateStrEnd;
	}

	public void setDateStrEnd(String dateStrEnd) {
		this.dateStrEnd = dateStrEnd;
	}

	public String getDateStrDue() {
		return dateStrDue;
	}

	public void setDateStrDue(String dateStrDue) {
		this.dateStrDue = dateStrDue;
	}

	public String getDueDayDeadline() {
		return dueDayDeadline;
	}

	public void setDueDayDeadline(String dueDayDeadline) {
		this.dueDayDeadline = dueDayDeadline;
	}

}
