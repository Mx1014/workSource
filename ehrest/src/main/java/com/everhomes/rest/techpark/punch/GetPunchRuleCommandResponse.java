package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

 /**
 * <ul>  
 * <li>punchRuleDTO：公司打卡规则dto</li>
 * </ul>
 */
public class GetPunchRuleCommandResponse{
 

    private PunchRuleDTO punchRuleDTO; 
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public PunchRuleDTO getPunchRuleDTO() {
		return punchRuleDTO;
	}

	public void setPunchRuleDTO(PunchRuleDTO punchRuleDTO) {
		this.punchRuleDTO = punchRuleDTO;
	}


 
 
 }
