package com.everhomes.rest.repeat;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ExpressionDTO {

	@ItemType(RepeatExpressionDTO.class)
	private List<RepeatExpressionDTO> expression;
	
	public List<RepeatExpressionDTO> getExpression() {
		return expression;
	}

	public void setExpression(List<RepeatExpressionDTO> expression) {
		this.expression = expression;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
