package com.everhomes.rest.activity;
/**
 * <ul>
 * 	<li>PEOPLE_COUNT_DESC: 1, 报名人数降序</li>
 *  <li>PEOPLE_COUNT_ASC: 2， 报名人数升序</li>
 *  <li>PUBLISH_TIME_DESC: 3， 发布时间降序</li>
 *  <li>PUBLISH_TIME_ASC: 4， 发布时间升序</li>
 *  <li>ACTIVITY_COUNT_DESC: 5， 发布活动数降序</li>
 *  <li>ACTIVITY_COUNT_ASC: 6， 发布活动数升序</li>
 * </ul>
 */
public enum StatisticsOrderByFlag {
    PEOPLE_COUNT_DESC((byte) 1), PEOPLE_COUNT_ASC((byte) 2), PUBLISH_TIME_DESC((byte) 3), PUBLISH_TIME_ASC((byte) 4), ACTIVITY_COUNT_DESC((byte) 5), ACTIVITY_COUNT_ASC((byte) 6);
    private Byte code;

    StatisticsOrderByFlag(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static StatisticsOrderByFlag fromCode(Byte code) {
    	if(null == code){
    		return null;
    	}
        for (StatisticsOrderByFlag flag : StatisticsOrderByFlag.values()) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
