package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

 /**
 * <ul>通过打卡异常申报
 * <li>id：申报id</li>
 * <li>processCode：处理结果:正常，迟到等 参考{@link com.everhomes.rest.techpark.punch.ApprovalStatus}</li>
 * </ul>
 */
public class ApprovePunchExceptionRequestCommand {

    private Long id;
    private Byte processCode;
   
 

     public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Byte getProcessCode() {
		return processCode;
	}



	public void setProcessCode(Byte processCode) {
		this.processCode = processCode;
	}



	@Override
     public String toString() {
         return StringHelper.toJsonString(this);
     }

 }
