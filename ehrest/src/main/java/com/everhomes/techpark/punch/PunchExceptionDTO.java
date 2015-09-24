package com.everhomes.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>requestTime：申请时间</li>
 * <li>description：申请内容{@link com.everhomes.techpark.punch.PunchStatus}</li>
 * <li>exceptionProcessStatus：异常处理状态：驳回，待审核，有效{@link com.everhomes.techpark.punch.ExceptionProcessStatus}</li>
 * <li>processCode：处理结果:正常，迟到等 参考{@link com.everhomes.techpark.punch.ApprovalStatus}</li>
 * <li>processDetails: 无效的时候有detail</li>
 * </ul>
 */
public class PunchExceptionDTO{
	private Long requestTime;
	private String   description;
	private Byte     exceptionProcessStatus;
	private Byte     processCode;
	private String   processDetails;
 
 
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 }
