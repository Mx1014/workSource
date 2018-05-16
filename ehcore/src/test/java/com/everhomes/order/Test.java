package com.everhomes.order;

import java.math.BigDecimal;

/**
*@author created by yangcx
*@date 2018年5月16日---上午10:06:27
**/
public class Test {
	
	public Long changePayAmount(BigDecimal amount){

        if(amount == null){
            return 0L;
        }
        return  amount.multiply(new BigDecimal(100)).longValue();
    }
	
	public static void main(String[] args) {
		Test test = new Test();
		System.out.println(test.changePayAmount(new BigDecimal("0.1")));
	}

}
