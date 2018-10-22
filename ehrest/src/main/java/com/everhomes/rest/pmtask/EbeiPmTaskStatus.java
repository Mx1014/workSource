package com.everhomes.rest.pmtask;

/**
 * 一碑方的报修状态
 * <ul>
 * <li>1: 未处理</li>
 * <li>2: 处理中</li>
 * <li>3: 已完成</li>
 * <li>4: 已评价，销单</li>
 * <li>5: 未评价，人工销单</li>
 * <li>6: 已取消</li>
 * <li>7: 已删除</li>
 * <li>8: 待单中</li>
 * <li>9: 已回访</li>
 * </ul>
 */
public enum EbeiPmTaskStatus {
    UNPROCESSED((byte)1,"未处理"), PROCESSING((byte)2,"处理中"), PROCESSED((byte)3,"已完成"),
    CLOSED((byte)4,"已评价，销单"), REVISITED((byte)5,"未评价，人工销单"),INACTIVE((byte)6,"已取消"),
    CANCELED((byte)7,"已删除"),HANGING((byte)8,"待单中"),REVIEWED((byte)9,"已回访");

    private byte code;
    private String desc;
    private EbeiPmTaskStatus(byte code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return this.code;
    }
    public String getDesc(){return this.desc;}

    public static EbeiPmTaskStatus fromCode(Byte code) {
        if(code != null) {
            EbeiPmTaskStatus[] values = EbeiPmTaskStatus.values();
            for(EbeiPmTaskStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }


}
